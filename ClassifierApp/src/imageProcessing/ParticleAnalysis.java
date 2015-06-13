package imageProcessing;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.lang3.ArrayUtils;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ByteProcessor;

public class ParticleAnalysis {
	private ImagePlus img;
	private ArrayList<double[]> featureValues;
	private String[] featureLabels;
	
	public ParticleAnalysis() {
		this.featureLabels = new String[] {"No."};
		this.featureValues = new ArrayList<double[]>();
	}
	
	public ImagePlus getImage() {
		return img;
	}
	
	public ArrayList<double[]> getFeatureValues() {
		return featureValues;
	}

	public String[] getFeatureLabels() {
		return featureLabels;
	}

	public void analyzeParticleShape(ImagePlus ip) {
		ByteProcessor bp = ip.duplicate().getProcessor().convertToByteProcessor();
		bp.autoThreshold();
		bp.findEdges();
		this.img = new ImagePlus(ip.getTitle(), bp);
		
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.SHAPE_DESCRIPTORS, rt, 0, Double.MAX_VALUE, 0, 1);
		pa.analyze(img);
		parseResultTable(rt);
	}
	
	public void analyzeParticleAreaShapeAndLocation(ImagePlus ip) {
		ByteProcessor bp = ip.duplicate().getProcessor().convertToByteProcessor();
		bp.autoThreshold();
		bp.findEdges();
		this.img = new ImagePlus(ip.getTitle(), bp);
		
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer pa = new ParticleAnalyzer(ParticleAnalyzer.SHOW_NONE, Measurements.SHAPE_DESCRIPTORS + Measurements.CENTROID + Measurements.AREA, rt, 0, Double.MAX_VALUE, 0, 1);
		pa.analyze(img);
		parseResultTable(rt);
	}
	
	private void parseResultTable(ResultsTable rt) {
		int x = rt.getCounter();
		
		for(int i=1; i<x; i++) {
			if(rt.getRowAsString(i) == null) {
				break;
			}

			StringTokenizer value = new StringTokenizer(rt.getRowAsString(i), "\t");
			double[] v = new double[value.countTokens()];
			int index = 1;
			value.nextToken();
			
			v[0] = i;
			while(value.hasMoreTokens()) {
				v[index] = Double.parseDouble(value.nextToken());
				index++;
			}
			
			featureValues.add(v);
		}
		
		featureLabels = ArrayUtils.addAll(featureLabels, rt.getHeadings());
	}
}
