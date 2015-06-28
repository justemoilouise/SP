package ImageProcessing;

import java.util.ArrayList;
import java.util.Arrays;

import ImageHandlers.ProcessImage;
import Helpers.ValueHelper;
import ij.ImagePlus;
import ij.process.BinaryProcessor;
import ij.process.ImageProcessor;

public class BaseShape {
	private ImagePlus img;
	private FeatureExtraction fe;
	private ParticleAnalysis pa;
	ArrayList<double[]> features;

	public BaseShape() {
		this.img = null;
		this.fe = new FeatureExtraction();
		this.pa = new ParticleAnalysis();
		this.features = new ArrayList<double[]>();
	}

	public void identifyBaseShape(ImagePlus ip1, ImagePlus ip2) {

		// subtract background
		ImagePlus imp1 = ProcessImage.subtractBackground(ip1.duplicate());
		ImagePlus imp2 = ProcessImage.subtractBackground(ip2.duplicate());

		// make binary
		BinaryProcessor bin1 = new BinaryProcessor(imp1.getProcessor().convertToByteProcessor());
		bin1.autoThreshold();
		BinaryProcessor bin2 = new BinaryProcessor(imp2.getProcessor().convertToByteProcessor());
		bin2.autoThreshold();

		// subtract images
		ImagePlus img1 = ProcessImage.getImageSubtract(
				new ImagePlus(imp1.getTitle() + " - Binary", bin1),
				new ImagePlus(imp2.getTitle() + " - Binary", bin2));

		// remove outliers then smooth
		ImageProcessor img2 = ProcessImage.removeOutliers(img1.getProcessor());
		img2.smooth();
		
		this.img = new ImagePlus(img1.getTitle() + " - Remove outliers and Smooth", img2);
	}

	public void analyzeBaseShape() {
//		fe.getShapeAndAreaDescriptors(img);
		fe.getTextureDescriptors(img.getProcessor());
		pa.analyzeParticleShapeAndAreaWithHoles(img);
//		pa.analyzeParticleShape(img);
		
		ArrayList<double[]> f = pa.getFeatureValues();
		for(double[] arr : f) {
			if(ValueHelper.IsValidBaseShapeFeature(arr)) {
				features.add(arr);
			}
		}
		
	}

	public ImagePlus getImage() {
		return img;
	}

	public String[] getBaseFeatureLabels() {
		return fe.getFeatureLabels();
	}

	public double[] getBaseFeatureValues() {
		return fe.getFeatureValues();
	}
	
	public ArrayList<double[]> getParticleFeatureValues() {
		return features;
	}

	public String[] getParticleFeatureLabels() {
		String[] labels = pa.getFeatureLabels();
		
		return Arrays.copyOfRange(labels, 1, labels.length);
	}
}
