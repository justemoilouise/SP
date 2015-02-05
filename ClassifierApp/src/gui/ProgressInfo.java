package gui;

import gui.listeners.Listener_InternalFrame;

import javax.swing.JInternalFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

@SuppressWarnings("serial")
public class ProgressInfo extends JInternalFrame {
	final static String LOOKANDFEEL = "System";
	final static String THEME = "Test";
	
	private int length = 0;
	private JProgressBar progressBar;
	private Listener_InternalFrame li;
	
	public ProgressInfo() {
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
	}
	
	public ProgressInfo(int length) {
		this.length = length-1;
		this.progressBar = new JProgressBar(0, this.length);
		this.li = new Listener_InternalFrame();
		
		initLookAndFeel();
		
		progressBar.setValue(0);
		add(progressBar);

		addInternalFrameListener(li);

		setName("progress");
		setTitle("Progress information");
		setBounds(100, 100, 350, 60);
		setVisible(true);
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
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void update(int value) {		
		if(value==length)
			closeProgressBar();
		else
			progressBar.setValue(value);
	}
	
	public void closeProgressBar() {
		progressBar.setIndeterminate(false);
		progressBar = null;
		setVisible(false);
	}
}
