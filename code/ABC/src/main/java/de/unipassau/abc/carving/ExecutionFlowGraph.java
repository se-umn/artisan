package de.unipassau.abc.carving;

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
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import de.unipassau.abc.utils.GraphUtility;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class ExecutionFlowGraph {

	private final Logger logger = LoggerFactory.getLogger(ExecutionFlowGraph.class);

	private final AtomicInteger id = new AtomicInteger(0);

	private Graph<MethodInvocation, String> graph;
	private MethodInvocation lastMethodInvocation = null;
	private MethodInvocation firstMethodInvocation = null;

	public ExecutionFlowGraph() {
		graph = new SparseMultigraph<MethodInvocation, String>();
	}

	// TODO Maybe change the name...
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

		vv.setPreferredSize(new Dimension(1000, 800));
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
	 * (include) but not excludeBy (exclude)
	 * 
	 * @param carveBy
	 * @param excludeBy
	 * @return
	 */
	public Collection<MethodInvocation> getMethodInvocationsFor(MethodInvocationMatcher carveBy,
			MethodInvocationMatcher... excludeBy) {
		ArrayList<MethodInvocation> matching = new ArrayList<>();
		for (MethodInvocation mi : graph.getVertices()) {
			if (carveBy.match(mi)) {
				// TODO Refactor with an AND matcher? not really want to go
				// there...
				boolean excluded = false;
				for (MethodInvocationMatcher exclude : excludeBy) {
					if (exclude.match(mi)) {
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
		List<MethodInvocation> predecessorsOf = new ArrayList<>();
		for (MethodInvocation mi : getPredecessors(methodInvocation)) {
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
			// getSuccessors(methodInvocation) can return null !? !
			for (MethodInvocation mi : getSuccessors(methodInvocation)) {
				// Add the one reach from this one
				successorsOf.addAll(getOrderedMethodInvocationsAfter(mi));
			}
			return successorsOf;
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
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

	/*
	 * Keep only the invocations that can be found in the provide set
	 */
	public void refine(Set<MethodInvocation> connectedMethodInvocations) {

		// VERIFY. Does this graph contains the connectedMethodInvocation
		if (!graph.getVertices().containsAll(connectedMethodInvocations)) {
			visualize();
			throw new RuntimeException(
					" Connected Method invocations " + connectedMethodInvocations + " are not in the graph ?!" + graph);
		}

		Set<MethodInvocation> unconnected = new HashSet<>();
		for (MethodInvocation node : graph.getVertices()) {
			if (!connectedMethodInvocations.contains(node)) {
				// NOTE: since we are iterating and removing at the same time,
				// this might create problems
				unconnected.add(node);
			}
		}
		//
		for (MethodInvocation mi : unconnected) {
			logger.debug("ExecutionFlowGraph.refine() Remove " + mi + " as unconnected");
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
				logger.debug("ExecutionFlowGraph.refine() Updated firstMethodInvocation to " + firstMethodInvocation);
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
				logger.debug("ExecutionFlowGraph.refine() Updated lastMethodInvocation to " + lastMethodInvocation);

			}

			// TODO first and last element can definitively be computed
			// afterwards by following the flow relations

			Collection<String> flowEdges = new HashSet<>(graph.getInEdges(mi));
			flowEdges.addAll(graph.getOutEdges(mi));

			for (String flowEdge : flowEdges) {
				logger.debug("ExecutionFlowGraph.refine() Removing Edge " + flowEdge);
				graph.removeEdge(flowEdge);
			}
			// Really its just one
			for (MethodInvocation predecessor : predecessors) {
				for (MethodInvocation successor : successors) {
					int edgeID = id.getAndIncrement();
					String edgeLabel = "ExecutionDependency-" + edgeID;
					boolean added = graph.addEdge(edgeLabel, predecessor, successor, EdgeType.DIRECTED);
					logger.debug("ExecutionFlowGraph.refine() Introducing replacemente edge "
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

}
