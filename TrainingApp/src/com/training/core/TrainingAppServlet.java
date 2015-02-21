package com.training.core;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.training.helpers.ServletHelper;

@SuppressWarnings("serial")
public class TrainingAppServlet extends HttpServlet {
	private TrainingAppProcessor processor;
	
	public TrainingAppServlet() {
		this.processor = new TrainingAppProcessor();
	}
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Object response = null;
		String method = ServletHelper.ExtractMethod(req.getRequestURI());
		
		if(method.equalsIgnoreCase("getmodellist")) {
			response = processor.getModelList();
		} else if(method.equalsIgnoreCase("getapplist")) {
			processor.getAppList();
		} else if(method.equalsIgnoreCase("saveclassifiermodel")) {
			response = processor.saveClassifierModel(null, null, null);
		} else if(method.equalsIgnoreCase("readdataset")) {
			String requestBody = ServletHelper.GetRequestBody(req.getReader());
			File f = ServletHelper.ConvertToObject(requestBody, File.class);
			
			response = processor.readDataset(f);
		} else if(method.equalsIgnoreCase("read")) {
			response = processor.read();
		}
		
		resp.setContentType("application/json");
		resp.getWriter().println(ServletHelper.ConvertToJson(response));
	}
	
//	private void UploadFile() {
//		 DiskFileItemFactory factory = new DiskFileItemFactory();
//	      // Location to save data that is larger than maxMemSize.
//	      factory.setRepository(new File("c:\\temp"));
//
//	      // Create a new file upload handler
//	      ServletFileUpload upload = new ServletFileUpload(factory);
//	      // maximum file size to be uploaded.
//	      upload.setSizeMax( maxFileSize );
//
//	      try{ 
//	      // Parse the request to get file items.
//	      List fileItems = upload.parseRequest(request);
//		
//	      // Process the uploaded file items
//	      Iterator i = fileItems.iterator();
//
//	      out.println("<html>");
//	      out.println("<head>");
//	      out.println("<title>Servlet upload</title>");  
//	      out.println("</head>");
//	      out.println("<body>");
//	      while ( i.hasNext () ) 
//	      {
//	         FileItem fi = (FileItem)i.next();
//	         if ( !fi.isFormField () )	
//	         {
//	            // Get the uploaded file parameters
//	            String fieldName = fi.getFieldName();
//	            String fileName = fi.getName();
//	            String contentType = fi.getContentType();
//	            boolean isInMemory = fi.isInMemory();
//	            long sizeInBytes = fi.getSize();
//	            // Write the file
//	            if( fileName.lastIndexOf("\\") >= 0 ){
//	               file = new File( filePath + 
//	               fileName.substring( fileName.lastIndexOf("\\"))) ;
//	            }else{
//	               file = new File( filePath + 
//	               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
//	            }
//	            fi.write( file ) ;
//	            out.println("Uploaded Filename: " + fileName + "<br>");
//	         }
//	      }
//	      }
//	}
}
