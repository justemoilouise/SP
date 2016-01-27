package FileHandlers;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.Input;
import Data.PreprocessModel;
import Data.SVMModel;
import Data.SVMResult;
import Data.Species;
import Helpers.SVMResultComparator;
import ImageHandlers.ProcessImage;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class FileOutput extends Thread {

	public FileOutput() {}
	
	public static boolean saveToExcelFile(ArrayList<Species> trainingSet, String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet worksheet = workbook.createSheet("Training Set");
			
			int rowCount = -1;
			//add headings
			String[] labels = trainingSet.get(0).getFeatureLabels();
			Row row = worksheet.createRow(rowCount++);			
			for(int colCount = 0; colCount <= labels.length; colCount++) {
				Cell cell = row.createCell(colCount);
				if(colCount == 0)
					cell.setCellValue("Species");
				else {
					cell.setCellValue(labels[colCount-1]);
				}
			}
			rowCount++;
			
			// add data
			Iterator<Species> iter = trainingSet.iterator();
			while(iter.hasNext()) {
				Species s = iter.next();
				Row speciesRow = worksheet.createRow(rowCount);
				for(int j = 0; j <= s.getFeatureValues().length; j++) {
					Cell cell = speciesRow.createCell(j);
					if(j == 0)
						cell.setCellValue(s.getName());
					else {
						cell.setCellValue(s.getFeatureValues()[j-1]);
					}
				}
				rowCount++;
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean saveToDATFile(ClassifierModel model, String fileName) {
		try {
			fileName = fileName.endsWith(".dat") ? fileName : fileName.concat(".dat");
			File f = new File(fileName);
			FileOutputStream fileStream = new FileOutputStream(f);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(model);
			objectStream.flush();
			objectStream.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static File saveToDATFile(ClassifierModel model) {		
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
	
	public static void saveToFile(ClassifierModel model, boolean isIJ) {
		saveToFile(model.getPreprocessModel(), isIJ);
		saveToFile(model.getSvmmodel(), isIJ);
	}
	
	public static boolean saveToFile(PreprocessModel model, boolean isIJ) {
		try {
			String flag = "_IJ";
			
			if(!isIJ)
				flag = "_JF";
				
			File f = new File("models/Preprocess" + flag + ".dat");
			
			if(f.exists()) {
				f.delete();
			}
			
			FileOutputStream fileStream = new FileOutputStream(f);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(model);
			objectStream.flush();
			objectStream.close();
			
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean saveToFile(SVMModel model, boolean isIJ) {
		try {
			String flag = "_IJ";
			
			if(!isIJ)
				flag = "_JF";
				
			File f = new File("models/SVM" + flag + ".dat");
			
			if(f.exists()) {
				f.delete();
			}
			
			FileOutputStream fileStream = new FileOutputStream(f);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(model);
			objectStream.flush();
			objectStream.close();
			
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean saveToFile(ClassifierModel model, Input input, int index, String fileName) {
		fileName = fileName.endsWith(".pdf") ? fileName : fileName.concat(".pdf");
		Image img = getImage(input);
		Document document=new Document(PageSize.LETTER, 20, 20, 50, 50);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();

			Paragraph title = new Paragraph("RaDSS", FontFactory.getFont(FontFactory.HELVETICA, 35, Font.BOLD));
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(15);
			document.add(title);

			LineSeparator ls = new LineSeparator(1, 100, BaseColor.BLACK, Element.ALIGN_LEFT, 1);			

			//Input
			if(img!=null) {
				Paragraph p0 = new Paragraph("Input");
				p0.setSpacingAfter(10);
				document.add(p0);
				document.add(ls);
				img.setAlignment(Element.ALIGN_CENTER);
				img.setSpacingAfter(30);
				document.add(img);
			}

			Species s = input.getSpecies();			
			
			//Measurements
			Paragraph p1 = new Paragraph("Measurements");
			p1.setSpacingBefore(20);
			p1.setSpacingAfter(10);
			PdfPTable t1 = new PdfPTable(2);
			t1.setSpacingAfter(30);
			t1.setSpacingBefore(10);
			t1.setKeepTogether(true);
			t1.addCell("Measurement");
			t1.addCell("Value");

			String[] label = s.getFeatureLabels();
			double[] value = s.getFeatureValues();

			for(int i=0; i<label.length; i++) {
				t1.addCell(label[i]);
				t1.addCell(Double.toString(value[i]));
			}

			document.add(p1);
			document.add(ls);
			document.add(t1);
			
			//SVM
			Paragraph p2 = new Paragraph("SVM Prediction");
			p2.setSpacingBefore(20);
			p2.setSpacingAfter(10);
			PdfPTable t2 = new PdfPTable(2);
			t2.setSpacingAfter(30);
			t2.setSpacingBefore(10);
			t2.setKeepTogether(true);
			t2.addCell("Species");
			t2.addCell("Probabilty");

			ArrayList<SVMResult> svmResult = input.getSvmResult();
			Collections.sort(svmResult, new SVMResultComparator());
			Iterator<SVMResult> iter = svmResult.iterator();
			while(iter.hasNext()) {
				SVMResult result = iter.next();
				t2.addCell(result.getName());
				t2.addCell(Double.toString(result.getProbability()));
			}

			document.add(p2);
			document.add(ls);
			document.add(t2);

			// model details
			Paragraph p3 = new Paragraph("Classifier model details");
			p3.setSpacingBefore(20);
			p3.setSpacingAfter(10);
			document.add(p3);
			document.add(ls);
			
			//scaling factors
			Paragraph scaleP = new Paragraph("Scaling factors");
			scaleP.setSpacingAfter(10);
			PdfPTable scale = new PdfPTable(3);
			scale.setSpacingAfter(30);
			scale.setSpacingBefore(10);
			scale.setKeepTogether(true);
			scale.addCell("Feature");
			scale.addCell("Minimum value");
			scale.addCell("Maximum value");
			String[] features = s.getFeatureLabels();
			double[] min = model.getPreprocessModel().getMin();
			double[] max = model.getPreprocessModel().getMax();
			for(int i=0; i<features.length; i++) {
				scale.addCell(features[i]);
				scale.addCell(Double.toString(min[i]));
				scale.addCell(Double.toString(max[i]));
			}
			document.add(scaleP);
			document.add(scale);

			// PCA
			Paragraph pcaP = new Paragraph("Principal components");
			pcaP.setSpacingAfter(10);
			double[][] pc = model.getPreprocessModel().getPrincipalComponents();
			PdfPTable pca = new PdfPTable(pc[0].length + 1);
			pca.setSpacingAfter(30);
			pca.setSpacingBefore(10);
			pca.setKeepTogether(true);
			
			for(int i=0; i<pc.length; i++) {
				pca.addCell(Integer.toString(i + 1));
				for(int j=0; j<pc[0].length; j++) {
					pca.addCell(new Phrase(String.format("%.2f", pc[i][j]), FontFactory.getFont(FontFactory.HELVETICA, 8)));
				}
			}
			document.add(pcaP);
			document.add(pca);
			
			// SVM
			String featuresUsed = model.isIJUsed() ? "Shape and basic features" : "Shape and Haralick texture";
			Paragraph svmP = new Paragraph("SVM");
			svmP.setSpacingAfter(10);
			PdfPTable svm = new PdfPTable(2);
			svm.setSpacingAfter(30);
			svm.setSpacingBefore(10);
			svm.setKeepTogether(true);
			svm.addCell("Features used");
			svm.addCell(featuresUsed);
			svm.addCell("Accuracy");
			svm.addCell(Double.toString(model.getSvmmodel().getAccuracy()));
			document.add(svmP);
			document.add(svm);
			
			document.close();
			
			return true;
		} catch (Exception e) {
			Prompt.PromptError("ERROR_SAVE_FILE");
		}
		
		return false;
	}
		
	private static Image getImage(Input input) {
		Image img = null;
		
		try {
			int newWidth = 200;
			if(!input.getImageName().equals("/resources/img_noimg.png")) {
				img = Image.getInstance(input.getImageName());
				Dimension d = ProcessImage.getScaledDimension(new Dimension((int) img.getWidth(), (int) img.getHeight()),
						new Dimension(newWidth, newWidth));
				img.scaleAbsolute(d.width, d.height);
			}
		}
		catch (Exception e) {
			Prompt.PromptError("ERROR_GET_IMAGE");
		}
		
		return img;
	}
}
