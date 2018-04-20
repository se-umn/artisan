package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

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

	public void performAMinimizationStep(Map<Pair<CompilationUnit, Signature>, Integer> currentIndex) {

		// NOTE THAT CompilationUnit IS changed as we go !!

		// Temporary store the original bodies of the test methods
		// Under the assumption that tests have unique method name !
		Map<String, BlockStmt> originalBodies = new HashMap<String, BlockStmt>();

		List<Pair<CompilationUnit, Signature>> toRemove = new ArrayList<>();
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
				// Go to the next
				s = testMethod.getBody().get().getStatement(newIndex);
				logger.debug("Try to remove " + s + " from " + methodName + " at position " + newIndex);
				boolean mandatory = Carver.isMandatory(s);
				if (mandatory) {
					logger.debug(s + " is mandatory ");
				}
				// Move to next statmt
				newIndex = newIndex - 1;
			} while (Carver.isMandatory(s) && newIndex > 0);

			// At this point if there's nothing more to check, we skip
			if (newIndex == 0) {
				toRemove.add(testMethodData);
				continue;
			}

			BlockStmt originalBody = testMethod.getBody().get().clone();
			originalBodies.put(testMethod.getSignature().asString(), originalBody);

			BlockStmt modifiedBody = testMethod.getBody().get().clone();
			modifiedBody.remove(s);

			testMethod.setBody(modifiedBody);

			// Store the index for the next round
			currentIndex.put(testMethodData, newIndex);
		}

		for (Pair<CompilationUnit, Signature> p : toRemove) {
			System.out.println("TestSuiteMinimizer.performAMinimizationStep() Completed "
					+ p.getFirst().getType(0).getNameAsString() + "." + p.getSecond().asString());
			currentIndex.remove(p);
		}

		// TODO Execute only the
		// This one takes care of compilation and execution errors !
		Collection<Pair<CompilationUnit, Signature>> failedTests = testSuiteExecutor
				.executeAndReportFailedTests(currentIndex);

		// Restore the body for the failedTests
		for (Pair<CompilationUnit, Signature> testMethodData : failedTests) {

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

			logger.info("DeltaDebugger.minimize() Verification Failed, for " + testMethod.getDeclarationAsString());

			BlockStmt originalBody = originalBodies.get(testMethodData.getSecond().asString());
			//
			testMethod.setBody(originalBody);
		}

		// Stats we compute them later
		// logger.info("Removed " + removed + " out of " + total + "
		// instructions from test "
		// + testMethod.getNameAsString() + " " + (((double) removed / (double)
		// total) * 100));

		// }

	}

	private Map<Pair<CompilationUnit, Signature>, Integer> generateCurrentIndex(CompilationUnit testClass) {
		Map<Pair<CompilationUnit, Signature>, Integer> currentIndex = new HashMap<Pair<CompilationUnit, Signature>, Integer>();

		testClass.accept(new VoidVisitorAdapter<Void>() {
			public void visit(MethodDeclaration n, Void arg) {
				if (n.isAnnotationPresent(Test.class)) {
					// Process only test methods, by findind the

					BlockStmt body = n.getBody().get();
					List<Statement> statements = new ArrayList<>(body.getStatements());
					Collections.reverse(statements);

					int total = statements.size();

					// Skip the XML Verifier calls
					int mutIndex = Integer.MAX_VALUE;
					for (int index = total - 1; index > 0; index--) {
						Statement s = body.getStatement(index);
						if (s.toString().contains("XMLVerifier")) {
							mutIndex = index - 1;
							continue;
						}
					}
					if (mutIndex == Integer.MAX_VALUE) {
						// This might happen for static void calls... but then,
						// what do we assert about them !?
						logger.error("Cannot generate current index for " + testClass.getType(0).getNameAsString() + "."
								+ n.getDeclarationAsString());
					} else {
						// mutIndex is the position of the MUT which we need to
						// skip during minimization
						currentIndex.put(new Pair<CompilationUnit, Signature>(testClass, n.getSignature()),
								mutIndex - 1);
					}

				}
			}
		}, null);

		return currentIndex;

	}

	//This assumes testClasses are augmented already !
	public Set<CompilationUnit> coverageBasedMinimization(Set<CompilationUnit> testClasses) throws CarvingException {

		double totalCoverage = Double.MAX_VALUE;
		try {
			// Run tests and collect coverage
			totalCoverage = testSuiteExecutor.compileRunAndGetCoverageJUnitTests(testClasses);
			logger.debug("TestSuiteMinimizer.minimizeTestSuite() Total coverage " + totalCoverage);
		} catch (CarvingException e) {
			throw e;
		} catch (Exception e) {
			throw new CarvingException("Wrapping", e);
		}

		int removed = 0;

		// Let's use the index and an array !
		CompilationUnit[] _testClasses = testClasses.toArray(new CompilationUnit[] {});

		for (int i = 0; i < _testClasses.length; i++) {

			logger.trace("TestSuiteMinimizer.minimizeTestSuite() Try to remove "
					+ _testClasses[i].getType(0).getNameAsString());

			Set<CompilationUnit> testsToRun = new HashSet<>();
			for (int j = 0; j < _testClasses.length; j++) {
				if (_testClasses[j] != null && j != i) { // Skip i
					testsToRun.add(_testClasses[j]);
				}
			}

			// Do the execution
			double coverage = testSuiteExecutor.compileRunAndGetCoverageJUnitTests(testsToRun);

			logger.trace("TestSuiteMinimizer.minimizeTestSuite() Coverage " + coverage);

			if (coverage == totalCoverage) {
				logger.debug("TestSuiteMinimizer.minimizeTestSuite() Coverage unchanged. Remove "
						+ _testClasses[i].getType(0).getNameAsString());
				removed = removed + 1;
				_testClasses[i] = null;
			} else {
				logger.debug("TestSuiteMinimizer.minimizeTestSuite() Coverage changed. Keep "
						+ _testClasses[i].getType(0).getNameAsString());
			}

		}
		// At this point testToMinimize contains the minimial set.
		logger.info("TestSuiteMinimizer.minimizeTestSuite() Removed " + removed + ", remaining " + testClasses.size()
				+ " test classes");

		// Now replace testClasses with the non null elements of _testClasses
		Set<CompilationUnit> minTestClasses = new HashSet<>();

		for (int i = 0; i < _testClasses.length; i++) {
			if (_testClasses[i] != null) {
				// CLONE THIS SO WE AVOID SIDE EFFECTS !
				minTestClasses.add(_testClasses[i].clone());
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
//		Pattern testName = Pattern.compile("Test\\(.*\\)_.*");
		// TODO Make this a proper pattern matching
		Map<String, Set<CompilationUnit>> batches = new HashMap<>();
		for (CompilationUnit testClass : testClasses) {
			// TestHotelView_264 -> HotelView
//			Matcher m = testName.matcher(testClass.getType(0).getNameAsString());
//			String cut = m.group(1);
			String cut = testClass.getType(0).getNameAsString().replace("Test","").split("_")[0];
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
			// Include the reset environment call if needed
			Carver.createAtBeforeResetMethod(resetEnvironmentBy, testClass);

			// Refactor the class (name, constructor, etc)
			Carver.renameClass(testClass, "_minimized");

			// Remove methods that statically are not state-changing
			Carver.removePureMethods(testClass);

			// Extract the TestMethods and set the currentIndex at the right
			// position. Put all since a test class might contain more test
			// methods !
			currentIndex.putAll(generateCurrentIndex(testClass));
		}

		while (!currentIndex.isEmpty()) {
			performAMinimizationStep(currentIndex);
		}

		for (CompilationUnit testClass : testClasses) {
			Carver.removeAtBeforeResetMethod(testClass);

			Carver.removeXMLVerifierCalls(testClass);

			// logger.info("Removed " + removed + " out of " + total + "
			// instructions from test "
			// + testMethod.getNameAsString() + " " + (((double) removed /
			// (double) total) * 100));

		}

	}

}
