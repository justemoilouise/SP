package gui;

import gui.listeners.Listener_Mouse;

import javax.swing.JInternalFrame;

@SuppressWarnings("serial")
public class FeaturesPanel extends JInternalFrame {
	private Listener_Mouse lm;
	
	public FeaturesPanel() {
		this.lm = new Listener_Mouse();
	}
}
