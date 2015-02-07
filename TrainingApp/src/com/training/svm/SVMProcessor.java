package com.training.svm;

import java.util.ArrayList;
import java.util.Hashtable;

import com.training.helpers.FileHelper;

import libsvm.svm;
import libsvm.svm_model;
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
		svm_parameter params = BuildSVMParameters();
		svm_problem prob = BuildSVMProblem();
		
		svm_model svmModel = svm.svm_train(prob, params);		
		double accuracy = crossValidate(params, prob);
		
		//save to SVMModel
		model.setModel(svmModel);
		model.setAccuracy(accuracy);
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

	private svm_parameter BuildSVMParameters() {
		//read parameters from web.xml
		Hashtable<String, String> params = FileHelper.readFromXML("svm-parameters");
		
		svm_parameter svmParams = new svm_parameter();
		svmParams.eps = Double.parseDouble(params.get("epsilon"));
		svmParams.kernel_type = Integer.parseInt(params.get("kernel"));
		svmParams.svm_type = Integer.parseInt(params.get("type"));
		svmParams.coef0 = Double.parseDouble(params.get("coef"));
		svmParams.nu = Double.parseDouble(params.get("nu"));
		svmParams.C = Double.parseDouble(params.get("cost"));
		svmParams.gamma = Double.parseDouble(params.get("gamma"));
		svmParams.degree = Integer.parseInt(params.get("degree"));
		
		return svmParams;
	}
	
	private svm_problem BuildSVMProblem() {
		//read from temporary file - input
		svm_problem prob = new svm_problem();
		
		return prob;
	}
}
