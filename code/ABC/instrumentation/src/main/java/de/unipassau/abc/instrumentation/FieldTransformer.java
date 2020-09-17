package de.unipassau.abc.instrumentation;

import java.util.ArrayList;
import java.util.List;

import org.jboss.util.NotImplementedException;

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
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import utils.Constants;

public class FieldTransformer extends AbstractStmtSwitch {
	private SootMethod currentlyInstrumentedMethod;
	private Body currentlyInstrumentedMethodBody;
	private PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain;
	private List<SootClass> userClasses;

	private SootClass clsMonitor;
	private SootMethod monitorOnSyntheticMethodCall;
	private SootMethod monitorOnLibMethodReturnNormally;

	private String packageName;

	/**
	 * This generic method invocation captures that a field, identified by its name
	 * (string) of an object instance, identified by a reference, is set to a given
	 * value (identified by the return value)
	 */
	public final static String SIGNATURE = "<abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String)>";

	public FieldTransformer(final String packageName, final SootMethod currentlyInstrumentedMethod,
			final Body currentlyInstrumentedMethodBody,
			final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain, List<SootClass> userClasses) {
		//
		this.packageName = packageName;

		this.currentlyInstrumentedMethod = currentlyInstrumentedMethod;
		this.currentlyInstrumentedMethodBody = currentlyInstrumentedMethodBody;
		this.currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBodyUnitChain;
		this.userClasses = userClasses;

		this.clsMonitor = Scene.v().getSootClass(utils.Constants.MONITOR_CLASS);
		// TODO Move this to enum or something or at least the strings !
		this.monitorOnSyntheticMethodCall = clsMonitor.getMethodByName("onSyntheticMethodCall");
		this.monitorOnLibMethodReturnNormally = clsMonitor.getMethodByName("onAppMethodReturnNormally");
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {

		if (!(stmt.getLeftOp() instanceof FieldRef)) {
			return;
		}

		System.out.println(
				"\t\t\t WRAP FieldRef assignemnt " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());

		// TODO Refactoring neededn.
		/**
		 * For each set of the field generate one start lib call and one return lib call
		 */
		Value leftSidrOfFieldAssignment = null;
		if (stmt.getLeftOp() instanceof InstanceFieldRef) {
			InstanceFieldRef left = (InstanceFieldRef) stmt.getLeftOp();
			leftSidrOfFieldAssignment = left.getBase();
		} else if (stmt.getLeftOp() instanceof StaticFieldRef) {
			leftSidrOfFieldAssignment = NullConstant.v();
		}

		if (stmt.getRightOp() instanceof FieldRef) {
			// TODO Not sure what we can do here, because soot will not let us "return a
			// reference to a field as return value of a call"... but maybe this never
			// happens?
			throw new NotImplementedException("We do not support field ref as right side operators!");
		}

		Value nameOfTheField = StringConstant.v(((FieldRef) stmt.getLeftOp()).getField().getName());
		// This will be the return value of the fake method call.
		Value rightSideOfFieldAssignment = stmt.getRightOp();

		/**
		 * Since the fields we look at belong to the app and since we are creating
		 * synthetic method calls, we invoke Monitor.onSyntheticMethodCall. It requires
		 * the following parameters:
		 * <ol>
		 * <li>String packageName</li>
		 * <li>Object methodOwner</li>
		 * <li>String methodSignature</li>
		 * <li>java.lang.Object[] methodParameters</li>
		 * </ol>
		 */
//		List<Unit> instrumentationCodeBefore = new ArrayList<>();
//
//		List<Value> monitorOnSyntheticCallParameters = new ArrayList<Value>();
//		// String packageName
//		monitorOnSyntheticCallParameters.add(StringConstant.v(packageName));
//		// Object methodOwner, this is null because the synthetic method is static
//		monitorOnSyntheticCallParameters.add(NullConstant.v());
//		// String methodSignature
//		monitorOnSyntheticCallParameters.add(StringConstant.v(SIGNATURE));
//		// java.lang.Object[] methodParameters
//		// PARAMATERS list of objects
//		List<Value> invocationActualParameters = new ArrayList<>();
//		invocationActualParameters.add(leftSidrOfFieldAssignment);
//		invocationActualParameters.add(nameOfTheField);
//		// This makes sure we correctly wrap primitives
//		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter.generateParameterArray(
//				RefType.v("java.lang.Object"), invocationActualParameters, currentlyInstrumentedMethodBody);
//		// Append the parameter array to the parameters for the
//		// trace start
//		monitorOnSyntheticCallParameters.add(tmpArgsListAndInstructions.getFirst());
//		/*
//		 * Insert the instructions to create the array before using it
//		 */
//		instrumentationCodeBefore.addAll(tmpArgsListAndInstructions.getSecond());
//
//		// Prepare the call to monitorOnLibCall
//		final Stmt callTracerMethodStart = Jimple.v().newInvokeStmt(Jimple.v()
//				.newStaticInvokeExpr(monitorOnSyntheticMethodCall.makeRef(), monitorOnSyntheticCallParameters));
//		// Append the call to the instrumentation code
//		instrumentationCodeBefore.add(callTracerMethodStart);
//
//		// Inject the code (not sure we need to tag it or not...)
//		UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
//				instrumentationCodeBefore);

		/**
		 * Prepare the call to monitor.onLibMethodReturnNormally. This mimic the fake
		 * method invocation that returns the value used to set the field. This could be
		 * implemented in different ways, including returning null, but passing the
		 * value to set as an additional paramater to the fake method
		 */

		List<Unit> instrumentationCodeAfter = new ArrayList<>();
		List<Value> monitorOnReturnIntoParameters = new ArrayList<Value>();

		// The fake method is "STATIC" so null OWNER
		monitorOnReturnIntoParameters.add(NullConstant.v());
		// METHOD SIGNATURE as String
		monitorOnReturnIntoParameters.add(StringConstant.v(SIGNATURE));
		// METHOD CONTEXT as String is the method in which this unit is invoked
		monitorOnReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
		// Add the RETURN VALUE. This has to be wrapped as well
		Pair<Value, List<Unit>> tmpReturnValueAndInstructions = UtilInstrumenter.generateReturnValue(rightSideOfFieldAssignment, currentlyInstrumentedMethodBody); 
		
		monitorOnReturnIntoParameters.add(tmpReturnValueAndInstructions.getFirst());
		// Make sure that the wrapping instructions are inserted before calling monitor
		instrumentationCodeAfter.addAll( tmpReturnValueAndInstructions.getSecond() );
		
		// TODO We assume that all set field never causes an exception. Maybe it does,
		// but we cannot know at this point?
		final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
				.newStaticInvokeExpr(monitorOnLibMethodReturnNormally.makeRef(), monitorOnReturnIntoParameters));
		instrumentationCodeAfter.add(onReturnIntoCall);

		UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
				instrumentationCodeAfter);

	}

//	public void instrumentFieldRef(Body body, InstanceFieldRef fieldRef, Value value, Stmt stmt) {
//
//		List<Value> parameterList = new ArrayList<Value>();
//
//		parameterList.add(value);
//
//		List<Unit> generatedMethodCall = new ArrayList<Unit>();
//		List<Unit> generatedMethodReturn = new ArrayList<Unit>();
//
//		generatedMethodCall
//				.addAll(wrapTraceStart(field.getDeclaringClass().toString(), methodSignature, parameterList, body));
//		generatedMethodReturn.addAll(wrapTraceEnd(methodSignature, NullConstant.v(), value, body));
//
//		UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
//				generatedMethodCall);
//		UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
//				generatedMethodReturn);
//	}

//	public List<Unit> wrapTraceStart(String owner, String methodSignature, List<Value> parameterList, Body body) {
//
//		List<Unit> instrumentationCodeBefore = new ArrayList<Unit>();
//		List<Value> onLibCallParameters = new ArrayList<Value>();
//
//		onLibCallParameters.add(StringConstant.v(owner));
//		onLibCallParameters.add(StringConstant.v(methodSignature));
//		onLibCallParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
//
//		Pair<Value, List<Unit>> tempArgListAndInstr = UtilInstrumenter
//				.generateParameterArray(RefType.v("java.lang.Object"), parameterList, body);
//
//		onLibCallParameters.add(tempArgListAndInstr.getFirst());
//
//		instrumentationCodeBefore.addAll(tempArgListAndInstr.getSecond());
//
//		// Scene.v().loadClassAndSupport("java.lang.System");
//		// SootMethod hashCodeMethod = Scene.v().getMethod("<java.lang.System: int
//		// identityHashCode(java.lang.Object)>");
//		// instrumentationCodeBefore.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(identityHashCodeMethod.makeRef(),
//		// intent)));
//
//		final Stmt onLibCallInvocation = Jimple.v()
//				.newInvokeStmt(Jimple.v().newStaticInvokeExpr(libCall.makeRef(), onLibCallParameters));
//
//		instrumentationCodeBefore.add(onLibCallInvocation);
//
//		return instrumentationCodeBefore;
//	}

//	public List<Unit> wrapTraceEnd(String methodSignature, Value ownerValue, Value returnValue, Body body) {
//
//		List<Value> onReturnIntoParameters = new ArrayList<Value>();
//		List<Unit> instrumentationCodeAfter = new ArrayList<Unit>();
//
//		onReturnIntoParameters.add(ownerValue);
//		onReturnIntoParameters.add(StringConstant.v(methodSignature));
//		onReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
//		onReturnIntoParameters.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody, returnValue,
//				instrumentationCodeAfter));
//
//		final Stmt onReturnIntoCall = Jimple.v()
//				.newInvokeStmt(Jimple.v().newStaticInvokeExpr(returnInto.makeRef(), onReturnIntoParameters));
//
//		instrumentationCodeAfter.add(onReturnIntoCall);
//
//		return instrumentationCodeAfter;
//	}
}
