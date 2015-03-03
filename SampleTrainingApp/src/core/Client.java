package core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import CoreHandler.Prompt;
import Data.Species;
import ExceptionHandlers.ExceptionHandler;
import FileHandlers.FileConfig;
import gui.MainWindow;
import gui.OutputPanel;

public class Client {
	private static Properties props;
	private static MainWindow pm;

	private static SVM svm;

	private static ArrayList<Species> dataset;

	public Client() {
		props = FileConfig.readConfig();
		svm = new SVM();
		pm = new MainWindow();	
		Prompt.SetParentComponent(pm.getDesktoPane());
	}
	
	public static Properties getProperties() {
		return props;
	}

	public static MainWindow getPm() {
		return pm;
	}

	public static SVM getSvm() {
		return svm;
	}

	public static void setSvm(SVM svm) {
		Client.svm = svm;
	}
	
	public static void setDataset(ArrayList<Species> dataset) {
		Client.dataset = dataset;
	}
		
	public static void onSubmit(Hashtable<String, Double> params) {
		svm.buildModel(params, dataset);		
		
		displayOutput();
	}
	
	public static void displayOutput() {
		OutputPanel output = new OutputPanel();
		output.setVisible(true);
		pm.addToDesktopPane(output);
	}
	
	public static void download(int index) {		
		//FileOutput.saveToFile(inputs.get(index-1), index);
	}
	
	public static void printStackTrace(Throwable ex) {
		ExceptionHandler.getStackTrace(ex);
	}
}
