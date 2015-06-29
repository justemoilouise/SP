package Data;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Input implements Serializable {
	private String imageName;
	private Species species;
	private ArrayList<SVMResult> svmResult;

	public Input() {
		this.imageName = "/resources/img_noimg.png";
		this.svmResult = new ArrayList<SVMResult>();
	}	

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public ArrayList<SVMResult> getSvmResult() {
		return svmResult;
	}
	
	public void setSvmResult(ArrayList<SVMResult> svmResult) {
		this.svmResult = svmResult;
	}
}
