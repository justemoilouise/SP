package featureExtraction;

import ij.process.ImageProcessor;
import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;

public class JFeature {
	private Haralick h;
	private String[] featureLabels = {"Angular second moment", "Contrast", "Correlation", "Sum of Squares: Variance", "Inverse difference moment", "Sum average", "Sum Variance",
			"Sum entropy", "Entropy", "Difference Variance", "Difference entropy", "Info. measure of Correlation 1", "Info. measure of Correlation 2", "Max. correlation coefficient"};
	private double[] featuresValues = null;
	
	public JFeature() {}
	
	public void getHaralickDescriptor(ImageProcessor ip) {
		h = new Haralick();
		h.run(ip);
		
		featuresValues = h.getFeatures().get(0);
	}
	
	public double[] getFeatureValues() {
		return featuresValues;
	}
	
	public String[] getFeatureLabels() {
		return featureLabels;
	}
}
