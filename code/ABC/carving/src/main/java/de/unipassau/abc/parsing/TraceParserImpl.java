package de.unipassau.abc.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.TraceParser;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.tracing.Trace;
import edu.emory.mathcs.backport.java.util.Arrays;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

/**
 * Parser "only" parse one trace file at time, and generates the data structures
 * required for carving.
 * 
 * Parser considers the following heuristic: - Thread Name contains UI: this is
 * the "main" thread of the android app - Thread Name contains Asynch/Task: this
 * is a background thread that execute a single task (and dies) - Any other
 * thread name indicates some background service.
 * 
 * Calls are intepreted using the tokens defined by {@link Monitor.class}
 * 
 * To account for multiple thread, use the thread name as context and the actual
 * position in the file as id (they get interleaved but are still ordered!)
 * 
 * One way to achieve this is to create different instances of the parses which
 * share the same ID generator and invoke them by looking up the instance using
 * the thread name.
 *
 * TODO: why the first tokens are encapsulated with [ ]? this is not neccessary
 * as they are tokens TODO: Maybe we can replace the DELIMITER to some char
 * which is NEVER used, like, * or % or | Strings are encoded so they are
 * basically digits
 * 
 * @author gambi
 *
 */
public class TraceParserImpl implements TraceParser {

	public interface ParserCLI {
		@Option(longName = "android-jar")
		File getAndroidJar();

		@Option(longName = "apk")
		File getApk();

		@Option(longName = "store-artifacts-to", defaultValue = ".")
		File getOutputDir();

		@Option(longName = "trace-files")
		List<File> getTraceFiles();
	}

	// TODO Replace this with something read from the ENV or passed from the command
	// line
	public final static File ANDROID_28 = new File("/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	public static final Pattern threadPattern = Pattern.compile("\\[((UI:main)|((Modern)?AsyncTask #[1-9][0-9]*))]");
	private static final Logger logger = LoggerFactory.getLogger(TraceParserImpl.class);

	public static void main(String[] args) throws IOException, CarvingException {

		long startTime = System.nanoTime();
		ParserCLI cli = CliFactory.parseArguments(ParserCLI.class, args);

		File outputArtifactTo = cli.getOutputDir();

		if (!outputArtifactTo.isDirectory()) {
			throw new CarvingException("Wrong input: " + outputArtifactTo.getAbsolutePath()
					+ " is a file, but should be a directory instead");
		}

		/*
		 * Soot is a singleton and exposes static methods only, so we encapsulate its
		 * configuration inside static method calls
		 */
		TraceParserImpl.setupSoot(cli.getAndroidJar(), cli.getApk());

		/*
		 * A test of an application might result in generating multiple traces if the
		 * app is decomissioned from the memory. However, here we assume that each trace
		 * is independent and the main method simply parses and stores the results for
		 * later processing into an XML object.
		 */
		TraceParserImpl parser = new TraceParserImpl();

		List<ParsedTrace> theParsedTraces = new ArrayList<>();

		for (File traceFile : cli.getTraceFiles()) {
			try {
				logger.info("Parsing Trace: " + traceFile);
				theParsedTraces.add(parser.parseFromTraceFile(traceFile));
			} catch (Throwable e) {
				logger.error("Failed to parse " + traceFile, e);
			}
		}

		/*
		 * Serialize the parsed graphs to files so maybe we can avoid re-parsing the
		 * trace over and over and we might be able to limit the memory footprint of the
		 * process
		 */

		if (!outputArtifactTo.exists()) {
			Files.createDirectories(outputArtifactTo.toPath(), new FileAttribute[] {});
		}

		for (ParsedTrace parsedTrace : theParsedTraces) {
			parsedTrace.storeTo(outputArtifactTo);
		}

	}

	public static void setupSoot(File androidJar, File apk) {
		G.reset();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		Options.v().set_soot_classpath(androidJar.getAbsolutePath());
		Options.v().set_process_dir(Arrays.asList(new String[] { apk.getAbsolutePath() }));
		Options.v().set_src_prec(soot.options.Options.src_prec_apk);

		soot.options.Options.v().set_allow_phantom_refs(true);

		// Soot has problems in working on mac.
		String osName = System.getProperty("os.name");
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", osName);

	}

	protected final SootClass scActivity = Scene.v().getSootClassUnsafe(JimpleUtils.ACTIVITY_CLASS);
	protected final SootClass scFragment = Scene.v().getSootClassUnsafe(JimpleUtils.FRAGMENT_CLASS);

	protected final SootClass scSupportActivity = Scene.v().getSootClassUnsafe(JimpleUtils.SUPPORT_ACTIVITY_CLASS);

	protected final SootClass scSupportFragment = Scene.v().getSootClassUnsafe(JimpleUtils.SUPPORT_FRAGMENT_CLASS);

	/**
	 * Ensures to have the right number of elements in the returning String[] or
	 * fail with an exception
	 */
	public Map<String, String> parseLine(String line) throws ABCException {
		try {
			// Remove the "ABC::" tag
			line = line.replaceFirst(Trace.ABC_TAG, "").trim();

			String[] _tokens = line.split(Trace.DELIMITER);

			// Extract the method id
			String methodId = _tokens[0].split(" ")[0];

			// Extracts the identifier of the thread that executed the invocation
			String threadName = _tokens[0].split(" ")[1];
//			.replaceFirst("\\[", "").replaceFirst("\\]", "");

			// Extract the context of the invocation
			String context = _tokens[1];
//			.substring(_tokens[1].indexOf('['), _tokens[1].indexOf(']')).replaceFirst("\\[",
//					"");

			// Extract method token. Token is DEFINED with [ ] so we do not need to remove
			// them
			String methodToken = _tokens[2];
//			.substring(_tokens[2].indexOf('['), _tokens[2].indexOf(']'))
//					.replaceFirst("\\[", "");

			/*
			 * TODO Static calls might be reported with a null owner... I do not really like
			 * this, but maybe we can distinguish them using the method token ?
			 */
			String methodOwner = _tokens[3];

			// Method Signature this is the JIMPLE format
			String methodSignature = _tokens[4];

			// Input parameters for method invocation, otherwise return value
			// We need to remove the "(" ")"
			String parametersOrReturnValue = _tokens[5].substring(1, _tokens[5].length() - 1);

// TODO Special case of array ? - Create a test for this ?
			//
//		if (_tokens.length != 4) {
//			// Does the line contains an array as OWNER ?! I guess in the end arrays can
//			// have method calls ...
//			// [>>];[Lorg.ametro.catalog.entities.TransportType;@71559508;<java.lang.Object:
//			// java.lang.Object clone()>;(); got 5 instead of 4
//			if (_tokens[1].startsWith("[") && _tokens[2].startsWith("@")) {
//				tokens[0] = _tokens[0];
//				tokens[2] = _tokens[3];
//				tokens[3] = _tokens[4];
//
//				// Convert the array owner to the expected form
//
//				tokens[1] = JimpleUtils.getBaseArrayType(_tokens[1]) + "[]" + _tokens[2];
//
//			} else {
//				throw new ABCException("Cannot parse line " + line + " got " + _tokens.length + " instead of 4");
//			}
//		} else {
//			tokens = _tokens;
//		}
//
//		// Flag the call as library or user
//		String openingToken = tokens[0];
//
//		// Count formalParameters
//		String[] formalParameters = JimpleUtils.getParameterList(methodSignature);
//
//		// At this point take the remainder of the String and parse the
//		// parameters or the return value
//		String parameterString = tokens[3];
//
//		// Remove the opening ( and closing )
//		parameterString = parameterString.substring(1, parameterString.length() - 1);

			Map<String, String> parsedLine = new HashMap<String, String>();
			parsedLine.put("invocationCount", methodId);
			parsedLine.put("threadName", threadName);
			parsedLine.put("methodContext", context);
			parsedLine.put("methodToken", methodToken);
			parsedLine.put("methodOwner", methodOwner);
			parsedLine.put("methodSignature", methodSignature);
			parsedLine.put("parametersOrReturnValue", parametersOrReturnValue);

			return parsedLine;
		} catch (Throwable t) {
			throw new ABCException("Cannot parse line", t);
		}
	}

	// TODO Maybe filtering should be app-specific ?
	public boolean filter(String methodSignature) {
		if (methodSignature.contains("java.lang.StringBuilder: java.lang.StringBuilder append(java.lang.String)")) {
			return true;
		}
		// else if( methodSignature.contains("" ) ){ return true; }
		else {
			return false;
		}
	}

	private SootMethod getSootMethodFor(String methodSignature) {
		if (isArrayMethod(methodSignature)) {
			return null;
		}

		try {

			return Scene.v().getMethod(methodSignature);
		} catch (Throwable e) {
			SootClass sootClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
			logger.warn("Cannot find method " + methodSignature + " in class " + sootClass);
			for (SootMethod sootMethod : sootClass.getMethods()) {
				System.out.println("- " + sootMethod);
			}
			return null;
		}
	}

	private boolean isActivity(SootClass sootClass) {
		return sootClass != null && !sootClass.isInterface() && ( //
		Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scActivity)
				|| Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scSupportActivity));
	}

	//
	private boolean isArrayMethod(String methodSignature) {
		return JimpleUtils.getClassNameForMethod(methodSignature).endsWith("[]");
	}

	private boolean isFragment(SootClass sootClass) {
		return sootClass != null && !sootClass.isInterface() && ( //
		Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scFragment)
				|| Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scSupportFragment));
	}

	private Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedElements = new HashMap<>();

	/**
	 * Parse the traceFilePath and generates the required DataStructures
	 * 
	 * TODO HARD TO TEST !!!
	 * 
	 * @param traceFile
	 * @return A map (ThreadName, ParsedTrace)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CarvingException
	 */
//    public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parseTrace(File traceFile)
	public ParsedTrace parseFromTraceFile(File traceFile) throws FileNotFoundException, IOException, ABCException {

		ParsedTrace parsedTrace = new ParsedTrace(traceFile.getName());

		/*
		 * This is global id of method invocations, acts like a global logical clock
		 * (across multiple traces/Threads
		 */
		AtomicInteger globalInvocationCount = new AtomicInteger(0);

		// TODO We probably do not handle GLOBAL variables -passed implicitly- or
		// accesses to fields ! See #123

		/*
		 * Parsing will create for each execution thread (String:key) a structure that
		 * contains the ordered list of executed method calls, a graph of method calls
		 * dependencies via data dependency (read and produced), and via method call
		 */

		// Parsing variables - some refactoring would not be bad here...
		ExecutionFlowGraph executionFlowGraph = null;
		DataDependencyGraph dataDependencyGraph = null;
		CallGraph callGraph = null;

		// TODO Maybe replace this with Scanner?
		// TODO Maybe we could replace this entire code with something more maintainable
		// which actually implements a lesser/parser or a scannerless parser?
		//

		try (BufferedReader b = new BufferedReader(new FileReader(traceFile))) {

			String currentLine = "";
			int i = 0;
			long startTime = System.currentTimeMillis();

			while ((currentLine = b.readLine()) != null) {

				// Increment line count
				i++;

				if (i % 10000 == 0) {
					long elapsed = System.currentTimeMillis() - startTime;
					logger.info("Parsed " + i + " lines took "
							+ String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(elapsed),
									TimeUnit.MILLISECONDS.toSeconds(elapsed)
											- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed))));
					System.gc();
				}

				try {
					// TODO should the parseLine return an object instead of plain String?
					Map<String, String> tokens = parseLine(currentLine);
					// TODO Check whether we have all the required elements in the parsedLine ?
					// TODO Implement validation of line and move it inside parseLine method
					if (tokens.get("methodSignature") == null) {
						throw new ABCException("Missing method signature in trace. Offending line: " + currentLine);
					}

					// Load the data structure to fill up with the parsed info
					Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local = getOrInitializeDataStructure(
							tokens.get("threadName"));

					// TODO Brutally filter method calls that MOST-LIKELY are
					// not useful to unit tests...
					String methodSignature = tokens.get("methodSignature");
					String methodOwner = tokens.get("methodOwner");
					String openingToken = tokens.get("methodToken");

					// Filter special method invocations
					if (filter(methodSignature)) {
						logger.debug("Filtering out call: ", methodSignature);
						continue;
					}

					// TODO Maybe some of the following parsing and the like can be moved into Trace
					// ?

					// Thread Name, but to do so, we need to change this code

					MethodInvocation methodInvocation = null;

					SootClass sootClass = null;
					if (!isArrayMethod(methodSignature)) {
						sootClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
					} else {
						logger.debug("Found synthetic method array ", methodOwner, methodSignature);
					}

					// TODO ALESSIO UP TO HERE. Refactor and check whether we can greate testrs fro
					// this clasas.
					// TODO Parsing might be easily parallelizable (for example, by filtering by

					switch (openingToken) {

					case Trace.PRIVATE_METHOD_START_TOKEN:
					case Trace.SYNTHETIC_METHOD_START_TOKEN:
					case Trace.METHOD_START_TOKEN:
					case Trace.LIB_METHOD_START_TOKEN:
						parseMethodInvocation(globalInvocationCount, local, tokens, sootClass);
						break;
					case Trace.METHOD_END_TOKEN:
						parseMethodReturn(globalInvocationCount, local, tokens, sootClass);
						break;
					case Trace.EXCEPTION_METHOD_END_TOKEN:
					case Trace.CAUGHT_EXCEPTION_METHOD_END_TOKEN:
						parseMethodReturnWithException(globalInvocationCount, local, tokens, sootClass);
						break;
					default:
						throw new ABCException("Invalid token" + openingToken);
					}
				} catch (Throwable t) {
					logger.error("Error while parsing line " + currentLine, t);
					throw t;
				}
			}
		}

		// Ensure parsing was done right (matching calls, etc).
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTraceResult : parsedElements
				.values()) {
			CallGraph cGraph = parsedTraceResult.getThird();
			cGraph.verify();
		}

		parsedTrace.setContent(parsedElements);
		//
		return parsedTrace;

	}

	public void parseMethodReturnWithException(AtomicInteger globalInvocationCount,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local, Map<String, String> tokens,
			SootClass sootClass) throws ABCException {

		String methodSignature = tokens.get("methodSignature");
		String methodOwner = tokens.get("methodOwner");
		String parameterString = tokens.get("parametersOrReturnValue");

		MethodInvocation methodInvocation;
		String exceptionAsString = parameterString;

		//
		assert exceptionAsString != null;
		/*
		 * Retrieve the last method invocation registered Check that this corresponds to
		 * the one we just parsed Method Signatures must match and owner must match
		 * unless this is a call to constructor
		 */
		methodInvocation = local.getThird().pop();

		methodInvocation.setExceptional(true);

		/*
		 * Check this is actually the call we are looking for
		 */
		if (!methodInvocation.getMethodSignature().equals(methodSignature)) {

			List<MethodInvocation> subsumingMethods = local.getThird()
					.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

			throw new ABCException("Unexpected method return from " + methodInvocation + "\nSubsuming methods; "
					+ prettyPrint(subsumingMethods));
		}

		if (!methodInvocation.isStatic() && !JimpleUtils.isConstructorOrClassConstructor(methodSignature)) {
			// We compare by object id since all the owner
			// attributes are not there yet
			String storedMethodOwner = methodInvocation.getOwner().getObjectId();

			if (!storedMethodOwner.equals(methodOwner)) {

				List<MethodInvocation> subsumingMethods = local.getThird()
						.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

				throw new ABCException("Method owner does not match with " + storedMethodOwner + "\nSubsuming methods; "
						+ subsumingMethods.toString().replaceAll(",", "\n"));
			}

		}

		/*
		 * At this point we found a matching method call we update the data structures.
		 */
		if (JimpleUtils.isConstructor(methodSignature)) {
			/*
			 * Ownership info for constructors are available only by the end of the
			 * invocation... This is not 100% accurate but we try to live with that...
			 */
			ObjectInstance owner = new ObjectInstance(methodOwner);
			owner.setAndroidActivity(isActivity(sootClass));
			owner.setAndroidFragment(isFragment(sootClass));
			methodInvocation.setOwner(owner);
			local.getSecond().addDataDependencyOnOwner(methodInvocation, owner);
			// Still not sure WHY we need to register the data
			// deps
			// if
			// we have that into the methodInvocation and data
			// deps...
			// executionFlowGraph.addOwnerToMethodInvocation(methodInvocation,
			// owner);
		}
		/*
		 * Store Instance of Exception.
		 */
		DataNode exceptionValue = DataNodeFactory.getFromException(exceptionAsString);
		methodInvocation.setRaisedException(exceptionValue);
		local.getSecond().addDataDependencyOnReturn(methodInvocation, exceptionValue);

		if (logger.isTraceEnabled()) {
			logger.trace("Method invocation:" + methodInvocation + " raised exception " + exceptionValue);
		}

	}

	public void parseMethodReturn(AtomicInteger globalInvocationCount,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local, Map<String, String> tokens,
			SootClass sootClass) throws ABCException {

		String methodSignature = tokens.get("methodSignature");
		String methodOwner = tokens.get("methodOwner");
		String parameterString = tokens.get("parametersOrReturnValue");

		MethodInvocation methodInvocation;
		String returnValueAsString = parameterString.isEmpty() ? null : parameterString;
		/*
		 * Retrieve the last method invocation registered Check that this corresponds to
		 * the one we just parsed Method Signatures must match and owner must match
		 * unless this is a call to constructor
		 */
		methodInvocation = local.getThird().pop();

		/*
		 * Check this is actually the call we are looking for
		 */
		if (!methodInvocation.getMethodSignature().equals(methodSignature)) {

			List<MethodInvocation> subsumingMethods = local.getThird()
					.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

			throw new ABCException("Unexpected method return from " + methodInvocation + "\nSubsuming methods; "
					+ prettyPrint(subsumingMethods));
		}

		if (!methodInvocation.isStatic() && !JimpleUtils.isConstructorOrClassConstructor(methodSignature)) {
			// We compare by object id since all the owner
			// attributes are not there yet
			String storedMethodOwner = methodInvocation.getOwner().getObjectId();

			if (!storedMethodOwner.equals(methodOwner)) {

				List<MethodInvocation> subsumingMethods = local.getThird()
						.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

				throw new ABCException("Method owner does not match with " + storedMethodOwner + "\nSubsuming methods; "
						+ subsumingMethods.toString().replaceAll(",", "\n"));
			}

		}

		/*
		 * At this point we found a matching method call we update the data structures.
		 */
		if (JimpleUtils.isConstructor(methodSignature)) {
			/*
			 * Ownership info for constructors are available only by the end of the
			 * invocation... This is not 100% accurate but we try to live with that...
			 */
			ObjectInstance owner = new ObjectInstance(methodOwner);
			owner.setAndroidActivity(isActivity(sootClass));
			owner.setAndroidFragment(isFragment(sootClass));
			methodInvocation.setOwner(owner);
			local.getSecond().addDataDependencyOnOwner(methodInvocation, owner);
			// Still not sure WHY we need to register the data
			// deps
			// if
			// we have that into the methodInvocation and data
			// deps...
			// executionFlowGraph.addOwnerToMethodInvocation(methodInvocation,
			// owner);
		}
		/*
		 * Return type
		 */
		String returnType = methodSignature.trim().split(" ")[1];
		if (!JimpleUtils.isVoid(returnType)) {
			DataNode returnValue = DataNodeFactory.get(returnType, returnValueAsString);
			methodInvocation.setReturnValue(returnValue);
			local.getSecond().addDataDependencyOnReturn(methodInvocation, returnValue);
		}
		if (logger.isTraceEnabled())
			logger.trace("Returning from Method invocation:" + methodInvocation + ""
					+ ((methodInvocation.isStatic()) ? " static" : "")
					+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
	}

	private void parseMethodInvocation(AtomicInteger globalInvocationCount,
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> local, Map<String, String> tokens,
			SootClass sootClass) throws ABCException {

		try {

			String methodSignature = tokens.get("methodSignature");
			String openingToken = tokens.get("methodToken");
			String methodOwner = tokens.get("methodOwner");
			String parameterString = tokens.get("parametersOrReturnValue");

			MethodInvocation methodInvocation;
			/*
			 * This is the basic carving object, a method call/method invocation
			 */
			methodInvocation = new MethodInvocation(globalInvocationCount.incrementAndGet(), methodSignature);
			/*
			 * Explicitly set the relevant attributes of the object
			 */
			methodInvocation.setLibraryCall(openingToken.equals(Trace.LIB_METHOD_START_TOKEN));
			methodInvocation.setSyntheticMethod(openingToken.equals(Trace.SYNTHETIC_METHOD_START_TOKEN));
			methodInvocation.setPrivate(openingToken.equals(Trace.PRIVATE_METHOD_START_TOKEN));

			if (methodInvocation.isSynthetic()) {
				/*
				 * A call which lacks the owner and is not an instance constructor must be
				 * static. Note there's no way from the methodSignature to distinguish static vs
				 * instance calls but we cannot load the soot method for syntethic method call
				 * generated by ABC.
				 */
				if (!JimpleUtils.isConstructor(methodSignature)) {
					methodInvocation.setStatic(methodOwner.isEmpty());
				}
				// Since the method is generate we need to use an
				// heuristic
				// to see if that's lifecycle

			} else {

				SootMethod sootMethod = getSootMethodFor(methodSignature);

				if (sootMethod != null) {

					methodInvocation.setStatic(sootMethod.isStatic());
					methodInvocation.setPrivate(sootMethod.isPrivate());
					methodInvocation.setPublic(sootMethod.isPublic());
					methodInvocation.setProtected(sootMethod.isProtected());
				} else if (isArrayMethod(methodSignature)) {
					/*
					 * This is a method that we created on the fly, it does not exist for real...
					 */
					methodInvocation.setStatic(false);
					methodInvocation.setPrivate(false);
					methodInvocation.setPublic(true);
					methodInvocation.setProtected(false);

					methodInvocation.setConstructor(JimpleUtils.isConstructorOrClassConstructor(methodSignature));

				} else {
					/*
					 * At this point sootMethod might be null for special classes like
					 * "BuildConfig". In this case we rely on heuristics to set the details of the
					 * methodInvocation. Usually those classes have no methods except <clinit>
					 */

					if (JimpleUtils.isClassConstructor(methodSignature)) {
						methodInvocation.setStatic(true);
						// Really necessary ?
						methodInvocation.setConstructor(true);
					}
				}
			}

			if (isActivity(sootClass)) {
				methodInvocation.setAndroidActivityCallback(JimpleUtils.isActivityLifecycle(methodSignature));
			} else if (isFragment(sootClass)) {
				methodInvocation.setAndroidFragmentCallback(JimpleUtils.isFragmentLifecycle(methodSignature));
			}

			/*
			 * Update the parsing data
			 */
			local.getFirst().enqueueMethodInvocations(methodInvocation);
			local.getSecond().addMethodInvocationWithoutAnyDependency(methodInvocation);
			local.getThird().push(methodInvocation);

			/*
			 * Update data dependencies and link the various objects
			 */

			/*
			 * Method Ownership for non static, non constructor method (including static
			 * initializers)
			 */
			if (!(methodInvocation.isStatic() || JimpleUtils.isConstructorOrClassConstructor(methodSignature))) {
				ObjectInstance owner = new ObjectInstance(methodOwner);
				// Is the sootclass is null we cannot tell.
				owner.setAndroidActivity(isActivity(sootClass));
				owner.setAndroidFragment(isFragment(sootClass));

				methodInvocation.setOwner(owner);
				local.getSecond().addDataDependencyOnOwner(methodInvocation, owner);
			}

			/*
			 * Parameters
			 */
			String[] actualParameters = getActualParameters(methodSignature, parameterString);
			String[] formalParameters = getFormalParameters(methodSignature);
			List<DataNode> actualParameterInstances = new ArrayList<>();

			for (int position = 0; position < actualParameters.length; position++) {
				String actualParameterAsString = actualParameters[position];
				String formalParameter = formalParameters[position];
				DataNode actualParameter = DataNodeFactory.get(formalParameter, actualParameterAsString);
				actualParameterInstances.add(actualParameter);
				local.getSecond().addDataDependencyOnActualParameter(methodInvocation, actualParameter, position);
			}
			methodInvocation.setActualParameterInstances(actualParameterInstances);

			if (logger.isTraceEnabled())
				logger.trace("Starting Method invocation:" + methodInvocation + ""
						+ ((methodInvocation.isStatic()) ? " static" : "")
						+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
		} catch (Throwable e) {
			throw new ABCException("Cannot parsed method invocation", e);
		}
	}

	public final static Pattern params = Pattern.compile("\\((.*?)\\)");

	public String[] getFormalParameters(String jimpleMethod) {

		Matcher matchPattern = params.matcher(jimpleMethod);
		if (matchPattern.find()) {
			String t = matchPattern.group(1);
			if (t.length() == 0) {
				return new String[] {};
			}
			return t.split(",");
		}
		return new String[] {};
	}

	// TODO If this is used only somewhere else, move it to TraceParseImpl maybe
	public String[] getActualParameters(String methodSignature, String parameterString) {
		String[] formalParameters = JimpleUtils.getParameterList(methodSignature);
		String[] actualParametersOrReturnValue = new String[formalParameters.length];

		if (formalParameters.length == 0) {
			return actualParametersOrReturnValue;
		}

		// TODO Consider to rebuild the strings first and then parse using spli
		String[] tokens = parameterString.split(",");
		int pCount = 0; // formalParameters.length;
		boolean accumulateStringContent = false;
		String partialStringValue = null;

		for (String token : tokens) {

			// Empty String. Note that we cannot reliably rely on formal parameters as they
			// might be Object
			if (token.equals("[]")) {
				accumulateStringContent = false;
			}
			// Single Char Strings
			else if (token.contains("[") && token.contains("]")) {
				accumulateStringContent = false;
			} else if (token.startsWith("[") && !token.contains("@")) { // Arrays have [Type form...
				accumulateStringContent = true;
				// Initialize the String
				partialStringValue = "";
			} else if (token.endsWith("]")) {
				accumulateStringContent = false;
				// Append the last token, without the delimiter
				partialStringValue += token;
			}

			if (accumulateStringContent) {
				// If the token represents a string-bytes, accumulate the
				// elements
				partialStringValue += token + ",";
			} else {
				if (partialStringValue == null) {
					// Handle null at first position
					if (token.isEmpty()) {
						actualParametersOrReturnValue[pCount] = null;
					} else {
						actualParametersOrReturnValue[pCount] = token;
					}
				} else {
					actualParametersOrReturnValue[pCount] = partialStringValue;
				}
				pCount++;
				partialStringValue = null;
			}
		}

		assert actualParametersOrReturnValue.length == formalParameters.length : "actual and formal parameters do not match";

		return actualParametersOrReturnValue;

	}

	/*
	 * Initialize the data structure holding the parsing results or getting the
	 * existing one
	 */
	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getOrInitializeDataStructure(String context) {
		if (!parsedElements.containsKey(context)) {
			parsedElements.put(context, new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
					new ExecutionFlowGraph(), new DataDependencyGraph(), new CallGraph()));
		}
		return parsedElements.get(context);
	}

	private String prettyPrint(List<MethodInvocation> subsumingMethods) {
		StringBuffer stringBuffer = new StringBuffer();
		for (MethodInvocation methodInvocation : subsumingMethods) {
			stringBuffer.append(methodInvocation).append("\n");
		}
		return null;
	}

	@Override
	public Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parseTrace(String traceFilePath,
			List<MethodInvocationMatcher> externalInterfaceMatchers)
			throws FileNotFoundException, IOException, ABCException {
		// TODO Auto-generated method stub
		throw new RuntimeException("FIX ME. I am defined in an interface but this parser does not implement me...");
	}

}
