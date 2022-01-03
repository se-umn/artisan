/**
 * File: src/dynCG/icgInst.java
 * -------------------------------------------------------------------------------------------
 * Date			Author      Changes
 * -------------------------------------------------------------------------------------------
 * 10/19/15		hcai		created; for profiling function calls and intent-based ICCs 
 * 10/26/15		hcai		added ICC monitoring with time-stamping call events
 * 02/14/17		hcai		added the option of event tracking
 * 02/15/17		hcai		first working version with event tracking
 * 04/22/17		hcai		added instrumentation for tracking reflection-called method 
 */
package de.unipassau.abc.instrumentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.graphstream.ui.graphicGraph.stylesheet.parser.StyleSheetParserTokenManager;
import org.xmlpull.v1.XmlPullParserException;

import de.unipassau.abc.data.Pair;
import dev.navids.soottutorial.visual.Visualizer;
import edu.emory.mathcs.backport.java.util.Collections;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.ConstantValueToInitializerTransformer;
import soot.util.Chain;
import utils.Constants;

/**
 * TODO Replace: opts.debugOut() with a proper logger
 *
 * TODO Update the logic of Monitor to correctly report and handle exceptions.
 * Now the logic is different: - App methods can exit normally or exceptionally
 * - LibCalls are more tricky we need to match them with "open" methods? -
 * 
 * Monitor.
 * 
 * TODO Rename this class!
 *
 * @author gambi
 *
 */
public class SceneInstrumenterWithMethodParameters extends SceneTransformer {

    // MOVE THIS INTO A SEPARATE CLAS
    public final static String INTENT_TAINT_TAG = "INTENT_TAINT_TAG";

    private static final boolean MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT = System.getProperties()
            .containsKey("abc.make.android.lifecycle.events.explicit");

    // TODO Consider distribute the instrumentation logic across several
    // workers/tasks to be registered to soot
    private static final boolean TAINT_ANDROID_INTENTS = System.getProperties()
            .containsKey("abc.taint.android.intents");

    private static final boolean OUTPUT_INSTRUMENTED_CODE = System.getProperties()
            .containsKey("abc.output.instrumented.code");

    private static final boolean INSTRUMENT_ARRAY_OPERATIONS = System.getProperties()
            .containsKey("abc.instrument.array.operations");

    private static final boolean INSTRUMENT_FIELDS_OPERATIONS = System.getProperties()
            .containsKey("abc.instrument.fields.operations");

    private static final boolean INSTRUMENT_TRAPS = System.getProperties().containsKey("abc.instrument.traps");

    // Additional inputs that cannot be easily passed using command line
    private static final String INCLUDE_FILTER = System.getProperty("abc.instrument.include", "");

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

    private List<SootClass> userClasses;

    protected File fJimpleOrig = null;
    protected File fJimpleInsted = null;

    private String appPackageName;

    private List<Pattern> packageFilters = new ArrayList<Pattern>();

    // This class is the usual <package-name>.R class that contains many static
    // values for an android application
    private SootClass rIdClass;

    private boolean DEBUG = System.getProperties().containsKey("abc.instrument.debug");
    private boolean CONSIDER_MULTIPLE_THREADS = System.getProperties().containsKey("abc.instrument.multithreaded");

    protected static boolean bProgramStartInClinit = false;
//	protected static Options opts = new Options();

    // whether instrument in 3rd party code such as android.support.v$x
    public static boolean g_instr3rdparty = false;

    public SceneInstrumenterWithMethodParameters(String appPackageName) {
        /*
         * At this moment, soot did not run so there's no class loaded into the
         * Scene.v()
         */
        this.appPackageName = appPackageName;
    }

    public void setPackageFilters(List<String> packageFilters) {
        if (packageFilters != null) {
            packageFilters.forEach(s -> this.packageFilters.add(Pattern.compile(s)));
        }
    }

    public void setDebug(boolean debug) {
        DEBUG = debug;
    }

    public void setMultiThreadedExecution(boolean considerMultipleThreads) {
        this.CONSIDER_MULTIPLE_THREADS = considerMultipleThreads;
    }

    /**
     * This is the method invoked by Soot
     */
    @Override
    protected void internalTransform(String arg0, Map<String, String> arg1) {
        // TODO This might be improved:
        System.out.println("SceneInstrumenterWithMethodParameters.internalTransform() RUNNING ");
        this.run();

    }

    public void run() {
        g_instr3rdparty = false;

        loadUserClasses();

        initMonitorClass();

        // We can include additional calls here and make sure they will appear in the
        // trace
        if (TAINT_ANDROID_INTENTS) {
            taintAndroidIntents();
        }

        extractRClass();

        try {
            instrument();
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }

    }

    private void extractRClass() {
        System.out.println("---- SceneInstrumenterWithMethodParameters.extractRClass() ----");

        Iterator<SootClass> applicationClassesIterator = userClasses.iterator();
        if (userClasses.size() == 0) {
            System.out.println("\n\n NO USER CLASSES DEFINED !");
            return;
        }

        while (applicationClassesIterator.hasNext()) {
            SootClass currentlyInstrumentedSootClass = (SootClass) applicationClassesIterator.next();
            if (currentlyInstrumentedSootClass.getName().contains("R$id")) {
                System.out.println("SceneInstrumenterWithMethodParameters.extractRClass() FOUND "
                        + currentlyInstrumentedSootClass);
                rIdClass = currentlyInstrumentedSootClass;
                break;
            }
        }

    }

    /**
     * For each intent that is creates include an additional putExtra/getExtra with
     * the following KEYWORD: ABC_INTENT_ID to map the intents across invocations
     */
    public void taintAndroidIntents() {
        // TODO Move it to a separate class?

        System.out.println("---- SceneInstrumenterWithMethodParameters.taintAndroidIntents() ----");

        Iterator<SootClass> applicationClassesIterator = userClasses.iterator();
        if (userClasses.size() == 0) {
            System.out.println("\n\n NO USER CLASSES DEFINED !");
        }

        while (applicationClassesIterator.hasNext()) {

            SootClass currentlyInstrumentedSootClass = (SootClass) applicationClassesIterator.next();

            if (skipClass(currentlyInstrumentedSootClass)) {
                continue;
            }

            /*
             * Traverse all methods of this class and check whether or not a new Intent is
             * created
             */
            Iterator<SootMethod> methodsIterator = currentlyInstrumentedSootClass.getMethods().iterator();
            while (methodsIterator.hasNext()) {

                SootMethod currentlyInstrumentedMethod = (SootMethod) methodsIterator.next();

                if (skipMethod(currentlyInstrumentedMethod)) {
                    continue;
                }

                /*
                 * Forces soot to (re)load the entire class body using retrieveActiveBody()
                 */
                final Body currentlyInstrumentedMethodBody = currentlyInstrumentedMethod.retrieveActiveBody();

                /*
                 * Go over the units of the method looking for Intent instantiations
                 */

                SootClass intentClass = Scene.v().getSootClass("android.content.Intent");

                SootMethod putExtraIntMethod = intentClass
                        .getMethod("android.content.Intent putExtra(java.lang.String,int)");
                SootMethod getExtraIntMethod = intentClass.getMethod("int getIntExtra(java.lang.String,int)");

                Scene.v().loadClassAndSupport("java.lang.System");
                SootMethod identityHashCodeMethod = Scene.v()
                        .getMethod("<java.lang.System: int identityHashCode(java.lang.Object)>");

                UnitPatchingChain currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBody.getUnits();
                for (final Iterator<Unit> iter = currentlyInstrumentedMethodBodyUnitChain.snapshotIterator(); iter
                        .hasNext();) {

                    final Unit currentUnit = iter.next();

                    currentUnit.apply(new AbstractStmtSwitch() {

                        @Override
                        public void caseInvokeStmt(InvokeStmt stmt) {
                            InvokeExpr invocation = stmt.getInvokeExpr();
                            // Probably wont work if we use a subclass of intent?
                            if (invocation.getMethod().isConstructor()) {

                                Value intent = ((SpecialInvokeExpr) invocation).getBase();

                                if (intent.getType().equals(intentClass.getType())) {

                                    System.out.println(" Found Instantiation of Intent " + intent + "at "
                                            + currentlyInstrumentedMethod);

                                    List<Unit> instrumentationCode = new ArrayList<>();

                                    Local intentHashCode = UtilInstrumenter
                                            .generateFreshLocal(currentlyInstrumentedMethodBody, IntType.v());

                                    instrumentationCode.add(Jimple.v().newAssignStmt(intentHashCode,
                                            Jimple.v().newStaticInvokeExpr(identityHashCodeMethod.makeRef(), intent)));
//
                                    instrumentationCode.add(Jimple.v().newInvokeStmt(//
                                            Jimple.v().newVirtualInvokeExpr(//
                                                    (Local) intent, //
                                                    putExtraIntMethod.makeRef(), //
                                                    StringConstant.v(INTENT_TAINT_TAG), //
                                                    intentHashCode)));
//
                                    UtilInstrumenter.instrumentAfter(currentlyInstrumentedMethodBodyUnitChain, stmt,
                                            instrumentationCode);
                                }
                            }
                        }

                        @Override
                        public void caseAssignStmt(AssignStmt stmt) {
                            /*
                             * If an Intent is assigned as a result of a method invocation, we make sure to
                             * trace the call as this is an Intent definition (use)
                             */

                            Value leftSide = stmt.getLeftOp();

                            if (leftSide.getType().equals(intentClass.getType()) && stmt.containsInvokeExpr()) {

                                List<Unit> instrumentationCode = new ArrayList<>();

                                InvokeExpr intentDefinition = stmt.getInvokeExpr();
                                System.err.println(" Found Assignment of Intent from " + intentDefinition);

                                instrumentationCode.add(Jimple.v().newInvokeStmt(//
                                        Jimple.v().newVirtualInvokeExpr(//
                                                (Local) leftSide, //
                                                getExtraIntMethod.makeRef(), //
                                                StringConstant.v(INTENT_TAINT_TAG), IntConstant.v(-1))));

                                UtilInstrumenter.instrumentAfter(currentlyInstrumentedMethodBodyUnitChain, stmt,
                                        instrumentationCode);

                            }
                        }
                    });
                }
            }
        }

    }

    /**
     * Descendants may want to use customized event monitors
     */
    protected void initMonitorClass() {
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

            /*
             * To configure the clsMonitor class we directly change its code and set the
             * value of the variables as defined by the Java properties passed to this class
             */

            /*
             * Since we know those are static fields, we need to capture the moment of their
             * allocation. However, those are not primitives, so to set their value we need
             * to go the long way: create a local variable, and assign that to the fields in
             * the class initializer
             */
            SootMethod staticInitializer = clsMonitor.getMethodByName("<clinit>");

            final Body staticInitializerMethodBody = staticInitializer.retrieveActiveBody();
            UnitPatchingChain staticInitializerMethodBodyUnitChain = staticInitializerMethodBody.getUnits();
            for (final Iterator<Unit> iter = staticInitializerMethodBodyUnitChain.snapshotIterator(); iter.hasNext();) {
                final Unit currentUnit = iter.next();

                System.out.println(currentUnit);
                currentUnit.apply(new AbstractStmtSwitch() {

                    @Override
                    public void caseAssignStmt(AssignStmt stmt) {
                        Local booleanLocal = null;
                        SootClass sootClass = Scene.v().getSootClass("java.lang.Boolean");
                        SootMethod valueOfMethod = sootClass.getMethod("java.lang.Boolean valueOf(boolean)");
                        StaticInvokeExpr staticInvokeExpr = null;
                        Unit newAssignStmt = null;
                        if (stmt.getLeftOp() instanceof StaticFieldRef) {
                            StaticFieldRef ref = (StaticFieldRef) stmt.getLeftOp();
                            final String fieldName = ref.getField().getName();
                            switch (fieldName) {
                            case "DEBUG":

                                // Add a new local
                                booleanLocal = UtilInstrumenter.generateFreshLocal(staticInitializerMethodBody,
                                        RefType.v("java.lang.Boolean"));
                                staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(),
                                        IntConstant.v((DEBUG ? 1 : 0)));
                                newAssignStmt = Jimple.v().newAssignStmt(booleanLocal, staticInvokeExpr);
                                UtilInstrumenter.instrumentBeforeWithAndTag(staticInitializerMethodBodyUnitChain, stmt,
                                        Collections.singletonList(newAssignStmt));
                                //
                                stmt.setRightOp(booleanLocal);
                                System.out.println("Setting Monitoring field to " + stmt + " - " + DEBUG);
                                break;
                            case "REPORT_ONLY_MAIN_THREAD":
                                // Note that this is the negation!
                                // Add a new local
                                booleanLocal = UtilInstrumenter.generateFreshLocal(staticInitializerMethodBody,
                                        RefType.v("java.lang.Boolean"));
                                staticInvokeExpr = Jimple.v().newStaticInvokeExpr(valueOfMethod.makeRef(),
                                        IntConstant.v((CONSIDER_MULTIPLE_THREADS ? 0 : 1)));
                                newAssignStmt = Jimple.v().newAssignStmt(booleanLocal, staticInvokeExpr);
                                UtilInstrumenter.instrumentBeforeWithAndTag(staticInitializerMethodBodyUnitChain, stmt,
                                        Collections.singletonList(newAssignStmt));
                                //
                                stmt.setRightOp(booleanLocal);
                                System.out.println(
                                        "Setting Monitoring field to " + stmt + " - " + CONSIDER_MULTIPLE_THREADS);
                                break;
                            default:
                                break;
                            }
                        }
                        super.caseAssignStmt(stmt);
                    }
                });

            }

            /*
             * void handleAssign(DefinitionStmt stmt) { Value lval = stmt.getLeftOp(); Value
             * rval = stmt.getRightOp(); Variable rvar; if (lval instanceof Local) { rvar =
             * getLocalVariable((Local)lval); } else { rvar = jt.makeVariable(rval); }
             * et.translateExpr(rvar, stmt.getRightOpBox()); if (lval instanceof ArrayRef) {
             * notSupported("We do not support arrays"); } else if (lval instanceof
             * FieldRef) { notSupported("We do not support field references"); } }
             * 
             */

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
     * Instrumentation:</br>
     * <ul>
     * <li>inject onMethodEntry at the beginning of application methods</li>
     * </li>inject onMethodEntry at the before each non-application (lib, android)
     * method</li>
     * <li>inject onReturnInfo after each invoked method</li>
     * <li>TODO Handle Exceptions</li>
     * </ul>
     *
     * We first analyze the body of the method and ensure we wrap it with a
     * try/catch/finally to capture unhandled exceptions. This step must ensure that
     * we do not tamper with the existing traps mechanisms: that is, we do not have
     * to violate the method signature making it impossible to throw explicit
     * exceptions. Capturing the entire method body + traps within an BIG
     * try/catch(Throwable t){ throw t; } which simply rethrows the throwable
     * captured should not change the semantic. So eventually we need to create a
     * new trap after the LAST trap/unit of the body and a new EXIT point. Actually,
     * this will be the SOLE "exceptional" exit point of the method, so how do we
     * distinguish whether or not the exception here was expected (matching the
     * declared ones) or unexpected? Do we care? In theory, we do not for the sake
     * of the current method as this results in a method that ends exceptionally in
     * any case.
     * 
     * TODO: How do we handle the existing finally blocks?
     * 
     * 
     * We instrument the methods' start > monitorAppMethodStart
     * 
     * We instrument the methods' body (libCall only!) > monitorLibMethodStart >
     * monitorLibMethodEnd
     * 
     * We instrument the methods' traps TODO - We need this to track the exceptions.
     * For each trap we simply notify the monitor about the captured exception. This
     * includes also the "outer" catch-all trap However, this changes the definition
     * of the traps (begin->end), so if there are "chained traps", and they are
     * always there because of step #1, we need to ensure we are correctly shifting
     * them! TODO > monitorAppMethodCapturException
     * 
     * Finally, we instrument the methods' ends:- Those should be a set of return
     * stmt - The ONLY throw statement in the outer Trap >
     * monitorAppMethodExitNormally > monitorAppMethodExitExceptionally
     *
     *
     * @throws XmlPullParserException
     * @throws IOException
     */

    public void instrument() throws IOException, XmlPullParserException {

        System.out.println("---- SceneInstrumenterWithMethodParameters.instrument() ----");
        System.out.println("Options.");
        // TODO Reorganize how we handle options and command line argumentes
        System.out.println("OUTPUT_INSTRUMENTED_CODE: " + this.OUTPUT_INSTRUMENTED_CODE);
        System.out.println("MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT: " + this.MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT);
        System.out.println("INSTRUMENT_ARRAY_OPERATIONS: " + this.INSTRUMENT_ARRAY_OPERATIONS);
        System.out.println("INSTRUMENT_FIELD_OPERATIONS: " + this.INSTRUMENT_FIELDS_OPERATIONS);
        System.out.println("INSTRUMENT_TRAPS: " + this.INSTRUMENT_TRAPS);

        Iterator<SootClass> applicationClassesIterator = userClasses.iterator();
        if (userClasses.size() == 0) {
            System.out.println("\n\n NO USER CLASSES DEFINED !");
        }

        while (applicationClassesIterator.hasNext()) {

            SootClass currentlyInstrumentedSootClass = (SootClass) applicationClassesIterator.next();

            if (skipClass(currentlyInstrumentedSootClass)) {
                System.out.println("\n\n SKIPPING CLASS " + currentlyInstrumentedSootClass.getName());
                System.out.println("\n\n");
                continue;
            } else {
                System.out.println("\n\n INSTRUMENTING CLASS " + currentlyInstrumentedSootClass.getName());
            }

            if (MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT) {
                /*
                 * Create dummy methods about android life-cycle so they can be logged if
                 * necessary
                 */
                System.out.println("\t MAK ANDROID EVENT EXPLICITY:");
                makeAndroidActivityEventsExplicit(currentlyInstrumentedSootClass);
            }

            // Process fields and make sure there's getter and setter attached to them as
            // syntetic operations

            /*
             * Traverse all methods of this class at include the monitor initialize and the
             * onMethodEnter for application methods
             */
            Iterator<SootMethod> methodsIterator = currentlyInstrumentedSootClass.getMethods().iterator();
            while (methodsIterator.hasNext()) {

                SootMethod currentlyInstrumentedMethod = (SootMethod) methodsIterator.next();

                System.out.println("\n\t METHOD: " + currentlyInstrumentedMethod.getSignature());

                if (skipMethod(currentlyInstrumentedMethod)) {
                    System.out.println("\t\t SKIPPED");
                    continue;
                }
                /*
                 * Forces soot to (re)load the entire class body using retrieveActiveBody()
                 */
                Body currentlyInstrumentedMethodBody = currentlyInstrumentedMethod.retrieveActiveBody();

                System.out.println("\t INSTRUMENTING ");
                for (Unit u : currentlyInstrumentedMethodBody.getUnits()) {
                    System.out.println("\t\t " + u);
                }

                /*
                 * Global variables
                 */
                PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBody
                        .getUnits();

                /*
                 * Be sure that for every trap there's a call to returnFromException and the
                 * other methods. Do it before wrapTryCatchAllBlocks and the other methods in
                 * order to avoid logging the exception twice?
                 */
                // Wrap the entire method in a big try/catch/re-throw
                // TODO HOW DO WE HANDLE FINAL BLOCKS?
                System.out.println("\t WRAP METHOD INSIDE TRY-CATCH-ALL");
                wrapTryCatchAllBlocks(currentlyInstrumentedMethod);

                // TODO - Instrument FINAL blocks?
                // boolean hasFinally =
                // getFinally(currentlyInstrumentedMethodBody.toString());

                /*
                 * To Ensure we log app methods being called we insert an methodEnter probe at
                 * the beginning of the currentlyInstrumentedMethod. Private methods are tagged
                 * as such
                 */
                System.out.println("\t INSTRUMENTING METHOD ENTRY POINT");
                instumentMethodBegins(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
                        currentlyInstrumentedMethodBodyUnitChain);

                /*
                 * Instrument the all calls to methods that do not belong to the app, including
                 * thoses inside trap handlers !
                 * 
                 * TODO Does this instrument also trap handlers' body ?!
                 * 
                 */
                System.out.println("\t INSTRUMENTING METHOD BODY");
                instrumentMethodBody(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
                        currentlyInstrumentedMethodBodyUnitChain);

                /*
                 * Inject code that store the thrown exception. So we log the fact that the
                 * method is about to throw an exception before possibly logging it will end
                 * exceptionally
                 * 
                 */
                System.out.println("\t INSTRUMENTING THROWS");
                instrumentMethodThrows(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
                        currentlyInstrumentedMethodBodyUnitChain);

                /*
                 * Inject instrumentation code before any return/throw statement in the method.
                 */

                System.out.println("\t INSTRUMENTING METHOD EXIT-POINTS");
                instrumentMethodEnds(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
                        currentlyInstrumentedMethodBodyUnitChain);

                /*
                 * Inject instrumentation code after capturing an exception
                 */
                System.out.println("\t INSTRUMENTING TRAPS");
                instrumentMethodTraps(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
                        currentlyInstrumentedMethodBodyUnitChain);

                /*
                 * To ensure monitor is initialized we inject initialization code. Note that we
                 * apply this later because we need to put this code at the very beginning of
                 * the method (possibly BEFORE the methodBegins probe!). THIS IS NEED IF THERE"S
                 * CLINIT IN THE ENTRY CLASSES< BUT WE DO NOT HAVE ENTRY CLASSES SOMEHOW ?
                 */

                // int nCurCallsite = -> TODO Not sure how to use this and why
                // we care...
                /*
                 * TODO CFG is useful because it carries a lot of info, but it also skip some,
                 * e.g., it does not consider call sites inside method of abstract classes which
                 * can be actually invoked. So we resort going through the body of the method
                 * and doing stuff manually, Probably we can get rid of this entire DuafDroid
                 * thingy ...
                 *
                 */
                // instrumentCallSites(currentlyInstrumentedMethod,
                // currentlyInstrumentedMethodBody,
                // currentlyInstrumentedMethodBodyUnitChain);

                // Use plain old soot instrumentation

                /*
                 * TODO: To Ensure we log app when the app is terminated using System.exit we
                 * insert a probe before calling System.exit
                 */
                // injectTerminationCode(cfg,
                // currentlyInstrumentedSootMethod,
                // currentlyInstrumentedMethodBodyUnitChain);

                // PRINT THE INSTRUMENTED CODE
                if (OUTPUT_INSTRUMENTED_CODE) {
                    System.out.println("\n\t INSTRUMENTED");
                    // Print the units of this method
                    for (Unit u : currentlyInstrumentedMethodBody.getUnits()) {
                        System.out.println((u.getTags().contains(ABCTag.TAG) ? "**" : "") + "\t\t " + u);
                    }
                }

            } // -- while (meIt.hasNext())

        } // -- while (clsIt.hasNext())

    } // --

    /**
     * Go over the method body and ensures that every throw statement is captured
     * and the monitor is notified about it
     * 
     * @param currentlyInstrumentedSootMethod
     * @param currentlyInstrumentedMethodBody
     * @param currentlyInstrumentedMethodBodyUnitChain
     */
    private void instrumentMethodThrows(final SootMethod currentlyInstrumentedSootMethod, //
            final Body currentlyInstrumentedMethodBody, //
            final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain) {

        for (final Iterator<Unit> iter = currentlyInstrumentedMethodBodyUnitChain.snapshotIterator(); iter.hasNext();) {
            // Consider also our code since we include a wrap all re-throw and we need that
            // !
            final Unit currentUnit = iter.next();

            /*
             * Instrument the throw clause by inserting a call to monitor right before it.
             * Note that this might follow the call to returnExceptionally that we already
             * added
             */
            currentUnit.apply(new AbstractStmtSwitch() {

                @Override
                public void caseThrowStmt(ThrowStmt stmt) {
                    System.out.println("\t\t\t Instrumenting throw " + stmt);

                    // Wrap the value to be returned into a new object
                    // local and pass it to the monitor
                    List<Unit> instrumentationCode = new ArrayList<>();

                    // Prepare the call to the monitor
                    List<Value> throwCapturedException = new ArrayList<>();

                    //
                    if (currentlyInstrumentedSootMethod.isStatic() || currentlyInstrumentedSootMethod.isConstructor()) {
                        throwCapturedException.add(NullConstant.v());
                    } else {
                        throwCapturedException.add(currentlyInstrumentedMethodBody.getThisLocal());
                    }
                    //

                    final String currentlyInstrumentedMethodSignature = currentlyInstrumentedSootMethod.getSignature();
                    throwCapturedException.add(StringConstant.v(currentlyInstrumentedMethodSignature));
                    // Context
                    throwCapturedException.add(StringConstant.v(currentlyInstrumentedMethodSignature));

                    // Pass the exception instead of the return value or null
                    // ((RefType) ts.getOp().getType()).getSootClass()
                    throwCapturedException.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
                            stmt.getOp(), instrumentationCode));

                    Stmt throwCapturedExceptionCall = Jimple.v().newInvokeStmt(Jimple.v()
                            .newStaticInvokeExpr(monitorOnAppMethodThrowException.makeRef(), throwCapturedException));

                    instrumentationCode.add(throwCapturedExceptionCall);

                    UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
                            instrumentationCode);
                }
            });
        }

    }

    /**
     * When a catch block gets triggered some method raised an exception, so we
     * simply record that this method captured such an exception. Because we are
     * potentially changing the trap handler (first unit) we must ensure that the
     * trap definitino is correctly updated
     * 
     */
    private void instrumentMethodTraps(SootMethod currentlyInstrumentedSootMethod,
            /*
             * Need to generate fresh locals and access other elements like "this.local" -
             * TODO Is this.local the method owner or the method itself?
             */
            Body currentlyInstrumentedMethodBody,
            /*
             * Need to inject the code inside the currentlyInstrumentedMethod - TODO Can't
             * we simply get a new instance of this or shall we pass it around ?
             */
            PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain

    ) throws IOException, XmlPullParserException {

        Chain<Trap> traps = currentlyInstrumentedMethodBody.getTraps();
        for (Trap t : traps) {

            System.out.println("\t\t\t Instrumenting trap " + t);
            List<Unit> instrumentationCode = new ArrayList<>();

            // Assign the caught exception to a local
            SootClass caughtExceptionSootClass = t.getException();
            RefType caughtExceptionRefType = RefType.v(caughtExceptionSootClass);
            Local exceptionToBePassedToMonitor = UtilInstrumenter.generateFreshLocal(currentlyInstrumentedMethodBody,
                    caughtExceptionRefType);

            // This corresponds to the @caughtexception, e.g., "r5 := @caughtexception"
            Unit handlerUnit = t.getHandlerUnit();
            // Extract the local of this unit
            Value caughtException = ((IdentityStmt) handlerUnit).getLeftOp();
            // Assign the @caughtexception to a new Local TODO Really necessary ?!
            Stmt assignException = Jimple.v().newAssignStmt(exceptionToBePassedToMonitor, caughtException);

            instrumentationCode.add(assignException);

            // Wrap the value to be returned into a new object
            // local and pass it to the monitor

            // Prepare the call to the monitor
            List<Value> onAppMethodCaptureException = new ArrayList<>();

            // Method Owner
            if (currentlyInstrumentedSootMethod.isStatic() || currentlyInstrumentedSootMethod.isConstructor()) {
                onAppMethodCaptureException.add(NullConstant.v());
            } else {
                onAppMethodCaptureException.add(currentlyInstrumentedMethodBody.getThisLocal());
            }
            final String currentlyInstrumentedMethodSignature = currentlyInstrumentedSootMethod.getSignature();
            // Signature
            onAppMethodCaptureException.add(StringConstant.v(currentlyInstrumentedMethodSignature));
            // Context
            onAppMethodCaptureException.add(StringConstant.v(currentlyInstrumentedMethodSignature));

            // Exception - THIS REQUIRES A VALUE AND NOT A LOCAL ?
            onAppMethodCaptureException.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
                    caughtException, instrumentationCode));

            Stmt onMonitorAppMethodCaptureExceptionCall = Jimple.v().newInvokeStmt(Jimple.v()
                    .newStaticInvokeExpr(monitorOnAppMethodCaptureException.makeRef(), onAppMethodCaptureException));

            instrumentationCode.add(onMonitorAppMethodCaptureExceptionCall);

            // Add the code right after the handler unit so we do not change the definion of
            // the trap...
            UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain,
                    (IdentityStmt) handlerUnit, instrumentationCode);
        }

    }

    /**
     * Gets/creates collection of all classes defined in user code --- classes
     * having name prefix matching the package name of the APK
     */
    public void loadUserClasses() {

        userClasses = new ArrayList<SootClass>();
        for (Iterator itCls = Scene.v().getApplicationClasses().snapshotIterator(); itCls.hasNext();) {
            // speculatively skip non-app classes
            SootClass curcls = (SootClass) itCls.next();

            if (curcls.getName().contains(appPackageName)) {
                userClasses.add(curcls);
            } else if (curcls.getName().startsWith(INCLUDE_FILTER)) {
                userClasses.add(curcls);
            }

        }
    }

    /**
     * Ensure that we can log "interesting" life cycle event. We can log all the
     * events, but we limit ourselves to the one we can trigger during testing using
     * ActivityController by Robolectrics
     *
     * @param currentlyInstrumentedSootClass
     */
    private static final List<String> interestingAndroidLifecycleCallbacks = Arrays.asList(
            new String[] { "onCreate", "onDestroy", "onPause", "onPostCreate", "onRestart", "onRestoreInstanceState",
                    "onFreeze", "onResume", "onSaveInstanceState", "onStart", "onStop", "onUserLeaving" });

    private static final List<String> interestingFragmentLifecycleCallbacks = Arrays.asList(new String[] {
            // Setup and Creation
            "onAttach", "onCreate", "onCreateView", "onActivityCreated", "onStart", "onResume",
            // Back navigation, or removed/replace, or fragment added to back
            // and then removed/replace
            "onPause", "onStop", "onDestroyView", "onDestroy", "onDetach",
            /// What about additional stuff like onCreateDialog ?
    });

    /**
     * This method takes all the interesting events a class extending Android
     * Activity has and make them explicit such that they appear in the trace. Those
     * methods simply call they super method.
     * 
     * TODO: This is focused on Activities, but fragments and the like should be
     * logged as well
     * 
     * This explicitly tags the methods as synthetic so it is clear we introduced
     * them
     * 
     * @param currentlyInstrumentedSootClass
     */
    private void makeAndroidActivityEventsExplicit(SootClass currentlyInstrumentedSootClass) {
        // TODO THis method can be refactored as it seems replicating the same code for
        // each "interesting class"

        // TODO Do we need to extend this list?
        SootClass androidActivity = Scene.v().getSootClass("android.app.Activity");
        SootClass androidFragment = Scene.v().getSootClass("android.support.v4.app.Fragment");

        if (Scene.v().getActiveHierarchy().isClassSubclassOf(currentlyInstrumentedSootClass, androidActivity)) {
            /*
             * For each method in androidActivity which starts with "on" we check whether
             * currentlyInstrumentedSootClass overrides it or not. If not we create a
             * synthetic method which simply calls super...
             */
            for (SootMethod androidActivityMethod : androidActivity.getMethods()) {

                if (interestingAndroidLifecycleCallbacks.contains(androidActivityMethod.getName())
                        && !androidActivityMethod.isFinal() && !androidActivityMethod.isPrivate()) {

                    if (!currentlyInstrumentedSootClass.declaresMethod(androidActivityMethod.getSubSignature())) {

                        /*
                         * Lookup the hierarchy until we find the one class implementing this method and
                         * use that as super for the call... Worst case is the AndroidActivity class
                         * itself
                         */
                        SootClass superClassActuallyImplementingTheMethod = getSuperClassActuallyImplementingMethod(
                                currentlyInstrumentedSootClass, androidActivityMethod);

                        assert superClassActuallyImplementingTheMethod != null : "Cannot find original class declaring "
                                + androidActivityMethod;

                        /*
                         * This is the actual method to call
                         */
                        SootMethod theSuperClassMethod = superClassActuallyImplementingTheMethod
                                .getMethod(androidActivityMethod.getSubSignature());

                        if (theSuperClassMethod.isFinal() || theSuperClassMethod.isPrivate()) {
                            continue;
                        }

                        System.out.println("\t\t\t Override " + androidActivityMethod + " from "
                                + superClassActuallyImplementingTheMethod);
                        /*
                         * Create the override method
                         */
                        SootMethod method = new SootMethod(theSuperClassMethod.getName(),
                                theSuperClassMethod.getParameterTypes(), theSuperClassMethod.getReturnType());
                        // Use the same modifiers as super
                        method.setModifiers(theSuperClassMethod.getModifiers());
                        // Tag the method as synthetic
                        method.addTag(SyntheticCallTag.TAG);
                        // Inject the method in the class
                        currentlyInstrumentedSootClass.addMethod(method);

                        /*
                         * Implement the override method by calling the corresponding super method via a
                         * specialinvoke and handling its return type
                         */
                        JimpleBody body = Jimple.v().newBody(method);
                        method.setActiveBody(body);
                        PatchingChain<Unit> units = body.getUnits();
                        // This is an instance method (I hope !)

                        // Generate the identityStmt for this
                        Local thisLocal = UtilInstrumenter.generateFreshLocal(body,
                                currentlyInstrumentedSootClass.getType());
                        units.add(Jimple.v().newIdentityStmt(thisLocal,
                                Jimple.v().newThisRef(currentlyInstrumentedSootClass.getType())));

                        // Generate the identityStmt for the parameters
                        List<Local> parameters = new ArrayList<>();
                        for (int number = 0; number < method.getParameterTypes().size(); number++) {
                            Type type = method.getParameterTypes().get(number);
                            Local parameterLocal = UtilInstrumenter.generateFreshLocal(body, type);
                            units.add(Jimple.v().newIdentityStmt(parameterLocal,
                                    Jimple.v().newParameterRef(type, number)));
                            parameters.add(parameterLocal);
                        }

                        if (theSuperClassMethod.getReturnType() instanceof VoidType) {
                            // Capture the return value ?
                            units.add(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(thisLocal,
                                    theSuperClassMethod.makeRef(), parameters)));
                            units.add(Jimple.v().newReturnVoidStmt());
                        } else {
                            // Store returnValue into a local
                            Local returnValue = UtilInstrumenter.generateFreshLocal(body,
                                    theSuperClassMethod.getReturnType());
                            // AssignStmt
                            units.add(Jimple.v().newAssignStmt(returnValue, Jimple.v().newSpecialInvokeExpr(thisLocal,
                                    theSuperClassMethod.makeRef(), parameters)));
                            // Call super passing the parameters
                            units.add(Jimple.v().newReturnStmt(returnValue));
                        }
                    }
                }
            }
        } else if (Scene.v().getActiveHierarchy().isClassSubclassOf(currentlyInstrumentedSootClass, androidFragment)) {
            /*
             * For each interesting life cycle method in androidFragment check if the
             * currentlyInstrumentedSootClass override it or not. If not create a synthetic
             * method which simply calls super...
             */
            for (SootMethod androidFragmentMethod : androidFragment.getMethods()) {

                if (interestingFragmentLifecycleCallbacks.contains(androidFragmentMethod.getName())
                        //
                        && !androidFragmentMethod.isFinal() && !androidFragmentMethod.isPrivate()) {

                    if (!currentlyInstrumentedSootClass.declaresMethod(androidFragmentMethod.getSubSignature())) {

                        /*
                         * Lookup the hierarchy until we find the one implementing this method and use
                         * that as super for the call... Worst case is androidActivity class
                         */
                        SootClass superClassActuallyImplementingTheMethod = getSuperClassActuallyImplementingMethod(
                                currentlyInstrumentedSootClass, androidFragmentMethod);

                        // Maybe throwing a real assertion here...
                        assert superClassActuallyImplementingTheMethod != null : "Cannot find original class declaring "
                                + androidFragmentMethod;

                        /*
                         * This is the actual method to call
                         */
                        SootMethod theSuperClassMethod = superClassActuallyImplementingTheMethod
                                .getMethod(androidFragmentMethod.getSubSignature());

                        if (theSuperClassMethod.isFinal() || theSuperClassMethod.isPrivate()) {
                            continue;
                        }

                        System.out.println("\t\t\t Override " + androidFragmentMethod + " from "
                                + superClassActuallyImplementingTheMethod);
                        /*
                         * Create the override method
                         */
                        SootMethod method = new SootMethod(theSuperClassMethod.getName(),
                                theSuperClassMethod.getParameterTypes(), theSuperClassMethod.getReturnType());
                        // Use the same modifiers as super
                        method.setModifiers(theSuperClassMethod.getModifiers());
                        // Tag the method as synthetic
                        method.addTag(SyntheticCallTag.TAG);
                        // Inject the method in the class
                        currentlyInstrumentedSootClass.addMethod(method);

                        /*
                         * Implement the override method by calling the corresponding super method via a
                         * specialinvoke and handling its return type
                         */
                        JimpleBody body = Jimple.v().newBody(method);
                        method.setActiveBody(body);
                        PatchingChain<Unit> units = body.getUnits();
                        // This is an instance method (I hope !)

                        // Generate the identityStmt for this
                        Local thisLocal = UtilInstrumenter.generateFreshLocal(body,
                                currentlyInstrumentedSootClass.getType());
                        units.add(Jimple.v().newIdentityStmt(thisLocal,
                                Jimple.v().newThisRef(currentlyInstrumentedSootClass.getType())));

                        // Generate the identityStmt for the parameters
                        List<Local> parameters = new ArrayList<>();
                        for (int number = 0; number < method.getParameterTypes().size(); number++) {
                            Type type = method.getParameterTypes().get(number);
                            Local parameterLocal = UtilInstrumenter.generateFreshLocal(body, type);
                            units.add(Jimple.v().newIdentityStmt(parameterLocal,
                                    Jimple.v().newParameterRef(type, number)));
                            parameters.add(parameterLocal);
                        }

                        if (theSuperClassMethod.getReturnType() instanceof VoidType) {
                            // Capture the return value ?
                            units.add(Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(thisLocal,
                                    theSuperClassMethod.makeRef(), parameters)));
                            units.add(Jimple.v().newReturnVoidStmt());
                        } else {
                            // Store returnValue into a local
                            Local returnValue = UtilInstrumenter.generateFreshLocal(body,
                                    theSuperClassMethod.getReturnType());
                            // AssignStmt
                            units.add(Jimple.v().newAssignStmt(returnValue, Jimple.v().newSpecialInvokeExpr(thisLocal,
                                    theSuperClassMethod.makeRef(), parameters)));
                            // Call super passing the parameters
                            units.add(Jimple.v().newReturnStmt(returnValue));
                        }
                    }
                }
            }
        }

    }

    private SootClass getSuperClassActuallyImplementingMethod(SootClass baseClass, SootMethod methodFromSuperClass) {
        // I hope those are sorted by is-a, i.e., java.lang.Object is the last
        for (SootClass superClass : Scene.v().getActiveHierarchy().getSuperclassesOf(baseClass)) {
            if (superClass.declaresMethod(methodFromSuperClass.getSubSignature())) {
                return superClass;
            }
        }
        // This should never happens...
        return null;
    }

    /**
     * Go over the units of this methods and instrument all the "method" calls that
     * do not belong to the current app as those are already instrumented by means
     * of methodBegin and methodEnds. Note that this code ALSO instrument methods
     * calls that belong to trap handlers!
     * 
     * @param currentlyInstrumentedMethod
     * @param currentlyInstrumentedMethodBody
     * @param currentlyInstrumentedMethodBodyUnitChain
     * @throws XmlPullParserException
     */
    private void instrumentMethodBody(final SootMethod currentlyInstrumentedMethod,
            final Body currentlyInstrumentedMethodBody,
            final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain)
            throws IOException, XmlPullParserException {

        // BufferedWriter unitFileWriter = new BufferedWriter(new
        // FileWriter("unit_log.txt"));

        for (final Iterator<Unit> iter = currentlyInstrumentedMethodBodyUnitChain.snapshotIterator(); iter.hasNext();) {

            final Unit currentUnit = iter.next();

            // unitFileWriter.write(currentUnit.toString());
            // unitFileWriter.newLine();

            /*
             * Do not instrument our instrumentation ...
             */
            if (currentUnit.getTags().contains(ABCTag.TAG)) {
                continue;
            }

            /*
             * Instrument all the libCall method invocations by wrapping them with begin/end
             */
            currentUnit.apply(new InstrumentLibCall(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
                    currentlyInstrumentedMethodBodyUnitChain, userClasses, rIdClass));

            if (INSTRUMENT_FIELDS_OPERATIONS) {
                /*
                 * Capture field setting as fake operations
                 */
                currentUnit.apply(new FieldTransformer(Constants.getAPKName(), currentlyInstrumentedMethod,
                        currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
            }
            /*
             * Make sure we properly report ArrayOperations. TODO Are we sure those apply
             * ONLY to AssignStmt?
             */
            if (INSTRUMENT_ARRAY_OPERATIONS && canInstrumentArrayOperations(currentlyInstrumentedMethod)) {
                currentUnit.apply(new ArrayConstructorTransformer(currentlyInstrumentedMethod,
                        currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
                currentUnit.apply(new ArrayAccessTransformer(currentlyInstrumentedMethod,
                        currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
                currentUnit.apply(new ArrayStoreTransformer(currentlyInstrumentedMethod,
                        currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
            }
        }

    }

    /**
     * This methods decides whether we can instrument array operations for the
     * SootMethod. The issue is that we may simply generate too much method
     * invocations by introducing our instrumentation.
     * 
     * @param currentlyInstrumentedMethod
     * @return
     */
    public boolean canInstrumentArrayOperations(SootMethod currentlyInstrumentedMethod) {
        // TODO Heuristic here... Skip all the <package=name>.R$ classes, e.g.,
        // abc/basiccalculator/R$styleable
        return !currentlyInstrumentedMethod.getDeclaringClass().getName().contains(".R$");

    }

    /*
     * Collect the parameters for invoking "monitorOnEnter" ->
     * dynCG2.Monitor.enter(String apkName, Object methodOwnerOrNull, String
     * methodSignature, Object[] methodParameters)
     *
     * NOTE parameters must passed using Object[], so primitive types must be
     * suitably Boxed
     */
    private void instumentMethodBegins(SootMethod currentlyInstrumentedMethod,
            /*
             * Need to generate fresh locals and access other elements like "this.local" -
             * TODO Is this.local the method owner or the method itself?
             */
            Body currentlyInstrumentedMethodBody,
            /*
             * Need to inject the code inside the currentlyInstrumentedMethod - TODO Can't
             * we simply get a new instance of this or shall we pass it around ?
             */
            PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain

    ) throws IOException, XmlPullParserException {

        System.out.println("\t\t Instrumenting Method Entry point for " + currentlyInstrumentedMethod);

        List<Unit> instrumentationCode = new ArrayList<>();

        List<Value> methodStartParameters = new ArrayList<Value>();

        // String apkName
        methodStartParameters.add(StringConstant.v(Constants.getAPKName()));

        // Object methodOwnerOrNull
        if (currentlyInstrumentedMethod.isStatic()) {
            /*
             * Static methods cannot refer to instances. And null might simply break the
             * code...
             */
            methodStartParameters.add(NullConstant.v());
        } else if (currentlyInstrumentedMethod.isConstructor()) {
            /*
             * Constructors are tricky: they let you refer to this, but this is initialized
             * by a chain of super.init. Instead of logging this mid-way during the
             * constructor execution, we report this when the constructor returns
             */
            methodStartParameters.add(NullConstant.v());
        } else {
            methodStartParameters.add(currentlyInstrumentedMethodBody.getThisLocal());
        }

        // String methodSignature
        methodStartParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));

        // Object[] methodParameters
        // Make sure that if a parameter is null, we do not trace it as void@0 but we
        // use the formal parameter of the method
        List<Local> parameterList = currentlyInstrumentedMethodBody.getParameterLocals();
        Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter.generateParameterArrayFromParametersLocal(
                RefType.v("java.lang.Object"), parameterList, currentlyInstrumentedMethodBody);

        methodStartParameters.add(tmpArgsListAndInstructions.getFirst());

        // Add the code to create the parameterList Object[]
        instrumentationCode.addAll(tmpArgsListAndInstructions.getSecond());
        /*
         * Add the code which calls "monitorOnEnter" unless the method is a synthetic
         * method we generated. In that case, invoke a monitorOnEnterSynthetic.
         */
        // This must come after we create the Object[]

        if (currentlyInstrumentedMethod.hasTag(SyntheticCallTag.TAG_NAME)) {
            final Stmt callMonitorOnEnter = Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(monitorOnSyntheticMethodCall.makeRef(), methodStartParameters));
            //
            instrumentationCode.add(callMonitorOnEnter);
        } else if (currentlyInstrumentedMethod.isPrivate()) {
            final Stmt callMonitorOnEnter = Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(monitorOnPrivateAppMethodCall.makeRef(), methodStartParameters));
            instrumentationCode.add(callMonitorOnEnter);
        } else {

            final Stmt callMonitorOnEnter = Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(monitorOnAppMethodCall.makeRef(), methodStartParameters));
            //
            instrumentationCode.add(callMonitorOnEnter);
        }

        /*
         * Since monitorOnEnter requires "THIS" and the input parameters, we cannot
         * invoke it before both this (if any) and the input parameters have been
         * assigned to their Locals. Not that for constructor we cannot log at this
         * position the reference to this, but we need to wait when super.init returns.
         * For this reason we log the reference to this only before returning.
         */
        Iterator<Unit> iterator = currentlyInstrumentedMethodBodyUnitChain.snapshotIterator();
        while (iterator.hasNext()) {
            Unit u = iterator.next();
            if (u instanceof IdentityStmt) {
                continue;
            }

            UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, u,
                    instrumentationCode);

            break;
        }

    }

    // Keep this method to have meaningful explanation why a class has been skipped
    private boolean skipClass(SootClass currentlyInstrumentedSootClass) {
        if (currentlyInstrumentedSootClass.isPhantom()) {
            System.out.println("\n\n Phantom class: " + currentlyInstrumentedSootClass.getName());
            return true;
        }
        if (!currentlyInstrumentedSootClass.isApplicationClass()) {
            System.out.println("\n\n Not an application class: " + currentlyInstrumentedSootClass.getName());
            return true;
        }
        if (currentlyInstrumentedSootClass.isInterface()) {
            System.out.println("\n\n Interface - not a class: " + currentlyInstrumentedSootClass.getName());
            return true;
        }

        // If any of the specified filters matches then skip this
        if (this.packageFilters.stream()
                .anyMatch(p -> p.matcher(currentlyInstrumentedSootClass.getPackageName()).matches())) {
            System.out.println("\n\n Matches custom package filter " + currentlyInstrumentedSootClass.getName());
            return true;
        } else {
            return false;
        }
    }

    /**
     * We find all the exit points of this method and instrument all of them. This
     * includes calls to return inside the BODY and TRAPS. So this already changes
     * the content of traps and we might need to "split traps again"?
     * 
     * 
     * TODO This seems to be broken ?!
     * 
     * 
     * @param currentlyInstrumentedSootMethod
     * @param currentlyInstrumentedMethodBody
     * @param currentlyInstrumentedMethodBodyUnitChain
     */
    private void instrumentMethodEnds(final SootMethod currentlyInstrumentedSootMethod,
            /*
             * Need to generate fresh locals and access other elements like "this.local" -
             * TODO Is this.local the method owner or the method itself?
             */
            final Body currentlyInstrumentedMethodBody,
            /*
             * Need to inject the code inside the currentlyInstrumentedMethod - TODO Can't
             * we simply get a new instance of this or shall we pass it around ?
             */
            final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain) {

        /*
         * To ensure that we log all the method ends, including return and throw
         * statements that are not trapped we need to build the exceptionalUnitGraph (a
         * CFG that includes the exceptional branches), get the exit points of the
         * method, i.e., the tails and insert the instrumentation right before each of
         * them.
         * 
         * According to the documentation, the Exceptional Unit Graph is build such that
         * for every Unit which may implicitly throw an exception that could be caught
         * by a Trap in the Body, there will be an edge from each of the excepting
         * Unit's predecessors to the Trap handler's first Unit (since any of those
         * predecessors may have been the last Unit to complete execution before the
         * handler starts execution). If the excepting Unit might have the side effect
         * of changing some field, then there will definitely be an edge from the
         * excepting Unit itself to its handlers, since the side effect might occur
         * before the exception is raised. If the excepting Unit has no side effects,
         * then parameters passed to the ExceptionalUnitGraph constructor determine
         * whether or not there is an edge from the excepting Unit itself to the handler
         * Unit.
         */

        // REFACTOR THIS INTO A METHOD
        // TODO Can this be null ? How?
        ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(
                currentlyInstrumentedSootMethod.getActiveBody());

        // Plotting is not supported !
//        if (DEBUG) {
//            Visualizer.v().addUnitGraph(exceptionalUnitGraph);
//            Visualizer.v().draw();
//        }

        final String currentlyInstrumentedMethodSignature = currentlyInstrumentedSootMethod.getSignature();
        /*
         * Tails also includes throw stmt which will be captured by catch blocks/traps
         * inside the code.
         * 
         * 
         * TODO Those are not really method exit points, but we care about them?
         *
         * For every ThrowInst or ThrowStmt (what's the difference?) Unit which may
         * explicitly throw an exception that would be caught by a Trap in the Body,
         * there will be an edge from the throw Unit to the Trap handler's first Unit.
         * 
         * 
         * This means that this particular Throw does NOT "break" the start-end pattern
         * of method invocations. What about the other cases -> method signature
         * declares a "throws"
         * 
         */
        List<Unit> exitPoints = new ArrayList<>();
        List<Unit> nonExitPoints = new ArrayList<>();

        for (Unit potentialExitPoint : exceptionalUnitGraph.getTails()) {
            if (potentialExitPoint instanceof ReturnStmt || potentialExitPoint instanceof ReturnVoidStmt) {
                System.out.println(
                        "\t\t\t SceneInstrumenterWithMethodParameters.instrumentMethodEnds() Found return exit point "
                                + potentialExitPoint);
                // TODO In some cases we get "return null". For example, BasicCalculator... not
                // sure this is a problem

                // Let's conservatively keep return instructions as exit points to process later
                exitPoints.add(potentialExitPoint);
                continue;
            } else if (potentialExitPoint instanceof ThrowStmt) {

                for (Trap trap : currentlyInstrumentedSootMethod.getActiveBody().getTraps()) {
                    // None of the following should ever return null...
                    if (exceptionalUnitGraph.getSuccsOf(potentialExitPoint).contains(trap.getHandlerUnit())) {
                        // This is not really an exit point, since the exception is captured by the
                        // trap. Never the less we need to inform the monitor that one exception has
                        // been raised
                        System.out.println(
                                "\t\t\t SceneInstrumenterWithMethodParameters.instrumentMethodEnds() Found a throw that is NOT an exit point "
                                        + potentialExitPoint);
                        nonExitPoints.add(potentialExitPoint);
                        // Avoid repeatedly remove the "same" object

                        break;
                    } else {
                        // If we reach this point, the ThrowStmt will cause THIS method to exit.
                        exitPoints.add(potentialExitPoint);
                        System.out.println(
                                "\t\t\t SceneInstrumenterWithMethodParameters.instrumentMethodEnds() Found throw exit point "
                                        + potentialExitPoint);
                        break;
                    }
                }
            } else {
                throw new RuntimeException("\t\t\t Unknown exitpoint " + potentialExitPoint);
            }
        }

        // Instrument the exit points we found
        for (Unit exitPoint : exitPoints) {
            exitPoint.apply(new AbstractStmtSwitch() {
                // TODO Refactor this into a separate class?

                // The wrapping thingy does not work on constructors
                @Override
                public void caseThrowStmt(ThrowStmt stmt) {
                    System.out.println(
                            "\t\t\t Instrumenting throw exit point " + stmt + " throwing exception " + stmt.getOp());

                    // Wrap the value to be returned into a new object
                    // local and pass it to the monitor
                    List<Unit> instrumentationCode = new ArrayList<>();
                    // Prepare the call to the monitor
                    List<Value> onReturnIntoFromExceptionArgs = new ArrayList<>();
                    //
                    if (currentlyInstrumentedSootMethod.isStatic() || currentlyInstrumentedSootMethod.isConstructor()) {
                        onReturnIntoFromExceptionArgs.add(NullConstant.v());
                    } else {
                        onReturnIntoFromExceptionArgs.add(currentlyInstrumentedMethodBody.getThisLocal());
                    }
                    // Signature
                    onReturnIntoFromExceptionArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
                    // Context
                    onReturnIntoFromExceptionArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));

                    // Pass the exception instead of the return value or null. Why this should be
                    // null ?!
                    // ((RefType) ts.getOp().getType()).getSootClass()
                    onReturnIntoFromExceptionArgs.add(UtilInstrumenter
                            .generateCorrectObject(currentlyInstrumentedMethodBody, stmt.getOp(), instrumentationCode));

                    Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(
                            monitorOnAppMethodReturnExceptionally.makeRef(), onReturnIntoFromExceptionArgs));

                    instrumentationCode.add(onReturnIntoCall);

                    UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
                            instrumentationCode);

                }

                @Override
                public void caseReturnStmt(ReturnStmt stmt) {
                    System.out.println("\t\t\t Instrumenting return exit point " + stmt);
                    // Wrap the value to be returned into a new object
                    // local and pass it to the monitor
                    List<Unit> instrumentationCode = new ArrayList<>();
                    // Prepare the call to the monitor
                    List<Value> onReturnIntoArgs = new ArrayList<>();
                    //
                    if (currentlyInstrumentedSootMethod.isStatic()) {
                        onReturnIntoArgs.add(NullConstant.v());
                    } else {
                        onReturnIntoArgs.add(currentlyInstrumentedMethodBody.getThisLocal());
                    }
                    //
                    onReturnIntoArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
                    // Context
                    onReturnIntoArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
                    // Return value Null if VOID
                    onReturnIntoArgs.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
                            stmt.getOp(), instrumentationCode));
                    Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
                            .newStaticInvokeExpr(monitorOnAppMethodReturnNormally.makeRef(), onReturnIntoArgs));
                    instrumentationCode.add(onReturnIntoCall);

                    // Hope this does not break it
                    // currentlyInstrumentedMethodBodyUnitChain.insertBefore(instrumentationCode,
                    // stmt);
                    UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
                            instrumentationCode);
                }

                @Override
                public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
                    System.out.println("\t\t\t Instrumenting return void exit point " + stmt);

                    List<Unit> instrumentationCode = new ArrayList<>();
                    // Prepare the call to the monitor
                    List<Value> onReturnIntoArgs = new ArrayList<>();

                    //
                    if (currentlyInstrumentedSootMethod.isStatic()) {
                        onReturnIntoArgs.add(NullConstant.v());
                    } else {
                        onReturnIntoArgs.add(currentlyInstrumentedMethodBody.getThisLocal());
                    }
                    //
                    onReturnIntoArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
                    //
                    onReturnIntoArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
                    // Void
                    onReturnIntoArgs.add(NullConstant.v());
                    Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
                            .newStaticInvokeExpr(monitorOnAppMethodReturnNormally.makeRef(), onReturnIntoArgs));
                    instrumentationCode.add(onReturnIntoCall);
                    // Hope this does not break it
                    // currentlyInstrumentedMethodBodyUnitChain.insertBefore(instrumentationCode,
                    // stmt);
                    UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
                            instrumentationCode);
                }

            });

            // This should not be necessary since we are not changing the defintion of traps
            // very mush like when we instrument the body !
            // TODO We need to make sure that all traps objects are coorectly handled, and
            // if mofied (their Begin and End UNit might have changed?)
            // Since we might have changed the traps of this method, we might need to split
            // them again?
//			TrapManager tm = new TrapManager();
//			tm.splitTrapsAgainst(b, rangeStart, rangeEnd);

        }

        // TODO We still need to handle Exceptions raised by non-user
        // methods....
    }

    /**
     * This method is used to skip instrumenting methods. 
     * @param currentlyInstrumentedSootMethod
     * @return
     */
    private boolean skipMethod(SootMethod currentlyInstrumentedSootMethod) {
        if (!currentlyInstrumentedSootMethod.isConcrete()) {
            System.out.println(
                    "\n\t SKIPPING METHOD: " + currentlyInstrumentedSootMethod.getSignature() + " NOT CONCRETE");
            return true;
        }

        if ((currentlyInstrumentedSootMethod.toString().indexOf(": java.lang.Class class$") != -1)) {
            System.out.println(
                    "\n\t SKIPPING METHOD: " + currentlyInstrumentedSootMethod.getSignature() + " NOT A CLASS ?");
            return true;
        }

        // Not sure how to check that return is void... && currentlyInstrumentedSootMethod.getReturnType().equals("void") 
        if( currentlyInstrumentedSootMethod.isStatic() && currentlyInstrumentedSootMethod.getName().contains("access$")) {
            System.out.println(
                    "\n\t SKIPPING METHOD: " + currentlyInstrumentedSootMethod.getSignature() + " S");
            return true;
        }

        return false;
    }

    /**
     * Add to the method a Trap that captures Throwable t and re-throws it. To be
     * safe we add this try-catch-all in any case, as a method might have a trap
     * that captures Throwable but does something else later that raises an
     * exception which we will lose if we do not wrap everything !
     * 
     * TODO How do we handle FINALLY ?
     * 
     * TODO Uniform the method signature to receive always the same input...
     * 
     * @param currentlyInstrumentedMethod
     */
    public void wrapTryCatchAllBlocks(SootMethod currentlyInstrumentedMethod) {

        SootClass throwableSootClass = Scene.v().getSootClass("java.lang.Throwable");
        assert throwableSootClass != null;
        RefType throwableClassRefType = RefType.v(throwableSootClass);

        Body body = currentlyInstrumentedMethod.retrieveActiveBody();
        PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain = body.getUnits();

        /*
         * To define a trap we need the following data: - exception, - start, - stop, -
         * handler:
         * 
         * Between the start and stop units, including start, but not including stop, if
         * the exception is thrown, execution continues at handler.
         * 
         * Exception: Throwable Start: the start of the method body, since we need to
         * wrap the entire thing Stop (excluded): should point to the instruction right
         * "after" the last unit of the body to fully include the body in the trap scope
         * Handler: is the code that rethrow the exception
         * 
         */

        Stmt startOfTheMethod = UtilInstrumenter.getFirstNonIdStmt(currentlyInstrumentedMethodBodyUnitChain);

        // Found the last unit of the body - TODO This code is taken from DUAFDROID and
        // I cannot tell if that's reliable or not...
        Stmt lastUnitOfCurrentlyInstrumentedBodyUnitChain = (Stmt) currentlyInstrumentedMethodBodyUnitChain.getLast();

        // // This causes problems
//		if (lastUnitOfCurrentlyInstrumentedBodyUnitChain instanceof ThrowStmt && body.getTraps().size() >= 1) {
//			/*
//			 * This happens when the whole body is nested in a synchronized block
//			 */
//			lastUnitOfCurrentlyInstrumentedBodyUnitChain = (Stmt) body.getTraps().getLast().getBeginUnit();
//		}

        /*
         * Empty method won't cause any exception so we don't need instrument it at all
         */
        if (startOfTheMethod == lastUnitOfCurrentlyInstrumentedBodyUnitChain) {
            // Empty method?
            return;
        }

        // TODO. Not sure what this means ... What's this? An empty body for a instance
        // method?
        if (lastUnitOfCurrentlyInstrumentedBodyUnitChain instanceof IdentityStmt) {
            System.out.println("wrapTryCatchAllBlocks: Special case encountered in method "
                    + currentlyInstrumentedMethod.getSignature() + " stmt: "
                    + lastUnitOfCurrentlyInstrumentedBodyUnitChain);
            // Ensures we remember this case exists !
            throw new RuntimeException("wrapTryCatchAllBlocks: Special case encountered in method "
                    + currentlyInstrumentedMethod.getSignature() + " stmt: "
                    + lastUnitOfCurrentlyInstrumentedBodyUnitChain);

            /*
             * Stmt tgt = (Stmt)utils.getFirstNonIdUnit(pchain); if (tgt != null) tgt =
             * (Stmt)pchain.getPredOf(tgt); if (tgt == null) tgt = sLast;
             * 
             * InstrumManager.v().insertAtProbeBottom(pchain, tcProbes, sLast);
             */
        }

        /*
         * The followig code resemble the one in this class:
         * https://github.com/soot-oss/soot/blob/master/src/main/java/soot/jimple/
         * toolkits/invoke/SynchronizerManager.java#L376
         */

        List<Unit> instrumentationCode = new ArrayList<Unit>();

        /*
         * Ideally one can add a dummy operation at last, i.e., nop, but doing that will
         * break the code since nop will appear after return statements. So we need to
         * work around this.
         */
//		Stmt gotoForLastUnitOfTheMethod = Jimple.v().newGotoStmt(lastUnitOfCurrentlyInstrumentedBodyUnitChain);
        // Should this be included in the method?
//		instrumentationCode.add(gotoForLastUnitOfTheMethod);
        /*
         * Form the Trap body: we need to take the caught exception and store it into a
         * local, such that we can rethrow it afterwards since ThrowStmt works on locals
         */

        Local exceptionCaughtByTrapLocal = UtilInstrumenter.generateFreshLocal(body, throwableClassRefType);
        Local exceptionToBeThrownLocal = UtilInstrumenter.generateFreshLocal(body, throwableClassRefType);

        // This corresponds to the @caughtexception, e.g., "r5 := @caughtexception"
        Stmt handlerStmt = Jimple.v().newIdentityStmt(exceptionCaughtByTrapLocal, Jimple.v().newCaughtExceptionRef());
        // Assign the @caughtexception to a new Local
        Stmt assignException = Jimple.v().newAssignStmt(exceptionToBeThrownLocal, exceptionCaughtByTrapLocal);
        // (re)Throw the exception. pay attention to use the rigth Local
        Stmt rethrowTheException = Jimple.v().newThrowStmt(exceptionToBeThrownLocal);

        // Assemble the catch block
        instrumentationCode.add(handlerStmt);
        instrumentationCode.add(assignException);
        // Add a fake couple of statements just to see if they are instrumented and
        // returned ...
        instrumentationCode.add(rethrowTheException);

        //
        UtilInstrumenter.instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain,
                lastUnitOfCurrentlyInstrumentedBodyUnitChain, instrumentationCode);
        // Inject some fake code here not tagged as instrumentation
// THIS IS TRICKTY AS System.out might not exist at all !
//		{
//			List<Unit> debugUnits = new ArrayList<Unit>();
//			// Resolve dependencies
//			Scene.v().loadClassAndSupport("java.lang.Object");
//			Scene.v().loadClassAndSupport("java.lang.System");
//
//			Local arg, tmpRef;
//
//			// Add locals, java.io.printStream tmpRef
//			tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
//			body.getLocals().add(tmpRef);
//
//			// add "tmpRef = java.lang.System.out"
//			debugUnits.add(Jimple.v().newAssignStmt(tmpRef, Jimple.v()
//					.newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
//
//			// insert "tmpRef.println("Hello world!")"
//			SootMethod toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
//			debugUnits.add(Jimple.v().newInvokeStmt(
//					Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), StringConstant.v("Hello world!"))));
//
//			// Add them before rethrowTheException
//			currentlyInstrumentedMethodBodyUnitChain.insertBefore(debugUnits, rethrowTheException);
//		}
        // We now need to inject the code into the method body and then define how the
        // trap looks like/can be reached?.
//		UtilInstrumenter.instrumentBeforeNoRedirect(currentlyInstrumentedMethodBodyUnitChain, instrumentationCode,
//				lastUnitOfCurrentlyInstrumentedBodyUnitChain);

        // I am not sure this makes sense to use handlerStmt as the unit after the last
        // (return)
        Trap trap = Jimple.v().newTrap(throwableSootClass, startOfTheMethod,
                lastUnitOfCurrentlyInstrumentedBodyUnitChain, handlerStmt);
        // Add the trap without forcing it to be the last one as the semantic is not
        // clear...
        body.getTraps().add(trap);
    }

}
// -- public class icgInst
