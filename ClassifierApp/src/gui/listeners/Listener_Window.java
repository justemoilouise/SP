package gui.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JOptionPane;

import CoreHandler.Prompt;
import core.Client;

public class Listener_Window implements WindowListener {

	public Listener_Window() {}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		int choice = Prompt.ConfirmExit("main");
		
		if(choice == JOptionPane.YES_OPTION) {
			int count = Client.getCount();

			for(int i=1; i<=count; i++) {
				File img = new File("img/"+i+".png");

				if(img.exists()) {
					img.delete();
				}
			}

			File somImg = new File("img/species.png");

			if(somImg.exists()) {
				somImg.delete();
			}
			
			System.exit(0);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
