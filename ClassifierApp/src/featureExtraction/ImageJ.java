package featureExtraction;


import ij.ImagePlus;
import ij.Prefs;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugins.GLCM_Texture;
import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;


public class ImageJ implements Measurements {	
	private ArrayList<String> labels = new ArrayList<String>();
	private ArrayList<Double> values = new ArrayList<Double>();
	
	public ImageJ() {}
	
	public String[] getFeatureLabels() {
		String[] featureLabels = new String[labels.size()];
		int counter = 0;
		Iterator<String> iter = labels.iterator();
		while(iter.hasNext()) {
			featureLabels[counter] = iter.next();
			counter++;
		}
		return featureLabels;
	}

	public void setFeatureLabels(ArrayList<String> labels) {
		this.labels = labels;
	}

	public double[] getFeatureValues() {
		double[] featureValues = new double[values.size()];
		int counter = 0;
		Iterator<Double> iter = values.iterator();
		while(iter.hasNext()) {
			featureValues[counter] = iter.next();
			counter++;
		}
		return featureValues;
	}

	public void setFeatureValues(ArrayList<Double> values) {
		this.values = values;
	}

	public void measure(ImagePlus ip) {
		int m = Prefs.getInt("measurements", SHAPE_DESCRIPTORS);
		ResultsTable featureResults = new ResultsTable();
		Analyzer a = new Analyzer(ip, m, featureResults);
		a.measure();
		saveToTable(featureResults);
	}
	
	private void saveToTable(ResultsTable rt) {
		StringTokenizer value = new StringTokenizer(rt.getRowAsString(0), "\t");
		value.nextToken();

		while(value.hasMoreTokens()) {
			values.add(Double.parseDouble(value.nextToken()));
		}
		labels.addAll(Arrays.asList(rt.getHeadings()));
	}

	public void getTextureFeatures(ImageProcessor ip) {
		GLCM_Texture texture = new GLCM_Texture();
		texture.resetResults();
		texture.run(ip);
		ResultsTable textureResults = texture.getResults();
		saveToTable(textureResults);
	}
}
