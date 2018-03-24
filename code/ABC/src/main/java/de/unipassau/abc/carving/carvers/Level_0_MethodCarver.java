package de.unipassau.abc.carving.carvers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodCarver;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.carving.exceptions.NotALevel0TestCaseException;
import de.unipassau.abc.data.Pair;

public class Level_0_MethodCarver implements MethodCarver {

	private final Logger logger = LoggerFactory.getLogger(Level_0_MethodCarver.class);

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
	public Level_0_MethodCarver(ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph,
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
	}

	/**
	 * 
	 * Level 0 means that we cannot invoke any primate method or rely on
	 * knowledge about the internals of the CUT
	 * 
	 * We generate several test cases for the same MUT if the parameters comes
	 * from different level (see https://en.wikipedia.org/wiki/Law_of_Demeter)
	 * up to the level of the current MUT ?
	 * 
	 * The point is: a parameter is generated and modified at some location, and
	 * we need to get it. We can either replicate all the invocations on it (and
	 * the ones needed there) but also we can go up one level, and invoke
	 * whatever method provided that instance (at last) as return value
	 * 
	 * We create a new test for method which return any parameters handled by
	 * MUT. This can be simply done by including that method before removing
	 * subsumed calls
	 * 
	 * @param methodInvocationToCarve
	 * @return
	 * @throws CarvingException
	 */

	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> level0TestCarving(
			MethodInvocation methodInvocationToCarve, boolean minimalCarve) throws CarvingException {

		if (!minimalCarve) {
			logger.info("Carving " + methodInvocationToCarve + " Trace size "
					+ executionFlowGraph.getOrderedMethodInvocations().size());
		}

		/*
		 * Not sure "minimal" is the right term
		 * 
		 */
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestsForMethodInvocation = new ArrayList<>();
		// CArving: start from MUT
		// 1 - Include all the invocations that happen before (Execution graph)
		// -
		// this excludes methods that happened after MUT

		// 2 - Create a data dependency sub graph which contains only the Before
		// method and

		// 3 - include all the invocations that have are
		// transitively reached via data dependency no matter in which previous
		// method are those called.
		// For example, using a factory to create object instances will result
		// in calling the actual methods
		// inside the factory instead of calling the factory itself. TODO Link
		// this on GitLab

		// 4 -Exclude all the invocations the are subsumed by any
		// invocation already considered, i.e., that are
		// reachable by the transitive closure of the call graph of any method
		// (Call Graph)

		// At this point, for each object instance we expand the set of carved
		// test by including one by one the methods which return them, starting
		// from the ones at deeper level in the call graph

		// Carve by Time => "Before"
		List<MethodInvocation> executioneBefore = executionFlowGraph
				.getOrderedMethodInvocationsBefore(methodInvocationToCarve);
		// Extract relevant Data Dependencies from "Before"
		DataDependencyGraph subGraphFromPastExecution = dataDependencyGraph.getSubGraph(executioneBefore);

		// Do the carving by following the method invocations chain
		Set<MethodInvocation> backwardSlice = new HashSet<>(executioneBefore);
		backwardSlice.retainAll(subGraphFromPastExecution.getMethodInvocationsRecheableFrom(methodInvocationToCarve));

		// The backwardSlice is the slice which contains the MUT-centric view of
		// the execution. It disregards the location of methods which have
		// potential impact on the CUT.

		// The first carved tests considers only the direct constructors or the
		// call which returns the object (i.e., factory method, or method which
		// belongs to an external interface like: <java.nio.Files: java.io.File
		// Files.createTempFile()>;

		// Ensures that the constructors are ALL BEFORE MUT. it might happen
		// that subclasses are identified despite being called later. This is
		// because (I Suspect) Soot associates N<init> method to the same
		// instance.
		List<MethodInvocation> constructors = new ArrayList<>();
		// THIS - Unless is a static call
		if (!methodInvocationToCarve.isStatic()) {

			// For some instances, there's no INIT call, tho there's calls which
			// return it.
			MethodInvocation constructor = subGraphFromPastExecution // dataDependencyGraph
					.getInitMethodInvocationFor(subGraphFromPastExecution // dataDependencyGraph
							.getOwnerFor(methodInvocationToCarve));
			if (constructor != null) {
				constructors.add(constructor);
			} else {
				logger.info("We cannot find an INIT call for this object "
						+ subGraphFromPastExecution.getOwnerFor(methodInvocationToCarve));

				// Take the first invocation which returns the object instead
				List<MethodInvocation> methodInvocationsWhichReturnCUT = new ArrayList<>(
						subGraphFromPastExecution.getMethodInvocationsWhichReturn(
								subGraphFromPastExecution.getOwnerFor(methodInvocationToCarve)));

				try {
					constructors
							.add(Collections.min(methodInvocationsWhichReturnCUT, new Comparator<MethodInvocation>() {
								@Override
								public int compare(MethodInvocation o1, MethodInvocation o2) {
									return o1.getInvocationCount() - o2.getInvocationCount();
								}
							}));
				} catch (Throwable e1) {
					logger.info("We cannot find a call which returns this object "
							+ subGraphFromPastExecution.getOwnerFor(methodInvocationToCarve));
					// TODO: handle exception
					throw new CarvingException("Object "
							+ subGraphFromPastExecution // dataDependencyGraph
									.getOwnerFor(methodInvocationToCarve)
							+ " which is the owner of the method to carve " + methodInvocationToCarve
							+ " comes out of the blue !");
				}
			}
		}

		// DIRECT PARAMETERS
		for (ObjectInstance dataDependency : subGraphFromPastExecution // dataDependencyGraph
				.getObjectInstancesAsParametersOf(methodInvocationToCarve)) {

			// Static instances like Syste.in have no init calls
			// DO WE STILL NEED THIS PATCH ?!
			if (dataDependency.equals(ObjectInstance.SystemIn())) {
				logger.debug(
						"Level_0_MethodCarver.level0TestCarving() No INIT call for static instances " + dataDependency);
				continue;
			}

			MethodInvocation constructor = subGraphFromPastExecution // dataDependencyGraph
					.getInitMethodInvocationFor(dataDependency);
			if (constructor != null) {
				constructors.add(constructor);
			} else {
				logger.info("We cannot find an INIT call for this object " + dataDependency);

				// Take the first invocation which returns the object instead
				List<MethodInvocation> methodInvocationsWhichReturnTheDataDependency = new ArrayList<>(
						subGraphFromPastExecution.getMethodInvocationsWhichReturn(dataDependency));

				try {
					constructors.add(Collections.min(methodInvocationsWhichReturnTheDataDependency,
							new Comparator<MethodInvocation>() {
								@Override
								public int compare(MethodInvocation o1, MethodInvocation o2) {
									return o1.getInvocationCount() - o2.getInvocationCount();
								}
							}));
				} catch (Throwable e1) {
					logger.info("We cannot find a call which returns this object " + dataDependency);
					// TODO: handle exception
					throw new CarvingException(
							"Object " + dataDependency + " which is the owner of the method to carve "
									+ methodInvocationToCarve + " comes out of the blue !");
				}

			}
		} /// FIXME: Check that each of the data either is init or provided with
			/// return !

		if (minimalCarve) {
			try {
				// Generate ONLY the first carved test. This raises an
				// exceptions if the test cannot be created
				Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTestFromConstructors = generateSingleTestCaseFromSliceFor(
						methodInvocationToCarve, backwardSlice, constructors);

				carvedTestsForMethodInvocation.add(carvedTestFromConstructors);
				return carvedTestsForMethodInvocation;
			} catch (NotALevel0TestCaseException e) {
				logger.warn("while carving preconditions of " + methodInvocationToCarve + " invalid test generated as :"
						+ e.getSubsumedMethodInvocation() + " is subsubed by " + e.getSubsumingMethodInvocation()
						+ " via " + e.getSubsumingPath());
				// This should never happen, the basic test shall be there ? Not
				// really in case a constructor of a subclass is called soot
				// also report the call to the constructor of the super class.
				// So in all the cases we get one the other is subsume. This is
				// not an error if both init refer to the same owner !
				// throw new RuntimeException("This never happen as the basic
				// carved test should contain the MUT", e);
			}
		}

		// Can this recreate the carvedTestFromConstructors test case ?

		Map<ObjectInstance, Set<MethodInvocation>> returningCalls = new HashMap<>();

		// Now find all the calls which returns any of the objectInstances
		// in the data dependency graph. This one is odd, if we consider plenty
		// of objects we get a huge number of possibilities
		// which then we filter away... On the other size, if we do not consider
		// this, how do we include calls to external libraries, maybe simply by
		// looking into the exec graph ?

		System.out.println("Level_0_MethodCarver.level0TestCarving() All object instances : "
				+ subGraphFromPastExecution.getObjectInstances().size());
		// XXX We try to use the following methods to reduce the amount of
		// ObjectInstances considered, but failed to create some preconditions
		// and generated wrong test cases:
		// System.out.println("Level_0_MethodCarver.level0TestCarving() Object
		// used before : "
		// +
		// subGraphFromPastExecution.getObjectInstancesUsedBefore(methodInvocationToCarve).size());
		// System.out.println("Level_0_MethodCarver.level0TestCarving() Object
		// used before by external interfaces : "
		// +
		// subGraphFromPastExecution.getObjectInstancesUsedBeforeByExternalInterfaces(methodInvocationToCarve)
		// .size());

		for (ObjectInstance objectInstance : subGraphFromPastExecution.getObjectInstances()) {

			Set<MethodInvocation> returningCallsForObjectInstance = subGraphFromPastExecution // dataDependencyGraph
					.getMethodInvocationsWhichReturn(objectInstance);

			// Some calls return objects that are also their parameters, we need
			// to rule them out otherwise
			// we generate "invalid/impossible" test cases, which require to
			// execute the call to provide a parameter of the same call

			/*
			 * If from the returning call, by GOING BACKWARDS, I stumble upon
			 * the same object, I cannot use that call as returning one... Since
			 * the call is changing the object, and we put the object as input,
			 * we have the object already. This might not be true in the
			 * original java, e.g., : Path p = Files.write(Paths.get("p"),
			 * bytes); But it will in the carved one:
			 * 
			 * Path _p = Paths.get("p"); Path p = Files.write(_p, bytes);
			 * 
			 */

			for (Iterator<MethodInvocation> retCallIterator = returningCallsForObjectInstance
					.iterator(); retCallIterator.hasNext();) {
				MethodInvocation retCall = retCallIterator.next();
				if (subGraphFromPastExecution.getObjectInstancesAsParametersOf(retCall).contains(objectInstance)) {
					System.out
							.println(">>> The method " + retCall + " returns one of its parameters " + objectInstance);
					retCallIterator.remove();
				}
			}

			// Here we need to include the constructors as well to create
			// the cross products. Note that sometimes, because the
			// instrumentation cannot cover "everything" we might not be
			// able to initialize
			// all the instances: For example:
			// org.junit.contrib.java.lang.system.TextFromStandardInputStream$SystemInMock@463345942
			// Still we like to carve out some test
			MethodInvocation init = subGraphFromPastExecution // dataDependencyGraph
					.getInitMethodInvocationFor(objectInstance);
			if (init != null) {
				returningCallsForObjectInstance.add(init);
			}

			// System.out.println("Level_0_MethodCarver.level0TestCarving() " +
			// objectInstance + " --> "
			// + returningCallsForObjectInstance.size());

			if (returningCallsForObjectInstance.size() != 0) {
				returningCalls.put(objectInstance, returningCallsForObjectInstance);
			} else {
				// There no init call or method returning public static
				// instances like System.in
				if (!objectInstance.equals(ObjectInstance.SystemIn())) {
					throw new CarvingException("Cannot find either INIT or returning calls for " + objectInstance);
				}
			}

		}

		// And generate the possible combinations by computing the Cartesian
		// Product of those calls per. For each call generate a new carvedTest
		/// FIXME This becomes HUGE, probably we shall consider some
		// optimization ?
		// See: #39
		// The problem is rooted in the fact that many StringBuilder are
		// generated and re-generated all over the places. Those do not usually impact the final results...
		// For example System.out.println("A " + foo()); is handled by generating a StringBuilder.
		// TODO: In case it turns out to be a problem, simply discard combinations of StringBuilder, or StringBuilders entirely
		

		// Pre select the returnin calls to reduce the number of their
		// combination, since ATM there's no evidence this will impact the
		// quality of the results. Ideally this might have an impact on the SIZE
		// of the carved test case because we might invoke less calls to setup
		// the preconditions (e.g., use Factory methods) or avoid to generate
		// impossible tests (private methods? )
		//

		for (Entry<ObjectInstance, Set<MethodInvocation>> entry : returningCalls.entrySet()) {
			if (entry.getValue().size() > 1) {
				// Pick the first call, the one which has smaller id
				// System.out.println("Level_0_MethodCarver.level0TestCarving()
				// Pre-select returning call for: " + entry.getKey() );
				MethodInvocation preSelected = Collections.min(entry.getValue());
				entry.getValue().clear();
				entry.getValue().add(preSelected);
			}
		}

		Set<List<MethodInvocation>> fullCartesianProduct = Sets
				.cartesianProduct(new ArrayList<>(returningCalls.values()));

		if (fullCartesianProduct.size() > 1000) {
			logger.warn("The cartesian products of object instance is too big " + fullCartesianProduct.size()
					+ ". Expect scalability issue !");
			// + fullCartesianProduct);
		}

		for (List<MethodInvocation> combination : fullCartesianProduct) {
			try {
				Pair<ExecutionFlowGraph, DataDependencyGraph> testCase = generateSingleTestCaseFromSliceFor(
						methodInvocationToCarve, backwardSlice, combination);
				//
				carvedTestsForMethodInvocation.add(testCase);
			} catch (NotALevel0TestCaseException e) {
				// This might happen as consequence of including calls which
				// subsume methodInvocationToCarve
				// Note that this might be a call required as precondition by
				// any of the returning calls which
				// compose this combination. TODO We can make it more specific
				// by cross checkig the subsuming calls in callGraph and the one
				// in the generated test case
				assert methodInvocationToCarve.equals(e.getSubsumedMethodInvocation());
				if (!methodInvocationToCarve.equals(e.getSubsumedMethodInvocation())) {
					logger.warn(" The subsumed call " + e.getSubsumedMethodInvocation()
							+ " is not the one under carving " + methodInvocationToCarve);
				}
				logger.warn("Invalid test case for " + e.getSubsumedMethodInvocation() + " which is subsumed by "
						+ e.getSubsumingMethodInvocation() + " via " + e.getSubsumingPath());
			} catch (RuntimeException e) {
				e.printStackTrace();
				logger.debug("Swallow : " + e);
			}
		}

		if (logger.isDebugEnabled()) {
			if (!minimalCarve) {
				for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTestsForMethodInvocation) {
					logger.info("\t Test : " + carvedTest.getFirst().getOrderedMethodInvocations());
				}
			}
		}

		return carvedTestsForMethodInvocation;
	}

	private Pair<ExecutionFlowGraph, DataDependencyGraph> generateSingleTestCaseFromSliceFor(
			MethodInvocation methodInvocationToCarve, Set<MethodInvocation> backwardSlice,
			List<MethodInvocation> dataReturningCalls) throws NotALevel0TestCaseException {

		logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() for " + dataReturningCalls
				+ " from backward slice " + backwardSlice);

		// FIXME If the call is the only thing inside the backwardSlice we might
		// short-circuit here probably

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
					preconditionCache.put(returnCall, computePreconditionsFor(returnCall));
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
			// System.out.println("Level_0_MethodCarver.generateSingleTestCaseFromSliceFor()
			// FOUND "
			// + methodInvocationToCarve + " inside subsumed set by " + mi);
			// }

			subsumedCalls.addAll(subsumedBy);
		}

		/*
		 * If the subsumed call is also the method under carving, the resulting
		 * test case cannot be defined as Level_0. So we stop its generation
		 * directly here. Not that we could simply return null, or something,
		 * but this way its clear what did happen.
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
				!dataDependencyGraph.getObjectInstancesAsParametersOf(returnCall).isEmpty()) {

			// This might return an empty set of precondition for calls
			// that are subsumed, like constructors of super- and sub-
			// classes.
			// TODO Try to see if you can avoid doing the precondition
			// check for those specific cases (which might be also
			// chains)

			try {
				List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedPreconditions = level0TestCarving(returnCall,
						true);

				if (!carvedPreconditions.isEmpty()) {
					//
					preconditions.addAll(carvedPreconditions.get(0).getFirst().getOrderedMethodInvocations());

					/*
					 * Here we do not really care about data dep, since the
					 * later call should include them as well !?
					 */
					logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Preconditions are : "
							+ preconditions);

					if (carvedPreconditions.size() > 1) {
						System.out.println("Level_0_MethodCarver.computePreconditionsFor() MULTIPLE PRECONDITIONS FOR "
								+ returnCall);
					}
				} else {
					logger.trace("Level_0_MethodCarver.generateSingleTestCaseFromSlice() Method " + returnCall
							+ " has no preconditions");
				}
			} catch (CarvingException e) {
				logger.error("Cannot compute preconditions for return call " + returnCall);
				e.printStackTrace();
				System.out.println("Level_0_MethodCarver.generateSingleTestCaseFromSliceFor() WHAT TO DO NOW ?");
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
	public List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carve(MethodInvocationMatcher carveBy,
			MethodInvocationMatcher excludeBy) {
		List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

		for (MethodInvocation methodInvocationUnderTest : executionFlowGraph.getMethodInvocationsFor(carveBy,
				/* excludeMain, excludeJavaLang, */
				excludeBy)) {

			if (methodInvocationUnderTest.isPrivate()) {
				logger.info("We do not carve private methods");
			}

			List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTestsPetMethodInvocation = new ArrayList<>();

			try {
				carvedTestsPetMethodInvocation.addAll(level0TestCarving(methodInvocationUnderTest, false));

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

				simplify(methodInvocationUnderTest, carvedTestsPetMethodInvocation);

				// Add to big list
				carvedTests.addAll(carvedTestsPetMethodInvocation);
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
				if (methodInvocation.belongsToExternalInterface()) {
					coreMethodInvocations
							.addAll(dataDependencyGraph.getWeaklyConnectedComponentContaining(methodInvocation));
				}
			}

			dataDependencyGraph.refine(coreMethodInvocations);
			executionFlowGraph.refine(coreMethodInvocations);
		}
	}

}
