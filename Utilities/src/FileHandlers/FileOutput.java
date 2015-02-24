package FileHandlers;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import CoreHandler.Prompt;
import Data.ClassifierModel;
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
	
	public static File saveToXMLFile(ClassifierModel model) {
		org.w3c.dom.Element root, element, subElement;
		File f = null;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.newDocument();
			
			root = doc.createElement("classifier-model");
			doc.appendChild(root);
			
			element = doc.createElement("version");
			element.appendChild(doc.createTextNode(Double.toString(model.getVersion())));
			root.appendChild(element);
			
			element = doc.createElement("created-date");
			element.appendChild(doc.createTextNode(model.getCreatedDate().toString()));
			root.appendChild(element);
			
			element = doc.createElement("notes");
			element.appendChild(doc.createTextNode(model.getNotes()));
			root.appendChild(element);
			
			element = doc.createElement("isIJUsed");
			element.appendChild(doc.createTextNode(Boolean.toString(model.isIJUsed())));
			root.appendChild(element);
			
			// Preprocessing model
			PreprocessModel preprocessModel = model.getPreprocessModel();
			element = doc.createElement("preprocess-model");
			root.appendChild(element);
			
			subElement = doc.createElement("scale-min");
			subElement.appendChild(doc.createTextNode(Arrays.toString(preprocessModel.getMin())));
			element.appendChild(subElement);
			
			subElement = doc.createElement("scale-max");
			subElement.appendChild(doc.createTextNode(Arrays.toString(preprocessModel.getMax())));
			element.appendChild(subElement);
			
			subElement = doc.createElement("pca-pc");
			subElement.appendChild(doc.createTextNode(Integer.toString(preprocessModel.getPC())));
			element.appendChild(subElement);
			
			subElement = doc.createElement("pca-mean");
			subElement.appendChild(doc.createTextNode(Arrays.toString(preprocessModel.getMean())));
			element.appendChild(subElement);
			
			subElement = doc.createElement("pca-principal-components");
			subElement.appendChild(doc.createTextNode(Arrays.toString(preprocessModel.getPrincipalComponents())));
			element.appendChild(subElement);
			
			// SVM model
			SVMModel svmModel = model.getSvmmodel();
			element = doc.createElement("svm-model");
			root.appendChild(element);
			
			subElement = doc.createElement("svm-svm-model");
			subElement.appendChild(doc.createTextNode(svmModel.getModel().toString()));
			element.appendChild(subElement);
			
			subElement = doc.createElement("svm-accuracy");
			subElement.appendChild(doc.createTextNode(Double.toString(svmModel.getAccuracy())));
			element.appendChild(subElement);
			
			subElement = doc.createElement("svm-classes");
			subElement.appendChild(doc.createTextNode(Arrays.toString(svmModel.getClasses().toArray())));
			element.appendChild(subElement);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			
			f = new File("classifier-model-" + model.getVersion() + ".xml");
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
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
	
	public static boolean saveToFile(PreprocessModel model, boolean isIJ) {
		try {
			String flag = "_IJ";
			
			if(!isIJ)
				flag = "_JF";
				
			File f = new File("models/Preprocess" + flag + ".dat");
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
	
	public static boolean saveToFile(double[] arr, boolean isIJUsed) {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<arr.length; i++) {
			sb.append(arr[i]+"|");
		}
		sb.setLength(sb.length()-1);
		
		StringBuilder fileName = new StringBuilder();
		fileName.append("settings");
		
		if(isIJUsed) {
			fileName.append("_IJ");
		}
		else {
			fileName.append("_JF");
		}
		
		fileName.append("_preprocess.txt");
		
		File f = new File(fileName.toString());

		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(f, true));
			pw.print(sb.toString());
			pw.println();
			pw.flush();
			pw.close();
			
			return true;
		} catch (Exception e) {
			Prompt.PromptError("ERROR_SAVE_SCALE");
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
			
			if(!input.getImageName().equals("img/noimg.png")) {
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
