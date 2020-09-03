package de.unipassau.abc.carving.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.AbstractObjectCarver;
import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.instrumentation.SceneInstrumenterWithMethodParameters;
import de.unipassau.abc.parsing.ParsedTrace;

/**
 * Carve the given intent assuming that intent has been tainted during execution
 * to connect intent used to start an activity from a source and the
 * corresponding intent received by the target activity.
 * 
 * @author gambitemp
 *
 */
public class TaintendIntentCarver extends AbstractObjectCarver {

	private final Logger logger = LoggerFactory.getLogger(TaintendIntentCarver.class);
	private ParsedTrace parsedTrace;

	public static final String INTENT_TYPE = "android.content.Intent";

	public TaintendIntentCarver(ParsedTrace parsedTrace) {
		this.parsedTrace = parsedTrace;
	}

	/*
	 * We need to ensure the status of the object at the point it was used as
	 * parameter for this invocation.
	 */
	@Override
	public List<CarvedExecution> carveAsParameterAfterValidation(final ObjectInstance intentObjectInstance,
			final MethodInvocation contextMethodInvocation) throws CarvingException, ABCException {

		if (!intentObjectInstance.getType().equals(INTENT_TYPE)) {
			logger.warn("Cannot log a non-intent object" + intentObjectInstance);
			return new ArrayList<CarvedExecution>();
		}

		/**
		 * Assumptions of this method: - single thread - the intent have been carved
		 */

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> mainTrace = parsedTrace.getUIThreadParsedTrace();

		// Find intentAliases using taints
		final Set<ObjectInstance> intentAndAliases = new HashSet<ObjectInstance>();
		// Add the intent
		intentAndAliases.add(intentObjectInstance);
		// Add all the aliases
		intentAndAliases.addAll(findIntentAliasesFor(intentObjectInstance));

		Set<MethodInvocation> relevantMethodInvocations = mainTrace.getFirst()
				.getMethodInvocationsBefore(contextMethodInvocation, new Predicate<MethodInvocation>() {

					@Override
					public boolean test(MethodInvocation t) {
						/*
						 * We consider a method as relevant if its execution might have changed the
						 * state of the intent. This includes direct calls to the intend or any of its
						 * aliases, and any libCall that receives the intent as parameter and
						 * potentially modify it. We do not consider app calls that receive the intent
						 * as parameter because we can trace them, and see whether they invoke methods
						 * on the object
						 */
						if (!t.isStatic()) {
							// All the instance methods called the object or any of its aliases
							return intentAndAliases.contains(t.getOwner());
						}
						if (t.isLibraryCall()) {
							// Any method call that used the object or any of its aliases as parameter
							return t.getActualParameterInstances().contains(intentObjectInstance);

							// TODO Why this should matter at all? It might be problematic to use since
							// activity methods and factory methods return the object... and if we include
							// them we include a lot more method invocations than needed?
							// Any method call that returned the object or any of its aliases as return
							// value
//									|| t.getReturnValue().equals(intentObjectInstance);
						}
						return false;
					}
				});

		BasicCarver basicCarver = new BasicCarver(parsedTrace);
		// This will return all possible ways to carve each method invocation, which is
		// potentially is a large cartesian product.

		Map<MethodInvocation, List<CarvedExecution>> carvedExecutions = basicCarver
				.carve(new ArrayList<MethodInvocation>(relevantMethodInvocations));

		/*
		 * Now we pick one carved execution for each of the method call that we carved.
		 * However, it might happen that one method call is already included in the
		 * carved execution of another so we need to merge those results somehow into a
		 * single List of carved executions
		 */

		// For simplicity, we produce a single carved execution as result. Pretty sure
		// there's a better way to do this...
		CarvedExecution result = new CarvedExecution();
		result.methodInvocationUnderTest = contextMethodInvocation;
		result.carvedObject = intentObjectInstance;

		// This might contain duplicates - removing them does not seem trivial at this
		// point...
		List<CarvedExecution> allExecutions = new ArrayList<CarvedExecution>();
		// Collect all the clusters and put them together, then simplify?
		for (Entry<MethodInvocation, List<CarvedExecution>> entry : carvedExecutions.entrySet()) {
			/*
			 * Each carved execution reports a number of connected components that represent
			 * tightly coupled method executions necessary to carve the corresponding method
			 * invocation.
			 */

			// Ideally if the method invocation has been already carved we should not
			// consider it...
			MethodInvocation mi = entry.getKey();
			List<CarvedExecution> ces = entry.getValue();
			if (ces.isEmpty()) {
				logger.error("We were not able to carve " + mi);
			} else {
				// Greedy stragety consider only the first among the possible CarveExecutions
				allExecutions.add(ces.get(0));
			}
		}

		Iterator<CarvedExecution> outerIterator = allExecutions.iterator();
		while (outerIterator.hasNext()) {
			CarvedExecution firstElement = outerIterator.next();

			// Collect all the method carves by the firstElement
			Set<MethodInvocation> allCarvedMethods = new HashSet<MethodInvocation>();
			for (ExecutionFlowGraph executionFlowGraph : firstElement.executionFlowGraphs) {
				allCarvedMethods.addAll(executionFlowGraph.getOrderedMethodInvocations());
			}

			// Check if the other elements are already contained in the first one
			Iterator<CarvedExecution> innerIterator = allExecutions.iterator();
			while (innerIterator.hasNext()) {

				CarvedExecution secondElement = innerIterator.next();

				if (firstElement == secondElement) {
					continue;
				}

				if (allCarvedMethods.contains(secondElement.methodInvocationUnderTest)) {
					innerIterator.remove();
				}

			}

		}

		return allExecutions;

	}

	// Recursive ?
	private Set<ObjectInstance> findIntentAliasesFor(ObjectInstance intentObjectInstance) {

		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> mainTrace = parsedTrace.getUIThreadParsedTrace();

		final DataNode parameter = DataNodeFactory.get("java.lang.String",
				Arrays.toString(SceneInstrumenterWithMethodParameters.INTENT_TAINT_TAG.getBytes()));

		// Find the TAINT of the intentObjectInstance
		final Set<String> taints = mainTrace.getSecond().getMethodInvocationsForOwner(intentObjectInstance).stream()
				.filter(new Predicate<MethodInvocation>() {

					@Override
					public boolean test(MethodInvocation t) {
						return JimpleUtils.getMethodName(t.getMethodSignature()).equals("getIntExtra")
								&& t.getActualParameterInstances().contains(parameter);
					}
				})
				// Extract the return value, which is a string representation of an Integer
				.map(new Function<MethodInvocation, String>() {

					@Override
					public String apply(MethodInvocation t) {
						// TODO Rebuild the original intent refernce by using its type and the stored
						// int
						return INTENT_TYPE + "@" + t.getReturnValue().toString();
					}
				})
				// Collect but avoid duplicates
				.collect(Collectors.toSet());

		// Each taint represent the systemID of an objectInstace so we can look them up
		// by Type and ID

		return mainTrace.getSecond().getObjectInstances().stream().filter(new Predicate<ObjectInstance>() {

			@Override
			public boolean test(ObjectInstance t) {
				if (t.getType().equals(INTENT_TYPE)) {
					logger.info("Object id " + t.getObjectId() + " taints " + taints);
				}
				return t.getType().equals(INTENT_TYPE) && taints.contains(t.getObjectId());
			}

		}).collect(Collectors.toSet());

	}

	@Override
	public List<CarvedExecution> carveAsReturnValueAfterValidation(ObjectInstance intentObjectInstance,
			MethodInvocation contextMethodInvocation) throws CarvingException, ABCException {

		if (!intentObjectInstance.getType().equals(INTENT_TYPE)) {
			logger.warn("Cannot log a non-intent object" + intentObjectInstance);
			return new ArrayList<CarvedExecution>();
		}

		// If we do not automatically include the contenxtMethodInvocation in the carved
		// execution, this is basically the same as the previous one...
		return carveAsParameterAfterValidation(intentObjectInstance, contextMethodInvocation);
	}

}
