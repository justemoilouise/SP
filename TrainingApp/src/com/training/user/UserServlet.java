package com.training.user;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	private UserProcessor processor;
	
	public UserServlet() {
		this.processor = new UserProcessor();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		
		if(method.equalsIgnoreCase("login")) {
			response = processor.LogIn(req.getParameter("username"), req.getParameter("password"));
		} else if(method.equalsIgnoreCase("logout")) {
			response = processor.LogOut();
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
}

