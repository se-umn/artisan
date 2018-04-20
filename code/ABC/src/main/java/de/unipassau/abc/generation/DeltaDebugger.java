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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.SystemUtils;
import org.jboss.util.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.Carver;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.data.Triplette;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;

/**
 * TODO Do we need to remove elements in the inputs if we remove calls to
 * scanner.next? in theory we shall
 * 
 * @author gambi
 *
 */
public class DeltaDebugger {

	private final static Logger logger = LoggerFactory.getLogger(DeltaDebugger.class);

	private Set<CompilationUnit> augmentedTestClasses;
	private File outputDir;
	private String resetEnvironmentBy;
	private List<File> projectJars;


	public DeltaDebugger(File outputDir,
			Set<CompilationUnit> augmentedTestClasses, //
			String resetEnvironmentBy, //
			List<File> projectJars) throws IOException {

		this.augmentedTestClasses = augmentedTestClasses;
		this.outputDir = outputDir;
		this.resetEnvironmentBy = resetEnvironmentBy;
		this.projectJars = projectJars;
	}

	public void outputToFile() throws IOException {
		// Eventually output the files
		for (CompilationUnit testClass : augmentedTestClasses) {

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

	/*
	 * Minimize all the test in parallalel
	 */
	public void minimizeTestCases() throws IOException, URISyntaxException, InterruptedException {
		TestSuiteMinimizer testSuiteMinimizer = new TestSuiteMinimizer(augmentedTestClasses, resetEnvironmentBy,
				new TestSuiteExecutor(projectJars));
		testSuiteMinimizer.minimizeTestMethods();
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
		List<Unit> validation = Carver.generateValidationUnit(testMethod, mut.getXmlDumpForOwner(), mut.getXmlDumpForReturn(),
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

	@Deprecated
	public static boolean verifyExecution(SootMethod testMethod, List<File> projectJars) {
			throw new NotImplementedException();
//		long time = System.currentTimeMillis();
//		try {
//			boolean resolveTypes = false;
//			//
//			File tempOutputDir = Files.createTempDirectory("ABC-Delta-Debug").toFile();
//			tempOutputDir.deleteOnExit();
//			//
//			SootClass testClass = testMethod.getDeclaringClass();
//			
//			//TestCaseFactory.generateTestFiles(projectJars, tempOutputDir, Collections.singleton(testClass),
//				//	resolveTypes);
		//
		// return compileAndRunJUnitTest(testClass.getName(), tempOutputDir,
		// projectJars);
		// // Execute tests in tempOutputDir
		//
		// } catch (IOException | URISyntaxException | InterruptedException e) {
		// // e.printStackTrace();
		// logger.debug("Failed verification of " + testMethod);
		// return false;
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.debug("Failed verification of " + testMethod);
		// return false;
		// } finally {
		// time = System.currentTimeMillis() - time;
		// logger.info("Verification done in " + time + " ms");
		// }
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

	// Run delta debugger from Command Line
	public static void main(String[] args) {
		
	}
}
