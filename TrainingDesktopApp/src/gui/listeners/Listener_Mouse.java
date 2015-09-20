package gui.listeners;

import helpers.DataHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import core.Client;
import CoreHandler.Prompt;
import Data.Species;
import FileHandlers.FileInput;

public class Listener_Mouse implements ActionListener {

	public Listener_Mouse() {}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();
		
		if(command.equals("init_cancel")) {
			System.exit(0);
		} else if(command.equals("init_ok")) {
			JCheckBox[] cb = Client.getModes();
			
			if(cb[0].isSelected() && cb[1].isSelected()) {
				Client.setMode(new int[] {1, 2});
			} else if(cb[0].isSelected()) {
				Client.setMode(new int[] {1});
			} else {
				Client.setMode(new int[] {2});
			}
			
			Client.showMainWindow();
		} else if(command.equals("input_excel")) {
			File f = FileInput.uploadExcelFile();
			
			if(f != null) {
				Client.setTextFieldText(f.getName());
				
				ArrayList<Species> dataset = FileInput.readSpecies(f);
				
				Client.setDataset(dataset);
				Client.enableButtonPanels();
			}
		} else if(command.equals("input_image")) {
			File[] f = FileInput.uploadImageFiles();
			
			if(f != null) {
				Client.setTextFieldText(DataHelper.GetFileNames(f));
				
				ArrayList<Species> dataset = FileInput.readSpecies(f);
				
				Client.setDataset(dataset);
				Client.enableButtonPanels();
			}
		} else if(command.equals("next")) {
			Client.next();
		} else if(command.equals("prev")) {
			Client.prev();
		} else if(command.equals("save")) {
			Client.uploadModel();
		} else if(command.equals("cancel")) {
			int choice = Prompt.ConfirmExit("cancel");
			
			if(choice == JOptionPane.YES_OPTION) {
				Client.showMainWindow();
			}
		} else if(command.equals("close")) {
			int choice = Prompt.ConfirmExit("main");
			
			if(choice == JOptionPane.YES_OPTION) {			
				System.exit(0);
			}
		}
	}

}
