package de.unipassau.abc.carving;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import com.google.common.io.Files;

import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.ManualTest;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

/**
 * System test for carving. Each system test must be declarade in its own file
 * for maven to spin off a new VM for each of them
 * 
 * 
 * @author gambi
 *
 */
public class TestCarverForHotelReservationSystem {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Test
	@Category(ManualTest.class)
	public void testCarve() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			// File outputDirectory = temporaryFolderRule.newFolder();

			File outputDirectory = Files.createTempDir();

			String traceFile = "/Users/gambi/action-based-test-carving/code/ABC/scripts/tracingOut/trace.txt";

			// String carveBy = "package=org.hotelme";
			// String carveBy = "class=org.hotelme.HotelModel";
			// String carveBy = "method=<org.hotelme.HotelController: void
			// <init>(org.hotelme.HotelModel,org.hotelme.HotelView)>";
			String carveBy = "method=<org.hotelme.HotelModel: int checkRoomsAvailable(java.sql.Date,java.sql.Date,java.lang.String)>";
			// <org.hotelme.utils.ScriptRunner: void
			// <init>(java.sql.Connection,boolean,boolean)>";
			// String carveBy = "invocation=<org.hotelme.User: java.lang.String
			// getFname()>_137";
			// String carveBy = "package=org.hotelme";

			String[] args = new String[] { //
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", traceFile,
					// String projectJar =
					"--project-jar", 
					"./src/test/resources/HotelReservationSystem.jar",
					"./src/test/resources/HotelReservationSystem-tests.jar",
					// Why providing those jars it does not work anymore ?
					"/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar",
					"/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar",
					//
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(), //
					// To not create tests for those
					"--exclude-by", "package=org.hotelme.systemtests", "class=org.hotelme.utils.ScriptRunner", //
					"--external", //
					"package=java.nio.file", "java.util.Scanner", "package=java.sql", "package=java.io"};
			//
			carver.main(args);
			//
			int count = ABCTestUtils.countFiles(outputDirectory, ".class");

			// assertEquals(1, count);

			ABCTestUtils.printJavaClasses(outputDirectory);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised " + e);
		}
	}
}
