package ExceptionHandlers;

import java.lang.Thread.UncaughtExceptionHandler;

import CoreHandler.Prompt;

@SuppressWarnings("serial")
public class ThreadException extends InterruptedException implements UncaughtExceptionHandler {

	public ThreadException() {}

	@Override
	public void uncaughtException(Thread th, Throwable ex) {
		// TODO Auto-generated method stub
		if(th.getName().equals("SplashScreen")) {
			Prompt.PromptError("ERROR_SPLASH");
		}
		else {
			Prompt.PromptError("ERROR_GENERAL");
		}
	}
}
