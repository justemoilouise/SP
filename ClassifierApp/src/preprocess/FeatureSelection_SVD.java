package preprocess;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class FeatureSelection_SVD {
	private double[][] dataset;
	private SingularValueDecomposition SVD;
	
	public FeatureSelection_SVD() {}
	
	public FeatureSelection_SVD(double[][] dataset) {
		this.dataset = dataset;
	}
	
	public void getSVD() {
		Matrix m = convertToMatrix(dataset);
		SVD = new SingularValueDecomposition(m);
	}
	
	public double[][] getU() {
		Matrix m = SVD.getU();
		
		return m.getArray();
	}
	
	public double[][] getV() {
		Matrix m = SVD.getV();
		
		return m.getArray();
	}
	
	private Matrix convertToMatrix(double[][] arr) {
		return new Matrix(arr);
	}
}
