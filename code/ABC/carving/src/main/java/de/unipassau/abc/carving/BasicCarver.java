package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.CallGraphImpl;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.ImplicitDependencyOnStaticClass;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.NullInstance;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;

/**
 * Basic carver is a method carver, that is, it will produce a carved execution
 * whose last element is the invocation of the given method or methods
 * 
 * 
 * @author gambitemp
 *
 */
public class BasicCarver implements MethodCarver {

    // TODO Has this to be global/static?
    // For each MethodInvocation it links the necessary method invocations found for
    // it/
    // TODO For the moment we can carve the necessary method invocations of the
    // carved methods instead of storing everything
    private static Map<MethodInvocation, Set<MethodInvocation>> globalCache = new HashMap<MethodInvocation, Set<MethodInvocation>>();

    public static void resetGlobalCache() {
        globalCache.clear();
    }

    private static Logger logger = LoggerFactory.getLogger(BasicCarver.class);

    private static boolean globalCachedEnabled = false;

    final private ExecutionFlowGraph executionFlowGraph;
    final private DataDependencyGraph dataDependencyGraph;
    final private CallGraph callGraph;
    private String traceId;

    // Bookkeep the carved method invocations. - Why this is reset every time?
    private Set<MethodInvocation> carvedMethodInvocationsCache = new HashSet<MethodInvocation>();

    // This enables carving from a carvedExecution
    public BasicCarver(CarvedExecution carvedExecution) {
        // We need to MERGE the clusters back together
        // Here we assume that the graphs can be simply merged together...

        this.dataDependencyGraph = new DataDependencyGraphImpl();
        carvedExecution.dataDependencyGraphs.stream().forEach(ddg -> this.dataDependencyGraph.include(ddg));

        this.executionFlowGraph = new ExecutionFlowGraphImpl();
        carvedExecution.executionFlowGraphs.stream().forEach(efg -> this.executionFlowGraph.include(efg));

        this.callGraph = new CallGraphImpl();
        carvedExecution.callGraphs.stream().forEach(cg -> this.callGraph.include(cg));
    }

    public BasicCarver(ParsedTrace parsedTrace) {
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> executionTraceForMainThread = parsedTrace
                .getThreadParsedTraceFor(ParsedTrace.MAIN_THREAD);

        this.executionFlowGraph = executionTraceForMainThread.getFirst();
        this.dataDependencyGraph = executionTraceForMainThread.getSecond();
        this.callGraph = executionTraceForMainThread.getThird();
        
        this.traceId = parsedTrace.traceFileName();

    }

    /**
     * This carver follows a basic heuristic:
     * <ol>
     * <li>Locate the method invocation in the graph and its owner</li>
     * <li>Collect the method invocations on the same owner (if any) that happen
     * before the target method invocation</li>
     * <li>Collect the object instances that have been used as parameters for each
     * of those method invocations</li>
     * <li>For each object instance, collects all the method invocations that have
     * been called on them BEFORE their use. (this should take care of both Direct
     * Data Dependencies (params, return) and Indirect Data Dependencies (via
     * reference, static referencing).</li>
     * <li>For each of those methods, repeat the carving process</li>
     * </ol>
     * 
     * 
     * 
     */

    private Set<MethodInvocation> findNecessaryMethodInvocations(MethodInvocation methodInvocation) {

        logger.debug("* Find necessary method invocations for" + methodInvocation.toString());

        // For the moment this is limited to return carvable targets that have been
        // already carved out
        if (globalCachedEnabled && globalCache.containsKey(methodInvocation)) {
            logger.debug(
                    "- " + methodInvocation + " already carved. Return necessary invocations from the global cache.");
            // Set the method invocations to be necessary
            Set<MethodInvocation> cached = globalCache.get(methodInvocation);
            cached.forEach(mi -> {
                mi.setNecessary(true);
            });

            return cached;
        }

        Set<MethodInvocation> relevantMethodInvocations = new HashSet<>();

        // The first relevant method invocation is the one to carve
        relevantMethodInvocations.add(methodInvocation);

        if (isMethodInvocationAlreadyCarved(methodInvocation)) {
            logger.debug("Method invocation already cached: " + methodInvocation.toString());
            // Return an empty result
            return relevantMethodInvocations;
        }

        /*
         * Relevant method invocations are the ones that have been called before on the
         * SAME object, including the constructor or whatever method is used to
         * instantiate the object.
         * 
         * TODO Note that System-level factory methods may be considered necessary at
         * this point
         */
        if (!methodInvocation.isStatic()) {
            final ObjectInstance methodInvocationOwner = methodInvocation.getOwner();

            /*
             * Locate all the method invocations that have been invoked on the same object
             * before invoking methodInvocation
             */
            relevantMethodInvocations.addAll(this.executionFlowGraph.getMethodInvocationsBefore(methodInvocation,
                    /*
                     * Make sure we compare always the non-null with the possibly null otherwise it
                     * fails silenty and return an empyt set
                     */
                    t -> methodInvocationOwner.equals(t.getOwner())));
            logger.debug("Adding " + relevantMethodInvocations.size() + " method invocations on owner: " + methodInvocationOwner);
            logger.debug("  " + Arrays.toString(relevantMethodInvocations.toArray()));
        } else {
            // TODO In theory those are NOT mutually exclusive since a class may have static
            // methods and instances...

            /*
             * Locate all the method invocations the have been invoked on the same STATIC
             * object before invoking methodInvocation
             */
            List<MethodInvocation> staticMethodsOnSameClass = new ArrayList(
                    this.executionFlowGraph.getMethodInvocationsBefore(methodInvocation,
                            t -> t.isStatic() && JimpleUtils.getClassNameForMethod(t.getMethodSignature())
                                    .equals(JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()))));

            relevantMethodInvocations.addAll(staticMethodsOnSameClass);

            logger.debug("Adding " + relevantMethodInvocations.size() +" method invocations on STATIC of same class: "
                    + Arrays.toString(relevantMethodInvocations.toArray()));

        }

        Set<MethodInvocation> relevantMethodInvocationsOnAliases = new HashSet<>();

        /*
         * Relevant method invocations are also the ones that have been called before on
         * any of the KNOWN alias of the SAME object, including the constructor used to
         * instantiate them.
         */
        if (!methodInvocation.isStatic()) {
            Set<ObjectInstance> aliasesOfMethodInvocationOwner = dataDependencyGraph
                    .getAliasesOf(methodInvocation.getOwner());

            for (ObjectInstance alias : aliasesOfMethodInvocationOwner) {
                logger.debug("Including method invocations of alias " + alias);
                /*
                 * Locate all the method invocations that have been invoked on the alias of the
                 * object before invoking methodInvocation
                 */
                relevantMethodInvocationsOnAliases
                        .addAll(this.executionFlowGraph.getMethodInvocationsBefore(methodInvocation,
                                /*
                                 * Make sure we compare always the non-null with the possibly null otherwise it
                                 * fails silenty and return an empyt set
                                 */
                                t -> alias.equals(t.getOwner())));

            }
        }

        logger.debug("Adding method invocations on ALIAS owner "
                + Arrays.toString(relevantMethodInvocationsOnAliases.toArray()));

        // Put them together
        relevantMethodInvocations.addAll(relevantMethodInvocationsOnAliases);

        // Do relevantMethodInvocations contains the constructor or at least on
        // generator?
        // This FAIL the multi activity minimization
//        if ( ! methodInvocation.isStatic() && relevantMethodInvocations.stream()
//                .filter(mi -> mi.isConstructor() && mi.getOwner().equals(methodInvocation.getOwner())).count() == 0) {
//            logger.debug("Not constructore found for " + methodInvocation.getOwner());
//            // Possible constructors or generators
//            // TODO This might not work
//            Optional<MethodInvocation> generator = dataDependencyGraph
//                    .getMethodInvocationsWhichReturn(methodInvocation.getOwner()).stream()
//                    .filter(mi -> mi.getInvocationTraceId() < methodInvocation.getInvocationTraceId()).findFirst();
//            if (generator.isPresent()) {
//                logger.debug("Take the first generator " + generator.get());
//                relevantMethodInvocations.add(generator.get());
//            } else {
//                // TODO Stop execution if the CUT is Dangling !!!
//                logger.debug("Object " + methodInvocation.getOwner() + " is dangling !!!");
//            }
//
//        }

        /*
         * The last relevant method invocations are the one that have been called by the
         * objects used by this methodInvocation at some point
         */
        Map<ObjectInstance, List<MethodInvocation>> relevantUses = new HashMap<>();

        /*
         * Relevant Uses (not sure what are they).
         */
        logger.debug("Collecting data dependencies for relevant " + relevantMethodInvocations.size() + " method invocations");

        // TODO Maybe we can avoid this because we have them cached? So we do not add
        // them

        // Use a map for debugging?
        Set<MethodInvocation> relevantMethodInvocationsOnDataDeps = new HashSet<>();

        int i = 1;
        int all = relevantMethodInvocations.size();
        
        for (Iterator<MethodInvocation> it = relevantMethodInvocations.iterator(); it.hasNext(); i++) {
            
            MethodInvocation relevantMethodInvocation = it.next();

            Set<DataNode> dataDendencies = new HashSet<DataNode>();

            // Process the parameters
            for (DataNode parameter : this.dataDependencyGraph.getParametersOf(relevantMethodInvocation)) {
                dataDendencies.add(parameter);
                if (parameter instanceof ObjectInstance) {
                    dataDendencies.addAll(this.dataDependencyGraph.getAliasesOf((ObjectInstance) parameter));
                }
            }
            // Include implicit deps
            for (DataNode parameter : this.dataDependencyGraph
                    .getImplicitDataDependenciesOf(relevantMethodInvocation)) {
                dataDendencies.add(parameter);
                if (parameter instanceof ObjectInstance) {
                    dataDendencies.addAll(this.dataDependencyGraph.getAliasesOf((ObjectInstance) parameter));
                }
            }

            logger.debug( i + "/" + all + ") Found " +dataDendencies.size() + " DataDendencies for " + relevantMethodInvocation + ": "
                    + Arrays.toString(dataDendencies.toArray()));

            // TODO Really we should also look at system calls that take this data as
            // parameter as they may change it using side-effects

            // Look for the last time this data dependency was invoked BEFORE the
            // methodInvocation to carve (the others will be included
//            transitively)... but way to wait?

            for (final DataNode dataDep : dataDendencies.stream().filter(dn -> dn instanceof ObjectInstance)
                    .toArray(DataNode[]::new)) {

                if (dataDep instanceof NullInstance) {
                    continue;
                }

                // TODO For static methods this is tricky... because some of them are indeed
                // needed
                List<MethodInvocation> usesOfDataDep = new ArrayList(this.executionFlowGraph.getMethodInvocationsBefore(
                        methodInvocation, t -> !t.isStatic() && t.getOwner().equals(dataDep)));
                // Be sure they are returned sorted
                Collections.sort(usesOfDataDep);
                logger.debug("Visible uses of " + dataDep + " before " + methodInvocation + " are : "
                        + Arrays.toString(usesOfDataDep.toArray()));

                if (usesOfDataDep.size() > 0) {
                    MethodInvocation lastUse = usesOfDataDep.get(usesOfDataDep.size() - 1);
                    relevantMethodInvocationsOnDataDeps.add(lastUse);
                    logger.debug("Last visible use of " + dataDep + " before " + methodInvocation + " is " + lastUse);
                } else {
                    // This may happen for Implicit Data Dependencies
                    logger.debug("There are no last visible uses of " + dataDep + " before " + methodInvocation);
                    // Look for a generator, that is a method that returns this instance - This
                    // brings in some issues, as we do not really know what to do if there are more
                    // than one... Probably we should check the one in the same context as
                    // methodInvocation?
                    // TODO We can limit ourselves to KNOWN situation here.. e.g.,
                    // abc.DefaultContextGenerator...
                    logger.debug("Look for possible generators of " + dataDep);
                    List<MethodInvocation> generators = new ArrayList(this.executionFlowGraph
                            .getMethodInvocationsBefore(methodInvocation, t -> dataDep.equals(t.getReturnValue())));
                    if (generators.size() > 0) {
                        logger.debug("Found " + generators.size() + " generators for " + dataDep);
                        generators.stream().map(g -> g.toString()).forEach(logger::debug);
                        // TODO RISKY ! Take the first
                        logger.debug("Select " + generators.get(0));
                        relevantMethodInvocationsOnDataDeps.add(generators.get(0));
                    }
//                    else {
//                        logger.debug("There are no visible generators for " + dataDep);
//                    }

                }
            }

            // Include Implicit Data Dependency on Static Method calls
            for (final DataNode dataDep : dataDendencies.stream()
                    .filter(dn -> dn instanceof ImplicitDependencyOnStaticClass).toArray(DataNode[]::new)) {
                // TODO For static methods this is tricky... because some of them are indeed
                // needed
                List<MethodInvocation> usesOfDataDep = new ArrayList(this.executionFlowGraph
                        .getMethodInvocationsBefore(methodInvocation, t -> t.isStatic() && JimpleUtils
                                .getClassNameForMethod(t.getMethodSignature()).equals(dataDep.getType())));

                // Be sure they are returned sorted
                Collections.sort(usesOfDataDep);
                logger.debug("Visible uses of STATIC " + dataDep + " before " + methodInvocation + " are : "
                        + Arrays.toString(usesOfDataDep.toArray()));

                if (usesOfDataDep.size() > 0) {
                    MethodInvocation lastUse = usesOfDataDep.get(usesOfDataDep.size() - 1);
                    relevantMethodInvocationsOnDataDeps.add(lastUse);
                    logger.debug(
                            "Last visible use of STATIC " + dataDep + " before " + methodInvocation + " is " + lastUse);
                } else {
                    logger.debug("There are no last visible uses of STATIC " + dataDep + " before " + methodInvocation);
                }
            }

            // Relevant uses are the relevant method invocations that used the data
            // dependency... but why we need it this way?
            // Should we look at the uses of this data dependencies BEFORE to set their
            // state?
//            for (DataNode dataDendency : dataDendencies) {
//                if (dataDendency instanceof NullInstance) {
//                    continue;
//                }
//                if (dataDendency instanceof ObjectInstance) {
//                    ObjectInstance objectParameter = (ObjectInstance) dataDendency;
//                    relevantUses.putIfAbsent(objectParameter, new ArrayList<>());
//                    relevantUses.get(objectParameter).add(relevantMethodInvocation);
//                }
//            }

        }
        logger.debug("Adding method invocations on data deps and their aliases"
                + Arrays.toString(relevantMethodInvocationsOnDataDeps.toArray()));

        // Put them together
        relevantMethodInvocations.addAll(relevantMethodInvocationsOnDataDeps);

        // At this point we find the last method before the use that have been called on
        // each object and carve them !
//        for (Entry<ObjectInstance, List<MethodInvocation>> relevantUse : relevantUses.entrySet()) {
//
//            final ObjectInstance dataDep = relevantUse.getKey();
//
//            // Sort in ascending order of invocation
//            Collections.sort(relevantUse.getValue());
//
//            System.out.println("BasicCarver.carve() relevantUses of dep " + dataDep);
//            relevantUse.getValue().stream().forEach(System.out::println);
//
//            // Get the last one before methodInvocation
//
//            List<MethodInvocation> usesOfDataDep = new ArrayList(this.executionFlowGraph
//                    .getMethodInvocationsBefore(methodInvocation, new Predicate<MethodInvocation>() {
//
//                        @Override
//                        public boolean test(MethodInvocation t) {
//                            if (!t.isStatic()) {
//                                return t.getOwner().equals(dataDep);
//                            }
//                            return false;
//                        }
//                    }));
////             = relevantUse.getValue().get(relevantUse.getValue().size() - 1);
//            //
//            if (usesOfDataDep.size() > 0) {
//                MethodInvocation lastUse = usesOfDataDep.get(usesOfDataDep.size() - 1);
//                relevantMethodInvocations.add(lastUse);
//                System.out.println("Last use of " + dataDep + " before " + methodInvocation + " is " + lastUse);
//            } else {
//                logger.debug("There are no uses of DataDep?!" + dataDep + " relevant for " + relevantUse.getValue());
//            }
//
//        }

        // Mark the given method invocation as already carved
        cacheCarvedMethodInvocation(methodInvocation);

        // Prepare the work by enqueueing all the relevant method invocations found so
        // far that have not yet been carved
        Queue<MethodInvocation> queue = new LinkedList<>();

        // Sort the method invocations so the last one would leverage that the first
        // have been carved already.
        List<MethodInvocation> orderedRelevantMethodInvocations = new ArrayList<>(relevantMethodInvocations);
        // Sort them in reverse mode so we can always start from the last one to the
        // first one
        Collections.reverse(orderedRelevantMethodInvocations);

        // Now check the cache so we do not retrigger the call, however, we pay the
        // price of looking up the graph
        for (MethodInvocation relevantMethodInvocation : orderedRelevantMethodInvocations) {
            if (!isMethodInvocationAlreadyCarved(relevantMethodInvocation)) {
                queue.add(relevantMethodInvocation);
            } else {
                logger.debug("Filter out relevantMethodInvocation " + relevantMethodInvocation
                        + " as it was already cached");
            }
        }

        // Process the job and accumulate data - Fix point algorithm
        while (!queue.isEmpty()) {
            MethodInvocation relevantMethodInvocation = queue.remove();
            relevantMethodInvocations.addAll(findNecessaryMethodInvocations(relevantMethodInvocation));
        }

        return relevantMethodInvocations;
    }

    private void cacheCarvedMethodInvocation(MethodInvocation methodInvocation) {
        carvedMethodInvocationsCache.add(methodInvocation);

    }

    private boolean isMethodInvocationAlreadyCarved(MethodInvocation methodInvocation) {
        return carvedMethodInvocationsCache.contains(methodInvocation);
    }

    @Override
    public List<CarvedExecution> carve(final MethodInvocation methodInvocation) throws CarvingException, ABCException {
        logger.info("-------------------------");
        logger.info("  CARVING: " + methodInvocation);
        logger.info("-------------------------");

        Set<MethodInvocation> necessaryMethodInvocations = new HashSet<MethodInvocation>();

        /*
         * Collect and sort the method invocations necessary for the carving. Note that
         * the findNecessaryMethodInvocations DOES NOT tag the method invocations as
         * necessary, as those will impact future carving activities. Instead, we clone
         * them and explicitly tag them as necessary.
         */

        necessaryMethodInvocations.addAll(findNecessaryMethodInvocations(methodInvocation).stream().map(t -> {
            // Create a clone and mark it as necessary
            MethodInvocation clonedMethodInvocation = t.clone();
            clonedMethodInvocation.setNecessary(true);
            
            return clonedMethodInvocation;
        }).collect(Collectors.toSet()));

        // We use a set to avoid duplicates. The algorithms below can be improved a
        // lot...
        List<MethodInvocation> allMethodInvocations = new ArrayList<MethodInvocation>(necessaryMethodInvocations);
        // SORT THEM
        Collections.sort(allMethodInvocations);
        //
        logger.info("-------------------------");
        logger.info("  NECESSARY INVOCATIONS FOR: " + methodInvocation + " = " + allMethodInvocations.size());
        logger.info("-------------------------");

        if (logger.isDebugEnabled()) {
            allMethodInvocations.stream().map(new Function<MethodInvocation, String>() {

                @Override
                public String apply(MethodInvocation t) {
                    return t.toString();
                }
            }).forEach(logger::debug);
        }

        if (globalCachedEnabled) {
            logger.debug("Storing necessary method invocations for " + methodInvocation + " in the global cache");
            globalCache.put(methodInvocation, necessaryMethodInvocations);
        }

        List<CarvedExecution> carvedExecutions = carveFrom(allMethodInvocations, necessaryMethodInvocations);

        // Make sure we tag those with the correct method under test
        carvedExecutions.forEach(ce -> {
            ce.methodInvocationUnderTest = methodInvocation;
        });
        return carvedExecutions;
    }

    /**
     * Starting from the list of necessary method invocations we create a
     * "consistent" execution up to what we can do. So there will be dangling
     * objects, connected components in the graphs, and in general missing elements
     * here and there.
     */
    public List<CarvedExecution> carveFrom(List<MethodInvocation> allMethodInvocations,
            Set<MethodInvocation> necessaryMethodInvocations) throws CarvingException, ABCException {

        List<CarvedExecution> carvedExecutions = new ArrayList<CarvedExecution>();

        /*
         * Make sure you consider the method invocations that we have tagged as
         * necessary method invocations here. We need them because they are tagged as
         * necessary.
         */

        /*
         * First collect all the method invocations that will be done as consequence of
         * invoking the necessaryMethodInvocations. In theory, executing those method
         * invocations should not require any additional carving.
         */
        // This is to avoid ConcurrentModificationException
        for (MethodInvocation necessaryMethodInvocation : necessaryMethodInvocations) {
            // We need to be sure NOT to override the method invocations marked as relevant
            // here that why we do not use a set.
            // This one takes a little...
            for (MethodInvocation mi : callGraph.getMethodInvocationsSubsumedBy(necessaryMethodInvocation)) {
                if (!allMethodInvocations.contains(mi)) {
                    allMethodInvocations.add(mi);
                }
            }
        }
        Collections.sort(allMethodInvocations);

        if (logger.isDebugEnabled()) {
            logger.debug("-------------------------");
            logger.debug("NECESSARY INVOCATIONS INCLUDING SUBSUMED METHODS");
            logger.debug("-------------------------");

            allMethodInvocations.stream().map(new Function<MethodInvocation, String>() {

                @Override
                public String apply(MethodInvocation t) {
                    return t.toString();
                }
            }).forEach(logger::debug);
        }

        // NOTE: Remember to remove this one before running the experiments:
        // This takes a lot because it needs to lookup the callGraph and such...
//        logger.debug("\n" + PrintUtility.printWithNesting(allMethodInvocations, callGraph));

        // ALESSIO: TODO Why the calls subsumed by necessary calls should be marked as
        // necessary as well?
        /*
         * Union of the necessary method invocation set and the set of method
         * invocations obtained by cloning the subsumed method invocations. Since the
         * hash and equalsTo function have been re-defined for the MethodInvocation, it
         * should hold that 'a.equals(a.clone())'
         * 
         * TODO Assumption: addAll does not replace the objects that are ALREADY inside
         * the set, we need this to propagate the "isNecessary" tag.
         */
//		necessaryMethodInvocations.addAll(allMethodInvocations.stream().map(t -> {
//			// Create a clone and mark it as necessary
//			MethodInvocation clonedMethodInvocation = t.clone();
//			clonedMethodInvocation.setNecessary(true);
//			return clonedMethodInvocation;
//		}).collect(Collectors.toSet()));

        /*
         * Next extract from the traces those fragments (connected components?) that
         * correspond to the method invocations inside necessaryMethodInvocations
         * 
         */

        logger.debug("Extrapolating method invocations from graphs");
        CarvedExecution carvedExecution = new CarvedExecution(traceId);

        // How do we ensure that whatever we extrapolate from the graphs belong
        // together? We order method invocations per id of the first call?
        carvedExecution.executionFlowGraphs = this.executionFlowGraph.extrapolate(new HashSet(allMethodInvocations));

        // List activities
//        System.out.println("Listing Activities:");
//        dataDependencyGraph.getObjectInstances().stream().filter(obj -> obj.getObjectId().contains("Activity"))
//                .map(obj -> (obj.isAndroidActivity()) ? "** " + obj.toString() : "" + obj.toString())
//                .forEach(System.out::println);

        carvedExecution.dataDependencyGraphs = this.dataDependencyGraph.extrapolate(new HashSet(allMethodInvocations));

//        System.out.println("Listing Activities of the extrapolated graphs !:");
//        for (DataDependencyGraph ddg : carvedExecution.dataDependencyGraphs) {
//            ddg.getObjectInstances().stream().filter(obj -> obj.getObjectId().contains("Activity"))
//                    .map(obj -> (obj.isAndroidActivity()) ? "** " + obj.toString() : "" + obj.toString())
//                    .forEach(System.out::println);
//        }

        carvedExecution.callGraphs = this.callGraph.extrapolate(new HashSet(allMethodInvocations));

//        logger.trace(">> Execution Flow Graphs:");
//        int i = 0;
//        for (ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs) {
//            logger.trace("SubGraph " + i);
//            executionFlowGraph.getOrderedMethodInvocations().stream().map(new Function<MethodInvocation, String>() {
//
//                @Override
//                public String apply(MethodInvocation t) {
//                    return (t.isNecessary() ? "* " : " ") + t.toString();
//                }
//            }).forEach(logger::trace);
//            i++;
//        }
//
//        logger.trace("");
//        logger.trace(">> Data Dependency Graphs:");
//        i = 0;
//
//        // TODO Somehow the extrapolated elements do not have the isNecessary attribute
//        // anymore?
//        for (DataDependencyGraph dataDependencyGraph : carvedExecution.dataDependencyGraphs) {
//
//            // List the activity here...
//
//            logger.trace("SubGraph " + i);
//            List<MethodInvocation> sorted = new ArrayList<MethodInvocation>(
//                    dataDependencyGraph.getAllMethodInvocations());
//            Collections.sort(sorted);
//            // Use dataDepGraph.contextualize instead
//            sorted.stream().map(new Function<MethodInvocation, String>() {
//
//                @Override
//                public String apply(MethodInvocation t) {
//                    return (t.isNecessary() ? "* " : " ") + t.toString() + "\n" + //
//                    "- IN (DIRECT) : " + dataDependencyGraph.getParametersOf(t) + "\n" + //
//                    "- IN (IMPLICIT): " + dataDependencyGraph.getImplicitDataDependenciesOf(t) + "\n" + //
//                    "- OUT " + dataDependencyGraph.getReturnObjectLocalFor(t);
//
//                }
//            }).forEach(logger::trace);
//            i++;
//        }
//
//        logger.trace("");
//        logger.trace(">> Call Graphs:");
//        i = 0;
//        for (CallGraph callGraph : carvedExecution.callGraphs) {
//            logger.trace("SubGraph " + i);
//
//            List<MethodInvocation> sorted = new ArrayList<MethodInvocation>(callGraph.getAllMethodInvocations());
//            Collections.sort(sorted);
//            sorted.stream().map(new Function<MethodInvocation, String>() {
//
//                @Override
//                public String apply(MethodInvocation t) {
//                    return (t.isNecessary() ? "* " : " ") + t.toString();
//                }
//            }).forEach(logger::trace);
//            i++;
//        }

        carvedExecutions.add(carvedExecution);

        return carvedExecutions;
    }

    // TODO Does this have to be a LIST for carving? I do not think so...
    @Override
    public Map<MethodInvocation, List<CarvedExecution>> carve(List<MethodInvocation> methodInvocations)
            throws CarvingException, ABCException {

        Map<MethodInvocation, List<CarvedExecution>> carvedExecutions = new HashMap<>();

        for (MethodInvocation methodInvocation : methodInvocations) {
            // Unless we want to push caching further, just clear the cache here
            clearTheCache();

            carvedExecutions.put(methodInvocation, carve(methodInvocation));
        }

        return carvedExecutions;
    }

    public void clearTheCache() {
        carvedMethodInvocationsCache.clear();
    }

    /**
     * This is similar to carving but must be used after we simplify the
     * CarvedTrace. Additionally, recarving cannot change the necessary tag, so we
     * need to ensure that only methods that were necessary before, and are still
     * alive after, recarving are tagged as necessary
     * 
     * @throws ABCException
     * @throws CarvingException
     */
    public List<CarvedExecution> recarve(MethodInvocation methodInvocation) throws CarvingException, ABCException {
        logger.info("-------------------------");
        logger.info("  RECARVING: " + methodInvocation);
        logger.info("-------------------------");
        Set<MethodInvocation> necessaryMethodInvocations = new HashSet<MethodInvocation>();

        // Step 1: Collect all the necessary invocations that have been tagged during
        // simplification. They shall remain
        Set<MethodInvocation> alreadyNecessaryMethodInvocations = this.executionFlowGraph.getOrderedMethodInvocations()
                .stream().filter(mi -> mi.isNecessary()).collect(Collectors.toSet());

        if (logger.isDebugEnabled()) {
            logger.debug("Found " + alreadyNecessaryMethodInvocations.size() + " ALREADY necessary method invocations");
            alreadyNecessaryMethodInvocations.forEach(mi -> logger.debug("- " + mi));
        }
        alreadyNecessaryMethodInvocations.forEach(necessaryMi -> {
            logger.debug("Find necessary method invocations for " + necessaryMi);
            necessaryMethodInvocations.addAll(findNecessaryMethodInvocations(necessaryMi).stream().map(t -> {
                // Create a clone and mark it as necessary
                MethodInvocation clonedMethodInvocation = t.clone();
                clonedMethodInvocation.setNecessary(true);
                return clonedMethodInvocation;
            }).collect(Collectors.toSet()));
        });

        // At this point, it is OK if the method invocation under test is not present,
        // as it might have been replaced
        // by a different call (e.g., Robolectric). TODO We should keep it stored
        // somewhere to avoid breaking the code later

        if (this.executionFlowGraph.contains(methodInvocation)) {
            // TODO Probably can be refactored
            logger.info("-------------------------");
            logger.info("  RECARVING: " + methodInvocation);
            logger.info("-------------------------");

            /*
             * Collect and sort the method invocations necessary for the carving. Note that
             * the findNecessaryMethodInvocations DOES NOT tag the method invocations as
             * necessary, as those will impact future carving activities. Instead, we clone
             * them and explicitly tag them as necessary.
             */
            // Make sure we do NOT use the global cache otherwise we'll bring back methods
            // that we just removed during simplification
            necessaryMethodInvocations.addAll(findNecessaryMethodInvocations(methodInvocation).stream().map(t -> {
                // Create a clone and mark it as necessary
                MethodInvocation clonedMethodInvocation = t.clone();
                clonedMethodInvocation.setNecessary(true);
                return clonedMethodInvocation;
            }).collect(Collectors.toSet()));
        } else {
            logger.warn("-------------------------");
            logger.warn("  MUT : " + methodInvocation + " not found !");
            logger.warn("-------------------------");
            // TODO Probably need to tag this somehow
        }

        // We use a set to avoid duplicates. The algorithms below can be improved a
        // lot...
        List<MethodInvocation> allMethodInvocations = new ArrayList<>(necessaryMethodInvocations);
        // SORT THEM
        Collections.sort(allMethodInvocations);
        //
        if (logger.isDebugEnabled()) {
            logger.debug("-------------------------");
            logger.debug("  NECESSARY INVOCATIONS FOR: " + methodInvocation);
            logger.debug("-------------------------");

            allMethodInvocations.stream().map(MethodInvocation::toString).forEach(logger::debug);
        }

        List<CarvedExecution> carvedExecutions = carveFrom(allMethodInvocations, necessaryMethodInvocations);

        // Ensure isNecessary tags are correct
        carvedExecutions.forEach(ce -> {
            ce.setNecessaryTagFor(alreadyNecessaryMethodInvocations);
        });

        carvedExecutions.forEach(ce -> {
            ce.methodInvocationUnderTest = methodInvocation;
            ce.isMethodInvocationUnderTestWrapped = !this.executionFlowGraph.contains(methodInvocation);

        });

        return carvedExecutions;

    }

    public static void enableGlobalCache() {
        logger.warn("Enable Global Cache");
        globalCachedEnabled = true;

    }

    public static void disableGlobalCache() {
        logger.warn("Disable Global Cache");
        globalCachedEnabled = false;

    }

}
