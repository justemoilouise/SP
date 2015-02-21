package com.training.user;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			response = processor.LogIn(req.getParameter("username"), req.getParameter("password"));
			
			if((Boolean) response) {
				session.setAttribute("admin", "admin");
				session.setAttribute("alert_type", "success");
			} else {
				session.setAttribute("alert_type", "warning");
			}
		} else if(method.equalsIgnoreCase("logout")) {
			response = processor.LogOut();
			
			if((Boolean) response) {
				session.removeAttribute("admin");
				session.setAttribute("alert_type", "success");
			} else {
				session.setAttribute("alert_type", "warning");
			}
		}

		resp.getWriter().println(response.toString());
	}
}

