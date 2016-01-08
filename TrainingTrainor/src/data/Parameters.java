package data;

import java.util.Random;
import Data.SVMParameter;

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
		this.svmParameter.setGamma(r.nextDouble() % 1000);
		this.svmParameter.setEpsilon(r.nextDouble() % 1000);
		this.svmParameter.setDegree(r.nextInt(999) + 1);
		this.svmParameter.setNu(r.nextDouble() % 1000);
		this.svmParameter.setCoefficient(r.nextDouble() % 1000);
	}
	
	public String convertToString() {
		return this.PCA + "," + this.svmParameter.getCost() + "," + this.svmParameter.getGamma() + "," +
				this.svmParameter.getEpsilon() + "," + this.svmParameter.getDegree() + "," + 
				this.svmParameter.getNu() + "," + this.svmParameter.getCoefficient();
	}
}
