package de.unipassau.abc.instrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.testsubject.DummySystemTestGetDoubleModifiedWithDelegate;
import de.unipassau.abc.testsubject.DummySystemTestGetModified;
import de.unipassau.abc.testsubject.DummySystemTestGetModifiedWithDelegate;
import de.unipassau.abc.testsubject.DummySystemTestGetSimple;
import de.unipassau.abc.testsubject.DummySystemTestGetSimpleWithDelegate;
import de.unipassau.abc.testsubject.DummySystemTestGetSimpleWithNonRequiredParameter;
import de.unipassau.abc.testsubject.DummySystemTestGetSimpleWithParameter;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class InstrumentDummyProjectTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

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
		
		File outputDir = temporaryFolder.newFolder();
//		File outputDir = Files.createTempDirectory("TEMP").toFile();

		// Note that this includes both SUT and its test. If we do not trace
		// test cases, then we might miss information for carving
		File testsubjectJar = new File("./libs/testsubject-tests.jar");

		InstrumentTracer tracer = new InstrumentTracer();
		tracer.main(new String[] { "--project-jar", testsubjectJar.getAbsolutePath(), //
				"--output-to", outputDir.getAbsolutePath(), //
				"--output-type", "class", "--include", "de.unipassau.abc.testsubject.*" });
		//
		final AtomicInteger count = new AtomicInteger(0);
		Files.walkFileTree(outputDir.toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.toString().endsWith(".class")) {
					count.incrementAndGet();
				}
				return super.visitFile(file, attrs);
			}
		});
		// TODO Maybe a Hamcrest matcher here?
		assertEquals(14, count.get());

		// At this point we have the instrumented classes and we can start
		// system tests and make assertions on the resulting traces.
		// TODO SUpporting jars !
		runSystemTestFromClass(DummySystemTestGetDoubleModifiedWithDelegate.class, outputDir, testsubjectJar, traceJarCP);
		runSystemTestFromClass(DummySystemTestGetModified.class, outputDir, testsubjectJar, traceJarCP);
		runSystemTestFromClass(DummySystemTestGetModifiedWithDelegate.class, outputDir, testsubjectJar, traceJarCP);
		runSystemTestFromClass(DummySystemTestGetSimple.class, outputDir, testsubjectJar, traceJarCP);
		runSystemTestFromClass(DummySystemTestGetSimpleWithDelegate.class, outputDir, testsubjectJar, traceJarCP);
		runSystemTestFromClass(DummySystemTestGetSimpleWithNonRequiredParameter.class, outputDir, testsubjectJar,
				traceJarCP);
		runSystemTestFromClass(DummySystemTestGetSimpleWithParameter.class, outputDir, testsubjectJar, traceJarCP);

	}

	private void runSystemTestFromClass(Class systemTestClass, File outputDir, File testsubjectJar, String traceJarCP)
			throws IOException, URISyntaxException, InterruptedException {
		String systemTestClassName = systemTestClass.getName();

		File traceOutput = temporaryFolder.newFile(systemTestClassName + "trace.txt");

		// Run the tests for collecting the traces using a second JVM
		File systemTestClassClassFile = new File(
				systemTestClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

		String classpath = outputDir + File.pathSeparator + testsubjectJar + File.pathSeparator
				+ systemTestClassClassFile + File.pathSeparator + traceJarCP;

		String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

		ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, systemTestClassName);
		processBuilder.environment().put("trace.output", traceOutput.getAbsolutePath());
		System.out.println("InstrumentEmployeeTest.instrumentAndTraceTestSubjects()" + processBuilder.command());

		// This causes problems
		// processBuilder.inheritIO();

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
