package core;

import java.util.ArrayList;

import Data.Feature;
import Data.Species;
import ImageHandlers.ProcessImage;
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
			// horns
			Feature f = new Feature("Horn");
			f.setmLabels(p.getFeatureLabels());
			f.setmValues(values);
			f.setCount(values.size());

			species.getFeatures().add(f);
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
			// shell
			Feature f = new Feature("Shell");
			f.setmLabels(bs.getBaseFeatureLabels());
			f.getmValues().add(bValues);

			species.getFeatures().add(f);
		}
		
		ArrayList<double[]> pValues = bs.getParticleFeatureValues();
		if(pValues.size() > 0) {
			// pores
			Feature f = new Feature("Pore");
			f.setmLabels(bs.getParticleFeatureLabels());
			f.setmValues(pValues);

			species.getFeatures().add(f);
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
