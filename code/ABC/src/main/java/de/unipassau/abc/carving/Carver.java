package de.unipassau.abc.carving;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.carvers.Level_0_MethodCarver;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.MockingGenerator;
import de.unipassau.abc.generation.TestCaseFactory;
import de.unipassau.abc.generation.TestGenerator;
import de.unipassau.abc.utils.JimpleUtils;
import soot.SootClass;

// TODO Use some sort of CLI/JewelCLI
public class Carver {

	private static final Logger logger = LoggerFactory.getLogger(Carver.class);

	public interface CarverCLI {
		@Option(longName = "trace-file")
		File getTraceFile();

		@Option(longName = "project-jar")
		List<File> getProjectJar();

		@Option(longName = "output-to", defaultToNull = true)
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
		String getExcludeBy();

		// List the external interfaces, this can be packages or classes, but
		// for the moment we JUST use packages
		// since most of the time, thos interfaces are organized in some way
		@Option(longName = "external", defaultToNull = true)
		List<String> getExternalInterfaces();

	}

	private static MethodInvocationMatcher getMatcherFor(final String type, final String regEx) {
		switch (type) {
		case "package":
			return MethodInvocationMatcher.byPackage(regEx);
		case "class":
			return MethodInvocationMatcher.byClass(regEx);
		case "method":
			// PAY ATTENTION TO THIS
			return MethodInvocationMatcher.byMethodLiteral(regEx);
		case "regex":
			return MethodInvocationMatcher.byMethod(regEx);
		case "return":
			return MethodInvocationMatcher.byReturnType(regEx);
		case "instance":
			// TODO how to rule out primitive, null, and possibly Strings ?
			return MethodInvocationMatcher.byInstance(new ObjectInstance(regEx));
		default:
			throw new ArgumentValidationException("Unknown matcher specification " + type + "=" + regEx);
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		File traceFile = null;
		List<File> projectJars = new ArrayList<>();
		File outputDir = null;
		MethodInvocationMatcher carveBy = null;
		MethodInvocationMatcher excludeBy = null;
		// TODO Code duplicate with InstrumentTracer
		List<String> externalInterfaces = new ArrayList<>();
		{
			// Default Interfaces are the one for Files, Network, etc. ?
			// External interfaces are defined by Class
			externalInterfaces.add("java.util.Scanner");
			externalInterfaces.add("java.nio.file.Path");
		}

		List<String> pureMethods = new ArrayList<>();
		{
			// Default pure methods
		}
		try {
			CarverCLI cli = CliFactory.parseArguments(CarverCLI.class, args);
			traceFile = cli.getTraceFile();
			outputDir = cli.getOutputDir();
			projectJars.addAll( cli.getProjectJar() );

			// TODO This can be moved inside CLI parsing using an instance
			// strategy
			String[] carveByTokens = cli.getCarveBy().split("=");
			carveBy = getMatcherFor(carveByTokens[0], carveByTokens[1]);

			if (cli.getExcludeBy() != null) {
				String[] excludeByTokens = cli.getExcludeBy().split("=");
				excludeBy = getMatcherFor(excludeByTokens[0], excludeByTokens[1]);
			} else {
				excludeBy = MethodInvocationMatcher.noMatch();
			}

			externalInterfaces.addAll((cli.getExternalInterfaces() != null) ? cli.getExternalInterfaces()
					: new ArrayList<String>());

		} catch (ArgumentValidationException e) {
		}

		// Build the externalInterfaceMatchers, during parsers those will mark
		// corresponding method invocation with the flag
		List<MethodInvocationMatcher> externalInterfaceMatchers = new ArrayList<MethodInvocationMatcher>();
		for (String externalInterface : externalInterfaces) {
			
			System.out.println("Carver.main() >>>> external interface " + externalInterface );
			
			// By default those are class matchers !
			externalInterfaceMatchers.add(MethodInvocationMatcher.byClass(externalInterface));
		}

		List<MethodInvocationMatcher> purityMatchers = new ArrayList<MethodInvocationMatcher>();
		for (String pureMethod : pureMethods) {
			// By default those are class method matchers!
			purityMatchers.add(MethodInvocationMatcher.byMethod(pureMethod));
		}
		//
		{
			// TODO Additional pure methods... this is TRICKY !
			purityMatchers.add(MethodInvocationMatcher.byClass("java.lang.StringBuilder"));
		}

		logger.info("Carver.main() Start parsing ");
		// Parse the trace file into graphs
		StackImplementation traceParser = new StackImplementation(purityMatchers);
		// TODO How to handle multiple trace files ? All together or one after
		// another?

		Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = traceParser
				.parseTrace(traceFile.getAbsolutePath(), externalInterfaceMatchers);

		logger.info("Carver.main() End parsing ");
		logger.info(">> Analyzed " + parsedTrace.size() + " system tests");

		// Carving
		logger.info("Carver.main() Start carving");

		// TODO Propagate here the tracing link to system test if necessary
		// For each system test we carve out unit tests and accumulate
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTest : parsedTrace.values()) {
			Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTest.getFirst(), parsedTest.getSecond(),
					parsedTest.getThird());
			carvedTests.addAll(testCarver.carve(carveBy, excludeBy));
		}

		System.out.println("Carver.main() End carving");
		System.out.println(">> Carved tests : " + carvedTests.size());

		// Verify
		// VERIFY THAT NO CARVED TEST IS EMPTY !
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			if (!carvedTest.getFirst().verify()) {
				throw new RuntimeException("Failed verification");
			}
		}

		System.out.println("Carver.main() Start code generation");
		// Test Generation
		TestGenerator testCaseGenerator = new TestGenerator(projectJars.toArray( new File[]{}));
		Collection<SootClass> testCases = testCaseGenerator.generateTestCases(carvedTests);

		// TODO Are assertions STILL generated ?!
		//// DECORATORS HERE: ASSERTIONS AND MOCKING !
		for (SootClass testClass : testCases) {
			MockingGenerator.addSystemIn(testClass, parsedTrace);
		}

		// TODO Add Asssertions here...
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

		// FOR VISUAL DEBUG
		if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
			for (SootClass testCase : testCases) {
				JimpleUtils.prettyPrint(testCase);
			}
		}

		if (outputDir == null) {
			// Use default
			outputDir = new File("./sootOutput/carvedTests");
		}
		TestCaseFactory.generateTestFiles(outputDir, testCases);
		System.out.println("Carver.main() End code generation");

		long endTime = System.nanoTime();

		logger.info("Unit Test Generation " + (endTime - startTime) / 1000000000.0 + " s");
		// Why is this required? Will the application hang otherwise
		// System.exit(0);
	}

}
