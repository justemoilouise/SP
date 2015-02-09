package preprocess;

import java.util.ArrayList;
import java.util.Iterator;

import Data.Species;
import Interfaces.IPREPROCESS;

public class Preprocess implements IPREPROCESS {
	private ArrayList<Species> dataset_raw;
	
	public Preprocess() {}
	
	public Preprocess(ArrayList<Species> dataset_raw) {
		this.dataset_raw = dataset_raw;
	}

	public double[] getClasses() {		
		return extractClasses();
	}
	
	public int[] getAttributes() {
		return extractAttributes();
	}

	private double[] extractClasses() {
		int count = 0, index=0;
		String name = "";
		double[] classes = new double[dataset_raw.size()];
		Iterator<Species> i = dataset_raw.iterator();
		while(i.hasNext()) {
			Species s = i.next();
			
			if(!s.getName().equals(name)) {
				name = s.getName();
				count++;
			}
			
			classes[index] = count;
			index++;
		}
		
		return classes;
	}
	
	private int[] extractAttributes() {
		int attributesLength = dataset_raw.get(0).getFeatureValues().length;
		int[] attributes = new int[attributesLength];
		
		for(int i=0; i<attributesLength; i++)
			attributes[i] = i+1;
		
		return attributes;
	}

	@Override
	public ArrayList<Species> reduceFeatures(ArrayList<Species> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] reduceFeatures(double[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Species> scale(ArrayList<Species> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] scale(double[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
