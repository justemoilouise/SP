package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class StartScreen extends JWindow implements Runnable {
	private JProgressBar progress;
	private boolean execute;
	
	public StartScreen() {
		this.progress = new JProgressBar();
		
		ImageIcon imgIcon = new ImageIcon(this.getClass().getResource("/resources/img_SplashScreen.png"));
		JLabel imgLabel = new JLabel(imgIcon);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.DARK_GRAY);
		panel.add(progress);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(imgLabel, BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.SOUTH);
		pack();
		
		setLocationRelativeTo(null);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		progress.setIndeterminate(true);
		setVisible(true);
		
		while(execute) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		setMessage("Preparing workspace..");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dispose();
	}
	
	public void setMessage(String msg) {
		progress.setStringPainted(true);
		progress.setString(msg);
	}
	
	public void setExecutable(boolean execute) {
		this.execute = execute;
	}
}
