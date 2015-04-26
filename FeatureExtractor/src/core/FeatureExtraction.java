package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import ij.ImagePlus;
import ij.Prefs;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugins.GLCM_Texture;
import ij.plugins.Moment_Calculator;
import ij.process.ImageProcessor;
import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;

public class FeatureExtraction {
	private Haralick h;
	private ArrayList<Double> featureValues;
	private ArrayList<String> featureLabels;
	
	public FeatureExtraction() {
		this.featureLabels = new ArrayList<String>();
		this.featureValues = new ArrayList<Double>();
	}
	
	public double[] getFeatureValues() {
		double[] values = new double[featureValues.size()];
		int counter = 0;
		Iterator<Double> iter = featureValues.iterator();
		while(iter.hasNext()) {
			values[counter] = iter.next();
			counter++;
		}
		return values;
	}

	public String[] getFeatureLabels() {
		String[] labels = new String[featureLabels.size()];
		int counter = 0;
		Iterator<String> iter = featureLabels.iterator();
		while(iter.hasNext()) {
			labels[counter] = iter.next();
			counter++;
		}
		return labels;
	}
	
	public void getShapeDescriptors(ImagePlus ip) {
		int m = Prefs.getInt("measurements", ij.measure.Measurements.SHAPE_DESCRIPTORS);
		ResultsTable shapeDescriptors = new ResultsTable();
		Analyzer a = new Analyzer(ip, m, shapeDescriptors);
		a.measure();
		
		parseResultTable(shapeDescriptors);
	}
	
	public void getImageMoments(ImagePlus ip) {
		Moment_Calculator moment = new Moment_Calculator();
		moment.run(ip);
		
		addToFeatureLabels(moment.getHeadings());
		addToFeatureValues(moment.getValues());
	}
	
	public void getTextureDescriptors(ImageProcessor ip) {
		GLCM_Texture texture = new GLCM_Texture();
		texture.resetResults();
		texture.run(ip);
		ResultsTable textureResults = texture.getResults();
		
		parseResultTable(textureResults);
	}
	
	public void getHaralickDescriptors(ImageProcessor ip) {
		String[] haralickFeatureLabels = {"Angular second moment", "Contrast", "Correlation", "Sum of Squares: Variance", "Inverse difference moment", "Sum average", "Sum Variance",
				"Sum entropy", "Entropy", "Difference Variance", "Difference entropy", "Info. measure of Correlation 1", "Info. measure of Correlation 2", "Max. correlation coefficient"};
		
		h = new Haralick();
		h.run(ip);
		
		addToFeatureValues(h.getFeatures().get(0));
		addToFeatureLabels(haralickFeatureLabels);
	}
	
	private void parseResultTable(ResultsTable rt) {
		StringTokenizer value = new StringTokenizer(rt.getRowAsString(0), "\t");
		value.nextToken();

		while(value.hasMoreTokens()) {
			featureValues.add(Double.parseDouble(value.nextToken()));
		}
		
		addToFeatureLabels(rt.getHeadings());
	}
	
	private void addToFeatureValues(double[] values) {
		for(double val : values) {
			featureValues.add(val);
		}
	}
	
	private void addToFeatureLabels(String[] values) {
		for(String val : values) {
			featureLabels.add(val);
		}
	}
}
