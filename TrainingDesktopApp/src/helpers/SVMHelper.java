package helpers;

public class SVMHelper {
	private static String[] parameters = new String[] {"Cost", "Epsilon", "Gamma", "Nu", "Degree", "Coefficient"};
	private static String[] svmTypes = new String[] {"C-SVC", "nu-SVC", "one-class SVM", "epsilon-SVR", "nu-SVR"};
	private static String[] kernel = new String[] {"Linear", "Polynomial", "Radial basis", "Sigmoid"};

	public static String[] GetParametrs() {
		return parameters;
	}
	
	public static String[] GetSvmTypes() {
		return svmTypes;
	}
	
	public static String[] GetKernels() {
		return kernel;
	}
}
