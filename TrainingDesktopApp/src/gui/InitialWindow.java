package gui;

import gui.listeners.Listener_Mouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

@SuppressWarnings("serial")
public class InitialWindow extends JFrame {
	final static String LOOKANDFEEL = "System";
	final static String THEME = "Test";
	
	private Listener_Mouse lm;
	private JCheckBox[] cb = {
			new JCheckBox("Decision tree"),
			new JCheckBox("Support Vector Machine")
	};
	
	public InitialWindow() {
		this.lm = new Listener_Mouse();
		
		initLookAndFeel();
		
		ImageIcon img = new ImageIcon("img/logo.png");
		
		add(chooseModelOptions());
		
		setSize(200, 150);
		setIconImage(img.getImage());
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

	private JPanel chooseModelOptions() {		
		JPanel panel = new JPanel();
		
		for(JCheckBox c : cb) {
			panel.add(c);
		}
		
		panel.add(getButtonPanel(), BorderLayout.SOUTH);

		return panel;
	}
	
	private JPanel getButtonPanel() {
		JButton btnSubmit = new JButton("OK");
		btnSubmit.setActionCommand("init_ok");
		btnSubmit.addActionListener(lm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("init_cancel");
		btnCancel.addActionListener(lm);
		
		JPanel panel = new JPanel();
		panel.add(btnSubmit);
		panel.add(btnCancel);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		return panel;
	}
	
	public JCheckBox[] getCheckBoxes() {
		return cb;
	}
}
