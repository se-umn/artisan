package de.unipassau.abc.instrumentation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;
import soot.G;

public class InstrumentHotelMeTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private static File traceJar;

	@BeforeClass
	public static void setupTraceJar() {
		// Use this if you want to inspect the traces afterwards
		// File outputDir = com.google.common.io.Files.createTempDir();
		// File traceOutput = File.createTempFile("trace", ".txt");

		traceJar = new File("./libs/trace.jar"); // Eclipse testing
		if (!traceJar.exists()) {
			traceJar = new File("../libs/trace.jar"); // Actual usage ...
			if (!traceJar.exists()) {
				throw new RuntimeException("trace.jar file is missing");
			}
		}
	}

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTrace() throws URISyntaxException, IOException {
		File outputDir = temporaryFolder.newFolder();
		InstrumentTracer tracer = new InstrumentTracer();

		List<String> args = new ArrayList<>( Arrays.asList( new String[] { //
				"--project-jar", "./src/test/resources/HotelReservationSystem.jar",
				"./src/test/resources/HotelReservationSystem-tests.jar",
				// Why providing those jars it does not work anymore ?
				// "/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar",
				// "/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar",
				//
				// DEBUG
				"--exclude", //
				"org.hotelme.HotelController", //
				 "org.hotelme.HotelModel", // -> This problems
				"org.hotelme.HotelView", //
				"org.hotelme.Main", //
				"org.hotelme.Room", //
				"org.hotelme.User", //
//				"org.hotelme.utils", //
				//
				"org.hotelme.systemtests.AbstractHotelSystemTest", //
				"org.hotelme.systemtests.SystemTest", //
				"org.hotelme.systemtests.TestHotel", //
				"org.hotelme.systemtests.TestHotelAlreadyRegistered", //
				"org.hotelme.systemtests.TestHotelExit", //
				"org.hotelme.systemtests.TestHotelReserveRoom", //
				"org.hotelme.systemtests.TestHotelSignUp", //
				"org.hotelme.systemtests.TestHotelSignUpWithTruncateTable", //
				"--output-to", outputDir.getAbsolutePath()})); //
				
		List<String> jimpleArgs = new ArrayList<>( args );
		jimpleArgs.add("--output-type");
		jimpleArgs.add("jimple");
		// Generate .jimple
		tracer.main( jimpleArgs.toArray( new String[]{}) );
		
		G.reset();
		
		// TODO Separate methos call; Visualize the JIMPLE Files
		for (File jimpleFile : outputDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jimple");
			}
		})) {
			System.out.println("==================================");
			System.out.println(" " + jimpleFile.getAbsolutePath());
			System.out.println("==================================");
			for (String line : Files.readAllLines(jimpleFile.toPath(), Charset.defaultCharset())) {
				System.out.println(line);
			}
			System.out.println("==================================");
		}
		// Generate .class
				List<String> classArgs = new ArrayList<>( args );
				classArgs.add("--output-type");
				classArgs.add("class");
				tracer.main( classArgs.toArray( new String[]{}) );
	}

}
