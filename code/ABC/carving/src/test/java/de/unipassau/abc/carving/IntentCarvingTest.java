package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.android.TaintendIntentCarver;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class IntentCarvingTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);
//
//	// TODO Brittle, but cannot do otherwise for the moment. Maybe something from
//	// the ENV?
//	// Or some known location inside the project (with symlink as well)
	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	@Test
	public void testCarvePutExtraIntOnIntent() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-taintedIntents.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseTrace(traceFile);

		BasicCarver carver = new BasicCarver(parsedTrace);
		// This method will use the intent as parameter but the intent has not alias,
		// i.e., no other intents it has been linked to before this usage
		// 26 | [>>];abc.basiccalculator.MainActivity@4728355;<android.app.Activity:
		// void
		// startActivity(android.content.Intent)>;(android.content.Intent@159559917);

		// The first method invocation is the constructor of the Main Activity
		MethodInvocation methodInvocation = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(9);

		List<CarvedExecution> carvedExecutions = carver.carve(methodInvocation);

		for (CarvedExecution carvedExecution : carvedExecutions) {
			for (ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs) {
				System.out.println(executionFlowGraph.getOrderedMethodInvocations());
			}
		}
	}

	@Test
	public void testCarveIntentAsParameterWithTaintButNoAlias()
			throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-taintedIntents.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseTrace(traceFile);

		TaintendIntentCarver intentCarver = new TaintendIntentCarver(parsedTrace);
		// This method will use the intent as parameter but the intent has not alias,
		// i.e., no other intents it has been linked to before this usage
		// 26 | [>>];abc.basiccalculator.MainActivity@4728355;<android.app.Activity:
		// void
		// startActivity(android.content.Intent)>;(android.content.Intent@159559917);

		// The first method invocation is the constructor of the Main Activity
		MethodInvocation methodInvocation = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(25);
		// The intent is the first and only parameter of this invocation
		ObjectInstance intent = (ObjectInstance) methodInvocation.getActualParameterInstances().get(0);

		List<CarvedExecution> carvedExecutions = intentCarver.carveAsParameter(intent, methodInvocation);

		for (CarvedExecution carvedExecution : carvedExecutions) {
			for (ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs) {
				System.out.println(executionFlowGraph.getOrderedMethodInvocations());
			}
		}
	}

	@Test
	public void testCarveIntentAsReturnValueWithTaintAndAlias()
			throws FileNotFoundException, IOException, ABCException {
		// Test the case in which the app creates the intent and trigger an activity.
		// Note: This is different than having an "external" intent starting the
		// activity
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-taintedIntents.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseTrace(traceFile);

		TaintendIntentCarver intentCarver = new TaintendIntentCarver(parsedTrace);

		/*
		 * This method will return the intent as return value. // 32 |
		 * [>>];abc.basiccalculator.ResultActivity@28191787;<android.app.Activity:
		 * android.content.Intent getIntent()>;(); We are interested to see if the
		 * carver can make the connection with the intent that was linked to this via
		 * taint
		 */
		MethodInvocation methodInvocation = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(31);
		// The intent is the first and only parameter of this invocation
		ObjectInstance intent = (ObjectInstance) methodInvocation.getReturnValue();

		List<CarvedExecution> carvedExecutions = intentCarver.carveAsReturnValue(intent, methodInvocation);

		for (CarvedExecution carvedExecution : carvedExecutions) {
			for (ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs) {
				System.out.println(executionFlowGraph.getOrderedMethodInvocations());
			}
		}
	}

}
