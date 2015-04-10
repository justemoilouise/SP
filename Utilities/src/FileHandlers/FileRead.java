package FileHandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import CoreHandler.Prompt;

public class FileRead {
	
	@SuppressWarnings("resource")
	public static String readFile(String fileName) {
		File f = new File(fileName);
		StringBuilder contents = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			while(reader.ready()) {
				contents.append(reader.readLine() + "\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Prompt.PromptError("ERROR_GET_TUTORIAL");
		}

		return contents.toString();
	}
}
