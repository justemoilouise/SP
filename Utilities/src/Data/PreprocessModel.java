package Data;

public class PreprocessModel {
	private double[][] scalingFactors;
	private int PC;
	private double[] mean;
	private double[][] principalComponents;
	
	public PreprocessModel() {}

	public double[][] getScalingFactors() {
		return scalingFactors;
	}

	public void setScalingFactors(double[][] scalingFactors) {
		this.scalingFactors = scalingFactors;
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
}
