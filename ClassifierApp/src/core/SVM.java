package core;

import java.util.ArrayList;

import Data.SVMModel;
import Data.SVMResult;
import Interfaces.ISVM;
import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class SVM implements ISVM {
	private SVMModel IJModel, JFModel, model;
	private boolean isIJ;

	public SVM() {}

	public boolean isIJ() {
		return isIJ;
	}

	public void setIJ(boolean isIJ) {
		this.isIJ = isIJ;
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

	@Override
	public void buildModel(boolean isIJused) {
		// TODO Auto-generated method stub
		
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
		
		if(isIJused)
			model = IJModel;
		else
			model = JFModel;

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

	private ArrayList<SVMResult> saveResults(double[] proby) {
		ArrayList<SVMResult> results = new ArrayList<SVMResult>();
		
		for(int i=0; i<proby.length; i++) {
			SVMResult result = new SVMResult();
			result.setName(model.getClasses().get(i));
			result.setProbability(proby[i]);
			
			results.add(result);
		}
		
		return results;
	}
}
