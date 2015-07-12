package Interfaces;

import java.util.ArrayList;

import Data.Species;

public interface IDECISIONTREE {
	
	/**
	 * 
	 * @return List of processed images (protrusions and base shape)
	 */
	public ArrayList<Species> processImageSet();
	
	/**
	 * 
	 * @param imageset
	 * 
	 * @return List of processed images (protrusions and base shape)
	 */
	public ArrayList<Species> processImageSet(ArrayList<Species> imageset);
	
	/**
	 * 
	 * @param imageset
	 * 
	 * @return Accuracy of decision tree model
	 */
	public double crossValidate(ArrayList<Species> imageset);
}
