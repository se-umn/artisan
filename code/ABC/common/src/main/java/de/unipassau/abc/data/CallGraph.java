package de.unipassau.abc.data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CallGraph {

	/**
	 * Return a graph that is the union of the fragments where the method
	 * invocations are given methodInvocations are linked by caller-callee relation.
	 * 
	 * @param methodInvocations
	 * @return
	 */
	public CallGraph getSubGraph(List<MethodInvocation> methodInvocations);

	/**
	 * Return the set of method invocations at the top level of the call graph,
	 * i.e., the roots. For each call graph there is always at least one root.
	 * 
	 * @return
	 */
	public Set<MethodInvocation> getRoots();

	/**
	 * Replace a method invocation with another one ensuring that the connections of
	 * the nodes (edges) are semantically preserved
	 * 
	 * @param orig
	 * @param repl
	 */
	public void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl);

	/**
	 * Replace a method invocation with its executions. Basically, it flatten the
	 * call graph by removing the nesting
	 * 
	 * @param methodInvocationToReplace
	 */
	public void replaceMethodInvocationWithExecution(MethodInvocation methodInvocationToReplace);

	/**
	 * Return the list of the methods that from the root of the call graph arrive to
	 * the given method invocation ordered by the call relation.
	 * 
	 * @param methodInvocationUnderTest
	 * @return
	 */
	public List<MethodInvocation> getOrderedSubsumingMethodInvocationsFor(MethodInvocation methodInvocationUnderTest);

	/**
	 * Return the set of method invocations that will be invoked if the given method
	 * invocation is invoked. This is the transitive closure of the "call" relation
	 * over the method invocations.
	 * 
	 * @param methodInvocation
	 * @return
	 */
	public Set<MethodInvocation> getMethodInvocationsSubsumedBy(MethodInvocation methodInvocation);

	Collection<MethodInvocation> getAll();

	List<MethodInvocation> verify();

	// Used only during parsing in order to match start-return of methods
	MethodInvocation pop();

	// Used only during parsing in order to match start-return of methods
	void push(MethodInvocation methodInvocation);

	List<MethodInvocation> getSubsumingPathFor(MethodInvocation subsumingMethodInvocation,
			MethodInvocation methodInvocationToCarve);

	MethodInvocation getCallerOf(MethodInvocation methodUnderInspection);

	CallGraph getSubGraph(MethodInvocation testSetupFromContext);

	void markParentAndPruneAfter(MethodInvocation parent);

	/**
	 * This is used mostly for testing. Can be lifted to upper interface. TODO:
	 * CarvingGraph or something
	 * 
	 * @return
	 */
	public Collection<MethodInvocation> getAllMethodInvocations();

	/**
	 * Return the collection of weakly connected components that can be formed from
	 * the given method invocations. In case there are none, the node itself is the WCC
	 * 
	 * @param necessaryMethodInvocations
	 * @return
	 */
	public Collection<CallGraph> extrapolate(Set<MethodInvocation> necessaryMethodInvocations);

	@Deprecated
	void visualize();
//	

	/**
	 * Remove a node from the call graph including all the nodes that are reachable
	 * from it.
	 * 
	 * @param node
	 */
	public void remove(MethodInvocation node);

}
