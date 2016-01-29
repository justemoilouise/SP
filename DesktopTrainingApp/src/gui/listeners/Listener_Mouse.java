package gui.listeners;

import gui.AboutWindow;
import gui.TutorialWindow;
import ij.ImagePlus;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
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

		if(command.equals("input_image")) {
			File f = ProcessImage.upload();
			if(f!=null) {
				try {
					Image img = ImageIO.read(f);
					ImagePlus imgPlus = new ImagePlus(f.getName(), img);
					Client.setImgPlus(imgPlus);
				}
				catch(Exception x) {}
			}
		}
		else if(command.equals("input_file")) {
			Client.getPm().appendToConsole("Reading file..");
			File f = FileInput.uploadExcelFile();
			if(f != null) {
				ArrayList<Species> input = FileInput.readSpecies(f);
				Client.setTrainingSet(input);
				Client.displayTrainingSet();
			}
		}
		else if(command.equals("get_isIJ")) {
			Client.isIJ = Prompt.chooseFeatures(Client.isIJ);
			String message = Client.isIJ ? "Shape and basic textures features used" : "Shape and Haralick texture descriptors used";
			Client.getPm().appendToConsole(message);
		}
		else if(command.equals("image_extractFeatures")) {
			if(Client.imgPlus != null) {
				String name = Prompt.GetSpeciesName();
				Client.getFeatures(name);
			}
			else Prompt.PromptError("ERROR_INPUT");
		}
		else if(command.equals("get_parameters")) {
			Client.getParameters();
		}
		else if(command.equals("build_model")) {
			boolean isValid = Client.validateInput();
			if(isValid)  {
				Client.onSubmit();
				Client.displayOutput();
			}
			else Prompt.PromptError("ERROR_INPUT");
		}
		else if(command.equals("save_model")) {
			JFileChooser fc = new JFileChooser();
			int dload = fc.showSaveDialog(null);
			
			if(dload == JFileChooser.APPROVE_OPTION) {
				String filename = fc.getSelectedFile().getAbsolutePath();
				Client.saveModel(filename);
			}
		}
		else if(command.startsWith("export")) {
			JFileChooser fc = new JFileChooser();
			int dload = fc.showSaveDialog(null);
			
			if(dload == JFileChooser.APPROVE_OPTION) {
				String filename = fc.getSelectedFile().getAbsolutePath();
				Client.exportTrainingSet(filename);
			}
		}
		else if(command.equals("view_tutorial")) {
			TutorialWindow tw = new TutorialWindow();
			Client.getPm().addToDesktopPane(tw);
		}
		else if(command.equals("view_about")) {
			AboutWindow aw = new AboutWindow();
			Client.getPm().addToDesktopPane(aw);
		}
		else if(command.equals("quit")) {
			int choice = Prompt.ConfirmExit("main");
			
			if(choice == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}
}
