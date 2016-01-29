package gui;

import gui.listeners.Listener_Mouse;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Data.Input;
import ImageHandlers.ProcessImage;
import core.Client;

@SuppressWarnings("serial")
public class ImagesPanel extends JInternalFrame {
	private Listener_Mouse lm;
	private JScrollPane images = new JScrollPane();
	
	public ImagesPanel() {
		this.lm = new Listener_Mouse();
		add(buttonPanel());
		add(images);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setTitle("Chosen images");;
		setName("Images");
		setBounds(10, 10, 800, 450);
		setClosable(true);
		setIconifiable(true);
	}
	
	public void refreshImages() {
		remove(images);			
		images = getImages();
		add(images);
		revalidate();
	}
	
	private JScrollPane getImages() {
		ArrayList<Input> unclassified = new ArrayList<Input>();
		ArrayList<Input> inputs = Client.getInput();
		Iterator<Input> it = inputs.iterator();
		while(it.hasNext()) {
			Input i = it.next();
			if(i.getSpecies().getName() != null)
				unclassified.add(i);
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(Math.round(unclassified.size()/4), 4, 3, 3));
		it = unclassified.iterator();
		while(it.hasNext()) {
			Input i = it.next();
			int newWidth = 150;
			ImageIcon img = ProcessImage.getScaledImage(i.getImageName(), new Dimension(newWidth, newWidth));
			panel.add(new JLabel(img));
		}
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize(new Dimension(775, 375));
		return scrollPane;
	}
	
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.setPreferredSize(new Dimension(775, 25));
		
		JButton btn = new JButton("Add another image");
		btn.setActionCommand("classify_image");
		btn.addActionListener(lm);
		panel.add(btn);
		
		btn = new JButton("Done");
		btn.setActionCommand("view_output");
		btn.addActionListener(lm);
		panel.add(btn);
		
		return panel;
	}
}
