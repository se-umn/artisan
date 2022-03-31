package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class MethodInvocationSearcherTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	@Test
	public void testGetAllCarvableMethodInvocations() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-small.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
		ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
		
		MethodInvocationSelector mis = new MethodInvocationSelector();
		Collection<MethodInvocation> carvableMethodInvocation = mis.findCarvableMethodInvocations( parsedTrace );
		
		carvableMethodInvocation.forEach( System.out::println);
		
		
	}
}
