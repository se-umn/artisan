package de.unipassau.utils;

import java.util.List;

import com.google.common.hash.HashCode;

import de.unipassau.carving.GraphNode;
import de.unipassau.carving.MethodInvocation;
import edu.uci.ics.jung.graph.Graph;

public class GraphUtility {

	public static boolean areDataDependencyGraphsEqual(Graph<GraphNode, String> graph, Graph<GraphNode, String> graph2) {

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
		System.out.println("GraphUtility.areExecutionGraphsEqual() \n"
				+ "1: " + graph + "\n"
				+ "2: " + graph2 );
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


	public static int getHashCodeFor(List<MethodInvocation> orderedMethodInvocation) {
		System.out.println("GraphUtility.getHashCodeFor() " + orderedMethodInvocation );
		StringBuffer sb = new StringBuffer();
		for( MethodInvocation mi : orderedMethodInvocation){
			sb.append(mi.hashCode());
		}
		return HashCode.fromString( sb.toString() ).asInt();
	}

}
