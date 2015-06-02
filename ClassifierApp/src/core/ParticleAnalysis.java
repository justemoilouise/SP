package core;

import java.util.ArrayList;
import java.util.StringTokenizer;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ByteProcessor;

public class ParticleAnalysis {
	private ArrayList<double[]> featureValues;
	private String[] featureLabels;
	
	public ParticleAnalysis() {
		this.featureValues = new ArrayList<double[]>();
	}
	
	public ArrayList<double[]> getFeatureValues() {
		return featureValues;
	}

	public String[] getFeatureLabels() {
		return featureLabels;
	}

	public void analyzeParticles(ImagePlus ip) {
		ByteProcessor bp = new ByteProcessor(ip.duplicate().getProcessor(), false);
		bp.autoThreshold();
		bp.findEdges();
		ImagePlus imp = new ImagePlus(ip.getTitle(), bp);
		imp.show();
		
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.SHAPE_DESCRIPTORS, rt, 0, Double.MAX_VALUE, 0, 1);
		pa.analyze(imp);
		rt.show("");
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
		
		featureLabels = rt.getHeadings();
	}
}
