package core;

import java.util.ArrayList;

import core.Preprocess;
import Data.SVMModel;
import Data.SVMResult;
import Interfaces.ISVM;
import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM extends Thread implements ISVM {
	private SVMModel model;
	private Preprocess preprocess;

	public SVM() {}
	
	public SVM(SVMModel model) {
		this.model = model;
	}

	public void init_SVM(boolean isIJ) {
		//read model files from resources
	}

	@Override
	public void buildModel(boolean isIJused) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<SVMResult> classify(double[] features, boolean isIJused) {
		// TODO Auto-generated method stub
		double[][] f = new double[1][];
		f[0] = preprocess.scale(features);
		f[0] = preprocess.reduceFeatures(f[0]);

		svm_node[] nodes = new svm_node[f[0].length];

		for(int i=0; i<f[0].length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = f[0][i];

			nodes[i] = node;
		}

		double proby[] = new double[svm.svm_get_nr_class(model.getModel())]; 
		svm.svm_predict_probability(model.getModel(), nodes, proby);

		return saveResults(proby);
	}

	@Override
	public double crossValidate(svm_parameter param, svm_problem prob) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getAccuracy() {
		return model.getAccuracy();
	}

	private ArrayList<SVMResult> saveResults(double[] proby) {
		ArrayList<SVMResult> results = new ArrayList<SVMResult>();
		
		for(int i=0; i<proby.length; i++) {
			SVMResult result = new SVMResult();
			result.setName("");
			result.setProbability(proby[i]);
			
			results.add(result);
		}
		
		return results;
	}
}
