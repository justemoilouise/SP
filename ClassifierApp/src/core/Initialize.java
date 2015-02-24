package core;

import java.util.Hashtable;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Data.ClassifierModel;
import Data.PreprocessModel;
import Data.SVMModel;
import FileHandlers.FileInput;
import FileHandlers.FileOutput;
import gui.StartScreen;

public class Initialize implements Runnable {
	private StartScreen screen;
	private Properties props;

	public Initialize(StartScreen screen, Properties props) {
		this.screen = screen;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Hashtable<String, Object> models = readModels();
		init_Preprocess((PreprocessModel)models.get("preprocess_model_IJ"), (PreprocessModel)models.get("preprocess_model_JF"));
		init_SVM((SVMModel)models.get("svm_model_IJ"), (SVMModel)models.get("svm_model_JF"));

		screen.setExecutable(false);
	}

	private void init_Preprocess(PreprocessModel IJmodel, PreprocessModel JFmodel) {
		screen.setMessage("Initializing Preprocessing model..");
		long startTime = System.currentTimeMillis();
		
		Preprocess preprocess = Client.getPreprocess();
		preprocess.setIJModel(IJmodel);
		preprocess.setJFModel(JFmodel);
		
		Client.getPm().appendToConsole("Preprocessing took " + (System.currentTimeMillis()-startTime) + " ms to initialize..");
		Client.setPreprocess(preprocess);
	}

	private void init_SVM(SVMModel IJmodel, SVMModel JFmodel) {
		screen.setMessage("Initializing SVM model..");
		long startTime = System.currentTimeMillis();
		
		SVM svm = Client.getSvm();
		svm.setIJModel(IJmodel);
		svm.setJFModel(JFmodel);
		
		Client.getPm().appendToConsole("SVM took " + (System.currentTimeMillis()-startTime) + " ms to initialize..");
		Client.setSvm(svm);
	}

	private Hashtable<String, Object> readModels() {
		screen.setMessage("Retrieving models..");
		
		boolean isModelUsed = Boolean.getBoolean(props.getProperty("model.used"));
		
		if(!isModelUsed) {
			DecompressModel();
		}
		
		String filename = props.getProperty("");
		FileInput.readModelFromDATFile(filename);
		
		return null;
	}
	
	@SuppressWarnings("resource")
	private void DecompressModel() {
		String zipPath = props.getProperty("models/Classifier.zip");
		
		try {
			ZipInputStream stream = new ZipInputStream(new FileInputStream(zipPath));
			ZipEntry entry = stream.getNextEntry();
			
			while(entry != null) {
				String filename = entry.getName();
				
				ClassifierModel model = FileInput.readModelFromDATFile(filename);
				FileOutput.saveToFile(model, model.isIJUsed());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
