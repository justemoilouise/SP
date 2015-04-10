package com.training.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.training.data.ClassifierModelList;
import com.training.helpers.ObjectComparator;
import com.training.helpers.ServletHelper;

@SuppressWarnings({ "serial", "deprecation" })
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	private BlobstoreService blobstoreService;
	private MemcacheService memcacheService;
	private HttpSession session;
	final String cacheModelKey = "model_keys";
	final String cacheAppKey = "app_keys";

	public TrainingAppServlet() {
		this.processor = new TrainingAppProcessor();
		this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		this.memcacheService = MemcacheServiceFactory.getMemcacheService();
	}
	
	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		resp.setContentType("application/json");
		
		if(method.equalsIgnoreCase("getmodellist")) {
			ArrayList<ClassifierModelList> models = new ArrayList<ClassifierModelList>();
			
			if(memcacheService.contains(cacheModelKey)) {
				models = (ArrayList<ClassifierModelList>)memcacheService.get(cacheModelKey);
				Collections.sort(models, new ObjectComparator());
			}
			
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
			saveToCache(key);
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
	private void saveToCache(BlobKey key) {
		ArrayList<ClassifierModelList> models = new ArrayList<ClassifierModelList>();
		ClassifierModel model = readModelFromBlob(key);
		
		if(model != null) {
			ClassifierModelList modelList = new ClassifierModelList(key, model);
			
			if(memcacheService.contains(cacheModelKey)) {
				models = (ArrayList<ClassifierModelList>) memcacheService.get(cacheModelKey);
			}

			models.add(modelList);
			memcacheService.put(cacheModelKey, models, null);
		}
	}
}
