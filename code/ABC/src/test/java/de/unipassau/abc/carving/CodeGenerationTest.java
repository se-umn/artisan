package de.unipassau.abc.carving;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.StackImplementation;
import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.utils.JimpleUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.SootClass;

public class CodeGenerationTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	private final MethodInvocationMatcher excludeNoMethodInvocationsMatcher = MethodInvocationMatcher.noMatch();

	@Ignore
	@Test
	public void generateTestCasesForEmployeeProject() throws FileNotFoundException, IOException, InterruptedException {
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		String employeeProjectJar = "./src/test/resources/Employee.jar";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
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
	public void generateTestCode() throws FileNotFoundException, IOException, InterruptedException {
		String jimpleMethod = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		int invocationID = 116;

		StackImplementation stackImplementation = new StackImplementation();
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = stackImplementation
				.parseTrace("./src/test/resources/Employee-trace.txt");

		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());

		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(
				MethodInvocationMatcher.fromMethodInvocation(new MethodInvocation(jimpleMethod, invocationID)),
				excludeNoMethodInvocationsMatcher);

		File projectJar = new File("./src/test/resources/Employee.jar");
		TestGenerator testCaseGenerator = new TestGenerator(projectJar.getAbsolutePath());
		Collection<SootClass> testCases = testCaseGenerator.generateTestCases(carvedTests);

		assertEquals(1, testCases.size());
		SootClass testCase = testCases.iterator().next();

		JimpleUtils.prettyPrint(testCase);
	}

	@Ignore
	@Test
	public void generateTestCodeForEmployeeProject() throws FileNotFoundException, IOException, InterruptedException {
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		String employeeProjectJar = "./src/test/resources/Employee.jar";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
		// List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests =
		// stackImplementation
		// .fileTransformer(traceFile.getAbsolutePath(), methodToCarve);

		// for( Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest :
		// carvedTests ){
		// carvedTest.getFirst().visualize();
		// carvedTest.getSecond().visualize();
		// }
		//
		//
		// TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
		// Collection<SootClass> testCases =
		// testGenerator.generateTestCases(carvedTests);
		//
		// assertEquals(1, testCases.size());
		//
		// System.out.println("CodeGenerationTest.generateTestCasesForEmployeeProject()
		// test cases " + testCases);
		//
		// SootClass testCase = testCases.iterator().next();
		// assumeTrue(2 == testCase.getMethods().size());
		//
		// // Assertions
		// File outputDir = temporaryFolder.newFolder();
		// File outputDir = new File(SourceLocator.v().getOutputDir());

		// TestCaseFactory.generateTestFiles(outputDir, testCases );
		//
		// // Make assertions here becayse the file and folder will be deleted
		// after the test by the rule !
		// File[] generatedFiles = outputDir.listFiles();
		// // one .class and one .java
		// assertEquals(2, generatedFiles.length);
		//
		// // Print the java files
		// for( File file : generatedFiles ){
		//
		// System.out.println( file.getAbsolutePath() );
		// assertTrue( "Empty output file! " + file.getAbsolutePath() ,
		// file.length() > 0 );
		// System.out.println( file.lastModified());
		//
		// if( file.getAbsolutePath().endsWith(".java")){
		// System.out.println("CodeGenerationTest.generateTestCodeForEmployeeProject()
		// Reading from java file");
		// List<String> allLines = Files.readAllLines( file.toPath(),
		// Charset.defaultCharset());
		// for( String line : allLines ){
		// System.out.println( line );
		// }
		// }
		// }

		fail("Not implemented! ");

	}

}
