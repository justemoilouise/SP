package gui.listener;

import gui.InputPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import CoreHandler.Prompt;
import Data.Species;
import FileHandlers.FileInput;
import FileHandlers.FileOutput;
import core.Client;

public class Listener_Mouse implements ActionListener {

	public Listener_Mouse() {}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command = e.getActionCommand();

		if(command.equals("input_file")) {
			File f = FileInput.uploadExcelFile();
			
			if(f != null) {
				ArrayList<Species> dataset = FileInput.readSpecies(f);
				Client.setDataset(dataset);
			}
		}
		else if(command.equals("submit")) {
			InputPanel panel = (InputPanel) e.getSource();
			Hashtable<String, Double> params = panel.getParameters();
			
			Client.onSubmit(params);
		}
		else if(command.equals("save")) {
			FileOutput.saveToDATFile(Client.getModel());
		}
		else if(command.equals("quit")) {
			int choice = Prompt.ConfirmExit("main");
			
			if(choice == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}
}
