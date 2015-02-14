package com.training.core;

import java.util.ArrayList;
import java.util.Date;

import Data.ClassifierModel;
import Data.PreprocessModel;
import Data.SVMModel;

public class TrainingAppProcessor {

	public TrainingAppProcessor() {}
	
	public ArrayList<ClassifierModel> getModelList() {
		return null;
	}
	
	public void getAppList() {}
	
	public double saveClassifierModel(PreprocessModel preprocessModel, SVMModel svmModel, String notes) {
		ClassifierModel model = new ClassifierModel();
		model.setPreprocessModel(preprocessModel);
		model.setSvmmodel(svmModel);
		model.setNotes(notes);
		model.setCreatedDate(new Date());
		
		return model.getVersion();
	}
}
