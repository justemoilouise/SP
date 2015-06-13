package helpers;

public class ValueHelper {
	private final static int circular = 1, conical = 2;		//shapes
	
	public static int GetShape(double circularity) {
		if(circularity > 0.5)
			return circular;
		
		return conical;
	}
	
	public static boolean IsValidFeature(double[] arr) {
		if(arr[1] > 1000) {
			return true;
		}
		return false;
	}
}
