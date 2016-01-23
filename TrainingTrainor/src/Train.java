import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Data.ClassifierModel;
import Data.Species;
import data.Parameters;

public class Train {
	final String fileName = "data/results.csv";
	final String[] trainingFile = {"Shape and basic texture - Train.xlsx", "Shape and Haralick texture - Train.xlsx"};
	final String[] testingFile = {"Shape and basic texture - Test.xlsx", "Shape and Haralick texture - Test.xlsx"};

	private Preprocess preprocess;
	private SVM svm;
	private String[] keys;
	private double prevAccuracy = 0;

	public Train() {
		init();
		
		for(int j=0; j<trainingFile.length; j++) {
			for(int i = 0; i<1000; i++) {
				Parameters p = new Parameters();
				p.setRandomValues();
				
				ArrayList<Species> list = readSpecies("trainingSet/" + trainingFile[j]);
				ArrayList<Species> sDataset = preprocess.scale(list);
				ArrayList<Species> pDataset = preprocess.reduceFeatures(sDataset, p.getPCA());
				double accuracy = svm.buildModel(pDataset, p.getSvmParameter());
				
//				ArrayList<Species> test = readSpecies("testingSet/" + testingFile[j]);
//				double accuracy = testModel(test);
				
				if(accuracy > prevAccuracy) {
					prevAccuracy = accuracy;
					ClassifierModel model = buildClassifierModel(j+1, j==0);
					saveToDATFile(model);
				}
				saveToFile(j, p, accuracy);
			}
		}
	}
	
	public void init() {
		this.preprocess = new Preprocess();
		this.svm = new SVM();
	}

	public ArrayList<Species> readSpecies(String filename) {
		// TODO Auto-generated method stub
		ArrayList<Species> list = new ArrayList<Species>();

		try {
			FileInputStream fis = new FileInputStream(filename);
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

	public double testModel(ArrayList<Species> list) {
		int correct = 0;
		
		for(Species s : list) {
			double[] features = s.getFeatureValues();
			features = preprocess.scale(features);
			features = preprocess.reduceFeatures(features);
			String prediction = svm.classify(features);
			
			if(prediction.equalsIgnoreCase(s.getName()))
				correct++;
		}
		return 100.0*correct/list.size();
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
	
	private ClassifierModel buildClassifierModel(int version, boolean isIJUsed) {
		ClassifierModel model = new ClassifierModel();
		model.setCreatedDate(new Date());
		model.setIJUsed(isIJUsed);
		model.setVersion(version);
		model.setPreprocessModel(preprocess.getModel());
		model.setSvmmodel(svm.getModel());
		model.setNotes("");
		return model;
	}
	
	private File saveToDATFile(ClassifierModel model) {
		
		try {
			File f = new File("classifier-model-" + model.getVersion() + ".dat");
			FileOutputStream fileStream = new FileOutputStream(f);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(model);
			objectStream.flush();
			objectStream.close();
			
			return f;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
