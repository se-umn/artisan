package de.unipassau.abc.instrumentation;

import static de.unipassau.abc.instrumentation.ABCInstrumenter.addTraceObject;
import static de.unipassau.abc.instrumentation.ABCInstrumenter.addTraceStart;
import static de.unipassau.abc.instrumentation.ABCInstrumenter.addTraceStop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.VirtualInvokeExpr;

/**
 * IDEALLY WE SHOULD TRACK EACH MOD THAT WE MADE AND SKIP ARTIFICAL CALLS WHILE
 * INSTRUMENTING !
 * 
 * @author gambi
 *
 */
public class ABCBodyTranformer extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(ABCBodyTranformer.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {

		final SootMethod containerMethod = body.getMethod();

		System.out.println("ABCBodyTranformer.internalTransform() STARTING " + containerMethod);
		

		// Skip the instrumentation of those methods. I.e., do not instrument
		// them
		if (InstrumentTracer.filterMethod(containerMethod)) {
			logger.debug("Skip instrumentation of: " + containerMethod);
			return;
		}
		// Skip the instrumentation of those methods
		if (InstrumentTracer.isExternalLibrary(containerMethod)) {
			logger.debug("Skip instrumentation of: " + containerMethod);
			return;
		}

		final PatchingChain<Unit> units = body.getUnits();
		for (final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {

			final Unit currentUnit = iter.next();

			if (currentUnit.hasTag(ABCTag.TAG_NAME)) {
				logger.info("DO NOT TRACE OUR OWN CODE " + currentUnit);
			}

			currentUnit.apply(new AbstractStmtSwitch() {

				// At this point we care only for assign which have locals on
				// the left and invoke on the right.
				// The invokes are such that only LOCALS are parameters
				public void caseAssignStmt(AssignStmt stmt) {

					if (stmt.getLeftOp() instanceof Local && stmt.getRightOp() instanceof InvokeExpr) {

						logger.debug("Processing " + stmt);
						
						if( stmt.hasTag( ABCTag.TAG_NAME ) ){
							System.out.println("Do not trace stmt " + stmt);
						}

						InvokeExpr invokeExpr = stmt.getInvokeExpr();

						String invokeType = "";
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

						instrumentInvokeExpression(body, currentUnit, invokeExpr.getMethod(), invokeExpr.getArgs(), //
								stmt.getLeftOp(), //
								instanceValue, invokeType);
					} else {
						logger.debug("Skip " + stmt);
					}
				}

				public void caseInvokeStmt(InvokeStmt stmt) {

					// We care only for invoke methods which return void since
					// those are not covered by the previous case
					InvokeExpr invokeExpr = stmt.getInvokeExpr();
					if (!(invokeExpr.getMethod().getReturnType() instanceof VoidType)) {
						return;
					} else {

						if (InstrumentTracer.doNotTraceCallsTo(invokeExpr.getMethod())) {
							// Becomes debug !
							logger.debug("Do not trace calls to " + currentUnit);
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

						instrumentInvokeExpression(body, currentUnit, invokeExpr.getMethod(), invokeExpr.getArgs(),
								null, // THIS IS ONLY FOR VOID
								instanceValue, invokeType);
					}

				}

			});
		}

	}

	/**
	 * BodyTransformer repeats for every execution, so to make a check that
	 * every method is instrumented only once I implement HashSet for every
	 * function. Can be done using class as well, but i'll look into that later.
	 *
	 * 
	 * @param body
	 * @param units
	 * @param currentUnit
	 * @param method
	 * @param parameterValues
	 * @param returnValue
	 * @param instanceValue
	 * @param invokeType
	 */
	private void instrumentInvokeExpression(//
			Body body, Unit currentUnit, //
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
		
		if( method.getSignature().equals("<java.lang.Integer: java.lang.Integer valueOf(int)>")){
			System.err.println("GOTCHA");
		}

		if (InstrumentTracer.doNotTraceCallsTo(method)) {
			// Becomes debug !
			logger.debug("Do not trace calls to " + method);
			return;
		}
		
		if (currentUnit.hasTag(ABCTag.TAG_NAME)) {
			logger.info("DO NOT TRACE OUR OWN CODE " + currentUnit);
		}
		
		
		
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
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			// NOTE THAT HERE WE NEED TO SWAP THIS...
			if (method.getSignature().contains("<init>")) {
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
		injectTracingCode(body, currentUnit, generatedBefore, generatedAfter);

		// TODO XML SERIALIZATION
		// dumpToXML(b, units, u, method, parameterValues, returnValue,
		// instanceValue, invokeType);
	}

	/*
	 * Ensure to TAG own on code !
	 */
	private void injectTracingCode(Body body, Unit currentUnit, List<Unit> generatedBefore, List<Unit> generatedAfter) {
		PatchingChain<Unit> units = body.getUnits();
		for (Unit unit : generatedBefore) {
			System.out.println("ABCInstrumentArtificialInvocations.injectTracingCode() Tagging >>> " + unit );
			unit.addTag(ABCTag.TAG);
		}

		for (Unit unit : generatedAfter) {
			unit.addTag(ABCTag.TAG);
		}
		// Be sure to tage the units we generate !
		units.insertBefore(generatedBefore, currentUnit);
		units.insertAfter(generatedAfter, currentUnit);

	}
}
