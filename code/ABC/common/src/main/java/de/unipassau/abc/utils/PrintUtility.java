package de.unipassau.abc.utils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.robolectric.util.Logger;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.CallGraphImpl;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.MethodInvocation;

public class PrintUtility {

    public static String printWithNesting(List<MethodInvocation> allMethodInvocations, CallGraph callGraph) {
        return allMethodInvocations.stream().map(new Function<MethodInvocation, String>() {

            @Override
            public String apply(MethodInvocation t) {
                // Assign a level to each method invocation
                try {
                    int nestingLevel = callGraph.getNestingLevelOf(t);
                    return (t.isNecessary() ? "*L" : " L") + String.join("", Collections.nCopies(nestingLevel, "_"))
                            + t.toString() + "\n";
                } catch (Exception e) {
                    Logger.warn("@@@ Cannot find " + t + " in the call graph");
                    return (t.isNecessary() ? "-L" : "=L") + t.toString() + "\n";
                }

            }
        }).collect(Collectors.joining());

    }

//    private String plotAll(CarvedExecution carvedExecution) {
//        // There might be a lot of pieces that form this test, so we need to check that
//        // they are all considered?
//        DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
//        carvedExecution.dataDependencyGraphs.stream().forEach(ddg -> dataDependencyGraph.include(ddg));
//
//        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
//        carvedExecution.executionFlowGraphs.stream().forEach(efg -> executionFlowGraph.include(efg));
//
//        CallGraph callGraph = new CallGraphImpl();
//        carvedExecution.callGraphs.stream().forEach(cg -> callGraph.include(cg));
//
//        return PrintUtility.printWithNesting(executionFlowGraph.getOrderedMethodInvocations(), callGraph);
//    }

}
