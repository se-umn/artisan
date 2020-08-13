package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.NullInstance;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;

/**
 * Basic carver is a method carver, that is, it will produce a carved execution
 * whose last element is the invocation of the given method or methods
 * 
 * 
 * @author gambitemp
 *
 */
public class BasicCarver implements MethodCarver {

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	public BasicCarver(ParsedTrace parsedTrace) {
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> executionTraceForMainThread = parsedTrace
				.getThreadParsedTraceFor(ParsedTrace.MAIN_THREAD);

		this.executionFlowGraph = executionTraceForMainThread.getFirst();
		this.dataDependencyGraph = executionTraceForMainThread.getSecond();
		this.callGraph = executionTraceForMainThread.getThird();
	}

	/**
	 * This carver follows a basic heuristic:
	 * <ol>
	 * <li>Locate the method invocation in the graph and its owner</li>
	 * <li>Collect the method invocations on the same owner (if any) that happen
	 * before the target method invocation</li>
	 * <li>Collect the object instances that have been used as parameters for each
	 * of those method invocations</li>
	 * <li>For each object instance, collects all the method invocations that have
	 * been called on them BEFORE their use. (this should take care of both Direct
	 * Data Dependencies (params, return) and Indirect Data Dependencies (via
	 * reference, static referencing).</li>
	 * <li>For each of those methods, repeat the carving process</li>
	 * </ol>
	 * 
	 * 
	 * 
	 */

	private Set<MethodInvocation> findNecessaryMethodInvocations(MethodInvocation methodInvocation) {
		Set<MethodInvocation> relevantMethodInvocations = new HashSet<MethodInvocation>();

		// The first relevant method invocation is the one to carve
		relevantMethodInvocations.add(methodInvocation);

		if (methodInvocation.alreadyCarved) {
			// Return an empty result
			return relevantMethodInvocations;
		}

		/*
		 * Other relevant method invocations are the ones that have been called before
		 * on the same object, including the constructor used to instatiate it
		 */
		if (!methodInvocation.isStatic()) {
			final ObjectInstance methodInvocationOwner = methodInvocation.getOwner();

			/*
			 * Locate all the method invocations that have been invoked on the same object
			 * before invoking methodInvocation
			 */
			relevantMethodInvocations.addAll(this.executionFlowGraph.getMethodInvocationsBefore(methodInvocation,
					new Predicate<MethodInvocation>() {

						@Override
						public boolean test(MethodInvocation t) {
							return t.getOwner().equals(methodInvocationOwner);
						}
					}));
		}

		// The last relevant method invocations are the one that have been called by the
		// objects used at some point

		// Keep track at which point an object as been used, as we need only the last
		// one (the ones before will be included automatically)
		Map<ObjectInstance, List<MethodInvocation>> relevantUses = new HashMap<ObjectInstance, List<MethodInvocation>>();
		for (MethodInvocation relevantMethodInvocation : relevantMethodInvocations) {
			for (DataNode parameter : this.dataDependencyGraph.getParametersOf(relevantMethodInvocation)) {
				if (parameter instanceof NullInstance) {
					continue;
				}
				if (parameter instanceof ObjectInstance) {
					ObjectInstance objectParameter = (ObjectInstance) parameter;
					relevantUses.putIfAbsent(objectParameter, new ArrayList<MethodInvocation>());
					relevantUses.get(objectParameter).add(relevantMethodInvocation);
				}
			}
		}

		// At this point we find the last method before the use that have been called on
		// each object and carve them !
		for (Entry<ObjectInstance, List<MethodInvocation>> relevantUse : relevantUses.entrySet()) {
			// Sort in ascending order of invocation
			Collections.sort(relevantUse.getValue());
			// Get the last one
			MethodInvocation lastUse = relevantUse.getValue().get(relevantUse.getValue().size() - 1);
			relevantMethodInvocations.add(lastUse);
		}

		// Mark the given method invocation as already carved
		methodInvocation.alreadyCarved = true;

		// Prepare the work by enqueueing all the relevant method invocations found so
		// far that have not yet been carved
		Queue<MethodInvocation> queue = new LinkedList<MethodInvocation>();

		// Sort the method invocations so the last one would leverage that the first
		// have been carved already.
		List<MethodInvocation> orderedRelevantMethodInvocations = new ArrayList<MethodInvocation>(
				relevantMethodInvocations);
		Collections.sort(orderedRelevantMethodInvocations);

		for (MethodInvocation relevantMethodInvocation : orderedRelevantMethodInvocations) {
			if (!relevantMethodInvocation.alreadyCarved) {
				queue.add(relevantMethodInvocation);
			}
		}

		// Process the job and accumulate data
		while (!queue.isEmpty()) {
			MethodInvocation relevantMethodInvocation = queue.remove();
			relevantMethodInvocations.addAll(findNecessaryMethodInvocations(relevantMethodInvocation));
		}

		return relevantMethodInvocations;
	}

	@Override
	public List<CarvedExecution> carve(MethodInvocation methodInvocation) throws CarvingException, ABCException {

		List<CarvedExecution> carvedExecutions = new ArrayList<CarvedExecution>();

		/*
		 * Collect and sort the method invocations necessary for the carving
		 */
		Set<MethodInvocation> necessaryMethodInvocations = findNecessaryMethodInvocations(methodInvocation).stream()
				.map(new Function<MethodInvocation, MethodInvocation>() {
					@Override
					public MethodInvocation apply(MethodInvocation t) {
						// Create a clone and mark it as necessary
						MethodInvocation clonedMethodInvocation = t.clone();
						clonedMethodInvocation.setNecessary(true);
						return clonedMethodInvocation;
					}
				}).collect(Collectors.toSet());

		/**
		 * Starting from the set of necessary method invocations we create a
		 * "consistent" execution up to what we can do. So there will be dangling
		 * objects, connected components in the graphs, and in general missing elements
		 * here and there.
		 */

		// We use a set to avoid duplicates. The algorithms below can be improved a
		// lot...
		Set<MethodInvocation> allMethodInvocations = new HashSet<MethodInvocation>();

		/*
		 * First collect all the method invocations that will be done as consequence of
		 * invoking the necessaryMethodInvocations. In theory, executing those method
		 * invocations should not require any additional carving.
		 */
		for (MethodInvocation necessaryMethodInvocation : necessaryMethodInvocations) {
			allMethodInvocations.addAll(callGraph.getMethodInvocationsSubsumedBy(necessaryMethodInvocation));
		}

		/*
		 * Union of the necessary method invocation set and the set of method
		 * invocations obtained by cloning the subsumed method invocations. Since the
		 * hash and equalsTo function have been re-defined for the MethodInvocation, it
		 * should hold that 'a.equals(a.clone())'
		 * 
		 * TODO Assumption: addAll does not replace the objects that are ALREADY inside
		 * the set, we need this to propagate the "isNecessary" tag.
		 */
		necessaryMethodInvocations
				.addAll(allMethodInvocations.stream().map(new Function<MethodInvocation, MethodInvocation>() {
					@Override
					public MethodInvocation apply(MethodInvocation t) {
						// Create a clone and mark it as necessary
						MethodInvocation clonedMethodInvocation = t.clone();
						clonedMethodInvocation.setNecessary(true);
						return clonedMethodInvocation;
					}
				}).collect(Collectors.toSet()));

		/*
		 * Next extract from the traces those fragments (connected components?) that
		 * correspond to the method invocations inside necessaryMethodInvocations
		 * 
		 */

		CarvedExecution carvedExecution = new CarvedExecution();
		carvedExecution.executionFlowGraphs = this.executionFlowGraph.extrapolate(necessaryMethodInvocations);
		carvedExecution.dataDependencyGraphs = this.dataDependencyGraph.extrapolate(necessaryMethodInvocations);
		carvedExecution.callGraphs = this.callGraph.extrapolate(necessaryMethodInvocations);

		carvedExecutions.add(carvedExecution);

		return carvedExecutions;
	}

	@Override
	public Map<MethodInvocation, List<CarvedExecution>> carve(List<MethodInvocation> methodInvocations)
			throws CarvingException, ABCException {
		// TODO Make sure we reset the method invocations objects at some point....
		Map<MethodInvocation, List<CarvedExecution>> carvedExecutions = new HashMap<MethodInvocation, List<CarvedExecution>>();

		for (MethodInvocation methodInvocation : methodInvocations) {
			carvedExecutions.put(methodInvocation, carve(methodInvocation));
		}

		return carvedExecutions;
	}

}
