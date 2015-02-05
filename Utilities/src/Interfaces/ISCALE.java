package Interfaces;

public interface ISCALE {

	/**
	 * Scales the values to (0,1)
	 */
	public void scale();
	public double[] scale(double[] features);
	
	/**
	 * Saves important preprocessing numbers
	 * 	--scaling factors
	 * 
	 * @param arr
	 */
	public void saveToFile(double[] arr);

	/**
	 * Sets the minimum values per feature
	 * 
	 * @param min
	 */
	public void setMinimum(double[] min);
	
	/**
	 * Sets the maximum value for each feature
	 * 
	 * @param max
	 */
	public void setMaximum(double[] max);

	/**
	 * Identifies the minimum and maximum values per feature
	 * 
	 * @param data
	 */
	public void getMinAndMax(double[][] data);
}
