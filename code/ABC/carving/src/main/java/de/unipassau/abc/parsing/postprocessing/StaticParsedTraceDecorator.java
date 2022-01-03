package de.unipassau.abc.parsing.postprocessing;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.parsing.ParsedTrace;
import edu.emory.mathcs.backport.java.util.Arrays;

public class StaticParsedTraceDecorator implements ParsedTraceDecorator {

    private static Logger logger = LoggerFactory.getLogger(StaticParsedTraceDecorator.class);

    @Override
    public ParsedTrace decorate(ParsedTrace parsedTrace) {
        // Include implicit dependencies on Static Methods/Classes?
        ParsedTrace decorated = decorateWithStaticDataDependencies(parsedTrace);
        return decorated;
    }

    /**
     * Data stored in static elements (i.e., classes/fields) or returned by static
     * methods creates "implicit" dependencies. We need to expose them as far as
     * possible. So we can introduce a "STATIC" dependency every time we have an
     * invocation to anything static related to a class hence we include all its
     * (static) invocations. Any static dependency MUST be propagated to invoking
     * (caller) methods.
     * 
     * TODO Possibly use a white/black list
     * 
     * @param parsedTrace
     * @return
     */

    final static List<String> blackList = Arrays
            .asList(new String[] { "<java.lang.System: int identityHashCode(java.lang.Object)>",
                    "<java.lang.Double: java.lang.Double valueOf(double)>",
                    "<java.lang.Integer: int parseInt(java.lang.String)>",
                    "<java.util.Arrays: java.util.List asList(java.lang.Object[])>",
                    // Synthetic Methods
                    "<abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String)>",
                    // Android Logging
                    "<android.util.Log: int i(java.lang.String,java.lang.String)>",
                    });

    private ParsedTrace decorateWithStaticDataDependencies(ParsedTrace parsedTrace) {
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            // Get an handle to all the static methods.
            // We do not have to track clinit()
            List<MethodInvocation> staticMethodInvocations = executionFlowGraph.getOrderedMethodInvocations().stream()
                    .filter(mi -> mi.isStatic() && ! mi.isConstructor() && !blackList.contains(mi.getMethodSignature()))
                    .collect(Collectors.toList());

            HashMap<String, ObjectInstance> classNameToStaticDependency = new HashMap();
            for (MethodInvocation staticMethodInvocation : staticMethodInvocations) {
                // TODO Simple of FQN?
                String classNameProvidingStaticMethod = JimpleUtils
                        .getClassNameForMethod(staticMethodInvocation.getMethodSignature());
                if (!classNameToStaticDependency.containsKey(classNameProvidingStaticMethod)) {
                    // Generate the Default Dependency for this method invocation
                    // Add this implicit dependency to all the method that directly or indirectly
                    // contains the
                    // staticMethodInvocation

                    classNameToStaticDependency.put(classNameProvidingStaticMethod,
                            DataNodeFactory.getImplicitDependencyOnStaticClass(classNameProvidingStaticMethod));
                }

                ObjectInstance theDependency = classNameToStaticDependency.get(classNameProvidingStaticMethod);

                for (MethodInvocation methodThatRequireTheDependency : callGraph.getOrderedSubsumingMethodInvocationsFor(staticMethodInvocation)) {
//                    MethodInvocation methodThatRequireTheDependency = callGraph.getCallerOf(staticMethodInvocation);
                    
                    //
                    if (methodThatRequireTheDependency != null) {
                        logger.info(
                                "StaticParsedTraceDecorator.decorateWithStaticDataDependencies() ADDING STATIC DEPENDECY "
                                        + theDependency + " to " + methodThatRequireTheDependency + " via the call "
                                        + staticMethodInvocation);

                        dataDependencyGraph.addImplicitDataDependency(methodThatRequireTheDependency, theDependency);
                    } else {
                        logger.info("StaticParsedTraceDecorator. Static method " + staticMethodInvocation
                                + "is a root method invocation");
                    }
                }
            }
        });

        // TODO Start with one simple example: <abc.basiccalculator.UiStorage:
        // java.util.List<android.view.View> getElements()>
        return parsedTrace;
    }

}
