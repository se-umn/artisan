package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
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
import soot.NullType;
import soot.PatchingChain;
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

			Type parameterArrayType = RefType.v("java.lang.String");
			Type parameterArray = ArrayType.v(parameterArrayType, 1);
			// FIXME
			// Can this be the problem with bytecode generation !?!
			Value newArrayLocal = generateFreshLocal(body, parameterArray);
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

		if (value.getType() instanceof PrimType || JimpleUtils.isPrimitive(value.getType())) {
			// in case of a primitive type, we use boxing (I know it is not
			// nice, but it works...) in order to use the Object type
			if (value.getType() instanceof BooleanType || value.getType().toString().equals("boolean")) {
				Local booleanLocal = generateFreshLocal(body, RefType.v("java.lang.Boolean"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Boolean");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Boolean valueOf(boolean)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(booleanLocal, staticInvokeExpr);
				// newAssignStmt.addTag(new InstrumentedCodeTag());
				generated.add(newAssignStmt);

				return booleanLocal;
			} else if (value.getType() instanceof ByteType || value.getType().toString().equals("byte")) {
				Local byteLocal = generateFreshLocal(body, RefType.v("java.lang.Byte"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Byte");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Byte valueOf(byte)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(byteLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return byteLocal;
			} else if (value.getType() instanceof CharType || value.getType().toString().equals("char")) {
				Local characterLocal = generateFreshLocal(body, RefType.v("java.lang.Character"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Character");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Character valueOf(char)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(characterLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return characterLocal;
			} else if (value.getType() instanceof DoubleType || value.getType().toString().equals("double")) {
				Local doubleLocal = generateFreshLocal(body, RefType.v("java.lang.Double"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Double");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Double valueOf(double)");

				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(doubleLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return doubleLocal;
			} else if (value.getType() instanceof FloatType || value.getType().toString().equals("float")) {
				Local floatLocal = generateFreshLocal(body, RefType.v("java.lang.Float"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Float");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Float valueOf(float)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(floatLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return floatLocal;
			} else if (value.getType() instanceof IntType || value.getType().toString().equals("int")) {
				Local integerLocal = generateFreshLocal(body, RefType.v("java.lang.Integer"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Integer");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Integer valueOf(int)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(integerLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return integerLocal;
			} else if (value.getType() instanceof LongType || value.getType().toString().equals("long")) {
				Local longLocal = generateFreshLocal(body, RefType.v("java.lang.Long"));

				SootClass sootClass = Scene.v().getSootClass("java.lang.Long");
				SootMethod valueOfMethod = sootClass.getMethod("java.lang.Long valueOf(long)");
				StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(), value);

				Unit newAssignStmt = Jimple.v().newAssignStmt(longLocal, staticInvokeExpr);
				generated.add(newAssignStmt);

				return longLocal;
			} else if (value.getType() instanceof ShortType || value.getType().toString().equals("short")) {
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

		if (type instanceof NullType) {
			System.out.println("UtilInstrumenter.generateFreshLocal() Null type !!");
		}

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

		if (!rightSide.equals(returnValue)) {
			// No need for adding boxing and such
			Value newBoxedObjectLocal = generateFreshLocal(body, RefType.v("java.lang.Object"));
			Unit returnAssignStmt = Jimple.v().newAssignStmt(newBoxedObjectLocal, rightSide);
			generated.add(returnAssignStmt);
			return new Pair<Value, List<Unit>>(newBoxedObjectLocal, generated);
		}
		return new Pair<Value, List<Unit>>(returnValue, generated);
	}

	/**
	 * Return the objected loaded from XML which has type "ownerType"
	 * 
	 * @param body
	 * @param units
	 * @param xmlFile
	 * @param ownerType
	 * @return
	 */
	public static Local generateExpectedValueFor(Body body, List<Unit> validationUnits, String xmlFile,
			String ownerType) {
		// TODO Check if xmlFile exists

		// System.out.println("UtilInstrumenter.generateExpectedValueFor()
		// xmlFile is " + xmlFile );
		// TODO Check for nulls !
		List<Value> parameters = new ArrayList<>();
		parameters.add(StringConstant.v(xmlFile));

		// Make the call to load the object from memory as Object
		Local uncastedAndLoadedFromXml = generateFreshLocal(body, RefType.v("java.lang.Object"));
		Local expectedValue = generateFreshLocal(body, RefType.v(ownerType));

		SootMethod loadFromXml = Scene.v()
				.getMethod("<de.unipassau.abc.tracing.XMLDumper: java.lang.Object loadObject(java.lang.String)>");

		// Assign the uncasted to the return value of XMLDumper

		validationUnits.add(Jimple.v().newAssignStmt(uncastedAndLoadedFromXml,
				Jimple.v().newStaticInvokeExpr(loadFromXml.makeRef(), parameters)));

		// Create a second assignment which assign the result of casting the
		// uncasted to the target type
		validationUnits.add(Jimple.v().newAssignStmt(expectedValue,
				Jimple.v().newCastExpr(uncastedAndLoadedFromXml, expectedValue.getType())));

		return expectedValue;
	}

	public static Local generateExpectedValueForOwner(MethodInvocation methodInvocation, Body body,
			List<Unit> validationUnits) {

		return generateExpectedValueFor(body, validationUnits, methodInvocation.getXmlDumpForOwner(),
				JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()));
	}

	// This must consider the case of Primitive types !
	public static Local generateExpectedValueForReturn(MethodInvocation methodInvocation, Body body,
			List<Unit> validationUnits) {
			return generateExpectedValueFor(body, validationUnits, methodInvocation.getXmlDumpForReturn(),
					JimpleUtils.getReturnType(methodInvocation.getMethodSignature()));
	}
}