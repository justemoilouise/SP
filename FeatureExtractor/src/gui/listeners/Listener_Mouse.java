package gui.listeners;

import ij.ImagePlus;
import ij.gui.ImageWindow;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import CoreHandler.Prompt;
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
					boolean isIJ = Prompt.chooseFeatures(true);
					Client.setIsIJ(isIJ);
				}
				catch(Exception x) {}
			}
		}
		else if(command.equals("submit")) {
			boolean isValid = Client.validateInput();

			if(isValid) {
				Client.onSubmit();
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
