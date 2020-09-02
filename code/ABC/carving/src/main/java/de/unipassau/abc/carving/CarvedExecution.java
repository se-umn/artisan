package de.unipassau.abc.carving;

import java.util.Collection;
import java.util.NoSuchElementException;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;

/**
 * This class holds all the fragments extracted during the carving. For the
 * moment this is just a DTO
 * 
 * @author gambi
 *
 */
public class CarvedExecution {

	/*
	 * 
	 */
	public MethodInvocation methodInvocationUnderTest;

	public Collection<ExecutionFlowGraph> executionFlowGraphs;
	public Collection<DataDependencyGraph> dataDependencyGraphs;
	public Collection<CallGraph> callGraphs;

	/**
	 * Return the callGraph which contain the methodInvocationUnderTest or null
	 * 
	 * @return
	 */
	public CallGraph getCallGraphContainingTheMethodInvocationUnderTest() {
		try {
			// https://stackoverflow.com/questions/22694884/filter-java-stream-to-1-and-only-1-element
			return callGraphs.stream().filter(cg -> cg.getAllMethodInvocations().contains(methodInvocationUnderTest))
					.reduce((a, b) -> {
						// This should never happen anyway...
						throw new IllegalStateException("Multiple elements: " + a + ", " + b);
					}).get();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

}
