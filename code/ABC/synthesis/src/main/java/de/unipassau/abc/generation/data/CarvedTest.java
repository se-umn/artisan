package de.unipassau.abc.generation.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;

public class CarvedTest {

	// We need to keep track of this explicitly as assertions are invocations added
	// AFTER the execution of this method
	private MethodInvocation methodInvocationUnderTest;

	/*
	 * The carved test is nothing more than a list of method calls chained together
	 * and linked by data dependencies;
	 */
	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;

	public CarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		this.methodInvocationUnderTest = methodInvocationUnderTest;
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
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
}