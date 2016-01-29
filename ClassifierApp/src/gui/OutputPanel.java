package gui;

import gui.listeners.Listener_Mouse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Data.Input;
import Data.PreprocessModel;
import Data.SVMModel;
import Data.SVMResult;
import Helpers.SVMResultComparator;
import ImageHandlers.ProcessImage;
import core.Client;

@SuppressWarnings("serial")
public class OutputPanel extends JInternalFrame {
	private Input input;
	private Listener_Mouse lm;
	private int index;

	public OutputPanel(Input input, int index) {
		this.input = input;
		this.index = index;
		lm = new Listener_Mouse();
		
		addToGroupLayout();

		setBounds((10*index)+10, (10*index)+10, 800, 450);
		setClosable(true);
		setIconifiable(true);
	}
	
	private JPanel getImageAndPrediction() {
		JPanel panel = new JPanel();
		
		int newWidth = 245;
		ImageIcon img = ProcessImage.getScaledImage(input.getImageName(),
				new Dimension(newWidth, newWidth));
		JLabel imgLabel = new JLabel(img);
		
		JButton dload = new JButton("Download");
		dload.setActionCommand("download_" +index);
		dload.addActionListener(lm);
		
		JPanel footer = new JPanel();
		footer.setLayout(new BorderLayout());
		footer.add(dload, BorderLayout.SOUTH);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(10, Short.MAX_VALUE)
					.addComponent(imgLabel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(50)
					.addComponent(footer)
					.addContainerGap(114, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addComponent(imgLabel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(footer)
					.addContainerGap(240, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		panel.add(imgLabel, BorderLayout.CENTER);
		panel.add(footer, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JTabbedPane getResults() {
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("Summary", getSummary());
		tp.addTab("Measurements", getFeatures());
		tp.addTab("SVM", getSVMResult());
		
		return tp;
	}
	
	private JScrollPane getSummary() {
		PreprocessModel pModel = input.isIJUsed() ? Client.getPreprocess().getIJModel() : Client.getPreprocess().getJFModel();
		SVMModel sModel = input.isIJUsed() ? Client.getSvm().getIJModel() : Client.getSvm().getJFModel();
		
		String featuresUsed = input.isIJUsed() ? "Shape and basic texture features" : "Shape and Haralick texture";
		String[] featureLabels = new String[] { "Features used", "No. of Principal Components", "SVM Accuracy" };
		Object[] featureValues = new Object[] { featuresUsed, pModel.getPC(), sModel.getAccuracy() };
		
		String[] headers = {"Description", "Value"};
		String[][] tableData = new String[featureLabels.length][2];
		for(int i=0; i<featureLabels.length; i++) {
			tableData[i][0] = featureLabels[i];
			tableData[i][1] = featureValues[i].toString();
		}
		TableModel tableModel = new DefaultTableModel(tableData, headers);
		JTable table = new JTable(tableModel);
		table.setEnabled(false);
		
		JLabel label = new JLabel("CLASSIFIER MODEL DETAILS");
		JPanel modelDetails = new JPanel();
		modelDetails.setLayout(new BoxLayout(modelDetails, BoxLayout.Y_AXIS));
		modelDetails.add(label);
		modelDetails.add(table);

		JPanel preditionPanel = new JPanel();
		preditionPanel.setLayout(new BoxLayout(preditionPanel, BoxLayout.Y_AXIS));
		String prediction = input.getSpecies().getName();
		label = new JLabel("PREDICTED SPECIES: " + prediction);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		preditionPanel.add(label);
		
		if(prediction.equalsIgnoreCase("UNKNOWN")) {
			label = new JLabel("Input doesn't fall in any known species.");
		} else {
			label = new JLabel("There's a " + getProbability() + "% that it is the unknown's class.");
		}
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		preditionPanel.add(label);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

		JPanel panel = new JPanel();
		panel.add(modelDetails);
		panel.add(separator);
		panel.add(preditionPanel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return new JScrollPane(panel);
	}
	
	private JScrollPane getFeatures() {
		String[] featureLabels = input.getSpecies().getFeatureLabels();
		double[] featureValues = input.getSpecies().getFeatureValues();
		
		String[] headers = {"Measurement", "Value"};
		String[][] tableData = new String[featureLabels.length][2];
		
		for(int i=0; i<featureLabels.length; i++) {
			tableData[i][0] = featureLabels[i];
			tableData[i][1] = Double.toString(featureValues[i]);
		}
		
		TableModel model = new DefaultTableModel(tableData, headers);
		JTable table = new JTable(model);
		table.setEnabled(false);

		return (new JScrollPane(table));
	}

	private JScrollPane getSVMResult() {
		ArrayList<SVMResult> result = input.getSvmResult();
		Collections.sort(result, new SVMResultComparator());
		String[] headers = {"Class Name", "Probability"};
		String[][] tableData = new String[result.size()][2];
		
		for(int i=0; i<result.size(); i++) {
			SVMResult r = result.get(i);
			
			tableData[i][0] = r.getName();
			tableData[i][1] = Double.toString(r.getProbability());
		}
		
		TableModel model = new DefaultTableModel(tableData, headers);
		JTable table = new JTable(model);
		table.setEnabled(false);
		
		return (new JScrollPane(table));
	}

	private double getProbability() {
		double probability = 0;
		ArrayList<SVMResult> results = input.getSvmResult();
		
		for(SVMResult r : results) {
			if(r.getProbability() > probability) {
				probability = r.getProbability();
			}
		}
		
		return probability;
	}
	
	private void addToGroupLayout() {
		JPanel panel = getImageAndPrediction();
		JTabbedPane tabbedPane = getResults();
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 520, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(tabbedPane, Alignment.LEADING)
						//.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		
		getContentPane().setLayout(groupLayout);
	}
}
