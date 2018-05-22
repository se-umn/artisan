package de.unipassau.abc.carving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.TestCaseFactory;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.generation.impl.AllTestTogether;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.JimpleUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.SootClass;
import soot.SootMethod;

public class CarveWithExpansionsTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@ClassRule // This seams not to be respect ?!
	public static Slf4jSimpleLoggerRule loggerLeverRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	private final String methodToCarveString = "<de.unipassau.abc.testsubject.DummyObject: void end()>";
	private final MethodInvocationMatcher methodToCarveMatcher = MethodInvocationMatcher
			.fromJimpleMethod(methodToCarveString);
	// This matcher returns always false
	private final List<MethodInvocationMatcher> excludeNoMethodInvocationsMatcher = Collections
			.singletonList(MethodInvocationMatcher.noMatch());

	private final List<MethodInvocationMatcher> emptyMethodInvocationMatcherList = new ArrayList<MethodInvocationMatcher>();

	// @Before
	// public void setupSoot(){
	// Carver.setupSoot( new ArrayList<File>());
	// }
	// @After
	// public void resetSoot(){
	// G.reset();
	// }

	@Ignore
	@Test
	public void testExpansionWithSimpleStatic() {
		try {
			File traceFile = new File("./src/test/resources/DummySystemTestGetSimple-trace.txt");

			StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
			// Parsing
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
					.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

			// visualize(parsedTrace);
			assertEquals(1, parsedTrace.size());

			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values()
					.iterator().next();
			// Carving
			Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
					parsedSystemTest.getSecond(), parsedSystemTest.getThird());

			List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
					excludeNoMethodInvocationsMatcher);

			// DEBUG MOSTLY
			// visualize(carvedTests);
			/*
			 * We expect 2 carved test for this method because 1 is the expanded
			 * and 1 is with the method which returns the parameter
			 */
			assertEquals(2, carvedTests.size());

			String testSubjectJar = "./libs/testsubject-tests.jar";

			Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));

			TestGenerator testGenerator = new TestGenerator(parsedTrace);
			
			Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
			assertEquals(1, carvedTestCases.size());

			Set<SootClass> testCases = new HashSet<>();
			for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
				testCases.add( carvedTestCase.getThird().getDeclaringClass() );
			}
			
			// Visual debug
			for (SootClass testCase : testCases) {
				JimpleUtils.prettyPrint(testCase);
			}

			// Generate code
			File outputDir = temporaryFolder.newFolder();
//			TestCaseFactory.generateTestFiles( new ArrayList<File>(), outputDir, testCases, true);

			// TODO Assertion: 1 .class, 1 .java
			int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
			assertEquals(1, total);

			ABCTestUtils.printJavaClasses(outputDir);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	@Ignore
	@Test
	public void testExpansionWithModifiedStatic() throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetModified-trace.txt");
		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(2, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}

		// Visual debug
		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Ignore
	@Test
	public void testExpansionWithSimpleWithDelegate() throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimpleWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Ignore
	@Test
	/**
	 * In this case we use a delegate, and for some tests the call:
	 * <de.unipassau.abc.testsubject.DummyCreator: void <init>()>_3 is
	 * unconnected. Hence it is removed.
	 * 
	 * ... TODO Ideally we should make and assertion that checks that...
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void testExpansionWithModifiedWithDelegate()
			throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetModifiedWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}


		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Ignore
	@Test
	/**
	 * In this case we use a delegate, and for some tests the call:
	 * <de.unipassau.abc.testsubject.DummyCreator: void <init>()>_3 is
	 * unconnected. However, we declare
	 * "de.unipassau.abc.testsubject.DummyCreator" as external interface, which
	 * forces any of its calls to remain there, as they potentially affect the
	 * state of the application
	 * 
	 * ... TODO Ideally we should make and assertion that checks that...
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void testExpansionWithModifiedWithDelegateAndExternalInterface()
			throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetModifiedWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		List<MethodInvocationMatcher> externalInterfaceMatchers = new ArrayList<>();
		externalInterfaceMatchers.add(MethodInvocationMatcher.byClass("de.unipassau.abc.testsubject.DummyCreator"));

		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Ignore
	@Test
	public void testExpansionWithDoubleModifiedWithDelegate()
			throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetDoubleModifiedWithDelegate-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);

		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter TODO Does 3
		 * correspond to nesting level? Or distance from <init> -> MUT
		 */
		assertEquals(3, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}

		// Visual debug
		// for (SootClass testCase : testCases) {
		// JimpleUtils.prettyPrint(testCase);
		// }

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}
	
	@Ignore
	@Test
	public void testExpansionWithSimpleWithParameter() throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimpleWithParameter-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);

		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// DEBUG MOSTLY
		// visualize(carvedTests);
		/*
		 * We expect 2 carved test for this method because 1 is the expanded and
		 * 1 is with the method which returns the parameter TODO Does 3
		 * correspond to nesting level? Or distance from <init> -> MUT
		 */

		assertEquals(4, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}

		// Visual debug
		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}

	@Ignore
	@Test
	public void testExpansionWithSimpleWithNonRequiredParameter()
			throws Exception{
		File traceFile = new File("./src/test/resources/DummySystemTestGetSimpleWithNonRequiredParameter-trace.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);

		// Parsing
		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = stackImplementation
				.parseTrace(traceFile.getAbsolutePath(), emptyMethodInvocationMatcherList);

		assertEquals(1, parsedTrace.size());

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedSystemTest = parsedTrace.values().iterator()
				.next();
		// Carving
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedSystemTest.getFirst(),
				parsedSystemTest.getSecond(), parsedSystemTest.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodToCarveMatcher,
				excludeNoMethodInvocationsMatcher);

		// ABCTestUtils.visualize(carvedTests);

		assertEquals(4, carvedTests.size());

		String testSubjectJar = "./libs/testsubject-tests.jar";

		Carver.setupSoot(Collections.singletonList(new File(testSubjectJar)));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);

		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for( Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases ){
			testCases.add( carvedTestCase.getThird().getDeclaringClass() );
		}

		// Visual debug
		for (SootClass testCase : testCases) {
			JimpleUtils.prettyPrint(testCase);
		}

		// Generate code
		File outputDir = temporaryFolder.newFolder();
//		TestCaseFactory.generateTestFiles(new ArrayList<File>(), outputDir, testCases, true);

		int total = ABCTestUtils.countFiles(outputDir, ".class") + ABCTestUtils.countFiles(outputDir, ".java");
		assertEquals(2, total);

		ABCTestUtils.printJavaClasses(outputDir);

	}

}
