package de.unipassau.carving;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.utils.Slf4jSimpleLoggerRule;

public class CodeGenerationTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Test
	public void generateTestCasesForEmployeeProject() throws FileNotFoundException, IOException, InterruptedException {
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		String employeeProjectJar = "./src/test/resources/Employee.jar";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
//		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = stackImplementation
//				.fileTransformer(traceFile.getAbsolutePath(), methodToCarve);

//		TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
//		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);

//		assertEquals(1, testCases.size());

//		System.out.println("CodeGenerationTest.generateTestCasesForEmployeeProject() test cases " + testCases);

		// Assertions
//		SootClass testCase = testCases.iterator().next();

//		assertEquals(2, testCase.getMethods().size());

//		JimpleUtils.prettyPrint(testCase);
		fail("Not implemented! ");

	}

	@Test
	public void generateTestCodeForEmployeeProject() throws FileNotFoundException, IOException, InterruptedException {
		String methodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
		String employeeProjectJar = "./src/test/resources/Employee.jar";
		File traceFile = new File("./src/test/resources/Employee-trace-simple.txt");

		StackImplementation stackImplementation = new StackImplementation();
//		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = stackImplementation
//				.fileTransformer(traceFile.getAbsolutePath(), methodToCarve);

//		for( Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests ){
//			carvedTest.getFirst().visualize();
//			carvedTest.getSecond().visualize();
//		}
//		
//		
//		TestGenerator testGenerator = new TestGenerator(employeeProjectJar);
//		Collection<SootClass> testCases = testGenerator.generateTestCases(carvedTests);
//
//		assertEquals(1, testCases.size());
//
//		System.out.println("CodeGenerationTest.generateTestCasesForEmployeeProject() test cases " + testCases);
//
//		SootClass testCase = testCases.iterator().next();
//		assumeTrue(2 == testCase.getMethods().size());
//		
//		// Assertions
//		File outputDir = temporaryFolder.newFolder();
//		File outputDir = new File(SourceLocator.v().getOutputDir());
		
//		TestCaseFactory.generateTestFiles(outputDir, testCases );
//		
//		// Make assertions here becayse the file and folder will be deleted after the test by the rule !
//		File[] generatedFiles = outputDir.listFiles();
//		// one .class and one .java
//		assertEquals(2, generatedFiles.length);
//		
//		// Print the java files
//		for( File file : generatedFiles ){
//			
//			System.out.println( file.getAbsolutePath() );
//			assertTrue( "Empty output file! " + file.getAbsolutePath() , file.length() > 0 );
//			System.out.println( file.lastModified());
//			
//			if( file.getAbsolutePath().endsWith(".java")){
//				System.out.println("CodeGenerationTest.generateTestCodeForEmployeeProject() Reading from java file");
//				List<String> allLines = Files.readAllLines( file.toPath(), Charset.defaultCharset());
//				for( String line : allLines ){
//					System.out.println( line );
//				}
//			} 
//		}
		
		fail("Not implemented! ");
		
	}

}
