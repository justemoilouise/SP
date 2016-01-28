package core;

import java.util.ArrayList;
import java.util.Iterator;

public class ValueHelper {
	
	public static String[] SVMParameters() {
		return new String[] { "SVM Type", "Kernel", "Cost", "Gamma", "Epsilon",
				"Degree", "Nu", "Coefficient" };
	}
	
	public static String[] SVMTypes() {
		return new String[] { "C-SVC", "nu-SVC", "one-class SVM", "epsilon-SVR", "nu-SVR" };
	}
	
	public static String[] SVMKernelFunctions() {
		return new String[] { "Linear", "Polynomial", "Radial basis", "Sigmoid" };
	}
	
	public static String ToString(ArrayList<String> list) {
		StringBuilder str = new StringBuilder();
		Iterator<String> it = list.iterator();
		while(it.hasNext()) {
			str.append(it.next());
			str.append(", ");
		}
		System.out.println(str.toString());
		return str.toString().substring(0, str.length()-2);
	}
}
