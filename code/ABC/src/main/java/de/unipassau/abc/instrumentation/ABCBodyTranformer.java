package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;

public class ABCBodyTranformer extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(ABCBodyTranformer.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {

		final SootMethod containerMethod = body.getMethod();

		System.out.println("ABCBodyTranformer.internalTransform() >>> STARTING " + containerMethod);

		// Filter methods that we do not want to instrument
		// TODO Make this parameteric
		// FOR EXAMPLE external Library
		if (InstrumentTracer.filterMethod(containerMethod)) {
			logger.info("Skip instrumentation of: " + containerMethod);
			return;
		}

		if (InstrumentTracer.isExternalLibrary(containerMethod)) {
			// Probably we can do something about it
			logger.info("Skip instrumentation of: " + containerMethod);
			return;
		}

		final PatchingChain<Unit> units = body.getUnits();
		for (final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {

			final Unit u = iter.next();

			u.apply(new AbstractStmtSwitch() {

				public void caseAssignStmt(AssignStmt stmt) {
					// TODO Refactor. Use Jimple constants a not plain string...
					logger.trace("Inside method " + containerMethod + " caseAssignStmt() " + stmt);

					boolean containsInvokeExpr = stmt.containsInvokeExpr();

					if (containsInvokeExpr) {

						System.out.println(">>>> INVOKE " + stmt);

						InvokeExpr invokeExpr = stmt.getInvokeExpr();

						String invokeType = "";
						Value instanceValue = null;

						// TODO Some methods like, string builder and string
						// operations are not tracked at the moment
						SootMethod m = invokeExpr.getMethod();

						if (InstrumentTracer.doNotTraceCallsTo(m)) {
							logger.trace("Do not trace calls to " + m);
							return;
						}

						if (invokeExpr instanceof InstanceInvokeExpr) {
							invokeType = "InstanceInvokeExpr";
							instanceValue = ((InstanceInvokeExpr) invokeExpr).getBase();
						}
						if (invokeExpr instanceof SpecialInvokeExpr) {
							invokeType = "SpecialInvokeExpr";
							instanceValue = ((SpecialInvokeExpr) invokeExpr).getBase();
						}
						if (invokeExpr instanceof VirtualInvokeExpr) {
							invokeType = "VirtualInvokeExpr";
							instanceValue = ((VirtualInvokeExpr) invokeExpr).getBase();
						}
						if (invokeExpr instanceof StaticInvokeExpr) {
							// invokeType = Jimple.STATICINVOKE; == staticinvoke
							invokeType = "StaticInvokeExpr";

						}
						if (invokeExpr instanceof NewInvokeExpr) {
							invokeType = "NewInvokeExpr";

						}
						if (invokeExpr instanceof InterfaceInvokeExpr) {
							invokeType = "InterfaceInvokeExpr";
							instanceValue = ((InterfaceInvokeExpr) invokeExpr).getBase();

						}
						if (invokeExpr instanceof DynamicInvokeExpr) {
							invokeType = "DynamicInvokeExpr";

						}

						logger.trace(
								"New assignment invoke of :" + m.getSignature() + " instanceValue " + instanceValue);

						// Here we instrument the invocation inside the give
						// method.
						// if (!filterMethod(m)) {
						instrumentInvokeExpression(body, units, u, invokeExpr.getMethod(), invokeExpr.getArgs(), //
								stmt.getLeftOp(), // Return value ? Or the
													// target of the assign
													// ?
								instanceValue, invokeType);// , true);
						// }
					} else {

						if (stmt.getLeftOp() instanceof ArrayRef) {
							ArrayRef arrayRefExp = (ArrayRef) stmt.getLeftOp();

							instrumentArrayAssignmentExpression(body, units, u,
									//
									arrayRefExp.getBase(), // The ref to arraym
															// is this LeftOp ?!
									arrayRefExp.getIndex(), // The position in
															// the array
									stmt.getRightOp()); // I have no better
														// option at this
														// point...
						}
						if (stmt.getRightOp() instanceof ArrayRef) {
							System.out.println("\t >>> FOUND ASSIGNMENT FROM ARRAY " + stmt);

							ArrayRef arrayRefExp = (ArrayRef) stmt.getRightOp();

							instrumentArrayAccessExpression(body, units, u, stmt.getLeftOp(), //
									//
									arrayRefExp.getBase(), // The ref to arraym
															// is this LeftOp ?!
									arrayRefExp.getIndex()); // The position in
																// the array

						} else if (stmt.getRightOp() instanceof NewArrayExpr) {
							// Array INITIALIZATION
							// TODO Can it be a field reference on the left side
							// ?!

							NewArrayExpr right = (NewArrayExpr) stmt.getRightOp();
							// System.out.println( right.getBaseType() ); String
							// System.out.println( right.getType() ); String[]

							instrumentArrayInitExpression(body, units, u,
									//
									stmt.getLeftOp(), //
									right.getSize());

						} else if (stmt.getLeftOp().getType().equals(RefType.v("java.lang.String"))
								&& stmt.getRightOp() instanceof StringConstant) {
							instrumentStringAssignmentExpression(body, units, u, //
									stmt.getLeftOp(), // Ref to String
									stmt.getRightOp()); // Value of String
						} else {
							System.out.println(">>>> ASSIGN " + stmt);
						}

					}
				}

				// @Override
				// public void caseIdentityStmt(IdentityStmt stmt) {
				// System.out.println(
				// "InstrumentTracer.internalTransform(...).new
				// AbstractStmtSwitch() {...}.caseIdentityStmt() " + stmt);
				// super.caseIdentityStmt(stmt);
				// }
				//
				/*
				 * When soot triggers this, since there is no assignment of the
				 * return value, we cannot access it... probably, if we need to
				 * track this value as well, we can introcude a new assignement
				 * and attach it to the invoke stmt. This is also invokes inside
				 * if/for/etc. when there's no assignments...I hope that in case
				 * of assignment the invoke is triggered only once.
				 */
				public void caseInvokeStmt(InvokeStmt stmt) {

					// Most likely we can add a Local to host the value, how do
					// we handle invocations inside IF statement for example?

					logger.trace("Inside method " + containerMethod + " caseInvokeStmt() " + stmt);

					InvokeExpr invokeExpr = stmt.getInvokeExpr();
					SootMethod m = invokeExpr.getMethod();

					if (InstrumentTracer.doNotTraceCallsTo(m)) {
						logger.info("Do not trace calls to " + m);
						return;
					}

					String invokeType = null;
					Value instanceValue = null;

					if (invokeExpr instanceof InstanceInvokeExpr) {
						invokeType = "InstanceInvokeExpr";
						instanceValue = ((InstanceInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof SpecialInvokeExpr) {
						invokeType = "SpecialInvokeExpr";
						instanceValue = ((SpecialInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof VirtualInvokeExpr) {
						invokeType = "VirtualInvokeExpr";
						instanceValue = ((VirtualInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof StaticInvokeExpr) {
						invokeType = "StaticInvokeExpr";
						// Cannot be associate to any "value/instance"
					}
					if (invokeExpr instanceof NewInvokeExpr) {
						invokeType = "NewInvokeExpr";
						// TODO What's this ?!

					}
					if (invokeExpr instanceof InterfaceInvokeExpr) {
						invokeType = "InterfaceInvokeExpr";
						instanceValue = ((InterfaceInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof DynamicInvokeExpr) {
						invokeType = "DynamicInvokeExpr";

					}

					// Why the hell we pass null as returnValue ?! Because
					// it is hard to get that value from
					// the invoke expression

					// This creates many problems if the return type is
					// primitive... !
					Value returnValue = null;
					if (!(invokeExpr.getMethod().getReturnType() instanceof VoidType)) {
						// TODO
						// logger.debug(
						System.out.println(
								"InstrumentTracer.instrumentInvokeExpression() Add AssignStmt to access the value of "
										+ stmt + " which is not read ");
						// This is requires for generating regression assertions
						// in case this method is MUT
						returnValue = UtilInstrumenter.generateFreshLocal(body, invokeExpr.getMethod().getReturnType());
						Unit capturingAssignStmt = Jimple.v().newAssignStmt(returnValue, invokeExpr);
						units.insertBefore(capturingAssignStmt, stmt);
					}

					instrumentInvokeExpression(body, units, u, invokeExpr.getMethod(), invokeExpr.getArgs(),
							returnValue, instanceValue, invokeType);

				}

			});
		}

	}

	/**
	 * Fake a call to a generic method STORE of the array
	 * 
	 * @param body
	 * @param units
	 * @param u
	 * @param array
	 * @param arrayIndex
	 * @param rightOp
	 */
	private void instrumentArrayAssignmentExpression(Body body, PatchingChain<Unit> units, Unit u,
			//
			Value array, Value arrayIndex, //
			Value rightOp) {

		String invokeType = "ArrayOperation";
		String fakeMethodSignature = "<" + array.getType() + ": void store(int,"
				+ array.getType().toString().replace("[]", "") + ")>";

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Value> parameterList = new ArrayList<>();
		parameterList.add(arrayIndex);
		parameterList.add(rightOp);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));
		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			generatedAfter.addAll(addTraceObject(array, fakeMethodSignature));
		}

		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		generatedAfter.addAll(addTraceStop(fakeMethodSignature, null, body));
		//
		units.insertBefore(generatedBefore, u);
		units.insertAfter(generatedAfter, u);

	}

	/**
	 * Fake a call to a generic method GET of the array
	 * 
	 * @param body
	 * @param units
	 * @param u
	 * @param array
	 * @param arrayIndex
	 * @param rightOp
	 */
	private void instrumentArrayAccessExpression(Body body, PatchingChain<Unit> units, Unit u,
			//
			Value leftOp, //
			Value array, Value arrayIndex //
	) {

		String invokeType = "ArrayOperation";
		String fakeMethodSignature = "<" + array.getType() + ": " + array.getType().toString().replace("[]", "")
				+ " get(int)>";

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Value> parameterList = new ArrayList<>();
		parameterList.add(arrayIndex);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		//
		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));

		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			generatedAfter.addAll(addTraceObject(array, fakeMethodSignature));
		}

		generatedAfter.addAll(addTraceStop(fakeMethodSignature, leftOp, body));
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		// FIXME Not sure that the return value is the right one...
		units.insertBefore(generatedBefore, u);
		units.insertAfter(generatedAfter, u);

	}

	private void instrumentStringAssignmentExpression(Body body, PatchingChain<Unit> units, Unit u, Value leftOp,
			Value rightOp) {

		String invokeType = "StringOperation";
		String fakeMethodSignature = "<java.lang.String: void <init>()>";

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Value> parameterList = new ArrayList<>();
		// parameterList.add(rightOp);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();
		
		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));
		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		generatedAfter.addAll(addTraceObject(leftOp, fakeMethodSignature));
		generatedAfter.addAll(addTraceStop(fakeMethodSignature, null, body));
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		units.insertBefore(generatedBefore, u);
		units.insertAfter(generatedAfter, u);

	}

	// Arrays do not have an INIT function, they are not objects, so we fake one
	// the moment we see newarray
	private void instrumentArrayInitExpression(Body body, PatchingChain<Unit> units, Unit u,
			//
			Value array, Value arraySize) {

		String invokeType = "ArrayOperation";
		// Init <size>
		String fakeMethodSignature = "<" + array.getType() + ": void <init>(int)>";

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Value> parameterList = new ArrayList<>();
		parameterList.add(arraySize);

		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, fakeMethodSignature, parameterList, body));
		/*
		 * Instrument body to include a call to Trace.object the execution of
		 * method, we store the reference to the instance object making the call
		 * unless the method is static. In this case there's no object instance,
		 * hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			generatedAfter.addAll(addTraceObject(array, fakeMethodSignature));
		}

		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		generatedAfter.addAll(addTraceStop(fakeMethodSignature, null, body));

		units.insertBefore(generatedBefore, u);
		units.insertAfter(generatedAfter, u);

	}

	/**
	 * BodyTransformer repeats for every execution, so to make a check that
	 * every method is instrumented only once I implement HashSet for every
	 * function. Can be done using class as well, but i'll look into that later.
	 *
	 * 
	 * @param body
	 * @param units
	 * @param u
	 * @param method
	 * @param parameterValues
	 * @param returnValue
	 * @param instanceValue
	 * @param invokeType
	 */
	private void instrumentInvokeExpression(//
			Body body, // Method body of the method which contains <method>
			final PatchingChain<Unit> units, //
			Unit u, //
			//
			SootMethod method, // The method which is Invoked this one is the
								// one we want to wrap: call traceStart and
								// traceObject (only non static) before the
								// actual call, and traceStop after the call
			List<Value> parameterValues, // The referes to value parameter. The
											// actual parameter
			Value returnValue, //
			/*
			 * The instance upon which the method is invoked or null if this is
			 * static method
			 */
			Value instanceValue, //
			/*
			 * Type of invocation.
			 */
			String invokeType) {

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Unit> generatedBefore = new ArrayList<>();
		List<Unit> generatedAfter = new ArrayList<>();

		generatedBefore.addAll(addTraceStart(invokeType, method.getSignature(), parameterValues, body));

		/*
		 * Instrument body to include a call to Trace.object BEFORE ! the
		 * execution of method, we store the reference to the instance object
		 * making the call unless the method is static. In this case there's no
		 * object instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType) ) {
			if( method.getSignature().contains("<init>")){
				generatedAfter.addAll(addTraceObject(instanceValue, method.getSignature()));
			} else {
				generatedBefore.addAll(addTraceObject(instanceValue, method.getSignature()));
			}
		}

		generatedAfter.addAll(addTraceStop(method.getSignature(), returnValue, body));
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		units.insertBefore(generatedBefore, u);
		units.insertAfter(generatedAfter, u);

		// TODO XML SERIALIZATION
		// dumpToXML(b, units, u, method, parameterValues, returnValue,
		// instanceValue, invokeType);
	}

	private List<Unit> addTraceObject(Value instanceValue, String methodSignature) {

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
			generated.add(callTracerMethodObject);

		} else {
			System.out.println("ABCBodyTranformer.addTraceObject() null instance for " + methodSignature);
		}
		return generated;
	}

	private List<Unit> addTraceStop(// SootMethod method,
			String methodSignature, Value returnValue, Body body) {

		logger.trace("InstrumentTracer.addTraceStop() for " + methodSignature);
		List<Unit> generated = new ArrayList<Unit>();

		{
			final SootMethod methodStop = Scene.v()
					.getMethod("<de.unipassau.abc.tracing.Trace: void methodStop(java.lang.String,java.lang.Object)>");
			// Trace.methodStop requires
			// String -> methodName
			// Object -> Return value
			List<Value> methodStopParameters = new ArrayList<Value>();

			// Method Name
			methodStopParameters.add(StringConstant.v(methodSignature));

			// This box return value or null or void constants
			Pair<Value, List<Unit>> tmpReturnValueAndInstructions = null;
			if (JimpleUtils.isVoid(JimpleUtils.getReturnType(methodSignature))) {
				tmpReturnValueAndInstructions = new Pair<Value, List<Unit>>(StringConstant.v("void"),
						new ArrayList<Unit>());

			} else if (returnValue == null) {
				throw new RuntimeException("Cannot have a null returnValue at this point " + methodSignature);
			} else {
				tmpReturnValueAndInstructions = UtilInstrumenter.generateReturnValue(returnValue, body);
			}

			// Append to the array the possibly Boxed return value
			methodStopParameters.add(tmpReturnValueAndInstructions.getFirst());
			final Stmt callStmtMethodStop = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodStop.makeRef(), methodStopParameters));
			// Append the instructions to units.
			tmpReturnValueAndInstructions.getSecond().add(callStmtMethodStop);

			generated.addAll(tmpReturnValueAndInstructions.getSecond());
		}
		// units.insertAfter(generated, u);
		return generated;
	}

	private List<Unit> addTraceStart(String invokeType, String methodSignature, List<Value> parameterList, Body body) {

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

		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter
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

		// Add this code before invoking the method
		// units.insertBefore(tmpArgsListAndInstructions.getSecond(), u);
		generated.addAll(tmpArgsListAndInstructions.getSecond());
		// logger.trace
		System.out.println("\n" + "InstrumentTracer.traceCall() Start Method Parameter List " + methodStartParameters
				+ "\n" + "InstrumentTracer.traceCall() Start Method Instructions"
				+ tmpArgsListAndInstructions.getSecond());

		return generated;
	}

}
