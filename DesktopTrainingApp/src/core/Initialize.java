package core;

import gui.StartScreen;

public class Initialize implements Runnable {
	private StartScreen screen;

	public Initialize(StartScreen screen) {
		this.screen = screen;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		screen.setMessage("Intializing...");
	}
}
