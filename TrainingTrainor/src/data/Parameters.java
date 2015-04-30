package data;

import java.util.Random;

public class Parameters {
	private int PCA;
	private SVMParameter svmParameter;
	
	public Parameters() {
		this.svmParameter = new SVMParameter();
	}

	public int getPCA() {
		return PCA;
	}

	public SVMParameter getSvmParameter() {
		return svmParameter;
	}

	public void setRandomValues()  {
		Random r = new Random();
		
		this.PCA = r.nextInt(9) + 1;
		this.svmParameter.setCost(r.nextInt(999) + 1);
		this.svmParameter.setGamma(r.nextDouble());
		this.svmParameter.setEpsilon(r.nextDouble());
		this.svmParameter.setDegree(r.nextInt());
		this.svmParameter.setNu(r.nextDouble());
		this.svmParameter.setCoefficient(r.nextDouble());
	}
	
	public String convertToString() {
		return this.PCA + "," + this.svmParameter.getCost() + "," + this.svmParameter.getGamma() + "," +
				this.svmParameter.getEpsilon() + "," + this.svmParameter.getDegree() + "," + 
				this.svmParameter.getNu() + "," + this.svmParameter.getCoefficient();
	}
}
