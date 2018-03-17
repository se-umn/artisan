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
 * FIXME #40
 * 
 * @author gambi
 *
 */
public class ByInstanceCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Test
	@Category(SystemTest.class)
	public void testCarvingByInstance() throws IOException, InterruptedException {
		try {

			// XXX Do not use scanner as external library yet, since we do not
			// track primitives and Strings,
			// scanner will always be reported but never USED.
			// Similarly, we should entirely remove calls for which we do not
			// have or cannot provide object instanecs.
			// Last: external libraries that provides inputs are never really
			// used in Level-0 carving...
			// externalInterfaces.add("java.util.Scanner");

			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String carveBy = "instance=org.employee.Validation@459848100";
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

			assertEquals(2, outputDirectory.listFiles().length);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}
}
