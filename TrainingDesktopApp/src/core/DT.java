package core;

import ij.ImagePlus;

import java.util.ArrayList;

import CoreHandler.DecisionTree;
import CoreHandler.ImageProcessing;
import Data.DecisionTreeModel;
import Data.Species;
import Interfaces.IDECISIONTREE;

public class DT implements IDECISIONTREE, Runnable {
	private DecisionTreeModel model;
	private DecisionTree classifier;
	private ArrayList<Species> dataset;

	public DT() {
		this.model = new DecisionTreeModel();
		this.classifier = new DecisionTree();
	}
	
	public void setDataSet(ArrayList<Species> dataset) {
		this.dataset = dataset;
	}
	
	public DecisionTreeModel getModel() {
		return model;
	}

	@Override
	public double crossValidate(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		int correct = 0;

		for(Species s : dataset) {
			String predictedName = classifier.classify(s);

			if(predictedName.equalsIgnoreCase(s.getName()))
				correct++;
		}

		double accuracy = 100.0*correct/dataset.size();
		model.setAccuracy(accuracy);
		
		return accuracy;
	}
	
	@Override
	public ArrayList<Species> processImageSet() {
		// TODO Auto-generated method stub
		ArrayList<String> classes = new ArrayList<String>();
		ArrayList<Species> processed = new ArrayList<Species>();

		for(Species s : dataset) {
			if(!classes.contains(s.getName())) {
				classes.add(s.getName());
			}
			
			ImageProcessing ip = new ImageProcessing(s.getImg());
			ip.extractFeatures(true);
			ImagePlus p = ip.getImageProtrusions();
			ImagePlus b = ip.getImageBaseShape(p);

			Species sp = ip.getSpecies();
			sp.setProtrusions(p);
			sp.setBase(b);

			processed.add(sp);
		}
		
		model.setClasses(classes);

		return processed;
	}

	@Override
	public ArrayList<Species> processImageSet(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		ArrayList<String> classes = new ArrayList<String>();
		ArrayList<Species> processed = new ArrayList<Species>();

		for(Species s : dataset) {
			if(!classes.contains(s.getName())) {
				classes.add(s.getName());
			}
			
			ImageProcessing ip = new ImageProcessing(s.getImg());
			ip.extractFeatures(true);
			ImagePlus p = ip.getImageProtrusions();
			ImagePlus b = ip.getImageBaseShape(p);

			Species sp = ip.getSpecies();
			sp.setProtrusions(p);
			sp.setBase(b);

			processed.add(sp);
		}
		
		model.setClasses(classes);

		return processed;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ArrayList<Species> processed = processImageSet();
		crossValidate(processed);
	}
}
