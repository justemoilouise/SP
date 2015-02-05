package FileHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Properties;

import CoreHandler.Prompt;

public class FileConfig {
	final static String configName = "settings/config.properties";

	public static Properties readConfig() {
		try {
			File config = new File(configName);
			InputStream is = new FileInputStream(config);
			//InputStream is = FileConfig.class.getResourceAsStream(configName);
			
			Properties props = new Properties();
			props.load(is);
			
			return props;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void updateConfig(Hashtable<String, Double> params, String mode) {
		Properties old = readConfig();
		
		if(mode.equals("SVM"))
			updateSVM(old, params);
		
		saveToFile(old);
	}
	
	private static void updateSVM(Properties old, Hashtable<String, Double> params) {
		old.setProperty("svm.type", params.get("SVM type").toString());
		old.setProperty("svm.kernel", params.get("SVM Kernel").toString());
		old.setProperty("svm.cost", params.get("Cost").toString());
		old.setProperty("svm.degree", params.get("Degree").toString());
		old.setProperty("svm.nu", params.get("Nu").toString());
		old.setProperty("svm.epsilon", params.get("Epsilon").toString());
	}
	
	private static void saveToFile(Properties props) {
		try {
			File config = new File(configName);
			config.delete();
			OutputStream os = new FileOutputStream(config);
			
			props.store(os, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Prompt.PromptError("ERROR_UPDATE_FILE");
		}
	}
}
