package helpers;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;

public class DataHelper {

	public static String GetFileNames(File[] arr) {
		StringBuilder b = new StringBuilder();
		
		for(File f : arr) {
			b.append(f.getName());
			b.append(", ");
		}
		
		return b.substring(0, b.length() - 2);
	}
	
	public static String ConvertArrayListToString(ArrayList<String> arr) {
		StringBuilder b = new StringBuilder();
		
		for(String s : arr) {
			b.append(s);
			b.append(", ");
		}
		
		return b.substring(0, b.length() - 2);
	}
	
	public static String ConvertArrayToString(double[] arr) {
		StringBuilder b = new StringBuilder();
		
		for(double d : arr) {
			b.append(d);
			b.append(", ");
		}
		
		return b.substring(0, b.length() - 2);
	}
	
	public static String AddTableRow(String label, Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append("<td>");
		sb.append(label);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(value);
		sb.append("</td>");
		sb.append("</tr>");
		
		return sb.toString();
	}
	
	public static String AddTableRow(String label, Object val1, Object val2) {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append("<td>");
		sb.append(label);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(val1);
		sb.append("</td>");
		sb.append("<td>");
		sb.append(val2);
		sb.append("</td>");
		sb.append("</tr>");
		
		return sb.toString();
	}
	
	public static String ConvertToJson(Object o) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(o);
		
		return jsonString;
	}
}
