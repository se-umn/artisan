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

public class InstrumentJPass {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private static File traceJar;

	// TODO REQUIRES TO REBUILD TRACE !!
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

		// The jpass jar is an UBER jar
		List<String> args = new ArrayList<>(Arrays.asList(new String[] { //
				"--project-jar", "./src/test/resources/jpass-0.1.17-SNAPSHOT.jar", //
//				"--include", //
//				"jpass.data.*",
				// DEBUG
				"--exclude", //
//				"jpass.*", //
				"com.fasterxml.*", //
				"org.codehaus.*", //
				"com.ctc.*", //
				"--output-to", outputDir.getAbsolutePath() })); //

		List<String> jimpleArgs = new ArrayList<>(args);
		jimpleArgs.add("--output-type");
		jimpleArgs.add("jimple");
		// Generate .jimple
		tracer.main(jimpleArgs.toArray(new String[] {}));

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
		List<String> classArgs = new ArrayList<>(args);
		classArgs.add("--output-type");
		classArgs.add("class");
		tracer.main(classArgs.toArray(new String[] {}));
	}

}
