package gui;

import gui.listeners.Listener_Mouse;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class OutputPanel extends JTabbedPane {
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private Listener_Mouse lm;
	private ClassifierModel model;

	public OutputPanel(ClassifierModel model) {
		this.model = model;
		this.panel = new JPanel();
		this.tabbedPane = new JTabbedPane();
		this.lm = new Listener_Mouse();

		decisionTreeOutputPanel();
		svmOutputPanel();
		
		panel.add(tabbedPane, BorderLayout.CENTER);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);
	}

	public JPanel getOutputPanel() {
		return panel;
	}

	private void decisionTreeOutputPanel() {
		if(model.getDecisionTreeModel() != null) {
			JTextPane textPane = new JTextPane();
			JPanel panel = new JPanel();
			panel.add(textPane);

			tabbedPane.add("Decision Tree", panel);
		}
	}

	private void svmOutputPanel() {
		if(model.getSvmmodel() != null) {
			JTextPane textPane = new JTextPane();
			JPanel panel = new JPanel();
			panel.add(textPane);

			tabbedPane.add("SVM", panel);
		}
	}
	
	private JPanel getButtonPanel() {
		JButton btnSubmit = new JButton("Save model");
		btnSubmit.setActionCommand("save");
		btnSubmit.addActionListener(lm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(lm);
		
		JPanel panel = new JPanel();
		panel.add(btnSubmit);
		panel.add(btnCancel);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		return panel;
	}
}
