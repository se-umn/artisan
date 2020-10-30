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
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

public class ArrayAccessTransformer extends AbstractStmtSwitch {

	private List<SootClass> userClasses;
	private SootMethod currentlyInstrumentedMethod;
	private Body currentlyInstrumentedMethodBody;
	private PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain;
	//
	private SootClass clsMonitor;
	private SootMethod monitorOnLibCall;
	private SootMethod monitorOnReturnInto;

	public ArrayAccessTransformer(final SootMethod currentlyInstrumentedMethod, //
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

	// TODO Is this necessary to be here ?! Maybe we can refactor all the "Synthetic
	// operation in a single place so we can instrument them as regular libCalls
	/**
	 * Simulate a call that returns an element of an array
	 *
	 * $r3 = $r2[$i0] -> $r3 = $r2.get($i0)
	 */
	@Override
	public void caseAssignStmt(AssignStmt stmt) {
		if (!(stmt.getRightOp() instanceof ArrayRef)) {
			return;

		}
		System.out.println("\t\t\t WRAP arrayAccess " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());

		ArrayRef arrayRef = (ArrayRef) stmt.getRightOp();
		Value array = arrayRef.getBase();
		Value position = arrayRef.getIndex();
		//
		Value returnValue = stmt.getLeftOp();

		// We need to replace only the first (actually only one) otherwise nested arrays
		// cannot be handled correctly [][][]
		String fakeMethodSignature = "<" + array.getType() + ": "
				+ array.getType().toString().replaceFirst("\\[\\]", "") + " get(int)>";

		List<Unit> instrumentationCodeBefore = new ArrayList<>();

		List<Value> monitorOnLibCallParameters = new ArrayList<Value>();
		// Owner is the array
		monitorOnLibCallParameters.add(array);
		// String methodSignature
		monitorOnLibCallParameters.add(StringConstant.v(fakeMethodSignature));
		// String Context
		monitorOnLibCallParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
		// methodParameters: position of the accessed elemenet
		List<Value> invocationActualParameters = new ArrayList<>();
		invocationActualParameters.add(position);
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

		// Owner of the method is the array
		monitorOnReturnIntoParameters.add(array);
		// String csMethodSignature
		monitorOnReturnIntoParameters.add(StringConstant.v(fakeMethodSignature));
		// String context
		monitorOnReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
		// We need to wrap the return value to deal with primitives before invoking
		// Monitor
		monitorOnReturnIntoParameters.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
				returnValue, instrumentationCodeAfter));
		// Prepare the actual call to Monitor.onReturn
		final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(
				Jimple.v().newStaticInvokeExpr(monitorOnReturnInto.makeRef(), monitorOnReturnIntoParameters));
		instrumentationCodeAfter.add(onReturnIntoCall);

		UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
				instrumentationCodeAfter);
	}
}
