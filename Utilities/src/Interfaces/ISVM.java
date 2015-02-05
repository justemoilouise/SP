package Interfaces;

import java.util.Hashtable;

import libsvm.svm_parameter;
import libsvm.svm_problem;

public interface ISVM {

	/**
	 * Builds SVM model
	 */
	public void buildModel();
	
	/**
	 * Cross validates model
	 * 
	 * @param SVM parameters used in building model
	 * @param SVM problem inputs
	 * 
	 * @return accuracy of the model
	 */
	public double crossValidate(svm_parameter param, svm_problem prob);

	/**
	 * Classifies the given data
	 * 
	 * @param features
	 */
	public void classify(double[] features);
	
	/**
	 * Simply retrieves the accuracy of the generated model
	 * 
	 * @return accuracy
	 */
	public double getAccuracy();
	
	/**
	 * Returns the classification name
	 * 
	 * @return class name
	 */
	public String getClassName();

	/**
	 * Returns table of results
	 * 
	 * @return
	 */
	public Hashtable<String, Double> getResults();
}
