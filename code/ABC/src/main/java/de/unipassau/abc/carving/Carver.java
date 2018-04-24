package de.unipassau.abc.carving;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.DeltaDebugger;
import de.unipassau.abc.generation.MockingGenerator;
import de.unipassau.abc.generation.SootTestCaseFactory;
import de.unipassau.abc.generation.TestCaseFactory;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.generation.TestSuiteExecutor;
import de.unipassau.abc.generation.impl.EachTestAlone;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
import de.unipassau.abc.utils.JimpleUtils;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicBoolean;
import soot.Body;
import soot.G;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.options.Options;

// TODO Use some sort of CLI/JewelCLI
public class Carver {

	private static final Logger logger = LoggerFactory.getLogger(Carver.class);

	public interface CarverCLI {
		@Option(longName = "trace-file")
		File getTraceFile();

		@Option(longName = "project-jar")
		List<File> getProjectJar();

		@Option(longName = "output-to", defaultValue = "./sootOutput/carvedTests")
		File getOutputDir();

		// This can be refined as package=<regex>, class=<regex>,
		// method=<jimple method>, return=<regex>, instance=<Object@ID>
		@Option(longName = "carve-by")
		String getCarveBy();

		// This works like carveBy but we use it to exclude method invocations
		// that are in the trace
		// but we do not want in the carving. For example, I can carve by return
		// type int, and got all the methods
		// like: <java.lang.Integer: int Integer.parse(java.lang.String)>
		// Probably we can also exclude java.lang by default inside Carving,
		// nota this simply tell the system to NOT carve tests for those method
		@Option(longName = "exclude-by", defaultToNull = true)
		List<String> getExcludeBy();

		// List the external interfaces, this can be packages or classes, but
		// for the moment we JUST use packages
		// since most of the time, thos interfaces are organized in some way
		@Option(longName = "external", defaultToNull = true)
		List<String> getExternalInterfaces();

		@Option(longName = "test-setup-by", defaultToNull = true)
		List<String> getTestSetupBy();

		// This is for delta debugging, to deal with state pollution like
		// filling in a db with predefined data...
		// It a static fully qualified method with void like
		// "SystemTestUtils.dropAndRecreateTheDb();"
		@Option(longName = "reset-environment-by", defaultToNull = true)
		String getResetEnvironmentBy();

		@Option(longName = "skip-minimize")
		boolean isSkipMinimize();

	}

	private static MethodInvocationMatcher getMatcherFor(final String type, final String regEx) {
		switch (type) {
		case "package":
			return MethodInvocationMatcher.byPackage(regEx);
		case "class":
			// return MethodInvocationMatcher.byClass(regEx);
			return MethodInvocationMatcher.byClassLiteral(regEx);
		case "method":
			// PAY ATTENTION TO THIS
			return MethodInvocationMatcher.byMethodLiteral(regEx);
		case "regex":
			return MethodInvocationMatcher.byMethod(regEx);
		case "invocation":
			return MethodInvocationMatcher.byMethodInvocation(regEx);
		case "return":
			return MethodInvocationMatcher.byReturnType(regEx);
		case "instance":
			// TODO how to rule out primitive, null, and possibly Strings ?
			return MethodInvocationMatcher.byInstance(new ObjectInstance(regEx));
		default:
			throw new ArgumentValidationException("Unknown matcher specification " + type + "=" + regEx);
		}
	}

	/*
	 * This is global anyway. Public for testing
	 */
	public static void setupSoot(List<File> projectJars) {
		System.out.println("Carver.setupSoot() Project Jars: ");
		for (File f : projectJars) {
			System.out.println("- " + f);
		}
		G.reset();
		//

		// System Settings Begin
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		String junit4Jar = null;
		String hamcrestCoreJar = null;

		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
			if (cpEntry.contains("junit-4")) {
				junit4Jar = cpEntry;
			} else if (cpEntry.contains("hamcrest-core")) {
				hamcrestCoreJar = cpEntry;
			}
		}

		String traceJar = ABCUtils.getTraceJar();
		String systemRulesJar = ABCUtils.getSystemRulesJar();
		List<File> xStreamJars = ABCUtils.getXStreamJars();

		ArrayList<String> necessaryJar = new ArrayList<String>();
		// Include here JUnit and Hamcrest
		for (File projectLib : projectJars) {
			necessaryJar.add(projectLib.getAbsolutePath());
		}
		necessaryJar.add(junit4Jar);
		necessaryJar.add(hamcrestCoreJar);
		necessaryJar.add(traceJar);
		//
		necessaryJar.add(systemRulesJar);
		//
		for (File projectLib : xStreamJars) {
			necessaryJar.add(projectLib.getAbsolutePath());
		}

		/*
		 * To process a JAR file, just use the same option but provide a path to
		 * a JAR instead of a directory.
		 */
		Options.v().set_process_dir(necessaryJar);

		// Soot has problems in working on mac.
		String osName = System.getProperty("os.name");
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", osName);
		//
		// p = Paths.get(file.getAbsolutePath());
		// System.out.println("P = " + p);

	}

	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
		long startTime = System.nanoTime();

		boolean skipMinimize = false;
		String resetEnvironmentBy = null;
		File traceFile = null;
		List<File> projectJars = new ArrayList<>();
		File outputDir = null;
		MethodInvocationMatcher carveBy = null;
		List<MethodInvocationMatcher> excludeBy = new ArrayList<>();
		{
			excludeBy.add(MethodInvocationMatcher.byPackage("java.lang"));
		}

		List<MethodInvocationMatcher> testSetupBy = new ArrayList<>();

		// TODO Code duplicate with InstrumentTracer
		List<String> externalInterfaces = new ArrayList<>();
		{
			// Default Interfaces are the one for Files, Network, etc. ?
		}

		try {
			CarverCLI cli = CliFactory.parseArguments(CarverCLI.class, args);
			traceFile = cli.getTraceFile();
			outputDir = cli.getOutputDir();
			projectJars.addAll(cli.getProjectJar());

			setupTypeSolver(projectJars);

			resetEnvironmentBy = cli.getResetEnvironmentBy();

			skipMinimize = cli.isSkipMinimize();

			// TODO This can be moved inside CLI parsing using an instance
			// strategy
			String[] carveByTokens = cli.getCarveBy().split("=");
			carveBy = getMatcherFor(carveByTokens[0], carveByTokens[1]);

			if (cli.getExcludeBy() != null) {
				for (String exclude : cli.getExcludeBy()) {
					String[] excludeByTokens = exclude.split("=");
					excludeBy.add(getMatcherFor(excludeByTokens[0], excludeByTokens[1]));
				}
			}

			if (cli.getTestSetupBy() != null) {
				for (String testSetup : cli.getTestSetupBy()) {
					String[] testSetupTokens = testSetup.split("=");
					testSetupBy.add(getMatcherFor(testSetupTokens[0], testSetupTokens[1]));
				}
			}

			externalInterfaces.addAll(
					(cli.getExternalInterfaces() != null) ? cli.getExternalInterfaces() : new ArrayList<String>());

		} catch (ArgumentValidationException e) {
			throw new RuntimeException(e);
		}

		// Build the externalInterfaceMatchers, during parsers those will mark
		// corresponding method invocation with the flag
		List<MethodInvocationMatcher> externalInterfaceMatchers = new ArrayList<MethodInvocationMatcher>();
		for (String externalInterface : externalInterfaces) {

			System.out.println("Carver.main() >>>> external interface " + externalInterface);
			// MethodInvocationMatcher.
			// By default those are class matchers !
			if (externalInterface.contains("=")) {
				externalInterfaceMatchers
						.add(getMatcherFor(externalInterface.split("=")[0], externalInterface.split("=")[1]));

			} else {
				externalInterfaceMatchers.add(MethodInvocationMatcher.byClass(externalInterface));
			}
		}

		setupSoot(projectJars);

		logger.info("Carver.main() Start parsing ");

		long parsingTime = System.currentTimeMillis();

		// Parse the trace file into graphs
		StackImplementation traceParser = new StackImplementation(testSetupBy);
		// TODO How to handle multiple trace files ? All together or one after
		// another?

		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = traceParser
				.parseTrace(traceFile.getAbsolutePath(), externalInterfaceMatchers);

		parsingTime = System.currentTimeMillis() - parsingTime;

		logger.info("Carver.main() End parsing ");

		logger.debug(">> Analyzed " + parsedTrace.size() + " system tests");

		logger.info("Carver.main() Start carving");
		long carvingTime = System.currentTimeMillis();

		// TODO Propagate here the tracing link to system test if necessary
		// For each system test we carve out unit tests and accumulate
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();
		//
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTest : parsedTrace.values()) {
			Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTest.getFirst(), parsedTest.getSecond(),
					parsedTest.getThird());
			carvedTests.addAll(testCarver.carve(carveBy, excludeBy));
		}

		// FIXME Remove duplicates here !

		logger.info("Carver.main() End carving");

		logger.debug(">> Carved tests : " + carvedTests.size());
		carvingTime = System.currentTimeMillis() - carvingTime;

		// Verify
		// VERIFY THAT NO CARVED TEST IS EMPTY !
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			if (!carvedTest.getFirst().verify()) {
				throw new RuntimeException("Failed verification");
			}
		}

		logger.info("Carver.main() Start Test generation");
		long testGenerationTime = System.currentTimeMillis();

		// Test Generation
		TestGenerator testCaseGenerator = new TestGenerator(parsedTrace);
		// DEFAULT SETTINGS
		List<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases = testCaseGenerator
				.generateTestCases(carvedTests, new EachTestAlone());

		// Collect the test classes (back compatibility)
		Set<SootClass> testClasses = new HashSet<>();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {
			testClasses.add(carvedTestCase.getThird().getDeclaringClass());
		}

		logger.debug("Generating input mocking values");
		// TODO Are assertions STILL generated ?!
		//// DECORATORS HERE: ASSERTIONS AND MOCKING !
		for (SootClass testClass : testClasses) {
			MockingGenerator.addSystemIn(testClass, parsedTrace);
			// FIXME ?! -> How it is possible that I see system.exit on the
			// output ?!
			// MockingGenerator.addSystemExit(testClass, parsedTrace);
		}

		// logger.info("Generating regression assertions - SKIP");
		// MethodInvocation methodInvocationUnderTest =
		// executionFlowGraph.getLastMethodInvocation();
		//
		// if (!methodInvocationUnderTest.isStatic()) {
		// Value actualOwner =
		// dataDependencyGraph.getObjectLocalFor(methodInvocationUnderTest);
		// Value expectedOwner =
		// UtilInstrumenter.generateExpectedValueForOwner(methodInvocationUnderTest,
		// body,
		// units);
		// AssertionGenerator.gerenateRegressionAssertionOnOwner(body, units,
		// expectedOwner, actualOwner);
		// }
		//
		// if
		// (!JimpleUtils.isVoid(JimpleUtils.getReturnType(methodInvocationUnderTest.getJimpleMethod())))
		// {
		// Value expectedReturnValue =
		// dataDependencyGraph.getReturnObjectLocalFor(methodInvocationUnderTest);
		// if (JimpleUtils.isPrimitive(expectedReturnValue.getType())
		// || JimpleUtils.isString(expectedReturnValue.getType())) {
		// // Do nothing, since the expectedReturnValue is a primitive
		// // value
		// } else {
		// // Otherwise, load expectations from file:
		// // Reads this from XML and introduce the code to load this
		// // from XML
		// expectedReturnValue =
		// UtilInstrumenter.generateExpectedValueForReturn(methodInvocationUnderTest,
		// body,
		// units);
		// }
		//
		// // Find the
		// Local actualReturnValue = null;
		// for (Local local : body.getLocals().getElementsUnsorted()) {
		// if ("returnValue".equals(local.getName())) {
		// actualReturnValue = local;
		// break;
		// }
		// }
		// AssertionGenerator.gerenateRegressionAssertionOnReturnValue(body,
		// units, expectedReturnValue,
		// actualReturnValue);
		// }

		// Output Regular Carved Tests to files and returns them as
		// CompilationUnit. Note that these classes have no problems with
		// generics and casting.

		// boolean resolveTypes = true;
		// This checks and addresses problems like missing initialization,
		// casting, etc.
		Set<CompilationUnit> generatedTestClasses = null;

		// Set<CompilationUnit> generatedTestClasses =
		// TestCaseFactory.generateTestFiles(projectJars, testClasses,
		// resolveTypes);

		// Store original tests to file
		// storeToFile(generatedTestClasses, outputDir);

		/// Store those to another folder for debugging
		Set<CompilationUnit> sootGeneratedTestClasses = SootTestCaseFactory.generateTestFiles(projectJars, testClasses);
		storeToFile(sootGeneratedTestClasses, outputDir);

		// Try out generation using soot to see if it faster/better
		generatedTestClasses = sootGeneratedTestClasses;

		// Soot generated files are optimized, there's less variables and test
		// cases have less instructions

		testGenerationTime = System.currentTimeMillis() - testGenerationTime;

		logger.info("Carver.main() End Test generation. Files are available under " + outputDir.getAbsolutePath());

		///////

		logger.info("Validating the carved tests STARTS");

		// This is only to improve readability of the code
		Set<CompilationUnit> augmentedTestClasses = generatedTestClasses;
		try {
			// Augment the test cases with validation calls and reset
			// environment. This regenerate the compilation units but DOES not
			// rename it !
			augmentTestSuite(augmentedTestClasses, carvedTestCases, projectJars, resetEnvironmentBy);

			TestSuiteExecutor testSuiteExecutor = new TestSuiteExecutor(projectJars);
			// This executes the test into a temp folder anyway, no need to
			// rename them !
			testSuiteExecutor.compileRunAndGetCoverageJUnitTests(augmentedTestClasses);
		} catch (CarvingException e1) {
			// This happens if
			logger.warn("Validation of the carved tests failed. ", e1);
		}
		logger.info("Validating the carved tests END");

		// long testSuiteMinimizationTime = System.currentTimeMillis();

		// // TODO Use test minimizer in post processing
		// if (!skipMinimize) {
		// logger.info("Carver.main() Start Minimize Test Suite");
		// // At this point augmentedTestClasses contains all the necessary
		// // code
		// // for validation and minimization
		// try {
		// TestSuiteMinimizer testSuiteMinimizer = new
		// TestSuiteMinimizer(augmentedTestClasses, resetEnvironmentBy,
		// new TestSuiteExecutor(projectJars));
		//
		// // We use clone, so this should return new classes...
		// Set<CompilationUnit> reducedTestSuite =
		// testSuiteMinimizer.minimizeTestSuite();
		//
		// cleanUpTestSuite(reducedTestSuite);
		//
		// // REFACTOR and Clean up.
		// for (CompilationUnit reducedTestClass : reducedTestSuite) {
		// renameClass(reducedTestClass, "_reduced");
		//
		// Carver.removeAtBeforeResetMethod(reducedTestClass);
		//
		// Carver.removeXMLVerifierCalls(reducedTestClass);
		// }
		//
		// // STORE IN THE SAME OUTPUT FOLDER
		// storeToFile(reducedTestSuite, outputDir);
		//
		// } catch (CarvingException e) {
		// logger.error("Error while minimimize the test suite !", e);
		// System.exit(1);
		// }
		//
		// logger.info("Carver.main() End Minimize Test Suite");
		// }
		// testSuiteMinimizationTime = System.currentTimeMillis() -
		// testSuiteMinimizationTime;

		long minimizationTime = System.currentTimeMillis();

		// Delta Debugging - We apply this to OUR test cases, not the REDUCED
		// ones !
		// This might take some time to complete... Note that we are modifying
		// augmentedTestClasses
		if (!skipMinimize) {
			DeltaDebugger deltaDebugger = new DeltaDebugger(outputDir, augmentedTestClasses, resetEnvironmentBy,
					projectJars);

			// Minimized Carved Tests - We work at the level of JAVA files
			// instead of SootMethods since soot methods do not carry
			// Information about "generics", it's JVM fault BTW since bytecode
			// does not have that.
			//
			logger.info("Carver.main() Start Minimize Test Cases via Delta Debugging");

			// This changes augmentedTestClasses
			deltaDebugger.minimizeTestCases();

			// TODO At this point we might compress and remove duplicate test
			// cases !

			// REFACTOR and clean up
			for (CompilationUnit reducedTestClass : augmentedTestClasses) {
				renameClass(reducedTestClass, "_minimized");

				Carver.removeAtBeforeResetMethod(reducedTestClass);

				Carver.removeXMLVerifierCalls(reducedTestClass);
			}

			// Output the files
			deltaDebugger.outputToFile();

		}
		logger.info("Carver.main() End Minimize Test Cases via Delta Debugging");
		//
		minimizationTime = System.currentTimeMillis() - minimizationTime;

		// At this point we can start the minimization via delta debugging

		long endTime = System.nanoTime();
		// Add statistics here ? number of tests etc ?
		System.out.println("====================================");
		//
		System.out.println("parsingTime " + parsingTime);
		System.out.println("carvingTime " + carvingTime);
		System.out.println("testGenerationTime " + testGenerationTime);
		if (!skipMinimize) {
			// System.out.println("testSuiteMinimizationTime " +
			// testSuiteMinimizationTime);
			System.out.println("minimizationTime " + minimizationTime);
		}
		System.out.println("====================================");
		System.out.println("Total " + (endTime - startTime) / 1000000000.0 + " s");

		// Why is this required? Will the application hang otherwise
		// System.exit(0);
	}

	private static void cleanUpTestSuite(Set<CompilationUnit> reducedTestSuite) {
		for (CompilationUnit testClass : reducedTestSuite) {
			Carver.removeAtBeforeResetMethod(testClass);

			Carver.removeXMLVerifierCalls(testClass);
		}

	}

	public static void storeToFile(Set<CompilationUnit> generatedTestClasses, File outputDir) throws IOException {
		logger.info("Storing tests to " + outputDir.getAbsolutePath());

		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new RuntimeException("Output dir " + outputDir.getAbsolutePath() + " cannot be created ");
		}

		for (CompilationUnit testClass : generatedTestClasses) {
			// Store to file
			File packageDir = new File(outputDir,
					testClass.getPackageDeclaration().get().getNameAsString().replaceAll("\\.", File.separator));
			packageDir.mkdirs();
			File classFile = new File(packageDir, testClass.getType(0).getNameAsString() + ".java");
			//
			Files.write(classFile.toPath(), testClass.toString().getBytes(), StandardOpenOption.CREATE_NEW);
		}

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

				// Wrap everythign into a String ! Why ? Can't I simply box the
				// expected Value as well, because this will be invoked on java
				// and fail !
				methodStartParameters.add(StringConstant.v(expectedValue.toString()));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerifyPrimitive.makeRef(), methodStartParameters)));
			};

			private void generateValidationForValue(Value value, String xmlFile, List<Unit> validationUnits) {

				if (xmlFile == null || xmlFile.trim().length() == 0) {
					// it can be a void call
					logger.debug("Null XML File for " + value + " cannot do not create validation call");
					return;
				}

				if (!new File(xmlFile).exists()) {
					logger.warn("Cannot find XML File" + xmlFile + ", do not create validation call");
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

	/**
	 * This returns an augmented test suite+ resetMethod @ before +XMLVerify
	 * calls
	 * 
	 * FIXME: instead of regenerating the entire class from soot, generate ONLY
	 * the XML verify statements, in the end those, if any are simply two calls
	 * to static methods with given parameteres (objectName and returnValue) !
	 * 
	 * @param carvedTestCases
	 * @param projectJars
	 * @param resetEnvironmentBy
	 * @return
	 * @throws IOException
	 */
	public static void augmentTestSuite(Set<CompilationUnit> generatedClasses,
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases, //
			List<File> projectJars, //
			String resetEnvironmentBy) throws IOException {

		// Deep copy, no we keep using the augmented one
		// Set<CompilationUnit> augmentedTestClasses = new
		// HashSet<>(generatedClasses);

		// Include in the tests the XML Assertions
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {

			MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
			SootMethod testMethod = carvedTestCase.getThird();

			// If the return value is a primitive it cannot be found in the xml
			// file ! This includes also wrapping types !!! :(

			final List<Unit> validation = generateValidationUnit(testMethod, mut.getXmlDumpForOwner(),
					mut.getXmlDumpForReturn(), carvedTestCase.getSecond().getReturnObjectLocalFor(mut));

			// Find the class and test method
			for (CompilationUnit cu : generatedClasses) {
				String sootClassName = testMethod.getDeclaringClass().getName();
				String javaClassName = cu.getPackageDeclaration().get().getNameAsString() + "."
						+ cu.getType(0).getNameAsString();
				if (javaClassName.equals(sootClassName)) {

					cu.accept(new ModifierVisitor<JavaParserFacade>() {
						public Visitable visit(MethodDeclaration n, JavaParserFacade arg) {

							System.out.println(" Visiting " + n.getDeclarationAsString());
							// There's only 1 test method
							// if
							// (n.getNameAsString().equals(testMethod.getName()))
							// {
							if (n.isAnnotationPresent(Test.class)) {
								BlockStmt methodBody = n.getBody().get();

								// TODO FIND A WAY TO GET THE LATEST INVOCATION, FIND IT'S RETURN TYPE, AND CREATE AN ASSIGN STATMENT TO REPLACE IT
//								// Check if the last statment has the
//								// returnValue variabls
//								Statement mut = methodBody.getStatements().removeLast();
//
//								if (!mut.toString().contains("returnValue")) {
//									// TODO We need to update this statement
//									mut.accept(new VoidVisitorAdapter<JavaParserFacade>() {
//										public void visit(MethodCallExpr n, JavaParserFacade arg) {
//											System.out.println(" Finding return type for " + n + " --> "
//													+ n.getNameAsExpression());
//											try {
//												arg.getType(n.getNameAsExpression());
//											} catch (Exception e) {
//												System.out.println("Cannot find type for " + n);
//											}
//										}
//
//									}, arg);
//
//								}
//
//								methodBody.addStatement(mut);

								// Include the statements into the body
								for (Unit validationUnit : validation) {

									if (validationUnit instanceof InvokeStmt) {
										InvokeExpr validationExpr = ((InvokeStmt) validationUnit).getInvokeExpr();

										Expression validationStmt = TestCaseFactory
												.convertSootInvocationExpr(validationExpr, testMethod.getActiveBody());

										// TODO Introduce the returnValue
										// varaible agin ! wee most likely need
										// symbolSovler
										methodBody.addStatement(validationStmt);
										// Create the statements here and
										// include
										// them in the right test method body !
										logger.info("DeltaDebugger.minimize() Add Validation code: " + validationStmt
												+ " to " + testMethod.getName());
									}
								}
								return n;
							} else {
								return super.visit(n, arg);
							}

						}
					}, JavaParserFacade.get(TYPE_SOLVER));
				}
			}

			// Generate "literally" the calls

			// Add the calls to the method bofy

			// final Body body = testMethod.getActiveBody();
			// // The very last unit is the "return" we need the one before
			// it...
			// final PatchingChain<Unit> units = body.getUnits();
			// // Insert the validation code - if any ?!
			// if (validation.isEmpty()) {
			// logger.debug("DeltaDebugger.minimize() VALIDAITON IS EMPTY FOR "
			// + testMethod);
			// } else {
			// units.insertBefore(validation, units.getLast());
			// }

		}

		// //
		// Set<SootClass> _testClasses = new HashSet<>();
		// for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>
		// carvedTestCase : carvedTestCases) {
		// SootClass sClass = carvedTestCase.getThird().getDeclaringClass();
		// if (!_testClasses.contains(sClass)) {
		// _testClasses.add(sClass);
		// }
		// }
		// //
		// // HERE WE DO NOT NEED TO Check for missing initialization and
		// casting and such anymore !!
		// boolean resolveTypes = true;
		// Set<CompilationUnit> testClasses =
		// TestCaseFactory.generateTestFiles(projectJars, _testClasses,
		// resolveTypes);

		for (CompilationUnit testClass : generatedClasses) {
			// Include the reset environment call if needed
			createAtBeforeResetMethod(resetEnvironmentBy, testClass);
		}

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
		// Change the class name - Avoid working with strings
		final SimpleName originalTestClassName = testClass.getType(0).getName();
		// New name
		testClass.getType(0).setName(originalTestClassName.asString().concat(postFix));
		final SimpleName newName = testClass.getType(0).getName();
		//
		testClass.accept(new ModifierVisitor<Void>() {
			@Override
			public Visitable visit(ConstructorDeclaration n, Void arg) {
				if (originalTestClassName.equals(n.getName())) {
					n.setName(newName);
				}
				return super.visit(n, arg);
			}
		}, null);

	}

	public static void removePureMethods(CompilationUnit testClass) {
		testClass.accept(new ModifierVisitor<Void>() {

			// This is a void or a method call whose return value is not
			// assigned to anyone
			@Override
			public Visitable visit(MethodCallExpr n, Void arg) {
				if (isPure(n)) {
					logger.info(" Remove pure method call " + n);
					return null;
				}
				return super.visit(n, arg);
			}

		}, null);

	}

	// TODO Assume tMethodCall are not Assign Expt !
	public static boolean isMandatory(Statement statement) {
		// return m.equals("close") || false;
		if (statement instanceof ExpressionStmt) {
			ExpressionStmt es = (ExpressionStmt) statement;
			Expression e = es.getExpression();
			// Naive
			String exp = e.toString();
			// System.out.println("TestSuiteMinimizer.isMandatory() Processing "
			// + statement );
			// Thos are all hardcoded
			return exp.contains(".close()") || exp.contains("= null");
		}
		return false;

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

	// Purity methods are checked for Method Calls, that is calls to methods
	// whose value is not assigned !
	public static boolean isPure(MethodCallExpr methodCall) {
		// TODO Assume tMethodCall are not Assign Expt !
		String m = methodCall.getNameAsString();
		return m.equals("length") || m.equals("startsWith") || m.equals("endsWith") || m.equals("lastIndexOf") || // String
				m.equals("getAutoCommit") || // Sql Connection
				m.equals("getResultSet") || // Sql statement
				m.equals("size") || // List/Collections
				// TODO Check the pure method is not also the MUT !
				// m.equals("getPrice") || m.equals("getTotalPrice") ||
				// m.equals("getAdults") || m.equals("getChildren")
				// || m.equals("getMaxOccupancy") || m.equals("getRoomID") ||
				// m.equals("getTotalPrice") || // HotelMe
				// "getCheckOut", getCheckIn
				false;

	}

	// VERY BAD !

	private static TypeSolver TYPE_SOLVER;

	private static void setupTypeSolver(List<File> projectJars) throws IOException {
		// Resolve the missing generics
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
		combinedTypeSolver.add(new ReflectionTypeSolver());

		for (File jar : projectJars) {
			combinedTypeSolver.add(new JarTypeSolver(jar.getAbsolutePath()));
		}
		TYPE_SOLVER = combinedTypeSolver;
	}

	private static final Set<String> externalInterfaces = new HashSet<>(Arrays.asList(new String[] { //
			// "java.util.Scanner", // Leave the calls to Scanner for the moment
			"java.io.FileWriter", //
			"java.nio.Path", //
			"java.io.BufferedWriter", //
			"java.io.Writer", //
			"java.io.File", //
			"java.nio.Files", //
			"java.sql.Connection", //
			"java.sql.Statement", //
			"java.sql.PreparedStatement" }));

	// this requires the SymbolSover to get the type of S
	// A statement might include several calls !
	@SuppressWarnings("unchecked")
	public static boolean isExternalInterface(Statement s) {
		final AtomicBoolean isExternalInterface = new AtomicBoolean(false);
		// System.out.println("Carver.isExternalInterface() " + s);
		s.accept(new VoidVisitorAdapter<JavaParserFacade>() {
			@Override
			public void visit(ExpressionStmt n, JavaParserFacade arg) {
				// TODO Auto-generated method stub
				// System.out.println("visit() ExpressionStmt " + n);
				super.visit(n, arg);
			}

			@Override
			public void visit(AssignExpr n, JavaParserFacade arg) {
				// TODO Auto-generated method stub
				System.out.println("visit() AssignExpr " + n);
				// ResolvedType targetType =
				// javaParserFacade.getType(n.getTarget());
				// ResolvedType valueType =
				// javaParserFacade.getType(n.getValue());
				super.visit(n, arg);
			}

			@Override
			public void visit(MethodCallExpr n, JavaParserFacade arg) {
				// TODO Auto-generated method stub
				// Unless its a static ?
				try {
					// System.out.println("visit() MethodCallExpr " + n + " " +
					// n.getScope().get());
					String typeName = arg.getType(n.getScope().get()).describe();
					// System.out.println("Resolved type: " + typeName);

					if (externalInterfaces.contains(typeName)) {
						// System.out.println("External interface : " +
						// typeName);
						isExternalInterface.set(true);
					}

				} catch (Exception e) {
					System.out.println("Cannot resolve type " + e);
				}
				super.visit(n, arg);

			}
		}, JavaParserFacade.get(TYPE_SOLVER));

		return isExternalInterface.get();
	}
}
