package Interfaces;

import java.util.ArrayList;

import Data.Species;

public interface IDECISIONTREE {
	
	/**
	 * 
	 * @param imageset
	 * 
	 * @return List of processed images (protrusions and base shape)
	 */
	public ArrayList<Species> processImageSet(ArrayList<Species> imageset);
	
	/**
	 * 
	 * @return Accuracy of decision tree model
	 */
	public double crossValidate();
	
	/**
	 * 
	 * @param imageset
	 * 
	 * @return Accuracy of decision tree model
	 */
	public double crossValidate(ArrayList<Species> imageset);
}
