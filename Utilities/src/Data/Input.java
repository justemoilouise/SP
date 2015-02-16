package Data;

import java.util.ArrayList;

import ij.ImagePlus;

public class Input {
	private ImagePlus img;
	private String imageName;
	private Species species;
	private ArrayList<SVMResult> svmResult;

	public Input() {
		this.img = null;
		this.imageName = "img/noimg.png";
		this.svmResult = new ArrayList<SVMResult>();
	}

	public ImagePlus getImg() {
		return img;
	}

	public void setImg(ImagePlus img) {
		this.img = img;
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
