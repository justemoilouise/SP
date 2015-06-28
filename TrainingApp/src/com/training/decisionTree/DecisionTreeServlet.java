package com.training.decisionTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.training.helpers.ServletHelper;

public class DecisionTreeServlet {
	private DecisionTreeProcessor processor;
	private BlobstoreService blobstoreService;
	private HttpSession session;
	
	public DecisionTreeServlet() {
		this.processor = new DecisionTreeProcessor();
		this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		
		if(method.equalsIgnoreCase("upload")) {
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
			List<BlobKey> blobKeys = blobs.get("imageset");

			if (blobKeys == null || blobKeys.isEmpty()) {
				response = false;
			} else {
				session.setAttribute("dt_key", blobKeys.get(0));
				response = true;
			}
		} else if(method.equalsIgnoreCase("readimageset")) {
			InputStream stream = readFileFromBlob((BlobKey) session.getAttribute("dt_key"));
			ArrayList<Object> dataset = processor.readImageSet(stream);

			if(!dataset.isEmpty()) {
				session.setAttribute("imageset", dataset);
				response = true;
			} else {
				response = false;
			}
		} else if(method.equalsIgnoreCase("crossvalidate")) {
			response = processor.crossValidate();
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
}
