package de.unipassau.abc.evaluation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.github.javaparser.ast.CompilationUnit;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.utils.MethodInvocationSearcher;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.BasicTestGenerator;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.NameTestCaseGlobally;
import de.unipassau.abc.generation.utils.TestCaseNamer;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestCaseOrganizers;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;

public class Main {

	public interface CLI {
		@Option(longName = "apk")
		public File getApk();

		@Option(longName = "trace-files")
		public List<File> getTraceFiles();

		@Option(longName = "output-to", defaultValue = "./carved-tests/")
		public File getOutputDir();

		@Option(longName = "android-jar")
		public File getAndroidJar();

	}

	// NOTE: If a class has inner classes more than one file will be generated by
	// the compiler
	@Deprecated
	private static Collection<File> compileTest(File sourceFolder, File testFile, String theClassPath)
			throws IOException {
		// https://www.logicbig.com/tutorials/core-java-tutorial/java-se-compiler-api/compiler-api-string-source.html
		// https://stackoverflow.com/questions/1563909/how-to-set-classpath-when-i-use-javax-tools-javacompiler-compile-the-source
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		Collection<File> compiledTests = new ArrayList<File>();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjectsFromStrings(Arrays.asList(testFile.getAbsolutePath()));

			List<String> optionList = new ArrayList<String>();

			// Include test-utils on the compilation classpath because we need the Carved
			// annotation
			for (String cpEntry : System.getProperty("java.class.path").split(File.pathSeparator)) {
				if (cpEntry.contains("test-utils")) {
					theClassPath += File.pathSeparator + cpEntry;
				}
			}

			optionList.addAll(Arrays.asList("-cp", theClassPath));
			optionList.addAll(Arrays.asList("-d", sourceFolder.getAbsolutePath()));

			JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null,
					compilationUnits);

			if (!task.call()) {
				diagnostics.getDiagnostics().forEach(System.out::println);
			} else {
				for (JavaFileObject jfo : fileManager.list(StandardLocation.CLASS_OUTPUT, "",
						Collections.singleton(JavaFileObject.Kind.CLASS), true)) {
					System.out.println("Compiled :" + jfo.getName());
					compiledTests.add(new File(jfo.getName()));
				}
			}
		}

		return compiledTests;
	}

	public static void main(String[] args) throws IOException, ABCException {

		CLI cli = CliFactory.parseArguments(CLI.class, args);

		/*
		 * Soot is a singleton and exposes static methods only, so we encapsulate its
		 * configuration inside static method calls
		 */
		ParsingUtils.setupSoot(cli.getAndroidJar(), cli.getApk());

		TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

		for (File traceFile : cli.getTraceFiles()) {

			TraceParser parser = new TraceParserImpl();
			ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
			ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
			ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

			MethodInvocationSearcher mis = new MethodInvocationSearcher();
			Set<MethodInvocation> targetMethodsInvocations = mis.findAllCarvableMethodInvocations(parsedTrace);

			int allCarvableTargets = targetMethodsInvocations.size();

			System.out.println("Carvable targets ");
			targetMethodsInvocations.forEach(System.out::println);

			BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
			Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations,
					parsedTrace);

			int carvedTargets = carvedTests.size();

			System.out.println("Carved targets " + carvedTargets + " / " + allCarvableTargets);

			// Put each test in a separate test case
			TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
			Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

			// Write test cases to files and try to compile them
			JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

			Map<TestClass, File> generatedTests = new HashMap<TestClass, File>();

			File sourceFolder = cli.getOutputDir();

			for (TestClass testCase : testSuite) {
				try {
					CompilationUnit cu = writer.generateJUnitTestCase(testCase);
					File testFileFolder = new File(sourceFolder,
							testCase.getPackageName().replaceAll("\\.", File.separator));
					testFileFolder.mkdirs();
					File testFile = new File(testFileFolder, testCase.getName() + ".java");
					try (PrintStream out = new PrintStream(new FileOutputStream(testFile))) {
						out.print(cu.toString());
					}
					generatedTests.put(testCase, testFile);
				} catch (Exception e) {
					System.err.println("Cannot generate test " + testCase.getName());
				}
			}

			System.out.println("Generated tests " + generatedTests.size() + " / " + carvedTargets);

		}

		// TODO At the moment compiling tests outside gradle has been proved tediuos and
		// hard to achieve...
//		Map<TestCase, Collection<File>> compiledTests = new HashMap<TestCase, Collection<File>>();
//
//		
//		for (Entry<TestCase, File> entry : generatedTests.entrySet()) {
//			TestCase testCase = entry.getKey();
//			File testFile = entry.getValue();
//			try {
//				// Compile the test
//				Collection<File> compiledTest = compileTest(sourceFolder, testFile, cli.getApkClasspath());
//				if (compiledTest.isEmpty()) {
//					System.out.println("Test " + testCase.getName() + " did not compile");
//				} else {
//					compiledTests.put(testCase, compiledTest);
//				}
//			} catch (Exception e) {
//				System.err.println("Cannot compile test " + testCase.getName());
//			}
//		}
//		System.out.println("Compiled test " + compiledTests.size() + "/" + generatedTests.size());

		// Execute the test cases and check if the pass
		// TODO Executing the tests outside gradle is an issue, same as compiling them
		// outside gradle
//		// Build the classpath:
//		List<String> cpEntries = new ArrayList<String>();
//		// Testing deps for the app including the jar of the app itself and the abc
//		// test-utils package
//		cpEntries.addAll(Arrays.asList(cli.getApkClasspath().split(File.pathSeparator)));
//
//		// Carved all the files that we have generated during compilation
//		for (Entry<TestCase, Collection<File>> compiledTest : compiledTests.entrySet()) {
//			cpEntries
//					.addAll(compiledTest.getValue().stream().map(f -> f.getAbsolutePath()).collect(Collectors.toSet()));
//		}
//
//		JUnitTestExecutor testExecutor = new JUnitTestExecutor(cpEntries);
//		String[] jvmFlags = new String[0];
//
//		int failedTests = 0;
//		int passedTests = 0;
//		// TODO Iteratve only over the keys
//		for (Entry<TestCase, Collection<File>> compiledTest : compiledTests.entrySet()) {
//			try {
//				System.out.println("Executing Test " + compiledTest.getKey().getName());
//				Map<String, Object> result = testExecutor.executeTest(compiledTest.getKey().getName(), jvmFlags);
//				if ((int) result.get("exitCode") != 0) {
//					System.out.println("Test Failed");
//					System.out.println(result.get("stdError"));
//					failedTests += 1;
//				} else {
//					System.out.println("Test Passed");
//					System.out.println(result.get("stdOut"));
//					passedTests += 1;
//				}
//
//			} catch (Exception e) {
//				System.err.println("Error in executing test " + compiledTest.getKey());
//				e.printStackTrace();
//			}
//		}
//
//		System.out.println("Passed tests " + passedTests);
//		System.out.println("Failed tests " + failedTests);
	}
}
