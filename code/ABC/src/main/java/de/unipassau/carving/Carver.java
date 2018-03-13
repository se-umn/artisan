package de.unipassau.carving;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.carving.carvers.Level_0_MethodCarver;
import de.unipassau.data.Pair;
import de.unipassau.data.Triplette;
import de.unipassau.generation.TestCaseFactory;
import de.unipassau.generation.TestGenerator;
import de.unipassau.utils.JimpleUtils;
import soot.SootClass;

// TODO Use some sort of CLI/JewelCLI
public class Carver {

	private static final Logger logger = LoggerFactory.getLogger(Carver.class);

	public interface CarverCLI {
		@Option
		File getTraceFile();

		@Option
		File getProjectJar();

		@Option(defaultToNull = true)
		File getOutputDir();

		@Option // This can be refined as package=regex, class=regex,
				// method="full jimple method"
		String getCarveBy();

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		long startTime = System.nanoTime();

		File traceFile = null;
		File projectJar = null;
		File outputDir = null;
		MethodInvocationMatcher carveBy = null;

		try {
			CarverCLI result = CliFactory.parseArguments(CarverCLI.class, args);
			traceFile = result.getTraceFile();
			outputDir = result.getOutputDir();
			projectJar = result.getProjectJar();

			// TODO This can be moved inside CLI parsing using an instance
			// strategy
			String[] carveByTokens = result.getCarveBy().split("=");
			if ("package".equals(carveByTokens[0])) {
				carveBy = new MethodInvocationMatcher(carveByTokens[1] + ".*");
			} else if ("class".equals(carveByTokens[0])) {
				carveBy = new MethodInvocationMatcher(carveByTokens[1]);
			} else if ("method".equals(carveByTokens[0])) {
				String jimpleMethod = carveByTokens[1];
				carveBy = MethodInvocationMatcher.fromJimpleMethod(jimpleMethod);
			} else {
				throw new ArgumentValidationException("Wrong carve by !");
			}

		} catch (ArgumentValidationException e) {
		}

		System.out.println("Carver.main() Start parsing ");
		// Parse the trace file into graphs
		StackImplementation traceParser = new StackImplementation();
		// TODO How to handle multiple trace files ? All together or one after
		// another?

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = traceParser
				.parseTrace(traceFile.getAbsolutePath());
		System.out.println("Carver.main() End parsing ");
		// Carving
		System.out.println("Carver.main() Start carving");
		// TODO Here organize, instantiate and execute the configured Carvers
		Level_0_MethodCarver testCarver = new Level_0_MethodCarver(parsedTrace.getFirst(), parsedTrace.getSecond(),
				parsedTrace.getThird());

		// TODO Instantiate a matcher and pass it along to Carver !
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = testCarver.carve(carveBy);
		System.out.println("Carver.main() End carving");

		// Verify
		// VERIFY THAT NO CARVED TEST IS EMPTY !
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			if (!carvedTest.getFirst().verify()) {
				throw new RuntimeException("Failed verification");
			}
		}

		System.out.println("Carver.main() Start code generation");
		// Test Generation
		TestGenerator testCaseGenerator = new TestGenerator(projectJar.getAbsolutePath());
		Collection<SootClass> testCases = testCaseGenerator.generateTestCases(carvedTests);

		for (SootClass testCase : testCases){
			JimpleUtils.prettyPrint(testCase);
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
