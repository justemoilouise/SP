package core;

import java.util.ArrayList;

import Data.Feature;
import Data.Species;
import ImageHandlers.ProcessImage;
import helpers.ArrayHelper;
import helpers.ValueHelper;
import ij.ImagePlus;
import imageProcessing.BaseShape;
import imageProcessing.FeatureExtraction;
import imageProcessing.ParticleAnalysis;
import imageProcessing.Protrusions;

public class ImageProcessing {
	private ImagePlus originalImg;
	private Species species;

	public ImageProcessing() {
		this.species = new Species();
	}

	public ImageProcessing(ImagePlus originalImg) {
		this.originalImg = originalImg;
		this.species = new Species();
	}

	public ImagePlus getImageProtrusions() {
		ImagePlus img = ProcessImage.topHatTransform(originalImg.duplicate());

		Protrusions p = new Protrusions();
		p.identifyProtrusions(originalImg, img);
		p.analyzeProtrusions();

		ArrayList<double[]> values = p.getFeatureValues();
		if(values.size() > 0) {
			Feature f = new Feature();

			int i1 = ArrayHelper.GetIndexOf(p.getFeatureLabels(), "Circ.");
			double circularity = ArrayHelper.GetFeatureAverage(values, i1);

			int i2 = ArrayHelper.GetIndexOf(p.getFeatureLabels(), "Perim.");
			double perimeter = ArrayHelper.GetFeatureAverage(values, i2);

			String name = "";
			int pr = ValueHelper.GetProtrusion(circularity, perimeter);
			switch(pr) {
			case 1: name = "Horn"; break;
			case 2: name = "Spine"; break;
			default: break;
			}

			if(!name.equals("")) {
				f.setName(name);
				f.setmLabels(p.getFeatureLabels());
				f.setmValues(values);
				f.setCount(values.size());

				species.getFeatures().put(f.getName(), f);
			}
		}

		return p.getImage();	
	}

	@SuppressWarnings("null")
	public ImagePlus getImageBaseShape(ImagePlus p) {
		BaseShape bs = new BaseShape();
		bs.identifyBaseShape(originalImg, p);
		bs.analyzeBaseShape();

		double[] bValues = bs.getBaseFeatureValues();
		if(bValues != null || bValues.length > 0) {
			// shape
			int index = ArrayHelper.GetIndexOf(bs.getBaseFeatureLabels(), "Round");
			int shape = ValueHelper.GetRoundness(bValues[index]);

			Feature f = new Feature("Shape");
			f.setmLabels(bs.getBaseFeatureLabels());
			f.getmValues().add(bValues);
			f.setCount(1);
			f.setDescription(shape == 1 ? "Spherical" : "Conical");

			species.getFeatures().put(f.getName(), f);

			// meshwork
			index = ArrayHelper.GetIndexOf(bs.getBaseFeatureLabels(), "Angular Second Moment");
			int mw = ValueHelper.GetTexture(bValues[index]);

			f = new Feature("Meshwork");
			f.setmLabels(bs.getBaseFeatureLabels());
			f.getmValues().add(bValues);
			f.setCount(1);
			f.setDescription(mw == 1 ? "Fine" : "Spongy");

			species.getFeatures().put(f.getName(), f);
		}

		//		ArrayList<double[]> pValues = bs.getParticleFeatureValues();
		//		if(pValues.size() > 0) {
		//			// pores
		//			int index = ArrayHelper.GetIndexOf(bs.getParticleFeatureLabels(), "Circ.");
		//			double mean = ArrayHelper.GetFeatureAverage(pValues, index);
		//			int shape = ValueHelper.GetCircularity(mean);
		//
		//			Feature f = new Feature("Pore");
		//			f.setmLabels(bs.getParticleFeatureLabels());
		//			f.setmValues(pValues);
		//			f.setCount(pValues.size());
		//			f.setDescription(shape == 1 ? "Spherical" : "Conical");
		//
		//			species.getFeatures().put(f.getName(), f);
		//		}

		return bs.getImage();
	}

	public void extractFeatures(boolean isIJ) {
		FeatureExtraction featureExtraction = new FeatureExtraction();
		featureExtraction.getShapeDescriptors(originalImg);

		if(isIJ) {
			featureExtraction.getTextureDescriptors(originalImg.getProcessor());
		}
		else {
			featureExtraction.getHaralickDescriptors(originalImg.getProcessor());
		}

		ParticleAnalysis pa = new ParticleAnalysis();
		pa.analyzeParticleShape(originalImg);

		species.setFeatureLabels(featureExtraction.getFeatureLabels());
		species.setFeatureValues(featureExtraction.getFeatureValues());
		species.setParticleLabels(pa.getFeatureLabels());
		species.setParticleValues(pa.getFeatureValues());
	}

	public Species getSpecies() {
		return species;
	}
}
