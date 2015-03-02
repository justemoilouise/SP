package gui.listener;

import ij.ImagePlus;
import ij.gui.ImageWindow;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CoreHandler.Prompt;
import Data.Species;
import FileHandlers.FileInput;
import ImageHandlers.ProcessImage;
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
				ArrayList<Species> speciesList = FileInput.readSpecies(f);
			}
		}
		else if(command.equals("submit")) {
			boolean isValid = Client.validateInput();

			if(isValid) {
				//Prompt.ChooseFeatures();

				boolean isIJ = Prompt.isIJ();			
				Client.onSubmit(isIJ);
			}
			else {
				Prompt.PromptError("ERROR_INPUT");
			}
		}
		else if(command.equals("IJ")) {
			boolean isIJ = Prompt.isIJ();

			if(isIJ) {
				Prompt.SetIJSelected(true);
			}
			else {
				Prompt.SetIJSelected(false);
			}
		}
		else if(command.equals("JF")) {
			boolean isIJ = Prompt.isIJ();

			if(isIJ) {
				Prompt.SetIJSelected(false);
			}
			else {
				Prompt.SetIJSelected(true);
			}
		}
		else if(command.equals("stop")) {
			boolean isValid = Client.validateInput();

			if(isValid) {
				int choice = Prompt.ConfirmExit("progress");
				
				if(choice == JOptionPane.YES_OPTION) {
					Client.stop();
				}
			}
			else {
				Prompt.PromptError("ERROR_INPUT");
			}
		}
		else if(command.equals("quit")) {
			int choice = Prompt.ConfirmExit("main");
			
			if(choice == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}
}
