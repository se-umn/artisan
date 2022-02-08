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
        // Include aliasing for tainted Intents and Serialized/Parcelized Objects - This
        // changes the graph so we need to do it before static deps are added
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
            int invocationCount = -2; // This will be set by the execution flow graph
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
            ((CallGraphImpl) callGraph).insertAsRoot(generateDefaultAndroidContext);

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

    /**
     * To completely replace one object instance with another we need to replace: -
     * deps of the object as parameters and static/implicit dependencies - deps of
     * the object as returnType - deps of the object as owners (pay attention to the
     * constructors!)
     * 
     * @param toReplace
     * @param willReplace
     * @param callGraph
     * @param executionFlowGraph
     */
    private void replaceEveryWhereWith(ObjectInstance toReplace, ObjectInstance willReplace,
            ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph, CallGraph callGraph) {

        logger.info("Replacing everywhere " + toReplace + " with " + willReplace);

        logger.info("Replacing " + toReplace + " as parameter");
        // Parameters - including implicit data deps
        for (MethodInvocation mi : dataDependencyGraph.getMethodInvocationsWhichUse(toReplace)) {
            for (int i = 0; i < mi.getActualParameterInstances().size(); i++) {
                DataNode param = mi.getActualParameterInstances().get(i);
                if (toReplace.equals(param)) {

                    logger.info("Replacing " + toReplace + " with " + willReplace + " in " + mi + " at position " + i);
                    mi.getActualParameterInstances().set(i, willReplace);

                    dataDependencyGraph.replaceDataDependencyOnActualParameter(mi, willReplace, i);
                    //
                    executionFlowGraph.get(mi).getActualParameterInstances().set(i, willReplace);
                    callGraph.get(mi).getActualParameterInstances().set(i, willReplace);

                }
            }
        }

        // Returns
        logger.info("Replacing " + toReplace + " as return value");
        // Returns
        for (MethodInvocation mi : dataDependencyGraph.getMethodInvocationsWhichReturn(toReplace)) {
            logger.info("Updating the return of " + mi + " to " + willReplace);
            mi.setReturnValue(willReplace);
            //
            dataDependencyGraph.replaceDataDependencyOnReturn(mi, willReplace);

            //
            executionFlowGraph.get(mi).setReturnValue(willReplace);
            callGraph.get(mi).setReturnValue(willReplace);
        }

        // Ownership
        // Replace the invocations that have as owner sinkIntent with sourceIntent
        // instead
        for (MethodInvocation mi : dataDependencyGraph.getMethodInvocationsForOwner(toReplace)) {

            if (mi.isConstructor()) {
                logger.info("Delete the original constructor " + mi);
                callGraph.getMethodInvocationsSubsumedBy(mi).forEach(smi -> {
                    executionFlowGraph.remove(smi);
                    dataDependencyGraph.remove(smi);
                });
                executionFlowGraph.remove(mi);
                dataDependencyGraph.remove(mi);
                callGraph.remove(mi);
            } else {

                logger.info("Updating the ownership of " + mi + " to " + willReplace);
                mi.setOwner(willReplace);
                dataDependencyGraph.replaceDataDependencyOnOwner(mi, willReplace);

                executionFlowGraph.get(mi).setOwner(willReplace);
                callGraph.get(mi).setOwner(willReplace);
            }
        }

        logger.info("Removing " + toReplace + " from the datagraph");
        // Finally remove the sink data node from the datagraph
        dataDependencyGraph.remove(toReplace);
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

            Set<MethodInvocation> tantingMethodInvocations = new HashSet<MethodInvocation>();

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

                        tantingMethodInvocations.add(mi);
                    }

                    if (mi.getMethodSignature()
                            .equals("<android.content.Intent: int getIntExtra(java.lang.String,int)>")
                            && ((PrimitiveValue) mi.getActualParameterInstances().get(0)).toString()
                                    .equals(intentTagAsString)) {

                        tantingMethodInvocations.add(mi);

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
                    final ObjectInstance sinkIntent = sinkIntents.get(taint);

                    // In some cases, like
                    // nz.org.cacophony.birdmonitor.CreateAccountTest#createAccountTest the intent
                    // is both SINK and SOURCE.
                    // This is something involving PendingIntent and broadcasting, which Alessio
                    // never saw before...

                    if (sinkIntent.equals(sourceIntent)) {
                        logger.warn("Sink intent " + sinkIntent + " and source intent " + sourceIntent
                                + " are the same for TAINT: " + taint);
                        continue;
                    }

                    logger.info("Replacing sink intent " + sinkIntent + " with source intent " + sourceIntent
                            + " for TAINT: " + taint);

                    // At this point the two intents are linked together. Since they represent the
                    // same logical object, they must contain the
                    // same "extras". Primitive types are not an issue; however, complex objects are
                    // because they are serialized and reconstructed.
                    // Hence we need to match them as well !
                    // First collect objects stored in the source intent:
                    Map<String, ObjectInstance> keyAndSourceObject = new HashMap<String, ObjectInstance>();
                    for (MethodInvocation putExtraMethodInvocation : dataDependencyGraph
                            .getMethodInvocationsForOwner(sourceIntent).stream()
                            .filter(mi -> mi.getMethodSignature().equals(
                                    "<android.content.Intent: android.content.Intent putExtra(java.lang.String,android.os.Parcelable)>")
                                    || mi.getMethodSignature().equals(
                                            "<android.content.Intent: android.content.Intent putExtra(java.lang.String,java.io.Serializable)>"))
                            .collect(Collectors.toList())) {

                        String key = ((PrimitiveValue) putExtraMethodInvocation.getActualParameterInstances().get(0))
                                .toString();
                        ObjectInstance sourceObject = (ObjectInstance) putExtraMethodInvocation
                                .getActualParameterInstances().get(1);

                        logger.info("Found Parcelable/Serializable " + key + " " + sourceObject);
                        keyAndSourceObject.put(key, sourceObject);
                    }

                    // Now replace this object with the object of the matching intent with the same
                    // key
                    Map<ObjectInstance, ObjectInstance> objectToReplaceWith = new HashMap<ObjectInstance, ObjectInstance>();
                    for (MethodInvocation getExtraMethodInvocation : dataDependencyGraph
                            .getMethodInvocationsForOwner(sinkIntent).stream()
                            .filter(mi -> mi.getMethodSignature().equals(
                                    "<android.content.Intent: android.os.Parcelable getParcelableExtra(java.lang.String)>")
                                    || mi.getMethodSignature().equals(
                                            "<android.content.Intent: java.io.Serializable getSerializableExtra(java.lang.String)>"))
                            .collect(Collectors.toList())) {

                        String key = ((PrimitiveValue) getExtraMethodInvocation.getActualParameterInstances().get(0))
                                .toString();
                        ObjectInstance toReplace = (ObjectInstance) getExtraMethodInvocation.getReturnValue();

                        logger.info("Found matching Parcelable/Serializable " + toReplace + " in sinkIntent for key "
                                + key);

                        ObjectInstance willReplace = keyAndSourceObject.get(key);
                        objectToReplaceWith.put(toReplace, willReplace);
                    }

                    // Here to the actual replacement
                    objectToReplaceWith.keySet().forEach(toReplace -> {
                        replaceEveryWhereWith(toReplace, objectToReplaceWith.get(toReplace), //
                                executionFlowGraph, dataDependencyGraph, callGraph);
                    });

                    // Now replace also the intents:
                    replaceEveryWhereWith(sinkIntent, sourceIntent, //
                            executionFlowGraph, dataDependencyGraph, callGraph);
                    // Additionally, make sure we replace the sink intents also inside the
                    // activities that require
                    // them
                    for (ObjectInstance activity : androidActivities) {
                        if (activity.requiresIntent() && activity.getIntent().equals(sinkIntents.get(taint))) {
                            logger.info("Replace sink intent " + sinkIntents.get(taint) + " with source intent "
                                    + source.getValue() + " for taint " + taint);
                            activity.setIntent(source.getValue());
                        }
                    }
                }
            }

            tantingMethodInvocations.forEach(tmi -> {
                logger.info("Getting rid of " + tmi.toFullString());
                callGraph.getMethodInvocationsSubsumedBy(tmi).forEach(smi -> {
                    dataDependencyGraph.remove(smi);
                    executionFlowGraph.remove(smi);
                });

                callGraph.remove(tmi);
                dataDependencyGraph.remove(tmi);
                executionFlowGraph.remove(tmi);
            });

        });

        return parsedTrace;
    }

    // Note: This does not require Intents to be tainted. However, if they are not,
    // they will be mocked, and mocking them breaks Robolectric
    private ParsedTrace decorateWithIntentDataDependencies(ParsedTrace parsedTrace) {
        final MethodInvocationMatcher getIntentMethodInvocationMatcher = MethodInvocationMatcher
                .byMethodLiteral("<android.app.Activity: android.content.Intent getIntent()>");

        // TODO This applies only to activity right?
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            // Find all the Android Activities (TODO Right? Or do we need more classes?)

            Set<ObjectInstance> androidActivities = new HashSet<>();
            Map<ObjectInstance, Collection<MethodInvocation>> methodInvocationsToCheck = new HashMap<>();
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

                            // Refinement. If the intent is never used besides calling getExtras() that
                            // returns null
                            // then try to something about it, otherwise, do not add this dependency

                            try {
                                DataNode intent = dataDependencyGraph.getReturnValue(subsumed_mi).get();

                                if (!isTheIntentReallyNeeded((ObjectInstance) intent, dataDependencyGraph)) {
                                    logger.info("Do not add dependencies on empty intent " + intent);
                                    continue;
                                }

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

    private boolean isTheIntentReallyNeeded(ObjectInstance intent, DataDependencyGraph dataDependencyGraph) {
        // If there's anything more than a getExtras that return null, then it is
        // relevant
        return dataDependencyGraph.getMethodInvocationsForOwner(intent).stream().filter(
                mi -> !(mi.getMethodSignature().equals("<android.content.Intent: android.os.Bundle getExtras()>")
                        && mi.getReturnValue() instanceof NullInstance))
                .count() > 0;
    }

    private ParsedTrace decorateWithAndroidMetadata(ParsedTrace parsedTrace) {
        // Go over the parsed trace and replace each method invocation with the android
        // versions of it. This must be done for all the graphs not only one, since they
        // cannot share the objects
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

                            callGraph.get(methodInvocation).getOwner().setAndroidActivity(true);
                            executionFlowGraph.get(methodInvocation).getOwner().setAndroidActivity(true);
                        } else if (isAndroidFragment(methodInvocation, callGraph)) {
                            dataDependencyGraph.getOwnerFor(methodInvocation).setAndroidFragment(true);

                            callGraph.get(methodInvocation).getOwner().setAndroidFragment(true);
                            executionFlowGraph.get(methodInvocation).getOwner().setAndroidFragment(true);
                        } else if (isAndroidService(methodInvocation, callGraph)) {
                            dataDependencyGraph.getOwnerFor(methodInvocation).setAndroidService(true);

                            callGraph.get(methodInvocation).getOwner().setAndroidService(true);
                            executionFlowGraph.get(methodInvocation).getOwner().setAndroidService(true);
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

    private boolean isAndroidService(MethodInvocation methodInvocation, CallGraph callGraph) {
        return isAndroidElement(methodInvocation, callGraph, className -> className.endsWith("Service"));
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
            return subMethods.stream()
                .anyMatch(subMethod -> isAndroidElement(subMethod, callGraph, filterPredicate));
        }
    }

}
