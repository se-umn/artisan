package de.unipassau.abc.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class CallGraphImpl implements CallGraph {

	private final static Logger logger = LoggerFactory.getLogger(CallGraphImpl.class);

	private final AtomicInteger id = new AtomicInteger(0);

	private Graph<MethodInvocation, String> graph;

	private Stack<MethodInvocation> stack;

	public CallGraphImpl() {
		graph = new DirectedSparseMultigraph<>();
		stack = new Stack<>();
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

	public Set<MethodInvocation> getRoots() {
		Set<MethodInvocation> roots = new HashSet<>();
		// A root in this graph is defined as a node which does not have incoming edges
		// TODO This is because we only model call relation and not return into
		for (MethodInvocation mi : graph.getVertices()) {
			if (graph.getInEdges(mi).size() == 0) {
				roots.add(mi);
			}
		}
		return roots;

	}

	public void visualize() {
		VisualizationViewer<MethodInvocation, String> vv = new VisualizationViewer<>(
				// new TreeLayout<>((Forest<MethodInvocation, String> )graph));
				// // Does not work..
				new KKLayout<>(graph));

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
		vv.getRenderContext().setVertexFillPaintTransformer((Function<GraphNode, Paint>) node -> {
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
		});

		JFrame frame = new JFrame("Call Graph View");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

	@Override
	public void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl) {
		MethodInvocation originalMethodInvocation = graph.getVertices().stream()
				.filter(methodInvocation -> methodInvocation.equals(orig)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No such method invocation"));

		Collection<String> inEdges = graph.getInEdges(originalMethodInvocation);
		Collection<String> outEdges = graph.getOutEdges(originalMethodInvocation);

		List<Pair<String, MethodInvocation>> inEdgesWithSources = inEdges.stream()
				.map(edge -> new Pair<>(edge, graph.getOpposite(originalMethodInvocation, edge)))
				.collect(Collectors.toList());
		List<Pair<String, MethodInvocation>> outEdgesWithDestinations = outEdges.stream()
				.map(edge -> new Pair<>(edge, graph.getOpposite(originalMethodInvocation, edge)))
				.collect(Collectors.toList());

		graph.removeVertex(originalMethodInvocation);
		graph.addVertex(repl);

		inEdgesWithSources.forEach(edgeNameMethodInvocationPair -> graph
				.addEdge(edgeNameMethodInvocationPair.getFirst(), edgeNameMethodInvocationPair.getSecond(), repl));
		outEdgesWithDestinations.forEach(edgeNameMethodInvocationPair -> graph
				.addEdge(edgeNameMethodInvocationPair.getFirst(), repl, edgeNameMethodInvocationPair.getSecond()));
	}

	@Override
	public void replaceMethodInvocationWithExecution(MethodInvocation methodInvocationToReplace) {
		Collection<MethodInvocation> callers = getPredecessors(methodInvocationToReplace);
		if (callers.size() > 1) {
			throw new IllegalStateException(
					"Call graph contains multiple callers for " + methodInvocationToReplace + ": " + callers);
		} else if (callers.isEmpty()) {
			/*
			 * If the caller was at the root level we do not have to do anything except
			 * removing it from this call graph. This will remove the node and the edges
			 * attached to it, hence promoting the methods directly invoked by it by one
			 * level
			 */
			this.graph.removeVertex(methodInvocationToReplace);
		} else {
			// Get method calling methodInvocationToReplace
			MethodInvocation caller = callers.iterator().next();

			// Collect the methods directly called by methodInvocationToReplace. No need for
			// ordering
			Collection<MethodInvocation> calleds = this.getSuccessors(methodInvocationToReplace);

			// Connect the caller of methodInvocationToReplace to the method invocations it
			// subsumes
			for (MethodInvocation called : calleds) {
				// Not sure we need to encode any specify information here...
				this.graph.addEdge("Replaced-CallDependency-" + id.getAndIncrement(), caller, called,
						EdgeType.DIRECTED);
			}

			// Finally, remove the methodInvocation
			this.graph.removeVertex(methodInvocationToReplace);

		}

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

	/**
	 * For some reason, the graph library says that a give edge is UNDIRECTED, while
	 * it is DIRECTED (we verify that at construction time). I suspect some sort of
	 * buffer overflow or something... so NOW we need to double check all the
	 * operations ?!
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
		if (!stack.isEmpty()) {
			return stack.peek();
		} else {
			return null;
		}
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
		// TODO This does not contain the input --- actually there's duplicates
		// here ...
		return subsumingPath;

	}

	/*
	 * For some reason onDestroy sometimes terminates and returns sometimes it does
	 * not. So we tolerate a number of onDestroy method invocations on the stack by
	 * the end of the trace..
	 */
	public List<MethodInvocation> verify() {
		List<MethodInvocation> methodInvocationToDrop = new ArrayList<>();
		if (stack.isEmpty()) {
			return methodInvocationToDrop;
		} else {
			while (!stack.isEmpty()) {
				MethodInvocation top = stack.pop();
				if (!JimpleUtils.getMethodName(top.getMethodSignature()).equals("onDestroy")) {
					throw new RuntimeException("Stack contains the wrong element at the end of parsing: " + top);
				} else {
					methodInvocationToDrop.add(top);
				}
			}
			return methodInvocationToDrop;
		}
//        for (String edge : graph.getEdges()) {
//            if (graph.getEdgeType(edge).equals(EdgeType.UNDIRECTED)) {
//                throw new RuntimeException(
//                        "Graph contains the wrong type of edge for : " + edge + " " + graph.getEndpoints(edge));
//            }
//        }
	}

	/**
	 * Return the parent of this method invocation if any
	 *
	 * @param methodInvocation
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

	public CallGraphImpl getSubGraph(MethodInvocation methodToCarve) {
		Collection<MethodInvocation> vertices = graph.getVertices();
		Collection<String> edges = graph.getEdges();

		CallGraphImpl subGraph = new CallGraphImpl();
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

		// logger.trace("CallGraph.getSubGraph() Original GRAPH " +
		// vertices.size() + " -- " + edges.size());
		// logger.trace("CallGraph.getSubGraph() SUB GRAPH " +
		// subGraph.graph.getVertexCount() + " -- "
		// + subGraph.graph.getEdgeCount());
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

	public void remove(MethodInvocation node) {

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

	public CallGraph getSubGraph(List<MethodInvocation> methodInvocations) {
		Collection<MethodInvocation> vertices = graph.getVertices();
		Collection<String> edges = graph.getEdges();

		CallGraphImpl subGraph = new CallGraphImpl();
		// Add the nodes that corresponds to the given methodInvocations
		for (MethodInvocation vertex : vertices) {
			if (methodInvocations.contains(vertex)) {
				subGraph.graph.addVertex(vertex);
			}
		}
		// Keep an edge ONLY if both of its ends are already inside the subgraph
		for (String edge : edges) {
			MethodInvocation source = graph.getSource(edge);
			MethodInvocation dest = graph.getDest(edge);
			if (subGraph.graph.containsVertex(source) && subGraph.graph.containsVertex(dest)) {
				subGraph.graph.addEdge(edge, source, dest, EdgeType.DIRECTED);
				if (source.compareTo(dest) >= 0) {
					// TODO How is this possible ?
					throw new RuntimeException("Cannot add a calling graph in the PAST !");
				}
			}
		}
		return subGraph;
	}

	// Order is not necessary guaranteed
	public Collection<MethodInvocation> getAllMethodInvocations() {
		Collection<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();
		for (GraphNode node : graph.getVertices()) {
			if (node instanceof MethodInvocation) {
				methodInvocations.add((MethodInvocation) node);
			}
		}
		return methodInvocations;
	}

	@Override
	public Collection<CallGraph> extrapolate(Set<MethodInvocation> methodInvocations) {
		Collection<CallGraph> extrapolated = new ArrayList<CallGraph>();

		Graph<MethodInvocation, String> union = new DirectedSparseMultigraph<MethodInvocation, String>();

		// THIS IS REALLY ANNOYING ! We need to store the clones otherwise graph will
		// not realize it is the same node ..
		Map<MethodInvocation, MethodInvocation> cloneMap = new HashMap<MethodInvocation, MethodInvocation>();

		// Add all method invocation nodes - clone them in the process
		for (MethodInvocation methodInvocation : methodInvocations) {
			// Add the method invocation vertex and its neighbors, unless they are there
			// already
			MethodInvocation cloned = methodInvocation.clone();
			union.addVertex(cloned);
			cloneMap.put(cloned, methodInvocation);

			for (MethodInvocation neighbor : graph.getPredecessors(methodInvocation)) {
				MethodInvocation clonedNode = neighbor.clone();
				cloneMap.put(clonedNode, neighbor);
				union.addVertex(cloned);
			}

			for (MethodInvocation neighbor : graph.getSuccessors(methodInvocation)) {
				MethodInvocation clonedNode = neighbor.clone();
				cloneMap.put(clonedNode, neighbor);
				union.addVertex(cloned);
			}

		}
		// Add all the edges between the nodes
		for (MethodInvocation source : union.getVertices()) {
			for (MethodInvocation target : union.getVertices()) {

				MethodInvocation originalSource = cloneMap.get(source);
				MethodInvocation originalTarget = cloneMap.get(target);

				if (!graph.containsVertex(originalSource)) {
					logger.warn("Graph does not contain " + source);
					logger.warn("ALL VERTICES " + graph.getVertices());

				}

				if (!graph.containsVertex(originalTarget)) {
					logger.warn("Graph does not contain " + target);
					logger.warn("ALL VERTICES " + graph.getVertices());
				}

				// Original grap here.. BUT, it works with == and not equals ! :(
				Collection<String> edges = graph.findEdgeSet(originalSource, originalTarget);
				if (edges != null) {
					for (String edge : edges) {
						union.addEdge(edge, source, target, EdgeType.DIRECTED);
					}
				}
			}
		}

		// Find the weakly connected components
		WeakComponentClusterer<MethodInvocation, String> clusterer = new WeakComponentClusterer<MethodInvocation, String>();
		Set<Set<MethodInvocation>> clusters = clusterer.apply(union);

		// Clusters at this points are partitions of the nodes, nodes are clones of the
		// original nodes, so we can build the sub graphs.
		// Note that the extrapolated graphs will have different edges;
		// they will start from 0 and increment.

		for (Set<MethodInvocation> cluster : clusters) {

			// We need the implementation class here to directly access the attribute
			CallGraphImpl callGraph = new CallGraphImpl();
			// I would have preferred using pop/push, the primitives for this graph, but it
			// might be tedious to get it right

			for (MethodInvocation source : cluster) {
				for (MethodInvocation target : cluster) {

					MethodInvocation originalSource = cloneMap.get(source);
					MethodInvocation originalTarget = cloneMap.get(target);

					Collection<String> edges = graph.findEdgeSet(originalSource, originalTarget);

					if (edges != null) {
						for (String edge : edges) {
							callGraph.graph.addEdge(edge, source, target, EdgeType.DIRECTED);
						}
					}

				}
			}

			extrapolated.add(callGraph);
		}

		return extrapolated;
	}

}
