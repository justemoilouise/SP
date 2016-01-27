package gui;

import gui.listeners.Listener_Mouse;

import javax.swing.JInternalFrame;

@SuppressWarnings("serial")
public class ParametersPanel extends JInternalFrame {
	private Listener_Mouse lm;
	
	public ParametersPanel() {
		this.lm = new Listener_Mouse();
	}
}
