package core;

import gui.MainWindow;

import java.util.ArrayList;

import Data.Species;

public class Client {
	private static DT decisionTree;
	private static Preprocess preprocess;
	private static SVM svm;
	
	private static ArrayList<Species> dataset;
	private static int[] mode;
	private static int currentMode;
	
	private static MainWindow main;
	
	public Client() {
		decisionTree = new DT();
		preprocess = new Preprocess();
		svm = new SVM();
		dataset = new ArrayList<Species>();
		main = new MainWindow();
	}
	
	public static void setMode(int[] mode) {
		Client.mode =  mode;
	}
	
	public static void setDataset(ArrayList<Species> dataset) {
		Client.dataset = dataset;
	}
	
	public static void trainClassifier() {
		if(currentMode == 1) {
			trainDecisionTree();
		} else if(currentMode == 2) {
			trainSVM();
		}
	}
	
	private static void trainDecisionTree() {
		decisionTree.crossValidate(dataset);
	}
	
	private static void trainSVM() {
		ArrayList<Species> ds = preprocess.scale(dataset);
		ds = preprocess.reduceFeatures(ds);
		
		svm.buildModel(ds, null);
	}
}
