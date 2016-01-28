package core;

import java.util.ArrayList;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import Data.SVMModel;
import Data.SVMParameter;
import Data.SVMResult;
import Data.Species;
import Interfaces.ISVM;

public class SVM implements ISVM {
	private SVMModel model;
	private ArrayList<String> classes;
	
	public SVM() {}

	public SVMModel getSVMModel() {
		return model;
	}
	
	public void buildModel(ArrayList<Species> dataset, SVMParameter svmParameter) {
		// TODO Auto-generated method stub
		this.model = new SVMModel();
		this.classes = new ArrayList<String>();
		
		svm_parameter params = BuildSVMParameters(svmParameter);
		svm_problem prob = BuildSVMProblem(dataset);
		
		svm_model svmModel = svm.svm_train(prob, params);		
		double accuracy = crossValidate(params, prob);
		
		//save to SVMModel
		model.setClasses(classes);
		model.setModel(svmModel);
		model.setAccuracy(accuracy);
	}

	@Override
	public ArrayList<SVMResult> classify(double[] features, boolean isIJUsed) {
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

	private svm_parameter BuildSVMParameters(SVMParameter params) {		
		svm_parameter svmParams = new svm_parameter();
		svmParams.eps = params.getEpsilon();
		svmParams.kernel_type = params.getKernel();
		svmParams.svm_type = params.getSvmType();
		svmParams.coef0 = params.getCoefficient();
		svmParams.nu = params.getNu();
		svmParams.C = params.getCost();
		svmParams.gamma = params.getGamma();
		svmParams.degree = params.getDegree();
		svmParams.probability = 1;
		
		return svmParams;
	}
	
	private svm_problem BuildSVMProblem(ArrayList<Species> dataset) {
		svm_problem prob = new svm_problem();
		
		int dataCount = dataset.size();
		prob.y = new double[dataCount];
		prob.l = dataCount;
		prob.x = new svm_node[dataCount][];

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

			if(classes.size() == 0 || !classes.contains(s.getName())) {
				classes.add(s.getName());
			}
			
			prob.y[i] = classes.indexOf(s.getName());
		}
		
		return prob;
	}
}
