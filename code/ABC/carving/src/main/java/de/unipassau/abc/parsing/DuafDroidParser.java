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
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.DataNodeFactory;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
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
 * Parses the traces collected over android devices using the dyniac/duafdroid
 * code.
 * 
 * Each class is either an user class (for which we can generate carved tests),
 * or a library class. Library classes are either Android system Classes (also
 * Java lang) or 3rd party libraries, which we might invoke/mock but for which
 * we cannot generate tests.
 * 
 * Parser "only" parse one trace file at time, and generates the data structures
 * required for carving.
 * 
 * Parser consider the following heuristic: - Thread Name contains UI: this is
 * the "main" thread of the android app - Thread Name contains Asynch/Task: this
 * is a background thread that execute a single task (and dies) - Anyother
 * thread name indicates some background service.
 * 
 * Library calls are identified by [>>] while user calls are identified by [>]
 * Synthetic calls are identified by [>s]
 * 
 * TODO To account for multiple thread, use the thread name as context and the
 * actual position in the file as id (they get interleaved but are still
 * ordered!)
 * 
 * One way to achieve this is to create different instances of the parses which
 * share the same ID generator and invoke them by looking up the instance using
 * the thread name.
 * 
 * TODO Most likely also the parsed trace object will be updated
 * 
 * @author gambi
 *
 */
public class DuafDroidParser {

	public final static File ANDROID_28 = new File("/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

	private static final Logger logger = LoggerFactory.getLogger(DuafDroidParser.class);

	public interface ParserCLI {
		@Option(longName = "trace-files")
		List<File> getTraceFiles();

		@Option(longName = "apk")
		File getApk();

		@Option(longName = "android-jar")
		File getAndroidJar();

		@Option(longName = "store-artifacts-to", defaultValue = "./wip")
		File getOutputDir();

		// TODO Ignore calls to
	}

	/*
	 * This is global anyway. Public for testing. Note that this loads all the
	 * classes in my classpath ! COde Duplication !
	 */
	public static void setupSoot(File androidJar, File apk) {
		G.reset();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		Options.v().set_soot_classpath(androidJar.getAbsolutePath());
		Options.v().set_process_dir(Arrays.asList(new String[] { apk.getAbsolutePath() }));
		Options.v().set_src_prec(soot.options.Options.src_prec_apk);

		soot.options.Options.v().set_allow_phantom_refs(true);

		// // Soot has problems in working on mac.
		String osName = System.getProperty("os.name");
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", osName);

	}

	protected final SootClass scFragment = Scene.v().getSootClassUnsafe(JimpleUtils.FRAGMENT_CLASS);
	protected final SootClass scSupportFragment = Scene.v().getSootClassUnsafe(JimpleUtils.SUPPORT_FRAGMENT_CLASS);
	protected final SootClass scActivity = Scene.v().getSootClassUnsafe(JimpleUtils.ACTIVITY_CLASS);
	protected final SootClass scSupportActivity = Scene.v().getSootClassUnsafe(JimpleUtils.SUPPORT_ACTIVITY_CLASS);

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
	public ParsedTrace parseTrace(File traceFile) throws FileNotFoundException, IOException, ABCException {

		ParsedTrace parsedTrace = new ParsedTrace(traceFile.getName());

		// This is global
		AtomicInteger globalInvocationCount = new AtomicInteger(0);

		Map<String, // ThreadName
				Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> result = new HashMap<>();

		/*
		 * Read the ENTIRE file into a set of string, this might be a problem for long
		 * traces... TODO Refector with StreamAPI or sequential access
		 */

		/*
		 * Trace should start with opening line: ---- STARTING TRACING for
		 * /com.farmerbb.notepad_107 ----
		 * 
		 * We assume that a trace ends when it ends ;) that is System.exit might have
		 * not been called
		 */

		// Since now we do not log STARTING Trace anymore this starts from zero
		// int initialPosition = 0;

		// Parsing variables - some refactoring would not be bad here...
		ExecutionFlowGraph executionFlowGraph = null;
		DataDependencyGraph dataDependencyGraph = null;
		CallGraph callGraph = null;

		// List<String> lines = new ArrayList<>();
		// lines.addAll(Files.readAllLines(traceFile.toPath(),
		// Charset.defaultCharset()));
		// Line number:
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

				// Extract contextual data
				String context = extractContextInformation(currentLine);

				// Initialize data structure or get the one
				if (!result.containsKey(context)) {
					executionFlowGraph = new ExecutionFlowGraph();
					dataDependencyGraph = new DataDependencyGraph();
					callGraph = new CallGraph();
					result.put(context, new Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>(
							executionFlowGraph, dataDependencyGraph, callGraph));
				} else {
					Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> partial = result.get(context);
					executionFlowGraph = partial.getFirst();
					dataDependencyGraph = partial.getSecond();
					callGraph = partial.getThird();
				}

				// Parse the line as before, but update the thread local
				// structure
				try {

					/*
					 * We must call this every time we start a new trace to ensure it removes
					 * spurious/uncomplete onDestroy methods
					 */
					if (currentLine.contains("---- STARTING TRACING for")) {
						List<MethodInvocation> invocationsToDrop = callGraph.verify();
						for (MethodInvocation toDrop : invocationsToDrop) {
							executionFlowGraph.dequeue(toDrop);
							dataDependencyGraph.removeMethodInvocation(toDrop);
						}

						continue;
					}

					/*
					 * Remove from the trace threading information and filter trace entry by thread
					 * name
					 */
					currentLine = removeThreadDataAndContextInformation(currentLine);
					if (currentLine == null) {
						continue;
					}

					String[] parsedLine = Trace.parseLine(currentLine);

					if (parsedLine.length != 4) {
						throw new RuntimeException("Cannot parse " + i + " -- " + currentLine);
					}

					// Flag the call as library or user
					String openingToken = parsedLine[0];
					// Method owner if any or empty string otherwise
					String methodOwner = parsedLine[1];
					// Method Signature - a la JIMPLE
					String methodSignature = parsedLine[2];
					// Actual Parameters
					String parameterString = parsedLine[3];

					// Currently analyzes method invocation in the trace
					MethodInvocation methodInvocation = null;

					// TODO Brutally filter method calls that MOST-LIKELY are
					// not useful to unit tests...
					if (filter(methodSignature)) {
						continue;
					}

					// For some reason, classes that do not exists like
					// "java.lang.String[]" are still not null.
					// Those classes result also a subtypes of everything
					// according to Soot.hierarchy...
					// TODO What about synthetic classes and methods?
					SootClass sootClass = null;
					if (!isArrayMethod(methodSignature)) {
						sootClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
					}

					switch (openingToken) {

					case Trace.PRIVATE_METHOD_START_TOKEN:
					case Trace.SYNTHETIC_METHOD_START_TOKEN:
					case Trace.METHOD_START_TOKEN:
					case Trace.LIB_METHOD_START_TOKEN:

						/*
						 * This is the basic carving object, a method call/method invocation
						 */
						methodInvocation = new MethodInvocation(globalInvocationCount.incrementAndGet(),
								methodSignature);
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

								methodInvocation
										.setConstructor(JimpleUtils.isConstructorOrClassConstructor(methodSignature));

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
							methodInvocation
									.setAndroidActivityCallback(JimpleUtils.isActivityLifecycle(methodSignature));
						} else if (isFragment(sootClass)) {
							methodInvocation
									.setAndroidFragmentCallback(JimpleUtils.isFragmentLifecycle(methodSignature));
						}

						/*
						 * Update the parsing data
						 */
						executionFlowGraph.enqueueMethodInvocations(methodInvocation);
						callGraph.push(methodInvocation);
						dataDependencyGraph.addMethodInvocationWithoutAnyDependency(methodInvocation);

						/*
						 * Update data dependencies and link the various objects
						 */

						/*
						 * Method Ownership for non static, non constructor method (including static
						 * initializers)
						 */
						if (!(methodInvocation.isStatic()
								|| JimpleUtils.isConstructorOrClassConstructor(methodSignature))) {
							ObjectInstance owner = new ObjectInstance(methodOwner);
							// Is the sootclass is null we cannot tell.
							owner.setAndroidActivity(isActivity(sootClass));
							owner.setAndroidFragment(isFragment(sootClass));

							methodInvocation.setOwner(owner);
							dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
						}

						/*
						 * Parameters
						 */
						String[] actualParameters = Trace.getActualParameters(methodSignature, parameterString);
						String[] formalParameters = Trace.getFormalParameters(methodSignature);
						List<DataNode> actualParameterInstances = new ArrayList<>();

						for (int position = 0; position < actualParameters.length; position++) {
							String actualParameterAsString = actualParameters[position];
							String formalParameter = formalParameters[position];
							DataNode actualParameter = DataNodeFactory.get(formalParameter, actualParameterAsString);
							actualParameterInstances.add(actualParameter);
							dataDependencyGraph.addDataDependencyOnActualParameter(methodInvocation, actualParameter,
									position);
						}
						methodInvocation.setActualParameterInstances(actualParameterInstances);

						if (logger.isTraceEnabled())
							logger.trace("Starting Method invocation:" + methodInvocation + ""
									+ ((methodInvocation.isStatic()) ? " static" : "")
									+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
						break;
					case Trace.METHOD_END_TOKEN:
					case Trace.EXCEPTION_METHOD_END_TOKEN:
						String returnValueAsString = parameterString.isEmpty() ? null : parameterString;
						/*
						 * Retrieve the last method invocation registered Check that this corresponds to
						 * the one we just parsed Method Signatures must match and owner must match
						 * unless this is a call to constructor
						 */
						methodInvocation = callGraph.pop();

						/*
						 * Check this is actually the call we are looking for
						 */
						if (!methodInvocation.getMethodSignature().equals(methodSignature)) {

							List<MethodInvocation> subsumingMethods = callGraph
									.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

							throw new RuntimeException("Cannot parse " + i + " -- " + currentLine
									+ ". Unexpected method return from " + methodInvocation + "\nSubsuming methods; "
									+ prettyPrint(subsumingMethods));
						}

						if (!methodInvocation.isStatic()
								&& !JimpleUtils.isConstructorOrClassConstructor(methodSignature)) {
							// We compare by object id since all the owner
							// attributes are not there yet
							String storedMethodOwner = methodInvocation.getOwner().getObjectId();

							if (!storedMethodOwner.equals(methodOwner)) {

								List<MethodInvocation> subsumingMethods = callGraph
										.getOrderedSubsumingMethodInvocationsFor(methodInvocation);

								throw new RuntimeException("Cannot parse " + i + " -- " + currentLine
										+ ". Method owner does not match with " + storedMethodOwner
										+ "\nSubsuming methods; " + subsumingMethods.toString().replaceAll(",", "\n"));
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
							dataDependencyGraph.addDataDependencyOnOwner(methodInvocation, owner);
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
							dataDependencyGraph.addDataDependencyOnReturn(methodInvocation, returnValue);
						}
						if (logger.isTraceEnabled())
							logger.trace("Returning from Method invocation:" + methodInvocation + ""
									+ ((methodInvocation.isStatic()) ? " static" : "")
									+ ((methodInvocation.isLibraryCall()) ? " libCall" : ""));
						break;
					default:
						logger.error("WARNING SKIP THE FOLLOWING LINE ! " + currentLine);
						break;
					}
				} catch (Throwable t) {
					logger.error("Error while parsing line " + currentLine, t);
					throw t;
				}
			}
		}

		// Ensure parsing was done right (matching calls, etc).
		for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTraceResult : result.values()) {
			CallGraph cGraph = parsedTraceResult.getThird();
			cGraph.verify();
		}

//        Map<String, // ThreadName 
//            Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> result = new HashMap<>();
		// Maybe a bit too generic ?
		parsedTrace.setContent(result);
		//
		return parsedTrace;

	}

	//
	private boolean isArrayMethod(String methodSignature) {
		return JimpleUtils.getClassNameForMethod(methodSignature).endsWith("[]");
	}

	private String prettyPrint(List<MethodInvocation> subsumingMethods) {
		StringBuffer stringBuffer = new StringBuffer();
		for (MethodInvocation methodInvocation : subsumingMethods) {
			stringBuffer.append(methodInvocation).append("\n");
		}
		return null;
	}

	/*
	 * [UI:main];[>];;<org.ametro.app.ApplicationEx: void <init>()>;();
	 * [ModernAsyncTask #1];[>>];;<java.lang.Object: void <init>()>;();
	 */
	private String removeThreadDataAndContextInformation(String each) {

		/*
		 * It might be the case that traces do not have thread and context,
		 */
		if (each.split(Trace.DELIMITER).length == 4) {
			return each;
		}
		//
		String threadData = each.split(Trace.DELIMITER)[0];
		each = each.replaceFirst(Pattern.quote(threadData + Trace.DELIMITER), "");
		String context = each.split(Trace.DELIMITER)[0];
		each = each.replaceFirst(Pattern.quote(context + Trace.DELIMITER), "");
		return each;
	}

	public final static String UI_THREAD = "UI:MainThread";

	public String extractContextInformation(String currentLine) {
		/*
		 * It might be the case that traces do not have thread and context,
		 */
		if (currentLine.split(Trace.DELIMITER).length == 4) {
			return UI_THREAD;
		}
		//
		String threadData = currentLine.split(Trace.DELIMITER)[0];

		// Get rid of the *ABC:: #line* prefix
		threadData = threadData.substring(threadData.lastIndexOf(" ") + 1);

		if (threadData.startsWith("[UI:")) {
			currentLine = currentLine.replaceFirst(Pattern.quote(threadData + Trace.DELIMITER), "");
			// Debug mostly
			String context = currentLine.split(Trace.DELIMITER)[0];
			currentLine = currentLine.replaceFirst(Pattern.quote(context + Trace.DELIMITER), "");
			//
			return UI_THREAD;
		} else {
			// Get the full thread name, e.g., [Async Thread #1];
			// Let's assume that the thread name does not contain "]"
			String threadName = currentLine.substring(0, currentLine.indexOf(']')).replace("[", "");
			return threadName;
		}
	}

	private boolean isActivity(SootClass sootClass) {
		return sootClass != null && !sootClass.isInterface() && ( //
		Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scActivity)
				|| Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scSupportActivity));
	}

	private boolean isFragment(SootClass sootClass) {
		return sootClass != null && !sootClass.isInterface() && ( //
		Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scFragment)
				|| Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(sootClass, scSupportFragment));
	}

	private SootMethod getSootMethodFor(String methodSignature) {
		if (isArrayMethod(methodSignature)) {
			return null;
		}

		try {

			return Scene.v().getMethod(methodSignature);
		} catch (Throwable e) {
			logger.warn("Cannot find method " + methodSignature);
			// SootClass sootClass =
			// Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
			// System.out.println("DuafDroidParser.getSootMethodFor() Methods
			// for class " + sootClass);
			// for (SootMethod sootMethod : sootClass.getMethods()) {
			// System.out.println("- " + sootMethod);
			// }
			return null;
		}
	}

	public static void main(String[] args) throws IOException, CarvingException {

		long startTime = System.nanoTime();
		ParserCLI cli = CliFactory.parseArguments(ParserCLI.class, args);

		File outputArtifactTo = cli.getOutputDir();

		if (!outputArtifactTo.isDirectory()) {
			throw new CarvingException("Wrong input: " + outputArtifactTo.getAbsolutePath()
					+ " is a file, but should be a directory instead");
		}

		DuafDroidParser.setupSoot(cli.getAndroidJar(), cli.getApk());

//        Map<String, // TraceFile
//                Map<String, // ThreadContext
//                        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> // ActualContent
//        >> parsedTraceFiles = new HashMap<>();

		DuafDroidParser parser = new DuafDroidParser();
		// Start the parsing
		List<ParsedTrace> theParsedTraces = new ArrayList<>();

		for (File traceFile : cli.getTraceFiles()) {
			try {
				logger.debug("Parsing : " + traceFile);
				theParsedTraces.add(parser.parseTrace(traceFile));
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

}
