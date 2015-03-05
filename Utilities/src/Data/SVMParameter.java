package Data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SVMParameter implements Serializable {
	private int svmType;
	private int kernel;
	private double cost;
	private double gamma;
	private double epsilon;
	private int degree;
	private double nu;
	private double coefficient;
	
	public int getSvmType() {
		return svmType;
	}

	public void setSvmType(int svmType) {
		this.svmType = svmType;
	}

	public int getKernel() {
		return kernel;
	}

	public void setKernel(int kernel) {
		this.kernel = kernel;
	}
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public double getGamma() {
		return gamma;
	}
	
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	
	public double getEpsilon() {
		return epsilon;
	}
	
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public int getDegree() {
		return degree;
	}
	
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public double getNu() {
		return nu;
	}
	
	public void setNu(double nu) {
		this.nu = nu;
	}
	
	public double getCoefficient() {
		return coefficient;
	}
	
	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}	
}
