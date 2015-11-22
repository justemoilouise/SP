package gui.listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JOptionPane;

import CoreHandler.Prompt;

public class Listener_Window implements WindowListener {

	public Listener_Window() {}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		int choice = Prompt.ConfirmExit("main");
		
		if(choice == JOptionPane.YES_OPTION) {
			File file = new File("tmp");
			String[]entries = file.list();
			for(String s: entries){
			    File currentFile = new File(file.getPath(), s);
			    currentFile.delete();
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
