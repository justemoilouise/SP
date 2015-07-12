package core;

import gui.InitialWindow;
import gui.MainWindow;
import gui.OutputPanel;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JCheckBox;

import Data.ClassifierModel;
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

	public static int[] getMode() {
		return mode;
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

	public static void next() {
		if(currentMode != mode[mode.length - 1]) {
			currentMode = mode[1];
			showInputPanels();
		}
		else {
			OutputPanel output = new OutputPanel(mode);
			main.addOutputPanel(output);
		}
	}

	public static void setDataset(ArrayList<Species> dataset) {
		Client.dataset = dataset;
	}

	public static ClassifierModel getClassifierModel() {
		ClassifierModel model = new ClassifierModel();
		model.setDecisionTreeModel(decisionTree.getModel());
		model.setPreprocessModel(preprocess.getPreprocessModel());
		model.setSvmmodel(svm.getSVMModel());
		model.setCreatedDate(new Date());
		
		return model;
	}
	
	public static void trainClassifier() {
		if(currentMode == 1) {
			trainDecisionTree();
		} else if(currentMode == 2) {
			trainSVM();
		}
		
		next();
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
