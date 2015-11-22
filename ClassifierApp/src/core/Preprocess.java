package core;

import java.util.ArrayList;

import CoreHandler.MathFunctions;
import Data.PreprocessModel;
import Data.Species;
import Interfaces.IPREPROCESS;

public class Preprocess implements IPREPROCESS {
	private PreprocessModel IJModel, JFModel, model;
	private boolean isIJ;
	
	public Preprocess() {}
	
	public boolean isIJ() {
		return isIJ;
	}

	public void setIJ(boolean isIJ) {
		this.isIJ = isIJ;
	}

	public PreprocessModel getIJModel() {
		return IJModel;
	}

	public void setIJModel(PreprocessModel iJModel) {
		IJModel = iJModel;
	}

	public PreprocessModel getJFModel() {
		return JFModel;
	}

	public void setJFModel(PreprocessModel jFModel) {
		JFModel = jFModel;
	}

	public Preprocess(PreprocessModel model) {
		this.model = model;
	}
	
	@Override
	public double[] scale(double[] features) {
		// TODO Auto-generated method stub
		
		if(isIJ)
			model = IJModel;
		else
			model = JFModel;
		
		double[] scaled = new double[features.length];
		double[] min = model.getMin();
		double[] max = model.getMax();
		
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
