package de.unipassau.abc.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.checkerframework.checker.units.qual.m;
import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.TestCase;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;

public class JUnitTestCaseWriteTest {
// THIS IS NOT VISIBLE
//	@Rule
//	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);
//
//	// TODO Brittle, but cannot do otherwise for the moment. Maybe something from
//	// the ENV?
//	// Or some known location inside the project (with symlink as well)
	private final static File ANDROID_JAR = new File(
			"/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	@Test
	public void testGenerateTestForNoArgsCostructor() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseTrace(traceFile);

		// Basic Carver gets' only a sigle structre
		BasicCarver carver = new BasicCarver(parsedTrace);

		// The first method invocation is the constructor of the Main Activity
		MethodInvocation mainActivityConstructor = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(0);

		List<CarvedExecution> carvedExecutions = carver.carve(mainActivityConstructor);

		CarvedExecution carvedExecution = carvedExecutions.get(0);

		// Take only the first instruction and the variable to use
		MethodInvocation methodInvocationUnderTest = carvedExecution.methodInvocationUnderTest;

		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		executionFlowGraph.enqueueMethodInvocations(methodInvocationUnderTest);

		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
		dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocationUnderTest);
		dataDependencyGraph.addDataDependencyOnOwner(methodInvocationUnderTest, methodInvocationUnderTest.getOwner());

		//
		CarvedTest carvedTest = new CarvedTest(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph);

		Set<CarvedTest> carvedTests = new HashSet<CarvedTest>();
		carvedTests.add(carvedTest);

		JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

		TestCase testCase = new TestCase("abc.test", "DummyTest", carvedTests);
		CompilationUnit cu = writer.generateJUnitTestCase(testCase);
		System.out.println("JUnitTestCaseWriteTest.smokeTest() " + cu);
	}

	@Test
	public void testGenerateTestSimpleInvocationSequence() throws FileNotFoundException, IOException, ABCException {
		File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace.txt");
		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

		// Really needed?
		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

		TraceParser parser = new TraceParserImpl();
		ParsedTrace parsedTrace = parser.parseTrace(traceFile);

		// Basic Carver gets' only a sigle structre
		BasicCarver carver = new BasicCarver(parsedTrace);

		// The first method invocation is the constructor of the Main Activity
		MethodInvocation targetMethodInvocation = parsedTrace.getUIThreadParsedTrace().getFirst()
				.getOrderedMethodInvocations().get(2);

		List<CarvedExecution> carvedExecutions = carver.carve(targetMethodInvocation);

		CarvedExecution carvedExecution = carvedExecutions.get(0);

		// Take only the first instruction and the variable to use
		MethodInvocation methodInvocationUnderTest = carvedExecution.methodInvocationUnderTest;

		// Take only the call graphs at the highest level (root) and order them by index
		List<MethodInvocation> topLevelMethodInvocations = new ArrayList<MethodInvocation>();
		for (CallGraph callGraph : carvedExecution.callGraphs) {
			topLevelMethodInvocations.addAll(callGraph.getRoots());
		}
		// At this point those method invocations subsume all the required method
		// invocations, not we need to "sort" them and create the final sequence of
		// method calls
		Collections.sort(topLevelMethodInvocations);

		// TODO: I cannot understand the following. If the method invocations already
		// contains the refernce to various objects and data and the like, why do we
		// need to propagate the data dependency graph?
		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
		for (MethodInvocation methodInvocation : topLevelMethodInvocations) {
			executionFlowGraph.enqueueMethodInvocations(methodInvocation);
			// Fix the data deps
			dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocation);
			if (!methodInvocation.isStatic()) {
				dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, methodInvocation.getOwner());
			}
			if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())) {
				dataDependencyGraph.addDataDependencyOnReturn(methodInvocation, methodInvocation.getReturnValue());
			}

			ListIterator<DataNode> it = methodInvocation.getActualParameterInstances().listIterator();
			while (it.hasNext()) {
				int position = it.nextIndex();
				DataNode parameter = it.next();
				dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocation, parameter, position);
			}
		}

		//
		CarvedTest carvedTest = new CarvedTest(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph);

		Set<CarvedTest> carvedTests = new HashSet<CarvedTest>();
		carvedTests.add(carvedTest);

		JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

		TestCase testCase = new TestCase("abc.test", "DummyTest", carvedTests);
		CompilationUnit cu = writer.generateJUnitTestCase(testCase);
		System.out.println("JUnitTestCaseWriteTest.smokeTest() " + cu);
	}
}
