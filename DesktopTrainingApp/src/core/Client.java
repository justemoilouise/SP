package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.Species;
import ExceptionHandlers.ExceptionHandler;
import FileHandlers.FileConfig;
import FileHandlers.FileOutput;
import gui.FeaturesPanel;
import gui.MainWindow;
import gui.ParametersPanel;
import gui.StartScreen;
import ij.ImagePlus;

public class Client {
	public static double modelVersion;
	private static StartScreen screen;
	private static MainWindow pm;
	private static FeaturesPanel fp;
	private static ParametersPanel pp;
	private static Properties props;
	private static ImagePlus imgPlus;
	private static ArrayList<Species> trainingSet;
	private static boolean isIJ;
	private static ClassifierModel model;

	public Client() {
		props = FileConfig.readConfig();
		trainingSet = new ArrayList<Species>();
		pm = new MainWindow();
		fp = new FeaturesPanel();
		Prompt.SetParentComponent(pm.getDesktoPane());
		modelVersion = Double.parseDouble(props.getProperty("model.version"));
		init();
	}

	private void init() {		
		screen = new StartScreen();
		screen.setExecutable(true);
		screen.setMessage("Initializing...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		screen.setExecutable(false);
		isIJ = Prompt.chooseFeatures(true);
		pm.setVisible(true);
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
		return false;
	}
		
	@SuppressWarnings("unchecked")
	public static void onSubmit() {
		Preprocess preprocess = new Preprocess();
		preprocess.setPC(pp.getPCA());
		ArrayList<Species> dataset = preprocess.scale((ArrayList<Species>)trainingSet.clone());
		dataset = preprocess.reduceFeatures(dataset);
		
		SVM svm = new SVM();
		svm.buildModel(dataset, pp.getSVMParams());
		
		model = new ClassifierModel();
		model.setVersion(modelVersion);
		model.setCreatedDate(new Date());
		model.setPreprocessModel(preprocess.getPreprocessModel());
		model.setSvmmodel(svm.getSVMModel());
	}

	public static void getFeatures(String name) {
		try {			
			FeatureExtraction featureExtraction = new FeatureExtraction();
			featureExtraction.getShapeDescriptors(imgPlus);
			if(isIJ) {
				featureExtraction.getTextureDescriptors(imgPlus.getProcessor());
			}
			else {
				featureExtraction.getHaralickDescriptors(imgPlus.getProcessor());
			}
			
			Species s = new Species();
			s.setName(name);
			s.setFeatureLabels(featureExtraction.getFeatureLabels());
			s.setFeatureValues(featureExtraction.getFeatureValues());
			trainingSet.add(s);
			fp.refresh();
			fp.setVisible(true);
			if(pm.getFromDesktopPane("Input") == null)
				pm.addToDesktopPane(fp);
		}
		catch(Exception e) {
			Prompt.PromptError("ERROR_INPUT_FEATURES");
			printStackTrace(e);
		}
	}
	
	public static void getParameters() {
		pp = new ParametersPanel();
		pp.setVisible(true);
		pm.addToDesktopPane(pp);
	}
	
	public static void exportTrainingSet(String filename) {
		pm.appendToConsole("Exporting training set...");
		boolean dloadSuccess = FileOutput.saveToExcelFile(trainingSet, filename);
		if(dloadSuccess)
			Prompt.PromptSuccess("SUCCESS_DLOAD");
		else
			Prompt.PromptSuccess("ERROR_DLOAD");
	}
	
	public static void saveModel(String filename) {
		pm.appendToConsole("Saving classifier model...");
		boolean dloadSuccess = FileOutput.saveToDATFile(model, filename);
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
