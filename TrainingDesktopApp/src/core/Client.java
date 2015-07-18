package core;

import gui.InitialWindow;
import gui.MainWindow;
import gui.OutputPanel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JCheckBox;

import Data.ClassifierModel;
import Data.Species;

public class Client {
	private final static String uploadURL = "http://radiss-training.appspot.com/trainingapp/saveclassifiermodel";

	private static DT decisionTree;
	private static Preprocess preprocess;
	private static SVM svm;
	private static ClassifierModel model;

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
		initial.dispose();
		currentMode = mode[0];
		main.setVisible(true);
		showInputPanels();
	}

	public static void showInputPanels() {
		if(currentMode == 1) {
			main.showCard("input_dt");
		} else if(currentMode == 2) {
			main.showCard("input_svm");
		}
	}
	
	public static void enableButtonPanels() {
		main.enableButtonPanel();
	}

	public static void next() {
		if(currentMode != mode[mode.length - 1]) {
			currentMode = mode[1];
			showInputPanels();
		}
		else {
			buildClassifierModel();
			OutputPanel output = new OutputPanel(model);
			main.addOutputPanel(output);
		}
	}

	public static void setDataset(ArrayList<Species> dataset) {
		Client.dataset = dataset;
	}

	public static void buildClassifierModel() {
		model = new ClassifierModel();
		model.setDecisionTreeModel(decisionTree.getModel());
		model.setPreprocessModel(preprocess.getPreprocessModel());
		model.setSvmmodel(svm.getSVMModel());
		model.setCreatedDate(new Date());
	}

	public static boolean uploadModel() {
		try {
			URL url = new URL(uploadURL);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);

			ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
			out.writeObject(model);
			out.flush();
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result;
			while ((result = in.readLine()) != null) {}
			in.close();
			
			return Boolean.parseBoolean(result);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return false;
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
