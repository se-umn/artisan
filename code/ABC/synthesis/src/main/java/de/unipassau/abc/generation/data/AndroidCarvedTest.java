package de.unipassau.abc.generation.data;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;

public class AndroidCarvedTest extends CarvedTest {

	public AndroidCarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		super(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph);
	}

	public AndroidCarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph, CatchBlock catchExpectedException,
			CatchBlock catchUnexpectedException) {
		super(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph, catchExpectedException,
				catchUnexpectedException);
	}

}