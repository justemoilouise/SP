package gui;

import gui.listeners.Listener_Mouse;
import helpers.SVMHelper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Data.SVMParameter;

public class InputPanel {
	private JPanel panel;
	private JTextField[] svmParameters;
	private JButton btnSubmit, btnCancel;
	private JComboBox<String> svmTypes, kernel;
	private Listener_Mouse lm;

	public InputPanel(int mode) {
		this.lm = new Listener_Mouse();
		
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

	private JPanel decisionTreeInputPanel() {
		JLabel label = new JLabel("Data set: ");
		JTextField textBox = new JTextField();
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
		input.add(getButtonPanel());
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
		B.add(B1);
		B.add(B2);
		
		String[] parameters = SVMHelper.GetParametrs();
		this.svmParameters = new JTextField[parameters.length];
		Arrays.fill(svmParameters, new JTextField());
		
		for(int i=0; i<parameters.length; i++) {
			JPanel B3 = new JPanel();
			B3.add(new JLabel(parameters[i]), BorderLayout.WEST);
			B3.add(svmParameters[i], BorderLayout.EAST);
			
			B.add(B3);
		}
		
		JPanel input = new JPanel();
		input.add(A);
		input.add(B);
		input.add(getButtonPanel());
		input.setName("input_svm");
		
		return input;
	}
	
	private JPanel getButtonPanel() {
		btnSubmit = new JButton("Next >>");
		btnSubmit.setActionCommand("next");
		btnSubmit.addActionListener(lm);
		
		btnCancel = new JButton("<< Previous");
		btnCancel.setActionCommand("prev");
		btnCancel.addActionListener(lm);
		
		JPanel panel = new JPanel();
		panel.add(btnSubmit);
		panel.add(btnCancel);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
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
