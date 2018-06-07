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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.utils.ABCTestUtils;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import de.unipassau.abc.utils.SystemTest;

public class InstrumentEmployeeTestAndRunSystemTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private static List<File> traceCP;

	@BeforeClass
	public static void setupTraceJar() {
		traceCP = new ArrayList<>();
		traceCP.add( new File(ABCUtils.getTraceJar()));
		traceCP.addAll( ABCUtils.getXStreamJars() );
		traceCP.add( new File(ABCUtils.getSystemRulesJar()));

	}

	// @Ignore
	@Test
	@Category(SystemTest.class)
	public void instrumentAndTrace() throws Exception {

		// File outputDir = temporaryFolder.newFolder();
		File outputDir = Files.createTempDirectory("TEMP").toFile();

		InstrumentTracer tracer = new InstrumentTracer();

		File testsubjectJar = new File("./src/test/resources/Employee.jar");
		// Note that -tests is not instrumented so we lose those informations !
		File testsubjectTestsJar = new File("./src/test/resources/Employee-tests.jar");
		
		
		tracer.main(new String[] { "--project-jar", testsubjectJar.getAbsolutePath(), //
				"--output-to", outputDir.getAbsolutePath(), //
				"--output-type", "class" });

		// TODO Maybe a Hamcrest matcher here?
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
		assertEquals(11, count.get());

		// System tests for this class needs supporting jars implementing
		// mocking of input
		List<File> jarFiles = new ArrayList<File>();
		jarFiles.add(testsubjectJar);
		jarFiles.add(testsubjectTestsJar);
		jarFiles.addAll(traceCP);

		String systemTest = "org.employee.MainTest";

		File trace = runSystemTestAndGetTraceFile(systemTest, outputDir, jarFiles);

		// Now do the carving

		File carvingOutputDirectory = Files.createTempDirectory("CARVING").toFile();
		// Problem with FileWriter which gets the wrong (false, instead of true)
		// input
		String carveBy = "method=<org.employee.FileRead2: int fileIsRead(java.lang.String,java.lang.String)>";

		String[] args = new String[] { "--carve-by", carveBy,
				// String traceFile =
				"--trace-file", trace.getAbsolutePath(),
				// String projectJar =
				"--project-jar", "./src/test/resources/Employee.jar", "./src/test/resources/Employee-tests.jar",
				// String outputDir =
				"--output-to", carvingOutputDirectory.getAbsolutePath(),
				// List the external interfaces here
				"--external", "java.io.Writer", "java.io.FileWriter", "java.io.BufferedWriter", "java.io.File",
				"java.nio.file.Path", "java.nio.file.Files", //
				"--test-setup-by", "class=org.employee.systemtest.SystemTestUtils",
				// "abc.StaticField", // System.in, System.out, System.err
				// "abc.Field"
				"--exclude-by", "package=org.employee.systemtest", "class=org.employee.Employee", //
				"--skip-minimize" };
		//
		Carver.main(args);
		//
		// assertEquals(5, ABCTestUtils.countFiles(outputDirectory, ".java"));

		ABCTestUtils.printJavaClasses(carvingOutputDirectory);

	}

	// Return trace file
	private File runSystemTestAndGetTraceFile(String systemTestClassName, File instrumentedClassesDir,
			List<File> jarFiles) throws IOException, URISyntaxException, InterruptedException {

		File tracingOutput = Files.createTempDirectory("CARVING").toFile();

		///
		StringBuilder cpBuilder = new StringBuilder();
		for (File jarFile : jarFiles) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}
		cpBuilder.append(ABCTestUtils.buildJUnit4Classpath());
		///

		String classpath = instrumentedClassesDir + File.pathSeparator + cpBuilder.toString();

		String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

		ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, 
				"-Dtrace.output="+tracingOutput.getAbsolutePath(),
				 "-Ddump.output="+tracingOutput.getAbsolutePath(),
				 "-Ddump.by=package=org.employee",
				//
				"org.junit.runner.JUnitCore",
				systemTestClassName);

		System.out.println("InstrumentEmployeeTest.instrumentAndTraceTestSubjects()" + processBuilder.command());

		// This causes problems wher run on command line
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

		return trace;

	}

}
