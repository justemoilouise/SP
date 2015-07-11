package core;

import java.util.ArrayList;

import Data.Species;

public class Client {
	private DT decisionTree;
	private Preprocess preprocess;
	private SVM svm;
	
	private ArrayList<Species> dataset;
	
	public Client() {
		this.decisionTree = new DT();
		this.preprocess = new Preprocess();
		this.svm = new SVM();
		this.dataset = new ArrayList<Species>();
	}
}
