package de.unipassau.abc.generation.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.generation.assertions.CarvingAssertion;
import de.unipassau.abc.generation.mocks.CarvingMock;
import de.unipassau.abc.generation.mocks.CarvingShadow;

public class CarvedTest {

    private static final Logger logger = LoggerFactory.getLogger(CarvedTest.class);

    private static AtomicInteger identityCounter = new AtomicInteger(0);

    private int uniqueIdentifier;

    // We need to keep track of this explicitly as assertions are invocations added
    // AFTER the execution of this method
    private MethodInvocation methodInvocationUnderTest;

    /*
     * The carved test is nothing more than a list of method calls chained together
     * and linked by data dependencies;
     */
    private ExecutionFlowGraph executionFlowGraph;
    private DataDependencyGraph dataDependencyGraph;

    /*
     * But to flexibly assert the occurrence of exceptional behaviors we need to
     * tweak the basic structure to include catch-blocks for the expected exception
     * (pass) and any other one (fail)
     */
    private CatchBlock catchExpectedException;
    private CatchBlock catchUnexpectedException;

    private List<CarvingAssertion> assertions;
    private List<CarvingMock> mocks;
    private List<CarvingShadow> shadows;
    private String traceId;

    // Basic Tests
    public CarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph, CatchBlock catchExpectedException,
            CatchBlock catchUnexpectedException, String traceId) {
        this.uniqueIdentifier = identityCounter.incrementAndGet();
        this.methodInvocationUnderTest = methodInvocationUnderTest;
        this.executionFlowGraph = executionFlowGraph;
        this.dataDependencyGraph = dataDependencyGraph;
        this.catchExpectedException = catchExpectedException;
        this.catchUnexpectedException = catchUnexpectedException;
        //
        this.assertions = new ArrayList<>();
        this.mocks = new ArrayList<>();
        this.shadows = new ArrayList<>();
        this.traceId = traceId;
    }

    public CarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
            DataDependencyGraph dataDependencyGraph, String traceId) {
        this.uniqueIdentifier = identityCounter.incrementAndGet();
        this.methodInvocationUnderTest = methodInvocationUnderTest;
        this.executionFlowGraph = executionFlowGraph;
        this.dataDependencyGraph = dataDependencyGraph;
        //
        this.assertions = new ArrayList<>();
        this.mocks = new ArrayList<>();
        this.shadows = new ArrayList<>();

        // DEBUG
        this.dataDependencyGraph.getObjectInstances().forEach(oi -> {
            logger.debug(">> Constructor of " + oi + " == " + this.dataDependencyGraph.getConstructorOf(oi));
            logger.debug(
                    ">> Generators of " + oi + " == " + this.dataDependencyGraph.getMethodInvocationsWhichReturn(oi));
        });

        this.traceId = traceId;

    }

    public String getTraceId() {
        return traceId;
    }

    public int getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setExecutionFlowGraph(ExecutionFlowGraph executionFlowGraph) {
        this.executionFlowGraph = executionFlowGraph;
    }

    public void addMock(CarvingMock mock) {
        this.mocks.add(mock);
    }

    public List<CarvingMock> getMocks() {
        return mocks;
    }

    public void addShadow(CarvingShadow shadow) {
        this.shadows.add(shadow);
    }

    public List<CarvingShadow> getShadows() {
        return shadows;
    }

    public List<String> getShadowsNames() {
        List<String> shadowsNames = new ArrayList<String>();
        for (CarvingShadow shadow : shadows) {
            shadowsNames.add(shadow.getShadowName());
        }
        return shadowsNames;
    }

    // TODO Not sure we need a setAssertions
    public void addAssertion(CarvingAssertion assertion) {
        this.assertions.add(assertion);
    }

    public List<CarvingAssertion> getAssertions() {
        return assertions;
    }

    public MethodInvocation getMethodUnderTest() {
        return this.methodInvocationUnderTest;
    }

    // As first approximation, every Object handled in the test requires a variable
    // to be stored. Naming of such variables should be handled on the receiving
    // side. Order does not matter, but duplicates do
    // TODO Resolve Aliases? Should we keep track that some objects can or cannot be
    // used as different types in different contextes?
    public Set<ObjectInstance> getObjectInstances() {
        // This might not return what do we expect...
        return new HashSet<ObjectInstance>(dataDependencyGraph.getObjectInstances());
    }

    // The list of statements that make up the entire test (setup, invocation, and
    // assertions). Each method invocation contains references to DataNode (and
    // ObjectInstances). the matching between names and objectInstances must be
    // hjandled on the receiving side.
    public List<MethodInvocation> getStatements() {
        // Note that this includes EXACTLY the sequences of methods that we need to
        // invoke
        return this.executionFlowGraph.getOrderedMethodInvocations();
    }

    public boolean expectException() {
        return this.catchExpectedException != null;
    }

    public CatchBlock getExpectedExceptionCatchBlock() {
        return this.catchExpectedException;

    }

    public CatchBlock getUnexpectedExceptionCatchBlock() {
        return this.catchUnexpectedException;
    }

    public ExecutionFlowGraph getExecutionFlowGraph() {
        return this.executionFlowGraph;
    }

    public DataDependencyGraph getDataDependencyGraph() {
        return this.dataDependencyGraph;
    }

    public void setDataDependencyGraph(DataDependencyGraph dataDependencyGraph) {
        this.dataDependencyGraph = dataDependencyGraph;
    }

}
