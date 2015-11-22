package CoreHandler;

import java.util.ArrayList;

import Data.Feature;
import Data.Species;
import ImageHandlers.ProcessImage;
import Helpers.ArrayHelper;
import Helpers.ValueHelper;
import ij.ImagePlus;
import ImageProcessing.BaseShape;
import ImageProcessing.FeatureExtraction;
import ImageProcessing.ParticleAnalysis;
import ImageProcessing.Protrusions;

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

			//			int i1 = ArrayHelper.GetIndexOf(p.getFeatureLabels(), "Circ.");
			//			double circularity = ArrayHelper.GetFeatureAverage(values, i1);
			//
			//			int i2 = ArrayHelper.GetIndexOf(p.getFeatureLabels(), "Perim.");
			//			double perimeter = ArrayHelper.GetFeatureAverage(values, i2);

			int i3 = ArrayHelper.GetIndexOf(p.getFeatureLabels(), "AR");
			double ar = ArrayHelper.GetFeatureAverage(values, i3);

			String name = "";
			//			int pr = ValueHelper.GetProtrusion(circularity, perimeter);
			int pr = ValueHelper.GetProtrusion(ar);
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
			String[] labels = bs.getBaseFeatureLabels();

			// meshwork
			int index = ArrayHelper.GetIndexOf(bs.getBaseFeatureLabels(), "Angular Second Moment");
			int mw = ValueHelper.GetTexture(bValues[index]);

			Feature f = new Feature("Meshwork");
			f.setmLabels(labels);
			f.getmValues().add(bValues);
			f.setCount(1);
			f.setDescription(mw == 1 ? "Fine" : "Spongy");

			species.getFeatures().put(f.getName(), f);
		}

		ArrayList<double[]> pValues = bs.getParticleFeatureValues();
		if(pValues.size() > 0) {
			// shape
			double[] features = ArrayHelper.GetFeatureAverage(pValues);
			
			int index = ArrayHelper.GetIndexOf(bs.getParticleFeatureLabels(), "Round");
			int shape = ValueHelper.GetRoundness(features[index]);

			Feature f = new Feature("Shape");
			f.setmLabels(bs.getParticleFeatureLabels());
			f.getmValues().add(features);
			f.setCount(1);
			f.setDescription(shape == 1 ? "Spherical" : "Conical");

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
