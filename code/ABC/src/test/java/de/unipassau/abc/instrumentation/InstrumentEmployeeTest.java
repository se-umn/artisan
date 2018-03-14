package de.unipassau.abc.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.instrumentation.InstrumentTracer;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class InstrumentEmployeeTest {

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

		tracer.main(new String[] { "--project-jar", "./src/test/resources/Employee.jar", //
				"--output-to", outputDir.getAbsolutePath(), //
				"--output-type", "jimple" });

		// TODO Maybe a Hamcrest matcher here?
		assertEquals(13, outputDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jimple");
			}
		}).length);

	}

	private void runSystemTestFromClass(Class systemTestClass, File outputDir, File testsubjectJar, File traceJar)
			throws IOException, URISyntaxException, InterruptedException {
		String systemTestClassName = systemTestClass.getName();

		File traceOutput = temporaryFolder.newFile(systemTestClassName + "trace.txt");

		// Run the tests for collecting the traces using a second JVM
		File systemTestClassClassFile = new File(
				systemTestClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

		String classpath = outputDir + File.pathSeparator + testsubjectJar + File.pathSeparator
				+ systemTestClassClassFile + File.pathSeparator + traceJar.getAbsolutePath();

		String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

		ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, systemTestClassName);
		processBuilder.environment().put("trace.output", traceOutput.getAbsolutePath());
		System.out.println("InstrumentEmployeeTest.instrumentAndTraceTestSubjects()" + processBuilder.command());

		processBuilder.inheritIO();

		Process process = processBuilder.start();

		int result = process.waitFor();

		assertEquals("System tests fail", 0, result);
		// This creates the trace in trace.output
		assertTrue("No trace output", traceOutput.exists());
		assertNotEquals("Empty trace  output", 0, traceOutput.length());

		// Printout the file since temporary folder will delete it !!!
		System.out.println("=====================================");
		System.out.println("Trace for " + systemTestClassName);
		System.out.println("=====================================");
		for (String line : java.nio.file.Files.readAllLines(traceOutput.toPath(), Charset.defaultCharset())) {
			System.out.println(line);
		}
		System.out.println("=====================================");

	}

}
