package com.training.core;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import Data.ClassifierModel;
import Data.PreprocessModel;
import Data.SVMModel;
import Data.Species;
import FileHandlers.FileInput;

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
	
	public ArrayList<Species> readDataset(String filename) {
		File f = FileInput.uploadExcelFile();
		if(f != null) {
			return FileInput.readSpecies(f);
		}
		
		return new ArrayList<Species>();
	}
	
	public ArrayList<Species> readDataset(File f) {
		if(f != null) {
			return FileInput.readSpecies(f);
		}
		
		return new ArrayList<Species>();
	}
	
	public ArrayList<Species> readDataset(InputStream stream) {
//		if(f != null) {
//			return FileInput.readSpecies(f);
//		}
		
		return new ArrayList<Species>();
	}
}
