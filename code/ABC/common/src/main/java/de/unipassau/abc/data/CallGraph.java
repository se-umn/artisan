package de.unipassau.abc.data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CallGraph {

	void visualize();

	List<MethodInvocation> getOrderedSubsumingMethodInvocationsFor(MethodInvocation methodInvocationUnderTest);

	Set<MethodInvocation> getMethodInvocationsSubsumedBy(MethodInvocation methodInvocation);

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

}
