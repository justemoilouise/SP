package com.training.decisionTree;

import java.io.IOException;
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
			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
			List<BlobKey> blobKeys = blobs.get("imageset");

			if (blobKeys == null || blobKeys.isEmpty()) {
				response = false;
			} else {
				ArrayList<BlobKey> keys = new ArrayList<BlobKey>();

				if(session.getAttribute("dt_key") != null) {
					keys = (ArrayList<BlobKey>) session.getAttribute("dt_key");
				}

				keys.add(blobKeys.get(0));
				session.setAttribute("dt_key", keys);
				response = true;
			}
		} else if(method.equalsIgnoreCase("readimageset")) {
//			ArrayList<BlobKey> blobKeys = (ArrayList<BlobKey>) session.getAttribute("dt_key");
//			ArrayList<Species> dataset = readFilesFromBlob(blobKeys);
//
//			if(!dataset.isEmpty()) {
//				session.setAttribute("imageset", dataset);
//				response = true;
//			} else {
//				response = false;
//			}
			response = true;
		} else if(method.equalsIgnoreCase("processimageset")) {
			ArrayList<Species> dataset = (ArrayList<Species>) session.getAttribute("imageset");
			dataset = processor.processImageSet(dataset);
			session.setAttribute("dataset", dataset);
			response = true;
		} else if(method.equalsIgnoreCase("crossvalidate")) {
			ArrayList<Species> dataset = (ArrayList<Species>) session.getAttribute("imageset");
			response = processor.crossValidate(dataset);
		}

		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}
