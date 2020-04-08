package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class ParserSmokeTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	// TODO Brittle, but cannot do otherwise for the moment. Maybe something from
	// the ENV?
	// Or some known location inside the project (with symlink as well)
	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	@Test
	public void parseTrace() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./traces/de.unipassau.calculator/Trace-1585866107653.txt");
		File apk_file = new File("./src/test/resources/apks/BasicCalculator.apk");
		
		TraceParserImpl.setupSoot(ANDROID_JAR, apk_file);
		
		TraceParserImpl parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseFromTraceFile(traceFile);
	}

	@Test
	public void parseTraceWithManyThreads() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./traces/ca.rmen.android.poetassistant/Crash.test-trace");
		File apk_file = new File("./src/test/resources/apks/Poet.apk");
		
		TraceParserImpl.setupSoot(ANDROID_JAR, apk_file);
		
		
		TraceParserImpl parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseFromTraceFile(traceFile);
	}
}
