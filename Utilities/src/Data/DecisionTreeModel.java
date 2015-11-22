package Data;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class DecisionTreeModel implements Serializable {
	private double accuracy;
	private ArrayList<String> classes;
	
	public DecisionTreeModel() {
		this.classes = new ArrayList<String>();
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public ArrayList<String> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<String> classes) {
		this.classes = classes;
	}
}
