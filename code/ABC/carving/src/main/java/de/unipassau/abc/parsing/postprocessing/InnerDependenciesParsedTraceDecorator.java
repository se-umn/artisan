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

/**
 * This decorator makes sure we expose (implicit) data dependencies through
 * class fields (static?). So if method foo access the field this.x, then foo
 * must depends on x; hence, all the methods invoked on it must be included.
 * 
 * TODO In reality, only the write to it should be included and not all the writes !!!
 * 
 * @author gambitemp
 *
 */
public class InnerDependenciesParsedTraceDecorator implements ParsedTraceDecorator {

    private static Logger logger = LoggerFactory.getLogger(InnerDependenciesParsedTraceDecorator.class);

    @Override
    public ParsedTrace decorate(ParsedTrace parsedTrace) {
        // Include implicit dependencies on Static Methods/Classes?
        ParsedTrace decorated = decorateWithFieldDataDependencies(parsedTrace);
        return decorated;
    }

    private ParsedTrace decorateWithFieldDataDependencies(ParsedTrace parsedTrace) {
        return parsedTrace;
//        // Look in each method for field accesses 
//        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
//            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
//            CallGraph callGraph = graphs.getThird();
//            DataDependencyGraph dataDependencyGraph = graphs.getSecond();
//
//            // <abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String)>
//            List<MethodInvocation> fieldAccessesInvocations = executionFlowGraph.getOrderedMethodInvocations().stream()
//                    .filter(mi -> mi.isSynthetic() && mi.getMethodSignature().equals(FIELD_ACCESS_SIGNATURE)));
//                    .collect(Collectors.toList());
//
//            HashMap<String, ObjectInstance> classNameToStaticDependency = new HashMap();
//            for (MethodInvocation staticMethodInvocation : staticMethodInvocations) {
//                // TODO Simple of FQN?
//                String classNameProvidingStaticMethod = JimpleUtils
//                        .getClassNameForMethod(staticMethodInvocation.getMethodSignature());
//                if (!classNameToStaticDependency.containsKey(classNameProvidingStaticMethod)) {
//                    // Generate the Default Dependency for this method invocation
//                    // Add this implicit dependency to all the method that directly or indirectly
//                    // contains the
//                    // staticMethodInvocation
//
//                    classNameToStaticDependency.put(classNameProvidingStaticMethod,
//                            DataNodeFactory.getImplicitDependencyOnStaticClass(classNameProvidingStaticMethod));
//                }
//
//                ObjectInstance theDependency = classNameToStaticDependency.get(classNameProvidingStaticMethod);
//
//                for (MethodInvocation methodThatRequireTheDependency : callGraph
//                        .getOrderedSubsumingMethodInvocationsFor(staticMethodInvocation)) {
////                    MethodInvocation methodThatRequireTheDependency = callGraph.getCallerOf(staticMethodInvocation);
//
//                    //
//                    if (methodThatRequireTheDependency != null) {
//                        logger.info(
//                                "StaticParsedTraceDecorator.decorateWithStaticDataDependencies() ADDING STATIC DEPENDECY "
//                                        + theDependency + " to " + methodThatRequireTheDependency + " via the call "
//                                        + staticMethodInvocation);
//
//                        dataDependencyGraph.addImplicitDataDependency(methodThatRequireTheDependency, theDependency);
//                    } else {
//                        logger.info("StaticParsedTraceDecorator. Static method " + staticMethodInvocation
//                                + "is a root method invocation");
//                    }
//                }
//            }
//        });
//
//        // TODO Start with one simple example: <abc.basiccalculator.UiStorage:
//        // java.util.List<android.view.View> getElements()>
//        return parsedTrace;
    }

}
