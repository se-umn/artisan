package de.unipassau.abc.utils;

import java.util.List;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.GraphNode;
import de.unipassau.abc.data.MethodInvocation;
import edu.uci.ics.jung.graph.Graph;

public class GraphUtility {

	public static boolean areDataDependencyGraphsEqual(Graph<GraphNode, String> graph,
			Graph<GraphNode, String> graph2) {

		if (graph == null && graph2 == null) {
			return true;
		} else if (graph == null && graph2 != null) {
			return false;
		} else if (graph != null && graph2 == null) {
			return false;
		}
		return graph.getVertices().containsAll(graph2.getVertices()) && //
				graph.getEdges().containsAll(graph2.getEdges()) && //
				//
				graph2.getVertices().containsAll(graph.getVertices()) && //
				graph2.getEdges().containsAll(graph.getEdges());
	}

	public static boolean areExecutionGraphsEqual(Graph<MethodInvocation, String> graph,
			Graph<MethodInvocation, String> graph2) {
		if (graph == null && graph2 == null) {
			return true;
		} else if (graph == null && graph2 != null) {
			return false;
		} else if (graph != null && graph2 == null) {
			return false;
		}
		return graph.getVertices().containsAll(graph2.getVertices()) && //
				graph.getEdges().containsAll(graph2.getEdges()) && //
				//
				graph2.getVertices().containsAll(graph.getVertices()) && //
				graph2.getEdges().containsAll(graph.getEdges());
	}

	// The Jimple sequence is the same, but not necessary the ID of the
	// method invocation
	public static boolean areExecutionGraphsEquivalent(ExecutionFlowGraph first, ExecutionFlowGraph first2) {
		// They must have the same amount
		List<MethodInvocation> orderedMethodInvocation = first.getOrderedMethodInvocations();
		List<MethodInvocation> orderedMethodInvocation2 = first2.getOrderedMethodInvocations();

		if (orderedMethodInvocation.size() != orderedMethodInvocation2.size()) {
			return false;
		}

		for (int index = 0; index < orderedMethodInvocation.size(); index++) {
			if (!orderedMethodInvocation.get(index).getMethodSignature()
					.equals(orderedMethodInvocation2.get(index).getMethodSignature())) {
				return false;
			}
		}

		return true;
	}

	public static boolean areDataDependencyGraphsEquivalent(DataDependencyGraph second, DataDependencyGraph second2) {
		// TODO This way only exec graph matter
		return true;
	}

}
