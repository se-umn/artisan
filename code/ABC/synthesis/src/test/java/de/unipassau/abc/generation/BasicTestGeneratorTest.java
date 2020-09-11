package de.unipassau.abc.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.TestCase;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestCaseOrganizers;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class BasicTestGeneratorTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	/**
	 * Because we carve a method that belongs to an activity, the carver do not
	 * include in the test methods that are invoked INSIDE the activity.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ABCException
	 */
	@Test
	public void testEndToEndSendResult() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testNullPointerThrownBySystem-trace.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

		// TODO How do we handle extensions like Android automatically?
		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
		ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

		Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
		final String targetSignature = "<abc.basiccalculator.MainActivity: void sendResult(android.view.View)>";

		// TODO Expose some method to easily find the method calls that we need
		parsedTrace.getUIThreadParsedTrace().getFirst().getOrderedMethodInvocations().stream()
				.filter(methodInvocation -> targetSignature.equals(methodInvocation.getMethodSignature()))
				.forEach(targetMethodsInvocations::add);

		// One only for this test
		Assume.assumeTrue(targetMethodsInvocations.size() == 1);

		TestGenerator basicTestGenerator = new BasicTestGenerator();
		Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations, parsedTrace);

		// Group CarvedTests in classes
		TestCaseOrganizer organizer = TestCaseOrganizers.byAllTestsTogether();
		Set<TestCase> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

		JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

		for (TestCase testCase : testSuite) {
			CompilationUnit cu = writer.generateJUnitTestCase(testCase);
			System.out.println(cu);
		}
	}

	/**
	 * This test is a bit trickier than the previous one. And it might not even be
	 * considered valid to some extend. The issue is that the eval method of the
	 * activity is invoked inside the sendResult method of the same activity.
	 * Because send result is invoked
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ABCException
	 */
	@Test
	public void testEndToEndEval() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testNullPointerThrownBySystem-trace.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

		// TODO How do we handle extensions like Android automatically?
		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
		ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

		Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
		final String targetSignature = "<abc.basiccalculator.MainActivity: java.lang.String eval(java.lang.String)>";

		// TODO Expose some method to easily find the method calls that we need
		parsedTrace.getUIThreadParsedTrace().getFirst().getOrderedMethodInvocations().stream()
				.filter(methodInvocation -> targetSignature.equals(methodInvocation.getMethodSignature()))
				.forEach(targetMethodsInvocations::add);

		// One only for this test
		Assume.assumeTrue(targetMethodsInvocations.size() == 1);

		TestGenerator basicTestGenerator = new BasicTestGenerator();
		Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations, parsedTrace);

		// Group CarvedTests in classes
		TestCaseOrganizer organizer = TestCaseOrganizers.byAllTestsTogether();
		Set<TestCase> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

		JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

		for (TestCase testCase : testSuite) {
			CompilationUnit cu = writer.generateJUnitTestCase(testCase);
			System.out.println(cu);
		}
	}

	@Test
	public void testAssertionOnPrimitives() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

		// TODO How do we handle extensions like Android automatically?
		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
		ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

		Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
		final String targetSignature = "<abc.basiccalculator.MainActivity: java.lang.String eval(java.lang.String)>";

		// TODO Expose some method to easily find the method calls that we need
		parsedTrace.getUIThreadParsedTrace().getFirst().getOrderedMethodInvocations().stream()
				.filter(methodInvocation -> targetSignature.equals(methodInvocation.getMethodSignature()))
				.forEach(targetMethodsInvocations::add);

		// One only for this test
		Assume.assumeTrue(targetMethodsInvocations.size() == 1);

		TestGenerator basicTestGenerator = new BasicTestGenerator();
		Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations, parsedTrace);

		// Group CarvedTests in classes
		TestCaseOrganizer organizer = TestCaseOrganizers.byAllTestsTogether();
		Set<TestCase> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

		JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

		for (TestCase testCase : testSuite) {
			CompilationUnit cu = writer.generateJUnitTestCase(testCase);
			System.out.println(cu);
		}
	}

}
