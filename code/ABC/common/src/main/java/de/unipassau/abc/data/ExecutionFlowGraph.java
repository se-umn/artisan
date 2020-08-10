package de.unipassau.abc.data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ExecutionFlowGraph {

	public List<MethodInvocation> getOrderedMethodInvocations();

	public boolean contains(MethodInvocation methodInvocation);

	public void enqueueMethodInvocations(MethodInvocation methodInvocation);

	public List<MethodInvocation> getMethodInvocationsFor(MethodInvocationMatcher carveBy,
			MethodInvocationMatcher... excludeBy);

	public MethodInvocation getLastMethodInvocation();

	public List<MethodInvocation> getOrderedMethodInvocationsBefore(MethodInvocation second);

	public void refine(Set<MethodInvocation> requiredMethodInvocations);

	public void include(ExecutionFlowGraph acitivityLifecycleExecutionFlowGraph);

	public void summarize(CallGraph callGraph);

	public Set<MethodInvocation> getMethodInvocationsBefore(MethodInvocation second);

	public Set<MethodInvocation> getMethodInvocationsAfter(MethodInvocation former);

	public void removeMethodInvocation(MethodInvocation methodInvocation);

	public ExecutionFlowGraph getSubGraph(List<MethodInvocation> invocationsBefore);

	public Set<MethodInvocation> getTestSetupMethodInvocations();

	public List<MethodInvocation> getOrderedMethodInvocationsToExternalInterfaceBefore(MethodInvocation methodToCarve);

	public Set<MethodInvocation> getMethodInvocationsToExternalInterfaceBefore(
			MethodInvocation methodInvocationToCarve);

	public void markNodeAsExternalInterface(MethodInvocation mi);

	public List<MethodInvocation> getOrderedMethodInvocationsAfter(MethodInvocation startActivityForResult);

	public void visualize();

}
