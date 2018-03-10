package de.unipassau.carving;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.unipassau.carving.carvers.Level_0_MethodCarver;
import de.unipassau.data.Pair;
import de.unipassau.data.Triplette;
import de.unipassau.generation.TestCaseFactory;
import de.unipassau.generation.TestGenerator;
import soot.SootClass;

public class TestCarvingExpansions {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	// This method is commonly invoked as last in any system test on the created
	// DummyInstance
	private final String staticMethodToCarve = "<de.unipassau.testsubject.DummyObject: void end()>";

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
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(staticMethodToCarve);

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
		assertEquals( 2, outputDir.listFiles().length);
		
		printJavaClasses( outputDir );

	}

	private final FilenameFilter javaFileNameFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".java");
		}
	};
	
	private void printJavaClasses(File outputDir) throws IOException {
		for( File javaFile : outputDir.listFiles( javaFileNameFilter )){
			System.out.println("File: " + javaFile.getAbsolutePath() );
			for( String line : Files.readAllLines( javaFile.toPath(), Charset.defaultCharset())){
				System.out.println( line );
			}
		}
		
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
