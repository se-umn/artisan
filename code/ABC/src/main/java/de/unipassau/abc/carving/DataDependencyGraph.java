package de.unipassau.abc.carving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.InputStream;
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

import de.unipassau.abc.utils.GraphUtility;
import de.unipassau.abc.utils.JimpleUtils;
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

	private Graph<GraphNode, String> graph;

	// It seems that Graph does not really return the same objects. Somehow if I
	// set complex attributes on a node by looking up that node once again I
	// loose them

	// So we store data OUTSIDE the graph...
	private Map<DataNode, Value> additionalData;

	public DataDependencyGraph() {
		graph = new SparseMultigraph<GraphNode, String>();
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
	@SuppressWarnings("unchecked")
	public void addMethodInvocation(MethodInvocation methodInvocation, String... actualParameters) {

		if (!graph.containsVertex(methodInvocation)) {
			graph.addVertex(methodInvocation);
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

			// DANGLING OBJECTS !
			if (!graph.containsVertex(node)) {

				// HERE We need to check if this data note is not yet define, if
				// this is an OBJECT might be an error, since all the objects
				// must
				// be define before use... There might be different explanations
				// here: the one we found is the objects like System.in gets
				// initialized inside java code, so we cannot see this. Hence we
				// cannot carve this. We use the simple heuristic here that we
				// force "System.in"
				if (node instanceof ObjectInstance) {

					// We use heuristics here:

					//
					// Check if this is System.in mocking
					if (((ObjectInstance) node).getType().startsWith(
							"org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock")) {
						node = ObjectInstance.SystemIn();
					} else if (((ObjectInstance) node).getType().endsWith("[]")) {
						// Check if this is an array... Shall we default to
						// empty array ?!
						// Otherwise, we might need to look at their serialized
						// form ?!
						System.out.println("DataDependencyGraph.addMethodInvocation() It's an array of type " + ((ObjectInstance) node).getType().replace("[]", ""));
						System.out.println("DataDependencyGraph.addMethodInvocation() WE SKIP ARRAY FOR THE MOMENT");

					} else {

						try {
							@SuppressWarnings("rawtypes")
							Class actualClass = Class.forName(((ObjectInstance) node).getType());
							if (InputStream.class.isAssignableFrom(actualClass)) {
								// System.out.println("DataDependencyGraph.addMethodInvocation()
								// FIRST SEEN " + node);
								node = ObjectInstance.SystemIn();
								// System.out.println("DataDependencyGraph.addMethodInvocation()
								// Patch with " + node);
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				graph.addVertex(node);

			}

			graph.addEdge(DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement(), node, methodInvocation,
					EdgeType.DIRECTED);
		}
	}

	// This will not work for static methods
	public void addDataDependencyOnReturn(MethodInvocation methodInvocation, String returnValue) {

		if (!graph.containsVertex(methodInvocation)) {
			graph.addVertex(methodInvocation);
		}

		//

		// extract formal parameters from method invocation, build a mask
		// <org.employee.Validation: int
		// numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
		String formalReturnValue = JimpleUtils.getReturnType(methodInvocation.getJimpleMethod());
		DataNode node = null;

		if (JimpleUtils.isVoid(formalReturnValue)) {
			return;
		} else if (JimpleUtils.isPrimitive(formalReturnValue)) {
			// If returnValue is null then it;s a problem, raise the exception
			// is the way to go !
			node = PrimitiveNodeFactory.get(formalReturnValue, returnValue);
			setValueFor(node, ((ValueNode) node).getData());
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
		if (!graph.containsVertex(node)) {
			graph.addVertex(node);
		}
		// METHOD -> RETURN

		graph.addEdge(RETURN_DEPENDENCY_PREFIX + id.getAndIncrement(), methodInvocation, node, EdgeType.DIRECTED);
	}

	// This will not work for static methods
	public void addDataDependencyOnOwner(MethodInvocation methodInvocation, String objectInstanceId) {
		if (!graph.containsVertex(methodInvocation)) {
			graph.addVertex(methodInvocation);
		}

		ObjectInstance oi = new ObjectInstance(objectInstanceId);
		methodInvocation.setOwner(oi);

		if (!graph.containsVertex(oi)) {
			graph.addVertex(oi);
		}
		// Dependency as owner of the method OBJECT -> INIT
		graph.addEdge(OWNERSHIP_DEPENDENCY_PREFIX + id.getAndIncrement(), oi, methodInvocation, EdgeType.DIRECTED);
	}

	// https://www.youtube.com/watch?v=I6eAA7tmgsQ
	public void visualize() {
		VisualizationViewer<GraphNode, String> vv = new VisualizationViewer<GraphNode, String>(
				new KKLayout<GraphNode, String>(graph));

		vv.setPreferredSize(new Dimension(1000, 800)); // Sets the viewing area
		// Code duplication...
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String apply(Object node) {
				if (node instanceof ValueNode) {
					// TODO Not sure we can skip the visualization at all...
					return "VALUE NODE";
					// } else if (node instanceof ObjectInstance) {
					// return ((ObjectInstance) node).getObjectId();
					// } else if (node instanceof MethodInvocation) {
					// return ((MethodInvocation) node).getJimpleMethod();
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
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}
	// public boolean addEdges(String v1, String v2) {
	// return graph.addEdge("Edge" + Math.random(), v2, v1,
	// EdgeType.UNDIRECTED);
	// }

	public boolean vertexExists(ObjectInstance oi) {
		return graph.containsVertex(oi);
	}

	public boolean vertexExists(MethodInvocation mi) {
		return graph.containsVertex(mi);
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
		// workList.addAll(graph.getPredecessors(methodInvocation));

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
				workList.addAll(getMethodInvocationsWhichReturn((ObjectInstance) node));
			} else if (node instanceof MethodInvocation) {
				dataDependent.add((MethodInvocation) node);
				// Add all the preconditions
				workList.addAll(getPredecessors(node));
				workList.addAll(getSuccessors(node));
			}
		}

		return dataDependent;
	}

	public Collection<GraphNode> getSuccessors(GraphNode node) {
		Collection<GraphNode> successors = graph.getSuccessors(node);
		return (Collection<GraphNode>) (successors != null ? successors : new HashSet<>());
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
		// workList.addAll(graph.getPredecessors(methodInvocation));

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
				Collection<GraphNode> nodes = getPredecessors(node);
				workList.addAll(nodes);
			}
		}

		return dataDependent;
	}

	public Collection<GraphNode> getPredecessors(GraphNode node) {
		Collection<GraphNode> predecessors = graph.getPredecessors(node);
		return (Collection<GraphNode>) (predecessors != null ? predecessors : new HashSet<>());
	}

	public Collection<ObjectInstance> getObjectInstances() {
		Collection<ObjectInstance> objectInstances = new ArrayList<>();
		// This probably is a copy... "view"
		for (GraphNode node : graph.getVertices()) {
			if (node instanceof ObjectInstance) {
				objectInstances.add((ObjectInstance) node);
			}
		}
		return objectInstances;
	}

	// Return the local corresponding to the owner of this invocation unless
	// this invocation is static
	public Local getObjectLocalFor(MethodInvocation methodInvocation) {
		if (methodInvocation.isStatic()) {
			return null;
		}

		for (GraphNode node : graph.getVertices()) {
			if (node instanceof DataNode) {
				continue;
			} else if (node instanceof MethodInvocation) {
				if (((MethodInvocation) node).equals(methodInvocation)) {
					Set<String> dataDependencyEdges = new HashSet<String>(graph.getInEdges(node));
					for (String edge : dataDependencyEdges) {
						if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
							// graph.getIncidentVertices(edge);
							return (Local) getValueFor((ObjectInstance) graph.getOpposite(node, edge));
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

	/*
	 * If the return type is a Primitive or an Object which is not used in the
	 * then this does not find it, because we did not tracked it !
	 * 
	 */
	public Value getReturnObjectLocalFor(MethodInvocation methodInvocation) {
		if (JimpleUtils.hasVoidReturnType(methodInvocation.getJimpleMethod())) {
			return null;
		}

		for (GraphNode node : graph.getVertices()) {
			if (node instanceof DataNode) {
				continue;
			} else if (node instanceof MethodInvocation) {
				if (((MethodInvocation) node).equals(methodInvocation)) {
					// Really this should be max one
					Set<String> dataDependencyEdges = new HashSet<String>(graph.getOutEdges(node));

					for (String edge : dataDependencyEdges) {
						if (edge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
							GraphNode returnValue = graph.getOpposite(node, edge);

							if (returnValue instanceof ValueNode) {
								return ((ValueNode) returnValue).getData();
							} else if (returnValue instanceof ObjectInstance) {
								return getValueFor((ObjectInstance) returnValue);
							}
						}
					}
				}

			} else {
				logger.error("Unkonw type of node " + node);
			}

		}
		// Can this be a null value, it's a value that we did not tracked (from
		// invokeStmts that do not have an assignment)
		logger.info("Cannot find ReturnObjectLocalFor for " + methodInvocation.getJimpleMethod());
		return null;
	}

	// Find the Local or Value nodes required for this method invocation...
	// order them by position (which is on the edge)
	public List<Value> getParametersSootValueFor(MethodInvocation methodInvocation) {
		try {
			int parameterCount = JimpleUtils.getParameterList(methodInvocation.getJimpleMethod()).length;

			int dataDependencyCount = graph.getInEdges(methodInvocation).size();
			if (!methodInvocation.isStatic()) {
				// Remove the owner from the dependencies of this method
				dataDependencyCount = dataDependencyCount - 1;
			}

			// System.out.println("DataDependencyGraph.getParametersFor() " +
			// methodInvocation + " with " + parameterCount
			// + " formal parameters and " + (dataDependencyCount) + " actual
			// parameters");

			if (parameterCount == 0) {
				return new ArrayList<Value>();
			}

			// Parameters must be ordered in the right order...
			Value[] parameters = new Value[dataDependencyCount];

			for (String incomingEdge : graph.getInEdges(methodInvocation)) {
				if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {
					int position = Integer
							.parseInt(incomingEdge.replace(DATA_DEPENDENCY_PREFIX + "_", "").split("_")[0]);

					// Extract either the Value or the Local for the nodes which
					// set
					// the preconditions/parameters to this method invocation
					DataNode dn = (DataNode) (graph.getOpposite(methodInvocation, incomingEdge));

					parameters[position] = getValueFor(dn);

					// System.out.println("DataDependencyGraph.getParametersFor()
					// Processing inEdge " + incomingEdge
					// + " position " + position + " corresponds to " +
					// graph.getOpposite(methodInvocation, incomingEdge)
					// + " which has value " + getValueFor(dn));
				}
			}
			return Arrays.asList(parameters);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("DataDependencyGraph.getParametersSootValueFor() In Edges for " + methodInvocation + " "
					+ graph.getInEdges(methodInvocation));
			System.out.println("DataDependencyGraph.getParametersSootValueFor() " + graph);
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

			if (!subGraph.graph.containsVertex(methodInvocation)) {
				subGraph.graph.addVertex(methodInvocation);
			}

			// Precondition edges
			if (graph.getInEdges(methodInvocation) != null) {
				for (String incomingEdge : graph.getInEdges(methodInvocation)) {
					if (!subGraph.graph.containsVertex(graph.getOpposite(methodInvocation, incomingEdge))) {
						subGraph.graph.addVertex(graph.getOpposite(methodInvocation, incomingEdge));

						subGraph.additionalData.put((DataNode) graph.getOpposite(methodInvocation, incomingEdge),
								additionalData.get(((DataNode) graph.getOpposite(methodInvocation, incomingEdge))));

					}
					subGraph.graph.addEdge(incomingEdge, graph.getOpposite(methodInvocation, incomingEdge),
							methodInvocation, EdgeType.DIRECTED);
				}
			}

			// Returns
			if (graph.getOutEdges(methodInvocation) != null) {
				for (String outgoingEdge : graph.getOutEdges(methodInvocation)) {
					if (!subGraph.graph.containsVertex(graph.getOpposite(methodInvocation, outgoingEdge))) {
						subGraph.graph.addVertex(graph.getOpposite(methodInvocation, outgoingEdge));
						subGraph.additionalData.put((DataNode) graph.getOpposite(methodInvocation, outgoingEdge),
								additionalData.get((DataNode) graph.getOpposite(methodInvocation, outgoingEdge)));
					}

					subGraph.graph.addEdge(outgoingEdge, methodInvocation,
							graph.getOpposite(methodInvocation, outgoingEdge), EdgeType.DIRECTED);
				}
			}
		}

		return subGraph;
	}

	public Collection<MethodInvocation> getMethodInvocationsForOwner(ObjectInstance node) {
		Collection<MethodInvocation> methodInvocations = new HashSet<>();
		for (String edge : graph.getOutEdges(node)) {
			if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				methodInvocations.add((MethodInvocation) graph.getOpposite(node, edge));
			}
		}
		return methodInvocations;
	}

	// THIS DOES NOT WORK FOR STATIC METHOD INVOCATIONS
	public ObjectInstance getOwnerFor(MethodInvocation node) {
		for (String edge : graph.getInEdges(node)) {
			if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				return (ObjectInstance) graph.getOpposite(node, edge);
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

		for (String incomingEdge : getIncomingEdges(methodInvocation)) {
			if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {
				if (graph.getOpposite(methodInvocation, incomingEdge) instanceof ObjectInstance) {
					parametersOf.add(((ObjectInstance) graph.getOpposite(methodInvocation, incomingEdge)));
				}
			}
		}

		return parametersOf;
	}

	private Collection<String> getIncomingEdges(MethodInvocation methodInvocation) {
		Collection<String> incomingEdges = graph.getInEdges(methodInvocation);
		return (incomingEdges != null) ? incomingEdges : new HashSet<String>();
	}

	public MethodInvocation getInitMethodInvocationFor(ObjectInstance objectInstance) {
		// include the init call
		for (String outgoingEdge : graph.getOutEdges(objectInstance)) {
			if (outgoingEdge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				if (graph.getOpposite(objectInstance, outgoingEdge) instanceof MethodInvocation) {
					MethodInvocation methodInvocation = (MethodInvocation) graph.getOpposite(objectInstance,
							outgoingEdge);
					if (methodInvocation.getJimpleMethod().contains("<init>")) {
						return ((MethodInvocation) graph.getOpposite(objectInstance, outgoingEdge));
					}
				}
			}
		}

		// TODO There's problem with mocked objects
		// java.lang.RuntimeException: Cannot find INIT call for
		// org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock@1869997857
		throw new RuntimeException("Cannot find INIT call for " + objectInstance);
	}

	public Set<MethodInvocation> getMethodInvocationsWhichReturn(ObjectInstance objectInstance) {
		Set<MethodInvocation> returningMethodInvocations = new HashSet<>();

		for (String incomingEdge : graph.getInEdges(objectInstance)) {
			// There should be only this kind nevertheless
			if (incomingEdge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
				// This should be the only possible case
				if (graph.getOpposite(objectInstance, incomingEdge) instanceof MethodInvocation) {
					returningMethodInvocations
							.add(((MethodInvocation) graph.getOpposite(objectInstance, incomingEdge)));
				}
			}
		}
		return returningMethodInvocations;
	}

	// This works similarly to subGraph but directly on this object
	public void refine(Set<MethodInvocation> connectedMethodInvocations) {
		Set<MethodInvocation> unconnected = new HashSet<>();
		for (GraphNode node : graph.getVertices()) {
			if (node instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation) node;
				if (!connectedMethodInvocations.contains(mi) && !mi.belongsToExternalInterface()) {
					unconnected.add(mi);
				}
			}
		}
		// This remove the node and the corresponding edges
		for (MethodInvocation mi : unconnected) {
			logger.trace("DataDependencyGraph.refine() Removing " + mi + " as unconnected ");
			graph.removeVertex(mi);
		}
		// Eventually remove data nodes that are unconnected, i.e., invoke no
		// methods.
		Set<DataNode> unconnectedData = new HashSet<>();
		for (GraphNode node : graph.getVertices()) {
			if (node instanceof DataNode) {
				if (graph.getInEdges(node).isEmpty() && graph.getOutEdges(node).isEmpty()) {
					unconnectedData.add((DataNode) node);
				}
			}
		}
		for (DataNode dataNode : unconnectedData) {
			logger.trace("DataDependencyGraph.refine() Removing " + dataNode + " as unconnected ");
			graph.removeVertex(dataNode);
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalData == null) ? 0 : additionalData.hashCode());
		// TODO This is not accurate since graph does not implment propert hash
		// and equals
		result = prime * result + ((graph == null) ? 0 : graph.hashCode());
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
		DataDependencyGraph other = (DataDependencyGraph) obj;
		if (additionalData == null) {
			if (other.additionalData != null)
				return false;
		} else if (!additionalData.equals(other.additionalData))
			return false;

		return GraphUtility.areDataDependencyGraphsEqual(graph, other.graph);
	}

}
