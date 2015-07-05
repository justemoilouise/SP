package Data;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ClassifierModel implements Serializable {
	private double version;
	private Date createdDate;
	private String notes;
	private PreprocessModel preprocessModel;
	private SVMModel svmmodel;
	private DecisionTreeModel decisionTreeModel;
	private boolean isIJUsed;
	
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

	public DecisionTreeModel getDecisionTreeModel() {
		return decisionTreeModel;
	}

	public void setDecisionTreeModel(DecisionTreeModel decisionTreeModel) {
		this.decisionTreeModel = decisionTreeModel;
	}

	public boolean isIJUsed() {
		return isIJUsed;
	}

	public void setIJUsed(boolean isIJUsed) {
		this.isIJUsed = isIJUsed;
	}
}
