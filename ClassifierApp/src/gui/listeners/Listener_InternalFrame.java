package gui.listeners;

import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import CoreHandler.Prompt;
import core.Client;

public class Listener_InternalFrame implements InternalFrameListener {

	public Listener_InternalFrame() {}
	
	@Override
	public void internalFrameActivated(InternalFrameEvent arg0) {}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		int choice = Prompt.ConfirmExit("progress");
		
		if(choice == JOptionPane.YES_OPTION) {
			Client.stop();
			System.exit(0);
		}
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent arg0) {}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent arg0) {}

	@Override
	public void internalFrameIconified(InternalFrameEvent arg0) {}

	@Override
	public void internalFrameOpened(InternalFrameEvent arg0) {}
}
