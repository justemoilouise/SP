package core;

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
}
