package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

import FileHandlers.FileInput;
import core.Client;

@SuppressWarnings("serial")
public class AboutWindow extends JInternalFrame {
	private Properties props;
	private String content;
	
	public AboutWindow() {		
		getContents();
		setContents();
		
		setTitle("About");
		setVisible(true);
		setBounds(300, 100, 350, 150);
		setClosable(true);
	}
	
	private void getContents() {
		if(props == null)
			props = Client.getProperties();
		
		content = FileInput.readFile(props.getProperty("about.overview"));
	}
	
	private void setContents() {
		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setBackground(getContentPane().getBackground());
		
		try {			
			StyledDocument doc = text.getStyledDocument();
			doc.insertString(0, content, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageIcon img = new ImageIcon(this.getClass().getResource("/resources/img_logo.png"));
		JLabel imgLabel = new JLabel(img);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(imgLabel, BorderLayout.WEST);
		panel.add(text, BorderLayout.EAST);
		
		getContentPane().add(panel);
	}
}
