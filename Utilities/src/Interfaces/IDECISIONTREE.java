package Interfaces;

import java.io.InputStream;
import java.util.ArrayList;

import Data.Species;

public interface IDECISIONTREE {

	/**
	 * 
	 * @param stream
	 * 
	 * @return List of input image species
	 */
	public ArrayList<Species> readImageSet(InputStream stream);
	
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
