package de.unipassau.carving.carvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unipassau.carving.CallGraph;
import de.unipassau.carving.DataDependencyGraph;
import de.unipassau.carving.ExecutionFlowGraph;
import de.unipassau.carving.MethodCarver;
import de.unipassau.carving.MethodInvocation;
import de.unipassau.data.Pair;

public class Level_0_MethodCarver implements MethodCarver {

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	/*
	 * This control how we deal with methods which returns objects instances
	 * needed by the carved methods
	 */
	private boolean expandCalls;

	/**
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @param callGraph
	 * @param expandCalls
	 */
	public Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph, boolean expandCalls) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
		this.expandCalls = expandCalls;
	}

	/**
	 * By default expand the calls
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @param callGraph
	 */
	public Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph) {
		this(executionFlowGraph, dataDependencyGraph, callGraph, true);
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
	 * @param methodInvocation
	 * @return
	 */

	public Pair<ExecutionFlowGraph, DataDependencyGraph> level0TestCarving(MethodInvocation methodInvocation) {
		// CArving: start from MUT
		// 1 - Include all the invocations that happen before (Execution graph)
		// -
		// this excludes methods that happened after MUT

		// 2 - Create a data dependency sub graph which contains only the Before
		// method and

		// 3 - IF expandCalls==true THEN
		// 3.1 - include all the invocations that have are
		// transitively reached via data dependency no matter in which previous
		// method are those called.
		// For example, using a factory to create object instances will result
		// in calling the actual methods
		// inside the factory instead of calling the factory itself. TODO Link
		// this on GitLab
		// ELSE
		// 3.2 Call directly the method which
		// caller methods, but includes also methods which produce parameters ?
		// that are used somewhere in the chain

		// 4 - Finally, exclude all the invocations the are subsumed by any
		// invocation already considered, i.e., that are
		// reachable by the transitive closure of the call graph of any method
		// (Call Graph)

		// Carve by Time => "Before"
		List<MethodInvocation> executioneBefore = executionFlowGraph
				.getOrderedMethodInvocationsBefore(methodInvocation);
		// Extract relevant Data Dependencies from "Before"
		DataDependencyGraph subGraphFromPastExecution = dataDependencyGraph.getSubGraph(executioneBefore);

		// Do the carving by following the method invocations chain
		Set<MethodInvocation> backwardSlice = new HashSet<>(executioneBefore);
		backwardSlice.retainAll(subGraphFromPastExecution.getMethodInvocationsRecheableFrom(methodInvocation));

		// Eventually remove the subsumed Calls
		Set<MethodInvocation> subsumedCalls = new HashSet<>();
		for (MethodInvocation mi : backwardSlice) {
			// Filter out the calls that are subsumed by the call graph
			subsumedCalls.addAll(callGraph.getMethodInvocationsSubsumedBy(mi));
		}
		// Delete from the slice all those calls that will be done nevertheless
		// ...
		backwardSlice.removeAll(subsumedCalls);
		List<MethodInvocation> carvedTestCase = new ArrayList<>(backwardSlice);
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
		// We need to build a subgraph of the DataDependecyGraph which contains
		// the data fro ExecutionFlow

		// ArrayList<String> list = new ArrayList<String>(testCaseInstructions);
		// Collections.copy(list, testCaseInstructions);
		// NOT SURE WHAYT externalDependencyList
		// for (String s : list) {
		// for (String external : externalDependencyList) {
		// if (s.contains(external)) {
		//
		// if (!externalTestCases.isEmpty()) {
		//
		// testCaseInstructions.addAll(0, externalTestCases);
		// }
		// }
		// }
		// }
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
			carvedTests.add(level0TestCarving(methodInvocationUnderTest));
		}
		return carvedTests;
	}
}
