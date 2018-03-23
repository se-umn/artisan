package de.unipassau.abc.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.apache.commons.lang3.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.testsubject2.Main;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class InstrumentClassInDummyProjectTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private static String traceJarCP;

	@BeforeClass
	public static void setupTraceJar() {
		// Use this if you want to inspect the traces afterwards
		// File outputDir = com.google.common.io.Files.createTempDir();
		// File traceOutput = File.createTempFile("trace", ".txt");

		File traceJarFile = new File("./libs/trace.jar"); // Eclipse testing
		if (!traceJarFile.exists()) {
			traceJarFile = new File("../libs/trace.jar"); // Actual usage ...
			if (!traceJarFile.exists()) {
				throw new RuntimeException("trace.jar file is missing");
			}
		}

		traceJarCP = traceJarFile.getAbsolutePath() + File.pathSeparator + //
				new File("./src/test/resources/xmlpull-1.1.3.1.jar").getAbsolutePath() + File.pathSeparator + //
				new File("./src/test/resources/xstream-1.4.10.jar").getAbsolutePath();

	}

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTraceTestSubjects() throws URISyntaxException, IOException, InterruptedException {

		// File outputDir = temporaryFolder.newFolder();
		File outputDir = Files.createTempDirectory("TEMP").toFile();

		// Note that this includes both SUT and its test. If we do not trace
		// test cases, then we might miss information for carving
		File testsubjectJar = new File("./libs/testsubject-tests.jar");

		InstrumentTracer tracer = new InstrumentTracer();
		tracer.main(new String[] { "--project-jar", testsubjectJar.getAbsolutePath(), //
				"--output-to", outputDir.getAbsolutePath(), //
				"--output-type", "class", "--include",
				 "de.unipassau.abc.testsubject2.*"
//				"de.unipassau.abc.testsubject3.*" 
				//
				});
		//
		// TODO Maybe a Hamcrest matcher here?
		// int count = ABCTestUtils.countFiles(outputDir, ".class");
		// There's two files now
		// assertEquals(4, count);
		// ABCTestUtils.printFiles(outputDir, ".jimple");
		 runSystemTestFromClass(Main.class, outputDir, testsubjectJar, traceJarCP);
//		runSystemTestFromClass(StringHandlingClass.class, outputDir, testsubjectJar, traceJarCP);
	}

	private void runSystemTestFromClass(Class systemTestClass, File outputDir, File testsubjectJar, String traceJarCP)
			throws IOException, URISyntaxException, InterruptedException {
		String systemTestClassName = systemTestClass.getName();

		File tracingOutput = temporaryFolder.newFolder();

		// Run the tests for collecting the traces using a second JVM
		File systemTestClassClassFile = new File(
				systemTestClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

		String classpath = outputDir + File.pathSeparator + testsubjectJar + File.pathSeparator
				+ systemTestClassClassFile + File.pathSeparator + traceJarCP;

		String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

		ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, systemTestClassName);
		processBuilder.environment().put("trace.output", tracingOutput.getAbsolutePath());
		System.out.println("InstrumentEmployeeTest.instrumentAndTraceTestSubjects()" + processBuilder.command());

		// This causes problems
		processBuilder.inheritIO();

		Process process = processBuilder.start();

		int result = process.waitFor();

		assertEquals("System tests fail", 0, result);
		// This creates the trace in trace.output
		File trace = new File(tracingOutput, "trace.txt");
		assertTrue("No trace output", trace.exists());
		assertNotEquals("Empty trace  output", 0, trace.length());

		// Printout the file since temporary folder will delete it !!!
		System.out.println("=====================================");
		System.out.println("Trace for " + systemTestClassName);
		System.out.println("=====================================");
		for (String line : java.nio.file.Files.readAllLines(trace.toPath(), Charset.defaultCharset())) {
			System.out.println(line);
		}
		System.out.println("=====================================");

	}

}
