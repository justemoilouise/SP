package classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map.Entry;

import Data.SVMResult;
import Interfaces.ISVM;
import preprocess.FeatureSelection_PCATransform;
import preprocess.Preprocess_Scale;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM implements ISVM {
	private svm_model model;
	private Preprocess_Scale pp;
	private FeatureSelection_PCATransform pca;
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
	public void buildModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<SVMResult> classify(double[] features) {
		// TODO Auto-generated method stub
		double[][] f = new double[1][];
		f[0] = pp.scale(features);
		f[0] = pca.getReducedFeatures(f[0]);

		svm_node[] nodes = new svm_node[f[0].length];

		for(int i=0; i<f[0].length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = f[0][i];

			nodes[i] = node;
		}

		double proby[] = new double[svm.svm_get_nr_class(model)]; 
		svm.svm_predict_probability(model, nodes, proby);

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
		return accuracy;
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
