package FileHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import CoreHandler.Prompt;
import Data.Input;
import Data.Species;
import Interfaces.IFILE_READ;

public class FileInput implements IFILE_READ {
	private File file;
	private String[] keys;
	private double[] values;

	@Override
	public void parse(Row row, int rowNumber) {
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
			Hashtable<String, Double> features = new Hashtable<String, Double>();
			while(cellIter.hasNext()) {
				values[cCount] = cellIter.next().getNumericCellValue();
				cCount++;
			}

			addToInput(features);
		}
	}

	@Override
	public boolean read() {
		// TODO Auto-generated method stub		
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int rCount = 0;
			Iterator<Row> rowIter = sheet.iterator();
			while(rowIter.hasNext() && rCount<sheet.getPhysicalNumberOfRows()) {
				Row row = rowIter.next();

				parse(row, rCount);
				rCount++;
			}

			return true;
		} catch (Exception e) {
			Prompt.PromptError("ERROR_READ_FILE");
		}

		return false;
	}

	private Input addToInput(Hashtable<String, Double> features) {
		Species species = new Species();
		species.setFeatureLabels(keys);
		species.setFeatureValues(values);

		Input i = new Input();
		i.setSpecies(species);
		
		return i;
	}

	@Override
	public boolean upload_file() {
		// TODO Auto-generated method stub		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		int status = fc.showDialog(null, "Choose file");
		fc.setVisible(true);

		try {
			if (status == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				return true;
			}
			else if (status == JFileChooser.CANCEL_OPTION);
		}
		catch(Exception x) {
			Prompt.PromptError("ERROR_UPLOAD_FILE");
		}

		return false;
	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return file;
	}

	@Override
	public boolean read(File f) {
		// TODO Auto-generated method stub
		return false;
	}
}
