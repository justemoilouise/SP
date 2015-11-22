package gui;

import gui.listeners.Listener_Mouse;
import ij.gui.Toolbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	final static String LOOKANDFEEL = "System";
	final static String THEME = "Test";

	private Listener_Mouse lm;
	private JDesktopPane dp;
	private JButton submit, stop;

	public MainWindow() {		
		lm = new Listener_Mouse();
		dp = new JDesktopPane();
		
		initLookAndFeel();

		dp.setBackground(Color.GRAY);
		Container c = getContentPane();
		c.add(setToolbar(), BorderLayout.NORTH);
		c.add(dp);

		setSize(1000, 600);
		setJMenuBar(setMenubar());
		setTitle("RadiSS");
		setBackground(Color.DARK_GRAY);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	private JMenuBar setMenubar() {
		JMenuBar mb = new JMenuBar();

		JMenu menu;
		JMenuItem item;

		menu = new JMenu("File");
		item = new JMenuItem("New image");
		item.setActionCommand("input_image");
		item.addActionListener(lm);
		menu.add(item);
		menu.addSeparator();

		item = new JMenuItem("Quit");
		item.setActionCommand("quit");
		item.addActionListener(lm);
		menu.add(item);
		mb.add(menu);

		return mb;
	}

	private JToolBar setToolbar() {
		JToolBar toolbar = new JToolBar();
		
		//inputs
		ImageIcon imgIcon = new ImageIcon(this.getClass().getResource("/resources/img-icon_open-image.png"));
		JButton button = new JButton(imgIcon);
		button.setActionCommand("input_image");
		button.addActionListener(lm);
		toolbar.add(button);
		toolbar.addSeparator();

		//execute operations
		imgIcon = new ImageIcon(this.getClass().getResource("/resources/img-icon_run.png"));
		submit = new JButton(imgIcon);
		submit.setActionCommand("submit");
		submit.addActionListener(lm);
		toolbar.add(submit);
		imgIcon = new ImageIcon(this.getClass().getResource("/resources/img-icon_stop.png"));
		stop = new JButton(imgIcon);
		stop.setActionCommand("stop");
		stop.addActionListener(lm);
		toolbar.add(stop);
		toolbar.addSeparator();

		//ImageJ
		JLabel label = new JLabel(" Selection tools: ");
		Toolbar t = new Toolbar();
		t.setTool(Toolbar.WAND);
		toolbar.add(label);
		toolbar.add(t);
		toolbar.addSeparator();

		toolbar.setFloatable(false);		
		return toolbar;
	}

	public void addToDesktopPane(Component comp) {
		dp.add(comp);
	}

	public Component getFromDesktopPane(String name) {
		Component[] comp = dp.getComponents();

		for(int i=0; i<comp.length; i++) {
			if(comp[i].getName().equals(name))
				return comp[i];
		}

		return null;
	}

	public Component getDesktoPane() {
		return dp;
	}
}
