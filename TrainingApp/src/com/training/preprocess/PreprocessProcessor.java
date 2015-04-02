package com.training.preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import CoreHandler.MathFunctions;
import Data.PreprocessModel;
import Data.Species;
import Interfaces.IPREPROCESS;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class PreprocessProcessor implements IPREPROCESS {
	private PreprocessModel model;
	
	public PreprocessProcessor() {
		this.model = new PreprocessModel();
	}
	
	public boolean setPC(int PC) {
		model.setPC(PC);
		return true;
	}
	
	public PreprocessModel getPreprocessModel() {
		return model;
	}

	@Override
	public ArrayList<Species> reduceFeatures(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		double[][] featureSet = extractFeatures(dataset);
		featureSet = MathFunctions.Transpose(featureSet);
		
		double[][] phi = calculatePhi(featureSet);
		double[][] u = getSVD_U(featureSet);
		double[][] pc = MathFunctions.GetMatrixColumns(u, 0, model.getPC());
		double[][] principalComponents = MathFunctions.Transpose(pc);
		featureSet = MathFunctions.MatrixMultiplication(principalComponents, phi);
		
		//save principal components to model
		model.setPrincipalComponents(principalComponents);

		featureSet = MathFunctions.Transpose(featureSet);
		return updateDataset(dataset, featureSet);
	}

	@Override
	public double[] reduceFeatures(double[] dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Species> scale(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		double[][] featureSet = extractFeatures(dataset);
		featureSet = MathFunctions.Transpose(featureSet);
		
		//scaling proper
		double[] min = MathFunctions.GetMinimum(featureSet);
		double[] max = MathFunctions.GetMaximum(featureSet);
		
		for(int i=0; i<featureSet.length; i++) {			
			for(int j=0; j<dataset.size(); j++) {
				featureSet[i][j] = (featureSet[i][j]-min[i])/(max[i]-min[i]);
			}
		}
		
		//save scaling factors to model
		model.setMin(min);
		model.setMax(max);
		model.setFeatures(dataset.get(0).getFeatureLabels());
		
		featureSet = MathFunctions.Transpose(featureSet);
		return updateDataset(dataset, featureSet);
	}

	@Override
	public double[] scale(double[] dataset) {
		// TODO Auto-generated method stub
		return null;
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
		double[] mean = MathFunctions.GetMatrixMean(data);
		
		//save to model
		model.setMean(mean);
		
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
