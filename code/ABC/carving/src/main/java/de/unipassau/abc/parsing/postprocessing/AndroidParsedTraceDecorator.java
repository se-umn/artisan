package de.unipassau.abc.parsing.postprocessing;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;

public class AndroidParsedTraceDecorator implements ParsedTraceDecorator {

    @Override
    public ParsedTrace decorate(ParsedTrace parsedTrace) {
        // Include additional metadata about Android invocations
        ParsedTrace decorated = decorateWithAndroidMetadata(parsedTrace);
        // Include implicit data dependencies that are Android-specific
        decorated = decorateWithIntentDataDependencies(parsedTrace);
        //
        return decorated;
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
            Set<MethodInvocation> methodInvocationsToCheck = new HashSet<MethodInvocation>();
            for (ObjectInstance obj : dataDependencyGraph.getObjectInstances()) {
                if (obj.isAndroidActivity()) {
                    if (!androidActivities.contains(obj)) {
                        System.out.println("Found New Activity " + obj);
                        // Add all its method invocations
                        methodInvocationsToCheck.addAll(dataDependencyGraph.getMethodInvocationsForOwner(obj));
                    }

                }
            }
            for (MethodInvocation mi : methodInvocationsToCheck) {
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
                            System.out.println(
                                    "AndroidParsedTraceDecorator.decorateWithIntentDataDependencies(): Adding implicit dep between "
                                            + methodThatRequireTheDependencyOnTheIntent + " and " + intent);
                        } catch (ABCException e) {
                            // TODO Auto-generated catch block
//                            e.printStackTrace();
                            throw new RuntimeException(e);

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
