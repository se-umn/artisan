package de.unipassau.abc.carving;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
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
import de.unipassau.abc.generation.TestCaseFactory;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.generation.impl.EachTestAlone;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
import de.unipassau.abc.utils.JimpleUtils;
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
		boolean resolveTypes = true;
		TestCaseFactory.generateTestFiles(projectJars, outputDir, testClasses, resolveTypes);

		testGenerationTime = System.currentTimeMillis() - testGenerationTime;

		logger.info("Carver.main() End Test generation");

		long testSuiteMinimizationTime = System.currentTimeMillis();

		logger.info("Carver.main() Start Minimize Test Suite");
		// Generate Java Code from JimpleCode and add the validation units
		Set<CompilationUnit> _testClasses = getTestClasses(carvedTestCases, projectJars);

		// Minimized Carved Tests - We work at the level of JAVA files instead
		// of SootMethods since soot methods do not carry
		// Information about "generics", it's JVM fault BTW since bytecode does
		// not have that.
		//
		DeltaDebugger deltaDebugger = new DeltaDebugger(outputDir, _testClasses, resetEnvironmentBy, projectJars);
		try {
			deltaDebugger.minimizeTestSuite();
		} catch (CarvingException e) {
			logger.error("Error while minimimize the test suite !", e);
			System.exit(1);
		}

		logger.info("Carver.main() End Minimize Test Suite");
		testSuiteMinimizationTime = System.currentTimeMillis() - testSuiteMinimizationTime;

		long minimizationTime = System.currentTimeMillis();
		if (!skipMinimize) {
			// Minimized Carved Tests - We work at the level of JAVA files
			// instead of SootMethods since soot methods do not carry
			// Information about "generics", it's JVM fault BTW since bytecode
			// does not have that.
			//
			logger.info("Carver.main() Start Minimize Test Cases via Delta Debugging");

			deltaDebugger.minimizeTestCases();

			logger.info("Carver.main() End Minimize Test Cases via Delta Debugging");
		}
		minimizationTime = System.currentTimeMillis() - minimizationTime;

		// Output the files
		deltaDebugger.outputToFile();

		// At this point we can start the minimization via delta debugging

		long endTime = System.nanoTime();
		// Add statistics here ? number of tests etc ?
		System.out.println("====================================");
		//
		System.out.println("parsingTime " + parsingTime);
		System.out.println("carvingTime " + carvingTime);
		System.out.println("testGenerationTime " + testGenerationTime);
		System.out.println("testSuiteMinimizationTime " + testSuiteMinimizationTime);
		if (!skipMinimize) {
			System.out.println("minimizationTime " + minimizationTime);
		}
		System.out.println("====================================");
		System.out.println("Total " + (endTime - startTime) / 1000000000.0 + " s");

		// Why is this required? Will the application hang otherwise
		// System.exit(0);
	}

	public static Set<CompilationUnit> getTestClasses(
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod>> carvedTestCases,
			List<File> projectJars) throws IOException {

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
		//
		return TestCaseFactory.generateTestFiles(projectJars, tempFolder, _testClasses, resolveTypes);
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

				if( xmlFile == null ){
					System.out.println("Null XML File for " + value +" cannot do not create validation call");
					return;
				}
				
				if (!new File(xmlFile).exists()) {
					System.out.println("Cannot find XML File" + xmlFile + ", do not create validation call");
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
