package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.CallGraphImpl;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.exceptions.NotALevel0TestCaseException;

/**
 * This carved uses an heuristic to select a single CARVING strategy when
 * multiple solutions to setup pre-conditions exist
 * 
 * @author gambi
 *
 */
public class Level_0_MethodCarver implements MethodCarver {

	private final Logger logger = LoggerFactory.getLogger(Level_0_MethodCarver.class);

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	// Store partial carving results
	private Map<MethodInvocation, List<MethodInvocation>> preconditionCache = new HashMap<>();

	/**
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @param callGraph
	 */
	public Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
	}

	/**
	 * 
	 * Level 0 means that we cannot invoke any "primitive" (?) method or rely on
	 * knowledge about the internals of the CUT
	 * 
	 * We generate several test cases for the same MUT if the parameters comes from
	 * different level (see https://en.wikipedia.org/wiki/Law_of_Demeter) up to the
	 * level of the current MUT ?
	 * 
	 * The point is: a parameter is generated and modified at some location, and we
	 * need to get it. We can either replicate all the invocations on it (and the
	 * ones needed there) but also we can go up one level, and invoke whatever
	 * method provided that instance (at last) as return value
	 * 
	 * We create a new test for method which return any parameters handled by MUT.
	 * This can be simply done by including that method before removing subsumed
	 * calls
	 * 
	 * @param methodInvocationToCarve
	 * @return
	 * @throws ABCException
	 */
	@Override
	public List<CarvedExecution> carve(MethodInvocation methodInvocationToCarve) throws ABCException {
		// Build the context for the carving. At the beginning the context IS
		// the entire trace.
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph, callGraph);

		// Include all the external interfaces..
		boolean skipExternalInterfaces = false;

		// This is the actual implementation of method carving
		return null;
		// TODO ! FIX ME
//		return level0TestCarving(methodInvocationToCarve, context, skipExternalInterfaces);
	}

	// Public only for testability
	public List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> level0TestCarving(
			MethodInvocation methodInvocationToCarve,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context, boolean skipExternalInterfaces)
			throws ABCException {
		//

		// This creates the root of the tree and invoke the recursive method
		List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTests = new ArrayList<>();

		/*
		 * A task is a pair made of work done (first), and work to be done (second)
		 */
		Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList = new LinkedList<>();

		// So the context is the
		// Collect the calls to the E.I.
		Set<MethodInvocation> methosInvocationsToExternalInterfaces = context.getFirst()
				.getMethodInvocationsToExternalInterfaceBefore(methodInvocationToCarve);
		// FIXME Collect explicit calls to setup the test
		// Set<MethodInvocation> testSetupMethosInvocations =
		// context.getFirst().getTestSetupMethodInvocations();

		// What do we need to carve
		Set<MethodInvocation> workToDo = new HashSet<MethodInvocation>();
		workToDo.add(methodInvocationToCarve);
		//
		workToDo.addAll(methosInvocationsToExternalInterfaces);
		// FIXME: Cannot remember the meaning of this, probably reduce the size
		// of the data deps by compressing setup calls
		// workToDo.addAll(testSetupMethosInvocations);

		// Empty
		Set<MethodInvocation> workDone = new HashSet<MethodInvocation>();

		// Add the MUT as task in the work list
		Pair<Set<MethodInvocation>, Set<MethodInvocation>> mutTask = new Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
				workDone, workToDo);

		// Schedule the task for the task
		workList.add(mutTask);

		// Pass everything along
		// This produces the "real" carved tests, that is, those invocations
		// which
		// recreate the usage of the CUT. Those, however, might miss
		// preconditions (e.g., setup of DB)
		// provided by external interfaces.
		//

		// THIS ONE IS THE ONE WHICH ACTUALLYS COMPUTE THE CARVE using recursion
		// and FULL CARTESIAN

		// We cannot simply skip cartesian, because there's private methods
		// which are invoked otherwise !
		// For example, private constructors instead of factory methods !
		// boolean skipCartesian = false;
		boolean skipCartesian = true;
		long start = System.currentTimeMillis();
		level0TestCarving(workList, carvedTests, context, skipCartesian);
		long end = System.currentTimeMillis();
		logger.info("\t Carving took " + (end - start));
		// Ensure that any required testSetupCall, which is not yet included in
		// the carved test, is taken into account,
		// including its own carved preconditions
		// start = System.currentTimeMillis();
		// Include the user defined test setup calls. Those are by definition
		// disjointed from the actual test execution !
		// Roughly, they correspond to @Before calls

		// FIXME: What about this one ?!
		// includeTestSetupCalls(carvedTests, context);

		// end = System.currentTimeMillis();
		// logger.info("\t Core Test Setup Calls took " + (end - start));
		//
		// start = System.currentTimeMillis();
		// includeCallsToExternalInterfaces(carvedTests, context);
		// end = System.currentTimeMillis();
		// logger.info("\t Carving External Interface took " + (end - start));

		return carvedTests;
	}

	// public void includeTestSetupCalls(List<Pair<ExecutionFlowGraph,
	// DataDependencyGraph>> carvedTests,
	// Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context)
	// throws CarvingException {
	//
	// // Collect the testSetupMethodInvocations from the context
	// Set<MethodInvocation> testSetupMethosInvocations =
	// context.getFirst().getTestSetupMethodInvocations();
	//
	// // If the method invocation is not there yet, include it.
	// for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest
	// :
	// carvedTests) {
	//
	// for (MethodInvocation testSetupFromContext : testSetupMethosInvocations)
	// {
	//
	// if (!carvedTest.getFirst().contains(testSetupFromContext)) {
	// logger.trace("Level_0_MethodCarver.includeTestSetupCalls() INCLUDING Test
	// Setup Call "
	// + testSetupFromContext + " in carved test "
	// + carvedTest.getFirst().getOrderedMethodInvocations());
	//
	// // Build the testSetupContext1
	// List<MethodInvocation> invocationsBefore = context.getFirst()
	// .getOrderedMethodInvocationsBefore(testSetupFromContext);
	// ExecutionFlowGraph first =
	// context.getFirst().getSubGraph(invocationsBefore);
	// DataDependencyGraph second =
	// context.getSecond().getSubGraph(invocationsBefore);
	// //
	// CallGraph third = context.getThird().getSubGraph(testSetupFromContext);
	// //
	// Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>
	// testSetupContext = new Triplette<ExecutionFlowGraph, DataDependencyGraph,
	// CallGraph>(
	// first, second, third);
	//
	// //
	// Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList = new
	// LinkedList<>();
	//
	// // Add the MUT as task in the work list
	// Pair<Set<MethodInvocation>, Set<MethodInvocation>> mutTask = new
	// Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
	// new HashSet<MethodInvocation>(),
	// new
	// HashSet<MethodInvocation>(Collections.singleton(testSetupFromContext)));
	// //
	// workList.add(mutTask);
	// }
	// }
	//
	// List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>
	// carvedPreconditions =
	// new ArrayList<>();
	//
	// boolean skipCartesian = true;
	// level0TestCarving(workList, carvedPreconditions, testSetupContext,
	// skipCartesian);
	//
	// //
	// Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>
	// carvedPrecondition =
	// carvedPreconditions.iterator().next();
	// preconditionCache.put(testSetupFromContext,
	// carvedPrecondition.getFirst().getOrderedMethodInvocations());
	// logger.trace("Level_0_MethodCarver.includeTestSetupCalls() PRECONDITION
	// for " + testSetupFromContext
	// + " STORED IN THE CACHE");
	// // }
	//
	// //
	// // In case there's more than one because of cartesian
	// // product, pick the first one.
	// // NOTE THAT THERE"S SHOULD BE NO DATA DEPS OR SIMILAR INTO
	// // THE CARVE PRECONDITIONS !
	// Set<MethodInvocation> mergedSorted = new
	// HashSet(carvedTest.getFirst().getOrderedMethodInvocations());
	// // Return this from the cache !
	// mergedSorted.addAll(preconditionCache.get(testSetupFromContext));
	//
	// List<MethodInvocation> orderedSlice = new ArrayList<>(mergedSorted);
	// Collections.sort(orderedSlice);
	// //
	// carvedTest.setFirst(context.getFirst().getSubGraph(orderedSlice));
	// carvedTest.setSecond(context.getSecond().getSubGraph(orderedSlice));
	//
	// }
	//
	// }

	//// TODO: WHY DO NOT WE USE THE SAME APPROACH FOR CARVING REGULAR METHODS?
	//// We HAVE the work list, we can simply start from there, isn't it?
	public void includeCallsToExternalInterfaces(
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTests,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context) throws ABCException {

		// If the method invocation is not there yet, include it.
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest : carvedTests) {

			// Collect the testSetupMethodInvocations from the context
			MethodInvocation methodInvocationToCarve = carvedTest.getFirst().getLastMethodInvocation();
			//
			Set<MethodInvocation> methosInvocationsToExternalInterfaces = context.getFirst()
					.getMethodInvocationsToExternalInterfaceBefore(methodInvocationToCarve);

			//
			Set<MethodInvocation> subsumedCallByCarvedTest = new HashSet<>();
			for (MethodInvocation mi : carvedTest.getFirst().getOrderedMethodInvocations()) {
				subsumedCallByCarvedTest.add(mi);
				subsumedCallByCarvedTest.addAll(context.getThird().getMethodInvocationsSubsumedBy(mi));
			}

			for (MethodInvocation methodInvocationToExternalInterface : methosInvocationsToExternalInterfaces) {

				if (!subsumedCallByCarvedTest.contains(methodInvocationToExternalInterface)) {
					logger.trace("Level_0_MethodCarver.includeTestSetupCalls() INCLUDING Call to E.I. "
							+ methodInvocationToExternalInterface + " in carved test "
							+ carvedTest.getFirst().getOrderedMethodInvocations());

					// Build the testSetupContext1
					List<MethodInvocation> invocationsBefore = context.getFirst()
							.getOrderedMethodInvocationsBefore(methodInvocationToExternalInterface);
					ExecutionFlowGraph first = context.getFirst().getSubGraph(invocationsBefore);
					DataDependencyGraph second = context.getSecond().getSubGraph(invocationsBefore);
					//
					CallGraph third = context.getThird().getSubGraph(methodInvocationToExternalInterface);
					//
					Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> testSetupContext = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
							first, second, third);

					//
					Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList = new LinkedList<>();

					// Add the MUT as task in the work list
					Pair<Set<MethodInvocation>, Set<MethodInvocation>> mutTask = new Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
							new HashSet<MethodInvocation>(),
							new HashSet<MethodInvocation>(Collections.singleton(methodInvocationToExternalInterface)));
					//
					workList.add(mutTask);
					// THIS ONE IS THE ONE WHICH ACTUALLYS COMPUTE THE CARVE
					// using recursion
					// and FULL CARTESIAN... But do not include test setup calls
					// ;)

					List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedPreconditions = new ArrayList<>();
					//

					if (!preconditionCache.containsKey(methodInvocationToExternalInterface)) {

						/// This one is tricky ! Because of string aliasing
						/// calls to different EI, or EI already discarded might
						/// be included here as well !
						// For example, if scanner.next returns "test" multiple
						/// times and we get a deps on "test" then we get ALL
						/// the deps on it !
						boolean skipCartesian = true;
						level0TestCarving(workList, carvedPreconditions, testSetupContext, skipCartesian);
						// TODO We might need to include all of them if we have
						// problems carving preconditions with subsumed calls ?!
						// In case there's more than one because of cartesian
						// product, pick the first one.

						Iterator<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> iterator = carvedPreconditions
								.iterator();
						if (iterator.hasNext()) {
							Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedPrecondition = iterator
									.next();
							preconditionCache.put(methodInvocationToExternalInterface,
									carvedPrecondition.getFirst().getOrderedMethodInvocations());
							logger.trace("Level_0_MethodCarver.includeTestSetupCalls() PRECONDITION for "
									+ methodInvocationToExternalInterface + " STORE IN THE CACHE:\n"
									+ carvedPrecondition.getFirst().getOrderedMethodInvocations() + "");
						} else {
							logger.info("Level_0_MethodCarver.includeTestSetupCalls() CANNOT FIND PRECONDITION for "
									+ methodInvocationToExternalInterface);
						}
					}

					Set<MethodInvocation> mergedSorted = new HashSet<MethodInvocation>(
							carvedTest.getFirst().getOrderedMethodInvocations());

					// Check that no methods in the precondition will delete
					// methods already in the carved test.
					// But also check that the method is not already subsumed by
					// the carved test case !!! TO AVOID STRING ALIASING !
					boolean mergePreconditions = true;

					List<MethodInvocation> filteredPreconditionsFromCache = new ArrayList<>(
							preconditionCache.get(methodInvocationToExternalInterface));
					for (Iterator<MethodInvocation> iterator = filteredPreconditionsFromCache.iterator(); iterator
							.hasNext();) {
						//
						MethodInvocation precondition = iterator.next();
						Set<MethodInvocation> subsumedByPrecondition = context.getThird()
								.getMethodInvocationsSubsumedBy(precondition);
						// Precondition might be already there, so we are fine..
						subsumedByPrecondition.remove(precondition);

						if (!Collections.disjoint(subsumedByPrecondition,
								carvedTest.getFirst().getOrderedMethodInvocations())) {
							logger.trace(
									"\n\n >>>>> Level_0_MethodCarver.includeCallsToExternalInterfaces() Precondition "
											+ precondition + " subsumes calls already in the carved test !! \n\n");
							mergePreconditions = false;
							break;
						} else if (subsumedCallByCarvedTest.contains(precondition)) {
							// This might be caused by String aliasing !
							logger.trace(
									"\n\n >>>>> Level_0_MethodCarver.includeCallsToExternalInterfaces() Precondition "
											+ precondition + " subsumed by test DO NOT INCLUDE IT TWICE ! \n\n");
							iterator.remove();
						}
					}

					if (mergePreconditions) {
						// Return this from the cache !
						mergedSorted.addAll(filteredPreconditionsFromCache);

						List<MethodInvocation> orderedSlice = new ArrayList<>(mergedSorted);
						Collections.sort(orderedSlice);
						//
						carvedTest.setFirst(context.getFirst().getSubGraph(orderedSlice));
						carvedTest.setSecond(context.getSecond().getSubGraph(orderedSlice));
					}

				} else {
					logger.trace("Level_0_MethodCarver.includeTestSetupCalls() Cannot include Call to E.I. "
							+ methodInvocationToExternalInterface + " in carved test.");
					logger.trace("Subsumed by: " + context.getThird()
							.getOrderedSubsumingMethodInvocationsFor(methodInvocationToExternalInterface));
				}
			}
		}

	}

	/**
	 * We want to extract the least amount of method invocation as possible which
	 * can recreate the state of the external dependencies. We do like this: - We
	 * get the list of External Invocations before MUT -> WORK LIST - We take the
	 * last one from WORKLIST - we get the parent invocation of this one if the
	 * parent contains MUT (or any instruction of the CARVED TEST?) - Output the
	 * entry (not the parent) >> REQUIRED CALL else mark all the E.I. it subsumes as
	 * TAKEN mark the parent node as E.I. remove all the E.I. subsumed from the
	 * graphs add the parent node to WORKLIST In the end we should have the highest
	 * "calls" in the call graph which do not subsume the CUT.
	 *
	 * Carve those CALLS (with the "reduced" graph ?)
	 * 
	 * @param carvedTest
	 * 
	 * @param carvedTests
	 * @throws ABCException
	 */
	private List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> processExternalInterfaces(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest, //
			MethodInvocation methodToCarve) throws ABCException {

		// TODO ADD CACHING HERE !!

		logger.info("=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+\n"
				+ "Level_0_MethodCarver.processExternalInterfaces() Processing External interface for " + methodToCarve
				+ "\n" + "=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+\n");

		List<MethodInvocation> externalInterfaceInvocations = executionFlowGraph
				.getOrderedMethodInvocationsToExternalInterfaceBefore(methodToCarve);

		logger.trace("Level_0_MethodCarver.processExternalInterfaces() " + externalInterfaceInvocations);

		Collections.reverse(externalInterfaceInvocations);

		logger.trace("Level_0_MethodCarver.processExternalInterfaces() Size " + externalInterfaceInvocations.size());
		// Remove from this list all the external interface invocations that are
		// ALREADY in the carved test
		externalInterfaceInvocations.removeAll(carvedTest.getFirst().getOrderedMethodInvocations());
		logger.trace("Level_0_MethodCarver.processExternalInterfaces() Size after removing Carved test "
				+ externalInterfaceInvocations.size());

		Queue<MethodInvocation> worklist = new LinkedList<>(externalInterfaceInvocations);
		Set<MethodInvocation> bookeeping = new HashSet<>();

		// Create a subgraph which contains only the nodes BEFORE MUT
		CallGraph subGraph = callGraph.getSubGraph(methodToCarve);

		Set<MethodInvocation> compressedSetupCalls = new HashSet<>();

		while (!worklist.isEmpty()) {

			MethodInvocation methodUnderInspection = worklist.poll();
			logger.trace("Level_0_MethodCarver.processExternalInterfaces() Inspecting E.I. method "
					+ methodUnderInspection + " " + methodUnderInspection.belongsToExternalInterface());

			if (bookeeping.contains(methodUnderInspection)) {
				logger.debug(
						"Level_0_MethodCarver.processExternalInterfaces() Alredy covered " + methodUnderInspection);
				continue;
			} else {
				// Add this call to the bookeeping.
				bookeeping.add(methodUnderInspection);
			}

			if (carvedTest.getFirst().contains(methodUnderInspection)) {
				logger.info(methodUnderInspection + " is already in the carved test");
				continue;
			} else {
				logger.info(methodUnderInspection + " not in the carved test");
			}

			// Process the worklist
			MethodInvocation parent = subGraph.getCallerOf(methodUnderInspection);
			//
			// Probably here we need to consider ALL the methods in the
			// CarvedTest !

			if (parent == null ||
			// If parent is NOT disjoint from carved test, which are considering
			// the same calls multiple times
			// So we stop there...
					!Collections.disjoint(subGraph.getMethodInvocationsSubsumedBy(parent),
							carvedTest.getFirst().getOrderedMethodInvocations())) {

				// // FIXME it is not true that if parent is null then
				// // automatically we can subsume the other call.
				// // In fact using @Before methods break this, since their call
				// is
				// // not captured (JUnit invoke them)
				// // while their execution is tracked. But its not true that
				// they
				// // have the same parent !!
				//
				logger.info("Level_0_MethodCarver.processExternalInterfaces() " + parent + " DISJOINT FROM "
						+ methodToCarve + " start the carving of: " + methodUnderInspection);
				// // Carve the Method we found on the simplified graph...
				methodUnderInspection.setBelongsToExternalInterface(true);
				compressedSetupCalls.add(methodUnderInspection);
				//
				continue;
			} else {
				// Compute the intersection of the method subsumed by parent
				// and the one external interface
				// to get the element of the workList that are already
				// covered
				Set<MethodInvocation> covered = subGraph.getMethodInvocationsSubsumedBy(parent);
				// Why this ?
				// covered.retainAll(externalInterfaceInvocations);
				// logger.trace(
				// "Level_0_MethodCarver.processExternalInterfaces() External
				// Interface automatically covered "
				// + covered);
				bookeeping.addAll(covered);
				// Propagate the info on External Interface and prune the
				// graph
				subGraph.markParentAndPruneAfter(parent);
				//
				// logger.trace("Level_0_MethodCarver.processExternalInterfaces()
				// >> queueing " + parent);
				worklist.add(parent);
			}
		}

		logger.info(
				"\n\nLevel_0_MethodCarver.processExternalInterfaces() Preparing the carving of the Compressed Setup calls "
						+ "" + compressedSetupCalls);

		// At this point the subGraph is a simplified version where nodes are
		// "compressed".
		// On this one, we need to carve the calls to the external interfaces.

		// Build the context...for this carving...
		// The context has less nodes of the original, and different
		// nodes are marked with setBelongToExternalInterface !

		List<MethodInvocation> orderedSlice = new ArrayList<>(subGraph.getAll());
		Collections.sort(orderedSlice);

		logger.info("Level_0_MethodCarver.processExternalInterfaces() Refined context size " + orderedSlice.size());
		//
		ExecutionFlowGraph _executionFlowGraph = executionFlowGraph.getSubGraph(orderedSlice);
		DataDependencyGraph _dataDependencyGraph = dataDependencyGraph.getSubGraph(orderedSlice);

		// This one is the one we already computes but we need to update
		// the context to propagate the belongToInterface flag...
		CallGraph _callGraph = subGraph;

		for (MethodInvocation mi : subGraph.getAll()) {
			if (mi.belongsToExternalInterface()) {
				logger.trace("Level_0_MethodCarver.processExternalInterfaces() Propagate the E.I. marker for " + mi
						+ " to refined context");
				_executionFlowGraph.markNodeAsExternalInterface(mi);
				_dataDependencyGraph.markNodeAsExternalInterface(mi);
			}
		}

		// The context is the same for all the external compressedSetupCalls
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> compressedContext = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				_executionFlowGraph, _dataDependencyGraph, _callGraph);

		// Accumulate those here...
		List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> partialCarvedExternalInterfaces = new ArrayList<>();

		boolean skipExternalInterfaces = true;
		for (MethodInvocation setupCall : compressedSetupCalls) {
			partialCarvedExternalInterfaces
					.addAll(level0TestCarving(setupCall, compressedContext, skipExternalInterfaces));
		}

		// logger.trace("Level_0_MethodCarver.processExternalInterfaces()
		// Merging Carved Preconditions: ");
		// // Maybe is enough to collect the Executions
		// Set<MethodInvocation> merge = new HashSet<>();
		// for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>
		// partialCarvedExternalInterface : partialCarvedExternalInterfaces) {
		// merge.addAll(partialCarvedExternalInterface.getFirst().getOrderedMethodInvocations());
		// }
		// //
		// List<MethodInvocation> mergedSlice = new ArrayList<>(merge);
		// Collections.sort(mergedSlice);
		// ExecutionFlowGraph mergedExecutionFlowGraph =
		// executionFlowGraph.getSubGraph(mergedSlice);
		// DataDependencyGraph mergedDataDependencyGraph =
		// dataDependencyGraph.getSubGraph(mergedSlice);

		return partialCarvedExternalInterfaces;
	}

	/**
	 * Check that we are not removing calls that are indeed needed. ATM, we can rely
	 * only on heuristics. For example, if we subsume an <init> we need to be sure
	 * the test does not call directly that particular object !
	 * 
	 * @param slice
	 * @param _callGraph
	 * @return
	 * @throws ABCException
	 * @throws CarvingException
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> generateCarvedTestFromSlice(
			Set<MethodInvocation> slice, CallGraph _callGraph) throws ABCException {
		// Order the slice.
		List<MethodInvocation> orderedSlice = new ArrayList<>(slice);

		MethodInvocation methodInvocationToCarve = Collections.max(orderedSlice);

		Collections.sort(orderedSlice);
		// The last method is the one to carve

		logger.debug("\t\t generateCarvedTestFromSlice() for " + methodInvocationToCarve + " from " + orderedSlice);

		// Eventually remove the subsumed Calls
		Set<MethodInvocation> subsumedCalls = new HashSet<>();
		for (MethodInvocation mi : orderedSlice) {
			// Filter out the calls that are subsumed by the call graph
			Set<MethodInvocation> subsumedBy = _callGraph.getMethodInvocationsSubsumedBy(mi);
			subsumedCalls.addAll(subsumedBy);
		}

		/*
		 * If the subsumed call is also the method under carving, the resulting test
		 * case cannot be defined as Level_0. So we stop its generation directly here.
		 * Not that we could simply return null, or something, but this way its clear
		 * what did happen.
		 * 
		 */
		if (subsumedCalls.contains(methodInvocationToCarve)) {
			// If this happens, the methodInvocation to carve will be removed
			// from the test... which is wrong...

			List<MethodInvocation> subsumingCalls = _callGraph
					.getOrderedSubsumingMethodInvocationsFor(methodInvocationToCarve);
			// Keep only the ones that belong to the slice
			subsumingCalls.retainAll(slice);

			// This should be only 1 - This fails for Hotelme.Room ?!
			// assert subsumingCalls.size() == 1;
			if (subsumingCalls.size() != 1) {
				logger.warn("Wrong subsumingCalls count " + subsumingCalls.size() + " instead of 1");
			}
			MethodInvocation subsumingMethodInvocation = subsumingCalls.iterator().next();
			List<MethodInvocation> subsumingPath = _callGraph.getSubsumingPathFor(subsumingMethodInvocation,
					methodInvocationToCarve);

			logger.info("GenerateCarvedTestFromSlice() Cannot generate for " + methodInvocationToCarve + " from "
					+ orderedSlice + " Not a LEVEL 0 TEST CASE");
			throw new NotALevel0TestCaseException(methodInvocationToCarve, subsumingMethodInvocation, subsumingPath);

		} else {

			// Check we do not subsumedCalls an <init> that also belongs to
			// slice
			subsumedCalls.retainAll(slice);

			for (MethodInvocation subsumedConstructor : subsumedCalls) {

				if (JimpleUtils.getMethodName(subsumedConstructor.getMethodSignature()).equals("<init>")) {
					logger.debug("Level_0_MethodCarver.generateCarvedTestFromSlice() We are subsuming method "
							+ subsumedConstructor);

					// Given than soot reports calls to all the super types, if
					// the owner of the subsumed method is the same of the
					// subsuming
					// we mark those as safe. Basically, it will be always the
					// case the a subtype.init subsumes the supertype.init (via
					// implicit super())
					//

					// Find if there's any other method in the slice which
					// returns the same object
					// These do not include INIT !
					List<MethodInvocation> callsWhichReturnTheObject = new ArrayList<>(
							dataDependencyGraph.getMethodInvocationsWhichReturn(subsumedConstructor.getOwner()));
					// Consider only the one in the slice
					callsWhichReturnTheObject.retainAll(slice);
					// But remove ALL the methods which will be subsumed
					callsWhichReturnTheObject.removeAll(subsumedCalls);
					// Sort the remaining
					Collections.sort(callsWhichReturnTheObject);
					// Take the first if any.
					MethodInvocation firstCallInTheSliceWhichReturnTheObject = (callsWhichReturnTheObject.isEmpty())
							? null
							: callsWhichReturnTheObject.get(0);

					// Ensure that there's no access to the owner of that
					// method, but only write
					// Get the method calls for those object that are also in
					// the slice
					List<MethodInvocation> callsOnTheObject = new ArrayList<>(
							dataDependencyGraph.getMethodInvocationsForOwner(subsumedConstructor.getOwner()));
					// Sort by execution time
					Collections.sort(callsOnTheObject);
					// Remove the constructor - this is gonna be subsumed and it
					// should be the FIRST METHOD
					callsOnTheObject.remove(subsumedConstructor);
					// Take only the calls which appear in the slice
					callsOnTheObject.retainAll(slice);
					// But remove ALL the methods which will be subsumed
					callsOnTheObject.removeAll(subsumedCalls);

					// Consider only the calls, if any that are BEFORE
					// firstCallInTheSliceWhichReturnTheObject
					List<MethodInvocation> unsafeCalls = new ArrayList<>(callsOnTheObject);
					if (firstCallInTheSliceWhichReturnTheObject != null) {
						for (Iterator<MethodInvocation> iterator = unsafeCalls.iterator(); iterator.hasNext();) {
							if (firstCallInTheSliceWhichReturnTheObject.isBefore(iterator.next())) {
								iterator.remove();
							}
						}
					}

					if (!unsafeCalls.isEmpty()) {
						// At this point, whatever call in inside unsafeCalls
						// is an access to an object which is not initialized in
						// the test code.

						// We try to recover this by looking for methods of the
						// "subsumer" object which can
						// replace one or more of those calls. Since we need to
						// ensure those calls are made, but
						// not necessarily directly by the test code

						// Computing the calls which subsumed <init>
						List<MethodInvocation> subsumingCalls = _callGraph
								.getOrderedSubsumingMethodInvocationsFor(subsumedConstructor);
						// Keep only the ones that belong to the slice
						subsumingCalls.retainAll(slice);

						// Check if among the subsuming methods one fits our
						// goal
						Set<MethodInvocation> replacementCalls = new HashSet<>();
						//
						for (MethodInvocation subsumingMethodInvocation : subsumingCalls) {
							if (subsumingMethodInvocation.isStatic()) {
								logger.warn(
										"Level_0_MethodCarver.generateCarvedTestFromSlice() Static methods are not yet covered: "
												+ subsumingMethodInvocation);
								continue;
							}
							// TODO Static methods are not covered !
							ObjectInstance subsumingOwner = dataDependencyGraph.getOwnerFor(subsumingMethodInvocation);
							logger.trace("Inspecting subsuming call " + subsumingMethodInvocation + " with owner "
									+ subsumingOwner);
							// Does this object have a method which subsumes the
							// unsafe calls ?
							// Get all the objects of this owner BEFORE unsafe
							// calls.

							for (MethodInvocation methodsOfSubsumingOwner : dataDependencyGraph
									.getMethodInvocationsForOwner(subsumingOwner)) {

								logger.trace("Owner has potential call " + methodsOfSubsumingOwner);

								for (Iterator<MethodInvocation> unsafeIterator = unsafeCalls.iterator(); unsafeIterator
										.hasNext();) {
									MethodInvocation unsafe = unsafeIterator.next();

									if (subsumingOwner.equals(unsafe.getOwner())) {
										// Calls that belong to the same owner
										// are safe.
										unsafeIterator.remove();
									} else if (methodsOfSubsumingOwner.isAfter(unsafe)) {
										// Skip calls later than usafe calls
										continue;
									} else if (callGraph.getMethodInvocationsSubsumedBy(methodsOfSubsumingOwner)
											.contains(unsafe)) {
										logger.debug(
												" Method " + methodsOfSubsumingOwner + " subsumes unsafe " + unsafe);
										replacementCalls.add(methodsOfSubsumingOwner);
										unsafeIterator.remove();
									}
								}
							}
						}

						logger.debug(
								"Level_0_MethodCarver.generateCarvedTestFromSlice() Found the following REPLACEMENT calls: \n"
										+ replacementCalls);

						if (!unsafeCalls.isEmpty()) {
							// If at this point there's still unsafe calls we
							// cannot do more than that

							throw new ABCException("GenerateCarvedTestFromSlice() Cannot generate for "
									+ methodInvocationToCarve + " from " + orderedSlice + "\n"
									+ "There are unsafe operations: " + unsafeCalls);
						} else {
							// Include the replacement calls and hope the tests
							// are fine ! :D
							slice.addAll(replacementCalls);
							// TODO We shall definitively repeat this check ...
							// Check that we did not have subsumed the MUT !

							// Refactor this
							// Order the slice.
							orderedSlice = new ArrayList<>(slice);
							methodInvocationToCarve = Collections.max(orderedSlice);
							Collections.sort(orderedSlice);
							// Eventually remove the subsumed Calls
							subsumedCalls = new HashSet<>();
							for (MethodInvocation mi2 : orderedSlice) {
								// Filter out the calls that are subsumed by the
								// call graph
								Set<MethodInvocation> subsumedBy = _callGraph.getMethodInvocationsSubsumedBy(mi2);
								subsumedCalls.addAll(subsumedBy);
							}
							if (subsumedCalls.contains(methodInvocationToCarve)) {
								throw new NotALevel0TestCaseException(methodInvocationToCarve,
										"We accidentally subsumed MUT after adding replacement calls !");
							}

						}

					}

					// If not, try to recover this by checking if in the
					// subsuming calls there's an object whose call can
					// replace some calls in the slice via subsumption

				}
			}
		}

		// Delete from the slice all those calls that will be done nevertheless
		Iterator<MethodInvocation> iterator = subsumedCalls.iterator();
		while (iterator.hasNext()) {
			MethodInvocation toRemove = iterator.next();
			logger.trace("\t -- Removing " + toRemove);
			orderedSlice.remove(toRemove);
		}

		logger.trace("\t >>>> Generate the following test from slice : \n" + orderedSlice);

		return new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph.getSubGraph(orderedSlice), dataDependencyGraph.getSubGraph(orderedSlice),
				// TODO CallGraph was never intended to be used here, as this is something we
				// might need to compute ourselves?
				new CallGraphImpl());
	}

	// Each of this tests ends with a call to the method to carve.
	public void level0TestCarving(
			// This is the node to expand
			Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList, //
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTests, //
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context, //
			boolean skipCartesian) throws ABCException {

		ExecutionFlowGraph _executionFlowGraph = context.getFirst();
		DataDependencyGraph _dataDependencyGraph = context.getSecond();
		CallGraph _callGraph = context.getThird();

		while (!workList.isEmpty()) {
			Pair<Set<MethodInvocation>, Set<MethodInvocation>> task = workList.poll();
			// logger.trace("\nProcessing " + task.getFirst() + " --> " +
			// task.getSecond() + " by Carving");

			if (task.getSecond().isEmpty()) {
				try {
					// This creates finally the test
					carvedTests.add(generateCarvedTestFromSlice(task.getFirst(), _callGraph));
				} catch (NotALevel0TestCaseException e) {
					e.printStackTrace();
					logger.info("Skip test generation, because test in not valid " + e.getSubsumedMethodInvocation()
							+ " was subsumed by " + e.getSubsumingMethodInvocation() + " via " + e.getSubsumingPath());
				}
			} else {
				// Process the first unit of work, not all the elements in
				// task.getSecond() !
				List<MethodInvocation> workUnits = new ArrayList<>(task.getSecond());
				Collections.sort(workUnits);
				MethodInvocation unitOfWork = workUnits.get(0);

				// This might not be really relevant since later we remove the
				// work done from the work to do, and unit of work is workdone
				//
				// System.out
				// .print("Level_0_MethodCarver.level0TestCarving()
				// task.getSecond() " + task.getSecond().size());
				if (!task.getSecond().remove(unitOfWork)) {
					throw new CarvingException("Cannot remove " + unitOfWork + " from work list");
				}
				// System.out.println(" --> " + task.getSecond().size());

				// Do the carving
				if (!preconditionCache.containsKey(unitOfWork)) {
					// logger.info("Carving: " + unitOfWork);

					// Carve the method and store the preconditions
					Map<MethodInvocation, Set<ObjectInstance>> dataDependencies = new HashMap<>();
					Set<MethodInvocation> methodDependencies = new HashSet<>();

					// Compute whatever dependencies are required by the
					// remaining
					// work units
					MethodInvocation methodInvocation = unitOfWork;

					/*
					 * 1 - Include all the invocations that happen before (Execution graph). this
					 * excludes methods that happened after MUT
					 */
					// Carve by Time => "Before"
					List<MethodInvocation> executioneBefore = _executionFlowGraph
							.getOrderedMethodInvocationsBefore(methodInvocation);

					/*
					 * 2 - Create a data dependency sub graph which contains only the data used
					 * Before the method
					 */
					DataDependencyGraph subGraphFromPastExecution = _dataDependencyGraph.getSubGraph(executioneBefore);

					/*
					 * 3 - Include all the invocations made on any data dependency of this method to
					 * ensure we set the dependencies right before calling the method
					 */

					// Do the carving by following the method invocations
					// chain
					Set<MethodInvocation> backwardSlice = new HashSet<>(executioneBefore);
					backwardSlice
							.retainAll(subGraphFromPastExecution.getMethodInvocationsRecheableFrom(methodInvocation));
					/*
					 * The backwardSlice is the slice which contains the MUT-centric view of the
					 * execution. It disregards the location of methods which have potential impa
					 */

					// logger.trace(
					// "Level_0_MethodCarver.level0TestCarving() Dependencies on
					// Methods " + backwardSlice.size());
					// logger.trace("" + backwardSlice);

					// TODO This might include calls which subsume also
					// other
					// calls !

					// Accumulate the backwardSlice in the worklist
					methodDependencies.addAll(backwardSlice);

					// Now accumulate the data dependencies for the method
					// under
					// carving
					if (methodInvocation.isStatic()) {
						// Static calls require only parameters if any
						if (JimpleUtils.getParameterList(methodInvocation.getMethodSignature()).length > 0) {

							Set<DataNode> allParameters = new HashSet<>(
									_dataDependencyGraph.getParametersOf(methodInvocation));

							Set<ObjectInstance> parameters = new HashSet<>();
							for (DataNode paramter : allParameters) {
								if (paramter instanceof ObjectInstance) {
									parameters.add((ObjectInstance) paramter);
								}
							}
							//
							// logger.trace("Level_0_MethodCarver.level0TestCarving()
							// Data Dependencies for "
							// + methodInvocation + " are " +
							// parameters.size());
							// logger.trace("Level_0_MethodCarver.level0TestCarving()
							// Data Dependencies for "
							// + methodInvocation + " are " + parameters);
							//
							dataDependencies.put(methodInvocation, parameters);
						}

					} else if (methodInvocation.getMethodSignature().contains(": void <init>(")) {
						Set<ObjectInstance> deps = new HashSet<>();
						// If the method is a constructor the preconditions
						// are
						// the parameters, no parameters means no
						// preconditions
						ObjectInstance owner = _dataDependencyGraph.getOwnerFor(methodInvocation);
						// logger.debug("Level_0_MethodCarver.level0TestCarving()
						// Owner for " + methodInvocation + " is "
						// + owner);
						deps.add(owner);
						//
						if (JimpleUtils.getParameterList(methodInvocation.getMethodSignature()).length > 0) {
							List<DataNode> allParameters = _dataDependencyGraph.getParametersOf(methodInvocation);

							Set<ObjectInstance> parameters = new HashSet<>();
							for (DataNode paramter : allParameters) {
								if (paramter instanceof ObjectInstance) {
									parameters.add((ObjectInstance) paramter);
								}
							}

							// logger.debug("Level_0_MethodCarver.level0TestCarving()
							// dependencies for " + methodInvocation
							// + " are " + parameters);
							deps.addAll(parameters);
						}
						// logger.debug("Level_0_MethodCarver.level0TestCarving()
						// Data Dependencies for "
						// + methodInvocation + " are " + deps.size());
						// logger.trace("Level_0_MethodCarver.level0TestCarving()
						// Data Dependencies for "
						// + methodInvocation + " are " + deps);
						dataDependencies.put(methodInvocation, deps);

					} else {
						Set<ObjectInstance> deps = new HashSet<>();
						ObjectInstance owner = _dataDependencyGraph.getOwnerFor(methodInvocation);
						// logger.debug("Level_0_MethodCarver.level0TestCarving()
						// Owner for " + methodInvocation + " is "
						// + owner);
						deps.add(owner);
						// Regular invocations require parameters and the
						// constructor if the constructor is not there yet
						if (JimpleUtils.getParameterList(methodInvocation.getMethodSignature()).length > 0) {
							List<DataNode> allParameters = _dataDependencyGraph.getParametersOf(methodInvocation);
							Set<ObjectInstance> parameters = new HashSet<>();
							for (DataNode paramter : allParameters) {
								if (paramter instanceof ObjectInstance) {
									parameters.add((ObjectInstance) paramter);
								}
							}

							// logger.debug("Level_0_MethodCarver.level0TestCarving()
							// dependencies for " + methodInvocation
							// + " are " + parameters);

							deps.addAll(parameters);
						}
						// logger.debug("Level_0_MethodCarver.level0TestCarving()
						// Data Dependencies for "
						// + methodInvocation + " are " + deps.size());
						// logger.trace("Level_0_MethodCarver.level0TestCarving()
						// Data Dependencies for "
						// + methodInvocation + " are " + deps);
						dataDependencies.put(methodInvocation, deps);
					}

					/*
					 * Elaborate on the possible ways to return the same object instances
					 */

					Map<ObjectInstance, Set<MethodInvocation>> methodsWhichReturnTheObjects = new HashMap<>();

					for (Entry<MethodInvocation, Set<ObjectInstance>> entry : dataDependencies.entrySet()) {
						for (ObjectInstance data : entry.getValue()) {

							if (data == null) {
								// Static calls have this..
								logger.warn("Level_0_MethodCarver.level0TestCarving() NULL DATA FOR " + entry.getKey());
								// SKIP THIS... Probably will break the test
								// generation ?!
								continue;
							}
//							// Is this patch still neeede ?
//							if (ObjectInstance.systemIn.equals(data) || //
//									ObjectInstance.systemOut.equals(data) || //
//									ObjectInstance.systemErr.equals(data)) {
//								continue;
//							}

							// Collect methods which return the instance
							Set<MethodInvocation> methodsWhichReturnTheObject = new HashSet<>();
							MethodInvocation constructor = dataDependencyGraph.getInitMethodInvocationFor(data);
							if (constructor != null) {
								/*
								 * If we match this by time we stumble upon the fact that super() is called
								 * after <init> Consider only those methods BEFORE or the SAME
								 */
								methodsWhichReturnTheObject.add(constructor);
							}

							Set<MethodInvocation> tempSet = new HashSet<>();
							// Filter out the one that are AFTER the
							// dependency
							for (MethodInvocation returning : dataDependencyGraph
									.getMethodInvocationsWhichReturn(data)) {

								if (returning.getInvocationCount() <= entry.getKey().getInvocationCount()) {
									tempSet.add(returning);
								}
							}

							// We need to pick the CLOSEST one, which is not
							// necessarily the LAST one (since calls are nested)
							// !
							if (!tempSet.isEmpty()) {

								MethodInvocation closest = null;
								if (tempSet.size() == 1) {
									closest = tempSet.iterator().next();
								} else {

									int target = entry.getKey().getInvocationCount();

									Set<MethodInvocation> ranked = null;

									// What about negative, positive ?!
									int distance = Integer.MAX_VALUE;
									for (MethodInvocation mi : tempSet) {
										// int current =
										// callGraph.distanceToRoot(mi);
										int current = mi.getInvocationCount();

										if (target - current < distance && target - current > 0) {
											distance = target - current;
											ranked = new HashSet<>();
											ranked.add(mi);
											// logger.trace("Level_0_MethodCarver.level0TestCarving()
											// New distance "
											// + distance + " for " + mi);
										}
									}
									// logger.trace("Level_0_MethodCarver.level0TestCarving()
									// RANKED CALLS " + ranked);
									// if (distance > 0) {
									// There should be only 1
									closest = Collections.max(ranked);
									// } else {
									// closest = Collections.min(ranked);
									// }
									// }
									// logger.debug("MULTIPLE METHODS RETURN " +
									// data + "\n" + tempSet
									// + " \n choosing the closest " + closest +
									// " to " + entry.getKey());
								}
								methodsWhichReturnTheObject.add(closest);
							}

							/*
							 * Verify that for each data dependency there's at least one call which returns
							 * it
							 */
							if (methodsWhichReturnTheObject.isEmpty()) {

								// If this is a static
								throw new CarvingException("Object " + data + " required for carving " + workList
										+ " comes out of the blue !");
							} else {
								// Not sure what to do with objects !
								if (methodsWhichReturnTheObjects.containsKey(data)
										&& "java.lang.String".equals(data.getType())) {

									// logger.trace(
									// "Level_0_MethodCarver.level0TestCarving()\n\n\n"
									// + "Already a call set for "
									// + data + " " +
									// methodsWhichReturnTheObjects.get(data) +
									// "\n\n\n");

									Set<MethodInvocation> firstCallSet = new HashSet<>(
											methodsWhichReturnTheObjects.get(data));
									firstCallSet.addAll(methodsWhichReturnTheObject);
									List<MethodInvocation> firstCall = new ArrayList<>(firstCallSet);
									Collections.sort(firstCall);
									firstCallSet = new HashSet();
									firstCallSet.add(firstCall.get(0));
									// logger.trace(
									// "Level_0_MethodCarver.level0TestCarving()
									// REPLACING WITH " + firstCallSet);
									methodsWhichReturnTheObjects.put(data, firstCallSet);

								} else {

									// Those cannot be later than the user of
									// the
									// data !
									methodsWhichReturnTheObjects.put(data, methodsWhichReturnTheObject);
									// logger.trace("Level_0_MethodCarver.level0TestCarving()
									// methodsWhichReturn " + data
									// + " before " + entry.getKey() + " are " +
									// methodsWhichReturnTheObject);
								}
							}
						}
					}

					/*
					 * At this point we collect up to N methods which might return the data, we pick
					 * only one according to the following heuristic: - Constructors are preferred
					 * unless they are private, otherwise chose the other. In theory, there's only
					 * one "other" call, the closest one...
					 */
					for (Entry<ObjectInstance, Set<MethodInvocation>> methodsWhichReturnTheObject : methodsWhichReturnTheObjects
							.entrySet()) {

						if (methodsWhichReturnTheObject.getValue().size() > 1) {

							// Keep only the constructor, unless it is private !
							Set<MethodInvocation> methods = methodsWhichReturnTheObject.getValue();
							for (MethodInvocation method : methods) {
								if (method.getMethodSignature().contains("<init>") && !method.isPrivate()) {
									// logger.trace("Reduce the combination for
									// " +
									// methodsWhichReturnTheObject.getKey());
									methodsWhichReturnTheObject.setValue(Collections.singleton(method));
									break;
								}
							}
							// TODO Refactor this method ...
							methods = methodsWhichReturnTheObject.getValue();
							// This is just NAIVE, but let's see how far we can
							// get with it...
							for (MethodInvocation method : methods) {
								if (!method.getMethodSignature().contains("<init>") && !method.isPrivate()) {
									// logger.trace("RANDOMLY SELECT " + method
									// + "To reduce the combination for "
									// + methodsWhichReturnTheObject.getKey());
									methodsWhichReturnTheObject.setValue(Collections.singleton(method));
									break;
								}
							}

						}
					}

					// Full cartesian
					Set<List<MethodInvocation>> fullCartesianProduct = Sets
							.cartesianProduct(new ArrayList<>(methodsWhichReturnTheObjects.values()));

					if (fullCartesianProduct.size() > 1) {
						throw new CarvingException(
								"Full cartesian product shall have only 1 combination, instead it has "
										+ fullCartesianProduct.size());
					}

					// Add unit of work in the cache here
					Set<MethodInvocation> dependencies = new HashSet<>(fullCartesianProduct.iterator().next());
					dependencies.addAll(methodDependencies);
					//
					List<MethodInvocation> preconditions = new ArrayList<>(dependencies);
					Collections.sort(preconditions);
					// Not sure WHY this might be there...
					preconditions.remove(unitOfWork);

					preconditionCache.put(unitOfWork, preconditions);
				}
				// else {
				// logger.info("Carving: (Cached) " + unitOfWork);
				// }

				// logger.debug("Carve of " + unitOfWork + " Done " +
				// preconditionCache.get(unitOfWork));

				/////// Prepare the next iteration

				// FIXME This can be optimized by fast-forwarding carving using
				// the cache without the need to recreate tasks over and over

				// Accumulate past work
				Set<MethodInvocation> workDone = new HashSet<>(task.getFirst());
				// Include new work - from cache
				workDone.add(unitOfWork);

				// Register the work to do in the next iteration -
				// Preconditions from data deps
				Set<MethodInvocation> workToDo = new HashSet<>(preconditionCache.get(unitOfWork));

				// Since we process a MI at the time, keep the open work
				// around
				workToDo.addAll(task.getSecond());

				// Avoid to repeat work already done - in this task
				workToDo.removeAll(workDone);

				// Recreate the task
				Pair<Set<MethodInvocation>, Set<MethodInvocation>> newTask = new Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
						workDone, workToDo);

				//
				// logger.debug("Level_0_MethodCarver.level0TestCarving()
				// Enqueuing new task with "
				// + newTask.getSecond().size() + " method invocations");
				// logger.debug("" + newTask.getSecond());
				workList.add(newTask);

			}
		}

	}

	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> generateSingleTestCaseFromSliceFor(
			MethodInvocation methodInvocationToCarve, Set<MethodInvocation> backwardSlice,
			List<MethodInvocation> dataReturningCalls) throws NotALevel0TestCaseException {

		logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() for " + dataReturningCalls
				+ " from backward slice " + backwardSlice);

		// FIXME If the call is the only thing inside the backwardSlice we might
		// short-circuit here probably

		// Already at this point there no more ref to Files.write !!

		// Create a local copy of the backwardSlide. This is the "minimal slice"
		Set<MethodInvocation> _backwardSlice = new HashSet<>(backwardSlice);
		// Explicitly Include the returning calls

		// At any time if a call was not yet included
		/// in original backwardSlide we carve with minimal carve of that call !
		// This way we ensure that all the preconditions for that call are
		/// included as well...
		// We adopt the naive solution of calling "carve" for each, every time.
		/// This of course can be improved by caching the carved test for those
		/// calls as well.
		// Another possibility might be to define carved test as carve(of carve(
		/// of carve())) for each method invocation encountered in the first
		/// test carved
		// TODO This probably should be called OUTSIDE !!! TO AVOID USELESS
		// COMPUTATION FOR THE FIRST TEST CASE?
		// Ensure preconditions
		for (MethodInvocation returnCall : dataReturningCalls) {
			if (!backwardSlice.contains(returnCall)) {

				logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Must Ensure preconditions of "
						+ returnCall);

				if (!preconditionCache.containsKey(returnCall)) {
					List<MethodInvocation> preconditions = computePreconditionsFor(returnCall);

					preconditionCache.put(returnCall, preconditions);
				}

				// At this point we need to recompute the
				// dataDependencyGraph to include any deps on
				// preconditions
				// Which basically consist on carving them ?
				_backwardSlice.addAll(preconditionCache.get(returnCall));
			}
		}

		_backwardSlice.addAll(dataReturningCalls);

		// Eventually remove the subsumed Calls
		Set<MethodInvocation> subsumedCalls = new HashSet<>();
		for (MethodInvocation mi : _backwardSlice) {
			// Filter out the calls that are subsumed by the call graph

			Set<MethodInvocation> subsumedBy = callGraph.getMethodInvocationsSubsumedBy(mi);
			// if (subsumedBy.contains(methodInvocationToCarve)) {
			// logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSliceFor()
			// FOUND "
			// + methodInvocationToCarve + " inside subsumed set by " + mi);
			// }

			subsumedCalls.addAll(subsumedBy);
		}

		/*
		 * If the subsumed call is also the method under carving, the resulting test
		 * case cannot be defined as Level_0. So we stop its generation directly here.
		 * Not that we could simply return null, or something, but this way its clear
		 * what did happen.
		 * 
		 */
		if (subsumedCalls.contains(methodInvocationToCarve)) {
			// If this happens, the methodInvocation to carve will be removed
			// from the test... which is wrong...

			List<MethodInvocation> subsumingCalls = callGraph
					.getOrderedSubsumingMethodInvocationsFor(methodInvocationToCarve);

			logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSliceFor() SubsumingCalls for "
					+ methodInvocationToCarve + " --> " + subsumingCalls);

			subsumingCalls.retainAll(_backwardSlice);

			// This should be only 1
			assert subsumingCalls.size() == 1;
			if (subsumingCalls.size() != 1) {
				logger.warn("Wrong subsumingCalls count " + subsumingCalls.size() + " instead of 1");
			}
			MethodInvocation subsumingMethodInvocation = subsumingCalls.iterator().next();
			List<MethodInvocation> subsumingPath = callGraph.getSubsumingPathFor(subsumingMethodInvocation,
					methodInvocationToCarve);

			throw new NotALevel0TestCaseException(methodInvocationToCarve, subsumingMethodInvocation, subsumingPath);

		}

		// Delete from the slice all those calls that will be done nevertheless
		_backwardSlice.removeAll(subsumedCalls);
		List<MethodInvocation> carvedTestCase = new ArrayList<>(_backwardSlice);
		// Order by invocation
		Collections.sort(carvedTestCase, new Comparator<MethodInvocation>() {
			@Override
			public int compare(MethodInvocation o1, MethodInvocation o2) {
				return o1.getInvocationCount() - o2.getInvocationCount();
			}
		});

		ExecutionFlowGraph carvedExecutionGraph = new ExecutionFlowGraphImpl();
		for (MethodInvocation mi : carvedTestCase) {
			carvedExecutionGraph.enqueueMethodInvocations(mi);
		}

		// This is the original graph
		DataDependencyGraph carvedDataDependencyGraph = dataDependencyGraph.getSubGraph(carvedExecutionGraph);

		return new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(carvedExecutionGraph,
				carvedDataDependencyGraph,
				// TODO CallGraph was never intended to be used here, as this is something we
				// might need to compute ourselves?
				new CallGraphImpl());
	}

	private List<MethodInvocation> computePreconditionsFor(MethodInvocation returnCall) {

		List<MethodInvocation> preconditions = new ArrayList<>();

		if (dataDependencyGraph.getOwnerFor(returnCall) != null || //
				!dataDependencyGraph.getParametersOf(returnCall).isEmpty()) {

			// This might return an empty set of precondition for calls
			// that are subsumed, like constructors of super- and sub-
			// classes.
			// TODO Try to see if you can avoid doing the precondition
			// check for those specific cases (which might be also
			// chains)

			try {
				// Recursive call to carve
				List<CarvedExecution> carvedPreconditions = carve(returnCall);

				throw new NotImplementedException();
//				if (!carvedPreconditions.isEmpty()) {
//					//
//					preconditions.addAll(carvedPreconditions.get(0).getFirst().getOrderedMethodInvocations());
//
//					/*
//					 * Here we do not really care about data dep, since the later call should
//					 * include them as well !?
//					 */
//					logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Preconditions are : "
//							+ preconditions);
//
//					if (carvedPreconditions.size() > 1) {
//						logger.error("Level_0_MethodCarver.computePreconditionsFor() MULTIPLE PRECONDITIONS FOR "
//								+ returnCall);
//					}
//				} else {
//					logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Method " + returnCall
//							+ " has no preconditions");
//				}
			} catch (ABCException e) {
				logger.error("Cannot compute preconditions for return call " + returnCall);
				e.printStackTrace();
				// logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSliceFor()
				// WHAT TO DO NOW ?");
			}
		} else {
			logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Method " + returnCall
					+ " has no preconditions");
		}
		return preconditions;
	}

	public Map<MethodInvocation, List<CarvedExecution>> carve(List<MethodInvocation> methodsInvocations) {
		throw new NotImplementedException();
//
//		Map<MethodInvocation, List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>> allCarvedExecution = new HashMap<MethodInvocation, List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>>();
//
////        List<MethodInvocation> orderedMethodsInvocationsToCarve = new ArrayList<>(executionFlowGraph.getMethodInvocationsFor(carveBy, excludeBy.toArray(new MethodInvocationMatcher[] {})));
//		/*
//		 * By ordering them we should be able to exploit the precondition cache and
//		 * incrementally carve later invocations from previous carved invocations
//		 */
//		Collections.sort(methodsInvocations);
//
//		for (MethodInvocation methodInvocationUnderTest : methodsInvocations) {
//			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTestsPerMethodInvocation = new ArrayList<>();
//			// Store the list in the map to ensure we produce one
//			allCarvedExecution.put(methodInvocationUnderTest, carvedTestsPerMethodInvocation);
//
//			// TODO Filter UNCARVABLE Method invocations
////            if (ABCUtils.ARTIFICIAL_METHODS.contains(methodInvocationUnderTest.getInvocationType())) {
////                logger.info("We do not carve ABC artificial methods " + methodInvocationUnderTest);
////                continue;
////            }
//
//			// Skip methods which has no sense to carve
//			if (methodInvocationUnderTest.isPrivate()) {
//				logger.info("We cannot carve private methods " + methodInvocationUnderTest);
//				continue;
//			}
//
//			try {
//
//				logger.info("\n\n====================================================\n" //
//						+ "Starting the carve of " + methodInvocationUnderTest + "\n" //
//						+ "====================================================");
//				long carvingTime = System.currentTimeMillis();
//
//				// TODO Recursive call to carve
//				carvedTestsPerMethodInvocation.addAll(carve(methodInvocationUnderTest));
//
//				carvingTime = System.currentTimeMillis() - carvingTime;
//				logger.info("\n\n====================================================\n" //
//						+ "Carved  " + carvedTestsPerMethodInvocation.size() + " in " + +carvingTime + " msec \n" //
//						+ "====================================================");
//
//			} catch (ABCException e) {
//				logger.error("Cannot carve test for " + methodInvocationUnderTest, e);
//			}
	}

	/*
	 * TODO Post processing carved tests. If any...
	 */
//
//		// Here we need to remove the duplicated tests. After simplify they
//		// might end up implementing the same functionalities
//		// HashSet is difficult to ues since graph do not implements proper
//		// hashCode. Better use equals
//		// Set<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>
//		// uniqueCarvedTests
//		// = new HashSet<>(carvedTests);
//		List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> uniqueCarvedTests = new ArrayList<>();
//
//		// Here I need to check if there are test cases which have the SAME
//		// jimple calls or equivalent calls not exactly carving the same
//		// invocations !
//		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest : allCarvedExecution) {
//			// This is a simplistic check, it cannot rule out the code which is
//			// generated from different method invocations, which implement the
//			// same functionalities, however is SIMPLE to implement. We check
//			// for equivalence later, at JIMPLE level.
//			if (!uniqueCarvedTests.contains(carvedTest)) {
//				uniqueCarvedTests.add(carvedTest);
//			} else {
//				logger.debug("Found duplicate test" + carvedTest.getFirst().getOrderedMethodInvocations());
//			}
//		}
//
//		// Order by size (in jimple statements)
//		Collections.sort(uniqueCarvedTests,
//				new Comparator<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>() {
//
//					@Override
//					public int compare(Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> o1,
//							Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> o2) {
//						return o1.getFirst().getOrderedMethodInvocations().size()
//								- o2.getFirst().getOrderedMethodInvocations().size();
//					}
//
//				});
//
//		return uniqueCarvedTests;

//	return allCarvedExecution;

//	}

	// FIXME: this takes half a second and I do not recall what's for, Maybe
	// removing duplicates or reducing the size of the carved tests ?
	private void simplify(MethodInvocation methodInvocationUnderTest,
			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTests) {

		long start = System.currentTimeMillis();
		logger.debug("Simplify for " + methodInvocationUnderTest);

		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest : carvedTests) {

			DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();
			ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();

			// This is the CORE of the carved test
			Set<MethodInvocation> coreMethodInvocations = dataDependencyGraph
					.getWeaklyConnectedComponentContaining(methodInvocationUnderTest);

			// This is to consider external interfaces and their preconditions
			for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
				if (methodInvocation.belongsToExternalInterface() || methodInvocation.isTestSetupCall()) {
					coreMethodInvocations.add(methodInvocation);
					coreMethodInvocations
							.addAll(dataDependencyGraph.getWeaklyConnectedComponentContaining(methodInvocation));
				}
			}

			// This is to consider the external interfaces which are static
			// invocations ?

			dataDependencyGraph.refine(coreMethodInvocations);
			executionFlowGraph.refine(coreMethodInvocations);
		}
		long end = System.currentTimeMillis();
		logger.info("Simplify took " + (end - start) + " msec");

	}

}
