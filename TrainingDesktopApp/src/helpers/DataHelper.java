package helpers;

import java.util.ArrayList;

public class DataHelper {

	public static String ConvertArrayListToString(ArrayList<String> arr) {
		StringBuilder b = new StringBuilder();
		
		for(String s : arr) {
			b.append(s);
			b.append(", ");
		}
		
		b.substring(0, b.length() - 2);
		
		return b.toString();
	}
	
	public static String ConvertArrayToString(double[] arr) {
		StringBuilder b = new StringBuilder();
		
		for(double d : arr) {
			b.append(d);
			b.append(", ");
		}
		
		b.substring(0, b.length() - 2);
		
		return b.toString();
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
}