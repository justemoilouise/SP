package Data;

import java.util.Date;

public class ClassifierModel {
	private double version;
	private Date createdDate;
	private String notes;
	private PreprocessModel preprocessModel;
	private SVMModel svmmodel;
	
	public ClassifierModel() {}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public PreprocessModel getPreprocessModel() {
		return preprocessModel;
	}

	public void setPreprocessModel(PreprocessModel preprocessModel) {
		this.preprocessModel = preprocessModel;
	}

	public SVMModel getSvmmodel() {
		return svmmodel;
	}

	public void setSvmmodel(SVMModel svmmodel) {
		this.svmmodel = svmmodel;
	}
}
