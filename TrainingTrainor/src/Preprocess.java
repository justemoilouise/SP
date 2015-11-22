import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import data.Species;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Preprocess {
	private double[] min, max, mean;
	private double[][] principalComponents;
	
	public Preprocess() {}
	
	public double[] scale(double[] features) {		
		double[] scaled = new double[features.length];
		
		for(int i=0; i<features.length; i++) {
			scaled[i] = (features[i]-min[i])/(max[i]-min[i]);
		}
		
		return scaled;
	}
	
	public double[] reduceFeatures(double[] features) {
		double[] phi = MathFunctions.MatrixSubtraction(features, mean);
		double[][] reducedFeatures = MathFunctions.MatrixMultiplication(principalComponents, phi);
		
		return MathFunctions.TransposeTo1D(reducedFeatures);
	}
	
	public ArrayList<Species> reduceFeatures(ArrayList<Species> dataset, int PC) {
		// TODO Auto-generated method stub
		double[][] featureSet = extractFeatures(dataset);
		featureSet = MathFunctions.Transpose(featureSet);
		
		double[][] phi = calculatePhi(featureSet);
		double[][] u = getSVD_U(featureSet);
		double[][] pc = MathFunctions.GetMatrixColumns(u, 0, PC);
		principalComponents = MathFunctions.Transpose(pc);
		featureSet = MathFunctions.MatrixMultiplication(principalComponents, phi);
		featureSet = MathFunctions.Transpose(featureSet);
		return updateDataset(dataset, featureSet);
	}

	public ArrayList<Species> scale(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		double[][] featureSet = extractFeatures(dataset);
		featureSet = MathFunctions.Transpose(featureSet);
		
		//scaling proper
		min = MathFunctions.GetMinimum(featureSet);
		max = MathFunctions.GetMaximum(featureSet);
		
		for(int i=0; i<featureSet.length; i++) {			
			for(int j=0; j<dataset.size(); j++) {
				featureSet[i][j] = (featureSet[i][j]-min[i])/(max[i]-min[i]);
			}
		}
		
		featureSet = MathFunctions.Transpose(featureSet);
		return updateDataset(dataset, featureSet);
	}
	
	private double[][] extractFeatures(ArrayList<Species> dataset) {
		double[][] features = new double[dataset.size()][];
		int counter = 0;
		int len = dataset.get(0).getFeatureValues().length;

		Iterator<Species> i = dataset.iterator();
		while(i.hasNext()) {
			features[counter] = Arrays.copyOfRange(i.next().getFeatureValues(), 0, len);
			counter++;
		}

		return features;
	}
	
	private double[][] calculatePhi(double[][] data) {
		mean = MathFunctions.GetMatrixMean(data);
		
		return MathFunctions.MatrixSubtraction(data, mean);
	}
	
	private double[][] getSVD_U(double[][] data) {
		SingularValueDecomposition svd = new SingularValueDecomposition(new Matrix(data));
		Matrix u = svd.getU();
		
		return u.getArray();
	}
	
	private ArrayList<Species> updateDataset(ArrayList<Species> rawDataset, double[][] data) {
		// TODO Auto-generated method stub
		ArrayList<Species> preprocessedDataset = new ArrayList<Species>();
		int dCounter = 0;

		Iterator<Species> i = rawDataset.iterator();
		while(i.hasNext()) {
			Species s = i.next();
			s.setFeatureValues(data[dCounter]);

			preprocessedDataset.add(s);
			dCounter++;
		}
		
		return preprocessedDataset;
	}
}
