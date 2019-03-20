package de.unipassau.abc.carving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class CallGraph {

	private final static Logger logger = LoggerFactory.getLogger(CallGraph.class);

	private final AtomicInteger id = new AtomicInteger(0);

	private Graph<MethodInvocation, String> graph;

	private Stack<MethodInvocation> stack;

	public CallGraph() {
		graph = new DirectedSparseMultigraph<MethodInvocation, String>();
		stack = new Stack<MethodInvocation>();
	}

	public void push(MethodInvocation methodInvocation) {
	    boolean purityFlag = methodInvocation.isPure();

		if (stack.isEmpty()) {
			// This register the call in the graph
			if (!purityFlag && !graph.containsVertex(methodInvocation)) {
				graph.addVertex(methodInvocation);
			}
		} else {
			// If the stack is not empty we can link directly the nodes
			MethodInvocation caller = stack.peek();
			if (!purityFlag && !graph.containsVertex(methodInvocation)) {
				graph.addVertex(methodInvocation);
				// Link the two
				graph.addEdge("CallDependency-" + id.getAndIncrement(), caller, methodInvocation, EdgeType.DIRECTED);

				if (caller.compareTo(methodInvocation) >= 0) {
					throw new RuntimeException("Cannot add a call in the past");
				}
			}
		}
		// This keeps track of the depth
		stack.push(methodInvocation);
	}

	// Pop=ping an empty stack IS an error
	public MethodInvocation pop() {
		MethodInvocation callee = stack.pop();
		// if (!stack.isEmpty()) {
		// MethodInvocation caller = stack.peek();
		// graph.addEdge("CallDependency-" + id.getAndIncrement(), caller,
		// callee, EdgeType.DIRECTED);
		// }
		return callee;
	}

	public void visualize() {
		VisualizationViewer<MethodInvocation, String> vv = new VisualizationViewer<MethodInvocation, String>(
				// new TreeLayout<>((Forest<MethodInvocation, String> )graph));
				// // Does not work..
				new KKLayout<MethodInvocation, String>(graph));

		vv.setPreferredSize(new Dimension(1000, 800));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String apply(Object node) {
				if (node instanceof ObjectInstance) {
					return ((ObjectInstance) node).getObjectId();
				} else if (node instanceof MethodInvocation) {
					return ((MethodInvocation) node).getMethodSignature();
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

		JFrame frame = new JFrame("Call Graph View");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}
	// public boolean addVertices(String data){
	// return graph.addVertex(data);
	// }
	// public boolean addEdges(String v1,String v2){
	// return graph.addEdge("Edge"+Math.random(), v2, v1,EdgeType.DIRECTED);
	// }
	// public boolean verticeExists(String data){
	// return graph.containsVertex(data);
	// }

	// This cannot be made as list because call graph is a tree and a node might
	// have multiple childreen
	/**
	 * For some reason, the graph library says that a give edge is UNDIRECTED,
	 * while it is DIRECTED (we verify that at construction time). I suspect
	 * some sort of buffer overflow or something... so NOW we need to double
	 * check all the operations ?!
	 * 
	 * @param methodInvocation
	 * @return
	 */
	public Set<MethodInvocation> getMethodInvocationsSubsumedBy(MethodInvocation methodInvocation) {
		Set<MethodInvocation> subsumedCalls = new HashSet<>();
		for (MethodInvocation mi : getSuccessors(methodInvocation)) {

			if (mi.compareTo(methodInvocation) <= 0) {
				logger.error("Since successors " + mi + " is in the past or is the method invocation "
						+ methodInvocation + " itself. we skip this !");
				continue;
			}
			subsumedCalls.addAll(getMethodInvocationsSubsumedBy(mi));
			subsumedCalls.add(mi);
		}
		return subsumedCalls;
	}

	public Collection<MethodInvocation> getSuccessors(MethodInvocation methodInvocation) {
		Collection<MethodInvocation> successors = graph.getSuccessors(methodInvocation);
		return (Collection<MethodInvocation>) (successors != null ? successors : new HashSet<>());
	}

	public Collection<MethodInvocation> getPredecessors(MethodInvocation methodInvocation) {
		Collection<MethodInvocation> predecessors = graph.getPredecessors(methodInvocation);
		return (Collection<MethodInvocation>) (predecessors != null ? predecessors : new HashSet<>());
	}

	public MethodInvocation peek() {
		if (!stack.isEmpty())
			return stack.peek();
		else
			return null;
	}

	// Return the ordered list that starting from (main?) reaches
	// methodinvocation by means of call chain
	public List<MethodInvocation> getOrderedSubsumingMethodInvocationsFor(MethodInvocation methodInvocation) {
		List<MethodInvocation> subsumingCalls = new ArrayList<>();
		for (MethodInvocation mi : getPredecessors(methodInvocation)) {
			subsumingCalls.addAll(getOrderedSubsumingMethodInvocationsFor(mi));
			subsumingCalls.add(mi);
		}
		return subsumingCalls;
	}

	public List<MethodInvocation> getSubsumingPathFor(MethodInvocation subsumingMethodInvocation,
			MethodInvocation subsumedMethodInvocation) {
		List<MethodInvocation> subsumingPath = getOrderedSubsumingMethodInvocationsFor(subsumedMethodInvocation);
		subsumingPath.removeAll(getOrderedSubsumingMethodInvocationsFor(subsumingMethodInvocation));
		// TODO This does not contain the input
		return subsumingPath;

	}

	public void verify() {
		// TODO Register hook to call system.exit nevertheless ?
		// Either the stack is empty or it has system.exit
		if (stack.isEmpty()) {
			return;
		} else {
			MethodInvocation top = stack.peek();
			if (!top.getMethodSignature().equals("<java.lang.System: void exit(int)>")) {
				throw new RuntimeException("Stack contains the wrong element at the end of parsing: " + top);
			}
		}
		for (String edge : graph.getEdges()) {
			if (graph.getEdgeType(edge).equals(EdgeType.UNDIRECTED)) {
				throw new RuntimeException(
						"Graph contains the wrong type of edge for : " + edge + " " + graph.getEndpoints(edge));
			}
		}
	}

	/**
	 * Return the parent of this method invocation if any
	 * 
	 * @param methodUnderInspection
	 * @return
	 */
	public MethodInvocation getCallerOf(MethodInvocation methodInvocation) {
		Collection<MethodInvocation> parents = getPredecessors(methodInvocation);
		if (parents.isEmpty()) {
			return null;
		} else {
			if (parents.size() > 1) {
				logger.warn("MethodInvocation " + methodInvocation + " has MULTIPLE PARENTS !! " + parents);
			}
			// Return the first, and only, one
			return parents.iterator().next();
		}
	}

	public CallGraph getSubGraph(MethodInvocation methodToCarve) {
		Collection<MethodInvocation> vertices = graph.getVertices();
		Collection<String> edges = graph.getEdges();

		CallGraph subGraph = new CallGraph();
		// Readd all the node
		for (MethodInvocation vertex : vertices) {
			if (vertex.compareTo(methodToCarve) <= 0) {
				subGraph.graph.addVertex(vertex);
			}
		}
		// Keep edges ONLY if both ends are inside the subgraph
		for (String edge : edges) {
			MethodInvocation source = graph.getSource(edge);
			MethodInvocation dest = graph.getDest(edge);
			if (subGraph.graph.containsVertex(source) && subGraph.graph.containsVertex(dest)) {
				// Adding the edge to the sub graph
				// System.out.println("CallGraph.getSubGraph() Keeping edge " +
				// source + "--[" + edge + "]-->" + dest);
				subGraph.graph.addEdge(edge, source, dest, EdgeType.DIRECTED);
				if (source.compareTo(dest) >= 0) {
					throw new RuntimeException("Cannot add a calling graph in the PAST !");
				}
			}
		}

		subGraph.verify();

//		logger.trace("CallGraph.getSubGraph() Original GRAPH " + vertices.size() + " -- " + edges.size());
//		logger.trace("CallGraph.getSubGraph() SUB GRAPH " + subGraph.graph.getVertexCount() + " -- "
//				+ subGraph.graph.getEdgeCount());
		return subGraph;
	}

	// Remove the nodes after parent (recursively this should remove all the
	// nodes)
	// Replace parent with the given one
	public void markParentAndPruneAfter(MethodInvocation parent) {
		logger.trace("CallGraph.markParentAndPruneAfter() ORIGINAL " + graph.getVertexCount() + " -- "
				+ graph.getEdgeCount());

		// This might be also null
		MethodInvocation grandParent = getCallerOf(parent);
		String originalCallEdge = null;
		if (grandParent != null) {
			originalCallEdge = graph.findEdge(grandParent, parent);
		}
		// Recursively remothe nodes ?
		remove(parent);
		// Add the parent once again
		parent.setTestSetupCall(true);
		//
		// System.out.println("Readded " + parent);
		graph.addVertex(parent);
		if (originalCallEdge != null) {
			graph.addEdge(originalCallEdge, grandParent, parent);
		}

		logger.debug(
				"CallGraph.markParentAndPruneAfter() PRUNED " + graph.getVertexCount() + " -- " + graph.getEdgeCount());

	}

	private void remove(MethodInvocation node) {

		for (MethodInvocation child : getSuccessors(node)) {
			remove(child);
		}
		graph.removeVertex(node);
	}

	//
	public Collection<MethodInvocation> getAll() {
		return graph.getVertices();
	}

	public boolean contains(MethodInvocation methodUnderInspection) {
		return graph.getVertices().contains(methodUnderInspection);
	}

	public int distanceToRoot(MethodInvocation key) {
		int distance = 0;
		MethodInvocation parent = getCallerOf(key);
		while (parent != null) {
			distance++;
			parent = getCallerOf(parent);
		}
		return distance;
	}
}
