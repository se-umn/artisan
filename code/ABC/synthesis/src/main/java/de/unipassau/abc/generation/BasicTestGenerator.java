package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import de.unipassau.abc.evaluation.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.CallGraph;
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
import de.unipassau.abc.generation.assertions.AssertionGenerator;
import de.unipassau.abc.generation.assertions.CarvingAssertion;
import de.unipassau.abc.generation.assertions.NullValueAssertionGenerator;
import de.unipassau.abc.generation.assertions.PrimitiveValueAssertionGenerator;
import de.unipassau.abc.generation.data.AndroidCarvedTest;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.data.CatchBlock;
import de.unipassau.abc.generation.mocks.MockGenerator;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.TraceParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.StaticFieldRef;

public class BasicTestGenerator implements TestGenerator {

	private final static Logger logger = LoggerFactory.getLogger(BasicTestGenerator.class);

	@Override
	public Collection<CarvedTest> generateTests(List<MethodInvocation> targetMethodsInvocations, ParsedTrace trace)
			throws CarvingException, ABCException {
		// NOT EASY TO FIND DUPLICATES :)
		Collection<CarvedTest> carvedTests = new ArrayList<>();

		/*
		 * Extract only the necessary invocations from the trace, using direct
		 * data-dependencies
		 */
		BasicCarver carver = new BasicCarver(trace);

		for (MethodInvocation targetMethodInvocation : targetMethodsInvocations) {
			// TODO When carving multiple targes we can use
			// carver.carve(List<methodInvocations>), this invalidate automatically
			// the cache
			carver.clearTheCache();

			for (CarvedExecution carvedExecution : carver.carve(targetMethodInvocation)) {
				try {
					logger.info("Carving " + targetMethodInvocation);
					carvedTests.add(generateCarvedTestFromCarvedExecution(carvedExecution));
					logger.info("Done Carving " + targetMethodInvocation);
				}
				catch(Exception e){
					// TODO May be too much
					logger.error("Error Carving " + targetMethodInvocation, e);
				}
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

		MethodInvocation methodInvocationUnderTest = carvedExecution.methodInvocationUnderTest;
		//logging
		logger.info("Method invocation under test:");
		logger.info(methodInvocationUnderTest.toString());
		logger.info("END");

		/*
		 * The carved execution contains a collection of (complete) call graphs, but in
		 * the test we can only directly invoke the root(s) of those graphs, as all the
		 * other invocations will be automatically executed (unless we breakpoint the
		 * execution?).
		 *
		 */
		final List<MethodInvocation> directlyCallableMethodInvocations = new ArrayList<>();
		carvedExecution.callGraphs.forEach(callGraph -> directlyCallableMethodInvocations.addAll(callGraph.getRoots()));
		//logging
		logger.info("Directly callable method invocations:");
		for(MethodInvocation mi:directlyCallableMethodInvocations){
			logger.info(mi.toString());
		}
		logger.info("END");

		/*
		 * Validation: If the directlyCallableMethodInvocations do not contain the
		 * target method invocation the test needs some adjustment. For the moment,
		 * simply raise an exception.
		 */
		boolean needsFix = false;
		if (!directlyCallableMethodInvocations.stream()
				.anyMatch(mi -> mi.equals(carvedExecution.methodInvocationUnderTest))) {

			logger.info("Method invocation under test " + carvedExecution.methodInvocationUnderTest
					+ " is not visible. Try to recover ...");
			needsFix = true;

			// Try to recover this test? Or maybe this is an actual feature of the CARVER ?
			/*
			 * Recovery works as follow. Recursively replace the invocation of a parent
			 * method with its execution until the mehtod invocation under test becomes
			 * visible again. Next, remove the "un-necessary" method invocations from the
			 * carve execution.
			 */

			CallGraph callGraph = carvedExecution.getCallGraphContainingTheMethodInvocationUnderTest();

			// Reverse iteration
			List<MethodInvocation> methodsSubsumingMethodInvocationUnderTest = callGraph
					.getOrderedSubsumingMethodInvocationsFor(carvedExecution.methodInvocationUnderTest);
			//logging
			logger.info("Subsuming method invocations:");
			for(MethodInvocation mi:methodsSubsumingMethodInvocationUnderTest){
				logger.info(mi.toString());
			}
			logger.info("END");


			ListIterator<MethodInvocation> listIterator = methodsSubsumingMethodInvocationUnderTest
					.listIterator(methodsSubsumingMethodInvocationUnderTest.size());

			while (listIterator.hasPrevious()) {
				MethodInvocation methodInvocationToReplace = listIterator.previous();
				callGraph.replaceMethodInvocationWithExecution(methodInvocationToReplace);
			}

			// TODO FIXME I suspect that at this point we lost the information on relevant
			// invocations because the methodInvocationUnderTest is not listed among the
			// relevant method invocations ?!

			/*
			 * At this point we should get rid of the method invocations that became visible
			 * after replacing their caller but are not strictly necessary for carving, but
			 * have been included only because subsumed
			 */

			// THIS PROBABLY CAN BE DONE WITH SOME reduce/map magic...
			List<MethodInvocation> unecessaryMethodInvocations = callGraph.getRoots().stream()
					.filter(mi -> !mi.isNecessary()).collect(Collectors.toList());
			//logging
			logger.info("Unnecessary method invocations:");
			for(MethodInvocation mi:unecessaryMethodInvocations){
				logger.info(mi.toString());
			}
			logger.info("END");
			for (MethodInvocation removeMe : unecessaryMethodInvocations) {
				/*
				 * Remove unnecessary method invocations and all the method invocations that are
				 * subsumed from it
				 */
				callGraph.remove(removeMe);
			}

		}

		// Check if, after the recovery, the method under test is still not visible...
		directlyCallableMethodInvocations.clear();
		carvedExecution.callGraphs.forEach(callGraph -> directlyCallableMethodInvocations.addAll(callGraph.getRoots()));
		if(needsFix){
			//logging
			logger.info("New directly callable method invocations:");
			for (MethodInvocation mi : directlyCallableMethodInvocations) {
				logger.info(mi.toString());
			}
			logger.info("END");
		}

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
				throw new CarvingException(
						"Carved tests cannot make use of PRIVATE methods! Offending method invocation: "
								+ directlyCallableMethodInvocation);
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
			/*
			 * If the method invocation is exceptional, there's no return type there !
			 */
			if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())
					&& !methodInvocation.isExceptional()) {
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
			}
		}

		//
		CarvedTest carvedTest = null;

		if (methodInvocationUnderTest.isExceptional()) {
			// This is basically generating assertions...
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
			MethodInvocation defaultFailMethodInvocation = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, nextMethod,
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
			MethodInvocation failMethodInvocation = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, methodCount,
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
			// We use Throwable to fix !97. It should be ok, in 99% of the cases, since
			// application methods do not usually throw "Throwable"		
			unexpectedExceptions.add("java.lang.Throwable");
			CatchBlock catchUnexpectedException = new CatchBlock(unexpectedExceptions,
					unexpectedExceptionCatchBlockExecutionFlowGraph, unexpectedExceptionCatchBlockDataDependencyGraph);

			boolean containsAndroidMethodInvocations = containsAndroidMethodInvocations(executionFlowGraph,
					carvedExecution.callGraphs);

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

			// Include assertions here
			boolean containsAndroidMethodInvocations = containsAndroidMethodInvocations(executionFlowGraph,
					carvedExecution.callGraphs);
			if (containsAndroidMethodInvocations) {
				carvedTest = new AndroidCarvedTest(methodInvocationUnderTest, //
						executionFlowGraph, dataDependencyGraph);
			} else {
				carvedTest = new CarvedTest(methodInvocationUnderTest, //
						executionFlowGraph, dataDependencyGraph);
			}
			// Add the assertions using the AssertionGenerationPipeline (basically, add all
			// the assertions that can be added).
			if (!JimpleUtils.hasVoidReturnType(carvedTest.getMethodUnderTest().getMethodSignature())) {
				// TODO We might not be able to handle the case of a NULL string since we
				// consider String as primitive type, and primitives cannot be null...
				if (JimpleUtils
						.isPrimitive(JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature())) ||
				//
						JimpleUtils.isString(
								JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))) {
					AssertionGenerator assertionGenerator = new PrimitiveValueAssertionGenerator();

					CarvingAssertion returnValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
							carvedExecution);

					carvedTest.addAssertion(returnValueAssertion);

				} else if (JimpleUtils
						.isArray(JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))) {
					logger.warn(
                            "Implementing assertions over array is not yet implemented. There will not be assertions for method under test: "
                                    + carvedTest.getMethodUnderTest());
					//throw new NotImplementedException("Assertions over arrays not yet implemented");
				} else {
					// Those are regular objects

					// Assert whether the return value is null/not-null
					AssertionGenerator assertionGenerator = new NullValueAssertionGenerator();
					CarvingAssertion nullValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
							carvedExecution);
					carvedTest.addAssertion(nullValueAssertion);

					// Assert the value for boxed types that are NOT null
					if (JimpleUtils.isBoxedPrimitive(
							JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))
							&& !((ObjectInstance) carvedExecution.methodInvocationUnderTest.getReturnValue())
									.isNull()) {
						CarvingAssertion returnValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
								carvedExecution);
						carvedTest.addAssertion(returnValueAssertion);
					}

					// Assert the value for String types that are NOT null
					if (JimpleUtils
							.isString(JimpleUtils.getReturnType(carvedTest.getMethodUnderTest().getMethodSignature()))
							&& !((ObjectInstance) carvedExecution.methodInvocationUnderTest.getReturnValue())
									.isNull()) {
						CarvingAssertion returnValueAssertion = assertionGenerator.generateAssertionsFor(carvedTest,
								carvedExecution);
						carvedTest.addAssertion(returnValueAssertion);
					}
				}
			}
		}

     	MockGenerator mockGenerator = new MockGenerator();
     	mockGenerator.generateMocks(carvedTest, carvedExecution);

		return carvedTest;
	}

	/**
	 * For each method invocation that we call explicitly from the execution flow
	 * graph, we check if it or any of the subsumed method invocations belongs to
	 * android
	 *
	 * @param executionFlowGraph
	 * @param callGraphs
	 * @return
	 */
	private boolean containsAndroidMethodInvocations(ExecutionFlowGraph executionFlowGraph,
			Collection<CallGraph> callGraphs) {
		for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
			// Check if any of the explicitly called mi's is an android method invocation
			if (mi instanceof AndroidMethodInvocation) {
				return true;
			}

			// Check if some of the invocations subsumed by mi is an android one
			for (CallGraph callGraph : callGraphs) {
				if (callGraph.getAllMethodInvocations().contains(mi)) {
					boolean containsAndroidMethodInvocations = callGraph.getMethodInvocationsSubsumedBy(mi).stream()
							.anyMatch(subMi -> subMi instanceof AndroidMethodInvocation);

					if (containsAndroidMethodInvocations) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
