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

import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

/**
 * System test for carving. Each system test must be declarade in its own file
 * for maven to spin off a new VM for each of them
 * 
 * 
 * @author gambi
 *
 */
public class ByReturnWithExclusionCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(SystemTest.class)
	public void testCarvingByReturnWithExclusion() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String carveBy = "return=int";
			String excludeBy = "method=<org.employee.Validation: int numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)";
			String[] args = new String[] { //
					"--carve-by", carveBy,
					//
					"--exclude-by", excludeBy, // Refine the carving by including
												// not all the methods
					// String traceFile =
					"--trace-file", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--project-jar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath() , //
					"--external", //
					"java.io.File", "java.nio.file.Path", "java.nio.file.Files", "org.junit.rules.TemporaryFolder",
					"java.util.Scanner" };
			//
			carver.main(args);

			int count = ABCTestUtils.countFiles(outputDirectory, ".class");
			assertEquals(3, count);

			// TODO Make assertion on Java classes, for example, can they be
			// compiled ? Can they be executed? Do they produce a green test
			// suite?
			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}
}
