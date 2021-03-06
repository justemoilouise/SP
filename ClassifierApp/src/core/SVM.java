package core;

import java.util.ArrayList;
import java.util.Properties;

import Data.SVMModel;
import Data.SVMParameter;
import Data.SVMResult;
import Data.Species;
import Interfaces.ISVM;
import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM implements ISVM {
	private SVMModel IJModel, JFModel, model;
	private boolean isIJ;
	public double threshold;

	public SVM() {}

	public void setThreshold(Properties props) {
		this.threshold = Double.parseDouble(props.getProperty("model.threshold"));
	}
	
	public boolean isIJ() {
		return isIJ;
	}

	public void setIJ(boolean isIJ) {
		this.isIJ = isIJ;
		if(isIJ)
			model = IJModel;
		else
			model = JFModel;
	}

	public SVMModel getIJModel() {
		return IJModel;
	}

	public void setIJModel(SVMModel iJModel) {
		IJModel = iJModel;
	}

	public SVMModel getJFModel() {
		return JFModel;
	}

	public void setJFModel(SVMModel jFModel) {
		JFModel = jFModel;
	}
	
	public SVMModel getModel() {
		return model;
	}

	@Override
	public ArrayList<SVMResult> classify(double[] features, boolean isIJused) {
		// TODO Auto-generated method stub
		svm_node[] nodes = new svm_node[features.length];

		for(int i=0; i<features.length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = features[i];

			nodes[i] = node;
		}

		double proby[] = new double[svm.svm_get_nr_class(model.getModel())];
		svm.svm_predict_probability(model.getModel(), nodes, proby);

		return saveResults(proby);
	}
	
	public ArrayList<SVMResult> classify(double[] features) {
		// TODO Auto-generated method stub

		svm_node[] nodes = new svm_node[features.length];

		for(int i=0; i<features.length; i++) {
			svm_node node = new svm_node();
			node.index = i;
			node.value = features[i];

			nodes[i] = node;
		}
		
		if(isIJ)
			model = IJModel;
		else
			model = JFModel;

		double proby[] = new double[svm.svm_get_nr_class(model.getModel())];
		svm.svm_predict_probability(model.getModel(), nodes, proby);

		return saveResults(proby);
	}

	@Override
	public double crossValidate(svm_parameter param, svm_problem prob) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getAccuracy(boolean isIJ) {
		if(model != null)
			return model.getAccuracy();
		else {
			if(isIJ)
				return IJModel.getAccuracy();
			else
				return JFModel.getAccuracy();
		}
	}

	public String analyzeResults(ArrayList<SVMResult> results) {
		String prediction = "Unknown";
		double max = 0;

		for(SVMResult result : results) {
			if((result.getProbability() > threshold) && (result.getProbability() > max)) {
				max = result.getProbability();
				prediction = result.getName();
			}
		}
		
		return prediction;
	}
	
	private ArrayList<SVMResult> saveResults(double[] proby) {
		ArrayList<SVMResult> results = new ArrayList<SVMResult>();
		
		for(int i=0; i<proby.length; i++) {
			SVMResult result = new SVMResult();
			result.setName(model.getClasses().get(i));
			result.setProbability(proby[i]*100);
			
			results.add(result);
		}
		
		return results;
	}
	
	@Override
	public void buildModel(ArrayList<Species> arg0, SVMParameter arg1) {
		// TODO Auto-generated method stub
		
	}
}
