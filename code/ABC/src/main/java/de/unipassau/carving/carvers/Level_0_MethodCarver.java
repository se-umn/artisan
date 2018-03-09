package de.unipassau.carving.carvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import de.unipassau.carving.CallGraph;
import de.unipassau.carving.DataDependencyGraph;
import de.unipassau.carving.ExecutionFlowGraph;
import de.unipassau.carving.MethodCarver;
import de.unipassau.carving.MethodInvocation;
import de.unipassau.carving.ObjectInstance;
import de.unipassau.data.Pair;

public class Level_0_MethodCarver implements MethodCarver {

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
			MethodInvocation methodInvocationToCarve) {

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

		// Accumulate all the calls which return the owner instance of MUT and
		// its parameters
		// TODO This shall be extended to recursively include the others.
		Map<ObjectInstance, Set<MethodInvocation>> returningCalls = new HashMap<>();
		// This include "this" and all the parameters of MUT
		// But does not yet include transitive data dependencies to create
		// them..
		returningCalls.put(
				dataDependencyGraph.getOwnerFor( methodInvocationToCarve), 
				dataDependencyGraph.getMethodInvocationsWhichReturn(dataDependencyGraph.getOwnerFor( methodInvocationToCarve)));
		//
		for (ObjectInstance dataDependency : dataDependencyGraph.getParametersOf(methodInvocationToCarve)) {
			returningCalls.put(dataDependency, dataDependencyGraph.getMethodInvocationsWhichReturn(dataDependency));
		}

		// Compute the Cartesian Product of those calss per
		// ObjectInstance/DataDependency
		/// FIXME Ideally, for every new call we need to include its necessary preconditions !!!
		//
		for (List<MethodInvocation> combinations : Sets.cartesianProduct(new ArrayList<>(returningCalls.values()))) {
			carvedTestsForMethodInvocation.add(generateSingleTestCaseFromSlice(backwardSlice, combinations));
		}

		return carvedTestsForMethodInvocation;
	}

	private Pair<ExecutionFlowGraph, DataDependencyGraph> generateSingleTestCaseFromSlice(
			Set<MethodInvocation> backwardSlice, List<MethodInvocation> dataReturningCalls) {

		// Create a local copy of the backwardSlide. This is the "minimal slice"
		Set<MethodInvocation> _backwardSlice = new HashSet<>(backwardSlice);
		// Explicitly Include the returning calls

		// FIXME This might require to recursively call carve method on them ?
		// because they might require
		// additional data to be inplace?
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
			carvedTests.addAll(level0TestCarving(methodInvocationUnderTest));
		}
		return carvedTests;
	}
}
