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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.generation.impl.AllTestTogether;
import de.unipassau.abc.instrumentation.JimpleUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.SootClass;
import soot.SootMethod;

public class CodeGenerationTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	private final List<MethodInvocationMatcher> excludeNoMethodInvocationsMatcher = Collections
			.singletonList(MethodInvocationMatcher.noMatch());

	private final List<MethodInvocationMatcher> emptyMethodInvocationMatcherList = new ArrayList<MethodInvocationMatcher>();

	@Ignore
	@Test
	public void generateTestCasesForEmployeeProject() throws FileNotFoundException, IOException, InterruptedException {
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		String employeeProjectJar = "./src/test/resources/Employee.jar";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation(emptyMethodInvocationMatcherList);
		// List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests =
		// stackImplementation
		// .fileTransformer(traceFile.getAbsolutePath(), methodToCarve);

		// TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
		// Collection<SootClass> testCases =
		// testGenerator.generateTestCases(carvedTests);

		// assertEquals(1, testCases.size());

		// System.out.println("CodeGenerationTest.generateTestCasesForEmployeeProject()
		// test cases " + testCases);

		// Assertions
		// SootClass testCase = testCases.iterator().next();

		// assertEquals(2, testCase.getMethods().size());

		// JimpleUtils.prettyPrint(testCase);
		fail("Not implemented! ");

	}

	@Ignore
	@Test
	public void generateTestCode() throws Exception{
		String jimpleMethod = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		int invocationID = 116;
		File traceFile = new File("./src/test/resources/Employee-trace.txt");

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

		List<MethodInvocation> methodsInvocationsToCarve = new ArrayList<>(
		        parsedSystemTest.getFirst().getMethodInvocationsFor(
		                MethodInvocationMatcher.fromMethodInvocation(new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, jimpleMethod, invocationID)),
		                excludeNoMethodInvocationsMatcher.toArray(new MethodInvocationMatcher[]{})));
		
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(methodsInvocationsToCarve);
				

		File projectJar = new File("./src/test/resources/Employee.jar");
		Carver.setupSoot(Collections.singletonList(projectJar));
		TestGenerator testGenerator = new TestGenerator(parsedTrace);
		Collection<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testGenerator
				.generateTestCases(carvedTests, new AllTestTogether());
		assertEquals(1, carvedTestCases.size());

		Set<SootClass> testCases = new HashSet<>();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {
			testCases.add(carvedTestCase.getThird().getDeclaringClass());
		}
		SootClass testCase = testCases.iterator().next();

		JimpleUtils.prettyPrint(testCase);
	}

}
