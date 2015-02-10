package FileHandlers;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Map.Entry;

import CoreHandler.Prompt;
import Data.Input;
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

	public void saveToFile(double[] arr, boolean isIJ) {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<arr.length; i++) {
			sb.append(arr[i]+"|");
		}
		sb.setLength(sb.length()-1);
		
		StringBuilder fileName = new StringBuilder();
		fileName.append("settings");
		
		if(isIJ) {
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

//			Files.setAttribute(f.toPath(), "dos:hidden", true);

		} catch (Exception e) {
			Prompt.PromptError("ERROR_SAVE_SCALE");
		}
	}
	
	public void saveToFile(Input input, int index) {
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

			Hashtable<String, Double> svmResult = input.getSvmResult();

			for(Entry<String, Double> e : svmResult.entrySet()) {
				t2.addCell(e.getKey());
				t2.addCell(e.getValue().toString());
			}

			document.add(p2);
			document.add(ls);
			document.add(t2);
			document.newPage();

			document.close();
		} catch (Exception e) {
			Prompt.PromptError("ERROR_SAVE_FILE");
		}
	}

	private Image getImage(Input input) {
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
