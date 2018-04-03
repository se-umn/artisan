package de.unipassau.abc.generation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
import de.unipassau.abc.tracing.XMLDumper;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Body;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.annotation.j5anno.AnnotationGenerator;
import soot.tagkit.Tag;

// https://github.com/Sable/soot/wiki/Adding-profiling-instructions-to-applications
public class MockingGenerator {

	private final static Logger logger = LoggerFactory.getLogger(MockingGenerator.class);

	public static final String SYSTEM_IN_RULE_NAME = "userInputs";
	public static final String SYSTEM_EXIT_RULE_NAME = "expectedSystemExit";

	/**
	 * This introduce a default JUnit SystemRule which mock user inputs and can
	 * provide inputs to java.util.Scanner. It's hard=coded, but it should make
	 * it. Then for the test it carve out the inputs. Note we assume the last
	 * call in the each test method is the invocation of the method under test
	 * 
	 * @param testClass
	 * @param testMethod
	 * @param carvedTest
	 */
	public static void addSystemIn(SootClass testClass,
			// Now we have the problem that carved tests come from different
			// system tests, and at this point we do not have this information
			// easily available. Guess we look each and every parsedTrace to
			// find the right one
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace) {
		SootField systemInJunit4RuleField = null;
		if (testClass.declaresFieldByName(SYSTEM_IN_RULE_NAME)) {
			systemInJunit4RuleField = testClass.getFieldByName(SYSTEM_IN_RULE_NAME);
			return;
		} else {
			systemInJunit4RuleField = new SootField(SYSTEM_IN_RULE_NAME,
					RefType.v("org.junit.contrib.java.lang.system.TextFromStandardInputStream"),
					Modifier.PUBLIC | Modifier.FINAL);
			// Add annotation
			AnnotationGenerator.v().annotate(systemInJunit4RuleField, Rule.class);
			Scene.v().loadClassAndSupport("org.junit.contrib.java.lang.system.TextFromStandardInputStream");
			testClass.addField(systemInJunit4RuleField);

			// Go inside the <init> and initialize the field
			SootMethod testClassInitializer;
			// create a class initializer if one does not already exist.
			// TODO Not sure HOW I CAN GENERATE IN PLACE INITIALIZATION, but
			// this works anyway
			if (testClass.declaresMethodByName("<init>")) {
				testClassInitializer = testClass.getMethodByName("<init>");
			} else {
				testClassInitializer = new SootMethod("<init>", Collections.<Type>emptyList(), VoidType.v(),
						Modifier.PUBLIC);
				// Build the body from the input list of statements
				JimpleBody body = Jimple.v().newBody(testClassInitializer);

				testClassInitializer.setActiveBody(body);
				testClass.addMethod(testClassInitializer);
				//
				body.insertIdentityStmts();

				// Get the super class
				SootClass superClass = Scene.v().loadClassAndSupport("java.lang.Object");
				testClass.setSuperclass(superClass);

				body.getUnits().add(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(body.getThisLocal(),
						Scene.v().getMethod("<java.lang.Object: void <init>()>").makeRef())));
				// TODO Insert this call -> super();
				// specialinvoke r0.<java.lang.Object: void <init>()>();

				// Add the return stmt
				// Insert final void return to close the test method
				testClassInitializer.getActiveBody().getUnits().add(Jimple.v().newReturnVoidStmt());

			}

			Body constructorBody = testClassInitializer.getActiveBody();
			PatchingChain<Unit> units = constructorBody.getUnits();

			Local local = UtilInstrumenter.generateFreshLocal(constructorBody,
					RefType.v("org.junit.contrib.java.lang.system.TextFromStandardInputStream"));
			Unit newAssignStmt = Jimple.v().newAssignStmt(local,
					Jimple.v()
							.newStaticInvokeExpr(Scene.v()
									.getMethod(
											"<org.junit.contrib.java.lang.system.TextFromStandardInputStream: org.junit.contrib.java.lang.system.TextFromStandardInputStream emptyStandardInputStream()>")
									.makeRef()));
			//
			List<Unit> generated = new ArrayList<Unit>();
			generated.add(newAssignStmt);
			// localRule = $r1
			// r0.<de.unipassau.abc.testsubject.JUnitAssertions:
			// org.junit.rules.TemporaryFolder temporaryFolder> = $r1
			generated.add(Jimple.v().newAssignStmt(
					Jimple.v().newInstanceFieldRef(constructorBody.getThisLocal(), systemInJunit4RuleField.makeRef()),
					local));

			// Get the last unit - return
			units.insertBefore(generated, units.getLast());

		}

		for (SootMethod testMethod : testClass.getMethods()) {
			if (testMethod.getSignature().contains("test_")) {
				addSystemInToTest(testMethod, systemInJunit4RuleField, parsedTrace);
				// FIMXME. Add this if needed to check when carved test invokes
				// System.exit, otherwise this breaks the carved test
				// addSystemExit(testClass, parsedTrace);
			}
		}
	}

	public static void addSystemExit(SootClass testClass,
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace) {
		SootField systemExitJunit4RuleField = null;
		if (testClass.declaresFieldByName(SYSTEM_EXIT_RULE_NAME)) {
			systemExitJunit4RuleField = testClass.getFieldByName(SYSTEM_EXIT_RULE_NAME);
			return;
		} else {
			systemExitJunit4RuleField = new SootField(SYSTEM_EXIT_RULE_NAME,
					RefType.v("org.junit.contrib.java.lang.system.ExpectedSystemExit"),
					Modifier.PUBLIC | Modifier.FINAL);
			// Add annotation
			AnnotationGenerator.v().annotate(systemExitJunit4RuleField, Rule.class);
			Scene.v().loadClassAndSupport("org.junit.contrib.java.lang.system.ExpectedSystemExit");
			testClass.addField(systemExitJunit4RuleField);

			// Go inside the <init> and initialize the field
			SootMethod testClassInitializer;
			// create a class initializer if one does not already exist.
			// TODO Not sure HOW I CAN GENERATE IN PLACE INITIALIZATION, but
			// this works anyway
			if (testClass.declaresMethodByName("<init>")) {
				testClassInitializer = testClass.getMethodByName("<init>");
			} else {
				// FIXME This utility method to get or create <init> !
				// FIXME apparently here we shall call supet() or this() ?
				testClassInitializer = new SootMethod("<init>", Collections.<Type>emptyList(), VoidType.v(),
						Modifier.PUBLIC);
				// Build the body from the input list of statements
				JimpleBody body = Jimple.v().newBody(testClassInitializer);
				testClassInitializer.setActiveBody(body);
				testClass.addMethod(testClassInitializer);
				//
				body.insertIdentityStmts();
				// Add the return stmt
				// Insert final void return to close the test method
				testClassInitializer.getActiveBody().getUnits().add(Jimple.v().newReturnVoidStmt());
			}

			Body constructorBody = testClassInitializer.getActiveBody();
			PatchingChain<Unit> units = constructorBody.getUnits();

			Local local = UtilInstrumenter.generateFreshLocal(constructorBody,
					RefType.v("org.junit.contrib.java.lang.system.ExpectedSystemExit"));
			Unit newAssignStmt = Jimple.v().newAssignStmt(local,
					Jimple.v()
							.newStaticInvokeExpr(Scene.v()
									.getMethod(
											"<org.junit.contrib.java.lang.system.ExpectedSystemExit: org.junit.contrib.java.lang.system.ExpectedSystemExit none()>")
									.makeRef()));
			//
			List<Unit> generated = new ArrayList<Unit>();
			generated.add(newAssignStmt);
			// localRule = $r1
			// r0.<de.unipassau.abc.testsubject.JUnitAssertions:
			// org.junit.rules.TemporaryFolder temporaryFolder> = $r1
			generated.add(Jimple.v().newAssignStmt(
					Jimple.v().newInstanceFieldRef(constructorBody.getThisLocal(), systemExitJunit4RuleField.makeRef()),
					local));

			// Get the last unit - return
			units.insertBefore(generated, units.getLast());
		}

		// Capture the exit value for each test...
		for (SootMethod testMethod : testClass.getMethods()) {
			if (JimpleUtils.getMethodName(testMethod.getSignature()).startsWith("test_")) {
				addSystemExitToTest(testMethod, systemExitJunit4RuleField, parsedTrace);
			}
		}
	}

	private static void addSystemExitToTest(SootMethod testMethod, SootField systemExitJunit4RuleField, //
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace) {

		List<Unit> generated = new ArrayList<>();

		MethodInvocation methodInvocationToBeCarved = findMethodInvocationToBeCarved(testMethod);
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTraceFromSystemTest = findParsedSystemTest(
				methodInvocationToBeCarved, parsedTrace);

		// Go over the execution flow and find where exit or the rule is invoked
		// with the parameter
		// This also works as regression assertion

		Value valueReadFromInput = collectExpectedSystemExitValue(parsedTraceFromSystemTest,
				methodInvocationToBeCarved);

		if (valueReadFromInput == null) {
			// System.exit cannot be called by this test (I hope)
			return;
		}

		Body body = testMethod.getActiveBody();

		SootMethod expectSystemExit = Scene.v().getMethod(
				"<org.junit.contrib.java.lang.system.ExpectedSystemExit: void expectSystemExitWithStatus(int)>");

		Local localRule = UtilInstrumenter.generateFreshLocal(body,
				RefType.v("org.junit.contrib.java.lang.system.ExpectedSystemExit"));

		// Assign local to THIS.field
		generated.add(Jimple.v().newAssignStmt(localRule,
				Jimple.v().newInstanceFieldRef(body.getThisLocal(), systemExitJunit4RuleField.makeRef())));

		generated.add(Jimple.v().newInvokeStmt(
				Jimple.v().newVirtualInvokeExpr(localRule, expectSystemExit.makeRef(), valueReadFromInput)));
		//
		// Add everything at the beginnign of the method
		body.getUnits().insertAfter(generated, body.getUnits().getFirst());

	}

	// System exit might not event be called in the scope of the carved test !
	private static Value collectExpectedSystemExitValue(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace,
			MethodInvocation methodInvocationToBeCarved) {

		// Default to 0
		MethodInvocationMatcher systemExitMethodMatcher = MethodInvocationMatcher
				.byMethod("<java.lang.System: void exit(int)>");
		// Which one do we pick ?!
		// [>];StaticInvokeExpr;<java.lang.System: void exit(int)>;(0)
		// [>];VirtualInvokeExpr;<org.junit.contrib.java.lang.system.ExpectedSystemExit:
		// void expectSystemExitWithStatus(int)>;(0)

		DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
		CallGraph callGraph = parsedTrace.getThird();

		Set<MethodInvocation> subsequentCalls = callGraph.getMethodInvocationsSubsumedBy(methodInvocationToBeCarved);
		// System.out.println("MockingGenerator.collectExpectedSystemExitValue()
		// Calls subsumed by " + methodInvocationToBeCarved );
		// System.out.println(" " + subsequentCalls );

		for (MethodInvocation methodInvocation : subsequentCalls) {
			if (systemExitMethodMatcher.matches(methodInvocation)) {
				return dataDependencyGraph.getParametersSootValueFor(methodInvocation).get(0);
			}
		}
		return null;
	}

	private static MethodInvocation findMethodInvocationToBeCarved(SootMethod testMethod) {
		// The name of the tag is the value
		MethodInvocation methodInvocationToBeCarved = null;
		for (Tag tag : testMethod.getTags()) {

			if (tag.getName().startsWith("carving:")) {
				methodInvocationToBeCarved = MethodInvocation.fromTag(tag);
			}
		}

		return methodInvocationToBeCarved;
	}

	private static Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> findParsedSystemTest(
			MethodInvocation methodInvocationToBeCarved,
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace) {

		// Find the "right" SystemTest
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTraceFromSystemTest = null;
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> pTrace : parsedTrace.values()) {
			if (pTrace.getFirst().contains(methodInvocationToBeCarved)) {
				return pTrace;
			}
		}

		// System.out.println("MockingGenerator.findParsedSystemTest() Cannot
		// find any system test for test method "
		// + methodInvocationToBeCarved);
		return null;
	}

	private static void addSystemInToTest(SootMethod testMethod, SootField systemInJunit4RuleField, //
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace) {

		// create the string array which contains in the observation ORDER all
		// the method invocations
		// observed on Scanner's next methods. It might be something different
		// but at today,
		// this is the one we use in the evaluation

		MethodInvocation methodInvocationToBeCarved = findMethodInvocationToBeCarved(testMethod);
		Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTraceFromSystemTest = findParsedSystemTest(
				methodInvocationToBeCarved, parsedTrace);

		List<Value> valuesReadFromInput = collectValuesReadFromInput(parsedTraceFromSystemTest,
				methodInvocationToBeCarved);

		// At this point we have accumulated all the values that were read from
		// input during the execution, we store them into an array and invoke
		// org.junit.contrib.java.lang.system.TextFromStandardInputStream.provideLines(
		// java.lang.String[] ); to set up the input mocking
		Body body = testMethod.getActiveBody();
		Pair<Value, List<Unit>> parameterArrayAndInstructionsToCreateIt = UtilInstrumenter
				.generateStringArray(valuesReadFromInput, body);
		// TODO Find the insertion point at the beginning of the method
		List<Unit> generated = new ArrayList<>(parameterArrayAndInstructionsToCreateIt.getSecond());
		SootMethod provideLines = Scene.v().getMethod(
				"<org.junit.contrib.java.lang.system.TextFromStandardInputStream: void provideLines(java.lang.String[])>");

		// Generate a local ref
		Local localRule = UtilInstrumenter.generateFreshLocal(body,
				RefType.v("org.junit.contrib.java.lang.system.TextFromStandardInputStream"));

		// Assign local to THIS.field
		generated.add(Jimple.v().newAssignStmt(localRule,
				Jimple.v().newInstanceFieldRef(body.getThisLocal(), systemInJunit4RuleField.makeRef())));

		generated.add(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(localRule, provideLines.makeRef(),
				parameterArrayAndInstructionsToCreateIt.getFirst())));
		//
		// Add everything at the beginnign of the method
		body.getUnits().insertAfter(generated, body.getUnits().getFirst());

		// systemInJunit4RuleField.user

	}

	private static List<MethodInvocation> getPreviousCalls(MethodInvocation methodInvocationToBeCarved,
			ExecutionFlowGraph executionFlowGraph, DataDependencyGraph dataDependencyGraph, CallGraph callGraph) {
		// Get the last method, which the next method called after MUT, minus 1

		Set<MethodInvocation> subsubmedCalls = callGraph.getMethodInvocationsSubsumedBy(methodInvocationToBeCarved);

		// System.out.println("MockingGenerator.collectValuesReadFromInput()
		// Subsumed calls " + subsubmedCalls);
		// It there's not subsumed calls then the MUT is the last call
		MethodInvocation lastSubsumedCall = null;
		if (!subsubmedCalls.isEmpty()) {
			// The me the last call subsumed by the method to be carved.
			List<MethodInvocation> orderedSubsumedCalls = new ArrayList<>(subsubmedCalls);
			Collections.sort(orderedSubsumedCalls, new Comparator<MethodInvocation>() {
				@Override
				public int compare(MethodInvocation o1, MethodInvocation o2) {
					// Revert sort, the most likely one at the beginning
					return o2.getInvocationCount() - o1.getInvocationCount();
				}
			});

			// Keep looking for lastMethod calls that are actually inside the
			// execution flow graph !
			Iterator<MethodInvocation> iterator = orderedSubsumedCalls.iterator();
			while (iterator.hasNext()) {
				MethodInvocation mi = iterator.next();
				if (executionFlowGraph.contains(mi)) {
					lastSubsumedCall = mi;
					break;
				}
			}
		}

		if (lastSubsumedCall == null) {
			lastSubsumedCall = methodInvocationToBeCarved;
		}

		return executionFlowGraph.getOrderedMethodInvocationsBefore(lastSubsumedCall);
	}

	private static List<Value> collectValuesReadFromInput(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace,
			MethodInvocation methodInvocationToBeCarved) {

		List<Value> valuesFromInput = new ArrayList<>();

		// This is gonna match also nextInt/nextLong/etc... We need String types
		// !
		MethodInvocationMatcher scannerNextMethodMatcher = MethodInvocationMatcher
				.byMethod("<java.util.Scanner: .* next.*()>");

		ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
		DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
		CallGraph callGraph = parsedTrace.getThird();

		List<MethodInvocation> previousCalls = getPreviousCalls(methodInvocationToBeCarved, executionFlowGraph,
				dataDependencyGraph, callGraph);

		for (MethodInvocation methodInvocation : previousCalls) {
			if (scannerNextMethodMatcher.matches(methodInvocation)) {

				Value valueReadFromInput = dataDependencyGraph.getReturnObjectLocalFor(methodInvocation);
				if (valueReadFromInput == null) {
					logger.trace("MockingGenerator.collectValuesReadFromInput() >>> Null read from XML for "
							+ methodInvocation);
					try {
						valueReadFromInput = StringConstant
								.v((String) XMLDumper.loadObject(methodInvocation.getXmlDumpForReturn()));
					} catch (IOException e) {
						// // // TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("Swallow: " + e);
					}
				}

				logger.debug("MockingGenerator.collectValuesReadFromInput() " + methodInvocation + " --> "
						+ valueReadFromInput + " " + valueReadFromInput.getType());

				valuesFromInput.add(valueReadFromInput);
			}
		}

		return valuesFromInput;
	}
}
