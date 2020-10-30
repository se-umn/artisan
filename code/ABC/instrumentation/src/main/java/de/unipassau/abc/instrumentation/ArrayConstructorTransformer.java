package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.data.Pair;
import soot.Body;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

public class ArrayConstructorTransformer extends AbstractStmtSwitch {

	private List<SootClass> userClasses;
	private SootMethod currentlyInstrumentedMethod;
	private Body currentlyInstrumentedMethodBody;
	private PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain;
	//
	private SootClass clsMonitor;
	private SootMethod monitorOnLibCall;
	private SootMethod monitorOnReturnInto;

	public ArrayConstructorTransformer(final SootMethod currentlyInstrumentedMethod, //
			final Body currentlyInstrumentedMethodBody, //
			final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain, //
			List<SootClass> userClasses) {
		this.currentlyInstrumentedMethod = currentlyInstrumentedMethod;
		this.currentlyInstrumentedMethodBody = currentlyInstrumentedMethodBody;
		this.currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBodyUnitChain;
		this.userClasses = userClasses;

		this.clsMonitor = Scene.v().getSootClass(utils.Constants.MONITOR_CLASS);
		this.monitorOnLibCall = clsMonitor.getMethodByName("libCall");
		this.monitorOnReturnInto = clsMonitor.getMethodByName("returnInto");

	}

	/**
	 * Arrays do not have an INIT function, they are not objects, so we fake one the
	 * moment we see newarray. We need to wrap the call to their constructor
	 *
	 * @param stmt
	 */
	@Override
	public void caseAssignStmt(AssignStmt stmt) {

		if (!(stmt.getRightOp() instanceof NewArrayExpr)) {
			return;
		}

		System.out.println("\t\t\t WRAP arrayInit " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());

		NewArrayExpr right = (NewArrayExpr) stmt.getRightOp();
		// Parameter
		Value arraySize = right.getSize();
		// Return value from the constructor/owner of the
		// constructor
		Value array = stmt.getLeftOp();

		// Fake array init method for this array
		String fakeMethodSignature = "<" + array.getType() + ": void <init>(int)>";

		List<Unit> instrumentationCodeBefore = new ArrayList<>();

		List<Value> monitorOnLibCallParameters = new ArrayList<Value>();
		// There's no owner before calling the constructor
		monitorOnLibCallParameters.add(NullConstant.v());
		// String methodSignature
		monitorOnLibCallParameters.add(StringConstant.v(fakeMethodSignature));
		// String Context
		monitorOnLibCallParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
		// methodParameters: only the size
		List<Value> invocationActualParameters = new ArrayList<>();
		invocationActualParameters.add(arraySize);
		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter.generateParameterArray(
				RefType.v("java.lang.Object"), invocationActualParameters, currentlyInstrumentedMethodBody);
		// Append the parameter array to the parameters for the
		// trace start
		monitorOnLibCallParameters.add(tmpArgsListAndInstructions.getFirst());
		/*
		 * Insert the instructions to create the array before using it
		 */
		instrumentationCodeBefore.addAll(tmpArgsListAndInstructions.getSecond());

		// Prepare the call to monitorOnLibCall
		final Stmt callTracerMethodStart = Jimple.v()
				.newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorOnLibCall.makeRef(), monitorOnLibCallParameters));
		// Append the call to the instrumentation code
		instrumentationCodeBefore.add(callTracerMethodStart);

		// Inject the code (not sure we need to tag it or not...)
		UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
				instrumentationCodeBefore);

		List<Unit> instrumentationCodeAfter = new ArrayList<>();
		List<Value> monitorOnReturnIntoParameters = new ArrayList<Value>();

		// Owner of the method is the array being created
		monitorOnReturnIntoParameters.add(array);
		// String csMethodSignature
		monitorOnReturnIntoParameters.add(StringConstant.v(fakeMethodSignature));
		// String context
		monitorOnReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
		// Constructor returns no value, it's the owner that
		// changes...
		monitorOnReturnIntoParameters.add(NullConstant.v());
		// Prepare the actual call to Monitor.onReturn
		final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(
				Jimple.v().newStaticInvokeExpr(monitorOnReturnInto.makeRef(), monitorOnReturnIntoParameters));
		instrumentationCodeAfter.add(onReturnIntoCall);

		UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
				instrumentationCodeAfter);

	}
}
