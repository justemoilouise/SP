package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import core.Client;
import Data.Species;
import FileHandlers.FileInput;

public class Listener_Mouse implements ActionListener {

	public Listener_Mouse() {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if(command.equals("input_excel")) {
			File f = FileInput.uploadExcelFile();
			
			if(f != null) {
				ArrayList<Species> dataset = FileInput.readSpecies(f);
				
				Client.setDataset(dataset);
			}
		} else if(command.equals("input_image")) {
			File[] f = FileInput.uploadImageFiles();
			
			if(f != null) {
				ArrayList<Species> dataset = FileInput.readSpecies(f);
				
				Client.setDataset(dataset);
			}
		} else if(command.equals("submit")) {
			Client.trainClassifier();
		} else if(command.equals("cancel")) {
			
		}
	}

}
