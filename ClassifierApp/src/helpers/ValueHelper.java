package helpers;

public class ValueHelper {
	private final static int circular = 1, conical = 2;		//shapes
	private final static int unknown = 0, horn = 1, spine = 2;
	
	public static int GetShape(double circularity) {
		if(circularity > 0.5)
			return circular;
		
		return conical;
	}
	
	public static int GetProtrusion(double circularity, double perimeter) {
		if(circularity < 0.4 && (perimeter > 500 && perimeter < 700))
			return spine;
		else if(circularity > 0.4 && (perimeter > 200 && perimeter < 500))
			return horn;
		
		return unknown;
	}
	
	public static boolean IsValidFeature(double[] arr) {
		if(arr[1] > 1000) {
			return true;
		}
		return false;
	}
}
