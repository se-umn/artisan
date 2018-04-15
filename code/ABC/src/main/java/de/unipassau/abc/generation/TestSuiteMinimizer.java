package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.CallableDeclaration.Signature;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.unipassau.abc.data.Pair;

public class TestSuiteMinimizer {

	private Set<CompilationUnit> testClasses;
	private String resetEnvironmentBy;
	private TestSuiteExecutor testSuiteExecutor;
	//
	private final static Logger logger = LoggerFactory.getLogger(TestSuiteMinimizer.class);

	public TestSuiteMinimizer(Set<CompilationUnit> allTestClasses, String resetEnvironmentBy,
			TestSuiteExecutor testSuiteExecutor) {

		this.testClasses = new HashSet<>(allTestClasses);
		this.resetEnvironmentBy = resetEnvironmentBy;
		this.testSuiteExecutor = testSuiteExecutor;
	}

	public boolean isDone() {
		return testClasses.isEmpty();
	}

	// maybe pair<Signature>, pair<MethodDeclaration,Interger?>
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
				s = testMethod.getBody().get().getStatement( newIndex );
				logger.debug("Try to remove " + s + " from " + methodName + " at position " + newIndex);
				boolean mandatory = isMandatory(s);
				if( mandatory){
					logger.debug( s + " is mandatory ");
				}
			// Move to next statmt
				newIndex = newIndex - 1;
			} while ( isMandatory(s) );

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

	// create a @Before method which invokes resetEnvironment by unless there's
	// already one
	public static void createAtBeforeResetMethod(String resetEnvironmentBy, CompilationUnit testClass) {
		if (resetEnvironmentBy == null) {
			return;
		}
		//
		TypeDeclaration<?> testType = testClass.getTypes().get(0);
		for (MethodDeclaration md : testType.getMethods()) {
			if (md.isAnnotationPresent(Before.class)) {
				return;
			}
		}
		//
		MethodDeclaration setupMethod = testType.addMethod("setup", Modifier.PUBLIC);
		setupMethod.addAnnotation(Before.class);
		BlockStmt body = new BlockStmt();
		body.addStatement(new NameExpr(resetEnvironmentBy));
		setupMethod.setBody(body);

	}

	public static void renameClass(CompilationUnit testClass, String postFix) {
		// Change the class name
		final String originalTestClassName = testClass.getType(0).getNameAsString();
		String newTestClassName = originalTestClassName.concat(postFix);
		// New name
		testClass.getType(0).setName(newTestClassName);
		//
		testClass.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(ConstructorDeclaration n, Void arg) {
				if (originalTestClassName.equals(n.getNameAsString())) {
					n.setName(newTestClassName);
				}
				return super.visit(n, arg);
			}
		}, null);

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

	public void minimize() {

		Map<Pair<CompilationUnit, Signature>, Integer> currentIndex = new HashMap<>();

		for (CompilationUnit testClass : testClasses) {
			// Include the reset environment call if needed
			createAtBeforeResetMethod(resetEnvironmentBy, testClass);

			// Refactor the class (name, constructor, etc)
			renameClass(testClass, "_minimized");

			// Remove methods that statically are not state-changing
			removePureMethods(testClass);

			// Extract the TestMethods and set the currentIndex at the right
			// position. Put all since a test class might contain more test
			// methods !
			currentIndex.putAll(generateCurrentIndex(testClass));
		}

		while (!currentIndex.isEmpty()) {
			performAMinimizationStep(currentIndex);
		}

		for (CompilationUnit testClass : testClasses) {
			removeAtBeforeResetMethod(testClass);

			removeXMLVerifierCalls(testClass);

			// logger.info("Removed " + removed + " out of " + total + "
			// instructions from test "
			// + testMethod.getNameAsString() + " " + (((double) removed /
			// (double) total) * 100));

		}

	}

	// TODO Hardcoded values for the moment. Make strong assumption. We should
	// have the symbol solver instead, but that's unrealiable !
	// TODO We should check that the instance type is String tho !
	public boolean isPure(MethodCallExpr methodCall) {
		// TODO Assume tMethodCall are not Assign Expt !
		String m = methodCall.getNameAsString();
		return m.equals("length") || m.equals("startsWith") || m.equals("endsWith") || m.equals("lastIndexOf") || false;

	}

	// TODO Assume tMethodCall are not Assign Expt !
	public boolean isMandatory(Statement statement) {
//		return m.equals("close") || false;
		if( statement instanceof ExpressionStmt ){
			ExpressionStmt es = (ExpressionStmt) statement;
			Expression e = es.getExpression();
			// Naive
			String exp = e.toString();
//			System.out.println("TestSuiteMinimizer.isMandatory() Processing " + statement );
			return exp.contains(".close()");
		} else {
			// Skop string initialization !
			statement instanceof 
		}
		return false;

	}

	public void removePureMethods(CompilationUnit testClass) {
		testClass.accept(new ModifierVisitor<Void>() {

			@Override
			public Visitable visit(MethodCallExpr n, Void arg) {
				if (isPure(n)) {
					logger.debug(" Remove pure method call " + n);
					return null;
				}
				return super.visit(n, arg);
			}

		}, null);

	}

	public static void removeXMLVerifierCalls(CompilationUnit testClass) {
		// Remove the calls to verify from each test method !
		for (MethodDeclaration testMethod : testClass.getType(0).getMethods()) {
			if (testMethod.isAnnotationPresent(Test.class)) {
				testMethod.getBody().get().accept(new ModifierVisitor<Void>() {
					public Visitable visit(MethodCallExpr n, Void arg) {

						if (n.getScope().isPresent()) {
							if (n.getScope().get().toString().equals("de.unipassau.abc.tracing.XMLVerifier")) {
								return null;
							}
						}
						return super.visit(n, arg);
					}

				}, null);
			}
		}

	}

	public static void removeAtBeforeResetMethod(CompilationUnit cu) {
		cu.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(MethodDeclaration n, Void arg) {
				if (n.isAnnotationPresent(Before.class)) {
					return null;
				}
				return super.visit(n, arg);
			}
		}, null);

	}
}
