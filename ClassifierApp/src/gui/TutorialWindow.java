package gui;

import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

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
		text.setText(contents.get(key));
		text.setEditable(false);
		text.setCaretPosition(0);

		mainPanel.setPreferredSize(new Dimension(550, 455));
		mainPanel.setViewportView(text);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	private void getContent(String key) {
		if(props == null)
			props = Client.getProperties();
		
		if(key.equals("RadiSS")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.overview")));
			contents.put(key, str);
		}
		else if(key.equals("Menu bar")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.menubar")));
			contents.put(key, str);
		}
		else if(key.equals("Toolbar")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.toolbar")));
			contents.put(key, str);
		}
		else if(key.equals("Workspace")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.workspace")));
			contents.put(key, str);
		}
		else if(key.equals("Console Log")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.console")));
			contents.put(key, str);
		}
		else if(key.equals("Error Log")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.error")));
			contents.put(key, str);
		}
		else if(key.equals("Features")) {
			String str = updateImgContent(FileInput.readFile(props.getProperty("tutorial.features")));
			contents.put(key, str);
		}
	}
	
	private String updateImgContent(String text) {
		if(text.contains("img")) {
			String content = text;
			StringTokenizer tokenizer = new StringTokenizer(text, "\r\n");
			while(tokenizer.hasMoreTokens()) {
				String line = tokenizer.nextToken();
				if(line.contains("img")) {
					int index1 = line.indexOf("'");
					int index2 = line.lastIndexOf("'");
					String imgPath = line.substring(index1 + 1, index2);
					String src = this.getClass().getResource(imgPath).toString();
					content = content.replace(imgPath, src);
				}
			}
			return content;
		}
		
		return text;
	}
}
