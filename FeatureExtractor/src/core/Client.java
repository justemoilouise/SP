package core;

import ij.ImagePlus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import CoreHandler.Prompt;
import gui.MainWindow;

public class Client {
	private static MainWindow pm;
	private static boolean isIJ;
	private final static String fileName = "data/results.csv";
	private static ImagePlus imgPlus;
	private static String[] headings;
	private static int count = -1;

	public Client() {
		pm = new MainWindow();
		pm.setVisible(true);
		
		Prompt.SetParentComponent(pm);
		isIJ = Prompt.chooseFeatures(true);
	}

	public static void setImgPlus(ImagePlus img) {
		imgPlus = img;
	}
	
	public static void setIsIJ(boolean isIJUsed) {
		isIJ = isIJUsed;
	}

	public static MainWindow getPm() {
		return pm;
	}

	public static boolean validateInput() {
		if(imgPlus != null)
			return true;

		return false;
	}

	public static void onSubmit() {
		count++;
		double[] features = getFeatures();
		saveToFile(features);
	}

	private static double[] getFeatures() {
		try {			
			if(imgPlus !=null) {
				FeatureExtraction featureExtraction = new FeatureExtraction();
				featureExtraction.getShapeDescriptors(imgPlus);
				//featureExtraction.getImageMoments(imgPlus);
				
				if(isIJ) {
					featureExtraction.getTextureDescriptors(imgPlus.getProcessor());
				}
				else {
					featureExtraction.getHaralickDescriptors(imgPlus.getProcessor());
				}

				headings = featureExtraction.getFeatureLabels();
				return featureExtraction.getFeatureValues();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static boolean saveToFile(double[] arr) {
		try {
			File f = new File(fileName);
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			
			if(count == 0) {
				StringBuilder str = new StringBuilder();
				str.append("Species");
				str.append(",");

				for(int i=0; i<headings.length; i++) {
					str.append(headings[i]);
					str.append(",");
				}
				
				out.println(str.substring(0, str.length()-1));
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("Zhamoidellum ovum Dumitrica");
			sb.append(",");

			for(int i=0; i<arr.length; i++) {
				sb.append(arr[i]);
				sb.append(",");
			}
			
			out.println(sb.substring(0, sb.length()-1));
			out.flush();
			out.close();

			return true;

		} catch(Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
