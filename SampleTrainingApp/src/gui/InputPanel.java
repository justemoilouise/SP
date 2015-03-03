package gui;

import gui.listener.Listener_Mouse;

import java.util.Arrays;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class InputPanel extends JInternalFrame {
	private JButton submit;
	private JLabel label[];
	private JTextField field[];
	
	private Listener_Mouse lm;
	
	public InputPanel() {
		lm = new Listener_Mouse();
		
		JPanel paramsPanel = initParametersPanel();
		
		submit = new JButton();
		submit.setActionCommand("submit");
		submit.addActionListener(lm);
		
		add(paramsPanel);
		add(submit);
	}
	
	private JPanel initParametersPanel() {
		JPanel panel = new JPanel();

		label = new JLabel[] {
				new JLabel("svm_type"),
				new JLabel("kernel"),
				new JLabel("cost"),
				new JLabel("epsilon"),
				new JLabel("nu"),
				new JLabel("gamma"),
				new JLabel("degree"),
				new JLabel("coefficient"),
		};
		
		field = new JTextField[label.length];
		Arrays.fill(field, new JTextField());

		for(int i=0; i<label.length; i++) {
			JPanel param = new JPanel();
			param.add(label[i]);
			param.add(field[i]);

			panel.add(param);
		}

		return panel;
	}

	public Hashtable<String, Double> getParameters() {
		Hashtable<String, Double> params = new Hashtable<String, Double>();
		
		for(int i=0; i<label.length; i++) {
			params.put(label[i].getText(), Double.parseDouble(field[i].getText()));
		}

		return params;
	}
}
