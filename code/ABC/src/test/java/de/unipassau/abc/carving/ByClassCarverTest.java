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
public class ByClassCarverTest {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(SystemTest.class)
	public void testCarvingByClass() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			File outputDirectory = temporaryFolderRule.newFolder();
			// String className = "org.employee.Validation";
			// String className = "org.employee.DummyObjectToPassAsParameter";

			// This one is problematic, since no tests are generated. FIXME The
			// problem is related to subclasses
			/*
			 * Level_0_MethodCarver - <org.employee.EmployeeMetaData: void
			 * <init>(java.io.File)>_544 subsubed by
			 * <org.employee.SoftwareTrainee: void <init>(java.io.File)>_543 via
			 * [<org.employee.SoftwareTrainee: void <init>(java.io.File)>_543]
			 * 
			 * First I am not sure why it tries to carve 544 while it should
			 * stop at 543. in other terms 544 should not be there right?
			 * 
			 */
			String className = "org.employee.SoftwareTrainee";
			String[] args = new String[] { //
					"--carve-by", "class=" + className,
					// String traceFile =
					"--trace-file", "./src/test/resources/Employee-trace.txt",
					// String projectJar =
					"--project-jar", "./src/test/resources/Employee.jar",
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath() };
			//
			carver.main(args);
			//
			int count = ABCTestUtils.countFiles(outputDirectory, ".class");
			assertEquals(1, count);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised");
		}
	}
}
