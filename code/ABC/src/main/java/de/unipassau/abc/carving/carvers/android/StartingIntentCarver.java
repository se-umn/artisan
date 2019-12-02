package de.unipassau.abc.carving.carvers.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.NullInstance;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import soot.Scene;
import soot.SootClass;

/**
 * @author gambi
 *
 *         TODO At the moment let's do a shallow carving of the intent. We defer
 *         later the decision which parameter we want to further explore/carve
 *         or mock
 */
public class StartingIntentCarver {

    private final Logger logger = LoggerFactory.getLogger(StartingIntentCarver.class);

    private ExecutionFlowGraph executionFlowGraph;
    private DataDependencyGraph dataDependencyGraph;
    private CallGraph callGraph;

    public StartingIntentCarver(ExecutionFlowGraph executionFlowGraph, //
            DataDependencyGraph dataDependencyGraph, //
            CallGraph callGraph) {
        super();
        this.executionFlowGraph = executionFlowGraph;
        this.dataDependencyGraph = dataDependencyGraph;
        this.callGraph = callGraph;
    }

    public Pair<ExecutionFlowGraph, DataDependencyGraph> carve(ObjectInstance activityUnderTest)
            throws CarvingException {

        if (!activityUnderTest.isAndroidActivity()) {
            throw new CarvingException("Not an Android activity " + activityUnderTest);
        }

        MethodInvocation onCreate = findTheOriginalOnCreateMethod(activityUnderTest);
        MethodInvocation methodInvocationStartingThisActivity = findTheStartActivityMethod(activityUnderTest, onCreate);

        if (methodInvocationStartingThisActivity == null) {
            logger.info(activityUnderTest + " is the main activity there's no intent starting it");
            return new Pair<ExecutionFlowGraph, DataDependencyGraph>(new ExecutionFlowGraph(),
                    new DataDependencyGraph());
        } else {
            ObjectInstance startingIntent = (ObjectInstance) methodInvocationStartingThisActivity
                    .getActualParameterInstances().get(0);
            logger.info(activityUnderTest + " was started by intent " + startingIntent + " from "
                    + methodInvocationStartingThisActivity);
            // We perform a shallow carving of the intent, that is we do not
            // follow data dependencies, to collect any putExtra method call
            Pair<ExecutionFlowGraph, DataDependencyGraph> carvedIntent = shallowCarveOf(startingIntent, methodInvocationStartingThisActivity);
            /*
             * It the intend does not carry any data, we can avoid to return it as it is not required to create the test.
             */
            
            List<MethodInvocation> carvingIntentMethods = carvedIntent.getFirst().getOrderedMethodInvocations();
            // Keep only the methods invoked on the intent.
            MethodInvocationMatcher.keepMatchingInPlace( MethodInvocationMatcher.byOwner( startingIntent), carvingIntentMethods);
            // Check if there's some calls that we need to keep in place: set*|put*|remove*|add*|replace*|remove*|fill*|read*
            // TODO Untested behavior
            MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("set*|put*|remove*|add*|replace*|remove*|fill*|read*"), carvingIntentMethods);
            if( carvingIntentMethods.isEmpty() ){
                logger.info("Starting intent is basic, we can omit it");
                return new Pair<ExecutionFlowGraph, DataDependencyGraph>(new ExecutionFlowGraph(),
                        new DataDependencyGraph());
            } else {
                logger.info("Starting intent has state change methods, we cannot omit it");
                return carvedIntent;
            }
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

    private MethodInvocation findTheStartActivityMethod(ObjectInstance activityUnderTest, MethodInvocation onCreate) throws CarvingException {
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
            MethodInvocation startActivity = methodsWhichMightHaveStartedThisActivity.get(methodsWhichMightHaveStartedThisActivity.size() - 1);
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
        onCreateMethods.addAll(this.dataDependencyGraph.getMethodInvocationsForOwner( activityUnderTest ));
        Collections.sort(onCreateMethods);
        
        MethodInvocationMatcher.keepMatchingInPlace(
                MethodInvocationMatcher.byMethodName("onCreate"),
                onCreateMethods);
        
        MethodInvocationMatcher.keepMatchingInPlace(
                MethodInvocationMatcher.byClass(activityUnderTest.getType()),
                onCreateMethods);
        
        if( onCreateMethods.size() != 1 ){
            logger.error("Wrong expectation on onCreate methods. Found " + onCreateMethods );
            throw new CarvingException("Wrong expectation on onCreate methods. Found " + onCreateMethods.size() + " instead of 1");
            
        }
        
        MethodInvocation onCreateMethodInvocation = onCreateMethods.get(0);
        
        if( onCreateMethodInvocation.getActualParameterInstances().get(0) instanceof NullInstance ){
            logger.info("Found original onCreate method " + onCreateMethodInvocation + " for activity " + activityUnderTest );
            return onCreateMethodInvocation;
        } else {
            // If the activity was destroyed after a device rotation, the bundle object is exactly the same. But we cannot really rely on this, do we ?
            // Not sure what happens android kills and restart the app... probably we get the onCreate(null), do we?
            logger.info("Found an onCreate method " + onCreateMethodInvocation + " with bundle " + onCreateMethodInvocation.getActualParameterInstances().get(0) + " for activity " + activityUnderTest );
            
            // Get the previously onDestroied activity of the same type...
            List<MethodInvocation> onDestroyMethods = this.executionFlowGraph.getOrderedMethodInvocationsBefore( onCreateMethodInvocation );
            
            MethodInvocationMatcher.keepMatchingInPlace(
                    MethodInvocationMatcher.byMethodName("onDestroy"),
                    onDestroyMethods);
            MethodInvocationMatcher.keepMatchingInPlace(
                    MethodInvocationMatcher.byClass(activityUnderTest.getType()),
                    onDestroyMethods);
            // Get the last one and return its owner
            ObjectInstance previouslyDestroyedActivity = onDestroyMethods.get(onDestroyMethods.size() - 1).getOwner();
            return findTheOriginalOnCreateMethod( previouslyDestroyedActivity );
        }
    }

}
