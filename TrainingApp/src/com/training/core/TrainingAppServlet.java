package com.training.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Data.ClassifierModel;
import Data.PreprocessModel;
import Data.SVMModel;
import Data.Species;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	private BlobstoreService blobstoreService;
	private HttpSession session;

	public TrainingAppServlet() {
		this.processor = new TrainingAppProcessor();
		this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		
		if(method.equalsIgnoreCase("getmodellist")) {
			response = processor.getModelList();
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
			}
		} else if(method.equalsIgnoreCase("saveclassifiermodel")) {
			PreprocessModel pModel = (PreprocessModel) session.getAttribute("model_preprocess");
			SVMModel sModel = (SVMModel) session.getAttribute("model_svm");
			String notes = ServletHelper.GetRequestBody(req.getReader());
			
			ClassifierModel model = processor.saveClassifierModel(pModel, sModel, notes);
			response = uploadModel(model);
		} else if(method.equalsIgnoreCase("uploadclassifiermodel")) {
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
	        List<BlobKey> blobKeys = blobs.get("model");

	        if (blobKeys == null || blobKeys.isEmpty()) {
	            response = false;
	        } else {
	        	response = true;
	        }
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
	
	private InputStream readFileFromBlob(BlobKey key) {
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

        byte[] filebytes = out.toByteArray();
        
        if(filebytes.length > 0)
        	return new ByteArrayInputStream(filebytes);
        
        return null;
	}
	
	private boolean uploadModel(ClassifierModel model) {

		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
	        ObjectOutputStream o = new ObjectOutputStream(b);
	        o.writeObject(model);
	        byte[] payload = b.toByteArray();
	        
			URL url = new URL(blobstoreService.createUploadUrl("/trainingapp/uploadclassifiermodel"));
			HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);
            request.setPayload(payload);
            
            URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
            urlFetch.fetch(request);
            
            return true;
		}
		catch (Exception ex) {}
		
		return false;
	}
}
