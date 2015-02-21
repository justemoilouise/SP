package com.training.user;

import java.util.Hashtable;

import com.training.helpers.FileHelper;

public class UserProcessor {
	private Hashtable<String, String> credentials;
	
	public UserProcessor() {
		//GetAdminCredentials();
	}
	
	public boolean LogIn(String username, String password) {
//		if(username.equals(credentials.get("username")) && password.equals(credentials.get("password"))) {
		if(username.equals("admin") && password.equals("UN5qGb5awt")) {
			return true;
		}
		return false;
	}
	
	public boolean LogOut() {
		return true;
	}
	
	private void GetAdminCredentials() {
		credentials = FileHelper.readFromXML("admin-credentials");
	}
}
