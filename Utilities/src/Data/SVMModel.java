package Data;

import libsvm.svm_model;

public class SVMModel {
	private svm_model model;
	private double accuracy;
	
	public SVMModel() {}

	public svm_model getModel() {
		return model;
	}

	public void setModel(svm_model model) {
		this.model = model;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
}
