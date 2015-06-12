package Data;

import java.io.Serializable;
import java.util.ArrayList;

import ij.ImagePlus;

@SuppressWarnings("serial")
public class Input implements Serializable {
	private ImagePlus img, protrusions, base;
	private String imageName;
	private Species species;
	private ArrayList<SVMResult> svmResult;

	public Input() {
		this.img = null;
		this.protrusions = null;
		this.base = null;
		this.imageName = "/resources/img_noimg.png";
		this.svmResult = new ArrayList<SVMResult>();
	}

	public ImagePlus getImg() {
		return img;
	}

	public void setImg(ImagePlus img) {
		this.img = img;
	}

	public ImagePlus getProtrusions() {
		return protrusions;
	}

	public void setProtrusions(ImagePlus protrusions) {
		this.protrusions = protrusions;
	}

	public ImagePlus getBase() {
		return base;
	}

	public void setBase(ImagePlus base) {
		this.base = base;
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
