package core;

import gui.InitialWindow;
import gui.InputWindow;
import gui.OutputWindow;
import helpers.DataHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JCheckBox;

import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

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
	private static InputWindow input;

	public Client() {
		decisionTree = new DT();
		preprocess = new Preprocess();
		svm = new SVM();
		dataset = new ArrayList<Species>();
		input = new InputWindow();

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
		initial.setVisible(false);
		currentMode = mode[0];
		input.setVisible(true);
		showInputPanels();
	}

	public static void showInputPanels() {
		if(currentMode == 1) {
			input.showCard("input_dt");
		} else if(currentMode == 2) {
			input.showCard("input_svm");
		}
		
		setTextFieldText("");
	}
	
	public static void setTextFieldText(String text) {
		input.getCurrentPanel().setTextBoxText(text);
	}
	
	public static void enableButtonPanels() {
		input.enableButtonPanel();
	}
	
	public static void prev() {
		if(currentMode == mode[0]) {
			input.setVisible(false);
			initial.setVisible(true);
		}
		else {
			currentMode = mode[0];
			showInputPanels();
		}
	}

	public static void next() {
		trainClassifier();
		
		if(currentMode != mode[mode.length - 1]) {
			currentMode = mode[1];
			showInputPanels();
		}
		else {
			buildClassifierModel();
			input.setVisible(false);
			OutputWindow output = new OutputWindow(model);
			output.setVisible(true);
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
//		model.setCreatedDate(new Date());
	}

	public static boolean uploadModel() {
		try {
//			RemoteApiOptions options = new RemoteApiOptions()
//			    .server("radiss-training.appspot.com", 80);
//			    //.credentials(username, password);
//	
//			RemoteApiInstaller installer = new RemoteApiInstaller();
//			installer.install(options);
//			// ... all API calls executed remotely
//			installer.uninstall();
		
			URL url = new URL(uploadURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/xml");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();

			String modelObj = DataHelper.ConvertToJson(model);
			ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
			out.writeObject(modelObj.getBytes());
			out.flush();
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String result;
			while ((result = in.readLine()) != null) {}
			in.close();
			
			conn.disconnect();
			
			return Boolean.parseBoolean(result);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}
	
	private static void trainClassifier() {
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
