package de.unipassau.abc.carving;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.data.ExecutionFlowGraph;

public class ExecutionFlowGraphTest {

	@Test
	public void testGetOrderedMethodInvocationsAfter() {
		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraph();
		
		for( int invocationCount =0 ; invocationCount < 10; invocationCount++){
			executionFlowGraph.enqueueMethodInvocations( new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, "Invocation" , invocationCount));
		}
		
		assertEquals(10, executionFlowGraph.getOrderedMethodInvocations().size());
	}
}
