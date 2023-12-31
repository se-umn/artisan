package de.unipassau.abc.carving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import com.google.common.io.Files;

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
	@Ignore
	@Category(ManualTest.class)
	public void manuallyCarvingEmployee() throws IOException, InterruptedException {

		try {
			Carver carver = new Carver();
			File outputDirectory = Files.createTempDir();

			// Employee Project
			// String jimpleMethodToCarve = "<org.employee.Validation: int
			// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>";
			// //
			// String jimpleMethodToCarve =
			// "<org.employee.DummyObjectToPassAsParameter: void <init>()>";

			String traceFile = "/Users/gambi/action-based-test-carving/code/scripts/employee-tracingOut/trace.txt";
			String carveBy = "invocation=<org.employee.FileRead2: int fileIsRead(java.lang.String,java.lang.String)>_4556";
//			String carveBy = "method=<org.employee.FileRead2: int fileIsRead(java.lang.String,java.lang.String)>";
			/*
			 * This requires a setup of a file by means of Files.write, which is
			 * an external interface
			 */
			System.setProperty("debug", "true");

			String[] args = new String[] { 
					"--assert-via", "USAGE_CLASS", // or XML ?
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", traceFile,
					// String projectJar =
					"--project-jar",
					"/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar",
					"/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(),
					// List the external interfaces here
					"--external", //
					"java.io.Writer","java.io.BufferedWriter","java.io.FileWriter","java.io.File","java.nio.Path","java.util.Scanner","java.nio.file.Files", //
					"java.nio.file.Path", "java.nio.file.Files", //
					"--test-setup-by", "class=org.employee.systemtest.SystemTestUtils",
					// TODO Probably we should have something here which recreates the file?
					// "--reset-environment-by",
					"--exclude-by", "package=org.employee.systemtest", "class=org.employee.Employee", //
					"--skip-minimize" };
			//
			carver.main(args);
			//
			// assertEquals(5, ABCTestUtils.countFiles(outputDirectory,
			// ".java"));

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}

	@Test
	@Category(ManualTest.class)
	public void testCarvingApplicationDemo() throws IOException, InterruptedException {

		try {
			Carver carver = new Carver();
			// File outputDirectory = temporaryFolderRule.newFolder();
			File outputDirectory = Files.createTempDir();

			// SBC Demo Project
			 System.setProperty("directory.home",
			 "/Users/gambi/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo");
			 String traceFile =
			 "/Users/gambi/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/data/abc-trace/trace.txt";
//			 String carveBy = "method=" + jimpleMethodToCarve;
//			 String carveBy = "class=DummyObjectToPassAsParameter";
//			 String carveBy ="invocation=<org.employee.DummyObjectToPassAsParameter: void <init>()>_40";
			 String carveBy= "invocation=<directory.Command: void setRecord(directory.Record)>_82";
//			 String carveBy= "class=directory.Command";

			/*
			 * This requires a setup of a file by means of Files.write, which is
			 * an external interface
			 */
			System.setProperty("debug", "true");

			String[] args = new String[] { "--carve-by", carveBy,
					"--assert-via", "USAGE_INSTANCE", // XML | GETTERS | USAGE_CLASS | USAGE_INSTANCE
					// String traceFile =
					"--trace-file", traceFile,
					// String projectJar =
					"--project-jar", //
					 "/Users/gambi/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/abc-app/directory-v0.jar",
					 "/Users/gambi/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/lib/hsqldb-2.4.0.jar",
					 "/Users/gambi/action-based-test-carving/test-subjects/OriginalEvaluation/ApplicationCarvingDemo/lib/log4j-1.2.9.jar",
					 //
					"--output-to", outputDirectory.getAbsolutePath(),
					// List the external interfaces here
					"--external", "java.io.Writer", "java.io.FileWriter", "java.io.BufferedWriter", "java.io.File",
					"java.nio.file.Path", "java.nio.file.Files", "package=java.sql", "package=java.sql", "package=org.hsqldb",//
					//
					 "--reset-environment-by", "directory.DBTrucate.clearDatabase()",
					 //
					"--exclude-by", "package=org.employee.systemtest", "class=org.employee.Employee", //
					"--skip-minimize" };
			//
			carver.main(args);
			//
			// assertEquals(5, ABCTestUtils.countFiles(outputDirectory,
			// ".java"));

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
