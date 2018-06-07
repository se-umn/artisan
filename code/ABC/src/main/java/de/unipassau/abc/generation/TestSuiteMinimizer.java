package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;

/// org.codedefenders.multiplayer.CoverageGenerator
// Test suite minimizer uses DeltaDebugger and TestSuite reduction via Coverage analyss !
/*
 * Greedy run tests (all, per CUT, per MUT), measure coverage, remove a test
 *reexecuted and re-meausre coverage. If coverage is lower, keep test, if same remove test
 */
public class TestSuiteMinimizer {

	private Set<CompilationUnit> testClasses;
	private String resetEnvironmentBy;
	private TestSuiteExecutor testSuiteExecutor;
	//
	private final static Logger logger = LoggerFactory.getLogger(TestSuiteMinimizer.class);

	public TestSuiteMinimizer(Set<CompilationUnit> allTestClasses, String resetEnvironmentBy,
			TestSuiteExecutor testSuiteExecutor) {

		// this.testClasses = new HashSet<>(allTestClasses);
		this.testClasses = allTestClasses;
		this.resetEnvironmentBy = resetEnvironmentBy;
		this.testSuiteExecutor = testSuiteExecutor;
	}

	public boolean isDone() {
		return testClasses.isEmpty();
	}

	// Return the count of removed methods
	public int performAMinimizationStep(Map<Pair<CompilationUnit, Signature>, Integer> currentIndex) {

		// Temporary store the original bodies of the test methods
		// Under the assumption that tests have unique method name !
		Map<String, BlockStmt> originalBodies = new HashMap<String, BlockStmt>();

		List<Pair<CompilationUnit, Signature>> toRemove = new ArrayList<>();

		Map<String, Statement> statementsUnderAnalysis = new HashMap<>();

		// Modify each test by tentatively removing a statement.
		for (Pair<CompilationUnit, Signature> testMethodData : currentIndex.keySet()) {

			Integer newIndex = currentIndex.get(testMethodData);
			if (newIndex == 0) {
				toRemove.add(testMethodData);
				continue;
			}

			// Copy and store the original body
			String methodName = testMethodData.getSecond().getName();
			Type[] _parameterTypes = testMethodData.getSecond().getParameterTypes().toArray(new Type[] {});
			String[] parameterTypes = new String[_parameterTypes.length];
			for (int i = 0; i < _parameterTypes.length; i++) {
				parameterTypes[i] = _parameterTypes[i].asString();
			}

			// There must be exactly one method with the same parameters and
			// name !
			MethodDeclaration testMethod = testMethodData.getFirst().getType(0)
					.getMethodsBySignature(methodName, parameterTypes).get(0);

			// Find the next statement to remove, skip mandatory calls
			Statement s = null;
			newIndex = currentIndex.get(testMethodData);
			do {
				// Go to the next interesting statement
				s = testMethod.getBody().get().getStatement(newIndex);

				if (Carver.isMandatory(s)) {
					logger.info("-- Skip mandatory call " + s);
					newIndex = newIndex - 1;
					continue;
				}

				if (Carver.isExternalInterface(s)) {
					logger.info("-- Get External Interface call " + s);
					newIndex = newIndex - 1;
					break;
				}
				//
				logger.info("-- Skip non external call " + s); // We need to
																// remove it !
				newIndex = newIndex - 1;
			} while (newIndex > 0);

			logger.info("Try to remove " + s + " from " + methodName + " at position " + (newIndex + 1));

			// At this point if there's nothing more to check, we skip
			if (newIndex == 0) {
				toRemove.add(testMethodData);
				continue;
			}

			BlockStmt originalBody = testMethod.getBody().get().clone();
			originalBodies.put(testMethod.getSignature().asString(), originalBody);

			// Use the real body
			BlockStmt modifiedBody = testMethod.getBody().get();
			boolean removed = modifiedBody.remove(s);

			if (!removed) {
				System.out.println("TestSuiteMinimizer.performAMinimizationStep() ERROR " + s + " not removed !");
			}

			// Stats
			statementsUnderAnalysis.put(testMethod.getSignature().asString(), s);

			// Force the new body
			testMethod.setBody(modifiedBody);

			// Store the index for the next round
			currentIndex.put(testMethodData, newIndex);
		}

		for (Pair<CompilationUnit, Signature> p : toRemove) {
			logger.info(">>> TestSuiteMinimizer.performAMinimizationStep() Completed "
					+ p.getFirst().getType(0).getNameAsString() + "." + p.getSecond().asString());
			currentIndex.remove(p);
		}

		Collection<Pair<CompilationUnit, Signature>> failedTests = testSuiteExecutor
				.executeAndReportFailedTests(currentIndex);

		// System.out.println("FAILED TEST: " + failedTests.size());

		// Restore the body for the failedTests
		for (Pair<CompilationUnit, Signature> testMethodData : currentIndex.keySet()) {

			// System.out
			// .println("TestSuiteMinimizer.performAMinimizationStep() " +
			// testMethodData.getSecond().asString());
			//
			// // Since we changed the test Compilation Unit is changed as well
			// !

			for (Pair<CompilationUnit, Signature> failedTestMethodData : failedTests) {

				if (testMethodData.getSecond().asString().equals(failedTestMethodData.getSecond().asString())) {

					String methodName = testMethodData.getSecond().getName();
					Type[] _parameterTypes = testMethodData.getSecond().getParameterTypes().toArray(new Type[] {});
					String[] parameterTypes = new String[_parameterTypes.length];
					for (int i = 0; i < _parameterTypes.length; i++) {
						parameterTypes[i] = _parameterTypes[i].asString();
					}
					//
					MethodDeclaration testMethod = testMethodData.getFirst().getType(0)
							.getMethodsBySignature(methodName, parameterTypes).get(0);

					logger.info(
							"DeltaDebugger.minimize() Verification Failed, for " + testMethod.getDeclarationAsString());

					BlockStmt originalBody = originalBodies.get(testMethodData.getSecond().asString());
					//
					testMethod.setBody(originalBody);
				}
				// else {
				// logger.info(" - Removed " +
				// statementsUnderAnalysis.get(testMethodData.getSecond().asString())
				// + " from " + testMethodData.getSecond().asString());
				// }
			}
		}

		// Stats how many instruction removed?
		int removed = currentIndex.size() - failedTests.size();
		//
		return removed;

	}

	private Map<Pair<CompilationUnit, Signature>, Integer> generateCurrentIndex(CompilationUnit testClass) {
		Map<Pair<CompilationUnit, Signature>, Integer> currentIndex = new HashMap<Pair<CompilationUnit, Signature>, Integer>();

		// Before we needed to skip some calls but now it's getting ugly, so we
		// start from the bottom and will skip later calls to XMLVerifier
		testClass.accept(new VoidVisitorAdapter<Void>() {
			public void visit(MethodDeclaration n, Void arg) {
				if (n.isAnnotationPresent(Test.class)) {
					// // Process only test methods, by findind the
					//
					// BlockStmt body = n.getBody().get();
					// List<Statement> statements = new
					// ArrayList<>(body.getStatements());
					// Collections.reverse(statements);
					//
					int total = n.getBody().get().getStatements().size() - 1;
					//
					// // Skip the XML Verifier calls
					// int mutIndex = Integer.MAX_VALUE;
					// for (int index = total - 1; index > 0; index--) {
					// Statement s = body.getStatement(index);
					// if (s.toString().contains("XMLVerifier")) {
					// mutIndex = index - 1;
					// continue;
					// }
					// }
					// if (mutIndex == Integer.MAX_VALUE) {
					// // This might happen for static void calls... but then,
					// // what do we assert about them !? Nothing
					// // Pick the last as MUT
					// mutIndex = total - 1;
					// logger.info("Cannot generate current index for " +
					// testClass.getType(0).getNameAsString() + "."
					// + n.getDeclarationAsString() + " use " + mutIndex);
					// }
					// // mutIndex is the position of the MUT which we need to
					// // skip during minimization
					currentIndex.put(new Pair<CompilationUnit, Signature>(testClass, n.getSignature()), total - 1);

				}
			}
		}, null);

		return currentIndex;

	}

	// This assumes testClasses are augmented already !
	// This does not need to recreate and recompile tests over and over !
	public Set<CompilationUnit> coverageBasedMinimization(Set<CompilationUnit> testClasses) throws CarvingException {
		try {
			// https://javabeat.net/the-java-6-0-compiler-api/
			File tempWorkingDir = Files.createTempDirectory("Reduction").toFile();
			tempWorkingDir.deleteOnExit();
			//
			return coverageBasedMinimization(testClasses, tempWorkingDir);
		} catch (Exception e) {
			throw new CarvingException("Error while doing coverageBasedMinimization", e);
		}
	}

	public Set<CompilationUnit> coverageBasedMinimization(Set<CompilationUnit> testClasses, File workingDir)
			throws CarvingException {

		double totalCoverage = Double.MAX_VALUE;
		try {
			if (true)
				throw new RuntimeException("Not implemented !");

			// Compile and run tests. Collect total coverage
			// totalCoverage =
			// testSuiteExecutor.compileRunAndGetCoverageJUnitTests(testClasses,
			// workingDir);
			// logger.debug("TestSuiteMinimizer.minimizeTestSuite() Total
			// coverage " + totalCoverage);
			// } catch (CarvingException e) {
			// throw e;
		} catch (Exception e) {
			throw new CarvingException("Wrapping", e);
		}

		int removed = 0;

		String[] testClassFQNames = new String[testClasses.size()];
		Iterator<CompilationUnit> iterator = testClasses.iterator();

		for (int i = 0; i < testClassFQNames.length; i++) {
			CompilationUnit cu = iterator.next();
			testClassFQNames[i] = cu.getPackageDeclaration().get().getNameAsString() + "."
					+ cu.getType(0).getNameAsString();
		}

		for (int i = 0; i < testClassFQNames.length; i++) {

			logger.trace("TestSuiteMinimizer.minimizeTestSuite() Try to remove " + testClassFQNames[i]);

			List<String> testsToRun = new ArrayList<>();
			for (int j = 0; j < testClassFQNames.length; j++) {
				if (testClassFQNames[j] != null && j != i) { // Skip i
					testsToRun.add(testClassFQNames[j]);
				}
			}

			// Do the execution
			double coverage = testSuiteExecutor.runAndGetCoverageJUnitTests(testsToRun, workingDir);

			logger.info("TestSuiteMinimizer.minimizeTestSuite() Coverage " + coverage);

			if (coverage == totalCoverage) {
				logger.info("TestSuiteMinimizer.minimizeTestSuite() Coverage unchanged. Remove " + testClassFQNames[i]);
				removed = removed + 1;
				testClassFQNames[i] = null;
			} else {
				logger.info("TestSuiteMinimizer.minimizeTestSuite() Coverage changed. Keep " + testClassFQNames[i]);
			}

		}
		// At this point testToMinimize contains the minimial set.
		logger.info("TestSuiteMinimizer.minimizeTestSuite() Removed " + removed + " out of " + testClasses.size()
				+ " test classes");

		// Now replace testClasses with the non null elements of _testClasses
		Set<CompilationUnit> minTestClasses = new HashSet<>();

		for (int i = 0; i < testClassFQNames.length; i++) {
			// logger.info("TestSuiteMinimizer.coverageBasedMinimization() " +
			// testClassFQNames[i] );
			if (testClassFQNames[i] == null) {
				continue;
			} else {
				// logger.info("TestSuiteMinimizer.coverageBasedMinimization()
				// Processing " + testClassFQNames[i]);

				for (CompilationUnit cu : testClasses) {

					String className = cu.getPackageDeclaration().get().getNameAsString() + "."
							+ cu.getType(0).getNameAsString();

					if (className.equals(testClassFQNames[i])) {
						minTestClasses.add(cu.clone());
					}
				}
			}
		}

		logger.info("Min test suite size " + minTestClasses.size());
		return minTestClasses;
	}

	/**
	 * Greedly remove tests which do not increase coverage
	 * 
	 * @throws CarvingException
	 */
	public Set<CompilationUnit> minimizeTestSuite() throws CarvingException {

		Set<CompilationUnit> minimizedTestSuite = new HashSet<>();

		// Process batches of classes and then clean up resources
		// Javaparser heavily rely on strings which blows up GC
		// Pattern testName = Pattern.compile("Test\\(.*\\)_.*");
		// TODO Make this a proper pattern matching
		Map<String, Set<CompilationUnit>> batches = new HashMap<>();
		for (CompilationUnit testClass : testClasses) {
			// TestHotelView_264 -> HotelView
			// Matcher m =
			// testName.matcher(testClass.getType(0).getNameAsString());
			// String cut = m.group(1);
			String cut = testClass.getType(0).getNameAsString().replace("Test", "").split("_")[0];
			if (!batches.containsKey(cut)) {
				batches.put(cut, new HashSet<>());
			}
			batches.get(cut).add(testClass);
		}

		for (Entry<String, Set<CompilationUnit>> batch : batches.entrySet()) {
			logger.info("Processing " + batch.getKey() + " with " + batch.getValue().size() + " test cases");
			minimizedTestSuite.addAll(coverageBasedMinimization(batch.getValue()));
		}

		return minimizedTestSuite;

	}

	public void minimizeTestMethods() {

		Map<Pair<CompilationUnit, Signature>, Integer> currentIndex = new HashMap<>();

		for (CompilationUnit testClass : testClasses) {
			// Remove methods that statically are not state-changing
			Carver.removePureMethods(testClass);
			// Extract the TestMethods and set the currentIndex at the right
			// position. Put all since a test class might contain more test
			// methods !
			currentIndex.putAll(generateCurrentIndex(testClass));
		}
		// Current Index at this point points on MUT
		int totalInstructionsRemoved = 0;
		while (!currentIndex.isEmpty()) {
			int removed = performAMinimizationStep(currentIndex);
			totalInstructionsRemoved = totalInstructionsRemoved + removed;
			logger.info("Removed " + removed + " stmt. Cumulative " + totalInstructionsRemoved);
		}

	}

	private static Set<CompilationUnit> getCompilationUnitFromSourceDirectory(File outputDir) throws IOException {
		Set<CompilationUnit> compilationUnits = new HashSet<>();
		Files.walkFileTree(outputDir.toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.toString().endsWith(".java")) {
					compilationUnits.add(JavaParser.parse(file));
				}
				return super.visitFile(file, attrs);
			}
		});

		return compilationUnits;

	}

	public interface CommandLineInterface {

		@Option(longName = "tests")
		List<File> getTestFiles();

		@Option(longName = "output-to")
		File getOutputFolder();

		@Option(longName = "project-jar")
		List<File> getProjectJar();

		@Option(longName = "reset-environment-by", defaultToNull = true)
		String getResetEnvironmentBy();

		@Option(longName = "additional-properties", defaultToNull = true)
		List<String> getAdditionalProperties();

	}

	public static void main(String[] args) throws CarvingException, IOException {

		String resetEnvironmentBy = null;
		Set<CompilationUnit> testSuite = new HashSet<>();

		List<File> projectJars = new ArrayList<>();
		List<String> additionalProperties = new ArrayList<>();
		File outputDir = null;
		// Parse input
		try {
			CommandLineInterface cli = CliFactory.parseArguments(CommandLineInterface.class, args);
			for (File f : cli.getTestFiles()) {
				testSuite.add(JavaParser.parse(f));
			}
			resetEnvironmentBy = cli.getResetEnvironmentBy();
			projectJars.addAll(cli.getProjectJar());
			if (cli.getAdditionalProperties() != null) {
				additionalProperties.addAll(cli.getAdditionalProperties());
			}
			outputDir = cli.getOutputFolder();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		//
		TestSuiteMinimizer minimizer = new TestSuiteMinimizer(testSuite, resetEnvironmentBy,
				new TestSuiteExecutor(projectJars, additionalProperties));

		logger.info("Coverage Based Minimization Start");

		long time = System.currentTimeMillis();
		// This will store the files and compiled files to outputDir
		File tempWorkingDir = Files.createTempDirectory("Minimizer").toFile();
		tempWorkingDir.deleteOnExit();

		Set<CompilationUnit> reducedTestSuite = minimizer.coverageBasedMinimization(testSuite, tempWorkingDir);

		Carver.storeToFile(reducedTestSuite, outputDir);

		logger.info("Coverage Based Minimization End. It took " + (System.currentTimeMillis() - time) + "msec");

	}
}
