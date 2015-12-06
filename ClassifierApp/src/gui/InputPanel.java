package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Data.Input;
import ImageHandlers.ProcessImage;
import core.Client;

@SuppressWarnings("serial")
public class InputPanel extends JInternalFrame {
	private Input input;

	public InputPanel(Input input) {
		this.input = input;
		
		addToGroupLayout();
		
		int count = Client.getCount()+1;
		setTitle("Input (" + count + ")");;
		setName("Input");
		setBounds((10*count), (10*count), 800, 450);
		setClosable(true);
		setIconifiable(true);
	}
	
	private JPanel getImageAndPrediction() {
		JPanel panel = new JPanel();
		
		int newWidth = 250;
		ImageIcon img = ProcessImage.getScaledImage("/resources/img_noimg.png",
				new Dimension(newWidth, newWidth));
		JLabel imgLabel = new JLabel(img);	
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(25, Short.MAX_VALUE)
					.addComponent(imgLabel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addGap(24))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(94)
					.addContainerGap(114, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addComponent(imgLabel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addContainerGap(240, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		panel.add(imgLabel, BorderLayout.CENTER);
		
		return panel;
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

	private void addToGroupLayout() {
		JPanel panel = getImageAndPrediction();
		JScrollPane scrollPane = getFeatures();
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 520, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(scrollPane, Alignment.LEADING)
						//.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		
		getContentPane().setLayout(groupLayout);
	}
}
