package de.unipassau.abc.instrumentation;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.MethodInvocationMatcher;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.options.Options;

/**
 * TODO: handling array store/load as actions handling assignment/read to public
 * fields as actions
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
							logger.trace("Do not trace calls to " + m);
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
								instanceValue, invokeType);// , true);
						// }
					} else {
						// Those are regular assignments...
						// assignemnts and new ?

						if (stmt.containsArrayRef()) {
							ArrayRef arrayRefExp = stmt.getArrayRef();

							instrumentArrayAssignmentExpression(body, units, u,
									//
									arrayRefExp.getBase(), // The ref to arraym
															// is this LeftOp ?!
									arrayRefExp.getIndex(), // The position in
															// the array
									stmt.getRightOp()); // I have no better
														// option at this
														// point...
						}
						
						// Are those exclusive ?
//						if (stmt.containsFieldRef()) {
//							System.out.println("Processing Field REF " + stmt);
//							
//						}
						if( stmt.getRightOp() instanceof NewArrayExpr){
							// This might come wihout an init attached to it 
							System.out.println(
									"InstrumentTracer.internalTransform(...).new AbstractStmtSwitch() {...}.caseAssignStmt() " + stmt);
							
							NewArrayExpr right = (NewArrayExpr) stmt.getRightOp();
							// System.out.println( right.getBaseType() ); String
//							System.out.println( right.getType() );  String[]
							
							instrumentArrayInitExpression(body, units, u,
									// 
									stmt.getLeftOp(), 
									right.getSize());
						}
						
					}
				}

//				@Override
//				public void caseIdentityStmt(IdentityStmt stmt) {
//					System.out.println(
//							"InstrumentTracer.internalTransform(...).new AbstractStmtSwitch() {...}.caseIdentityStmt() " + stmt);
//					super.caseIdentityStmt(stmt);
//				}
//				
				/*
				 * When soot triggers this, since there is no assignment of the
				 * return value, we cannot access it... probably, if we need to
				 * track this value as well, we can introcude a new assignement
				 * and attach it to the invoke stmt. This is also invokes inside
				 * if/for/etc. when there's no assignments...I hope that in case
				 * of assignment the invoke is triggered only once.
				 */
				public void caseInvokeStmt(InvokeStmt stmt) {

					// Most likely we can add a Local to host the value, how do
					// we handle invocations inside IF statement for example?

					logger.trace("Inside method " + containerMethod + " caseInvokeStmt() " + stmt);

					InvokeExpr invokeExpr = stmt.getInvokeExpr();
					SootMethod m = invokeExpr.getMethod();

					if (doNotTraceCallsTo(m)) {
						logger.info("Do not trace calls to " + m);
						return;
					}

					String invokeType = null;
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

					// This creates many problems if the return type is
					// primitive... !
					Value returnValue = null;
					if (!(invokeExpr.getMethod().getReturnType() instanceof VoidType)) {
						// TODO
						logger.debug(
								"InstrumentTracer.instrumentInvokeExpression() Add AssignStmt to access the value of "
										+ stmt + " which is not read ");
						// This is requires for generating regression assertions
						// in case this method is MUT
						returnValue = UtilInstrumenter.generateFreshLocal(body, invokeExpr.getMethod().getReturnType());
						Unit capturingAssignStmt = Jimple.v().newAssignStmt(returnValue, invokeExpr);
						units.insertBefore(capturingAssignStmt, stmt);
					}

					instrumentInvokeExpression(body, units, u, invokeExpr.getMethod(), invokeExpr.getArgs(),
							returnValue, instanceValue, invokeType);

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

	private void instrumentArrayAssignmentExpression(
			Body body, PatchingChain<Unit> units, Unit u, 
			//
			Value array,
			Value arrayIndex, //
			Value rightOp) {

		String invokeType = "ArrayOperation";
		String fakeMethodSignature = "<" + array.getType() + ": void store(int," + array.getType().toString().replace("[]", "") + ")>";

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Value> parameterList = new ArrayList<>();
		parameterList.add( arrayIndex );
		parameterList.add( rightOp );
		addTraceStart(invokeType, fakeMethodSignature, parameterList, body, units, u);
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		addTraceStop(fakeMethodSignature, null, body, units, u);
		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			addTraceObject(array, fakeMethodSignature, body, units, u);
		}

	}
	
	// Arrays do not have an INIT function, they are not objects, so we fake one the moment we see newarray
	private void instrumentArrayInitExpression(
			Body body, PatchingChain<Unit> units, Unit u, 
			//
			Value array,
			Value arraySize) {

		String invokeType = "ArrayOperation";
		// Init <size>
		String fakeMethodSignature = "<" + array.getType() + ": void <init>(int)>";

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		List<Value> parameterList = new ArrayList<>();
		parameterList.add( arraySize );
		addTraceStart(invokeType, fakeMethodSignature, parameterList, body, units, u);
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		addTraceStop(fakeMethodSignature, null, body, units, u);
		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			addTraceObject(array, fakeMethodSignature, body, units, u);
		}

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
			String invokeType) {

		/*
		 * Instrument body to include a call to Trace.start BEFORE the execution
		 * of method, we store its parameters as well.
		 */
		addTraceStart(invokeType, method.getSignature(), parameterValues, body, units, u);
		/*
		 * Instrument body to include a call to Trace.stop AFTER the execution
		 * of method, we store its return type as well.
		 */
		addTraceStop(method.getSignature(), returnValue, body, units, u);
		/*
		 * Instrument body to include a call to Trace.object AFTER the execution
		 * of method, we store the reference to the instance object making the
		 * call unless the method is static. In this case there's no object
		 * instance, hence no trace entry
		 */
		if (!Jimple.STATICINVOKE.equals(invokeType)) {
			addTraceObject(instanceValue, method.getSignature(), body, units, u);
		}

		// TODO XML SERIALIZATION
		// dumpToXML(b, units, u, method, parameterValues, returnValue,
		// instanceValue, invokeType);
	}

	private void addTraceObject(Value instanceValue, String methodSignature, Body body, final PatchingChain<Unit> units,
			Unit u) {

		List<Unit> generated = new ArrayList<Unit>();

		if (instanceValue != null) {
			final SootMethod methodObject = Scene.v().getMethod(
					"<de.unipassau.abc.tracing.Trace: void methodObject(java.lang.String,java.lang.Object)>");
			// Trace.methodObject requires
			// String -> methodName
			// Object -> "this" / invocation owner if any

			List<Value> methodObjectParameters = new ArrayList<Value>();
			// Method Name
			methodObjectParameters.add(StringConstant.v(methodSignature));
			//
			methodObjectParameters.add(instanceValue);

			// Generate the invocation to Trace.traceObject
			final Stmt callTracerMethodObject = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodObject.makeRef(), methodObjectParameters));
			generated.add(callTracerMethodObject);

		}
		//
		units.insertAfter(generated, u);

	}

	private void addTraceStop(// SootMethod method,
			String methodSignature, Value returnValue, Body body, final PatchingChain<Unit> units, Unit u) {

		logger.trace("InstrumentTracer.addTraceStop() for " + methodSignature);
		List<Unit> generated = new ArrayList<Unit>();

		{
			final SootMethod methodStop = Scene.v()
					.getMethod("<de.unipassau.abc.tracing.Trace: void methodStop(java.lang.String,java.lang.Object)>");
			// Trace.methodStop requires
			// String -> methodName
			// Object -> Return value
			List<Value> methodStopParameters = new ArrayList<Value>();

			// Method Name
			methodStopParameters.add(StringConstant.v(methodSignature));

			// This box return value or null or void constants
			Pair<Value, List<Unit>> tmpReturnValueAndInstructions = null;
			if (JimpleUtils.isVoid(JimpleUtils.getReturnType(methodSignature))) { // if
																					// (method.getReturnType()
																					// instanceof
																					// VoidType)
																					// {
//				System.out.println("InstrumentTracer.addTraceStop() VoidType "); // Not
																					// sure
																					// this
																					// is
																					// actually
																					// a
																					// thing...
				tmpReturnValueAndInstructions = new Pair<Value, List<Unit>>(StringConstant.v("void"),
						new ArrayList<Unit>());

			} else if (returnValue == null) {
				throw new RuntimeException("Cannot have a null returnValue at this point " + methodSignature);
			} else {
				tmpReturnValueAndInstructions = UtilInstrumenter.generateReturnValue(returnValue, body);
			}

			// Append to the array the possibly Boxed return value
			methodStopParameters.add(tmpReturnValueAndInstructions.getFirst());
			final Stmt callStmtMethodStop = Jimple.v()
					.newInvokeStmt(Jimple.v().newStaticInvokeExpr(methodStop.makeRef(), methodStopParameters));
			// Append the instructions to units.
			tmpReturnValueAndInstructions.getSecond().add(callStmtMethodStop);

			generated.addAll(tmpReturnValueAndInstructions.getSecond());
		}
		units.insertAfter(generated, u);
	}

	private void addTraceStart(String invokeType, String methodSignature, List<Value> parameterList, Body body,
			final PatchingChain<Unit> units, Unit u) {

		final SootMethod methodStart = Scene.v().getMethod(
				"<de.unipassau.abc.tracing.Trace: void methodStart(java.lang.String,java.lang.String,java.lang.Object[])>");

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
		
		System.out.println("InstrumentTracer.addTraceStart() Generate parameter array for: " + parameterList);
		Pair<Value, List<Unit>> tmpArgsListAndInstructions = UtilInstrumenter
				.generateParameterArray(RefType.v("java.lang.Object"), parameterList, body);
		
		
		
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

	public interface InstrumentTracerCLI {
		@Option(longName = "project-jar")
		List<File> getProjectJar();

		@Option(longName = "output-to", defaultValue = "./sootOutput")
		File getOutputDir();

		@Option(longName = "output-type", defaultValue = "class") // class,
																	// jimple
		String getOutputType();

		// Those values are space separated
		@Option(longName = "exclude", defaultToNull = true)
		List<String> getExcludes();

		@Option(longName = "include", defaultToNull = true)
		List<String> getIncludes();

		// This identifies the classes/packages that define the boundary of the
		// carving. Methods in this list
		// will be not instrumented, i.e., soot will not go inside them.
		// What do we need external for here ?
		@Option(longName = "external", defaultToNull = true)
		List<String> getExternalInterfaces();

		// Some calls can be omitted by the tracing since are considered not to
		// have side effects
		@Option(longName = "pure-methods", defaultToNull = true)
		List<String> getPureMethods();

	}

	private static final List<Pattern> pureMethods = new ArrayList<Pattern>();

	public static void main(String[] args) throws URISyntaxException {
		String phaseName = "jtp.mainInstrumentation";

		List<File> projectJars = new ArrayList<>();
		File outputDir = null;
		String outputType = null;
		// This contains a list of package? classes? not sure that tell soot
		// which classes it MUST not instrument.
		List<String> sootExclude = new ArrayList<>();
		{
			// TODO - this is for XML DUMP. Ideally we should shade those
			// dependencies
			// sootExclude .add("com.thoughtworks.xstream.*");
			sootExclude.add("de.unipassau.abc.*");
			// TODO Shall we also exclude loggers and such ? Again Shaded deps ?
			// NOTE THAT SOOT COMES WITH ITS OWN DEPS IN THE JAR !
			///// Sure about this ?
			// sootExclude.add("java.io.*");
			// sootExclude.add("java.nio.*");
		}
		List<String> sootInclude = new ArrayList<>();
		{
			// No default include
		}
		List<String> externalInterfaces = new ArrayList<>();
		{
			// External interfaces are not instrumented by soot
			// externalInterfaces.add("java.util.Scanner");
		}

		// Default pure methods. Pure methods ARE not tracked !!
		{
			// m.getDeclaringClass().getName().equals("java.lang.StringBuilder")
			// ||
			// m.getDeclaringClass().getName().equals("java.util.Scanner") || //

			pureMethods.add(Pattern.compile(Pattern.quote("<java.lang.Object: void <init>()>")));
			pureMethods.add(Pattern.compile("<java.io.PrintStream: void println\\(.*>"));

			//
			// pureMethods.add(Pattern.compile("<java.nio.file.Path: File
			// toFile()>"));

			// We treat Strings as primitives, i.e., we pass around them by
			// value. This means that we can omit to track calls to to
			// StringBuilder ... (I hope so! -> not really, the moment those are
			// parameters of any carved invocation this will fail !
			// pureMethods.add(Pattern.compile("<java.lang.StringBuilder: .*"));
			// Boxed type and Strings "cannot" be modified
			pureMethods.add(Pattern.compile("<java.lang.String: .*"));
			///
			pureMethods.add(Pattern.compile("<java.lang.Boolean: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Byte: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Character: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Float: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Integer: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Long: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Short: .*"));
			pureMethods.add(Pattern.compile("<java.lang.Double: .*"));
		}

		try {
			InstrumentTracerCLI commandLineOptions = CliFactory.parseArguments(InstrumentTracerCLI.class, args);
			projectJars.addAll(commandLineOptions.getProjectJar());
			outputDir = commandLineOptions.getOutputDir();
			outputType = commandLineOptions.getOutputType();
			//
			externalInterfaces.addAll((commandLineOptions.getExternalInterfaces() != null)
					? commandLineOptions.getExternalInterfaces() : new ArrayList<String>());
			//
			sootExclude.addAll((commandLineOptions.getExcludes() != null) ? commandLineOptions.getExcludes()
					: new ArrayList<String>());
			// We exclude from soot analysis the external interfaces as well
			sootExclude.addAll(externalInterfaces);
			//
			sootInclude.addAll((commandLineOptions.getIncludes() != null) ? commandLineOptions.getIncludes()
					: new ArrayList<String>());
			//
			if (commandLineOptions.getPureMethods() != null) {
				for (String pure : commandLineOptions.getPureMethods()) {
					if (MethodInvocationMatcher.jimpleMethodInvocationPattern.matcher(pure).matches()) {
						// Literal matching for the methods
						pureMethods.add(Pattern.compile(Pattern.quote(pure)));
					} else {
						// RegEx for the others
						pureMethods.add(Pattern.compile(pure));
					}
				}
			}

		} catch (ArgumentValidationException e) {
			throw e;
		}

		String[] sootArguments = new String[] { "-f", outputType };

		long startTime = System.nanoTime();

		// Setup the Soot settings
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		// TODO: the problem with this one is that soot tries to instrument java
		// as well ?
		// Options.v().set_soot_classpath(System.getProperty("java.path"));
		//
		Options.v().set_output_dir(outputDir.getAbsolutePath());

		// Packages that Soot shall not look into can be brutally filtered out
		// at
		// this level as well. As long as the allow_phantom_ref is true,
		// everything is fine.
		// The more we remove, the faster the analysis gets.
		// For example, we can include here all the external interfaces already
		// !
		logger.info("Excluding: " + sootExclude);
		Options.v().set_exclude(sootExclude);
		Options.v().set_no_bodies_for_excluded(true);

		logger.info("Including: " + sootInclude);
		Options.v().set_include(sootInclude);

		InstrumentTracer it = new InstrumentTracer();

		PackManager.v().getPack("jtp").add(new Transform(phaseName, it));

		// Supporting jars are Xstream and trace
		// TODO HardCoded !
		// String xStreamJar = "./src/main/resources/xstream-1.4.9.jar";
		// AT THE MOMENT I CANNOT FIND A WAY TO HAVE SOOT USING THE SINGLE TRACE
		// CLASS

		String traceJar = ABCUtils.getTraceJar();

		// This is the application under analysis. 1 jar -> 1 entry
		// There can be more jars (libraries, tests, etc.)
		ArrayList<String> list = new ArrayList<String>();
		for (File projectJar : projectJars) {
			list.add(projectJar.getAbsolutePath());
		}
		// TODO Most likely project dependencies shall be listed here
		// Supporting jar. TODO Pretty sure this can be done in a different way
		// !
		list.add(traceJar);
		//
		logger.info("Project dir: " + list);
		Options.v().set_process_dir(list);

		// Patch for Mac OS.
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", "Mac OS X");

		// Run Soot - Probablu we can also execut Packs().run or something.
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
				m.getSignature().contains("de.unipassau.abc.tracing.Trace") || // ABC
				// instrastructre
				//
				false;
	}

	/*
	 * TODO : We pretend those calls never happened
	 */
	private boolean doNotTraceCallsTo(SootMethod m) {
		for (Pattern purePattern : pureMethods) {
			if (purePattern.matcher(m.toString()).matches()) {
				logger.trace(m + " is declared PURE. Do not trace this call");
				return true;
			}
		}
		return false;
	}

}
