package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class ParserSmokeTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	// TODO Brittle, but cannot do otherwise for the moment. Maybe something from the ENV?
	// Or some known location inside the project (with symlink as well)
	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");
	// Note there's no version number because this is a symlink
	private final static File APK_FILE = new File("./src/test/resources/apks/BasicCalculator.apk");

	@BeforeClass
	public static void setupEnvironment() {
		//
		DuafDroidParser.setupSoot(ANDROID_JAR, APK_FILE);
	}

	@Test
	public void parseTrace() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./traces/de.unipassau.calculator/Trace-1585570774429.txt");
		DuafDroidParser parser = new DuafDroidParser();
		ParsedTrace parsedTrace = parser.parseTrace(traceFile);

	}
}
