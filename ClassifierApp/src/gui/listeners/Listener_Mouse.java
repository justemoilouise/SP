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
import javax.swing.JOptionPane;

import CoreHandler.Prompt;
import Data.Input;
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
				catch(Exception x) {
					//Prompt.PromptError("ERROR_UPLOAD_IMAGE");		
					//Client.printStackTrace(x);
				}
			}
		}
		else if(command.equals("input_file")) {
			FileInput ifp = new FileInput();
			
			File f = ifp.uploadFile();
			
			if(f != null) {
				ArrayList<Input> input = ifp.readInput(f);
				
				Client.setInputs(input);
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
		else if(command.startsWith("download")) {
			int marker = command.indexOf("_") + 1;
			int index = Integer.parseInt(command.substring(marker, command.length()));

			Client.download(index);
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
