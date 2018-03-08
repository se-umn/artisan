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
		String traceFile = args[1]; // "./src/main/resources/trace.txt";

		// Parse the trace file into graphs
		StackImplementation si = new StackImplementation();
		// TODO How to handle multiple trace files ? All together or one after
		// another?
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = si.parseTrace(traceFile);

		// Carving 
		// TODO Here organize, instantiate and execute the configured Carvers
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToBeCarved);
		
		// Test Generation
		String projectJar = args[2];
		TestGenerator testCaseGenerator = new TestGenerator(projectJar);
		Collection<SootClass> testCases = testCaseGenerator.generateTestCases(carvedTests);
		
		// Code Generator
		String outputDir = args[3];
		TestCaseFactory.generateTestFiles(new File( outputDir ), testCases);
		long endTime = System.nanoTime();
		
		//
		logger.info("Unit Test Generation " + (endTime - startTime) / 1000000000.0 + " s");
//
		System.exit(0);
	}

}
