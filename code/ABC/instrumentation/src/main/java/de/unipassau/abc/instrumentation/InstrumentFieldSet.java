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
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

public class FieldTransformer extends AbstractStmtSwitch {

    private String packageName;

    private SootMethod currentlyInstrumentedMethod;
    private Body currentlyInstrumentedMethodBody;
    private PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain;
    private List<SootClass> userClasses;

    private SootClass clsMonitor;
    private SootMethod monitorOnSyntheticMethodCall;
    private SootMethod monitorOnLibMethodReturnNormally;

    /**
     * This generic method invocation captures that a field, identified by its name
     * (string) of an object instance, identified by a reference, is set to a given
     * value (identified by the return value)
     */
    public final static String SIGNATURE = "<abc.Field: java.lang.Object syntheticFieldSetter(java.lang.Object,java.lang.String)>";

    public FieldTransformer(final String packageName, final SootMethod currentlyInstrumentedMethod,
            final Body currentlyInstrumentedMethodBody,
            final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain, List<SootClass> userClasses) {

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

        // If we try to log a set reference inside a constructor, then it is impossible to access "this" at least before calling super()
        if (currentlyInstrumentedMethod.isConstructor()) {
            System.out.println("DEBUG: Skip constructor instrumentation");
            return;
        }

        // Cannot currently deal with field on right side of assignment
        if (stmt.getRightOp() instanceof FieldRef) {
            System.out.println("DEBUG: Skip field on right side of assignment");
            return;
        } 

        // Begin instrumentation
        if (stmt.getLeftOp() instanceof FieldRef) {
        	System.out.println("\t\t\t WRAP FieldRef assignemnt " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());
            instrumentFieldRef(stmt, stmt.getRightOp());
        }

        
    }

    public void instrumentFieldRef(AssignStmt stmt, Value value) {

        // Identify static fields, set these to a null Value
        Value leftSideOfFieldAssignment = null;
        if (stmt.getLeftOp() instanceof InstanceFieldRef) {
            InstanceFieldRef left = (InstanceFieldRef) stmt.getLeftOp();
            leftSideOfFieldAssignment = left.getBase();
        } else if (stmt.getLeftOp() instanceof StaticFieldRef) {
            leftSideOfFieldAssignment = NullConstant.v();
        }

        // This will be the return value
        Value rightSide = stmt.getRightOp();

        // Extract field name
        Value fieldName = StringConstant.v(((FieldRef) stmt.getLeftOp()).getField().getName());

        // Holds synthetic method parameters, to be passed to monitor class
        List<Value> parameterList = new ArrayList<Value>();

        parameterList.add(leftSideOfFieldAssignment);
        parameterList.add(fieldName);

        List<Unit> generatedMethodCall = new ArrayList<Unit>();
        List<Unit> generatedMethodReturn = new ArrayList<Unit>();

        // Generate units to be injected
        generatedMethodCall.addAll(wrapTraceStart(parameterList));
        generatedMethodReturn.addAll(wrapTraceEnd(rightSide));

        // Instrument generated units
        UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, generatedMethodCall);
        UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, generatedMethodReturn);
    }

    // Generate Units for method call
    public List<Unit> wrapTraceStart(List<Value> parameterList) {

        List<Unit> instrumentationCodeBefore = new ArrayList<Unit>();
        List<Value> methodCallParameters = new ArrayList<Value>();

        // Add actual parameters for monitor class method
        methodCallParameters.add(StringConstant.v(packageName));
        methodCallParameters.add(NullConstant.v());
        methodCallParameters.add(StringConstant.v(SIGNATURE));

        // Correctly wrap primitive types, generate appropriate Units
        Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter.generateParameterArray(RefType.v("java.lang.Object"), parameterList, currentlyInstrumentedMethodBody);

        // Include synthetic method parameters
        methodCallParameters.add(tmpArgsListAndInstructions.getFirst());

        instrumentationCodeBefore.addAll(tmpArgsListAndInstructions.getSecond());

        // Create invocation monitor class function
        final Stmt callMethodStart = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorOnSyntheticMethodCall.makeRef(), methodCallParameters));

        instrumentationCodeBefore.add(callMethodStart);

        return instrumentationCodeBefore;
    }

    // This is nearly identical to the above function; generates return method invocation instead
    public List<Unit> wrapTraceEnd(Value rightSide) {

        List<Unit> instrumentationCodeAfter = new ArrayList<>();
        List<Value> methodReturnParameters = new ArrayList<Value>();

        methodReturnParameters.add(NullConstant.v());
        methodReturnParameters.add(StringConstant.v(SIGNATURE));
        methodReturnParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));

        Pair<Value, List<Unit>> tmpReturnValueAndInstructions = UtilInstrumenter.generateReturnValue(rightSide, currentlyInstrumentedMethodBody);

        methodReturnParameters.add(tmpReturnValueAndInstructions.getFirst());

        instrumentationCodeAfter.addAll(tmpReturnValueAndInstructions.getSecond());

        final Stmt callMethodReturn = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorOnLibMethodReturnNormally.makeRef(), methodReturnParameters));

        instrumentationCodeAfter.add(callMethodReturn);

        return instrumentationCodeAfter;
    }
}
