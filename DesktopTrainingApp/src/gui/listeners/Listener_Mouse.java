package gui.listeners;

import gui.AboutWindow;
import gui.TutorialWindow;
import ij.ImagePlus;
import ij.gui.ImageWindow;

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
					ImageWindow imgWindow = new ImageWindow(imgPlus);
					Client.setImgPlus(imgPlus);
					Client.getPm().addToDesktopPane(imgWindow);
				}
				catch(Exception x) {}
			}
		}
		else if(command.equals("input_file")) {			
			File f = FileInput.uploadExcelFile();
			if(f != null) {
				ArrayList<Species> input = FileInput.readSpecies(f);
				Client.setImgPlus(null);
				Client.setTrainingSet(input);
			}
		}
		else if(command.equals("image_extractFeatures")) {
			Client.getFeatures();
		}
		else if(command.equals("build_model")) {
			Client.onSubmit();
		}
		else if(command.equals("upload_model")) {
			File f = FileInput.uploadModelFile();
			
			if(f != null) {
//				ClassifierModel model = FileInput.readModelFromDATFile(f);
//				FileOutput.saveToFile(model, model.isIJUsed());
//				FileConfig.updateModelInfo(model);
//				Client.setModel(model);
//				Client.getPm().appendToConsole("Classifier model updated to version " + model.getVersion() + "..");
//				Prompt.PromptSuccess("SUCCESS_UPLOAD_FILE");
			}
		}
		else if(command.equals("save_model")) {
//			boolean isValid = Client.validateInput();
//
//			if(isValid) {
//				boolean isIJ = Prompt.chooseFeatures(true);
//				Client.getFeatures(isIJ);
//			}
//			else {
//				Prompt.PromptError("ERROR_INPUT");
//			}
		}
//		else if(command.equals("stop")) {
//			boolean isValid = Client.validateInput();
//
//			if(isValid) {
//				int choice = Prompt.ConfirmExit("progress");
//				
//				if(choice == JOptionPane.YES_OPTION) {
//					Client.stop();
//				}
//			}
//			else {
//				Prompt.PromptError("ERROR_INPUT");
//			}
//		}
		else if(command.startsWith("download")) {
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
