package com.training.user;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.training.data.LogIn;
import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	private UserProcessor processor;
	private HttpSession session;
	
	public UserServlet() {
		this.processor = new UserProcessor();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		
		this.session = req.getSession();
		
		if(method.equalsIgnoreCase("login")) {
			if(processor.getCredentials() == null) {
				ServletConfig config = getServletConfig();
				Hashtable<String, String> credentials = new Hashtable<String, String>();
				credentials.put("username", config.getInitParameter("username"));
				credentials.put("password", config.getInitParameter("password"));
				
				processor.setCredentials(credentials);
			}
			
			String requestBody = ServletHelper.GetRequestBody(req.getReader());
			LogIn login = ServletHelper.ConvertToObject(requestBody, LogIn.class);
			response = processor.LogIn(login);
			
			if((Boolean) response) {
				session.setAttribute("admin", "admin");
			}
		} else if(method.equalsIgnoreCase("logout")) {
			response = processor.LogOut();
			
			if((Boolean) response) {
				session.removeAttribute("admin");
			}
		}

		resp.getWriter().println(response.toString());
	}
}

