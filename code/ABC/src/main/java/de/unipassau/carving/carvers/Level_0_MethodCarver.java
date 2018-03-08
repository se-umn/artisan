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

	public Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
	}

	public Pair<ExecutionFlowGraph, DataDependencyGraph> level0TestCarving(MethodInvocation methodInvocation) {
		// CArving: start from MUT
		// Include all the invocations that happen before (Execution graph) -
		// this excludes methods that happened after MUT
		// NOTE: DataNode Dependency is not directed ! It links instances with
		// method invocations !
		// Include all the invocations that have are transitively reached via
		// data dependency from MUT (DataNode dependency) - this excludes main
		// and
		// caller methods, but includes also methods which produce parameters
		// that are used somewhere in the chain
		// Exclude all the invocations the are subsumed, i.e., that are
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
