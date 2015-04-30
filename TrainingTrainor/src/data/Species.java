package data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Species implements Serializable {
	private String name;
	private String[] featureLabels;
	private double[] featureValues;
	
	public Species () {
		this.name = "";
	}	
	public Species(String name, String[] featureLabels, double[] featureValues) {
		this.name = name;
		this.featureLabels = featureLabels;
		this.featureValues = featureValues;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getFeatureLabels() {
		return featureLabels;
	}
	public void setFeatureLabels(String[] featureLabels) {
		this.featureLabels = featureLabels;
	}
	public double[] getFeatureValues() {
		return featureValues;
	}
	public void setFeatureValues(double[] featureValues) {
		this.featureValues = featureValues;
	}
}
