package FileHandlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import CoreHandler.Prompt;
import Data.ClassifierModel;

public class FileConfig {
	final static String configName = "settings/config.properties";

	public static Properties readConfig() {
		Properties props = new Properties();
		
		try {
			FileInputStream stream = new FileInputStream(configName);
			props.load(stream);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return props;
	}
	
	public static void updateModelInfo(ClassifierModel model) {
		Properties old = readConfig();
		old.setProperty("model.version", Double.toString(model.getVersion()));
		old.setProperty("model.createdDate", model.getCreatedDate().toString());
		old.setProperty("model.uploadDate", new Date().toString());
		old.setProperty("model.used", "true");
		saveToFile(old);
	}
	
	public static void saveToFile(Properties props) {
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
