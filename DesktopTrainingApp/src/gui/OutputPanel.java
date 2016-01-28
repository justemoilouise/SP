package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import gui.listeners.Listener_Mouse;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import core.ValueHelper;
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
		tp.setPreferredSize(new Dimension(975, 400));
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
		pane.setSize(975, 400);
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
		pane.setSize(975, 400);
		return (pane);
	}
	
	private JScrollPane getSVMModel() {
		String[][] tableData = new String[2][2];
		tableData[0][0] = "Classes";
		tableData[0][1] = ValueHelper.ToString(model.getSvmmodel().getClasses());
		tableData[1][0] = "Accuracy";
		tableData[1][1] = Double.toString(model.getSvmmodel().getAccuracy());
		
		TableModel model = new DefaultTableModel(tableData, new String[] { "Property", "Value" });
		JTable table = new JTable(model);
		table.setEnabled(false);

		JScrollPane pane = new JScrollPane(table);
		pane.setSize(975, 400);
		return (pane);
	}
	
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		panel.setPreferredSize(new Dimension(975, 25));
		
		JButton btn = new JButton("Save");
		btn.setActionCommand("save_model");
		btn.addActionListener(lm);
		panel.add(btn);
		
		btn = new JButton("Upload");
		btn.setActionCommand("upload_model");
		btn.addActionListener(lm);
		panel.add(btn);
		
		btn = new JButton("Rebuild");
		btn.setActionCommand("build_model");
		btn.addActionListener(lm);
		panel.add(btn);
		
		return panel;
	}
}
