package de.unipassau.abc.carving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.GraphUtility;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

/**
 * Using strings inside the edge makes this really memory intensive (string
 * buffer, and CG goes nuts) Maybe we can optimize this? In the end, all those
 * objects are Immutable...
 * 
 * @author gambi
 *
 */
public class ExecutionFlowGraph {

	private final Logger logger = LoggerFactory.getLogger(ExecutionFlowGraph.class);

	private final AtomicInteger id = new AtomicInteger(0);
	private final AtomicInteger tid = new AtomicInteger(0);

	private Graph<MethodInvocation, String> graph;
	private MethodInvocation lastMethodInvocation = null;
	private MethodInvocation firstMethodInvocation = null;

	public ExecutionFlowGraph() {
		// graph = new SparseMultigraph<MethodInvocation, String>();
		graph = new DirectedSparseMultigraph<MethodInvocation, String>();
	}

	// TODO Maybe change the name...and replace the next method
	public void addOwnerToMethodInvocation(MethodInvocation methodInvocation, ObjectInstance object) {
	    addOwnerToMethodInvocation(methodInvocation, object.getObjectId());
	}
	
	public void addOwnerToMethodInvocation(MethodInvocation methodInvocation, String objectId) {

		if (graph.containsVertex(methodInvocation)) {
			// No need to look for it really, as we replace the node

			// Store data on in and out edges from this node
			Map<String, MethodInvocation> inEdges = new HashMap<>();
			Map<String, MethodInvocation> outEdges = new HashMap<>();
			for (String inEdge : graph.getInEdges(methodInvocation)) {
				inEdges.put(inEdge, graph.getOpposite(methodInvocation, inEdge));
			}
			for (String outEdge : graph.getOutEdges(methodInvocation)) {
				outEdges.put(outEdge, graph.getOpposite(methodInvocation, outEdge));
			}
			// Remove the original node - this also removes the edges... I hope
			graph.removeVertex(methodInvocation);

			// Updated the node
			methodInvocation.setOwner(new ObjectInstance(objectId));

			// Put the node back
			graph.addVertex(methodInvocation);

			// Put back the edges
			for (String inEdge : inEdges.keySet()) {
				graph.addEdge(inEdge, inEdges.get(inEdge), methodInvocation, EdgeType.DIRECTED);
			}

			for (String outEdge : outEdges.keySet()) {
				graph.addEdge(outEdge, methodInvocation, outEdges.get(outEdge), EdgeType.DIRECTED);
			}

			///

		}

		// if (graph.containsVertex(methodInvocation)) {
		// for( MethodInvocation mi : graph.getVertices()){
		// if( mi.equals( methodInvocation ) ){
		// System.out.println( methodInvocation + " ==> " +
		// methodInvocation.getOwner() );
		// System.out.println( mi + " ==> " + mi.getOwner() );
		// }
		// }
		// }

		// Check that the information is really there !

	}

	public void enqueueMethodInvocations(MethodInvocation methodInvocation) {

		// At this point we do not have access to instance information !
		// System.out.println("ExecutionFlowGraph.enqueueMethodInvocations() " +
		// methodInvocation +
		// ( methodInvocation.isStatic() ? "" : " with owner " +
		// methodInvocation.getOwner() ));

		// MethodInvocation mi = new MethodInvocation(methodInvocation);
		if (!graph.containsVertex(methodInvocation)) {
			graph.addVertex(methodInvocation);
		}

		if (lastMethodInvocation != null) {
			graph.addEdge("ExecutionDependency-" + id.getAndIncrement(), lastMethodInvocation, methodInvocation,
					EdgeType.DIRECTED);
		}
		lastMethodInvocation = methodInvocation;

		if (firstMethodInvocation == null) {
			firstMethodInvocation = methodInvocation;
		}
	}

	public void visualize() {
		VisualizationViewer<MethodInvocation, String> vv = new VisualizationViewer<MethodInvocation, String>(
				new KKLayout<MethodInvocation, String>(graph));

		vv.setPreferredSize(new Dimension(2000, 1600));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		// vv.getRenderContext().setVertexLabelTransformer(new
		// ToStringLabeller() {
		// @Override
		// public String apply(Object node) {
		// if (node instanceof ObjectInstance) {
		// return ((ObjectInstance) node).getObjectId();
		// } else if (node instanceof MethodInvocation) {
		// return ((MethodInvocation) node).getJimpleMethod();
		// } else {
		// return super.apply(node);
		// }
		// }
		// });
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
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

	private Collection<MethodInvocation> getPredecessors(MethodInvocation mi) {
		Collection<MethodInvocation> predecessors = graph.getPredecessors(mi);
		return (Collection<MethodInvocation>) (predecessors != null ? predecessors : new HashSet<>());
	}

	/**
	 * Return the calls in the Execution Flow Graph which match carveBy
	 * (include) but not excludeBy (exclude). We also omit private method
	 * invocations since those cannot be carved !
	 * 
	 * @param carveBy
	 * @param excludeBy
	 * @return
	 */
	public Collection<MethodInvocation> getMethodInvocationsFor(MethodInvocationMatcher carveBy,
			MethodInvocationMatcher... excludeBy) {
		ArrayList<MethodInvocation> matching = new ArrayList<>();
		for (MethodInvocation mi : graph.getVertices()) {

			if (mi.isPrivate()) {
				continue;
			}

			if (carveBy.matches(mi)) {
				// TODO Refactor with an AND matcher? not really want to go
				// there...
				boolean excluded = false;
				for (MethodInvocationMatcher exclude : excludeBy) {
					if (exclude.matches(mi)) {
						excluded = true;
						break;
					}
				}

				if (!excluded) {
					matching.add(mi);
				}
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

		List<MethodInvocation> orderedMethodInvocationsBefore = new ArrayList<>( graph.getVertices());
		// Sort them. Method invocation implements comparable
		Collections.sort( orderedMethodInvocationsBefore );
		// Find the position of the given mi
		int position = orderedMethodInvocationsBefore.indexOf( methodInvocation );
		// now remove all the elements that are before that position. The resulting list should include the given mi
		
		orderedMethodInvocationsBefore.removeAll( orderedMethodInvocationsBefore.subList(position, orderedMethodInvocationsBefore.size()-1));
		// We need to ensure the given mi is there
		orderedMethodInvocationsBefore.add(0, methodInvocation);
		return orderedMethodInvocationsBefore;
		
//		if (!graph.containsVertex(methodInvocation)) {
//			logger.info("Method invocation " + methodInvocation + " not in the execution graph");
//			return Collections.<MethodInvocation>emptyList();
//		}
//
//		List<MethodInvocation> predecessorsOf = new ArrayList<>();
//		for (MethodInvocation mi : getPredecessors(methodInvocation)) {
//			predecessorsOf.addAll(getOrderedMethodInvocationsBefore(mi));
//		}
//		// As last add the mi passed as parameter
//		predecessorsOf.add(methodInvocation);
//
//		return predecessorsOf;
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

	// TODO: Double check that this actually produces the same results as before.
	// Plus check why we needed the methodInvocation to be in this list...
	public List<MethodInvocation> getOrderedMethodInvocationsAfter(MethodInvocation methodInvocation) {

		List<MethodInvocation> orderedMethodInvocationsAfter = new ArrayList<>( graph.getVertices());
		// Sort them. Method invocation implements comparable
		Collections.sort( orderedMethodInvocationsAfter );
		// Find the position of the given mi
		int position = orderedMethodInvocationsAfter.indexOf( methodInvocation );
		// now remove all the elements that are before that position. The resulting list should include the given mi
		orderedMethodInvocationsAfter.removeAll( orderedMethodInvocationsAfter.subList(0, position));
		
		return orderedMethodInvocationsAfter;
		
//		try {
//			List<MethodInvocation> successorsOf = new ArrayList<>();
//			logger.debug("getOrderedMethodInvocationsAfter " + methodInvocation);
//			// Add the actual one ?! Why is this here ?!
//			successorsOf.add(methodInvocation);
//
//			// getSuccessors(methodInvocation) can return null !? !
//			for (MethodInvocation mi : getSuccessors(methodInvocation)) {
//
//				if (successorsOf.contains(mi)) {
//					logger.error(mi + " is already in the successors list ?!");
//				} else {
//					// Add the one reach from this one
//					successorsOf.addAll(getOrderedMethodInvocationsAfter(mi));
//				}
//			}
//			return successorsOf;
//		} catch (Throwable e) {
//			e.printStackTrace();
//			throw e;
//		}
	}

	public Collection<MethodInvocation> getSuccessors(MethodInvocation methodInvocation) {
		Collection<MethodInvocation> successors = graph.getSuccessors(methodInvocation);
		return (Collection<MethodInvocation>) (successors != null ? successors : new HashSet<>());
	}

	public MethodInvocation getLastMethodInvocation() {
		return lastMethodInvocation;

	}

	public List<MethodInvocation> getOrderedMethodInvocations() {
		return getOrderedMethodInvocationsAfter(firstMethodInvocation);
	}

	public void markNodeAsExternalInterface(MethodInvocation mi) {
		// System.out.println("ExecutionFlowGraph.markNodeAsExternalInterface()
		// " + mi);
		// Collecte edges

		// TODO Those might not really realiable !!
		Collection<Pair<MethodInvocation, String>> predecessors = new HashSet<>();
		for (String inEdge : graph.getInEdges(mi)) {
			predecessors.add(new Pair<MethodInvocation, String>(graph.getOpposite(mi, inEdge), inEdge));
		}

		Collection<Pair<MethodInvocation, String>> successors = new HashSet<>();
		for (String outEdge : graph.getOutEdges(mi)) {
			predecessors.add(new Pair<MethodInvocation, String>(graph.getOpposite(mi, outEdge), outEdge));
		}

		// Remove the node, this shall remove also the edges
		graph.removeVertex(mi);
		// Update the node content
		mi.setBelongsToExternalInterface(true);
		// Reinsert the node
		graph.addVertex(mi);
		// Reinsert the edges
		for (Pair<MethodInvocation, String> predecessor : predecessors) {
			graph.addEdge(predecessor.getSecond(), predecessor.getFirst(), mi, EdgeType.DIRECTED);
		}
		for (Pair<MethodInvocation, String> successor : successors) {
			graph.addEdge(successor.getSecond(), mi, successor.getFirst(), EdgeType.DIRECTED);
		}
	}

	/*
	 * Keep only the invocations that can be found in the provide set
	 */
	public void refine(Set<MethodInvocation> requiredMethodInvocations) {

		// VERIFY. Does this graph contains the connectedMethodInvocation
		if (!graph.getVertices().containsAll(requiredMethodInvocations)) {
			// Only in debugging mode visualize();
			throw new RuntimeException(
					" Connected Method invocations " + requiredMethodInvocations + " are not in the graph ?!" + graph);
		}

		Set<MethodInvocation> unconnected = new HashSet<>();
		for (MethodInvocation node : graph.getVertices()) {

			if (!requiredMethodInvocations.contains(node)) {
				logger.debug("ExecutionFlowGraph.refine() Remove " + node + " as not required");
				unconnected.add(node);
			}

		}
		//
		for (MethodInvocation mi : unconnected) {
			// We need to connect predecessor and successor before removing this
			// vertex
			Collection<MethodInvocation> predecessors = getPredecessors(mi);
			Collection<MethodInvocation> successors = getSuccessors(mi);

			if (predecessors == null || predecessors.isEmpty()) {
				// This is the FIRST METHOD INVOCATION - update the reference
				if (successors == null || successors.isEmpty()) {
					// This was als the last one
					firstMethodInvocation = null;
				} else {
					// At least one element (actually, only one)
					firstMethodInvocation = successors.iterator().next();
				}
				logger.trace("ExecutionFlowGraph.refine() Updated firstMethodInvocation to " + firstMethodInvocation);
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
				logger.trace("ExecutionFlowGraph.refine() Updated lastMethodInvocation to " + lastMethodInvocation);

			}

			// TODO first and last element can definitively be computed
			// afterwards by following the flow relations

			Collection<String> flowEdges = new HashSet<>(graph.getInEdges(mi));
			flowEdges.addAll(graph.getOutEdges(mi));

			for (String flowEdge : flowEdges) {
				logger.trace("ExecutionFlowGraph.refine() Removing Edge " + flowEdge);
				graph.removeEdge(flowEdge);
			}
			// Really its just one
			for (MethodInvocation predecessor : predecessors) {
				for (MethodInvocation successor : successors) {
					int edgeID = id.getAndIncrement();
					String edgeLabel = "ExecutionDependency-" + edgeID;
					boolean added = graph.addEdge(edgeLabel, predecessor, successor, EdgeType.DIRECTED);
					logger.trace("ExecutionFlowGraph.refine() Introducing replacemente edge "
							+ graph.getEndpoints(edgeLabel) + " added  " + added);
				}
			}

			graph.removeVertex(mi);
			logger.debug("ExecutionFlowGraph.refine() Removed " + mi);
		}
	}

	public boolean verify() {
		if (graph.getVertices().isEmpty()) {
			logger.error("ExecutionFlowGraph.verify() Empty nodes !");
			return false;
		}
		if (firstMethodInvocation == null) {
			logger.error("ExecutionFlowGraph.verify() Missing first node!");
			return false;
		}
		if (lastMethodInvocation == null) {
			logger.error("ExecutionFlowGraph.verify() Missing last node!");
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstMethodInvocation == null) ? 0 : firstMethodInvocation.hashCode());
		result = prime * result + ((graph == null) ? 0 : graph.hashCode());
		result = prime * result + ((lastMethodInvocation == null) ? 0 : lastMethodInvocation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecutionFlowGraph other = (ExecutionFlowGraph) obj;

		if (firstMethodInvocation == null) {
			if (other.firstMethodInvocation != null)
				return false;
		} else if (!firstMethodInvocation.equals(other.firstMethodInvocation))
			return false;
		//
		if (lastMethodInvocation == null) {
			if (other.lastMethodInvocation != null)
				return false;
		} else if (!lastMethodInvocation.equals(other.lastMethodInvocation))
			return false;
		//
		return GraphUtility.areExecutionGraphsEqual(graph, other.graph);
	}

	// Maybe use a Comparator ?
	// True if method1 was executed before method 2. If they are the same, we do
	// not return it?
	public boolean isBefore(MethodInvocation methodInvocation1, MethodInvocation methodInvocation2) {
		return methodInvocation1.getInvocationCount() <= methodInvocation2.getInvocationCount();
	}

	public boolean contains(MethodInvocation methodInvocation) {
		return graph.containsVertex(methodInvocation);
	}

	public ExecutionFlowGraph getSubGraph(List<MethodInvocation> orderedSlice) {
		ExecutionFlowGraph subGraph = new ExecutionFlowGraph();
		for (MethodInvocation methodInvocation : orderedSlice) {
			subGraph.enqueueMethodInvocations(methodInvocation);
		}
		return subGraph;
	}

	// Insert the method invocation in the "right" place...Usually in front,
	// TODO but there might be more than one ?
	public void insertTestSetupCall(MethodInvocation testSetupCall) {
		System.out.println("ExecutionFlowGraph.insertTestSetupCall() " + testSetupCall);
		graph.addVertex(testSetupCall);
		//
		graph.addEdge("TestSetupCall-" + tid.getAndIncrement(), testSetupCall, firstMethodInvocation,
				EdgeType.DIRECTED);
		// Replace the first call
		firstMethodInvocation = testSetupCall;
	}

	public Set<MethodInvocation> getTestSetupMethodInvocations() {
		Set<MethodInvocation> methodInvocations = new HashSet<>();
		for (MethodInvocation methodInvocation : getOrderedMethodInvocations()) {
			if (methodInvocation.isTestSetupCall()) {
				methodInvocations.add(methodInvocation);
			}
		}
		return methodInvocations;
	}

	public Set<MethodInvocation> getMethodInvocationsToExternalInterfaceBefore(
			MethodInvocation methodInvocationToCarve) {
		return new HashSet<MethodInvocation>(
				getOrderedMethodInvocationsToExternalInterfaceBefore(methodInvocationToCarve));
	}

	public List<MethodInvocation> getOrderedMethodInvocationsToExternalInterfaceBefore(
			MethodInvocation methodInvocationToCarve) {
		List<MethodInvocation> all = new ArrayList<>(getOrderedMethodInvocationsBefore(methodInvocationToCarve));
		// Now remove everything not an external interface
		Iterator<MethodInvocation> iterator = all.iterator();
		while (iterator.hasNext()) {
			MethodInvocation methodInvocation = iterator.next();
			if (!methodInvocation.belongsToExternalInterface()) {
				iterator.remove();
			}
		}
		return all;
	}

}
