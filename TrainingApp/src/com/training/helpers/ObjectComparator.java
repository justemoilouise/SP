package com.training.helpers;

import java.util.Comparator;

import com.training.data.ClassifierModelList;

public class ObjectComparator implements Comparator<ClassifierModelList> {

	@Override
	public int compare(ClassifierModelList o1, ClassifierModelList o2) {
		// TODO Auto-generated method stub
		if (o1.getModel().getVersion() > o2.getModel().getVersion()) {
	        return -1;
	    } else if (o1.getModel().getVersion() < o2.getModel().getVersion()) {
	        return 1;
	    }
	    return 0;
	}

}
