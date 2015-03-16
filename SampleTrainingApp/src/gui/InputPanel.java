package gui;

import gui.listener.Listener_Mouse;

import java.awt.BorderLayout;
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
		
		add(paramsPanel, BorderLayout.CENTER);
		add(submit, BorderLayout.SOUTH);
		
		setSize(300, 500);
		setVisible(true);
	}
	
	private JPanel initParametersPanel() {
		JPanel panel = new JPanel();
		panel.setSize(200, 250);

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
		
		field = new JTextField[] {
				new JTextField(),
				new JTextField(),
				new JTextField(),
				new JTextField(),
				new JTextField(),
				new JTextField(),
				new JTextField(),
				new JTextField()
		};

		for(int i=0; i<label.length; i++) {
			field[i].setColumns(5);
			JPanel param = new JPanel();
			param.setSize(200, 50);
			param.add(label[i], BorderLayout.WEST);
			param.add(field[i], BorderLayout.EAST);

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
