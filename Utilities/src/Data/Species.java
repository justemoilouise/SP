package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class Species implements Serializable {
	private String name, svmName, dTreeName;
	private String[] featureLabels;
	private double[] featureValues;
	private String[] particleLabels;
	private ArrayList<double[]> particleValues;
	private Hashtable<String, Feature> features;
	
	public Species () {
		this.name = "";
		this.particleValues = new ArrayList<double[]>();
		this.features = new Hashtable<String, Feature>();
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
	public String getSvmName() {
		return svmName;
	}
	public void setSvmName(String svmName) {
		this.svmName = svmName;
	}
	public String getdTreeName() {
		return dTreeName;
	}
	public void setdTreeName(String dTreeName) {
		this.dTreeName = dTreeName;
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
	public String[] getParticleLabels() {
		return particleLabels;
	}
	public void setParticleLabels(String[] particleLabels) {
		this.particleLabels = particleLabels;
	}
	public ArrayList<double[]> getParticleValues() {
		return particleValues;
	}
	public void setParticleValues(ArrayList<double[]> particleValues) {
		this.particleValues = particleValues;
	}
	public Hashtable<String, Feature> getFeatures() {
		return features;
	}
	public void setFeatures(Hashtable<String, Feature> features) {
		this.features = features;
	}
}
