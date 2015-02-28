package com.training.svm;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class SVMServlet extends HttpServlet {
	private SVMProcessor processor;
	private HttpSession session;
	
	public SVMServlet() {
		this.processor = new SVMProcessor();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		this.session = req.getSession();
		
		if(method.equalsIgnoreCase("getmodel")) {
			response = processor.getSVMModel();
			session.setAttribute("model_svm", response);
		} else if(method.equalsIgnoreCase("buildmodel")) {
			processor.buildModel(true);
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}
