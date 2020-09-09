package de.unipassau.abc.instrumentation;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.Pair;

import java.util.List;
import java.util.ArrayList;

import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.StringConstant;
import soot.jimple.Stmt;
import soot.jimple.NullConstant;
import soot.jimple.Jimple;
import soot.SootMethod;
import soot.SootClass;
import soot.SootField;
import soot.Body;
import soot.PatchingChain;
import soot.Unit;
import soot.RefType;
import soot.Value;
import soot.Type;
import soot.NullType;
import soot.Scene;

public class FieldTransformer extends AbstractStmtSwitch {
    private SootMethod currentlyInstrumentedMethod;
    private Body currentlyInstrumentedMethodBody;
    private PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain;
    private List<SootClass> userClasses;

    private SootClass clsMonitor;
    private SootMethod libCall;
    private SootMethod returnInto;

    public FieldTransformer(final SootMethod currentlyInstrumentedMethod,
        final Body currentlyInstrumentedMethodBody,
        final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain,
        List<SootClass> userClasses) {
            this.currentlyInstrumentedMethod = currentlyInstrumentedMethod;
            this.currentlyInstrumentedMethodBody = currentlyInstrumentedMethodBody;
            this.currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBodyUnitChain;
            this.userClasses = userClasses;

            this.clsMonitor = Scene.v().getSootClass(utils.Constants.MONITOR_CLASS);
            this.libCall = clsMonitor.getMethodByName("onLibMethodCall");
            this.returnInto = clsMonitor.getMethodByName("onLibMethodReturnNormally");
    }

    @Override
    public void caseAssignStmt(AssignStmt stmt) {

        System.out.println("\t\t\t STMT LOG: " + stmt);

        if (stmt.getLeftOp() instanceof FieldRef) { 
            System.out.println("\t\t\t FIELDREF LOG: " + stmt);

            instrumentFieldRef(currentlyInstrumentedMethodBody, ((FieldRef) stmt.getLeftOp()).getField(), stmt.getRightOp(), stmt);

            // if (stmt.getLeftOp().getType().equals(RefType.v("android.widget.Button"))) {
            //     System.out.println("\t\t\t WIDGET LOG: " + stmt);
            // }
        }
    }

    public void instrumentFieldRef(Body body, SootField field, Value value, Stmt stmt) {
        
        String invokeType = "FieldOperation";

        String methodSignature = "<abc.Field: void syntheticFieldSetter(" + field.getDeclaringClass() + "@" + field.getDeclaringClass().hashCode() + ", " + field.getType() + ", " + field.getName() + ")>";

        List<Value> parameterList = new ArrayList<Value>();

        parameterList.add(value);

        List<Unit> generatedMethodCall = new ArrayList<Unit>();
        List<Unit> generatedMethodReturn = new ArrayList<Unit>();
        
		generatedMethodCall.addAll(wrapTraceStart(invokeType, methodSignature, parameterList, body));
		generatedMethodReturn.addAll(wrapTraceEnd(methodSignature, NullConstant.v(), value, body));

		UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, generatedMethodCall);
		UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, generatedMethodReturn);
    }

	public List<Unit> wrapTraceStart(String invokeType, String methodSignature, List<Value> parameterList, Body body) {

        List<Unit> instrumentationCodeBefore = new ArrayList<Unit>();
        List<Value> onLibCallParameters = new ArrayList<Value>();

        onLibCallParameters.add(NullConstant.v());
        onLibCallParameters.add(StringConstant.v(methodSignature));
        onLibCallParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));

        Pair<Value, List<Unit>> tempArgListAndInstr = UtilInstrumenter.generateParameterArray(RefType.v("java.lang.Object"), parameterList, body);

        onLibCallParameters.add(tempArgListAndInstr.getFirst());

        instrumentationCodeBefore.addAll(tempArgListAndInstr.getSecond());

        // Scene.v().loadClassAndSupport("java.lang.System");
        // SootMethod hashCodeMethod = Scene.v().getMethod("<java.lang.System: int identityHashCode(java.lang.Object)>");
        // instrumentationCodeBefore.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(identityHashCodeMethod.makeRef(), intent)));

		final Stmt onLibCallInvocation = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(libCall.makeRef(), onLibCallParameters));

        instrumentationCodeBefore.add(onLibCallInvocation);

        return instrumentationCodeBefore;
    }

	public List<Unit> wrapTraceEnd(String methodSignature, Value ownerValue, Value returnValue, Body body) {

        List<Value> onReturnIntoParameters = new ArrayList<Value>();
        List<Unit> instrumentationCodeAfter = new ArrayList<Unit>();

        onReturnIntoParameters.add(ownerValue);
        onReturnIntoParameters.add(StringConstant.v(methodSignature));
        onReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
        onReturnIntoParameters.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody, returnValue, instrumentationCodeAfter));

        final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(returnInto.makeRef(), onReturnIntoParameters));

        instrumentationCodeAfter.add(onReturnIntoCall);

        return instrumentationCodeAfter;
	}
}
