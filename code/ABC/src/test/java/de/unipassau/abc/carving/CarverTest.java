package de.unipassau.abc.carving;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.utils.ABCTestUtils;
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

	// @Test
	@Category(SystemTest.class)
	public void testCarvingByMethodForSubsumedCall() throws IOException, InterruptedException {
		/**
		 * This call can be subsumed by a factory call, during carving when the
		 * factory call is taken, the MUT is filtered out. IT WILL BE INVOKED AT
		 * RUNTIME, BUT ONLY INDIRECTLY by the factory call, hence we cannot
		 * consider this as valid Level_0 test.
		 */
		String jimpleMethodToCarve = "<org.employee.DummyObjectToPassAsParameter: void <init>()>"; //

		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			String[] args = new String[] {
					"--carveBy", "method=" + jimpleMethodToCarve,
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
