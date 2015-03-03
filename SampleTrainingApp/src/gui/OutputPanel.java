package gui;

import gui.listener.Listener_Mouse;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class OutputPanel extends JInternalFrame {
	private Listener_Mouse lm;
	
	public OutputPanel(ClassifierModel model) {
		this.lm = new Listener_Mouse();
		
		JLabel label = new JLabel(Double.toString(model.getSvmmodel().getAccuracy()));
		add(label);
		
		JButton button = new JButton("SAVE");
		button.setActionCommand("save");
		button.addActionListener(lm);
		add(button);
	}
}
