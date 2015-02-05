package Data;

import java.util.Hashtable;

import ij.ImagePlus;

public class Input {
	private ImagePlus img;
	private String imageName;
	private Species species;
	private Hashtable<String, Double> svmResult;

	public Input() {
		this.img = null;
		this.imageName = "img/noimg.png";
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

	public Hashtable<String, Double> getSvmResult() {
		return svmResult;
	}

	public void setSvmResult(Hashtable<String, Double> svmResult) {
		this.svmResult = svmResult;
	}	
}
