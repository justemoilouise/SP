package FileHandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import Data.PreprocessModel;
import Data.SVMModel;
import Data.Species;

public class FileInput {
	private static String[] keys;

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

	public static PreprocessModel readPreprocessModelFromDATFile(File f) {
		try {
			FileInputStream fileStream = new FileInputStream(f);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			PreprocessModel model = (PreprocessModel)objectStream.readObject();			
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

	public static PreprocessModel readPreprocessModelFromDATFile(String filename) {
		try {
			FileInputStream fileStream = new FileInputStream(filename);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			PreprocessModel model = (PreprocessModel)objectStream.readObject();			
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
	
	public static SVMModel readSVMModelFromDATFile(File f) {
		try {
			FileInputStream fileStream = new FileInputStream(f);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			SVMModel model = (SVMModel)objectStream.readObject();			
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

	public static SVMModel readSVMModelFromDATFile(String filename) {
		try {
			FileInputStream fileStream = new FileInputStream(filename);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			SVMModel model = (SVMModel)objectStream.readObject();			
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
	
	public static ClassifierModel readModelFromDATFile(File f) {
		try {
			FileInputStream fileStream = new FileInputStream(f);
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

				if(rCount == 0) {
					FillKeys(row, false);
				}
				else {
					Input i = parseInput(row, rCount);
					list.add(i);
				}

				rCount++;
			}
		} catch (Exception e) {
			Prompt.PromptError("ERROR_READ_FILE");
		}

		return list;
	}

	public static ArrayList<Species> readSpecies(InputStream stream) {
		// TODO Auto-generated method stub
		ArrayList<Species> list = new ArrayList<Species>();

		try {
			XSSFWorkbook workbook = new XSSFWorkbook(stream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rCount = 0;
			Iterator<Row> rowIter = sheet.iterator();
			while(rowIter.hasNext() && rCount<sheet.getPhysicalNumberOfRows()) {
				Row row = rowIter.next();

				if(rCount == 0) {
					FillKeys(row, true);
				}
				else {
					Species s = parseSpecies(row, rCount);
					list.add(s);
				}

				rCount++;
			}
		} catch (Exception e) {}

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

				if(rCount == 0) {
					FillKeys(row, true);
				}
				else {
					Species s = parseSpecies(row, rCount);
					list.add(s);
				}

				rCount++;
			}
		} catch (Exception e) {}

		return list;
	}

	public static Input parseInput(Row row, int rowNumber) {
		// TODO Auto-generated method stub
		double[] values = new double[keys.length];
		int cCount = 0;
		Iterator<Cell> cellIter = row.iterator();
		while(cellIter.hasNext()) {
			values[cCount] = cellIter.next().getNumericCellValue();
			cCount++;
		}

		return setInput(values);
	}

	public static Species parseSpecies(Row row, int rowNumber) {
		// TODO Auto-generated method stub
		double[] values = new double[keys.length];
		int cCount = 0;
		Iterator<Cell> cellIter = row.iterator();
		String name = cellIter.next().getStringCellValue();
		while(cellIter.hasNext()) {
			values[cCount] = cellIter.next().getNumericCellValue();
			cCount++;
		}

		return setSpecies(name, values);
	}

	@SuppressWarnings("resource")
	public static String readFile(String fileName) {
		File f = new File(fileName);
		StringBuilder contents = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			while(reader.ready()) {
				contents.append(reader.readLine() + "\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Prompt.PromptError("ERROR_GET_TUTORIAL");
		}

		return contents.toString();
	}
	
	private static void FillKeys(Row row, boolean skipFirstColumn) {
		int cCount = 0;
		int len = row.getPhysicalNumberOfCells()-1;
		keys = new String[len];

		Iterator<Cell> cellIter = row.iterator();
		
		if(skipFirstColumn)
			cellIter.next();
		
		while(cellIter.hasNext()) {
			keys[cCount] = cellIter.next().getStringCellValue();
			cCount++;
		}
	}

	private static Species setSpecies(String name, double[] values) {
		Species species = new Species();
		species.setName(name);
		species.setFeatureLabels(keys);
		species.setFeatureValues(values);

		return species;
	}

	private static Input setInput(double[] values) {
		Input i = new Input();
		i.setSpecies(setSpecies("", values));

		return i;
	}

}
