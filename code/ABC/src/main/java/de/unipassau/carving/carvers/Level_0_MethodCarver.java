package de.unipassau.carving.carvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.unipassau.carving.CallGraph;
import de.unipassau.carving.DataDependencyGraph;
import de.unipassau.carving.ExecutionFlowGraph;
import de.unipassau.carving.MethodCarver;
import de.unipassau.carving.MethodInvocation;
import de.unipassau.carving.ObjectInstance;
import de.unipassau.data.Pair;

public class Level_0_MethodCarver implements MethodCarver {

	private final Logger logger = LoggerFactory.getLogger(Level_0_MethodCarver.class);

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	/**
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @param callGraph
	 */
	public Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
	}

	/**
	 * 
	 * Level 0 means that we cannot invoke any primate method or rely on
	 * knowledge about the internals of the CUT
	 * 
	 * We generate several test cases for the same MUT if the parameters comes
	 * from different level (see https://en.wikipedia.org/wiki/Law_of_Demeter)
	 * up to the level of the current MUT ?
	 * 
	 * The point is: a parameter is generated and modified at some location, and
	 * we need to get it. We can either replicate all the invocations on it (and
	 * the ones needed there) but also we can go up one level, and invoke
	 * whatever method provided that instance (at last) as return value
	 * 
	 * We create a new test for method which return any parameters handled by
	 * MUT. This can be simply done by including that method before removing
	 * subsumed calls
	 * 
	 * @param methodInvocationToCarve
	 * @return
	 */

	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> level0TestCarving(
			MethodInvocation methodInvocationToCarve, boolean minimalCarve) {
		/*
		 * Not sure "minimal" is the right term
		 * 
		 */
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestsForMethodInvocation = new ArrayList<>();
		// CArving: start from MUT
		// 1 - Include all the invocations that happen before (Execution graph)
		// -
		// this excludes methods that happened after MUT

		// 2 - Create a data dependency sub graph which contains only the Before
		// method and

		// 3 - include all the invocations that have are
		// transitively reached via data dependency no matter in which previous
		// method are those called.
		// For example, using a factory to create object instances will result
		// in calling the actual methods
		// inside the factory instead of calling the factory itself. TODO Link
		// this on GitLab

		// 4 -Exclude all the invocations the are subsumed by any
		// invocation already considered, i.e., that are
		// reachable by the transitive closure of the call graph of any method
		// (Call Graph)

		// At this point, for each object instance we expand the set of carved
		// test by including one by one the methods which return them, starting
		// from the ones at deeper level in the call graph

		// Carve by Time => "Before"
		List<MethodInvocation> executioneBefore = executionFlowGraph
				.getOrderedMethodInvocationsBefore(methodInvocationToCarve);
		// Extract relevant Data Dependencies from "Before"
		DataDependencyGraph subGraphFromPastExecution = dataDependencyGraph.getSubGraph(executioneBefore);

		// Do the carving by following the method invocations chain
		Set<MethodInvocation> backwardSlice = new HashSet<>(executioneBefore);
		backwardSlice.retainAll(subGraphFromPastExecution.getMethodInvocationsRecheableFrom(methodInvocationToCarve));

		// The backwardSlice is the slice which contains the MUT-centric view of
		// the execution. It disregards the location of methods which have
		// potential impact on the CUT.

		// The first carved tests considers only the direct constructors
		// It ENSURES that one test is carved !
		List<MethodInvocation> constructors = new ArrayList<>();
		// THIS - Unless is a static call
		if (!methodInvocationToCarve.isStatic()) {
			constructors.add(dataDependencyGraph
					.getInitMethodInvocationFor(dataDependencyGraph.getOwnerFor(methodInvocationToCarve)));
		}
		// DIRECT PARAMETERS
		for (ObjectInstance dataDependency : dataDependencyGraph
				.getObjectInstancesAsParametersOf(methodInvocationToCarve)) {
			constructors.add(dataDependencyGraph.getInitMethodInvocationFor(dataDependency));
		}

		// Generate ONLY the first carved test
		Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTestFromConstructors = generateSingleTestCaseFromSlice(
				backwardSlice, constructors);
		//
		if (minimalCarve) {
			carvedTestsForMethodInvocation.add(carvedTestFromConstructors);
			return carvedTestsForMethodInvocation;
		}

		Map<ObjectInstance, Set<MethodInvocation>> returningCalls = new HashMap<>();

		// Now find all the calls which returns any of the objectInstances
		// in the data dependency graph
		for (ObjectInstance objectInstance : subGraphFromPastExecution.getObjectInstances()) {

			//
			Set<MethodInvocation> returningCallsForObjectInstance = dataDependencyGraph
					.getMethodInvocationsWhichReturn(objectInstance);
			// Here we need to include the constructors as well to create the
			// cross products
			returningCallsForObjectInstance.add(dataDependencyGraph.getInitMethodInvocationFor(objectInstance));

			if (returningCallsForObjectInstance.isEmpty()) {
				throw new RuntimeException("Object Instance " + objectInstance + " has no returning calls !");
			}

			returningCalls.put(objectInstance, returningCallsForObjectInstance);

		}

		// And generate the possible combinations by computing the Cartesian
		// Product of those calls per. For each call generate a new carvedTest

		Set<List<MethodInvocation>> fullCartesianProduct = Sets
				.cartesianProduct(new ArrayList<>(returningCalls.values()));

		for (List<MethodInvocation> combination : fullCartesianProduct) {
			carvedTestsForMethodInvocation.add(generateSingleTestCaseFromSlice(backwardSlice, combination));
		}

		return carvedTestsForMethodInvocation;
	}

	private Pair<ExecutionFlowGraph, DataDependencyGraph> generateSingleTestCaseFromSlice(
			Set<MethodInvocation> backwardSlice, List<MethodInvocation> dataReturningCalls) {

		// System.out.println("\n\n\n
		// Level_0_MethodCarver.generateSingleTestCaseFromSlice() From " +
		// dataReturningCalls);

		// Create a local copy of the backwardSlide. This is the "minimal slice"
		Set<MethodInvocation> _backwardSlice = new HashSet<>(backwardSlice);
		// Explicitly Include the returning calls

		// At any time if a call was not yet included
		/// in original backwardSlide we carve with minimal carve of that call !
		// This way we ensure that all the preconditions for that call are
		/// included as well...
		// We adopt the naive solution of calling "carve" for each, every time.
		/// This of course can be improved by caching the carved test for those
		/// calls as well.
		// Another possibility might be to define carved test as carve(of carve(
		/// of carve())) for each method invocation encountered in the first
		/// test carved
		// TODO This probably should be called OUTSIDE !!! TO AVOID USELESS
		// COMPUTATION FOR THE FIRST TEST CASE?
		// Ensure preconditions
		for (MethodInvocation returnCall : dataReturningCalls) {
			if (!backwardSlice.contains(returnCall)) {
				// System.out
				// .println("Level_0_MethodCarver.generateSingleTestCaseFromSlice()
				// Must Ensure preconditions of "
				// + returnCall);

				if (dataDependencyGraph.getOwnerFor(returnCall) != null || //
						!dataDependencyGraph.getObjectInstancesAsParametersOf(returnCall).isEmpty()) {

					Pair<ExecutionFlowGraph, DataDependencyGraph> carvedPreconditions = level0TestCarving(returnCall,
							true).get(0);
					// Here we do not really care about data dep, since the
					// later
					// call should include them as well !
					// System.out.println("Level_0_MethodCarver.generateSingleTestCaseFromSlice()
					// Preconditions are : "
					// +
					// carvedPreconditions.getFirst().getOrderedMethodInvocations());
					// At this point we need to recompute the
					// dataDependencyGraph to include any deps on preconditions
					// Which basically consist on carving them ?
					_backwardSlice.addAll(carvedPreconditions.getFirst().getOrderedMethodInvocations());
				} else {
					logger.debug("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Method " + returnCall
							+ " has no preconditions");
				}
			}
		}

		_backwardSlice.addAll(dataReturningCalls);

		// Eventually remove the subsumed Calls
		Set<MethodInvocation> subsumedCalls = new HashSet<>();
		for (MethodInvocation mi : _backwardSlice) {
			// Filter out the calls that are subsumed by the call graph
			subsumedCalls.addAll(callGraph.getMethodInvocationsSubsumedBy(mi));
		}
		// Delete from the slice all those calls that will be done nevertheless
		_backwardSlice.removeAll(subsumedCalls);
		List<MethodInvocation> carvedTestCase = new ArrayList<>(_backwardSlice);
		// Order by invocation
		Collections.sort(carvedTestCase, new Comparator<MethodInvocation>() {
			@Override
			public int compare(MethodInvocation o1, MethodInvocation o2) {
				return o1.getInvocationCount() - o2.getInvocationCount();
			}
		});

		ExecutionFlowGraph carvedExecutionGraph = new ExecutionFlowGraph();
		for (MethodInvocation mi : carvedTestCase) {
			carvedExecutionGraph.enqueueMethodInvocations(mi);
		}

		// This is the original graph
		DataDependencyGraph carvedDataDependencyGraph = dataDependencyGraph.getSubGraph(carvedExecutionGraph);

		return new Pair<ExecutionFlowGraph, DataDependencyGraph>(carvedExecutionGraph, carvedDataDependencyGraph);
	}

	/**
	 * methodToBeCarved in Jimple format
	 * 
	 * @param methodToBeCarved
	 * @return
	 */
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(String methodToBeCarved) {
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

		// TODO: handle here the reg exp or use the execution Flow Graph instead

		for (MethodInvocation methodInvocationUnderTest : executionFlowGraph
				.getMethodInvocationsFor(methodToBeCarved)) {

			carvedTests.addAll(level0TestCarving(methodInvocationUnderTest, false));

			// Simplify the carved tests. Following the data dependencies we
			// might
			// take into tests, invocations that are there but are independent
			// from
			// the CUT/MUT. We identify them as NON connected by means of data
			// dependencies.
			// The caveut is: if ANY of those involves calls to "external"
			// libraries, we must keep them.

			// FIXME RHandle external libraries
			simplify(methodInvocationUnderTest, carvedTests);
		}

		return carvedTests;
	}

	private void simplify(MethodInvocation methodInvocationUnderTest,
			List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests) {
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			Set<MethodInvocation> connectedMethodInvocations = carvedTest.getSecond()
					.getWeaklyConnectedComponentContaining(methodInvocationUnderTest);
			// Remove from the execution graph and data dependency graph all the
			// invocations and object instances that are not connected in any
			// way to methodInvocation, unless they below (or contain?) calls to external libraries?
			carvedTest.getFirst().refine(connectedMethodInvocations);
			carvedTest.getSecond().refine(connectedMethodInvocations);
		}

	}
}
