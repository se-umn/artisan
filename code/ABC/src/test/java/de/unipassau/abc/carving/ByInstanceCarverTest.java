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

import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

/**
 * System test for carving. Each system test must be declarade in its own file
 * for maven to spin off a new VM for each of them
 * 
 * FIXME #40
 * 
 * 
 * @author gambi
 *
 */
public class ByInstanceCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Ignore // THIS IS BRITTLE... every time we update the trace file this breaks --> FIXME Mark this as Debug category instead 
	@Test
	@Category(SystemTest.class)
	public void testCarvingByInstance() throws IOException, InterruptedException {
		try {

			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String carveBy = "instance=org.employee.Validation@1493776331";
			String[] args = new String[] { //
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--project-jar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);

			int count = ABCTestUtils.countFiles(outputDirectory, ".class");
			assertEquals(1, count);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}
}
