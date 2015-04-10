package com.training.user;

import java.util.Hashtable;

public class UserProcessor {	
	public UserProcessor() {}
	
	public boolean LogIn(Hashtable<String, String> credentials, String username, String password) {
		if(username.equals(credentials.get("username")) && password.equals(credentials.get("password"))) {
			return true;
		}
		return false;
	}
	
	public boolean LogOut() {
		return true;
	}
}
