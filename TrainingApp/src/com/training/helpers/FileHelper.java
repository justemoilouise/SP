package com.training.helpers;

import FileHandlers.FileOutput;
import FileHandlers.FileRead;

public class FileHelper {
	private FileOutput output;
	private FileRead reader;
	
	public FileHelper() {
		this.output = new FileOutput();
		this.reader = new FileRead();
	}
}
