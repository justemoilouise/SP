package Data;

import java.util.ArrayList;

public class Feature {
	private String name;
	private int count;
	private String[] mLabels;
	private ArrayList<double[]> mValues;
	private String description;
	
	public Feature() {
		this.mValues = new ArrayList<double[]>();
	}
	
	public Feature(String name) {
		this.name = name;
		this.mValues = new ArrayList<double[]>();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
}
