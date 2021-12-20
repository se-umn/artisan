package de.unipassau.abc.generation.data;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;

public class AndroidCarvedTest extends CarvedTest {

	public AndroidCarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph, String traceId) {
		super(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph, traceId);
	}

	public AndroidCarvedTest(MethodInvocation methodInvocationUnderTest, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph, CatchBlock catchExpectedException,
			CatchBlock catchUnexpectedException, String traceId) {
		super(methodInvocationUnderTest, executionFlowGraph, dataDependencyGraph, catchExpectedException,
				catchUnexpectedException, traceId);
	}

}