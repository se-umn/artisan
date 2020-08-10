package de.unipassau.abc.data;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import soot.Local;
import soot.Value;

public interface DataDependencyGraph {

	public void visualize();

	public Collection<ObjectInstance> getObjectInstances();

	public Collection<? extends MethodInvocation> getMethodInvocationsForOwner(ObjectInstance startingIntent);

	public DataDependencyGraph getSubGraph(ExecutionFlowGraph executionFlowGraph);

	public void include(DataDependencyGraph second);

	public boolean verifyObjectInstanceProvenance();

	public Set<ObjectInstance> getDanglingObjects();

	public void addDataDependencyOnActualParameter(MethodInvocation methodInvocation, DataNode actualParameter,
			int position);

	public void addDataDependencyOnOwner(MethodInvocation controllerLifecycleMethod, ObjectInstance objectInstance);

	public void addMethodInvocationWithoutAnyDependency(MethodInvocation controllerLifecycleMethod);

	public void summarize(ExecutionFlowGraph executionFlowGraph);

	public Set<ObjectInstance> getAliasesOf(ObjectInstance owner);

	public void addDataDependencyOnReturn(MethodInvocation methodInvocationToCarve, DataNode returnValue);

	public DataDependencyGraph getSubGraph(List<MethodInvocation> orderedSlice);

	public void markNodeAsExternalInterface(MethodInvocation mi);

	public Set<MethodInvocation> getWeaklyConnectedComponentContaining(MethodInvocation methodInvocationUnderTest);

	public void refine(Set<MethodInvocation> coreMethodInvocations);

	public void purge();

	public Optional<MethodInvocation> getConstructorOf(ObjectInstance objectInstance);

	public Collection<MethodInvocation> getMethodInvocationsWhichUse(DataNode dataNode);

	public ObjectInstance getOwnerFor(MethodInvocation methodInvocationToCarve);

	public Set<MethodInvocation> getMethodInvocationsWhichReturn(ObjectInstance objectInstanceToCarve);

	public MethodInvocation getInitMethodInvocationFor(ObjectInstance data);

	public Collection<MethodInvocation> getMethodInvocationsRecheableFrom(MethodInvocation methodInvocation);

	public List<ObjectInstance> getParametersOf(MethodInvocation methodInvocation);

	public Set<DataNode> getDataNodes();

	public Value getReturnObjectLocalFor(MethodInvocation mut);

	public Local getObjectLocalFor(MethodInvocation mut);

	public List<Value> getParametersSootValueFor(MethodInvocation methodInvocation);

	public void setSootValueFor(DataNode node, Value localVariable);

}
