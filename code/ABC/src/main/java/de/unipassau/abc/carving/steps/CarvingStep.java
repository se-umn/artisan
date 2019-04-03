package de.unipassau.abc.carving.steps;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.GraphNode;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
import no.systek.dataflow.steps.ListStep;

/**
 * A carving step receives a number of carved executions and generates another
 * carved executions which comprises them
 * 
 * @author gambi
 *
 */
public class CarvingStep
        extends ListStep<Pair<ExecutionFlowGraph, DataDependencyGraph>, Pair<ExecutionFlowGraph, DataDependencyGraph>> 
        implements ABCCarvingStep {

    private GraphNode carvingTarget;

    protected CarvingStep(String name) {
        super(name);
    }

    public CarvingStep(MethodInvocation carvingTarget) {
        this("Carving Step for : " + carvingTarget.toString());
        this.carvingTarget = carvingTarget;
    }
    
    public CarvingStep(DataNode carvingTarget, MethodInvocation context) {
        this("Carving Step for : " + carvingTarget.toString() + " in the context " + context );
        this.carvingTarget = carvingTarget;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    protected List<Pair<ExecutionFlowGraph, DataDependencyGraph>> execute(
            List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedDependencies) {

        System.out.println("CarvingStep.execute() " + this.getName() + " with " + carvedDependencies.size() + " dependencies");
        
        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> result = new ArrayList<>(1);
        //
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        result.add(new Pair<ExecutionFlowGraph, DataDependencyGraph>(executionFlowGraph, dataDependencyGraph));
        //
        for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedDependency : carvedDependencies) {
            executionFlowGraph.include(carvedDependency.getFirst());
            dataDependencyGraph.include(carvedDependency.getSecond());
        }
        /*
         * At this point if the carvingTarget was a DataNode, there's nothing
         * left to do because the executionFlowGraph contains the code which
         * returns that DataNode from its final call AND the data dependency
         * graph links the DataNode as return (or owner). If the carving target
         * is a method invocation we need to add it as last and update all its
         * data dependencies
         */
        if (carvingTarget instanceof MethodInvocation) {
            includeTheCarvingTarget((MethodInvocation) carvingTarget, //
                    executionFlowGraph, dataDependencyGraph);
        } else {
            includeTheCarvingTarget((ObjectInstance) carvingTarget, //
                    executionFlowGraph, dataDependencyGraph);
        }

        return result;
    }

    private void includeTheCarvingTarget(ObjectInstance carvingTarget, ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph) {
        /*
         * It might happen that objects appear out of the blue, do not let them go away... 
         */
        dataDependencyGraph.addObjectInstance( carvingTarget);
        
    }

    public void includeTheCarvingTarget(MethodInvocation methodInvocationToCarve, ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph) {
        /*
         * Add the plain methodInvocationToCarve as method to execute
         */
        executionFlowGraph.enqueueMethodInvocations(methodInvocationToCarve);

        /*
         * Update the data dependencies on that method call.
         */
        dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocationToCarve);

        /*
         * Unless the method call is static or a constructor the owner MUST be
         * there already
         */
        if (! methodInvocationToCarve.isConstructor() && ! methodInvocationToCarve.isStatic() ) {
            if (!dataDependencyGraph.getDataNodes().contains(methodInvocationToCarve.getOwner())) {
                throw new RuntimeException(
                        methodInvocationToCarve + " lacks its owner " + methodInvocationToCarve.getOwner() + ")");
            }
        }
        /*
         * If the method is a constructor register its ownership
         */
        if( ! methodInvocationToCarve.isStatic() ) {
            dataDependencyGraph.addDataDependencyOnOwner(methodInvocationToCarve, methodInvocationToCarve.getOwner());
        }

        /*
         * Parameters MUST be there already !
         */
        for (DataNode actualParameterToCarve : methodInvocationToCarve.getActualParameterInstances()) {

            if (!dataDependencyGraph.getDataNodes().contains(actualParameterToCarve)) {
                throw new RuntimeException(methodInvocationToCarve + " lacks its parameter " + actualParameterToCarve
                        + "(position="
                        + methodInvocationToCarve.getActualParameterInstances().indexOf(actualParameterToCarve) + ")");
            }

            dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocationToCarve, actualParameterToCarve, //
                    methodInvocationToCarve.getActualParameterInstances().indexOf(actualParameterToCarve));
        }

        /*
         * There's no check on return value because the same object might be the
         * return value of other method calls (e.g., FactoryPattern) So we just
         * include the return value of the carved call if any.
         */
        if (!JimpleUtils.hasVoidReturnType(methodInvocationToCarve.getMethodSignature())) {
            dataDependencyGraph.addDataDependencyOnReturn(methodInvocationToCarve,
                    methodInvocationToCarve.getReturnValue());
        }

    }

}