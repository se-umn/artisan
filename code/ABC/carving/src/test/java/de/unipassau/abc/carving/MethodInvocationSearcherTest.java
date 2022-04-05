package de.unipassau.abc.carving;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

import de.unipassau.abc.carving.utils.MethodInvocationSelector.StrategyEnum;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.BeforeClass;
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

	private static ParsedTrace parsedTrace;

	private MethodInvocationSelector mis;

	@BeforeClass
	public static void setUp() throws ABCException, IOException {
		File traceFile = new File("./src/test/resources/nz.org.cacophony.birdmonitor/trace.txt");
		File apk_file = new File("./src/test/resources/nz.org.cacophony.birdmonitor/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
		parsedTrace = decorator.decorate(_parsedTrace);
	}

	@Before
	public void setUpEach() {
		mis = new MethodInvocationSelector();
	}

	@Test
	public void testGetAllCarvableMethodInvocations() throws FileNotFoundException, IOException, ABCException {
		Collection<MethodInvocation> carvableMethodInvocation = mis.findCarvableMethodInvocations( parsedTrace );
		
		carvableMethodInvocation.forEach(System.out::println);
	}

	@Test
	public void testGetUniqueCarvableMethodInvocations() throws FileNotFoundException, IOException, ABCException {
		Collection<MethodInvocation> uniqueCarvableMethodInvocation = mis
				.findCarvableMethodInvocations(parsedTrace, StrategyEnum.SELECT_ONE);

		uniqueCarvableMethodInvocation.forEach(System.out::println);
		System.out.printf("Number of method invocations: %d%n", uniqueCarvableMethodInvocation.size());

		Set<String> methodSignatures = uniqueCarvableMethodInvocation.stream()
				.map(MethodInvocation::getMethodSignature)
				.collect(Collectors.toSet());
		assertThat(uniqueCarvableMethodInvocation.size(), is(equalTo(methodSignatures.size())));
	}

	@Test
	public void testGetAllActivityCarvableMethodInvocations() throws FileNotFoundException, IOException, ABCException {
		Set<MethodInvocation> carvableActivityMethodInvocations = mis
				.findCarvableMethodInvocations(parsedTrace, StrategyEnum.SELECT_ACTIVITIES_ALL);

		carvableActivityMethodInvocations.forEach(System.out::println);
		assertTrue(carvableActivityMethodInvocations.stream().allMatch(m -> m.getOwner().isAndroidActivity()));
	}

	@Test
	public void testGetUniqueActivityCarvableMethodInvocations() {
		Set<MethodInvocation> carvableActivityMethodInvocations = mis
				.findCarvableMethodInvocations(parsedTrace, StrategyEnum.SELECT_ACTIVITIES_ONE);

		carvableActivityMethodInvocations.forEach(System.out::println);
		Set<String> methodSignatures = carvableActivityMethodInvocations.stream()
				.map(MethodInvocation::getMethodSignature)
				.collect(Collectors.toSet());

		assertTrue(carvableActivityMethodInvocations.stream().allMatch(m -> m.getOwner().isAndroidActivity()));
		assertThat(carvableActivityMethodInvocations.size(), is(equalTo(methodSignatures.size())));
	}
}
