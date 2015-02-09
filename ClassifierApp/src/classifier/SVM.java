package classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;

import Data.SVMModel;
import Data.SVMResult;
import Interfaces.ISVM;
import preprocess.Preprocess_Scale;
import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM implements ISVM {
	private SVMModel model;
	private Preprocess_Scale pp;
	private ArrayList<String> classes;
	private Hashtable<String, Double> result;

	public SVM() {
		classes = new ArrayList<String>();
		result = new Hashtable<String, Double>();
	}
	
	public SVM(SVMModel model) {
		this.model = model;
	}

	public void init_SVM(boolean isIJ) {
		//read model files from resources
	}

	@Override
	public void buildModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<SVMResult> classify(double[] features) {
		// TODO Auto-generated method stub
		double[][] f = new double[1][];
		f[0] = pp.scale(features);
//		f[0] = pca.getReducedFeatures(f[0]);

		svm_node[] nodes = new svm_node[f[0].length];

		for(int i=0; i<f[0].length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = f[0][i];

			nodes[i] = node;
		}

		double proby[] = new double[svm.svm_get_nr_class(model.getModel())]; 
		svm.svm_predict_probability(model.getModel(), nodes, proby);

		saveResults(proby);
		
		return null;
	}

	@Override
	public double crossValidate(svm_parameter param, svm_problem prob) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAccuracy() {
		return model.getAccuracy();
	}

	private void saveResults(double[] proby) {
		for(int i=0; i<proby.length; i++) {
			result.put(classes.get(i), proby[i]*100);
		}
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		Object[] proby = result.values().toArray();
		Arrays.sort(proby);

		for(Entry<String, Double> e : result.entrySet()) {
			if(e.getValue() == proby[proby.length-1])
				return e.getKey().toString();
		}

		return null;
	}
}
