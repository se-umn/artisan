package de.unipassau.carving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import org.mockito.internal.stubbing.answers.ThrowsException;

import com.google.common.base.Function;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import jas.Method;

public class ExecutionFlowGraph {

	private final AtomicInteger id = new AtomicInteger(0);
	private Graph<MethodInvocation, String> g;

	private MethodInvocation lastMethodInvocation = null;
	private MethodInvocation firstMethodInvocation = null;

	Collection<String> c;
	// Why private variables ?!
	// HashSet<String> hs;
	HashSet<String> hsForSuccessors;
	HashSet<String> hsForEnhancement;
	LinkedList<String> hsForPredecessors;
	private static AtomicInteger countOfMethod = new AtomicInteger(1);

	public ExecutionFlowGraph() {
		g = new SparseMultigraph<MethodInvocation, String>();
		hsForPredecessors = new LinkedList<String>();

	}

	public void enqueueMethodInvocations(MethodInvocation methodInvocation) {
		// MethodInvocation mi = new MethodInvocation(methodInvocation);
		if (!g.containsVertex(methodInvocation)) {
			g.addVertex(methodInvocation);
		}

		if (lastMethodInvocation != null) {
			g.addEdge("ExecutionDependency-" + id.getAndIncrement(), lastMethodInvocation, methodInvocation,
					EdgeType.DIRECTED);
		}
		lastMethodInvocation = methodInvocation;

		if (firstMethodInvocation == null) {
			firstMethodInvocation = methodInvocation;
		}
	}

	public void visualize() {
		VisualizationViewer<MethodInvocation, String> vv = new VisualizationViewer<MethodInvocation, String>(
				new KKLayout<MethodInvocation, String>(g));

		vv.setPreferredSize(new Dimension(1000, 800));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String apply(Object node) {
				if (node instanceof ObjectInstance) {
					return ((ObjectInstance) node).getObjectId();
				} else if (node instanceof MethodInvocation) {
					return ((MethodInvocation) node).getJimpleMethod();
				} else {
					return super.apply(node);
				}
			}
		});
		vv.getRenderContext().setVertexFillPaintTransformer(new Function<GraphNode, Paint>() {
			@Override
			public Paint apply(GraphNode node) {
				if (node instanceof ValueNode) {
					// TODO Not sure we can skip the visualization at all...
					return Color.YELLOW;
				} else if (node instanceof ObjectInstance) {
					return Color.GREEN;
				} else if (node instanceof MethodInvocation) {
					MethodInvocation methodInvocation = (MethodInvocation) node;
					return (methodInvocation.getInvocationType().equals("StaticInvokeExpr")) ? Color.ORANGE : Color.RED;
				} else {
					return Color.BLUE;
				}
			}
		});
		JFrame frame = new JFrame("Execution Flow Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

	// public boolean isPresent(String data) {
	// if (g.containsVertex(data))
	// return true;
	// return false;
	// }

	// /**
	// *
	// * @param nameOfEdge
	// * @param vertex1
	// * @param vertex2
	// */
	// public void edgeAdd(String nameOfEdge, String vertex1, String vertex2) {
	//// System.out.println("ExecutionFlowGraph.edgeAdd() " + nameOfEdge + " " +
	// vertex1 + " " + vertex2 );
	// g.addEdge(nameOfEdge, vertex1, vertex2, EdgeType.DIRECTED);
	// ArrayList<String> v = new ArrayList<String>();
	// v.add(vertex1);
	// v.add(vertex2);
	//
	// }

	// public void printGraph() {
	// System.out.println("The graph g = " + g.toString());
	// }

	// TODO No idea what is this for...
	public Set<String> getParents(MethodInvocation mi) {

		Set<String> hs = new HashSet<String>();
		System.out.println("The vertex is " + mi.getJimpleMethod());

		if (g.containsVertex(mi)) {

			for (MethodInvocation m : g.getPredecessors(mi)) {
				hs.add(m.getJimpleMethod());
			}

			// for(String each:hs)
			// {
			// System.out.println("The predeccessor is " + each);
			// hsForPredecessors.add(each);
			// getParents(each);
			//
			// }

		}
		return hs;
	}

	// Find all the method invocations that match the given JImp
	public Collection<MethodInvocation> getMethodInvocationsFor(String jimpleMethod) {
		ArrayList<MethodInvocation> matching = new ArrayList<>();
		for (MethodInvocation mi : g.getVertices()) {
			if (mi.getJimpleMethod().equals(jimpleMethod)) {
				matching.add(mi);
			}
		}
		return matching;
	}

	/**
	 * Returns all the elements before the provided one including the provided
	 * one. This can be also implemented differently with the index parameter on
	 * invocation.
	 * 
	 * @param methodInvocation
	 * @return
	 */
	public Set<MethodInvocation> getMethodInvocationsBefore(MethodInvocation methodInvocation) {
		return new HashSet<>(getOrderedMethodInvocationsBefore(methodInvocation));
	}

	public List<MethodInvocation> getOrderedMethodInvocationsBefore(MethodInvocation methodInvocation) {
		List<MethodInvocation> predecessorsOf = new ArrayList<>();
		for (MethodInvocation mi : g.getPredecessors(methodInvocation)) {
			predecessorsOf.addAll(getOrderedMethodInvocationsBefore(mi));
		}
		// As last add the mi passed as parameter
		predecessorsOf.add(methodInvocation);

		return predecessorsOf;
	}

	/**
	 * Returns all the elements before the provided one including the provided
	 * one. This can be also implemented differently with the index parameter on
	 * invocation.
	 * 
	 * @param methodInvocation
	 * @return
	 */
	public Set<MethodInvocation> getMethodInvocationsAfter(MethodInvocation methodInvocation) {
		return new HashSet<>(getOrderedMethodInvocationsAfter(methodInvocation));
	}

	public List<MethodInvocation> getOrderedMethodInvocationsAfter(MethodInvocation methodInvocation) {
		try {
			List<MethodInvocation> successorsOf = new ArrayList<>();
			// Add the actual one
			successorsOf.add(methodInvocation);
			// g.getSuccessors(methodInvocation) can return null !? !
			for (MethodInvocation mi : g.getSuccessors(methodInvocation)) {
				// Add the one reach from this one
				successorsOf.addAll(getOrderedMethodInvocationsAfter(mi));
			}
			return successorsOf;
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}

	public MethodInvocation getLastMethodInvocation() {
		return lastMethodInvocation;

	}

	public List<MethodInvocation> getOrderedMethodInvocations() {
		return getOrderedMethodInvocationsAfter(firstMethodInvocation);
	}

	/*
	 * Keep only the invocations that can be found in the provide set
	 */
	public void refine(Set<MethodInvocation> connectedMethodInvocations) {
		Set<MethodInvocation> unconnected = new HashSet<>();
		for (MethodInvocation node : g.getVertices()) {
			if (!connectedMethodInvocations.contains(node)) {
				// NOTE: since we are iterating and removing at the same time,
				// this might create problems
				unconnected.add(node);
			}
		}
		//
		for (MethodInvocation mi : unconnected) {
			System.out.println("ExecutionFlowGraph.refine() Remove " + mi + " as unconnected");
			// We need to connect predecessor and successor before removing this
			// vertex
			Collection<MethodInvocation> predecessors = g.getPredecessors(mi);
			Collection<MethodInvocation> successors = g.getSuccessors(mi);

			if (predecessors == null || predecessors.isEmpty()) {
				// This is the FIRST METHOD INVOCATION - update the reference
				if (successors == null || successors.isEmpty()) {
					// This was als the last one
					firstMethodInvocation = null;
				} else {
					// At least one element (actually, only one)
					firstMethodInvocation = successors.iterator().next();
				}
				System.out.println(
						"ExecutionFlowGraph.refine() Updated firstMethodInvocation to " + firstMethodInvocation);
			}

			if (successors == null || successors.isEmpty()) {
				// This is the last element
				if (predecessors == null || predecessors.isEmpty()) {
					// This is the only element
					lastMethodInvocation = null;
				} else {
					// At least one element (actually, only one)
					lastMethodInvocation = predecessors.iterator().next();
				}
				System.out
						.println("ExecutionFlowGraph.refine() Updated lastMethodInvocation to " + lastMethodInvocation);

			}

			// TODO first and last element can definitively be computed
			// afterwards by following the flow relations

			Collection<String> flowEdges = new HashSet<>(g.getInEdges(mi));
			flowEdges.addAll(g.getOutEdges(mi));

			for (String flowEdge : flowEdges) {
				System.out.println("ExecutionFlowGraph.refine() Removing Edge " + flowEdge);
				g.removeEdge(flowEdge);
			}
			// Really its just one
			for (MethodInvocation predecessor : predecessors) {
				for (MethodInvocation successor : successors) {
					int edgeID = id.getAndIncrement();
					String edgeLabel = "ExecutionDependency-" + edgeID;
					boolean added = g.addEdge(edgeLabel, predecessor, successor, EdgeType.DIRECTED);
					System.out.println("ExecutionFlowGraph.refine() Introducing replacemente edge "
							+ g.getEndpoints(edgeLabel) + " added  " + added);
				}
			}

			g.removeVertex(mi);
			System.out.println("ExecutionFlowGraph.refine() Removed " + mi);
		}
	}

	// public void showPredecessors(String data) {
	// hsForPredecessors.addFirst(data);
	// for (String s : hsForPredecessors) {
	// System.out.println("The predeccessor cvxcvxcv is " + s);
	// }
	// }
	//
	// public void getSuccessors(String data) {
	// if (verticesExists(data)) {
	// hsForSuccessors = new HashSet<String>(g.getSuccessors(data));
	// for (String each : hsForSuccessors) {
	//
	// hsForSuccessors.addAll(g.getSuccessors(each));
	//
	// }
	// for (String each : hsForSuccessors) {
	// System.out.println("The successor is " + each);
	//
	// }
	// }
	//
	// }
	//
	// public void enhanceMethods() {
	// System.out.println("ExecutionFlowGraph.enhanceMethods()");
	// hsForEnhancement = new HashSet<String>(g.getVertices());
	// // vertexAdd("MAIN");
	// for (String each : hsForEnhancement) {
	// if (!each.contains("init")) {
	// if (g.getPredecessors(each).isEmpty()) {
	// System.out.println("ExecutionFlowGraph.enhanceMethods() The vertices are
	// " + each);
	// g.addEdge("init" + countOfMethod.getAndIncrement(), "MAIN", each,
	// EdgeType.DIRECTED);
	// // edgeAdd("init"+countOfMethod.getAndIncrement(), "MAIN",
	// // each,EdgeType.DIRECTED);
	// }
	// } else {
	// if (g.getPredecessors(each).isEmpty()) {
	// // g.addVertex("MAIN");
	// g.addEdge("main" + countOfMethod.getAndIncrement(), each, "MAIN",
	// EdgeType.DIRECTED);
	// }
	// }
	//
	// }
	//
	// }
	//
	// // public String initVertex(String vertex){
	// // System.out.println("initVertex "+vertex);
	// // HashSet<String> vertexSet=new HashSet<String>(g.getVertices());
	// //
	// //
	// //
	// // if(Graph_Details.hashIdDuplicate.containsKey(vertex)){
	// // String idOfVertex=Graph_Details.hashIdDuplicate.get(vertex);
	// // System.out.println("vertex "+idOfVertex);
	// // for(String each : vertexSet){
	// // if(each.contains("init")){
	// // String idOfInit=Graph_Details.hashIdDuplicate.get(each);
	// // if(idOfVertex.equals(idOfInit))
	// // return each;
	// // }
	// //
	// // }
	// // }
	// // else
	// // { System.out.println("In else");
	// // if(Graph_Details.hashIdForSameNameVertices.containsKey(vertex)){
	// // String idOfVertex=Graph_Details.hashIdForSameNameVertices.get(vertex);
	// // System.out.println("vertex "+idOfVertex);
	// // String idInDuplicate=Graph_Details.hashIdDuplicate.get(idOfVertex);
	// // for(String each : vertexSet){
	// // if(each.contains("init")){
	// // String idOfInit=Graph_Details.hashIdDuplicate.get(each);
	// // if(idInDuplicate.equals(idOfInit))
	// // return each;
	// // }
	// // }
	// // }
	// // }
	// // return vertex;
	// //
	// // }
	// public boolean verticesExists(String vertex) {
	// if (g.containsVertex(vertex))
	// return true;
	// else {
	// System.out.println("Wrong Vertex " + vertex);
	// return false;
	// }
	// }
	//
	// public boolean edgeExists(String vertex1, String vertex2) {
	// if (g.findEdge(vertex1, vertex2) != null)
	// return true;
	// return false;
	// }

}
