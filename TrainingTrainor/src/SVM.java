import java.util.ArrayList;
import java.util.Iterator;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import Data.SVMModel;
import Data.SVMParameter;
import Data.SVMResult;
import Data.Species;

public class SVM {
	private SVMModel svmModel;
	private ArrayList<String> classes;
	private svm_model model;
	private double accuracy;
	
	public SVM() {
		this.svmModel = new SVMModel();
	}
	
	public SVMModel getModel() {
		return svmModel;
	}
	
//	public void buildModel(ArrayList<Species> dataset, SVMParameter svmParameter) {
//		// TODO Auto-generated method stub
//		this.classes = new ArrayList<String>();
//		
//		svm_parameter params = BuildSVMParameters(svmParameter);
//		svm_problem prob = BuildSVMProblem(dataset);
//		
//		model = svm.svm_train(prob, params);		
//	}
	
	public double buildModel(ArrayList<Species> dataset, SVMParameter svmParameter) {
		// TODO Auto-generated method stub
		this.classes = new ArrayList<String>();
		svmModel.setClasses(classes);
		
		svm_parameter params = BuildSVMParameters(svmParameter);
		svm_problem prob = BuildSVMProblem(dataset);
		
		model = svm.svm_train(prob, params);
		svmModel.setModel(model);
		accuracy = crossValidate(params, prob);
		svmModel.setAccuracy(accuracy);
		
		return accuracy;
	}

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
	
	public String classify(double[] features) {
		svm_node[] nodes = new svm_node[features.length];

		for(int i=0; i<features.length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = features[i];

			nodes[i] = node;
		}

		double proby[] = new double[svm.svm_get_nr_class(model)];
		svm.svm_predict_probability(model, nodes, proby);
		ArrayList<SVMResult> svmResults = saveResults(proby);

		return analyzeResults(svmResults);
	}
	
	private ArrayList<SVMResult> saveResults(double[] proby) {
		ArrayList<SVMResult> results = new ArrayList<SVMResult>();
		
		for(int i=0; i<proby.length; i++) {
			SVMResult result = new SVMResult();
			result.setName(classes.get(i));
			result.setProbability(proby[i]*100);
			
			results.add(result);
		}
		
		return results;
	}
	
	private String analyzeResults(ArrayList<SVMResult> results) {
		String prediction = "Unknown";
		double max = 0;
		
		Iterator<SVMResult> iter = results.iterator();
		while(iter.hasNext()) {
			SVMResult result = iter.next();
			
			if((result.getProbability() > 0.5) && (result.getProbability() > max)) {
				prediction = result.getName();
			}
		}
		
		return prediction;
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
