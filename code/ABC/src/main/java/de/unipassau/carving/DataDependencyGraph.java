package de.unipassau.carving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import de.unipassau.utils.JimpleUtils;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import soot.Local;
import soot.Value;

/**
 * @author gambi
 */
public class DataDependencyGraph {

	private static Logger logger = LoggerFactory.getLogger(DataDependencyGraph.class);

	public static final String OWNERSHIP_DEPENDENCY_PREFIX = "Ownership";
	public static final String DATA_DEPENDENCY_PREFIX = "DataNode";
	public static final String RETURN_DEPENDENCY_PREFIX = "Return";

	private static AtomicInteger id = new AtomicInteger(0);

	private Graph<GraphNode, String> g;

	// It seems that Graph does not really return the same objects. Somehow if I
	// set complex attributes on a node by looking up that node once again I
	// loose them

	// So we store data OUTSIDE the graph...
	private Map<DataNode, Value> additionalData;

	public DataDependencyGraph() {
		g = new SparseMultigraph<GraphNode, String>();
		additionalData = new HashMap<>();
	}

	/**
	 * This gets the actual data dependencies, values of primitives and string,
	 * references for objects. Node that objectInstanceIds must be ordered
	 * positionally !!!
	 * 
	 * @param methodInvocation
	 * @param actualParameters
	 */
	public void addMethodInvocation(MethodInvocation methodInvocation, String... actualParameters) {

		if (!g.containsVertex(methodInvocation)) {
			g.addVertex(methodInvocation);
		}

		// extract formal parameters from method invocation, build a mask
		// <org.employee.Validation: int
		// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
		String[] formalParameters = JimpleUtils.getParameterList(methodInvocation.getJimpleMethod());
		for (int position = 0; position < formalParameters.length; position++) {
			DataNode node = null;

			if (JimpleUtils.isPrimitive(formalParameters[position])) {
				node = PrimitiveNodeFactory.get(formalParameters[position], actualParameters[position]);
				setValueFor(node, ((ValueNode) node).getData());
			} else if (actualParameters[position] == null) {
				node = NullNodeFactory.get(formalParameters[position]);
				setValueFor(node, ((ValueNode) node).getData());
			} else if (JimpleUtils.isString(formalParameters[position])) {
				// We store non null string by Value
				node = StringNodeFactory.get(actualParameters[position]);
				setValueFor(node, ((ValueNode) node).getData());
			} else {
				node = new ObjectInstance(actualParameters[position]);
			}
			if (!g.containsVertex(node)) {
				g.addVertex(node);
			}
			g.addEdge(DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement(), node, methodInvocation,
					EdgeType.DIRECTED);
		}
	}

	// This will not work for static methods
	public void addDataDependencyOnReturn(MethodInvocation methodInvocation, String returnValue) {

		if (!g.containsVertex(methodInvocation)) {
			g.addVertex(methodInvocation);
		}

		//

		// extract formal parameters from method invocation, build a mask
		// <org.employee.Validation: int
		// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
		String formalReturnValue = JimpleUtils.getReturnType(methodInvocation.getJimpleMethod());
		DataNode node = null;

		if (JimpleUtils.isVoid(formalReturnValue)) {
			return;
		} else if (JimpleUtils.isPrimitive(formalReturnValue) && returnValue != null) {

			node = PrimitiveNodeFactory.get(formalReturnValue, returnValue);
			setValueFor(node, ((ValueNode) node).getData());
		} else if (JimpleUtils.isPrimitive(formalReturnValue) && returnValue == null) {
			// TODO In case the return value is not used in the code,
			// returnValue is
			// null also FOR primitive types
			// In this case, we shall NOT record this dependency
			logger.debug("We do not store return values for primitive types if they are not assigned");
			return;
		} else if (returnValue == null) {
			node = NullNodeFactory.get(formalReturnValue);
			setValueFor(node, ((ValueNode) node).getData());
		} else if (JimpleUtils.isString(formalReturnValue)) {
			// Note: We store non null string by Value
			node = StringNodeFactory.get(returnValue);
			setValueFor(node, ((ValueNode) node).getData());
		} else {
			node = new ObjectInstance(returnValue);
		}
		if (!g.containsVertex(node)) {
			g.addVertex(node);
		}
		// METHOD -> RETURN

		g.addEdge(RETURN_DEPENDENCY_PREFIX + id.getAndIncrement(), methodInvocation, node, EdgeType.DIRECTED);
	}

	// This will not work for static methods
	public void addDataDependencyOnOwner(MethodInvocation methodInvocation, String objectInstanceId) {

		if (!g.containsVertex(methodInvocation)) {
			g.addVertex(methodInvocation);
		}

		ObjectInstance oi = new ObjectInstance(objectInstanceId);

		if (!g.containsVertex(oi)) {
			g.addVertex(oi);
		}
		// Dependency as owner of the method OBJECT -> INIT
		g.addEdge(OWNERSHIP_DEPENDENCY_PREFIX + id.getAndIncrement(), oi, methodInvocation, EdgeType.DIRECTED);
	}

	// https://www.youtube.com/watch?v=I6eAA7tmgsQ
	public void visualize() {
		VisualizationViewer<GraphNode, String> vv = new VisualizationViewer<GraphNode, String>(
				new KKLayout<GraphNode, String>(g));

		vv.setPreferredSize(new Dimension(1000, 800)); // Sets the viewing area
		// Code duplication...
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String apply(Object node) {
				if (node instanceof ValueNode) {
					// TODO Not sure we can skip the visualization at all...
					return "VALUE NODE";
				} else if (node instanceof ObjectInstance) {
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

		JFrame frame = new JFrame("DataNode Dependency View");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}
	// public boolean addEdges(String v1, String v2) {
	// return g.addEdge("Edge" + Math.random(), v2, v1, EdgeType.UNDIRECTED);
	// }

	public boolean vertexExists(ObjectInstance oi) {
		return g.containsVertex(oi);
	}

	public boolean vertexExists(MethodInvocation mi) {
		return g.containsVertex(mi);
	}

	// Return all the nodes that are reacheable by anymeans from the method
	// under test
	public Set<MethodInvocation> getWeaklyConnectedComponentContaining(MethodInvocation methodInvocation) {
		// Find all the Method invocation nodes that are reachable via
		// Dependency Graph from the method invocation
		// Probably there's some bookeeping going on ?
		Set<MethodInvocation> dataDependent = new HashSet<>();
		Set<ObjectInstance> bookeeping = new HashSet<>();

		Queue<GraphNode> workList = new LinkedList<>();

		// Process direct predecessors, i.e., the PRE-CONDITIONS to invoke this
		// method.
		// workList.addAll(g.getPredecessors(methodInvocation));

		workList.add(methodInvocation);

		while (!workList.isEmpty()) {
			GraphNode node = workList.poll();

			if (dataDependent.contains(node) || bookeeping.contains(node) || node instanceof ValueNode) {
				continue;
			}

			if (node instanceof ObjectInstance && !bookeeping.contains(node)) {
				bookeeping.add((ObjectInstance) node);
				// Get the calls which are made on this object
				workList.addAll(getMethodInvocationsForOwner((ObjectInstance) node));
				// Get the calls which return this object
				workList.addAll( getMethodInvocationsWhichReturn((ObjectInstance) node));
			} else if (node instanceof MethodInvocation) {
				dataDependent.add((MethodInvocation) node);
				// Add all the preconditions
				workList.addAll(g.getPredecessors(node));
				workList.addAll(g.getSuccessors(node));
			}
		}

		return dataDependent;
	}

	public Set<MethodInvocation> getMethodInvocationsRecheableFrom(MethodInvocation methodInvocation) {
		// Find all the Method invocation nodes that are reachable via
		// Dependency Graph from the method invocation
		// Probably there's some bookeeping going on ?
		Set<MethodInvocation> dataDependent = new HashSet<>();
		Set<ObjectInstance> bookeeping = new HashSet<>();

		Queue<GraphNode> workList = new LinkedList<>();

		// Process direct predecessors, i.e., the PRE-CONDITIONS to invoke this
		// method.
		// workList.addAll(g.getPredecessors(methodInvocation));

		workList.add(methodInvocation);

		while (!workList.isEmpty()) {
			GraphNode node = workList.poll();

			if (dataDependent.contains(node)) {
				continue;
			}

			if (bookeeping.contains(node)) {
				continue;
			}

			if (node instanceof ValueNode) {
				continue;
			}

			if (node instanceof ObjectInstance && !bookeeping.contains(node)) {
				bookeeping.add((ObjectInstance) node);
				// Here we take both predecessors (i.e., the method which
				// returns this instance), and successors (i.e, the methods
				// which use this instance)

				/// TODO WE MUST FOLLOW ONLY OWNESRSHIP SUCCESSORS ?

				workList.addAll(getMethodInvocationsForOwner((ObjectInstance) node));
			} else if (node instanceof MethodInvocation) {
				dataDependent.add((MethodInvocation) node);
				// Add all the preconditions
				Collection<GraphNode> nodes = g.getPredecessors(node);
				workList.addAll(nodes);
			}
		}

		return dataDependent;
	}

	public Collection<ObjectInstance> getObjectInstances() {
		Collection<ObjectInstance> objectInstances = new ArrayList<>();
		// This probably is a copy... "view"
		for (GraphNode node : g.getVertices()) {
			if (node instanceof ObjectInstance) {
				objectInstances.add((ObjectInstance) node);
			}
		}
		return objectInstances;
	}

	// Return the local corresponding to the owner of this invocation
	public Local getObjectLocalFor(MethodInvocation methodInvocation) {
		for (GraphNode node : g.getVertices()) {
			if (node instanceof DataNode) {
				continue;
			} else if (node instanceof MethodInvocation) {
				if (((MethodInvocation) node).equals(methodInvocation)) {
					Set<String> dataDependencyEdges = new HashSet<String>(g.getInEdges(node));
					for (String edge : dataDependencyEdges) {
						if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
							g.getIncidentVertices(edge);
							return (Local) getValueFor((ObjectInstance) g.getOpposite(node, edge));
						}
					}
				}

			} else {
				logger.error("Unkonw type of node " + node);
			}

		}
		logger.warn("Cannot find ObjectLocal for " + methodInvocation.getJimpleMethod());
		return null;
	}

	public Local getReturnObjectLocalFor(MethodInvocation methodInvocation) {
		if (JimpleUtils.hasVoidReturnType(methodInvocation.getJimpleMethod())) {
			return null;
		}

		for (GraphNode node : g.getVertices()) {
			if (node instanceof DataNode) {
				continue;
			} else if (node instanceof MethodInvocation) {
				if (((MethodInvocation) node).equals(methodInvocation)) {
					// Really this should be max one
					Set<String> dataDependencyEdges = new HashSet<String>(g.getOutEdges(node));

					for (String edge : dataDependencyEdges) {
						if (edge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
							GraphNode returnValue = g.getOpposite(node, edge);

							if (returnValue instanceof ValueNode) {
								logger.info("ValueNodes are not tracked as return value !");
								return null;
							} else if (returnValue instanceof ObjectInstance) {
								return (Local) getValueFor((ObjectInstance) returnValue);
							}
						}
					}
				}

			} else {
				logger.error("Unkonw type of node " + node);
			}

		}
		// Can this be a null value ?
		logger.warn("Cannot find ObjectLocal for " + methodInvocation.getJimpleMethod());
		return null;
	}

	// Find the Local or Value nodes required for this method invocation...
	// order them by position (which is on the edge)
	public List<Value> getParametersSootValueFor(MethodInvocation methodInvocation) {
		try {
			int parameterCount = JimpleUtils.getParameterList(methodInvocation.getJimpleMethod()).length;

			int dataDependencyCount = g.getInEdges(methodInvocation).size();
			if (!methodInvocation.isStatic()) {
				// Remove the owner from the dependencies of this method
				dataDependencyCount = dataDependencyCount - 1;
			}

//			System.out.println("DataDependencyGraph.getParametersFor() " + methodInvocation + " with " + parameterCount
//					+ " formal parameters and " + (dataDependencyCount) + " actual parameters");

			if (parameterCount == 0) {
				return new ArrayList<Value>();
			}

			// Parameters must be ordered in the right order...
			Value[] parameters = new Value[dataDependencyCount];

			for (String incomingEdge : g.getInEdges(methodInvocation)) {
				if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {
					int position = Integer
							.parseInt(incomingEdge.replace(DATA_DEPENDENCY_PREFIX + "_", "").split("_")[0]);

					// Extract either the Value or the Local for the nodes which
					// set
					// the preconditions/parameters to this method invocation
					DataNode dn = (DataNode) (g.getOpposite(methodInvocation, incomingEdge));

					parameters[position] = getValueFor(dn);

					// System.out.println("DataDependencyGraph.getParametersFor()
					// Processing inEdge " + incomingEdge
					// + " position " + position + " corresponds to " +
					// g.getOpposite(methodInvocation, incomingEdge)
					// + " which has value " + getValueFor(dn));
				}
			}
			return Arrays.asList(parameters);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("DataDependencyGraph.getParametersSootValueFor() In Edges for " + methodInvocation + " "
					+ g.getInEdges(methodInvocation));
			System.out.println("DataDependencyGraph.getParametersSootValueFor() " + g);
			throw e;
		}
	}

	/**
	 * Generate a subgraph which contains the provided methodInvocations and
	 * their data dependencies (this, parameters, and returnValues)
	 * 
	 * @param carvedExecutionGraph
	 * @return
	 */
	public DataDependencyGraph getSubGraph(ExecutionFlowGraph carvedExecutionGraph) {
		return getSubGraph(carvedExecutionGraph.getOrderedMethodInvocations());
	}

	public DataDependencyGraph getSubGraph(List<MethodInvocation> orderedMerthodInvocations) {
		// For each node in carvedExecutionFlow we bring in the Object/Value
		// nodes and return types unless they are there, and connects the dots
		DataDependencyGraph subGraph = new DataDependencyGraph();
		// Inside this class we have direct access to internal representation
		for (MethodInvocation methodInvocation : orderedMerthodInvocations) {

			if (!subGraph.g.containsVertex(methodInvocation)) {
				subGraph.g.addVertex(methodInvocation);
			}

			// Precondition edges
			if (g.getInEdges(methodInvocation) != null) {
				for (String incomingEdge : g.getInEdges(methodInvocation)) {
					if (!subGraph.g.containsVertex(g.getOpposite(methodInvocation, incomingEdge))) {
						subGraph.g.addVertex(g.getOpposite(methodInvocation, incomingEdge));

						subGraph.additionalData.put((DataNode) g.getOpposite(methodInvocation, incomingEdge),
								additionalData.get(((DataNode) g.getOpposite(methodInvocation, incomingEdge))));

					}
					subGraph.g.addEdge(incomingEdge, g.getOpposite(methodInvocation, incomingEdge), methodInvocation,
							EdgeType.DIRECTED);
				}
			}

			// Returns
			if (g.getOutEdges(methodInvocation) != null) {
				for (String outgoingEdge : g.getOutEdges(methodInvocation)) {
					if (!subGraph.g.containsVertex(g.getOpposite(methodInvocation, outgoingEdge))) {
						subGraph.g.addVertex(g.getOpposite(methodInvocation, outgoingEdge));
						subGraph.additionalData.put((DataNode) g.getOpposite(methodInvocation, outgoingEdge),
								additionalData.get((DataNode) g.getOpposite(methodInvocation, outgoingEdge)));
					}

					subGraph.g.addEdge(outgoingEdge, methodInvocation, g.getOpposite(methodInvocation, outgoingEdge),
							EdgeType.DIRECTED);
				}
			}
		}

		return subGraph;
	}

	public Collection<MethodInvocation> getMethodInvocationsForOwner(ObjectInstance node) {
		Collection<MethodInvocation> methodInvocations = new HashSet<>();
		for (String edge : g.getOutEdges(node)) {
			if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				methodInvocations.add((MethodInvocation) g.getOpposite(node, edge));
			}
		}
		return methodInvocations;
	}

	// THIS DOES NOT WORK FOR STATIC METHOD INVOCATIONS
	public ObjectInstance getOwnerFor(MethodInvocation node) {
		for (String edge : g.getInEdges(node)) {
			if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				return (ObjectInstance) g.getOpposite(node, edge);
			}
		}
		//
		return null;
	}

	public void setValueFor(DataNode node, Value localVariable) {
		additionalData.put(node, localVariable);
	}

	public Value getValueFor(DataNode node) {
		return additionalData.get(node);
	}

	///
	public List<ObjectInstance> getObjectInstancesAsParametersOf(MethodInvocation methodInvocation) {

		List<ObjectInstance> parametersOf = new ArrayList<>();

		for (String incomingEdge : g.getInEdges(methodInvocation)) {
			if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {
				if (g.getOpposite(methodInvocation, incomingEdge) instanceof ObjectInstance) {
					parametersOf.add(((ObjectInstance) g.getOpposite(methodInvocation, incomingEdge)));
				}
			}
		}

		return parametersOf;
	}

	public MethodInvocation getInitMethodInvocationFor(ObjectInstance objectInstance) {
		// include the init call
		for (String outgoingEdge : g.getOutEdges(objectInstance)) {
			if (outgoingEdge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				if (g.getOpposite(objectInstance, outgoingEdge) instanceof MethodInvocation) {
					MethodInvocation methodInvocation = (MethodInvocation) g.getOpposite(objectInstance, outgoingEdge);
					if (methodInvocation.getJimpleMethod().contains("<init>")) {
						return ((MethodInvocation) g.getOpposite(objectInstance, outgoingEdge));
					}
				}
			}
		}
		throw new RuntimeException("Cannot find INIT call for " + objectInstance);
	}

	public Set<MethodInvocation> getMethodInvocationsWhichReturn(ObjectInstance objectInstance) {
		Set<MethodInvocation> returningMethodInvocations = new HashSet<>();

		for (String incomingEdge : g.getInEdges(objectInstance)) {
			// There should be only this kind nevertheless
			if (incomingEdge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
				// This should be the only possible case
				if (g.getOpposite(objectInstance, incomingEdge) instanceof MethodInvocation) {
					returningMethodInvocations.add(((MethodInvocation) g.getOpposite(objectInstance, incomingEdge)));
				}
			}
		}
		return returningMethodInvocations;
	}

	// This works similarly to subGraph but directly on this object
	public void refine(Set<MethodInvocation> connectedMethodInvocations) {
		Set<MethodInvocation> unconnected = new HashSet<>();
		for (GraphNode node : g.getVertices()) {
			if (node instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation) node;
				if (!connectedMethodInvocations.contains(node)) {
					// NOTE: since we are iterating and removing at the same time, this might create proble,s
					unconnected.add( mi );
				}
			}
		}
		//
		for( MethodInvocation mi : unconnected ){
			System.out.println("DataDependencyGraph.refine() Removing " + mi + " as unconnected ");
			g.removeVertex( mi );
		}
	}

}
