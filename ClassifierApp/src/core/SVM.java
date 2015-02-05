package core;

import java.util.Hashtable;

import libsvm.svm_model;
import classifier.SVM_IJ;
import classifier.SVM_JFeature;

public class SVM implements Runnable {
	private SVM_IJ svmIJ;
	private SVM_JFeature svmJF;
	
	private boolean isIJ;
	private double[] features;
	private Hashtable<String, Double> result;
	
	public SVM() {
		this.isIJ = true;
		this.result = new Hashtable<String, Double>();
				
		svmIJ = new SVM_IJ();
		svmJF = new SVM_JFeature();
	}
	
	public void setIsIJ(boolean isIJ) {
		this.isIJ = isIJ;
	}

	public void init_SVM() {
		svmIJ.init_SVM(true);
		svmJF.init_SVM(false);
	}
	
	public void setInput(double[] features) {
		this.features = features;
	}
	
	public svm_model getModel() {
		if(isIJ)
			return svmIJ.getModel();
		return svmJF.getModel();
	}
	
	public double getAccuracy() {
		if(isIJ)
			return svmIJ.getAccuracy();
		return svmJF.getAccuracy();
	}
	
	public String classify() {		
		if(isIJ) {
			svmIJ.classify(features);
			
			this.result = svmIJ.getResults();
			return svmIJ.getClassName();
		}
		else {
			svmJF.classify(features);
			
			this.result = svmJF.getResults();
			return svmJF.getClassName();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		Client.getPm().appendToConsole("SVM classification started..");
		String className = classify();
		Client.getInput(Client.getCount()).getSpecies().setName(className);
		Client.getInput(Client.getCount()).setSvmResult(result);
		Client.getPm().appendToConsole("SVM classification took " + (System.currentTimeMillis()-startTime) + " ms..");
	}
}
