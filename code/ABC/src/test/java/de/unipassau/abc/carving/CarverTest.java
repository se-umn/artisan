package de.unipassau.abc.carving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.ManualTest;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

/**
 * System test for carving. Each system test must be declarade in its own file
 * for maven to spin off a new VM for each of them THIS file has nasty manifest
 * dependencies, probably through out Soot or some other variable, because if I
 * run all the tests alone, they pass but the moment I run them together they
 * fail !
 * 
 * @author gambi
 *
 */
public class CarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(ManualTest.class)
	public void testCarvingManually() throws IOException, InterruptedException {

		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			
//			String jimpleMethodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>"; //
//			String jimpleMethodToCarve = "<org.employee.DummyObjectToPassAsParameter: void <init>()>";
			String traceFile = "/Users/gambi/action-based-test-carving/code/ABC/scripts/employee-tracingOut/trace.txt";
//			String carveBy = "method=" + jimpleMethodToCarve;
//			String carveBy = "class=DummyObjectToPassAsParameter";
//			String carveBy = "package=org.employee";
//			String carveBy = "invocation=<org.employee.DummyObjectToPassAsParameter: void <init>()>_40";
			
			/*
			 * This requires a setup of a file by means of Files.write, which is an external interface
			 */
//			String carveBy = "class=org.employee.FileRead2";
			String carveBy = "method=<org.employee.FileRead2: int fileIsRead(java.lang.String,java.lang.String)>";
//			String carveBy = "invocation=<org.employee.Validation: void <init>()>_54";

			String[] args = new String[] {
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", traceFile, 
					// String projectJar =
					"--project-jar", "./src/test/resources/Employee.jar", "./src/test/resources/Employee-tests.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(),
					// List the external interfaces here
					"--external", "java.io.File", "java.nio.file.Path", "java.nio.file.Files",//
					"--test-setup-by", "class=org.employee.systemtest.SystemTestUtils"
//					"abc.StaticField", // System.in, System.out, System.err
//					"abc.Field"
					};
			//
			carver.main(args);
			//
			//assertEquals(5, ABCTestUtils.countFiles(outputDirectory, ".java"));

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}

	// @Test
	@Category(SystemTest.class)
	public void testCarvingByMethodSimple() throws IOException, InterruptedException {
		String jimpleMethodToCarve = "<org.employee.Validation: int numberValidation(java.lang.String)>";

		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { "--carveBy", "method=" + jimpleMethodToCarve,
					// String traceFile =
					"--traceFile", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--projectJar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--outputDir", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);
			//
			assertEquals(2, outputDirectory.listFiles().length);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}

	}

	// @Test
	@Category(SystemTest.class)
	public void testCarvingByClass() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { "--carveBy", "class=org.employee.Validation",
					// String traceFile =
					"--traceFile", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--projectJar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--outputDir", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);
			//
			assertEquals(2, outputDirectory.listFiles().length);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}

	// @Test
	@Category(SystemTest.class)
	public void testCarvingByPackage() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { "--carveBy", "package=org.employee",
					// String traceFile =
					"--traceFile", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--projectJar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--outputDir", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);
			// TODO This can be derived by grepping, sorting, and filtering in
			// the trace.
			// CLASSES IN PACKAGE : cat src/test/resources/Employee-trace.txt |
			// grep "<org.employee." | grep "\[>\]" | tr ";" " " | awk '{print
			// $3}' | sort | uniq | wc -l
			// 2 * CLASSES IN PACKAGE
			assertEquals(12, outputDirectory.listFiles().length);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}

	}
}
