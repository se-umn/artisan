package de.unipassau.abc.data;

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
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import de.unipassau.abc.ABCGlobalOptions;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.utils.GraphUtility;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
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
public class DataDependencyGraphImpl implements DataDependencyGraph {

    private static Logger logger = LoggerFactory.getLogger(DataDependencyGraphImpl.class);

    public static final String OWNERSHIP_DEPENDENCY_PREFIX = "Ownership";

    public static final String DATA_DEPENDENCY_DUMMY_PREFIX = "Dummy";

    // Parameter to a call
    public static final String DATA_DEPENDENCY_PREFIX = "DataNode";
    // Identify all other dependencies of a method that are not captured by its
    // parameters
    public static final String IMPLICIT_DATA_DEPENDENCY_PREFIX = "Implicit_DataNode";

    public static final String RETURN_DEPENDENCY_PREFIX = "Return";
    public static final String ALIAS_DEPENDENCY_PREFIX = "Aliasing";

    private static AtomicInteger id = new AtomicInteger(0);

    /*
     * It seems that Graph does not really return the same objects. Somehow if I set
     * complex attributes on a node by looking up that node once again I loose them.
     * We need to use addUserDatum or re-implement the clone method of the data
     * objects. Check http://www.datalab.uci.edu/papers/JUNG_tech_report.html
     * 
     * In particular we should use UserData.SHARED to pass around the references ?
     */
    private Graph<GraphNode, String> graph;
    // So we store data OUTSIDE the graph...
    private Map<DataNode, Value> additionalData;

    public DataDependencyGraphImpl() {
        // graph = new SparseMultigraph<GraphNode, String>();
        graph = new DirectedSparseMultigraph<GraphNode, String>();
        additionalData = new HashMap<>();
    }

    /**
     * In some cases there we observed weird behaviors if we assume that the library
     * cannot realize that two different instances represent the same vertex.
     * Probably, somewhere it uses == and in others .equals(). This will fail if the
     * node cannot be found! (and probably should fail if there are more than one...
     * 
     * @param vertex
     * @return
     */
    private GraphNode retrieveTheActualVertex(GraphNode vertex) {
        Optional<GraphNode> optionalActualNode = graph.getVertices().stream().filter(n -> n.equals(vertex)).findFirst();
        return optionalActualNode.get();
    }

    @Override
    public MethodInvocation get(MethodInvocation methodInvocation) {
        return (MethodInvocation) retrieveTheActualVertex(methodInvocation);
    }

    @Override
    public void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl) {
        GraphNode originalNode = this.retrieveTheActualVertex(orig);

        Collection<String> inEdges = graph.getInEdges(originalNode);
        Collection<String> outEdges = graph.getOutEdges(originalNode);

        List<Pair<String, GraphNode>> inEdgesWithSources = inEdges.stream()
                .map(edge -> new Pair<>(edge, graph.getOpposite(originalNode, edge))).collect(Collectors.toList());
        List<Pair<String, GraphNode>> outEdgesWithDestinations = outEdges.stream()
                .map(edge -> new Pair<>(edge, graph.getOpposite(originalNode, edge))).collect(Collectors.toList());

        graph.removeVertex(originalNode);
        graph.addVertex(repl);

        inEdgesWithSources.forEach(edgeNameMethodInvocationPair -> graph
                .addEdge(edgeNameMethodInvocationPair.getFirst(), edgeNameMethodInvocationPair.getSecond(), repl));
        outEdgesWithDestinations.forEach(edgeNameMethodInvocationPair -> graph
                .addEdge(edgeNameMethodInvocationPair.getFirst(), repl, edgeNameMethodInvocationPair.getSecond()));
    }

    public void addMethodInvocationWithoutAnyDependency(MethodInvocation methodInvocation) {
        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }
    }

    // This makes sense only on ObjectInstances
    public void addAliasingDataDependency(ObjectInstance node, ObjectInstance alias) {
        // Aliasing is possible only on Existing nodes
//        if (!graph.containsVertex(node)) {
//            System.out.println("ERROR: DataDependencyGraphImpl.addAliasingDataDependency() Cannot find " + node);
//            // TODO Maybe some exception?
//            return;
//        } else {
        // Can be improved using generics...
        node = (ObjectInstance) this.retrieveTheActualVertex(node);
//        }
//        if (!graph.containsVertex(alias)) {
//            System.out.println("ERROR: DataDependencyGraphImpl.addAliasingDataDependency() Cannot find " + alias);
//            // TODO Maybe some exception?
//            return;
//        } else {
        alias = (ObjectInstance) this.retrieveTheActualVertex(alias);
//        }
        if (// alias != null &&
        compatibleClasses(node, alias)) {
            // Recording the aliasing relation - This is bidirectional
            graph.addEdge(ALIAS_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), node, alias, EdgeType.DIRECTED);
            graph.addEdge(ALIAS_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), alias, node, EdgeType.DIRECTED);
//            System.out.println(" >> Aliasing " + node + " and " + alias);
        }
    }

    /*
     * The positional information is ENCODED in the edge label !
     */
    public void addDataDependencyOnActualParameter(MethodInvocation methodInvocation, DataNode actualParameter,
            int position) {
        DataNode node = null;

        if (!graph.containsVertex(actualParameter)) {

            // Null instances are treated like primitive types, but they are
            // unique ... otherwise we create all sort of deps over null
            // TODO Not sure what to do here...
            if (actualParameter instanceof ObjectInstance) {
                node = handleDanglingObjectInstance((ObjectInstance) actualParameter);
            } else {
                // Primitives, Strings, etc.
                node = actualParameter;
            }

            graph.addVertex(node);

        } else {
            node = (DataNode) this.retrieveTheActualVertex(actualParameter);
        }

        // There might be name collisions, therefore we look for the next free id
        String edgeName;
        do {
            edgeName = DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement();
        } while (graph.containsEdge(edgeName));

        graph.addEdge(edgeName, node, methodInvocation, EdgeType.DIRECTED);

    }

    public void replaceDataDependencyOnOwner(MethodInvocation methodInvocation, ObjectInstance newOwner) {
        // If newOwner does not exist, we add it
        ObjectInstance originalOwner = null;

        // This is the real methodInvocation to "update" (we remove and reintroduce it)
        methodInvocation = (MethodInvocation) this.retrieveTheActualVertex(methodInvocation);

        // Find the link of the owner to be replaced
        String edgeToRemove = null;
        for (String incomingEdge : getIncomingEdges(methodInvocation)) {

            if (incomingEdge.startsWith(OWNERSHIP_DEPENDENCY_PREFIX)) {
                edgeToRemove = incomingEdge;
                break;
            }
        }

        if (!graph.removeEdge(edgeToRemove)) {
            logger.warn("Cannot remove " + edgeToRemove + " while replacing ownership of " + methodInvocation);
            // TODO Replace with an exception
        } else {
            // Here we brutally add a new edge instead of reusing the original one
            this.addDataDependencyOnOwner(methodInvocation, newOwner);
//            graph.addEdge(edgeToRemove, node, methodInvocation, EdgeType.DIRECTED);
        }
    }

    public void replaceDataDependencyOnReturn(MethodInvocation methodInvocation, DataNode newReturn) {
        // This is the real methodInvocation to "update" (we remove and reintroduce it)
        methodInvocation = (MethodInvocation) this.retrieveTheActualVertex(methodInvocation);

        // Find the link of the owner to be replaced
        String edgeToRemove = null;
        for (String outgoingEdge : getOutgoingEgdes(methodInvocation)) {

            if (outgoingEdge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
                edgeToRemove = outgoingEdge;
                break;
            }
        }

        if (!graph.removeEdge(edgeToRemove)) {
            logger.warn("Cannot remove " + edgeToRemove + " while replacing return of " + methodInvocation);
            // TODO Replace with an exception
        } else {
            // Here we brutally add a new edge instead of reusing the original one
            // TODO Will this work with String returns? Anyway, we use it only for intents
            // at the moment
            this.addDataDependencyOnReturn(methodInvocation, newReturn);
        }
    }

    /*
     * The positional information is ENCODED in the edge label ! THIS IS UNSAFE, WE
     * DO NOT CHECK FOR TYPES AND THE LIKE!
     */
    public void replaceDataDependencyOnActualParameter(MethodInvocation methodInvocation, DataNode actualParameter,
            int position) {
        DataNode node = null;

        methodInvocation = (MethodInvocation) this.retrieveTheActualVertex(methodInvocation);

        // Find the link of the parameter to be replaced
        String edgeToRemove = null;
        for (String incomingEdge : getIncomingEdges(methodInvocation)) {

            if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {

                if (Integer.parseInt(incomingEdge.split("_")[1]) == position) {
                    edgeToRemove = incomingEdge;
                    break;
                }
            }
        }

        if (!graph.removeEdge(edgeToRemove)) {
            logger.warn("Cannot remove " + edgeToRemove + " while replacing dependency at position " + position
                    + " for " + methodInvocation);
            // TODO Replace with an exception
        } else {
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
                node = (DataNode) this.retrieveTheActualVertex(actualParameter);
            }

            // Note: We reuse the just removed edge name to maintain consistency
            graph.addEdge(edgeToRemove, node, methodInvocation, EdgeType.DIRECTED);
        }
    }

    @Override
    public void addImplicitDataDependency(MethodInvocation methodInvocation, DataNode implicitDataDependency) {
        DataNode node = null;

        if (!graph.containsVertex(implicitDataDependency)) {

            // Null instances are treated like primitive types, but they are
            // unique ... otherwise we create all sort of deps over null
            if (implicitDataDependency instanceof ObjectInstance) {
                node = handleDanglingObjectInstance((ObjectInstance) implicitDataDependency);
            } else {
                // Primitives, Strings, etc.
                node = implicitDataDependency;
            }

            graph.addVertex(node);

        } else {
            node = (DataNode) this.retrieveTheActualVertex(implicitDataDependency);
        }

        // There might be name collisions, therefore we look for the next free id
        String edgeName;
        do {
            // Graphs MUST have UNIQUE labels ...
            edgeName = IMPLICIT_DATA_DEPENDENCY_PREFIX + "_" + id.getAndIncrement();
        } while (graph.containsEdge(edgeName));

        graph.addEdge(edgeName, node, methodInvocation, EdgeType.DIRECTED);

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
            node = (DataNode) this.retrieveTheActualVertex(returnValue);
        }

        // TODO do we have to check for collisions here?
        graph.addEdge(RETURN_DEPENDENCY_PREFIX + id.getAndIncrement(), methodInvocation, node, EdgeType.DIRECTED);
    }

    /**
     * This gets the actual data dependencies, values of primitives and string,
     * references for objects. Node that objectInstanceIds must be ordered
     * positionally !!!
     * 
     * @param methodInvocation
     */
    @SuppressWarnings("unchecked")
    public void addMethodInvocation(MethodInvocation methodInvocation) {

        // TODO Add and return can be defined here as an utility method
        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        } else {
            methodInvocation = (MethodInvocation) this.retrieveTheActualVertex(methodInvocation);
        }

        String[] actualParameters = methodInvocation.getActualParameters();
        String[] formalParameters = JimpleUtils.getParameterList(methodInvocation.getMethodSignature());

        /*
         * An object of type String might be passed around as Object, that is the formal
         * parameter of the call is Object, the actual parameter is a String
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
//			else if ("java.lang.System.in".equals(actualParameters[position])) {
//				node = ObjectInstance.systemIn;
//			} else if ("java.lang.System.out".equals(actualParameters[position])) {
//				node = ObjectInstance.systemOut;
//			} else if ("java.lang.System.err".equals(actualParameters[position])) {
//				node = ObjectInstance.systemErr;
//			} else if ("StaticFieldOperation".equals(methodInvocation.getInvocationType())) {
//				node = new StaticObjectInstance(actualParameters[position] + "@0",
//						JimpleUtils.getReturnType(methodInvocation.getMethodSignature()));
//			}
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

            } else {
                node = (DataNode) this.retrieveTheActualVertex(node);
            }

            graph.addEdge(DATA_DEPENDENCY_PREFIX + "_" + position + "_" + id.getAndIncrement(), node, methodInvocation,
                    EdgeType.DIRECTED);
        }

    }

    /*
     * HERE We need to check if this data note is not yet define, if this is an
     * OBJECT might be an error, since all the objects must be define before use...
     * There might be different explanations here: the one we found is the objects
     * like System.in gets initialized inside java code, so we cannot see this.
     * Hence we cannot carve this. We use the simple heuristic here that we force
     * "System.in"
     */
    private ObjectInstance handleDanglingObjectInstance(ObjectInstance node) {

        if (JimpleUtils.isNull(node.getObjectId())) {
            // System.out.println("Call a method on null object... NPE?!");
            // Null parameter
            setSootValueFor(node, NullConstant.v());
        }
//		else if (((ObjectInstance) node).getType()
//				.startsWith("org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock")) {
//			// TODO This feels wrong... Check if this is System.in mocking
//			logger.debug(
//					"DataDependencyGraph.addMethodInvocation() Aliasing " + node + " with " + ObjectInstance.systemIn);
//			return ObjectInstance.systemIn;
//		} 

        else if (((ObjectInstance) node).getType().endsWith("[]")) {
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
             * TODO: Aliasing based on system id is not reliable on Delvik, so use a better
             * heuristic which considers the type of the objects... this require type
             * information to be available.. which is not ATM
             */

            if (alias != null && compatibleClasses(node, alias)) {

                // Recording the aliasing relation - This is bidirectional
                graph.addEdge(ALIAS_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), node, alias, EdgeType.DIRECTED);
                graph.addEdge(ALIAS_DEPENDENCY_PREFIX + "_" + id.getAndIncrement(), alias, node, EdgeType.DIRECTED);
                logger.debug("DataDependencyGraph.addMethodInvocation() Aliasing " + node + " with " + alias);
                // TODO MAYBE we shall encode directly here the type for the alias relation, and
                // only have it one-directional?
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
        if (aliasClass.isInterface() && !nodeClass.isInterface()) {
            return Scene.v().getActiveHierarchy().getImplementersOf(aliasClass).contains(nodeClass);
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
        } else {
            methodInvocation = (MethodInvocation) this.retrieveTheActualVertex(methodInvocation);
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
        } else if (!ABCGlobalOptions.STRINGS_AS_OBJECTS && JimpleUtils.isString(formalReturnValue)) {
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

        } else {
            node = (DataNode) this.retrieveTheActualVertex(node);
        }

        graph.addEdge(RETURN_DEPENDENCY_PREFIX + id.getAndIncrement(), methodInvocation, node, EdgeType.DIRECTED);
        //
        return node;
    }

    public void addDataDependencyOnDummy(MethodInvocation call1, MethodInvocation call2) {
        boolean added = graph.addEdge(DATA_DEPENDENCY_DUMMY_PREFIX + "_" + id.getAndIncrement(), call1, call2,
                EdgeType.DIRECTED);
        if (!added) {
            logger.warn("Cannot add Edge " + DATA_DEPENDENCY_DUMMY_PREFIX + "_" + id.intValue() + " from " + call1
                    + " to " + call2);
        }
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
        } else {
            methodInvocation = (MethodInvocation) this.retrieveTheActualVertex(methodInvocation);
        }

        ObjectInstance node = null;
        if (!graph.containsVertex(objectInstance)) {
            // TODO not sure what's this ...
            node = handleDanglingObjectInstance(objectInstance);
            graph.addVertex(node);
        } else {
            // Let's assume searching by object id is always stable
            node = (ObjectInstance) this.retrieveTheActualVertex(objectInstance); // getVertexById(objectInstance.getObjectId());
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
                System.out.println("Label node " + node);
//				if (node instanceof ValueNode) {
//					// TODO Not sure we can skip the visualization at all...
//					return "VALUE NODE";
//					// } else if (node instanceof ObjectInstance) {
//					// return ((ObjectInstance) node).getObjectId();
//					// } else if (node instanceof MethodInvocation) {
//					// return ((MethodInvocation) node).getJimpleMethod();
//				} else {
                return super.apply(node);
//				}
            }
        });

        vv.getRenderContext().setVertexFillPaintTransformer(new Function<GraphNode, Paint>() {
            @Override
            public Paint apply(GraphNode node) {
                System.out.println("Fill Node " + node);
                if (node instanceof ValueNode) {
//					// TODO Not sure we can skip the visualization at all...
                    return Color.YELLOW;
                } else if (node instanceof NullInstance) {
                    return Color.BLACK;
                } else if (node instanceof ObjectInstance) {
                    return Color.GREEN;
                }
//				} else if (node instanceof MethodInvocation) {
//					MethodInvocation methodInvocation = (MethodInvocation) node;
//					return (methodInvocation.getInvocationType().equals("StaticInvokeExpr")) ? Color.ORANGE : Color.RED;
//				}
                else {
                    return Color.BLUE;
                }
//				return super.apply(node);
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
    // TODO This can be replaced with a call to Clusterer?
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
     * If the return type is a Primitive or an Object which is not used in the then
     * this does not find it, because we did not tracked it !
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
     * Generate a subgraph which contains the provided methodInvocations and their
     * data dependencies (this, parameters, and returnValues)
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
        DataDependencyGraphImpl subGraph = new DataDependencyGraphImpl();
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

    public List<DataNode> getParametersOf(MethodInvocation methodInvocation) {

        // TODO Maybe a getParameterCount might be ok here...
        DataNode[] parametersOf = new DataNode[methodInvocation.getActualParameterInstances().size()];

        for (String incomingEdge : getIncomingEdges(methodInvocation)) {
            /*
             * Identify incident relations to method invocation tagged as data. The
             * parameter is the other end of the incoming link
             */
            if (incomingEdge.startsWith(DATA_DEPENDENCY_PREFIX)) {
                // TODO Why in the above we used ObjectInstance?
//				if (graph.getOpposite(methodInvocation, incomingEdge) instanceof ObjectInstance) {
//					parametersOf.add(((ObjectInstance) graph.getOpposite(methodInvocation, incomingEdge)));
//				}

                parametersOf[Integer.parseInt(incomingEdge.split("_")[1])] = (DataNode) graph
                        .getOpposite(methodInvocation, incomingEdge);
            }
        }

        return Arrays.asList(parametersOf);
    }

    @Override
    public List<DataNode> getImplicitDataDependenciesOf(MethodInvocation methodInvocation) {

        List<DataNode> implicitDataDependencies = new ArrayList<DataNode>();

        for (String incomingEdge : getIncomingEdges(methodInvocation)) {
            /*
             * Identify incident relations to method invocation tagged as data. The
             * parameter is the other end of the incoming link.
             * 
             * TODO We may need to add tags to distinguish the various types of implicit
             * data dependency
             */
            if (incomingEdge.startsWith(IMPLICIT_DATA_DEPENDENCY_PREFIX)) {
                implicitDataDependencies.add((DataNode) graph.getOpposite(methodInvocation, incomingEdge));
            }
        }

        return implicitDataDependencies;

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
        DataDependencyGraphImpl other = (DataDependencyGraphImpl) obj;
        if (additionalData == null) {
            if (other.additionalData != null)
                return false;
        } else if (!additionalData.equals(other.additionalData))
            return false;

        return GraphUtility.areDataDependencyGraphsEqual(graph, other.graph);
    }

    /**
     * Return all the object instances which have at least one invocation before the
     * methodInvocationToCarve and TODO belongs to external interface.
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
     * Return all the method calls which have the given object as parameter BUT not
     * as a static dependency
     * 
     * @param dataNode
     * @return
     */
    public Set<MethodInvocation> getMethodInvocationsWhichUse(DataNode dataNode) {
        Set<MethodInvocation> methodInvocations = new HashSet<>();
        for (String edge : getOutgoingEgdes(dataNode)) {
            if (edge.startsWith(DATA_DEPENDENCY_PREFIX) || edge.startsWith(IMPLICIT_DATA_DEPENDENCY_PREFIX)) {
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
     * Update this graph to include all the nodes from the "other" graph (union
     * nodes). According to Jung;s documentation one node can be only and only in
     * one graph at the type, but we can copy them. Note: the _second graph will NOT
     * be changed. However, we build our graphs using Clonables and not Vertex, so
     * this is not possible here...
     * 
     * @param second
     */
    public void include(DataDependencyGraph _second) {

        // We need to cast to access low-level graph functionality!
        DataDependencyGraphImpl second = (DataDependencyGraphImpl) _second;

        /*
         * Add vertices
         */
        for (GraphNode vertex : second.graph.getVertices()) {
            // TODO Do we need to clone them first ?
            graph.addVertex(vertex);
        }
        // Add the relations if they are not there already
        // What if one of the two vertives was inside and the other was not ?!
        for (String edge : second.graph.getEdges()) {
            if (!graph.containsEdge(edge)) {
                EdgeType edgeType = second.graph.getEdgeType(edge);
                GraphNode v1 = this.retrieveTheActualVertex(second.graph.getSource(edge));
                GraphNode v2 = this.retrieveTheActualVertex(second.graph.getDest(edge));

                if (!graph.addEdge(edge, v1, v2, edgeType)) {
                    logger.warn("Merge of datadependency failed for edge" + edge + " " + v1 + " -> " + v2);
                }
            }
        }
    }

//    private GraphNode getVertex(GraphNode matching) {
//        if (!graph.containsVertex(matching)) {
//            logger.warn("Graph does not contain " + matching);
//        }
//        // TODO Must implement this ?
//        return matching;
//    }

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
     * Returns all the object for which we cannot establish provenance unless those
     * are "null" or aliased by other objects. This should also include aliases.
     * 
     * @return
     */
    public Set<ObjectInstance> getDanglingObjects() {
        Set<ObjectInstance> danglingObjects = new HashSet<>();
        for (ObjectInstance objectInstance : getObjectInstances()) {
            // TODO For some reason, probably cloning, NullInstances are not recognized as
            // such anymore?
            if (objectInstance instanceof NullInstance) {
                continue;
            } else if (objectInstance.getObjectId().endsWith("@0")) {
                continue;
            } else {
                // Check the object and its aliases
                Set<ObjectInstance> theObjectAndItsAliases = new HashSet<>();
                theObjectAndItsAliases.add(objectInstance);
                theObjectAndItsAliases.addAll(getAliasesOf(objectInstance));
                boolean isDangling = true;
                for (ObjectInstance oi : theObjectAndItsAliases) {
//                    if (! getConstructorOf(oi).isPresent()) {
//                        logger.info(">> Cannot find constructor of " + oi);
//                    }

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

    // TODO Move to Utility class
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
     * Remove from the graph all the subsumed method invocations and the nodes. This
     * is not quite getSubGraph in the sense this method modifies the current graph
     * while the other returns a new one !
     * 
     * TODO Check if the relations are then maintained ...
     * 
     * @param executionFlowGraph
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

    // This assume we are indeed working on the instances stored in the graph, not
    // shallow copy of them !
    public void remove(GraphNode toRemove) {
        if (graph.containsVertex(toRemove)) {
            toRemove = this.retrieveTheActualVertex(toRemove);
            if (!graph.removeVertex(toRemove)) {
                logger.warn("Cannot Remove node " + toRemove);
            }
        }
    }

    public Set<ObjectInstance> getAliasesOf(ObjectInstance objectInstanceToCarve) {
        // Aliasing is a bidirectional relation but to be sure let's check both sides...
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

    /*
     * If the return type is a Primitive or an Object which is not used in the then
     * this does not find it, because we did not tracked it !
     * 
     */
    public Optional<DataNode> getReturnValue(MethodInvocation methodInvocation) throws ABCException {
        if (JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())) {
            return Optional.empty();
        } else {
            for (GraphNode node : graph.getVertices()) {
                if (node instanceof MethodInvocation) {
                    if (((MethodInvocation) node).equals(methodInvocation)) {

                        Set<String> dataDependencyEdges = new HashSet<String>(getOutgoingEgdes(node));

                        for (String edge : dataDependencyEdges) {
                            if (edge.startsWith(RETURN_DEPENDENCY_PREFIX)) {
                                GraphNode returnValue = graph.getOpposite(node, edge);
                                // TODO This could not be otherwise !
                                return Optional.of((DataNode) returnValue);
                            }
                        }
                    }
                }
            }
        }

        throw new ABCException("Cannot find any return value for method invocation " + methodInvocation);
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

//    public class MyWeakComponentClusterer implements Function<Graph<GraphNode, String>, Set<Set<GraphNode>>> {
//        /**
//         * Extracts the weak components from a graph.
//         * 
//         * @param graph the graph whose weak components are to be extracted
//         * @return the list of weak components
//         */
//        public Set<Set<GraphNode>> apply(Graph<GraphNode, String> graph) {
//
//            Set<Set<GraphNode>> clusterSet = new HashSet<Set<GraphNode>>();
//
//            System.out.println("\n\n DataDependencyGraphImpl.MyWeakComponentClusterer.apply() APPLY");
//            graph.getVertices().stream().filter(gn -> gn instanceof ObjectInstance).map(gn -> (ObjectInstance) gn)
//                    .map(obj -> (obj.isAndroidActivity() ? "** " + obj.toString() : "  " + obj.toString()))
//                    .forEach(System.out::println);
//
//            HashSet<GraphNode> unvisitedVertices = new HashSet<GraphNode>(graph.getVertices());
//
//            System.out.println("\n\n DataDependencyGraphImpl.MyWeakComponentClusterer.apply() UNVISITED");
//            unvisitedVertices.stream().filter(gn -> gn instanceof ObjectInstance).map(gn -> (ObjectInstance) gn)
//                    .map(obj -> (obj.isAndroidActivity() ? "** " + obj.toString() : "  " + obj.toString()))
//                    .forEach(System.out::println);
//
//            while (!unvisitedVertices.isEmpty()) {
//                Set<GraphNode> cluster = new HashSet<GraphNode>();
//                GraphNode root = unvisitedVertices.iterator().next();
//
//                if (root instanceof ObjectInstance) {
//                    System.out.println("Clusterer Processing root " + root + " - Activity: "
//                            + ((ObjectInstance) root).isAndroidActivity());
//                }
//
//                unvisitedVertices.remove(root);
//                cluster.add(root);
//
//                Queue<GraphNode> queue = new LinkedList<GraphNode>();
//                queue.add(root);
//
//                while (!queue.isEmpty()) {
//                    GraphNode currentVertex = queue.remove();
//                    Collection<GraphNode> neighbors = graph.getNeighbors(currentVertex);
//
//                    if (currentVertex instanceof ObjectInstance) {
//                        System.out.println(
//                                "\n\n DataDependencyGraphImpl.MyWeakComponentClusterer.apply() OO neighbors of  OO "
//                                        + (((ObjectInstance) currentVertex).isAndroidActivity()
//                                                ? "** " + currentVertex.toString()
//                                                : currentVertex.toString()));
//                    } else {
//                        System.out
//                                .println("\n\n DataDependencyGraphImpl.MyWeakComponentClusterer.apply() OO neighbors of"
//                                        + currentVertex);
//                    }
//                    neighbors.stream().filter(gn -> gn instanceof ObjectInstance).map(gn -> (ObjectInstance) gn)
//                            .map(obj -> (obj.isAndroidActivity() ? "** " + obj.toString() : "  " + obj.toString()))
//                            .forEach(System.out::println);
//
//                    for (GraphNode neighbor : neighbors) {
//                        if (unvisitedVertices.contains(neighbor)) {
//                            queue.add(neighbor);
//                            unvisitedVertices.remove(neighbor);
//                            cluster.add(neighbor);
//
//                        }
//                    }
//                }
//                clusterSet.add(cluster);
//            }
//            return clusterSet;
//        }
//    }

    // TODO There is no more need to copy and duplicate the data here. We need to be
    // sure we use the right instances and then call the primitives to create new
    // nodes and graphs!
    @Override
    public Collection<DataDependencyGraph> extrapolate(Set<MethodInvocation> methodInvocationsToExtrapolate) {
        Collection<DataDependencyGraph> extrapolated = new ArrayList<DataDependencyGraph>();

        Graph<GraphNode, String> union = new DirectedSparseMultigraph<GraphNode, String>();

        Map<GraphNode, GraphNode> cloneMap = new HashMap<GraphNode, GraphNode>();

        final Set<MethodInvocation> allMethodInvocations = graph.getVertices().stream()//
                .filter(v -> MethodInvocation.class.isInstance(v))//
                .map(MethodInvocation.class::cast)//
                .collect(Collectors.toSet());

        // Add all method invocation nodes - clone them in the process
        for (MethodInvocation methodInvocation : methodInvocationsToExtrapolate) {

            // Avoid using incomplete information from methodInvocation
            MethodInvocation originalMethodInvocation = allMethodInvocations.parallelStream()
                    .filter(mi -> mi.getInvocationCount() == methodInvocation.getInvocationCount())//
                    .findAny().orElse(null);

            // TODO Consider using the primitives to copy new nodes!
//            logger.trace("Node " + originalMethodInvocation + " has the following predecessors:");

            MethodInvocation cloned = originalMethodInvocation.clone();
            union.addVertex(cloned);
            cloneMap.put(cloned, originalMethodInvocation);

            for (GraphNode neighbor : graph.getPredecessors(originalMethodInvocation)) {
                logger.trace(" - " + neighbor);
                GraphNode clonedNode = neighbor.clone();
                cloneMap.put(clonedNode, neighbor);
                boolean added = union.addVertex(clonedNode);

//                if (neighbor instanceof ObjectInstance) {
//                    ObjectInstance obj = (ObjectInstance) neighbor;
//                    if (obj.isAndroidActivity()) {
//                        logger.debug("** Source Node" + obj + " is ACTIVITY. Cloned? "
//                                + ((ObjectInstance) clonedNode).isAndroidActivity());
//                    }
//                }
            }

            logger.trace("Node " + originalMethodInvocation + " has the following successors:");
            for (GraphNode neighbor : graph.getSuccessors(originalMethodInvocation)) {
                logger.trace(" - " + neighbor);
                GraphNode clonedNode = neighbor.clone();
                cloneMap.put(clonedNode, neighbor);

                boolean added = union.addVertex(clonedNode);

//                if (neighbor instanceof ObjectInstance) {
//                    ObjectInstance obj = (ObjectInstance) neighbor;
//                    if (obj.isAndroidActivity()) {
//                        logger.debug("** Source Node" + obj + " is ACTIVITY. Cloned? "
//                                + ((ObjectInstance) clonedNode).isAndroidActivity());
//                    }
//                }
            }
        }

//        for (GraphNode vv : union.getVertices()) {
//            if (vv instanceof ObjectInstance) {
//                ObjectInstance obj = (ObjectInstance) vv;
//                if (obj.isAndroidActivity()) {
//                    logger.debug("* Activity " + obj + " cloned  ");
//                }
//            }
//        }

        // Above we see that cloned nodes are OK, below they are not?
        // Add all the edges between the nodes
        for (GraphNode clonedSource : union.getVertices()) {
            // TODO Do not check a node with itself
            for (GraphNode clonedTarget : union.getVertices()) {

                GraphNode originalSource = cloneMap.get(clonedSource);
                GraphNode originalTarget = cloneMap.get(clonedTarget);

                if (!graph.containsVertex(originalSource)) {
                    logger.trace("Graph does not contain " + clonedSource);
                    logger.trace("ALL VERTICES " + graph.getVertices());

                }

                if (!graph.containsVertex(originalTarget)) {
                    logger.trace("Graph does not contain " + clonedTarget);
                    logger.trace("ALL VERTICES " + graph.getVertices());

                }

                // Original grap here.. BUT, it works with == and not equals ! :(
                Collection<String> edges = graph.findEdgeSet(originalSource, originalTarget);
                if (edges != null) {
                    for (String edge : edges) {
                        union.addEdge(edge, clonedSource, clonedTarget, EdgeType.DIRECTED);
                        logger.trace("Adding dependency " + edge);
                    }
                }
            }
        }

//        for (GraphNode vv : union.getVertices()) {
//            if (vv instanceof ObjectInstance) {
//                ObjectInstance obj = (ObjectInstance) vv;
//                if (obj.isAndroidActivity()) {
//                    System.out.println("* Activity " + obj + " cloned  ");
//                }
//            }
//        }

        // Find the weakly connected components
//        this.graph.getVertices().stream().filter(gn -> gn instanceof ObjectInstance).map(gn -> (ObjectInstance) gn)
//                .map(obj -> (obj.isAndroidActivity() ? "** " + obj.toString() : "  " + obj.toString()))
//                .forEach(System.out::println);

        WeakComponentClusterer<GraphNode, String> clusterer = new WeakComponentClusterer<GraphNode, String>();
//        MyWeakComponentClusterer clusterer = new MyWeakComponentClusterer();
        Set<Set<GraphNode>> clusters = clusterer.apply(union);

        // Clusters at this points are partitions of the nodes, nodes are clones of the
        // original nodes, so we can build the sub graphs.
        // Note that the extrapolated graphs will have different edges;
        // they will start from 0 and increment.

        // Is it possible that this changes the attributes of the nodes?!!

        for (Set<GraphNode> cluster : clusters) {

//            System.out.println("DataDependencyGraphImpl.extrapolate() NODES IN CLUSTER : ");
//            cluster.stream().filter(gn -> gn instanceof ObjectInstance).map(gn -> (ObjectInstance) gn)
//                    .map(obj -> (obj.isAndroidActivity() ? "** " + obj.toString() : "  " + obj.toString()))
//                    .forEach(System.out::println);
//            System.out.println("\n\n\n");
            logger.trace("Processing cluster " + cluster);
            // TODO Here probably one should use the INTERFACE not the implementation
            DataDependencyGraphImpl dataDependencyGraph = new DataDependencyGraphImpl();
            for (GraphNode vertex : cluster) {
                // Add Data and Method Invocations to the graph
                if (!dataDependencyGraph.graph.addVertex(vertex)) {
                    logger.trace("ERROR: Cannot add vertex" + vertex);
                }
            }
            // Connect them
            for (GraphNode source : dataDependencyGraph.graph.getVertices()) {
                for (GraphNode dest : dataDependencyGraph.graph.getVertices()) {
                    GraphNode originalSource = cloneMap.get(source);
                    GraphNode originalTarget = cloneMap.get(dest);

                    Collection<String> edges = graph.findEdgeSet(originalSource, originalTarget);
                    if (edges != null) {
                        for (String edge : edges) {
                            dataDependencyGraph.graph.addEdge(edge, source, dest, EdgeType.DIRECTED);
                        }
                    }
                }
            }

            extrapolated.add(dataDependencyGraph);
        }

        return extrapolated;

    }

}
