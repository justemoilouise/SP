package helpers;

public class ArrayHelper {

	public static int GetIndexOf(String[] arr, String e) {
		for(int i=0; i<arr.length; i++) {
			if(arr[i].equalsIgnoreCase(e)) {
				return i;
			}
		}
		
		return -1;
	}
}
