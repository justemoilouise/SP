package Interfaces;

public interface ISCALE {

	/**
	 * Scales the values to (0,1)
	 */
	public void scale();
	public double[] scale(double[] features);
}
