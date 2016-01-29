package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Iterator;

import gui.listeners.Listener_Mouse;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class OutputPanel extends JInternalFrame {
	private Listener_Mouse lm;
	private ClassifierModel model;
	
	public OutputPanel(ClassifierModel model) {
		this.lm = new Listener_Mouse();
		this.model = model;
		add(getResults());
		add(buttonPanel());
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setTitle("New classifier model");;
		setName("Output");
		setBounds(10, 10, 1000, 450);
		setClosable(true);
		setIconifiable(true);
	}
	
	private JTabbedPane getResults() {
		JTabbedPane tp = new JTabbedPane();
		tp.setPreferredSize(new Dimension(975, 375));
		tp.addTab("Scaling factors", getScalingFactors());
		tp.addTab("Principal Components", getPrincipalComponents());
		tp.addTab("SVM", getSVMModel());
		return tp;
	}
	
	private JScrollPane getScalingFactors() {
		String[] featureLabels = model.getPreprocessModel().getFeatures();		
		String[] headers = {"Feature", "Minimum", "Maximum"};
		String[][] tableData = new String[featureLabels.length][3];
		
		for(int i=0; i<featureLabels.length; i++) {
			tableData[i][0] = featureLabels[i];
			tableData[i][1] = Double.toString(model.getPreprocessModel().getMin()[i]);
			tableData[i][2] = Double.toString(model.getPreprocessModel().getMax()[i]);
		}
		
		TableModel model = new DefaultTableModel(tableData, headers);
		JTable table = new JTable(model);
		table.setEnabled(false);

		JScrollPane pane = new JScrollPane(table);
		pane.setSize(975, 375);
		return (pane);
	}
	
	private JScrollPane getPrincipalComponents() {
		double[][] values = model.getPreprocessModel().getPrincipalComponents();		
		String[] headers = new String[values.length];
		for(int i=0; i<headers.length; i++) {
			headers[i] = "PC #".concat(Integer.toString(i+1));
		}
		
		String[][] tableData = new String[values[0].length][values.length];
		for(int i=0; i<values[0].length; i++) {
			for(int j=0; j<values.length; j++) {
				tableData[i][j] = Double.toString(values[j][i]);
			}
		}
		
		TableModel model = new DefaultTableModel(tableData, headers);
		JTable table = new JTable(model);
		table.setEnabled(false);

		JScrollPane pane = new JScrollPane(table);
		pane.setSize(975, 375);
		return (pane);
	}
	
	private JScrollPane getSVMModel() {
		StringBuilder content = new StringBuilder();
		content.append("<h3> Classes </h3>");
		
		Iterator<String> it = model.getSvmmodel().getClasses().iterator();
		content.append("<ul>");
		while(it.hasNext()) {
			content.append("<li>");
			content.append(it.next());
			content.append("</li>");
		}
		content.append("</ul>");
		content.append("<br />");
		content.append("<h3> Accuracy: </h3>");
		content.append("&emsp;");
		content.append(model.getSvmmodel().getAccuracy() + "%");
		
		JTextPane text = new JTextPane();
		text.setContentType("text/html");
		text.setText(content.toString());
		text.setEditable(false);
		text.setCaretPosition(0);

		JScrollPane pane = new JScrollPane(text);
		pane.setSize(975, 375);
		pane.setViewportView(text);
		return (pane);
	}
	
	private JPanel buttonPanel() {
		JButton btn = new JButton("Save classifier model");
		btn.setActionCommand("save_model");
		btn.addActionListener(lm);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setPreferredSize(new Dimension(975, 25));
		panel.add(btn);		
		return panel;
	}
}
