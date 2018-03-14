package de.unipassau.abc.carving;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.StackImplementation;
import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.TestCaseFactory;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.JimpleUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.SootClass;

public class CarveWithExpansionsTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLeverRule = new Slf4jSimpleLoggerRule(Level.DEBUG);
	
	private final String methodToCarveString = "<de.unipassau.abc.abc.testsubject.DummyObject: void end()>";
	private final MethodInvocationMatcher methodToCarveMatcher = MethodInvocationMatcher.fromJimpleMethod( methodToCarveString );
	// This matcher returns always false
	private final MethodInvocationMatcher excludeNoMethodInvocationsMatcher = MethodInvocationMatcher.noMatch();

	@Test
	public void testExpansionWithSimpleStatic() throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimple-trace.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		// visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(2, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Test
	public void testExpansionWithModifiedStatic() throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetModified-trace.txt");
		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		// visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(2, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Test
	public void testExpansionWithSimpleWithDelegate() throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimpleWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		// visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Test
	public void testExpansionWithModifiedWithDelegate()
			throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetModifiedWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		// visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Test
	public void testExpansionWithDoubleModifiedWithDelegate()
			throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetDoubleModifiedWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		// visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter TODO Does 3
		 * correspond to nesting level? Or distance from <init> -> MUT
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Test
	public void testExpansionWithSimpleWithParameter() throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimpleWithParameter-trace.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

		// visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter TODO Does 3
		 * correspond to nesting level? Or distance from <init> -> MUT
		 */

		assertEquals(4, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}
	
	@Test
	public void testExpansionWithSimpleWithNonRequiredParameter() throws FileNotFoundException, IOException, InterruptedException {
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimpleWithNonRequiredParameter-trace.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// Parsing
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath());

//		 visualize(parsedTrace);

		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher, excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
//		 visualize(carvedTests);

		 assertEquals(4, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";
		TestGenerator testGenerator = new TestGenerator(testSubjectJar);
		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
		assertEquals(1, testCases.size());

		// Visual debug
		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}

		// Generate code
		File outputDir = temporaryFolder.newFolder();
		TestCaseFactory.generateTestFiles(outputDir, testCases);

		// TODO Assertion: 1 .class, 1 .java
		assertEquals(2, outputDir.listFiles().length);

		ABCTestUtils.printJavaClasses(outputDir);

	}

}
