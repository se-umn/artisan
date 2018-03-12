package de.unipassau.instrumentation;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.data.Pair;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.options.Options;

/**
 * TODO Open points: - Tracking return values for invoke that are not assigned.
 * This might not be necessary unless we need deep regression assertion ? - Dump
 * XML is not implemented yet, please check the original Soot_Instrumentation
 * project
 * 
 * @author gambi
 *
 */
public class InstrumentTracer extends BodyTransformer {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentTracer.class);

	@Override
	protected void internalTransform(final Body body, String phaseName, @SuppressWarnings("rawtypes") Map options) {

		final SootMethod containerMethod = body.getMethod();

		// Filter methods that we do not want to instrument
		// TODO Make this parameteric
		// FOR EXAMPLE external Library
		if (filterMethod(containerMethod)) {
			logger.info("Skip instrumentation of: " + containerMethod);
			return;
		}

		if (isExternalLibrary(containerMethod)) {
			// Probably we can do something about it
			logger.info("Skip instrumentation of: " + containerMethod);
			return;
		}

		final PatchingChain<Unit> units = body.getUnits();
		for (final Iterator<Unit> iter = units.snapshotIterator(); iter.hasNext();) {
			final Unit u = iter.next();

			u.apply(new AbstractStmtSwitch() {

				public void caseAssignStmt(AssignStmt stmt) {
					// TODO Refactor. Use Jimple constants a not plain string...
					logger.trace("Inside method " + containerMethod + " caseAssignStmt() " + stmt);
					boolean containsInvokeExpr = stmt.containsInvokeExpr();
					if (containsInvokeExpr) {

						InvokeExpr invokeExpr = stmt.getInvokeExpr();

						String invokeType = "";
						Value instanceValue = null;

						// TODO Some methods like, string builder and string
						// operations are not tracked at the moment
						SootMethod m = invokeExpr.getMethod();

						if (doNotTraceCallsTo(m)) {
							logger.info("Do not trace calls to " + m);
							return;
						}

						if (invokeExpr instanceof InstanceInvokeExpr) {
							invokeType = "InstanceInvokeExpr";
							instanceValue = ((InstanceInvokeExpr) invokeExpr).getBase();
						}
						if (invokeExpr instanceof SpecialInvokeExpr) {
							invokeType = "SpecialInvokeExpr";
							instanceValue = ((SpecialInvokeExpr) invokeExpr).getBase();
						}
						if (invokeExpr instanceof VirtualInvokeExpr) {
							invokeType = "VirtualInvokeExpr";
							instanceValue = ((VirtualInvokeExpr) invokeExpr).getBase();
						}
						if (invokeExpr instanceof StaticInvokeExpr) {
							// invokeType = Jimple.STATICINVOKE; == staticinvoke
							invokeType = "StaticInvokeExpr";

						}
						if (invokeExpr instanceof NewInvokeExpr) {
							invokeType = "NewInvokeExpr";

						}
						if (invokeExpr instanceof InterfaceInvokeExpr) {
							invokeType = "InterfaceInvokeExpr";
							instanceValue = ((InterfaceInvokeExpr) invokeExpr).getBase();

						}
						if (invokeExpr instanceof DynamicInvokeExpr) {
							invokeType = "DynamicInvokeExpr";

						}

						logger.trace(
								"New assignment invoke of :" + m.getSignature() + " instanceValue " + instanceValue);

						// Here we instrument the invocation inside the give
						// method.
						// if (!filterMethod(m)) {
						instrumentInvokeExpression(body, units, u, invokeExpr.getMethod(), invokeExpr.getArgs(), //
								stmt.getLeftOp(), // Return value ? Or the
													// target of the assign
													// ?
								instanceValue, invokeType, true);
						// }
					}
				}

				/*
				 * When soot triggers this, since there is no assignment of the
				 * return value, we cannot access it... probably, if we need to
				 * track this value as well, we can introcude a new assignement
				 * and attach it to the invoke stmt. This is also invokes inside
				 * if/for/etc. when there's no assignments...I hope that in case
				 * of assignment the invoke is triggered only once.
				 */
				public void caseInvokeStmt(InvokeStmt stmt) {
					logger.trace("Inside method " + containerMethod + " caseInvokeStmt() " + stmt);

					InvokeExpr invokeExpr = stmt.getInvokeExpr();
					SootMethod m = invokeExpr.getMethod();

					if (doNotTraceCallsTo(m)) {
						logger.info("Do not trace calls to " + m);
						return;
					}

					String invokeType = "";
					Value instanceValue = null;
					if (invokeExpr instanceof InstanceInvokeExpr) {
						invokeType = "InstanceInvokeExpr";
						instanceValue = ((InstanceInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof SpecialInvokeExpr) {
						invokeType = "SpecialInvokeExpr";
						instanceValue = ((SpecialInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof VirtualInvokeExpr) {
						invokeType = "VirtualInvokeExpr";
						instanceValue = ((VirtualInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof StaticInvokeExpr) {
						invokeType = "StaticInvokeExpr";
						// Cannot be associate to any "value/instance"
					}
					if (invokeExpr instanceof NewInvokeExpr) {
						invokeType = "NewInvokeExpr";
						// TODO What's this ?!

					}
					if (invokeExpr instanceof InterfaceInvokeExpr) {
						invokeType = "InterfaceInvokeExpr";
						instanceValue = ((InterfaceInvokeExpr) invokeExpr).getBase();
					}
					if (invokeExpr instanceof DynamicInvokeExpr) {
						invokeType = "DynamicInvokeExpr";

					}

					// Why the hell we pass null as returnValue ?! Because
					// it is hard to get that value from
					// the invoke expression
					Value returnValue = null;
					// This creates many problems if the return type is
					// primitive... !
					logger.debug("TODO: How do we get the value resulting from this call ? " + invokeExpr
							+ " Maybe we can match this to a return statement somewhere ?");

					// The following method it HUGE !!!
					instrumentInvokeExpression(body, units, u, invokeExpr.getMethod(), invokeExpr.getArgs(),
							returnValue, instanceValue, invokeType, false);

				}

			});
		}

	}

	/**
	 * Check if the current method matches any of the external library calls or
	 * it belongs to a class marked as implementing an external library
	 * 
	 * @param containerMethod
	 * @return
	 */
	private boolean isExternalLibrary(SootMethod containerMethod) {
		// TODO Auto-generated method stub
		return containerMethod.getSignature().contains("microsoft") || //
				containerMethod.getSignature().contains("java.sql.");
	}

	/**
	 * BodyTransformer repeats for every execution, so to make a check that
	 * every method is instrumented only once I implement HashSet for every
	 * function. Can be done using class as well, but i'll look into that later.
	 *
	 * 
	 * @param body
	 * @param units
	 * @param u
	 * @param method
	 * @param parameterValues
	 * @param returnValue
	 * @param instanceValue
	 * @param invokeType
	 */
	private void instrumentInvokeExpression(//
			Body body, // Method body of the method which contains <method>
			final PatchingChain<Unit> units, //
			Unit u, //
			//
			SootMethod method, // The method which is Invoked this one is the
								// one we want to wrap: call traceStart and
								// traceObject (only non static) before the
								// actual call, and traceStop after the call
			List<Value> parameterValues, // The referes to value parameter. The
											// actual parameter
			Value returnValue, //
			/*
			 * The instance upon which the method is invoked or null if this is
			 * static method
			 */
			Value instanceValue, //
			/*
			 * Type of invocation.
			 */
			String invokeType, //
			/*
			 * The return value of the invoke might not be used in the code.
			 * Soot return null for it, which breaks the code if the return
			 * value is primitive. Primitive types cannot be null. This is patch
			 * to be removed upon refactoring
			 */
			boolean isRead

	) {
		traceMethodCall(body, units, u, method, parameterValues, returnValue, instanceValue, invokeType, isRead);

		// TODO XML SERIALIZATION
		// dumpToXML(b, units, u, method, parameterValues, returnValue,
		// instanceValue, invokeType);
	}

	// }

	// TODO Double check that the original implementation does nothing more than
	// this.
	private void traceMethodCall(Body b, final PatchingChain<Unit> units, Unit u, SootMethod method,
			List<Value> parameterList, Value returnValue, Value instanceValue, String invokeType, boolean isRead) {
		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		addTraceStart(invokeType, method.getSignature(), parameterList, b, units, u);
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		addTraceStop(method, returnValue, isRead, b, units, u);
		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			addTraceObject(instanceValue, method, units, u);
		}

	}

	private void addTraceObject(Value instanceValue, SootMethod method, final PatchingChain<Unit> units, Unit u) {
		final SootMethod methodObject = Scene.v()
				.getMethod("<de.unipassau.tracing.Trace: void methodObject(java.lang.String,java.lang.Object)>");

		// StickType ?
		// If the method is not Static, i.e., instance is not null - maybe
		// there's a better heuristic to identify static methods ?
		if (instanceValue != null) {
			// Trace.methodObject requires
			// String -> methodName
			// Object -> "this" / invocation owner if any

			List<Value> methodObjectParameters = new ArrayList<Value>();
			// Method Name
			methodObjectParameters.add(StringConstant.v(method.getSignature()));
			//
			methodObjectParameters.add(instanceValue);

			// Generate the invocation to Trace.traceObject
			final Stmt callTracerMethodObject = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodObject.makeRef(), methodObjectParameters));

			units.insertAfter(callTracerMethodObject, u);
		}
	}

	private void addTraceStop(SootMethod method, Value returnValue, boolean isRead, Body body,
			final PatchingChain<Unit> units, Unit u) {

		final SootMethod methodStop = Scene.v().getMethod(
				"<de.unipassau.tracing.Trace: void methodStop(java.lang.String,java.lang.String,java.lang.Object)>");

		// Trace.methodStop requires
		// String -> methodName
		// Object -> Return value
		List<Value> methodStopParameters = new ArrayList<Value>();

		// Method Name
		methodStopParameters.add(StringConstant.v(method.getSignature()));

		// Is Read ? Is Used ? Patch
		methodStopParameters.add(StringConstant.v(Boolean.valueOf(isRead).toString()));

		// This box return value or null or void constants
		Pair<Value, List<Unit>> tmpReturnValueAndInstructions = null;
		if (method.getReturnType() instanceof VoidType || method.getReturnType().toString().equals("void")) {
			// This is a dummy placeholder ! isRead is false anyway
			// tmpReturnValueAndInstructions =
			// UtilInstrumenter.generateReturnValue(StringConstant.v("void"),
			// b);
			tmpReturnValueAndInstructions = new Pair<Value, List<Unit>>(StringConstant.v("void"),
					new ArrayList<Unit>());
			logger.debug("After void/init");

		} else if (returnValue == null) {
			logger.trace("InstrumentTracer.traceMethodCall() 	or " + method + " is read " + isRead);
			tmpReturnValueAndInstructions = new Pair<Value, List<Unit>>(StringConstant.v("not-read"),
					new ArrayList<Unit>());
		} else {
			tmpReturnValueAndInstructions = UtilInstrumenter.generateReturnValue(returnValue, body);
		}

		// Append to the array the possibly Boxed return value
		methodStopParameters.add(tmpReturnValueAndInstructions.getFirst());
		final Stmt callStmtMethodStop = Jimple.v()
				.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodStop.makeRef(), methodStopParameters));
		// Append the instructions to units.
		tmpReturnValueAndInstructions.getSecond().add(callStmtMethodStop);

		units.insertAfter(tmpReturnValueAndInstructions.getSecond(), u);

	}

	private void addTraceStart(String invokeType, String methodSignature, List<Value> parameterList, Body body,
			final PatchingChain<Unit> units, Unit u) {

		final SootMethod methodStart = Scene.v().getMethod(
				"<de.unipassau.tracing.Trace: void methodStart(java.lang.String,java.lang.String,java.lang.Object[])>");

		// Trace.methodStart requires
		// String -> methodType
		// String -> methodName
		// String -> Parameter Array...
		List<Value> methodStartParameters = new ArrayList<Value>();

		// Method Type
		methodStartParameters.add(StringConstant.v(invokeType));

		// Method Name
		methodStartParameters.add(StringConstant.v(methodSignature));

		// Parameter Array

		// Returns a Pair which contains the array and the instructions to
		// create it
		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter.generateParameterArray(parameterList,
				body);
		// Append the array to the parameters for the trace start
		methodStartParameters.add(tmpArgsListAndInstructions.getFirst());

		// Insert the instructions to create the array before using it, i.e,
		// before calling Trace.start

		// Create the invocation object
		final Stmt callTracerMethodStart = Jimple.v()
				.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodStart.makeRef(), methodStartParameters));

		// Append the trace start call as last after the instructions to prepare
		// its parameters
		tmpArgsListAndInstructions.getSecond().add(callTracerMethodStart);

		// Add this code before invoking the method
		units.insertBefore(tmpArgsListAndInstructions.getSecond(), u);

		logger.trace("\n" + "InstrumentTracer.traceCall() Start Method Parameter List " + methodStartParameters + "\n"
				+ "InstrumentTracer.traceCall() Start Method Instructions" + tmpArgsListAndInstructions.getSecond());
	}

	// TODO Add output folder to soot options
	public static void main(String[] args) throws URISyntaxException {
		String phaseName = "jtp.mainInstrumentation";
		String projectJar = args[0];
		if (args.length >= 2) {
			String outputDir = args[1];
			System.out.println("InstrumentTracer.main() Output to " + outputDir);
			Options.v().set_output_dir(outputDir);
		}
		String[] sootArguments = new String[] { "-f", "class" };
		// Output should be in .class format unless specified otherwise
		// Declare "jimple" for debug instrumented code
		if (args.length >= 3) {
			sootArguments = new String[] { "-f", args[2] };
		}

		long startTime = System.nanoTime();
		// Setup the Soot settings
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);
		Options.v().set_soot_classpath(System.getProperty("java.path"));
		//

		// TODO Get this from input
		// Maybe use some sort of CLI/JewelCLI ?
		List<String> excludeList = new ArrayList<>();

		// Packages that Soot shall not look into can be brutally filtered out
		// at
		// this level as well. As long as the allow_phantom_ref is true,
		// everything is fine.
		// The more we remove, the faster the analysis gets.
		// For example, we can include here all the external interfaces already
		// !
		// FIXME: This might be a bit too much
		excludeList.add("com.thoughtworks.xstream.*");
		// Maybe logger as well ?
		excludeList.add("de.unipassau.*");

		Options.v().set_exclude(excludeList);
		// THIS IS ONLY FOR TESTING
		List<String> includeList = new ArrayList<>();
		includeList.add("de.unipassau.testsubject.*");
		Options.v().set_include(includeList);

		Options.v().set_no_bodies_for_excluded(true);

		InstrumentTracer it = new InstrumentTracer();

		PackManager.v().getPack("jtp").add(new Transform(phaseName, it));

		// Supporting jars are Xstream and trace
		// TODO HardCoded !
		// String xStreamJar = "./src/main/resources/xstream-1.4.9.jar";
		// AT THE MOMENT I CANNOT FIND A WAY TO HAVE SOOT USING THE SINGLE TRACE
		// CLASS

		String traceJar = "./libs/trace.jar"; // Eclipse testing

		if (!new File(traceJar).exists()) {
			traceJar = "../libs/trace.jar"; // Actual usage ...
			if (!new File(traceJar).exists()) {
				throw new RuntimeException(new File(traceJar).getAbsolutePath() + "file is missing");
			}
		}

		// This is the application under analysis. 1 jar -> 1 entry
		ArrayList<String> list = new ArrayList<String>();
		list.add(projectJar);
		list.add(traceJar);
		Options.v().set_process_dir(list);

		// Patch for Mac OS.
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", "Mac OS X");

		// Run Soot
		soot.Main.main(sootArguments);

		long endTime = System.nanoTime();
		logger.debug("System test instrumentation took " + (endTime - startTime) + " ns");

	}

	// Supporting methods
	/**
	 * Avoid tracing of library methods or methods which implement external
	 * interfaces.
	 * 
	 * @param m
	 * @return
	 */
	private boolean filterMethod(SootMethod m) {
		return m.isJavaLibraryMethod() || //
				m.getSignature().contains("org.slf4j") || // Our Logger
				m.getSignature().contains("xstream") || // Dump XML
				m.getSignature().contains("de.unipassau.tracing.Trace") || // ABC
																			// instrastructre
				//
				false;
	}

	private boolean doNotTraceCallsTo(SootMethod m) {
		return m.getDeclaringClass().getName().equals("java.lang.StringBuilder") || //
				m.getDeclaringClass().getName().equals("java.io.PrintStream") || //
				// Is this ok ?!
				m.getDeclaringClass().getName().equals("java.lang.Object") || //
				false;
	}

}
