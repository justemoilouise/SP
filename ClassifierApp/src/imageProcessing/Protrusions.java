package imageProcessing;

import java.util.ArrayList;

import ImageHandlers.ProcessImage;
import helpers.ValueHelper;
import ij.ImagePlus;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;

public class Protrusions {
	private ImagePlus img;
	private ParticleAnalysis pa;
	private ArrayList<double[]> features;

	public Protrusions() {
		this.img = null;
		this.pa = new ParticleAnalysis();
		this.features = new ArrayList<double[]>();
	}

	public void identifyProtrusions(ImagePlus ip) {

		// subtract background
		ImagePlus imp = ProcessImage.subtractBackground(ip.duplicate());

		// make binary
		ByteProcessor bp = imp.duplicate().getProcessor().convertToByteProcessor();
		BinaryProcessor bin = new BinaryProcessor(bp);
		bin.autoThreshold();

		// remove outliers then smooth
		bp = (ByteProcessor) ProcessImage.removeOutliers(bin);
		bp.smooth();

		this.img = new ImagePlus(imp.getTitle() + " - Smooth", bin);
	}

	public void identifyProtrusions(ImagePlus ip1, ImagePlus ip2) {
		
		// subtract background
		ImagePlus imp1 = ProcessImage.subtractBackground(ip1.duplicate());
		ImagePlus imp2 = ProcessImage.subtractBackground(ip2.duplicate());

		// get image difference
		ImagePlus ip = ProcessImage.getImageDifference(imp1, imp2);

		// make binary
		ByteProcessor bp = new ByteProcessor(ip.duplicate().getProcessor(), false);
		BinaryProcessor bin = new BinaryProcessor(bp);
		bin.autoThreshold();

		// remove outliers, smooth
		bin = (BinaryProcessor) ProcessImage.removeOutliers(bin);
		bin.smooth();

		this.img = new ImagePlus(ip.getTitle() + " - Remove outliers and Smooth", bin);
	}

	public void analyzeProtrusions() {
		pa.analyzeParticleAreaPerimeterAndShape(img);
		
		ArrayList<double[]> f = pa.getFeatureValues();
		for(double[] arr : f) {
			if(ValueHelper.IsValidFeature(arr)) {
				features.add(arr);
			}
		}
	}

	public ImagePlus getImage() {
		return img;
	}

	public ArrayList<double[]> getFeatureValues() {
		return features;
	}

	public String[] getFeatureLabels() {
		return pa.getFeatureLabels();
	}
}
