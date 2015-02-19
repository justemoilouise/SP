package FileHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.Input;
import Data.Species;

public class FileInput {
	private static String[] keys;
	private static double[] values;

	public static File uploadModelFile() {
		// TODO Auto-generated method stub
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Dat files", "dat");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		int status = fc.showDialog(null, "Choose file");
		fc.setVisible(true);

		try {
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				return file;
			}
			else if (status == JFileChooser.CANCEL_OPTION);
		}
		catch(Exception x) {
			Prompt.PromptError("ERROR_UPLOAD_FILE");
		}

		return null;
	}

	public static ClassifierModel readModelFromDATFile(String filename) {
		try {
			FileInputStream fileStream = new FileInputStream(filename);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			ClassifierModel model = (ClassifierModel)objectStream.readObject();			
			objectStream.close();
			
			return model;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public static File uploadExcelFile() {
		// TODO Auto-generated method stub		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		int status = fc.showDialog(null, "Choose file");
		fc.setVisible(true);

		try {
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				return file;
			}
			else if (status == JFileChooser.CANCEL_OPTION);
		}
		catch(Exception x) {
			Prompt.PromptError("ERROR_UPLOAD_FILE");
		}

		return null;
	}

	public static ArrayList<Input> readInput(File f) {
		// TODO Auto-generated method stub
		ArrayList<Input> list = new ArrayList<Input>();
		
		try {
			FileInputStream fis = new FileInputStream(f);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rCount = 0;
			Iterator<Row> rowIter = sheet.iterator();
			while(rowIter.hasNext() && rCount<sheet.getPhysicalNumberOfRows()) {
				Row row = rowIter.next();

				Input i = parseInput(row, rCount);
				list.add(i);
				
				rCount++;
			}
		} catch (Exception e) {
			Prompt.PromptError("ERROR_READ_FILE");
		}
		
		return list;
	}

	public static ArrayList<Species> readSpecies(File f) {
		// TODO Auto-generated method stub
		ArrayList<Species> list = new ArrayList<Species>();
		
		try {
			FileInputStream fis = new FileInputStream(f);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rCount = 0;
			Iterator<Row> rowIter = sheet.iterator();
			while(rowIter.hasNext() && rCount<sheet.getPhysicalNumberOfRows()) {
				Row row = rowIter.next();

				Species s = parseSpecies(row, rCount);
				list.add(s);
				
				rCount++;
			}
		} catch (Exception e) {
			Prompt.PromptError("ERROR_READ_FILE");
		}
		
		return list;
	}

	public static Input parseInput(Row row, int rowNumber) {
		// TODO Auto-generated method stub
		int cCount = 0;
		Iterator<Cell> cellIter = row.iterator();
		if(rowNumber==0) {
			keys = new String[row.getPhysicalNumberOfCells()];
			values = new double[row.getPhysicalNumberOfCells()];
			while(cellIter.hasNext()) {
				keys[cCount] = cellIter.next().getStringCellValue();
				cCount++;
			}
		}
		else {
			while(cellIter.hasNext()) {
				values[cCount] = cellIter.next().getNumericCellValue();
				cCount++;
			}

			return setInput();
		}
		
		return null;
	}

	public static Species parseSpecies(Row row, int rowNumber) {
		// TODO Auto-generated method stub
		int cCount = 0;
		Iterator<Cell> cellIter = row.iterator();
		if(rowNumber==0) {
			keys = new String[row.getPhysicalNumberOfCells()];
			values = new double[row.getPhysicalNumberOfCells()];
			while(cellIter.hasNext()) {
				keys[cCount] = cellIter.next().getStringCellValue();
				cCount++;
			}
		}
		else {
			while(cellIter.hasNext()) {
				values[cCount] = cellIter.next().getNumericCellValue();
				cCount++;
			}

			return setSpecies();
		}
		
		return null;
	}
	
	private static Species setSpecies() {
		Species species = new Species();
		species.setFeatureLabels(keys);
		species.setFeatureValues(values);
		
		return species;
	}
	
	private static Input setInput() {
		Input i = new Input();
		i.setSpecies(setSpecies());
		
		return i;
	}

}
