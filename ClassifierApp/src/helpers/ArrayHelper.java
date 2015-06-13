package helpers;

import java.util.ArrayList;

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
}
