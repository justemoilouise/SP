package com.training.core;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TrainingAppServlet extends HttpServlet {

	public TrainingAppServlet() {}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		resp.getWriter().println("Hello world!");
	}
}
