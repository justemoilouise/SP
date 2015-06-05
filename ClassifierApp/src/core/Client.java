package core;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import imageProcessing.Fast_Filters;
import imageProcessing.FeatureExtraction;
import imageProcessing.ParticleAnalysis;
import imageProcessing.Protrusions;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Properties;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.Input;
import Data.SVMResult;
import Data.Species;
import ExceptionHandlers.ExceptionHandler;
import ExceptionHandlers.ThreadException;
import FileHandlers.FileConfig;
import FileHandlers.FileOutput;
import ImageHandlers.ProcessImage;
import gui.InputPanel;
import gui.OutputPanel;
import gui.StartScreen;
import gui.MainWindow;
import gui.ProgressInfo;

public class Client {
	private static Properties props;
	private static Thread t;
	private static StartScreen screen;
	private static MainWindow pm;
	private static ProgressInfo progress;
	
	private static Preprocess preprocess;
	private static SVM svm;
	private static ClassifierModel model;

	private static ImagePlus imgPlus;
	private static ArrayList<Input> inputs;
	private static int count = 0;

	public Client() {
		inputs = new ArrayList<Input>();
		props = FileConfig.readConfig();
		preprocess = new Preprocess();
		svm = new SVM();
		pm = new MainWindow();	
		Prompt.SetParentComponent(pm.getDesktoPane());
		
		init();
	}

	private void init() {
		screen = new StartScreen();
		screen.setExecutable(true);
		
		Thread splashScreen = new Thread(screen, "SplashScreen");
		Thread init = new Thread(new Initialize(screen, props), "Initialize");

		splashScreen.setUncaughtExceptionHandler(new ThreadException());
		splashScreen.start();

		init.setUncaughtExceptionHandler(new ThreadException());
		init.start();
	}
	
	public static Properties getProperties() {
		return props;
	}
	
	public static void setImgPlus(ImagePlus img) {
		imgPlus = img;
	}
	
	public static int getCount() {
		return count;
	}
	
	public static void addInput(Input in) {
		inputs.add(in);
	}
	
	public static Input getInput(int index) {
		if(index > 0)
			return inputs.get(index-1);
		
		return inputs.get(index);
	}

	public static MainWindow getPm() {
		return pm;
	}

	public static ProgressInfo getProgress() {
		return progress;
	}

	public static Preprocess getPreprocess() {
		return preprocess;
	}

	public static void setPreprocess(Preprocess preprocess) {
		Client.preprocess = preprocess;
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

	public static void setModel(ClassifierModel model) {
		Client.model = model;
		
		//update currently used models
		if(model.isIJUsed()) {
			preprocess.setIJModel(model.getPreprocessModel());
			svm.setIJModel(model.getSvmmodel());
		}
		else {
			preprocess.setJFModel(model.getPreprocessModel());
			svm.setJFModel(model.getSvmmodel());
		}
	}

	public static boolean validateInput() {
		if(inputs.size() > 0) {
			Input input = inputs.get(inputs.size()-1);
			
			if(input.getSpecies() != null)
				return true;
		}
		if(imgPlus != null)
			return true;
		
		return false;
	}
		
	public static void onSubmit(boolean isIJ) {
		count++;
		segmentImage();
		extractFeatures(isIJ);

		progress = new ProgressInfo();
		progress.setVisible(true);
		pm.addToDesktopPane(progress);
		
		preprocess.setIJ(isIJ);
		svm.setIJ(isIJ);
		
		Input i = inputs.get(inputs.size()-1);
		Species s = i.getSpecies();
		double[] features = s.getFeatureValues();
		
		double[] preprocessedData = preprocess.scale(features);
		preprocessedData = preprocess.reduceFeatures(preprocessedData);
		ArrayList<SVMResult> results = svm.classify(preprocessedData, isIJ);			
		
		i.setSvmResult(results);
		s.setName(svm.analyzeResults(results));
		
		progress.closeProgressBar();
		displayOutput();
	}
	
	public static void stop() {
		if(t != null) {
			if(t.getState() == Thread.State.RUNNABLE) {
				t.interrupt();
				
				pm.appendToConsole(t.getName() + " interrupted.");
				progress.closeProgressBar();
			}
		}
		else {
			Prompt.PromptError("ERROR_STOP");
		}
	}

	private static void segmentImage() {
		if(imgPlus != null) {
			// get base shape
//			ImagePlus img = ProcessImage.topHatTransform(imgPlus.duplicate());
//			img.show();
			FloatProcessor fp = imgPlus.duplicate().getProcessor().convertToFloatProcessor();		
			Fast_Filters ff = new Fast_Filters();
			ff.run(fp);
			ImagePlus img = new ImagePlus(imgPlus.getTitle() + " - Top Hat", fp);
			img.show();
			
//			// isolate protrusions
			Protrusions p = new Protrusions();
			p.identifyProtrusions(imgPlus, img);
//			
//			// measure protrusions
//			p.analyzProtrusions();
			
			Input input = new Input();
			input.setSegmentation(img);
			inputs.add(input);
		}		
	}
	
	private static void extractFeatures(boolean isIJ) {
		try {
			if(imgPlus !=null) {
				Input input = inputs.get(inputs.size()-1);
				BufferedImage bi = ProcessImage.getROI(imgPlus);

				String name = "tmp/"+count+".png";
				ProcessImage.saveImage(bi, name);
				input.setImg(new ImagePlus(name));
				input.setImageName(name);
				
				FeatureExtraction featureExtraction = new FeatureExtraction();
				featureExtraction.getShapeDescriptors(imgPlus);
				
				if(isIJ) {
					featureExtraction.getTextureDescriptors(imgPlus.getProcessor());
				}
				else {
					featureExtraction.getHaralickDescriptors(imgPlus.getProcessor());
				}
				
				ParticleAnalysis pa = new ParticleAnalysis();
				pa.analyzeParticles(imgPlus);
				
				Species s = new Species();
				s.setFeatureLabels(featureExtraction.getFeatureLabels());
				s.setFeatureValues(featureExtraction.getFeatureValues());
				s.setParticleLabels(pa.getFeatureLabels());
				s.setParticleValues(pa.getFeatureValues());
				
				input.setSpecies(s);
			}
		}
		catch(Exception e) {
			Prompt.PromptError("ERROR_INPUT_FEATURES");
			printStackTrace(e);
		}
	}

	public static void displayInput() {
		InputPanel input = new InputPanel(inputs.get(inputs.size()-1));
		input.setVisible(true);
		pm.addToDesktopPane(input);
	}
	
	public static void displayOutput() {
		OutputPanel output = new OutputPanel(inputs.get(count-1));
		output.setVisible(true);
		pm.addToDesktopPane(output);
	}
	
	public static void download(int index) {
		progress = new ProgressInfo();
		progress.setVisible(true);
		pm.addToDesktopPane(progress);

		boolean dloadSuccess = FileOutput.saveToFile(inputs.get(index-1), index);
		
		if(dloadSuccess)
			Prompt.PromptSuccess("SUCCESS_DLOAD");
		else
			Prompt.PromptSuccess("ERROR_DLOAD");
	}
	
	public static void printStackTrace(Throwable ex) {
		String stackTrace = ExceptionHandler.getStackTrace(ex);
		pm.appendToErrorLog(stackTrace);
	}

	public static void setInputs(ArrayList<Input> inputList) {
		inputs = inputList;
	}
}
