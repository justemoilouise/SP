package gui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class OutputPanel extends JPanel {
	private JPanel panel;
	
	public OutputPanel(int mode) {
		switch(mode) {
		case 1:
			panel = decisionTreeOutputPanel();
			break;
		case 2:
			panel = svmOutputPanel();
			break;
		default: break;
		}
	}

	public JPanel getOutputPanel() {
		return panel;
	}

	private JPanel decisionTreeOutputPanel() {
		return null;
	}

	private JPanel svmOutputPanel() {
		return null;
	}
}
