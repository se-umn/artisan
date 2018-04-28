package de.unipassau.abc.generation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.SystemUtils;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import soot.SootClass;

// Execute the give test classes, parse the results and returns a data structor for pass/fail tests
// XXXX THIS WORKS UNDER THE ASSUMPTION THAT THERE"S ONLY ONE TEST METHOD FOR EACH TEST CLASS !!
public class TestSuiteExecutor {

	private static final Logger logger = LoggerFactory.getLogger(TestSuiteExecutor.class);

	private List<File> projectJars;

	public TestSuiteExecutor(List<File> projectJars) {
		this.projectJars = new ArrayList<>(projectJars);
	}

	public String buildClassPath(File srcFolder) {
		///
		StringBuilder cpBuilder = new StringBuilder();
		// Include the class we are delta debugging
		cpBuilder.append(srcFolder.getAbsolutePath()).append(File.pathSeparator);
		// Include the project deps
		for (File jarFile : this.projectJars) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}
		// Include JUnit
		cpBuilder.append(ABCUtils.buildJUnit4Classpath()).append(File.pathSeparator);
		// Include System rules for
		cpBuilder.append(ABCUtils.getSystemRulesJar()).append(File.pathSeparator);
		// Include XMLDumper
		cpBuilder.append(ABCUtils.getTraceJar()).append(File.pathSeparator);
		// Include XMLDumper Deps
		for (File jarFile : ABCUtils.getXStreamJars()) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}

		return cpBuilder.toString();
	}

	// XXXX THIS WORKS UNDER THE ASSUMPTION THAT THERE"S ONLY ONE TEST METHOD
	// FOR EACH TEST CLASS !!
	// FOR THE MOMENT WE COMPILE AND RUN EVERYTHING EVERYTIME
	public Collection<Pair<CompilationUnit, Signature>> executeAndReportFailedTests(
			Map<Pair<CompilationUnit, Signature>, Integer> currentIndex) {

		Collection<Pair<CompilationUnit, Signature>> failedTests = new HashSet<>();

		Set<CompilationUnit> _testClasses = new HashSet<>();

		for (Pair<CompilationUnit, Signature> p : currentIndex.keySet()) {
			_testClasses.add(p.getFirst());
		}
		//
		Iterator<CompilationUnit> iterator = _testClasses.iterator();
		// Note that this is correct only for the case of 1 test class 1 test
		// method !!
		while (iterator.hasNext()) {
			CompilationUnit testClass = iterator.next();
			if (!compileJUnitTest(testClass)) {
				logger.warn(" Cannot compile " + testClass.getType(0).getNameAsString());
				// Accumulate the test methods in this call
				failedTests.addAll(getTestMethods(testClass));
				//
				iterator.remove();
			}
		}
		// At this point the testClasses set should contain only compilable
		// tests
		// final Pattern methodName =
		// Pattern.compile("^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+$");

		for (Pair<String, String> failedTest : compileAndRunJUnitTests(_testClasses)) {
			// Find the corresponding Method Declaration
			for (CompilationUnit testClass : _testClasses) {

				if (failedTest.getFirst().equals(testClass.getPackageDeclaration().get().getNameAsString() + "."
						+ testClass.getType(0).getNameAsString())) {
					testClass.accept(new VoidVisitorAdapter() {
						public void visit(MethodDeclaration n, Object arg) {

							if (n.getNameAsString().equals(failedTest.getSecond())) {
								failedTests.add(new Pair<CompilationUnit, Signature>(testClass, n.getSignature()));
							}
							super.visit(n, arg);
						}
					}, null);
				}
			}
		}
		//
		return failedTests;

	}

	private Set<Pair<CompilationUnit, Signature>> getTestMethods(CompilationUnit testClass) {
		final Set<Pair<CompilationUnit, Signature>> testMethods = new HashSet<>();
		testClass.accept(new VoidVisitorAdapter<Void>() {
			@Override
			public void visit(MethodDeclaration n, Void arg) {
				if (n.isAnnotationPresent(org.junit.Test.class)) {
					testMethods.add(new Pair<CompilationUnit, Signature>(testClass, n.getSignature()));
				}
				super.visit(n, arg);
			}
		}, null);

		return testMethods;
	}

	private boolean compileJUnitTest(CompilationUnit junitClass) {
		try {
			File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
			tempOutputDir.deleteOnExit();
			boolean result = compileJUnitTest(junitClass, tempOutputDir);
			// Remove the folder
			// tempOutputDir.delete();

			return result;

		} catch (Throwable e) {
			logger.warn("Cannot compileJUnitTest " + junitClass.getTypes().get(0).getNameAsString(), e);
			return false;
		}

	}

	private boolean compileJUnitTest(CompilationUnit junitClass, File tempOutputDir) {
		String testClassName = junitClass.getType(0).getNameAsString();
		String packageDeclaration = junitClass.getPackageDeclaration().get().getNameAsString();

		try {
			// Create the structure as javac expects
			File packageFile = new File(tempOutputDir, packageDeclaration.replaceAll("\\.", File.separator));
			packageFile.mkdirs();

			// Store the testClassName into a File inside the tempOutputDir
			File classFile = new File(packageFile, testClassName + ".java");

			// Not nice to pass by String ...
			Files.write(classFile.toPath(), junitClass.toString().getBytes());

			// TODO Compilatoin is still one by one ...

			String classpath = buildClassPath(tempOutputDir);

			//
			final List<String> args = new ArrayList<>();
			args.add("-classpath");
			args.add(classpath);

			java.nio.file.Files.walkFileTree(tempOutputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".java")) {
						args.add(file.toFile().getAbsolutePath());
					}
					return super.visitFile(file, attrs);
				}
			});

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int compilationResult = compiler.run(null, null, null, //
					args.toArray(new String[] {}));

			if (compilationResult != 0) {
				logger.debug("Compilation failed");
				return false;
			} else {
				return true;
			}
		} catch (Throwable e) {
			logger.warn("Cannot compile " + testClassName, e);
			return false;
		}
	}

	private final static ExecutorService testExecutorThreadsPool = Executors.newFixedThreadPool(5);

	/**
	 * Execute test cases and return stmt coverage
	 * 
	 * @param testClasses
	 * @return
	 */

	public double runAndGetCoverageJUnitTests(List<String> testClassNames, File sourceDir) throws CarvingException {
		long time = System.currentTimeMillis();
		long prepareExecution = -1;
		long prepareProcess = -1;
		long actualExecution = -1;
		long coverageAnalysis = -1;

		double coverage = 0;

		if (testClassNames.isEmpty()) {
			// No test no coverage
			return coverage;
		}

		try {
			// Now execute all of classes under tempOutputDir
			// Refactor
			List<String> commands = new ArrayList<>();
			String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";
			commands.add(javaPath);

			// Add JaCoCo agent
			File jacocoExec = new File(sourceDir, "jacoco.exec");
			// TODO FIXME
			String jacocoAgent = ABCUtils.getJacocoAget();

			// TODO FIXME
			String excludeList = "org.hotelme.Main,org.hotelme.systemtests.*,org.hotelme.utils.*";

			commands.add("-javaagent:" + jacocoAgent + "=destfile=" + jacocoExec.getAbsolutePath() + ",excludes="
					+ excludeList);

			String classpath = buildClassPath(sourceDir);
			commands.add("-cp");
			commands.add(classpath);

			commands.add("org.junit.runner.JUnitCore");

			// Include test names
			commands.addAll(testClassNames);

			ProcessBuilder processBuilder = new ProcessBuilder(commands);

			logger.trace("Execute for coverage: " + processBuilder.command());

			// Disabling this creates some sort of data race and execution does
			// not complete
			processBuilder.inheritIO();

			prepareProcess = System.currentTimeMillis() - prepareExecution;

			Process process = processBuilder.start();

			// Something breaks at this point ...

			// Tests include TIMEOUT already
			int result = process.waitFor();

			if (result != 0) {
				throw new CarvingException(
						"Test execution cannot fail at this point ! Check " + processBuilder.command());
			}

			actualExecution = System.currentTimeMillis() - prepareProcess;

			// Compute coverage
			ExecFileLoader execFileLoader = new ExecFileLoader();
			execFileLoader.load(jacocoExec);

			CoverageBuilder coverageBuilder = new CoverageBuilder();
			Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

			// The source not the tests ! Do we need this everytime ?!
			for (File projectJar : projectJars) {
				analyzer.analyzeAll(projectJar);
			}

			// TODO NOt sure about the title
			IBundleCoverage bundle = coverageBuilder.getBundle("ABC");
			coverage = bundle.getLineCounter().getCoveredRatio();
			// bundle.getBranchCounter().getCoveredRatio();

			coverageAnalysis = System.currentTimeMillis() - actualExecution;

		} catch (CarvingException e) {
			logger.error("Exception (Re)Thrown " + e.getMessage());
			// Re-throw this one
			throw e;
		} catch (Exception e) {
			logger.error("Exception Thrown " + e.getMessage());
			e.printStackTrace();
		} finally {
			logger.info("Execution of " + testClassNames.size() + " tests took " + (System.currentTimeMillis() - time)
					+ "\n" + " prepareExecution " + prepareExecution + " prepareProcess " + prepareProcess
					+ " actualExecution " + actualExecution + " coverageAnalysis " + coverageAnalysis);
		}

		return coverage;
	}

	// FIXME THIS DOES NOT REALLY NeEd TO COMPILE AND RECOMPILE THE STUFF...
	// OUTPUTTING THE STRINGS OF COMPILATION UNIT BECOMES A BURDER FOR THE CG !
	public double compileRunAndGetCoverageJUnitTests(Collection<CompilationUnit> testClasses)
			throws CarvingException, IOException {
		// https://javabeat.net/the-java-6-0-compiler-api/
		File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
		tempOutputDir.deleteOnExit();
		//
		return compileRunAndGetCoverageJUnitTests(testClasses, tempOutputDir);

	}

	public double compileRunAndGetCoverageJUnitTests(Collection<CompilationUnit> testClasses, File workingDir)
			throws CarvingException {
		long time = System.currentTimeMillis();
		long prepareExecution = -1;
		long prepareProcess = -1;
		long actualExecution = -1;
		long coverageAnalysis = -1;

		double coverage = 0;

		List<String> testClassesAsArg = new ArrayList<>();
		for (CompilationUnit testClass : testClasses) {
			testClassesAsArg.add(testClass.getPackageDeclaration().get().getNameAsString() + "."
					+ testClass.getTypes().get(0).getNameAsString());
		}

		prepareExecution = System.currentTimeMillis() - time;

		if (testClasses.isEmpty()) {
			// No test no coverage
			return coverage;
		}

		try {

			if (!compileJUnitTests(testClasses, workingDir)) {
				throw new CarvingException("Compilation should not fail at this point !!!");
			}

			// Now execute all of classes under tempOutputDir
			// Refactor
			List<String> commands = new ArrayList<>();
			String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";
			commands.add(javaPath);

			// Add JaCoCo agent
			File jacocoExec = new File(workingDir, "jacoco.exec");
			// TODO FIXME
			String jacocoAgent = ABCUtils.getJacocoAget();

			// TODO FIXME
			String excludeList = "org.hotelme.Main,org.hotelme.systemtests.*,org.hotelme.utils.*";

			commands.add("-javaagent:" + jacocoAgent + "=destfile=" + jacocoExec.getAbsolutePath() + ",excludes="
					+ excludeList);

			String classpath = buildClassPath(workingDir);
			commands.add("-cp");
			commands.add(classpath);

			commands.add("org.junit.runner.JUnitCore");

			// Include test names
			commands.addAll(testClassesAsArg);

			ProcessBuilder processBuilder = new ProcessBuilder(commands);

			logger.trace("Execute for coverage: " + processBuilder.command());

			// Disabling this creates some sort of data race and execution does
			// not complete
			processBuilder.inheritIO();

			prepareProcess = System.currentTimeMillis() - prepareExecution;

			Process process = processBuilder.start();

			// Something breaks at this point ...

			// Tests include TIMEOUT already
			int result = process.waitFor();

			if (result != 0) {
				throw new CarvingException(
						"Test execution cannot fail at this point ! Check " + processBuilder.command());
			}

			actualExecution = System.currentTimeMillis() - prepareProcess;

			// Compute coverage
			ExecFileLoader execFileLoader = new ExecFileLoader();
			execFileLoader.load(jacocoExec);

			CoverageBuilder coverageBuilder = new CoverageBuilder();
			Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

			// The source not the tests ! Do we need this everytime ?!
			for (File projectJar : projectJars) {
				analyzer.analyzeAll(projectJar);
			}

			// TODO NOt sure about the title
			IBundleCoverage bundle = coverageBuilder.getBundle("ABC");
			coverage = bundle.getLineCounter().getCoveredRatio();
			// bundle.getBranchCounter().getCoveredRatio();

			coverageAnalysis = System.currentTimeMillis() - actualExecution;

		} catch (CarvingException e) {
			logger.error("Exception (Re)Thrown " + e.getMessage());
			// Re-throw this one
			throw e;
		} catch (Exception e) {
			logger.error("Exception Thrown " + e.getMessage());
			e.printStackTrace();
		} finally {
			logger.info("Execution of " + testClassesAsArg.size() + " tests took " + (System.currentTimeMillis() - time)
					+ "\n" + " prepareExecution " + prepareExecution + " prepareProcess " + prepareProcess
					+ " actualExecution " + actualExecution + " coverageAnalysis " + coverageAnalysis);
		}

		return coverage;

	}

	/**
	 * Execute test cases and return failed tests
	 * 
	 * @param testClasses
	 * @return
	 */
	public Set<Pair<String, String>> compileAndRunJUnitTests(Collection<CompilationUnit> testClasses) {

		long time = System.currentTimeMillis();

		List<String> testClassesAsArg = new ArrayList<>();
		for (CompilationUnit testClass : testClasses) {
			testClassesAsArg.add(testClass.getPackageDeclaration().get().getNameAsString() + "."
					+ testClass.getTypes().get(0).getNameAsString());
		}

		final Set<Pair<String, String>> failedTests = new HashSet<>();

		if (testClasses.isEmpty()) {
			return failedTests;
		}

		try {
			// https://javabeat.net/the-java-6-0-compiler-api/
			File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
			tempOutputDir.deleteOnExit();

			if (!compileJUnitTests(testClasses, tempOutputDir)) {
				throw new RuntimeException("Compilation should not fail at this point !!!");
			}

			// Now execute all of classes under tempOutputDir
			// Refactor
			List<String> commands = new ArrayList<>();
			String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";
			commands.add(javaPath);

			// TODO Here it would be nice to execute a single test, but this
			// requires a custom runner that must be used instead of plain
			// JUnitCore
			// https://stackoverflow.com/questions/9288107/run-single-test-from-a-junit-class-using-command-line
			String classpath = buildClassPath(tempOutputDir);
			commands.add("-cp");
			commands.add(classpath);

			commands.add("org.junit.runner.JUnitCore");

			// Include test names
			commands.addAll(testClassesAsArg);

			ProcessBuilder processBuilder = new ProcessBuilder(commands);

			logger.trace("DeltaDebugger.runJUnitTest() " + processBuilder.command());

			// Parse output ...

			// processBuilder.redirectError();
			// processBuilder.redirectInput();
			processBuilder.redirectOutput();
			Process process = processBuilder.start();

			final Pattern failedTestPattern = Pattern
					.compile("\\d\\d*\\) (.*)" + "\\(([a-zA_Z_][\\.\\w]*(\\[\\])?)?(,[a-zA_Z_][\\.\\w]*(\\[\\])?)*\\)");

			// PUT THIS INTO A THREAD !
			testExecutorThreadsPool.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					try (BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
						String line = null;
						while ((line = output.readLine()) != null) {
							// System.out.println(
							// ">>> " + line );
							Matcher matcher = failedTestPattern.matcher(line);
							if (matcher.matches()) {
								String testMethod = matcher.group(1);
								String testClass = matcher.group(2);
								// logger.debug(">>> Failed Test " + testClass +
								// "."+ testMethod);
								failedTests.add(new Pair<String, String>(testClass, testMethod));
							}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						// e1.printStackTrace();
					} catch (Exception e) {
						// TODO: handle exception
						// e.printStackTrace();
					}
					// System.out.println("End Test Execution");
					return null;
				}
			});

			// TODO Check:
			// https://stackoverflow.com/questions/27038351/getting-list-of-tests-from-junit-command-line
			// Wait for the execution to end.
			// I hope that we do not get fooled by calling System.exit
			process.waitFor();

			// if (!process.waitFor(4000 * testClasses.size(),
			// TimeUnit.MILLISECONDS)) {
			// logger.info("Timeout for test execution");
			// // timeout - kill the process.
			// process.destroyForcibly(); // consider using destroyForcibly
			// // instead
			// // Force Wait 1 sec to clean up
			// Thread.sleep(1000);
			// }
			// This should be into a finally
			// process.destroyForcibly(); // consider using destroyForcibly
			// instead
			// NOT SURE
			// Are we sure that at this point the thread finished to read all
			// the input ?

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			logger.info(
					"Execution of " + testClassesAsArg.size() + " tests took " + (System.currentTimeMillis() - time));
		}

		return failedTests;

	}

	private boolean compileJUnitTests(Collection<CompilationUnit> testClasses, File tempOutputDir) {
		try {

			// TODO Factor this out
			// Create a file to compile for each test
			for (CompilationUnit junitClass : testClasses) {
				String testClassName = junitClass.getType(0).getNameAsString();
				String packageDeclaration = junitClass.getPackageDeclaration().get().getNameAsString();

				// Create the structure as javac expects
				File packageFile = new File(tempOutputDir, packageDeclaration.replaceAll("\\.", File.separator));
				packageFile.mkdirs();

				// Store the testClassName into a File inside the tempOutputDir
				File classFile = new File(packageFile, testClassName + ".java");

				// Not nice to pass by String ...
				Files.write(classFile.toPath(), junitClass.toString().getBytes());
			}

			String classpath = buildClassPath(tempOutputDir);

			// Now compile all of them together
			final List<String> args = new ArrayList<>();
			args.add("-classpath");
			args.add(classpath);

			java.nio.file.Files.walkFileTree(tempOutputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".java")) {
						args.add(file.toFile().getAbsolutePath());
					}
					return super.visitFile(file, attrs);
				}
			});

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int compilationResult = compiler.run(null, null, null, //
					args.toArray(new String[] {}));

			if (compilationResult != 0) {
				logger.debug("Compilation failed");
				return false;
			} else {
				return true;
			}
		} catch (Throwable e) {
			logger.warn("Compilation failed ", e);
			return false;
		}
	}

	public boolean compileAndRunJUnitTest(//
			CompilationUnit junitClass, MethodDeclaration testMethod, //
			List<File> projectJars) throws IOException, URISyntaxException, InterruptedException {

		long time = System.currentTimeMillis();
		try {
			// https://javabeat.net/the-java-6-0-compiler-api/
			File tempOutputDir = Files.createTempDirectory("DeltaDebug").toFile();
			tempOutputDir.deleteOnExit();

			String classpath = buildClassPath(tempOutputDir);
			String testClassName = junitClass.getType(0).getNameAsString();
			String packageDeclaration = junitClass.getPackageDeclaration().get().getNameAsString();

			if (!compileJUnitTest(junitClass, tempOutputDir)) {
				logger.debug("Compilation failed");
				return false;
			}

			String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

			// TODO Here it would be nice to execute a single test, but this
			// requires a custom runner that must be used instead of plain
			// JUnitCore
			// https://stackoverflow.com/questions/9288107/run-single-test-from-a-junit-class-using-command-line
			ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, "org.junit.runner.JUnitCore",
					packageDeclaration + "." + testClassName);

			// if (logger.isTraceEnabled()) {
			logger.trace("DeltaDebugger.runJUnitTest() " + processBuilder.command());
			// This causes problems wher run on command line
			processBuilder.inheritIO();
			// }

			Process process = processBuilder.start();

			// Wait for the execution to end.
			if (!process.waitFor(4000, TimeUnit.MILLISECONDS)) {
				logger.info("Timeout for test execution");
				// timeout - kill the process.
				process.destroyForcibly(); // consider using destroyForcibly
											// instead
				// Force Wait 1 sec to clean up
				Thread.sleep(1000);
			}

			try {
				int result = process.exitValue(); // ;process.waitFor();

				if (result != 0) {
					logger.debug(" Test FAILED " + result);
				}
				return (result == 0);
			} catch (Throwable e) {
				e.printStackTrace();
				// Avoid resource leakeage. This should be IDEMPOTENT
				process.destroyForcibly();
				// TODO: handle exception
			}
			return false;
		} finally {
			time = System.currentTimeMillis() - time;
			logger.info("Verification done in " + time + " ms");
		}

	}

	public static void compileAndRunJUnitSootTests(Collection<SootClass> testClasses) {
		// TODO Auto-generated method stub

	}
}
