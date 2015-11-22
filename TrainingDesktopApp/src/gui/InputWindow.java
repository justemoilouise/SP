package gui;

import gui.listeners.Listener_Window;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

@SuppressWarnings("serial")
public class InputWindow extends JFrame {
	final static String LOOKANDFEEL = "System";
	final static String THEME = "Test";

	private JPanel cards;
	private InputPanel currentPanel;
	private Hashtable<String, InputPanel> inputPanels;

	public InputWindow() {		
		initLookAndFeel();
		initInputPanels();
		initCards();

		add(cards);
		addWindowListener(new Listener_Window());
		setSize(300, 400);
		setIconImage(new ImageIcon("img/logo.png").getImage());
		setTitle("RadiSS");
		setBackground(Color.DARK_GRAY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private void initLookAndFeel() {
		String lookAndFeel = null;

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();                
			}
			else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			} 

			else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			} 

			else if (LOOKANDFEEL.equals("GTK")) { 
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			} 

			else {
				System.err.println("Unexpected value of LOOKANDFEEL specified: "
						+ LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);

				if (LOOKANDFEEL.equals("Metal")) {
					if (THEME.equals("DefaultMetal"))
						MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
					else if (THEME.equals("Ocean"))
						MetalLookAndFeel.setCurrentTheme(new OceanTheme());

					UIManager.setLookAndFeel(new MetalLookAndFeel()); 
				}	
			} 

			catch (ClassNotFoundException e) {
				System.err.println("Couldn't find class for specified look and feel:"
						+ lookAndFeel);
				System.err.println("Did you include the L&F library in the class path?");
				System.err.println("Using the default look and feel.");
			} 

			catch (UnsupportedLookAndFeelException e) {
				System.err.println("Can't use the specified look and feel (" + lookAndFeel
						+ ") on this platform.");
				System.err.println("Using the default look and feel.");
			} 

			catch (Exception e) {
				System.err.println("Couldn't get specified look and feel (" + lookAndFeel
						+ "), for some reason.");
				System.err.println("Using the default look and feel.");
				e.printStackTrace();
			}
		}
	}

	private void initInputPanels() {
		this.inputPanels = new Hashtable<String, InputPanel>();
		inputPanels.put("input_dt", new InputPanel(1));
		inputPanels.put("input_svm", new InputPanel(2));
	}

	private void initCards() {
		cards = new JPanel(new CardLayout());

		Enumeration<String> keys = inputPanels.keys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			cards.add(inputPanels.get(key).getInputPanel(), key);
		}
	}

	public void showCard(String name) {
		CardLayout cl = (CardLayout)(cards.getLayout());
		cl.show(cards, name);
		this.currentPanel = inputPanels.get(name);
	}
	
	public InputPanel getCurrentPanel() {
		return currentPanel;
	}

	public void enableButtonPanel() {
		currentPanel.enableButtonPanel(true);
	}
}
