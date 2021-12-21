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
    // TODO This code probably can be wrapped somehow
    // Class which is invoked dynamically from the inserted probes to trace the
    // execution.
    // The actual class will be choosen dynamically
    protected SootClass clsMonitor;
    // Methods of the clsMonitor class invoked by the probes
    protected SootMethod monitorInitialize;
    // Those are methods that can be called by the tests, so can be carved
    protected SootMethod monitorOnAppMethodCall;
    // Those are methods that cannot be called by the tests, so cannot be carved
    protected SootMethod monitorOnPrivateAppMethodCall;
    // Those are calls to external libs cannot be carved
    protected SootMethod monitorOnLibMethodCall;
    // Calling one of the method we fake to uniform the representation
    protected SootMethod monitorOnSyntheticMethodCall;
    //
    protected SootMethod monitorOnAppMethodReturnNormally;
    protected SootMethod monitorOnLibMethodReturnNormally;
    // Monitor when an app method exits exceptionally
    protected SootMethod monitorOnAppMethodReturnExceptionally;
    // Monitor when an app method ends exceptionally
    protected SootMethod monitorOnAppMethodThrowException;
    // Monitor when an app method capture an exception (via a trap)
    protected SootMethod monitorOnAppMethodCaptureException;

    public ArrayConstructorTransformer(final SootMethod currentlyInstrumentedMethod, //
            final Body currentlyInstrumentedMethodBody, //
            final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain, //
            List<SootClass> userClasses) {
        this.currentlyInstrumentedMethod = currentlyInstrumentedMethod;
        this.currentlyInstrumentedMethodBody = currentlyInstrumentedMethodBody;
        this.currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBodyUnitChain;
        this.userClasses = userClasses;

        initMonitorClass();
    }

    /*
     * TODO This can also be moved to another class
     */
    private void initMonitorClass() {
        try {
            // TODO At the moment this is hardcoded to Monitor(Simple Monitor)
            clsMonitor = Scene.v().getSootClass(utils.Constants.MONITOR_CLASS);
            //
            clsMonitor.setApplicationClass();
            // Make sure we include also supporting classes
            SootClass stackElementClass = Scene.v().getSootClass(StackElement.class.getName());
            stackElementClass.setApplicationClass();

            // TODO Not sure we are actually using any of those
            Scene.v().getSootClass("utils.MethodEventComparator").setApplicationClass();

            // TODO Eventually we need to get rid of this one!
            Scene.v().getSootClass("utils.logicClock").setApplicationClass();

            // Get a reference to the methods of the monitor class to inject them
            // TODO Refactor this to be simpler and more usable. Maybe some class to
            // encapsulate this weird logic...

            monitorInitialize = clsMonitor.getMethodByName("initialize");
            monitorOnAppMethodCall = clsMonitor.getMethodByName("onAppMethodCall");
            monitorOnSyntheticMethodCall = clsMonitor.getMethodByName("onSyntheticMethodCall");
            monitorOnPrivateAppMethodCall = clsMonitor.getMethodByName("onPrivateAppMethodCall");
            monitorOnLibMethodCall = clsMonitor.getMethodByName("onLibMethodCall");
            monitorOnAppMethodReturnNormally = clsMonitor.getMethodByName("onAppMethodReturnNormally");
            monitorOnLibMethodReturnNormally = clsMonitor.getMethodByName("onLibMethodReturnNormally");
            monitorOnAppMethodReturnExceptionally = clsMonitor.getMethodByName("onAppMethodReturnExceptionally");
            monitorOnAppMethodCaptureException = clsMonitor.getMethodByName("onAppMethodCaptureException");
            monitorOnAppMethodThrowException = clsMonitor.getMethodByName("onAppMethodThrowException");

        } catch (Throwable t) {
            t.printStackTrace();

            if (t instanceof RuntimeException) {
                if (((RuntimeException) t).getMessage().contains("couldn't find method ")) {
                    System.err.println("SceneInstrumenterWithMethodParameters.init() CLASS: " + clsMonitor);
                    System.err.println("\t " + clsMonitor.getMethods());
                }
            }

            throw t;
        }
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
                .newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorOnLibMethodCall.makeRef(), monitorOnLibCallParameters));
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
                Jimple.v().newStaticInvokeExpr(monitorOnLibMethodReturnNormally.makeRef(), monitorOnReturnIntoParameters));
        instrumentationCodeAfter.add(onReturnIntoCall);

        UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
                instrumentationCodeAfter);

    }
}
