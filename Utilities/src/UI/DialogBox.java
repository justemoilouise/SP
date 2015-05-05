package UI;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class DialogBox extends JDialog {
	private Component comp;
	private static Hashtable<String, String> message;
	
	public DialogBox() {
		populateMessages();
	}
	
	private void populateMessages() {
		message = new Hashtable<String, String>();
		
		//INPUT
		message.put("SUCCESS_UPLOAD_FILE", "File uploaded successfully!");
		message.put("ERROR_INPUT", "Missing or invalid input.");
		message.put("ERROR_INPUT_FEATURES", "Error in getting input's features.");
		message.put("ERROR_UPLOAD_IMAGE", "Error in uploading image. Please try again.");
		message.put("ERROR_SAVE_IMAGE", "Error in saving image.");
		message.put("ERROR_GET_IMAGE", "Error in retrieving images.");
		
		//OUTPUT
		message.put("ERROR_DLOAD", "An error occurred while downloading file.");
		message.put("SUCCESS_DLOAD", "File downloaded successfully!");
		
		//INITIALIZATION
		message.put("ERROR_INITIALIZE", "Error in initializing application.");
		message.put("ERROR_SPLASH", "Error in loading splash screen.");
		
		//PROCESSING
		message.put("ERROR_SAVE_SCALE", "Error in saving scaling factors to file.");
		message.put("ERROR_STOP", "No processes are currently executing.");
		
		//CONTENTS
		message.put("ERROR_GET_TUTORIAL", "Error in retrieving content.");
			
		//FILES
		message.put("ERROR_READ_FILE", "Error in reading file.");
		message.put("ERROR_UPDATE_FILE", "Error in updating file.");
		message.put("ERROR_UPLOAD_FILE", "Error in uploading file.");
		message.put("ERROR_SAVE_FILE", "Error in saving file.");
		
		message.put("ERROR_GENERAL", "An error has occurred.");
	}
	
	public void setParentComponent(Component comp) {
		this.comp = comp;
	}
	
	public void error(String key) {
		JOptionPane.showInternalMessageDialog(comp, message.get(key));
	}
	
	public void success(String key) {
		JOptionPane.showInternalMessageDialog(comp, message.get(key));
	}
	
	public boolean chooseFeatures(boolean isIJ) {		
		JRadioButton ij = new JRadioButton("Shape and basic texture features");		
		JRadioButton jf = new JRadioButton("Shape and Haralick texture descriptors");

		ButtonGroup btnGrp = new ButtonGroup();
		btnGrp.add(ij);
		btnGrp.add(jf);
		
		JPanel panel = new JPanel();
		panel.add(ij);
		panel.add(jf);
		
		JOptionPane.showConfirmDialog(comp, panel, "Choose which features to use:", JOptionPane.OK_CANCEL_OPTION);
		
		return ij.isSelected();
	}
	
	public int confirmExit(String mode) {
		String buttons[] = {"Yes", "No"};
		String message = null;
		
		if(mode.equals("main"))
			message = "Are you sure you want to close application?";
		else
			message = "Are you sure you want to cancel operation?";
			
		return JOptionPane.showOptionDialog(comp, message, "Confirm exit", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.WARNING_MESSAGE, null, buttons, buttons[1]);
	}
}
