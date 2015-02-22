package com.training.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	private BlobstoreService blobstoreService;

	public TrainingAppServlet() {
		this.processor = new TrainingAppProcessor();
		this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		
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
	            response = true;
	        }
		} else if(method.equalsIgnoreCase("readdataset")) {
			String requestBody = ServletHelper.GetRequestBody(req.getReader());
			File f = ServletHelper.ConvertToObject(requestBody, File.class);
			
			response = processor.readDataset(f);
		} else if(method.equalsIgnoreCase("saveclassifiermodel")) {
			response = processor.saveClassifierModel(null, null, null);
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}
