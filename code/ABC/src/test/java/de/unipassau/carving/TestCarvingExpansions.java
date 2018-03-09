package de.unipassau.carving;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.unipassau.carving.carvers.Level_0_MethodCarver;
import de.unipassau.data.Pair;
import de.unipassau.data.Triplette;

public class TestCarvingExpansions {

	// This method is commonly invoked as last in any system test on the created
	// DummyInstance
	private final String staticMethodToCarve = "<de.unipassau.testsubject.DummyObject: void end()>";

	@Test
	public void testExpansionWithSimpleStatic() throws FileNotFoundException, IOException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimple-trace.txt");
		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(staticMethodToCarve);

		// DEBUG MOSTLY
		visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(2, carvedTests.size());

		// // System tests contains 2 executions
		//
		// for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest :
		// carvedTests) {
		// // The only invocation in the test is the static invocation of the
		// // method
		// assertEquals(1,
		// carvedTest.getFirst().getOrderedMethodInvocations().size());
		// }
		//
		// String employeeProjectJar = "./src/test/resources/Employee.jar";
		// TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
		// Collection<SootClass> testCases =
		// testGenerator.generateTestCases(carvedTests);
		// assertEquals(1, testCases.size());
		//
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }
	}

	private void visualize(Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace) {
		parsedTrace.getFirst().visualize();
		parsedTrace.getSecond().visualize();
		parsedTrace.getThird().visualize();
	}

	private void visualize(List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests) {
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			carvedTest.getFirst().visualize();
			carvedTest.getSecond().visualize();
		}

	}
}
