package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.Pair;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;

/**
 * This class contains the logic to wrap around libCalls, that is, method calls
 * that do not belong to the app. This also includes "artificial calls"
 * 
 * @author gambi
 *
 */
public class InstrumentLibCall extends AbstractStmtSwitch {

	private List<SootClass> userClasses;
	private SootMethod currentlyInstrumentedMethod;
	private Body currentlyInstrumentedMethodBody;
	private PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain;
	//
	private SootClass clsMonitor;
	private SootMethod monitorOnLibMethodCall;
	private SootMethod monitorOnLibMethodReturnNormally;

	public InstrumentLibCall(final SootMethod currentlyInstrumentedMethod, //
			final Body currentlyInstrumentedMethodBody, //
			final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain, //
			List<SootClass> userClasses) {
		this.currentlyInstrumentedMethod = currentlyInstrumentedMethod;
		this.currentlyInstrumentedMethodBody = currentlyInstrumentedMethodBody;
		this.currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBodyUnitChain;
		this.userClasses = userClasses;

		this.clsMonitor = Scene.v().getSootClass(utils.Constants.MONITOR_CLASS);
		// TODO Move this to enum or something or at least the strings !
		this.monitorOnLibMethodCall = clsMonitor.getMethodByName("onLibMethodCall");
		this.monitorOnLibMethodReturnNormally = clsMonitor.getMethodByName("onLibMethodReturnNormally");

	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		// System.out.println("InstrumentLibCall.caseAssignStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	// TODO: What's this?
	@Override
	public void caseBreakpointStmt(soot.jimple.BreakpointStmt stmt) {
		// System.out.println("InstrumentLibCall.caseBreakpointStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	// TODO: What's this? synchronize block?
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		// System.out.println("InstrumentLibCall.caseEnterMonitorStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	};

	// TODO: What's this? synchronize block?
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		// System.out.println("InstrumentLibCall.caseExitMonitorStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	};

	// TODO: What's this?
	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		// System.out.println("InstrumentLibCall.caseGotoStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	};

	// TODO: What's this? the initial "this", the implementation of self-referencing
	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {
		// System.out.println("InstrumentLibCall.caseIdentityStmt() " + stmt);
		super.caseIdentityStmt(stmt);
	};

	// TODO Is it possible that there's method calls at this point?
	@Override
	public void caseIfStmt(IfStmt stmt) {
		// System.out.println("InstrumentLibCall.caseIfStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	};

	// TODO Why there's no instrumentation of this?
	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		// System.out.println("InstrumentLibCall.caseInvokeStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		// System.out.println("InstrumentLibCall.caseLookupSwitchStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	@Override
	public void caseNopStmt(NopStmt stmt) {
		// System.out.println("InstrumentLibCall.caseNopStmt() " + stmt);
		super.caseNopStmt(stmt);
	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		// System.out.println("InstrumentLibCall.caseRetStmt() " + stmt);
		super.caseRetStmt(stmt);
	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		// System.out.println("InstrumentLibCall.caseReturnStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
		super.caseReturnStmt(stmt);
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		// System.out.println("InstrumentLibCall.caseReturnVoidStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {
		// System.out.println("InstrumentLibCall.caseTableSwitchStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		// System.out.println("InstrumentLibCall.caseThrowStmt() " + stmt);
		wrapLibraryInvocationIfAny(stmt);
	}

	/**
	 * This is the code that actually does the injection
	 * 
	 * @param csStmt
	 */
	public void wrapLibraryInvocationIfAny(Stmt csStmt) {
		if (!csStmt.containsInvokeExpr()) {
			return;
		}

		InvokeExpr invokeExpr = csStmt.getInvokeExpr();
		SootMethod invokedMethod = invokeExpr.getMethod();
		SootClass invokedClass = invokedMethod.getDeclaringClass();

		// Skip the methods that belong to app classes
		if (userClasses.contains(invokedClass)) {
			return;
		}

		System.out.println("\t\t\t WRAP method call site " + invokedMethod + " inside "
				+ currentlyInstrumentedMethod.getSignature());

		List<Unit> instrumentationCodeBefore = new ArrayList<>();
		List<Value> monitorOnLibCallParameters = new ArrayList<Value>();

		// Object methodOwnerOrEmptyString
		Value csOwner = null;
		if (invokedMethod.isStatic()) {
			csOwner = NullConstant.v(); // StringConstant.v("");
		} else if (invokedMethod.isConstructor()) {
			csOwner = NullConstant.v(); // StringConstant.v("");
		} else {
			if (invokeExpr instanceof InstanceInvokeExpr) {
				csOwner = ((InstanceInvokeExpr) invokeExpr).getBase();
			} else {
				// System.out.println("Not sure how to handle owner for " + invokeExpr);
				csOwner = StringConstant.v("");
			}
		}

		monitorOnLibCallParameters.add(csOwner);

		// String methodSignature
		monitorOnLibCallParameters.add(StringConstant.v(invokedMethod.getSignature()));

		// String Context
		monitorOnLibCallParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));

		/*
		 * Object[] methodParameters - We need to wrap this into an array of objects
		 */
		List<Value> invocationActualParameters = new ArrayList<>();
		for (int i = 0; i < invokeExpr.getArgCount(); i++) {
			invocationActualParameters.add(invokeExpr.getArg(i));
		}

		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter.generateParameterArray(
				RefType.v("java.lang.Object"), invocationActualParameters, currentlyInstrumentedMethodBody);

		// Append the array to the parameters for the trace start
		monitorOnLibCallParameters.add(tmpArgsListAndInstructions.getFirst());
		/*
		 * Insert the instructions to create the array before using it
		 */
		instrumentationCodeBefore.addAll(tmpArgsListAndInstructions.getSecond());

		// Prepare the call to monitorOnLibCall
		final Stmt callTracerMethodStart = Jimple.v().newInvokeStmt(
				Jimple.v().newStaticInvokeExpr(monitorOnLibMethodCall.makeRef(), monitorOnLibCallParameters));
		// Append the call to the instrumentation code
		instrumentationCodeBefore.add(callTracerMethodStart);

		/*
		 * We defer the instrumentation to enable holding the value of original owner
		 * back
		 */

		/*
		 * Preparing the call to monitorOnReturnInto -> dynCG2.Monitor.returnInto(Object
		 * csOwnerOrEmptyString, String methodSignature, Object returnValueOrNullIfVoid)
		 */
		List<Unit> instrumentationCodeAfter = new ArrayList<>();
		List<Value> monitorOnReturnIntoParameters = new ArrayList<Value>();

		// Object csOwnerOrEmptyString
		// After the constructor returns, csOwner is available.
		if (invokedMethod.isConstructor()) {
			if (invokeExpr instanceof InstanceInvokeExpr) {
				csOwner = ((InstanceInvokeExpr) invokeExpr).getBase();
			} else {
				// System.out.println("Not sure how to handle owner for " + invokeExpr);
				// TODO Probably NullConstant.v() ?
				csOwner = StringConstant.v("");
			}
		}

		if (csStmt instanceof AssignStmt) {
			/*
			 * [1] If the assign re-assign the owner, we need to log the "original" owner
			 * not the returned one... so we need to assign the original owner to a new
			 * local before the assign stmt overwrites it. In case of aliasing this might
			 * fail since it requires casting... so we need to check for that as well
			 * ?(TODO)
			 */
			if (((AssignStmt) csStmt).getLeftOp().equals(csOwner) && !JimpleUtils.isString(csOwner.getType())) {

				Local originalOwnerHolder = UtilInstrumenter.generateFreshLocal(currentlyInstrumentedMethodBody,
						csOwner.getType());

				AssignStmt capturingAssignStmt = Jimple.v().newAssignStmt(originalOwnerHolder, csOwner);
				// Since the new local assignment should trigger
				// BEFORE the method call we add it to:
				// instrumentationCodeBefore
				instrumentationCodeBefore.add(capturingAssignStmt);
				// TODO Store Assign
				// Use the originalOwnerHolder instead of the one to
				// be overwritten

				// if (opts.debugOut()) {
				// // System.out.println("\t\t\t >>>> APPLY PATCH TO
				// HOLD ORIGINAL OWNER TO " + csOwner + "
				// ("+csOwner.getType() +") --> " +
				// originalOwnerHolder +
				// "("+originalOwnerHolder.getType() +")" );
				// }

				csOwner = originalOwnerHolder;

			}
		}

		monitorOnReturnIntoParameters.add(csOwner);

		/*
		 * Finally insert the instrumentation code right before CS
		 * currentlyInstrumentedMethodBodyUnitChain.insertBefore(
		 * instrumentationCodeBefore, csStmt);
		 */
		UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, csStmt,
				instrumentationCodeBefore);

		// String csMethodSignature
		monitorOnReturnIntoParameters.add(StringConstant.v(invokedMethod.getSignature()));

		// String context
		monitorOnReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));

		// Object returnValueOrNullIfVoid
		Value returnValueFromCallSite = NullConstant.v();

		/*
		 * If csStmt is InvokeStmt its return value is not used, so soot does not link
		 * that to any local and we cannot read the return value directly. We replace
		 * the invoke stmt with an assignment stmt to a local, and use that assigned
		 * local to capture the return value (unless void)
		 *
		 * Unfortunately, this changes the CFG since the original invoke is removed
		 * (otherwise it will be executed twice!). but we might not even use the CFG
		 * anymore...
		 */

		if (csStmt instanceof InvokeStmt && !(invokedMethod.getReturnType() instanceof VoidType)) {
			/*
			 * We need to replace the original InvokeStmt with a AssignStmt and then return
			 * the Local assigned to
			 */
			returnValueFromCallSite = UtilInstrumenter.generateFreshLocal(currentlyInstrumentedMethodBody,
					invokedMethod.getReturnType());
			AssignStmt capturingAssignStmt = Jimple.v().newAssignStmt(returnValueFromCallSite, invokeExpr);
			// NOTE WE DO NOT TAG THIS AS TRACING CODE SINCE IT DOES
			// NOT ALTER THE SEMANTIC
			currentlyInstrumentedMethodBodyUnitChain.insertBefore(capturingAssignStmt, csStmt);
			// Remove the original call !
			currentlyInstrumentedMethodBodyUnitChain.remove(csStmt);
			// Replace the local vars
			csStmt = capturingAssignStmt;
		} else if (csStmt instanceof AssignStmt) {
			returnValueFromCallSite = ((AssignStmt) csStmt).getLeftOp();
		}

		// We need to wrap the return value to deal with primitives
		monitorOnReturnIntoParameters.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
				returnValueFromCallSite, instrumentationCodeAfter));
		// Prepare the actual call
		final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
				.newStaticInvokeExpr(monitorOnLibMethodReturnNormally.makeRef(), monitorOnReturnIntoParameters));
		instrumentationCodeAfter.add(onReturnIntoCall);

		/*
		 * Finally inject the instrumentation code AFTER the target which is either the
		 * original location or the new one stmt, after replacin invoke with assign
		 */
		// currentlyInstrumentedMethodBodyUnitChain.insertAfter(instrumentationCodeAfter,
		// targetStmt);
		UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, csStmt,
				instrumentationCodeAfter);
	}

}