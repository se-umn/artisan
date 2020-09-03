package de.unipassau.abc.data;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface ExecutionFlowGraph {

	/**
	 * Returns all the elements before the provided method inclusive of it that
	 * match the given predicate. This means that the given method is include only
	 * and only if it matches the predicate.
	 * 
	 * @param methodInvocation
	 * @param predicate
	 * @return
	 */
	public Set<MethodInvocation> getMethodInvocationsBefore(MethodInvocation methodInvocation,
			Predicate<MethodInvocation> predicate);

	/**
	 * Extract from the underlying graph a fragmented graph data structure where
	 * each connected component respects the relations of the original graph.
	 * Basically, the resulting graph will have one node (cloned) for each of the
	 * given method invocations, and there will be a link between nodes, only if
	 * there was a link connecting their counter parts in the original graph.
	 * 
	 * 
	 * TODO This might require to return a Collection<ExecutionFlowGraph> one for
	 * each connected components
	 * 
	 * @param methodInvocations
	 * @return
	 */
	public Collection<ExecutionFlowGraph> extrapolate(Collection<MethodInvocation> methodInvocations);

	public void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl);

	/**
	 * Return a new subgraph that contains all the given methodInvocations enqueued
	 * in the same order they are given. This effectively creates another graph
	 * object (so not a view) where nodes edges are cloned from the original graphs
	 * but nodes are not.
	 * 
	 * 
	 * @param methodInvocations
	 * @return
	 */
	public ExecutionFlowGraph getSubGraph(List<MethodInvocation> methodInvocations);

	/**
	 * THIS MODIFIED THE GRAPH ITSELF, WHILE I NEED ONLY THE SUBGRAPH WITH THE
	 * CONNECTED METHODS?
	 * 
	 * @param requiredMethodInvocations
	 */
	public void refine(Set<MethodInvocation> requiredMethodInvocations);

	public List<MethodInvocation> getOrderedMethodInvocations();

	public boolean contains(MethodInvocation methodInvocation);

	public void enqueueMethodInvocations(MethodInvocation methodInvocation);

	public List<MethodInvocation> getMethodInvocationsFor(MethodInvocationMatcher carveBy,
			MethodInvocationMatcher... excludeBy);

	public MethodInvocation getLastMethodInvocation();

	public List<MethodInvocation> getOrderedMethodInvocationsBefore(MethodInvocation second);

	public void include(ExecutionFlowGraph acitivityLifecycleExecutionFlowGraph);

	public void summarize(CallGraph callGraph);

	public Set<MethodInvocation> getMethodInvocationsAfter(MethodInvocation former);

	public void removeMethodInvocation(MethodInvocation methodInvocation);

	public Set<MethodInvocation> getTestSetupMethodInvocations();

	public List<MethodInvocation> getOrderedMethodInvocationsToExternalInterfaceBefore(MethodInvocation methodToCarve);

	public Set<MethodInvocation> getMethodInvocationsToExternalInterfaceBefore(
			MethodInvocation methodInvocationToCarve);

	public void markNodeAsExternalInterface(MethodInvocation mi);

	public List<MethodInvocation> getOrderedMethodInvocationsAfter(MethodInvocation startActivityForResult);

	public void visualize();

}
