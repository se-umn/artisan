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
 * System test for carving.
 * 
 * @author gambi
 *
 */
public class ByPackageCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(SystemTest.class)
	public void testCarvingByPackage() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] { //
					"--carve-by", "package=org.employee",
					// String traceFile =
					"--trace-file", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--project-jar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(), //
					"--external", //
					"java.io.File", "java.nio.file.Path", "java.nio.file.Files", "org.junit.rules.TemporaryFolder",
					"java.util.Scanner"
			};
			//
			carver.main(args);
			// TODO This can be derived by grepping, sorting, and filtering in the trace.
			// CLASSES IN PACKAGE : cat src/test/resources/Employee-trace.txt | grep "<org.employee." | grep "\[>\]" | tr ";" " " | awk '{print $3}' | sort | uniq | wc -l
			// 2 * CLASSES IN PACKAGE
			// Collect file recursively
			ABCTestUtils.printJavaClasses(outputDirectory);

			
			int count = ABCTestUtils.countFiles(outputDirectory, ".class");
			assertEquals(11, count);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised");
		}

	}
}
