package de.unipassau.carving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.utils.ABCTestUtils;
import de.unipassau.utils.Slf4jSimpleLoggerRule;
import de.unipassau.utils.SystemTest;

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
					"--carveBy", carveBy,
					//
					"--excludeBy", excludeBy, // Refine the carving by including
												// not all the methods
					// String traceFile =
					"--traceFile", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--projectJar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--outputDir", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);

			assertEquals(2, outputDirectory.listFiles().length);

			// TODO Make assertion on Java classes, for example, can they be
			// compiled ? Can they be executed? Do they produce a green test
			// suite?
			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}
}
