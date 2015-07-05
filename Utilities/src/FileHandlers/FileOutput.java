package FileHandlers;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.DecisionTreeModel;
import Data.Input;
import Data.PreprocessModel;
import Data.SVMModel;
import Data.SVMResult;
import Data.Species;
import ImageHandlers.ProcessImage;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class FileOutput extends Thread {

	public FileOutput() {}
	
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
		saveToFile(model.getDecisionTreeModel());
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
	
	public static boolean saveToFile(DecisionTreeModel model) {
		try {				
			File f = new File("models/DecisionTree.dat");
			
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
	
	public static boolean saveToFile(Input input, int index) {
		Image img = getImage(input);

		Document document=new Document(PageSize.LETTER, 50, 50, 50, 50);
		try {
			String fileName = "output (" + index + ").pdf";
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();

			Paragraph title = new Paragraph("Radiolarians", FontFactory.getFont(FontFactory.HELVETICA, 35, Font.BOLD));
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
				img.setSpacingAfter(20);
				document.add(img);
				document.newPage();
			}

			Species s = input.getSpecies();			
			
			//Measurements
			Paragraph p1 = new Paragraph("Measurements");
			p1.setSpacingBefore(20);
			p1.setSpacingAfter(10);
			PdfPTable t1 = new PdfPTable(2);
			t1.setSpacingAfter(20);
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
			document.newPage();
			
			//SVM
			Paragraph p2 = new Paragraph("SVM Prediction");
			p2.setSpacingBefore(20);
			p2.setSpacingAfter(10);
			PdfPTable t2 = new PdfPTable(2);
			t2.setSpacingAfter(20);
			t2.setSpacingBefore(10);
			t2.setKeepTogether(true);
			t2.addCell("Species");
			t2.addCell("Probabilty");

			ArrayList<SVMResult> svmResult = input.getSvmResult();

			Iterator<SVMResult> iter = svmResult.iterator();
			while(iter.hasNext()) {
				SVMResult result = iter.next();
				
				t2.addCell(result.getName());
				t2.addCell(Double.toString(result.getProbability()));
			}

			document.add(p2);
			document.add(ls);
			document.add(t2);
			document.newPage();

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
			int w = (int)PageSize.LETTER.getWidth();
			int h = (int)PageSize.LETTER.getHeight();
			
			if(!input.getImageName().equals("/resources/img_noimg.png")) {
				img = Image.getInstance(input.getImageName());
				Dimension d = ProcessImage.getScaledDimension(new Dimension((int) img.getWidth(), (int) img.getHeight()),
						new Dimension((int) (w*2)/3, (int) (h*2)/3));
				img.scaleAbsolute(d.width, d.height);
			}
		}
		catch (Exception e) {
			Prompt.PromptError("ERROR_GET_IMAGE");
		}
		
		return img;
	}
}
