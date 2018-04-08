package de.unipassau.abc.carving;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.DeltaDebugger;
import de.unipassau.abc.generation.MockingGenerator;
import de.unipassau.abc.generation.TestCaseFactory;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.generation.impl.EachTestAlone;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
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
		// This would set soot cp as the current running app cp
		// Options.v().set_soot_classpath(System.getProperty("java.path"));

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
		//
		System.setProperty("os.name", osName);
		//
		// p = Paths.get(file.getAbsolutePath());
		// System.out.println("P = " + p);

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

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

		// Output Regular Carved Tests
		TestCaseFactory.generateTestFiles(projectJars, outputDir, testClasses);

		testGenerationTime = System.currentTimeMillis() - testGenerationTime;
		logger.info("Carver.main() End Test generation");

		// Minimized Carved Tests
		logger.info("Carver.main() Start Minimize via Delta Debugging");
		long minimizationTime = System.currentTimeMillis();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {
			// Note that after minimization there might be equivalent classes...
			// TODO
			DeltaDebugger.minimize(carvedTestCase, projectJars);
		}
		// Rename Classes to _min
		Set<SootClass> minimizedTestClasses = new HashSet<>();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase : carvedTestCases) {
			minimizedTestClasses.add(carvedTestCase.getThird().getDeclaringClass());
		}
		for (SootClass minimizedTestClass : minimizedTestClasses) {
			minimizedTestClass.setName(minimizedTestClass.getName() + "_minimized");
		}
		TestCaseFactory.generateTestFiles(projectJars, outputDir, minimizedTestClasses);
		minimizationTime = System.currentTimeMillis() - minimizationTime;

		logger.info("Carver.main() End Minimize via Delta Debugging");

		// At this point we can start the minimization via delta debugging

		long endTime = System.nanoTime();
		// Add statistics here ? number of tests etc ?
		System.out.println("====================================");
		//
		System.out.println("parsingTime " + parsingTime);
		System.out.println("carvingTime " + carvingTime);
		System.out.println("testGenerationTime " + testGenerationTime);
		System.out.println("minimizationTime " + minimizationTime);
		System.out.println("====================================");
		System.out.println("Total " + (endTime - startTime) / 1000000000.0 + " s");

		// Why is this required? Will the application hang otherwise
		// System.exit(0);
	}

}
