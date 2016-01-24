package com.training.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import Data.ClassifierModel;
import Data.PreprocessModel;
import Data.SVMModel;
import Data.Species;
import FileHandlers.FileInput;

public class TrainingAppProcessor {
	private double version;

	public TrainingAppProcessor() {
		this.version = 6;
	}
	
	public ArrayList<ClassifierModel> getModelList() {
		return null;
	}
	
	public void getAppList() {}
	
	public ClassifierModel buildClassifierModel(ClassifierModel model) {
		model.setCreatedDate(new Date());
		model.setVersion(version);
		version++;
		
		return model;
	}
	
	public ClassifierModel saveClassifierModel(PreprocessModel preprocessModel, SVMModel svmModel, String notes) {
		ClassifierModel model = new ClassifierModel();
		model.setPreprocessModel(preprocessModel);
		model.setSvmmodel(svmModel);
		model.setNotes(notes);
		model.setCreatedDate(new Date());
		
		return model;
	}
	
	public ArrayList<Species> readDataset(InputStream stream) {
		if(stream != null) {
			return FileInput.readSpecies(stream);
		}
		
		return new ArrayList<Species>();
	}
}
