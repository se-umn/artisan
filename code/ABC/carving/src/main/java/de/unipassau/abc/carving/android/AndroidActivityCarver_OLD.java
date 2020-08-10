package de.unipassau.abc.carving.android;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.MethodCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.CallGraphImpl;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.NullInstance;
import de.unipassau.abc.data.NullNodeFactory;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.PrimitiveNodeFactory;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.data.ValueNode;
import de.unipassau.abc.exceptions.ABCException;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;

/**
 * Challenge 1: choosing the right way to setup a preconditions
 * 
 * Challenge 2: how to deal/handle objects provided by the runtime framework
 * "external": carving or mocking, maybe tainting ?
 * 
 * @author gambi
 *
 */
public class AndroidActivityCarver_OLD implements MethodCarver {

	private final Logger logger = LoggerFactory.getLogger(AndroidActivityCarver_OLD.class);

	private ExecutionFlowGraph executionFlowGraph;
	// TODO How this is not used
	private DataDependencyGraph dataDependencyGraph;
	// This one we need to use for computing if
	private CallGraph callGraph;

	/**
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @param callGraph
	 */
	public AndroidActivityCarver_OLD(ExecutionFlowGraph executionFlowGraph, //
			DataDependencyGraph dataDependencyGraph, //
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;

		initializeMockery();

	}

	private SootClass mockitoClass, ongoingStubbingClass;
	private SootMethod mockMethod, whenMethod, thenReturnManyResultMethod;

	private void initializeMockery() {
		mockitoClass = Scene.v().getSootClass(Mockito.class.getName());
		mockMethod = mockitoClass.getMethod("mock", Arrays.asList(new Type[] { RefType.v(Class.class.getName()) }));
		// Only one whenMethod method
		whenMethod = mockitoClass.getMethodByName("when");

		ongoingStubbingClass = Scene.v().getSootClass(OngoingStubbing.class.getName());

		thenReturnManyResultMethod = ongoingStubbingClass.getMethod("thenReturn", Arrays.asList(
				new Type[] { RefType.v(Object.class.getName()), RefType.v(Object.class.getName()).getArrayType() }));
	}

	@Override
	public List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carve(MethodInvocation methodInvocation)
			throws CarvingException, ABCException {
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest = carveTheMethodInvocationRecursive(
				methodInvocation);
		List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedExecutions = new ArrayList<>();
		carvedExecutions.add(carvedTest);
		return carvedExecutions;
	}

	/**
	 * Carve tests out of all the MethodInvocations which match the carveBy
	 * expression
	 * 
	 * @param carveBy
	 * @return
	 * @throws CarvingException
	 */
	@Override
	public Map<MethodInvocation, List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>> carve(
			List<MethodInvocation> orderedMethodsInvocationsToCarve) throws CarvingException {

		Map<MethodInvocation, List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>> carvedTests = new HashMap<MethodInvocation, List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>>();

		/*
		 * By ordering them we should be able to exploit the precondition cache and
		 * incrementally carve later invocations from previous carved invocations
		 */
		Collections.sort(orderedMethodsInvocationsToCarve);

		for (MethodInvocation methodInvocationUnderTest : orderedMethodsInvocationsToCarve) {

			List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTestsPerMethodInvocation = new ArrayList<>();
			carvedTests.put(methodInvocationUnderTest, carvedTestsPerMethodInvocation);

			/*
			 * Skip methods which has no sense to carve
			 */
			if (methodInvocationUnderTest.isPrivate()) {
				logger.info("We do not carve private methods " + methodInvocationUnderTest);
				continue;
			}

			/*
			 * Skip synthetic methods which does not exists in the original code
			 */
			if (methodInvocationUnderTest.isSynthetic()) {
				logger.info("We do not carve synthetic methods " + methodInvocationUnderTest);
				continue;
			}

			/*
			 * Skip activity constructors as those should never be there/implemented
			 */
			if (methodInvocationUnderTest.isConstructor()) {
				logger.info("We do not carve Activity constructors " + methodInvocationUnderTest);
				continue;
			}

			if (isTriviallyUncarvable(methodInvocationUnderTest)) {
				logger.info("\n\n====================================================\n" //
						+ " Skip carving of " + methodInvocationUnderTest + " as trivially uncarvable \n" //
						+ "====================================================");
				// Explain why this is trivially uncarvable
				logger.info("The following method subsumes " + methodInvocationUnderTest + ":"
						+ prettyPrint(callGraph.getOrderedSubsumingMethodInvocationsFor(methodInvocationUnderTest)));
				continue;

			}
			try {
				long carvingTime = System.currentTimeMillis();

				logger.info("\n\n====================================================\n" //
						+ "Starting the carve of " + methodInvocationUnderTest + "\n" //
						+ "====================================================");

				List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedTest = carve(
						methodInvocationUnderTest);

				carvedTestsPerMethodInvocation.addAll(carvedTest);

				carvingTime = System.currentTimeMillis() - carvingTime;
				logger.info("\n\n====================================================\n" //
						+ "Carved  " + carvedTestsPerMethodInvocation.size() + " in " + +carvingTime + " msec \n" //
						+ "====================================================");

			} catch (CarvingException e) {
				// e.printStackTrace();
				logger.warn("\n\n====================================================\n" //
						+ "Cannot carve test for " + methodInvocationUnderTest + " :: " + e.getMessage() + "\n"
						+ "====================================================");

				// DEBUG/DEVELOPMENT
				// if( e.getMessage().contains("More than one activity type")){
				// throw e;
				// }

			} catch (Throwable e) {
				// THIS IS UNEXPECTED
				e.printStackTrace();
				logger.warn("\n\n====================================================\n" //
						+ "Cannot carve test for " + methodInvocationUnderTest + " :: " + e.getMessage() + "\n"
						+ "====================================================");
			}
		}

		return carvedTests;
	}

	private String prettyPrint(Collection<MethodInvocation> methodInvocations) {
		StringBuffer stringBuffer = new StringBuffer();
		for (MethodInvocation methodInvocation : methodInvocations) {
			stringBuffer.append(methodInvocation.toString()).append("\n");
		}
		return stringBuffer.toString();
	}

	/**
	 * A method invocation is trivially uncarvable if is subsumed by another method
	 * from the same owner
	 * 
	 * @param methodInvocationUnderTest
	 * @return
	 */
	private boolean isTriviallyUncarvable(MethodInvocation methodInvocationUnderTest) {
		if (methodInvocationUnderTest.isStatic()) {
			return false;
		}

		// All the method invocations by the owner - and its aliases
		Set<MethodInvocation> methodInvocationsFromOwnerBefore = new HashSet<>(
				dataDependencyGraph.getMethodInvocationsForOwner(methodInvocationUnderTest.getOwner()));
		for (ObjectInstance alias : dataDependencyGraph.getAliasesOf(methodInvocationUnderTest.getOwner())) {
			methodInvocationsFromOwnerBefore.addAll(dataDependencyGraph.getMethodInvocationsForOwner(alias));
		}

		// Take only the ones before methodInvocationUnderTest
		MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.after(methodInvocationUnderTest),
				methodInvocationsFromOwnerBefore);
		// Remove the methodInvocation itself
		methodInvocationsFromOwnerBefore.remove(methodInvocationUnderTest);

		// Compute the method invocations subsumed-by the one before
		Set<MethodInvocation> subsumedByOwnerBefore = new HashSet<>();
		for (MethodInvocation methodInvocation : methodInvocationsFromOwnerBefore) {
			subsumedByOwnerBefore.addAll(callGraph.getMethodInvocationsSubsumedBy(methodInvocation));
			subsumedByOwnerBefore.add(methodInvocation);
		}

		// Check if methodInvocationUnderTest belongs to that set
		return subsumedByOwnerBefore.contains(methodInvocationUnderTest);
	}

	private final static List<String> forbiddenTypes = Arrays.asList(
			new String[] { "android.support.v4.app.FragmentManagerImpl", "android.support.v4.app.BackStackRecord" });

	/**
	 * Carving is a recursive activity -> carving methods and carving "data". This
	 * is the base call.
	 * 
	 * In theory carving might produce several different setup, we force this to
	 * return one of them.
	 * 
	 * TODO If the cache is reinitialized everytime that might not be smart ....
	 * Also consider to store the cache somewhere on the disk !!
	 * 
	 * @param methodInvocationToCarve
	 * @return
	 * @throws CarvingException
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveTheMethodInvocationRecursive(
			MethodInvocation methodInvocationToCarve) throws CarvingException {

		/**
		 * Those cache objects contains the fragments of the original graphs which are
		 * necessary to set the preconditions of the methods and objectinstances.
		 */
		Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods = new HashMap<>();
		Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects = new HashMap<>();
		Set<Object> workList = new HashSet<>();

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest = carveMethod(methodInvocationToCarve,
				workList, alreadyCarvedMethods, alreadyCarvedObjects);

		ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();
		DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();

		// This contains all the necessary invocations we now need to compress
		// this while keep the test valid.
		logger.debug("Carved Test: \n" + prettyPrint(executionFlowGraph));

		/*
		 * Let's do some android specific carving.
		 */

		/*
		 * Life-cycle fragment events are related to UI changes, since we do not care
		 * about them, we can remove them from the equation. Including their subsumed
		 * methods...
		 */
		logger.debug("Removing all the method invocations subsumed by fragment lifecycle events");

		List<MethodInvocation> fragmentLifeCycleEvents = this.executionFlowGraph
				.getOrderedMethodInvocationsBefore(methodInvocationToCarve);

		/*
		 * Keep only the fragment lifecycle events
		 */
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.isFragmentLifeCycle(),
				fragmentLifeCycleEvents);

		/*
		 * Identify all the method invocations subsumed by observed
		 * fragmentLifeCycleEvents. Include the fragmentLifeCycleEvents themselves
		 */

		Set<MethodInvocation> fragmentLifeCycleEventsSubsumedMethodCalls = new HashSet<>();
		for (MethodInvocation fragmentLifeCycleEvent : fragmentLifeCycleEvents) {
			fragmentLifeCycleEventsSubsumedMethodCalls
					.addAll(this.callGraph.getMethodInvocationsSubsumedBy(fragmentLifeCycleEvent));
			fragmentLifeCycleEventsSubsumedMethodCalls.add(fragmentLifeCycleEvent);
		}

		/*
		 * Remove from the execution traces all those methods
		 */
		Set<MethodInvocation> requiredMethodInvocations = new HashSet<>();
		requiredMethodInvocations.addAll(executionFlowGraph.getOrderedMethodInvocations());

		for (Iterator<MethodInvocation> iterator = requiredMethodInvocations.iterator(); iterator.hasNext();) {
			MethodInvocation methodInvocation = iterator.next();
			if (fragmentLifeCycleEventsSubsumedMethodCalls.contains(methodInvocation)) {
				if (methodInvocation.isAndroidFragmentCallback()) {
					logger.debug("Removing methodInvocation " + methodInvocation + " or it is a Fragment life cycle ");
				} else {
					logger.debug("Removing methodInvocation " + methodInvocation
							+ " as it is subsumed by a Fragment life cycle "
							+ this.callGraph.getOrderedSubsumingMethodInvocationsFor(methodInvocation));
				}
				iterator.remove();
			}
		}

		/*
		 * Keep only the invocations that can be found in the provided set
		 */
		executionFlowGraph.refine(requiredMethodInvocations);
		dataDependencyGraph.summarize(executionFlowGraph);

		/*
		 * Remove all the method invocations related to elements which cannot be invoked
		 * directly:
		 */
		requiredMethodInvocations.clear();
		//
		requiredMethodInvocations = new HashSet<>();
		requiredMethodInvocations.addAll(executionFlowGraph.getOrderedMethodInvocations());

		logger.debug("Removing all the method invocations related to forbidden objects");

		for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
			if (forbiddenTypes.contains(objectInstance.getType())) {
				// Remove all the invocations on forbidden objects
				MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.byOwner(objectInstance),
						requiredMethodInvocations);
				// Remove all the invocations to methods which require forbidden
				// objects as parameters
				MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.asParameter(objectInstance),
						requiredMethodInvocations);
				// Remove all the invocations to methods which return instances
				// of forbidden objects
				MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.byReturnValue(objectInstance),
						requiredMethodInvocations);
			}
		}

		/*
		 * Keep only the invocations that can be found in the provided set
		 */
		executionFlowGraph.refine(requiredMethodInvocations);
		dataDependencyGraph.summarize(executionFlowGraph);

		logger.debug("Carved Test after removing Fragments related method invocations: \n"
				+ prettyPrint(executionFlowGraph));

		/*
		 * Check if the MUT was already subsumed by a lifecycle event
		 */
		if (!executionFlowGraph.contains(methodInvocationToCarve)) {
			logger.warn("Method under test was removed by a lifecycle event");
			// Explain.
			logger.info(
					"Subsuming path: " + callGraph.getOrderedSubsumingMethodInvocationsFor(methodInvocationToCarve));
			throw new CarvingException("Method under test subsumed by lifecycle event !");
		}

		/*
		 * Fix visibility of methods regarding activity lifecycle by including calls of
		 * app-level activity which subsumes non-callable methods of alias (e.g., calls
		 * to super class)
		 */

		logger.debug("Fixing Activity lifecycle method invocations");

		Set<MethodInvocation> activityLifecycleMethodInvocations = new HashSet<>();
		activityLifecycleMethodInvocations.addAll(executionFlowGraph.getOrderedMethodInvocations());
		// Keep only activity life cycle method invocations
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.isActivityLifeCycle(),
				activityLifecycleMethodInvocations);
		// Include same activity life cycle events of their aliases (this
		// includes both super and sub classes)
		Set<MethodInvocation> allActivityLifecycleMethodInvocations = new HashSet<>();
		for (MethodInvocation activityLifecycleMethodInvocation : activityLifecycleMethodInvocations) {
			// TODO Not sure if this will contains the
			// activityLifecycleMethodInvocation
			allActivityLifecycleMethodInvocations
					.addAll(getSameMethodInvocationFromAliases(activityLifecycleMethodInvocation));
			allActivityLifecycleMethodInvocations.add(activityLifecycleMethodInvocation);
		}

		// Build a temporary executionFlowGraph to host the new method
		// invocations:
		ExecutionFlowGraph acitivityLifecycleExecutionFlowGraph = new ExecutionFlowGraphImpl();
		// Fill the graph by enqueueing the calls, this requires a sorted list
		List<MethodInvocation> sorted = new ArrayList<>();
		sorted.addAll(allActivityLifecycleMethodInvocations);
		Collections.sort(sorted);

		for (MethodInvocation methodInvocation : sorted) {
			logger.trace("AndroidMethodCarver.carveTheMethodInvocationRecursive() Including LIFECYCLE : "
					+ methodInvocation);
			acitivityLifecycleExecutionFlowGraph.enqueueMethodInvocations(methodInvocation);
		}
		DataDependencyGraph acitivityLifecycleDataDependecyGraph = this.dataDependencyGraph
				.getSubGraph(acitivityLifecycleExecutionFlowGraph);

		executionFlowGraph.include(acitivityLifecycleExecutionFlowGraph);
		dataDependencyGraph.include(acitivityLifecycleDataDependecyGraph);

		logger.debug(
				"Carved Test after fixing Activity lifecycle method invocations: \n" + prettyPrint(executionFlowGraph));

		// Generate a data dep grapsh which contains the required data deps for
		// the acitivityLifecycleExecutionFlowGraph
		// /*
		// * At this point, i.e., after having considered the lifecycle of
		// * activity and fragments, we need to remove all the remaining calls
		// to
		// * synthetic methods Since those methods do not really exist (ABC
		// * dynamically generated them), hence they trivially cannot be
		// invoked.
		// */
		// requiredMethodInvocations =
		// executionFlowGraph.getOrderedMethodInvocations();
		// MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.isSynthetic(),
		// requiredMethodInvocations);
		//
		// executionFlowGraph.refine(new HashSet<>(requiredMethodInvocations));
		// dataDependencyGraph.summarize(executionFlowGraph);

		/*
		 * Remove any duplicated method invocation
		 * 
		 * Summarize carved method invocations: If an invocation is subsumed by another
		 * invocation, we can remove it.
		 */
		logger.trace("Before Summarization: \n " + prettyPrint(executionFlowGraph));

		executionFlowGraph.summarize(callGraph);
		dataDependencyGraph.summarize(executionFlowGraph);

		logger.trace("After Summarization: \n " + prettyPrint(executionFlowGraph));

		/*
		 * Validate test: After summarization the "method under test" must be there !
		 */

		if (!executionFlowGraph.contains(methodInvocationToCarve)) {
			// TODO In many cases uncarvable methos invocations can be spotted
			// directly by checking for subsumption from methods of the same
			// owner !
			logger.warn("Method under test was removed after summarization");
			throw new CarvingException("No more method under test !");
		}
		// TODO Do we need a navigation graph ? A1 startWithResult -> A2 -> A3
		// -> etc. ?

		/*
		 * Get the list of activities executed before the current one. This includes any
		 * activity instance which is different than the current one. There are
		 * different cases:
		 * 
		 * A: Lifecycle. User navigates away and comes back to the SAME activity (but
		 * possibly another instance if the previous one was destroyed). How to
		 * discriminate? One or more instances of the same type of activity but not
		 * other activities with different type in between
		 * 
		 * It's semantically the same activity, but the activity was killed by Android
		 * -> No intent is present here, it was just a lifecycle thingy. Hence the
		 * lifecycle events must match (onDestroy -> init)
		 * 
		 * B: Invocation/Navigation. User navigates to another activity, the current
		 * activity get's paused. - Tests: 1 . Second activity starts (from First
		 * activity POV) 2 . Second activity starts with intent (form Second activity
		 * POV).
		 * 
		 * In case there are instances of the same activity, this is semantically a
		 * different activity (see #2). Started via broadcast/startActivity
		 * 
		 * there must be an startActivity method with an intent which points to this
		 * activity, i.e., the target class has the same type of the current activity.
		 * 
		 * This is a simplification which does not account for activities" of the app
		 * which are started from the outside while the app is under execution. We do
		 * not also consider the case in which the app uses a broadcast message, and the
		 * app iteself it is choosen to handle the message.
		 * 
		 * C: Return from another activity. Users returns to previous activity (with or
		 * without result). - Tests: 1.A: First activity setup and starts second
		 * activity, away results, test build the results and send intent back 1.B:
		 * First activity setup and starts second activity, do not await results, test
		 * should setup the state (VERY DIFFICULT OR IMPOSSIBLE!) How to discriminate ?
		 * In the scope of currentActivity we see the onActivityResult() -> we look for
		 * the corresponding (last) startActivity One or more instances of the same type
		 * of activity but other activities with different type in between There must be
		 * a startActivity with Intent of given class in between
		 * 
		 * This is a simplification which does not account for activities" of the app
		 * which are started from the outside while the app is under execution. We do
		 * not also consider the case in which the app uses a broadcast message, and the
		 * app iteself it is choosen to handle the message.
		 * 
		 */

		/*
		 * In case of multi activities the carved execution already contains all of them
		 * and their relative lifecycle events. This returns the reference (in order of
		 * appearance) of the various activity instances and a context
		 * (methodInvocations) of the transition. We need this in case the user
		 * ping-pongs between two activities...
		 * 
		 * We need to create some call graph between activities to match their
		 * invocations and carve away the rest. The final test should FOCUS only on a
		 * SINGLE LOGICAL activity.
		 */

		/*
		 * TODO If we build the activity graph at the beginning we might considerably
		 * speed-up the carving process since we narrow it down to specific regions of
		 * the trace instead of carving everything for discard this later??
		 */

		ActivityGraph activityGraph = buildActivityGraph(executionFlowGraph, dataDependencyGraph);

		if (activityGraph.isEmpty()) {
			logger.warn("Carved test contains no activities!");
			throw new CarvingException("Carved test contains no activities!");
		} else if (activityGraph.getActivityCount() == 1) {
			logger.info("Found only one activity (with context) ");
		} else {
			logger.info("Found the following activities (with context) " + activityGraph.getActivities());

			// We need to locate the MUT: Activity (AUT)
			// This return the node which containts the MUT
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location = activityGraph
					.locate(methodInvocationToCarve);

			if (activityGraph.isStartingOfActivity(location)) {
				logger.info(methodInvocationToCarve + " belongs to the start of activity" + location);
				// To carve a method in this location we need.
				// 1. The starting intent (unless location points to the
				// main/root activity)
				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> intentCarving = null;
				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> externalStateChanging = null;

				Pair<ObjectInstance, MethodInvocation> requestIntentWithContext = activityGraph
						.getRequestIntentWithContextForActivity(location);
				if (requestIntentWithContext != null) {
					logger.debug("Carving Intent " + requestIntentWithContext.getFirst() + " with context "
							+ requestIntentWithContext.getSecond());

					// TODO: This is a special type of carving: treat parameters
					// of constructor as primitives/references, basically, do
					// not propagate the carving to the "this/context" parameter
					// otherwise we will include the activity
					// which has started the Intent in the first place !

					intentCarving = carveTheIntent(requestIntentWithContext.getFirst(),
							requestIntentWithContext.getSecond(),
							// Those should be already empty
							workList,
							//
							alreadyCarvedMethods, alreadyCarvedObjects);
				}

				/*
				 * 2. Remove all the calls which belong to a different activity
				 */
				List<MethodInvocation> methodsOwnedByTheActivity = executionFlowGraph.getOrderedMethodInvocations();
				MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.byAnotherActivity(location.getFirst()),
						methodsOwnedByTheActivity);
				executionFlowGraph.refine(new HashSet<>(methodsOwnedByTheActivity));
				dataDependencyGraph.summarize(executionFlowGraph);

				/*
				 * 3. Calls to external libraries from previous method calls but without
				 * considering the left over
				 */
				externalStateChanging = carvePotentiallyExternalStateChangingMethods(methodInvocationToCarve,
						activityGraph, location);

				if (intentCarving != null) {
					executionFlowGraph.include(intentCarving.getFirst());
					dataDependencyGraph.include(intentCarving.getSecond());
				}

				executionFlowGraph.include(externalStateChanging.getFirst());
				dataDependencyGraph.include(externalStateChanging.getSecond());

			} else if (activityGraph.isLeftOverFromStartingOfActivity(location)) {
				logger.info(methodInvocationToCarve + "is the left over after a Start of activity" + location);
			} else if (activityGraph.isReturnFromActivity(location)) {
				logger.info(methodInvocationToCarve + " is the return of activity" + location);
			} else if (activityGraph.isLeftOverFromReturnFromActivity(location)) {
				logger.info(methodInvocationToCarve + " is the left over after a Return of activity" + location);
			} else {
				// NULL ?
				logger.warn("Cannot identify location" + location + " not an interesting life-cycle event");
			}

			// Do we need to carve the START, RETURN or LEFT-OVER, of AUT?

			// START: Intent that started AUT (Intent, bundle, etc.) + setup
			// from previous invocations (files, db, etc.)

			// RETURN: Intent that AUT used to start the activity which
			// returned, Intent generated by the activity which returned + setup
			// of the previous invocations (including AUT before going "down")

			// LEFT-OVER: Carve LEFT OVER and START or RETURN depending which
			// one is the closest

		}

		/*
		 * Remove any duplicated method invocation
		 * 
		 * Summarize carved method invocations: If an invocation is subsumed by another
		 * invocation, we can remove it.
		 */
		logger.trace("Before Summarization: \n " + prettyPrint(executionFlowGraph));

		executionFlowGraph.summarize(callGraph);
		dataDependencyGraph.summarize(executionFlowGraph);

		logger.trace("After Summarization: \n " + prettyPrint(executionFlowGraph));

		// At this point having multiple instances of activities is most likely
		// an error...mult
		activityGraph = buildActivityGraph(executionFlowGraph, dataDependencyGraph);

		if (activityGraph.isEmpty()) {
			logger.warn("Carved test contains no activities!");
			throw new CarvingException("Carved test contains no activities!");
		}
		if (activityGraph.getActivityCount() > 1) {
			logger.warn("Carved test contains more than one activity!");
			throw new CarvingException("Carved test contains more than one activity!");
		}
		/*
		 * The test cannot directly call non-visible methods or class definitions. We
		 * assume that the carved test is in the same package of the CUT
		 */
		String packageName = JimpleUtils.getPackage(methodInvocationToCarve.getMethodSignature());

		MethodInvocationMatcher nonVisibleMethodInvocationMatcher = MethodInvocationMatcher.or(
				MethodInvocationMatcher.visibilityPrivate(),
				MethodInvocationMatcher.not(MethodInvocationMatcher.isVisibleFromPackage(packageName)));

		// Return the elements matching the filter
		Set<MethodInvocation> nonVisibleMethodInvocations = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
				nonVisibleMethodInvocationMatcher, executionFlowGraph.getOrderedMethodInvocations());

		if (!nonVisibleMethodInvocations.isEmpty()) {
			logger.warn("Carved test contains direct calls to non-visible method invocations: "
					+ nonVisibleMethodInvocations.toString().replaceAll(",", "\n"));
			throw new CarvingException("Carved tests calls non-visible methods directly !");
		}

		// We cannot instantiate explicitly Lambda functions. Maybe we can get a
		// reference to the class defining them, but invoking them I think it is
		// possible only via
		// their declaring class/instance ..
		// https://stackoverflow.com/questions/34589435/get-the-enclosing-class-of-a-java-lambda-expression
		// target class name + "$$Lambda$" + a counter.
		MethodInvocationMatcher lambdaMethodInvocationMatcher = MethodInvocationMatcher.lambda();
		Set<MethodInvocation> lambdaMethodInvocations = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
				lambdaMethodInvocationMatcher, executionFlowGraph.getOrderedMethodInvocations());
		if (!lambdaMethodInvocations.isEmpty()) {
			logger.warn("Carved test contains lamdba method invocations: "
					+ lambdaMethodInvocations.toString().replaceAll(",", "\n"));
			throw new CarvingException("Carved test calls lamdba functions directly !");
		}

		/*
		 * Validate that in the test all object instances do not appear out of the blue
		 * unless they are null. For not-null object instances either there's a call to
		 * a constructor or some calls which returns them.
		 */
		if (!dataDependencyGraph.verifyObjectInstanceProvenance()) {
			List<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContext = new ArrayList<>();
			/*
			 * We need to remove the objects which do not explicitly appear in the
			 * executions trace, in the hope of getting rid of the aliases... Otherwise we
			 * need to choose the class for the aliases (i.e., the one deepest in the
			 * hierarchy?)
			 */
			for (Iterator<ObjectInstance> objectInstanceIterator = dataDependencyGraph.getDanglingObjects()
					.iterator(); objectInstanceIterator.hasNext();) {
				// Why I cannot put this inside the for statement?
				ObjectInstance objectInstance = objectInstanceIterator.next();
				// Check if this objectInstance is ever used by any method in
				// the carved test
				MethodInvocation objectUsageContext = null;
				for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
					if (objectInstance.equals(methodInvocation.getOwner())
							|| methodInvocation.getActualParameterInstances().contains(objectInstance)) {
						objectUsageContext = methodInvocation;
						break;
					}
				}
				if (objectUsageContext == null) {
					// The object was never used
					objectInstanceIterator.remove();
				} else {
					// The context for the dangling objects is the method to
					// carve
					danglingObjectWithContext
							.add(new Pair<ObjectInstance, MethodInvocation>(objectInstance, objectUsageContext));
				}
			}

			if (danglingObjectWithContext.size() > 0) {

				logger.warn("The following objects appear out of the blue: " + danglingObjectWithContext
						+ " try to mockMethod them");

				/*
				 * At this point, instead of giving up we try to recover those objects and
				 * create mocks (BoxedPrimitives have a value attached to them, so we do not
				 * technically create a mockMethod for them).
				 * 
				 * Note the tricky point is to get the expected return values from mockMethod.
				 * Are those returned values being mocked by default or shall we carve them? And
				 * why? - Mocking: conceptually those are in the right place, but we need
				 * complete visibility - Carving: we might pass real objects around so we do not
				 * care about complete visibility
				 * 
				 * TODO For the moment we MOCK only.
				 * 
				 * If for whatever reason we cannot mockMethod an object we fail...
				 */

				/*
				 * We passing the iterator around to include the additional objects to
				 * mockMethod with their "possibly new context" dynamically. The context might
				 * also help us to insert the mocks creation and configuration at the right
				 * place... i.e., before their use!
				 */
				for (Iterator<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContextIterator = danglingObjectWithContext
						.iterator(); danglingObjectWithContextIterator.hasNext();) {
					Pair<ObjectInstance, MethodInvocation> objectInstanceToMockWithContext = danglingObjectWithContextIterator
							.next();
					/*
					 * Introduce calls to Mockito which returns the expected DataNode. We use the
					 * method under test as context for the analysis
					 */
					ObjectInstance objectInstanceToMock = objectInstanceToMockWithContext.getFirst();
					MethodInvocation mockingContext = objectInstanceToMockWithContext.getSecond();
					//
					Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> stub = stubCallsFor(
							objectInstanceToMock, mockingContext, danglingObjectWithContextIterator);

					executionFlowGraph.include(stub.getFirst());
					dataDependencyGraph.include(stub.getSecond());

				}

				logger.trace("After Recovery: \n " + prettyPrint(executionFlowGraph));

				/*
				 * Remove all the objects which are not used
				 */
				dataDependencyGraph.purge();

				/*
				 * Double check that we did not introduce dangling objects
				 */
				if (!dataDependencyGraph.verifyObjectInstanceProvenance()) {
					Set<ObjectInstance> danglingObjects = dataDependencyGraph.getDanglingObjects();
					/*
					 * Remove objects not used in the carved test
					 */
					for (Iterator<ObjectInstance> objectInstanceIterator = danglingObjects
							.iterator(); objectInstanceIterator.hasNext();) {
						// Why I cannot put this inside the for statement?
						ObjectInstance objectInstance = objectInstanceIterator.next();
						/*
						 * Check if this objectInstance is ever used by any method in the carved test
						 */
						boolean used = false;
						for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
							if (objectInstance.equals(methodInvocation.getOwner())
									|| methodInvocation.getActualParameterInstances().contains(objectInstance)) {
								used = true;
								break;
							}
						}
						if (!used) {
							objectInstanceIterator.remove();
						}
					}

					if (danglingObjects.size() > 0) {
						logger.warn("The following objects appear out of the blue: " + danglingObjects);
						throw new CarvingException("Objects appear out of the blue");
					}
				}
			}
		}
		/*
		 * At this point we might need to do some more post processing depending on the
		 * target of carving:
		 */

		return carvedTest;
	}

	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveTheIntent(ObjectInstance intentToCarve,
			MethodInvocation context,
			//
			Set<Object> workList,
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects)
			throws CarvingException {

		/*
		 * Output dependencies of this owner before elaborating them
		 */
		List<MethodInvocation> potentiallyStateSettingMethods = new ArrayList<>();
		/*
		 * Collect all the method invocations on that object. No need for alias
		 */
		potentiallyStateSettingMethods.addAll(findPotentiallyInternalStateChangingMethodsFor(intentToCarve, context));

		Collections.sort(potentiallyStateSettingMethods);
		Collections.reverse(potentiallyStateSettingMethods);

		for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {
			logger.trace("\t Intent " + intentToCarve + " depends on method " + methodInvocationToCarve);
		}

		if (alreadyCarvedObjects.containsKey(new Pair<MethodInvocation, ObjectInstance>(context, intentToCarve))) {
			logger.trace("Returning Carving results for " + intentToCarve + " from cache");
			return alreadyCarvedObjects.get(new Pair<MethodInvocation, ObjectInstance>(context, intentToCarve));
		}

		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();

		/**
		 * Find the methods which "possibly" contribute to setting the state of this
		 * object and carve them, with the catch that the Context parameter of the
		 * constructor shall not be carved. I.e., only the reference should be there,
		 * since there's no need for the full state of that object.
		 */

		boolean followsActivities = false;

		for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {

			if (!workList.contains(new Pair<MethodInvocation, MethodInvocation>(context, methodInvocationToCarve))) {

				workList.add(new Pair<MethodInvocation, MethodInvocation>(context, methodInvocationToCarve));

				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResultFromCarveMethod = null;

				if (methodInvocationToCarve.isConstructor()) {
					// Shallow carving: include the call, the return value, and
					// the parameters as they are
					ExecutionFlowGraph shallowExecutionFlowGraph = new ExecutionFlowGraphImpl();
					DataDependencyGraph shallowDataDependencyGraph = new DataDependencyGraphImpl();

					carvingResultFromCarveMethod = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
							shallowExecutionFlowGraph, shallowDataDependencyGraph,
							// TODO
							new CallGraphImpl());

					executionFlowGraph.enqueueMethodInvocations(methodInvocationToCarve);
					dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocationToCarve);

					/*
					 * Include the return value of the carved call if any.
					 */
					if (!JimpleUtils.hasVoidReturnType(methodInvocationToCarve.getMethodSignature())) {
						dataDependencyGraph.addDataDependencyOnReturn(methodInvocationToCarve,
								methodInvocationToCarve.getReturnValue());
					}

					/*
					 * Include the ownership
					 */
					dataDependencyGraph.addDataDependencyOnOwner(methodInvocationToCarve,
							methodInvocationToCarve.getOwner());

					/*
					 * Include the parameters
					 */
					for (DataNode actualParameterToCarve : methodInvocationToCarve.getActualParameterInstances()) {
						dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocationToCarve,
								actualParameterToCarve, //
								methodInvocationToCarve.getActualParameterInstances().indexOf(actualParameterToCarve));
					}

				} else {
					// Do regular carving here... in the hope this does not link
					// other activities...
					carvingResultFromCarveMethod = carveMethod(methodInvocationToCarve, workList, alreadyCarvedMethods,
							alreadyCarvedObjects, followsActivities);
				}

				executionFlowGraph.include(carvingResultFromCarveMethod.getFirst());
				dataDependencyGraph.include(carvingResultFromCarveMethod.getSecond());
			}
		}

		// Add the result of carving into the cache before returning it
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResult = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph,
				// TODO
				new CallGraphImpl());

		alreadyCarvedObjects.put(new Pair<MethodInvocation, ObjectInstance>(context, intentToCarve), carvingResult);

		return carvingResult;
	}

	/*
	 * Go back from location to begin collects potential state changing method
	 * calls, i.e., calls to external libs like database, files, and networks (list
	 * is given), which do NOT belong to left over states
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvePotentiallyExternalStateChangingMethods(
			MethodInvocation methodInvocationToCarve, ActivityGraph activityGraph,
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) throws CarvingException {

		// Get ALL the method invocations before
		List<MethodInvocation> potentiallyExternalStateChangingMethods = this.executionFlowGraph
				.getOrderedMethodInvocationsBefore(methodInvocationToCarve);
		// Keep only the method invocations which belong to "External"
		// interfaces: java.io.File, java.io.Files, etc.
		// TODO we start with files only and add as we go...
		MethodInvocationMatcher.keepMatchingInPlace(
				MethodInvocationMatcher.or(MethodInvocationMatcher.byPackage("java.io"),
						MethodInvocationMatcher.byPackage("java.nio")),
				// TODO Add more ...
				potentiallyExternalStateChangingMethods);

		// Remove those method invocations which belongs to left-over
		// Get left-over of activities before location
		Set<MethodInvocation> subsumingMethodInvocations = new HashSet<>();
		for (Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> activityBefore : activityGraph
				.getActivitiesBefore(location)) {
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> leftOver = activityGraph
					.getLeftOverFor(activityBefore);
			if (leftOver != null) {
				subsumingMethodInvocations.addAll(getAllLeftOverMethodsFrom(leftOver));
			}
		}
		// Remove all the method invocations subsumed by left-over methods.
		// NOTE THAT THIS IS INCOMPLETE SINCE LIFECYCLE EVENTS PROPAGATE TO
		// FRAGMENTS !!
		//
		for (MethodInvocation methodInvocation : subsumingMethodInvocations) {
			MethodInvocationMatcher.filterByInPlace(
					MethodInvocationMatcher.isSubsumedBy(methodInvocation, this.callGraph),
					potentiallyExternalStateChangingMethods);
		}

		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedResult = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph,
				// TODO
				new CallGraphImpl());

		boolean followActivities = false;

		Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods = new HashMap<>();
		Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects = new HashMap<>();
		Set<Object> workList = new HashSet<>();

		for (MethodInvocation methodInvocation : potentiallyExternalStateChangingMethods) {
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> partial = carveMethod(methodInvocation,
					workList, alreadyCarvedMethods, alreadyCarvedObjects, followActivities);
			executionFlowGraph.include(partial.getFirst());
			dataDependencyGraph.include(partial.getSecond());
		}

		return carvedResult;
	}

	private Set<MethodInvocation> getAllLeftOverMethodsFrom(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> leftOver) {
		// The all the methods before second, including second
		Set<MethodInvocation> result = this.executionFlowGraph
				.getMethodInvocationsBefore(leftOver.getSecond().getSecond());
		result.add(leftOver.getSecond().getSecond());
		// Remove all the methods before first to obtain all the methods in
		// between
		MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.before(leftOver.getSecond().getFirst()),
				result);
		// Keep only the methods which belong to the activity
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byInstance(leftOver.getFirst()), result);
		// Keep only valid lifecycle events
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.isActivityLifeCycle(), result);
		// Simplify by keeping only root/user-level methods
		MethodInvocationMatcher.isSubsumedByAnotherAndroidActivityMethod(this.callGraph);
		//
		return result;
	}

	private String prettyPrintActivityWithContext(
			Collection<Pair<ObjectInstance, MethodInvocation>> activitiesWithContext) {
		StringBuffer testContent = new StringBuffer();
		for (Pair<ObjectInstance, MethodInvocation> activityWithContext : activitiesWithContext) {
			testContent.append(activityWithContext.getFirst() + " at " + activityWithContext.getSecond());
		}
		return testContent.toString();
	}

	private Set<MethodInvocation> getSameMethodInvocationFromAliases(
			MethodInvocation activityLifecycleMethodInvocation) {
		Set<MethodInvocation> result = new HashSet<>();
		if (activityLifecycleMethodInvocation.isStatic())
			return result;

		Set<MethodInvocation> subsumedMethodInvocations = this.callGraph
				.getMethodInvocationsSubsumedBy(activityLifecycleMethodInvocation);
		Set<MethodInvocation> subsumingMethodInvocations = new HashSet(
				this.callGraph.getOrderedSubsumingMethodInvocationsFor(activityLifecycleMethodInvocation));

		Set<ObjectInstance> ownerAndAliases = this.dataDependencyGraph
				.getAliasesOf(activityLifecycleMethodInvocation.getOwner());
		ownerAndAliases.add(activityLifecycleMethodInvocation.getOwner());

		for (ObjectInstance alias : ownerAndAliases) {
			// Get all the methods of the alias. We cannot use before/after
			// because we do not if this call takes place before or after the
			// activityLifecycleMethodInvocation
			// Include all the methods of the alias which are subsumed AND have
			// the same name of this method
			result.addAll(
					MethodInvocationMatcher
							.getMethodInvocationsMatchedBy(
									MethodInvocationMatcher.and(MethodInvocationMatcher.byOwner(alias),
											MethodInvocationMatcher.withSameMethodName(JimpleUtils.getMethodName(
													activityLifecycleMethodInvocation.getMethodSignature()))),
									subsumedMethodInvocations));
			// Include all the methods by alias which subsumed this method AND
			// have the same method name
			result.addAll(
					MethodInvocationMatcher
							.getMethodInvocationsMatchedBy(
									MethodInvocationMatcher.and(MethodInvocationMatcher.byOwner(alias),
											MethodInvocationMatcher.withSameMethodName(JimpleUtils.getMethodName(
													activityLifecycleMethodInvocation.getMethodSignature()))),
									subsumingMethodInvocations));
		}
		return result;
	}

	/**
	 * Inside a carved test if an activity is started after another, the activities
	 * before it should have completed their lifecycle otherwise we are in a
	 * inconsistent state (two activities which are in visible state).
	 * 
	 * THIS IS UNDER THE ASSUMPTION THE TEST HANDLES ONE SINGLE ACTIVITY TYPE !
	 * 
	 * This boils down to ensure that onDestroy of the previous activity is there if
	 * the second activity is already initialize.
	 * 
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @return
	 */
	private boolean isLifeCycleOfSameActivityTypeComplete(ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		List<MethodInvocation> androidActivitiesConstructors = new ArrayList<>();
		for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
			if (objectInstance.isAndroidActivity()) {
				Optional<MethodInvocation> constructor = dataDependencyGraph.getConstructorOf(objectInstance);
				if (constructor.isPresent()) {
					// Here we do not check that all the objects are initialized
					// before use..
					androidActivitiesConstructors.add(constructor.get());
				}
			}
		}

		if (androidActivitiesConstructors.size() == 1) {
			// No need to check anything here
			return true;
		}

		/*
		 * At this point the first element is the last invoked and we go backwards to
		 * check if in between each two constructors there's a destroy
		 */
		Collections.sort(androidActivitiesConstructors);
		Collections.reverse(androidActivitiesConstructors);

		final AtomicBoolean missingOnDestroy = new AtomicBoolean(true);
		tupleIterator(androidActivitiesConstructors, (latter, former) -> {
			Set<MethodInvocation> beforeLatterConstructor = executionFlowGraph.getMethodInvocationsBefore(latter);
			Set<MethodInvocation> afterFormerConstructor = executionFlowGraph.getMethodInvocationsAfter(former);
			// Compute the intersection
			Set<MethodInvocation> methodsInBetweenTheConstructors = new HashSet<>(afterFormerConstructor);
			afterFormerConstructor.retainAll(beforeLatterConstructor);
			// Check if the intersection contains onDestroy of the former
			for (MethodInvocation methodInvocation : methodsInBetweenTheConstructors) {
				if (former.getOwner().equals(methodInvocation.getOwner())
						&& "onDestroy".equals(JimpleUtils.getMethodName(methodInvocation.getMethodSignature()))) {
					missingOnDestroy.set(false);
					// To where this will return, will this stop the iteration?
					break;
				}
			}

			// If we already found out that the method is missing we
			// short-circuit and return
			if (missingOnDestroy.get()) {
				return;
			}
		});

		return missingOnDestroy.get();
	}

	// Taken from here:
	// https://stackoverflow.com/questions/16033711/java-iterating-over-every-two-elements-in-a-list
	public static <T> void tupleIterator(Iterable<T> iterable, BiConsumer<T, T> consumer) {
		Iterator<T> it = iterable.iterator();
		if (!it.hasNext())
			return;
		T first = it.next();

		while (it.hasNext()) {
			T next = it.next();
			consumer.accept(first, next);
			first = next;
		}
	}

	private ActivityGraph buildActivityGraph(ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) throws CarvingException {
		ActivityGraph activityGraph = new ActivityGraph(this.callGraph, this.executionFlowGraph,
				this.dataDependencyGraph);

		List<MethodInvocation> allActivityMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();
		// Keep only the methods owned by android activities
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.ownedByAnAndroidActivity(),
				allActivityMethodInvocations);
		/*
		 * Remove the methods subsumed by a different activity. This removes
		 * super/subclasses activities
		 */
		MethodInvocationMatcher.filterByInPlace(
				MethodInvocationMatcher.isSubsumedByAnotherAndroidActivityMethod(this.callGraph),
				allActivityMethodInvocations);
		/*
		 * Collect in order the appearing of all the activities, and call the
		 * activityGraph
		 */
		for (MethodInvocation methodInvocation : allActivityMethodInvocations) {
			ObjectInstance objectInstance = methodInvocation.getOwner();
			if (objectInstance.isAndroidActivity()) {
				activityGraph.add(objectInstance, methodInvocation);
			}
		}

		return activityGraph;
	}

	/*
	 * List all the activities that were executed in this carved execution
	 */
	private List<Pair<ObjectInstance, MethodInvocation>> getActivitiesWithContext(ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {

		List<Pair<ObjectInstance, MethodInvocation>> androidActivitiesWithContext = new ArrayList<>();

		List<Pair<ObjectInstance, MethodInvocation>> androidActivities = new ArrayList<>();

		//
		List<MethodInvocation> allActivityMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();

		// Keep only the methods owned by android activities
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.ownedByAnAndroidActivity(),
				allActivityMethodInvocations);

		/*
		 * Remove the methods subsumed by a different activity. This removes
		 * super/subclasses activities
		 */
		MethodInvocationMatcher.filterByInPlace(
				MethodInvocationMatcher.isSubsumedByAnotherAndroidActivityMethod(this.callGraph),
				allActivityMethodInvocations);

		/*
		 * Collect in order the appearing of all the activities. This contains for each
		 * activity the relative method invocation. We do not need a precise lifecycle
		 * analysis here because we just need to narrow down a context. For this we
		 * assume that no startActivity method are invoked during onPause/onDestroy or
		 * such ...
		 */
		for (MethodInvocation methodInvocation : allActivityMethodInvocations) {
			ObjectInstance objectInstance = methodInvocation.getOwner();
			if (objectInstance.isAndroidActivity()) {
				androidActivities.add(new Pair<ObjectInstance, MethodInvocation>(objectInstance, methodInvocation));
			}
		}
		/*
		 * Simplify the list, i.e., consecutive repetitions of the same instance are
		 * "merged" together by keeping ONLY the latest method invocation
		 */
		Pair<ObjectInstance, MethodInvocation> lastActivityWithContext = null;
		// androidActivitiesWithContext
		for (Iterator<Pair<ObjectInstance, MethodInvocation>> activityPairIterator = androidActivities
				.iterator(); activityPairIterator.hasNext();) {
			Pair<ObjectInstance, MethodInvocation> currentActivity = activityPairIterator.next();

			if (lastActivityWithContext == null) {
				lastActivityWithContext = currentActivity;
				continue;
			}

			if (currentActivity.getFirst().equals(lastActivityWithContext.getFirst())) {
				/*
				 * Same instance, update the lastActivityWithContext
				 */
				lastActivityWithContext = currentActivity;
			} else {
				/*
				 * Different instances. lastActivityWithContext is the last method observed on
				 * the "previous" activity so we store in into the result
				 */
				androidActivitiesWithContext.add(lastActivityWithContext);
				/*
				 * Update the lastActivityWithContext
				 */
				lastActivityWithContext = currentActivity;
			}
		}

		if (lastActivityWithContext == null) {
			// Not sure why this might happen, but just in case
			return androidActivitiesWithContext;
		}

		// At the end we store the last element if not there yet. This should
		// always be the case...
		if (!androidActivitiesWithContext.contains(lastActivityWithContext)) {
			androidActivitiesWithContext.add(lastActivityWithContext);

		}
		return androidActivitiesWithContext;
	}

	/**
	 * Ensures that the id of the method invocation is negative so the mockery
	 * executes before carved test code, or refactor carved test to include separate
	 * code sections !
	 * 
	 * Rules for mockery:
	 * <ul>
	 * <li>primitive boxed: directly instantiated them, i.e., no mocks</li>
	 * <li>complex objects:
	 * <ul>
	 * <li>If object belongs to user app, hence we can observe all its method calls,
	 * no problem. Collect usages in the context, i.e., after the context method
	 * returns we do not care.</li>
	 * <li>If the object does not belong to user app, but it is not passed as
	 * parameter to lib methods in the context, thenReturnManyResultMethod we can
	 * observe all its activity and we can mockMethod it</li>
	 * <li>If the object does not belong to user app AND is also passed around to
	 * lib methods, thenReturnManyResultMethod we cannot observe what happens to it
	 * and we cannot ensure this is safe. We raise an exception.</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param objectInstanceToMock
	 * @param context:             this is the method under test
	 * @return
	 * @throws CarvingException
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> stubCallsFor(
			ObjectInstance objectInstanceToMock, MethodInvocation context, //
			Iterator<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContextIterator)
			throws CarvingException {

		logger.debug("stubCallsFor() for " + objectInstanceToMock + " in the context of " + context);
		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> mockery = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph,
				// TODO
				new CallGraphImpl());

		if (objectInstanceToMock.isBoxedPrimitive()) {
			instantiateBoxedPrimitive(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);
		} else if (JimpleUtils.isArray(objectInstanceToMock.getType())) {
			if (!isTheObjectUsedInContext(objectInstanceToMock, context)) {
				// Instantiate an empty array
				createArrayDouble(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);
			} else {
				logger.warn("We cannot mock Arrays");
				throw new CarvingException("We cannot mock arrays " + objectInstanceToMock);
			}
		} else {
			if (!isTheObjectUsedInContext(objectInstanceToMock, context)) {
				/*
				 * If the object is passed as argument to method under test, but never user
				 * thenReturnManyResultMethod it is enough to pass an empty mockMethod for it.
				 * This should not happen, but "we never know"
				 */
				createMockeryFor(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);

			} else if (belongsToUserApp(objectInstanceToMock)
					|| isCompletelyVisibleInContext(objectInstanceToMock, context)) {
				/*
				 * Instantiate the mockery for objectInstanceToMock
				 */
				createMockeryFor(objectInstanceToMock, executionFlowGraph, dataDependencyGraph);
				/*
				 * Collect the invocations to the object in the context of the method under test
				 * and configure the mockMethod to replicate them... This might require to
				 * mockMethod additional objects, the information should be in the carved test
				 * already "before" the summarization. Since this might require the mocking (in
				 * the future carving) of additional objects as return type, we include the
				 * iterator
				 */
				configureMockeryFor(objectInstanceToMock, context, executionFlowGraph, dataDependencyGraph,
						danglingObjectWithContextIterator);

			} else {
				/*
				 * We cannot guarantee this is safe, so for the momemnt we raise an exception.
				 * Later we can try to do a best effort as see if more valid tests can be
				 * generated
				 */

				// https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric
				// https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric#using-shadows
				// http://robolectric.org/extending/
				throw new CarvingException("Cannot ensure mockery on " + objectInstanceToMock + " in the context "
						+ context + " is safe.");
			}
		}
		return mockery;
	}

	// TODO Find a way to define (negative) indices for mocks !
	AtomicInteger arrayInitId = new AtomicInteger(-1);

	private void createArrayDouble(ObjectInstance arrayToInstantiate, //
			ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph) {
		/*
		 * Create a special node here to host the value of ".class"
		 */
		DataNode arraySize = DataNodeFactory.get("int", "0");

		// TODO: We cannot use a constant id for method calls as those are
		// supposed to be unique.
		// One "naive solution" is to start from a very big negative number and
		// increment it, but do not use MIN_INTEGER !
		MethodInvocation arrayInit = new MethodInvocation(arrayInitId.getAndDecrement(),
				"<" + arrayToInstantiate.getType() + ": void <init>(int)>");
		arrayInit.setOwner(arrayToInstantiate);
		arrayInit.setActualParameterInstances(Arrays.asList(new DataNode[] { arraySize }));

		// Should definitively add something to enqueu stuff in front or at
		// position.
		// Somehin like a PatchingChain
		executionFlowGraph.enqueueMethodInvocations(arrayInit);
		//
		dataDependencyGraph.addMethodInvocationWithoutAnyDependency(arrayInit);
		dataDependencyGraph.addDataDependencyOnOwner(arrayInit, arrayToInstantiate);
		dataDependencyGraph.addDataDependencyOnActualParameter(arrayInit, arraySize, 0);

	}

	private void configureMockeryFor(ObjectInstance objectInstanceToMock, MethodInvocation context, //
			ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph, //
			Iterator<Pair<ObjectInstance, MethodInvocation>> danglingObjectWithContextIterator) {

		// Collect all the calls in the context which have objectInstanceToMock
		// has owner
		// TODO include aliases
		Set<MethodInvocation> callsOnOnbjectInstanceToMockInContext = new HashSet<>();
		// Starts with all the calls in the context
		callsOnOnbjectInstanceToMockInContext.addAll(this.callGraph.getMethodInvocationsSubsumedBy(context));
		// Filter the call by owner
		MethodInvocationMatcher filterByOwner = MethodInvocationMatcher.byOwnerAndAliases(objectInstanceToMock,
				this.dataDependencyGraph.getAliasesOf(objectInstanceToMock));
		callsOnOnbjectInstanceToMockInContext = MethodInvocationMatcher.getMethodInvocationsMatchedBy(filterByOwner,
				callsOnOnbjectInstanceToMockInContext);

		// Create mock/spy
		// https://stackoverflow.com/questions/11620103/mockitoClass-trying-to-spy-on-method-is-calling-the-original-method
		// https://static.javadoc.io/org.mockito/mockitoClass-core/1.10.19/org/mockitoClass/Mockito.html

		/*
		 * We sort the method invocation to ensure parameters are returned as expected,
		 * however, if we use parameter matching this might not be necessary...
		 */
		List<MethodInvocation> sortedMethodCallsOnOnbjectInstanceToMockInContext = new ArrayList<>(
				callsOnOnbjectInstanceToMockInContext);
		Collections.sort(sortedMethodCallsOnOnbjectInstanceToMockInContext);

		/*
		 * At this point we assume instead of objectInstanceToMock we can use its
		 * mockMethod
		 * 
		 * We need to setup a "whenMethod" expectation for each different method, and a
		 * then expectation for each method call. There's no need to put the in place
		 * (thenReturnManyResultMethod(many values)).
		 * 
		 * For the whenMethod we can start by using Mockito.anyXXX so we do not fall in
		 * the trap of having different calls to whenMethod for the same method and
		 * different combinations of parameter matchers (those are DECLARATIVE and
		 * override one another, it's tricky !)
		 * 
		 * The difficult part is to provide the correct ReturnValue. Return values must
		 * be carved or mocked as well, if we cannot carve them, then we need to ensure
		 * that they are COMPLETELY visible.
		 * 
		 * 
		 */
		int index = -2 * sortedMethodCallsOnOnbjectInstanceToMockInContext.size();
		//
		for (MethodInvocation methodInvocationToMock : sortedMethodCallsOnOnbjectInstanceToMockInContext) {

			logger.info("Mocking " + methodInvocationToMock);

			if (JimpleUtils.hasVoidReturnType(methodInvocationToMock.getMethodSignature())) {
				/*
				 * TODO We skip this for the moment, it might be useful for validation, tho
				 */
				logger.info("TODO We do not mockMethod void calls");
				continue;
			}

			// Ensures that we do not create more than one call to this
			if (this.dataDependencyGraph.getAliasesOf(methodInvocationToMock.getOwner())
					.contains(objectInstanceToMock)) {
				methodInvocationToMock.setOwner(objectInstanceToMock);
			}

			ObjectInstance ongoingStubbing = generateOrGetWhenCallForMock(methodInvocationToMock, index,
					executionFlowGraph, dataDependencyGraph);

			// Generate the "Then" part of the mockMethod
			// thenReturnManyResultMethod(T value) Sets a return value to be
			// returned
			// whenMethod
			// the method is called.
			// > Maybe later: thenReturnManyResultMethod(T value, T... values)
			// Sets
			// consecutive return values to be returned whenMethod the method is
			// called.
			// > When we add exceptions: thenThrow(Class<? extends
			// Throwable> throwableType) Sets a Throwable type to be thrown
			// whenMethod the method is called.
			index++;

			// TODO Not sure how to handle this now, most likely we need to
			// recarve everything ?!!!
			// carveObjectInstance(objectInstanceToCarve, context, isOwner,
			// workList, alreadyCarvedMethods, alreadyCarvedObjects)
			// TODO For the moment we carve primitive types which is easy
			DataNode mockedReturnValue = NullNodeFactory
					.get(JimpleUtils.getReturnType(methodInvocationToMock.getMethodSignature()));

			if (methodInvocationToMock.getReturnValue() instanceof ValueNode) {
				mockedReturnValue = methodInvocationToMock.getReturnValue();
			} else if (JimpleUtils.isBoxedPrimitive(methodInvocationToMock.getMethodSignature())) {
				mockedReturnValue = methodInvocationToMock.getReturnValue();
			}

			MethodInvocation thenMethodInvocation = new MethodInvocation(index,
					thenReturnManyResultMethod.getSignature());
			thenMethodInvocation.setOwner(ongoingStubbing);
			thenMethodInvocation.setActualParameterInstances(Arrays.asList(new DataNode[] { mockedReturnValue }));
			// This method returns its owner, it has fluent interface
			thenMethodInvocation.setReturnValue(ongoingStubbing);

			executionFlowGraph.enqueueMethodInvocations(thenMethodInvocation);

			dataDependencyGraph.addMethodInvocationWithoutAnyDependency(thenMethodInvocation);
			//
			dataDependencyGraph.addDataDependencyOnOwner(thenMethodInvocation, ongoingStubbing);
			dataDependencyGraph.addDataDependencyOnActualParameter(thenMethodInvocation, mockedReturnValue, 0);
			dataDependencyGraph.addDataDependencyOnReturn(thenMethodInvocation, ongoingStubbing);

		}
	}

	// Cache
	// Mock + MethodSignature -> OngoingStubbing
	Map<Pair<ObjectInstance, String>, ObjectInstance> alreadyMockedCallsForMock = new HashMap();

	/**
	 * Return the right OngoingStubbing object. I.e. do not create more than one
	 * WHEN call per method. Generate the "When" part of the mockMethod. Ensure that
	 * no aliases are used at this point for the mocked object.
	 * 
	 * To implement whenMethod we use the "eq()" argument matcher for the instances
	 * we can refer to otherwise we use the isAnyXXXX() argument matcher.
	 * 
	 * If we use isAny() we can chain together multiple
	 * "thenReturnManyResultMethod()" stubs and we might simplify the thing.
	 * Otherwise we can simply create many whenMethod-then calls.
	 * 
	 * Actually we might follow the suggestion by:
	 * https://stackoverflow.com/questions/31512245/calling-mockitoClass-whenMethod
	 * -multiple-times-on-same-object?rq=1 and use the doReturn( returnValue
	 * ).when(mockMethod).mockedMethod( arguments );
	 * 
	 * 
	 * 
	 * @param methodInvocationToMock
	 * @param executionFlowGraph
	 * @param dataDependencyGraph
	 * @return
	 */
	private ObjectInstance generateOrGetWhenCallForMock(MethodInvocation methodInvocationToMock, int index,
			ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph) {

		/*
		 * Check in the cache if we already declared a whenMethod method for these
		 * method (not method invocation) and mockMethod
		 */
		String methodSubSignature = JimpleUtils.getSubSignature(methodInvocationToMock.getMethodSignature());
		ObjectInstance mockObject = methodInvocationToMock.getOwner();
		//
		Pair<ObjectInstance, String> mockingTask = new Pair<ObjectInstance, String>(mockObject, methodSubSignature);

		if (alreadyMockedCallsForMock.containsKey(mockingTask)) {
			logger.debug("We already declare a WHEN mocking call for : " + mockingTask);
			return alreadyMockedCallsForMock.get(mockingTask);
		}

		DataNode methodCall = PrimitiveNodeFactory.createMethodCallLiteralValue(methodInvocationToMock);
		// org.mockito.stubbing.OngoingStubbing - since this is
		// generated... I have no idea how to assign numbers/id to it...
		// t
		// I hope
		// NOTE HERE we do not use DataNodeFactory
		ObjectInstance ongoingStubbing = ObjectInstanceFactory
				.get(OngoingStubbing.class.getName() + "@" + generatedId.incrementAndGet());

		MethodInvocation whenMethodInvocation = new MethodInvocation(index, whenMethod.getSignature());
		whenMethodInvocation.setStatic(true);
		whenMethodInvocation.setActualParameterInstances(Arrays.asList(new DataNode[] { methodCall }));
		whenMethodInvocation.setReturnValue(ongoingStubbing);

		executionFlowGraph.enqueueMethodInvocations(whenMethodInvocation);

		dataDependencyGraph.addMethodInvocationWithoutAnyDependency(whenMethodInvocation);
		//
		dataDependencyGraph.addDataDependencyOnActualParameter(whenMethodInvocation, methodCall, 0);
		dataDependencyGraph.addDataDependencyOnReturn(whenMethodInvocation, ongoingStubbing);

		alreadyMockedCallsForMock.put(mockingTask, ongoingStubbing);

		return ongoingStubbing;
	}

	/*
	 * Boxed Primitives We generate a call which initialize this object instance
	 * using the provided string value.
	 */
	private void instantiateBoxedPrimitive(ObjectInstance objectInstanceToMock, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		SootClass sootClass = Scene.v().getSootClass(objectInstanceToMock.getType());
		SootMethod valueOfFromString = sootClass.getMethod("valueOf",
				Arrays.asList(new Type[] { RefType.v(String.class.getName()) }));

		MethodInvocation boxedPrimitiveInstantiation = new MethodInvocation(-100, valueOfFromString.getSignature());
		boxedPrimitiveInstantiation.setStatic(true);

		// Strings are managed by value !
		// TODO I hope this is never null ...
		DataNode stringValue = DataNodeFactory.get(String.class.getName(),
				Arrays.toString(objectInstanceToMock.getStringValue().getBytes()));

		boxedPrimitiveInstantiation.setActualParameterInstances(Arrays.asList(new DataNode[] { stringValue }));
		boxedPrimitiveInstantiation.setReturnValue(objectInstanceToMock);
		// Add this to the execution flow graph
		executionFlowGraph.enqueueMethodInvocations(boxedPrimitiveInstantiation);
		//
		dataDependencyGraph.addMethodInvocationWithoutAnyDependency(boxedPrimitiveInstantiation);
		dataDependencyGraph.addDataDependencyOnActualParameter(boxedPrimitiveInstantiation, stringValue, 0);
		dataDependencyGraph.addDataDependencyOnReturn(boxedPrimitiveInstantiation, objectInstanceToMock);

	}

	private static final AtomicInteger generatedId = new AtomicInteger(1);
	private static final AtomicInteger mockedCallsId = new AtomicInteger(-100);

	private void createMockeryFor(ObjectInstance objectInstanceToMock, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {

		/*
		 * Create a special node here to host the value of ".class"
		 */
		DataNode classToMock = PrimitiveNodeFactory.createClassLiteralFor(objectInstanceToMock);

		/*
		 * The mockery gets the class of the instance to mockMethod, and generate the
		 * expected instance. Ensures this is called before anything else !
		 */
		MethodInvocation mockeryCreation = new MethodInvocation(mockedCallsId.incrementAndGet(),
				mockMethod.getSignature());
		mockeryCreation.setStatic(true);

		mockeryCreation.setActualParameterInstances(Arrays.asList(new DataNode[] { classToMock }));
		mockeryCreation.setReturnValue(objectInstanceToMock);

		//
		executionFlowGraph.enqueueMethodInvocations(mockeryCreation);

		dataDependencyGraph.addMethodInvocationWithoutAnyDependency(mockeryCreation);
		dataDependencyGraph.addDataDependencyOnActualParameter(mockeryCreation, classToMock, 0);
		dataDependencyGraph.addDataDependencyOnReturn(mockeryCreation, objectInstanceToMock);

	}

	private boolean isCompletelyVisibleInContext(ObjectInstance objectInstanceToMock, MethodInvocation context) {
		// Those are all the *visible* method calls made inside context
		// Include the aliases in the check
		Set<ObjectInstance> objectInstanceToMockAndItsAliases = dataDependencyGraph.getAliasesOf(objectInstanceToMock);
		objectInstanceToMockAndItsAliases.add(objectInstanceToMock);

		for (MethodInvocation visibleMethodInvocation : callGraph.getMethodInvocationsSubsumedBy(context)) {
			// We need this because set operations change the set !
			Set<DataNode> copyOfObjectInstanceAndItsAliases = new HashSet<>(objectInstanceToMockAndItsAliases);
			// Is the object or any of the aliases used as parameter ? Do the
			// intersection and see what's left
			copyOfObjectInstanceAndItsAliases.retainAll(visibleMethodInvocation.getActualParameterInstances());
			if (!copyOfObjectInstanceAndItsAliases.isEmpty() && visibleMethodInvocation.isLibraryCall()) {
				/*
				 * if this object is used a parameter to call a lib (non visible) method,
				 * thenReturnManyResultMethod all its object usages might not be completely
				 * visible
				 */
				return false;
			}
		}
		return true;
	}

	private boolean isTheObjectUsedInContext(ObjectInstance objectInstanceToMock, MethodInvocation context) {
		// Those are all the *visible* method calls made inside context
		// We should consider also the aliases here...
		Set<ObjectInstance> objectInstanceToMockAndItsAliases = dataDependencyGraph.getAliasesOf(objectInstanceToMock);
		objectInstanceToMockAndItsAliases.add(objectInstanceToMock);

		for (MethodInvocation visibleMethodInvocation : callGraph.getMethodInvocationsSubsumedBy(context)) {
			if (objectInstanceToMockAndItsAliases.contains(visibleMethodInvocation.getOwner())) {
				return true;
			}
			// We need this because set operations change the set !
			Set<DataNode> copyOfObjectInstanceAndItsAliases = new HashSet<>(objectInstanceToMockAndItsAliases);
			// Is the object or any of the aliases used as parameter ? Do the
			// intersection and see what's left
			copyOfObjectInstanceAndItsAliases.retainAll(visibleMethodInvocation.getActualParameterInstances());
			if (!copyOfObjectInstanceAndItsAliases.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * An object belongs to user app if the method invocations on him are NOT tagged
	 * as libCalls ! NOTE if there's no method invocations, there's no need to
	 * mockMethod behavior, just proi
	 */
	private boolean belongsToUserApp(ObjectInstance objectInstance) {
		// This looks everywhere... but get only one method to make the
		// decision.
		// TODO THIS IS AN HEURISTIC, WE SHOULD GO FOR ADDING A FLAS INSISDE
		// OBJECT INSTANCE INSTEAD, BUT THAT REQUIRES HAVING THOSE INFORMATION
		// DURING TRACING/PARSING/ETC...
		Set<MethodInvocation> methodInvocationsOnObjectInstanceAndOnItsAliases = new HashSet<>(
				dataDependencyGraph.getMethodInvocationsForOwner(objectInstance));
		for (ObjectInstance alias : dataDependencyGraph.getAliasesOf(objectInstance)) {
			methodInvocationsOnObjectInstanceAndOnItsAliases
					.addAll(dataDependencyGraph.getMethodInvocationsForOwner(alias));
		}
		// If there's at least a call to a non libCall we return true...
		for (MethodInvocation methodInvocation : methodInvocationsOnObjectInstanceAndOnItsAliases) {
			if (!methodInvocation.isLibraryCall()) {
				return true;
			}
		}
		return false;
	}

	private String prettyPrint(ExecutionFlowGraph executionFlowGraph) {
		StringBuffer testContent = new StringBuffer();

		int position = 1;

		for (MethodInvocation mi : executionFlowGraph.getOrderedMethodInvocations()) {
			testContent.append(((mi.isSynthetic()) ? "ABC" : "") + "\t" + +position + "\t" + mi.getInvocationCount()
					+ " " + mi.getOwner() + "::" + mi.getMethodSignature() + "(");
			for (DataNode actualParameterInstance : mi.getActualParameterInstances()) {
				testContent.append(prettyPrint(actualParameterInstance));
				if (mi.getActualParameterInstances()
						.indexOf(actualParameterInstance) != mi.getActualParameterInstances().size() - 1) {
					testContent.append(",");
				}
			}
			testContent.append(")" + "->" + prettyPrint(mi.getReturnValue()));
			testContent.append("\n");
			position++;
		}
		return testContent.toString();
	}

	private String prettyPrint(DataNode actualParameter) {
		if (actualParameter == null) {
			return "null";
		} else if (actualParameter instanceof NullInstance) {
			return "null";
		} else {
			// else if (actualParameter instanceof PrimitiveValue
			// && JimpleUtils.isStringContent(((PrimitiveValue)
			// actualParameter).getStringValue())) {
			// return actualParameter.toString();
			//
			//
			// String stringContent = ((PrimitiveValue)
			// actualParameter).getStringValue();
			// stringContent = stringContent.replace("[", "").replace("]", "");
			// if (stringContent.isEmpty()) {
			// return "";
			// } else {
			// String[] bytesAsString = stringContent.split(",");
			// byte[] bytes = new byte[bytesAsString.length];
			// for (int ii = 0; ii < bytes.length; ii++) {
			// bytes[ii] = new Byte(bytesAsString[ii].trim());
			// }
			// return '"' + new String(bytes) + '"';
			// }
			// } else {
			return actualParameter.toString();
		}
	}

	/**
	 * Carving a method requires: Carving its owner at the state it was before the
	 * invocation, and carving its paramters. If the methods does not have formally
	 * an owner (static | constructor) we skip the first step.
	 * 
	 * @param methodInvocationToCarve
	 * @param carvedTests
	 * @param alreadyCarvedMethods
	 * @param alreadyCarvedObjects
	 * @return
	 * @throws CarvingException
	 */
	@Deprecated
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveMethod(
			MethodInvocation methodInvocationToCarve, Set<Object> workList,
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects)
			throws CarvingException {
		// Flag to decide if we want to consider previous activities in the
		// carve... This will likely leave the place for something else...
		boolean followActivities = true;
		return carveMethod(methodInvocationToCarve, workList, alreadyCarvedMethods, alreadyCarvedObjects,
				followActivities);
	}

	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveMethod(
			MethodInvocation methodInvocationToCarve, Set<Object> workList,
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects, //
			boolean followActivities) throws CarvingException {

		logger.trace("> Carve Method " + methodInvocationToCarve.toString());

		if (alreadyCarvedMethods.containsKey(
				new Pair<MethodInvocation, MethodInvocation>(methodInvocationToCarve, methodInvocationToCarve))) {
			logger.trace("Returning Carving results for " + methodInvocationToCarve + " from cache");
			return alreadyCarvedMethods.get(
					new Pair<MethodInvocation, MethodInvocation>(methodInvocationToCarve, methodInvocationToCarve));
		}

		/*
		 * Write out method dependencies before elaborating them!
		 */

		/*
		 * A method had data dependencies and possibly method dependencies. Like
		 * android-lifecycle events
		 */

		if (!(methodInvocationToCarve.isStatic() || methodInvocationToCarve.isConstructor())) {
			/*
			 * Method ownership
			 */
			logger.trace("\t Method " + methodInvocationToCarve.toString() + " depends on owner "
					+ methodInvocationToCarve.getOwner());
		}

		for (int position = 0; position < methodInvocationToCarve.getActualParameterInstances().size(); position++) {
			DataNode actualParameterToCarve = methodInvocationToCarve.getActualParameterInstances().get(position);
			/*
			 * Method parameter
			 */
			logger.trace("\t Method " + methodInvocationToCarve.toString() + " depends on parameter[" + position + "] "
					+ actualParameterToCarve);
		}

		MethodInvocation previousActivitySyncMethod = null;

		/**
		 * We need to include the method invocations from other activities, however, we
		 * should do so ONLY for lifecycle of USER-provided activities, and not the
		 * super types they implement....
		 * 
		 * Not that there are cases that, despite there's other activities before this
		 * one, methodInvocationToCarve does not depend on them. For example, A1.onPause
		 * depends solely on A1, if A1 put itself on pause (via startAnother Activity)
		 */
		if (doesMethodInvocationRequirePreviousActivity(methodInvocationToCarve) && //
				followActivities) {
			ObjectInstance currentActivity = methodInvocationToCarve.getOwner();
			logger.debug("\t Method" + methodInvocationToCarve + " is an Activity CallBack for " + currentActivity);
			/*
			 * Activity Dependency. We need to include the activity BEFORE this one. The
			 * previous one ONLY since this will recursively bring others if any, but
			 * simplify the computation of the current context.
			 */
			List<MethodInvocation> otherActivitiesBefore = this.executionFlowGraph
					.getOrderedMethodInvocationsBefore(methodInvocationToCarve);
			// Reverse the list
			Collections.reverse(otherActivitiesBefore);
			/*
			 * Look for the first activity which belongs to another activity. Start by
			 * removing from this list the calls to methods which belong to this activity
			 * and method which are not lifecycle callbacks
			 */
			MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.or(
					// Filter invocations to THIS activity
					MethodInvocationMatcher.byOwner(currentActivity),
					// Filter invocations to non-lifecyle calls
					MethodInvocationMatcher.not(MethodInvocationMatcher.isActivityLifeCycle())), otherActivitiesBefore);
			// At this point only activity callbacks are there. Take the first,
			// if any
			if (otherActivitiesBefore.isEmpty()) {
				logger.trace("\t There's no Activity dependencies for " + methodInvocationToCarve);
			} else {
				ObjectInstance previousActivity = otherActivitiesBefore.get(0).getOwner();
				/**
				 * The order of life-cycle events is not guaranteed, the only guarantee is given
				 * by "sync" points. TODO Note that this might return the invocation of a method
				 * of the super class...
				 * 
				 */
				previousActivitySyncMethod = getPreviousActivitySyncMethod(methodInvocationToCarve, previousActivity);

				logger.trace("\t Method " + methodInvocationToCarve.toString() + " depends on previous activity "
						+ previousActivity + " lifecycle method " + previousActivitySyncMethod);
			}
		}

		// TODO I am not sure I can freely share nodes/references among those
		// graphs, somehow they return copies and iterators ...
		// The problems are the "dependenncy informations"
		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResult = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph,
				// TODO
				new CallGraphImpl());

		/*
		 * Add the plain methodInvocationToCarve.
		 */
		executionFlowGraph.enqueueMethodInvocations(methodInvocationToCarve);
		dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocationToCarve);

		/*
		 * Include the return value of the carved call if any. TODO Somehow if this is
		 * null everything breaks silently ...
		 */
		if (!JimpleUtils.hasVoidReturnType(methodInvocationToCarve.getMethodSignature())) {
			dataDependencyGraph.addDataDependencyOnReturn(methodInvocationToCarve,
					methodInvocationToCarve.getReturnValue());
		}

		if (!(methodInvocationToCarve.isStatic() || methodInvocationToCarve.isConstructor())) {

			// Check in the worklist if we have already scheduled this job,
			// otherwise skip it
			if (!workList.contains(new Pair<MethodInvocation, ObjectInstance>(methodInvocationToCarve,
					methodInvocationToCarve.getOwner()))) {

				// Add to the work list and execute it
				workList.add(new Pair<MethodInvocation, ObjectInstance>(methodInvocationToCarve,
						methodInvocationToCarve.getOwner()));

				/**
				 * Do the carving on the data
				 */
				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> partialCarvingResult = carveMethodOwner(
						methodInvocationToCarve.getOwner(), methodInvocationToCarve, //
						workList, //
						alreadyCarvedMethods, alreadyCarvedObjects);

				if (!methodInvocationToCarve.getOwner()
						.equals(partialCarvingResult.getSecond().getOwnerFor(methodInvocationToCarve))) {
					throw new RuntimeException("Failed to register ownership for " + methodInvocationToCarve + " with "
							+ methodInvocationToCarve.getOwner());
				}

				/*
				 * Include the required actions to get to this object instance. This will
				 * restructure the graphs
				 */
				executionFlowGraph.include(partialCarvingResult.getFirst());
				dataDependencyGraph.include(partialCarvingResult.getSecond());

			}
		} else if (methodInvocationToCarve.isConstructor()) {
			/*
			 * Include the dependencies between the methodInvocationToCarve and its owner
			 */
			dataDependencyGraph.addDataDependencyOnOwner(methodInvocationToCarve, methodInvocationToCarve.getOwner());
		}

		for (DataNode actualParameterToCarve : methodInvocationToCarve.getActualParameterInstances()) {
			// Check in the worklist if we have already scheduled this job,
			// otherwise skip it
			if (!workList
					.contains(new Pair<MethodInvocation, DataNode>(methodInvocationToCarve, actualParameterToCarve))) {

				// Add to the work list and execute it
				workList.add(new Pair<MethodInvocation, DataNode>(methodInvocationToCarve, actualParameterToCarve));

				/**
				 * Do the carving on the data
				 */
				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> partialCarvingResult = carveMethodParameter(
						actualParameterToCarve, methodInvocationToCarve, //
						workList, alreadyCarvedMethods, alreadyCarvedObjects);
				/*
				 * Include the required actions to get to this object instance
				 */
				executionFlowGraph.include(partialCarvingResult.getFirst());
				dataDependencyGraph.include(partialCarvingResult.getSecond());
				/*
				 * Include the dependencies between the methodInvocationToCarve and its
				 * parameter in the given position
				 */
				dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocationToCarve, actualParameterToCarve, //
						methodInvocationToCarve.getActualParameterInstances().indexOf(actualParameterToCarve));
			}
		}

		/*
		 * Schedule carving of the previous activity by carving it's last method !
		 */
		if (previousActivitySyncMethod != null) {

			if (!workList.contains(new Pair<MethodInvocation, MethodInvocation>(previousActivitySyncMethod,
					previousActivitySyncMethod))) {

				workList.add(new Pair<MethodInvocation, MethodInvocation>(previousActivitySyncMethod,
						previousActivitySyncMethod));

				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResultFromPreviousActivitySyncMethod = carveMethod(
						previousActivitySyncMethod, workList, alreadyCarvedMethods, alreadyCarvedObjects);

				executionFlowGraph.include(carvingResultFromPreviousActivitySyncMethod.getFirst());
				dataDependencyGraph.include(carvingResultFromPreviousActivitySyncMethod.getSecond());
			}

			//
			//
			//
			// if (!workList.contains(new Pair<MethodInvocation,
			// DataNode>(context, previousActivity))) {
			//
			// // Add to the work list and execute it
			// workList.add(new Pair<MethodInvocation, DataNode>(context,
			// previousActivity));
			//
			// /**
			// * Do the carving on the previousActivity.
			// */
			// Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>
			// partialCarvingResult = carvePreviousActivity(
			// previousActivity, context, //
			// workList, alreadyCarvedMethods, alreadyCarvedObjects);
			// /*
			// * Include the required actions to get to the previous activity
			// * in the right state. Note that the data dependency graphs
			// * might be disjoint as the two activities communicate only
			// * indirectly
			// */
			// executionFlowGraph.include(partialCarvingResult.getFirst());
			// dataDependencyGraph.include(partialCarvingResult.getSecond());
			//
			// }
		}

		/*
		 * TODO: At this point we need to be sure we called all the methods to external
		 * libraries which might have changed the external state of the app
		 */

		// Is this necessary for POJO CLASSES ? MAYBE WE CAN FILTER THIS OUT FOR
		// ACTIVITY AND FRAGMENTS AS WELL ?
		// for(MethodInvocation methodInvocation :
		// findPotentiallyExternalStateChangingMethodsFor(methodInvocationToCarve)){
		// if (!workList
		// .contains(new Pair<MethodInvocation,
		// MethodInvocation>(methodInvocationToCarve, methodInvocation))) {
		//
		// workList.add(new Pair<MethodInvocation,
		// MethodInvocation>(methodInvocationToCarve, methodInvocation));
		//
		// Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>
		// partialCarvingResult =
		// carveMethod(methodInvocation, workList, alreadyCarvedMethods,
		// alreadyCarvedObjects);
		//
		// /*
		// * Include the required actions to include external calls
		// */
		// executionFlowGraph.include(partialCarvingResult.getFirst());
		// dataDependencyGraph.include(partialCarvingResult.getSecond());
		// }
		// }

		/*
		 * Add the result of carving into the cache before returning it
		 */
		alreadyCarvedMethods.put(
				new Pair<MethodInvocation, MethodInvocation>(methodInvocationToCarve, methodInvocationToCarve),
				carvingResult);

		return carvingResult;
	}

	private final static Set<String> selfDistructingMethods;
	static {
		selfDistructingMethods = new HashSet<>();
		selfDistructingMethods.add("startActivityForResult");
		selfDistructingMethods.add("startActivity");
		selfDistructingMethods.add("finish");
		// List is not complete...
	}

	private boolean doesMethodInvocationRequirePreviousActivity(MethodInvocation methodInvocationToCarve) {
		/*
		 * Check corner cases: A1.onPause depends solely on A1, if A1 put itself on
		 * pause (via startAnother Activity)
		 */
		if (methodInvocationToCarve.isAndroidActivityCallback() &&
		//
				isAnUserActivityType(JimpleUtils.getClassNameForMethod(methodInvocationToCarve.getMethodSignature())) &&
				// Some event lifecycle comes always after others (of the same
				// activity)
				initialActivityCallbacks
						.contains(JimpleUtils.getMethodName(methodInvocationToCarve.getMethodSignature()))) {

			/*
			 * Check if the activity put itself to sleep or it destroys itself. In those
			 * cases, the activity does not really dependents on previous activities if the
			 * last method called before this one from the same activity subsumes: for
			 * example, startActivity, startActivityForResult cause the currect activity to
			 * go in pause, <android.app.Activity: void finish()> calls the onDestroy method
			 * which is common if the activity was started by another activity, and it has
			 * finished its job (after setting the Result object); on pause
			 */
			List<MethodInvocation> allMethodsBeforeSameOwner = this.executionFlowGraph
					.getOrderedMethodInvocationsBefore(methodInvocationToCarve);
			// Ensure we do not include: methodInvocationToCarve
			allMethodsBeforeSameOwner.remove(methodInvocationToCarve);

			// Filter by owner
			MethodInvocationMatcher.keepMatchingInPlace(
					MethodInvocationMatcher.byOwner(methodInvocationToCarve.getOwner()), allMethodsBeforeSameOwner);
			// Filter by Formal Type to rule out super/subclasses
			MethodInvocationMatcher.keepMatchingInPlace(
					MethodInvocationMatcher
							.byType(JimpleUtils.getClassNameForMethod(methodInvocationToCarve.getMethodSignature())),
					allMethodsBeforeSameOwner);

			if (allMethodsBeforeSameOwner.isEmpty()) {
				// Not sure, but if there's nothing here, it might be the case
				// this activity depends on another activity
				return true;
			}

			/*
			 * At this point we should have the ordered list of method on this owner for
			 * this class The last one is the one we should look at: TODO Unsate for size ==
			 * 0
			 */
			MethodInvocation lastByOwner = allMethodsBeforeSameOwner.get(allMethodsBeforeSameOwner.size() - 1);

			// Check if lastByOwner subsume the startActivity* methods
			for (MethodInvocation subsumed : this.callGraph.getMethodInvocationsSubsumedBy(lastByOwner)) {
				// TODO Can be made more robust by checking the subsignature:
				// <android.support.v4.app.FragmentActivity: void
				// startActivityForResult(android.content.Intent,int)>
				// Or by including soot objects in the data structure
				if (selfDistructingMethods.contains(JimpleUtils.getMethodName(subsumed.getMethodSignature()))) {
					logger.debug(lastByOwner + " is subsumed by the self-destructing method " + subsumed);
					// Since the activity puts itseld on pause we do not require
					// other activities
					return false;
				}
			}

			return true;

		}
		return false;
	}

	private Set<String> allUserActivityTypesObservedInTheTrace = null;

	private boolean isAnUserActivityType(String type) {
		if (allUserActivityTypesObservedInTheTrace == null) {
			// Build it once for all the carvable tests
			allUserActivityTypesObservedInTheTrace = new HashSet<>();
			allUserActivityTypesObservedInTheTrace.addAll(getUserActivityTypes());
			logger.warn("Recreating allUserActivityTypesObservedInTheTrace Set : "
					+ allUserActivityTypesObservedInTheTrace);

		}
		return allUserActivityTypesObservedInTheTrace.contains(type);
	}

	private Set<String> getUserActivityTypes() {
		/*
		 * Get all the types of all the user activity in the parsed trace. Those are
		 * instances whose methods are not subsumed by other activities methods
		 */
		Set<String> activityTypes = new HashSet<>();
		for (ObjectInstance objectInstance : this.dataDependencyGraph.getObjectInstances()) {
			if (objectInstance.isAndroidActivity()) {
				activityTypes.add(objectInstance.getType());
			}
		}
		return activityTypes;
	}

	/**
	 * Returns the lifecycle method of previous activity which must be ensured to
	 * run BEFORE methodInvocationToCarve. We need to compute this because many
	 * methods of THIS activity might depend on the same LAST method of the previous
	 * activity.
	 * 
	 * 
	 * First lookup for the "INITIAL" callback of THIS activity in this context:
	 * onCreate, onPause, onRestart, onResume, onDestroy Then lookup to the
	 * corresponding "SYNCHRONIZATION" call back of the PREVIOUS activity, that
	 * would be the context.
	 * 
	 * 
	 * @param methodInvocationToCarve
	 * @param previousActivity
	 * @return
	 */
	public final static Set<String> initialActivityCallbacks = new HashSet<>();
	public final static Map<String, Set<String>> syncActivityCallbacks = new HashMap<>();
	// Mappings do not consider SINGLE activities, that is SAME INSTANCE of the
	// SAME ACTIVITY
	static {
		initialActivityCallbacks
				.addAll(Arrays.asList(new String[] { "onCreate", "onPause", "onRestart", "onDestroy" }));
		// onActivityResult

		syncActivityCallbacks.put("onCreate",
				new HashSet<>(Arrays.asList(new String[] { "onResume", "onSavedInstanceState", "onDestroy" })));
		syncActivityCallbacks.put("onPause",
				new HashSet<>(Arrays.asList(new String[] { "onSavedInstanceState", "onDestroy" })));
		syncActivityCallbacks.put("onRestart", new HashSet<>(Arrays.asList(new String[] { "onResume" })));
		syncActivityCallbacks.put("onDestroy", new HashSet<>(Arrays.asList(new String[] { "onResume" })));
	}

	private MethodInvocation getPreviousActivitySyncMethod(MethodInvocation methodInvocationToCarve,
			ObjectInstance previousActivity) {

		List<MethodInvocation> thisActivityMethodInvocations = this.executionFlowGraph
				.getOrderedMethodInvocationsBefore(methodInvocationToCarve);
		// Ensure this includes the methodInvocationToCarve as well
		if (!thisActivityMethodInvocations.contains(methodInvocationToCarve)) {
			thisActivityMethodInvocations.add(methodInvocationToCarve);
		}
		// Reverse the order so the first in the last is the last method called,
		// i.e., methodInvocationToCarve
		Collections.reverse(thisActivityMethodInvocations);
		// Filter the ordered method invocations: keep only the ones which
		// belong to this activity and are lifecycle callbacks
		MethodInvocationMatcher.keepMatchingInPlace(
				MethodInvocationMatcher.and(MethodInvocationMatcher.byOwner(methodInvocationToCarve.getOwner()),
						MethodInvocationMatcher.isActivityLifeCycle()),
				thisActivityMethodInvocations);

		// Filter the resulting method invocations by keeping only the
		// initialActivityCallbacks
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.methodNameIsOneOf(initialActivityCallbacks),
				thisActivityMethodInvocations);

		assert !thisActivityMethodInvocations.isEmpty() : "Cannot find an initial activity callback for "
				+ methodInvocationToCarve.getOwner();

		// At this point, the first element in the ordered list is the last
		// initial call back of the activity
		MethodInvocation initialCallBackOfThisActivity = thisActivityMethodInvocations.get(0);

		// logger.debug("LAST INITIAL CALL BACK FOR " +
		// methodInvocationToCarve.getOwner() + " is "
		// + initialCallBackOfThisActivity);

		// Leverage the predefined mappings for looking for the corresponding
		// SYNC method invocations of the previous activity

		List<MethodInvocation> previousActivityMethodInvocations = this.executionFlowGraph
				.getOrderedMethodInvocationsBefore(initialCallBackOfThisActivity);
		// Reverse the order so the first in the last is the last method called,
		// i.e., initialCallBackOfThisActivity
		Collections.reverse(previousActivityMethodInvocations);
		// Filter the ordered method invocations: keep only the ones which
		// belong to the previousActivity and are lifecycle callbacks
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher
				.and(MethodInvocationMatcher.byOwner(previousActivity), MethodInvocationMatcher.isActivityLifeCycle()),
				previousActivityMethodInvocations);
		// At this point filter by corresponding synch activity events to the
		// (methodName) of initialCallBackOfThisActivity
		MethodInvocationMatcher.keepMatchingInPlace(
				MethodInvocationMatcher.methodNameIsOneOf(syncActivityCallbacks
						.get(JimpleUtils.getMethodName(initialCallBackOfThisActivity.getMethodSignature()))),
				previousActivityMethodInvocations);

		/*
		 * We need to rule out few cases. If activity1 starts activity2 and expect
		 * results, we observe activity1 onPause, but this on pause does not depend on
		 * any previous activity !
		 */

		assert !previousActivityMethodInvocations
				.isEmpty() : "Cannot find an synch activity callback for previous activity: " + previousActivity
						+ " while carving " + methodInvocationToCarve + " of this activity: "
						+ methodInvocationToCarve.getOwner();

		// The first element in the list corresponds to the last in time
		MethodInvocation syncCallBackOfPreviousActivity = previousActivityMethodInvocations.get(0);

		return syncCallBackOfPreviousActivity;
	}

	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvePreviousActivity(
			ObjectInstance previousActivity, MethodInvocation context, Set<Object> workList,
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects)
			throws CarvingException {

		logger.trace(" > Carve carvePreviousActivity " + previousActivity + " in context " + context);

		boolean isOwner = false;
		return carveObjectInstance(previousActivity, context, isOwner, workList, alreadyCarvedMethods,
				alreadyCarvedObjects);
	}

	/**
	 * A method owner is always an object and it is always not-null, otherwise the
	 * trace would have logged an exception... TODO right ?
	 * 
	 * @param owner
	 * @param alreadyCarvedMethods
	 * @param alreadyCarvedObjects
	 * @return
	 * @throws CarvingException
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveMethodOwner(ObjectInstance owner,
			MethodInvocation context, //
			Set<Object> workList, //
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects)
			throws CarvingException {
		logger.trace(" > Carve Object " + owner + " in context " + context);
		boolean isOwner = true;
		return carveObjectInstance(owner, context, isOwner, //
				workList, //
				alreadyCarvedMethods, alreadyCarvedObjects);
	}

	/**
	 * If the actual parameter is a primitive type or a String or a null object
	 * there's nothing to carve out of them.
	 * 
	 * TODO Shall we return the actual Parameter as data dependency as well ? I
	 * suspect we might need to include it somehow...
	 * 
	 * @param actualParameter
	 * @param alreadyCarvedMethods
	 * @param alreadyCarvedObjects
	 * @return
	 * @throws CarvingException
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveMethodParameter(DataNode actualParameter,
			MethodInvocation context, //
			Set<Object> workList, //
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects)
			throws CarvingException {

		logger.trace(" > Carve MethodParameter " + actualParameter + " in context " + context);

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResult = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				new ExecutionFlowGraphImpl(), new DataDependencyGraphImpl(), new CallGraphImpl());

		if (actualParameter instanceof ObjectInstance) {
			// Will this be the case to have a NullInstance representing a
			// non-string object which is null ?
			if (actualParameter instanceof NullInstance) {
				return carvingResult;
			} else {
				boolean isOwner = false;
				return carveObjectInstance((ObjectInstance) actualParameter, context, isOwner, workList,
						alreadyCarvedMethods, alreadyCarvedObjects);
			}

		} else {
			return carvingResult;
		}
	}

	@Deprecated
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveObjectInstance(
			ObjectInstance objectInstanceToCarve, MethodInvocation context, // We need this to track down where the
																			// objectInstanceToCarve was needed
			boolean isOwner, //
			Set<Object> workList, //
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects)
			throws CarvingException {

		boolean followsActivities = true;
		return carveObjectInstance(objectInstanceToCarve, context, // We need
																	// this to
																	// track down
																	// where the
				// objectInstanceToCarve was needed
				isOwner, //
				workList, //
				alreadyCarvedMethods, alreadyCarvedObjects, //
				followsActivities);

	}

	/**
	 * 
	 * @param objectInstanceToCarve
	 * @param context
	 * @param isOwner
	 * @param workList
	 * @param alreadyCarvedMethods
	 * @param alreadyCarvedObjects
	 * @return
	 * @throws CarvingException
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carveObjectInstance(
			ObjectInstance objectInstanceToCarve, MethodInvocation context, // We need this to track down where the
																			// objectInstanceToCarve was needed
			boolean isOwner, //
			Set<Object> workList, //
			Map<Pair<MethodInvocation, MethodInvocation>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedMethods,
			Map<Pair<MethodInvocation, ObjectInstance>, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> alreadyCarvedObjects, //
			boolean followsActivities) throws CarvingException {

		/*
		 * Output dependencies of this owner before elaborating them
		 */
		List<MethodInvocation> potentiallyStateSettingMethods = new ArrayList<>();
		/*
		 * Collect all the method invocations on that object.
		 */
		potentiallyStateSettingMethods
				.addAll(findPotentiallyInternalStateChangingMethodsFor(objectInstanceToCarve, context));

		// TODO Do we always include the provider of the aliases ?
		/*
		 * Include the methods on that alias -> Honestly I have no idea how is this
		 * possible... maybe the system id is tricky ?!
		 */
		for (ObjectInstance alias : this.dataDependencyGraph.getAliasesOf(objectInstanceToCarve)) {
			potentiallyStateSettingMethods.addAll(findPotentiallyInternalStateChangingMethodsFor(alias, context));
		}

		Collections.sort(potentiallyStateSettingMethods);
		Collections.reverse(potentiallyStateSettingMethods);

		for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {
			logger.trace("\t Object " + objectInstanceToCarve + " depends on method " + methodInvocationToCarve);
		}

		// Should this be contextual ?!
		if (alreadyCarvedObjects
				.containsKey(new Pair<MethodInvocation, ObjectInstance>(context, objectInstanceToCarve))) {
			logger.trace("Returning Carving results for " + objectInstanceToCarve + " from cache");
			return alreadyCarvedObjects.get(new Pair<MethodInvocation, ObjectInstance>(context, objectInstanceToCarve));
		}

		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		DataDependencyGraph dataDependencyGraph = new DataDependencyGraphImpl();

		/**
		 * Find the methods which "possibly" contribute to setting the state of this
		 * object and carve them. Those are methods that we called
		 * <strong>before</strong> the context:
		 * <ul>
		 * <li>on that object</li>
		 * <li>on aliases of that object</li>
		 * <li>In case the object or aliases are not "ours", all the methods that do not
		 * belong to the app code, which we receive that object as parameter</li>
		 * </ul>
		 * 
		 * TODO: NOTE: Direct access to object fields (because they are public) will not
		 * be considered in this analysis !!!
		 */

		for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {

			if (!workList.contains(new Pair<MethodInvocation, MethodInvocation>(context, methodInvocationToCarve))) {

				workList.add(new Pair<MethodInvocation, MethodInvocation>(context, methodInvocationToCarve));

				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResultFromCarveMethod = carveMethod(
						methodInvocationToCarve, workList, alreadyCarvedMethods, alreadyCarvedObjects,
						followsActivities);

				executionFlowGraph.include(carvingResultFromCarveMethod.getFirst());
				dataDependencyGraph.include(carvingResultFromCarveMethod.getSecond());
			}
		}

		if (isOwner) {
			dataDependencyGraph.addDataDependencyOnOwner(context, objectInstanceToCarve);
		}
		// Add here ownership information

		// Add the result of carving into the cache before returning it
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvingResult = new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
				executionFlowGraph, dataDependencyGraph,
				// TODO
				new CallGraphImpl());

		alreadyCarvedObjects.put(new Pair<MethodInvocation, ObjectInstance>(context, objectInstanceToCarve),
				carvingResult);

		return carvingResult;
	}

	/**
	 * List all the method calls which might influence the state of the given object
	 * directly or indirectly:
	 * <ul>
	 * <li>Methods OWNED by the object (direct)</li>
	 * <li>Methods which PASS the object as parameter to other methods (both library
	 * or user provided)</li>
	 * </ul>
	 * 
	 * 
	 * A special attention should be given to method which provides the instance in
	 * the first place, either those being constructors, factory methods,
	 * singletons, etc. We use an heuristic: if the constructor is private, hence
	 * not visible in the trace, we look for those methods which return the object
	 * as return value. We sort those methods as follow: - static methods in the
	 * same class -> singleton pattern - static methods in the same package (?) ->
	 * factory pattern - exception -> at this point not sure we can track this
	 * object somewhere... Maybe it was a parameter from the outside ?!
	 * 
	 * Note that we also rely on Aliasing relations to find possible methods which
	 * are related to this object ...
	 * 
	 * @param objectInstanceToCarve
	 * @return
	 * @throws CarvingException
	 */
	private Collection<MethodInvocation> findPotentiallyInternalStateChangingMethodsFor(
			ObjectInstance objectInstanceToCarve, MethodInvocation context) throws CarvingException {
		/*
		 * Get the methods which are invoked on the objectInstance BEFORE the use of the
		 * instance
		 */
		Set<MethodInvocation> potentiallyStateChangingMethods = new HashSet<>();

		// Take all the invocations before the method to carve
		List<MethodInvocation> methodInvocationsBefore = executionFlowGraph.getOrderedMethodInvocationsBefore(context);

		// if (objectInstanceToCarve.getType().contains("Intent")) {
		// System.out.println("AndroidMethodCarver.findPotentiallyInternalStateChangingMethodsFor():::");
		// System.out.println(prettyPrint(methodInvocationsBefore));
		// }

		MethodInvocation constructor = dataDependencyGraph.getConstructorOf(objectInstanceToCarve).orElse(null);

		// Take all the calls in this set which were invoked on the method owner
		// - TODO What's the difference with byInstance ?
		MethodInvocationMatcher ownership = MethodInvocationMatcher.byOwner(objectInstanceToCarve);
		potentiallyStateChangingMethods
				.addAll(MethodInvocationMatcher.getMethodInvocationsMatchedBy(ownership, methodInvocationsBefore));

		// Take all the calls in which this object was used but which do not
		// belong to user classes (i.e., library calls). Do not limit to
		// libCalls
		MethodInvocationMatcher asParameter = MethodInvocationMatcher.asParameter(objectInstanceToCarve);
		potentiallyStateChangingMethods
				.addAll(MethodInvocationMatcher.getMethodInvocationsMatchedBy(asParameter, methodInvocationsBefore));

		boolean methodFound = potentiallyStateChangingMethods.contains(constructor);

		// Check for methods in the same class which return this object
		if (!methodFound) {
			Set<MethodInvocation> methodInvocationsReturningTheObject = dataDependencyGraph
					.getMethodInvocationsWhichReturn(objectInstanceToCarve);
			/*
			 * Among the methods which return this object, is there anything which is static
			 * and belong to the same class? Maybe some builder method..
			 */
			Set<MethodInvocation> singletonPattern = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
					/*
					 * XXX MethodInvocationMatcher.byClass(objectInstanceToCarve. getType()), ->
					 * This fails for Arrays...
					 */
					MethodInvocationMatcher.byClassLiteral(objectInstanceToCarve.getType()),
					methodInvocationsReturningTheObject);
			singletonPattern = MethodInvocationMatcher
					.getMethodInvocationsMatchedBy(MethodInvocationMatcher.staticCall(), singletonPattern);
			singletonPattern = MethodInvocationMatcher
					.getMethodInvocationsMatchedBy(MethodInvocationMatcher.before(context), singletonPattern);

			/*
			 * Order them by ID and take the last one. This might be not accurate, one
			 * should run some sort of data-flow analysis...
			 */

			if (!singletonPattern.isEmpty()) {
				List<MethodInvocation> sortedCalls = new ArrayList<>(singletonPattern);
				Collections.sort(sortedCalls);
				methodFound = true;
				potentiallyStateChangingMethods.add(sortedCalls.get(sortedCalls.size() - 1));
			}
		}

		// Check for methods in the same class which return this object
		if (!methodFound) {
			Set<MethodInvocation> methodInvocationsReturningTheObject = dataDependencyGraph
					.getMethodInvocationsWhichReturn(objectInstanceToCarve);
			Set<MethodInvocation> factoryPattern = MethodInvocationMatcher.getMethodInvocationsMatchedBy(
					MethodInvocationMatcher.staticCall(), methodInvocationsReturningTheObject);
			factoryPattern = MethodInvocationMatcher
					.getMethodInvocationsMatchedBy(MethodInvocationMatcher.before(context), factoryPattern);

			/*
			 * Order them by ID and take the last one. This might be not accurate, one
			 * should run some sort of data-flow analysis...
			 */

			if (!factoryPattern.isEmpty()) {
				List<MethodInvocation> sortedCalls = new ArrayList<>(factoryPattern);
				Collections.sort(sortedCalls);
				methodFound = true;
				potentiallyStateChangingMethods.add(sortedCalls.get(sortedCalls.size() - 1));
			}
		}

		// if (!methodFound) {
		// // We do not fail here because there's might be additional
		// // trickeries to get this instance....
		// logger.warn("Provider of object instance " + objectInstanceToCarve
		// + " not found. This will likely fail the test generation");
		// }

		return potentiallyStateChangingMethods;
	}

	// Find the method activities which are in between the context, and belongs
	// to the objects (and are lifecyle event?)
	public Set<MethodInvocation> getLifeCycleEventsFor(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		// TODO Auto-generated method stub
		return null;
	}

}
