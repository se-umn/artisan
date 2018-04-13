package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;

/**
 * TODO Do we need to remove elements in the inputs if we remove calls to scanner.next? in theory we shall
 * @author gambi
 *
 */
public class DeltaDebugger {

	private final static Logger logger = LoggerFactory.getLogger(DeltaDebugger.class);

	/*
	 * Minimize all the test in parallalel
	 */
	public static void minimize(File outputDir,
			final List<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases,
			String resetEnvironmentBy, //
			List<File> projectJars) throws IOException, URISyntaxException, InterruptedException {

		// Method under test is the last before XMLValidation

		// Include in the tests the XML Assertions
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {

			MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
			SootMethod testMethod = carvedTestCase.getThird();

			// If the return value is a primitive it cannot be found in the xml
			// file !
			List<Unit> validation = generateValidationUnit(testMethod, mut.getXmlDumpForOwner(),
					mut.getXmlDumpForReturn(), carvedTestCase.getSecond().getReturnObjectLocalFor(mut));

			logger.info("DeltaDebugger.minimize() Validation code: " + validation);
			final Body body = testMethod.getActiveBody();
			// The very last unit is the "return" we need the one before it...
			final PatchingChain<Unit> units = body.getUnits();
			// Insert the validation code - if any ?!
			if (validation.isEmpty()) {
				logger.warn("DeltaDebugger.minimize() VALIDAITON IS EMPTY FOR " + testMethod);
			} else {
				units.insertBefore(validation, units.getLast());
			}

		}

		Set<SootClass> _testClasses = new HashSet<>();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {
			SootClass sClass = carvedTestCase.getThird().getDeclaringClass();
			if (!_testClasses.contains(sClass)) {
				_testClasses.add(sClass);
			}
		}
		//
		boolean resolveTypes = true;
		File tempFolder = Files.createTempDirectory("DeltaDebug").toFile();
		Set<CompilationUnit> allTestClasses = TestCaseFactory.generateTestFiles(projectJars, tempFolder, _testClasses,
				resolveTypes);

		TestSuiteMinimizer testSuiteMinimizer = new TestSuiteMinimizer(allTestClasses, resetEnvironmentBy, new TestSuiteExecutor( projectJars ));
		// This will change allTestClasses by running delta debugging
		testSuiteMinimizer.minimize();

		// Eventually output the files
		for (CompilationUnit testClass : allTestClasses) {

			// Create the structure as javac expects
			String packageDeclaration = testClass.getPackageDeclaration().get().getNameAsString();
			String testClassName = testClass.getType(0).getNameAsString();

			File packageFile = new File(outputDir, packageDeclaration.replaceAll("\\.", File.separator));
			packageFile.mkdirs();

			// Store the testClassName into a File inside the tempOutputDir
			File classFile = new File(packageFile, testClassName + ".java");

			// Not nice to pass by String ...
			Files.write(classFile.toPath(), testClass.toString().getBytes());

		}

	}

	

	@Deprecated
	public static void minimize(File testSourceFolder,
			final Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase,
			List<File> projectJars) {

		// Generate the code to validate the given test
		MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
		SootMethod testMethod = carvedTestCase.getThird();

		// .getMethod("<de.unipassau.abc.tracing.XMLVerifier: void
		// verify(java.lang.Object,java.lang.String)>");

		logger.debug("DeltaDebugger.minimize() mut is " + mut);
		List<Unit> validation = generateValidationUnit(testMethod, mut.getXmlDumpForOwner(), mut.getXmlDumpForReturn(),
				null);

		final Body body = testMethod.getActiveBody();
		// The very last unit is the "return" we need the one before it...
		final PatchingChain<Unit> units = body.getUnits();
		//
		// final Unit methodUnderTest = units.getPredOf(units.getLast());

		List<Unit> reversedInvocations = new ArrayList<>(units);
		Collections.reverse(reversedInvocations);

		// Attach validation to testMethod
		units.insertBefore(validation, units.getLast());

		Iterator<Unit> iterator = reversedInvocations.iterator();

		// Skip return
		iterator.next();
		// Skip MUT
		iterator.next();
		// Not start Delta Debugging
		int removed = 0;
		int total = (reversedInvocations.size() - 2);
		//
		logger.debug("DeltaDebugger.minimize() Starting DELTA DEBUG ON (inaccurate) " + total + " invocations");
		// TODO We shall not consider newInvokeStatements here !
		while (iterator.hasNext()) {
			Unit unit = iterator.next();

			if (unit instanceof InvokeStmt) {
				InvokeExpr invokeExp = ((InvokeStmt) unit).getInvokeExpr();
				if (invokeExp instanceof NewInvokeExpr) {
					logger.info("DeltaDebugger.minimize() -- Skip " + unit);
					total--;
					continue;
				} else if (invokeExp instanceof InstanceInvokeExpr) {
					Value owner = ((InstanceInvokeExpr) invokeExp).getBase();
					if (owner.getType().equals(RefType.v("org.junit.contrib.java.lang.system.ExpectedSystemExit"))) {
						logger.info("DeltaDebugger.minimize() -- Skip " + unit);
						continue;
					}

				}
			}
			// Do not remove System.exit expectations otherwise the test breaks
			// !

			logger.debug("DeltaDebugger.minimize() -- Try to Remove " + unit);
			Unit insertionPoint = units.getPredOf(unit);
			if (insertionPoint == null) {
				// We reached the first instruction
				break;
			}
			// Try to remove the unit and check if test pass
			units.remove(unit);
			if (verifyExecution(testMethod, projectJars)) {
				logger.debug("DeltaDebugger.minimize() Verification Passed, remove " + unit);
				removed++;
			} else {
				logger.info("DeltaDebugger.minimize() Verification Failed ! Keep stmt: " + unit);
				units.insertAfter(unit, insertionPoint);
			}
		}

		// Remove validation unit
		units.removeAll(validation);

		// Clean up the unused the Variables !!
		removeUnusedVariables(body);

		logger.info("Removed " + removed + " out of " + total + " instructions from test " + testMethod + " "
				+ (((double) removed / (double) total) * 100));

		logger.debug("Generated test " + TestCaseFactory.converSootClassToJavaClass(testMethod.getDeclaringClass()));
	}

	private static void removeUnusedVariables(Body body) {
		final PatchingChain<Unit> units = body.getUnits();

		for (Iterator<Local> localsIterator = body.getLocals().iterator(); localsIterator.hasNext();) {
			if (!localUsed(localsIterator.next(), units)) {
				localsIterator.remove();
			}
		}

	}

	private static boolean localUsed(Local local, PatchingChain<Unit> units) {
		final AtomicInteger used = new AtomicInteger(0);
		for (Unit unit : units) {
			unit.apply(new AbstractStmtSwitch() {
				@Override
				public void caseAssignStmt(AssignStmt stmt) {
					super.caseAssignStmt(stmt);
					if (stmt.getLeftOp().equals(local)) {
						used.incrementAndGet();
					}
				}

				// Include also void and init
				@Override
				public void caseInvokeStmt(InvokeStmt stmt) {
					super.caseInvokeStmt(stmt);
					InvokeExpr invokeExpr = stmt.getInvokeExpr();
					if (invokeExpr instanceof InstanceInvokeExpr) {
						if (((InstanceInvokeExpr) invokeExpr).getBase().equals(local)) {
							used.incrementAndGet();
						}
					}
				}
			});
		}
		return (used.intValue() > 0);
	}

	public static boolean verifyExecution(SootMethod testMethod, List<File> projectJars) {
		long time = System.currentTimeMillis();
		try {
			boolean resolveTypes = false;
			//
			File tempOutputDir = Files.createTempDirectory("ABC-Delta-Debug").toFile();
			tempOutputDir.deleteOnExit();
			//
			SootClass testClass = testMethod.getDeclaringClass();
			TestCaseFactory.generateTestFiles(projectJars, tempOutputDir, Collections.singleton(testClass),
					resolveTypes);

			// Execute tests in tempOutputDir
			return compileAndRunJUnitTest(testClass.getName(), tempOutputDir, projectJars);

		} catch (IOException | URISyntaxException | InterruptedException e) {
			// e.printStackTrace();
			logger.debug("Failed verification of " + testMethod);
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Failed verification of " + testMethod);
			return false;
		} finally {
			time = System.currentTimeMillis() - time;
			logger.info("Verification done in " + time + " ms");
		}
	}

	

	@Deprecated
	public static boolean compileAndRunJUnitTest(String systemTestClassName, File tempOutputDir, List<File> projectJars)
			throws IOException, URISyntaxException, InterruptedException {
		// https://javabeat.net/the-java-6-0-compiler-api/

		///
		StringBuilder cpBuilder = new StringBuilder();
		// Include the class we are delta debugging
		cpBuilder.append(tempOutputDir.getAbsolutePath()).append(File.pathSeparator);
		// Include the project deps
		for (File jarFile : projectJars) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}
		// Include JUnit
		cpBuilder.append(ABCUtils.buildJUnit4Classpath()).append(File.pathSeparator);
		// Include System rules for
		cpBuilder.append(ABCUtils.getSystemRulesJar()).append(File.pathSeparator);
		// Include XMLDumper
		cpBuilder.append(ABCUtils.getTraceJar()).append(File.pathSeparator);
		// Include XMLDumper
		for (File jarFile : ABCUtils.getXStreamJars()) {
			cpBuilder.append(jarFile.getAbsolutePath()).append(File.pathSeparator);
		}

		String classpath = cpBuilder.toString();

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
		}

		String javaPath = SystemUtils.JAVA_HOME + File.separator + "bin" + File.separator + "java";

		ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", classpath, "org.junit.runner.JUnitCore",
				systemTestClassName);
		// System.out.println("DeltaDebugger.runJUnitTest() " +
		// processBuilder.command());

		// This causes problems wher run on command line
		// processBuilder.inheritIO();

		Process process = processBuilder.start();

		// Wait for the execution to end.
		if (!process.waitFor(4000, TimeUnit.MILLISECONDS)) {
			logger.info("Timeout for test execution");
			// timeout - kill the process.
			process.destroyForcibly(); // consider using destroyForcibly instead
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

	}

	public static List<Unit> generateValidationUnit(final SootMethod testMethod, final String xmlOwner,
			final String xmlReturnValue, //
			final Value primitiveReturnValue) {

		final List<Unit> validationUnits = new ArrayList<>();

		final Body body = testMethod.getActiveBody();
		// The very last unit is the "return" we need the one before it...
		PatchingChain<Unit> units = body.getUnits();
		final Unit methodUnderTest = units.getPredOf(units.getLast());

		// System.out.println("DeltaDebugger.generateValidationUnit() for " +
		// methodUnderTest);
		methodUnderTest.apply(new AbstractStmtSwitch() {

			final SootMethod xmlDumperVerify = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.XMLVerifier: void verify(java.lang.Object,java.lang.String)>");

			final SootMethod xmlDumperVerifyPrimitive = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.XMLVerifier: void verifyPrimitive(java.lang.Object,java.lang.String)>");

			// return value
			public void caseAssignStmt(soot.jimple.AssignStmt stmt) {
				InvokeExpr invokeExpr = stmt.getInvokeExpr();

				if (!(invokeExpr instanceof StaticInvokeExpr)) {
					generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
				}

				generateValidationForReturnValue(stmt.getLeftOp());
			};

			// No Return value
			public void caseInvokeStmt(soot.jimple.InvokeStmt stmt) {
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				if (stmt.containsInvokeExpr()) {
					if (!(stmt.getInvokeExpr() instanceof StaticInvokeExpr)) {
						// Extract OWNER
						generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
					}
				}
			}

			private void generateValidationForReturnValue(Value value) {
				Value wrappedValue = UtilInstrumenter.generateCorrectObject(body, value, validationUnits);
				if (JimpleUtils.isPrimitive(value.getType())) {
					generateValidationForPrimitiveValue(wrappedValue, primitiveReturnValue, validationUnits);
				} else {
					generateValidationForValue(wrappedValue, xmlReturnValue, validationUnits);
				}
			}

			private void generateValidationForCut(Value value) {
				generateValidationForValue(value, xmlOwner, validationUnits);
			}

			private void generateValidationForPrimitiveValue(Value actualValue, Value expectedValue,
					List<Unit> validationUnits) {

				List<Value> methodStartParameters = new ArrayList<Value>();
				methodStartParameters.add(actualValue);
				// Wrap everythign into a String !
				methodStartParameters.add(StringConstant.v(expectedValue.toString()));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerifyPrimitive.makeRef(), methodStartParameters)));
			};

			private void generateValidationForValue(Value value, String xmlFile, List<Unit> validationUnits) {

				if (!new File(xmlFile).exists()) {
					System.out.println("Cannot file XML File" + xmlFile + ", do not create validation call");
					return;
				}

				List<Value> methodStartParameters = new ArrayList<Value>();
				methodStartParameters.add(value);
				methodStartParameters.add(StringConstant.v(xmlFile));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerify.makeRef(), methodStartParameters)));
			};
		});

		// This can be a void method -> validate only CUT
		// a static method call -> validate only ReturnValue
		// a static void method call -> no validation needed
		// other -> validate both CUT and ReturnValue

		return validationUnits;
	}

	

}
