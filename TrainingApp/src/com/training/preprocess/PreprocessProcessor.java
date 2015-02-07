package com.training.preprocess;

import java.util.ArrayList;

import Data.PreprocessModel;
import Data.Species;
import Interfaces.IPREPROCESS;

public class PreprocessProcessor implements IPREPROCESS {
	private PreprocessModel model;
	
	public PreprocessProcessor() {
		this.model = new PreprocessModel();
	}

	@Override
	public ArrayList<Species> reduceFeatures(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] reduceFeatures(double[] dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Species> scale(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] scale(double[] dataset) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private double[][] extractFeatures() {
		return null;
	}
}
