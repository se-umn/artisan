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

/**
 * System test for carving.
 * 
 * @author gambi
 *
 */
public class ByMethodSimpleCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Test
	@Category(SystemTest.class)
	public void testCarvingByMethodSimple() throws IOException, InterruptedException {
		// <org.employee.Validation: int
		// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
		// String jimpleMethodToCarve = "<org.employee.Validation: int
		// numberValidation(java.lang.String)>";
		String jimpleMethodToCarve = "<org.employee.EmployeeMetaData: void addMeta()>";

		String traceFile = "./src/test/resources/Employee-trace.txt";
		// TODO Mark this with a DebugTest otherwise:
		// String traceFile =
		// "/Users/gambi/action-based-test-carving/code/ABC/scripts/tracingOut/trace.txt";
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { //
					"--carve-by", "method=" + jimpleMethodToCarve,
					// String traceFile =
					"--trace-file", traceFile,
					// String projectJar =
					"--project-jar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(),
					// Define External interfaces
					"--external", "java.io.File", "java.nio.Path", "java.util.Scanner", "java.nio.file.Files",
					"org.junit.contrib.java.lang.system.ExpectedSystemExit" };
			//
			carver.main(args);
			//
			int count = ABCTestUtils.countFiles(outputDirectory, ".class");
			assertEquals(1, count);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}

	}

}