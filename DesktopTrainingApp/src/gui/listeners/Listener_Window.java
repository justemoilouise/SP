package gui.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.JOptionPane;

import core.Client;
import CoreHandler.Prompt;
import FileHandlers.FileConfig;

public class Listener_Window implements WindowListener {

	public Listener_Window() {}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		int choice = Prompt.ConfirmExit("main");
		
		if(choice == JOptionPane.YES_OPTION) {
			Properties props = Client.getProperties();
			props.setProperty("model.version", Double.toString(Client.modelVersion));
			FileConfig.saveToFile(props);
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
