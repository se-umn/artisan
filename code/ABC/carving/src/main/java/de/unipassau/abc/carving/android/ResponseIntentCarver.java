package de.unipassau.abc.carving.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;

/**
 * @author gambi
 *
 *         The intent might be also null if the result is set using
 *         'setResult(int)'. In that case, there's nothing to carve
 */
public class ResponseIntentCarver {

	private final Logger logger = LoggerFactory.getLogger(ResponseIntentCarver.class);

	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;
	private CallGraph callGraph;

	public ResponseIntentCarver(ExecutionFlowGraph executionFlowGraph, //
			DataDependencyGraph dataDependencyGraph, //
			CallGraph callGraph) {
		super();
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
		this.callGraph = callGraph;
	}

	public Pair<ExecutionFlowGraph, DataDependencyGraph> carve(ObjectInstance activityUnderTest)
			throws CarvingException {

		if (!activityUnderTest.isAndroidActivity()) {
			throw new CarvingException("Not an Android activity " + activityUnderTest);
		}

		// By definition there's at most one of such method for any activity instance
		MethodInvocation setResult = findSetResultMethod(activityUnderTest);

		if (setResult == null) {
			// This activity does not have a returning Intent/result
			return null;
		}

		if (setResult.getActualParameterInstances().size() > 1) {
			// Intent is there - setResult(int, Intent)
			ObjectInstance returningIntent = (ObjectInstance) setResult.getActualParameterInstances().get(1);
			logger.info(activityUnderTest + " returned intent " + returningIntent + " from " + setResult);
			return shallowCarveOf(returningIntent, setResult);
		} else {
			// Intent is null return an empty
			logger.info(activityUnderTest + " returned no specific intent from " + setResult);
			return new Pair<ExecutionFlowGraph, DataDependencyGraph>(new ExecutionFlowGraphImpl(),
					new DataDependencyGraphImpl());
		}

	}

	private Pair<ExecutionFlowGraph, DataDependencyGraph> shallowCarveOf(ObjectInstance returningIntent,
			MethodInvocation setResultMethod) {
		List<MethodInvocation> potentiallyStateSettingMethods = new ArrayList<>();
		potentiallyStateSettingMethods.addAll(this.dataDependencyGraph.getMethodInvocationsForOwner(returningIntent));
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.before(setResultMethod),
				potentiallyStateSettingMethods);

		Collections.sort(potentiallyStateSettingMethods);

		ExecutionFlowGraph executionFlowGraph = new ExecutionFlowGraphImpl();
		for (MethodInvocation methodInvocationToCarve : potentiallyStateSettingMethods) {
			logger.trace("\t Intent " + returningIntent + " depends on method " + methodInvocationToCarve);
			executionFlowGraph.enqueueMethodInvocations(methodInvocationToCarve);
		}

		DataDependencyGraph dataDependencyGraph = this.dataDependencyGraph.getSubGraph(executionFlowGraph);

		Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = new Pair<ExecutionFlowGraph, DataDependencyGraph>(
				executionFlowGraph, dataDependencyGraph);
		return carvingResult;
	}

	private MethodInvocation findSetResultMethod(ObjectInstance activityUnderTest) throws CarvingException {

		// We cannot directly look for this. We need to find the latest onPause and the
		// last setResult before it
		List<MethodInvocation> activityMethods = new ArrayList<>();
		activityMethods.addAll(this.dataDependencyGraph.getMethodInvocationsForOwner(activityUnderTest));
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("onPause|onResume"),
				activityMethods);
		// Remove calls to super
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byClass(activityUnderTest.getType()),
				activityMethods);
		Collections.sort(activityMethods);

		if (activityMethods.size() < 2) {
			// THIS IS NOT AN ERROR BUT CANNOT HANDLE RIGHT NOW
			throw new CarvingException("TODO Somehow there's no onPause for " + activityUnderTest
					+ " while looking fro a setResult method !");
		}
		MethodInvocation lastOnPause = activityMethods.get(activityMethods.size() - 1);
		if (!lastOnPause.getMethodSignature().contains("onPause")) {
			throw new CarvingException("TODO Somehow there's no onPause for " + activityUnderTest
					+ " while looking fro a setResult method !");
		}
		MethodInvocation lastOnResume = activityMethods.get(activityMethods.size() - 2);
		if (!lastOnResume.getMethodSignature().contains("onResume")) {
			// THis is actually an error !
			throw new CarvingException("onPause for " + activityUnderTest + " does not have a matchin onResume!");
		}

		logger.info("Last onResume/onPause for " + activityMethods + " is " + lastOnResume + " -- " + lastOnPause);

		// Find the last setResult in between onResume and onPause
		List<MethodInvocation> setResultMethods = new ArrayList<>();
		setResultMethods.addAll(this.executionFlowGraph.getMethodInvocationsBefore(lastOnPause));
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.before(lastOnPause), setResultMethods);
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.after(lastOnResume), setResultMethods);
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.byMethodName("setResult"),
				setResultMethods);
		//
		if (setResultMethods.size() > 1) {
			// THis is actually an error !
			throw new CarvingException(
					"Wrong expectation for " + activityUnderTest + " foudn more than 1 setResult " + setResultMethods);
		}

		if (setResultMethods.isEmpty()) {
			logger.info(activityUnderTest + " does not set a return value");
			return null;
		} else {
			logger.info(activityUnderTest + " set a return value with " + setResultMethods.get(0));
		}
		return setResultMethods.get(0);
	}

}
