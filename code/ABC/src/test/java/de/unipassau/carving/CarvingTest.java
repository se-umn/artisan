package de.unipassau.carving;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.tracing.Trace;
import de.unipassau.utils.Slf4jSimpleLoggerRule;

public class CarvingTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Test
	public void testFileTransformer() throws FileNotFoundException, IOException {
		System.setProperty("debug", "true");

		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String)>";

		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");
		StackImplementation stackImplementation = new StackImplementation();

		stackImplementation.parseTrace(traceFile.getAbsolutePath());
		//
		fail("Not implemented! ");
	}
	@Test
	public void testCarveMethodWithDependencies() throws FileNotFoundException, IOException {
		// System.setProperty("debug", "true");

		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";

		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");
		StackImplementation stackImplementation = new StackImplementation();

		stackImplementation.parseTrace(traceFile.getAbsolutePath());
		
		fail("Not implemented! ");

	}

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
//		stackImplementation.getExectuionFlowGraph().visualize();
		//
		assertNotNull(stackImplementation.getExectuionFlowGraph());
		
		fail("Not implemented! ");
		
	}

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

//		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestCases = stackImplementation
//				.methodCarving(jimpleMethod);
		
//		assertEquals(1, carvedTestCases.size());

		// TODO Make assertions here
//		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTestCase : carvedTestCases) {
//			carvedTestCase.getFirst().visualize();
//			carvedTestCase.getSecond().visualize();
//		}
		fail("Not implemented! ");
	}

	@Test
	public void testCarving() throws FileNotFoundException, IOException {
		//
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String)>";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
		
		
//		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> testCases = stackImplementation.parseTrace(traceFile.getAbsolutePath(), methodToCarve);

		// This works by updating the static class {@link Graph_Details}
		stackImplementation.getExectuionFlowGraph().visualize();
		stackImplementation.getDataDependencyGraph().visualize();
		stackImplementation.getCallGraph().visualize();

//		assertEquals(1, testCases.size());
//		System.out.println("CarvingTest.testCarving() " + testCases.get(0));
		fail("Not implemented! ");
	}

	@Test
	public void testCarvingMethodWithDependencey() throws FileNotFoundException, IOException {
		//
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
//		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestCases = stackImplementation
//				.parseTrace(traceFile.getAbsolutePath(), methodToCarve);

		// This works by updating the static class {@link Graph_Details}
		stackImplementation.getExectuionFlowGraph().visualize();
		stackImplementation.getDataDependencyGraph().visualize();
		stackImplementation.getCallGraph().visualize();

//		assertEquals(2, carvedTestCases.size());

		// TODO Make assertions here
//		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTestCase : carvedTestCases) {
//			carvedTestCase.getFirst().visualize();
//			carvedTestCase.getSecond().visualize();
//			assertEquals(2, carvedTestCases.size());
//		}
		fail("Not implemented! ");
	}

}
