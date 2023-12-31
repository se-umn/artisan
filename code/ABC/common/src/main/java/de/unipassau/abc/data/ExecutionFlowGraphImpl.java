package de.unipassau.abc.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.utils.GraphUtility;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.event.GraphEvent.Edge;
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
public class ExecutionFlowGraphImpl implements ExecutionFlowGraph {

    private final Logger logger = LoggerFactory.getLogger(ExecutionFlowGraph.class);

    // Note this starts from 1 to match the first Method Invocation id
    private AtomicInteger id = new AtomicInteger(1);
    private AtomicInteger tid = new AtomicInteger(1);

    private Graph<MethodInvocation, String> graph;
    private MethodInvocation lastMethodInvocation = null;
    private MethodInvocation firstMethodInvocation = null;

    public ExecutionFlowGraphImpl() {
        // graph = new SparseMultigraph<MethodInvocation, String>();
        graph = new DirectedSparseMultigraph<>();
    }

    public MethodInvocation retrieveActualVertex(MethodInvocation methodInvocation) {
        MethodInvocation actualMethodInvocation = graph.getVertices().stream().filter(mi -> mi.equals(methodInvocation))
                .findFirst().get();

        return actualMethodInvocation;
    }

    @Override
    public void remove(MethodInvocation methodInvocationToRemove) {

        if (!graph.containsVertex(methodInvocationToRemove)) {
            return;
        } else {
            methodInvocationToRemove = this.retrieveActualVertex(methodInvocationToRemove);
        }

        // Collect prev/next
        MethodInvocation prev = null;
        MethodInvocation succ = null;

        if (graph.getPredecessorCount(methodInvocationToRemove) > 0) {
            // NOTE By design there's only at most one predecessors
            prev = graph.getPredecessors(methodInvocationToRemove).iterator().next();
        }

        if (graph.getSuccessorCount(methodInvocationToRemove) > 0) {
            // NOTE By design there's only at most one predecessors
            succ = graph.getSuccessors(methodInvocationToRemove).iterator().next();
        }

        // Remove the node - this removes also any in/out edges
        graph.removeVertex(methodInvocationToRemove);

        // Connect the previous prev to succ if they are both non-null
        if (prev != null && succ != null) {
            String edge = generateNewEdge();
            graph.addEdge(edge, prev, succ, EdgeType.DIRECTED);
        }

        // Update Head/Tail
        if (firstMethodInvocation == methodInvocationToRemove) {
            firstMethodInvocation = succ;
        }

        if (lastMethodInvocation == methodInvocationToRemove) {
            lastMethodInvocation = prev;
        }
    }

    public void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl) {
        MethodInvocation originalMethodInvocation = retrieveActualVertex(orig);

        if (firstMethodInvocation == originalMethodInvocation) {
            firstMethodInvocation = repl;
        }

        if (lastMethodInvocation == originalMethodInvocation) {
            lastMethodInvocation = repl;
        }

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

        inEdgesWithSources
                .forEach(edgeNameMethodInvocationPair -> graph.addEdge(edgeNameMethodInvocationPair.getFirst(),
                        edgeNameMethodInvocationPair.getSecond(), repl, EdgeType.DIRECTED));
        outEdgesWithDestinations
                .forEach(edgeNameMethodInvocationPair -> graph.addEdge(edgeNameMethodInvocationPair.getFirst(), repl,
                        edgeNameMethodInvocationPair.getSecond(), EdgeType.DIRECTED));
    }

    @Deprecated
    public void addOwnerToMethodInvocation(MethodInvocation methodInvocation, ObjectInstance object) {
        addOwnerToMethodInvocation(methodInvocation, object.getObjectId());
    }

    @Deprecated
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
        }
    }

    // For some reason, doing this breaks everything
//    public void injectMethodInvocationAfter(MethodInvocation methodInvocationToInsert,
//            MethodInvocation methodInvocationBefore) throws ABCException {
//
//        // Assume this exists
//        MethodInvocation actualMethodInvocationBefore = retrieveActualVertex(methodInvocationBefore);
//
//        if (actualMethodInvocationBefore.equals(lastMethodInvocation)) {
//            enqueueMethodInvocations(methodInvocationToInsert);
//        } else {
//            String outgoingFromBefore = graph.getOutEdges( actualMethodInvocationBefore).iterator().next();
//            MethodInvocation after = graph.getOpposite(actualMethodInvocationBefore, outgoingFromBefore);
//            
//            System.out.println("Removing Edge " + outgoingFromBefore + " between  " + actualMethodInvocationBefore + " and " + after );
//            graph.removeEdge(outgoingFromBefore);
//
//            if (!graph.containsVertex(methodInvocationToInsert)) {
//                if (! graph.addVertex(methodInvocationToInsert) ) {
//                    throw new ABCException("Cannot include methodInvocationToInsert!");
//                } else {
//                    System.out.println("Added " + methodInvocationToInsert);
//                }
//            } else {
//                throw new ABCException("Cannot include the same invocation twice!");
//            }
//            
//            graph.addEdge(outgoingFromBefore, actualMethodInvocationBefore, methodInvocationToInsert, EdgeType.DIRECTED);
//            System.out.println("Readding " + outgoingFromBefore + " from " + actualMethodInvocationBefore + " and " + methodInvocationToInsert );
//
//            // Now we add an edge to connect the methodInvocation
//            String edge = "ExecutionDependency-" + id.getAndIncrement();
//            graph.addEdge( edge , methodInvocationToInsert, after, EdgeType.DIRECTED);
//            System.out.println("Adding " + edge + " from " + methodInvocationToInsert + " and " + after );
//            
//            // This might create duplicates !
//            methodInvocationToInsert.invocationCount = methodInvocationBefore.getInvocationCount();
//
//            System.out.println("ExecutionFlowGraphImpl.injectMethodInvocationAfter() Insert as "
//                    + methodInvocationToInsert.invocationCount + " !");
//        }
//
//    }

    public void prependMethodInvocation(MethodInvocation methodInvocation) {
        if (firstMethodInvocation == null) {
            // In this case, we can simply add it
            enqueueMethodInvocations(methodInvocation);
        } else {
//            int headID = firstMethodInvocation.getInvocationCount() - 1;
//            // Unsafe
//            methodInvocation.invocationId = headID;
            if (!graph.containsVertex(methodInvocation)) {
                // Otherwise
                graph.addVertex(methodInvocation);
                // Add it to the graph as FIRST !
                String edge = generateNewEdge();
                graph.addEdge(edge, methodInvocation, firstMethodInvocation, EdgeType.DIRECTED);
                // Update the head of the queue
                firstMethodInvocation = methodInvocation;
            }
        }
    }

    public void enqueueMethodInvocations(MethodInvocation methodInvocation) {
        if (!graph.containsVertex(methodInvocation)) {
            graph.addVertex(methodInvocation);
        }

        if (lastMethodInvocation != null) {
            String edge = generateNewEdge();
            graph.addEdge(edge, lastMethodInvocation, methodInvocation, EdgeType.DIRECTED);
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
     * Return the calls in the Execution Flow Graph which match carveBy (include)
     * but not excludeBy (exclude). We also omit private method invocations since
     * those cannot be carved !
     *
     * @param carveBy
     * @param excludeBy
     * @return
     */
    public List<MethodInvocation> getMethodInvocationsFor(MethodInvocationMatcher carveBy,
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
     * Returns all the elements before the provided one including the provided one.
     * This can be also implemented differently with the index parameter on
     * invocation.
     *
     * @param methodInvocation
     * @return
     */
    public Set<MethodInvocation> getMethodInvocationsBefore(MethodInvocation methodInvocation,
            Predicate<MethodInvocation> predicate) {
        boolean includeTheGivenMethodInvocation = true; // NOT SURE WHY ONE
        // WOULD DO THIS..
        return getMethodInvocationsBefore(methodInvocation, includeTheGivenMethodInvocation, predicate);
    }

    public int getPositionOf(MethodInvocation methodInvocation) {
        return getOrderedMethodInvocations().indexOf(methodInvocation);
    }

    public Set<MethodInvocation> getMethodInvocationsBefore(MethodInvocation methodInvocation, boolean inclusive,
            Predicate<MethodInvocation> predicate) {
        // This creates a copy
        int fromIndex = 0;

        // This shold use the position in the sequence of those methods not
        // their count !
        int toIndex = (inclusive) ? getPositionOf(methodInvocation) : getPositionOf(methodInvocation) - 1;
        // In case the method is not there...
        if (toIndex < fromIndex) {
            return new HashSet<>();
        }

        List<MethodInvocation> orderedInvocationsBefore = getOrderedMethodInvocations().subList(fromIndex, toIndex);
        // Remove the elements that do not match the predicate
        orderedInvocationsBefore.removeIf(predicate.negate());
        // This might not be necessary for removing the duplicates...
        return new HashSet<>(orderedInvocationsBefore);
    }

    /**
     * Returns an ordered list of invocations registered BEFORE (inclusive) the
     * given one...
     *
     * @param methodInvocation
     * @return
     */
    public List<MethodInvocation> getOrderedMethodInvocationsBefore(MethodInvocation methodInvocation) {

        List<MethodInvocation> orderedMethodInvocationsBefore = new ArrayList<>(graph.getVertices());
        // Sort them. Method invocation implements comparable
        Collections.sort(orderedMethodInvocationsBefore);
        // Find the position of the given mi
        int position = orderedMethodInvocationsBefore.indexOf(methodInvocation);
        // now remove all the elements that are after that position. Since the
        // resulting list should should include the given mi
        // NOTE that sublist is exclusive for toIndex !
        orderedMethodInvocationsBefore.subList(position, orderedMethodInvocationsBefore.size()).clear();
        // We need to ensure the given mi is there, as last invocation
        // orderedMethodInvocationsBefore.add(methodInvocation);
        return orderedMethodInvocationsBefore;

        // if (!graph.containsVertex(methodInvocation)) {
        // logger.debug("Method invocation " + methodInvocation + " not in the
        // execution graph");
        // return Collections.<MethodInvocation>emptyList();
        // }
        //
        // List<MethodInvocation> predecessorsOf = new ArrayList<>();
        // for (MethodInvocation mi : getPredecessors(methodInvocation)) {
        // predecessorsOf.addAll(getOrderedMethodInvocationsBefore(mi));
        // }
        // // As last add the mi passed as parameter
        // predecessorsOf.add(methodInvocation);
        //
        // return predecessorsOf;
    }

    /**
     * Returns all the elements before the provided one including the provided one.
     * This can be also implemented differently with the index parameter on
     * invocation.
     *
     * @param methodInvocation
     * @return
     */
    public Set<MethodInvocation> getMethodInvocationsAfter(MethodInvocation methodInvocation) {
        return new HashSet<>(getOrderedMethodInvocationsAfter(methodInvocation));
    }

    public List<MethodInvocation> getMethodInvocationsSortedByID() {

        List<MethodInvocation> allMethodInvocations = new ArrayList<>(graph.getVertices());
        // Sort them. Method invocation implements comparable
        Collections.sort(allMethodInvocations);
        // Return all of them
        return allMethodInvocations;
    }

    // TODO: Double check that this actually produces the same results as
    // before.
    // Plus check why we needed the methodInvocation to be in this list...
    // Why we need this logic? Because during carving the number of method calls is
    // Huge
    public List<MethodInvocation> getOrderedMethodInvocationsAfter(MethodInvocation methodInvocation) {

        List<MethodInvocation> orderedMethodInvocationsAfter = new ArrayList<>(graph.getVertices());
        // Sort them. Method invocation implements comparable
        Collections.sort(orderedMethodInvocationsAfter);

        if (orderedMethodInvocationsAfter.isEmpty() || orderedMethodInvocationsAfter.size() == 1)
            return orderedMethodInvocationsAfter;

        // Find the position of the given mi
        int position = orderedMethodInvocationsAfter.indexOf(methodInvocation);

        if (position == -1) {
            logger.warn("The provided method invocation is NOT in the list" + methodInvocation);
            position = orderedMethodInvocationsAfter.size() - 1;
        }

        // now remove all the elements that are before that position. The
        // resulting list should include the given mi
        orderedMethodInvocationsAfter.removeAll(orderedMethodInvocationsAfter.subList(0, position));

        return orderedMethodInvocationsAfter;

        // try {
        // List<MethodInvocation> successorsOf = new ArrayList<>();
        // logger.debug("getOrderedMethodInvocationsAfter " + methodInvocation);
        // // Add the actual one ?! Why is this here ?!
        // successorsOf.add(methodInvocation);
        //
        // // getSuccessors(methodInvocation) can return null !? !
        // for (MethodInvocation mi : getSuccessors(methodInvocation)) {
        //
        // if (successorsOf.contains(mi)) {
        // logger.error(mi + " is already in the successors list ?!");
        // } else {
        // // Add the one reach from this one
        // successorsOf.addAll(getOrderedMethodInvocationsAfter(mi));
        // }
        // }
        // return successorsOf;
        // } catch (Throwable e) {
        // e.printStackTrace();
        // throw e;
        // }
    }

    public Collection<MethodInvocation> getSuccessors(MethodInvocation methodInvocation) {
        Collection<MethodInvocation> successors = graph.getSuccessors(methodInvocation);
        return (Collection<MethodInvocation>) (successors != null ? successors : new HashSet<>());
    }

    /*
     * Enable fast computation of boundaries when looking up for previous/last
     * calls.
     */
    public MethodInvocation getFirstMethodInvocation() {
        return firstMethodInvocation;
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
                // logger.trace("ExecutionFlowGraph.refine() Remove " + node + "
                // as not required");
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
                // logger.trace("ExecutionFlowGraph.refine() Updated
                // firstMethodInvocation to " + firstMethodInvocation);
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
                // logger.trace("ExecutionFlowGraph.refine() Updated
                // lastMethodInvocation to " + lastMethodInvocation);

            }

            // TODO first and last element can definitively be computed
            // afterwards by following the flow relations

            Collection<String> flowEdges = new HashSet<>(graph.getInEdges(mi));
            flowEdges.addAll(graph.getOutEdges(mi));

            for (String flowEdge : flowEdges) {
                // logger.trace("ExecutionFlowGraph.refine() Removing Edge " +
                // flowEdge);
                graph.removeEdge(flowEdge);
            }
            // Really its just one
            for (MethodInvocation predecessor : predecessors) {
                for (MethodInvocation successor : successors) {
                    String edge = generateNewEdge();
                    graph.addEdge(edge, predecessor, successor, EdgeType.DIRECTED);
                    // logger.trace("ExecutionFlowGraph.refine() Introducing
                    // replacemente edge "
                    // + graph.getEndpoints(edgeLabel) + " added " + added);
                }
            }

            graph.removeVertex(mi);
            // logger.trace("ExecutionFlowGraph.refine() Removed " + mi);
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
        ExecutionFlowGraphImpl other = (ExecutionFlowGraphImpl) obj;

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

    /**
     * By default this will return a new graph object where nodes and esges are
     * cloned from the original graph
     */
    public ExecutionFlowGraph getSubGraph(List<MethodInvocation> orderedSlice) {
        ExecutionFlowGraphImpl subGraph = new ExecutionFlowGraphImpl();
        for (MethodInvocation methodInvocation : orderedSlice) {
            subGraph.enqueueMethodInvocations(methodInvocation);
        }
        return subGraph;
    }

    /**
     * By default this will return a new graph object where nodes and esges are
     * cloned from the original graph.
     * 
     */
    public Collection<ExecutionFlowGraph> extrapolate(Collection<MethodInvocation> methodInvocations) {
        Collection<ExecutionFlowGraph> extrapolated = new ArrayList<ExecutionFlowGraph>();

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
                    logger.debug("Graph does not contain " + source);
                    logger.debug("ALL VERTICES " + graph.getVertices());
                }

                if (!graph.containsVertex(originalTarget)) {
                    logger.debug("Graph does not contain " + target);
                    logger.debug("ALL VERTICES " + graph.getVertices());

                }

                // Original grap here.. BUT, it works with == and not equals ! :(
                Collection<String> edges = graph.findEdgeSet(originalSource, originalTarget);
                if (edges != null) {
                    for (String edge : edges) {
                        if (!union.addEdge(edge, source, target, EdgeType.DIRECTED)) {
                            logger.error("EDGE NOT ADDED");
                        }
                    }
                }
            }
        }

        WeakComponentClusterer<MethodInvocation, String> clusterer = new WeakComponentClusterer<MethodInvocation, String>();
        // TODO: For whatever freaking reason this creates new instances of the nodes,
        // but do not invoke CLONE !
        Set<Set<MethodInvocation>> clusters = clusterer.apply(union);

        // Clusters at this points are partions of the nodes, nodes are clones of the
        // original nodes, but somehow NOT all the so we can build the sub graphs.
        // Note that the extrapolated graphs will have different edges;
        // they will start from 0 and increment, each one. However, the cloned method
        // invocations will retain their original value inside invocationCount

        // TODO It seems that the information about being a necessary method invocation
        // (isNecessary) is gone at this. Instances in the cluster are NOT the ones
        // inside union, so the clusterer somehow clone or copy them.
        // point?
        for (Set<MethodInvocation> cluster : clusters) {

            List<MethodInvocation> orderedMethodInvocations = cluster.stream().sorted().collect(Collectors.toList());

            ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();

            for (MethodInvocation methodInvocation : orderedMethodInvocations) {
                executionFlowGraph.enqueueMethodInvocations(methodInvocation);
            }

            extrapolated.add(executionFlowGraph);

        }
        return extrapolated;
    }

    @Deprecated
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
        return new HashSet<>(getOrderedMethodInvocationsToExternalInterfaceBefore(methodInvocationToCarve));
    }

    public List<MethodInvocation> getOrderedMethodInvocationsToExternalInterfaceBefore(
            MethodInvocation methodInvocationToCarve) {
        List<MethodInvocation> all = new ArrayList<>(getOrderedMethodInvocationsBefore(methodInvocationToCarve));
        // Now remove everything not an external interface
        all.removeIf(methodInvocation -> !methodInvocation.belongsToExternalInterface());
        return all;
    }

    public void include(ExecutionFlowGraph other) {
        /*
         * Take all the method invocations from the current graph and make the union
         * with the "other" graph. We remove the duplicates this way
         */
        Set<MethodInvocation> allMethodInvocations = new HashSet<MethodInvocation>(this.getOrderedMethodInvocations());
        allMethodInvocations.addAll(other.getOrderedMethodInvocations());
        /*
         * Order the resulting method invocations
         */
        List<MethodInvocation> orderedMethodInvocations = new ArrayList<>(allMethodInvocations);
        Collections.sort(orderedMethodInvocations);
        /*
         * Recreate the internal graph structure
         */
        // TODO What's the difference between these two ?
        id = new AtomicInteger(0);
        tid = new AtomicInteger(0);
        //
        firstMethodInvocation = null;
        lastMethodInvocation = null;
        graph = new DirectedSparseMultigraph<MethodInvocation, String>();
        //
        for (MethodInvocation methodInvocation : orderedMethodInvocations) {
            this.enqueueMethodInvocations(methodInvocation);
        }
    }

    /*
     * Remove from the graph all the subsumed calls. Will this update also the
     * relations between method invocations
     */
    public void summarize(CallGraph callGraph) {
        Set<MethodInvocation> unecessaryMethodInvocations = new HashSet<>();
        for (MethodInvocation subsumingCall : getOrderedMethodInvocations()) {
            for (MethodInvocation subsumedCall : callGraph.getMethodInvocationsSubsumedBy(subsumingCall)) {
                if (graph.containsVertex(subsumedCall) && !unecessaryMethodInvocations.contains(subsumedCall)) {
                    logger.trace("> " + subsumedCall + " as this is subsumed by " + subsumingCall);
                    unecessaryMethodInvocations.add(subsumedCall);
                }

            }
        }

        Set<MethodInvocation> necessaryMethodInvocations = new HashSet<>();
        necessaryMethodInvocations.addAll(getOrderedMethodInvocations());
        necessaryMethodInvocations.removeAll(unecessaryMethodInvocations);

        this.refine(necessaryMethodInvocations);
    }

    public void dequeue(MethodInvocation toDrop) {
        // Remove the last method invocation
        if (lastMethodInvocation.equals(toDrop)) {
            List<MethodInvocation> before = getOrderedMethodInvocationsBefore(toDrop);
            // remove itself from the list if is there...
            before.remove(toDrop);
            //
            boolean removed = graph.removeVertex(toDrop);
            if (!removed) {
                logger.warn("Cannot dequeue " + toDrop);
                return;
            }
            // Probably will fail for size == 1;
            // Not sure why remove does not really remove it...
            lastMethodInvocation = before.get(before.size() - 1);

        } else {
            logger.warn("Cannot dequeue " + toDrop + " as this is not the last invocation " + lastMethodInvocation);
        }
    }

    public String prettyPrint() {
        StringBuffer sb = new StringBuffer();
        for (MethodInvocation m : getOrderedMethodInvocations()) {
            sb.append(m).append("\n");
        }
        return sb.toString();
    }

    public void removeMethodInvocation(MethodInvocation toDrop) {
        List<MethodInvocation> requiredMethodInvocations = new ArrayList<>(getOrderedMethodInvocations());
        if (requiredMethodInvocations.remove(toDrop)) {
            this.refine(new HashSet<>(requiredMethodInvocations));
        } else {
            logger.warn("Cannot removeMethodInvocation " + toDrop);
        }
    }

    @Override
    public void insertNodeRightBefore(MethodInvocation nodeToPlace, MethodInvocation _original) throws ABCException {
        try {
            retrieveActualVertex(nodeToPlace);
            throw new ABCException("Cannot insert node if already in the graph!");
        } catch (java.util.NoSuchElementException e) {
            // Pass
        }

        MethodInvocation original = retrieveActualVertex(_original);

        Collection<MethodInvocation> predecessors = getPredecessors(original);
        // This means original is the first node, we just add nodeToPlace as head
        if (predecessors.size() == 0) {
            logger.debug("Add " + nodeToPlace + " as first node");
            prependMethodInvocation(nodeToPlace);
        } else {
            // Collect and remove the incoming = Not sure this returns a copy, so we make
            // sure it is the actual node
            MethodInvocation prev = retrieveActualVertex(predecessors.iterator().next());
            // There must be only one...
            String edgeToRemove = graph.getInEdges(original).stream().findFirst().get();

            if (!graph.removeEdge(edgeToRemove)) {
                throw new ABCException("Cannot remove edge " + edgeToRemove + " between "
                        + graph.getSource(edgeToRemove) + " and " + graph.getDest(edgeToRemove));
            } else {
                // This must should null... meaning there not more link between the nodes
                logger.debug("Removed Edge " + edgeToRemove);
            }

            // Insert the new node
            graph.addVertex(nodeToPlace);

            // Link prev to the node and node to the original (using new edges?)
            String prevToNode = generateNewEdge();
            graph.addEdge(prevToNode, prev, nodeToPlace, EdgeType.DIRECTED);
            logger.debug("Added new edge " + prevToNode + " between " + graph.getSource(prevToNode) + " and "
                    + graph.getDest(prevToNode));

            String nodeToOriginal = generateNewEdge();
            graph.addEdge(nodeToOriginal, nodeToPlace, original, EdgeType.DIRECTED);
            logger.debug("Added new edge " + nodeToOriginal + " between " + graph.getSource(nodeToOriginal) + " and "
                    + graph.getDest(nodeToOriginal));
        }

    }

    private String generateNewEdge() {
        String edge = "ExecutionDependency-" + id.getAndIncrement();
        while (graph.containsEdge(edge)) {
            edge = "ExecutionDependency-" + id.getAndIncrement();
        }
        return edge;
    }

    @Override
    public MethodInvocation get(MethodInvocation methodInvocation) {
        return retrieveActualVertex(methodInvocation);
    }

}
