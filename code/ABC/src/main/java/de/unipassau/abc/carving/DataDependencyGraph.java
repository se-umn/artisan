package de.unipassau.abc.carving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

import de.unipassau.abc.carving.ObjectInstance.StaticObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.GraphUtility;
import de.unipassau.abc.utils.JimpleUtils;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import soot.Local;
import soot.Value;
import soot.jimple.NullConstant;

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
//		graph = new SparseMultigraph<GraphNode, String>();
		graph = new DirectedSparseMultigraph<GraphNode, String>();
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

//		System.out.println("DataDependencyGraph.addMethodInvocation() " + methodInvocation + " "
//				+ Arrays.toString(actualParameters));

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
				setSootValueFor(node, ((ValueNode) node).getData());
			} else if ("java.lang.System.in".equals(actualParameters[position])) {
				node = ObjectInstance.systemIn;
			} else if ("java.lang.System.out".equals(actualParameters[position])) {
				node = ObjectInstance.systemOut;
			} else if ("java.lang.System.err".equals(actualParameters[position])) {
				node = ObjectInstance.systemErr;
			} else if( "StaticFieldOperation".equals( methodInvocation.getInvocationType())){
				node = new StaticObjectInstance( actualParameters[position]+"@0", JimpleUtils.getReturnType( methodInvocation.getJimpleMethod() ) );
			}
			// else if (actualParameters[position].equals("java.lang.String@0"))
			// {
			// NULL string
			// node = NullNodeFactory.get("java.lang.String");
			// setSootValueFor(node, ((ValueNode) node).getData());
			// }
			else if (actualParameters[position].split("@").length == 2) {
				// Objects
				if (JimpleUtils.isNull(actualParameters[position])) {
					// Null objects
					// Not even sure that at this point here this can be null !
					// System.out.println("DataDependencyGraph.addMethodInvocation()
					// Got null " + methodInvocation + " "
					// + actualParameters[position]);
					// Null parameter
					node = NullNodeFactory.get(formalParameters[position]);
					setSootValueFor(node, ((ValueNode) node).getData());
				} else {
					node = new ObjectInstance(actualParameters[position]);
				}
			}
			// else {
			// Regular Strings
			// node = StringNodeFactory.get(actualParameters[position]);
			// }

			// DANGLING OBJECTS !
			if (!graph.containsVertex(node)) {

				if (node instanceof ObjectInstance) {
					node = handleDanglingObjectInstance((ObjectInstance) node);
				}

				graph.addVertex(node);

			}

			graph.addEdge(DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement(), node, methodInvocation,
					EdgeType.DIRECTED);
		}

	}

	// HERE We need to check if this data note is not yet define, if
	// this is an OBJECT might be an error, since all the objects
	// must
	// be define before use... There might be different explanations
	// here: the one we found is the objects like System.in gets
	// initialized inside java code, so we cannot see this. Hence we
	// cannot carve this. We use the simple heuristic here that we
	// force "System.in"
	private ObjectInstance handleDanglingObjectInstance(ObjectInstance node) {

		if (JimpleUtils.isNull(node.getObjectId())) {
			// System.out.println("Call a method on null object... NPE?!");
			// Null parameter
			setSootValueFor(node, NullConstant.v());
		} else if (((ObjectInstance) node).getType()
				.startsWith("org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock")) {
			// TODO This feels wrong... Check if this is System.in mocking
			logger.debug("DataDependencyGraph.addMethodInvocation() Aliasing " + node + " with "
					+ ObjectInstance.systemIn);
			return ObjectInstance.systemIn;
		} else if (((ObjectInstance) node).getType().endsWith("[]")) {
			return node;
		} else {
			// For some reason, the tracer reports an interface
			// level ref instead of an implementation level one
			// for Path and UnixPath. Therefore we use the following
			// heuristic: if in the graph there's already an object
			// with the same
			// system id and which implements and interface (or
			// maybe is implemented by?) we merge the two....
			// TODO We might track ALIAS relations now and then
			// merge those...
			// TODO Not sure if we always see first the interface
			// and then the implementation, tho

			// This returns the FIRST one there, does not check if there's more
			// than one
			ObjectInstance alias = getVertexById(((ObjectInstance) node).getObjectId());
			if (alias != null) {
				// TODO Check interface !
				logger.debug("DataDependencyGraph.addMethodInvocation() Aliasing " + node + " with " + alias);
				return alias;
			}
		}

		// System.out.println("DataDependencyGraph.handleDanglingObjectInstance()
		// Return the original node");
		// Worst case return the original node
		return node;

	}

	// TODO We might need to check that there no 2 objects with same id...
	private ObjectInstance getVertexById(String objectId) {
		// We need to extract the id
		for (ObjectInstance objectInstance : getObjectInstances()) {
			if (objectInstance.getObjectId().split("@")[1].equals(objectId.split("@")[1])) {
				return objectInstance;
			}
		}
		return null;
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
			node = PrimitiveNodeFactory.get(formalReturnValue, returnValue);
			setSootValueFor(node, ((ValueNode) node).getData());
		}
		// else if (JimpleUtils.isString(formalReturnValue)) {
		// // FIXME How to handle NULL string?
		// node = StringNodeFactory.get(returnValue);
		// System.out.println("DataDependencyGraph.addDataDependencyOnReturn()
		// NEW STRING NODE " + node);
		// }
		else if (JimpleUtils.isNull(returnValue)) {
			// System.out.println("DataDependencyGraph.addDataDependencyOnReturn()
			// Capture a null return value");
			node = NullNodeFactory.get(formalReturnValue);
			setSootValueFor(node, ((ValueNode) node).getData());
		}
		// else if (JimpleUtils.isString(formalReturnValue)) {
		// // Note: We store non null string by Value
		// node = StringNodeFactory.get(returnValue);
		// setSootValueFor(node, ((ValueNode) node).getData());
		// }
		else {
			node = new ObjectInstance(returnValue);
		}

		// DANGLING OBJECTS !
		if (!graph.containsVertex(node)) {
			if (node instanceof ObjectInstance) {
				node = handleDanglingObjectInstance((ObjectInstance) node);
			}

			graph.addVertex(node);

		}

		graph.addEdge(RETURN_DEPENDENCY_PREFIX + id.getAndIncrement(), methodInvocation, node, EdgeType.DIRECTED);
	}

	// This will not work for static methods
	public void addDataDependencyOnOwner(MethodInvocation methodInvocation, String objectInstanceId) {

		if (!graph.containsVertex(methodInvocation)) {
			graph.addVertex(methodInvocation);
		}

		// Handle Strings
		ObjectInstance node = null;
		// if
		// (JimpleUtils.isString(JimpleUtils.getClassNameForMethod(methodInvocation.getJimpleMethod())))
		// {
		// node = StringNodeFactory.get(objectInstanceId);
		// }
		// else {
		node = new ObjectInstance(objectInstanceId);
		// }

		if (!graph.containsVertex(node)) {
			if (node instanceof ObjectInstance) {
				node = handleDanglingObjectInstance((ObjectInstance) node);
			}
			graph.addVertex(node);
		}

		methodInvocation.setOwner(node);
		// Dependency as owner of the method OBJECT -> INIT
		graph.addEdge(OWNERSHIP_DEPENDENCY_PREFIX + id.getAndIncrement(), node, methodInvocation, EdgeType.DIRECTED);
	}

	// https://www.youtube.com/watch?v=I6eAA7tmgsQ
	public void visualize() {
		VisualizationViewer<GraphNode, String> vv = new VisualizationViewer<GraphNode, String>(
				new KKLayout<GraphNode, String>(graph));

		vv.setPreferredSize(new Dimension(2000, 1600)); // Sets the viewing area
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

	// Return all the nodes that are reacheable by any means from the method
	// under test. This does not include the methodInvocation
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
		// This is a shallow copy
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
					Set<String> dataDependencyEdges = new HashSet<String>(getIncomingEdges(node));
					for (String edge : dataDependencyEdges) {
						if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {

							ObjectInstance objectInstance = (ObjectInstance) graph.getOpposite(node, edge);
							return (Local) getSootValueFor(objectInstance);

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
					Set<String> dataDependencyEdges = new HashSet<String>(getOutgoingEgdes(node));

					for (String edge : dataDependencyEdges) {
						if (edge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
							GraphNode returnValue = graph.getOpposite(node, edge);

							if (returnValue instanceof ValueNode) {
								return ((ValueNode) returnValue).getData();
							} else if (returnValue instanceof ObjectInstance) {
								return getSootValueFor((ObjectInstance) returnValue);
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
		logger.warn("Cannot find ReturnObjectLocalFor for " + methodInvocation.getJimpleMethod());
		return null;
	}

	// Find the Local or Value nodes required for this method invocation...
	// order them by position (which is on the edge)
	public List<Value> getParametersSootValueFor(MethodInvocation methodInvocation) {
		try {
			int parameterCount = JimpleUtils.getParameterList(methodInvocation.getJimpleMethod()).length;

			int dataDependencyCount = getIncomingEdges(methodInvocation).size();
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

			for (String incomingEdge : getIncomingEdges(methodInvocation)) {
				if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {
					int position = Integer
							.parseInt(incomingEdge.replace(DATA_DEPENDENCY_PREFIX + "_", "").split("_")[0]);

					// Extract either the Value or the Local for the nodes which
					// set
					// the preconditions/parameters to this method invocation
					DataNode dn = (DataNode) (graph.getOpposite(methodInvocation, incomingEdge));

					parameters[position] = getSootValueFor(dn);

					logger.debug(
							"DataDependencyGraph.getParametersFor() Processing inEdge " + incomingEdge + " position "
									+ position + " corresponds to " + graph.getOpposite(methodInvocation, incomingEdge)
									+ " which has value " + getSootValueFor(dn));
				}
			}
			return Arrays.asList(parameters);
		} catch (Throwable e) {
			e.printStackTrace();
			// System.out.println("DataDependencyGraph.getParametersSootValueFor()
			// In Edges for " + methodInvocation + " "
			// + getIncomingEgdes(methodInvocation));
			// System.out.println("DataDependencyGraph.getParametersSootValueFor()
			// " + graph);
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
			if (getIncomingEdges(methodInvocation) != null) {
				for (String incomingEdge : getIncomingEdges(methodInvocation)) {
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
			for (String outgoingEdge : getOutgoingEgdes(methodInvocation)) {
				if (!subGraph.graph.containsVertex(graph.getOpposite(methodInvocation, outgoingEdge))) {
					subGraph.graph.addVertex(graph.getOpposite(methodInvocation, outgoingEdge));
					subGraph.additionalData.put((DataNode) graph.getOpposite(methodInvocation, outgoingEdge),
							additionalData.get((DataNode) graph.getOpposite(methodInvocation, outgoingEdge)));
				}

				subGraph.graph.addEdge(outgoingEdge, methodInvocation,
						graph.getOpposite(methodInvocation, outgoingEdge), EdgeType.DIRECTED);
			}
		}

		return subGraph;
	}

	public Collection<MethodInvocation> getMethodInvocationsForOwner(ObjectInstance node) {
		Collection<MethodInvocation> methodInvocations = new HashSet<>();
		for (String edge : getOutgoingEgdes(node)) {
			if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				methodInvocations.add((MethodInvocation) graph.getOpposite(node, edge));
			}
		}
		return methodInvocations;
	}

	// THIS DOES NOT WORK FOR STATIC METHOD INVOCATIONS
	public ObjectInstance getOwnerFor(MethodInvocation node) {
		for (String edge : getIncomingEdges(node)) {
			if (edge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
				return (ObjectInstance) graph.getOpposite(node, edge);
			}
		}
		//
		return null;
	}

	public void setSootValueFor(DataNode node, Value localVariable) {
		// System.out.println("DataDependencyGraph.setSootValueFor() " + node +
		// " " + localVariable);
		additionalData.put(node, localVariable);
	}

	public Value getSootValueFor(DataNode node) {
		// For the strings this can be null, so we look it up in the XML !

		return additionalData.get(node);
	}

	///
	public List<ObjectInstance> getParametersOf(MethodInvocation methodInvocation) {

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

	public Collection<String> getIncomingEdges(GraphNode methodInvocation) {
		Collection<String> incomingEdges = graph.getInEdges(methodInvocation);
		return (incomingEdges != null) ? incomingEdges : new HashSet<String>();
	}

	/// Note that static instances have no init method ...
	public MethodInvocation getInitMethodInvocationFor(ObjectInstance objectInstance) {

		// include the init call
		for (String outgoingEdge : getOutgoingEgdes(objectInstance)) {
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

		return null;
		// NOTE: For some object there's no INIT call, but there might be a call
		// which RETURN them. Especially if this call which returns them is an
		// EXTERNAL INTERFACE
		// throw new CarvingException("Cannot find INIT call for " +
		// objectInstance);
	}

	public Collection<String> getOutgoingEgdes(GraphNode objectInstance) {
		Collection<String> outgoingEdges = graph.getOutEdges(objectInstance);
		return (outgoingEdges != null) ? outgoingEdges : Collections.EMPTY_LIST;
	}

	public Set<MethodInvocation> getMethodInvocationsWhichReturn(ObjectInstance objectInstance) {
		Set<MethodInvocation> returningMethodInvocations = new HashSet<>();

		for (String incomingEdge : getIncomingEdges(objectInstance)) {
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
	public void refine(Set<MethodInvocation> requiredMethodInvocations) {
		Set<MethodInvocation> unconnected = new HashSet<>();
		for (GraphNode node : graph.getVertices()) {
			if (node instanceof MethodInvocation) {
				MethodInvocation mi = (MethodInvocation) node;

				if (!requiredMethodInvocations.contains(mi)) {
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
				if (getIncomingEdges(node).isEmpty() && getOutgoingEgdes(node).isEmpty()) {
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

	/**
	 * Return all the object instances which have at least one invocation before
	 * the methodInvocationToCarve and TODO belongs to external interface.
	 * 
	 * @param methodInvocationToCarve
	 * @return
	 */
	@Deprecated
	public Set<ObjectInstance> getObjectInstancesUsedBefore(MethodInvocation methodInvocationToCarve) {
		Set<ObjectInstance> objectInstances = new HashSet<>();
		for (ObjectInstance objectInstance : getObjectInstances()) {
			// Check if any of this invocation was before the given one
			List<MethodInvocation> mi = new ArrayList<>(getMethodInvocationsForOwner(objectInstance));
			Collections.sort(mi);

			Iterator<MethodInvocation> iterator = mi.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getInvocationCount() < methodInvocationToCarve.getInvocationCount()) {
					objectInstances.add(objectInstance);
					break;
				}
			}
		}

		return objectInstances;
	}

	@Deprecated
	public Set<ObjectInstance> getObjectInstancesUsedBeforeByExternalInterfaces(
			MethodInvocation methodInvocationToCarve) {
		Set<ObjectInstance> objectInstances = new HashSet<>();
		for (ObjectInstance objectInstance : getObjectInstances()) {

			// Check if any of this invocation was before the given one
			// What about parameters?
			List<MethodInvocation> miForObjectInstance = new ArrayList<>(getMethodInvocationsForOwner(objectInstance));
			// Includes the constructor
			miForObjectInstance.addAll(getMethodInvocationsWhichReturn(objectInstance));
			// Parameter
			miForObjectInstance.addAll(getMethodInvocationsWhichUse(objectInstance));

			Collections.sort(miForObjectInstance);

			Iterator<MethodInvocation> iterator = miForObjectInstance.iterator();
			while (iterator.hasNext()) {
				MethodInvocation mi = iterator.next();
				// We do not care about objects that
				if (!mi.belongsToExternalInterface()) {
					continue;
				} else if (mi.getInvocationCount() < methodInvocationToCarve.getInvocationCount()) {
					objectInstances.add(objectInstance);
					break;
				}
			}
		}
		return objectInstances;
	}

	/**
	 * Return all the method calls which have the given object as parameter
	 * 
	 * @param objectInstance
	 * @return
	 */
	public Set<MethodInvocation> getMethodInvocationsWhichUse(ObjectInstance objectInstance) {
		Set<MethodInvocation> methodInvocations = new HashSet<>();
		for (String edge : getOutgoingEgdes(objectInstance)) {
			if (edge.startsWith(DATA_DEPENDENCY_PREFIX)) {
				methodInvocations.add((MethodInvocation) graph.getOpposite(objectInstance, edge));
			}
		}
		return methodInvocations;
	}

	public void markNodeAsExternalInterface(MethodInvocation mi) {
		// Collecte edges
		
		// TODO Those might not really realiable !!
		Collection<Pair<GraphNode, String>> predecessors = new HashSet<>();
		for( String inEdge : graph.getInEdges( mi ) ){
			predecessors.add( new Pair<GraphNode, String>( graph.getOpposite(mi, inEdge), inEdge));
		}
		
		Collection<Pair<GraphNode, String>> successors = new HashSet<>();
		for( String outEdge : graph.getOutEdges( mi ) ){
			predecessors.add( new Pair<GraphNode, String>( graph.getOpposite(mi, outEdge), outEdge));
		}
		
		// Remove the node, this shall remove also the edges
		graph.removeVertex( mi );
		// Update the node content
		mi.setBelongsToExternalInterface(true);
		// Reinsert the node
		graph.addVertex( mi );
		// Reinsert the edges
		for(  Pair<GraphNode, String> predecessor : predecessors ){
			graph.addEdge( predecessor.getSecond(), predecessor.getFirst(), mi, EdgeType.DIRECTED);
		}
		for(  Pair<GraphNode, String> successor : successors ){
			graph.addEdge( successor.getSecond(), mi, successor.getFirst(), EdgeType.DIRECTED);
		}
	}

}
