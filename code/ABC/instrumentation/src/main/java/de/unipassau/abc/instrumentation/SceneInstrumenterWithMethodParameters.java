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

import org.xmlpull.v1.XmlPullParserException;

import android.R.bool;
import de.unipassau.abc.data.Pair;
import dev.navids.soottutorial.visual.Visualizer;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.TrapManager;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
// InfoFlow
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.Chain;

/**
 * TODO Get rid of the "old" dua thingy if possible, in the end we are just
 * running our own instrumentation code and nothing more ! TODO Array
 * instantiation and access must be transformed into
 *
 * TODO Replace: opts.debugOut() with a proper logger
 *
 * ABC.array <init>(type, size) ABC.array Type get(position) ABC.array Type
 * set(position)
 *
 * TODO Rename this class!
 *
 * @author gambi
 *
 */
public class SceneInstrumenterWithMethodParameters extends SceneTransformer {

	private static final boolean MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT = System.getProperties()
			.containsKey("abc.make.android.lifecycle.events.explicit");

	private static final boolean OUTPUT_INSTRUMENTED_CODE = System.getProperties()
			.containsKey("abc.output.instrumented.code");

	private static final boolean INSTRUMENT_ARRAY_OPERATIONS = System.getProperties()
			.containsKey("abc.instrument.array.operations");

	private static final boolean INSTRUMENT_TRAPS = System.getProperties().containsKey("abc.instrument.traps");

	private static final boolean DEBUG = System.getProperties().containsKey("abc.debug");

	// Class which is invoked dynamically from the inserted probes
	protected SootClass clsMonitor;

	// Methods of the clsMonitor class invoked by the probes
	protected SootMethod monitorInitialize;

	protected SootMethod monitorOnEnter;
	protected SootMethod monitorOnLibCall;
	protected SootMethod monitorOnEnterSynthetic;
	protected SootMethod monitorOnEnterPrivate;

	// Not used ?
	protected SootMethod monitorOnEnterStatic;
	// protected SootMethod monitorOnLibCallStatic;

	protected SootMethod monitorOnReturnInto;
	protected SootMethod monitorOnTerminateApp;
	//
	protected SootMethod monitorOnReturnIntoFromException;
	protected SootMethod monitoronExceptionCaptured;

	// Somehow we need to capture the situation for which a method throws an
	// exception that it handles. So a throw which is NOT an exit point.
	protected SootMethod monitorOnThrowCapturedException;

	private List<SootClass> userClasses;

	protected File fJimpleOrig = null;
	protected File fJimpleInsted = null;

	private String appPackageName;

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

		try {
			instrument();
		} catch (IOException | XmlPullParserException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Descendants may want to use customized event monitors
	 */
	protected void initMonitorClass() {
		try {
			clsMonitor = Scene.v().getSootClass(utils.Constants.MONITOR_CLASS);
			clsMonitor.setApplicationClass();

			// TODO Not sure we are actually using any of those
			Scene.v().getSootClass("utils.MethodEventComparator").setApplicationClass();

			// This is used also outside monitorICCC so we need to explicitly
			// load
			// it into soot
			Scene.v().getSootClass("utils.logicClock").setApplicationClass();

			monitorInitialize = clsMonitor.getMethodByName("initialize");

			// A method of our application was called
			monitorOnEnter = clsMonitor.getMethodByName("enter");
			monitorOnEnterStatic = clsMonitor.getMethodByName("enterStatic");

			monitorOnEnterSynthetic = clsMonitor.getMethodByName("enterSynthetic");
			monitorOnEnterPrivate = clsMonitor.getMethodByName("enterPrivate");
			// TODO A method of our application returned
			// monitorOnEnter = clsMonitor.getMethodByName("returns");

			// A method of our application called an external library or a
			// system
			// library (?)
			monitorOnLibCall = clsMonitor.getMethodByName("libCall");
			// monitorOnLibCallStatic =
			// clsMonitor.getMethodByName("libCallStatic");

			// Generic Method to log a return
			monitorOnReturnInto = clsMonitor.getMethodByName("returnInto");

			// Our application terminated
			monitorOnTerminateApp = clsMonitor.getMethodByName("terminate");

			// An exception returned in a catch method of our application
			monitorOnReturnIntoFromException = clsMonitor.getMethodByName("returnIntoFromException");

			monitoronExceptionCaptured = clsMonitor.getMethodByName("onExceptionCaptured");

			// A throw statement that is captured
			monitorOnThrowCapturedException = clsMonitor.getMethodByName("throwCapturedException");

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
		System.out.println("OUTPUT_INSTRUMENTED_CODE: " + this.OUTPUT_INSTRUMENTED_CODE);
		System.out.println("MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT: " + this.MAKE_ANDROID_LIFE_CYCLE_EVENTS_EXPLICIT);
		System.out.println("INSTRUMENT_ARRAY_OPERATIONS: " + this.INSTRUMENT_ARRAY_OPERATIONS);
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
				makeInterestingLifeCycleEventsExplicit(currentlyInstrumentedSootClass);
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
				 * Instrument the methods which do not belong to user called but are called by
				 * the currentlyInstrumentedMethod. Inside instrumentMethodBody we also handle
				 * special cases such as Arrays
				 * 
				 */
				System.out.println("\t INSTRUMENTING METHOD BODY");
				instrumentMethodBody(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
						currentlyInstrumentedMethodBodyUnitChain);

				/*
				 * Inject instrumentation code before any return/throw statement in the method.
				 * This might also require to explicitly introdyce try/catch statements to
				 * capture (and rethrow) also undeclared exceptions
				 */

				System.out.println("\t INSTRUMENTING METHOD EXIT-POINTS");
				instrumentMethodEnds(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
						currentlyInstrumentedMethodBodyUnitChain);

				// THE FOLLOWING CODE BREAKS THE APP AND TRIGGER THE ANDROID VERIFIER
////			if (INSTRUMENT_TRAPS) {
////			System.out.println("\t INSTRUMENTING TRAPS");
////			instrumentMethodTraps(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
////			currentlyInstrumentedMethodBodyUnitChain);
////			}

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
	 * When a catch block gets triggered some method raised an exception, but we do
	 * not know which one. However, we can notify the Monitor about the fact that we
	 * handled an exception so it can pinpoint what was the last executed method.
	 * The monitor can rule out exceptions that are explicitly thrown by the method
	 * itself because we log any throw stmt that is not an exit point
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

		// TODO Use this. Note that traps a linked one another so it should not happen
		// the start of one crosses the others
		TrapManager tm = new TrapManager();

		Chain<Trap> traps = currentlyInstrumentedMethodBody.getTraps();
		for (Trap t : traps) {
			// TODO Leave it be for the moment...
			System.out.println("---------------------------------------------");
			System.out.println("Instrumenting TRAP " + t);
			// TODO Is this the @Caught?
			System.out.println("First unit of trap is " + t.getBeginUnit());
			System.out.println("Last unit of trap is " + t.getEndUnit());

			// This gives us the Class of the exception
			System.out.println("Caught Exception is " + t.getException());
			SootClass sce = t.getException();

			// Not sure this really needed
			System.out.println("Handler Unit is " + t.getHandlerUnit());
			// This is where we n
			System.out.println(
					"Unit after the handler " + currentlyInstrumentedMethodBodyUnitChain.getSuccOf(t.getHandlerUnit()));
			// This should be the first "executable" instruction ?
			Unit unitAfterHandler = currentlyInstrumentedMethodBodyUnitChain.getSuccOf(t.getHandlerUnit());
			System.out.println("---------------------------------------------");

			// Wrap the value to be returned into a new object
			// local and pass it to the monitor
			List<Unit> instrumentationCode = new ArrayList<>();
			// Prepare the call to the monitor
			List<Value> onExceptionCapturedArgs = new ArrayList<>();
			// Method Owner
			//
			if (currentlyInstrumentedSootMethod.isStatic() || currentlyInstrumentedSootMethod.isConstructor()) {
				onExceptionCapturedArgs.add(NullConstant.v());
			} else {
				onExceptionCapturedArgs.add(currentlyInstrumentedMethodBody.getThisLocal());
			}
			// Method Signature
			final String currentlyInstrumentedMethodSignature = currentlyInstrumentedSootMethod.getSignature();
			onExceptionCapturedArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
			// Method Context - TODO What's the difference ? Probably libCall have a
			// different context ?
			onExceptionCapturedArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));

			// Pass the exception
			Value caughtException = ((IdentityStmt) t.getHandlerUnit()).getLeftOp();

			// This updates instrumentation code to include all the necessary wrapping code
			onExceptionCapturedArgs.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
					caughtException, instrumentationCode));

			Stmt onExceptionCapturedCall = Jimple.v().newInvokeStmt(
					Jimple.v().newStaticInvokeExpr(monitoronExceptionCaptured.makeRef(), onExceptionCapturedArgs));

			instrumentationCode.add(onExceptionCapturedCall);

			UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, unitAfterHandler,
					instrumentationCode);
			// TODO TO WE NEED TO "REFRESH" THE DEFINITION OF TRAPS, LIKE REPLAVING THE OLD
			// TRAP WITH THE NEW ONE INSTEAD OF SIMPLY ADDING STUFF TO IT?
			// It seems this might be the case as the verifier triggers at this point...
		}

//		Trap t = trapsIt.next();
//		Iterator<Unit> unitIt = units.iterator(t.getBeginUnit(), t.getEndUnit());
//		   Trap newTrap = (Trap) t.clone();
//		   t.setBeginUnit(rangeStart);
//		   newTrap.setEndUnit(rangeStart);
//		   traps.insertAfter(newTrap, t);	
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
			if (!curcls.getName().contains(appPackageName)) {
				continue;
			}
			userClasses.add(curcls);
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
	 * This method takes all the interesting events a class extending Android has
	 * and make them explicit such that they appear in the trace. Those methods
	 * simply call super
	 * 
	 * 
	 * This explicitly tags the synthetic methods (using a s in the trace)
	 * 
	 * ALESSIO: XXX Not sure this is still necessary
	 * 
	 * @param currentlyInstrumentedSootClass
	 */
	private void makeInterestingLifeCycleEventsExplicit(SootClass currentlyInstrumentedSootClass) {
		// /*
		// * Do not create synthetic methods for abstract classes, since those
		// are
		// * extended somewhere... Not ne
		// */
		// if (currentlyInstrumentedSootClass.isAbstract()) {
		// return;
		// }
		// This is not necessary the case: sometimes the abstract class
		// implement some, but not all the methods !

		SootClass androidActivity = Scene.v().getSootClass("android.app.Activity");
		SootClass androidFragment = Scene.v().getSootClass("android.support.v4.app.Fragment");

		if (Scene.v().getActiveHierarchy().isClassSubclassOf(currentlyInstrumentedSootClass, androidActivity)) {
			/*
			 * For each method in androidActivity which starts with on check if the
			 * currentlyInstrumentedSootClass override it or not. If not create a synthetic
			 * method which simply calls super...
			 */
			for (SootMethod androidActivityMethod : androidActivity.getMethods()) {

				if (interestingAndroidLifecycleCallbacks.contains(androidActivityMethod.getName())
						&& !androidActivityMethod.isFinal() && !androidActivityMethod.isPrivate()) {

					if (!currentlyInstrumentedSootClass.declaresMethod(androidActivityMethod.getSubSignature())) {

						/*
						 * Lookup the hierarchy until we find the one implementing this method and use
						 * that as super for the call... Worst case is androidActivity class
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

						System.out.println("Override " + androidActivityMethod + " from "
								+ superClassActuallyImplementingTheMethod);
						/*
						 * Create the override method
						 */
						SootMethod method = new SootMethod(theSuperClassMethod.getName(),
								theSuperClassMethod.getParameterTypes(), theSuperClassMethod.getReturnType());
						// Use the same modifiers as super
						method.setModifiers(theSuperClassMethod.getModifiers());
						// Tag the method as synthetic
						method.addTag(ABCTag.TAG);
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

						System.out.println("Override " + androidFragmentMethod + " from "
								+ superClassActuallyImplementingTheMethod);
						/*
						 * Create the override method
						 */
						SootMethod method = new SootMethod(theSuperClassMethod.getName(),
								theSuperClassMethod.getParameterTypes(), theSuperClassMethod.getReturnType());
						// Use the same modifiers as super
						method.setModifiers(theSuperClassMethod.getModifiers());
						// Tag the method as synthetic
						method.addTag(ABCTag.TAG);
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
	 * of methodBegin and methodEnds
	 * 
	 * 
	 * @param currentlyInstrumentedMethod
	 * @param currentlyInstrumentedMethodBody
	 * @param currentlyInstrumentedMethodBodyUnitChain
	 */
	private void instrumentMethodBody(final SootMethod currentlyInstrumentedMethod,
			final Body currentlyInstrumentedMethodBody,
			final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain) {

		for (final Iterator<Unit> iter = currentlyInstrumentedMethodBodyUnitChain.snapshotIterator(); iter.hasNext();) {

			final Unit currentUnit = iter.next();

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
					currentlyInstrumentedMethodBodyUnitChain, userClasses));

			/*
			 * Make sure we properly report ArrayOperations. TODO Are we sure those apply
			 * ONLY to AssignStmt?
			 */
			if (INSTRUMENT_ARRAY_OPERATIONS) {
				currentUnit.apply(new ArrayConstructorTransformer(currentlyInstrumentedMethod,
						currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
				currentUnit.apply(new ArrayAccessTransformer(currentlyInstrumentedMethod,
						currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
				currentUnit.apply(new ArrayStoreTransformer(currentlyInstrumentedMethod,
						currentlyInstrumentedMethodBody, currentlyInstrumentedMethodBodyUnitChain, userClasses));
			}
		}

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
		methodStartParameters.add(StringConstant.v(getAPKName()));

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

		if (currentlyInstrumentedMethod.hasTag(ABCTag.TAG_NAME)) {
			final Stmt callMonitorOnEnter = Jimple.v().newInvokeStmt(
					Jimple.v().newStaticInvokeExpr(monitorOnEnterSynthetic.makeRef(), methodStartParameters));
			//
			instrumentationCode.add(callMonitorOnEnter);
		} else if (currentlyInstrumentedMethod.isPrivate()) {
			final Stmt callMonitorOnEnter = Jimple.v().newInvokeStmt(
					Jimple.v().newStaticInvokeExpr(monitorOnEnterPrivate.makeRef(), methodStartParameters));
			instrumentationCode.add(callMonitorOnEnter);
		} else {

			final Stmt callMonitorOnEnter = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorOnEnter.makeRef(), methodStartParameters));
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

	private String getAPKName() throws IOException, XmlPullParserException {
		String apkPath = soot.options.Options.v().process_dir().get(0);
		ProcessManifest manifest = new ProcessManifest(apkPath);
		return manifest.getPackageName();
		// GlobalRef.apkVersionCode = manifest.getVersionCode();
		// GlobalRef.apkVersionName = manifest.getVersionName();
		// GlobalRef.apkMinSdkVersion = manifest.getMinSdkVersion();
		// GlobalRef.apkPermissions = manifest.getPermissions();
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
		return false;
	}

	/**
	 * We find all the exit points of this method and instrument all of them. This
	 * includes calls to return inside the BODY and TRAPS. So this already changes
	 * the content of traps and we might need to "split traps again"?
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

		if (DEBUG) {
			Visualizer.v().addUnitGraph(exceptionalUnitGraph);
			Visualizer.v().draw();
		}

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
					}
				}
			} else {
				throw new RuntimeException("\t\t\t Unknown exitpoint " + potentialExitPoint);
			}
		}

		// Instrument the non exit points we found, those should be ThrowStmt that are
		// captured by the current method
		for (Unit nonExitPoint : nonExitPoints) {
			nonExitPoint.apply(new AbstractStmtSwitch() {
				@Override
				public void caseThrowStmt(ThrowStmt stmt) {
					System.out.println("\t\t\t Instrumenting throw that is handled within the method " + stmt);

					// Monitor.throwCapturedException(Object methodOwner, String methodSignature,
					// String methodContext, Object exception

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
					throwCapturedException.add(StringConstant.v(currentlyInstrumentedMethodSignature));
					// Context
					throwCapturedException.add(StringConstant.v(currentlyInstrumentedMethodSignature));

					// Pass the exception instead of the return value or null
					// ((RefType) ts.getOp().getType()).getSootClass()
					throwCapturedException.add(UtilInstrumenter.generateCorrectObject(currentlyInstrumentedMethodBody,
							stmt.getOp(), instrumentationCode));

					Stmt throwCapturedExceptionCall = Jimple.v().newInvokeStmt(Jimple.v()
							.newStaticInvokeExpr(monitorOnThrowCapturedException.makeRef(), throwCapturedException));

					instrumentationCode.add(throwCapturedExceptionCall);

					UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
							instrumentationCode);
				}
			});
		}

		// Instrument the exit points we found
		for (Unit exitPoint : exitPoints) {
			exitPoint.apply(new AbstractStmtSwitch() {
				// TODO Refactor this into a separate class?

				// The wrapping thingy does not work on constructors
				@Override
				public void caseThrowStmt(ThrowStmt stmt) {
					System.out.println("\t\t\t Instrumenting throw exit point " + stmt);

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
					//
					onReturnIntoFromExceptionArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));
					// Context
					onReturnIntoFromExceptionArgs.add(StringConstant.v(currentlyInstrumentedMethodSignature));

					// Pass the exception instead of the return value or null
					// ((RefType) ts.getOp().getType()).getSootClass()
					onReturnIntoFromExceptionArgs.add(UtilInstrumenter
							.generateCorrectObject(currentlyInstrumentedMethodBody, stmt.getOp(), instrumentationCode));

					Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(
							monitorOnReturnIntoFromException.makeRef(), onReturnIntoFromExceptionArgs));

					instrumentationCode.add(onReturnIntoCall);

					UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
							instrumentationCode);

					System.out.println(
							"\t\t\t Injected monitorOnReturnIntoFromException at the end of the method before throwing exception "
									+ ((RefType) stmt.getOp().getType()));
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
					Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(monitorOnReturnInto.makeRef(), onReturnIntoArgs));
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
					Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(monitorOnReturnInto.makeRef(), onReturnIntoArgs));
					instrumentationCode.add(onReturnIntoCall);
					// Hope this does not break it
					// currentlyInstrumentedMethodBodyUnitChain.insertBefore(instrumentationCode,
					// stmt);
					UtilInstrumenter.instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
							instrumentationCode);

					System.out.println("\t\t\t Injected returnInto at the end of the method before returning (void)");

				}

			});

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

//	/**
//	 * instrument 'terminate' monitor before 'System.exit' as a special treatment
//	 * for Java programs
//	 * 
//	 * @param cfg
//	 * @param currentlyInstrumentedSootMethod
//	 * @return
//	 */
//	private int injectTerminationCode(CFG cfg, SootMethod currentlyInstrumentedSootMethod,
//			// We need to count the call site to inject the "terminate"
//			// event at the end of the method so we ensure we "dump the
//			// events out"
//			PatchingChain<Unit> patchingChain) {
//
//		int nCandCallsites = 0;
//		if (cfg == null) {
//			return nCandCallsites;
//		}
//
//		List<CallSite> callsites = cfg.getCallSites();
//		for (CallSite cs : callsites) {
//
//			if (cs.hasAppCallees() && !cs.isInCatchBlock()) {
//				nCandCallsites++;
//			}
//
//			for (SootMethod ce : cs.getAllCallees()) {
//
//				if (!ce.getSignature().equals("<java.lang.System: void exit(int)>")) {
//					continue;
//				}
//
//				System.out.println(
//						"SceneInstrumenterWithMethodParameters.injectTerminationCode() " + cs.getLoc().getStmt());
//				List<Unit> instrumentationCode = new ArrayList<Unit>();
//
//				// Get the parameter of this call
//				List<StringConstant> terminateArgs = new ArrayList<StringConstant>();
//				terminateArgs.add(StringConstant.v("System.exit"));
//				Stmt sTerminateCall = Jimple.v()
//						.newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorOnTerminateApp.makeRef(), terminateArgs));
//				instrumentationCode.add(sTerminateCall);
//
//				// // -- DEBUG
//				if (opts.debugOut()) {
//					System.out.println("monitor termination instrumented at the end of "
//							+ currentlyInstrumentedSootMethod.getSignature() + " before System.exit. "
//							+ cs.getLoc().getStmt());
//				}
//
//				InstrumManager.v().insertBeforeRedirect(patchingChain, instrumentationCode, cs.getLoc().getStmt());
//			}
//		}
//		return nCandCallsites;
//	}

	private boolean skipMethod(SootMethod currentlyInstrumentedSootMethod) {
		/*
		 * Complex Data Structure given by duafdroid. TODO Not sure this is really
		 * needed
		 */
		// CFG cfg =
		// ProgramFlowGraph.inst().getCFG(currentlyInstrumentedSootMethod);

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

		// Cannot be checked at this point... ).
		// if (!currentlyInstrumentedSootMethod.hasActiveBody()) {
		// System.out.println(
		// "\n\t SKIPPING METHOD: " +
		// currentlyInstrumentedSootMethod.getSignature() + " NOT ACTIVE BODY");
		// return true;
		// }

		// For some weird reason, methods like:
		// <com.farmerbb.notepad.activity.a: void <init>()> which are invoked
		// inside the constructor of MainActivity are not reacheable
		// if (cfg == null || !cfg.isReachableFromEntry()) {
		// System.out.println(
		// "\n\t SKIPPING METHOD: " +
		// currentlyInstrumentedSootMethod.getSignature() + " NOT REACHEABLE");
		// return true;
		// }

		return false;
	}

//	/**
//	 * TODO How do we get the invokation type of this method? or how do we know
//	 * which invocation type can be used to trigger a method ?
//	 * 
//	 * @param cfg
//	 * @param sootClassCurrentlyInstrumented
//	 * @param currentlyInstrumentedMethod
//	 * @return
//	 */
//	private List<Unit> generateMonitoringInitializationCode(Set<SootMethod> entryMethods, List<SootClass> entryClasses,
//			//
//			SootClass sootClassCurrentlyInstrumented, SootMethod currentlyInstrumentedMethod) {
//		// Handle Statically Initialized Entry Classes
//		// when there is a static initializer in the entry class, we will
//		// instrument the monitor initialize in there instead of the entry
//		// method
//
//		boolean hasCLINIT = false;
//		if (entryClasses.contains(sootClassCurrentlyInstrumented)) {
//			try {
//				SootMethod cl = sootClassCurrentlyInstrumented.getMethodByName("<clinit>");
//				hasCLINIT = (cl != null);
//			} catch (Exception e) {
//				// if exception was thrown, either because there are more
//				// than one such method (ambiguous) or there is none
//				hasCLINIT = (e instanceof RuntimeException && e.toString().contains("ambiguous method"));
//			}
//		}
//		boolean bInstProgramStart = false;
//		if (opts.sclinit()) {
//			bInstProgramStart = (hasCLINIT && currentlyInstrumentedMethod.getName().equalsIgnoreCase("<clinit>"))
//					|| (!hasCLINIT && entryMethods.contains(currentlyInstrumentedMethod));
//			if (!bProgramStartInClinit) {
//				bProgramStartInClinit = (hasCLINIT
//						&& currentlyInstrumentedMethod.getName().equalsIgnoreCase("<clinit>"));
//			}
//		} else {
//			bInstProgramStart = entryMethods.contains(currentlyInstrumentedMethod);
//		}
//
//		List<Unit> instrumCode = new ArrayList<>();
//
//		// when "-sclinit" option specified, instrument monitor
//		// initialize in <clinit> if any, otherwise in entry method
//		if (bInstProgramStart) {
////			if (opts.debugOut()) {
//				System.out.println("\t\t\t Injected monitorInitialize into : " + currentlyInstrumentedMethod);
////			}
//			// before the entry method executes, the monitor needs be
//			// initialized
//			List<StringConstant> initializeArgs = new ArrayList<StringConstant>();
//			initializeArgs.add(StringConstant.v(utils.getAPKName()));
//			Stmt sInitializeCall = Jimple.v()
//					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(monitorInitialize.makeRef(), initializeArgs));
//			instrumCode.add(sInitializeCall);
//		}
//
//		return instrumCode;
//	}

	/**
	 * Add to the method a Trap that captures Throwable t and re-throws it. To be
	 * safe we add this try-catch-all in any case, as a method might have a trap
	 * that captures Throwable but does something else later that raises an
	 * exception which we will lose if we do not wrap everything !
	 * 
	 * 
	 * 
	 * TODO How do we handle FINALLY ?
	 * 
	 * TODO Handle synchronized blocks ?
	 * 
	 * TODO Uniform the method signature to receive always the same input...
	 * 
	 * @param currentlyInstrumentedMethod
	 */
	public void wrapTryCatchAllBlocks(SootMethod currentlyInstrumentedMethod) {

		SootClass throwableSootClass = Scene.v().getSootClass("java.lang.Throwable");
		assert throwableSootClass != null;
		RefType throwableClassRefType = RefType.v(throwableSootClass);

		/*
		 * TODO Is this really necessary? I think this is implied... add
		 * "throws Throwable" declaration if absent so that we add exception rethrowing
		 * statement in the catch block to be added
		 * m.addExceptionIfAbsent(Scene.v().getSootClass("java.lang.Throwable")) ;
		 */
		// TODO I do not think this is by no means necessary
		// if (!currentlyInstrumentedMethod.throwsException(sce)) {
		// currentlyInstrumentedMethod.addException(sce);
		// }

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
		if (lastUnitOfCurrentlyInstrumentedBodyUnitChain instanceof ThrowStmt && body.getTraps().size() >= 1) {
			/*
			 * This happens when the whole body is nested in a synchronized block
			 */
			lastUnitOfCurrentlyInstrumentedBodyUnitChain = (Stmt) body.getTraps().getLast().getBeginUnit();
		}

		/*
		 * Empty method won't cause any exception so we don't need instrument it at all
		 */
		if (startOfTheMethod == lastUnitOfCurrentlyInstrumentedBodyUnitChain) {
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

		List<Stmt> instrumentationCode = new ArrayList<Stmt>();

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
		instrumentationCode.add(rethrowTheException);

		//
		currentlyInstrumentedMethodBodyUnitChain.addAll(instrumentationCode);
		// We now need to inject the code into the method body and then define how the
		// trap looks like/can be reached?.
//		UtilInstrumenter.instrumentBeforeNoRedirect(currentlyInstrumentedMethodBodyUnitChain, instrumentationCode,
//				lastUnitOfCurrentlyInstrumentedBodyUnitChain);

		// I am not sure this makes sense to use handlerStmt as the unit after the last
		// (return)
		Trap trap = Jimple.v().newTrap(throwableSootClass, startOfTheMethod,
				lastUnitOfCurrentlyInstrumentedBodyUnitChain, handlerStmt);
		// Be sure the trap is the last one
		body.getTraps().addLast(trap);
	}

}
// -- public class icgInst
