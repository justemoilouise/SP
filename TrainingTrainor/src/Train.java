import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Species;
import data.Parameters;

public class Train {
	final String fileName = "data/results.csv";
	final String[] inputFile = {"Shape and basic texture.xlsx", "Shape and Haralick texture.xlsx"};

	private Preprocess preprocess;
	private SVM svm;
	private String[] keys;

	public Train() {
		init();
		
		for(int j=0; j<inputFile.length; j++) {
			readSpecies(j);
			
			for(int i = 0; i<500; i++) {
				Parameters p = new Parameters();
				p.setRandomValues();
				
				ArrayList<Species> list = readSpecies(j);
				ArrayList<Species> sDataset = preprocess.scale(list);
				ArrayList<Species> pDataset = preprocess.reduceFeatures(sDataset, p.getPCA());
				double accuracy = svm.buildModel(pDataset, p.getSvmParameter());
				
				saveToFile(j, p, accuracy);
			}
		}
	}
	
	public void init() {
		this.preprocess = new Preprocess();
		this.svm = new SVM();
	}

	public ArrayList<Species> readSpecies(int index) {
		// TODO Auto-generated method stub
		ArrayList<Species> list = new ArrayList<Species>();

		try {
			FileInputStream fis = new FileInputStream("trainingSet/" + inputFile[index]);
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

	public void saveToFile(int index, Parameters params, double accuracy) {
		try {
			File f = new File(fileName);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));		
			out.println(index + "," + params.convertToString() + "," + accuracy);
			out.flush();
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private Species parseSpecies(Row row, int rowNumber) {
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

	private void FillKeys(Row row, boolean skipFirstColumn) {
		int cCount = 0;
		int len = row.getPhysicalNumberOfCells();
		keys = new String[len];

		Iterator<Cell> cellIter = row.iterator();

		if(skipFirstColumn) {
			cellIter.next();
			keys = new String[len-1];
		}

		while(cellIter.hasNext()) {
			keys[cCount] = cellIter.next().getStringCellValue();
			cCount++;
		}
	}

	private Species setSpecies(String name, double[] values) {
		Species species = new Species();
		species.setName(name);
		species.setFeatureLabels(keys);
		species.setFeatureValues(values);

		return species;
	}
}
