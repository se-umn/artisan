package de.unipassau.abc.generation.assertions;

import java.util.Arrays;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.generation.data.CarvedTest;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

public class NullValueAssertionGenerator implements AssertionGenerator {
	// TODO This might be at the level of AssertionGenerator so it can be shared
	// among all the assertions?
	private final static AtomicInteger id = new AtomicInteger(1);

	// TODO Probably better to move them to a common class about assertion constants
	// or something
	private final static String ASSERT_THAT_SIGNATURE = "<org.hamcrest.MatcherAssert: void assertThat(java.lang.Object,org.hamcrest.Matcher)>";
	private final static String IS_SIGNATURE = "<org.hamcrest.Matchers: org.hamcrest.Matcher is(org.hamcrest.Matcher)>";
	private final static String NULL_VALUE_SIGNATURE = "<org.hamcrest.Matchers: org.hamcrest.Matcher nullValue()>";
	private final static String NOT_NULL_VALUE_SIGNATURE = "<org.hamcrest.Matchers: org.hamcrest.Matcher notNullValue()>";

	@Override
	public CarvingAssertion generateAssertionsFor(CarvedTest carvedTest, CarvedExecution carvedExecution) {
		CarvingAssertion assertion = new CarvingAssertion();
		MethodInvocation originalMethodInvocation = carvedExecution.methodInvocationUnderTest;

		// Generate the assertion only if there's a return value and that's an object
		if (!JimpleUtils.hasVoidReturnType(originalMethodInvocation.getMethodSignature())
				&& originalMethodInvocation.getReturnValue() instanceof ObjectInstance) {

			ExecutionFlowGraph assertionExecutionFlowGraph = new ExecutionFlowGraphImpl();
			DataDependencyGraph assertionDataDependencyGraph = new DataDependencyGraphImpl();

			//
			ObjectInstance expectedReturnValue = (ObjectInstance) originalMethodInvocation.getReturnValue();

			// TODO Must those be unique in absolute sense? This might be tricky ! In that
			// case we need a MatcherFactory to ensure a new matcher is returned eveytime !
			// OTHERWISE Each assertion has its own id set, as there should be only one
			// assertion per TYPE in a test...
			// Not that this must carry the type of the return value (probably can be more safe compute it from the signature instead of hardcoding it!)
			DataNode nullNotNullMatcher = ObjectInstanceFactory.get("org.hamcrest.Matcher@1");
			if (expectedReturnValue.isNull()) {
				// Invokes Matchers.is using the previous matcher as input
				MethodInvocation matchersNullValue = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, id.getAndIncrement(), NULL_VALUE_SIGNATURE);
				matchersNullValue.setStatic(true);
				matchersNullValue.setReturnValue(nullNotNullMatcher);

				assertionExecutionFlowGraph.enqueueMethodInvocations(matchersNullValue);

				assertionDataDependencyGraph.addMethodInvocationWithoutAnyDependency(matchersNullValue);
				assertionDataDependencyGraph.addDataDependencyOnReturn(matchersNullValue, nullNotNullMatcher);

			} else {
				MethodInvocation matchersNotNullValue = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, id.getAndIncrement(),
						NOT_NULL_VALUE_SIGNATURE);
				matchersNotNullValue.setStatic(true);
				matchersNotNullValue.setReturnValue(nullNotNullMatcher);

				assertionExecutionFlowGraph.enqueueMethodInvocations(matchersNotNullValue);

				assertionDataDependencyGraph.addMethodInvocationWithoutAnyDependency(matchersNotNullValue);
				assertionDataDependencyGraph.addDataDependencyOnReturn(matchersNotNullValue, nullNotNullMatcher);
			}

			// Invokes Matchers.is using the previous matcher as input
			MethodInvocation matchersIs = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, id.getAndIncrement(), IS_SIGNATURE);
			matchersIs.setStatic(true);
			matchersIs.setActualParameterInstances(Arrays.asList(nullNotNullMatcher));
			DataNode isMatcher = DataNodeFactory.get("org.hamcrest.Matcher", "org.hamcrest.Matcher@2");
			matchersIs.setReturnValue(isMatcher);

			assertionExecutionFlowGraph.enqueueMethodInvocations(matchersIs);

			assertionDataDependencyGraph.addMethodInvocationWithoutAnyDependency(matchersIs);
			assertionDataDependencyGraph.addDataDependencyOnActualParameter(matchersIs, nullNotNullMatcher, 0);
			assertionDataDependencyGraph.addDataDependencyOnReturn(matchersIs, isMatcher);

			// Invokes MatcherAssert.assertThat
			// TODO This is tricky as we need to make the connection at some point ...
			DataNode actualReturnValue = carvedTest.getMethodUnderTest().getReturnValue();
			MethodInvocation assertThat = new MethodInvocation(MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT, id.getAndIncrement(), ASSERT_THAT_SIGNATURE);
			assertThat.setStatic(true);
			assertThat.setActualParameterInstances(Arrays.asList(actualReturnValue, isMatcher));
//
			assertionExecutionFlowGraph.enqueueMethodInvocations(assertThat);

			assertionDataDependencyGraph.addMethodInvocationWithoutAnyDependency(assertThat);
			assertionDataDependencyGraph.addDataDependencyOnActualParameter(assertThat, actualReturnValue, 0);
			assertionDataDependencyGraph.addDataDependencyOnActualParameter(assertThat, isMatcher, 1);

			// THOSE ARE POSITIONAL
			assertion.executionFlowGraphs.add(assertionExecutionFlowGraph);
			assertion.dataDependencyGraphs.add(assertionDataDependencyGraph);
		}
		return assertion;
	}

}
