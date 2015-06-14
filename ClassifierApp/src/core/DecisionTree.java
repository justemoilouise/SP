package core;

import java.util.Hashtable;

import Data.Attributes;
import Data.Feature;
import Data.Species;

public class DecisionTree {

	public DecisionTree() {}

	public String classify(Species s) {
		Hashtable<String, Feature> features = s.getFeatures();
		Attributes attr = new Attributes();
		String name = "UNKNOWN";

		// shell shape
		if(features.containsKey("Shape")) {
			if(features.get("Shape").getDescription().equals("Conical")) {
				attr.setSphericalShape(false);
				name = "Triassocampe coronata Bragin";
			} else if(features.get("Shape").getDescription().equals("Spherical")) {
				attr.setSphericalShape(true);
				if(features.containsKey("Horn")) {
					attr.setHasHorns(true);
					attr.setHornCount(features.get("Horn").getCount());

					if(features.get("Horn").getCount()==3) {
						name = "Eptingium manfredi Dumitrica";
					}
				}
				else if(features.containsKey("Spine")) {
					attr.setHasSpines(true);
					attr.setSpineCount(features.get("Spine").getCount());

					//if(features.get("Spine").getCount()==2) {
						if(features.get("Pore").getDescription().equals("Spherical")) {
							attr.setPoreShape(1);
							name = "Pseudostylosphaera compacta";
						}
						else if(features.get("Pore").getDescription().equals("Conical")) {
							attr.setPoreShape(2);
							name = "Pseudostylosphaera japonica";
						}
					//}
				}
			}
		}

		s.setAttr(attr);
		return name;
	}
}
