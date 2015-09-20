package gui;

import gui.listeners.Listener_Mouse;
import gui.listeners.Listener_Window;
import helpers.DataHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import Data.ClassifierModel;

@SuppressWarnings("serial")
public class OutputWindow extends JFrame {
	private Listener_Mouse lm;
	private ClassifierModel model;
	
	private String[] resourceName = {
			"/resources/DecisionTreeOutput.html",
			"/resources/SVMOutput.html"
	};

	public OutputWindow(ClassifierModel model) {
		this.model = model;
		this.lm = new Listener_Mouse();

		JPanel panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setName("Output");
		panel.add(getResults());
		panel.add(getButtonPanel());
		add(panel);
		addWindowListener(new Listener_Window());
		setSize(500, 300);
		setIconImage(new ImageIcon("img/logo.png").getImage());
		setTitle("RadiSS");
		setBackground(Color.DARK_GRAY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private JTabbedPane getResults() {
		JTabbedPane pane = new JTabbedPane();
		
		if(model.getDecisionTreeModel() != null) {
			pane.add("Decision Tree", getModelOutput(0));
		}
		
		if(model.getSvmmodel() != null) {
			pane.add("SVM", getModelOutput(1));
		}
		
		return pane;
	}
	
	private JScrollPane getModelOutput(int mode) {
		JTextPane textPane = new JTextPane();

		try {
			textPane.setContentType("text/html");
			String content = getContents(mode, this.getClass().getResource(resourceName[mode]));
			textPane.setText(content);
			textPane.setCaretPosition(0);
			textPane.setEditable(false);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

		JScrollPane panel = new JScrollPane(textPane);
//		panel.setViewportView(textPane);
		panel.revalidate();
		panel.repaint();
		panel.setPreferredSize(new Dimension(500, 0));
		panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		return panel;
	}
	
	private JPanel getButtonPanel() {
		JButton btnSubmit = new JButton("Save model");
		btnSubmit.setActionCommand("save");
		btnSubmit.addActionListener(lm);

		JButton btnCancel = new JButton("Close");
		btnCancel.setActionCommand("close");
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
			if(mode == 0) {
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
		
		return content.replace("\t", "");
	}
}
