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
 * System test for carving. Each system test must be declarade in its own file
 * for maven to spin off a new VM for each of them
 * 
 * 
 * @author gambi
 *
 */
public class ByReturnCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(SystemTest.class)
	public void testCarvingByReturn() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			// This fails beacuse we carve java.lang.Integer if we use "int" as
			// carveBY
			 String carveBy = "return=int";
			// This instead matches only isDummy() in the trace
//			String carveBy = "return=boolean";
			String[] args = new String[] { "--carveBy", carveBy,
					// String traceFile =
					"--traceFile", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--projectJar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--outputDir", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);

			assertEquals(2, outputDirectory.listFiles().length);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}
}
