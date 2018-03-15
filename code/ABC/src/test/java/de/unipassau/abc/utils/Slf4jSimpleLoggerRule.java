package de.unipassau.abc.utils;

import org.junit.rules.ExternalResource;
import org.slf4j.event.Level;

public class Slf4jSimpleLoggerRule extends ExternalResource {

	private Level currentLogLevel;
	private Level level;
	public Slf4jSimpleLoggerRule(Level level) {
		this.level = level;
	}
	@Override
	protected void before() throws Throwable {
		// TODO Get the log level for this particular test class into currentLogLevel; 
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, level.name());
//		System.out.println(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY + " --> " + level.name());
		super.before();
	}
	
	@Override
	protected void after() {
		// TODO Reapply the original log level from currentLogLevel
		super.after();
	}
}
