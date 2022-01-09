package de.unipassau.abc.carving.utils;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.parsing.ParsedTrace;

/**
 * 
 * Utility class that looks for specific methods
 * 
 * @author gambitemp
 *
 */
public class MethodInvocationSelector {

    public enum StrategyEnum {
        SELECT_ALL, SELECT_ONE;
    }

    /**
     * Return the list of method invocations that can be carved. Carvable method
     * invocations are non-synthetic public method calls that belong to the app. 
     * 
     * The classes must not be abstract !
     * 
     * Any method that contains $ will be ignored as well, as those are usually
     * dynamically generated or they belong to inner or anonymous classes that are not yet supported
     * 
     * For example, the static method: <abc.basiccalculator.ResultActivity:
     * android.widget.TextView
     * access$000(abc.basiccalculator.ResultActivity)>;(abc.basiccalculator.ResultActivity@107229557);
     *
     * 
     * Also, there are methods that makes no sense to carve as they are never called directly. A typical example 
     * is the constructor of any activity, as this will be ALWAYS invoked by the Android runtime.  
     * 
     * @param parsedTrace
     * @return
     */
    public Set<MethodInvocation> findAllCarvableMethodInvocations(ParsedTrace parsedTrace) {
        Set<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();

        for (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> entry : parsedTrace
                .getParsedTrace().entrySet()) {
            
            entry.getValue().getFirst().getOrderedMethodInvocations().parallelStream()
                    .filter(mi -> !mi.isPrivate() && !mi.isSynthetic() && !mi.isLibraryCall() && !mi.isAbstract())
                    .filter(mi -> !mi.getMethodSignature().contains("$")) // Inner Classes
                    .filter(mi -> !mi.getMethodSignature().contains("clinit")) // Static constructor
                    .filter(mi -> !(mi.isConstructor() && mi.getOwner().isAndroidActivity())) // Activity constructor
                    .forEachOrdered(carvableMethodInvocations::add);
        }
        
        // Additional filters
        
        
        return carvableMethodInvocations;

    }

    /**
     * Return a list of method invocations that can be carved. The list contains
     * once each signature. This is mostly to smoke test the implementation.
     * 
     * @param parsedTrace
     * @return
     */
    public Set<MethodInvocation> findUniqueCarvableMethodInvocations(ParsedTrace parsedTrace) {
        Set<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();
        Set<MethodInvocation> allCarvableMethodInvocations = findAllCarvableMethodInvocations(parsedTrace);
        // Filter by method invocation signature
        Set<String> uniqueMethodInvocationSignatures = new HashSet<String>();
        for (MethodInvocation methodInvocation : allCarvableMethodInvocations) {
            if (!uniqueMethodInvocationSignatures.contains(methodInvocation.getMethodSignature())) {
                carvableMethodInvocations.add(methodInvocation);
                uniqueMethodInvocationSignatures.add(methodInvocation.getMethodSignature());
            }
        }
        return carvableMethodInvocations;

    }

    /**
     * Return a list of method invocations that match the Method Invocation Matcher
     * 
     * @return
     */
    public Set<MethodInvocation> findByMethodInvocationMatcher(ParsedTrace parsedTrace,
            MethodInvocationMatcher matcInvocationMatcher) {
        Set<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();
        Set<MethodInvocation> allCarvableMethodInvocations = findAllCarvableMethodInvocations(parsedTrace);
        for (MethodInvocation methodInvocation : allCarvableMethodInvocations) {
            if (matcInvocationMatcher.matches(methodInvocation)) {
                carvableMethodInvocations.add(methodInvocation);
            }
        }
        return carvableMethodInvocations;

    }
}
