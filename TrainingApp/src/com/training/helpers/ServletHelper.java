package com.training.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.util.IOUtils;

import com.google.gson.Gson;

public class ServletHelper {

	public static String ExtractMethod(String requestUri) {
		int index = requestUri.lastIndexOf("/") + 1;
		
		return requestUri.substring(index, requestUri.length());
	}
	
	public static String GetRequestBody(BufferedReader reader) throws IOException {
		StringBuilder buffer = new StringBuilder();
		String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	    }

		return buffer.toString();
	}
	
	public static byte[] GetRequestBody(InputStream stream) throws IOException {
		return IOUtils.toByteArray(stream);
	}
	
	public static <T> T ConvertToObject(String obj, Class<T> classObj) {
		Gson gson = new Gson();
		T convertedObject = gson.fromJson(obj, classObj);

		return convertedObject;
	}
	
	public static String ConvertToJson(Object o) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(o);
		
		return jsonString;
	}
}
