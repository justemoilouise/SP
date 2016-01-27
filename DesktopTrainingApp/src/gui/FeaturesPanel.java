package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import gui.listeners.Listener_Mouse;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Data.Species;
import core.Client;

@SuppressWarnings("serial")
public class FeaturesPanel extends JInternalFrame {
	private Listener_Mouse lm;
	
	public FeaturesPanel() {
		this.lm = new Listener_Mouse();
		setTitle("Image features");;
		setName("Input");
		setBounds(10, 10, 800, 450);
		setClosable(true);
		setIconifiable(true);
	}
	
	private JScrollPane getFeatures() {
		ArrayList<Species> dataset = Client.getTrainingSet();
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Species");
		headers.addAll(Arrays.asList(dataset.get(0).getFeatureLabels()));
		String[][] tableData = new String[dataset.size()][headers.size()];
		int row = 0;
		Iterator<Species> it = dataset.iterator();
		while(it.hasNext()) {
			Species s = it.next();
			tableData[row][0] = s.getName();
			for(int i=0; i<s.getFeatureLabels().length; i++) {
				tableData[row][i+1] = Double.toString(s.getFeatureValues()[i]);
			}
		}
		
		TableModel model = new DefaultTableModel(tableData, headers.toArray());
		JTable table = new JTable(model);
		table.setEnabled(false);
		return (new JScrollPane(table));
	}
	
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		JButton btn = new JButton("Add another");
		btn.setActionCommand("image_extractFeatures");
		btn.addActionListener(lm);
		panel.add(btn);
		
		btn = new JButton("Export dataset");
		btn.setActionCommand("export");
		btn.addActionListener(lm);
		panel.add(btn);
		
		btn = new JButton("Build classifier model");
		btn.setActionCommand("build_model");
		btn.addActionListener(lm);
		panel.add(btn);
		
		
		
		return panel;
	}
}
