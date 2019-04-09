package de.unipassau.abc.carving.carvers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.DataNodeFactory;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.GraphNode;
import de.unipassau.abc.carving.MethodCarver;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.NullInstance;
import de.unipassau.abc.carving.NullNodeFactory;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.ObjectInstanceFactory;
import de.unipassau.abc.carving.PrimitiveNodeFactory;
import de.unipassau.abc.carving.ValueNode;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.carving.steps.CarvingNode;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import no.systek.dataflow.StepExecutor;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

/**
 * Challenge 1: choosing the right way to setup a preconditions
 * 
 * Challenge 2: how to deal/handle objects provided by the runtime framework
 * "external": carving or mocking, maybe tainting ?
 * 
 * @author gambi
 *
 */
public class AndroidMethodCarver implements MethodCarver {

    private final Logger logger = LoggerFactory.getLogger(AndroidMethodCarver.class);

    private ExecutionFlowGraph executionFlowGraph;
    // TODO How this is not used
    private DataDependencyGraph dataDependencyGraph;
    // This one we need to use for computing if
    private CallGraph callGraph;

    /**
     * 
     * @param executionFlowGraph
     * @param dataDependencyGraph
     * @param callGraph
     */
    public AndroidMethodCarver(ExecutionFlowGraph executionFlowGraph, //
            DataDependencyGraph dataDependencyGraph, //
            CallGraph callGraph) {
        super();
        this.executionFlowGraph = executionFlowGraph;
        this.dataDependencyGraph = dataDependencyGraph;
        this.callGraph = callGraph;

        initializeMockery();

    }

    private SootClass mockitoClass, ongoingStubbingClass;
    private SootMethod mockMethod, whenMethod, thenReturnManyResultMethod;

    private void initializeMockery() {
        mockitoClass = Scene.v().getSootClass(Mockito.class.getName());
        mockMethod = mockitoClass.getMethod("mock", Arrays.asList(new Type[] { RefType.v(Class.class.getName()) }));
        // Only one whenMethod method
        whenMethod = mockitoClass.getMethodByName("when");

        ongoingStubbingClass = Scene.v().getSootClass(OngoingStubbing.class.getName());

        thenReturnManyResultMethod = ongoingStubbingClass.getMethod("thenReturn", Arrays.asList(
                new Type[] { RefType.v(Object.class.getName()), RefType.v(Object.class.getName()).getArrayType() }));
    }

    /**
     * Carve tests out of all the MethodInvocations which match the carveBy
     * expression
     * 
     * @param carveBy
     * @return
     */
    public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(
            List<MethodInvocation> orderedMethodsInvocationsToCarve) {

        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

        /*
         * By ordering them we should be able to exploit the precondition cache
         * and incrementally carve later invocations from previous carved
         * invocations
         */
        Collections.sort(orderedMethodsInvocationsToCarve);

        for (MethodInvocation methodInvocationUnderTest : orderedMethodsInvocationsToCarve) {

            /*
             * Skip methods which has no sense to carve
             */
            if (methodInvocationUnderTest.isPrivate()) {
                logger.info("We do not carve private methods " + methodInvocationUnderTest);
                continue;
            }

            /*
             * Skip synthetic methods which does not exists in the original code
             */
            if (methodInvocationUnderTest.isSynthetic()) {
                logger.info("We do not carve synthetic methods " + methodInvocationUnderTest);
                continue;
            }

            List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestsPerMethodInvocation = new ArrayList<>();

            if (isTriviallyUncarvable(methodInvocationUnderTest)) {
                logger.info("\n\n====================================================\n" //
                        + " Skip carving of " + methodInvocationUnderTest + " as trivially uncarvable \n" //
                        + "====================================================");
                // Explain why this is trivially uncarvable
                logger.info("The following method subsumes " + methodInvocationUnderTest + ":"
                        + callGraph.getOrderedSubsumingMethodInvocationsFor(methodInvocationUnderTest).toString()
                                .replace("[", "\n\t").replace("]", "").replaceAll(",", "\n\t"));
                continue;

            }
            try {
                long carvingTime = System.currentTimeMillis();

                logger.info("\n\n====================================================\n" //
                        + "Starting the carve of " + methodInvocationUnderTest + "\n" //
                        + "====================================================");

                Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest = carveTheMethodInvocationRecursive(
                        methodInvocationUnderTest);

                carvedTestsPerMethodInvocation.add(carvedTest);

                carvingTime = System.currentTimeMillis() - carvingTime;
                logger.info("\n\n====================================================\n" //
                        + "Carved  " + carvedTestsPerMethodInvocation.size() + " in " + +carvingTime + " msec \n" //
                        + "====================================================");

                carvedTests.addAll(carvedTestsPerMethodInvocation);
            } catch (CarvingException e) {
                // e.printStackTrace();
                logger.warn("\n\n====================================================\n" //
                        + "Cannot carve test for " + methodInvocationUnderTest + " :: " + e.getMessage() + "\n"
                        + "====================================================");
            } catch (Throwable e) {
                // THIS IS UNEXPECTED
                e.printStackTrace();
                logger.warn("\n\n====================================================\n" //
                        + "Cannot carve test for " + methodInvocationUnderTest + " :: " + e.getMessage() + "\n"
                        + "====================================================");
            }
        }

        return carvedTests;
    }

    /**
     * A method invocation is trivially uncarvable if is subsumed by another
     * method from the same owner
     * 
     * @param methodInvocationUnderTest
     * @return
     */
    private boolean isTriviallyUncarvable(MethodInvocation methodInvocationUnderTest) {
        if (methodInvocationUnderTest.isStatic()) {
            return false;
        }

        // All the method invocations by the owner - and its aliases
        Set<MethodInvocation> methodInvocationsFromOwnerBefore = new HashSet<>(
                dataDependencyGraph.getMethodInvocationsForOwner(methodInvocationUnderTest.getOwner()));
        for (ObjectInstance alias : dataDependencyGraph.getAliasesOf(methodInvocationUnderTest.getOwner())) {
            methodInvocationsFromOwnerBefore.addAll(dataDependencyGraph.getMethodInvocationsForOwner(alias));
        }

        // Take only the ones before methodInvocationUnderTest
        MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.after(methodInvocationUnderTest),
                methodInvocationsFromOwnerBefore);
        // Remove the methodInvocation itself
        methodInvocationsFromOwnerBefore.remove(methodInvocationUnderTest);

        // Compute the method invocations subsumed-by the one before
        Set<MethodInvocation> subsumedByOwnerBefore = new HashSet<>();
        for (MethodInvocation methodInvocation : methodInvocationsFromOwnerBefore) {
            subsumedByOwnerBefore.addAll(callGraph.getMethodInvocationsSubsumedBy(methodInvocation));
            subsumedByOwnerBefore.add(methodInvocation);
        }

        // Check if methodInvocationUnderTest belongs to that set
        return subsumedByOwnerBefore.contains(methodInvocationUnderTest);
    }

    private ExecutorService executorService;
    protected StepExecutor stepExecutor;

    /**
     * With this method I tried to use a data-flow driven execution style to
     * parallelize speed up the computation but turns out that that framework
     * cannot handle large graphs. So I reverted back to the recursiveCall
     * one...
     * 
     * @param methodInvocationToCarve
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> carveTheMethodInvocation(
            MethodInvocation methodInvocationToCarve) throws CarvingException {
        /*
         * Build a graph of carving tasks first, and let it run later. Keep a
         * reference to the various tasks to avoid repeating them.
         * 
         * Eventually there will be a CarvingNode for each GraphNode
         * 
         * The Pair identifies the carving target in its context (for objects)
         */
        Map<Pair<GraphNode, MethodInvocation>, CarvingNode> workList = new HashMap<>();

        // Create the CarvingNode and then set its dependencies
        CarvingNode carvingNode = new CarvingNode(methodInvocationToCarve);
        // Register the node in the worklist - context is null since a method
        // invocation is unique
        workList.put(new Pair<GraphNode, MethodInvocation>(methodInvocationToCarve, null), carvingNode);
        logger.debug("Added CarvingNode for " + methodInvocationToCarve);
        // Discover the CarvingNode dependencies
        includeMethodDependencies(methodInvocationToCarve, carvingNode, workList);

        executorService = Executors.newFixedThreadPool(50);

        // TODO I have no idea what's doing here ....
        stepExecutor = new StepExecutor(executorService, s -> {
        }, () -> null, 50, 30, TimeUnit.SECONDS);

        // Start the execution with only empty data structures for input nodes
        // if any
        Pair<ExecutionFlowGraph, DataDependencyGraph> empty = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                new ExecutionFlowGraph(), new DataDependencyGraph());

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest = carvingNode.executeWith(stepExecutor);

        executorService.shutdown();
        executorService = null;
        stepExecutor = null;

        logger.debug("Carved test: \n" + prettyPrint(carvedTest.getFirst()));

        // Do POST PROCESSING HERE

        return carvedTest;

    }

    /*
     * This method finds the necessary dependencies of the method and attach
     * them to the corresponding CarvingNode, hence the carving node MUST be
     * there already.
     * 
     * Adopt a breath-first stile to easy understanding
     */
    private void includeMethodDependencies(MethodInvocation methodInvocationToCarve, CarvingNode dependentNode,
            Map<Pair<GraphNode, MethodInvocation>, CarvingNode> workList) throws CarvingException {

        logger.debug("Computing Dependencie for " + methodInvocationToCarve);

        int dataDependenciesCount = methodInvocationToCarve.getActualParameterInstances().size()
                + ((!methodInvocationToCarve.isStatic() || !methodInvocationToCarve.isConstructor()) ? 1 : 0);

        /*
         * If the method is an instance method but not a constructor attach its
         * owner as dependency
         */
        List<ObjectInstance> toExplore = new ArrayList<>();

        if (!methodInvocationToCarve.isStatic() && !methodInvocationToCarve.isConstructor()) {

            Pair<GraphNode, MethodInvocation> ownerTask = new Pair<GraphNode, MethodInvocation>(
                    methodInvocationToCarve.getOwner(), methodInvocationToCarve);

            // Check if this owner in the same context was already used
            // otherwise create a new one
            CarvingNode ownerDependency = null;
            if (!workList.containsKey(ownerTask)) {
                ownerDependency = new CarvingNode(methodInvocationToCarve.getOwner(), methodInvocationToCarve);
                workList.put(ownerTask, ownerDependency);
                logger.debug("Added CarvingNode for method owner " + methodInvocationToCarve.getOwner());

                // Since the owner is an Object we will need to look for its
                // dependencies as well
                // TODO Unless we already did it ?!
                toExplore.add(methodInvocationToCarve.getOwner());
            } else {
                ownerDependency = workList.get(ownerTask);
            }

            logger.debug("\t Declare dependency between " + methodInvocationToCarve + " and its owner "
                    + methodInvocationToCarve.getOwner());
            //
            dependentNode.acceptAsOwner(ownerDependency);
        }

        /*
         * Include dependencies on parameters. Keep the position into account.
         */
        for (int position = 0; position < methodInvocationToCarve.getActualParameterInstances().size(); position++) {

            DataNode actualParameterToCarve = methodInvocationToCarve.getActualParameterInstances().get(position);

            Pair<GraphNode, MethodInvocation> parameterTask = new Pair<GraphNode, MethodInvocation>(
                    actualParameterToCarve, methodInvocationToCarve);

            CarvingNode parameterDependency = null;
            if (workList.containsKey(parameterTask)) {
                parameterDependency = workList.get(parameterTask);
            } else {
                parameterDependency = new CarvingNode(actualParameterToCarve, methodInvocationToCarve);
                workList.put(parameterTask, parameterDependency);
                logger.debug("Added CarvingNode for parameter " + actualParameterToCarve + " in position " + position);

                if ((actualParameterToCarve instanceof ObjectInstance)
                        && !(actualParameterToCarve instanceof NullInstance)) {
                    // Since the owner is an non null Object we will need to
                    // look for its dependencies as well
                    // TODO Unless we already did it ?!
                    toExplore.add((ObjectInstance) actualParameterToCarve);
                }
            }

            logger.debug("\t Declare dependency between " + methodInvocationToCarve + " and its parameter "
                    + actualParameterToCarve + " in position " + position);
            //
            dependentNode.acceptAsParameterInPosition(parameterDependency, position);
        }

        if (toExplore.isEmpty()) {
            logger.debug("There's nothing left to explore for " + methodInvocationToCarve);
        } else {
            // Trigger dependency collection for the carving nodes
            for (ObjectInstance objectInstance : toExplore) {
                Pair<GraphNode, MethodInvocation> objectTask = new Pair<GraphNode, MethodInvocation>(objectInstance,
                        methodInvocationToCarve);
                // This cannot be null...
                CarvingNode objectCarvingNode = workList.get(objectTask);
                //
                includeObjectInstanceDependency(objectInstance, methodInvocationToCarve, objectCarvingNode, workList);
            }
        }
    }

    /**
     * Find the methods which "possibly" contribute to setting the state of this
     * object and carve them. Those are methods that we called
     * <strong>before</strong> the context:
     * <ul>
     * <li>on that object</li>
     * <li>on aliases of that object</li>
     * <li>In case the object or aliases are not "ours", all the methods that do
     * not belong to the app code, which we receive that object as
     * parameter</li>
     * </ul>
     * 
     * TODO: NOTE: Direct access to object fields (because they are public) will
     * not be considered in this analysis !!!
     */
    private void includeObjectInstanceDependency(ObjectInstance objectInstanceToCarve, MethodInvocation context, //
            CarvingNode dependentNode, //
            Map<Pair<GraphNode, MethodInvocation>, CarvingNode> workList) throws CarvingException {

        logger.debug("Collecting method dependencies for object " + objectInstanceToCarve + " in context " + context);

        /*
         * Collect all the method invocations on that object.
         */
        Collection<MethodInvocation> potentiallyStateSettingMethods = findPotentiallyInternalStateChangingMethodsFor(
                objectInstanceToCarve, context);

        /*
         * Include the methods on that alias -> Honestly I have no idea how is
         * this possible... maybe the system id is tricky ?!
         */
        for (ObjectInstance alias : this.dataDependencyGraph.getAliasesOf(objectInstanceToCarve)) {
            potentiallyStateSettingMethods.addAll(findPotentiallyInternalStateChangingMethodsFor(alias, context));
        }

        /*
         * Consider only the state changing methods BEFORE the context. We need
         * to redo this operation sinde aliases might have introduced additional
         * method calls AFTER context
         */

        MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.after(context), potentiallyStateSettingMethods);

        List<MethodInvocation> toExplore = new ArrayList<>();

        for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {
            Pair<GraphNode, MethodInvocation> methodToCarveTask = new Pair<GraphNode, MethodInvocation>(
                    methodInvocationToCarve, null);

            CarvingNode methodDependency = null;
            if (workList.containsKey(methodToCarveTask)) {
                methodDependency = workList.get(methodToCarveTask);
            } else {
                methodDependency = new CarvingNode(methodInvocationToCarve);
                workList.put(methodToCarveTask, methodDependency);
                //
                logger.debug("Added CarvingNode for " + methodInvocationToCarve);
                // Since this is a method invocation we might need to further
                // explore it
                toExplore.add(methodInvocationToCarve);
            }

            logger.debug("\t Declare dependency between " + objectInstanceToCarve + " and " + methodInvocationToCarve);
            dependentNode.acceptAsStateChangingMethod(methodDependency);
        }

        if (toExplore.isEmpty()) {
            logger.debug("There's nothing left to explore for " + objectInstanceToCarve + " in the context " + context);
        } else {
            // Trigger dependency collection for the state changing nodes
            for (MethodInvocation methodInvocation : potentiallyStateSettingMethods) {
                Pair<GraphNode, MethodInvocation> methodInvocationTask = new Pair<GraphNode, MethodInvocation>(
                        methodInvocation, null);
                CarvingNode methodInvocationDependency = workList.get(methodInvocationTask);

                includeMethodDependencies(methodInvocation, //
                        methodInvocationDependency, //
                        workList);
            }
        }
    }

    /**
     * Carving is a recursive activity -> carving methods and carving "data".
     * This is the base call.
     * 
     * In theory carving might produce several different setup, we force this to
     * return one of them.
     * 
     * TODO If the cache is reinitialized everytime that might not be smart ....
     * Also consider to store the cache somewhere on the disk !!
     * 
     * @param methodInvocationToCarve
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> carveTheMethodInvocationRecursive(
            MethodInvocation methodInvocationToCarve) throws CarvingException {

        /**
         * Those cache objects contains the fragments of the original graphs
         * which are necessary to set the preconditions of the methods and
         * objectinstances.
         */
        Map<Pair<MethodInvocation, MethodInvocation>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedMethods = new HashMap<>();
        Map<Pair<MethodInvocation, ObjectInstance>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedObjects = new HashMap<>();
        Set<Object> workList = new HashSet<>();

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest = carveMethod(methodInvocationToCarve, workList,
                alreadyCarvedMethods, alreadyCarvedObjects);

        ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();
        DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();

        // This contains all the necessary invocations we now need to compress
        // this while keep the test valid.
        logger.debug("Carved Test: \n" + prettyPrint(executionFlowGraph));

        /*
         * Let's do some android specific carving.
         */

        // Identify all the "Root" Fragment Activity Lifecycle invocations
        // before. This might be too stict
        List<MethodInvocation> fragmentActivityMethodInvocations = new ArrayList();
        // Use the "global" executionFlowGraph to consider all the method
        // invocations
        // Not only the one directly related to the Activity
        fragmentActivityMethodInvocations
                .addAll(this.executionFlowGraph.getOrderedMethodInvocationsBefore(methodInvocationToCarve));

        /*
         * Removes matching elements
         */
        MethodInvocationMatcher
                .filterByInPlace(
                        MethodInvocationMatcher
                                .not(MethodInvocationMatcher.or(MethodInvocationMatcher.isActivityLifeCycle(),
                                        MethodInvocationMatcher.isFragmentLifeCycle())),
                        fragmentActivityMethodInvocations);

        /*
         * Identify and remove all the subsumed actions of the Activity or
         * Fragments in the carved tests. In particular the one for the event
         * lifecycle since those MUST have happened in the context of the
         * life-cycle of an activity in any case.
         */
        // ExecutionFlowGraph carvedExecutionFlowGraph = carvedTest.getFirst();

        List<MethodInvocation> requiredMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();

        logger.debug("Method calls before removal of fragment lifecycle " + requiredMethodInvocations.size());
        for (Iterator<MethodInvocation> iterator = requiredMethodInvocations.iterator(); iterator.hasNext();) {
            MethodInvocation methodInvocation = iterator.next();
            // TODO Improve
            for (MethodInvocation fragmentLifecycleMethodInvocation : fragmentActivityMethodInvocations) {
                if (callGraph.getMethodInvocationsSubsumedBy(fragmentLifecycleMethodInvocation)
                        .contains(methodInvocation)) {
                    logger.debug(" methodInvocation " + methodInvocation
                            + " is subsumed by Activity/Fragment lifecycle " + fragmentLifecycleMethodInvocation);
                    iterator.remove();
                    // Avoid removing the same method multiple times
                    break;
                }
            }
        }

        /*
         * Keep only the invocations that can be found in the provided set
         */
        executionFlowGraph.refine(new HashSet<>(requiredMethodInvocations));
        dataDependencyGraph.summarize(executionFlowGraph);

        logger.debug("Method calls after removal of fragment lifecycle " + requiredMethodInvocations.size() + " == "
                + executionFlowGraph.getOrderedMethodInvocations().size());

        /*
         * Check if the MUT was already subsumed by a lifecycle event
         */
        if (!executionFlowGraph.contains(methodInvocationToCarve)) {
            logger.warn("Method under test was removed by a lifecycle event");
            // Explain.
            logger.info(
                    "Subsuming path: " + callGraph.getOrderedSubsumingMethodInvocationsFor(methodInvocationToCarve));
            throw new CarvingException("Method under test subsumed by lifecycle event !");
        }

        /*
         * At this point, i.e., after having considered the lifecycle of
         * activity and fragments, we need to remove all the remaining calls to
         * synthetic methods Since those methods do not really exist (ABC
         * dynamically generated them), hence they trivially cannot be invoked.
         */
        requiredMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();
        MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.isSynthetic(), requiredMethodInvocations);

        executionFlowGraph.refine(new HashSet<>(requiredMethodInvocations));
        dataDependencyGraph.summarize(executionFlowGraph);
        
        
        /*
         * Remove any duplicated method invocation
         * 
         * Summarize carved method invocations: If an invocation is subsumed by
         * another invocation, we can remove it.
         */
        logger.trace("Before Summarization: \n " + prettyPrint(executionFlowGraph));

        executionFlowGraph.summarize(callGraph);
        dataDependencyGraph.summarize(executionFlowGraph);

        logger.trace("After Summarization: \n " + prettyPrint(executionFlowGraph));

        /*
         * Validate test: After summarization the "method under test" must be
         * there !
         */

        if (!executionFlowGraph.contains(methodInvocationToCarve)) {
            // TODO In many cases uncarvable methos invocations can be spotted
            // directly by checking for subsumption from methods of the same
            // owner !
            logger.warn("Method under test was removed after summarization");
            throw new CarvingException("No more method under test !");
        }

        /*
         * ATM we cannot handle ICC/multi-activity tests
         */
        if (thereIsMoreThanOneActivity(dataDependencyGraph)) {
            // Is the activity returning something, so we mock it away ?
            // Is the other activity from the same app so we might (offline) reproduce its outputs?
            // Is the other activity THE same activity?
            
            
            logger.warn("Carved test contains more than one activity. We cannot handle this at the moment.");
            throw new CarvingException("More than one activity !");
        }

        /*
         * The test cannot directly call non-visible methods or class
         * definitions. We assume that the carved test is in the same package of
         * the CUT
         */
        String packageName = JimpleUtils.getPackage(methodInvocationToCarve.getMethodSignature());

        MethodInvocationMatcher nonVisibleMethodInvocationMatcher = MethodInvocationMatcher.or(
                MethodInvocationMatcher.visibilityPrivate(),
                MethodInvocationMatcher.not(MethodInvocationMatcher.isVisibleFromPackage(packageName)));

        // Return the elements matching the filter
        Set<MethodInvocation> nonVisibleMethodInvocations = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
                nonVisibleMethodInvocationMatcher, executionFlowGraph.getOrderedMethodInvocations());

        if (!nonVisibleMethodInvocations.isEmpty()) {
            logger.warn("Carved test contains direct calls to non-visible method invocations: "
                    + nonVisibleMethodInvocations.toString().replaceAll(",", "\n"));
            throw new CarvingException("Carved tests calls non-visible methods directly !");
        }

        // We cannot instantiate explicitly Lambda functions. Maybe we can get a
        // reference to the class defining them, but invoking them I think it is
        // possible only via
        // their declaring class/instance ..
        // https://stackoverflow.com/questions/34589435/get-the-enclosing-class-of-a-java-lambda-expression
        // target class name + "$$Lambda$" + a counter.
        MethodInvocationMatcher lambdaMethodInvocationMatcher = MethodInvocationMatcher.lambda();
        Set<MethodInvocation> lambdaMethodInvocations = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
                lambdaMethodInvocationMatcher, executionFlowGraph.getOrderedMethodInvocations());
        if (!lambdaMethodInvocations.isEmpty()) {
            logger.warn("Carved test contains lamdba method invocations: "
                    + lambdaMethodInvocations.toString().replaceAll(",", "\n"));
            throw new CarvingException("Carved test calls lamdba functions directly !");
        }

        /*
         * Validate that in the test all object instances do not appear out of
         * the blue unless they are null. For not-null object instances either
         * there's a call to a constructor or some calls which returns them.
         */
        if (!dataDependencyGraph.verifyObjectInstanceProvenance()) {
            List<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContext = new ArrayList<>();
            /*
             * We need to remove the objects which do not explicitly appear in
             * the executions trace, in the hope of getting rid of the
             * aliases... Otherwise we need to choose the class for the aliases
             * (i.e., the one deepest in the hierarchy?)
             */
            for (Iterator<ObjectInstance> objectInstanceIterator = dataDependencyGraph.getDanglingObjects()
                    .iterator(); objectInstanceIterator.hasNext();) {
                // Why I cannot put this inside the for statement?
                ObjectInstance objectInstance = objectInstanceIterator.next();
                // Check if this objectInstance is ever used by any method in
                // the carved test
                boolean used = false;
                for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
                    if (objectInstance.equals(methodInvocation.getOwner())
                            || methodInvocation.getActualParameterInstances().contains(objectInstance)) {
                        used = true;
                        break;
                    }
                }
                if (!used) {
                    objectInstanceIterator.remove();
                } else {
                    // The context for the dangling objects is the method to
                    // carve
                    danglingObjectWithContext
                            .add(new Pair<ObjectInstance, MethodInvocation>(objectInstance, methodInvocationToCarve));
                }
            }

            if (danglingObjectWithContext.size() > 0) {

                logger.warn("The following objects appear out of the blue: " + danglingObjectWithContext
                        + " try to mockMethod them");

                /*
                 * At this point, instead of giving up we try to recover those
                 * objects and create mocks (BoxedPrimitives have a value
                 * attached to them, so we do not technically create a
                 * mockMethod for them).
                 * 
                 * Note the tricky point is to get the expected return values
                 * from mockMethod. Are those returned values being mocked by
                 * default or shall we carve them? And why? - Mocking:
                 * conceptually those are in the right place, but we need
                 * complete visibility - Carving: we might pass real objects
                 * around so we do not care about complete visibility
                 * 
                 * TODO For the moment we MOCK only.
                 * 
                 * If for whatever reason we cannot mockMethod an object we
                 * fail...
                 */

                /*
                 * We passing the iterator around to include the additional
                 * objects to mockMethod with their "possibly new context"
                 * dynamically. The context might also help us to insert the
                 * mocks creation and configuration at the right place... i.e.,
                 * before their use!
                 */
                for (Iterator<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContextIterator = danglingObjectWithContext
                        .iterator(); danglingObjectWithContextIterator.hasNext();) {
                    Pair<ObjectInstance, MethodInvocation> objectInstanceToMockWithContext = danglingObjectWithContextIterator
                            .next();
                    /*
                     * Introduce calls to Mockito which returns the expected
                     * DataNode. We use the method under test as context for the
                     * analysis
                     */
                    ObjectInstance objectInstanceToMock = objectInstanceToMockWithContext.getFirst();
                    MethodInvocation mockingContext = objectInstanceToMockWithContext.getSecond();
                    //
                    Pair<ExecutionFlowGraph, DataDependencyGraph> stub = stubCallsFor(objectInstanceToMock,
                            mockingContext, danglingObjectWithContextIterator);

                    executionFlowGraph.include(stub.getFirst());
                    dataDependencyGraph.include(stub.getSecond());

                }

                logger.trace("After Recovery: \n " + prettyPrint(executionFlowGraph));

                /*
                 * Remove all the objects which are not used
                 */
                dataDependencyGraph.purge();

                /*
                 * Double check that we did not introduce dangling objects
                 */
                if (!dataDependencyGraph.verifyObjectInstanceProvenance()) {
                    Set<ObjectInstance> danglingObjects = dataDependencyGraph.getDanglingObjects();
                    /*
                     * Remove objects not used in the carved test
                     */
                    for (Iterator<ObjectInstance> objectInstanceIterator = danglingObjects
                            .iterator(); objectInstanceIterator.hasNext();) {
                        // Why I cannot put this inside the for statement?
                        ObjectInstance objectInstance = objectInstanceIterator.next();
                        /*
                         * Check if this objectInstance is ever used by any
                         * method in the carved test
                         */
                        boolean used = false;
                        for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
                            if (objectInstance.equals(methodInvocation.getOwner())
                                    || methodInvocation.getActualParameterInstances().contains(objectInstance)) {
                                used = true;
                                break;
                            }
                        }
                        if (!used) {
                            objectInstanceIterator.remove();
                        }
                    }

                    if (danglingObjects.size() > 0) {
                        logger.warn("The following objects appear out of the blue: " + danglingObjects);
                        throw new CarvingException("Objects appear out of the blue");
                    }
                }
            }
        }
        /*
         * At this point we might need to do some more post processing depending
         * on the target of carving:
         */

        return carvedTest;
    }

    /*
     * Check if there's more than one activity type, not instances. This enable to test activity restart cases.
     */
    private boolean thereIsMoreThanOneActivity(DataDependencyGraph dataDependencyGraph) {
        Set<String> androidActivities = new HashSet<>();
        for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
            if (objectInstance.isAndroidActivity()) {
                androidActivities.add(objectInstance.getType());
            }
        }
        if (androidActivities.size() != 1) {
            logger.info("Found the following activities in the carved test: " + androidActivities);
        }
        return androidActivities.size() > 1;
    }

    /**
     * Ensures that the id of the method invocation is negative so the mockery
     * executes before carved test code, or refactor carved test to include
     * separate code sections !
     * 
     * Rules for mockery:
     * <ul>
     * <li>primitive boxed: directly instantiated them, i.e., no mocks</li>
     * <li>complex objects:
     * <ul>
     * <li>If object belongs to user app, hence we can observe all its method
     * calls, no problem. Collect usages in the context, i.e., after the context
     * method returns we do not care.</li>
     * <li>If the object does not belong to user app, but it is not passed as
     * parameter to lib methods in the context, thenReturnManyResultMethod we
     * can observe all its activity and we can mockMethod it</li>
     * <li>If the object does not belong to user app AND is also passed around
     * to lib methods, thenReturnManyResultMethod we cannot observe what happens
     * to it and we cannot ensure this is safe. We raise an exception.</li>
     * </ul>
     * </li>
     * </ul>
     * 
     * @param objectInstanceToMock
     * @param context:
     *            this is the method under test
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> stubCallsFor(ObjectInstance objectInstanceToMock,
            MethodInvocation context, //
            Iterator<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContextIterator)
            throws CarvingException {

        logger.debug("stubCallsFor() for " + objectInstanceToMock + " in the context of " + context);
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        Pair<ExecutionFlowGraph, DataDependencyGraph> mockery = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                executionFlowGraph, dataDependencyGraph);

        if (objectInstanceToMock.isBoxedPrimitive()) {
            instantiateBoxedPrimitive(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);
        } else if (JimpleUtils.isArray(objectInstanceToMock.getType())) {
            if (!isTheObjectUsedInContext(objectInstanceToMock, context)) {
                // Instantiate an empty array
                createArrayDouble(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);
            } else {
                logger.warn("We cannot mock Arrays");
                throw new CarvingException("We cannot mock arrays " + objectInstanceToMock);
            }
        } else {
            if (!isTheObjectUsedInContext(objectInstanceToMock, context)) {
                /*
                 * If the object is passed as argument to method under test, but
                 * never user thenReturnManyResultMethod it is enough to pass an
                 * empty mockMethod for it. This should not happen, but
                 * "we never know"
                 */
                createMockeryFor(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);

            } else if (belongsToUserApp(objectInstanceToMock)
                    || isCompletelyVisibleInContext(objectInstanceToMock, context)) {
                /*
                 * Instantiate the mockery for objectInstanceToMock
                 */
                createMockeryFor(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);
                /*
                 * Collect the invocations to the object in the context of the
                 * method under test and configure the mockMethod to replicate
                 * them... This might require to mockMethod additional objects,
                 * the information should be in the carved test already "before"
                 * the summarization. Since this might require the mocking (in
                 * the future carving) of additional objects as return type, we
                 * include the iterator
                 */
                configureMockeryFor(objectInstanceToMock, context, executionFlowGraph, dataDependencyGraph,
                        danglingObjectWithContextIterator);

            } else {
                /*
                 * We cannot guarantee this is safe, so for the momemnt we raise
                 * an exception. Later we can try to do a best effort as see if
                 * more valid tests can be generated
                 */

                // https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric
                // https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric#using-shadows
                // http://robolectric.org/extending/
                throw new CarvingException("Cannot ensure mockery on " + objectInstanceToMock + " in the context "
                        + context + " is safe.");
            }
        }
        return mockery;
    }

    // TODO Find a way to define (negative) indices for mocks !
    AtomicInteger arrayInitId = new AtomicInteger(-1);

    private void createArrayDouble(ObjectInstance arrayToInstantiate, //
            ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph) {
        /*
         * Create a special node here to host the value of ".class"
         */
        DataNode arraySize = DataNodeFactory.get("int", "0");

        // TODO: We cannot use a constant id for method calls as those are
        // supposed to be unique.
        // One "naive solution" is to start from a very big negative number and
        // increment it, but do not use MIN_INTEGER !
        MethodInvocation arrayInit = new MethodInvocation(arrayInitId.getAndDecrement(),
                "<" + arrayToInstantiate.getType() + ": void <init>(int)>");
        arrayInit.setOwner(arrayToInstantiate);
        arrayInit.setActualParameterInstances(Arrays.asList(new DataNode[] { arraySize }));

        // Should definitively add something to enqueu stuff in front or at
        // position.
        // Somehin like a PatchingChain
        executionFlowGraph.enqueueMethodInvocations(arrayInit);
        //
        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(arrayInit);
        dataDependencyGraph.addDataDependencyOnOwner(arrayInit, arrayToInstantiate);
        dataDependencyGraph.addDataDependencyOnActualParameter(arrayInit, arraySize, 0);

    }

    private void configureMockeryFor(ObjectInstance objectInstanceToMock, MethodInvocation context, //
            ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph, //
            Iterator<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContextIterator) {

        // Collect all the calls in the context which have objectInstanceToMock
        // has owner
        // TODO include aliases
        Set<MethodInvocation> callsOnOnbjectInstanceToMockInContext = new HashSet<>();
        // Starts with all the calls in the context
        callsOnOnbjectInstanceToMockInContext.addAll(this.callGraph.getMethodInvocationsSubsumedBy(context));
        // Filter the call by owner
        MethodInvocationMatcher filterByOwner = MethodInvocationMatcher.byOwnerAndAliases(objectInstanceToMock,
                this.dataDependencyGraph.getAliasesOf(objectInstanceToMock));
        callsOnOnbjectInstanceToMockInContext = MethodInvocationMatcher.getMethodInvocationsMatchedBy(filterByOwner,
                callsOnOnbjectInstanceToMockInContext);

        // Create mock/spy
        // https://stackoverflow.com/questions/11620103/mockitoClass-trying-to-spy-on-method-is-calling-the-original-method
        // https://static.javadoc.io/org.mockito/mockitoClass-core/1.10.19/org/mockitoClass/Mockito.html

        /*
         * We sort the method invocation to ensure parameters are returned as
         * expected, however, if we use parameter matching this might not be
         * necessary...
         */
        List<MethodInvocation> sortedMethodCallsOnOnbjectInstanceToMockInContext = new ArrayList<>(
                callsOnOnbjectInstanceToMockInContext);
        Collections.sort(sortedMethodCallsOnOnbjectInstanceToMockInContext);

        /*
         * At this point we assume instead of objectInstanceToMock we can use
         * its mockMethod
         * 
         * We need to setup a "whenMethod" expectation for each different
         * method, and a then expectation for each method call. There's no need
         * to put the in place (thenReturnManyResultMethod(many values)).
         * 
         * For the whenMethod we can start by using Mockito.anyXXX so we do not
         * fall in the trap of having different calls to whenMethod for the same
         * method and different combinations of parameter matchers (those are
         * DECLARATIVE and override one another, it's tricky !)
         * 
         * The difficult part is to provide the correct ReturnValue. Return
         * values must be carved or mocked as well, if we cannot carve them,
         * then we need to ensure that they are COMPLETELY visible.
         * 
         * 
         */
        int index = -2 * sortedMethodCallsOnOnbjectInstanceToMockInContext.size();
        //
        for (MethodInvocation methodInvocationToMock : sortedMethodCallsOnOnbjectInstanceToMockInContext) {

            logger.info("Mocking " + methodInvocationToMock);

            if (JimpleUtils.hasVoidReturnType(methodInvocationToMock.getMethodSignature())) {
                /*
                 * TODO We skip this for the moment, it might be useful for
                 * validation, tho
                 */
                logger.info("TODO We do not mockMethod void calls");
                continue;
            }

            // Ensures that we do not create more than one call to this
            if (this.dataDependencyGraph.getAliasesOf(methodInvocationToMock.getOwner())
                    .contains(objectInstanceToMock)) {
                methodInvocationToMock.setOwner(objectInstanceToMock);
            }

            ObjectInstance ongoingStubbing = generateOrGetWhenCallForMock(methodInvocationToMock, index,
                    executionFlowGraph, dataDependencyGraph);

            // Generate the "Then" part of the mockMethod
            // thenReturnManyResultMethod(T value) Sets a return value to be
            // returned
            // whenMethod
            // the method is called.
            // > Maybe later: thenReturnManyResultMethod(T value, T... values)
            // Sets
            // consecutive return values to be returned whenMethod the method is
            // called.
            // > When we add exceptions: thenThrow(Class<? extends
            // Throwable> throwableType) Sets a Throwable type to be thrown
            // whenMethod the method is called.
            index++;

            // TODO Not sure how to handle this now, most likely we need to
            // recarve everything ?!!!
            // carveObjectInstance(objectInstanceToCarve, context, isOwner,
            // workList, alreadyCarvedMethods, alreadyCarvedObjects)
            // TODO For the moment we carve primitive types which is easy
            DataNode mockedReturnValue = NullNodeFactory
                    .get(JimpleUtils.getReturnType(methodInvocationToMock.getMethodSignature()));

            if (methodInvocationToMock.getReturnValue() instanceof ValueNode) {
                mockedReturnValue = methodInvocationToMock.getReturnValue();
            } else if (JimpleUtils.isBoxedPrimitive(methodInvocationToMock.getMethodSignature())) {
                mockedReturnValue = methodInvocationToMock.getReturnValue();
            }

            MethodInvocation thenMethodInvocation = new MethodInvocation(index,
                    thenReturnManyResultMethod.getSignature());
            thenMethodInvocation.setOwner(ongoingStubbing);
            thenMethodInvocation.setActualParameterInstances(Arrays.asList(new DataNode[] { mockedReturnValue }));
            // This method returns its owner, it has fluent interface
            thenMethodInvocation.setReturnValue(ongoingStubbing);

            executionFlowGraph.enqueueMethodInvocations(thenMethodInvocation);

            dataDependencyGraph.addMethodInvocationWithoutAnyDependency(thenMethodInvocation);
            //
            dataDependencyGraph.addDataDependencyOnOwner(thenMethodInvocation, ongoingStubbing);
            dataDependencyGraph.addDataDependencyOnActualParameter(thenMethodInvocation, mockedReturnValue, 0);
            dataDependencyGraph.addDataDependencyOnReturn(thenMethodInvocation, ongoingStubbing);

        }
    }

    // Cache
    // Mock + MethodSignature -> OngoingStubbing
    Map<Pair<ObjectInstance, String>, ObjectInstance> alreadyMockedCallsForMock = new HashMap();

    /**
     * Return the right OngoingStubbing object. I.e. do not create more than one
     * WHEN call per method. Generate the "When" part of the mockMethod. Ensure
     * that no aliases are used at this point for the mocked object.
     * 
     * To implement whenMethod we use the "eq()" argument matcher for the
     * instances we can refer to otherwise we use the isAnyXXXX() argument
     * matcher.
     * 
     * If we use isAny() we can chain together multiple
     * "thenReturnManyResultMethod()" stubs and we might simplify the thing.
     * Otherwise we can simply create many whenMethod-then calls.
     * 
     * Actually we might follow the suggestion by:
     * https://stackoverflow.com/questions/31512245/calling-mockitoClass-whenMethod
     * -multiple-times-on-same-object?rq=1 and use the doReturn( returnValue
     * ).when(mockMethod).mockedMethod( arguments );
     * 
     * 
     * 
     * @param methodInvocationToMock
     * @param executionFlowGraph
     * @param dataDependencyGraph
     * @return
     */
    private ObjectInstance generateOrGetWhenCallForMock(MethodInvocation methodInvocationToMock, int index,
            ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph) {

        /*
         * Check in the cache if we already declared a whenMethod method for
         * these method (not method invocation) and mockMethod
         */
        String methodSubSignature = JimpleUtils.getSubSignature(methodInvocationToMock.getMethodSignature());
        ObjectInstance mockObject = methodInvocationToMock.getOwner();
        //
        Pair<ObjectInstance, String> mockingTask = new Pair<ObjectInstance, String>(mockObject, methodSubSignature);

        if (alreadyMockedCallsForMock.containsKey(mockingTask)) {
            logger.debug("We already declare a WHEN mocking call for : " + mockingTask);
            return alreadyMockedCallsForMock.get(mockingTask);
        }

        DataNode methodCall = PrimitiveNodeFactory.createMethodCallLiteralValue(methodInvocationToMock);
        // org.mockito.stubbing.OngoingStubbing - since this is
        // generated... I have no idea how to assign numbers/id to it...
        // t
        // I hope
        // NOTE HERE we do not use DataNodeFactory
        ObjectInstance ongoingStubbing = ObjectInstanceFactory
                .get(OngoingStubbing.class.getName() + "@" + generatedId.incrementAndGet());

        MethodInvocation whenMethodInvocation = new MethodInvocation(index, whenMethod.getSignature());
        whenMethodInvocation.setStatic(true);
        whenMethodInvocation.setActualParameterInstances(Arrays.asList(new DataNode[] { methodCall }));
        whenMethodInvocation.setReturnValue(ongoingStubbing);

        executionFlowGraph.enqueueMethodInvocations(whenMethodInvocation);

        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMethodInvocation);
        //
        dataDependencyGraph.addDataDependencyOnActualParameter(whenMethodInvocation, methodCall, 0);
        dataDependencyGraph.addDataDependencyOnReturn(whenMethodInvocation, ongoingStubbing);

        alreadyMockedCallsForMock.put(mockingTask, ongoingStubbing);

        return ongoingStubbing;
    }

    /*
     * Boxed Primitives We generate a call which initialize this object instance
     * using the provided string value.
     */
    private void instantiateBoxedPrimitive(ObjectInstance objectInstanceToMock, ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph) {
        SootClass sootClass = Scene.v().getSootClass(objectInstanceToMock.getType());
        SootMethod valueOfFromString = sootClass.getMethod("valueOf",
                Arrays.asList(new Type[] { RefType.v(String.class.getName()) }));

        MethodInvocation boxedPrimitiveInstantiation = new MethodInvocation(-100, valueOfFromString.getSignature());
        boxedPrimitiveInstantiation.setStatic(true);

        // Strings are managed by value !
        // TODO I hope this is never null ...
        DataNode stringValue = DataNodeFactory.get(String.class.getName(),
                Arrays.toString(objectInstanceToMock.getStringValue().getBytes()));

        boxedPrimitiveInstantiation.setActualParameterInstances(Arrays.asList(new DataNode[] { stringValue }));
        boxedPrimitiveInstantiation.setReturnValue(objectInstanceToMock);
        // Add this to the execution flow graph
        executionFlowGraph.enqueueMethodInvocations(boxedPrimitiveInstantiation);
        //
        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(boxedPrimitiveInstantiation);
        dataDependencyGraph.addDataDependencyOnActualParameter(boxedPrimitiveInstantiation, stringValue, 0);
        dataDependencyGraph.addDataDependencyOnReturn(boxedPrimitiveInstantiation, objectInstanceToMock);

    }

    private static final AtomicInteger generatedId = new AtomicInteger(1);

    private void createMockeryFor(ObjectInstance objectInstanceToMock, ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph) {

        /*
         * Create a special node here to host the value of ".class"
         */
        DataNode classToMock = PrimitiveNodeFactory.createPrimitiveClassNode(objectInstanceToMock.getType());

        /*
         * The mockery gets the class of the instance to mockMethod, and
         * generate the expected instance. Ensures this is called before
         * anything else !
         */
        MethodInvocation mockeryCreation = new MethodInvocation(-100, mockMethod.getSignature());
        mockeryCreation.setStatic(true);

        mockeryCreation.setActualParameterInstances(Arrays.asList(new DataNode[] { classToMock }));
        mockeryCreation.setReturnValue(objectInstanceToMock);

        //
        executionFlowGraph.enqueueMethodInvocations(mockeryCreation);

        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(mockeryCreation);
        dataDependencyGraph.addDataDependencyOnActualParameter(mockeryCreation, classToMock, 0);
        dataDependencyGraph.addDataDependencyOnReturn(mockeryCreation, objectInstanceToMock);

    }

    private boolean isCompletelyVisibleInContext(ObjectInstance objectInstanceToMock, MethodInvocation context) {
        // Those are all the *visible* method calls made inside context
        // Include the aliases in the check
        Set<ObjectInstance> objectInstanceToMockAndItsAliases = dataDependencyGraph.getAliasesOf(objectInstanceToMock);
        objectInstanceToMockAndItsAliases.add(objectInstanceToMock);

        for (MethodInvocation visibleMethodInvocation : callGraph.getMethodInvocationsSubsumedBy(context)) {
            // We need this because set operations change the set !
            Set<DataNode> copyOfObjectInstanceAndItsAliases = new HashSet<>(objectInstanceToMockAndItsAliases);
            // Is the object or any of the aliases used as parameter ? Do the
            // intersection and see what's left
            copyOfObjectInstanceAndItsAliases.retainAll(visibleMethodInvocation.getActualParameterInstances());
            if (!copyOfObjectInstanceAndItsAliases.isEmpty() && visibleMethodInvocation.isLibraryCall()) {
                /*
                 * if this object is used a parameter to call a lib (non
                 * visible) method, thenReturnManyResultMethod all its object
                 * usages might not be completely visible
                 */
                return false;
            }
        }
        return true;
    }

    private boolean isTheObjectUsedInContext(ObjectInstance objectInstanceToMock, MethodInvocation context) {
        // Those are all the *visible* method calls made inside context
        // We should consider also the aliases here...
        Set<ObjectInstance> objectInstanceToMockAndItsAliases = dataDependencyGraph.getAliasesOf(objectInstanceToMock);
        objectInstanceToMockAndItsAliases.add(objectInstanceToMock);

        for (MethodInvocation visibleMethodInvocation : callGraph.getMethodInvocationsSubsumedBy(context)) {
            if (objectInstanceToMockAndItsAliases.contains(visibleMethodInvocation.getOwner())) {
                return true;
            }
            // We need this because set operations change the set !
            Set<DataNode> copyOfObjectInstanceAndItsAliases = new HashSet<>(objectInstanceToMockAndItsAliases);
            // Is the object or any of the aliases used as parameter ? Do the
            // intersection and see what's left
            copyOfObjectInstanceAndItsAliases.retainAll(visibleMethodInvocation.getActualParameterInstances());
            if (!copyOfObjectInstanceAndItsAliases.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /*
     * An object belongs to user app if the method invocations on him are NOT
     * tagged as libCalls ! NOTE if there's no method invocations, there's no
     * need to mockMethod behavior, just proi
     */
    private boolean belongsToUserApp(ObjectInstance objectInstance) {
        // This looks everywhere... but get only one method to make the
        // decision.
        // TODO THIS IS AN HEURISTIC, WE SHOULD GO FOR ADDING A FLAS INSISDE
        // OBJECT INSTANCE INSTEAD, BUT THAT REQUIRES HAVING THOSE INFORMATION
        // DURING TRACING/PARSING/ETC...
        Set<MethodInvocation> methodInvocationsOnObjectInstanceAndOnItsAliases = new HashSet<>(
                dataDependencyGraph.getMethodInvocationsForOwner(objectInstance));
        for (ObjectInstance alias : dataDependencyGraph.getAliasesOf(objectInstance)) {
            methodInvocationsOnObjectInstanceAndOnItsAliases
                    .addAll(dataDependencyGraph.getMethodInvocationsForOwner(alias));
        }
        // If there's at least a call to a non libCall we return true...
        for (MethodInvocation methodInvocation : methodInvocationsOnObjectInstanceAndOnItsAliases) {
            if (!methodInvocation.isLibraryCall()) {
                return true;
            }
        }
        return false;
    }

    private String prettyPrint(ExecutionFlowGraph executionFlowGraph) {
        StringBuffer testContent = new StringBuffer();

        int position = 1;

        for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
            testContent.append("\t" + position + "\t" + mi.getInvocationCount() + " " + mi.getOwner() + "::"
                    + mi.getMethodSignature() + "(");
            for (DataNode actualParameterInstance : mi.getActualParameterInstances()) {
                testContent.append(prettyPrint(actualParameterInstance));
                if (mi.getActualParameterInstances()
                        .indexOf(actualParameterInstance) != mi.getActualParameterInstances().size() - 1) {
                    testContent.append(",");
                }
            }
            testContent.append(")" + "->" + prettyPrint(mi.getReturnValue()));
            testContent.append("\n");
            position++;
        }
        return testContent.toString();
    }

    private String prettyPrint(DataNode actualParameter) {
        if (actualParameter == null) {
            return "null";
        } else if (actualParameter instanceof NullInstance) {
            return "null";
        } else {
            // else if (actualParameter instanceof PrimitiveValue
            // && JimpleUtils.isStringContent(((PrimitiveValue)
            // actualParameter).getStringValue())) {
            // return actualParameter.toString();
            //
            //
            // String stringContent = ((PrimitiveValue)
            // actualParameter).getStringValue();
            // stringContent = stringContent.replace("[", "").replace("]", "");
            // if (stringContent.isEmpty()) {
            // return "";
            // } else {
            // String[] bytesAsString = stringContent.split(",");
            // byte[] bytes = new byte[bytesAsString.length];
            // for (int ii = 0; ii < bytes.length; ii++) {
            // bytes[ii] = new Byte(bytesAsString[ii].trim());
            // }
            // return '"' + new String(bytes) + '"';
            // }
            // } else {
            return actualParameter.toString();
        }
    }

    /**
     * Carving a method requires: Carving its owner at the state it was before
     * the invocation, and carving its paramters. If the methods does not have
     * formally an owner (static | constructor) we skip the first step.
     * 
     * @param methodInvocationToCarve
     * @param carvedTests
     * @param alreadyCarvedMethods
     * @param alreadyCarvedObjects
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> carveMethod(MethodInvocation methodInvocationToCarve,
            Set<Object> workList,
            Map<Pair<MethodInvocation, MethodInvocation>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedMethods,
            Map<Pair<MethodInvocation, ObjectInstance>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedObjects)
            throws CarvingException {

        logger.trace("> Carve Method " + methodInvocationToCarve.toString());

        if (alreadyCarvedMethods.containsKey(
                new Pair<MethodInvocation, MethodInvocation>(methodInvocationToCarve, methodInvocationToCarve))) {
            logger.trace("Returning Carving results for " + methodInvocationToCarve + " from cache");
            return alreadyCarvedMethods.get(
                    new Pair<MethodInvocation, MethodInvocation>(methodInvocationToCarve, methodInvocationToCarve));
        }

        /*
         * Write out method dependencies before elaborating them!
         */

        if (!(methodInvocationToCarve.isStatic() || methodInvocationToCarve.isConstructor())) {
            /*
             * Method ownership
             */
            logger.trace("\t Method " + methodInvocationToCarve.toString() + " depends on owner "
                    + methodInvocationToCarve.getOwner());
        }

        for (int position = 0; position < methodInvocationToCarve.getActualParameterInstances().size(); position++) {
            DataNode actualParameterToCarve = methodInvocationToCarve.getActualParameterInstances().get(position);
            /*
             * Method parameter
             */
            logger.trace("\t Method " + methodInvocationToCarve.toString() + " depends on parameter[" + position + "] "
                    + actualParameterToCarve);
        }

        // TODO I am not sure I can freely share nodes/references among those
        // graphs, somehow they return copies and iterators ...
        // The problems are the "dependenncy informations"
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                executionFlowGraph, dataDependencyGraph);
        /*
         * Add the plain methodInvocationToCarve.
         */
        executionFlowGraph.enqueueMethodInvocations(methodInvocationToCarve);
        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocationToCarve);
        /*
         * Include the return value of the carved call if any. TODO Somehow if
         * this is null everything breaks silently ...
         */
        if (!JimpleUtils.hasVoidReturnType(methodInvocationToCarve.getMethodSignature())) {
            dataDependencyGraph.addDataDependencyOnReturn(methodInvocationToCarve,
                    methodInvocationToCarve.getReturnValue());
        }

        if (!(methodInvocationToCarve.isStatic() || methodInvocationToCarve.isConstructor())) {

            // Check in the worklist if we have already scheduled this job,
            // otherwise skip it
            if (!workList.contains(new Pair<MethodInvocation, ObjectInstance>(methodInvocationToCarve,
                    methodInvocationToCarve.getOwner()))) {

                // Add to the work list and execute it
                workList.add(new Pair<MethodInvocation, ObjectInstance>(methodInvocationToCarve,
                        methodInvocationToCarve.getOwner()));

                Pair<ExecutionFlowGraph, DataDependencyGraph> partialCarvingResult = carveMethodOwner(
                        methodInvocationToCarve.getOwner(), methodInvocationToCarve, //
                        workList, //
                        alreadyCarvedMethods, alreadyCarvedObjects);

                if (!methodInvocationToCarve.getOwner()
                        .equals(partialCarvingResult.getSecond().getOwnerFor(methodInvocationToCarve))) {
                    throw new RuntimeException("Failed to register ownership for " + methodInvocationToCarve + " with "
                            + methodInvocationToCarve.getOwner());
                }

                /*
                 * Include the required actions to get to this object instance.
                 * This will restructure the graphs
                 */
                executionFlowGraph.include(partialCarvingResult.getFirst());
                dataDependencyGraph.include(partialCarvingResult.getSecond());

            }
        } else if (methodInvocationToCarve.isConstructor()) {
            /*
             * Include the dependencies between the methodInvocationToCarve and
             * its owner
             */
            dataDependencyGraph.addDataDependencyOnOwner(methodInvocationToCarve, methodInvocationToCarve.getOwner());
        }

        for (DataNode actualParameterToCarve : methodInvocationToCarve.getActualParameterInstances()) {
            // Check in the worklist if we have already scheduled this job,
            // otherwise skip it
            if (!workList
                    .contains(new Pair<MethodInvocation, DataNode>(methodInvocationToCarve, actualParameterToCarve))) {

                // Add to the work list and execute it
                workList.add(new Pair<MethodInvocation, DataNode>(methodInvocationToCarve, actualParameterToCarve));

                Pair<ExecutionFlowGraph, DataDependencyGraph> partialCarvingResult = carveMethodParameter(
                        actualParameterToCarve, methodInvocationToCarve, //
                        workList, alreadyCarvedMethods, alreadyCarvedObjects);
                /*
                 * Include the required actions to get to this object instance
                 */
                executionFlowGraph.include(partialCarvingResult.getFirst());
                dataDependencyGraph.include(partialCarvingResult.getSecond());
                /*
                 * Include the dependencies between the methodInvocationToCarve
                 * and its parameter in the given position
                 */
                dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocationToCarve, actualParameterToCarve, //
                        methodInvocationToCarve.getActualParameterInstances().indexOf(actualParameterToCarve));
            }
        }

        /*
         * TODO: At this point we need to be sure we called all the methods to
         * external libraries which might have changed the external state of the
         * app
         */

        // Is this necessary for POJO CLASSES ? MAYBE WE CAN FILTER THIS OUT FOR
        // ACTIVITY AND FRAGMENTS AS WELL ?
        // for(MethodInvocation methodInvocation :
        // findPotentiallyExternalStateChangingMethodsFor(methodInvocationToCarve)){
        // if (!workList
        // .contains(new Pair<MethodInvocation,
        // MethodInvocation>(methodInvocationToCarve, methodInvocation))) {
        //
        // workList.add(new Pair<MethodInvocation,
        // MethodInvocation>(methodInvocationToCarve, methodInvocation));
        //
        // Pair<ExecutionFlowGraph, DataDependencyGraph> partialCarvingResult =
        // carveMethod(methodInvocation, workList, alreadyCarvedMethods,
        // alreadyCarvedObjects);
        //
        // /*
        // * Include the required actions to include external calls
        // */
        // executionFlowGraph.include(partialCarvingResult.getFirst());
        // dataDependencyGraph.include(partialCarvingResult.getSecond());
        // }
        // }

        /*
         * Add the result of carving into the cache before returning it
         */
        alreadyCarvedMethods.put(
                new Pair<MethodInvocation, MethodInvocation>(methodInvocationToCarve, methodInvocationToCarve),
                carvingResult);

        return carvingResult;
    }

    /**
     * A method owner is always an object and it is always not-null, otherwise
     * the trace would have logged an exception... TODO right ?
     * 
     * @param owner
     * @param alreadyCarvedMethods
     * @param alreadyCarvedObjects
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> carveMethodOwner(ObjectInstance owner,
            MethodInvocation context, //
            Set<Object> workList, //
            Map<Pair<MethodInvocation, MethodInvocation>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedMethods,
            Map<Pair<MethodInvocation, ObjectInstance>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedObjects)
            throws CarvingException {
        logger.trace(" > Carve Object " + owner + " in context " + context);
        boolean isOwner = true;
        return carveObjectInstance(owner, context, isOwner, //
                workList, //
                alreadyCarvedMethods, alreadyCarvedObjects);
    }

    /**
     * If the actual parameter is a primitive type or a String or a null object
     * there's nothing to carve out of them.
     * 
     * TODO Shall we return the actual Parameter as data dependency as well ? I
     * suspect we might need to include it somehow...
     * 
     * @param actualParameter
     * @param alreadyCarvedMethods
     * @param alreadyCarvedObjects
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> carveMethodParameter(DataNode actualParameter,
            MethodInvocation context, //
            Set<Object> workList, //
            Map<Pair<MethodInvocation, MethodInvocation>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedMethods,
            Map<Pair<MethodInvocation, ObjectInstance>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedObjects)
            throws CarvingException {

        logger.trace(" > Carve MethodParameter " + actualParameter + " in context " + context);

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                new ExecutionFlowGraph(), new DataDependencyGraph());

        if (actualParameter instanceof ObjectInstance) {
            // Will this be the case to have a NullInstance representing a
            // non-string object which is null ?
            if (actualParameter instanceof NullInstance) {
                return carvingResult;
            } else {
                boolean isOwner = false;
                return carveObjectInstance((ObjectInstance) actualParameter, context, isOwner, workList,
                        alreadyCarvedMethods, alreadyCarvedObjects);
            }

        } else {
            return carvingResult;
        }
    }

    /**
     * 
     * @param objectInstanceToCarve
     * @param context
     * @param isOwner
     * @param workList
     * @param alreadyCarvedMethods
     * @param alreadyCarvedObjects
     * @return
     * @throws CarvingException
     */
    private Pair<ExecutionFlowGraph, DataDependencyGraph> carveObjectInstance(ObjectInstance objectInstanceToCarve,
            MethodInvocation context, // We need this to track down where the
                                      // objectInstanceToCarve was needed
            boolean isOwner, // TODO Where is this used?
            Set<Object> workList, //
            Map<Pair<MethodInvocation, MethodInvocation>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedMethods,
            Map<Pair<MethodInvocation, ObjectInstance>, Pair<ExecutionFlowGraph, DataDependencyGraph>> alreadyCarvedObjects)
            throws CarvingException {

        /*
         * Output dependencies of this owner before elaborating them
         */
        List<MethodInvocation> potentiallyStateSettingMethods = new ArrayList<>();
        /*
         * Collect all the method invocations on that object.
         */
        potentiallyStateSettingMethods
                .addAll(findPotentiallyInternalStateChangingMethodsFor(objectInstanceToCarve, context));

        // TODO Do we always include the provider of the aliases ?
        /*
         * Include the methods on that alias -> Honestly I have no idea how is
         * this possible... maybe the system id is tricky ?!
         */
        for (ObjectInstance alias : this.dataDependencyGraph.getAliasesOf(objectInstanceToCarve)) {
            potentiallyStateSettingMethods.addAll(findPotentiallyInternalStateChangingMethodsFor(alias, context));
        }

        Collections.sort(potentiallyStateSettingMethods);
        Collections.reverse(potentiallyStateSettingMethods);

        for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {
            logger.trace("\t Object " + objectInstanceToCarve + " depends on method " + methodInvocationToCarve);
        }

        // Should this be contextual ?!
        if (alreadyCarvedObjects
                .containsKey(new Pair<MethodInvocation, ObjectInstance>(context, objectInstanceToCarve))) {
            logger.trace("Returning Carving results for " + objectInstanceToCarve + " from cache");
            return alreadyCarvedObjects.get(new Pair<MethodInvocation, ObjectInstance>(context, objectInstanceToCarve));
        }

        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();

        /**
         * Find the methods which "possibly" contribute to setting the state of
         * this object and carve them. Those are methods that we called
         * <strong>before</strong> the context:
         * <ul>
         * <li>on that object</li>
         * <li>on aliases of that object</li>
         * <li>In case the object or aliases are not "ours", all the methods
         * that do not belong to the app code, which we receive that object as
         * parameter</li>
         * </ul>
         * 
         * TODO: NOTE: Direct access to object fields (because they are public)
         * will not be considered in this analysis !!!
         */

        for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {

            if (!workList.contains(new Pair<MethodInvocation, MethodInvocation>(context, methodInvocationToCarve))) {

                workList.add(new Pair<MethodInvocation, MethodInvocation>(context, methodInvocationToCarve));

                Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResultFromCarveMethod = carveMethod(
                        methodInvocationToCarve, workList, alreadyCarvedMethods, alreadyCarvedObjects);

                executionFlowGraph.include(carvingResultFromCarveMethod.getFirst());
                dataDependencyGraph.include(carvingResultFromCarveMethod.getSecond());
            }
        }

        if (isOwner) {
            dataDependencyGraph.addDataDependencyOnOwner(context, objectInstanceToCarve);
        }
        // Add here ownership information

        // Add the result of carving into the cache before returning it
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                executionFlowGraph, dataDependencyGraph);

        alreadyCarvedObjects.put(new Pair<MethodInvocation, ObjectInstance>(context, objectInstanceToCarve),
                carvingResult);

        return carvingResult;
    }

    /**
     * List all the method calls which might influence the state of the given
     * object directly or indirectly:
     * <ul>
     * <li>Methods OWNED by the object (direct)</li>
     * <li>Methods which PASS the object as parameter to other methods (both
     * library or user provided)</li>
     * </ul>
     * 
     * 
     * A special attention should be given to method which provides the instance
     * in the first place, either those being constructors, factory methods,
     * singletons, etc. We use an heuristic: if the constructor is private,
     * hence not visible in the trace, we look for those methods which return
     * the object as return value. We sort those methods as follow: - static
     * methods in the same class -> singleton pattern - static methods in the
     * same package (?) -> factory pattern - exception -> at this point not sure
     * we can track this object somewhere... Maybe it was a parameter from the
     * outside ?!
     * 
     * Note that we also rely on Aliasing relations to find possible methods
     * which are related to this object ...
     * 
     * @param objectInstanceToCarve
     * @return
     * @throws CarvingException
     */
    private Collection<MethodInvocation> findPotentiallyInternalStateChangingMethodsFor(
            ObjectInstance objectInstanceToCarve, MethodInvocation context) throws CarvingException {
        /*
         * Get the methods which are invoked on the objectInstance BEFORE the
         * use of the instance
         */
        Set<MethodInvocation> potentiallyStateChangingMethods = new HashSet<>();

        boolean includeTheContext = false;
        // Take all the invocations before the method to carve
        Set<MethodInvocation> methodInvocationsBefore = executionFlowGraph.getMethodInvocationsBefore(context,
                includeTheContext);

        MethodInvocation constructor = dataDependencyGraph.getConstructorOf(objectInstanceToCarve).orElse(null);

        // Take all the calls in this set which were invoked on the method owner
        MethodInvocationMatcher ownership = MethodInvocationMatcher.byInstance(objectInstanceToCarve);
        potentiallyStateChangingMethods
                .addAll(MethodInvocationMatcher.getMethodInvocationsMatchedBy(ownership, methodInvocationsBefore));

        // Take all the calls in which this object was used but which do not
        // belong to user classes (i.e., library calls). Do not limit to
        // libCalls
        MethodInvocationMatcher asParameter = MethodInvocationMatcher.asParameter(objectInstanceToCarve);
        potentiallyStateChangingMethods
                .addAll(MethodInvocationMatcher.getMethodInvocationsMatchedBy(asParameter, methodInvocationsBefore));

        boolean methodFound = potentiallyStateChangingMethods.contains(constructor);

        // Check for methods in the same class which return this object
        if (!methodFound) {
            Set<MethodInvocation> methodInvocationsReturningTheObject = dataDependencyGraph
                    .getMethodInvocationsWhichReturn(objectInstanceToCarve);
            /*
             * Among the methods which return this object, is there anything
             * which is static and belong to the same class? Maybe some builder
             * method..
             */
            Set<MethodInvocation> singletonPattern = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
                    /*
                     * XXX
                     * MethodInvocationMatcher.byClass(objectInstanceToCarve.
                     * getType()), -> This fails for Arrays...
                     */
                    MethodInvocationMatcher.byClassLiteral(objectInstanceToCarve.getType()),
                    methodInvocationsReturningTheObject);
            singletonPattern = MethodInvocationMatcher
                    .getMethodInvocationsMatchedBy(MethodInvocationMatcher.staticCall(), singletonPattern);
            singletonPattern = MethodInvocationMatcher
                    .getMethodInvocationsMatchedBy(MethodInvocationMatcher.before(context), singletonPattern);

            /*
             * Order them by ID and take the last one. This might be not
             * accurate, one should run some sort of data-flow analysis...
             */

            if (!singletonPattern.isEmpty()) {
                List<MethodInvocation> sortedCalls = new ArrayList<>(singletonPattern);
                Collections.sort(sortedCalls);
                methodFound = true;
                potentiallyStateChangingMethods.add(sortedCalls.get(sortedCalls.size() - 1));
            }
        }

        // Check for methods in the same class which return this object
        if (!methodFound) {
            Set<MethodInvocation> methodInvocationsReturningTheObject = dataDependencyGraph
                    .getMethodInvocationsWhichReturn(objectInstanceToCarve);
            Set<MethodInvocation> factoryPattern = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
                    MethodInvocationMatcher.staticCall(), methodInvocationsReturningTheObject);
            factoryPattern = MethodInvocationMatcher
                    .getMethodInvocationsMatchedBy(MethodInvocationMatcher.before(context), factoryPattern);

            /*
             * Order them by ID and take the last one. This might be not
             * accurate, one should run some sort of data-flow analysis...
             */

            if (!factoryPattern.isEmpty()) {
                List<MethodInvocation> sortedCalls = new ArrayList<>(factoryPattern);
                Collections.sort(sortedCalls);
                methodFound = true;
                potentiallyStateChangingMethods.add(sortedCalls.get(sortedCalls.size() - 1));
            }
        }

        // if (!methodFound) {
        // // We do not fail here because there's might be additional
        // // trickeries to get this instance....
        // logger.warn("Provider of object instance " + objectInstanceToCarve
        // + " not found. This will likely fail the test generation");
        // }

        return potentiallyStateChangingMethods;
    }

    /**
     * 
     * @param objectInstanceToCarve
     * @return
     */
    private Collection<MethodInvocation> findPotentiallyExternalStateChangingMethodsFor(MethodInvocation context) {
        /*
         * Get the methods which are invoked on the objectInstance BEFORE the
         * use of the instance
         */
        Set<MethodInvocation> potentiallyStateChangingMethods = new HashSet<>();

        boolean includeTheContext = false;
        // Take all the invocations before the method to carve
        Set<MethodInvocation> methodInvocationsBefore = executionFlowGraph.getMethodInvocationsBefore(context,
                includeTheContext);

        MethodInvocationMatcher externalCall = MethodInvocationMatcher.libCall();
        potentiallyStateChangingMethods
                .addAll(MethodInvocationMatcher.getMethodInvocationsMatchedBy(externalCall, methodInvocationsBefore));

        return potentiallyStateChangingMethods;
    }

}
