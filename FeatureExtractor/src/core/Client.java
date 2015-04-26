package core;

import ij.ImagePlus;

import java.awt.image.BufferedImage;

import ImageHandlers.ProcessImage;
import gui.MainWindow;

public class Client {
	private static MainWindow pm;
	private static boolean isIJ;

	private static ImagePlus imgPlus;

	public Client() {
		pm = new MainWindow();	
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

	@SuppressWarnings("unused")
	public static void onSubmit() {		
		double[] features = getFeatures();
	}

	private static double[] getFeatures() {
		try {			
			if(imgPlus !=null) {
				BufferedImage bi = ProcessImage.getROI(imgPlus);
				ImagePlus imgPlus = new ImagePlus("", bi);

				FeatureExtraction featureExtraction = new FeatureExtraction();
				featureExtraction.getShapeDescriptors(imgPlus);

				if(isIJ) {
					featureExtraction.getTextureDescriptors(imgPlus.getProcessor());
				}
				else {
					featureExtraction.getHaralickDescriptors(imgPlus.getProcessor());
				}

				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
