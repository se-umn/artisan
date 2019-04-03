package de.unipassau.abc.carving.steps;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.GraphNode;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.NullInstance;
import de.unipassau.abc.carving.NullNodeFactory;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.PrimitiveValue;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
import no.systek.dataflow.Step;
import no.systek.dataflow.StepExecutor;
import no.systek.dataflow.Steps;
import no.systek.dataflow.steps.CollectorStep;

/**
 * This is a generic object which wraps the STEP thingy. It provides a collector
 * for the input, a processing node, which is the core, and a splitter for the
 * output.
 * 
 * Inputs and outputs are manages as sets to implement idempotent operations
 * 
 * @author gambi
 *
 */
public class CarvingNode {

    // This gets one single input of size [n] and produces an output of size [1]
    private CarvingStep processing;
    private PrimitiveCarvingStep sourceValue;
    private NullInstanceCarvingStep sourceNullValue;

    // This gets n inputs of size [1] and transform it into an input of size [n]
    private CollectorStep<Pair<ExecutionFlowGraph, DataDependencyGraph>> inputChannel;
    // This gets one input of size 1 and creates n inputs of size 1
    private Step<Pair<ExecutionFlowGraph, DataDependencyGraph>, Pair<ExecutionFlowGraph, DataDependencyGraph>> outputChannel = Steps
            .newParallel(orders -> orders);

    private GraphNode core;
    private MethodInvocation context;

    /*
     * We need to have an explicit input size here since we might not know
     * before hand how many connections for we need... Probably we can simply
     * use a very large number
     */
    public CarvingNode(DataNode core, MethodInvocation context) {
        if (core instanceof NullInstance) {
            initWithNullInstance((NullInstance) core, context);
        } else if (core instanceof PrimitiveValue) {
            initWithPrimitiveValue((PrimitiveValue) core, context);
        } else {
            initWithObjectInstance((ObjectInstance) core, context);
        }
    }

    public CarvingNode(MethodInvocation core) {
        this.core = core;
        processing = new CarvingStep(core);
        // Count the data dependencies
        int inputSize = JimpleUtils.getParameterList(core.getMethodSignature()).length;
        parameterDependencies = new CarvingNode[inputSize];
        // Include Owner as data dependency
        if (!core.isStatic() && !core.isConstructor()) {
            inputSize = inputSize + 1;
        }
        /*
         * If input size is 0 collector breaks because it receives nevertheless
         * the empty input for those nodes which do not depend on anything
         * else...
         */
        if (inputSize == 0) {
            inputSize = 1;
        }
        inputChannel = new CollectorStep<>(null, inputSize);
        processing.dependsOn(inputChannel.output());
        outputChannel.dependsOn(processing.output());
    }

    private void initWithObjectInstance(ObjectInstance core, MethodInvocation context) {
        this.core = core;
        this.context = context;
        processing = new CarvingStep(core, context);
        inputChannel = new CollectorStep<>(null, Integer.MAX_VALUE);
        processing.dependsOn(inputChannel.output());
        outputChannel.dependsOn(processing.output());
    }

    private void initWithPrimitiveValue(PrimitiveValue core, MethodInvocation context) {
        this.core = core;
        this.context = context;
        sourceValue = new PrimitiveCarvingStep(core, context);
        outputChannel.dependsOn(sourceValue.output());
    }

    private void initWithNullInstance(NullInstance core, MethodInvocation context) {
        this.core = core;
        this.context = context;
        sourceNullValue = new NullInstanceCarvingStep(core, context);
        outputChannel.dependsOn(sourceNullValue.output());
    }

    public GraphNode getCore() {
        return core;
    }

    private CarvingNode ownerDependency = null;
    private final static Logger logger = LoggerFactory.getLogger(CarvingNode.class);

    public void acceptAsOwner(CarvingNode parameter) {
        if (ownerDependency == null) {
            // System.out.println("CarvingNode.acceptAsOwner() : " +
            // parameter.core + " -> " + core + "");
            // Attach output of parameter as input to this node
            inputChannel.dependsOn(parameter.outputChannel.output());
        } else {
            boolean same = parameter.equals(ownerDependency);
            if (!same) {
                logger.warn("Try to over-write owner " + ownerDependency + " with " + parameter + " ! Skip this");
            }
        }
    }

    private CarvingNode[] parameterDependencies = null;

    public void acceptAsParameterInPosition(CarvingNode parameter, int position) {
        if (parameterDependencies[position] == null) {
            // System.out.println(
            // "CarvingNode.accept() binding Parameter[" + position + "] " +
            // parameter.core + " -> " + core);
            // Attach output of parameter as input to this node
            inputChannel.dependsOn(parameter.outputChannel.output());
        } else {
            boolean same = parameter.equals(parameterDependencies[position]);
            if (!same) {
                logger.warn("Try to over-write Parameter[" + position + "] " + parameterDependencies[position]
                        + " with " + parameter + " ! Skip this");
            }
        }
    }

    // We need to use MethodInvocation as Key sinve CarvingNode does not
    // implement a proper hashCode/equalsTo methods
    private Map<MethodInvocation, CarvingNode> stateChangingDepedencies = new HashMap<>();

    public void acceptAsStateChangingMethod(CarvingNode parameter) {
        if (!stateChangingDepedencies.containsKey(parameter.getCore())) {
            // System.out.println("CarvingNode.accept() binding State-changing
            // method " + parameter.core + " -> " + core
            // + " in context " + this.context);
            // Attach output of parameter as input to this node
            inputChannel.dependsOn(parameter.outputChannel.output());
        } else {
            boolean same = parameter.equals(stateChangingDepedencies.get(parameter.getCore()));
            if (!same) {
                logger.warn(
                        "Try to over-write State-changing method " + stateChangingDepedencies.get(parameter.getCore())
                                + " with " + parameter + " in context " + this.context + " ! Skip this");
            }
        }
    }

    public Pair<ExecutionFlowGraph, DataDependencyGraph> executeWith(StepExecutor stepExecutor) {
        ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
        DataDependencyGraph dataDependencyGraph = new DataDependencyGraph();
        // Start off with empty data structures
        System.out.println("CarvingNode.executeWith() STARTING THE EXECUTION");
        return stepExecutor.execute(outputChannel,
                new Pair<ExecutionFlowGraph, DataDependencyGraph>(executionFlowGraph, dataDependencyGraph));
    }

    
}
