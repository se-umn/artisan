package de.unipassau.carving;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExecutionFlowGraphTest {

	@Test
	public void testGetOrderedMethodInvocationsAfter() {
		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
		
		for( int invocationCount =0 ; invocationCount < 10; invocationCount++){
			executionFlowGraph.enqueueMethodInvocations( new MethodInvocation("Invocation" , invocationCount));
		}
		
		assertEquals(10, executionFlowGraph.getOrderedMethodInvocations().size());
	}
}