package de.unipassau.carving;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.carving.carvers.Level_0_MethodCarver;
import de.unipassau.data.Pair;
import de.unipassau.data.Triplette;
import de.unipassau.generation.TestCaseFactory;
import de.unipassau.generation.TestGenerator;
import soot.SootClass;

// TODO Use some sort of CLI/JewelCLI
public class Carver {

	private static final Logger logger = LoggerFactory.getLogger(Carver.class);

	public static void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		String methodToBeCarved = args[0]; //
		String traceFile = args[1];

		System.out.println("Carver.main() Start parsing ");
		// Parse the trace file into graphs
		StackImplementation si = new StackImplementation();
		// TODO How to handle multiple trace files ? All together or one after
		// another?
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = si.parseTrace(traceFile);
		System.out.println("Carver.main() End parsing ");
		// Carving
		System.out.println("Carver.main() Start carving " + methodToBeCarved);
		// TODO Here organize, instantiate and execute the configured Carvers
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToBeCarved);
		System.out.println("Carver.main() End carving " + methodToBeCarved);

		System.out.println("Carver.main() Start code generation");
		// Test Generation
		String projectJar = args[2];
		TestGenerator testCaseGenerator = new TestGenerator(projectJar);
		Collection<SootClass> testCases = testCaseGenerator.generateTestCases(carvedTests);

		String outputDir = "./sootOutput/carvedTests";
		// Code Generator
		if (args.length > 3) {
			outputDir = args[3];
		}
		TestCaseFactory.generateTestFiles(new File(outputDir), testCases);
		System.out.println("Carver.main() End code generation");

		long endTime = System.nanoTime();

		logger.info("Unit Test Generation " + (endTime - startTime) / 1000000000.0 + " s");
		// Why is this required? Will the application hang otherwise
		// System.exit(0);
	}

}
