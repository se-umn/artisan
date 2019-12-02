package de.unipassau.abc.carving.carvers.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.NullInstance;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;

/**
 * Perform a shallow carving of all the methods invoked on the given activity and
 * the previously linked ones from the given context
 * 
 * @author gambi
 *
 */
public class ActivityMethodsCarver {

    private final Logger logger = LoggerFactory.getLogger(ActivityMethodsCarver.class);

    private ExecutionFlowGraph executionFlowGraph;
    private DataDependencyGraph dataDependencyGraph;
    private CallGraph callGraph;

    public ActivityMethodsCarver(ExecutionFlowGraph executionFlowGraph, //
            DataDependencyGraph dataDependencyGraph, //
            CallGraph callGraph) {
        super();
        this.executionFlowGraph = executionFlowGraph;
        this.dataDependencyGraph = dataDependencyGraph;
        this.callGraph = callGraph;
    }

    public Pair<ExecutionFlowGraph, DataDependencyGraph> carve(ObjectInstance activityUnderTest, MethodInvocation from)
            throws CarvingException {

        if (!activityUnderTest.isAndroidActivity()) {
            throw new CarvingException("Not an Android activity " + activityUnderTest);
        }

        if (!activityUnderTest.equals(from.getOwner())) {
            throw new CarvingException("Not the same activity owner " + activityUnderTest + " " + from.getOwner()
                    + " for activity " + from);
        }

        // Define the starting point of the carving of this activity (this does
        // not include <init>, but that's never called right?)
        Set<ObjectInstance> linkedActivities = findLinkedActivities(activityUnderTest);

        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvedResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                executionFlowGraph, dataDependencyGraph);

        // At this point we need to incldue in the carve all the methods invoked
        // on all the linkedActivities
        for (ObjectInstance activity : linkedActivities) {
            logger.info("Collecting Methods dependencies for activity " + activity + " before " + from);
            List<MethodInvocation> methodInvokedBefore = new ArrayList(
                    this.dataDependencyGraph.getMethodInvocationsForOwner(activity));
            MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.before(from), methodInvokedBefore);
            // Include also the caller
            methodInvokedBefore.add(from);
            // Sort them
            Collections.sort( methodInvokedBefore );
            for (MethodInvocation methodInvocation : methodInvokedBefore) {
                logger.info("\t " + activity + " depends on " + methodInvocation);
                Pair<ExecutionFlowGraph, DataDependencyGraph> partialResult = shallowCarveOf(methodInvocation);
                executionFlowGraph.include(partialResult.getFirst());
                dataDependencyGraph.include(partialResult.getSecond());
            }
        }
        return carvedResult;
    }

    private Pair<ExecutionFlowGraph, DataDependencyGraph> shallowCarveOf(MethodInvocation methodToCarve) {
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        executionFlowGraph.enqueueMethodInvocations(methodToCarve);
        DataDependencyGraph dataDependencyGraph = this.dataDependencyGraph.getSubGraph(executionFlowGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                executionFlowGraph, dataDependencyGraph);
        return carvingResult;
    }

    private Set<ObjectInstance> findLinkedActivities(ObjectInstance activityUnderTest) throws CarvingException {
        Set<ObjectInstance> linkedActivities = new HashSet<>();
        linkedActivities.add(activityUnderTest);

        List<MethodInvocation> onCreateMethods = new ArrayList<>();
        onCreateMethods.addAll(this.dataDependencyGraph.getMethodInvocationsForOwner(activityUnderTest));
        Collections.sort(onCreateMethods);

        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("onCreate"), onCreateMethods);

        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byClass(activityUnderTest.getType()),
                onCreateMethods);

        if (onCreateMethods.size() != 1) {
            logger.error("Wrong expectation on onCreate methods. Found " + onCreateMethods);
            throw new CarvingException(
                    "Wrong expectation on onCreate methods. Found " + onCreateMethods.size() + " instead of 1");

        }

        MethodInvocation onCreateMethodInvocation = onCreateMethods.get(0);

        if (onCreateMethodInvocation.getActualParameterInstances().get(0) instanceof NullInstance) {
            return linkedActivities;
        } else {
            // Get the previously onDestroied activity of the same type...
            List<MethodInvocation> onDestroyMethods = this.executionFlowGraph
                    .getOrderedMethodInvocationsBefore(onCreateMethodInvocation);

            MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("onDestroy"),
                    onDestroyMethods);
            MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byClass(activityUnderTest.getType()),
                    onDestroyMethods);
            // Get the last one and return its owner
            ObjectInstance previouslyDestroyedActivity = onDestroyMethods.get(onDestroyMethods.size() - 1).getOwner();
            // Accumulate all the activities we find on our way
            linkedActivities.addAll(findLinkedActivities(previouslyDestroyedActivity));
            //
            return linkedActivities;
        }
    }

}
