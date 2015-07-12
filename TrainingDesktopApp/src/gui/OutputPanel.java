package gui;

import javax.swing.JTabbedPane;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class OutputPanel extends JTabbedPane {
	private JTabbedPane tabbedPane;
	private ClassifierModel model;
	
	public OutputPanel(ClassifierModel model) {
		this.model = model;
		this.tabbedPane = new JTabbedPane();
		
		decisionTreeOutputPanel();
		svmOutputPanel();
	}
	
	public JTabbedPane getOutputPanel() {
		return tabbedPane;
	}

	private void decisionTreeOutputPanel() {
		if(model.getDecisionTreeModel() != null) {
			
		}
	}

	private void svmOutputPanel() {
		if(model.getSvmmodel() != null) {
			
		}
	}
}
