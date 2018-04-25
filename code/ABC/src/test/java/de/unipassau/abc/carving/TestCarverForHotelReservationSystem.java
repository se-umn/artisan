package de.unipassau.abc.carving;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
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
 * for maven to spin off a new VM for each of them.
 * 
 * 
 * TODO For some reason this does not fail while the complete evaluation does...
 * 
 * 
 * @author gambi
 *
 */
public class TestCarverForHotelReservationSystem {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	// For some reason if carver invokes System.exit this see the call but fails
	// to accept it...
	// @Rule
	// public ExpectedSystemExit expectedSystemExit = ExpectedSystemExit.none();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(ManualTest.class)
	public void testCarve() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			// File outputDirectory = temporaryFolderRule.newFolder();

			File outputDirectory = Files.createTempDir();

			String traceFile = "/Users/gambi/action-based-test-carving/code/ABC/scripts/hotelme-tracingOut/trace.txt";

			// String carveBy = "package=org.hotelme";

			// This requires SOME, not all the setup actions !
			// String carveBy = "class=org.hotelme.User";

			// String carveBy = "method=<org.hotelme.Room: java.lang.String
			// getRoomType()>";
			// String carveBy = "method=<org.hotelme.User: int getUserID()>";
			// String carveBy = "invocation=<org.hotelme.User: java.lang.String
			// getFname()>_1874";

			// String carveBy = "invocation=<org.hotelme.HotelView: void
			// exitMessage()>_11744";

			// String carveBy = "method=<org.hotelme.HotelView: void
			// exitMessage()>";
			// String carveBy = "invocation=<org.hotelme.HotelView: void
			// exitMessage()";
			// String carveBy = "invocation=<org.hotelme.HotelView: void
			// exitMessage()>_27707";
			// String carveBy = "invocation=<org.hotelme.HotelView: void
			// exitMessage()>_40738";
			// String carveBy = "invocation=<org.hotelme.HotelView: void
			// exitMessage()>_40738";
			// String carveBy = "method=<org.hotelme.HotelView: int
			// roomConfirmation(java.util.Scanner,int,java.lang.String)>";
			// String carveBy = "method=<org.hotelme.Room: float
			// getTotalPrice()>";
			// String carveBy = "class=org.hotelme.Room";
			String carveBy = "method=<org.hotelme.HotelView: int deleteConfirmation(java.util.Scanner)>";
//			String carveBy = "invocation=<org.hotelme.Room: java.lang.String getRoomType()>_6746";

			// String carveBy = "invocation=<org.hotelme.HotelView: int
			// roomConfirmation(java.util.Scanner,int,java.lang.String)>_3302";

			String[] args = new String[] { //
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", traceFile,
					// String projectJar =
					"--project-jar", "./src/test/resources/HotelReservationSystem.jar",
					"./src/test/resources/HotelReservationSystem-tests.jar",
					// Why providing those jars it does not work anymore ?
					"/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar",
					"/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar",
					//
					// String outputDir =
					"--output-to", outputDirectory.getAbsolutePath(), //
					// To not create tests for those
					"--exclude-by", "package=org.hotelme.systemtests", "package=org.hotelme.utils",
					"class=org.hotelme.Main", //
					"--external", //
					"package=java.nio.file", "class=java.util.Scanner", "package=java.sql", "class=java.io.File",
					// This is for carving
					// "--test-setup-by",
					// "class=org.hotelme.utils.SystemTestUtils", //
					// This is for delta debugging
					"--reset-environment-by", "org.hotelme.utils.SystemTestUtils.dropAndRecreateTheDb()", //
//					"--skip-minimize",//

			};
			//// "class=org.hotelme.utils.ScriptRunner", //
			//

			// expectedSystemExit.expectSystemExitWithStatus(0);
			// expectedSystemExit.expectSystemExit();
			// expectedSystemExit.checkAssertionAfterwards( new Assertion() {
			//
			// @Override
			// public void checkAssertion() throws Exception {
			// }
			// });
			carver.main(args);

			int count = ABCTestUtils.countFiles(outputDirectory, ".java");
			Assert.assertTrue(count > 0);
			ABCTestUtils.printJavaClasses(outputDirectory);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised " + e);
		}
	}
}
