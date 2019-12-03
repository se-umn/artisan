package de.unipassau.abc.carving.carvers;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.unipassau.abc.carving.MethodCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.exceptions.NotALevel0TestCaseException;

public class Full_Level_0_MethodCarver implements MethodCarver {

	private final Logger logger = LoggerFactory.getLogger(Full_Level_0_MethodCarver.class);

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	// Default exclusion patterns
	// This is to avoid to carve the fake method that we create.
	// private final MethodInvocationMatcher excludeMain =
	// MethodInvocationMatcher.byMethod("<ABC: int MAIN()>");
	// private final MethodInvocationMatcher excludeJavaLang =
	// MethodInvocationMatcher.byPackage("java");
	// TODO We need to exclude also PRIVATE method !

	// Computing the cartesian product of preconditions is
	// empensive, especially if we carve over and over the same preconditions
	private Map<MethodInvocation, List<MethodInvocation>> preconditionCache = new HashMap<>();

	/**
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @param callGraph
	 */
	public Full_Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
	}

	/**
	 * 
	 * Level 0 means that we cannot invoke any primitive method or rely on knowledge
	 * about the internals of the CUT
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
	 * @throws CarvingException
	 */
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> level0TestCarving(
			MethodInvocation methodInvocationToCarve) throws CarvingException {

		// Build the context for the carving. At the beginning the context IS
		// the entire trace.
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph, callGraph);

		// Include all the external interfaces..
		boolean skipExternalInterfaces = false;

		return level0TestCarving(methodInvocationToCarve, context, skipExternalInterfaces);
	}

	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> level0TestCarving(
			MethodInvocation methodInvocationToCarve,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context, boolean skipExternalInterfaces)
			throws CarvingException {

		// This creates the root of the tree and invoke the recursive method
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

		// A task is a pair made of work done (first), and work to be done
		// (second)
		Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList = new LinkedList<>();

		// Add the MUT as task in the work list
		Pair<Set<MethodInvocation>, Set<MethodInvocation>> mutTask = new Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
				new HashSet<MethodInvocation>(),
				new HashSet<MethodInvocation>(Collections.singleton(methodInvocationToCarve)));
		//
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
		logger.info("\t Core Carving took " + (end - start));
		// Ensure that any required testSetupCall, which is not yet included in
		// the carved test, is taken into account,
		// including its own carved preconditions
		start = System.currentTimeMillis();
		// Include the user defined test setup calls. Those are by definition
		// disjointed from the actual test execution !
		// Roughly, they correspond to @Before calls
		includeTestSetupCalls(carvedTests, context);
		end = System.currentTimeMillis();
		logger.info("\t Core Test Setup Calls took " + (end - start));

		start = System.currentTimeMillis();
		includeCallsToExternalInterfaces(carvedTests, context);
		end = System.currentTimeMillis();
		logger.info("\t Carving External Interface took " + (end - start));

		return carvedTests;
	}

	public void includeTestSetupCalls(List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context) throws CarvingException {

		// Collect the testSetupMethodInvocations from the context
		Set<MethodInvocation> testSetupMethosInvocations = context.getFirst().getTestSetupMethodInvocations();

		// If the method invocation is not there yet, include it.
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {

			for (MethodInvocation testSetupFromContext : testSetupMethosInvocations) {

				if (!carvedTest.getFirst().contains(testSetupFromContext)) {
					logger.trace("Level_0_MethodCarver.includeTestSetupCalls() INCLUDING Test Setup Call "
							+ testSetupFromContext + " in carved test "
							+ carvedTest.getFirst().getOrderedMethodInvocations());

					// Build the testSetupContext1
					List<MethodInvocation> invocationsBefore = context.getFirst()
							.getOrderedMethodInvocationsBefore(testSetupFromContext);
					ExecutionFlowGraph first = context.getFirst().getSubGraph(invocationsBefore);
					DataDependencyGraph second = context.getSecond().getSubGraph(invocationsBefore);
					//
					CallGraph third = context.getThird().getSubGraph(testSetupFromContext);
					//
					Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> testSetupContext = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
							first, second, third);

					//
					Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList = new LinkedList<>();

					// Add the MUT as task in the work list
					Pair<Set<MethodInvocation>, Set<MethodInvocation>> mutTask = new Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
							new HashSet<MethodInvocation>(),
							new HashSet<MethodInvocation>(Collections.singleton(testSetupFromContext)));
					//
					workList.add(mutTask);
					// THIS ONE IS THE ONE WHICH ACTUALLYS COMPUTE THE CARVE
					// using recursion
					// and FULL CARTESIAN... But do not include test setup calls
					// ;)

					List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedPreconditions = new ArrayList<>();
					//

					if (!preconditionCache.containsKey(testSetupFromContext)) {
						// NOTE context -> testSetupContext
						boolean skipCartesian = true;
						level0TestCarving(workList, carvedPreconditions, testSetupContext, skipCartesian);
						//
						Pair<ExecutionFlowGraph, DataDependencyGraph> carvedPrecondition = carvedPreconditions
								.iterator().next();
						preconditionCache.put(testSetupFromContext,
								carvedPrecondition.getFirst().getOrderedMethodInvocations());
						logger.trace("Level_0_MethodCarver.includeTestSetupCalls() PRECONDITION for "
								+ testSetupFromContext + " STORED IN THE CACHE");
					}

					//
					// In case there's more than one because of cartesian
					// product, pick the first one.
					// NOTE THAT THERE"S SHOULD BE NO DATA DEPS OR SIMILAR INTO
					// THE CARVE PRECONDITIONS !
					Set<MethodInvocation> mergedSorted = new HashSet(
							carvedTest.getFirst().getOrderedMethodInvocations());
					// Return this from the cache !
					mergedSorted.addAll(preconditionCache.get(testSetupFromContext));

					List<MethodInvocation> orderedSlice = new ArrayList<>(mergedSorted);
					Collections.sort(orderedSlice);
					//
					carvedTest.setFirst(context.getFirst().getSubGraph(orderedSlice));
					carvedTest.setSecond(context.getSecond().getSubGraph(orderedSlice));

				}
			}
		}

	}

	public void includeCallsToExternalInterfaces(List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context) throws CarvingException {

		// If the method invocation is not there yet, include it.
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {

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

					List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedPreconditions = new ArrayList<>();
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

						Iterator<Pair<ExecutionFlowGraph, DataDependencyGraph>> iterator = carvedPreconditions
								.iterator();
						if (iterator.hasNext()) {
							Pair<ExecutionFlowGraph, DataDependencyGraph> carvedPrecondition = iterator.next();
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
	 * @throws CarvingException
	 */
	private List<Pair<ExecutionFlowGraph, DataDependencyGraph>> processExternalInterfaces(
			Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest, //
			MethodInvocation methodToCarve) throws CarvingException {

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
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> partialCarvedExternalInterfaces = new ArrayList<>();

		boolean skipExternalInterfaces = true;
		for (MethodInvocation setupCall : compressedSetupCalls) {
			partialCarvedExternalInterfaces
					.addAll(level0TestCarving(setupCall, compressedContext, skipExternalInterfaces));
		}

		// logger.trace("Level_0_MethodCarver.processExternalInterfaces()
		// Merging Carved Preconditions: ");
		// // Maybe is enough to collect the Executions
		// Set<MethodInvocation> merge = new HashSet<>();
		// for (Pair<ExecutionFlowGraph, DataDependencyGraph>
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
	 * @throws CarvingException
	 */
	private Pair<ExecutionFlowGraph, DataDependencyGraph> generateCarvedTestFromSlice(Set<MethodInvocation> slice,
			CallGraph _callGraph) throws ABCException {
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
												+ subsumingCalls);
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

							throw new CarvingException("GenerateCarvedTestFromSlice() Cannot generate for "
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

		return new Pair<ExecutionFlowGraph, DataDependencyGraph>(executionFlowGraph.getSubGraph(orderedSlice),
				dataDependencyGraph.getSubGraph(orderedSlice));
	}

	// Each of this tests ends with a call to the method to carve.
	public void level0TestCarving(
			// This is the node to expand
			Queue<Pair<Set<MethodInvocation>, Set<MethodInvocation>>> workList, //
			List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests, //
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> context, //
			boolean skipCartesian) throws CarvingException {

		ExecutionFlowGraph _executionFlowGraph = context.getFirst();
		DataDependencyGraph _dataDependencyGraph = context.getSecond();
		CallGraph _callGraph = context.getThird();

		while (!workList.isEmpty()) {
			Pair<Set<MethodInvocation>, Set<MethodInvocation>> task = workList.poll();
			logger.trace("\nProcessing " + task.getFirst() + " --> " + task.getSecond() + " by Carving");

			if (task.getSecond().isEmpty()) {
				try {
					// This creates finally the test
					carvedTests.add(generateCarvedTestFromSlice(task.getFirst(), _callGraph));
				} catch (NotALevel0TestCaseException e) {
					e.printStackTrace();
					logger.info("Skip test generation, because test in not valid " + e.getSubsumedMethodInvocation()
							+ " was subsumed by " + e.getSubsumingMethodInvocation() + " via " + e.getSubsumingPath());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					logger.info("Skip test generation, because test in not valid " + e);
				}
			} else {
				// Carving include dependencies on methods and dependencies on
				// data.
				// We need to ensure not only that we use the right data, but
				// also that these data are in the right state.
				// We recreate the state for each data by including in the slice
				// the calls on it that we observed before its use. In case we
				// subsume some method calls, we ensure that the remaining calls
				// are valid. In particular, we must ensure NOT to access
				// objects that are not initialized in the trace/test but inside
				// subsumed calls.

				// For any method invocation in getSecond() we need to collect
				// their preconditions.
				Map<MethodInvocation, Set<ObjectInstance>> dataDependencies = new HashMap<>();
				Set<MethodInvocation> methodDependencies = new HashSet<>();

				// Copmpute the latest possible action from second.. note that
				// this goes always to the past so we reduce the size of the
				// trace

				// MethodInvocation lastestMethodInvocation =
				// Collections.max(task.getSecond());

				for (MethodInvocation methodInvocation : task.getSecond()) {

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

					// Do the carving by following the method invocations chain
					Set<MethodInvocation> backwardSlice = new HashSet<>(executioneBefore);
					backwardSlice
							.retainAll(subGraphFromPastExecution.getMethodInvocationsRecheableFrom(methodInvocation));
					/*
					 * The backwardSlice is the slice which contains the MUT-centric view of the
					 * execution. It disregards the location of methods which have potential impa
					 */

					logger.trace(
							"Level_0_MethodCarver.level0TestCarving() Dependencies on Methods " + backwardSlice.size());
					logger.trace("" + backwardSlice);

					// TODO This might include calls which subsume also other
					// calls !

					// Accumulate the backwardSlice in the worklist
					methodDependencies.addAll(backwardSlice);

					// Now accumulate the data dependencies for the method under
					// carving
					if (methodInvocation.isStatic()) {
						// Static calls require only parameters if any
						if (JimpleUtils.getParameterList(methodInvocation.getMethodSignature()).length > 0) {
							Set<ObjectInstance> parameters = new HashSet<>(
									_dataDependencyGraph.getParametersOf(methodInvocation));
							//
							logger.trace("Level_0_MethodCarver.level0TestCarving() Data Dependencies for "
									+ methodInvocation + " are " + parameters.size());
							logger.trace("Level_0_MethodCarver.level0TestCarving() Data Dependencies for "
									+ methodInvocation + " are " + parameters);
							//
							dataDependencies.put(methodInvocation, parameters);
						}

					} else if (methodInvocation.getMethodSignature().contains(": void <init>(")) {
						Set<ObjectInstance> deps = new HashSet<>();
						// If the method is a constructor the preconditions are
						// the parameters, no parameters means no preconditions
						ObjectInstance owner = _dataDependencyGraph.getOwnerFor(methodInvocation);
						// logger.debug("Level_0_MethodCarver.level0TestCarving()
						// Owner for " + methodInvocation + " is "
						// + owner);
						deps.add(owner);
						//
						if (JimpleUtils.getParameterList(methodInvocation.getMethodSignature()).length > 0) {
							List<ObjectInstance> parameters = _dataDependencyGraph.getParametersOf(methodInvocation);
							// logger.debug("Level_0_MethodCarver.level0TestCarving()
							// dependencies for " + methodInvocation
							// + " are " + parameters);
							deps.addAll(parameters);
						}
						logger.debug("Level_0_MethodCarver.level0TestCarving() Data Dependencies for "
								+ methodInvocation + " are " + deps.size());
						logger.trace("Level_0_MethodCarver.level0TestCarving() Data Dependencies for "
								+ methodInvocation + " are " + deps);
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
							List<ObjectInstance> parameters = _dataDependencyGraph.getParametersOf(methodInvocation);

							// logger.debug("Level_0_MethodCarver.level0TestCarving()
							// dependencies for " + methodInvocation
							// + " are " + parameters);

							deps.addAll(parameters);
						}
						logger.debug("Level_0_MethodCarver.level0TestCarving() Data Dependencies for "
								+ methodInvocation + " are " + deps.size());
						logger.trace("Level_0_MethodCarver.level0TestCarving() Data Dependencies for "
								+ methodInvocation + " are " + deps);
						dataDependencies.put(methodInvocation, deps);
					}
				}

				/////

				Map<ObjectInstance, Set<MethodInvocation>> methodsWhichReturnTheObjects = new HashMap<>();

				// List<MethodInvocation> executioneBeforeLast =
				// _executionFlowGraph
				// .getOrderedMethodInvocationsBefore(lastestMethodInvocation);
				// DataDependencyGraph dataDependencyGraphBefore =
				// _dataDependencyGraph.getSubGraph(executioneBeforeLast);

				for (Entry<MethodInvocation, Set<ObjectInstance>> entry : dataDependencies.entrySet()) {
					for (ObjectInstance data : entry.getValue()) {

						if (data == null) {
							logger.warn("Level_0_MethodCarver.level0TestCarving() NULL DATA FOR " + entry.getKey());
							// SKIP THIS... Probably will break the test
							// generation ?!
							continue;
						}
						if (ObjectInstance.systemIn.equals(data) || //
								ObjectInstance.systemOut.equals(data) || //
								ObjectInstance.systemErr.equals(data)) {
							continue;
						}
						// REPLACED dataDependencyGraphBefore with //
						// dataDependencyGraph

						Set<MethodInvocation> methodsWhichReturnTheObject = new HashSet<>();
						MethodInvocation constructor = dataDependencyGraph.getInitMethodInvocationFor(data);
						if (constructor != null) {
							/*
							 * If we match this by time we stumble upon the fact that super() is called
							 * after <init> Consider only those methods BEFORE or the SAME
							 */
							methodsWhichReturnTheObject.add(constructor);
						}
						// else {
						// logger.trace("Level_0_MethodCarver.level0TestCarving()
						// NO CONSTRUCTOR FOR " + data
						// + " All methods are :");
						// logger.trace("OWNER -- " +
						// dataDependencyGraph.getMethodInvocationsForOwner(data));
						// logger.trace("RETURN -- " +
						// dataDependencyGraph.getMethodInvocationsWhichReturn(data));
						// logger.trace("USE -- " +
						// dataDependencyGraph.getMethodInvocationsWhichUse(data));
						// }

						/**
						 * The problem with this code is that String are handled by value, so different
						 * "strings" variables have the same hash code if they have the same content...
						 * hence, to be sure we pick the FIRST occurrence. This is because, in case the
						 * same variable is re-written, then it will change it's id, meaning that it
						 * results in a different node !
						 * 
						 * For "regular" objects the "MAX" is the right one... I hope...
						 * 
						 */
						Set<MethodInvocation> tempSet = new HashSet<>();
						// Filter out the one that are after the depenendency
						if ("java.lang.String".equals(data.getType())) {
							logger.trace("\n\n\n  The following return String " + data + "\n"
									+ dataDependencyGraph.getMethodInvocationsWhichReturn(data));
						}
						for (MethodInvocation returning : dataDependencyGraph.getMethodInvocationsWhichReturn(data)) {

							if (returning.getInvocationCount() <= entry.getKey().getInvocationCount()) {
								tempSet.add(returning);
							}
							// else {
							// logger.trace(">>>> " + returning + "
							// discarded as violates BEFORE USE");
							// }
						}

						// We need to pick the CLOSEST one, which is not
						// necessarily the LAST one (since calls are nested) !

						// TODO Always pick the last one... except for string
						// ?~!
						/// This is not true ...
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
										logger.trace("Level_0_MethodCarver.level0TestCarving() New distance " + distance
												+ " for " + mi);
									}
								}
								logger.trace("Level_0_MethodCarver.level0TestCarving() RANKED CALLS " + ranked);
								// if (distance > 0) {
								// There should be only 1
								closest = Collections.max(ranked);
								// } else {
								// closest = Collections.min(ranked);
								// }
								// }
								logger.debug("MULTIPLE METHODS RETURN " + data + "\n" + tempSet
										+ " \n choosing the closest " + closest + " to " + entry.getKey());
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

								logger.trace(
										"Level_0_MethodCarver.level0TestCarving()\n\n\n" + "Already a call set for "
												+ data + " " + methodsWhichReturnTheObjects.get(data) + "\n\n\n");

								Set<MethodInvocation> firstCallSet = new HashSet<>(
										methodsWhichReturnTheObjects.get(data));
								firstCallSet.addAll(methodsWhichReturnTheObject);
								List<MethodInvocation> firstCall = new ArrayList<>(firstCallSet);
								Collections.sort(firstCall);
								firstCallSet = new HashSet();
								firstCallSet.add(firstCall.get(0));
								logger.trace("Level_0_MethodCarver.level0TestCarving() REPLACING WITH " + firstCallSet);
								methodsWhichReturnTheObjects.put(data, firstCallSet);

							} else {

								// Those cannot be later than the user of the
								// data !
								methodsWhichReturnTheObjects.put(data, methodsWhichReturnTheObject);
								logger.debug("Level_0_MethodCarver.level0TestCarving() methodsWhichReturn " + data
										+ " before " + entry.getKey() + " are " + methodsWhichReturnTheObject);
							}
						}
					}
				}

				///////

				if (skipCartesian) {
					// Reduce to only one option for each. Constructors are
					// preferred unless they are private
					logger.trace("Reducing the size to skip Cartesian Product ! \n\n");
					for (Entry<ObjectInstance, Set<MethodInvocation>> methodsWhichReturnTheObject : methodsWhichReturnTheObjects
							.entrySet()) {
						if (methodsWhichReturnTheObject.getValue().size() > 1) {

							// Keep only the constructor, unless it is private !
							Set<MethodInvocation> methods = methodsWhichReturnTheObject.getValue();
							for (MethodInvocation method : methods) {
								if (method.getMethodSignature().contains("<init>") && !method.isPrivate()) {
									logger.trace("Reduce the combination for " + methodsWhichReturnTheObject.getKey());
									methodsWhichReturnTheObject.setValue(Collections.singleton(method));
									break;
								}
							}
							// At this point what do we pick ? One at random ?
							for (MethodInvocation method : methods) {
								if (!method.getMethodSignature().contains("<init>") && !method.isPrivate()) {
									logger.trace("RANDOMLY SELECT " + method + "To reduce the combination for "
											+ methodsWhichReturnTheObject.getKey());
									methodsWhichReturnTheObject.setValue(Collections.singleton(method));
									break;
								}
							}

						}

					}
				}

				// Full cartesian
				Set<List<MethodInvocation>> fullCartesianProduct = Sets
						.cartesianProduct(new ArrayList<>(methodsWhichReturnTheObjects.values()));

				// A new task for each sub-combination
				logger.trace("Level_0_MethodCarver.level0TestCarving() Full Cartesian Product is : "
						+ fullCartesianProduct.size() + " each combination has "
						+ methodsWhichReturnTheObjects.values().size() + " elements");

				// Schedule new tasks and update the cache

				for (List<MethodInvocation> combination : fullCartesianProduct) {

					// Move the method invocations considered so far in the
					// done list
					Set<MethodInvocation> workDone = new HashSet<>(task.getFirst());
					workDone.addAll(task.getSecond());
					// The combination of the methods which provide the data
					// for this round are the one to add
					Set<MethodInvocation> workToDo = new HashSet<>(combination);

					// We add to the backward slice computed before. This is the
					// same for each combination.
					workToDo.addAll(methodDependencies);
					// Avoid to repeat work already done
					workToDo.removeAll(workDone);

					// TODO Correctly handling the preconditions here is not
					// easy. It depends on each task, since each MUT might
					// ideally have different preconditions to account for the
					// various combination to get its data ?

					// Remove from the work to do all the method invocations
					// that are already carved

					for (Iterator<MethodInvocation> workToDoIterator = workToDo.iterator(); workToDoIterator
							.hasNext();) {
						MethodInvocation toDo = workToDoIterator.next();
						if (preconditionCache.containsKey(toDo)) {
							workToDoIterator.remove();
							//
							Set<MethodInvocation> cachedResults = new HashSet<>();
							cachedResults.add(toDo);
							cachedResults.addAll(preconditionCache.get(toDo));
							System.out.println("Level_0_MethodCarver.level0TestCarving() ToDo MI " + toDo
									+ " was already carved, we use cached results " + cachedResults);
							workDone.addAll(cachedResults);

						}
					}

					// We create a new task, meaning a new DUT, for each
					// combination
					Pair<Set<MethodInvocation>, Set<MethodInvocation>> newTask = new Pair<Set<MethodInvocation>, Set<MethodInvocation>>(
							workDone, workToDo);

					//
					logger.trace("Level_0_MethodCarver.level0TestCarving() Enqueuing new task with "
							+ newTask.getSecond().size() + " method invocations");
					logger.trace("" + newTask.getSecond());
					workList.add(newTask);
				}

			}
		}
	}

	// List<Pair<ExecutionFlowGraph, DataDependencyGraph>>
	// carvedTestsForMethodInvocation = new ArrayList<>();
	// // CArving: start from MUT
	// // 1 - Include all the invocations that happen before (Execution
	// graph)
	// // -
	// // this excludes methods that happened after MUT
	//
	// // 2 - Create a data dependency sub graph which contains only the
	// Before
	// // method and
	//
	// // 3 - include all the invocations that have are
	// // transitively reached via data dependency no matter in which
	// previous
	// // method are those called.
	// // For example, using a factory to create object instances will
	// result
	// // in calling the actual methods
	// // inside the factory instead of calling the factory itself. TODO
	// Link
	// // this on GitLab
	//
	// // 4 -Exclude all the invocations the are subsumed by any
	// // invocation already considered, i.e., that are
	// // reachable by the transitive closure of the call graph of any
	// method
	// // (Call Graph)
	//
	// // At this point, for each object instance we expand the set of
	// carved
	// // test by including one by one the methods which return them,
	// starting
	// // from the ones at deeper level in the call graph
	//
	// // Carve by Time => "Before"
	// List<MethodInvocation> executioneBefore = executionFlowGraph
	// .getOrderedMethodInvocationsBefore(workList);
	//
	// // Extract relevant Data Dependencies from "Before" - External
	// // interfaces ?
	// DataDependencyGraph subGraphFromPastExecution =
	// dataDependencyGraph.getSubGraph(executioneBefore);
	//
	// // Do the carving by following the method invocations chain
	// Set<MethodInvocation> backwardSlice = new
	// HashSet<>(executioneBefore);
	// backwardSlice.retainAll(subGraphFromPastExecution.getMethodInvocationsRecheableFrom(workList));
	//
	// // TODO At this point include all the calls that are made to external
	// // interfaces BEFORE methodInvocationToCarve
	// backwardSlice.addAll(
	// executionFlowGraph.getOrderedMethodInvocationsToExternalInterfaceBefore(workList));

	// The backwardSlice is the slice which contains the MUT-centric view of
	// the execution. It disregards the location of methods which have
	// potential impact on the CUT, it does not account for external
	// interfaces

	// Now we accumulate task in a work list to build a TREE of carved
	// tests. The tree structure comes from the fact that multiple methods
	// might be used to return the same object instance.
	// For example a Factory method creates an instance using its
	// constructor, so we might return that instance either using the
	// constructor or the Factory method

	// Collect dataDependency for the MUT, owner and parameters

	// Collection<ObjectInstance> dataDependencies = new ArrayList<>();
	//
	// if (!workList.isStatic()) {
	// ObjectInstance owner =
	// subGraphFromPastExecution.getOwnerFor(workList);
	// logger.trace(
	// "Level_0_MethodCarver.level0TestCarving() Owner for " + workList + "
	// is "
	// + owner);
	// dataDependencies.add(subGraphFromPastExecution.getOwnerFor(workList));
	// }
	//
	// if
	// (!JimpleUtils.isVoid(JimpleUtils.getReturnType(workList.getJimpleMethod())))
	// {
	// List<ObjectInstance> parameters =
	// subGraphFromPastExecution.getParametersOf(workList);
	// logger.trace("Level_0_MethodCarver.level0TestCarving()
	// parameters
	// for " + workList
	// + " are " + parameters);
	// dataDependencies.addAll(parameters);
	// }
	//
	// // Collect the call which can return any of those data
	//
	// Map<ObjectInstance, Set<MethodInvocation>>
	// methodsWhichReturnTheObjects =
	// new HashMap<>();
	// for (ObjectInstance data : dataDependencies) {
	// Set<MethodInvocation> methodsWhichReturnTheObject = new HashSet<>();
	// MethodInvocation constructor =
	// subGraphFromPastExecution.getInitMethodInvocationFor(data);
	// if (constructor != null) {
	// methodsWhichReturnTheObject.add(constructor);
	// }
	// methodsWhichReturnTheObject.addAll(subGraphFromPastExecution.getMethodInvocationsWhichReturn(data));
	// /*
	// * Verify that for each data dependency there's at least one call
	// * which returns it
	// */
	// if (methodsWhichReturnTheObject.isEmpty()) {
	// throw new CarvingException("Object " + data + " required for carving
	// " +
	// workList
	// + " comes out of the blue !");
	// }
	// }

	/*
	 * Compute the Cartesian product of the possible ways to recreate each and every
	 * data dependency. Each combination is a branch in our test tree. Eventually,
	 * we get to a point where there's only one combination of parameters or not
	 * even that. At that point the leaves are the carved tests. NOTE we do not
	 * really organize them into a tree...
	 */

	// The problem is rooted in the fact that many StringBuilder are
	// generated and re-generated all over the places. Those do not usually
	// impact the final results...
	// For example logger.trace("A " + foo()); is handled by
	// generating a StringBuilder.
	// TODO: In case it turns out to be a problem, simply discard
	// combinations of StringBuilder, or StringBuilders entirely

	// Pre select the returnin calls to reduce the number of their
	// combination, since ATM there's no evidence this will impact the
	// quality of the results. Ideally this might have an impact on the SIZE
	// of the carved test case because we might invoke less calls to setup
	// the preconditions (e.g., use Factory methods) or avoid to generate
	// impossible tests (private methods? )
	//

	// for (Entry<ObjectInstance, Set<MethodInvocation>> entry :
	// methodsWhichReturnTheObjects.entrySet()) {
	// if (entry.getKey().getType().equals("java.lang.StringBuilder")) {
	// // if (entry.getValue().size() > 1) {
	// // logger.trace(">>>>
	// // Level_0_MethodCarver.level0TestCarving() Pre-select returning
	// // call for: "
	// // + entry.getKey());
	// MethodInvocation preSelected = Collections.min(entry.getValue());
	// entry.getValue().clear();
	// entry.getValue().add(preSelected);
	//
	// }
	// }

	// // This gives us the combinations of calls which create the data...
	// Set<List<MethodInvocation>> fullCartesianProduct = Sets
	// .cartesianProduct(new
	// ArrayList<>(methodsWhichReturnTheObjects.values()));
	//
	// if (fullCartesianProduct.size() > 1000) {
	// logger.warn("The cartesian products of object instance is too big " +
	// fullCartesianProduct.size()
	// + ". Expect scalability issue !");
	// // + fullCartesianProduct);
	// }
	//
	// for (List<MethodInvocation> combination : fullCartesianProduct) {
	// // This springs another round of carving
	//
	// try {
	//
	// Pair<ExecutionFlowGraph, DataDependencyGraph> testCase =
	// generateSingleTestCaseFromSliceFor(workList,
	// backwardSlice, combination);
	// //
	// carvedTestsForMethodInvocation.add(testCase);
	// } catch (NotALevel0TestCaseException e) {
	// // This might happen as consequence of including calls which
	// // subsume methodInvocationToCarve
	// // Note that this might be a call required as precondition by
	// // any of the returning calls which
	// // compose this combination. TODO We can make it more specific
	// // by cross checkig the subsuming calls in callGraph and the one
	// // in the generated test case
	// assert workList.equals(e.getSubsumedMethodInvocation());
	// if (!workList.equals(e.getSubsumedMethodInvocation())) {
	// logger.warn(" The subsumed call " + e.getSubsumedMethodInvocation()
	// + " is not the one under carving " + workList);
	// }
	// logger.warn("Invalid test case for " +
	// e.getSubsumedMethodInvocation() + " which is subsumed by "
	// + e.getSubsumingMethodInvocation() + " via " + e.getSubsumingPath());
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// logger.debug("Swallow : " + e);
	// }
	// }
	//
	// // At this point we lost the connection...
	//
	// if (!minimalCarve) {
	// if (logger.isDebugEnabled()) {
	// for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest :
	// carvedTestsForMethodInvocation) {
	// logger.info("\t Test : " +
	// carvedTest.getFirst().getOrderedMethodInvocations());
	// }
	// }
	// }
	//
	// ///// At this point we consider the calls to external interfaces and
	// ///// carve them as well !
	//
	// return carvedTestsForMethodInvocation;

	/*
	 *************************************************************************************
	 * OLD CODE FOLLOWS
	 **************************************************************************************
	 * 
	 */

	// Ensures that the constructors are ALL BEFORE MUT. it might happen
	// that subclasses are identified despite being called later. This is
	// because (I Suspect) Soot associates N<init> method to the same
	// instance.

	//

	// // Accumulate the
	// List<MethodInvocation> constructors = new ArrayList<>();
	// // THIS - Unless is a static call
	// if (!methodInvocationToCarve.isStatic()) {
	//
	// // For some instances, there's no INIT call, tho there's calls which
	// // return it.
	// MethodInvocation constructor = subGraphFromPastExecution //
	// dataDependencyGraph
	// .getInitMethodInvocationFor(subGraphFromPastExecution //
	// dataDependencyGraph
	// .getOwnerFor(methodInvocationToCarve));
	// if (constructor != null) {
	// constructors.add(constructor);
	// } else {
	// logger.debug("We cannot find an INIT call for this object "
	// + subGraphFromPastExecution.getOwnerFor(methodInvocationToCarve));
	//
	// // Take the first invocation which returns the object instead
	// List<MethodInvocation> methodInvocationsWhichReturnCUT = new
	// ArrayList<>(
	// subGraphFromPastExecution.getMethodInvocationsWhichReturn(
	// subGraphFromPastExecution.getOwnerFor(methodInvocationToCarve)));
	//
	// try {
	// constructors
	// .add(Collections.min(methodInvocationsWhichReturnCUT, new
	// Comparator<MethodInvocation>() {
	// @Override
	// public int compare(MethodInvocation o1, MethodInvocation o2) {
	// return o1.getInvocationCount() - o2.getInvocationCount();
	// }
	// }));
	// } catch (Throwable e1) {
	// logger.info("We cannot find a call which returns this object "
	// + subGraphFromPastExecution.getOwnerFor(methodInvocationToCarve));
	// // TODO: handle exception
	// throw new CarvingException("Object "
	// + subGraphFromPastExecution // dataDependencyGraph
	// .getOwnerFor(methodInvocationToCarve)
	// + " which is the owner of the method to carve " +
	// methodInvocationToCarve
	// + " comes out of the blue !");
	// }
	// }
	// }

	// // DIRECT PARAMETERS
	// for (ObjectInstance dataDependency : subGraphFromPastExecution //
	// dataDependencyGraph
	// .getParametersOf(methodInvocationToCarve)) {
	//
	// // Static instances like Syste.in have no init calls
	// // DO WE STILL NEED THIS PATCH ?!
	// if (dataDependency.equals(ObjectInstance.SystemIn())) {
	// logger.debug(
	// "Level_0_MethodCarver.level0TestCarving() No INIT call for static
	// instances " + dataDependency);
	// continue;
	// }
	//
	// MethodInvocation constructor = subGraphFromPastExecution //
	// dataDependencyGraph
	// .getInitMethodInvocationFor(dataDependency);
	// if (constructor != null) {
	// constructors.add(constructor);
	// } else {
	// logger.debug("We cannot find an INIT call for this object " +
	// dataDependency);
	//
	// // Take the first invocation which returns the object instead
	// List<MethodInvocation> methodInvocationsWhichReturnTheDataDependency
	// = new ArrayList<>(
	// subGraphFromPastExecution.getMethodInvocationsWhichReturn(dataDependency));
	//
	// try {
	// constructors.add(Collections.min(methodInvocationsWhichReturnTheDataDependency,
	// new Comparator<MethodInvocation>() {
	// @Override
	// public int compare(MethodInvocation o1, MethodInvocation o2) {
	// return o1.getInvocationCount() - o2.getInvocationCount();
	// }
	// }));
	// } catch (Throwable e1) {
	// logger.error("We cannot find a call which returns this object " +
	// dataDependency);
	// // TODO: handle exception
	// throw new CarvingException(
	// "Object " + dataDependency + " which is the owner of the method to
	// carve "
	// + methodInvocationToCarve + " comes out of the blue !");
	// }
	//
	// }
	// } /// FIXME: Check that each of the data either is init or provided
	// with
	// /// return !
	//
	// if (minimalCarve) {
	// try {
	// // Generate ONLY the first carved test. This raises an
	// // exceptions if the test cannot be created
	// Pair<ExecutionFlowGraph, DataDependencyGraph>
	// carvedTestFromConstructors = generateSingleTestCaseFromSliceFor(
	// methodInvocationToCarve, backwardSlice, constructors);
	//
	// carvedTestsForMethodInvocation.add(carvedTestFromConstructors);
	// return carvedTestsForMethodInvocation;
	// } catch (NotALevel0TestCaseException e) {
	// logger.warn("while carving preconditions of " +
	// methodInvocationToCarve + " invalid test generated as :"
	// + e.getSubsumedMethodInvocation() + " is subsubed by " +
	// e.getSubsumingMethodInvocation()
	// + " via " + e.getSubsumingPath());
	// // This should never happen, the basic test shall be there ? Not
	// // really in case a constructor of a subclass is called soot
	// // also report the call to the constructor of the super class.
	// // So in all the cases we get one the other is subsume. This is
	// // not an error if both init refer to the same owner !
	// // throw new RuntimeException("This never happen as the basic
	// // carved test should contain the MUT", e);
	// }
	// }

	// Can this recreate the carvedTestFromConstructors test case ?

	// Map<ObjectInstance, Set<MethodInvocation>> returningCalls = new
	// HashMap<>();

	// Now find all the calls which returns any of the objectInstances
	// in the data dependency graph. This one is odd, if we consider plenty
	// of objects we get a huge number of possibilities
	// which then we filter away... On the other size, if we do not consider
	// this, how do we include calls to external libraries, maybe simply by
	// looking into the exec graph ?

	// logger.trace("Level_0_MethodCarver.level0TestCarving() All object
	// instances : "
	// + subGraphFromPastExecution.getObjectInstances().size());
	// XXX We try to use the following methods to reduce the amount of
	// ObjectInstances considered, but failed to create some preconditions
	// and generated wrong test cases:
	// logger.trace("Level_0_MethodCarver.level0TestCarving() Object
	// used before : "
	// +
	// subGraphFromPastExecution.getObjectInstancesUsedBefore(methodInvocationToCarve).size());
	// logger.trace("Level_0_MethodCarver.level0TestCarving() Object
	// used before by external interfaces : "
	// +
	// subGraphFromPastExecution.getObjectInstancesUsedBeforeByExternalInterfaces(methodInvocationToCarve)
	// .size());

	// for (ObjectInstance objectInstance :
	// subGraphFromPastExecution.getObjectInstances()) {
	//
	// Set<MethodInvocation> returningCallsForObjectInstance =
	// subGraphFromPastExecution // dataDependencyGraph
	// .getMethodInvocationsWhichReturn(objectInstance);
	//
	// // Some calls return objects that are also their parameters, we need
	// // to rule them out otherwise
	// // we generate "invalid/impossible" test cases, which require to
	// // execute the call to provide a parameter of the same call
	//
	// /*
	// * If from the returning call, by GOING BACKWARDS, I stumble upon
	// * the same object, I cannot use that call as returning one... Since
	// * the call is changing the object, and we put the object as input,
	// * we have the object already. This might not be true in the
	// * original java, e.g., : Path p = Files.write(Paths.get("p"),
	// * bytes); But it will in the carved one:
	// *
	// * Path _p = Paths.get("p"); Path p = Files.write(_p, bytes);
	// *
	// */
	//
	// for (Iterator<MethodInvocation> retCallIterator =
	// returningCallsForObjectInstance
	// .iterator(); retCallIterator.hasNext();) {
	// MethodInvocation retCall = retCallIterator.next();
	// if
	// (subGraphFromPastExecution.getParametersOf(retCall).contains(objectInstance))
	// {
	// logger.info("The method " + retCall + " returns one of its parameters
	// " + objectInstance);
	// retCallIterator.remove();
	// }
	// }
	//
	// // Here we need to include the constructors as well to create
	// // the cross products. Note that sometimes, because the
	// // instrumentation cannot cover "everything" we might not be
	// // able to initialize
	// // all the instances: For example:
	// //
	// org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock@463345942
	// // Still we like to carve out some test
	// MethodInvocation init = subGraphFromPastExecution //
	// dataDependencyGraph
	// .getInitMethodInvocationFor(objectInstance);
	// if (init != null) {
	// returningCallsForObjectInstance.add(init);
	// }
	//
	// // logger.trace("Level_0_MethodCarver.level0TestCarving() " +
	// // objectInstance + " --> "
	// // + returningCallsForObjectInstance.size());
	//
	// if (returningCallsForObjectInstance.size() != 0) {
	// returningCalls.put(objectInstance, returningCallsForObjectInstance);
	// } else {
	// // There no init call or method returning public static
	// // instances like System.in
	// if (!objectInstance.equals(ObjectInstance.SystemIn())) {
	// throw new CarvingException("Cannot find either INIT or returning
	// calls for " + objectInstance);
	// }
	// }
	//
	// }

	// // And generate the possible combinations by computing the Cartesian
	// // Product of those calls per. For each call generate a new
	// carvedTest
	// /// FIXME This becomes HUGE, probably we shall consider some
	// // optimization ?
	// // See: #39
	// // The problem is rooted in the fact that many StringBuilder are
	// // generated and re-generated all over the places. Those do not
	// usually
	// // impact the final results...
	// // For example logger.trace("A " + foo()); is handled by
	// // generating a StringBuilder.
	// // TODO: In case it turns out to be a problem, simply discard
	// // combinations of StringBuilder, or StringBuilders entirely
	//
	// // Pre select the returnin calls to reduce the number of their
	// // combination, since ATM there's no evidence this will impact the
	// // quality of the results. Ideally this might have an impact on the
	// SIZE
	// // of the carved test case because we might invoke less calls to
	// setup
	// // the preconditions (e.g., use Factory methods) or avoid to generate
	// // impossible tests (private methods? )
	// //
	//
	// for (Entry<ObjectInstance, Set<MethodInvocation>> entry :
	// returningCalls.entrySet()) {
	// if (entry.getKey().getType().equals("java.lang.StringBuilder")) {
	// // if (entry.getValue().size() > 1) {
	// // logger.trace(">>>>
	// // Level_0_MethodCarver.level0TestCarving() Pre-select returning
	// // call for: "
	// // + entry.getKey());
	// MethodInvocation preSelected = Collections.min(entry.getValue());
	// entry.getValue().clear();
	// entry.getValue().add(preSelected);
	//
	// }
	// }
	//
	// // This gives us the combinations of calls which create the data...
	// Set<List<MethodInvocation>> fullCartesianProduct = Sets
	// .cartesianProduct(new ArrayList<>(returningCalls.values()));
	//
	// if (fullCartesianProduct.size() > 1000) {
	// logger.warn("The cartesian products of object instance is too big " +
	// fullCartesianProduct.size()
	// + ". Expect scalability issue !");
	// // + fullCartesianProduct);
	// }
	//
	// for (List<MethodInvocation> combination : fullCartesianProduct) {
	// try {
	//
	// Pair<ExecutionFlowGraph, DataDependencyGraph> testCase =
	// generateSingleTestCaseFromSliceFor(
	// methodInvocationToCarve, backwardSlice, combination);
	// //
	// carvedTestsForMethodInvocation.add(testCase);
	// } catch (NotALevel0TestCaseException e) {
	// // This might happen as consequence of including calls which
	// // subsume methodInvocationToCarve
	// // Note that this might be a call required as precondition by
	// // any of the returning calls which
	// // compose this combination. TODO We can make it more specific
	// // by cross checkig the subsuming calls in callGraph and the one
	// // in the generated test case
	// assert
	// methodInvocationToCarve.equals(e.getSubsumedMethodInvocation());
	// if (!methodInvocationToCarve.equals(e.getSubsumedMethodInvocation()))
	// {
	// logger.warn(" The subsumed call " + e.getSubsumedMethodInvocation()
	// + " is not the one under carving " + methodInvocationToCarve);
	// }
	// logger.warn("Invalid test case for " +
	// e.getSubsumedMethodInvocation() + " which is subsumed by "
	// + e.getSubsumingMethodInvocation() + " via " + e.getSubsumingPath());
	// } catch (RuntimeException e) {
	// e.printStackTrace();
	// logger.debug("Swallow : " + e);
	// }
	// }
	//
	// // At this point we lost the connection...
	//
	// if (!minimalCarve) {
	// if (logger.isDebugEnabled()) {
	// for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest :
	// carvedTestsForMethodInvocation) {
	// logger.info("\t Test : " +
	// carvedTest.getFirst().getOrderedMethodInvocations());
	// }
	// }
	// }
	//
	// ///// At this point we consider the calls to external interfaces and
	// ///// carve them as well !
	//
	// return carvedTestsForMethodInvocation;
	// }

	private Pair<ExecutionFlowGraph, DataDependencyGraph> generateSingleTestCaseFromSliceFor(
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

		ExecutionFlowGraph carvedExecutionGraph = new ExecutionFlowGraph();
		for (MethodInvocation mi : carvedTestCase) {
			carvedExecutionGraph.enqueueMethodInvocations(mi);
		}

		// This is the original graph
		DataDependencyGraph carvedDataDependencyGraph = dataDependencyGraph.getSubGraph(carvedExecutionGraph);

		return new Pair<ExecutionFlowGraph, DataDependencyGraph>(carvedExecutionGraph, carvedDataDependencyGraph);
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
				List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedPreconditions = level0TestCarving(returnCall);

				if (!carvedPreconditions.isEmpty()) {
					//
					preconditions.addAll(carvedPreconditions.get(0).getFirst().getOrderedMethodInvocations());

					/*
					 * Here we do not really care about data dep, since the later call should
					 * include them as well !?
					 */
					logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Preconditions are : "
							+ preconditions);

					if (carvedPreconditions.size() > 1) {
						logger.error("Level_0_MethodCarver.computePreconditionsFor() MULTIPLE PRECONDITIONS FOR "
								+ returnCall);
					}
				} else {
					logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Method " + returnCall
							+ " has no preconditions");
				}
			} catch (CarvingException e) {
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

	/**
	 * methodToBeCarved in Jimple format
	 * 
	 * @param carveBy
	 * @return
	 */
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(
			List<MethodInvocation> orderedMethodsInvocationsToCarve) {
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

		// List<MethodInvocation> orderedMethodsInvocationsToCarve = new
		// ArrayList<>(
		// executionFlowGraph.getMethodInvocationsFor(carveBy,
		// /* excludeMain, excludeJavaLang, */
		// excludeBy.toArray(new MethodInvocationMatcher[] {})));
		// By ordering them we should be able to exploit the precondition cache
		Collections.sort(orderedMethodsInvocationsToCarve);

		// It seems that this cannot deal with Prefix. E.G., ClassFile,
		// ClassFile1, ClassFile2 are all matched by "ClassFile"

		for (MethodInvocation methodInvocationUnderTest : orderedMethodsInvocationsToCarve) {

			if (methodInvocationUnderTest.isPrivate()) {
				logger.info("We do not carve private methods " + methodInvocationUnderTest);
			}

			List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestsPerMethodInvocation = new ArrayList<>();

			try {

				logger.info("\n\n====================================================\n" + "Starting the carve of "
						+ methodInvocationUnderTest + "\n" + "====================================================");
				long carvingTime = System.currentTimeMillis();
				carvedTestsPerMethodInvocation.addAll(level0TestCarving(methodInvocationUnderTest));

				// Simplify the carved tests: Following the data dependencies we
				// might take into tests, invocations that are included but
				// independent
				// from the CUT/MUT. We identify them as NON connected by means
				// of
				// data
				// dependencies unless those are (or contains?) calls to
				// external
				// libraries.
				// FIXME Handle external libraries

				simplify(methodInvocationUnderTest, carvedTestsPerMethodInvocation);
				carvingTime = System.currentTimeMillis() - carvingTime;
				logger.info("\n\n====================================================\n" + "Carved  "
						+ carvedTestsPerMethodInvocation.size() + " in " + +carvingTime + " msec \n"
						+ "====================================================");

				// Add to big list
				carvedTests.addAll(carvedTestsPerMethodInvocation);
			} catch (CarvingException e) {
				logger.error("Cannot carve test for " + methodInvocationUnderTest, e);
			}
		}

		// Here we need to remove the duplicated tests. After simplify they
		// might end up implementing the same functionalities
		// HashSet is difficult to ues since graph do not implements proper
		// hashCode. Better use equals
		// Set<Pair<ExecutionFlowGraph, DataDependencyGraph>> uniqueCarvedTests
		// = new HashSet<>(carvedTests);
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> uniqueCarvedTests = new ArrayList<>();

		// Here I need to check if there are test cases which have the SAME
		// jimple calls or equivalent calls not exactly carving the same
		// invocations !
		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
			// This is a simplistic check, it cannot rule out the code which is
			// generated from different method invocations, which implement the
			// same functionalities, however is SIMPLE to implement. We check
			// for equivalence later, at JIMPLE level.
			if (!uniqueCarvedTests.contains(carvedTest)) {
				uniqueCarvedTests.add(carvedTest);
			} else {
				logger.debug("Found duplicate test" + carvedTest.getFirst().getOrderedMethodInvocations());
			}
		}

		// Order by size (in jimple statements)
		Collections.sort(uniqueCarvedTests, new Comparator<Pair<ExecutionFlowGraph, DataDependencyGraph>>() {

			@Override
			public int compare(Pair<ExecutionFlowGraph, DataDependencyGraph> o1,
					Pair<ExecutionFlowGraph, DataDependencyGraph> o2) {
				return o1.getFirst().getOrderedMethodInvocations().size()
						- o2.getFirst().getOrderedMethodInvocations().size();
			}

		});

		return uniqueCarvedTests;
	}

	private void simplify(MethodInvocation methodInvocationUnderTest,
			List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests) {

		logger.debug("Simplify for " + methodInvocationUnderTest);

		for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {

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
	}

}
