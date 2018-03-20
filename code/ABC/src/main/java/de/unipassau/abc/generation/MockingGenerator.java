package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Rule;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.instrumentation.UtilInstrumenter;
import soot.Body;
import soot.Local;
import soot.Modifier;
import soot.NullType;
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
import soot.jimple.NewExpr;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.toolkits.annotation.j5anno.AnnotationGenerator;
import soot.tagkit.Tag;

// https://github.com/Sable/soot/wiki/Adding-profiling-instructions-to-applications
public class MockingGenerator {

	public static final String SYSTEM_IN_RULE_NAME = "userInputs";

	/**
	 * 
	 * @param testClass
	 * @param testMethod
	 * @param carvedTest
	 */
	public static void addSystemIn(SootClass testClass,
			// SootMethod testMethod,
			// MethodInvocation methodInvocationToBeCarved, //
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace) {
		SootField systemInJunit4RuleField = null;
		if (testClass.declaresFieldByName(SYSTEM_IN_RULE_NAME)) {
			systemInJunit4RuleField = testClass.getFieldByName(SYSTEM_IN_RULE_NAME);
			System.out.println("MockingGenerator.addSystemIn() Alread added ");
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
				System.out.println("Generate <init>");
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

			// Inside <init>
			// Local
			// org.junit.rules.TemporaryFolder $r1;
			// Call init on this
			// $r1 = new org.junit.rules.TemporaryFolder;
			// specialinvoke $r1.<org.junit.rules.TemporaryFolder: void
			// <init>()>();

			// We need to use this to initialize the object !
			// org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream()
			// NewExpr instantiateRuleExpr = Jimple.v()
			// .newNewExpr(RefType.v("org.junit.contrib.java.lang.system.TextFromStandardInputStream"));

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

		/*
		 * // Set the field. units.insertAfter( Jimple.v().newAssignStmt(
		 * 
		 */

		/////
		for (SootMethod testMethod : testClass.getMethods()) {
			if (testMethod.getSignature().contains("test_")) {
				addSystemInToTest(testMethod, systemInJunit4RuleField, parsedTrace);
			}
		}
	}

	private static void addSystemInToTest(SootMethod testMethod, SootField systemInJunit4RuleField, //
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace) {
		System.out.println("MockingGenerator.addSystemInToTest() to method " + testMethod);

		// create the string array which contains in the observation ORDER all
		// the method invocations
		// observed on Scanner's next methods. It might be something different
		// but at today,
		// this is the one we use in the evaluation

		// The name of the tag is the value
		MethodInvocation methodInvocationToBeCarved = null;
		for (Tag tag : testMethod.getTags()) {

			if (tag.getName().startsWith("carving:")) {
				System.out.println("MockingGenerator.addSystemInToTest() FOUND TAG " + tag.getName());
				methodInvocationToBeCarved = MethodInvocation.fromTag(tag);
			}
		}

		if (methodInvocationToBeCarved == null) {
			System.out.println("MockingGenerator.addSystemInToTest() Null " + systemInJunit4RuleField);
			return;
		}

		List<Value> valuesReadFromInput = collectValuesReadFromInput(parsedTrace, methodInvocationToBeCarved);
		// At this point we have accumulated all the values that were read from
		// input during the execution, we store them into an array and invoke
		// org.junit.contrib.java.lang.system.TextFromStandardInputStream.provideLines(
		// java.lang.String[] ); to set up the input mocking
		Body body = testMethod.getActiveBody();
		Pair<Value, List<Unit>> parameterArrayAndInstructionsToCreateIt = UtilInstrumenter
				.generateParameterArray(RefType.v("java.lang.String"), valuesReadFromInput, body);
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

	private static List<Value> collectValuesReadFromInput(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace,
			MethodInvocation methodInvocationToBeCarved) {
		List<Value> valuesFromInput = new ArrayList<>();

		// Get the last method, which the next method called after MUT, minus 1
		ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
		DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
		CallGraph callGraph = parsedTrace.getThird();
		//
		MethodInvocationMatcher scannerNextMethodMatcher = MethodInvocationMatcher
				.byMethod("<java.util.Scanner: .* next.*()>");
		//

		Set<MethodInvocation> subsubmedCalls = callGraph.getMethodInvocationsSubsumedBy(methodInvocationToBeCarved);

		System.out.println("MockingGenerator.collectValuesReadFromInput() Subsumed calls " + subsubmedCalls);
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

		List<MethodInvocation> previousCalls = executionFlowGraph.getOrderedMethodInvocationsBefore(lastSubsumedCall);

		System.out.println("MockingGenerator.collectValuesReadFromInput() LAST SUBSUMED CALL " + lastSubsumedCall);

		// Check only the one before the last subsumed call
		for (MethodInvocation methodInvocation : previousCalls) {

			System.out.println("Processing " + methodInvocation);

			if (scannerNextMethodMatcher.matches(methodInvocation)) {
				System.out.println(">> Found a call to scanner method: " + methodInvocation);
				Value valueReadFromInput = dataDependencyGraph.getReturnObjectLocalFor(methodInvocation);
				System.out.println(">> Return value is " + valueReadFromInput);
				valuesFromInput.add(valueReadFromInput);
			}
		}

		return valuesFromInput;
	}
}
