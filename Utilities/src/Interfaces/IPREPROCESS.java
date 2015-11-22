package Interfaces;

import java.util.ArrayList;

import Data.Species;

public interface IPREPROCESS {
	
	/**
	 * Scales the values to (0,1)
	 */
	public ArrayList<Species> scale(ArrayList<Species> dataset);
	public double[] scale(double[] features);

	/**
	 * Apply PCA to set of features
	 * 
	 * @param features
	 * @return
	 */
	public ArrayList<Species> reduceFeatures(ArrayList<Species> dataset);
	public double[] reduceFeatures(double[] features);
}
