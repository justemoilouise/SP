package core;

import java.util.ArrayList;
import java.util.Hashtable;

import Data.SVMResult;
import Interfaces.ISVM;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM implements ISVM {
	private svm_model model;
	private PreProcess pp;
	private ArrayList<String> classes;
	private Hashtable<String, Double> result;
	private double accuracy;

	public SVM() {
		classes = new ArrayList<String>();
		result = new Hashtable<String, Double>();
		model = new svm_model();
	}

	public void init_SVM(boolean isIJ) {
		//read model files from resources
	}

	public svm_model getModel() {
		return model;
	}

	@Override
	public double crossValidate(svm_parameter param, svm_problem prob) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getAccuracy() {
		return accuracy;
	}

	@Override
	public void buildModel(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<SVMResult> classify(double[] arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}
