package Interfaces;

import java.util.ArrayList;

import Data.Species;

public interface IPREPROCESS {

	/**
	 * Gets the preprocessed dataset
	 * 
	 * @return ArrayList of the dataset that underwent preprocessing
	 */
	public ArrayList<Species> getPreprocessedDataset();
	
	/**
	 * 
	 * @param data
	 */
	public void updateDataset(double[][] data);
	
	/**
	 * 
	 * @return
	 */
	public double[][] extractFeatures();
}
