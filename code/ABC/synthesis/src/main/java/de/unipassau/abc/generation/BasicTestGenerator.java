package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.AndroidCarvedTest;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.data.CatchBlock;
import de.unipassau.abc.parsing.ParsedTrace;

public class BasicTestGenerator implements TestGenerator {

	private final static Logger logger = LoggerFactory.getLogger(BasicTestGenerator.class);

	@Override
	public Collection<CarvedTest> generateTests(Set<MethodInvocation> targetMethodsInvocations, ParsedTrace trace)
			throws CarvingException, ABCException {
		// NOT EASY TO FIND DUPLICATES :)
		Collection<CarvedTest> carvedTests = new ArrayList<>();

		/*
		 * Extract only the necessary invocations from the trace, using direct
		 * data-dependencies
		 */
		BasicCarver carver = new BasicCarver(trace);

		for (MethodInvocation targetMethodInvocation : targetMethodsInvocations) {
			for (CarvedExecution carvedExecution : carver.carve(targetMethodInvocation)) {
				carvedTests.add(generateCarvedTestFromCarvedExecution(carvedExecution));
			}
		}

		return carvedTests;
	}

	/**
	 * @param carvedExecution
	 * @return
	 * @throws CarvingException
	 */
	public CarvedTest generateCarvedTestFromCarvedExecution(CarvedExecution carvedExecution) throws CarvingException {

		/**
		 * A carved test can be implemented as a list of method executions (i.e,.
		 * ExecutionFlowGraph) connected with DataDependencies (i.e,
		 * DataDependencyGraph);
		 */
		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();

		// TODO Include Implicit oracles assertions, i.e., exception?
		// TODO Include regression assertions on basic types.
		// TODO Include mocking or Android-robolectric setup ?
		// TODO Generate oracles, DO We need to carve the BODY of the method under test
		// to see how parameters/objects are used by it?
		MethodInvocation methodInvocationUnderTest = carvedExecution.methodInvocationUnderTest;

		/*
		 * The carved execution contains a collection of (complete) call graphs, but in
		 * the test we can only directly invoke the root(s) of those graphs, as all the
		 * other invocations will be automatically executed (unless we breakpoint the
		 * execution?).
		 *
		 */
		List<MethodInvocation> directlyCallableMethodInvocations = new ArrayList<>();
		carvedExecution.callGraphs.forEach(callGraph -> directlyCallableMethodInvocations.addAll(callGraph.getRoots()));

		/*
		 * Validation: If the directlyCallableMethodInvocations do not contain the
		 * target method invocation the test needs some adjustment. For the moment,
		 * simply raise an exception.
		 */
		if (!directlyCallableMethodInvocations.stream()
				.anyMatch(mi -> mi.equals(carvedExecution.methodInvocationUnderTest))) {
			throw new CarvingException("Target method invocation " + carvedExecution.methodInvocationUnderTest
					+ "is not directly visible ");
		}
		/*
		 * Validation: If the directlyCallableMethodInvocations contain private methods,
		 * that's an error
		 */
		for (MethodInvocation directlyCallableMethodInvocation : directlyCallableMethodInvocations) {
			if (directlyCallableMethodInvocation.isPrivate()) {
				// TODO Describe
				throw new CarvingException("Carved tests cannot make use of PRIVATE methods!");
			}
		}

		/*
		 * Sort the method invocations by their original order of execution. This
		 * represents the body of the test
		 */
		Collections.sort(directlyCallableMethodInvocations);

		/*
		 * Generate the structures required for test synthesis. MOVE THIS TO AN EXTERNAL
		 * METHOD MAYBE?
		 */
		for (MethodInvocation methodInvocation : directlyCallableMethodInvocations) {
			executionFlowGraph.enqueueMethodInvocations(methodInvocation);
			// Fix the data deps
			dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocation);
			if (!methodInvocation.isStatic()) {
				dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, methodInvocation.getOwner());
			}
			if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())) {
				dataDependencyGraph.addDataDependencyOnReturn(methodInvocation, methodInvocation.getReturnValue());
			}

			ListIterator<DataNode> it = methodInvocation.getActualParameterInstances().listIterator();
			while (it.hasNext()) {
				int position = it.nextIndex();
				DataNode parameter = it.next();
				dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocation, parameter, position);
			}
		}

		// If the method is not exceptional (but this might not be 100% correct enables
		// spies if needed
		if (!methodInvocationUnderTest.isExceptional()) {
			/*
			 * SPY or not SPY
			 */

			if (!directlyCallableMethodInvocations.contains(methodInvocationUnderTest)) {
				// SPY
				throw new CarvingException(
						"Method Invocation under test is not visible in the carved tests. This requires SPY-ing, which not yet implemented!");
			} else {
				//
				// TODO Not yet there.... We focus primarily on exceptional behaviors
			}
		}

		//
		CarvedTest carvedTest = null;

		if (methodInvocationUnderTest.isExceptional()) {
			// TODO Move the following logic into separated methods. Probably an assertion
			// generator of some sort
			/*
			 * If the execution was exceptional, we need to include try-fail + catch-pass
			 * exception code + catch-fail ... This requires to MUD execution flow graph
			 * with branching... This requires us to implement a ControlFlow graph. Or maybe
			 * we can take a short cut and create "Synthetic instructions" instead? The
			 * control flow of a unit test should be linear...
			 */

			// Create the String message for the fail operation.
			final String defaultFailMessage = "Expected exception was not thrown!";
			DataNode defaultFailMessageNode = DataNodeFactory.get("java.lang.String",
					Arrays.toString(defaultFailMessage.getBytes()));

			// Create the invocation to "fail" with message in case.
			int nextMethod = executionFlowGraph.getLastMethodInvocation().getInvocationCount() + 1;
			MethodInvocation defaultFailMethodInvocation = new MethodInvocation(nextMethod,
					SyntheticMethodSignatures.FAIL_WITH_MESSAGE);
			defaultFailMethodInvocation.setPublic(true);
			defaultFailMethodInvocation.setStatic(true);
			defaultFailMethodInvocation.setSyntheticMethod(true);

			// Attach the parameter to the method invocation. This is a quite dangerous
			// duplication of information. We should probably rely only and only on the
			// graphs !
			List<DataNode> actualParameterInstances = new ArrayList<>();
			actualParameterInstances.add(defaultFailMessageNode);
			defaultFailMethodInvocation.setActualParameterInstances(actualParameterInstances);

			// Add the execution by the end of the test
			executionFlowGraph.enqueueMethodInvocations(defaultFailMethodInvocation);

			// Make sure we include the data dependencies on the string
			dataDependencyGraph.addMethodInvocationWithoutAnyDependency(defaultFailMethodInvocation);
			dataDependencyGraph.addDataDependencyOnActualParameter(defaultFailMethodInvocation, defaultFailMessageNode,
					0);

			// How do we tell that we want to catch that specific exception?
			ObjectInstance expectedException = methodInvocationUnderTest.getRaisedException();

			///

			ExecutionFlowGraph expectedExceptionCatchBlockExecutionFlowGraph = new ExecutionFlowGraphImpl();
			DataDependencyGraph expectedExceptionCatchBlockDataDependencyGraph = new DataDependencyGraphImpl();

			// If we reach the following block an exception was raised but this is not the
			List<String> expectedExceptions = new ArrayList<>();
			expectedExceptions.add(expectedException.getType());
			CatchBlock catchExpectedException = new CatchBlock(expectedExceptions,
					expectedExceptionCatchBlockExecutionFlowGraph, expectedExceptionCatchBlockDataDependencyGraph);

			// There's no such a thing like PASS for the test. TODO Add other assertions if
			// needed, cause, message, etc.

			// one we expected
			ExecutionFlowGraph unexpectedExceptionCatchBlockExecutionFlowGraph = new ExecutionFlowGraphImpl();
			DataDependencyGraph unexpectedExceptionCatchBlockDataDependencyGraph = new DataDependencyGraphImpl();

			// Create the String message for the fail operation.
			final String unexpectedExceptionFailMessage = "A unexpected exception was thrown!";
			DataNode unexpectedExceptionFailMessageNode = DataNodeFactory.get("java.lang.String",
					Arrays.toString(unexpectedExceptionFailMessage.getBytes()));

			// Create the invocation to "fail" with message in case. This is the first
			// invocation inside the catch block
			int methodCount = 0;
			MethodInvocation failMethodInvocation = new MethodInvocation(methodCount,
					SyntheticMethodSignatures.FAIL_WITH_MESSAGE);
			failMethodInvocation.setPublic(true);
			failMethodInvocation.setStatic(true);
			failMethodInvocation.setSyntheticMethod(true);
			//
			List<DataNode> actualParameterInstances2 = new ArrayList<DataNode>();
			actualParameterInstances2.add(unexpectedExceptionFailMessageNode);
			// TODO Get rid of data duplication between nodes and graphs...
			failMethodInvocation.setActualParameterInstances(actualParameterInstances2);

			// Methods
			unexpectedExceptionCatchBlockExecutionFlowGraph.enqueueMethodInvocations(failMethodInvocation);
			// Data
			unexpectedExceptionCatchBlockDataDependencyGraph
					.addMethodInvocationWithoutAnyDependency(failMethodInvocation);
			unexpectedExceptionCatchBlockDataDependencyGraph.addDataDependencyOnActualParameter(failMethodInvocation,
					unexpectedExceptionFailMessageNode, 0);

			List<String> unexpectedExceptions = new ArrayList<>();
			unexpectedExceptions.add("java.lang.Exception");
			CatchBlock catchUnexpectedException = new CatchBlock(unexpectedExceptions,
					unexpectedExceptionCatchBlockExecutionFlowGraph, unexpectedExceptionCatchBlockDataDependencyGraph);

			boolean containsAndroidMethodInvocations = executionFlowGraph.getOrderedMethodInvocations().stream()
					.anyMatch(methodInvocation -> methodInvocation instanceof AndroidMethodInvocation);
			// Patch to enable Android Specific extensions. Probably we need a better way to
			// do so
			// TODO Note that the method invocation is not the ONLY way to eastablish
			// whether or not the test case android related...
			if (containsAndroidMethodInvocations) {
				/*
				 * carvedTest = new AndroidCarvedTest(methodInvocationUnderTest,
				 * unexpectedExceptionCatchBlockExecutionFlowGraph,
				 * unexpectedExceptionCatchBlockDataDependencyGraph);
				 */
				carvedTest = new AndroidCarvedTest(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph,
						catchExpectedException, catchUnexpectedException);
			} else {
				carvedTest = new CarvedTest(methodInvocationUnderTest, //
						executionFlowGraph, dataDependencyGraph, // Test Body + Fail
						catchExpectedException, catchUnexpectedException);
			}
		} else {
			if (methodInvocationUnderTest instanceof AndroidMethodInvocation) {
				carvedTest = new AndroidCarvedTest(methodInvocationUnderTest, //
						executionFlowGraph, dataDependencyGraph);
			} else {
				carvedTest = new CarvedTest(methodInvocationUnderTest, //
						executionFlowGraph, dataDependencyGraph);
			}
		}

		return carvedTest;
	}

}
