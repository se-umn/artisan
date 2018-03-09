package de.unipassau.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import com.google.common.io.Files;

import de.unipassau.testsubject.DummySystemTest;
import de.unipassau.utils.Slf4jSimpleLoggerRule;
import de.unipassau.utils.SystemTest;

public class InstrumentTracerTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private String getJUnitCP() {
		String junitCp = "";
		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
			if (cpEntry.contains("junit-4.12.jar") || cpEntry.contains("hamcrest-core-1.3.jar")) {
				junitCp += cpEntry + File.pathSeparator;
			}
		}
		junitCp = junitCp.substring(0, junitCp.length());
		System.out.println("InstrumentTracerTest.getJUnitCP() " + junitCp);
		return junitCp;
	}

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTrace() throws URISyntaxException, IOException {
		String outputDir = temporaryFolder.newFolder().getAbsolutePath();
		InstrumentTracer tracer = new InstrumentTracer();
		tracer.main(new String[] { "./src/test/resources/Employee.jar", outputDir });
	}

	@Test
	@Category(SystemTest.class)
	public void instrumentAndTraceTestSubjects() throws URISyntaxException, IOException, InterruptedException {
		// File outputDir = temporaryFolder.newFolder();
		// File traceOutput = temporaryFolder.newFile("trace.txt");

		File outputDir = Files.createTempDir();
		File traceOutput = File.createTempFile("trace", ".txt");
		
		System.out.println("InstrumentTracerTest.instrumentAndTraceTestSubjects() " + traceOutput);

		File traceJar = new File("./libs/trace.jar"); // Eclipse testing
		if (!traceJar.exists()) {
			traceJar = new File("../libs/trace.jar"); // Actual usage ...
			if (!traceJar.exists()) {
				throw new RuntimeException("trace.jar file is missing");
			}
		}

		File testsubjectJar = new File("./libs/testsubject-tests.jar");

		
		InstrumentTracer tracer = new InstrumentTracer();
		tracer.main(new String[] { testsubjectJar.getAbsolutePath(), outputDir.getAbsolutePath(), "class" });

		// Assert ?

		String systemTestClass = DummySystemTest.class.getName();

		// Run the tests for collecting the traces using a second JVM
		File systemTestClassClassFile = new File(
				DummySystemTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

		// String jUnitClassPath = getJUnitCP();
		// + File.pathSeparator + jUnitClassPath;
		// JUnitCore.class.getName(),

		String classpath = outputDir + File.pathSeparator + testsubjectJar + File.pathSeparator
				+ systemTestClassClassFile + File.pathSeparator + traceJar.getAbsolutePath();

		String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

		ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, systemTestClass);
		processBuilder.environment().put("trace.output", traceOutput.getAbsolutePath());
		System.out.println("InstrumentTracerTest.instrumentAndTraceTestSubjects()" + processBuilder.command());

		processBuilder.inheritIO();

		Process process = processBuilder.start();

		int result = process.waitFor();

		assertEquals("System tests fail", 0, result);
		// This creates the trace in trace.output
		assertTrue("No trace output", traceOutput.exists());
		assertNotEquals("Empty trace  output", 0, traceOutput.length());
	}

}
