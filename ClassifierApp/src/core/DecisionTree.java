package core;

import java.util.Hashtable;

import Data.Feature;
import Data.Species;

public class DecisionTree {

	public DecisionTree() {}
	
	public String classify(Species s) {
		Hashtable<String, Feature> features = s.getFeatures();
		
		// shell shape
		if(features.containsKey("Shape")) {
			if(features.get("Shape").getDescription().equals("Conical")) {
				return "Triassocampe coronata Bragin";
			} else if(features.get("Shape").getDescription().equals("Spherical")) {
				if(features.containsKey("Horn")) {
					return "Eptingium manfredi Dumitrica";
				}
				else if(features.containsKey("Spine")) {
					return "Pseudostylosphaera compacta or Pseudostylosphaera japonica";
				}
				else {
					return "UNKNOWN";
				}
			} else {
				return "UNKNOWN";
			}
		}
		
		return "UNKNOWN";
	}
}
