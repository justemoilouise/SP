package Interfaces;

import java.util.ArrayList;

import Data.SVMParameter;
import Data.SVMResult;
import Data.Species;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public interface ISVM {

	/**
	 * Builds SVM model
	 */
	public void buildModel(ArrayList<Species> dataset, SVMParameter svmParameter);
	
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
	public ArrayList<SVMResult> classify(double[] features, boolean isIJUsed);
}
