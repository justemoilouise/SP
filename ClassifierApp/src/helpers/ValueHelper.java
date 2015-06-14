package helpers;

public class ValueHelper {
	private final static int circular = 1, conical = 2;		//shapes
	private final static int horn = 1, spine = 2;
	
	public static int GetShape(double circularity) {
		if(circularity > 0.5)
			return circular;
		
		return conical;
	}
	
	public static int GetProtrusion(double circularity, double perimeter) {
		if(circularity < 0.5 && (perimeter > 600 && perimeter < 700))
			return spine;
		
		return horn;
	}
	
	public static boolean IsValidFeature(double[] arr) {
		if(arr[1] > 5000) {
			return true;
		}
		return false;
	}
}
