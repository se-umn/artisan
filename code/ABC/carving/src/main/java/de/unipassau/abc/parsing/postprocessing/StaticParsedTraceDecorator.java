package de.unipassau.abc.parsing.postprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.EnumConstant;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.parsing.ParsedTrace;
import edu.emory.mathcs.backport.java.util.Arrays;

public class StaticParsedTraceDecorator implements ParsedTraceDecorator {

    private static Logger logger = LoggerFactory.getLogger(StaticParsedTraceDecorator.class);

    private boolean useOnlyOwnDependencies = false;
    private String packageName = null;

    public StaticParsedTraceDecorator() {
        this(false, null);
    }

    public StaticParsedTraceDecorator(boolean useOnlyOwnDependencies, String packageName) {
        this.useOnlyOwnDependencies = useOnlyOwnDependencies;
        this.packageName = packageName;
    }

    @Override
    public ParsedTrace decorate(ParsedTrace parsedTrace) {
        // Promote EnumConstants - We do this BEFORE computing the static deps
        parsedTrace = convertEnumConstantsAndCleanUpTrace(parsedTrace);
        // Include implicit dependencies on Static Methods/Classes.
        // TODO Under study: Limit to one applications's static dependencies
        parsedTrace = decorateWithStaticDataDependencies(parsedTrace);
        // Assign types to Anonym Classes
        parsedTrace = resolveFormalTypesOfAnonymClasses(parsedTrace);
        // Remove all the static constructors !
        parsedTrace = removeStaticConstructors(parsedTrace);

//        
        return parsedTrace;
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

    final static List<String> blackList = Arrays.asList(new String[] { //
            "<java.lang.System: int identityHashCode(java.lang.Object)>",
            // Boxed Type
            "<java.lang.Double: java.lang.Double valueOf(double)>",
            //
            "<java.lang.Integer: int parseInt(java.lang.String)>",
            "<java.lang.Integer: java.lang.Integer valueOf(int)>",
            //
            "<java.lang.Boolean: java.lang.Boolean valueOf(boolean)>",
            // Util Method
            "<java.util.Arrays: java.util.List asList(java.lang.Object[])>",
            // Synthetic Methods
            "<abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String,java.lang.String)>",
            "<abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String)>", // Backward
                                                                                                     // compatibility
                                                                                                     // with old traces
            // Android Logging
            "<android.util.Log: int i(java.lang.String,java.lang.String)>",
            "<android.util.Log: int d(java.lang.String,java.lang.String)>",
            // Not sure about this one...
            "<android.view.LayoutInflater: android.view.LayoutInflater 'from'(android.content.Context)>",
            //
            "<android.net.Uri: android.net.Uri parse(java.lang.String)>",
            // org.apache.commons.lang3.Validate Validate methods
//            "<org.apache.commons.lang3.Validate: java.lang.Object notNull(java.lang.Object,java.lang.String,java.lang.Object[])>",
//            "<org.apache.commons.lang3.Validate: java.lang.Object notNull(java.lang.Object)>"
            //
            // <com.chikeandroid.debtmanager.features.home.MainActivity$$Lambda$1:
            // android.view.View$OnClickListener
            // lambdaFactory$(com.chikeandroid.debtmanager.features.home.MainActivity)>_81_161
    });

    // Some generated method calls must be filtered using a different strategy
    final static Predicate<MethodInvocation> filterLamdaGenerators = mi -> !mi.getMethodSignature()
            .contains("lambdaFactory$");
    final static Predicate<MethodInvocation> filterLamdas= mi -> !mi.getMethodSignature()
            .contains("lambda$");
    
    // This is only to debug whether static deps create issues
    final static Predicate<MethodInvocation> filterAllForDebug = mi -> false;
    
    
    
    final static Predicate<MethodInvocation> filterDaggerPreconditions = mi -> !mi.getMethodSignature()
            .contains("<dagger.internal.Preconditions");
    final static Predicate<MethodInvocation> filterLang3Validate = mi -> !mi.getMethodSignature()
            .contains("<org.apache.commons.lang3.Validate");
    final static Predicate<MethodInvocation> filterAndroidLog = mi -> !mi.getMethodSignature()
            .contains("<android.util.Log");
// 
    // Build the actual predicate to filter static dependencies
    final static List<Predicate<MethodInvocation>> allPredicates = new ArrayList<Predicate<MethodInvocation>>();
    {
        // Enable this to avoid all static deps
//        allPredicates.add(filterAllForDebug);
        
        // https://www.baeldung.com/java-predicate-chain
        allPredicates.add(filterLamdaGenerators);
        allPredicates.add(filterLamdas);
        allPredicates.add(filterDaggerPreconditions);
        allPredicates.add(filterLang3Validate);
        //
        allPredicates.add(mi -> mi.isStatic());
        allPredicates.add(mi -> !mi.isConstructor());
        allPredicates.add(mi -> !blackList.contains(mi.getMethodSignature()));

    }

    // Look for code that clint + show the EnumConstant and promote it at the
    // beginning of the trace. Then remove all the calls to <abc.Enum: void
    // processingEnum()>
    //
    private ParsedTrace convertEnumConstantsAndCleanUpTrace(ParsedTrace parsedTrace) {
        logger.info("* Pre-process Enumeration Constants ");
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            final ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            final CallGraph callGraph = graphs.getThird();
            final DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            List<MethodInvocation> enumConstantInvocations = executionFlowGraph.getOrderedMethodInvocations().stream()
                    .filter(mi -> mi.isStatic() && mi.getMethodSignature()
                            .equals("<abc.Enum: java.lang.Object syntheticEnumConstantGetter(java.lang.String)>"))
                    .collect(Collectors.toList());

            // Create the enumConstants
            Set<ObjectInstance> originalEnumConstants = enumConstantInvocations.stream()
                    .map(mi -> (ObjectInstance) mi.getReturnValue()).collect(Collectors.toSet());

            Set<ObjectInstance> enumConstants = enumConstantInvocations.stream()
                    .map(mi -> new EnumConstant(((ObjectInstance) mi.getReturnValue()).getObjectId(),
                            // Somehow there's no way to return the content of the string without the
                            // quotation...
                            mi.getActualParameterInstances().get(0).toString().replaceAll("\"", "")

                    )).collect(Collectors.toSet());

//            logger.info("Enum Constants:");
//            enumConstants.forEach(ec -> {
//                logger.info("- " + ec);
//            });

            // Clean up the trace by removing those synthetic method invocations
            enumConstantInvocations.forEach(mi -> {
                executionFlowGraph.remove(mi);
                callGraph.remove(mi);
                dataDependencyGraph.remove(mi);
            });

            // Replace every occurrence of every enumConstant in the data graph and the
            // method invocations
            originalEnumConstants.forEach(oec -> {
                dataDependencyGraph.getMethodInvocationsForOwner(oec).forEach(mi -> {
                    // IF this fails here, there's a big issue somewhere !
                    ObjectInstance enumConstant = enumConstants.stream()
                            .filter(ec -> ec.getObjectId() == oec.getObjectId()).findFirst().get();
                    mi.setOwner(enumConstant);
                    //
                    dataDependencyGraph.replaceDataDependencyOnOwner(mi, enumConstant);
                    logger.info("Casting " + oec + " as EnumConstant in " + mi);
                });
                dataDependencyGraph.getMethodInvocationsWhichUse(oec).forEach(mi -> {
                    // IF this fails here, there's a big issue somewhere !
                    ObjectInstance enumConstant = enumConstants.stream()
                            .filter(ec -> ec.getObjectId() == oec.getObjectId()).findFirst().get();

                    // Replace the parameter in place
                    for (int i = 0; i < mi.getActualParameterInstances().size(); i++) {
                        if (mi.getActualParameterInstances().get(i).equals(oec)) {
                            // TODO
                            mi.getActualParameterInstances().set(i, enumConstant);
                            dataDependencyGraph.replaceDataDependencyOnActualParameter(mi, enumConstant, i);
                            logger.info("Casting " + oec + " as EnumConstant in " + mi);
                        }
                    }
                });
                dataDependencyGraph.getMethodInvocationsWhichReturn(oec).forEach(mi -> {
                    ObjectInstance enumConstant = enumConstants.stream()
                            .filter(ec -> ec.getObjectId() == oec.getObjectId()).findFirst().get();
                    mi.setReturnValue(enumConstant);
                    //
                    dataDependencyGraph.replaceDataDependencyOnReturn(mi, enumConstant);
                    //
                    logger.info("Casting " + oec + " as EnumConstant in " + mi);
                });
            });

            List<MethodInvocation> callsToProcessingEnum = executionFlowGraph.getOrderedMethodInvocations().stream()
                    .filter(mi -> mi.isStatic() && mi.getMethodSignature().equals("<abc.Enum: void processingEnum()>"))
                    .collect(Collectors.toList());

            // Cleanup the trace by removing the calls to <abc.Enum: void processingEnum()>
            // and all the methods they subsume
            callsToProcessingEnum.forEach(mi -> {
                // Get all the subsumed methods and remove them
                callGraph.getMethodInvocationsSubsumedBy(mi).forEach(smi -> {
                    logger.info("Getting rid of " + smi + " as subsumed by " + smi);
                    executionFlowGraph.remove(smi);
                    dataDependencyGraph.remove(smi);
                });
                // Finally remove the calls themselves
                logger.info("Getting rid of " + mi);
                callGraph.remove(mi);
                executionFlowGraph.remove(mi);
                dataDependencyGraph.remove(mi);
            });

        });

        return parsedTrace;
    }

    // removeStaticConstructors
    private ParsedTrace removeStaticConstructors(ParsedTrace parsedTrace) {
        logger.info("* Remove Static Constructors (clinit) ");
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            final ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            final CallGraph callGraph = graphs.getThird();
            final DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            List<MethodInvocation> staticConstructorsInvocations = executionFlowGraph.getOrderedMethodInvocations()
                    .stream().filter(mi -> mi.isStatic() && mi.isConstructor()).collect(Collectors.toList());

            // Remove all of them
            staticConstructorsInvocations.forEach(scmi -> {
                logger.info("Removing Static Constructor " + scmi);
                callGraph.getMethodInvocationsSubsumedBy(scmi).forEach(subsumedScmi -> {
                    callGraph.remove(subsumedScmi);
                    dataDependencyGraph.remove(subsumedScmi);
                    executionFlowGraph.remove(subsumedScmi);
                    logger.info("  - Removing " + subsumedScmi);
                });
                callGraph.remove(scmi);
                dataDependencyGraph.remove(scmi);
                executionFlowGraph.remove(scmi);
            });

        });
        return parsedTrace;
    }

    /**
     * Use various heuristic to identify anony and inner types and update them
     * accordingly
     * 
     * @param parsedTrace
     * @return
     */
    final static String ANONYM_CLASS_TYPE_PATTERN = ".*\\$[0-9]";

    final static Map<String, Set<String>> KNOWN_TYPES = new HashMap<String, Set<String>>();
    {
        // In theory this information can be provided as input or discovered statically
        // while instrumenting the code

        Set<String> types = new HashSet<String>(Arrays.asList(new String[] { "android.os.Parcelable.Creator" }));
        KNOWN_TYPES.put("de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign$1", types);

    }

    private ParsedTrace resolveFormalTypesOfAnonymClasses(ParsedTrace parsedTrace) {
        logger.info("* Resolve types of Anonym Classes");
        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {

            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            final Map<String, Set<String>> resolvedTypes = new HashMap<String, Set<String>>();
            resolvedTypes.putAll(KNOWN_TYPES);

            dataDependencyGraph.getObjectInstances().forEach(oi -> {

                String typeToResolve = oi.getType();
                if (typeToResolve.matches(ANONYM_CLASS_TYPE_PATTERN) && !resolvedTypes.containsKey(typeToResolve)) {

                    logger.info(typeToResolve + " requires type resolution ");

                    // Check whether this anonym type corresponds to a (Static) Field?
                    Set<String> resolvedTypesFromFieldSetters = executionFlowGraph.getOrderedMethodInvocations()
                            .stream().filter(
                                    //
                                    mi -> mi.isStatic() //
                                            // The last parameter is a Type
                                            && mi.getMethodSignature().equals(
                                                    "<abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String,java.lang.String)>")
                    // This is how we "set" fields
                                            && mi.getReturnValue().getType().equals(typeToResolve)
                    // Extract the type information - be sure to remove the " indicating this is a
                    // String
                    ).map(mi -> mi.getActualParameterInstances().get(2).toString().replace("$", ".").replaceAll("\"",
                            "")).collect(Collectors.toSet());
                    // and replace everywhere the reference to whatever type is the anonym with the
                    // one assigned in the field
                    // we assume ONLY one type is present (the same object is not assigned multiple
                    // times with different types!)

                    logger.info("Resolved types as Field: " + resolvedTypesFromFieldSetters);

                    Set<String> resolvedTypesAsParameter = executionFlowGraph.getOrderedMethodInvocations().stream()
                            .filter(
                                    // Has this object instance ever been used as a parameter
                                    mi -> mi.getActualParameterInstances().contains(oi))
                            .map(
                                    //
                                    mi -> {
                                        int positionAsParameter = mi.getActualParameterInstances().indexOf(oi);
                                        String formalType = JimpleUtils
                                                .getParameterList(mi.getMethodSignature())[positionAsParameter]
                                                        .replace("$", ".");
                                        return formalType;
                                    }

                            ).collect(Collectors.toSet());
                    logger.info("Resolved types as Parameter: " + resolvedTypesAsParameter);

                    Set<String> resolvedTypesForInstace = new HashSet<>();
                    resolvedTypesForInstace.addAll(resolvedTypesFromFieldSetters);
                    resolvedTypesForInstace.addAll(resolvedTypesAsParameter);
                    resolvedTypes.put(oi.getType(), resolvedTypesForInstace);
                }
            });

            // What shall I do with this information?
            executionFlowGraph.getOrderedMethodInvocations().forEach(mi -> {

                if (mi.getOwner() != null) {
                    mi.getOwner().setResolvedTypes(resolvedTypes.get(mi.getOwner().getType()));
                }
                if (mi.getReturnValue() != null && mi.getReturnValue() instanceof ObjectInstance) {
                    ((ObjectInstance) mi.getReturnValue())
                            .setResolvedTypes(resolvedTypes.get(mi.getReturnValue().getType()));
                }
                for (ObjectInstance param : mi.getActualParameterInstances().stream()
                        .filter(dn -> dn instanceof ObjectInstance).map(dn -> (ObjectInstance) dn)
                        .collect(Collectors.toList())) {

                    param.setResolvedTypes(resolvedTypes.get(param.getType()));
                }

            });
            callGraph.getAllMethodInvocations().forEach(mi -> {
                if (mi.getOwner() != null) {
                    mi.getOwner().setResolvedTypes(resolvedTypes.get(mi.getOwner().getType()));
                }
                if (mi.getReturnValue() != null && mi.getReturnValue() instanceof ObjectInstance) {
                    ((ObjectInstance) mi.getReturnValue())
                            .setResolvedTypes(resolvedTypes.get(mi.getReturnValue().getType()));
                }
                for (ObjectInstance param : mi.getActualParameterInstances().stream()
                        .filter(dn -> dn instanceof ObjectInstance).map(dn -> (ObjectInstance) dn)
                        .collect(Collectors.toList())) {

                    param.setResolvedTypes(resolvedTypes.get(param.getType()));
                }
            });
            dataDependencyGraph.getObjectInstances()
                    .forEach(oi -> oi.setResolvedTypes(resolvedTypes.get(oi.getType())));

        });

        return parsedTrace;
    }

    private ParsedTrace decorateWithStaticDataDependencies(ParsedTrace parsedTrace) {
        logger.info("* Decorate with static dependencies");
        if (this.useOnlyOwnDependencies) {
            // Make sure we ALSO filter by this predicate
            logger.info("Considers ONLY static dependencies defined in " + this.packageName);
            allPredicates.add(mi -> JimpleUtils.getPackage(mi.getMethodSignature()).equals(this.packageName));
        }

        parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
            ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
            CallGraph callGraph = graphs.getThird();
            DataDependencyGraph dataDependencyGraph = graphs.getSecond();

            // Get an handle to all the static methods that are NOT black listed (e.g.,
            // clinit())
            List<MethodInvocation> staticMethodInvocations = executionFlowGraph.getOrderedMethodInvocations().stream()
                    // Make sure we evaluate all the configured predicates
                    .filter(allPredicates.stream().reduce(x -> true, Predicate::and))//
                    .collect(Collectors.toList());

            HashMap<String, ObjectInstance> classNameToStaticDependency = new HashMap();
            for (MethodInvocation staticMethodInvocation : staticMethodInvocations) {

                logger.info("- Processing static method invocation: " + staticMethodInvocation);

                String classNameProvidingStaticMethod = JimpleUtils
                        .getClassNameForMethod(staticMethodInvocation.getMethodSignature());

                logger.info("- provided by : " + classNameProvidingStaticMethod);

                if (!classNameToStaticDependency.containsKey(classNameProvidingStaticMethod)) {
                    // Generate the Default Dependency for this method invocation
                    // Add this implicit dependency to all the method that directly or indirectly
                    // contains the staticMethodInvocation

                    classNameToStaticDependency.put(classNameProvidingStaticMethod,
                            DataNodeFactory.getImplicitDependencyOnStaticClass(classNameProvidingStaticMethod));
                }

                ObjectInstance theStaticDependency = classNameToStaticDependency.get(classNameProvidingStaticMethod);

                for (MethodInvocation methodThatRequireTheDependency : callGraph
                        .getOrderedSubsumingMethodInvocationsFor(staticMethodInvocation)) {
//                    MethodInvocation methodThatRequireTheDependency = callGraph.getCallerOf(staticMethodInvocation);

                    //
                    if (methodThatRequireTheDependency != null) {
                        logger.info("Adding static dependency: " + theStaticDependency + " to "
                                + methodThatRequireTheDependency + " via the call " + staticMethodInvocation);

                        dataDependencyGraph.addImplicitDataDependency(methodThatRequireTheDependency,
                                theStaticDependency);
                    } else {
                        logger.info("Static method " + staticMethodInvocation + "is a root method invocation");
                    }
                }
            }
        });

        // TODO Start with one simple example: <abc.basiccalculator.UiStorage:
        // java.util.List<android.view.View> getElements()>
        return parsedTrace;
    }

}
