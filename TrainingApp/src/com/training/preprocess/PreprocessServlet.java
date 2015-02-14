package com.training.preprocess;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.training.helpers.ServletHelper;

import Data.Species;

@SuppressWarnings("serial")
public class PreprocessServlet extends HttpServlet {
	private PreprocessProcessor processor;
	
	public PreprocessServlet() {
		this.processor = new PreprocessProcessor();
	}
	
	@SuppressWarnings("unchecked")
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		
		if(method.equalsIgnoreCase("getmodel")) {
			response = processor.getPreprocessModel();
		}
		else {
			String requestBody = ServletHelper.GetRequestBody(req.getReader());
			ArrayList<Species> dataset = ServletHelper.ConvertToObject(requestBody, ArrayList.class);
			
			if(method.equalsIgnoreCase("scale")) {
				response = processor.scale(dataset);
			} else if (method.equalsIgnoreCase("reducefeatures")) {
				response = processor.reduceFeatures(dataset);
			}
		}		
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}
