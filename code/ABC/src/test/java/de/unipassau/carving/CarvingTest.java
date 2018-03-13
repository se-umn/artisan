package de.unipassau.carving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.carving.carvers.Level_0_MethodCarver;
import de.unipassau.data.Pair;
import de.unipassau.data.Triplette;
import de.unipassau.generation.TestGenerator;
import de.unipassau.tracing.Trace;
import de.unipassau.utils.JimpleUtils;
import de.unipassau.utils.Slf4jSimpleLoggerRule;
import soot.SootClass;

public class CarvingTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Ignore
	@Test
	public void testFileTransformer() throws FileNotFoundException, IOException {
		System.setProperty("debug", "true");

		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");
		StackImplementation stackImplementation = new StackImplementation();

		stackImplementation.parseTrace(traceFile.getAbsolutePath());
		//
		fail("Not implemented! ");
	}

	@Ignore
	@Test
	public void testCarveStaticMethod() throws FileNotFoundException, IOException, InterruptedException {
		// StaticInvokeExpr;
		MethodInvocationMatcher staticMethodToCarve = MethodInvocationMatcher.fromJimpleMethod(
				"<org.employee.DummyObjectFactory: org.employee.DummyObjectToPassAsParameter createNewDummyObject()>");

		File traceFile = new File("./src/test/resources/Employee-trace-with-static.txt");
		StackImplementation stackImplementation = new StackImplementation();
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(staticMethodToCarve);

		// System tests contains 2 executions
		assertEquals(2, carvedTests.size());

		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			// The only invocation in the test is the static invocation of the
			// method
			assertEquals(1, carvedTest.getFirst().getOrderedMethodInvocations().size());
		}

		String employeeProjectJar = "./src/test/resources/Employee.jar";
		TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}
	}

	@Ignore
	@Test
	public void testCarveMethodWhichDependsOnFactoryStaticMethod()
			throws FileNotFoundException, IOException, InterruptedException {
		// This method gets a dummy object which is created via static factory
		MethodInvocationMatcher methodToCarve = MethodInvocationMatcher.fromJimpleMethod(
				"<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>");
		//
		File traceFile = new File("./src/test/resources/Employee-trace-with-static.txt");
		StackImplementation stackImplementation = new StackImplementation();
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarve);

		// The trace contains 2 executions. start -> exit, start -> register
		// user -> exit
		assertEquals(8, carvedTests.size());
		//
		String staticJimpleMethodProvidingObjects = "<org.employee.DummyObjectFactory: org.employee.DummyObjectToPassAsParameter createNewDummyObject()>";
		// Two of those tests MUST include two invocations to
		// DummyObjectFactory.
		// TODO Maybe define a Matcher on those graphs ?
		int matchCount = 0;
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			ExecutionFlowGraph carvedExecutionFlowGraph = carvedTest.getFirst();
			// carvedTest.getSecond().visualize();
			for (MethodInvocation methodInvocation : carvedExecutionFlowGraph.getOrderedMethodInvocations()) {
				if (staticJimpleMethodProvidingObjects.equals(methodInvocation.getJimpleMethod())) {
					System.out.println("CarvingTest.testCarveMethodWhichDependsOnFactoryStaticMethod() Found "
							+ methodInvocation.getJimpleMethod());
					matchCount++;
				}
			}
		}

		assertEquals("Missing invocations of " + staticJimpleMethodProvidingObjects, 4, matchCount);

		String employeeProjectJar = "./src/test/resources/Employee.jar";
		TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}
	}

	@Ignore
	@Test
	public void testCarveMethodWithDependencies() throws FileNotFoundException, IOException {
		// System.setProperty("debug", "true");

		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";

		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");
		StackImplementation stackImplementation = new StackImplementation();

		stackImplementation.parseTrace(traceFile.getAbsolutePath());

		fail("Not implemented! ");

	}

	@Ignore
	@Test
	public void testCallGraphGeneration() throws FileNotFoundException, IOException {
		System.setProperty("debug", "true");
		String objectId = "org.employee.Validation@2114874018";
		String dependencyObjectId = "org.employee.Dummy@2114874017";
		String trace =
				// Instantiate Dummy
				Trace.METHOD_START_TOKEN + "SpecialInvokeExpr;<org.employee.Dummy: void <init>()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN + "<org.employee.Dummy: void <init>()>;" + dependencyObjectId + "\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Dummy: void <init>()>;null" + "\n" +
						// Invoke foo on Dummy
						Trace.METHOD_START_TOKEN + "VirtualInvokeExpr;<org.employee.Dummy: void foo()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN + "<org.employee.Dummy: void foo()>;" + dependencyObjectId + "\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Dummy: void foo()>;null" + "\n" +
						//
						Trace.METHOD_START_TOKEN
						+ "SpecialInvokeExpr;<org.employee.Validation: void <init>(org.employee.Dummy)>;(org.employee.Dummy@2114874017)"
						+ "\n" + Trace.METHOD_OBJECT_TOKEN
						+ "<org.employee.Validation: void <init>(org.employee.Dummy)>;" + objectId + "\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Validation: void <init>(org.employee.Dummy)>;null"
						+ "\n" +
						//
						Trace.METHOD_START_TOKEN + "SpecialInvokeExpr;<org.employee.Validation: void zot()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN + "<org.employee.Validation: void zot()>;" + objectId + "\n" +
						// Nested call
						Trace.METHOD_START_TOKEN + "VirtualInvokeExpr;<org.employee.Dummy: void bar()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN + "<org.employee.Dummy: void bar()>;" + dependencyObjectId + "\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Dummy: void bar()>;null" + "\n" +

						Trace.METHOD_END_TOKEN + "<org.employee.Validation: void zot()>;null";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);
		}
		StackImplementation stackImplementation = new StackImplementation();

		// Parse trace and fill up Graph_Details.
		stackImplementation.parseTraceFile(traceFile.getAbsolutePath());

		stackImplementation.getCallGraph().visualize();

		assertNotNull(stackImplementation.getCallGraph());

		fail("Not implemented! ");

	}

	@Ignore
	@Test
	public void testExecutionGraphGeneration() throws FileNotFoundException, IOException {
		System.setProperty("debug", "true");
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String)>";

		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");
		StackImplementation stackImplementation = new StackImplementation();

		// Not sure what's this

		// Parse trace and fill up Graph_Details.
		stackImplementation.parseTraceFile(traceFile.getAbsolutePath());
		//
		// stackImplementation.buildExecutionFlowGraph();
		// stackImplementation.executionGraphView();
		// stackImplementation.getExectuionFlowGraph().visualize();
		//
		assertNotNull(stackImplementation.getExectuionFlowGraph());

		fail("Not implemented! ");

	}

	@Ignore
	@Test
	public void testDependencyGraphGeneration() throws FileNotFoundException, IOException {
		// Parse trace and fill up Graph_Details.
		String objectId = "org.employee.Validation@2114874018";
		String trace =
				// Instantiate Dummy
				Trace.METHOD_START_TOKEN + "SpecialInvokeExpr;<org.employee.Dummy: void <init>()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN
						+ "<org.employee.Dummy: void <init>()>;org.employee.Dummy@2114874017\n" + Trace.METHOD_END_TOKEN
						+ "<org.employee.Dummy: void <init>()>;null" + "\n" +
						// Invoke foo on Dummy
						Trace.METHOD_START_TOKEN + "VirtualInvokeExpr;<org.employee.Dummy: void foo()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN + "<org.employee.Dummy: void foo()>;org.employee.Dummy@2114874017\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Dummy: void foo()>;null" + "\n" +
						//
						Trace.METHOD_START_TOKEN
						+ "SpecialInvokeExpr;<org.employee.Validation: void <init>(org.employee.Dummy)>;(org.employee.Dummy@2114874017)"
						+ "\n" + Trace.METHOD_OBJECT_TOKEN
						+ "<org.employee.Validation: void <init>(org.employee.Dummy)>;" + objectId + "\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Validation: void <init>(org.employee.Dummy)>;null";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);
		}
		StackImplementation stackImplementation = new StackImplementation();

		// Not sure what's this

		// Parse trace and fill up Graph_Details.
		stackImplementation.parseTraceFile(traceFile.getAbsolutePath());
		// Demetras' law
		stackImplementation.getDataDependencyGraph().visualize();

		//
		assertNotNull(stackImplementation.getDataDependencyGraph());

		fail("Not implemented! ");

	}

	@Ignore
	@Test
	public void testCarvingShort() throws IOException {
		// Parse trace and fill up Graph_Details.
		String objectId = "org.employee.Validation@2114874018";
		String jimpleMethod = "<org.employee.Validation: void <init>(org.employee.Dummy)>";
		String trace =
				// Instantiate Dummy
				Trace.METHOD_START_TOKEN + "SpecialInvokeExpr;<org.employee.Dummy: void <init>()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN
						+ "<org.employee.Dummy: void <init>()>;org.employee.Dummy@2114874017\n" + Trace.METHOD_END_TOKEN
						+ "<org.employee.Dummy: void <init>()>" + "\n" +
						// Invoke foo on Dummy
						Trace.METHOD_START_TOKEN + "VirtualInvokeExpr;<org.employee.Dummy: void foo()>" + "\n"
						+ Trace.METHOD_OBJECT_TOKEN + "<org.employee.Dummy: void foo()>;org.employee.Dummy@2114874017\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Dummy: void foo()>" + "\n" +
						//
						Trace.METHOD_START_TOKEN
						+ "SpecialInvokeExpr;<org.employee.Validation: void <init>(org.employee.Dummy)>;(org.employee.Dummy@2114874017)"
						+ "\n" + Trace.METHOD_OBJECT_TOKEN
						+ "<org.employee.Validation: void <init>(org.employee.Dummy)>;" + objectId + "\n"
						+ Trace.METHOD_END_TOKEN + "<org.employee.Validation: void <init>(org.employee.Dummy)>";

		File traceFile = temporaryFolder.newFile("trace");
		try (PrintStream out = new PrintStream(new FileOutputStream(traceFile))) {
			out.print(trace);
		}

		StackImplementation stackImplementation = new StackImplementation();
		stackImplementation.parseTraceFile(traceFile.getAbsolutePath());
		//
		// This works by updating the static class {@link Graph_Details}
		stackImplementation.getExectuionFlowGraph().visualize();
		stackImplementation.getDataDependencyGraph().visualize();
		stackImplementation.getCallGraph().visualize();

		// List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestCases =
		// stackImplementation
		// .methodCarving(jimpleMethod);

		// assertEquals(1, carvedTestCases.size());

		// TODO Make assertions here
		// for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTestCase :
		// carvedTestCases) {
		// carvedTestCase.getFirst().visualize();
		// carvedTestCase.getSecond().visualize();
		// }
		fail("Not implemented! ");
	}

	@Ignore
	@Test
	public void testCarving() throws FileNotFoundException, IOException {
		//
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String)>";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();

		// List<Pair<ExecutionFlowGraph, DataDependencyGraph>> testCases =
		// stackImplementation.parseTrace(traceFile.getAbsolutePath(),
		// methodToCarve);

		// This works by updating the static class {@link Graph_Details}
		stackImplementation.getExectuionFlowGraph().visualize();
		stackImplementation.getDataDependencyGraph().visualize();
		stackImplementation.getCallGraph().visualize();

		// assertEquals(1, testCases.size());
		// System.out.println("CarvingTest.testCarving() " + testCases.get(0));
		fail("Not implemented! ");
	}

	@Ignore
	@Test
	public void testCarvingMethodWithDependencey() throws FileNotFoundException, IOException {
		//
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestCases =
		// stackImplementation
		// .parseTrace(traceFile.getAbsolutePath(), methodToCarve);

		// This works by updating the static class {@link Graph_Details}
		stackImplementation.getExectuionFlowGraph().visualize();
		stackImplementation.getDataDependencyGraph().visualize();
		stackImplementation.getCallGraph().visualize();

		// assertEquals(2, carvedTestCases.size());

		// TODO Make assertions here
		// for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTestCase :
		// carvedTestCases) {
		// carvedTestCase.getFirst().visualize();
		// carvedTestCase.getSecond().visualize();
		// assertEquals(2, carvedTestCases.size());
		// }
		fail("Not implemented! ");
	}

}
