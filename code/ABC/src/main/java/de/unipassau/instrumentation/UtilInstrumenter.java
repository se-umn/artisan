package de.unipassau.instrumentation;

import java.util.ArrayList;
import java.util.List;

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
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.StaticInvokeExpr;

// Taken from FuzzDroid
public class UtilInstrumenter {

	public static boolean isApiCall(InvokeExpr invokeExpr) {
		if (invokeExpr.getMethod().getDeclaringClass().isLibraryClass()
				|| invokeExpr.getMethod().getDeclaringClass().isJavaLibraryClass())
			return true;
		else
			return false;
	}

	public static Pair<Value, List<Unit>> generateParameterArray(List<Value> parameterList, Body body) {
		List<Unit> generated = new ArrayList<Unit>();

		// Create an array to host the values
		NewArrayExpr arrayExpr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.Object"),
				IntConstant.v(parameterList.size()));

		Value newArrayLocal = generateFreshLocal(body, getParameterArrayType());
		Unit newAssignStmt = Jimple.v().newAssignStmt(newArrayLocal, arrayExpr);
		generated.add(newAssignStmt);

		for (int i = 0; i < parameterList.size(); i++) {
			Value index = IntConstant.v(i);
			ArrayRef leftSide = Jimple.v().newArrayRef(newArrayLocal, index);
			Value rightSide = generateCorrectObject(body, parameterList.get(i), generated);

			Unit parameterInArray = Jimple.v().newAssignStmt(leftSide, rightSide);
			generated.add(parameterInArray);
		}

		return new Pair<Value, List<Unit>>(newArrayLocal, generated);
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
		} else
			// just return the value, there is nothing to box
			return value;
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

		Value newBoxedObjectLocal = generateFreshLocal(body, RefType.v("java.lang.Object"));
		Value rightSide = generateCorrectObject(body, returnValue, generated);
		Unit returnAssignStmt = Jimple.v().newAssignStmt(newBoxedObjectLocal, rightSide);
		generated.add(returnAssignStmt);

		return new Pair<Value, List<Unit>>(newBoxedObjectLocal, generated);
	}
}