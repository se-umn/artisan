package utils;

import de.unipassau.abc.instrumentation.Monitor;

public class Constants {

	/*
	 * This is the class which implements the Trace/Monitoring interface. Since,
	 * this is meta-programming we will get reference to the methods implemented by
	 * this class and call them from the instrumentation code
	 */
	public static final String MONITOR_CLASS = Monitor.class.getName();
}
