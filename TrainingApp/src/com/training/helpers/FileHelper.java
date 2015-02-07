package com.training.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Data.Species;
import FileHandlers.FileInput;

public class FileHelper {
	
	public static Hashtable<String, String> readFromXML(String tag) {
		Hashtable<String, String> results = new Hashtable<String, String>();
		
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.parse("web.xml");
			NodeList nodes = doc.getElementsByTagName(tag);
			
			if(nodes.getLength() > 0) {
				NodeList list = nodes.item(0).getChildNodes();
				for(int i=0; i<list.getLength(); i++) {
					Node n = list.item(i);
					results.put(n.getNodeName(), n.getNodeValue());
				}
			}
		}
		catch(Exception x) {}
		
		return results;
	}
	
	public static ArrayList<Species> readFromFile() {
		FileInput input = new FileInput();
		File f = input.uploadFile();
		
		if(f != null) {
			return input.read(f);
		}
		
		return null;
	}
	
}
