package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

// BROKEN TEST. We need to update it to use CarvedExecution instead of the Triplette
public class BasicCarverTest {
	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);
//
//	// TODO Brittle, but cannot do otherwise for the moment. Maybe something from
//	// the ENV?
//	// Or some known location inside the project (with symlink as well)
	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	// This is to avoid reparsing the same trace over and over. The assumption is
	// that the parsedTrace is NOT modified by the tests... Flakyness is around the
	// corner
	private static ParsedTrace parsedTrace;

	@BeforeClass
	public static void parseAndSetup() throws FileNotFoundException, IOException, ABCException {
		// Parse trace using TraceParserImpl
//		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-small.txt");
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		parsedTrace = parser.parseTrace(traceFile);
	}

	@Test
	public void testCarvingNoArgsConstructor() throws FileNotFoundException, IOException, ABCException {
		// Basic Carver gets' only a sigle structre
		BasicCarver carver = new BasicCarver(parsedTrace);

		// The first method invocation is the constructor of the Main Activity
		MethodInvocation mainActivityConstructor = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(0);

		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityConstructor);

		// Assert there's only one carved execution
		Assert.assertEquals(1, carvedExecutions.size());
		// Assert that this execution containt ONLY the targetMethodInvocation
		CarvedExecution carvedExecution = carvedExecutions.iterator().next();

		// Carved Execution contains collections of fragments

//		List<MethodInvocation> actual = carvedExecution.getFirst().getOrderedMethodInvocations();

//		List<MethodInvocation> expected = Collections.singletonList(mainActivityConstructor);

//		Assert.assertThat(actual, is(expected));
	}

	@Test
	public void testCarveIntentPutExtra() throws FileNotFoundException, IOException, ABCException {
		// Basic Carver gets' only a sigle structre
		BasicCarver carver = new BasicCarver(parsedTrace);

		// The first method invocation is the constructor of the Main Activity
		MethodInvocation mainActivityConstructor = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(0);

		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityConstructor);

		// Assert there's only one carved execution
		Assert.assertEquals(1, carvedExecutions.size());
		// Assert that this execution containt ONLY the targetMethodInvocation
		CarvedExecution carvedExecution = carvedExecutions.iterator().next();

	}

//
//	@Test
//	public void testCarvingMainActivityOnCreate() throws FileNotFoundException, IOException, ABCException {
//		// Basic Carver gets' only a sigle structre
//		BasicCarver carver = new BasicCarver(parsedTrace);
//
//		// Carve the method call number 2. This is a ZERO-index list
//		MethodInvocation mainActivityOnCreateInvocation = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(2);
//
//		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityOnCreateInvocation);
//
//		// Assert there's only one carved execution
//		Assert.assertEquals(1, carvedExecutions.size());
//		CarvedExecution carvedExecution = carvedExecutions.iterator().next();
//		List<MethodInvocation> actual = carvedExecution.getFirst().getOrderedMethodInvocations();
//
//		MethodInvocation mainActivityConstructor = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(0);
//
//		MethodInvocation mainActivityConstructorSuper = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(1);
//
//		List<MethodInvocation> expected = Arrays.asList(mainActivityConstructor, mainActivityConstructorSuper,
//				mainActivityOnCreateInvocation);
//
//		Assert.assertThat(actual, is(expected));
//
//		assertConsistencyOfCarvedExecution(carvedExecution);
//
//	}
//
//	@Test
//	public void testCarvingMainActivityOnStart() throws FileNotFoundException, IOException, ABCException {
//		// Basic Carver gets' only a sigle structre
//		BasicCarver carver = new BasicCarver(parsedTrace);
//		// Carve the method call number 5
//		MethodInvocation mainActivityOnStart = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(5);
//
//		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityOnStart);
//
//		// Assert there's only one carved execution
//		Assert.assertEquals(1, carvedExecutions.size());
//		// Assert that this execution containt ONLY the targetMethodInvocation
//		CarvedExecution carvedExecution = carvedExecutions.iterator().next();
//		List<MethodInvocation> actual = carvedExecution.getFirst().getOrderedMethodInvocations();
//
//		MethodInvocation mainActivityConstructor = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(0);
//
//		MethodInvocation mainActivityConstructorSuper = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(1);
//
//		MethodInvocation mainActivityOnCreateInvocation = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(2);
//
//		MethodInvocation mainActivityOnCreateInvocationSuper = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(3);
//
//		MethodInvocation mainActivitySetContentViewSuper = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(4);
//
//		List<MethodInvocation> expected = Arrays.asList(mainActivityConstructor, mainActivityConstructorSuper,
//				mainActivityOnCreateInvocation, mainActivityOnCreateInvocationSuper, mainActivitySetContentViewSuper,
//				mainActivityOnStart);
//
//		Assert.assertThat(actual, is(expected));
//
//		assertConsistencyOfCarvedExecution(carvedExecution);
//	}
//
//	@Test
//	public void testCarvingMainActivityOnDestroy() throws FileNotFoundException, IOException, ABCException {
//		// Basic Carver gets' only a sigle structre
//		BasicCarver carver = new BasicCarver(parsedTrace);
//		// Carve the method call number 66
//		MethodInvocation mainActivityOnStart = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(66);
//
//		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityOnStart);
//
//		// Assert there's only one carved execution
//		Assert.assertEquals(1, carvedExecutions.size());
//		// Assert that this execution containt ONLY the targetMethodInvocation
//		CarvedExecution carvedExecution = carvedExecutions.iterator().next();
//
//		assertConsistencyOfCarvedExecution(carvedExecution);
//	}
//
//	@Test
//	public void testCarvingResultActivityOnDestroy() throws FileNotFoundException, IOException, ABCException {
//		// Basic Carver gets' only a sigle structre
//		BasicCarver carver = new BasicCarver(parsedTrace);
//		// Carve the method call number 62.
//		/*
//		 * The ResultActivity is triggered by the MainActivity. but the basic carving
//		 * cannot find the correlation between the two activities via the Intent
//		 */
//		MethodInvocation mainActivityOnStart = parsedTrace.getUIThreadParsedTrace().getFirst()
//				.getOrderedMethodInvocations().get(62);
//
//		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityOnStart);
//
//		// Assert there's only one carved execution
//		Assert.assertEquals(1, carvedExecutions.size());
//		// Assert that this execution containt ONLY the targetMethodInvocation
//		CarvedExecution carvedExecution = carvedExecutions.iterator().next();
//		List<MethodInvocation> actual = carvedExecution.getFirst().getOrderedMethodInvocations();
//
//		// Check that the trace contains at least all the life-cycle methods
//		MethodInvocation lastMethodInvocation = null;
//		for (MethodInvocation mi : actual) {
////			mi.getActualParameters()
////			mi.getReturnValue()
//			System.out.println(carvedExecution.getSecond().getParametersOf(mi) + "-->" + mi + "-->"
//					+ carvedExecution.getSecond().getReturnValue(mi));
//		}
//
//		assertConsistencyOfCarvedExecution(carvedExecution);
//	}
//
//	// TODO Move to an HamCrest matcher
//	private void assertConsistencyOfCarvedExecution(CarvedExecution carvedExecution) {
//		List<MethodInvocation> actual = carvedExecution.getFirst().getOrderedMethodInvocations();
//
//		// Ensures that all the graphs contain exactly the same method invocations
//		List<MethodInvocation> actualFromDataDependencyGraph = new ArrayList<MethodInvocation>(
//				carvedExecution.getSecond().getAllMethodInvocations());
//		// Sort for easy comparison
//		Collections.sort(actualFromDataDependencyGraph);
//
//		Assert.assertThat("ExecutionFlowGraph and DataDependencyGraphs do not contain the same method invocations",
//				actual, is(actualFromDataDependencyGraph));
//
//		// Ensures that all the graphs contain exactly the same method invocations
//		List<MethodInvocation> actualFromCallGraph = new ArrayList<MethodInvocation>(
//				carvedExecution.getThird().getAllMethodInvocations());
//		// Sort for easy comparison
//		Collections.sort(actualFromCallGraph);
//		Assert.assertThat("ExecutionFlowGraph and CallGraph do not contain the same method invocations", actual,
//				is(actualFromDataDependencyGraph));
//
//	}
}
