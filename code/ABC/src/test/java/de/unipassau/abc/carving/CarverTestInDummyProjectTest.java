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
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class CarverTestInDummyProjectTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Test
	@Category(SystemTest.class)
	public void testCarvingManually() throws IOException, InterruptedException {

		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			
			String traceFile = "./src/test/resources/ArrayHandlingClass-trace.txt";
//			String carveBy = "method=" + "<de.unipassau.abc.testsubject2.ArrayHandlingClass: void callMeMaybe(java.lang.String[])>";
			String carveBy = "package=" + "de.unipassau.abc.testsubject2.ArrayHandlingClass";
			String[] args = new String[] {
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", traceFile, 
					// String projectJar =
					"--project-jar", "./libs/testsubject-tests.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(),
					// List the external interfaces here
					"--external", "org.junit.rules.TemporaryFolder", "java.nio.file.Files"};
			//
			carver.main(args);
			//
			assertEquals(1, ABCTestUtils.countFiles(outputDirectory, ".java"));

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
