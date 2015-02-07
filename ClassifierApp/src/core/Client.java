package core;

import ij.ImagePlus;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Properties;

import CoreHandler.Prompt;
import Data.Input;
import Data.Species;
import ExceptionHandlers.ExceptionHandler;
import ExceptionHandlers.ThreadException;
import FileHandlers.FileConfig;
import FileHandlers.FileOutput;
import ImageHandlers.ProcessImage;
import featureExtraction.ImageJ;
import featureExtraction.JFeature;
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

	private static SVM svm;

	private static ImagePlus imgPlus;
	private static ArrayList<Input> inputs;
	private static int count = 0;

	public Client() {
		inputs = new ArrayList<Input>();
		props = FileConfig.readConfig();
		svm = new SVM();
		pm = new MainWindow();	
		Prompt.SetParentComponent(pm.getDesktoPane());
		
		init();
	}

	private void init() {
		screen = new StartScreen();
		screen.setExecutable(true);
		
		Thread splashScreen = new Thread(screen, "SplashScreen");
		Thread init = new Thread(new Initialize(screen), "Initialize");

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

	public static SVM getSvm() {
		return svm;
	}

	public static void setSvm(SVM svm) {
		Client.svm = svm;
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
		getInputFeatures(isIJ);

		progress = new ProgressInfo();
		progress.setVisible(true);
		pm.addToDesktopPane(progress);
		
		svm.setIsIJ(isIJ);		
		
		t = new Thread(svm, "SVM");
		t.setUncaughtExceptionHandler(new ThreadException());
		t.start();
		
		try {
			t.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Prompt.PromptError("ERROR_DLOAD");
			printStackTrace(e);
		}
		finally {
			progress.closeProgressBar();
			displayOutput();
		}
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
		
	private static void getInputFeatures(boolean isIJ) {
		try {
			Input input = new Input();
			
			if(imgPlus !=null) {
				BufferedImage bi = ProcessImage.getROI(imgPlus);

				String name = "img/"+count+".png";
				ProcessImage.saveImage(bi, name);
				input.setImg(new ImagePlus(name));
				input.setImageName(name);

				Species s = new Species();
				ImagePlus imgPlus = new ImagePlus(name, bi);
				
				if(isIJ) {
					ImageJ ij = new ImageJ();
					ij.measure(imgPlus);
					ij.getTextureFeatures(imgPlus.getProcessor());
					
					s.setFeatureLabels(ij.getFeatureLabels());
					s.setFeatureValues(ij.getFeatureValues());
				}
				else {
					JFeature jf = new JFeature();
					jf.getHaralickDescriptor(imgPlus.getProcessor());
					
					s.setFeatureLabels(jf.getFeatureLabels());
					s.setFeatureValues(jf.getFeatureValues());
				}
				input.setSpecies(s);
				inputs.add(input);
			}
			else {
				input = inputs.get(inputs.size()-1);
			}
			svm.setInput(input.getSpecies().getFeatureValues());
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
		
		FileOutput fo = new FileOutput();
		fo.saveToFile(inputs.get(index-1), index);
		Thread output = new Thread(fo);
		output.start();
		try {
			output.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Prompt.PromptError("ERROR_DLOAD");
			printStackTrace(e);
		}
		finally {
			Prompt.PromptSuccess("SUCCESS_DLOAD");
		}
	}
	
	public static void printStackTrace(Throwable ex) {
		String stackTrace = ExceptionHandler.getStackTrace(ex);
		pm.appendToErrorLog(stackTrace);
	}
}
