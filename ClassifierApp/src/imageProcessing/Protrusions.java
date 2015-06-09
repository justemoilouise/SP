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

	public void identifyProtrusions(ImagePlus ip) {

		// subtract background
		ImagePlus imp = ProcessImage.subtractBackground(ip.duplicate());
		imp.show();

		// make binary
		ByteProcessor bp = new ByteProcessor(imp.duplicate().getProcessor(), false);
		bp.autoThreshold();

		(new ImagePlus(imp.getTitle() + " - Binary", bp)).show();

		// remove outliers
		bp = (ByteProcessor) ProcessImage.removeOutliers(bp);

		// smoothen
		bp.smooth();

		this.img = new ImagePlus(imp.getTitle() + " - Smooth", bp);
		img.show();
	}

	public void identifyProtrusions(ImagePlus ip1, ImagePlus ip2) {

		// subtract background
		ImagePlus imp1 = ProcessImage.subtractBackground(ip1.duplicate());
		imp1.show();
		ImagePlus imp2 = ProcessImage.subtractBackground(ip2.duplicate());
		imp2.show();

		// get image difference
		ImagePlus ip = ProcessImage.getImageDifference(imp1, imp2);
		ip.show();

		// make binary
		ByteProcessor bp = new ByteProcessor(ip.duplicate().getProcessor(), false);
		bp.autoThreshold();

		(new ImagePlus(ip.getTitle() + " - Binary", bp)).show();

		// remove outliers
		bp = (ByteProcessor) ProcessImage.removeOutliers(bp);

		// smoothen
		bp.smooth();

		this.img = new ImagePlus(ip.getTitle() + " - Smooth", bp);
		img.show();
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
