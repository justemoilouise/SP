package gui;

import gui.listeners.Listener_Mouse;
import helpers.DataHelper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class OutputPanel extends JTabbedPane {
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private Listener_Mouse lm;
	private ClassifierModel model;

	public OutputPanel(ClassifierModel model) {
		this.model = model;
		this.panel = new JPanel();
		this.tabbedPane = new JTabbedPane();
		this.lm = new Listener_Mouse();

		decisionTreeOutputPanel();
		svmOutputPanel();

		panel.add(tabbedPane, BorderLayout.CENTER);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);
	}

	public JPanel getOutputPanel() {
		return panel;
	}

	private void decisionTreeOutputPanel() {
		if(model.getDecisionTreeModel() != null) {
			JTextPane textPane = new JTextPane();

			try {
				String content = getContents(1, this.getClass().getResource("/resources/DecisionTreeOutput.html"));
				textPane.setText(content);
				textPane.setContentType("text/html");
				textPane.setCaretPosition(0);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}

			JScrollPane panel = new JScrollPane();
			panel.setViewportView(textPane);
			panel.revalidate();
			panel.repaint();
			panel.setPreferredSize(new Dimension(275, 0));
			panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			tabbedPane.add("Decision Tree", panel);
		}
	}

	private void svmOutputPanel() {
		if(model.getSvmmodel() != null) {
			JTextPane textPane = new JTextPane();

			try {
				String content = getContents(2, this.getClass().getResource("/resources/SVMOutput.html"));
				textPane.setText(content);
				textPane.setContentType("text/html");
				textPane.setCaretPosition(0);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}

			JScrollPane panel = new JScrollPane();
			panel.setViewportView(textPane);
			panel.revalidate();
			panel.repaint();
			panel.setPreferredSize(new Dimension(275, 0));
			panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			tabbedPane.add("SVM", panel);
		}
	}

	private JPanel getButtonPanel() {
		JButton btnSubmit = new JButton("Save model");
		btnSubmit.setActionCommand("save");
		btnSubmit.addActionListener(lm);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(lm);

		JPanel panel = new JPanel();
		panel.add(btnSubmit);
		panel.add(btnCancel);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		return panel;
	}

	private String getContents(int mode, URL url) {
		try {
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder contents = new StringBuilder();
			String c;
			while ((c = br.readLine()) != null) {
				contents.append(c);
			}
			br.close();
			
			return replaceOutputResults(mode, contents.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}

	private String replaceOutputResults(int mode, String content) {		
		if(!content.isEmpty()) {
			if(mode == 1) {
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(DataHelper.AddTableRow("Classes", 
						DataHelper.ConvertArrayListToString(model.getDecisionTreeModel().getClasses())));
				strBuilder.append(DataHelper.AddTableRow("Accuracy", model.getDecisionTreeModel().getAccuracy()));

				content = content.replace("<!-- @dt_properties -->", strBuilder.toString());
			}
			else {
				StringBuilder preprocess_scale = new StringBuilder();
				String[] features = model.getPreprocessModel().getFeatures();
				for(int i=0; i<features.length; i++) {
					preprocess_scale.append(DataHelper.AddTableRow(features[i], 
							model.getPreprocessModel().getMin()[i], model.getPreprocessModel().getMax()[i]));
				}

				StringBuilder preprocess_pca = new StringBuilder();
				double[][] pca = model.getPreprocessModel().getPrincipalComponents();
				for(int i=0; i<pca.length; i++) {
					preprocess_pca.append(DataHelper.AddTableRow(Integer.toString(i+1), 
							DataHelper.ConvertArrayToString(pca[i])));
				}

				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(DataHelper.AddTableRow("Classes", 
						DataHelper.ConvertArrayListToString(model.getSvmmodel().getClasses())));
				strBuilder.append(DataHelper.AddTableRow("Accuracy", model.getSvmmodel().getAccuracy()));

				content = content.replace("<!-- @preprocess_scale -->", preprocess_scale.toString());
				content = content.replace("<!-- @preprocess_pca -->", preprocess_pca.toString());
				content = content.replace("<!-- @svm_properties -->", strBuilder.toString());
			}
		}
		
		return content;
	}
}
