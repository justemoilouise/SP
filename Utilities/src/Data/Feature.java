package Data;

import java.util.ArrayList;

public class Feature {
	private String name;
	private int count;
	private String[] mLabels;
	private ArrayList<double[]> mValues;
	private FeatureProperties properties;
	
	public Feature() {}
	
	public Feature(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getmLabels() {
		return mLabels;
	}

	public void setmLabels(String[] mLabels) {
		this.mLabels = mLabels;
	}

	public ArrayList<double[]> getmValues() {
		return mValues;
	}

	public void setmValues(ArrayList<double[]> mValues) {
		this.mValues = mValues;
	}

	public FeatureProperties getProperties() {
		return properties;
	}

	public void setProperties(FeatureProperties properties) {
		this.properties = properties;
	}	
}
