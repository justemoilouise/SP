package gui;

import gui.listeners.Listener_Mouse;
import helpers.SVMHelper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Data.SVMParameter;

public class InputPanel {
	private JPanel panel, buttonPanel;
	private JTextField textBox;
	private JTextField[] svmParameters;
	private JComboBox<String> svmTypes, kernel;
	private Listener_Mouse lm;

	public InputPanel(int mode) {
		this.lm = new Listener_Mouse();
		this.buttonPanel = initButtonPanel();
		
		switch(mode) {
		case 1:
			panel = decisionTreeInputPanel();
			break;
		case 2:
			panel = svmInputPanel();
			break;
		default: break;
		}
	}

	public JPanel getInputPanel() {
		return panel;
	}
	
	public void setTextBoxText(String text) {
		textBox.setText(text);
	}

	private JPanel decisionTreeInputPanel() {
		JLabel label = new JLabel("Data set: ");
		textBox = new JTextField();
		textBox.setColumns(15);
		JButton btn = new JButton("Upload");
		btn.setActionCommand("input_image");
		btn.addActionListener(lm);
		
		JPanel A = new JPanel();
		A.add(label);
		A.add(textBox);
		A.add(btn);
		A.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		JPanel input = new JPanel();
		input.add(A);
		input.add(buttonPanel);
		input.setName("input_dt");
		
		return input;
	}

	private JPanel svmInputPanel() {
		JLabel label = new JLabel("Data set: ");
		JTextField textBox = new JTextField();
		textBox.setColumns(15);
		JButton btn = new JButton("Upload");
		btn.setActionCommand("input_excel");
		btn.addActionListener(lm);
		
		JPanel A = new JPanel();
		A.add(label);
		A.add(textBox);
		A.add(btn);
		A.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		JLabel labelA = new JLabel("SVM Type: ");
		svmTypes = new JComboBox<String>(SVMHelper.GetSvmTypes());
		JPanel B1 = new JPanel();
		B1.add(labelA);
		B1.add(svmTypes);
		
		
		JLabel labelB = new JLabel("Kernel Type: ");
		kernel = new JComboBox<String>(SVMHelper.GetKernels());
		JPanel B2 = new JPanel();
		B2.add(labelB);
		B2.add(kernel);
		
		JPanel B = new JPanel();
		B.setLayout(new BoxLayout(B, BoxLayout.Y_AXIS));
		B.add(B1);
		B.add(B2);
		
		String[] parameters = SVMHelper.GetParametrs();
		this.svmParameters = new JTextField[parameters.length];
		
		for(int i=0; i<parameters.length; i++) {
			svmParameters[i] = new JTextField();
			svmParameters[i].setColumns(5);
			
			JPanel B3 = new JPanel();
			B3.add(new JLabel(parameters[i]), BorderLayout.WEST);
			B3.add(svmParameters[i], BorderLayout.EAST);
			
			B.add(B3);
		}
		
		JPanel input = new JPanel();
		input.add(A);
		input.add(B);
		input.add(buttonPanel);
		input.setName("input_svm");
		
		return input;
	}
	
	public void enableButtonPanel(boolean isEnabled) {
		for(Component c : buttonPanel.getComponents()) {
			c.setEnabled(isEnabled);
		}
		buttonPanel.setEnabled(isEnabled);
	}
	
	private JPanel initButtonPanel() {
		JButton btnNext = new JButton("Next >>");
		btnNext.setActionCommand("next");
		btnNext.addActionListener(lm);
		btnNext.setEnabled(false);
		
		JButton btnPrev = new JButton("<< Previous");
		btnPrev.setActionCommand("prev");
		btnPrev.addActionListener(lm);
		btnPrev.setEnabled(true);
		
		JPanel panel = new JPanel();
		panel.add(btnPrev);
		panel.add(btnNext);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.setEnabled(false);
		
		return panel;
	}
	
	public SVMParameter getSVMParameters() {
		SVMParameter params = new SVMParameter();
		params.setSvmType(svmTypes.getSelectedIndex());
		params.setKernel(kernel.getSelectedIndex());
		params.setCost(Double.parseDouble(svmParameters[0].getText()));
		params.setEpsilon(Double.parseDouble(svmParameters[1].getText()));
		params.setGamma(Double.parseDouble(svmParameters[2].getText()));
		params.setNu(Double.parseDouble(svmParameters[3].getText()));
		params.setDegree(Integer.parseInt(svmParameters[4].getText()));
		params.setCoefficient(Double.parseDouble(svmParameters[5].getText()));
		
		return params;
	}
}
