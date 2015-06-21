package gui;

import gui.listeners.Listener_Mouse;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BoxLayout;
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
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Data.Attributes;
import Data.Feature;
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
		setBounds(10, 10, 1000, 550);
		setClosable(true);
		setIconifiable(true);
	}

	public JPanel getImageAndPrediction() {
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

	public JTabbedPane getResults() {
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("Summary", getSummary());
		tp.addTab("Protrusions", getProtrusions());
		tp.addTab("Base shape", getBaseShape());
		tp.addTab("Features", getFeatures());
		tp.addTab("Measurements", getImageFeatures());
		tp.addTab("Particle analysis", getParticleAnalysis());
		tp.addTab("SVM", getSVMResult());
		tp.addTab("Decision tree", getDecisionTreeResult());

		return tp;
	}

	public JScrollPane getSummary() {
		String prediction = input.getSpecies().getSvmName();
		JPanel panel = new JPanel();

		JLabel label = new JLabel("PREDICTED CLASS: " + prediction);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);

		if(prediction.equalsIgnoreCase("UNKNOWN")) {
			label = new JLabel("Species doesn't fall in any known class.");
		} else {
			label = new JLabel("There's a " + getProbability() + "% that it is the unknown's class.");
		}
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		return new JScrollPane(panel);
	}

	public JPanel getProtrusions() {
		ImageIcon img = new ImageIcon(input.getProtrusions().getImage());
		img = ProcessImage.getScaledImage(img, new Dimension(450, 450));
		JLabel label = new JLabel(img);
		JPanel panel = new JPanel();
		panel.add(label);

		return panel;
	}

	public JPanel getBaseShape() {
		ImageIcon img = new ImageIcon(input.getBase().getImage());
		img = ProcessImage.getScaledImage(img, new Dimension(450, 450));
		JLabel label = new JLabel(img);
		JPanel panel = new JPanel();
		panel.add(label);

		return panel;
	}

	public JScrollPane getFeatures() {
		Hashtable<String, Feature> features = input.getSpecies().getFeatures();
		JTabbedPane tp = new JTabbedPane();
		
		for(Feature f : features.values()) {
			String[] headers = f.getmLabels();
			String[][] tableData = new String[f.getCount()][headers.length];
			ArrayList<double[]> values = f.getmValues();
			int index = 0;

			for(double[] v : values) {
				for(int i=0; i<v.length; i++) {
					tableData[index][i] = Double.toString(v[i]);
				}
				index++;
			}

			TableModel model = new DefaultTableModel(tableData, headers);
			JTable table = new JTable(model);
			table.setEnabled(false);
		
			tp.addTab(f.getName(), table);
		}

		JScrollPane pane = new JScrollPane(tp);
		pane.setPreferredSize(new Dimension(300, 0));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return pane;
	}

	public JScrollPane getImageFeatures() {
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

	public JScrollPane getParticleAnalysis() {
		String[] labels =  input.getSpecies().getParticleLabels();
		ArrayList<double[]> values = input.getSpecies().getParticleValues();

		String[][] tableData = new String[values.size()][labels.length];

		for(int i=0; i<values.size(); i++) {
			double[] val = values.get(i);
			for(int j=0; j<val.length; j++) {
				tableData[i][j] = Double.toString(val[j]);
			}
		}

		TableModel model = new DefaultTableModel(tableData, labels);
		JTable table = new JTable(model);
		table.setEnabled(false);

		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(300, 0));
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		return pane;
	}

	public JScrollPane getSVMResult() {
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

	public JScrollPane getDecisionTreeResult() {
		JLabel label = new JLabel("PREDICTED CLASS: " + input.getSpecies().getdTreeName());

		Attributes a = input.getSpecies().getAttr();
		String[] attr = a.getAttr();
		
		String[] headers = {"Attribute", "Description"};
		String[][] tableData = new String[attr.length][2];

		for(int i=0; i<attr.length; i++) {		
			String desc = "None";
			if(attr[i].equalsIgnoreCase("shape")) {
				desc = a.isSphericalShape() ? "Spherical" : "Conical";
			} else if(attr[i].equalsIgnoreCase("horns")) {
				desc = a.hasHorns() ? Integer.toString(a.getHornCount()) : "None";
			} else if(attr[i].equalsIgnoreCase("spines")) {
				desc = a.hasSpines() ? Integer.toString(a.getSpineCount()) : "None";
			} else if(attr[i].equalsIgnoreCase("pores")) {
				desc = a.getPoreShape() == 1 ? "Spherical" : "Conical";
			}
			
			tableData[i][0] = attr[i];
			tableData[i][1] = desc;
		}

		TableModel model = new DefaultTableModel(tableData, headers);
		JTable table = new JTable(model);
		table.setEnabled(false);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(label, Component.LEFT_ALIGNMENT);
		panel.add(table, Component.LEFT_ALIGNMENT);

		return new JScrollPane(panel); 
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
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 710, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(panel, Alignment.LEADING)
//								.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
								.addComponent(tabbedPane, Alignment.LEADING, 500, 500, 500))
								.addContainerGap(13, Short.MAX_VALUE))
				);

		getContentPane().setLayout(groupLayout);
	}
}
