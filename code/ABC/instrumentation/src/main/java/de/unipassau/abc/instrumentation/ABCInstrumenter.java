package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.Pair;
import soot.Body;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

// This was the old Instrumented for Java...
@Deprecated
public class ABCInstrumenter {

	public static final Logger logger = LoggerFactory.getLogger(ABCInstrumenter.class);

	/**
	 * Returns the instructions to capture the owner of this invocation
	 * 
	 * @param instanceValue
	 * @param methodSignature
	 * @return
	 */
	public static List<Unit> addTraceObject(Value instanceValue, String methodSignature) {

		List<Unit> generated = new ArrayList<Unit>();

		if (instanceValue != null) {
			final SootMethod methodObject = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.Trace: void methodObject(java.lang.String,java.lang.Object)>");
			// Trace.methodObject requires
			// String -> methodName
			// Object -> "this" / invocation owner if any

			List<Value> methodObjectParameters = new ArrayList<Value>();
			// Method Name
			methodObjectParameters.add(StringConstant.v(methodSignature));
			//
			methodObjectParameters.add(instanceValue);

			// Generate the invocation to Trace.traceObject
			final Stmt callTracerMethodObject = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodObject.makeRef(), methodObjectParameters));
			//
			generated.add(callTracerMethodObject);

		} else {
			// TODO Not sure what's this ...
			logger.warn(">>>> ABCBodyTranformer.addTraceObject() null instance for " + methodSignature);
		}
		return generated;
	}

	public static List<Unit> addTraceStop(// SootMethod method,
			String methodSignature, Value ownerValue, Value returnValue, Body body) {

		List<Unit> generated = new ArrayList<Unit>();

		{
			final SootMethod methodStop = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.Trace: void methodStop(java.lang.String,java.lang.Object,java.lang.Object)>");
			// Trace.methodStop requires
			// String -> methodName
			// Object -> Return value
			List<Value> methodStopParameters = new ArrayList<Value>();

			// Method Name
			methodStopParameters.add(StringConstant.v(methodSignature));

			// Method Owner
			methodStopParameters.add(ownerValue);

			// Process return value
			// Why we need this ?

			// This box return value or null or void constants
			Pair<Value, List<Unit>> tmpReturnValueAndInstructions = null;
			if (JimpleUtils.isVoid(JimpleUtils.getReturnType(methodSignature))) {
				tmpReturnValueAndInstructions = new Pair<Value, List<Unit>>(StringConstant.v("void"),
						new ArrayList<Unit>());

			} else if (returnValue == null) {
				// Sure ? Why not?
				throw new RuntimeException("Cannot have a null returnValue at this point " + methodSignature);
			} else {
				tmpReturnValueAndInstructions = UtilInstrumenter2.generateReturnValue(returnValue, body);
			}

			// Append to the array the possibly Boxed return value
			methodStopParameters.add(tmpReturnValueAndInstructions.getFirst());
			final Stmt callStmtMethodStop = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodStop.makeRef(), methodStopParameters));
			// Append the instructions to units.
			tmpReturnValueAndInstructions.getSecond().add(callStmtMethodStop);

			generated.addAll(tmpReturnValueAndInstructions.getSecond());
		}
		return generated;
	}

	public static List<Unit> addTraceStart(String invokeType, String methodSignature, List<Value> parameterList,
			Body body) {

		List<Unit> generated = new ArrayList<>();

		final SootMethod methodStart = Scene.v().getMethod(
				"<de.unipassau.abc.tracing.Trace: void methodStart(java.lang.String,java.lang.String,java.lang.Object[])>");

		// Trace.methodStart requires
		// String -> methodType
		// String -> methodName
		// String -> Parameter Array...
		List<Value> methodStartParameters = new ArrayList<Value>();

		// Method Type
		methodStartParameters.add(StringConstant.v(invokeType));

		// Method Name
		methodStartParameters.add(StringConstant.v(methodSignature));

		// Parameter Array

		// Returns a Pair which contains the array and the instructions to
		// create it

		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter2
				.generateParameterArray(RefType.v("java.lang.Object"), parameterList, body);

		// Append the array to the parameters for the trace start
		methodStartParameters.add(tmpArgsListAndInstructions.getFirst());

		// Insert the instructions to create the array before using it, i.e,
		// before calling Trace.start

		// Create the invocation object
		final Stmt callTracerMethodStart = Jimple.v()
				.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodStart.makeRef(), methodStartParameters));

		// Append the trace start call as last after the instructions to prepare
		// its parameters
		tmpArgsListAndInstructions.getSecond().add(callTracerMethodStart);

		generated.addAll(tmpArgsListAndInstructions.getSecond());

		return generated;
	}
}
