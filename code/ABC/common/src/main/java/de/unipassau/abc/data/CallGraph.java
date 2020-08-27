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

	@Deprecated
	void visualize();

	void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl);

	List<MethodInvocation> getOrderedSubsumingMethodInvocationsFor(MethodInvocation methodInvocationUnderTest);

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

	public void reset();

	/**
	 * This is used mostly for testing. Can be lifted to upper interface. TODO:
	 * CarvingGraph or something
	 * 
	 * @return
	 */
	public Collection<MethodInvocation> getAllMethodInvocations();

	/**
	 * Return the collection of weakly connected components that can be formed from
	 * the given method invocations
	 * 
	 * @param necessaryMethodInvocations
	 * @return
	 */
	public Collection<CallGraph> extrapolate(Set<MethodInvocation> necessaryMethodInvocations);

//	

}
