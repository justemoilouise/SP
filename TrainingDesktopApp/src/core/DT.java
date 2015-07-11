package core;

import ij.ImagePlus;

import java.io.InputStream;
import java.util.ArrayList;

import CoreHandler.DecisionTree;
import CoreHandler.ImageProcessing;
import Data.Species;
import Interfaces.IDECISIONTREE;

public class DT implements IDECISIONTREE {
	private DecisionTree classifier;

	public DT() {
		this.classifier = new DecisionTree();
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

		return 100.0*correct/dataset.size();
	}

	@Override
	public ArrayList<Species> processImageSet(ArrayList<Species> dataset) {
		// TODO Auto-generated method stub
		ArrayList<Species> processed = new ArrayList<Species>();

		for(Species s : dataset) {
			ImageProcessing ip = new ImageProcessing(s.getImg());
			ip.extractFeatures(true);
			ImagePlus p = ip.getImageProtrusions();
			ImagePlus b = ip.getImageBaseShape(p);

			Species sp = ip.getSpecies();
			sp.setProtrusions(p);
			sp.setBase(b);

			processed.add(sp);
		}

		return processed;
	}

	@Override
	public ArrayList<Species> readImageSet(InputStream stream) {
		// TODO Auto-generated method stub
		return null;
	}
}