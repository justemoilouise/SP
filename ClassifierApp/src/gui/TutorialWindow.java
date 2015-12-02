package gui;

import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import gui.listeners.Listener_TreeContent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;

import CoreHandler.Prompt;
import FileHandlers.FileInput;
import ImageHandlers.ProcessImage;
import core.Client;

@SuppressWarnings("serial")
public class TutorialWindow extends JInternalFrame {
	private Properties props;
	private JScrollPane mainPanel;
	private Hashtable<String, String> contents;
	private JTextPane text;
	private String[] attributes = { "regular", "category", "subcategory" };
	
	public TutorialWindow() {
		contents = new Hashtable<String, String>();
		
		mainPanel = new JScrollPane();
		text = new JTextPane();
		addToMainPanel("RadiSS");
		
		JScrollPane sidePanel = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(9)
						.addComponent(sidePanel, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
						.addGap(6)
						.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 589, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(10)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE)
								.addComponent(sidePanel, GroupLayout.PREFERRED_SIZE, 505, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		sidePanel.setViewportView(setContentTree());
		getContentPane().setLayout(groupLayout);
		
		setTitle("Tutorial");
		setVisible(true);
		setBounds(10, 10, 800, 550);
		setClosable(true);
	}
	
	private void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "Arial");
        StyleConstants.setFontSize(def, 12);

        Style s = doc.addStyle("category", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setFontFamily(s, "Serif");
		StyleConstants.setFontSize(s, 24);
		StyleConstants.setUnderline(s, true);
		
        s = doc.addStyle("subcategory", regular);
        StyleConstants.setItalic(s, true);
		StyleConstants.setFontFamily(s, "Helvetica");
		StyleConstants.setFontSize(s, 16);
    }

	private JTree setContentTree() {
		DefaultMutableTreeNode category;
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("RadiSS");
		
		category = new DefaultMutableTreeNode("Menu bar");
		root.add(category);
		
		category = new DefaultMutableTreeNode("Toolbar");
		root.add(category);
		
		category = new DefaultMutableTreeNode("Workspace");
		root.add(category);
		
		category = new DefaultMutableTreeNode("Console Log");
		root.add(category);
		
		category = new DefaultMutableTreeNode("Error Log");
		root.add(category);
		
		category = new DefaultMutableTreeNode("Features");
		root.add(category);
		
		JTree tree = new JTree(root);
		tree.addTreeSelectionListener(new Listener_TreeContent(this, tree));
		
		return tree;
	}
	
	public void addToMainPanel(String key) {
		if(!contents.containsKey(key))
			getContent(key);

		StyledDocument doc = text.getStyledDocument();
		addStylesToDocument(doc);
		
		try {
			if(doc.getLength() > 0)
				doc.remove(0, doc.getLength());
			
			doc.insertString(0, key, doc.getStyle(attributes[1]));
			doc.insertString(doc.getLength(), "\n", doc.getStyle(attributes[0]));
			
			int count = 1;
			StringTokenizer token = new StringTokenizer(contents.get(key), "\n");
			while(token.hasMoreTokens()) {
				String content = token.nextToken();
				
				if(content.startsWith("**")) {
					content = count + ". " + content.substring(2, content.length());
					count++;
					
					doc.insertString(doc.getLength(), "\n  " + content, doc.getStyle(attributes[2]));
				}
				else if(content.startsWith("<img>")) {
					ImageIcon img = GetImage(content.substring(5, content.length()));
					doc.insertString(doc.getLength(), "\n ", doc.getStyle(attributes[0]));
					text.insertIcon(img);
				}
				else {
					doc.insertString(doc.getLength(), "    " + content, doc.getStyle(attributes[0]));
				}
				doc.insertString(doc.getLength(), "\n", doc.getStyle(attributes[0]));
			}
			doc.insertString(doc.getLength(), "\n", doc.getStyle(attributes[0]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Prompt.PromptError("ERROR_GET_TUTORIAL");
			Client.printStackTrace(e);
		}
		
		text.setEditable(false);
		text.setCaretPosition(0);

		mainPanel.setPreferredSize(new Dimension(550, 505));
		mainPanel.setViewportView(text);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	private void getContent(String key) {
		if(props == null)
			props = Client.getProperties();
		
		if(key.equals("RadiSS"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.overview")));
		else if(key.equals("Menu bar"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.menubar")));
		else if(key.equals("Toolbar"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.toolbar")));
		else if(key.equals("Workspace"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.workspace")));
		else if(key.equals("Console Log"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.console")));
		else if(key.equals("Error Log"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.error")));
		else if(key.equals("Features"))
			contents.put(key, FileInput.readFile(props.getProperty("tutorial.features")));
	}

	private ImageIcon GetImage(String imgName) {
		return ProcessImage.getScaledImage(imgName, new Dimension(560, 450));
	}
}
