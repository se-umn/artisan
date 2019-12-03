package de.unipassau.abc.carving.carvers.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.NullInstance;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;
import soot.Scene;
import soot.SootClass;

/**
 * @author gambi
 *
 *         Carve the Intent used to start a secondary activity FROM the activity
 *         under test and the corresponding Result. This is needed to trigger
 *         'onActivityResult'
 */
public class RequestResponseIntentsCarver {

    private final Logger logger = LoggerFactory.getLogger(RequestResponseIntentsCarver.class);

    private ExecutionFlowGraph executionFlowGraph;
    private DataDependencyGraph dataDependencyGraph;
    private CallGraph callGraph;

    public RequestResponseIntentsCarver(ExecutionFlowGraph executionFlowGraph, //
            DataDependencyGraph dataDependencyGraph, //
            CallGraph callGraph) {
        super();
        this.executionFlowGraph = executionFlowGraph;
        this.dataDependencyGraph = dataDependencyGraph;
        this.callGraph = callGraph;
    }

    public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(ObjectInstance activityUnderTest,
            MethodInvocation from) throws CarvingException {

        if (!activityUnderTest.isAndroidActivity()) {
            throw new CarvingException("Not an Android activity " + activityUnderTest);
        }

        if (!activityUnderTest.equals(from.getOwner())) {
            throw new CarvingException("Not the same activity owner " + activityUnderTest + " " + from.getOwner()
                    + " for activity " + from);
        }

        // Find all the methods of this activity
        ActivityMethodsCarver activityMethodsCarver = new ActivityMethodsCarver(this.executionFlowGraph,
                this.dataDependencyGraph, this.callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvedActivityMethods = activityMethodsCarver
                .carve(activityUnderTest, from);
        // Get only the onResume/onPause method invocations
        List<MethodInvocation> onResumeAndOnPauseMethodInvocations = new ArrayList<>(
                carvedActivityMethods.getFirst().getOrderedMethodInvocations());
        //
        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("onPause|onResume"),
                onResumeAndOnPauseMethodInvocations);
        // Get rid of calls to super classes
        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byClass(activityUnderTest.getType()),
                onResumeAndOnPauseMethodInvocations);

        logger.info("Found " + onResumeAndOnPauseMethodInvocations.size() + " onResume+onPause methods for activity "
                + activityUnderTest + " and linked activities");

        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedIntents = new ArrayList<>();
        
        if( onResumeAndOnPauseMethodInvocations.isEmpty() ){
            return carvedIntents;
        }
        
        // TODO Before each onPause there must be exactly one onResume
        if (onResumeAndOnPauseMethodInvocations.get(0).getMethodSignature().contains("onPause")) {
            logger.error("Wrong expectation on onResume/onPause. " + activityMethodsCarver
                    + " calls before onPause than onResume \n" + onResumeAndOnPauseMethodInvocations);
            throw new CarvingException("Wrong expectation on onResume/onPause. " + activityMethodsCarver
                    + " calls before onPause than onResume");
        }


        for (MethodInvocation onPauseMethodInvocation : onResumeAndOnPauseMethodInvocations) {

            if (!onPauseMethodInvocation.getMethodSignature().contains("onPause")) {
                // Skip onResume methods
                continue;
            }

            MethodInvocation onResumeMethodInvocation = onResumeAndOnPauseMethodInvocations
                    .get(onResumeAndOnPauseMethodInvocations.indexOf(onPauseMethodInvocation) - 1);

            if (onResumeMethodInvocation.getMethodSignature().contains("onPause")) {
                logger.error("Wrong expectation on onResume/onPause. " + activityMethodsCarver
                        + " do not call onResume before " + onPauseMethodInvocation + "\n");
                throw new CarvingException("Wrong expectation on onResume/onPause. " + activityMethodsCarver
                        + " do not call onResume before " + onPauseMethodInvocation + "\n");
            }

            // Was this onPause caused by a startActivityForResult, a
            // startActivity, or a deviceRotation/BackPressed ?
            MethodInvocation startActivityForResult = findStartActivityForResultCausingTheOnPauseBetween(
                    activityMethodsCarver, onResumeMethodInvocation, onPauseMethodInvocation);

            if (startActivityForResult == null) {
                logger.info("Did not find any startActivityForResult for " + activityMethodsCarver + " from " + from );
                continue;
            }

            // If we are here we found the startWithResult:
            ExecutionFlowGraph efGraph = new ExecutionFlowGraph();
            DataDependencyGraph ddGraph = new DataDependencyGraph();
            //
            Pair<ExecutionFlowGraph, DataDependencyGraph> carvedIntentsForOnPause = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                    efGraph, ddGraph);
            //
            carvedIntents.add(carvedIntentsForOnPause);
            // 1 - Carve the Request Intent (used to start the Activity)

            // Which activity was started by this intent
            ObjectInstance startedActivity = findStartedActivity(activityUnderTest, startActivityForResult);

            StartingIntentCarver intentCarver = new StartingIntentCarver(this.executionFlowGraph,
                    this.dataDependencyGraph, this.callGraph);
            Pair<ExecutionFlowGraph, DataDependencyGraph> requestIntent = intentCarver.carve(startedActivity);
            //
            efGraph.include(requestIntent.getFirst());
            ddGraph.include(requestIntent.getSecond());
            
            // 2 - Carve the Response Intent (from that Activity)
            ResponseIntentCarver responsedIntentCarver = new ResponseIntentCarver(this.executionFlowGraph,
                    this.dataDependencyGraph, this.callGraph);
            Pair<ExecutionFlowGraph, DataDependencyGraph> responseIntent = intentCarver.carve(startedActivity);
            //
            efGraph.include(responseIntent.getFirst());
            ddGraph.include(responseIntent.getSecond());
        }
        return carvedIntents;

    }

    private ObjectInstance findStartedActivity(ObjectInstance activityUnderTest,
            MethodInvocation startActivityForResult) throws CarvingException {
        // Get the first activtiy after start which is not the one under test
        List<MethodInvocation> invocationsAfter = new ArrayList<>();
        invocationsAfter.addAll(this.executionFlowGraph.getOrderedMethodInvocationsAfter(startActivityForResult));
        MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.byOwner(activityUnderTest), invocationsAfter);
        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.ownedByAnAndroidActivity(),
                invocationsAfter);

        // This happens if the intent is used to start an activity that belongs
        // to another application and it is not covered here !
        if (invocationsAfter.isEmpty()) {
            throw new CarvingException(
                    activityUnderTest + " does not start another activity via " + startActivityForResult);
        }

        // Since we started an activtiy there must be another activity here ...
        return invocationsAfter.get(0).getOwner();
    }

    private MethodInvocation findStartActivityForResultCausingTheOnPauseBetween(
            ActivityMethodsCarver activityMethodsCarver, MethodInvocation onResumeMethodInvocation,
            MethodInvocation onPauseMethodInvocation) throws CarvingException {

        // Get all the methods between onResume and onPause (of the same
        // activity)
        List<MethodInvocation> methodsInBetween = new ArrayList<>();
        methodsInBetween.addAll(this.executionFlowGraph.getOrderedMethodInvocationsBefore(onPauseMethodInvocation));
        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.after(onResumeMethodInvocation),
                methodsInBetween);
        // in between those there must be AT MOST one and only one startActivity

        MethodInvocationMatcher.keepMatchingInPlace(
                MethodInvocationMatcher.byMethodName("startActivity|startActivityForResult"), methodsInBetween);

        if (methodsInBetween.isEmpty()) {
            logger.info("OnPaused caused by Device Rotation or Navigation event");
            return null;
        }

        if (methodsInBetween.size() > 1) {
            logger.error("Wrong expectation on onResume/onPause. Found more than one startActivity between "
                    + onResumeMethodInvocation + " and " + onPauseMethodInvocation + ":\n " + methodsInBetween);
            throw new CarvingException("Wrong expectation on onResume/onPause. . Found more than one startActivity  + "
                    + methodsInBetween.size());
        }

        MethodInvocation startActivity = methodsInBetween.get(0);
        if (startActivity.getMethodSignature().contains("startActivityForResult")) {
            logger.info(activityMethodsCarver + " was paused by " + startActivity);
            return startActivity;
        } else {
            logger.info(activityMethodsCarver + " was paused by " + startActivity);
            return null;
        }
    }

    private Pair<ExecutionFlowGraph, DataDependencyGraph> shallowCarveOf(ObjectInstance startingIntent,
            MethodInvocation methodStartingTheActivity) {
        List<MethodInvocation> potentiallyStateSettingMethods = new ArrayList<>();
        potentiallyStateSettingMethods.addAll(this.dataDependencyGraph.getMethodInvocationsForOwner(startingIntent));
        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.before(methodStartingTheActivity),
                potentiallyStateSettingMethods);

        Collections.sort(potentiallyStateSettingMethods);

        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {
            logger.trace("\t Intent " + startingIntent + " depends on method " + methodInvocationToCarve);
            executionFlowGraph.enqueueMethodInvocations(methodInvocationToCarve);
        }

        DataDependencyGraph dataDependencyGraph = this.dataDependencyGraph.getSubGraph(executionFlowGraph);

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
                executionFlowGraph, dataDependencyGraph);
        return carvingResult;
    }

    private MethodInvocation findTheStartActivityMethod(ObjectInstance activityUnderTest, MethodInvocation onCreate)
            throws CarvingException {
        List<MethodInvocation> methodsWhichMightHaveStartedThisActivity = new ArrayList<>();
        methodsWhichMightHaveStartedThisActivity.addAll(this.executionFlowGraph.getMethodInvocationsBefore(onCreate));
        Collections.sort(methodsWhichMightHaveStartedThisActivity);

        /*
         * Keep only the startActivity* methods
         */
        MethodInvocationMatcher.keepMatchingInPlace(
                MethodInvocationMatcher.byMethodName("startActivity|startActivityForResult"),
                methodsWhichMightHaveStartedThisActivity);

        if (methodsWhichMightHaveStartedThisActivity.isEmpty()) {
            return null;
        } else {
            // TODO I am not sure this covers 100% of the cases...
            MethodInvocation startActivity = methodsWhichMightHaveStartedThisActivity
                    .get(methodsWhichMightHaveStartedThisActivity.size() - 1);
            DataNode intent = startActivity.getActualParameterInstances().get(0);
            if (intent instanceof ObjectInstance) {
                SootClass childClass = Scene.v().getSootClass(((ObjectInstance) intent).getType());
                SootClass intentClass = Scene.v().getSootClass("android.content.Intent");
                if (Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(childClass, intentClass)) {
                    return startActivity;
                }
            }
            logger.error("Expecting Intent Object but found " + intent + " instead !");
            throw new CarvingException("Expecting Intent Object but found " + intent + " instead !");
        }
    }

    private MethodInvocation findTheOriginalOnCreateMethod(ObjectInstance activityUnderTest) throws CarvingException {
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
            logger.info("Found original onCreate method " + onCreateMethodInvocation + " for activity "
                    + activityUnderTest);
            return onCreateMethodInvocation;
        } else {
            // If the activity was destroyed after a device rotation, the bundle
            // object is exactly the same. But we cannot really rely on this, do
            // we ?
            // Not sure what happens android kills and restart the app...
            // probably we get the onCreate(null), do we?
            logger.info("Found an onCreate method " + onCreateMethodInvocation + " with bundle "
                    + onCreateMethodInvocation.getActualParameterInstances().get(0) + " for activity "
                    + activityUnderTest);

            // Get the previously onDestroied activity of the same type...
            List<MethodInvocation> onDestroyMethods = this.executionFlowGraph
                    .getOrderedMethodInvocationsBefore(onCreateMethodInvocation);

            MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("onDestroy"),
                    onDestroyMethods);
            MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byClass(activityUnderTest.getType()),
                    onDestroyMethods);
            // Get the last one and return its owner
            ObjectInstance previouslyDestroyedActivity = onDestroyMethods.get(onDestroyMethods.size() - 1).getOwner();
            return findTheOriginalOnCreateMethod(previouslyDestroyedActivity);
        }
    }

}
