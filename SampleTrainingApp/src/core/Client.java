package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import core.Prompt;
import Data.ClassifierModel;
import Data.Species;
import ExceptionHandlers.ExceptionHandler;
import FileHandlers.FileOutput;
import gui.InputPanel;
import gui.MainWindow;

public class Client {
	private static MainWindow pm;
	private static InputPanel ip;

	private static ClassifierModel model;
	private static PreProcess preprocess;
	private static SVM svm;

	private static ArrayList<Species> dataset;

	public Client() {
		preprocess = new PreProcess();
		svm = new SVM();
		pm = new MainWindow();	
		Prompt.SetParentComponent(pm.getDesktoPane());
		
		pm.setVisible(true);
	}

	public static MainWindow getPm() {
		return pm;
	}

	public static InputPanel getIp() {
		return ip;
	}
	
	public static SVM getSvm() {
		return svm;
	}

	public static void setSvm(SVM svm) {
		Client.svm = svm;
	}
	
	public static ClassifierModel getModel() {
		return model;
	}
	
	public static void setDataset(ArrayList<Species> dataset) {
		Client.dataset = dataset;
	}
		
	public static void onSubmit(Hashtable<String, Double> params) {
		ArrayList<Species> pDataset = preprocess.scale(dataset);
		pDataset = preprocess.reduceFeatures(pDataset);
		svm.buildModel(params, pDataset);		
		
		model = new ClassifierModel();
		model.setCreatedDate(new Date());
		model.setIJUsed(true);
		model.setPreprocessModel(preprocess.getModel());
		model.setSvmmodel(svm.getSVMModel());
		
		FileOutput.saveToDATFile(model);
	}
	
	public static void displayInput() {
		ip = new InputPanel();
		pm.addToDesktopPane(ip);
	}
	
	public static void download(int index) {		
		//FileOutput.saveToFile(inputs.get(index-1), index);
	}
	
	public static void printStackTrace(Throwable ex) {
		ExceptionHandler.getStackTrace(ex);
	}
}
