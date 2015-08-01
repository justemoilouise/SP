package core;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import Data.ClassifierModel;
import Data.DecisionTreeModel;
import Data.PreprocessModel;
import Data.SVMModel;
import FileHandlers.FileConfig;
import FileHandlers.FileInput;
import FileHandlers.FileOutput;
import gui.StartScreen;

public class Initialize implements Runnable {
	private StartScreen screen;
	private Properties props;

	public Initialize(StartScreen screen, Properties props) {
		this.screen = screen;
		this.props = props;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Hashtable<String, Object> models = readModels();

		if(models != null) {
			init_Preprocess((PreprocessModel)models.get("preprocess_model_IJ"), (PreprocessModel)models.get("preprocess_model_JF"));
			init_SVM((SVMModel)models.get("svm_model_IJ"), (SVMModel)models.get("svm_model_JF"));

			screen.setExecutable(false);
		}
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

		boolean isModelUsed = Boolean.parseBoolean(props.getProperty("model.used"));
		if(!isModelUsed) {			
			DecompressModel();
		}

		Hashtable<String, Object> models = new Hashtable<String, Object>();

		// Preprocess models
		String filename = props.getProperty("model.preprocess.ij");
		PreprocessModel pModel = FileInput.readPreprocessModelFromDATFile(filename);
		models.put("preprocess_model_IJ", pModel);
		filename = props.getProperty("model.preprocess.jf");
		pModel = FileInput.readPreprocessModelFromDATFile(filename);
		models.put("preprocess_model_JF", pModel);

		// SVM models
		filename = props.getProperty("model.svm.ij");
		SVMModel sModel = FileInput.readSVMModelFromDATFile(filename);
		models.put("svm_model_IJ", sModel);
		filename = props.getProperty("model.svm.ij");
		sModel = FileInput.readSVMModelFromDATFile(filename);
		models.put("svm_model_JF", sModel);
		
		// Decision tree model
		filename = props.getProperty("model.decisionTree");
		DecisionTreeModel dModel = FileInput.readDeisionTreeModelFromDATFile(filename);
		models.put("dt_model", dModel);

		return models;
	}

	private void DecompressModel() {
		String zipPath = props.getProperty("model.classifier.zip");
		ClassifierModel model = null;

		try {
			ZipFile zipFile = new ZipFile(zipPath);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = "tmp/" + zipEntry.getName();

				File file = new File(name);
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

				model = FileInput.readModelFromDATFile(name);
				FileOutput.saveToFile(model, model.isIJUsed());
			}
			zipFile.close();
			FileConfig.updateModelInfo(model);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
