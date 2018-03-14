package de.unipassau.abc.carving;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;

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
