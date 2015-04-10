package com.training.user;

import java.util.Hashtable;

public class UserProcessor {
	private Hashtable<String, String> credentials;
	
	public UserProcessor() {}
	
	public Hashtable<String, String> getCredentials() {
		return credentials;
	}
	
	public void setCredentials(Hashtable<String, String> credentials) {
		this.credentials = credentials;
	}
	
	public boolean LogIn(com.training.data.LogIn login) {
		if(login.getUsername().equals(credentials.get("username")) && login.getPassword().equals(credentials.get("password"))) {
			return true;
		}
		return false;
	}
	
	public boolean LogOut() {
		return true;
	}
}
