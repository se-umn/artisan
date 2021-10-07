package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(BasicCarver.class);
            
	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	// Bookkeep the carved method invocations.
	private Set<MethodInvocation> carvedMethodInvocationsCache = new HashSet<MethodInvocation>();

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
		Set<MethodInvocation> relevantMethodInvocations = new HashSet<>();

		// The first relevant method invocation is the one to carve
		relevantMethodInvocations.add(methodInvocation);

		if (isMethodInvocationAlreadyCarved(methodInvocation)) {
			// Return an empty result
			return relevantMethodInvocations;
		}

		/*
		 * Relevant method invocations are the ones that have been called before
		 * on the SAME object, including the constructor used to instantiate it.
		 * 
		 * TODO Note that System-level factory methods may be considered necessary at this point
		 */
		if (!methodInvocation.isStatic()) {
			final ObjectInstance methodInvocationOwner = methodInvocation.getOwner();

			/*
			 * Locate all the method invocations that have been invoked on the same object
			 * before invoking methodInvocation
			 */
			relevantMethodInvocations.addAll(this.executionFlowGraph.getMethodInvocationsBefore(methodInvocation,
					/*
					 * Make sure we compare always the non-null with the possibly null otherwise it
					 * fails silenty and return an empyt set
					 */
					t -> methodInvocationOwner.equals(t.getOwner())));
		} 
		
		
		/*
		 * The last relevant method invocations are the one that have been called by the
		 * objects used by this methodInvocation at some point
		 */
		Map<ObjectInstance, List<MethodInvocation>> relevantUses = new HashMap<>();
		
		/*
		 * Regular Uses
		 */
		for (MethodInvocation relevantMethodInvocation : relevantMethodInvocations) {
			for (DataNode parameter : this.dataDependencyGraph.getParametersOf(relevantMethodInvocation)) {
				if (parameter instanceof NullInstance) {
					continue;
				}
				if (parameter instanceof ObjectInstance) {
					ObjectInstance objectParameter = (ObjectInstance) parameter;
					relevantUses.putIfAbsent(objectParameter, new ArrayList<>());
					relevantUses.get(objectParameter).add(relevantMethodInvocation);
				}
			}
			//
			for (DataNode implicitDataDependency : this.dataDependencyGraph.getImplicitDataDependenciesOf(relevantMethodInvocation)) {
                if (implicitDataDependency instanceof NullInstance) {
                    continue;
                }
                if (implicitDataDependency instanceof ObjectInstance) {
                    ObjectInstance objectParameter = (ObjectInstance) implicitDataDependency;
                    relevantUses.putIfAbsent(objectParameter, new ArrayList<>());
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
		cacheCarvedMethodInvocation(methodInvocation);

		// Prepare the work by enqueueing all the relevant method invocations found so
		// far that have not yet been carved
		Queue<MethodInvocation> queue = new LinkedList<>();

		// Sort the method invocations so the last one would leverage that the first
		// have been carved already.
		List<MethodInvocation> orderedRelevantMethodInvocations = new ArrayList<>(relevantMethodInvocations);
		Collections.sort(orderedRelevantMethodInvocations);

		for (MethodInvocation relevantMethodInvocation : orderedRelevantMethodInvocations) {
			if (!isMethodInvocationAlreadyCarved(relevantMethodInvocation)) {
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

	private void cacheCarvedMethodInvocation(MethodInvocation methodInvocation) {
		carvedMethodInvocationsCache.add(methodInvocation);

	}

	private boolean isMethodInvocationAlreadyCarved(MethodInvocation methodInvocation) {
		return carvedMethodInvocationsCache.contains(methodInvocation);
	}

	@Override
	public List<CarvedExecution> carve(MethodInvocation methodInvocation) throws CarvingException, ABCException {

		List<CarvedExecution> carvedExecutions = new ArrayList<CarvedExecution>();

		/*
		 * Collect and sort the method invocations necessary for the carving. Note that
		 * the findNecessaryMethodInvocations DOES NOT tag the method invocations as
		 * necessary, as those will impact future carving activities. Instead, we clone
		 * them and explicitly tag them as necessary.
		 */
		Set<MethodInvocation> necessaryMethodInvocations = findNecessaryMethodInvocations(methodInvocation).stream()
				.map(t -> {
					// Create a clone and mark it as necessary
					MethodInvocation clonedMethodInvocation = t.clone();
					clonedMethodInvocation.setNecessary(true);
					return clonedMethodInvocation;
				}).collect(Collectors.toSet());

//		System.out.println("BasicCarver.carve() Necessary method invocations:");
//		necessaryMethodInvocations.stream().forEach(System.out::println);
		/**
		 * Starting from the set of necessary method invocations we create a
		 * "consistent" execution up to what we can do. So there will be dangling
		 * objects, connected components in the graphs, and in general missing elements
		 * here and there.
		 */

		// We use a set to avoid duplicates. The algorithms below can be improved a
		// lot...
		Set<MethodInvocation> allMethodInvocations = new HashSet<>();

		/*
		 * Make sure you consider the method invocations that we have tagged as
		 * necessary method invocations here. We need them because they are tagged as
		 * necessary.
		 */
		allMethodInvocations.addAll(necessaryMethodInvocations);

//		System.out.println("");
//		System.out.println("BasicCarver.carve() Necessary method invocations (FROM ALL METHOD INVOCATIONS):");
//		allMethodInvocations.stream().filter(mi -> mi.isNecessary()).forEach(System.out::println);

		/*
		 * First collect all the method invocations that will be done as consequence of
		 * invoking the necessaryMethodInvocations. In theory, executing those method
		 * invocations should not require any additional carving.
		 */
		for (MethodInvocation necessaryMethodInvocation : necessaryMethodInvocations) {
			// We need to be sure NOT to override the method invocations marked as relevant
			// here
			for (MethodInvocation mi : callGraph.getMethodInvocationsSubsumedBy(necessaryMethodInvocation)) {
				if (!allMethodInvocations.contains(mi)) {
					allMethodInvocations.add(mi);
				}
			}
		}
//		System.out.println("");
//		System.out.println("BasicCarver.carve() Necessary method invocations (FROM ALL METHOD INVOCATIONS):");
//		allMethodInvocations.stream().filter(mi -> mi.isNecessary()).forEach(System.out::println);

		// ALESSIO: TODO Why the calls subsumed by necessary calls should be marked as
		// necessary as well?
		/*
		 * Union of the necessary method invocation set and the set of method
		 * invocations obtained by cloning the subsumed method invocations. Since the
		 * hash and equalsTo function have been re-defined for the MethodInvocation, it
		 * should hold that 'a.equals(a.clone())'
		 * 
		 * TODO Assumption: addAll does not replace the objects that are ALREADY inside
		 * the set, we need this to propagate the "isNecessary" tag.
		 */
//		necessaryMethodInvocations.addAll(allMethodInvocations.stream().map(t -> {
//			// Create a clone and mark it as necessary
//			MethodInvocation clonedMethodInvocation = t.clone();
//			clonedMethodInvocation.setNecessary(true);
//			return clonedMethodInvocation;
//		}).collect(Collectors.toSet()));

		/*
		 * Next extract from the traces those fragments (connected components?) that
		 * correspond to the method invocations inside necessaryMethodInvocations
		 * 
		 */

		CarvedExecution carvedExecution = new CarvedExecution();
		// How do we ensure that whatever we extrapolate from the graphs belong
		// together? We order method invocations per id of the first call?
		carvedExecution.executionFlowGraphs = this.executionFlowGraph.extrapolate(allMethodInvocations);
		carvedExecution.dataDependencyGraphs = this.dataDependencyGraph.extrapolate(allMethodInvocations);
		carvedExecution.callGraphs = this.callGraph.extrapolate(allMethodInvocations);

		// DEBUG CHECK FOR isNecessary
//		System.out.println("BasicCarver.carve() DEBUG NECESSARY: ");
//		for( ExecutionFlowGraph executionFlowGraph : carvedExecution.executionFlowGraphs ) {
//			for( MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations() ) {
//				System.out.println( ( mi.isNecessary() ? "*" : "-") + " " + mi );
//			}
//		}
//		System.out.println("");
//		System.out.println("BasicCarver.carve() DEBUG NECESSARY: ");
//		for( DataDependencyGraph dataDependencyGraph : carvedExecution.dataDependencyGraphs ) {
//			List<MethodInvocation> sorted = new ArrayList<MethodInvocation>( dataDependencyGraph.getAllMethodInvocations());
//			Collections.sort(sorted);
//			for( MethodInvocation mi : sorted ) {
//				System.out.println( ( mi.isNecessary() ? "*" : "-") + " " + mi );
//			}
//		}
//		System.out.println("");
//		
//		System.out.println("BasicCarver.carve() DEBUG NECESSARY: ");
//		for( CallGraph callGraph: carvedExecution.callGraphs) {
//			List<MethodInvocation> sorted = new ArrayList<MethodInvocation>( callGraph.getAllMethodInvocations());
//			Collections.sort(sorted);
//			for( MethodInvocation mi : sorted ) {
//				System.out.println( ( mi.isNecessary() ? "*" : "-") + " " + mi );
//			}
//		}
//		System.out.println("");

		carvedExecution.methodInvocationUnderTest = methodInvocation;

		carvedExecutions.add(carvedExecution);

		return carvedExecutions;
	}

	// TODO Does this have to be a LIST for carving? I do not think so...
	@Override
	public Map<MethodInvocation, List<CarvedExecution>> carve(List<MethodInvocation> methodInvocations)
			throws CarvingException, ABCException {

		Map<MethodInvocation, List<CarvedExecution>> carvedExecutions = new HashMap<>();

		for (MethodInvocation methodInvocation : methodInvocations) {
			// Unless we want to push caching further, just clear the cache here
			clearTheCache();

			carvedExecutions.put(methodInvocation, carve(methodInvocation));
		}

		return carvedExecutions;
	}

	public void clearTheCache() {
		carvedMethodInvocationsCache.clear();
	}

}
