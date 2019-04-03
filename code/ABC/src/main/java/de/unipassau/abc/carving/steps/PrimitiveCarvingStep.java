package de.unipassau.abc.carving.steps;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.PrimitiveValue;
import de.unipassau.abc.data.Pair;
import no.systek.dataflow.steps.SourceStep;

public class PrimitiveCarvingStep extends SourceStep<Pair<ExecutionFlowGraph, DataDependencyGraph>> implements ABCCarvingStep {

    private PrimitiveValue primitiveValue;

    // Do not use this ?!
    public PrimitiveCarvingStep(String name, int maxParallelExecution) {
        super(name, maxParallelExecution);
    }
    
    public PrimitiveCarvingStep(PrimitiveValue primitiveValue, MethodInvocation context) {
        super("Carving of primitive value" + primitiveValue + " in context " + context , Integer.MAX_VALUE);
        this.primitiveValue = primitiveValue;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    protected Pair<ExecutionFlowGraph, DataDependencyGraph> get() {
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        
        dataDependencyGraph.addPrimitiveValue( primitiveValue );
        
        return new Pair<ExecutionFlowGraph, DataDependencyGraph>(executionFlowGraph, dataDependencyGraph);
    }

}
