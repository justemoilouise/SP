package com.training.core;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	
	public TrainingAppServlet() {
		this.processor = new TrainingAppProcessor();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		
		if(method.equalsIgnoreCase("getmodellist")) {
			response = processor.getModelList();
		} else if(method.equalsIgnoreCase("getapplist")) {
			processor.getAppList();
		} else if(method.equalsIgnoreCase("saveclassifiermodel")) {
			response = processor.saveClassifierModel(null, null, null);
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}
