package de.unipassau.abc.carving.utils;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
        SELECT_ALL, SELECT_ONE, SELECT_ACTIVITIES_ALL, SELECT_ACTIVITIES_ONE;

        /**
         * Creates a new (possibly) stateful predicate based on the given strategy.
         *
         * @return predicate to comply with this strategy
         */
        Predicate<MethodInvocation> predicate() {
            switch (this) {
                case SELECT_ALL:
                    return e -> true;
                case SELECT_ONE:
                    return selectOne();
                case SELECT_ACTIVITIES_ALL:
                    return selectActivitiesAll();
                case SELECT_ACTIVITIES_ONE:
                    return selectActivitiesOne();
                default:
                    throw new RuntimeException("Not implemented");
            }
        }
    }

    /**
     * Selects only those method invocations with previously unseen method signature.
     * This is mostly to smoke test the implementation.
     *
     * @return predicate to select unique method invocations
     */
    private static Predicate<MethodInvocation> selectOne() {
        Set<String> uniqueMethodInvocationSignatures = new HashSet<>();
        return m -> uniqueMethodInvocationSignatures.add(m.getMethodSignature());
    }

    /**
     * Selects all method invocations that belong to android activities.
     *
     * @return predicate to select all android activity method invocations
     */
    private static Predicate<MethodInvocation> selectActivitiesAll() {
        return m -> m.getOwner().isAndroidActivity();
    }

    /**
     * Selects only those method invocations that belong to android activities and
     * have previously unseen method signature.
     *
     * @return predicate to select unique android activity method invocations
     */
    private static Predicate<MethodInvocation> selectActivitiesOne() {
        return selectActivitiesAll().and(selectOne());
    }

    /**
     * Return the list of method invocations that can be carved. Carvable method
     * invocations are non-synthetic public method calls that belong to the app.
     * 
     * The classes must not be abstract !
     * 
     * Any method that contains $ will be ignored as well, as those are usually
     * dynamically generated or they belong to inner or anonymous classes that are
     * not yet supported
     * 
     * For example, the static method: <abc.basiccalculator.ResultActivity:
     * android.widget.TextView
     * access$000(abc.basiccalculator.ResultActivity)>;(abc.basiccalculator.ResultActivity@107229557);
     *
     * 
     * Also, there are methods that makes no sense to carve as they are never called
     * directly. A typical example is the constructor of any activity, as this will
     * be ALWAYS invoked by the Android runtime.
     * 
     * @param parsedTrace
     * @return
     */
    public Set<MethodInvocation> findCarvableMethodInvocations(ParsedTrace parsedTrace) {
        return findCarvableMethodInvocations(parsedTrace, StrategyEnum.SELECT_ALL);
    }

    public Set<MethodInvocation> findCarvableMethodInvocations(ParsedTrace parsedTrace,
        StrategyEnum strategy) {
        Set<MethodInvocation> carvableMethodInvocations = new HashSet<>();

        for (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> entry : parsedTrace
            .getParsedTrace().entrySet()) {

            carvableMethodInvocations.addAll(entry.getValue().getFirst().getOrderedMethodInvocations());
        }

        // Additional filters

        return filterCarvableMethodInvocations(parsedTrace, carvableMethodInvocations, strategy.predicate());
    }

    /**
     * Apply the filters to remove method invocations that are not carvable
     * 
     * @param selectedCarvableMethodInvocation
     * @return
     */
    public Set<MethodInvocation> filterCarvableMethodInvocations(ParsedTrace parsedTrace,
            Set<MethodInvocation> selectedCarvableMethodInvocation,
        Predicate<MethodInvocation> filterStrategy) {

        // Apply Basic Filters
        Set<MethodInvocation> filteredSelectedCarvableMethodInvocations = selectedCarvableMethodInvocation.stream()
                .filter(mi -> !mi.isPrivate() && !mi.isSynthetic() && !mi.isLibraryCall() && !mi.isAbstract())
                .filter(mi -> !mi.getMethodSignature().contains("$")) // (static) methods that belong to inner/anonym
                                                                      // classes
                .filter(mi -> !mi.getMethodSignature().contains("clinit")) // Static constructor
                .filter(mi -> !(mi.isConstructor() && mi.getOwner().isAndroidActivity())) // Activity constructor
                .filter(mi -> !mi.isStatic() && !mi.getOwner().getType().contains("$")) // Methods that belong to
                                                                                        // private/inner/anonym
                                                                                        // classe
                .filter(mi -> !mi.isStatic() && !mi.getOwner().isAndroidFragment()) // Anything that belongs to a
                                                                                    // Fragment cannot be tested
                .filter(filterStrategy)  // Strategy filtering
                .collect(Collectors.toSet());

        // Apply Complex Filters

        // We cannot carve targets that have as Owner an object that is not constructed
        // in the trace itself. This violates the basic property that we can observe
        // what we need to carve
        // TODO This will not work for multi-threaded app
        final DataDependencyGraph ddg = parsedTrace.getUIThreadParsedTrace().getSecond();

        filteredSelectedCarvableMethodInvocations
                .removeIf(mi -> !mi.isStatic() && ddg.getMethodInvocationsForOwner(mi.getOwner()).stream()
                        .filter(anotherMi -> anotherMi.isConstructor()).count() == 0);

        return filteredSelectedCarvableMethodInvocations;

    }

    /**
     * Return a list of method invocations that can be carved. The list contains
     * once each signature. This is mostly to smoke test the implementation.
     * 
     * @param parsedTrace
     * @return
     */
    public Set<MethodInvocation> findUniqueCarvableMethodInvocations(ParsedTrace parsedTrace) {
        //Set<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();

        // Select all the valid ones
        Set<MethodInvocation> allCarvableMethodInvocations = findCarvableMethodInvocations(parsedTrace);
        return allCarvableMethodInvocations.stream().filter(selectOne()).collect(Collectors.toSet());
    }

    /**
     * Return a list of method invocations that match the Method Invocation Matcher
     * 
     * @return
     */
    public Set<MethodInvocation> findByMethodInvocationMatcher(ParsedTrace parsedTrace,
            MethodInvocationMatcher matcInvocationMatcher) {
        Set<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();
        // Select all the valid ones
        Set<MethodInvocation> allCarvableMethodInvocations = findCarvableMethodInvocations(parsedTrace);
        for (MethodInvocation methodInvocation : allCarvableMethodInvocations) {
            if (matcInvocationMatcher.matches(methodInvocation)) {
                carvableMethodInvocations.add(methodInvocation);
            }
        }
        return carvableMethodInvocations;

    }
}
