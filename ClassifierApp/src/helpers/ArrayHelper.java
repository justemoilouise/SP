package helpers;

import java.util.ArrayList;
import java.util.Arrays;

import CoreHandler.MathFunctions;

public class ArrayHelper {

	public static int GetIndexOf(String[] arr, String e) {
		for(int i=0; i<arr.length; i++) {
			if(arr[i].equalsIgnoreCase(e)) {
				return i;
			}
		}

		return -1;
	}

	public static double GetFeatureAverage(ArrayList<double[]> list, int index) {
		double[] arr = new double[list.size()];
		int i = 0;

		for(double[] l : list) {
			arr[i] = l[index];
			i++;
		}

		return MathFunctions.GetAverage(arr);
	}

	public static double[] GetFeatureAverage(ArrayList<double[]> list) {
		double[] arr = new double[list.get(0).length-1];
		Arrays.fill(arr, 0);

		for(double[] l : list) {
			for(int i=1; i<l.length; i++) {
				arr[i-1] += l[i];
			}
		}
		
		for(int i=0; i<arr.length; i++) {
			arr[i] /= list.size();
		}

		return arr;
	}
}
