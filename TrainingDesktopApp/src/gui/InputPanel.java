package gui;

import javax.swing.JPanel;

public class InputPanel {
	private JPanel panel;

	public InputPanel(int mode) {
		switch(mode) {
		case 1:
			panel = decisionTreeInputPanel();
			break;
		case 2:
			panel = svmInputPanel();
			break;
		default: break;
		}
	}

	public JPanel getInputPanel() {
		return panel;
	}

	private JPanel decisionTreeInputPanel() {
		return null;
	}

	private JPanel svmInputPanel() {
		return null;
	}
}
