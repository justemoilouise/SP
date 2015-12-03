package gui;

import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Properties;

import gui.listeners.Listener_TreeContent;

import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.GroupLayout.Alignment;
import javax.swing.tree.DefaultMutableTreeNode;

import FileHandlers.FileInput;
import core.Client;

@SuppressWarnings("serial")
public class TutorialWindow extends JInternalFrame {
	private Properties props;
	private JScrollPane mainPanel;
	private Hashtable<String, String> contents;
	private JTextPane text;
	
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
								.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 455, GroupLayout.PREFERRED_SIZE)
								.addComponent(sidePanel, GroupLayout.PREFERRED_SIZE, 455, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		sidePanel.setViewportView(setContentTree());
		getContentPane().setLayout(groupLayout);
		
		setTitle("Tutorial");
		setVisible(true);
		setBounds(10, 10, 800, 500);
		setClosable(true);
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
		
		text.setContentType("text/html");
		text.setEditable(false);
		text.setCaretPosition(0);
		text.setText(contents.get(key));

		mainPanel.setPreferredSize(new Dimension(550, 455));
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
}
