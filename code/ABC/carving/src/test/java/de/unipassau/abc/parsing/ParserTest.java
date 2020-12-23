package de.unipassau.abc.parsing;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.utils.MethodInvocationSearcher;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class ParserTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	@Test
	public void smokeTest() throws IOException, ABCException {

		File traceFile = new File("./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603438878387.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
		ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

		MethodInvocationSearcher mis = new MethodInvocationSearcher();
		Set<MethodInvocation> targetMethodsInvocations = mis.findAllCarvableMethodInvocations(parsedTrace);

	}
}
