package com.training.decisionTree;

import ij.ImagePlus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Data.Species;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class DecisionTreeServlet extends HttpServlet {
	private DecisionTreeProcessor processor;
	private HttpSession session;
	private BlobstoreService blobstoreService;
	
	public DecisionTreeServlet() {
		this.processor = new DecisionTreeProcessor();
		this.blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	}
	
	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		
		if(method.equalsIgnoreCase("upload")) {
			response = true;
//			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
//			List<BlobKey> blobKeys = blobs.get("imageset");
//
//			if (blobKeys == null || blobKeys.isEmpty()) {
//				response = false;
//			} else {
//				ArrayList<BlobKey> keys = new ArrayList<BlobKey>();
//				
//				if(session.getAttribute("dt_key") != null) {
//					keys = (ArrayList<BlobKey>) session.getAttribute("dt_key");
//				}
//				session.setAttribute("dt_key", keys.add(blobKeys.get(0)));
//				response = true;
//			}
		} else if(method.equalsIgnoreCase("readimageset")) {
			ArrayList<Species> dataset = readFilesFromBlob((ArrayList<BlobKey>) session.getAttribute("dt_key"));

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
	
	private ArrayList<Species> readFilesFromBlob(ArrayList<BlobKey> list) {
		ArrayList<Species> dataset = new ArrayList<Species>();
		
		for(BlobKey key : list) {
			try {
				InputStream stream = readFileFromBlob(key);
				ObjectInputStream oStream = new ObjectInputStream(stream);

				ImagePlus img = (ImagePlus) oStream.readObject();
				String name = img.getTitle().trim();
				
				Species s = new Species();
				s.setImg(img);
				s.setName(name);
				
				dataset.add(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return dataset;
	}
}
