package de.unipassau.abc.generation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.instrumentation.UtilInstrumenter2;
import soot.Body;
import soot.DoubleType;
import soot.Local;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.DoubleConstant;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.util.Chain;

/**
 * Contains methods that generate regression assertions given a method under
 * test
 * 
 * @author gambi
 *
 */
public class AssertionGenerator {

	public enum AssertVia {
		XML, GETTERS, USAGE_INSTANCE, USAGE_CLASS;
	}

	private static final Logger logger = LoggerFactory.getLogger(AssertionGenerator.class);

	/**
	 * This generates different kinds of regression assertions. Based on content
	 * (XML Serialization using XStream), and observed object usage.
	 * 
	 * @param carvedTestCase
	 * @param generateAssertionsUsing
	 * @param parsedTraces
	 */
	public static void gerenateRegressionAssertion(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase,
			AssertVia generateAssertionsUsing,
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces) {
		List<SootMethod> observers = new ArrayList<>();

		switch (generateAssertionsUsing) {
		case XML:
			generateRegressionAssertionUsingXStream(carvedTestCase);
			break;
		case GETTERS: // Consider all the getters
			observers.addAll(collectGettersFromClass(carvedTestCase.getThird().getDeclaringClass()));
			gerenateRegressionAssertionUsingGivenMethods(carvedTestCase, observers);
			break;
		case USAGE_INSTANCE:
			// Collect getters based only on the instance under test
			observers.addAll(collectUsageForInstance(carvedTestCase, parsedTraces));
			gerenateRegressionAssertionUsingGivenMethods(carvedTestCase, observers);
			break;
		case USAGE_CLASS:
			// Collect getters based only on the class under test
			observers.addAll(collectUsageForClass(carvedTestCase, parsedTraces));
			gerenateRegressionAssertionUsingGivenMethods(carvedTestCase, observers);
			break;
		default:
			break;
		}
	}

	private static Collection<? extends SootMethod> collectUsageForClass(

			Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase,
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces) {
		// Find all and only the invocation which belong to CUT irrespective on
		// their owner instance
		Set<MethodInvocation> observedUsageAtClassLevel = new HashSet<>();

		// Get all the method invocations invoke on instance of CUT
		MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();

		MethodInvocationMatcher cutMatchers = MethodInvocationMatcher
				.byClass(JimpleUtils.getClassNameForMethod(mut.getMethodSignature()));
		MethodInvocationMatcher gettersMatcher = MethodInvocationMatcher.byMethodName("get.*");

		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace : parsedTraces.values()) {
			observedUsageAtClassLevel.addAll(parsedTrace.getFirst().getMethodInvocationsFor(cutMatchers));
			observedUsageAtClassLevel.retainAll(parsedTrace.getFirst().getMethodInvocationsFor(gettersMatcher));
			// System.out.println("AssertionGenerator.collectUsageForClass()
			// Found " + observedUsageAtClassLevel.size() + " usages of class "
			// + JimpleUtils.getClassNameForMethod(mut.getJimpleMethod()) );
		}

		// Conver MethodInvodation to SootMethod
		Set<SootMethod> classUsage = new HashSet<>();
		for (MethodInvocation mi : observedUsageAtClassLevel) {
			classUsage.add(JimpleUtils.toSootMethod(mi));
		}
		return classUsage;
	}

	private static Set<SootMethod> collectUsageForInstance(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase,
			Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces) {
		MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();

		MethodInvocationMatcher theInstance = MethodInvocationMatcher.byInstance(mut.getOwner());
		MethodInvocationMatcher gettersMatcher = MethodInvocationMatcher.byMethodName("get.*");

		Set<MethodInvocation> observedUsage = new HashSet<>();
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace : parsedTraces.values()) {
			// get all the MUs which belong to the instance
			observedUsage.addAll(parsedTrace.getFirst().getMethodInvocationsFor(theInstance));
			// that are also getters
			observedUsage.retainAll(parsedTrace.getFirst().getMethodInvocationsFor(gettersMatcher));
		}

		// Conver MethodInvodation to SootMethod
		Set<SootMethod> usage = new HashSet<>();
		for (MethodInvocation mi : observedUsage) {
			usage.add(JimpleUtils.toSootMethod(mi));
		}
		return usage;
	}

	private static Set<SootMethod> collectGettersFromClass(SootClass cut) {
		Set<SootMethod> getters = new HashSet<>();
		for (SootMethod sootMethod : cut.getMethods()) {
			if (sootMethod.getName().startsWith("get") && sootMethod.getParameterCount() == 0) {
				getters.add(sootMethod);
			}
		}
		return getters;
	}

	/**
	 * This generate assertions using the abstract stmt switch !
	 * 
	 * @param carvedTestCase
	 */
	public static void generateRegressionAssertionUsingXStream(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase) {

		System.out.println("AssertionGenerator.generateRegressionAssertionUsingXStream()");

		MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
		SootClass cut = Scene.v().loadClassAndSupport(JimpleUtils.getClassNameForMethod(mut.getMethodSignature()));

		SootMethod testMethod = carvedTestCase.getThird();
		Body body = testMethod.getActiveBody();

		int beforeValidation = body.getUnits().size();

		List<Unit> validationUnits = new ArrayList<>();

		// The very last unit is the "return" we need the one before it...
		PatchingChain<Unit> units = body.getUnits();
		final Unit methodUnderTest = units.getPredOf(units.getLast());

		// Expected values !
		final String xmlOwner = mut.getXmlDumpForOwner();
		final String xmlReturnValue = mut.getXmlDumpForReturn();
		final Value primitiveReturnValue = carvedTestCase.getSecond().getReturnObjectLocalFor(mut);

		// System.out.println("DeltaDebugger.generateValidationUnit() for " +
		// methodUnderTest);
		methodUnderTest.apply(new AbstractStmtSwitch() {

			final SootMethod xmlDumperVerify = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.XMLVerifier: void verify(java.lang.Object,java.lang.String)>");

			final SootMethod xmlDumperVerifyPrimitive = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.XMLVerifier: void verifyPrimitive(java.lang.Object,java.lang.String)>");

			// return value
			public void caseAssignStmt(soot.jimple.AssignStmt stmt) {

				// In some case ? the right side of the stmt is not an
				// invocation (e.g., arrayref)

				System.out.println("Validation for " + stmt);

				if (stmt.containsInvokeExpr()) {

					InvokeExpr invokeExpr = stmt.getInvokeExpr();

					if (!(invokeExpr instanceof StaticInvokeExpr)) {
						generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
					}
				}
				generateValidationForReturnValue(stmt.getLeftOp());
			};

			// No Return value
			public void caseInvokeStmt(soot.jimple.InvokeStmt stmt) {
				InvokeExpr invokeExpr = stmt.getInvokeExpr();
				if (stmt.containsInvokeExpr()) {
					if (!(stmt.getInvokeExpr() instanceof StaticInvokeExpr)) {
						// Extract OWNER
						generateValidationForCut(((InstanceInvokeExpr) invokeExpr).getBase());
					}
				}
			}

			private void generateValidationForReturnValue(Value value) {
				Value wrappedValue = UtilInstrumenter2.generateCorrectObject(body, value, validationUnits);
				if (JimpleUtils.isPrimitive(value.getType())) {
					generateValidationForPrimitiveValue(wrappedValue, primitiveReturnValue, validationUnits);
				} else {
					generateValidationForValue(wrappedValue, xmlReturnValue, validationUnits);
				}
			}

			private void generateValidationForCut(Value value) {
				generateValidationForValue(value, xmlOwner, validationUnits);
			}

			private void generateValidationForPrimitiveValue(Value actualValue, Value expectedValue,
					List<Unit> validationUnits) {

				List<Value> methodStartParameters = new ArrayList<Value>();
				methodStartParameters.add(actualValue);

				// Wrap everythign into a String ! Why ? Can't I simply box the
				// expected Value as well, because this will be invoked on java
				// and fail !
				methodStartParameters.add(StringConstant.v(expectedValue.toString()));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerifyPrimitive.makeRef(), methodStartParameters)));
			};

			private void generateValidationForValue(Value value, String xmlFile, List<Unit> validationUnits) {

				if (xmlFile == null || xmlFile.trim().length() == 0) {
					// it can be a void call
					logger.debug("Null XML File for " + value + " cannot do not create validation call");
					return;
				}

				if (!new File(xmlFile).exists()) {
					logger.warn("Cannot find XML File" + xmlFile + ", do not create validation call");
					return;
				}

				List<Value> methodStartParameters = new ArrayList<Value>();
				methodStartParameters.add(value);
				methodStartParameters.add(StringConstant.v(xmlFile));

				// Create the invocation object
				validationUnits.add(Jimple.v().newInvokeStmt(
						Jimple.v().newStaticInvokeExpr(xmlDumperVerify.makeRef(), methodStartParameters)));
			};
		});

		// Include the validation units into the test method
		body.getUnits().insertBefore(validationUnits, body.getUnits().getLast());
	}

	/**
	 * Generate assertions on the owner based on the provided getters/observers
	 * methods
	 * 
	 * @param carvedTestCase
	 * @param getters
	 */
	private static void gerenateRegressionAssertionUsingGivenMethods(
			Triplette<ExecutionFlowGraph, DataDependencyGraph, SootMethod> carvedTestCase, List<SootMethod> getters) {

		logger.info("Generate Assertions with getters: " + getters);

		//
		MethodInvocation mut = carvedTestCase.getFirst().getLastMethodInvocation();
		SootClass cut = Scene.v().loadClassAndSupport(JimpleUtils.getClassNameForMethod(mut.getMethodSignature()));

		SootMethod testMethod = carvedTestCase.getThird();
		Body body = testMethod.getActiveBody();

		int beforeValidation = body.getUnits().size();

		List<Unit> validationUnits = new ArrayList<>();

		//
		if (getters.size() > 0) {
			final Local expectedValueForOwner = UtilInstrumenter2.generateExpectedValueForOwner(mut, body,
					validationUnits);
			// Why value and not Local ?
			final Local actualValueForOwner = (Local) carvedTestCase.getSecond().getObjectLocalFor(mut);

			for (SootMethod sootMethod : getters) {
				logger.info("Generating Regression assertions for " + mut + " using " + sootMethod);

				/*
				 * The problem here is: if the getter returns an Object, and this Object does
				 * not implement assertEquals. Then, no assertion is generated. Maybe we should
				 * recursively generate assertions till we get to a fixed point ? Or we can
				 * default to generate XML Assertion for those ?!
				 */

				// Call getter and store locally
				Local expectedValue = UtilInstrumenter2.generateFreshLocal(body, sootMethod.getReturnType());
				// TODO Probably a case statement here ? I have not idea
				// which
				// one should I call... and HOW to get this information
				// from...
				validationUnits.add(Jimple.v().newAssignStmt(expectedValue,
						Jimple.v().newVirtualInvokeExpr(expectedValueForOwner, sootMethod.makeRef())));

				Local actualValue = UtilInstrumenter2.generateFreshLocal(body, sootMethod.getReturnType());
				// TODO Probably a case statement here ? I have not idea
				// which
				// one should I call... and HOW to get this information
				// from...
				validationUnits.add(Jimple.v().newAssignStmt(actualValue,
						Jimple.v().newVirtualInvokeExpr(actualValueForOwner, sootMethod.makeRef())));

				// Assert that expected.getter equals actual.getter. If the
				// object DOES NOT DEFINE AN EQUALS METHOD, this skip the
				// generation. Store generation units into validationUnits

				if (JimpleUtils.isPrimitive(sootMethod.getReturnType())) {
					generateAssertEqualsForPrimitiveTypeUsingBoxing(body, validationUnits, sootMethod.getReturnType(),
							expectedValue, actualValue);
				} else {
					// Fall back to XML verify if equals is not defined. Many
					// other options are available, but not implemented.
					generateAssertEqualsForObjects(body, validationUnits, sootMethod.getReturnType(), expectedValue,
							actualValue);
				}
			}
		}

		// Generate assertions on return type only if there's a return type
		if (!JimpleUtils.isVoid(JimpleUtils.getReturnType(mut.getMethodSignature()))) {
			logger.info("Generating Regression assertions for return value of " + mut);

			Local returnValue = null;
			for (Local l : testMethod.getActiveBody().getLocals()) {
				if (l.getName().equals("returnValue")) {
					returnValue = l;
					break;
				}
			}

			if (JimpleUtils.isPrimitive(JimpleUtils.getReturnType(mut.getMethodSignature()))) {
				generateAssertEqualsForPrimitiveTypeUsingBoxing(body, validationUnits, returnValue.getType(),
						carvedTestCase.getSecond().getReturnObjectLocalFor(mut), //
						returnValue);
			} else {
				gerenateRegressionAssertionOnReturnValue(body, validationUnits, //
						// Are we sure those are ok ?! I suspect that the
						// following two are actually THE SAME !
						UtilInstrumenter2.generateExpectedValueForReturn(mut, body, validationUnits), //
						// carvedTestCase.getSecond().getReturnObjectLocalFor(mut)
						// ??
						returnValue);
			}
		}

		if (validationUnits.size() > 0) {
			// Eventually include the validation units in the right place:
			// before
			// the final return
			Unit insertionPoint = body.getUnits().getLast();
			body.getUnits().insertBefore(validationUnits, insertionPoint);
		}

		int afterValidation = body.getUnits().size();

		logger.info("\n\n\nValidation size " + (afterValidation - beforeValidation));

	}

	public static void gerenateRegressionAssertionOnOwner(Body body, List<Unit> validationUnits, Value expected,
			Value actual) {
		// IF TYPE PRIMITIVE THERE"S AN ERROR !
		// IF STRING THERE"S AN ERROR
		if (JimpleUtils.isPrimitive(actual.getType())) {
			logger.error("Something wrong happened ! A primitive cannot own method invocations ");
			//
		} else {
			generateAssertEqualsForObjects(body, validationUnits, actual.getType(), expected, actual);
		}

	}

	public static void gerenateRegressionAssertionOnReturnValue(Body body, List<Unit> validationUnits, Value expected,
			Value actual) {

		if (actual == null) {
			logger.error("Actual value is null ? ");
		} else {

			if (JimpleUtils.isPrimitive(actual.getType())) {
				generateAssertEqualsForPrimitiveTypeUsingBoxing(body, validationUnits, actual.getType(), expected,
						actual);
				//
			} else {
				generateAssertNullityForObjects(body, validationUnits, actual.getType(), expected, actual);
				generateAssertEqualsForObjects(body, validationUnits, actual.getType(), expected, actual);
			}
		}

	}

	public static void generateAssertNullityForObjects(Body body, List<Unit> validationUnits, Type type, Value expected,
			Value actual) {

		if (expected == null || expected instanceof NullType) {
			SootMethod assertNull = Scene.v().getMethod("<org.junit.Assert: void assertNull(java.lang.Object)>");
			validationUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertNull.makeRef(), actual)));
		} else {
			SootMethod assertNotNull = Scene.v().getMethod("<org.junit.Assert: void assertNotNull(java.lang.Object)>");
			validationUnits
					.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertNotNull.makeRef(), actual)));
		}
	}

	public static void generateAssertEqualsForPrimitiveTypeUsingBoxing(Body body, List<Unit> validationUnits, Type type,
			Value expectedValue, Value actualValue) {

		if ("boolean".equals(type.toString())) {

			List<Value> assertParameters = new ArrayList<>();
			assertParameters.add(actualValue);

			// // TODO Probably can be done better
			if (expectedValue.toString().equals("1")) {
				SootMethod assertTrue = Scene.v().getMethod("<org.junit.Assert: void assertTrue(boolean)>");
				validationUnits.add(Jimple.v()
						.newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertTrue.makeRef(), assertParameters)));
			} else {
				SootMethod assertFalse = Scene.v().getMethod("<org.junit.Assert: void assertFalse(boolean)>");
				validationUnits.add(Jimple.v()
						.newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertFalse.makeRef(), assertParameters)));
			}
		} else {
			Value boxedExpected = UtilInstrumenter2.generateCorrectObject(body, expectedValue, validationUnits);
			Value boxedActual = UtilInstrumenter2.generateCorrectObject(body, actualValue, validationUnits);

			// We cannot use tpye,since that would be a primitive !
			System.out.println(
					"AssertionGenerator.generateAssertEqualsForPrimitiveTypeUsingBoxing(): " + boxedExpected.getType());
			System.out.println(
					"AssertionGenerator.generateAssertEqualsForPrimitiveTypeUsingBoxing(): " + boxedActual.getType());
			//
			generateAssertEqualsForObjects(body, validationUnits, boxedExpected.getType(), boxedExpected, boxedActual);
		}
	}

	// NOTE that expected value getType returns object, it should return type
	// instead ?
	public static void generateAssertEqualsForObjects(Body body, List<Unit> validationUnits, Type type,
			Value expectedValue, Value actualValue) {

		// Generate the assertion only if the CUT re-implements equals/hashCode
		// Scene.v().getMethod("<"+ expectedValue.getType().toString() +":
		// boolean equals(java.lang.Object>");
		// Scene.v().loadClass(expectedValue.getType().toString() )
		if (!redefinesEqualsAndHashCodeMethods(type)) {
			generateIndirectAssertEquals(body, type, expectedValue, actualValue, validationUnits);
		} else {
			// Add the generated units to validationUnits
			generateAssertEqualsForObjects(body, type, expectedValue, actualValue, validationUnits);
		}

	}

	private static void generateIndirectAssertEquals(Body body, Type type, Value expectedValue, Value actualValue,
			List<Unit> validationUnits) {
		List<Value> assertParameters = new ArrayList<Value>();
		assertParameters.add(expectedValue);
		assertParameters.add(actualValue);

		final SootMethod xmlVerifyContent = Scene.v().getMethod(
				"<de.unipassau.abc.tracing.XMLVerifier: void verifyContentEquals(java.lang.Object,java.lang.Object)>");
		validationUnits.add(
				Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(xmlVerifyContent.makeRef(), assertParameters)));
	}

	// TODO Probably there's a simpler way to access that method directly or at
	// least
	// get an exception that says the class does not..
	// TODO How to handle super types at this point? If the super types?
	private static boolean redefinesEqualsAndHashCodeMethods(Type type) {
		System.out.println("AssertionGenerator.redefinesEqualsAndHashCodeMethods() " + type.getEscapedName());
		SootClass sc = Scene.v().loadClassAndSupport(type.getEscapedName());

		// Check if this class or any of its super types except for Object
		// redefine equals
		while (!sc.getType().equals(RefType.v("java.lang.Object"))) {
			for (SootMethod method : sc.getMethods()) {
				// NOTE THIS IS NAIVE
				if (method.getSignature().contains("equals(java.lang.Object)>")) {
					System.out.println(sc + " has method " + method);
					return true;
				}
			}
			sc = sc.getSuperclass();
		}
		// for (SootClass applicationClass : Scene.v().getApplicationClasses())
		// {
		// if (applicationClass.getType().equals(type)) {
		// for (SootMethod method : applicationClass.getMethods()) {
		// if (method.getName().equals("equals")) {
		// System.out.println(applicationClass + " has method " + method);
		// return true;
		// }
		// }
		// }
		// }
		return false;
	}

	private static void generateAssertEqualsForObjects(Body body, Type type, Value expectedValue, Value actualValue,
			List<Unit> generated) {

		List<Value> assertParameters = new ArrayList<Value>();

		assertParameters.add(expectedValue);
		assertParameters.add(actualValue);

		SootMethod assertEquals = Scene.v()
				.getMethod("<org.junit.Assert: void assertEquals(java.lang.Object,java.lang.Object)>");

		generated.add(
				Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertEquals.makeRef(), assertParameters)));
	}

	@Deprecated // Does not work. It might be the case we need to Box types to
	// enable casting ?
	public static void generateAssertEqualsForPrimitiveType(Body body, Chain<Unit> units, Type type,
			Value expectedValue, Value actualValue) {

		System.out.println(
				"AssertionGenerator.generateAssertEqualsForPrimitiveType() " + expectedValue + " -- " + actualValue);

		List<Value> assertParameters = new ArrayList<Value>();
		List<Unit> generated = new ArrayList<>();

		Local boxedExpected = (Local) UtilInstrumenter2.generateCorrectObject(body, expectedValue, generated);
		Local boxedActual = (Local) UtilInstrumenter2.generateCorrectObject(body, actualValue, generated);

		SootMethod assertEquals = null;
		switch (type.toString()) {
		case "boolean":
			assertParameters.add(actualValue);
			// // TODO Probably can be done better
			if (expectedValue.toString().equals("0")) {
				assertEquals = Scene.v().getMethod("<org.junit.Assert: void assertTrue(boolean)>");
			} else {
				assertEquals = Scene.v().getMethod("<org.junit.Assert: void assertFalse(boolean)>");
			}

			break;
		case "int":
		case "char":
		case "byte":
		case "short":
		case "long":

			// staticinvoke <java.lang.Integer: java.lang.Integer
			// valueOf(int)>(3)
			// Local expectedValueCastedToLong =
			// UtilInstrumenter.generateFreshLocal(body,
			// RefType.v("java.lang.Long"));
			// generated.add(Jimple.v().newAssignStmt(expectedValueCastedToLong,
			// Jimple.v().newCastExpr(boxedExpected,
			// RefType.v("java.lang.Long"))));

			// Local actualValueCastedToLong =
			// UtilInstrumenter.generateFreshLocal(body,
			// RefType.v("java.lang.Long"));
			// generated.add(Jimple.v().newAssignStmt(actualValueCastedToLong,
			// Jimple.v().newCastExpr(boxedActual,
			// RefType.v("java.lang.Long"))));

			Local expectedValueLong = UtilInstrumenter2.generateFreshLocal(body, RefType.v("long"));

			// Integer has longValue as well !
			SootMethod longValueMethod = Scene.v()
					.getMethod("<" + boxedExpected.getType().toString() + ": long longValue()>");
			generated.add(Jimple.v().newAssignStmt(expectedValueLong,
					Jimple.v().newVirtualInvokeExpr(boxedExpected, longValueMethod.makeRef())));

			Local actualValueLong = UtilInstrumenter2.generateFreshLocal(body, RefType.v("long"));
			generated.add(Jimple.v().newAssignStmt(actualValueLong,
					Jimple.v().newVirtualInvokeExpr(boxedActual, longValueMethod.makeRef())));

			assertParameters.add(expectedValueLong);
			assertParameters.add(actualValueLong);

			assertEquals = Scene.v().getMethod("<org.junit.Assert: void assertEquals(long,long)>");
			// assertEquals = Scene.v()
			// .getMethod("<org.junit.Assert: void
			// assertEquals(java.lang.Object,java.lang.Object)>");
			break;
		case "double":
		case "float":
			Local expectedValueCastedToDouble = UtilInstrumenter2.generateFreshLocal(body, DoubleType.v());
			generated.add(Jimple.v().newAssignStmt(expectedValueCastedToDouble,
					Jimple.v().newCastExpr(expectedValue, DoubleType.v())));

			Local actualValueCastedToDouble = UtilInstrumenter2.generateFreshLocal(body, DoubleType.v());
			generated.add(Jimple.v().newAssignStmt(actualValueCastedToDouble,
					Jimple.v().newCastExpr(actualValue, DoubleType.v())));

			assertParameters.add(expectedValue);
			assertParameters.add(actualValue);
			/// Delta
			assertParameters.add(DoubleConstant.v(0.01));
			assertEquals = Scene.v().getMethod("<org.junit.Assert: void assertEquals(double,double,double)>");
			break;
		default:
			break;
		}

		generated.add(
				Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertEquals.makeRef(), assertParameters)));

		units.addAll(generated);
	}

}
