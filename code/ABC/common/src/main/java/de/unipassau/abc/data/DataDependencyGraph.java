package de.unipassau.abc.data;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.unipassau.abc.exceptions.ABCException;
import soot.Local;
import soot.Value;

public interface DataDependencyGraph {

	/**
	 * Return the list of parameters used for the method invocation. Order matters!
	 * 
	 * @param methodInvocation
	 * @return
	 */
	public List<DataNode> getParametersOf(MethodInvocation methodInvocation);

	/**
	 * Method used during parsing to link the DataNode to the method invocation
	 * 
	 * @param methodInvocation
	 * @param actualParameter
	 * @param position
	 */
	public void addDataDependencyOnActualParameter(MethodInvocation methodInvocation, DataNode actualParameter,
			int position);

	/**
	 * Return the node that corresponds to the return value of this methodInvocation
	 * or Optional.empty if the signature is void. If the methodI invocation is not
	 * tied to any return value an exception will be thrown
	 * 
	 * @param mi
	 * @return
	 * @throws ABCException if a return value cannot be found for a method that is
	 *                      not declared as void
	 */
	public Optional<DataNode> getReturnValue(MethodInvocation mi) throws ABCException;

	/**
	 * Return all the weakly connected components that can be formed considering the
	 * given methodInvocations (and their dependencies)
	 * 
	 * @param methodInvocations
	 * @return
	 */
	public Collection<DataDependencyGraph> extrapolate(Set<MethodInvocation> methodInvocations);

	/**
	 * Return a set containing the object instances for which we cannot establish
	 * provenance, that is, object instances that appear out-of-the-blue. Null
	 * objects and objects that are modeled as primitive values (e.g., Strings) are
	 * not considered here.
	 * 
	 * @return
	 */
	public Set<ObjectInstance> getDanglingObjects();

	public void visualize();

	public Collection<ObjectInstance> getObjectInstances();

	public Collection<? extends MethodInvocation> getMethodInvocationsForOwner(ObjectInstance startingIntent);

	public DataDependencyGraph getSubGraph(ExecutionFlowGraph executionFlowGraph);

	public void include(DataDependencyGraph second);

	public boolean verifyObjectInstanceProvenance();

	public void addDataDependencyOnOwner(MethodInvocation controllerLifecycleMethod, ObjectInstance objectInstance);

	public void addMethodInvocationWithoutAnyDependency(MethodInvocation controllerLifecycleMethod);

	public void replaceMethodInvocation(MethodInvocation orig, MethodInvocation repl);

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

	public Set<DataNode> getDataNodes();

	public Value getReturnObjectLocalFor(MethodInvocation mut);

	public Local getObjectLocalFor(MethodInvocation mut);

	public List<Value> getParametersSootValueFor(MethodInvocation methodInvocation);

	public void setSootValueFor(DataNode node, Value localVariable);

	/**
	 * This is mostly used for testing. Returns all the methods invocations in this
	 * graph.
	 * 
	 * @return
	 */
	public Collection<MethodInvocation> getAllMethodInvocations();

}
