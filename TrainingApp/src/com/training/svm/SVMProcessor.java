package com.training.svm;

import java.util.ArrayList;
import java.util.Hashtable;

import com.training.helpers.FileHelper;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import Data.SVMModel;
import Data.SVMResult;
import Data.Species;
import Interfaces.ISVM;

public class SVMProcessor implements ISVM {
	private SVMModel model;
	private ArrayList<String> classes;
	
	public SVMProcessor() {
		this.model = new SVMModel();
		this.classes = new ArrayList<String>();
	}

	public SVMModel getSVMModel() {
		return model;
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
		int correct = 0;
		double[] target = new double[prob.l];
		svm.svm_cross_validation(prob, params, 10, target);

		for(int i=0; i<prob.l ;i++) {
			if(target[i] == prob.y[i]) {
				correct++;
			}
		}

		return 100.0*correct/prob.l;
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
		ArrayList<Species> dataset = FileHelper.readFromFile();
		svm_problem prob = new svm_problem();
		
		int dataCount = dataset.size();
		prob.y = new double[dataCount];
		prob.l = dataCount;
		prob.x = new svm_node[dataCount][];

		double classNameCtr = 0;
		String className = "";

		for(int i=0; i<dataCount; i++) {
			Species s = dataset.get(i);
			double[] features = s.getFeatureValues();
			prob.x[i] = new svm_node[features.length];
			for (int j=0; j<features.length; j++){
				svm_node node = new svm_node();
				node.index = j;
				node.value = features[j];

				prob.x[i][j] = node;
			}

			if(!className.equals(s.getName())) {
				className = s.getName();
				classes.add(className);
				classNameCtr++;
			}
			prob.y[i] = classNameCtr;
		}
		
		return prob;
	}
}
