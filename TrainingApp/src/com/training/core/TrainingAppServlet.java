package com.training.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
			
			response = processor.readDataset(stream);
		} else if(method.equalsIgnoreCase("saveclassifiermodel")) {
			response = processor.saveClassifierModel(null, null, null);
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
}
