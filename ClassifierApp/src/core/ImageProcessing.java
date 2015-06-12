package core;

import Data.Species;
import ImageHandlers.ProcessImage;
import ij.ImagePlus;
import imageProcessing.BaseShape;
import imageProcessing.FeatureExtraction;
import imageProcessing.ParticleAnalysis;
import imageProcessing.Protrusions;

public class ImageProcessing {
	private ImagePlus originalImg;

	public ImageProcessing() {}

	public ImageProcessing(ImagePlus originalImg) {
		this.originalImg = originalImg;
	}

	public ImagePlus getImageProtrusions() {
		ImagePlus img = ProcessImage.topHatTransform(originalImg.duplicate());
		//img.show();

		// isolate protrusions
		Protrusions p = new Protrusions();
		p.identifyProtrusions(originalImg, img);

		// measure protrusions
		p.analyzeProtrusions();

		return p.getImage();	
	}
	
	public ImagePlus getImageBaseShape(ImagePlus p) {
		BaseShape bs = new BaseShape();
		bs.identifyBaseShape(originalImg, p);
		bs.analyzeBaseShape();
		return bs.getImage();
	}

	public Species extractFeatures(boolean isIJ) {
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

		Species s = new Species();
		s.setFeatureLabels(featureExtraction.getFeatureLabels());
		s.setFeatureValues(featureExtraction.getFeatureValues());
		s.setParticleLabels(pa.getFeatureLabels());
		s.setParticleValues(pa.getFeatureValues());
		
		return s;
	}
}
