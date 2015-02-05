package preprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import Data.Species;
import Interfaces.IPREPROCESS;

public class Preprocess implements IPREPROCESS {
	private ArrayList<Species> dataset_raw;
	private ArrayList<Species> dataset_preprocessed;
	private String[] keys;
	
	public Preprocess() {}
	
	public Preprocess(ArrayList<Species> dataset_raw) {
		this.dataset_raw = dataset_raw;
		this.keys = dataset_raw.get(0).getFeatureLabels();
	}

	public double[] getClasses() {		
		return extractClasses();
	}
	
	public int[] getAttributes() {
		return extractAttributes();
	}
	
	@Override
	public ArrayList<Species> getPreprocessedDataset() {
		// TODO Auto-generated method stub
		return dataset_preprocessed;
	}

	@Override
	public void updateDataset(double[][] data) {
		// TODO Auto-generated method stub
		dataset_preprocessed = new ArrayList<Species>();
		int dCounter = 0;

		Iterator<Species> i = dataset_raw.iterator();
		while(i.hasNext()) {
			Species s = new Species();
			s.setName(i.next().getName());
			s.setFeatureLabels(keys);
			s.setFeatureValues(data[dCounter]);

			dataset_preprocessed.add(s);
			dCounter++;
		}
	}

	@Override
	public double[][] extractFeatures() {
		// TODO Auto-generated method stub
		double[][] features_raw = new double[dataset_raw.size()][];
		int counter = 0;
		int len = dataset_raw.get(0).getFeatureValues().length;

		Iterator<Species> i = dataset_raw.iterator();
		while(i.hasNext()) {
			features_raw[counter] = Arrays.copyOfRange(i.next().getFeatureValues(), 0, len);
			counter++;
		}

		return features_raw;
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
}
