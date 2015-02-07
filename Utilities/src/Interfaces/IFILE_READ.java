package Interfaces;

import java.io.File;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;

import Data.Input;
import Data.Species;

public interface IFILE_READ {
	
	/**
	 * Allows user to select file to be uploaded
	 */
	public File uploadFile();
	
	/**
	 * Reads the contents of a file
	 */
	public ArrayList<Input> readInput(File f);
	
	/**
	 * Parses the line read from the file
	 * 
	 * @param Excel row to be read
	 * @param row number
	 */
	public Input parseInput(Row row, int rowNumber);
	
	/**
	 * Reads the contents of a file
	 */
	public ArrayList<Species> read(File f);
	
	/**
	 * Parses the line read from the file
	 * 
	 * @param Excel row to be read
	 * @param row number
	 */
	public Species parse(Row row, int rowNumber);
}
