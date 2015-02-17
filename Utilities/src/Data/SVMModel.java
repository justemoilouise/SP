package Data;

import java.util.ArrayList;

import libsvm.svm_model;

public class SVMModel {
	private svm_model model;
	private double accuracy;
	private ArrayList<String> classes;
	
	public SVMModel() {
		this.classes = new ArrayList<String>();
	}

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

	public ArrayList<String> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<String> classes) {
		this.classes = classes;
	}
}
