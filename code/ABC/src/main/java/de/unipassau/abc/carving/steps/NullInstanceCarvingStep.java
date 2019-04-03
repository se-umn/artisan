package de.unipassau.abc.carving.steps;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.NullInstance;
import de.unipassau.abc.data.Pair;
import no.systek.dataflow.steps.SourceStep;

public class NullInstanceCarvingStep extends SourceStep<Pair<ExecutionFlowGraph, DataDependencyGraph>> implements ABCCarvingStep {

    private NullInstance nullInstance;

    // Do not use this ?!
    public NullInstanceCarvingStep(String name, int maxParallelExecution) {
        super(name, maxParallelExecution);
    }
    
    public NullInstanceCarvingStep(NullInstance nullInstance, MethodInvocation context) {
        super("Carving of null instance " + nullInstance + " in context " + context, 10);
        this.nullInstance = nullInstance;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    protected Pair<ExecutionFlowGraph, DataDependencyGraph> get() {

//        System.out.println("NullInstanceCarvingStep.get() " + getName() );
        
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        
        dataDependencyGraph.addNullInstance( nullInstance );
        
        return new Pair<ExecutionFlowGraph, DataDependencyGraph>(executionFlowGraph, dataDependencyGraph);
    }

}
