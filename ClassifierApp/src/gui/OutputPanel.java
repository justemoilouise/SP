package gui;

import gui.listeners.Listener_Mouse;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Data.Input;
import Data.SVMResult;
import ImageHandlers.ProcessImage;
import core.Client;

@SuppressWarnings("serial")
public class OutputPanel extends JInternalFrame {
	private Input input;
	private Listener_Mouse lm;
	private final int count;

	public OutputPanel(Input input) {
		this.input = input;
		this.count = Client.getCount();
		lm = new Listener_Mouse();
		
		addToGroupLayout();
		
		setTitle("Output (" + count + ")");;
		setName("Output");
		setBounds(10, 10, 800, 450);
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
		dload.setActionCommand("download_" +count);
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
		
		tp.addTab("Measurements", getFeatures());
		tp.addTab("SVM", getSVMResult());
		
		return tp;
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
