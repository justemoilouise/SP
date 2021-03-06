package com.training.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.training.helpers.ServletHelper;

@SuppressWarnings({ "serial" })
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	private BlobstoreService blobstoreService;
	private GcsService gcsService;
	private HttpSession session;
//	final String gcsBucket = "radiss-training.appspot.com";
	final String gcsBucket = "radiss-classifier-models";
	final String modelKeysFilename = "model-keys.dat";

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
			ArrayList<ClassifierModel> models = getClassifierModels();		
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
			writeFileToBlob(model);
			response = model.getVersion();
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

	private void writeFileToBlob(ClassifierModel model) {
		try {
			String fileName = "classifier-model-" + model.getVersion() + ".dat";
			GcsFilename gcsFilename = new GcsFilename(gcsBucket, fileName);
			GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
			ObjectOutputStream oStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
			oStream.writeObject(model);
			oStream.flush();
			outputChannel.close();
		} catch(Exception ex) {}
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

	@SuppressWarnings("unused")
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
			GcsFilename gcsFilename = new GcsFilename(gcsBucket, modelKeysFilename);
			GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
			if(readChannel != null) {
				ObjectInputStream iStream = new ObjectInputStream(Channels.newInputStream(readChannel));
				modelKeys = (ArrayList<BlobKey>) iStream.readObject();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modelKeys;
	}
	
	@SuppressWarnings("unused")
	private void saveModelToGCS(BlobKey key) {
		ArrayList<BlobKey> modelKeys = readModelKeysFromGCS();
		modelKeys.add(key);

		try {
			GcsFilename gcsFilename = new GcsFilename(gcsBucket, modelKeysFilename);
			GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
			ObjectOutputStream oStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
			oStream.writeObject(modelKeys);
			oStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ArrayList<ClassifierModel> getClassifierModels() {
		ArrayList<ClassifierModel> models = new ArrayList<ClassifierModel>();
		
		try {
			ListResult result = gcsService.list(gcsBucket, ListOptions.DEFAULT);
			while (result.hasNext()){
				ListItem l = result.next();
				String name = l.getName();

				if(!name.contains("keys")) {
					GcsFilename gcsFilename = new GcsFilename(gcsBucket, name);
					GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
					ObjectInputStream iStream = new ObjectInputStream(Channels.newInputStream(readChannel));
					ClassifierModel model = (ClassifierModel) iStream.readObject();
					models.add(model);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return models;
	}
}
