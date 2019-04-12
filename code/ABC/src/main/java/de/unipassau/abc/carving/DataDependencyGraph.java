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
import java.util.Optional;
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
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.Value;
import soot.jimple.NullConstant;

/**
 * @author gambi
 */
public class DataDependencyGraph {

    private static Logger logger = LoggerFactory.getLogger(DataDependencyGraph.class);

    public static final String OWNERSHIP_DEPENDENCY_PREFIX = "Ownership";

    // Parameter to a call
    public static final String DATA_DEPENDENCY_PREFIX = "DataNode";

    public static final String RETURN_DEPENDENCY_PREFIX = "Return";
    public static final String ALIAS_DEPENDENCY_PREFIX = "Aliasing";

    private static AtomicInteger id = new AtomicInteger(0);

    /*
     * It seems that Graph does not really return the same objects. Somehow if I
     * set complex attributes on a node by looking up that node once again I
     * loose them. We need to use addUserDatum or re-implement the clone method
     * of the data objects. Check
     * http://www.datalab.uci.edu/papers/JUNG_tech_report.html
     * 
     * In particular we should use UserData.SHARED to pass around the references
     * ?
     */
    private Graph<GraphNode, String> graph;
    // So we store data OUTSIDE the graph...
    private Map<DataNode, Value> additionalData;

    public DataDependencyGraph() {
        // graph = new SparseMultigraph<GraphNode, String>();
        graph = new DirectedSparseMultigraph<GraphNode, String>();
        additionalData = new HashMap<>();
    }

    public void addMethodInvocationWithoutAnyDependency(MethodInvocation methodInvocation) {
        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }

    }

    public void addDataDependencyOnActualParameter(MethodInvocation methodInvocation, DataNode actualParameter,
            int position) {
        DataNode node = null;

        if (!graph.containsVertex(actualParameter)) {

            // Null instances are treated like primitive types, but they are
            // unique ... otherwise we create all sort of deps over null
            if (actualParameter instanceof ObjectInstance) {
                node = handleDanglingObjectInstance((ObjectInstance) actualParameter);
            } else {
                // Primitives, Strings, etc.
                node = actualParameter;
            }

            graph.addVertex(node);

        } else {
            node = actualParameter;
        }

        graph.addEdge(DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement(), node, methodInvocation,
                EdgeType.DIRECTED);
    }

    public void addDataDependencyOnReturn(MethodInvocation methodInvocation, DataNode returnValue) {

        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }

        // DANGLING OBJECTS !
        DataNode node = returnValue;
        if (!graph.containsVertex(returnValue)) {
            if (node instanceof ObjectInstance) {
                node = handleDanglingObjectInstance((ObjectInstance) returnValue);
            } else {
                // Primitives, Strings, etc.
                node = returnValue;
            }
            graph.addVertex(node);
        } else {
            node = returnValue;
        }

        graph.addEdge(RETURN_DEPENDENCY_PREFIX + id.getAndIncrement(), methodInvocation, node, EdgeType.DIRECTED);
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
    public void addMethodInvocation(MethodInvocation methodInvocation) {

        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }

        String[] actualParameters = methodInvocation.getActualParameters();
        String[] formalParameters = JimpleUtils.getParameterList(methodInvocation.getMethodSignature());

        /*
         * An object of type String might be passed around as Object, that is
         * the formal parameter of the call is Object, the actual parameter is a
         * String
         */

        for (int position = 0; position < formalParameters.length; position++) {
            DataNode node = null;

            if (JimpleUtils.isPrimitive(formalParameters[position])) {
                node = PrimitiveNodeFactory.get(formalParameters[position], actualParameters[position]);
                // setSootValueFor(node, ((ValueNode) node).getData());
            } else if (JimpleUtils.isString(formalParameters[position])) {
                // We need to explicitly use java.lang.String here and not the
                // formal parameter, since that might be a super type (i.e.,
                // java.lang.Object)
                node = PrimitiveNodeFactory.get("java.lang.String", actualParameters[position]);
                // setSootValueFor(node, ((ValueNode) node).getData());
            } else if (JimpleUtils.isStringContent(actualParameters[position])) {
                // We need to explicitly use java.lang.String here and not the
                // formal parameter, since that might be a super type (i.e.,
                // java.lang.Object)
                node = PrimitiveNodeFactory.get("java.lang.String", actualParameters[position]);
                // setSootValueFor(node, ((ValueNode) node).getData());
            }
            // We need to check those hard coded constants as first... TODO
            // refactor to deal with system constant using artificial operation
            // maybe ?!
            else if ("java.lang.System.in".equals(actualParameters[position])) {
                node = ObjectInstance.systemIn;
            } else if ("java.lang.System.out".equals(actualParameters[position])) {
                node = ObjectInstance.systemOut;
            } else if ("java.lang.System.err".equals(actualParameters[position])) {
                node = ObjectInstance.systemErr;
            } else if ("StaticFieldOperation".equals(methodInvocation.getInvocationType())) {
                node = new StaticObjectInstance(actualParameters[position] + "@0",
                        JimpleUtils.getReturnType(methodInvocation.getMethodSignature()));
            }
            // else if (JimpleUtils.isString(formalParameters[position]) &&
            // !Carver.STRINGS_AS_OBJECTS) {
            // // Since the string here is a PARAMETER somewhere its value has
            // // been set, hence stored in the data dependency graph, we need
            // // to retrieve it.
            // // Look up the node
            // for (GraphNode vertex : graph.getVertices()) {
            // if (vertex instanceof PrimitiveValue) {
            // if (((PrimitiveValue) vertex).getRefid() != null) {
            // // This is a string node - TODO Refactor add a map
            // // or index to fast access to those nodes !
            // if (actualParameters[position].equals(((PrimitiveValue)
            // vertex).getRefid())) {
            //// System.out.println(
            //// "DataDependencyGraph.addMethodInvocation() Found the Primitive
            // Node for "
            //// + actualParameters[position]);
            // // We need to generate a new one for each use,
            // // so there's no deps on them.
            // node = PrimitiveNodeFactory.get(formalParameters[position],
            // ((PrimitiveValue) vertex).getStringValue());
            // setSootValueFor(node, ((ValueNode) node).getData());
            // break;
            // }
            // }
            // }
            // }
            //
            // // If there's no matching node, that's an error ...
            // if (node == null) {
            //
            // if (actualParameters[position].equals("java.lang.String@0")) {
            // node = NullNodeFactory.get("java.lang.Sting");
            // setSootValueFor(node, ((ValueNode) node).getData());
            // }
            // }
            //
            // if (node == null) {
            // throw new RuntimeException("Cannot find node value for String " +
            // actualParameters[position]);
            // }
            //
            // }
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
                // else {
                // node ?
                // }

                graph.addVertex(node);

            }

            graph.addEdge(DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement(), node, methodInvocation,
                    EdgeType.DIRECTED);
        }

    }

    /*
     * HERE We need to check if this data note is not yet define, if this is an
     * OBJECT might be an error, since all the objects must be define before
     * use... There might be different explanations here: the one we found is
     * the objects like System.in gets initialized inside java code, so we
     * cannot see this. Hence we cannot carve this. We use the simple heuristic
     * here that we force "System.in"
     */
    private ObjectInstance handleDanglingObjectInstance(ObjectInstance node) {

        if (JimpleUtils.isNull(node.getObjectId())) {
            // System.out.println("Call a method on null object... NPE?!");
            // Null parameter
            setSootValueFor(node, NullConstant.v());
        } else if (((ObjectInstance) node).getType()
                .startsWith("org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock")) {
            // TODO This feels wrong... Check if this is System.in mocking
            logger.debug(
                    "DataDependencyGraph.addMethodInvocation() Aliasing " + node + " with " + ObjectInstance.systemIn);
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

            /*
             * TODO: Aliasing based on system id is not reliable on Delvik, so
             * use a better heuristic which considers the type of the objects...
             * this require type information to be available.. which is not ATM
             */

            if (alias != null && compatibleClasses(node, alias)) {

                // Recording the aliasing relation - This is bidirectional
                graph.addEdge(ALIAS_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), node, alias, EdgeType.DIRECTED);
                graph.addEdge(ALIAS_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), alias, node, EdgeType.DIRECTED);
                logger.debug("DataDependencyGraph.addMethodInvocation() Aliasing " + node + " with " + alias);
                // TODO MAYBE we shall encode directly here the type for the alias relation, and only have it one-directional?
//                Maybe we can directly "retype" ? But to what? there might be more options ....
            }
        }

        // System.out.println("DataDependencyGraph.handleDanglingObjectInstance()
        // Return the original node");
        // Worst case return the original node
        return node;

    }

    private boolean compatibleClasses(ObjectInstance node, ObjectInstance alias) {
        return isAssignableFrom(node, alias) || isAssignableFrom(alias, node);
    }

    private boolean isAssignableFrom(ObjectInstance alias, ObjectInstance node) {
        // If the ONLY common super type is Object and they do not implement any
        // alias.isAssignableFrom(Foo.class) will be true whenever the class
        // represented by the alias object
        // is a superclass or superinterface of node.class.
        if (alias.getType().equals(node.getType())) {
            return true;
        }

        SootClass aliasClass = Scene.v().getSootClass(alias.getType());
        SootClass nodeClass = Scene.v().getSootClass(node.getType());

        // Compare classes
        if (!aliasClass.isInterface() && !nodeClass.isInterface()) {
            return Scene.v().getActiveHierarchy().isClassSuperclassOf(aliasClass, nodeClass);
        }
        // Compare interfaces
        if (aliasClass.isInterface() && nodeClass.isInterface()) {
            return Scene.v().getActiveHierarchy().isInterfaceSuperinterfaceOf(aliasClass, nodeClass);
        }
        
        // Does node implement alias ?
        if( aliasClass.isInterface() && ! nodeClass.isInterface()){
            return Scene.v().getActiveHierarchy().getImplementersOf( aliasClass ).contains( nodeClass );
        }

        // An interface cannot be a subclass of a class
        return false;

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
    // THIS IS A PATCH TO ALLOW STORING ADDITIONAL DATA IN STRING NODES...
    public DataNode addDataDependencyOnReturn(MethodInvocation methodInvocation, String returnValue) {

        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }

        //

        // extract formal parameters from method invocation, build a mask
        // <org.employee.Validation: int
        // numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
        String formalReturnValue = JimpleUtils.getReturnType(methodInvocation.getMethodSignature());
        DataNode node = null;

        if (JimpleUtils.isVoid(formalReturnValue)) {
            return null;
        } else if (JimpleUtils.isPrimitive(formalReturnValue)) {
            node = PrimitiveNodeFactory.get(formalReturnValue, returnValue);
            // setSootValueFor(node, ((ValueNode) node).getData());
        } else if (!Carver.STRINGS_AS_OBJECTS && JimpleUtils.isString(formalReturnValue)) {
            // This might be tweaked
            node = PrimitiveNodeFactory.createStringNode(formalReturnValue, returnValue);
            // setSootValueFor(node, ((ValueNode) node).getData());
        } else if (JimpleUtils.isNull(returnValue)) {
            // System.out.println("DataDependencyGraph.addDataDependencyOnReturn()
            // Capture a null return value");
            node = NullNodeFactory.get(formalReturnValue);
            // setSootValueFor(node, ((ValueNode) node).getData());
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
        //
        return node;
    }

    /**
     * Method Owners are always ObjectInstances !
     * 
     * @param methodInvocation
     * @param objectInstance
     */
    public void addDataDependencyOnOwner(MethodInvocation methodInvocation, ObjectInstance objectInstance) {
        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }

        ObjectInstance node = null;
        if (!graph.containsVertex(objectInstance)) {
            // TODO not sure what's this ...
            node = handleDanglingObjectInstance((ObjectInstance) objectInstance);
            graph.addVertex(node);
        } else {
            node = objectInstance;
        }

        methodInvocation.setOwner(node);
        // Dependency as owner of the method OBJECT -> INIT
        boolean added = graph.addEdge(OWNERSHIP_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), node, methodInvocation,
                EdgeType.DIRECTED);
        if (!added) {
            logger.warn("Cannot add Edge " + OWNERSHIP_DEPENDENCY_PREFIX + "_" + id.intValue() + " from " + node
                    + " to " + methodInvocation);
        }

    }

    public void addDataDependencyOnOwner(MethodInvocation methodInvocation, String objectInstanceId) {
        // Owner is assumed to be not null...
        ObjectInstance node = ObjectInstanceFactory.get(objectInstanceId);
        addDataDependencyOnOwner(methodInvocation, node);
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

            // If ( ! STRINGS_AS_OBJECTS ) then string nodes are of type
            // ValueNode !
            if (node instanceof ValueNode) {
                continue;
            }

            if (node instanceof ObjectInstance && !bookeeping.contains(node)) {

                // TODO NOT SURE ABOUT THIS... SHALL WE BOOK KEEP IT ?
                bookeeping.add((ObjectInstance) node);

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
        logger.warn("Cannot find ObjectLocal for " + methodInvocation.getMethodSignature());
        return null;
    }

    /*
     * If the return type is a Primitive or an Object which is not used in the
     * then this does not find it, because we did not tracked it !
     * 
     */
    public Value getReturnObjectLocalFor(MethodInvocation methodInvocation) {
        if (JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())) {
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
        logger.warn("Cannot find ReturnObjectLocalFor for " + methodInvocation.getMethodSignature());
        return null;
    }

    // Find the Local or Value nodes required for this method invocation...
    // order them by position (which is on the edge)
    public List<Value> getParametersSootValueFor(MethodInvocation methodInvocation) {
        try {
            int parameterCount = JimpleUtils.getParameterList(methodInvocation.getMethodSignature()).length;

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
    // Soot calls super.<init> recursively for each super class of this instance
    /// before calling the actual
    // <init> method. So we need to return the one which actually matches the
    /// type of the object
    //
    public MethodInvocation getInitMethodInvocationFor(ObjectInstance objectInstance) {

        // include the init call
        for (String outgoingEdge : getOutgoingEgdes(objectInstance)) {
            if (outgoingEdge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
                if (graph.getOpposite(objectInstance, outgoingEdge) instanceof MethodInvocation) {
                    MethodInvocation methodInvocation = (MethodInvocation) graph.getOpposite(objectInstance,
                            outgoingEdge);

                    if (methodInvocation.getMethodSignature().contains("<init>")
                            && JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature())
                                    .equals(objectInstance.getType())) {
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
    public Set<MethodInvocation> getMethodInvocationsWhichUse(DataNode dataNode) {
        Set<MethodInvocation> methodInvocations = new HashSet<>();
        for (String edge : getOutgoingEgdes(dataNode)) {
            if (edge.startsWith(DATA_DEPENDENCY_PREFIX)) {
                methodInvocations.add((MethodInvocation) graph.getOpposite(dataNode, edge));
            }
        }
        return methodInvocations;
    }

    public void markNodeAsExternalInterface(MethodInvocation mi) {
        // Collecte edges

        // TODO Those might not really realiable !!
        Collection<Pair<GraphNode, String>> predecessors = new HashSet<>();
        for (String inEdge : graph.getInEdges(mi)) {
            predecessors.add(new Pair<GraphNode, String>(graph.getOpposite(mi, inEdge), inEdge));
        }

        Collection<Pair<GraphNode, String>> successors = new HashSet<>();
        for (String outEdge : graph.getOutEdges(mi)) {
            predecessors.add(new Pair<GraphNode, String>(graph.getOpposite(mi, outEdge), outEdge));
        }

        // Remove the node, this shall remove also the edges
        graph.removeVertex(mi);
        // Update the node content
        mi.setBelongsToExternalInterface(true);
        // Reinsert the node
        graph.addVertex(mi);
        // Reinsert the edges
        for (Pair<GraphNode, String> predecessor : predecessors) {
            graph.addEdge(predecessor.getSecond(), predecessor.getFirst(), mi, EdgeType.DIRECTED);
        }
        for (Pair<GraphNode, String> successor : successors) {
            graph.addEdge(successor.getSecond(), mi, successor.getFirst(), EdgeType.DIRECTED);
        }
    }

    /**
     * Update this to include all the nodes from the "other" graph (union nodes)
     * and the union of the dependencies
     * 
     * TODO this should check for duplicate relations !
     * 
     * @param second
     */
    public void include(DataDependencyGraph second) {
        /*
         * Add vertices - Note that this might lose information if we do not
         * store data as datum !
         */
        for (GraphNode vertex : second.graph.getVertices()) {
            graph.addVertex(vertex);
        }
        // Add the relations if they are not there already
        // What if one of the two verted was inside and the other was not ?!
        for (String edge : second.graph.getEdges()) {
            if (!graph.containsEdge(edge)) {
                EdgeType edgeType = second.graph.getEdgeType(edge);
                GraphNode v1 = getVertex(second.graph.getSource(edge));
                GraphNode v2 = getVertex(second.graph.getDest(edge));

                // TODO Check that there are not already the same edge here !

                if (!graph.addEdge(edge, v1, v2, edgeType)) {
                    logger.warn("Merge of datadependency failed for edge" + edge + " " + v1 + " -> " + v2);
                }
            }
        }
    }

    private GraphNode getVertex(GraphNode matching) {
        if (!graph.containsVertex(matching)) {
            logger.warn("Graph does not contain " + matching);
        }
        // TODO Must implement this ?
        return matching;
    }

    public Optional<MethodInvocation> getConstructorOf(ObjectInstance objectInstance) {
        for (MethodInvocation methodInvocation : getMethodInvocationsForOwner(objectInstance)) {
            if (methodInvocation.isConstructor()) {
                return Optional.of(methodInvocation);
            }
        }
        return Optional.empty();
    }

    public boolean verifyObjectInstanceProvenance() {
        return getDanglingObjects().isEmpty();
    }

    /**
     * Returns all the object for which we cannot establish provenance unless
     * those are "null". This should also include aliases.
     * 
     * @return
     */
    public Set<ObjectInstance> getDanglingObjects() {
        Set<ObjectInstance> danglingObjects = new HashSet<>();
        for (ObjectInstance objectInstance : getObjectInstances()) {
            if (objectInstance instanceof NullInstance) {
                continue;
            } else {
                // Check the object and its aliases
                Set<ObjectInstance> theObjectAndItsAliases = new HashSet<>();
                theObjectAndItsAliases.add(objectInstance);
                theObjectAndItsAliases.addAll(getAliasesOf(objectInstance));
                boolean isDangling = true;
                for (ObjectInstance oi : theObjectAndItsAliases) {
                    if (getConstructorOf(oi).isPresent() || !getMethodInvocationsWhichReturn(oi).isEmpty()) {
                        isDangling = false;
                        break;
                    }
                }
                if (isDangling) {
                    danglingObjects.add(objectInstance);
                }
            }
        }
        return danglingObjects;
    }

    public Set<DataNode> getDataNodes() {
        Set<DataNode> dataNodes = new HashSet<>();
        for (GraphNode node : graph.getVertices()) {
            if (node instanceof DataNode) {
                dataNodes.add((DataNode) node);
            }
        }
        return dataNodes;
    }

    public String contextualize(DataNode dataNode) {
        // Where this is used ?
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append("Providing the object").append("\n");
        for (String in : graph.getInEdges(dataNode)) {
            sb.append(graph.getOpposite(dataNode, in) + " -> " + in).append("\n");
        }
        sb.append("Using the object").append("\n");
        for (String out : graph.getOutEdges(dataNode)) {
            sb.append(out + "->" + graph.getOpposite(dataNode, out)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Remove from the graph all the subsumed method invocations and the nodes.
     * This is not quite getSubGraph in the sense this method modifies the
     * current graph while the other returns a new one !
     * 
     * TODO Check if the relations are then maintained ...
     * @param callGraph
     */
    public void summarize(ExecutionFlowGraph executionFlowGraph) {
        // We do this in two step as removing method invocation will remove
        // additional edges

        Set<GraphNode> toRemove = new HashSet<>();

        // Remove all the method invocation which are not in executionFlowGraph
        for (GraphNode methodInvocation : graph.getVertices()) {
            if (methodInvocation instanceof MethodInvocation) {
                if (!executionFlowGraph.contains((MethodInvocation) methodInvocation)) {
                    toRemove.add(methodInvocation);
                }
            }
        }

        removeAll(toRemove);
        toRemove.clear();

        // At this point remove all the DataNode which are not connected to any
        // MethodInvocation nodes
        for (GraphNode dataNode : graph.getVertices()) {
            if (dataNode instanceof DataNode) {
                if (graph.getInEdges(dataNode).isEmpty() && graph.getOutEdges(dataNode).isEmpty()) {
                    toRemove.add(dataNode);
                }
            }
        }
        removeAll(toRemove);
    }

    private void removeAll(Set<GraphNode> toRemove) {
        for (GraphNode removeMe : toRemove) {
            boolean removed = graph.removeVertex(removeMe);
            if (!removed) {
                logger.warn("Cannot Remove " + removeMe);
            }
        }
    }

    public Set<ObjectInstance> getAliasesOf(ObjectInstance objectInstanceToCarve) {
        // Aliase is bidirectional but to be sure let's check both sides...
        Set<ObjectInstance> aliases = new HashSet<>();
        for (String edge : graph.getInEdges(objectInstanceToCarve)) {
            if (edge.startsWith(ALIAS_DEPENDENCY_PREFIX)) {
                // How it could be otherwise ?!
                aliases.add((ObjectInstance) graph.getOpposite(objectInstanceToCarve, edge));
            }
        }
        for (String edge : graph.getOutEdges(objectInstanceToCarve)) {
            if (edge.startsWith(ALIAS_DEPENDENCY_PREFIX)) {
                // How it could be otherwise ?!
                aliases.add((ObjectInstance) graph.getOpposite(objectInstanceToCarve, edge));
            }
        }

        return aliases;
    }

    /*
     * Remove all the data notes which are not owner or parameters of anything
     */
    public void purge() {
        Set<DataNode> toRemove = new HashSet<>();
        for (GraphNode node : graph.getVertices()) {
            if (node instanceof DataNode) {
                // Count how many NON-Alias edges this node has
                Set<String> edges = new HashSet<>(graph.getInEdges(node));
                edges.addAll(graph.getOutEdges(node));
                boolean remove = true;
                for (String edge : edges) {
                    if (edge.startsWith(ALIAS_DEPENDENCY_PREFIX)) {
                        continue;
                    }
                    remove = false;
                    break;
                }
                if (remove) {
                    toRemove.add((DataNode) node);
                }
            }
        }
        for (DataNode dataNode : toRemove) {
            logger.trace("Purging " + dataNode);
            graph.removeVertex(dataNode);
        }

    }

    public void removeMethodInvocation(MethodInvocation toDrop) {
        // This automatically remove all the edges
        boolean removed = graph.removeVertex(toDrop);
        if (!removed) {
            logger.warn("Did not removed " + toDrop);
        }

    }

    public void addPrimitiveValue(PrimitiveValue primitiveValue) {
        if (!graph.containsVertex(primitiveValue)) {
            graph.addVertex(primitiveValue);
        }
    }

    public void addNullInstance(NullInstance nullInstance) {
        if (!graph.containsVertex(nullInstance)) {
            graph.addVertex(nullInstance);
        }

    }

    public void addObjectInstance(ObjectInstance carvingTarget) {
        if (!graph.containsVertex(carvingTarget)) {
            graph.addVertex(carvingTarget);
        }
    }

}
