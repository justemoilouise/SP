package core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.Species;
import ExceptionHandlers.ExceptionHandler;
import ExceptionHandlers.ThreadException;
import FileHandlers.FileConfig;
import FileHandlers.FileOutput;
import ImageHandlers.ProcessImage;
import gui.MainWindow;
import gui.StartScreen;
import ij.ImagePlus;

public class Client {
	private static StartScreen screen;
	private static MainWindow pm;
	
	private static Properties props;
	private static ImagePlus imgPlus;
	private static ArrayList<Species> trainingSet;
	private static boolean isIJ;
	private static int count;
	private static ClassifierModel model;

	public Client() {
		props = FileConfig.readConfig();
		trainingSet = new ArrayList<Species>();
		count = 1;
		pm = new MainWindow();	
		Prompt.SetParentComponent(pm.getDesktoPane());
		isIJ = Prompt.chooseFeatures(true);
		init();
	}

	private void init() {		
		screen = new StartScreen();
		screen.setExecutable(true);
		
		Thread splashScreen = new Thread(screen, "SplashScreen");
		Thread init = new Thread(new Initialize(screen), "Initialize");

		splashScreen.setUncaughtExceptionHandler(new ThreadException());
		splashScreen.start();

		init.setUncaughtExceptionHandler(new ThreadException());
		init.start();
	}
	
	public static Properties getProperties() {
		return props;
	}
	
	public static void setImgPlus(ImagePlus img) {
		Client.imgPlus = img;
	}
	
	public static ArrayList<Species> getTrainingSet() {
		return trainingSet;
	}

	public static void setTrainingSet(ArrayList<Species> trainingSet) {
		Client.trainingSet = trainingSet;
	}

	public static MainWindow getPm() {
		return pm;
	}
	
	public static boolean validateInput() {
		if(trainingSet.size() > 0) {
				return true;
		}
		if(imgPlus != null)
			return true;
		return false;
	}
		
	@SuppressWarnings("unchecked")
	public static void onSubmit() {
		Preprocess preprocess = new Preprocess();
		ArrayList<Species> dataset = preprocess.scale((ArrayList<Species>)trainingSet.clone());
		dataset = preprocess.reduceFeatures(dataset);
		
		SVM svm = new SVM();
		svm.buildModel(dataset, null);
		
		model = new ClassifierModel();
		model.setCreatedDate(new Date());
		model.setPreprocessModel(preprocess.getPreprocessModel());
		model.setSvmmodel(svm.getSVMModel());
	}

	public static void getFeatures() {
		try {
			BufferedImage bi = ProcessImage.getROI(imgPlus);

			String name = "tmp/"+count+".png";
			ProcessImage.saveImage(bi, name);
			
			FeatureExtraction featureExtraction = new FeatureExtraction();
			featureExtraction.getShapeDescriptors(imgPlus);
			
			if(isIJ) {
				featureExtraction.getTextureDescriptors(imgPlus.getProcessor());
			}
			else {
				featureExtraction.getHaralickDescriptors(imgPlus.getProcessor());
			}
			
			Species s = new Species();
			s.setFeatureLabels(featureExtraction.getFeatureLabels());
			s.setFeatureValues(featureExtraction.getFeatureValues());
			trainingSet.add(s);
		}
		catch(Exception e) {
			Prompt.PromptError("ERROR_INPUT_FEATURES");
			printStackTrace(e);
		}
	}
	
	public static void exportTrainingSet(String filename) {
		pm.appendToConsole("Exporting training set...");

		boolean dloadSuccess = FileOutput.saveToExcelFile(trainingSet, filename);
		if(dloadSuccess)
			Prompt.PromptSuccess("SUCCESS_DLOAD");
		else
			Prompt.PromptSuccess("ERROR_DLOAD");
	}
	
	public static void printStackTrace(Throwable ex) {
		String stackTrace = ExceptionHandler.getStackTrace(ex);
		pm.appendToErrorLog(stackTrace);
	}
}
