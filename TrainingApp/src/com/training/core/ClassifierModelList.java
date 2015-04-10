package com.training.core;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class ClassifierModelList implements Serializable {
	private BlobKey key;
	private ClassifierModel model;
	
	public ClassifierModelList() {
		this.key = new BlobKey("abc123");
		this.model = new ClassifierModel();
	}
	
	public ClassifierModelList(BlobKey key, ClassifierModel model) {
		this.key = key;
		this.model = model;
	}

	public BlobKey getKey() {
		return key;
	}

	public void setKey(BlobKey key) {
		this.key = key;
	}

	public ClassifierModel getModel() {
		return model;
	}

	public void setModel(ClassifierModel model) {
		this.model = model;
	}
}
