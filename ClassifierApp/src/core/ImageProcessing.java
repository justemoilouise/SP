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
		//img.show();

		Protrusions p = new Protrusions();
		p.identifyProtrusions(originalImg, img);
		p.analyzeProtrusions();

		ArrayList<double[]> values = p.getFeatureValues();
		if(values.size() > 0) {
			Feature f = new Feature();
			
			if(values.size() == 2) {
				// spines
				f.setName("Spine");
				f.setmLabels(p.getFeatureLabels());
				f.setmValues(values);
				f.setCount(values.size());
				
				species.getFeatures().put(f.getName(), f);
			}
			else if(values.size() == 3) {
				// horns
				f.setName("Horn");
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
			int index = ArrayHelper.GetIndexOf(bs.getBaseFeatureLabels(), "Circ.");
			int shape = ValueHelper.GetShape(bValues[index]);
			
			Feature f = new Feature("Shape");
			f.setmLabels(bs.getBaseFeatureLabels());
			f.getmValues().add(bValues);
			f.setCount(1);
			f.setDescription(shape == 1 ? "Spherical" : "Conical");

			species.getFeatures().put(f.getName(), f);
		}
		
		ArrayList<double[]> pValues = bs.getParticleFeatureValues();
		if(pValues.size() > 0) {
			// pores
			Feature f = new Feature("Pore");
			f.setmLabels(bs.getParticleFeatureLabels());
			f.setmValues(pValues);
			f.setCount(pValues.size());

			species.getFeatures().put(f.getName(), f);
		}

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