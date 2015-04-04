package com.training.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Data.ClassifierModel;
import Data.Species;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
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
		
		if(method.equalsIgnoreCase("getmodellist")) {
			ArrayList<ClassifierModelList> models = new ArrayList<ClassifierModelList>();
			models.add(new ClassifierModelList());
			
			if(memcacheService.contains(cacheModelKey))
				models = (ArrayList<ClassifierModelList>)memcacheService.get(cacheModelKey);
			
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
		} else if(method.equalsIgnoreCase("uploadclassifiermodel")) {
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
	        List<BlobKey> blobKeys = blobs.get("model");

	        if (blobKeys == null || blobKeys.isEmpty()) {
	            response = false;
	        } else {
	        	BlobKey key = blobKeys.get(0);
	        	saveToCache(key);
	        	response = true;
	        }
		} else if(method.equalsIgnoreCase("download")) {
			String modelKey = req.getParameter("modelKey");
			BlobKey modelBlobKey = new BlobKey(modelKey);
			blobstoreService.serve(modelBlobKey, resp);
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
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
			memcacheService.put(cacheModelKey, models);
		}
	}
}
