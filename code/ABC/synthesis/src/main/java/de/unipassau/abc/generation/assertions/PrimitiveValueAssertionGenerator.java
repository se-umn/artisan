package de.unipassau.abc.generation.assertions;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ExecutionFlowGraphImpl;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.generation.data.CarvedTest;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

/**
 * Generate an assertions that compare the return value of the invocation with
 * the corresponding value stored in the trace. We use Hamcrest matchers for
 * portability
 * 
 * @author gambitemp
 *
 */
public class PrimitiveValueAssertionGenerator implements AssertionGenerator {

//	private final Static String <org.hamcrest.Matcher: boolean matches(java.lang.Object)>
//	<org.hamcrest.Matcher: void describeMismatch(java.lang.Object,org.hamcrest.Description)>
//	<org.hamcrest.Matcher: void _dont_implement_Matcher___instead_extend_BaseMatcher_()>
//	<org.hamcrest.MatcherAssert: void <init>()>
//	<org.hamcrest.MatcherAssert: void assertThat(java.lang.Object,org.hamcrest.Matcher)>
//	<org.hamcrest.MatcherAssert: void assertThat(java.lang.String,java.lang.Object,org.hamcrest.Matcher)>
//	<org.hamcrest.MatcherAssert: void assertThat(java.lang.String,boolean)>
//  <org.hamcrest.Matchers: org.hamcrest.Matcher is(java.lang.Object)>
//	
//	<org.hamcrest.Matchers: org.hamcrest.Matcher is(java.lang.Class)>

	private final static String ASSERT_THAT_SIGNATURE = "<org.hamcrest.MatcherAssert: void assertThat(java.lang.Object,org.hamcrest.Matcher)>";
	private final static String EQUAL_TO_SIGNATURE = "<org.hamcrest.Matchers: org.hamcrest.Matcher equalTo(java.lang.Object)>";
	private final static String IS_SIGNATURE = "<org.hamcrest.Matchers: org.hamcrest.Matcher is(org.hamcrest.Matcher)>";

	// TODO This might be at the level of AssertionGenerator so it can be shared
	// among all the assertions?
	private final static AtomicInteger id = new AtomicInteger(1);

	/**
	 * Generate a statement like MatcherAssert.assertThat(actualValue, is( equalTo( expectedValue ) ));
	 */
	@Override
	public CarvingAssertion generateAssertionsFor(CarvedTest carvedTest, CarvedExecution carvedExecution) {
		CarvingAssertion assertion = new CarvingAssertion();

		// This is the method invocation in the carved test, not the one in the original
		// execution
		// TODO Check this has different ID !!
		MethodInvocation methodInvocationInsideTest = carvedTest.getMethodUnderTest();
		//
		MethodInvocation originalMethodInvocation = carvedExecution.methodInvocationUnderTest;

		if (! JimpleUtils.hasVoidReturnType(originalMethodInvocation.getMethodSignature())) {
			ExecutionFlowGraph assertionExecutionFlowGraph = new ExecutionFlowGraphImpl();
			DataDependencyGraph assertionDataDependencyGraph = new DataDependencyGraphImpl();

			// This is the value recorded from the trace, but we still
			DataNode expectedReturnValue = originalMethodInvocation.getReturnValue();
			// TODO Not sure this is actually there yet..
			DataNode actualReturnValue = methodInvocationInsideTest.getReturnValue();

			// invoke Matchers.equalTo(operand) with operand being the expectedReturnValue
			// TODO This might be easier to wrap into a factory
			MethodInvocation matchersEqualTo = new MethodInvocation(id.getAndIncrement(), EQUAL_TO_SIGNATURE);
			matchersEqualTo.setStatic(true);
			matchersEqualTo.setActualParameterInstances(Arrays.asList(expectedReturnValue));
			DataNode equalsToMatcher = ObjectInstanceFactory.get("org.hamcrest.Matcher@1");
			matchersEqualTo.setReturnValue(equalsToMatcher);

			assertionExecutionFlowGraph.enqueueMethodInvocations(matchersEqualTo);
			
			assertionDataDependencyGraph.addMethodInvocationWithoutAnyDependency(matchersEqualTo);
			assertionDataDependencyGraph.addDataDependencyOnActualParameter(matchersEqualTo, expectedReturnValue, 0);
			assertionDataDependencyGraph.addDataDependencyOnReturn(matchersEqualTo, equalsToMatcher);

			// Invokes Matchers.is using the previous matcher as input
			MethodInvocation matchersIs = new MethodInvocation(id.getAndIncrement(), IS_SIGNATURE);
			matchersIs.setStatic(true);
			matchersIs.setActualParameterInstances(Arrays.asList(equalsToMatcher));
			DataNode isMatcher = DataNodeFactory.get("org.hamcrest.Matcher", "org.hamcrest.Matcher@2");
			matchersIs.setReturnValue(isMatcher);

			assertionExecutionFlowGraph.enqueueMethodInvocations(matchersIs);
			
			assertionDataDependencyGraph.addMethodInvocationWithoutAnyDependency(matchersIs);
			assertionDataDependencyGraph.addDataDependencyOnActualParameter(matchersIs, equalsToMatcher, 0);
			assertionDataDependencyGraph.addDataDependencyOnReturn(matchersIs, isMatcher);
			
			// Invokes MatcherAssert.assertThat
			MethodInvocation assertThat = new MethodInvocation(id.getAndIncrement(), ASSERT_THAT_SIGNATURE);
			assertThat.setStatic(true);
			assertThat.setActualParameterInstances(Arrays.asList(actualReturnValue, isMatcher));

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
