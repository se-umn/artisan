package de.unipassau.abc.generation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.instrumentation.UtilInstrumenter;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Body;
import soot.DoubleType;
import soot.Local;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.DoubleConstant;
import soot.jimple.Jimple;
import soot.util.Chain;

/**
 * Contains methods that generate regression assertions given a method under
 * test
 * 
 * @author gambi
 *
 */
public class AssertionGenerator {

	private static final Logger logger = LoggerFactory.getLogger(AssertionGenerator.class);

	public static void gerenateRegressionAssertionOnOwner(Body body, Chain<Unit> units, Value expected, Value actual) {
		// IF TYPE PRIMITIVE THERE"S AN ERROR !
		// IF STRING THERE"S AN ERROR
		if (JimpleUtils.isPrimitive(actual.getType())) {
			logger.error("Something wrong happened ! A primitive cannot own method invocations ");
			//
		} else {
			generateAssertEqualsForObjects(body, units, actual.getType(), expected, actual);
		}

	}

	public static void gerenateRegressionAssertionOnReturnValue(Body body, Chain<Unit> units, Value expected,
			Value actual) {
		if (JimpleUtils.isPrimitive(actual.getType())) {
			generateAssertEqualsForPrimitiveTypeUsingBoxing(body, units, actual.getType(), expected, actual);
			//
		} else {
			generateAssertNullityForObjects(body, units, actual.getType(), expected, actual);
			generateAssertEqualsForObjects(body, units, actual.getType(), expected, actual);
		}

	}

	public static void generateAssertNullityForObjects(Body body, Chain<Unit> units, Type type, Value expected,
			Value actual) {
		List<Unit> generated = new ArrayList<>();

		if (expected == null || expected instanceof NullType) {
			SootMethod assertNull = Scene.v().getMethod("<org.junit.Assert: void assertNull(java.lang.Object)>");
			generated.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertNull.makeRef(), actual)));
		} else {
			SootMethod assertNotNull = Scene.v().getMethod("<org.junit.Assert: void assertNotNull(java.lang.Object)>");
			generated.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertNotNull.makeRef(), actual)));
		}

		units.addAll(generated);

	}

	public static void generateAssertEqualsForPrimitiveTypeUsingBoxing(Body body, Chain<Unit> units, Type type,
			Value expectedValue, Value actualValue) {
		List<Unit> generated = new ArrayList<>();

		if ("boolean".equals(type.toString())) {

			List<Value> assertParameters = new ArrayList<>();
			assertParameters.add(actualValue);

			// // TODO Probably can be done better
			if (expectedValue.toString().equals("1")) {
				SootMethod assertTrue = Scene.v().getMethod("<org.junit.Assert: void assertTrue(boolean)>");
				generated.add(Jimple.v()
						.newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertTrue.makeRef(), assertParameters)));
			} else {
				SootMethod assertFalse = Scene.v().getMethod("<org.junit.Assert: void assertFalse(boolean)>");
				generated.add(Jimple.v()
						.newInvokeStmt(Jimple.v().newStaticInvokeExpr(assertFalse.makeRef(), assertParameters)));
			}
		} else {
			Value boxedExpected = UtilInstrumenter.generateCorrectObject(body, expectedValue, generated);
			Value boxedActual = UtilInstrumenter.generateCorrectObject(body, actualValue, generated);

			generateAssertEqualsForObjects(body, units, type, boxedExpected, boxedActual, generated);
		}
		//
		units.addAll(generated);

	}

	// NOTE that expected value getType returns object, it should return type
	// instead ?
	public static void generateAssertEqualsForObjects(Body body, Chain<Unit> units, Type type, Value expectedValue,
			Value actualValue) {

		// Generate the assertion only if the CUT re-implements equals/hashCode
		// Scene.v().getMethod("<"+ expectedValue.getType().toString() +":
		// boolean equals(java.lang.Object>");
		// Scene.v().loadClass(expectedValue.getType().toString() )
		if (!redefinesEqualsAndHashCodeMethods(type)) {
			// Not sure about primitives and boxed primitives at this point..
			System.out.println("AssertionGenerator skip generateAssertEqualsForObjects for object " + type);
			return;
		}

		List<Unit> generated = new ArrayList<Unit>();
		generateAssertEqualsForObjects(body, units, type, expectedValue, actualValue, generated);
		units.addAll(generated);

	}

	// TODO Probably there's a simpler way to access that method directly or at least
	// get an exception that says the class does not..
	// TODO How to handle super types at this point? If the super types?
	private static boolean redefinesEqualsAndHashCodeMethods(Type type) {
		System.out.println("AssertionGenerator.redefinesEqualsAndHashCodeMethods() " + type.getEscapedName() );
		SootClass sc=Scene.v().loadClassAndSupport( type.getEscapedName() );
		
		// Check if this class or any of its super types except for Object redefine equals
		while( ! sc.getType().equals(RefType.v("java.lang.Object"))){
			for (SootMethod method : sc.getMethods()) {
				// NOTE THIS IS NAIVE
				if (method.getSignature().contains("equals(java.lang.Object)>")) {
					System.out.println(sc+ " has method " + method);
					return true;
				}
			}
			sc = sc.getSuperclass();
		}
//		for (SootClass applicationClass : Scene.v().getApplicationClasses()) {
//			if (applicationClass.getType().equals(type)) {
//				for (SootMethod method : applicationClass.getMethods()) {
//					if (method.getName().equals("equals")) {
//						System.out.println(applicationClass + " has method " + method);
//						return true;
//					}
//				}
//			}
//		}
		return false;
	}

	public static void generateAssertEqualsForObjects(Body body, Chain<Unit> units, Type type, Value expectedValue,
			Value actualValue, List<Unit> generated) {

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

		Local boxedExpected = (Local) UtilInstrumenter.generateCorrectObject(body, expectedValue, generated);
		Local boxedActual = (Local) UtilInstrumenter.generateCorrectObject(body, actualValue, generated);

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

			Local expectedValueLong = UtilInstrumenter.generateFreshLocal(body, RefType.v("long"));

			// Integer has longValue as well !
			SootMethod longValueMethod = Scene.v()
					.getMethod("<" + boxedExpected.getType().toString() + ": long longValue()>");
			generated.add(Jimple.v().newAssignStmt(expectedValueLong,
					Jimple.v().newVirtualInvokeExpr(boxedExpected, longValueMethod.makeRef())));

			Local actualValueLong = UtilInstrumenter.generateFreshLocal(body, RefType.v("long"));
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
			Local expectedValueCastedToDouble = UtilInstrumenter.generateFreshLocal(body, DoubleType.v());
			generated.add(Jimple.v().newAssignStmt(expectedValueCastedToDouble,
					Jimple.v().newCastExpr(expectedValue, DoubleType.v())));

			Local actualValueCastedToDouble = UtilInstrumenter.generateFreshLocal(body, DoubleType.v());
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
