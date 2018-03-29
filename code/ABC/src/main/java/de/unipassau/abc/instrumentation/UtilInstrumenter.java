package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.data.Pair;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.ArrayRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewArrayExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.StringConstant;
import soot.util.Chain;

// Taken from FuzzDroid
public class UtilInstrumenter {

	/*
	 * In case parameters contains StringConstant we force those to be linked to
	 * a local so they get properly initialized
	 */
	public static Pair<Value, List<Unit>> generateParameterArray(Type arrayType, List<Value> parameterList, Body body) {
		try {
			List<Unit> generated = new ArrayList<Unit>();

			// Create an array to host the values
			NewArrayExpr arrayExpr = Jimple.v().newNewArrayExpr(arrayType, // RefType.v("java.lang.Object"),
					IntConstant.v(parameterList.size()));

			Value newArrayLocal = generateFreshLocal(body, getParameterArrayType());
			Unit newAssignStmt = Jimple.v().newAssignStmt(newArrayLocal, arrayExpr);
			generated.add(newAssignStmt);

			for (int i = 0; i < parameterList.size(); i++) {
				Value index = IntConstant.v(i);

				ArrayRef leftSide = Jimple.v().newArrayRef(newArrayLocal, index);
				Value rightSide = null;
				if (parameterList.get(i) instanceof ArrayRef) {
					System.out.println("UtilInstrumenter.generateParameterArray() Right side is also an array element "
							+ parameterList.get(i));
					// Probably we need to extract it to a local
					rightSide = generateCorrectObjectFromArrayRef(body, (ArrayRef) parameterList.get(i), generated);
				} else {
					rightSide = generateCorrectObject(body, parameterList.get(i), generated);
				}
				// System.out.println("UtilInstrumenter.generateParameterArray()
				// left " + leftSide);
				// System.out.println("UtilInstrumenter.generateParameterArray()
				// right " + rightSide);

				Unit parameterInArray = Jimple.v().newAssignStmt(leftSide, rightSide);
				generated.add(parameterInArray);
			}

			return new Pair<Value, List<Unit>>(newArrayLocal, generated);
		} catch (Throwable e) {
			System.out.println("UtilInstrumenter.generateParameterArray() ERROR While processing\n" + "arraytype "
					+ arrayType + "\n" + "parameterList " + parameterList + "\n" + "");
			throw e;
		}
	}

	// This is only for mocking input !
	public static Pair<Value, List<Unit>> generateStringArray(List<Value> parameterList, Body body) {
		try {
			List<Unit> generated = new ArrayList<Unit>();

			// Create an array to host the values
			NewArrayExpr stringArrayExpr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.String"),
					IntConstant.v(parameterList.size()));

			Value newArrayLocal = generateFreshLocal(body, getParameterArrayType());
			Unit newAssignStmt = Jimple.v().newAssignStmt(newArrayLocal, stringArrayExpr);
			generated.add(newAssignStmt);

			// For the elements that are not string, we call toString or String
			// value
			for (int i = 0; i < parameterList.size(); i++) {
				Value index = IntConstant.v(i);

				ArrayRef leftSide = Jimple.v().newArrayRef(newArrayLocal, index);
				Value rightSide = null;
				if (parameterList.get(i) instanceof ArrayRef) {
					System.out.println("UtilInstrumenter.generateParameterArray() Right side is also an array element "
							+ parameterList.get(i));
					// Probably we need to extract it to a local
					rightSide = generateStringObjectFromArrayRef(body, (ArrayRef) parameterList.get(i), generated);
				} else {
					rightSide = generateCorrectStringObject(body, parameterList.get(i), generated);
				}
				// System.out.println("UtilInstrumenter.generateParameterArray()
				// left " + leftSide);
				// System.out.println("UtilInstrumenter.generateParameterArray()
				// right " + rightSide);

				Unit parameterInArray = Jimple.v().newAssignStmt(leftSide, rightSide);
				generated.add(parameterInArray);
			}

			return new Pair<Value, List<Unit>>(newArrayLocal, generated);
		} catch (Throwable e) {
			throw e;
		}
	}

	public static Value generateStringObjectFromArrayRef(Body body, ArrayRef arrayRef, List<Unit> generated) {
		// Write some code to extract the element of the array into a variable
		// and then pass it along to the
		// generateCorrectObject

		Local arrayElement = generateFreshLocal(body, arrayRef.getType());
		Unit arrayElementAssignStmt = Jimple.v().newAssignStmt(arrayElement, arrayRef);
		generated.add(arrayElementAssignStmt);
		// Chain the call
		return generateCorrectStringObject(body, arrayElement, generated);
	}

	public static Value generateCorrectObjectFromArrayRef(Body body, ArrayRef arrayRef, List<Unit> generated) {
		// Write some code to extract the element of the array into a variable
		// and then pass it along to the
		// generateCorrectObject

		Local arrayElement = generateFreshLocal(body, arrayRef.getType());
		Unit arrayElementAssignStmt = Jimple.v().newAssignStmt(arrayElement, arrayRef);
		generated.add(arrayElementAssignStmt);
		// Chain the call
		return generateCorrectObject(body, arrayElement, generated);
	}

	/**
	 * Box primitive types into Objects or return the Object
	 * 
	 * @param body
	 * @param value
	 * @param generated
	 * @return
	 */
	public static Value generateCorrectObject(Body body, Value value, List<Unit> generated) {

		if (value.getType() instanceof PrimType) {
			// in case of a primitive type, we use boxing (I know it is not
			// nice, but it works...) in order to use the Object type
			if (value.getType() instanceof BooleanType) {
				Local booleanLocal = generateFreshLocal(body, RefType.v("java.lang.Boolean"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Boolean");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Boolean valueOf(boolean)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(booleanLocal, staticInvokeExpr);
				// newAssignStmt.addTag(new InstrumentedCodeTag());
				generated.add(newAssignStmt);

				return booleanLocal;
			} else if (value.getType() instanceof ByteType) {
				Local byteLocal = generateFreshLocal(body, RefType.v("java.lang.Byte"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Byte");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Byte valueOf(byte)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(byteLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return byteLocal;
			} else if (value.getType() instanceof CharType) {
				Local characterLocal = generateFreshLocal(body, RefType.v("java.lang.Character"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Character");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Character valueOf(char)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(characterLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return characterLocal;
			} else if (value.getType() instanceof DoubleType) {
				Local doubleLocal = generateFreshLocal(body, RefType.v("java.lang.Double"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Double");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Double valueOf(double)");

				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(doubleLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return doubleLocal;
			} else if (value.getType() instanceof FloatType) {
				Local floatLocal = generateFreshLocal(body, RefType.v("java.lang.Float"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Float");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Float valueOf(float)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(floatLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return floatLocal;
			} else if (value.getType() instanceof IntType) {
				Local integerLocal = generateFreshLocal(body, RefType.v("java.lang.Integer"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Integer");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Integer valueOf(int)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(integerLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return integerLocal;
			} else if (value.getType() instanceof LongType) {
				Local longLocal = generateFreshLocal(body, RefType.v("java.lang.Long"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Long");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Long valueOf(long)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(longLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return longLocal;
			} else if (value.getType() instanceof ShortType) {
				Local shortLocal = generateFreshLocal(body, RefType.v("java.lang.Short"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Short");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Short valueOf(short)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(shortLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return shortLocal;
			} else
				throw new RuntimeException("Ooops, something went all wonky!");
		} else {
			// just return the value, there is nothing to box
			return value;
		}
	}

	public static Value generateCorrectStringObject(Body body, Value value, List<Unit> generated) {
		if (value.getType() instanceof PrimType) {
			Local stringLocal = generateFreshLocal(body, RefType.v("java.lang.String"));
			StaticInvokeExpr staticInvokeExpr = null;
			if (value.getType() instanceof BooleanType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Boolean");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(boolean)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

			} else if (value.getType() instanceof ByteType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Byte");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(byte)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

			} else if (value.getType() instanceof CharType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Character");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(char)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

			} else if (value.getType() instanceof DoubleType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Double");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(double)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

			} else if (value.getType() instanceof FloatType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Float");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(float)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

			} else if (value.getType() instanceof IntType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Integer");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(int)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

			} else if (value.getType() instanceof LongType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Long");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(long)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);
			} else if (value.getType() instanceof ShortType) {

				SootClass sootClass = Scene.v().getSootClass("java.lang.Short");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.String toString(short)");
				staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);
			} else {
				throw new RuntimeException("Ooops, something went all wonky!");
			}

			Unit newAssignStmt = Jimple.v().newAssignStmt(stringLocal, staticInvokeExpr);
			generated.add(newAssignStmt);
			return stringLocal;
		} else {
			// Here it should be just String
			// just return the value, there is nothing to box
			return value;
		}

	}

	public static Local generateFreshLocal(Body body, Type type) {
		LocalGenerator lg = new LocalGenerator(body);
		return lg.generateLocal(type);
	}

	public static Type getParameterArrayType() {
		Type parameterArrayType = RefType.v("java.lang.Object");
		Type parameterArray = ArrayType.v(parameterArrayType, 1);

		return parameterArray;
	}

	public static Pair<Value, List<Unit>> generateReturnValue(Value returnValue, Body body) {

		List<Unit> generated = new ArrayList<Unit>();
		Value rightSide = generateCorrectObject(body, returnValue, generated);
		
		if( ! rightSide.equals( returnValue ) ){
			// No need for adding boxing and such
			Value newBoxedObjectLocal = generateFreshLocal(body, RefType.v("java.lang.Object"));
			Unit returnAssignStmt = Jimple.v().newAssignStmt(newBoxedObjectLocal, rightSide);
			generated.add(returnAssignStmt);
			return new Pair<Value, List<Unit>>(newBoxedObjectLocal, generated);
		}
		return new Pair<Value, List<Unit>>(returnValue, generated);
	}

	public static Value generateExpectedValueFor(Body body, Chain<Unit> units, String xmlFile) {

		// TODO Check for nulls !
		List<Value> parameters = new ArrayList<>();
		parameters.add(StringConstant.v(xmlFile));

		// Casting later
		// Local loadedFromXml = generateFreshLocal(body, RefType.v(
		// JimpleUtils.getReturnType( methodInvocation.getJimpleMethod() )));
		Local loadedFromXml = generateFreshLocal(body, RefType.v("java.lang.Object"));
		SootMethod loadFromXml = Scene.v()
				.getMethod("<de.unipassau.abc.tracing.XMLDumper: java.lang.Object loadObject(java.lang.String)>");

		units.add(Jimple.v().newAssignStmt(loadedFromXml,
				Jimple.v().newStaticInvokeExpr(loadFromXml.makeRef(), parameters)));

		return loadedFromXml;
	}

	public static Value generateExpectedValueForOwner(MethodInvocation methodInvocation, JimpleBody body,
			Chain<Unit> units) {

		return generateExpectedValueFor(body, units, methodInvocation.getXmlDumpForOwner());
	}

	public static Value generateExpectedValueForReturn(MethodInvocation methodInvocation, JimpleBody body,
			Chain<Unit> units) {
		return generateExpectedValueFor(body, units, methodInvocation.getXmlDumpForReturn());
	}
}