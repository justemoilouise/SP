package core;

import java.util.ArrayList;
import java.util.StringTokenizer;

import ImageHandlers.ProcessImage;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;

public class ParticleAnalysis {
	private ArrayList<double[]> featureValues;
	private ArrayList<String> featureLabels;
	
	public ParticleAnalysis() {
		this.featureLabels = new ArrayList<String>();
		this.featureValues = new ArrayList<double[]>();
	}
	
	public ArrayList<double[]> getFeatureValues() {
		return featureValues;
	}

	public ArrayList<String> getFeatureLabels() {
		return featureLabels;
	}

	public void analyzeParticles(ImagePlus ip) {
		ImagePlus imp = ProcessImage.convertToBinary(ip);
		imp.getProcessor().findEdges();
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.SHAPE_DESCRIPTORS, rt, 0, Double.MAX_VALUE, 0, 1);
		pa.analyze(imp);
		
		parseResultTable(rt);
	}
	
	private void parseResultTable(ResultsTable rt) {		
		for(int i=1;; i++) {
			if(rt.getRowAsString(i) == null) {
				break;
			}
			
			StringTokenizer value = new StringTokenizer(rt.getRowAsString(i), "\t");
			double[] v = new double[value.countTokens()];
			int index = 0;
			
			while(value.hasMoreTokens()) {
				v[index] = Double.parseDouble(value.nextToken());
				index++;
			}
			
			featureValues.add(v);
		}
		
		for(String val : rt.getHeadings()) {
			featureLabels.add(val);
		}
	}
}
