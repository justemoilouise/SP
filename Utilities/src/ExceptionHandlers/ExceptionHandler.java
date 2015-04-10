package ExceptionHandlers;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler {
	
	public static String getStackTrace(Throwable ex) {
		StringWriter stacktrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(stacktrace));
		
		return stacktrace.toString();
	}
}
