package preprocess;

import java.util.ArrayList;

import CoreHandler.MathFunctions;
import Data.Species;

public class FeatureSelection_PCATransform extends Preprocess {
	private int PC = 2;
	private double[][] trainingSet;
	private double[] mean;
	private double[][] principalComponents;
	
	public FeatureSelection_PCATransform() {
		super();
	}
	
	public FeatureSelection_PCATransform(int PC) {
		super();
		
		this.PC = PC;
	}
	
	public FeatureSelection_PCATransform(ArrayList<Species> dataset_raw) {
		super(dataset_raw);
	}
	
	public FeatureSelection_PCATransform(ArrayList<Species> dataset_raw, int PC) {
		super(dataset_raw);
		
		this.PC = PC;
	}
	
	public void transform() {
		double[][] dataset = null;
		this.trainingSet = MathFunctions.Transpose(dataset);
		
		double[][] phi = calculatePhi();
		
		FeatureSelection_SVD svd = new FeatureSelection_SVD(phi);
		svd.getSVD();
		
		double[][] u = svd.getU();
		double[][] pc = MathFunctions.GetMatrixColumns(u, 0, PC);
		principalComponents = MathFunctions.Transpose(pc);
		double[][] features = MathFunctions.MatrixMultiplication(principalComponents, phi);
		
		//updateDataset(MathFunctions.Transpose(features));
	}
	
	public double[] getReducedFeatures(double[] input) {
		double[] phi = MathFunctions.MatrixSubtraction(input, mean);
		double[][] features = MathFunctions.MatrixMultiplication(principalComponents, phi);
		
		return MathFunctions.TransposeTo1D(features);
	}
	
	private double[][] calculatePhi() {
		mean = MathFunctions.GetMatrixMean(trainingSet);
		
		return MathFunctions.MatrixSubtraction(trainingSet, mean);
	}
}
