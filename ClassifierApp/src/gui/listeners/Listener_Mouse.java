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
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import CoreHandler.Prompt;
import Data.ClassifierModel;
import Data.Input;
import FileHandlers.FileConfig;
import FileHandlers.FileInput;
import FileHandlers.FileOutput;
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
				ArrayList<Input> input = FileInput.readInput(f);
				Client.setImgPlus(null);
				Client.addInput(input.get(0));
				boolean isValid = Client.validateInput();
				if(isValid) {
					boolean isIJ = Prompt.chooseFeatures(true);
					Client.classify(isIJ);
					Client.displayInput();
					Client.displayImage();
				}
				else {
					Prompt.PromptError("ERROR_INPUT");
				}
			}
		}
		else if(command.equals("upload_model")) {
			File f = FileInput.uploadModelFile();
			
			if(f != null) {
				ClassifierModel model = FileInput.readModelFromDATFile(f);
				FileOutput.saveToFile(model, model.isIJUsed());
				FileConfig.updateModelInfo(model);
				Client.setModel(model);
				Client.getPm().appendToConsole("Classifier model updated to version " + model.getVersion() + "..");
				Prompt.PromptSuccess("SUCCESS_UPLOAD_FILE");
			}
		}
		else if(command.equals("classify_image")) {
			boolean isValid = Client.validateInput();

			if(isValid) {
				boolean isIJ = Prompt.chooseFeatures(true);
				Client.classify(isIJ);
				Client.displayImage();
			}
			else {
				Prompt.PromptError("ERROR_INPUT");
			}
		}
		else if(command.equals("view_output")) {
			Client.displayOutput();
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
		else if(command.startsWith("download")) {
			JFileChooser fc = new JFileChooser();
			int dload = fc.showSaveDialog(null);
			
			if(dload == JFileChooser.APPROVE_OPTION) {
				String filename = fc.getSelectedFile().getAbsolutePath();
				int marker = command.indexOf("_") + 1;
				int index = Integer.parseInt(command.substring(marker, command.length()));

				Client.download(index, filename);
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
		else if(command.equals("adjust_threshold")) {
			double t = Client.getSvm().threshold;
			String newValue = JOptionPane.showInputDialog("Input classification threshold: ", t);
			if(Double.parseDouble(newValue) != t) {
				Client.getSvm().threshold = Double.parseDouble(newValue);
				Client.getPm().appendToConsole("Classification threshold is now " + newValue + "..");
				
				//update properties file
				Properties props = Client.getProperties();
				props.setProperty("model.threshold", newValue);
				FileConfig.saveToFile(props);
			}
		}
	}
}
