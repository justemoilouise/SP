package imageProcessing;

import java.util.ArrayList;

import ImageHandlers.ProcessImage;
import ij.ImagePlus;
import ij.process.ByteProcessor;

public class Protrusions {
	private ImagePlus img;
	private ParticleAnalysis pa;
	
	public Protrusions() {
		this.pa = new ParticleAnalysis();
	}
	
	public void identifyProtrusions(ImagePlus ip1, ImagePlus ip2) {
		
		// subtract background
		ImagePlus img1 = ProcessImage.subtractBackground(ip1.duplicate());
		ImagePlus img2 = ProcessImage.subtractBackground(ip2.duplicate());
		
		img1.show();
		img2.show();
		
		// get image difference
		ImagePlus ip = ProcessImage.getImageDifference(img1, img2);
		
		ip.show();
		
		// make binary
		ByteProcessor bp = new ByteProcessor(ip.duplicate().getProcessor(), false);
		bp.autoThreshold();

		// remove outliers
		bp = (ByteProcessor) ProcessImage.removeOutliers(bp);
		
		// smoothen
		bp.smooth();
		
		this.img = new ImagePlus(ip.getTitle(), bp);
	}
	
	public void analyzProtrusions() {
		pa.analyzeParticles(img);
	}
	
	public ImagePlus getImage() {
		return img;
	}
	
	public ArrayList<double[]> getFeatureValues() {
		return pa.getFeatureValues();
	}

	public String[] getFeatureLabels() {
		return pa.getFeatureLabels();
	}
}
