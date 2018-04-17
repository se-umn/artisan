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

public class TestCarverForJPass {

	@Rule
	public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

	@Test
	@Category(ManualTest.class)
	public void testCarve() throws IOException, InterruptedException {
		try {
			Carver carver = new Carver();
			// File outputDirectory = temporaryFolderRule.newFolder();

			File outputDirectory = Files.createTempDir();

			String traceFile = "/Users/gambi/action-based-test-carving/code/ABC/scripts/jpass-tracingOut/trace.txt";

			// This produces some tests
//			String carveBy = "package=jpass.data";
			String carveBy = "package=jpass";
			
//			String carveBy = "class=jpass.data.DataModel";

			String[] args = new String[] { //
					"--carve-by", carveBy,
					// String traceFile =
					"--trace-file", traceFile,
					// String projectJar =
					"--project-jar", 
					"/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/GUI/jpass/target/jpass-0.1.17-SNAPSHOT.jar",
//					"./src/test/resources/jpass-0.1.17-SNAPSHOT.jar",
					
//					"/Users/gambi/.m2/repository/com/fasterxml/jackson/dataformat/jackson-dataformat-xml/2.9.3/jackson-dataformat-xml-2.9.3.jar"
//					"/Users/gambi/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.9.3/jackson-core-2.9.3.jar"
//					"/Users/gambi/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar"
//					"/Users/gambi/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.9.3/jackson-databind-2.9.3.jar"
//					"/Users/gambi/.m2/repository/com/fasterxml/jackson/module/jackson-module-jaxb-annotations/2.9.3/jackson-module-jaxb-annotations-2.9.3.jar"
//					"/Users/gambi/.m2/repository/org/codehaus/woodstox/stax2-api/3.1.4/stax2-api-3.1.4.jar"
//					"/Users/gambi/.m2/repository/com/fasterxml/woodstox/woodstox-core/5.0.3/woodstox-core-5.0.3.jar"
//					"/Users/gambi/.m2/repository/junit/junit/4.11/junit-4.11.jar"
//					"/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
					// Why providing those jars it does not work anymore ?
					"--output-to", outputDirectory.getAbsolutePath(), //
					// To not create tests for those
					"--exclude-by", 
					"class=jpass.JPass", //
					"package=jpass.ui", // Do not carve UI Classes 
					"package=jpass.xml", // Do not carve XML Classes
					"--external", //
					// The problem with this approach is that in whatever remote part of the app, I can read a file, and this brings IN all sort of crap !
//					"package=javax.swing",//
					"package=java.nio.file", "class=java.util.Scanner", "package=java.sql", "class=java.io.File", //
					"package=com.fasterxm", //
					"--skip-minimize"
			};
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
