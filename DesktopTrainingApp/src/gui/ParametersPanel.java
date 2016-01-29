package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import gui.listeners.Listener_Mouse;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Data.SVMParameter;
import core.ValueHelper;

@SuppressWarnings("serial")
public class ParametersPanel extends JInternalFrame {
	private Listener_Mouse lm;
	private JTextField pcaField = new JTextField(8);
	private JComponent[] svmFields;
	
	public ParametersPanel() {
		this.lm = new Listener_Mouse();
		
		JPanel content = new JPanel();
		content.add(parametersPanel());
		content.add(buttonPanel());
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		add(content);
		setTitle("Training parameters");;
		setName("Parameters");
		setBounds(10, 10, 250, 400);
		setClosable(true);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel parametersPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		Border titleBorder = BorderFactory.createTitledBorder("Preprocessing");
		JPanel ppPanel = new JPanel();
		ppPanel.setBorder(titleBorder);
		ppPanel.add(formGroup("PCA", pcaField));
		panel.add(ppPanel);
		
		titleBorder = BorderFactory.createTitledBorder("SVM");
		JPanel svmPanel = new JPanel();
		svmPanel.setLayout(new BoxLayout(svmPanel, BoxLayout.Y_AXIS));
		svmFields = new JComponent[ValueHelper.SVMParameters().length];
		for(int i=0; i<ValueHelper.SVMParameters().length; i++) {
			String param = ValueHelper.SVMParameters()[i];
			switch(param) {
			case "SVM Type":
				svmFields[i] = new JComboBox(ValueHelper.SVMTypes());
				break;
			case "Kernel":
				svmFields[i] = new JComboBox(ValueHelper.SVMKernelFunctions());
				break;
			default:
				svmFields[i] = new JTextField(8);
				break;
			}
			svmPanel.add(formGroup(param, svmFields[i]));
		}
		svmPanel.setBorder(titleBorder);
		panel.add(svmPanel);
		
		return panel;
	}

	private JPanel buttonPanel() {
		JButton btn = new JButton("Build model");
		btn.setActionCommand("build_model");
		btn.addActionListener(lm);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		panel.setPreferredSize(new Dimension(225, 25));
		panel.add(btn);		
		return panel;
	}
	
	public int getPCA() {
		return Integer.parseInt(pcaField.getText());
	}
	
	@SuppressWarnings("rawtypes")
	public SVMParameter getSVMParams() {
		SVMParameter svmParams = new SVMParameter();
		svmParams.setSvmType(((JComboBox) svmFields[0]).getSelectedIndex());
		svmParams.setKernel(((JComboBox) svmFields[1]).getSelectedIndex());
		svmParams.setCost(Double.parseDouble(((JTextField)svmFields[2]).getText()));
		svmParams.setGamma(Double.parseDouble(((JTextField)svmFields[3]).getText()));
		svmParams.setEpsilon(Double.parseDouble(((JTextField)svmFields[4]).getText()));
		svmParams.setDegree(Integer.parseInt(((JTextField)svmFields[5]).getText()));
		svmParams.setNu(Double.parseDouble(((JTextField)svmFields[6]).getText()));
		svmParams.setCoefficient(Double.parseDouble(((JTextField)svmFields[7]).getText()));
		return svmParams;
	}
	
	private JPanel formGroup(String label, JComponent field) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(label));
		panel.add(field);
		return panel;
	}
}
