package core;

import java.util.ArrayList;
import java.util.Hashtable;

import Data.SVMModel;
import Data.Species;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM {
	private svm_model model;
	private ArrayList<String> classes;
	private double accuracy;

	public SVM() {
		classes = new ArrayList<String>();
		model = new svm_model();
	}
	
	public SVMModel getSVMModel() {
		SVMModel svmModel = new SVMModel();
		svmModel.setModel(model);
		svmModel.setAccuracy(accuracy);
		svmModel.setClasses(classes);
		
		return svmModel;
	}

	public svm_model getModel() {
		return model;
	}
	
	public double getAccuracy() {
		return accuracy;
	}
	
	public void buildModel(Hashtable<String, Double> params, ArrayList<Species> dataset) {
		svm_parameter parameter = BuildSVMParameter(params);
		svm_problem problem = BuildSVMProblem(dataset);
		
		model = svm.svm_train(problem, parameter);
		accuracy = crossValidate(problem, parameter);
	}
	
	private svm_parameter BuildSVMParameter(Hashtable<String, Double> params) {
		svm_parameter svmParams = new svm_parameter();
		
		return svmParams;
	}
	
	private svm_problem BuildSVMProblem(ArrayList<Species> dataset) {
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

	private double crossValidate(svm_problem prob, svm_parameter params) {
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
}
