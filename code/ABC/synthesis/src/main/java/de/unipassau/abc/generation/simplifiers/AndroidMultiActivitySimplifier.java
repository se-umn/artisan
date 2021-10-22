package de.unipassau.abc.generation.simplifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.exceptions.ABCException;

public class AndroidMultiActivitySimplifier implements CarvedExecutionSimplifier {

    private static Logger logger = LoggerFactory.getLogger(AndroidMultiActivitySimplifier.class);

    @Override
    public CarvedExecution simplify(CarvedExecution carvedExecution) throws CarvingException, ABCException {

        // Remove Lambdas and the like no matter what
        CarvedExecution simplifiedCarvedExecution = removeLambdas(carvedExecution);

        // This simplifications applies ONLY if the test requires two or more activities at once
        while (countActivities(simplifiedCarvedExecution) >= 2) {

            // Find the methods that ORIGINALLY resulted in bringing in all the dependencies
            // of the (prev) activity
            // Heuristic: the last method on this activity
            // TODO Do we need to limit to necessaryOnly ?
            List<MethodInvocation> necessaryMethodInvocations = new ArrayList<MethodInvocation>();
            for (ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs) {
                necessaryMethodInvocations.addAll(executionFlowGraph.getOrderedMethodInvocations().stream()
                        .filter(mi -> mi.isNecessary()).collect(Collectors.toList()));
            }

            Collections.sort(necessaryMethodInvocations);
            Collections.reverse(necessaryMethodInvocations);

            // For each method invocation in the carved test, starting from the MUT we need
            // to find the method invocations that link
            // the second (and the other activities) as those are the deps we need to CUT

            List<MethodInvocation> problematicDependencies = findProblematicDependencyChain(necessaryMethodInvocations,
                    carvedExecution);

            // TODO This is tricky as the dep can be trigger at any point, while we should
            // consistently pick the one which have larger ID?
            //
            Collections.sort(problematicDependencies);
            Collections.reverse(problematicDependencies);
            // Take the one which has higher ID, meaning that "temporally" is closer to MUT
            MethodInvocation problematicMethodInvocation = problematicDependencies.iterator().next();
            //
            logger.info("Selected Problematic invocation:" + problematicMethodInvocation);

            simplifiedCarvedExecution = getRidOfProblematicMethodInvocation(carvedExecution, problematicMethodInvocation);

        }
        return simplifiedCarvedExecution;
    }

    private CarvedExecution getRidOfProblematicMethodInvocation(CarvedExecution carvedExecution,
            MethodInvocation problematicMethodInvocation) throws CarvingException, ABCException {
        /*
         * TODO I assume that the problematic method invocation generates, i.e.,
         * returns, the required data deps. So we try to replace the method invocation
         * with its content, CUT ALL DEPS TO THE PREVIOUS ACTIVITY (?), and recarve the
         * trace.
         */

        // Get all the method invocations that make up the problematicMethodInvocation
        CallGraph callGraph = carvedExecution.getCallGraphContainingTheMethodInvocation(problematicMethodInvocation);
        List<MethodInvocation> methodsSubsumedByProblematicMethodInvocation = new ArrayList(
                callGraph.getMethodInvocationsSubsumedBy(problematicMethodInvocation));
        Collections.sort(methodsSubsumedByProblematicMethodInvocation);

        logger.info("Subsumed methods of problematic invocation:");
        methodsSubsumedByProblematicMethodInvocation.stream().map(m -> m.toString()).forEach(logger::info);

        // Now go over them and try to identify all the "noisy" Android specific deps
        // (e.g., field setters/getters)

        Set<MethodInvocation> methodInvocationsToRemove = new HashSet<MethodInvocation>();

        logger.info("TODO TODO TODO Looking for Noisy dependencies to remove:");
        for (MethodInvocation mi : methodsSubsumedByProblematicMethodInvocation) {
            // TODO
        }

        // Remove whatever deps we found
        for (MethodInvocation miToRemove : methodInvocationsToRemove) {
            carvedExecution.remove(miToRemove);
        }
        // Finally replace the original problematicMethodInvocation with its
        // "new/simplified" body
        carvedExecution.getDataDependencyGraphContainingTheMethodInvocation(problematicMethodInvocation)
                .remove(problematicMethodInvocation);
        carvedExecution.getExecutionFlowGraphGraphContainingTheMethodInvocation(problematicMethodInvocation)
                .remove(problematicMethodInvocation);
        carvedExecution.getCallGraphContainingTheMethodInvocation(problematicMethodInvocation)
                .replaceMethodInvocationWithExecution(problematicMethodInvocation);

        // At this point, we can RE-CARVE the simplified execution: if we indeed removed
        // the problematic dependencies, carving will be able to get rid of everything
        // else
        carvedExecution.callGraphs.stream()
                .forEach(cg -> cg.getAllMethodInvocations().stream().forEach(mi -> mi.setNecessary(false)));
        carvedExecution.executionFlowGraphs.stream()
                .forEach(efg -> efg.getOrderedMethodInvocations().stream().forEach(mi -> mi.setNecessary(false)));
        carvedExecution.dataDependencyGraphs.stream()
                .forEach(ddg -> ddg.getAllMethodInvocations().stream().forEach(mi -> mi.setNecessary(false)));

        ///
        logger.info("RECARVING SIMPLIFIED CARVED EXECUTION");
        BasicCarver carver = new BasicCarver(carvedExecution);
        List<CarvedExecution> reCarvedExecution = carver.carve(carvedExecution.methodInvocationUnderTest);

        return reCarvedExecution.iterator().next();
    }

    // TODO Very poor API here...
    private CarvedExecution removeLambdas(CarvedExecution carvedExecution) {
        carvedExecution.callGraphs.forEach(callGraph -> {
            logger.info("Removing Lambdas from " + callGraph);
            Set<MethodInvocation> methodInvocationsToRemove = new HashSet<MethodInvocation>();
            for (MethodInvocation mi : callGraph.getAllMethodInvocations()) {
                if (JimpleUtils.getClassNameForMethod(mi.getMethodSignature()).contains("$Lambda$")) {
                    System.out.println("AndroidMultiActivitySimplifier.simplify() Removing " + mi + " has type Lambda "
                            + JimpleUtils.getClassNameForMethod(mi.getMethodSignature()));
                    // Note that this removes the lambda and all its content! This should be ok, as
                    // lambda will be executed only later so they do not matter now
                    // DOES NOTHING IF MI IS NOT THERE ANYMORE
                    methodInvocationsToRemove.add(mi);
                    methodInvocationsToRemove.addAll(callGraph.getMethodInvocationsSubsumedBy(mi));
                }

                if ((int) mi.getActualParameterInstances().stream().filter(dn -> dn.getType().contains("$Lambda$"))
                        .count() > 0) {
                    System.out.println("AndroidMultiActivitySimplifier.simplify() Removing " + mi
                            + " has it depends on a Lambda as parameter " + mi.getActualParameterInstances());
                    // Note that this removes the lambda and all its content! This should be ok, as
                    // lambda will be executed only later so they do not matter now
                    // DOES NOTHING IF MI IS NOT THERE ANYMORE
                    methodInvocationsToRemove.add(mi);
                    methodInvocationsToRemove.addAll(callGraph.getMethodInvocationsSubsumedBy(mi));
                }
            }

            // TODO Probably we should encapsulate this logic into the CarvedExecution
            // Object to consistently act on all the datastructures at once

            for (MethodInvocation miToRemove : methodInvocationsToRemove) {
                carvedExecution.remove(miToRemove);
            }
        });
        
        return carvedExecution;
    }

    private List<MethodInvocation> findProblematicDependencyChain(List<MethodInvocation> necessaryMethodInvocations,
            CarvedExecution carvedExecution) {
        List<MethodInvocation> problematicDependencies = new ArrayList<MethodInvocation>();
        Set<ObjectInstance> involvedActivities = new HashSet<ObjectInstance>();
        Set<MethodInvocation> cache = new HashSet<MethodInvocation>();

        // Find problematic dependency chain
        for (MethodInvocation mi : necessaryMethodInvocations) {

//            relevantDataDependencyGraph
            onWhichActivityDepends(carvedExecution, //
                    mi, //
                    cache, involvedActivities, problematicDependencies);

            //
            if (problematicDependencies.size() == 1) {
                // TODO This may be a corner case in which mi depends on two activities at once.
                // Not sure whether this is possible!
                System.out.println(
                        "AndroidMultiActivitySimplifier.simplify() Found the following problematic deps for " + mi);
                problematicDependencies.forEach(System.out::println);

                // Problematic Dependencies should not contain the mi itself? what about

                // Stop at the first one. We break one by one
                break;
            } else if (problematicDependencies.size() >= 1) {

                problematicDependencies = problematicDependencies.stream().filter(_mi -> !_mi.equals(mi))
                        .collect(Collectors.toList());

                System.out.println(
                        "AndroidMultiActivitySimplifier.simplify() Found the following problematic deps for " + mi);
                problematicDependencies.forEach(System.out::println);

                // Stop at the first one. We break one by one
                break;
            } else {
                System.out.println("AndroidMultiActivitySimplifier.simplify() No problematic deps for " + mi);
            }
        }

        return problematicDependencies;

    }

    // Given this method let's find out on which Activity it depends. Also
    // accumulate any problematic dependenci we found
    private void onWhichActivityDepends(CarvedExecution carvedExecution, MethodInvocation methodInvocation,
            Set<MethodInvocation> cache, Set<ObjectInstance> involvedActivities,
            // We keep them sorted
            List<MethodInvocation> problematicDepdendencies) {

        DataDependencyGraph relevantDataDependencyGraph = carvedExecution
                .getDataDependencyGraphContainingTheMethodInvocation(methodInvocation);

        if (cache.contains(methodInvocation)) {
            logger.info("onWhichActivityDepends " + methodInvocation + " CACHED");
            return;
        } else {
            logger.info("onWhichActivityDepends " + methodInvocation);
        }

        // If the owner of this method is an activity, this methods surely depends on it
        if (!methodInvocation.isStatic() && methodInvocation.getOwner().isAndroidActivity()) {
            involvedActivities.add(methodInvocation.getOwner());
        }

        if (involvedActivities.size() > 1) {
            logger.info("Found a problematic method that introduce a deps on another activity as owner",
                    methodInvocation);
            problematicDepdendencies.add(methodInvocation);
            // TODO Do we return at this point or continue the search?
            logger.info("onWhichActivityDepends " + methodInvocation + " --> " + involvedActivities);
            return;
        }

        // If not check if any of the dependency of this method introduce a noisy
        // dependency - UNION WITH IMPLICIT?

        Set<ObjectInstance> directAndImplicitDepdencies = new HashSet<ObjectInstance>();

        // FOLLOW OWNERSHIP
        // Be sure to consider the other calls made on this class
        if (!methodInvocation.isStatic()) {
            directAndImplicitDepdencies.add(relevantDataDependencyGraph.getOwnerFor(methodInvocation));
        }

        // FOLLOW DIRECT DEPENDENCIES
        // Be sure to consider the calls made on direct dependencies this class
        directAndImplicitDepdencies.addAll(relevantDataDependencyGraph.getParametersOf(methodInvocation).stream()
                .filter(dn -> dn instanceof ObjectInstance).map(dn -> (ObjectInstance) dn).collect(Collectors.toSet()));

        // FOLLOW IMPLICIT DEPENDENCIES
        // Be sure to consider the calls made on implicit dependencies this class
        directAndImplicitDepdencies.addAll(relevantDataDependencyGraph.getImplicitDataDependenciesOf(methodInvocation)
                .stream().filter(dn -> dn instanceof ObjectInstance).map(dn -> (ObjectInstance) dn)
                .collect(Collectors.toSet()));

        for (ObjectInstance dataDependecy : directAndImplicitDepdencies) {

            if (dataDependecy.isAndroidActivity()) {
                involvedActivities.add(dataDependecy);
            }

            if (involvedActivities.size() > 1) {
                problematicDepdendencies.add(methodInvocation);
                logger.info(
                        "Found a problematic method that introduce a deps on another activity as parameter or implicit dependency",
                        methodInvocation);
                logger.info("onWhichActivityDepends " + methodInvocation + " --> " + involvedActivities);
                // TODO Do we return at this point or continue the search?
                return;

            }

            // Find the methods that state this dependency's state BEFORE methodInvocation!
            Set<MethodInvocation> directOrIndirectMethodInvocations = new HashSet<MethodInvocation>();

            directOrIndirectMethodInvocations
                    .addAll(relevantDataDependencyGraph.getMethodInvocationsForOwner(dataDependecy).stream()
                            .filter(mi -> mi.isBefore(methodInvocation)).collect(Collectors.toSet()));
            // Filtering this is NOT superflous as there may be multiple methods that
            // returned this object
            directOrIndirectMethodInvocations
                    .addAll(relevantDataDependencyGraph.getMethodInvocationsWhichReturn(dataDependecy).stream()
                            .filter(mi -> mi.isBefore(methodInvocation)).collect(Collectors.toSet()));

            // If the method is static we need to ensure we consider all the static
            // invocations before it on the logically the same static resource (which is
            // comparable but different than the owner)
            // TODO This is a bit hacky, should be included in the data model IMHO
            List<MethodInvocation> usesOfStaticDataDep = new ArrayList();
            for (ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs) {
                usesOfStaticDataDep.addAll(
                        executionFlowGraph.getMethodInvocationsBefore(methodInvocation, t -> t.isStatic() && JimpleUtils
                                .getClassNameForMethod(t.getMethodSignature()).equals(dataDependecy.getType())));
            }
//            if (usesOfStaticDataDep.size() > 0) {
//                System.out.println("Found the following STATIC uses of " + dataDependecy);
//                usesOfStaticDataDep.forEach(System.out::println);
//            }

            directOrIndirectMethodInvocations.addAll(usesOfStaticDataDep);

            // TODO There are still some conceptual problems in how to reliably identify the
            // really problematic method invocation
            // I suspect we need to look up the CALL GRAPH and not only the order of
            // invocations, since method invocations are interleaved

            // TODO For the moment we filter our stuff that we KNOW will be filter our
            // anyways
            directOrIndirectMethodInvocations = directOrIndirectMethodInvocations.stream()
                    .filter(mi -> JimpleUtils.getClassNameForMethod(mi.getMethodSignature()).contains("$Lambda$"))
                    .collect(Collectors.toSet());

            // Process them always in reverse order
            List<MethodInvocation> workList = new ArrayList<MethodInvocation>(directOrIndirectMethodInvocations);
            Collections.sort(workList);
            Collections.reverse(workList);

            System.out.println("AndroidMultiActivitySimplifier.onWhichActivityDepends() dataDependecy " + dataDependecy
                    + " introduces the following NEW dependencies");

            workList.forEach(System.out::println);

            for (MethodInvocation dependencyMethodInvocation : workList) {
                // Propagate till base case
                onWhichActivityDepends(carvedExecution, dependencyMethodInvocation, cache, involvedActivities,
                        problematicDepdendencies);
                //
                if (involvedActivities.size() > 1) {
                    System.out.println(
                            "AndroidMultiActivitySimplifier.onWhichActivityDepends() Short circuit the worklist");
                    // But keep track of the chain of calls
                    problematicDepdendencies.add(methodInvocation);
                    break;
                }
            }
        }

        cache.add(methodInvocation);
        logger.info("onWhichActivityDepends " + methodInvocation + " --> " + involvedActivities);

    }

    private int countActivities(CarvedExecution carvedExecution) {
        Set<ObjectInstance> activities = new HashSet();
        for (DataDependencyGraph dataDependencyGraph : carvedExecution.dataDependencyGraphs) {
            activities.addAll(dataDependencyGraph.getObjectInstances().stream().filter(obj -> obj.isAndroidActivity())
                    .collect(Collectors.toSet()));
        }
        System.out.println("AndroidMultiActivitySimplifier.countActivities() activies: " + activities);
        return activities.size();
    }

}
