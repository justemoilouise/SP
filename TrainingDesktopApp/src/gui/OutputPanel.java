package gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class OutputPanel extends JTabbedPane {
	private JTabbedPane tabbedPane;
	
	public OutputPanel(int[] mode) {
		tabbedPane = new JTabbedPane();
		
		for(int i=0; i<mode.length; i++) {
			if(mode[i]==1) {
				tabbedPane.add("Decision Tree", decisionTreeOutputPanel());
			} else {
				tabbedPane.add("SVM", svmOutputPanel());
			}
		}
	}
	
	public JTabbedPane getOutputPanel() {
		return tabbedPane;
	}

	private JPanel decisionTreeOutputPanel() {
		return null;
	}

	private JPanel svmOutputPanel() {
		return null;
	}
}
