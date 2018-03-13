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
 * System test for carving.
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
	@Category(SystemTest.class)
	public void testCarvingByMethod() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { "--carveBy",
					"method=<org.employee.Validation: int numberValidation(java.lang.String)>",
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

	@Test
	@Category(SystemTest.class)
	public void testCarvingByClass() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { "--carveBy",
					"class=org.employee.Validation",
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
}
