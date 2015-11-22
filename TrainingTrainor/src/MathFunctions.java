

import java.util.Arrays;


public class MathFunctions {
	
	public static double GetAverage(double[] arr) {
		double sum = 0;

		for(int i=0; i<arr.length; i++)
			sum += arr[i];

		return sum/arr.length;
	}

	public static double[] ToArray(double[][] matrix, int dim) {
		double[] arr = new double[dim];
		double[][] arrT = Transpose(matrix);
		
		for(int i=0; i<arrT.length; i++) {
			int index = i*arrT[i].length;
			System.arraycopy(arrT[i], 0, arr, index, arrT[i].length);
		}
		
		return arr;
	}
	
	public static double[] GetMatrixMean(double[][] matrix) {
		double[] means = new double[matrix.length];
		
		for(int i=0; i<matrix.length; i++) {
			means[i] = GetAverage(matrix[i]);
		}
		
		return means;
	}
	
	public static double[][] GetMatrixColumns(double[][] matrix, int colStart, int colEnd) {
		int cols = colEnd-colStart;
		double[][] trimmedMatrix = new double[matrix.length][cols];

		for(int i=0; i<matrix.length; i++) {
			int k = 0;
			for(int j=colStart; j<colEnd; j++) {
				trimmedMatrix[i][k] = matrix[i][j];
				k++;
			}
		}
		
		return trimmedMatrix;
	}
	
	public static double[] TransposeTo1D(double[][] data) {
		double[] arr = new double[data.length];

		for(int i=0; i<data.length; i++) {
				arr[i] = data[i][0];
		}
		return arr;
	}
	
	public static double[][] Transpose(double[][] data) {
		double[][] arr = new double[data[0].length][data.length];

		for(int i=0; i<data.length; i++) {
			for(int j=0; j<data[0].length; j++)
				arr[j][i] = data[i][j];
		}
		return arr;
	}
		
	public static double[][] ToMatrix(double x, int nCols, int nRows) {
		double[][] matrix = new double[nRows][nCols];
		
		for(int i=0; i<nRows; i++) {
			for(int j=0; j<nCols; j++) {
				matrix[i][j] = x;
			}
		}
		
		return matrix;
	}
	
	public static double[] MatrixSubtraction(double[] matrixA, double[] matrixB) {
		double[] diff = new double[matrixA.length];
		
		for(int i=0; i<matrixA.length; i++) {
			diff[i] = matrixA[i] - matrixB[i];
		}
		
		return diff;
	}
	
	public static double[][] MatrixSubtraction(double[][] matrixA, double[] matrixB) {
		double[][] diff = new double[matrixA.length][matrixA[0].length];
		
		for(int i=0; i<matrixA.length; i++) {
			for(int j=0; j<matrixA[0].length; j++) {
				diff[i][j] = matrixA[i][j] - matrixB[i];
			}
		}
		
		return diff;
	}
	
	public static double[][] MatrixMultiplication(double[][] matrixA, double[] matrixB) {
		double[][] product = new double[matrixA.length][1];
		
		for (int i = 0; i < matrixA.length; i++) {
            	product[i][0] = 0;
            	
                for (int k = 0; k < matrixA[0].length; k++) {
                    product[i][0] += matrixA[i][k] * matrixB[k];
                }
        }
		
		return product;
	}
	
	public static double[][] MatrixMultiplication(double[][] matrixA, double[][] matrixB) {
		double[][] product = new double[matrixA.length][matrixB[0].length];
		
		for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
            	product[i][j] = 0;
            	
                for (int k = 0; k < matrixA[0].length; k++) {
                    product[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
		
		return product;
	}
	
	public static double[] GetMinimum(double[][] data) {
		double[] arr = new double[data.length];
		
		for(int i=0; i<data.length; i++) {
			double[] temp = data[i].clone();
			Arrays.sort(temp);
			arr[i] = temp[0];
		}
		
		return arr;
	}
	
	public static double[] GetMaximum(double[][] data) {
		double[] arr = new double[data.length];
		
		for(int i=0; i<data.length; i++) {
			double[] temp = data[i].clone();
			Arrays.sort(temp);
			arr[i] = temp[temp.length-1];
		}
		
		return arr;
	}
}
