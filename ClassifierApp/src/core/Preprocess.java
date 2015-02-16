package core;

import java.util.ArrayList;

import CoreHandler.MathFunctions;
import Data.PreprocessModel;
import Data.Species;
import Interfaces.IPREPROCESS;

public class Preprocess implements IPREPROCESS {
	private PreprocessModel model;
	
	public Preprocess() {}
	
	public Preprocess(PreprocessModel model) {
		this.model = model;
	}
	
	@Override
	public double[] scale(double[] features) {
		// TODO Auto-generated method stub
		double[] scaled = new double[features.length];
		double[] min = null;
		double[] max = null;
		
		for(int i=0; i<features.length; i++) {
			scaled[i] = (features[i]-min[i])/(max[i]-min[i]);
		}
		
		return scaled;
	}
	
	@Override
	public double[] reduceFeatures(double[] features) {
		// TODO Auto-generated method stub
		double[] phi = MathFunctions.MatrixSubtraction(features, model.getMean());
		double[][] reducedFeatures = MathFunctions.MatrixMultiplication(model.getPrincipalComponents(), phi);
		
		return MathFunctions.TransposeTo1D(reducedFeatures);
	}

	@Override
	public ArrayList<Species> scale(ArrayList<Species> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<Species> reduceFeatures(ArrayList<Species> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
