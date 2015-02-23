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
		readModels();
		init_SVM();
		
		screen.setExecutable(false);
	}
	
	private void init_SVM() {
		screen.setMessage("Initializing SVM..");
		
		long startTime = System.currentTimeMillis();
		SVM svm = Client.getSvm();
		//svm.init_SVM();
		
		Client.getPm().appendToConsole("SVM took " + (System.currentTimeMillis()-startTime) + " ms to initialize..");
		Client.setSvm(svm);
	}
	
	private void readModels() {
		
	}
}
