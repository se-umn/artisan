package de.unipassau.abc.parsing.postprocessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.CallGraphImpl;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.NullInstance;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.PrimitiveValue;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.instrumentation.SceneInstrumenterWithMethodParameters;
import de.unipassau.abc.parsing.ParsedTrace;
import edu.emory.mathcs.backport.java.util.Arrays;

public class AndroidParsedTraceDecorator implements ParsedTraceDecorator {

    public final static Logger logger = LoggerFactory.getLogger(AndroidParsedTraceDecorator.class);

    @Override
    public ParsedTrace decorate(ParsedTrace parsedTrace) {
        // Note: the order of applications of those methods matters!

        // Include additional metadata about Android invocations
        ParsedTrace decorated = decorateWithAndroidMetadata(parsedTrace);
        // Include aliasing for tainted Intents - This changed the graph so we need to do it before static deps are added
        decorated = decorateWithAliasForIntents(parsedTrace);
        // Include implicit data dependencies that are Android-specific
        decorated = decorateWithIntentDataDependencies(parsedTrace);
        // Remove known noisy dependencies
        decorated = decorateByRemovingKnownNoisyDependencies(parsedTrace);
        return decorated;
    }

    // This may be too disruptive
    // Replace dependencies that are not really useful to the end of carving, for
    // example, Intents' context
    private ParsedTrace decorateByRemovingKnownNoisyDependencies(ParsedTrace parsedTrace) {
        //
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            // We make sure that there exists some always callable method that can generate
            // this specific context
            // Maybe something like the chain in Soot would be better here...
            // int invocationTraceId, int invocationCount, String methodSignature
            int invocationTraceId = -1; // We do not really care since this is synthetic... Hope this does not break
                                        // anything
            int invocationCount = -1; // This will be set by the execution flow graph
            String methodSignature = "<abc.DefaultContextGenerator: android.content.Context generateDefaultContext()>";
            MethodInvocation generateDefaultAndroidContext = new MethodInvocation(invocationTraceId, invocationCount,
                    methodSignature);
            ObjectInstance defaultContext = new ObjectInstance("android.content.Context@-1");
            generateDefaultAndroidContext.setReturnValue(defaultContext);
            generateDefaultAndroidContext.setPublic(true);
            generateDefaultAndroidContext.setPrivate(false);
            generateDefaultAndroidContext.setStatic(true);
            generateDefaultAndroidContext.setSyntheticMethod(true);
            generateDefaultAndroidContext.setOwner(new NullInstance("abc.DefaultContextGenerator"));

            // Make sure this method call can be seen by everybody and is always included in
            // the carved tests
            // Note this changes also the method call object
            ((ExecutionFlowGraphImpl) executionFlowGraph).prependMethodInvocation(generateDefaultAndroidContext);

            // Make sure this call is on top of the nesting, i.e., root? so it is always
            // callable.
            ((CallGraphImpl) callGraph).injectCallAsRoot(generateDefaultAndroidContext);

            // Make sure we add the proper data dependencies
            dataDependencyGraph.addDataDependencyOnReturn(generateDefaultAndroidContext, defaultContext);

            // Now we can replace USES of ANY instance of android.content.Context AS
            // PARAMETER with the default CONTEXT
            // Find intents and replace their context with the default one
//            for (ObjectInstance obj : dataDependencyGraph.getObjectInstances()) {
//                if (!obj.getType().equals("android.content.Intent")) {
//                    continue;
//                }
            for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
                // In general we could look for any instance of android.content.Context but we
                // keep it tight here
                // TODO Here we can work only at syntactic level, no type resolution whatsoever
                if (mi.getMethodSignature().contains("android.content.Context")) {

                    // Find the parameters to replace. Forget about replacing instances of the
                    // Context
                    List<Integer> positions = new ArrayList<Integer>();
                    int currentPosition = 0;
                    for (String formalParameter : JimpleUtils.getParameterList(mi.getMethodSignature())) {
                        if (formalParameter.equals("android.content.Context")) {
                            positions.add(currentPosition);
                        }
                        currentPosition = currentPosition + 1;
                    }

                    // Replace them
                    for (Integer positionOfParameterToReplace : positions) {
                        // This works also by replacing the Default Contenxt with itself
                        logger.info("Replacing Actual Context "
                                + mi.getActualParameterInstances().get(positionOfParameterToReplace)
                                + " with DefaultContext one in method " + mi + " at position "
                                + positionOfParameterToReplace);

                        dataDependencyGraph.replaceDataDependencyOnActualParameter(mi, defaultContext,
                                positionOfParameterToReplace);
                        // Update also the object!
                        DataNode[] updatedParameters = mi.getActualParameterInstances().toArray(new DataNode[] {});
                        updatedParameters[positionOfParameterToReplace] = defaultContext;
                        // TODO Is this enough or should I do the same for actualParameters?
                        mi.setActualParameterInstances(Arrays.asList(updatedParameters));

                    }
                }
//                }
            }
        });
        return parsedTrace;
    }

    // TODO This introduces an Alias relation, but what if we replace every
    // occurrence of a data with its (first/elder) alias instead?
    // Now this is tricky since we linked activities and instances of intent before,
    // and now we want to update that link as well if they are SINK
    private ParsedTrace decorateWithAliasForIntents(ParsedTrace parsedTrace) {

        final String intentTagAsString = '"' + SceneInstrumenterWithMethodParameters.INTENT_TAINT_TAG + '"';

        // For each intent look for the corresponding one...
        // TODO This applies only to activity right?
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {

            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            //
            Map<String, ObjectInstance> sourceIntents = new HashedMap();
            Map<String, ObjectInstance> sinkIntents = new HashedMap();

            for (ObjectInstance obj : dataDependencyGraph.getObjectInstances()) {
                if (!obj.getType().equals("android.content.Intent")) {
                    continue;
                }
                for (MethodInvocation mi : dataDependencyGraph.getMethodInvocationsForOwner(obj)) {

                    if (mi.getMethodSignature()
                            .equals("<android.content.Intent: android.content.Intent putExtra(java.lang.String,int)>")
                            && ((PrimitiveValue) mi.getActualParameterInstances().get(0)).toString()
                                    .equals(intentTagAsString)) {
                        logger.info("Intent " + obj + " is SOURCE of TAINT " + mi.getActualParameterInstances().get(1));
                        sourceIntents.put(mi.getActualParameterInstances().get(1).toString(), obj);
                    }

                    if (mi.getMethodSignature()
                            .equals("<android.content.Intent: int getIntExtra(java.lang.String,int)>")
                            && ((PrimitiveValue) mi.getActualParameterInstances().get(0)).toString()
                                    .equals(intentTagAsString)) {

                        if (mi.getReturnValue().toString().equals("-1")) {
                            logger.info("Canont find SOURCE for SINK intent " + obj);
                        } else {
                            logger.info("Intent " + obj + " is SINK of TAINT " + mi.getReturnValue());
                            sinkIntents.put(mi.getReturnValue().toString(), obj);
                        }
                    }
                }
            }
            // Get the activities
            Set<ObjectInstance> androidActivities = new HashSet<ObjectInstance>();
            for (ObjectInstance obj : dataDependencyGraph.getObjectInstances()) {
                if (obj.isAndroidActivity()) {
                    if (!androidActivities.contains(obj)) {
                        androidActivities.add(obj);
                    }

                }
            }

            // Finally match sink and sources
            for (Entry<String, ObjectInstance> source : sourceIntents.entrySet()) {
                String taint = source.getKey();
                ObjectInstance sourceIntent = source.getValue();

                if (sinkIntents.containsKey(taint)) {
                    // Better to use alias or to replace the values? - I would replace the value of
                    // the intents everywhere, including method invocations
                    // Find all the method calls that take sin
                    // dataDependencyGraph.addAliasingDataDependency(source.getValue(),
                    // sinkIntents.get(taint));
                    logger.info("Replacing sink taint with source taing for TAINT: " + taint);
                    final ObjectInstance sinkIntent = sinkIntents.get(taint);

                    // Parameters
                    // Find all the method invocations that gets sink intent as parameter and
                    // replace it. This usually is empty since the sink intent is the one returned
                    // by the getIntent() call
                    for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
                        for (int i = 0; i < mi.getActualParameterInstances().size(); i++) {
                            DataNode param = mi.getActualParameterInstances().get(i);
                            if (sinkIntent.equals(param)) {
                                logger.info("Replacing " + sinkIntent + " with " + sourceIntent + " in " + mi
                                        + " at position " + i);
                                dataDependencyGraph.replaceDataDependencyOnActualParameter(mi, sourceIntent, i);
                                mi.getActualParameterInstances().set(i, sourceIntent);

                            }
                        }
                    }

                    // Ownership
                    // Replace the invocations that have as owner sinkIntent with sourceIntent
                    // instead
                    for (MethodInvocation sinkIntentMi : dataDependencyGraph.getMethodInvocationsForOwner(sinkIntent)) {

                        if (sinkIntentMi.isConstructor()) {
                            logger.info("Delete the original constructor " + sinkIntentMi);
                            dataDependencyGraph.remove(sinkIntentMi);
                        } else {

                            logger.info("Updating the ownership of " + sinkIntentMi + " to " + sourceIntent);
                            sinkIntentMi.setOwner(sourceIntent);
                            dataDependencyGraph.replaceDataDependencyOnOwner(sinkIntentMi, sourceIntent);
                        }

                    }

                    // Returns
                    for (MethodInvocation sinkIntentMi : dataDependencyGraph
                            .getMethodInvocationsWhichReturn(sinkIntent)) {
                        logger.info("Updating the return of " + sinkIntentMi + " to " + sourceIntent);
                        sinkIntentMi.setReturnValue(sourceIntent);
                        dataDependencyGraph.replaceDataDependencyOnReturn(sinkIntentMi, sourceIntent);

                    }

                    // Make sure we replace the sink intents also inside the activities that require
                    // them
                    for (ObjectInstance activity : androidActivities) {
                        if (activity.requiresIntent() && activity.getIntent().equals(sinkIntents.get(taint))) {
                            logger.info("Replace sink intent " + sinkIntents.get(taint) + " with source intent "
                                    + source.getValue() + " for taint " + taint);
                            activity.setIntent(source.getValue());
                        }
                    }

                    logger.info("Remove " + sinkIntent + " from the datagraph");
                    // Finally remove the sink data node from the datagraph
                    dataDependencyGraph.remove(sinkIntent);

                    // DEBUG: List all the methods on the source
                    dataDependencyGraph.getMethodInvocationsForOwner(sourceIntent).forEach(System.out::println);
                }
            }

        });

        return parsedTrace;
    }

    // Note This does not require Intents to be tainted. However, if they are not,
    // they will be mocked/shadowed probably.
    private ParsedTrace decorateWithIntentDataDependencies(ParsedTrace parsedTrace) {
        final MethodInvocationMatcher getIntentMethodInvocationMatcher = MethodInvocationMatcher
                .byMethodLiteral("<android.app.Activity: android.content.Intent getIntent()>");

        // TODO This applies only to activity right?
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            // Find all the Android Activities (TODO Right? Or do we need more classes?)

            Set<ObjectInstance> androidActivities = new HashSet<ObjectInstance>();
            Map<ObjectInstance, Collection<MethodInvocation>> methodInvocationsToCheck = new HashMap<ObjectInstance, Collection<MethodInvocation>>();
            for (ObjectInstance obj : dataDependencyGraph.getObjectInstances()) {
                if (obj.isAndroidActivity()) {
                    if (!androidActivities.contains(obj)) {
                        methodInvocationsToCheck.put(obj, dataDependencyGraph.getMethodInvocationsForOwner(obj));
                        androidActivities.add(obj);
                    }

                }
            }

            for (Entry<ObjectInstance, Collection<MethodInvocation>> entry : methodInvocationsToCheck.entrySet()) {
                final ObjectInstance activity = entry.getKey();
                for (MethodInvocation mi : entry.getValue()) {
                    for (MethodInvocation subsumed_mi : callGraph.getMethodInvocationsSubsumedBy(mi)) {
                        // Find the call to getIntent
                        if (getIntentMethodInvocationMatcher.matches(subsumed_mi)) {
                            // Extract the intent - It must be there or this is an error!
                            try {
                                DataNode intent = dataDependencyGraph.getReturnValue(subsumed_mi).get();
                                // Get the caller of this method and add the dependency to it
                                // We need the method that is directly calling getIntent
                                MethodInvocation methodThatRequireTheDependencyOnTheIntent = callGraph
                                        .getCallerOf(subsumed_mi);
                                // Create the dependency to the intent
                                dataDependencyGraph.addImplicitDataDependency(methodThatRequireTheDependencyOnTheIntent,
                                        intent);

                                activity.setRequiresIntent(true);
                                activity.setIntent((ObjectInstance) intent);

                                // TODO Patch: Make sure we automatically declares that also the constructor
                                // requires the same intent!

                                MethodInvocation activityConstructor = dataDependencyGraph
                                        .getInitMethodInvocationFor(activity);
                                dataDependencyGraph.addImplicitDataDependency(activityConstructor, intent);
                            } catch (ABCException e) {
                                // TODO Auto-generated catch block
//                            e.printStackTrace();
                                throw new RuntimeException(e);

                            }

                        }
                    }
                }
            }
        });

        return parsedTrace;

    }

    private ParsedTrace decorateWithAndroidMetadata(ParsedTrace parsedTrace) {
        // Go over the parsed trace and replace each method invocation with the android
        // versions of it
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            // First follow constructor call chains and check if somewhere an android super
            // constructor is called
            executionFlowGraph.getOrderedMethodInvocations().stream().filter(MethodInvocation::isConstructor)
                    .forEach(methodInvocation -> {
                        if (isAndroidActivity(methodInvocation, callGraph)) {
                            dataDependencyGraph.getOwnerFor(methodInvocation).setAndroidActivity(true);
                        } else if (isAndroidFragment(methodInvocation, callGraph)) {
                            dataDependencyGraph.getOwnerFor(methodInvocation).setAndroidFragment(true);
                        }
                    });

            // Replace original method invocation instances with android ones in the graphs
            executionFlowGraph.getOrderedMethodInvocations().stream().filter(methodInvocation -> {
                ObjectInstance owner = dataDependencyGraph.getOwnerFor(methodInvocation);

                if (owner == null) {
                    // Static methods like: android.util.Log
                    return JimpleUtils.isAndroidMethod(methodInvocation.getMethodSignature());
                } else {
                    // Basically, if a method has an android package or is part of an
                    // activity/fragment and
                    // is a constructor/lifecycle callback, then it is considered android related.
                    // But does
                    // this cover everything?
                    // TODO what about static methods?
                    return JimpleUtils.isAndroidMethod(methodInvocation.getMethodSignature())
                            || (owner.isAndroidActivity()
                                    && JimpleUtils.isActivityLifecycle(methodInvocation.getMethodSignature()))
                            || (owner.isAndroidActivity() && methodInvocation.isConstructor())
                            || (owner.isAndroidFragment()
                                    && JimpleUtils.isFragmentLifecycle(methodInvocation.getMethodSignature()))
                            || (owner.isAndroidFragment() && methodInvocation.isConstructor());

                }
            }).forEach(originalMethodInvocation -> {
                AndroidMethodInvocation androidMethodInvocation = new AndroidMethodInvocation(originalMethodInvocation);
                executionFlowGraph.replaceMethodInvocation(originalMethodInvocation, androidMethodInvocation);
                callGraph.replaceMethodInvocation(originalMethodInvocation, androidMethodInvocation);
                dataDependencyGraph.replaceMethodInvocation(originalMethodInvocation, androidMethodInvocation);
            });
        });

        return parsedTrace;
    }

    private boolean isAndroidActivity(MethodInvocation methodInvocation, CallGraph callGraph) {
        return isAndroidElement(methodInvocation, callGraph, className -> className.endsWith("Activity"));
    }

    private boolean isAndroidFragment(MethodInvocation methodInvocation, CallGraph callGraph) {
        return isAndroidElement(methodInvocation, callGraph, className -> className.endsWith("Fragment"));
    }

    /*
     * Lookup the sub method calls of method and check if there is an android
     * related constructor call
     */
    private boolean isAndroidElement(MethodInvocation methodInvocation, CallGraph callGraph,
            Predicate<String> filterPredicate) {
        Set<MethodInvocation> subMethods = callGraph.getMethodInvocationsSubsumedBy(methodInvocation);
        if (methodInvocation.isConstructor() && JimpleUtils.isAndroidMethod(methodInvocation.getMethodSignature())
                && filterPredicate.test(JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()))) {
            return true;
        } else {
            boolean isAndroid = false;
            for (MethodInvocation subMethod : subMethods) {
                isAndroid |= isAndroidActivity(subMethod, callGraph);
            }
            return isAndroid;
        }
    }

}
