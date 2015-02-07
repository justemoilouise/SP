package com.training.svm;

import java.util.ArrayList;

import libsvm.svm_parameter;
import libsvm.svm_problem;
import Data.SVMModel;
import Data.SVMResult;
import Interfaces.ISVM;

public class SVMProcessor implements ISVM {
	private SVMModel model;
	
	public SVMProcessor() {
		this.model = new SVMModel();
	}

	@Override
	public void buildModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<SVMResult> classify(double[] features) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double crossValidate(svm_parameter params, svm_problem prob) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAccuracy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}
}
