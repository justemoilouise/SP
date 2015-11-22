package com.training.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Data.ClassifierModel;
import Data.Species;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.training.data.ClassifierModelList;
import com.training.helpers.ServletHelper;

@SuppressWarnings({ "serial", "deprecation" })
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	private BlobstoreService blobstoreService;
	private GcsService gcsService;
	private HttpSession session;
	final GcsFilename gcsFilename = new GcsFilename("radiss-training.appspot.com", "model-keys.dat");
	final String cacheModelKey = "model_keys";
	final String cacheAppKey = "app_keys";

	public TrainingAppServlet() {
		this.processor = new TrainingAppProcessor();
		this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		this.gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	}

	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		resp.setContentType("application/json");

		if(method.equalsIgnoreCase("getmodellist")) {
			ArrayList<ClassifierModelList> models = getClassifierModels();		
			response = models;
		} else if(method.equalsIgnoreCase("getapplist")) {
			processor.getAppList();
		}  else if(method.equalsIgnoreCase("upload")) {
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
			List<BlobKey> blobKeys = blobs.get("file");

			if (blobKeys == null || blobKeys.isEmpty()) {
				response = false;
			} else {
				session.setAttribute("key", blobKeys.get(0));
				response = true;
			}
		} else if(method.equalsIgnoreCase("readdataset")) {
			InputStream stream = readFileFromBlob((BlobKey) session.getAttribute("key"));
			ArrayList<Species> dataset = processor.readDataset(stream);

			if(!dataset.isEmpty()) {
				session.setAttribute("dataset", dataset);
				response = true;
			} else {
				response = false;
			}
		} else if(method.equalsIgnoreCase("saveclassifiermodel")) {
			String requestBody = ServletHelper.GetRequestBody(req.getReader());
			ClassifierModel model = ServletHelper.ConvertToObject(requestBody, ClassifierModel.class);
			model = processor.buildClassifierModel(model);
			BlobKey key = writeFileToBlob(model);
			saveModelToGCS(key);
			response = true;
		} else if(method.equalsIgnoreCase("download")) {
			String modelKey = req.getParameter("modelKey");
			BlobKey modelBlobKey = new BlobKey(modelKey);			
			BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(modelBlobKey);
			resp.setHeader("Content-type", "application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + blobInfo.getFilename() +"\"");

			blobstoreService.serve(modelBlobKey, resp);
			return;
		}

		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}

	private BlobKey writeFileToBlob(ClassifierModel model) {
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutputStream oStream = new ObjectOutputStream(bStream);
			oStream.writeObject(model);

			FileService fileService = FileServiceFactory.getFileService();
			AppEngineFile file = fileService.createNewBlobFile("application/octet-stream", "classifier-model-" + model.getVersion() + ".dat");
			FileWriteChannel writeChannel = fileService.openWriteChannel(file, true);			
			writeChannel.write(ByteBuffer.wrap(bStream.toByteArray()));
			writeChannel.closeFinally();

			return fileService.getBlobKey(file);
		} catch(Exception ex) {}

		return new BlobKey("");
	}

	private byte[] readFromBlob(BlobKey key) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		long start = 0, end = 1024;
		boolean flag = false;

		do {
			try {
				byte[] b = blobstoreService.fetchData(key, start, end);
				out.write(b);

				if (b.length < 1024)
					flag = true;

				start = end + 1;
				end += 1025;

			} catch (Exception e) {
				flag = true;
			}

		} while (!flag);

		return out.toByteArray();
	}

	private InputStream readFileFromBlob(BlobKey key) {
		byte[] filebytes = readFromBlob(key);

		if(filebytes.length > 0)
			return new ByteArrayInputStream(filebytes);

		return null;
	}

	private ClassifierModel readModelFromBlob(BlobKey key) {
		byte[] objBytes = readFromBlob(key);
        
        if(objBytes.length > 0) {
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(objBytes);
	            ObjectInputStream is = new ObjectInputStream(in);
	            ClassifierModel model = (ClassifierModel) is.readObject();
	            
	            return model;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        return null;
	}
        
	@SuppressWarnings("unchecked")
	private ArrayList<BlobKey> readModelKeysFromGCS() {
		ArrayList<BlobKey> modelKeys = new ArrayList<BlobKey>();

		try {
			GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
			ObjectInputStream iStream = new ObjectInputStream(Channels.newInputStream(readChannel));
			modelKeys = (ArrayList<BlobKey>) iStream.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modelKeys;
	}
	
	private void saveModelToGCS(BlobKey key) {
		ArrayList<BlobKey> modelKeys = readModelKeysFromGCS();
		modelKeys.add(key);

		try {
			GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
			ObjectOutputStream oStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
			oStream.writeObject(modelKeys);
			oStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList<ClassifierModelList> getClassifierModels() {
		ArrayList<ClassifierModelList> models = new ArrayList<ClassifierModelList>();
		
		ArrayList<BlobKey> keys = readModelKeysFromGCS();
		for(BlobKey key : keys) {
			ClassifierModel model = readModelFromBlob(key);
			ClassifierModelList modelList = new ClassifierModelList(key, model);
			models.add(modelList);
		}
		return models;
	}
}
