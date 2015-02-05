package Interfaces;

import java.io.File;

import org.apache.poi.ss.usermodel.Row;

public interface IFILE_READ {

	/**
	 * Gets the uploaded file
	 * 
	 * @return file
	 */
	public File getFile();
	
	/**
	 * Allows user to select file to be uploaded
	 */
	public boolean upload_file();
	
	/**
	 * Reads the contents of a file
	 */
	public boolean read();
	
	/**
	 * Reads the contents of a file
	 */
	public boolean read(File f);
	
	/**
	 * Parses the line read from the file
	 * 
	 * @param Excel row to be read
	 * @param row number
	 */
	public void parse(Row row, int rowNumber);
}
