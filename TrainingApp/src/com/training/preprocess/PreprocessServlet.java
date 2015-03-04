package com.training.preprocess;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.training.helpers.ServletHelper;

import Data.Species;

@SuppressWarnings("serial")
public class PreprocessServlet extends HttpServlet {
	private PreprocessProcessor processor;
	private HttpSession session;
	
	public PreprocessServlet() {
		this.processor = new PreprocessProcessor();
	}
	
	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		
		if(method.equalsIgnoreCase("getmodel")) {
			response = processor.getPreprocessModel();
			session.setAttribute("model_preprocess", response);
		} else if(method.equalsIgnoreCase("setpca")) {
			int PC = Integer.parseInt(req.getParameter("pca"));
			response = processor.setPC(PC);
		}
		else {
			ArrayList<Species> dataset = (ArrayList<Species>) session.getAttribute("dataset");
			
			if(method.equalsIgnoreCase("scale")) {
				dataset = processor.scale(dataset);
				session.setAttribute("dataset", dataset);
				response = true;
			} else if (method.equalsIgnoreCase("reducefeatures")) {
				dataset = processor.reduceFeatures(dataset);
				session.setAttribute("dataset", dataset);
				response = true;
			}
		}		
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}
