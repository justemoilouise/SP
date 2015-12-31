package data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PreprocessModel implements Serializable {
	private double[] min;
	private double[] max;
	private int PC;
	private double[] mean;
	private double[][] principalComponents;
	private String[] features;
	
	public PreprocessModel() {}

	public double[] getMin() {
		return min;
	}

	public void setMin(double[] min) {
		this.min = min;
	}

	public double[] getMax() {
		return max;
	}

	public void setMax(double[] max) {
		this.max = max;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int pC) {
		PC = pC;
	}

	public double[] getMean() {
		return mean;
	}

	public void setMean(double[] mean) {
		this.mean = mean;
	}

	public double[][] getPrincipalComponents() {
		return principalComponents;
	}

	public void setPrincipalComponents(double[][] principalComponents) {
		this.principalComponents = principalComponents;
	}

	public String[] getFeatures() {
		return features;
	}

	public void setFeatures(String[] features) {
		this.features = features;
	}
}
