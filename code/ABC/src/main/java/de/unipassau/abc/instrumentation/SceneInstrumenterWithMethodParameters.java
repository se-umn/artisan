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
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import de.unipassau.abc.ABCTag;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
//
import dua.Extension;
import dua.Forensics;
import dua.global.ProgramFlowGraph;
//import dua.method.CFG;
//import dua.method.CallSite;
//import dynCG.Options;
//
import profile.InstrumManager;
import profile.UtilInstrum;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.toolkits.graph.ExceptionalUnitGraph;
import utils.utils;

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
 * 
 * @author gambi
 *
 */
public class SceneInstrumenterWithMethodParameters implements Extension {

	/*
	 * This is the class which implements the Trace/Monitoring interface. Since,
	 * this is meta-programming we will get reference to the methods implemented by
	 * this class and call them from the instrumentation code
	 */
	private static final String MONITOR_CLASS = Monitor.class.getName();

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

	protected File fJimpleOrig = null;
	protected File fJimpleInsted = null;

	protected static boolean bProgramStartInClinit = false;
//	protected static Options opts = new Options();

	// whether instrument in 3rd party code such as android.support.v$x
	public static boolean g_instr3rdparty = false;

	public static void main(String args[]) {
//		args = preProcessArgs(opts, args);

		SceneInstrumenterWithMethodParameters icgins = new SceneInstrumenterWithMethodParameters();
		// examine catch blocks
		dua.Options.ignoreCatchBlocks = false;

		// DOES THIS GIVE USE THE ENTRY METHOD ?
		// This model android life cycles and force the creation of a dummy main
		// entry point which include all the lifecycle events
		// for the app. Still not sure this is the right thing to get those
		// life-cycle event methods.
		// It takes a lot to start and collect all those data.
		// TODO The goal is to properly instrument methods such as onCreate
		// (since we capture it;s start, we might need to capture it's "end")
		// using true also leads to an exception
		dua.Options.modelAndroidLC = false;

		// This disable creating the PCG which I am not 100% sure we need....
		dua.Options.skipDUAAnalysis = false;
		// This might simply indicate we are processing android apps
		dua.Options.analyzeAndroid = true;

		soot.options.Options.v().set_src_prec(soot.options.Options.src_prec_apk);

		// output as APK, too//-f J
		soot.options.Options.v().set_output_format(soot.options.Options.output_format_dex);
		soot.options.Options.v().set_force_overwrite(true);

		// TODO What's this?
//		Scene.v().addBasicClass("com.ironsource.mobilcore.BaseFlowBasedAdUnit", SootClass.SIGNATURES);
		
		// Tell Soot about our Monitor class
		Scene.v().addBasicClass(MONITOR_CLASS);

		// This is used also when monitorICC is disabled
		Scene.v().addBasicClass("utils.logicClock");

		// TODO This is the Duafdroid machiney we should get rid of
		Forensics.registerExtension(icgins);
		Forensics.main(args);
	}

//	protected static String[] preProcessArgs(Options _opts, String[] args) {
//		opts = _opts;
//		args = opts.process(args);
//
//		String[] argsForDuaF;
//		int offset = 0;
//
//		argsForDuaF = new String[args.length + 2 - offset];
//		System.arraycopy(args, offset, argsForDuaF, 0, args.length - offset);
//		argsForDuaF[args.length + 1 - offset] = "-paramdefuses";
//		argsForDuaF[args.length + 0 - offset] = "-keeprepbrs";
//
//		return argsForDuaF;
//	}

	/**
	 * Descendants may want to use customized event monitors
	 */
	protected void init() {
		try {
			clsMonitor = Scene.v().getSootClass(MONITOR_CLASS);
			clsMonitor.setApplicationClass();
			// TODO Not sure we are actually using any of those
			Scene.v().getSootClass("utils.MethodEventComparator").setApplicationClass();

			// This is used also outside monitorICCC so we need to explicitly
			// load
			// it into soot
			Scene.v().getSootClass("utils.logicClock").setApplicationClass();

//			if (opts.monitorEvents()) {
//				Scene.v().getSootClass("eventTracker.Monitor").setApplicationClass();
//			}

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

	public void run() {
		g_instr3rdparty = false;

		init();

		try {
			instrument();
		} catch (IOException | XmlPullParserException e) {
			throw new RuntimeException(e);
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
	 * @throws XmlPullParserException
	 * @throws IOException
	 */

	public void instrument() throws IOException, XmlPullParserException {

		System.out.println("---- SceneInstrumenterWithMethodParameters.instrument() ----");

		// Record the entry methods/classes for this application.
		// TODO Problem is, there's none, so we never call monitor terminate.
		// Which might or might not be good...
		Set<SootMethod> entryMethods = utils.getEntryMethods(true);

		List<SootClass> classesContainingTheEntryMethods = null;
//		if (opts.sclinit()) {
		classesContainingTheEntryMethods = new ArrayList<SootClass>();
		for (SootMethod entryMethod : entryMethods) {
			classesContainingTheEntryMethods.add(entryMethod.getDeclaringClass());
		}
//		}
//		if (opts.debugOut()) {
		System.out.println("\n ENTRY METHODS");
		for (SootMethod entryMethod : entryMethods) {
			System.out.println("\t " + entryMethod.getSignature());
		}
//		}
		// TODO IN SOME PLACES REFLECTIVE CALLS ARE NOT HANDLED !

		/*
		 * Iterate over all the user classes. Those include any class which is generated
		 * to fit android (i.e., 1 class 1 smali). TODO Do we really need this program
		 * flow graph object?
		 */
		Iterator<SootClass> applicationClassesIterator = ProgramFlowGraph.inst().getUserClasses().iterator();
		while (applicationClassesIterator.hasNext()) {
			SootClass currentlyInstrumentedSootClass = (SootClass) applicationClassesIterator.next();

			if (skipClass(currentlyInstrumentedSootClass)) {
				System.out.println("\n\n SKIPPING CLASS " + currentlyInstrumentedSootClass.getName());
				System.out.println("\n\n");
				continue;
			} else {
				System.out.println("\n\n INSTRUMENTING CLASS " + currentlyInstrumentedSootClass.getName());
			}

			/*
			 * This explicitly tag the synthetic methods and its calls
			 */
			makeInterestingLifeCycleEventsExplicit(currentlyInstrumentedSootClass);

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

//				if (opts.debugOut()) {
				System.out.println("\t INSTRUMENTING ");
				for (Unit u : currentlyInstrumentedMethodBody.getUnits()) {
					System.out.println("\t\t " + u);
				}
//				}

				wrapTryCatchAllBlocks(currentlyInstrumentedMethod);

//				if (opts.debugOut()) {
				System.out.println("\t FORCE Try-Finally over the method:");
				for (Unit u : currentlyInstrumentedMethodBody.getUnits()) {
					System.out.println("\t\t " + u);
				}
//				}

				/*
				 * Global variables
				 */
				PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain = currentlyInstrumentedMethodBody
						.getUnits();

				// TODO
				// boolean hasFinally =
				// getFinally(currentlyInstrumentedMethodBody.toString());

				/*
				 * To ensure monitor is initialized we inject initialization code. Note that we
				 * apply this later because we need to put this code at the very beginning of
				 * the method (possibly BEFORE the methodBegins probe!). THIS IS NEED IF THERE"S
				 * CLINIT IN THE ENTRY CLASSES< BUT WE DO NOT HAVE ENTRY CLASSES SOMEHOW ?
				 */
				/*
				 * To Ensure we log app methods being called we insert an methodEnter probe at
				 * the beginning of the currentlyInstrumentedMethod. Private methods are tagged
				 * as such
				 */
				instumentMethodBegins(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
						currentlyInstrumentedMethodBodyUnitChain);
				/*
				 * Inject instrumentation code before any return/throw statement in the method.
				 * This might also require to explicitly introdyce try/catch statements to
				 * capture (and rethrow) also undeclared exceptions
				 */

				instrumentMethodEnds(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
						currentlyInstrumentedMethodBodyUnitChain);

				/*
				 * Instrument the methods which do not belong to user called but are called by
				 * the currentlyInstrumentedMethod
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
				instrumentMethodBody(currentlyInstrumentedMethod, currentlyInstrumentedMethodBody,
						currentlyInstrumentedMethodBodyUnitChain);

				/*
				 * TODO: To Ensure we log app when the app is terminated using System.exit we
				 * insert a probe before calling System.exit
				 */
				// injectTerminationCode(cfg,
				// currentlyInstrumentedSootMethod,
				// currentlyInstrumentedMethodBodyUnitChain);

				// PRINT THE INSTRUMENTED CODE
//				if (opts.debugOut()) {
				System.out.println("\n\t INSTRUMENTED");
				// Print the units of this method
				for (Unit u : currentlyInstrumentedMethodBody.getUnits()) {
					System.out.println((u.getTags().contains(ABCTag.TAG) ? "**" : "") + "\t\t " + u);
				}
//				}

			} // -- while (meIt.hasNext())

		} // -- while (clsIt.hasNext())

		// important warning concerning the later runs of the instrumented
		// subject
		if (bProgramStartInClinit) {
			System.out.println("\n***NOTICE***: program start event probe has been inserted in EntryClass::<clinit>(), "
					+ "the instrumented subject MUST thereafter run independently rather than via EARun!");
		}
	} // --

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

	private void instrumentMethodBody(final SootMethod currentlyInstrumentedMethod,
			final Body currentlyInstrumentedMethodBody,
			final PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain) {

		for (final Iterator<Unit> iter = currentlyInstrumentedMethodBodyUnitChain.snapshotIterator(); iter.hasNext();) {

			final Unit currentUnit = iter.next();

			if (currentUnit.getTags().contains(ABCTag.TAG)) {
				/*
				 * Do not instrument our instrumentation ...
				 */
				continue;
			}

			/*
			 * Instrument all the libCall method invocations by wrapping them
			 */
			currentUnit.apply(new AbstractStmtSwitch() {

				private void wrapLibraryInvocationIfAny(Stmt csStmt) {
					if (!csStmt.containsInvokeExpr()) {
						return;
					}

					InvokeExpr invokeExpr = csStmt.getInvokeExpr();
					SootMethod invokedMethod = invokeExpr.getMethod();
					SootClass invokedClass = invokedMethod.getDeclaringClass();

					/*
					 * Avoid double instrumentation for methods in user classes
					 */
					if (ProgramFlowGraph.inst().getUserClasses().contains(invokedClass)) {
						return;
					}

//					if (opts.debugOut()) {
					System.out.println("\t\t\t WRAP onMethod for call site " + invokedMethod + " inside "
							+ currentlyInstrumentedMethod.getSignature());
//					}

					List<Unit> instrumentationCodeBefore = new ArrayList<>();
					List<Value> monitorOnLibCallParameters = new ArrayList<Value>();

					// Object methodOwnerOrEmptyString
					Value csOwner = null;
					if (invokedMethod.isStatic()) {
						/*
						 * TODO Why empty and not NullConstant.v()
						 */
						csOwner = NullConstant.v(); // StringConstant.v("");
					} else if (invokedMethod.isConstructor()) {
						csOwner = NullConstant.v(); // StringConstant.v("");
					} else {
						if (invokeExpr instanceof InstanceInvokeExpr) {
							csOwner = ((InstanceInvokeExpr) invokeExpr).getBase();
						} else {
							System.out.println("Not sure how to handle owner for " + invokeExpr);
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
							Jimple.v().newStaticInvokeExpr(monitorOnLibCall.makeRef(), monitorOnLibCallParameters));
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
							System.out.println("Not sure how to handle owner for " + invokeExpr);
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
						if (((AssignStmt) csStmt).getLeftOp().equals(csOwner)
								&& !JimpleUtils.isString(csOwner.getType())) {

							Local originalOwnerHolder = UtilInstrumenter
									.generateFreshLocal(currentlyInstrumentedMethodBody, csOwner.getType());

							AssignStmt capturingAssignStmt = Jimple.v().newAssignStmt(originalOwnerHolder, csOwner);
							// Since the new local assignment should trigger
							// BEFORE the method call we add it to:
							// instrumentationCodeBefore
							instrumentationCodeBefore.add(capturingAssignStmt);
							// TODO Store Assign
							// Use the originalOwnerHolder instead of the one to
							// be overwritten

							// if (opts.debugOut()) {
							// System.out.println("\t\t\t >>>> APPLY PATCH TO
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
					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, csStmt,
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
					monitorOnReturnIntoParameters.add(UtilInstrumenter.generateCorrectObject(
							currentlyInstrumentedMethodBody, returnValueFromCallSite, instrumentationCodeAfter));
					// Prepare the actual call
					final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
							.newStaticInvokeExpr(monitorOnReturnInto.makeRef(), monitorOnReturnIntoParameters));
					instrumentationCodeAfter.add(onReturnIntoCall);

					/*
					 * Finally inject the instrumentation code AFTER the target which is either the
					 * original location or the new one stmt, after replacin invoke with assign
					 */
					// currentlyInstrumentedMethodBodyUnitChain.insertAfter(instrumentationCodeAfter,
					// targetStmt);
					instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, csStmt,
							instrumentationCodeAfter);
				}

				/**
				 * Arrays do not have an INIT function, they are not objects, so we fake one the
				 * moment we see newarray. We need to wrap the call to their constructor
				 * 
				 * @param body
				 * @param units
				 * @param currentUnit
				 * @param array
				 * @param arraySize
				 */
				private void instrumentArrayInitExpression(AssignStmt stmt) {

//					if (opts.debugOut()) {
					System.out.println(
							"\t\t\t WRAP arrayInit " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());
//					}

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
					final Stmt callTracerMethodStart = Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(monitorOnLibCall.makeRef(), monitorOnLibCallParameters));
					// Append the call to the instrumentation code
					instrumentationCodeBefore.add(callTracerMethodStart);

					// Inject the code (not sure we need to tag it or not...)
					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
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
					final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
							.newStaticInvokeExpr(monitorOnReturnInto.makeRef(), monitorOnReturnIntoParameters));
					instrumentationCodeAfter.add(onReturnIntoCall);

					instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, instrumentationCodeAfter);

				}

				/**
				 * Simulate a call that returns an element of an array
				 * 
				 * $r3 = $r2[$i0] -> $r3 = $r2.get($i0)
				 */
				private void instrumentArrayAccess(AssignStmt stmt) {

//					if (opts.debugOut()) {
					System.out.println(
							"\t\t\t WRAP arrayGet " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());
//					}

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
					final Stmt callTracerMethodStart = Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(monitorOnLibCall.makeRef(), monitorOnLibCallParameters));
					// Append the call to the instrumentation code
					instrumentationCodeBefore.add(callTracerMethodStart);

					// Inject the code (not sure we need to tag it or not...)
					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
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
					monitorOnReturnIntoParameters.add(UtilInstrumenter.generateCorrectObject(
							currentlyInstrumentedMethodBody, returnValue, instrumentationCodeAfter));
					// Prepare the actual call to Monitor.onReturn
					final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
							.newStaticInvokeExpr(monitorOnReturnInto.makeRef(), monitorOnReturnIntoParameters));
					instrumentationCodeAfter.add(onReturnIntoCall);

					instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, instrumentationCodeAfter);
				}

				/**
				 * Fake a call to a generic method STORE of the array. In JIMPLE those are
				 * ALWAYS ? $r10[0] = $r0 -> void $r10.store( $r0, 0 )
				 * 
				 */
				private void instrumentArrayStore(AssignStmt stmt) {

//					if (opts.debugOut()) {
					System.out.println(
							"\t\t\t WRAP arrayStore " + stmt + " inside " + currentlyInstrumentedMethod.getSignature());
//					}

					Value array = ((ArrayRef) stmt.getLeftOp()).getBase();
					Value position = ((ArrayRef) stmt.getLeftOp()).getIndex();

					Value value = stmt.getRightOp();

					String fakeMethodSignature = "<" + array.getType() + ": void store(int,"
							+ array.getType().toString().replaceFirst("\\[\\]", "") + ")>";

					List<Unit> instrumentationCodeBefore = new ArrayList<>();

					List<Value> monitorOnLibCallParameters = new ArrayList<Value>();
					// There owner is the array
					monitorOnLibCallParameters.add(array);
					// String methodSignature
					monitorOnLibCallParameters.add(StringConstant.v(fakeMethodSignature));
					// String Context
					monitorOnLibCallParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
					// methodParameters: Size and whatever is on the right side
					// of the assignment
					List<Value> invocationActualParameters = new ArrayList<>();
					invocationActualParameters.add(position);
					// TODO This might be wrong... If right is field access ?!
					invocationActualParameters.add(value);

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
					final Stmt callTracerMethodStart = Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(monitorOnLibCall.makeRef(), monitorOnLibCallParameters));
					// Append the call to the instrumentation code
					instrumentationCodeBefore.add(callTracerMethodStart);

					// Inject the code (not sure we need to tag it or not...)
					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt,
							instrumentationCodeBefore);

					List<Unit> instrumentationCodeAfter = new ArrayList<>();
					List<Value> monitorOnReturnIntoParameters = new ArrayList<Value>();

					// Owner of the method is the array being created (or its
					// element?!)
					monitorOnReturnIntoParameters.add(array);
					// String csMethodSignature
					monitorOnReturnIntoParameters.add(StringConstant.v(fakeMethodSignature));
					// String context
					monitorOnReturnIntoParameters.add(StringConstant.v(currentlyInstrumentedMethod.getSignature()));
					// Store returns no value
					monitorOnReturnIntoParameters.add(NullConstant.v());
					// Prepare the actual call to Monitor.onReturn
					final Stmt onReturnIntoCall = Jimple.v().newInvokeStmt(Jimple.v()
							.newStaticInvokeExpr(monitorOnReturnInto.makeRef(), monitorOnReturnIntoParameters));
					instrumentationCodeAfter.add(onReturnIntoCall);

					instrumentAfterWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, instrumentationCodeAfter);

				}

				public void caseAssignStmt(AssignStmt stmt) {

					// The right operator is mutually exclusive, either is a
					// regular method call
					// or its a fake one (init, access ?)
					if (stmt.getRightOp() instanceof NewArrayExpr) {
						instrumentArrayInitExpression(stmt);
					} else if (stmt.getRightOp() instanceof ArrayRef) {
						instrumentArrayAccess(stmt);
					} else {
						// Regular method invocation on objects
						wrapLibraryInvocationIfAny(stmt);
					}

					// The wrapped invocations produce a Local... right ?
					if (stmt.getLeftOp() instanceof ArrayRef) {
						instrumentArrayStore(stmt);
					}

					// TODO Not sure what is going to happen if we assign an
					// element of an array to be another element of possibly the
					// same array
				};

				public void caseInvokeStmt(InvokeStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseGotoStmt(soot.jimple.GotoStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseIfStmt(soot.jimple.IfStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseLookupSwitchStmt(soot.jimple.LookupSwitchStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseThrowStmt(soot.jimple.ThrowStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseBreakpointStmt(soot.jimple.BreakpointStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseEnterMonitorStmt(soot.jimple.EnterMonitorStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseExitMonitorStmt(soot.jimple.ExitMonitorStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseReturnStmt(ReturnStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};

				public void caseTableSwitchStmt(soot.jimple.TableSwitchStmt stmt) {
					wrapLibraryInvocationIfAny(stmt);
				};
				// public void caseIdentityStmt(IdentityStmt stmt) {
				// System.out.println( stmt.getClass() + "\t" + stmt );
				// };
				//
				// public void caseNopStmt(soot.jimple.NopStmt stmt) {
				// System.out.println( stmt.getClass() + "\t" + stmt );
				// };
				//
				// public void caseRetStmt(soot.jimple.RetStmt stmt) {
				// System.out.println( stmt.getClass() + "\t" + stmt );
				// };
			});
		}

	}

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

		List<Unit> instrumentationCode = new ArrayList<>();

		/*
		 * Collect the parameters for invoking "monitorOnEnter" ->
		 * dynCG2.Monitor.enter(String apkName, Object methodOwnerOrNull, String
		 * methodSignature, Object[] methodParameters)
		 * 
		 * NOTE parameters must passed using Object[], so primitive types must be
		 * suitably Boxed
		 */
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

			instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, u, instrumentationCode);

			break;
		}

//		if (opts.debugOut()) {
		System.out.println("\t\t\t Injected onEnter at the beginning of the method");
//		}
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

	/**
	 * Ensures we keep track of our instrumentation code by tagging it
	 * 
	 * @param currentlyInstrumentedMethodBodyUnitChain
	 * @param u
	 * @param instrumentationCode
	 */
	private void instrumentBeforeWithAndTag(PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain, Unit u,
			List<Unit> instrumentationCode) {

		for (Unit instrumentation : instrumentationCode) {
			instrumentation.addTag(ABCTag.TAG);
		}

		currentlyInstrumentedMethodBodyUnitChain.insertBefore(instrumentationCode, u);

	}

	/**
	 * Ensures we keep track of our instrumentation code by tagging it
	 * 
	 * @param currentlyInstrumentedMethodBodyUnitChain
	 * @param targetStmt
	 * @param instrumentationCodeAfter
	 */
	private void instrumentAfterWithAndTag(PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain,
			Stmt targetStmt, List<Unit> instrumentationCode) {
		for (Unit instrumentation : instrumentationCode) {
			instrumentation.addTag(ABCTag.TAG);
		}
		currentlyInstrumentedMethodBodyUnitChain.insertAfter(instrumentationCode, targetStmt);

	}

	private boolean skipClass(SootClass currentlyInstrumentedSootClass) {
		// In reality we should instrument anonym classes as well... but how do
		// we get them?!
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

	private void instrumentMethodEnds(SootMethod currentlyInstrumentedSootMethod,
			/*
			 * Need to generate fresh locals and access other elements like "this.local" -
			 * TODO Is this.local the method owner or the method itself?
			 */
			Body currentlyInstrumentedMethodBody,
			/*
			 * Need to inject the code inside the currentlyInstrumentedMethod - TODO Can't
			 * we simply get a new instance of this or shall we pass it around ?
			 */
			PatchingChain<Unit> currentlyInstrumentedMethodBodyUnitChain) {

		/*
		 * To ensure that we log the method ends in the right place, including
		 * exceptional branches and finally blocks we build the exceptionalUnitGraph (a
		 * CFG that includes the exceptional branches), get the exit points of the
		 * method, i.e., the tails and insert the instrumentation right before each of
		 * them.
		 */

		ExceptionalUnitGraph exceptionalUnitGraph = new ExceptionalUnitGraph(
				currentlyInstrumentedSootMethod.getActiveBody());

		String currentlyInstrumentedMethodSignature = currentlyInstrumentedSootMethod.getSignature();
		/*
		 * Tails also includes throw stmt which will be captured by catch blocks/traps
		 * inside the code. Those are not really method exit points so we need to rule
		 * them out.
		 * 
		 * For every ThrowInst or ThrowStmt Unit which may explicitly throw an exception
		 * that would be caught by a Trap in the Body, there will be an edge from the
		 * throw Unit to the Trap handler's first Unit.
		 * 
		 * For every Unit which may implicitly throw an exception that could be caught
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
		List<Unit> potentialExitPoints = new ArrayList<>();
		potentialExitPoints.addAll(exceptionalUnitGraph.getTails());

		for (Iterator<Unit> iterator = potentialExitPoints.iterator(); iterator.hasNext();) {
			// Check if this exit point MIGHT be covered by some catch statement
			Unit exitPoint = iterator.next();

			// Let's conservatively keep return instructions...
			if (exitPoint instanceof ReturnStmt || exitPoint instanceof ReturnVoidStmt) {
				continue;
			}

			for (Trap trap : currentlyInstrumentedSootMethod.getActiveBody().getTraps()) {
				if (exceptionalUnitGraph.getSuccsOf(exitPoint).contains(trap.getHandlerUnit())) {
					System.out.println("\t\t\t Skipping " + exitPoint + " as it is covered by the trap " + trap);
					iterator.remove();
					// Avoid repeatedly remove the "same" object
					break;
				}
			}
		}

		// Use only the exitPoint which our heuristic did not remove
		for (Unit exitPoint : potentialExitPoints) {

			exitPoint.apply(new AbstractStmtSwitch() {

				// The wrapping thingy does not work on constructors
				@Override
				public void caseThrowStmt(ThrowStmt stmt) {
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

					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, instrumentationCode);
					super.caseThrowStmt(stmt);

//					if (opts.debugOut()) {
					System.out.println("\t\t\t Injected returnInto at the end of the method before throwing exception "
							+ ((RefType) stmt.getOp().getType()));
//					}
				}

				@Override
				public void caseReturnStmt(ReturnStmt stmt) {
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
					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, instrumentationCode);
					super.caseReturnStmt(stmt);

//					if (opts.debugOut()) {
					System.out.println("\t\t\t Injected returnInto at the end of the method returning a value");
//					}
				}

				@Override
				public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
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
					instrumentBeforeWithAndTag(currentlyInstrumentedMethodBodyUnitChain, stmt, instrumentationCode);
					super.caseReturnVoidStmt(stmt);

//					if (opts.debugOut()) {
					System.out.println("\t\t\t Injected returnInto at the end of the method before returning (void)");
//					}
				}
			});

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

	public void wrapTryCatchAllBlocks(SootMethod currentlyInstrumentedMethod) {
		// all probes for the try-catch blocks insertion
		List<Stmt> tcProbes = new ArrayList<Stmt>();
		Body b = currentlyInstrumentedMethod.retrieveActiveBody();
		PatchingChain<Unit> pchain = b.getUnits();

		// it is only safe to insert probes after all ID statements
		Stmt sFirstNonId = UtilInstrum.getFirstNonIdStmt(pchain);
		Stmt sLast = (Stmt) pchain.getLast();
		if (sLast instanceof ThrowStmt && b.getTraps().size() >= 1) {
			// this happens when the whole body is nested in a synchronized
			// block
			sLast = (Stmt) b.getTraps().getLast().getBeginUnit();
		}

		// Empty method won't cause any exception so we don't need instrument at
		// all
		if (sFirstNonId == sLast) {
			System.out.println("SceneInstrumenterWithMethodParameters.wrapTryCatchAllBlocks() SPECIAL METHOD ? "
					+ currentlyInstrumentedMethod);
			return;
		}

		/*
		 * add "throws Throwable" declaration if absent so that we add exception
		 * rethrowing statement in the catch block to be added
		 * m.addExceptionIfAbsent(Scene.v().getSootClass("java.lang.Throwable")) ;
		 */
		SootClass sce = Scene.v().getSootClass("java.lang.Throwable");
		assert sce != null;
		// TODO I do not think this is by no means necessary
		// if (!currentlyInstrumentedMethod.throwsException(sce)) {
		// currentlyInstrumentedMethod.addException(sce);
		// }

		// goto the original last statement
		Stmt gtstmt = Jimple.v().newGotoStmt(sLast);
		tcProbes.add(gtstmt);

		// two Locals of the Exception type to be inserted in the catch block

		Local er1 = UtilInstrumenter.generateFreshLocal(b, RefType.v(sce));
		Local er2 = UtilInstrumenter.generateFreshLocal(b, RefType.v(sce));

		// Local er1 = utils.createUniqueLocal(b, "er1", RefType.v(sce));
		// Local er2 = utils.createUniqueLocal(b, "$er2", RefType.v(sce));
		// the ID statement
		/*
		 * We do not tag our code since it does not change the semantic, plus we should
		 * not skip instrumenting it with returnInto calls
		 */
		Stmt ids = Jimple.v().newIdentityStmt(er2, Jimple.v().newCaughtExceptionRef());
		// ids.addTag(ABCTag.TAG);
		// the assignment statement assigning the object for the throw statement
		Stmt ass = Jimple.v().newAssignStmt(er1, er2);
		// ass.addTag(ABCTag.TAG);
		// the throw statement rethrowing the exception caught in the block
		Stmt ths = Jimple.v().newThrowStmt(er1);
		// ths.addTag(ABCTag.TAG);

		// assemble the catch block
		tcProbes.add(ids);
		tcProbes.add(ass);
		tcProbes.add(ths);

		// add the catch block to the patching chain of the given method's body
		if (sLast instanceof IdentityStmt) {
			System.out.println("wrapTryCatchAllBlocks: special case encountered in method "
					+ currentlyInstrumentedMethod.getSignature() + " stmt: " + sLast);
			/*
			 * Stmt tgt = (Stmt)utils.getFirstNonIdUnit(pchain); if (tgt != null) tgt =
			 * (Stmt)pchain.getPredOf(tgt); if (tgt == null) tgt = sLast;
			 * InstrumManager.v().insertAtProbeBottom(pchain, tcProbes, sLast);
			 */
			// TODO So we do not instrument this ?!
			return;
		} else {
			// TODO Still not idea what's the meaning of "BeforeNoRedirect"
			InstrumManager.v().insertRightBeforeNoRedirect(pchain, tcProbes, sLast);
		}
		// finally, we add the trap associated with the newly added catch block
		// TODO DO we need to tag traps ?!
		Trap trap = Jimple.v().newTrap(sce, sFirstNonId, gtstmt, ids);
		b.getTraps().add(trap);
	}

} // -- public class icgInst

/* vim :set ts=4 tw=4 tws=4 */
