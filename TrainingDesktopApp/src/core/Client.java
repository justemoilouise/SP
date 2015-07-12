package core;

import gui.InitialWindow;
import gui.MainWindow;

import java.util.ArrayList;

import javax.swing.JCheckBox;

import Data.Species;

public class Client {
	private static DT decisionTree;
	private static Preprocess preprocess;
	private static SVM svm;
	
	private static ArrayList<Species> dataset;
	private static int[] mode;
	private static int currentMode;
	
	private static InitialWindow initial;
	private static MainWindow main;
	
	public Client() {
		decisionTree = new DT();
		preprocess = new Preprocess();
		svm = new SVM();
		dataset = new ArrayList<Species>();
		main = new MainWindow();
		
		initial = new InitialWindow();
		initial.setVisible(true);
	}
	
	public static JCheckBox[] getModes() {
		return initial.getCheckBoxes();
	}
	
	public static void setMode(int[] mode) {
		Client.mode =  mode;
	}
	
	public static void showMainWindow() {
		currentMode = mode[0];
		main.setVisible(true);
		showInputPanels();
	}
	
	public static void showInputPanels() {
		if(currentMode == 1) {
			main.showCard("DT");
		} else if(currentMode == 2) {
			main.showCard("SVM");
		}
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
		decisionTree.setDataSet(dataset);
		decisionTree.run();
	}
	
	private static void trainSVM() {
		ArrayList<Species> ds = preprocess.scale(dataset);
		ds = preprocess.reduceFeatures(ds);
		
		svm.buildModel(ds, null);
	}
}
