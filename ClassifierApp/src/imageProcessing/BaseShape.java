package imageProcessing;

import ImageHandlers.ProcessImage;
import ij.ImagePlus;
import ij.process.BinaryProcessor;
import ij.process.ImageProcessor;

public class BaseShape {
	private ImagePlus img;
	private FeatureExtraction fe;

	public BaseShape() {
		this.img = null;
		this.fe = new FeatureExtraction();
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
		img.show();
	}

	public void analyzeBaseShape() {
		fe.getShapeDescriptors(img);
	}

	public ImagePlus getImage() {
		return img;
	}

	public String[] getFeatureLabels() {
		return fe.getFeatureLabels();
	}

	public double[] getFeatureValues() {
		return fe.getFeatureValues();
	}
}
