/**
 * -------------------------------------------------------------------------------------------
 * Date			Author      	Changes
 * -------------------------------------------------------------------------------------------
 * 10/19/15		hcai			Created; for monitoring method events in DynCG 
 * 10/26/15		hcai			added ICC monitoring with time-stamping call events
 * 04/22/17		hcai		added instrumentation for tracking reflection-called method
 * 12/02/19		gambi		Reduce, clean up, update and remove all the un-necessary elements
 * 27/07/20		gambi		Changed the interface and strongly simplified implementation
 *
 */
package de.unipassau.abc.instrumentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Looper;
import android.util.Log;
import utils.logicClock;

public class MultithreadMonitor {

	public static final String ABC_TAG = ">> ABC::";

	public final static char DELIMITER = ';';

	public final static String METHOD_START_TOKEN = "[>]";
	public final static String SYNTHETIC_METHOD_START_TOKEN = "[>s]";
	public final static String PRIVATE_METHOD_START_TOKEN = "[>p]";

	public final static String LIB_METHOD_START_TOKEN = "[>>]";
	public final static String METHOD_END_TOKEN = "[<]";
	public final static String METHOD_END_TOKEN_FROM_EXCEPTION = "[<E]";

	private static ReentrantLock lock = new ReentrantLock();

	// Store trace info inside the phone memory
	private static BufferedWriter traceFileOutputWriter;

	// This is public to enable EspressoTest changing it using reflection, see MonitorRule
	public static boolean DEBUG = false;

	/*
	 * We need a reliable way to match exceptions with method calls. Ideally, we
	 * should use the stack trace, but it is hard given the standard stack frame to
	 * match every past call. So we implement a stack on our own. This seems a bit
	 * over-killing but I have no other ideas ATM
	 */
	protected static Map<String, Stack<StackElement>> stackTrace = new HashMap<String, Stack<StackElement>>();

	// Thread-locale data to store whether an exception is thrown (and not yet
	// handled)
	protected static Map<String, Object> thrownExceptions = new HashMap<String, Object>();

	/* first events */
//	protected static HashMap<String, Integer> F = new HashMap<String, Integer>();
	/* last events */
//	protected static HashMap<String, Integer> L = new HashMap<String, Integer>();

	/* all events */
//	protected static HashMap<Integer, String> A = new LinkedHashMap<Integer, String>();

	/* first message-receiving events */
//	protected static HashMap<String, Integer> S = new HashMap<String, Integer>();

	/* the global counter for timp-stamping each method event */
	protected static Integer g_counter = 0;

	/*
	 * Genuine DynCG will only produce a simplified procedure call sequence that
	 * reflects the EA relations; By default this will be compiled. Otherwise if
	 * specified to produce the full call sequence including all intermediate method
	 * (enter/returned-into) events
	 */
//	protected static boolean DynCGequenceOnly = true;

	/* output file for serializing the two event maps */
//	protected static String fnEventMaps = "";

	/*
	 * a flag ensuring the initialization and termination are both executed exactly
	 * once and they are paired
	 */
	protected static boolean bInitialized = false;

	/*
	 * debug flag: e.g. for dumping event sequence to human-readable format for
	 * debugging purposes, etc.
	 */
//	protected static boolean debugOut = true;

//	public static void turnDebugOut(boolean b) {
//		debugOut = b;
//	}

	/* The "DynCGequenceOnly" option will be set by EARun via this setter */
//	public static void setDynCGequenceOnly(boolean b) {
//		DynCGequenceOnly = b;
//	}

	/*
	 * The name of serialization target file will be set by EARun via this setter
	 */
//	public static void setEventMapSerializeFile(String fname) {
//		fnEventMaps = fname;
//
//		if (dumpCallmap) {
//			callmap.clear();
//		}
//		F.clear();
//		L.clear();
//		A.clear();
//		synchronized (g_counter) {
//			g_counter = 1;
//		}
//	}

	/** Used to avoid infinite recursion */
	// private static boolean active = false;

	/*
	 * if dump the dynamic calling relationship, namely a map from caller to callee
	 */
//	protected static boolean dumpCallmap = true;
//	protected static final String calleeTag = "after calling ";
//
//	public static void setDumpCallmap(boolean b) {
//		dumpCallmap = b;
//	}

//	private final static Map<String, String> callmap = new LinkedHashMap<String, String>();

	// TODO How is this used ?
	/* output file for serializing the two event maps */
//	protected static String fnCallmap = "/tmp/callmap-" + System.currentTimeMillis() + ".out";

	protected static BufferedWriter cg_writer = null;

	private final static List<String> boxedPrimitiveTypes = Arrays
			.asList(new String[] { Byte.class.getName(), Short.class.getName(), Integer.class.getName(),
					Long.class.getName(), Float.class.getName(), Double.class.getName(), Boolean.class.getName(),

			});

	private static final logicClock g_lgclock = new logicClock(new AtomicInteger(0), "" + android.os.Process.myUid());

	/*
	 * The name of serialization target file will be set by EARun via this setter
	 */
//	public static void setCallMapSerializeFile(String fname) {
//		fnCallmap = fname;
//	}

	private static boolean isStringMethod(String methodSignature) {
		return isString(methodSignature.replaceFirst("<", "").split(" ")[0].replaceAll(":", ""));
	}

	private static boolean isLogEverything() {
		// Replace true with a variable of some sort...
		return true || isUiThread();
	}

	// THIS IS THE INTERFACE OF THE CLASS.

	private static String testName = null;

	public static void testStart(String _testName) {
		testName = _testName;
		// This forces the reinitialization at the first call to the monitor
		bInitialized = false;

		android.util.Log.e(ABC_TAG, "---- CALLED testStart from " + testName);
	}

	public static void testEnd() {
		android.util.Log.e(ABC_TAG, "---- CALLED testEnd from " + testName);
		testName = null;
		bInitialized = false;
		// Here probably we can flush the output if needed

	}

	/**
	 * Ensures that the monitor class is properly initialized no matter how the app
	 * is started.
	 *
	 * Note: since getting the Context object from the app is tricky, we hardcode
	 * the path using the packagename
	 *
	 * @throws Exception
	 */
	public static void initialize(String packageName) throws Exception {

		android.util.Log.e(ABC_TAG, "---- CALLED initialize");
		synchronized (g_counter) {
			g_counter = 1;
			bInitialized = true;
			g_lgclock.initClock(0);
			// Setup the trace file for action based carving
			String filename = "Trace-" + System.currentTimeMillis() + ".txt";
			if (testName != null) {
				// Keep Trace- so we can automatically collect all the files at once
				filename = "Trace-" + testName + "-" + System.currentTimeMillis() + ".txt";
			}

			// Write to the internal memory of the app
			File homeFolder = new File("/data/data/" + packageName);
			if (!homeFolder.exists()) {
				homeFolder.mkdirs();
			}
			File traceFile = new File(homeFolder, filename);
			traceFileOutputWriter = new BufferedWriter(new FileWriter(traceFile));
			StringBuffer sb = new StringBuffer();
			sb.append(ABC_TAG).append(g_counter).append("---- STARTING TRACING for ").append(packageName)
					.append(" output to ").append(traceFile.getAbsolutePath()).append(" ----");

			android.util.Log.e(ABC_TAG,
					"---- STARTING TRACING for " + packageName + " output to " + traceFile.getAbsolutePath() + " ----");

			// For the fun of it... list all the files in this folder on Android log
			for (File file : traceFile.getParentFile().listFiles()) {
				android.util.Log.e("ABC:: ", "Internal storage: " + file.getAbsolutePath());
			}
		}

	}

	/**
	 * Triggered when the execution starts a non-private method that belongs to the
	 * instrumented application
	 * 
	 * @param packageName
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodParameters
	 * @throws Exception
	 */
	public static void onAppMethodCall(//
			String packageName, //
			Object methodOwner, //
			String methodSignature, //
			java.lang.Object[] methodParameters) throws Exception {
		// Ensure locking.
		lock.lock();
		try {
			if (!bInitialized) {
				initialize(packageName);
			}

			registerCall(methodOwner, methodSignature, null, false);

			enter_impl(methodOwner, methodSignature, methodParameters, METHOD_START_TOKEN);
		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ packageName + "\n" //
					+ methodOwner + "\n" //
					+ methodSignature + "\n" //
					+ Arrays.toString(methodParameters) + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Triggered when the execution starts a method that we faked. For example,
	 * accessing an array element or setting an array element are both faked as
	 * synthetic methods.
	 * 
	 * @param packageName
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodParameters
	 * @throws Exception
	 */
	public static void onSyntheticMethodCall(//
			String packageName, //
			Object methodOwner, //
			String methodSignature, //
			java.lang.Object[] methodParameters) throws Exception {
		// Ensure locking.
		lock.lock();
		try {

			if (!bInitialized) {
				initialize(packageName);
			}

			registerCall(methodOwner, methodSignature, null, false);

			enter_impl(methodOwner, methodSignature, methodParameters, SYNTHETIC_METHOD_START_TOKEN);
		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ packageName + "\n" //
					+ methodOwner + "\n" //
					+ methodSignature + "\n" //
					+ Arrays.toString(methodParameters) + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Triggered when the execution starts a private method of the app. We need to
	 * trace private method calls as they might generate data dependencies despite
	 * the methods cannot be directly called by the carved tests.
	 * 
	 * @param packageName
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodParameters
	 * @throws Exception
	 */
	public static void onPrivateAppMethodCall(//
			String packageName, //
			Object methodOwner, //
			String methodSignature, //
			java.lang.Object[] methodParameters) throws Exception {

		// Ensure locking.
		lock.lock();
		try {
			if (!bInitialized) {
				initialize(packageName);
			}
			//
			registerCall(methodOwner, methodSignature, null, false);

			enter_impl(methodOwner, methodSignature, methodParameters, PRIVATE_METHOD_START_TOKEN);
		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ packageName + "\n" //
					+ methodOwner + "\n" //
					+ methodSignature + "\n" //
					+ Arrays.toString(methodParameters) + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}

	}

	/**
	 * Triggered when the execution starts a method that does not belong to the
	 * instrumented app.
	 * 
	 * 
	 * @param packageName
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodParameters
	 * @throws Exception
	 */
	public static void onLibMethodCall(//
			Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			java.lang.Object[] methodParameters) throws Exception {

		// Ensure locking.
		lock.lock();
		try {

			if (isLogEverything() && !isStringMethod(methodSignature)) {
				// Should we consider all the calls instead?
				registerCall(methodOwner, methodSignature, methodContext, true);
			}

			libCall_impl(methodOwner, methodSignature, methodContext, methodParameters);
		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ methodOwner + "\n" //
					+ methodSignature + "\n" //
					+ methodContext + "\n" //
					+ Arrays.toString(methodParameters) + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	private synchronized static void registerCall(Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			boolean isLibCall) {
		// Store info on the LAST invoked event for the thread
		String threadData = getThreadData();

		if (!stackTrace.containsKey(threadData)) {
			stackTrace.put(threadData, new Stack<StackElement>());
		}

		StackElement se = new StackElement();
		se.methodOwner = methodOwner;
		se.methodSignature = methodSignature;
		se.methodContext = methodContext;
		se.isLibCall = isLibCall;

		stackTrace.get(threadData).push(se);
		
		if (DEBUG) {
			Log.i(ABC_TAG, threadData + "--" + String.join("",
					Collections.nCopies(stackTrace.get(threadData).size(), "-") + "}" + se.methodSignature));
		}
	}

	/**
	 * Triggered when the execution of an app method ends normally, via return or
	 * returnVoid
	 * 
	 * 
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodContext
	 * @param returnValue
	 * @throws Throwable
	 */
	public static void onAppMethodReturnNormally(Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			Object returnValue) throws Throwable {
		lock.lock();
		try {

			returnInto_impl(methodOwner, methodSignature, methodContext, returnValue, METHOD_END_TOKEN);

		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ methodOwner + "\n" //
					+ methodSignature + "\n" //
					+ methodContext + "\n" //
					+ returnValue + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Triggered when the execution of a lib method ends normally, via return or
	 * returnVoid
	 * 
	 * 
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodContext
	 * @param returnValue
	 * @throws Throwable
	 */
	public static void onLibMethodReturnNormally(Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			Object returnValue) throws Throwable {
		lock.lock();
		try {

			returnInto_impl(methodOwner, methodSignature, methodContext, returnValue, METHOD_END_TOKEN);

		} catch (

		Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ "methodOwner " + methodOwner + "\n" //
					+ "methodSignature " + methodSignature + "\n" //
					+ "methodContext " + methodContext + "\n" //
					+ "returnValue " + returnValue + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	// TODO Most likely we need to check all the conditions not only the signature?
	// What if two calls with the same signatures are here?
	private synchronized static StackElement popMethodCall(String methodSignature) {
		// Clean up the last LibCall data since it returned normally.
		String threadData = getThreadData();

		if (!stackTrace.containsKey(threadData)) {
			android.util.Log.e(ABC_TAG,
					"Cannot find active lib calls for " + threadData + " while processing " + methodSignature);
			throw new RuntimeException("Cannot find active lib calls for " + threadData);
		}

		// Use the stack of active lib calls. Match by signature
		StackElement activeCall = stackTrace.get(threadData).peek();

		if (DEBUG) {
			Log.i(ABC_TAG, threadData + "--" + String.join("",
					Collections.nCopies(stackTrace.get(threadData).size(), "-") + "{" + methodSignature));
		}

		if (!activeCall.methodSignature.equals(methodSignature)) {
			android.util.Log.e(ABC_TAG, "Mismatch for active call " + activeCall.methodSignature + " on thread "
					+ threadData + ". Got: " + methodSignature);
			// Print stack data
			android.util.Log.e(ABC_TAG, "Stack content is:");
			while (!stackTrace.get(threadData).isEmpty()) {
				StackElement se = stackTrace.get(threadData).pop();
				android.util.Log.e(ABC_TAG, se.methodSignature);
			}

			throw new RuntimeException("Mismatch for active call " + activeCall.methodSignature + " on thread "
					+ threadData + ". Got: " + methodSignature);
		}
		// If the last active lib call matches, remove it and the other elements from
		// the stack. This is getting close to a execution stack with frames...
		return stackTrace.get(threadData).pop();

	}

	// This parameter is not really useful here
	private synchronized static StackElement peekMethodCall(String methodSignature) {
		// Clean up the last LibCall data since it returned normally.
		String threadData = getThreadData();

		if (!stackTrace.containsKey(threadData)) {
			android.util.Log.e(ABC_TAG,
					"Cannot find active lib calls for " + threadData + " while processing " + methodSignature);
			throw new RuntimeException("Cannot find active lib calls for " + threadData);
		}

		return stackTrace.get(threadData).peek();

	}

	/**
	 * Triggered when the execution of an app method ends exceptionally, i.e., via a
	 * throw stmt that is also an exit point
	 * 
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodContext
	 * @param exception
	 * @throws Throwable
	 */
	public static void onAppMethodReturnExceptionally(Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			Object exception) throws Throwable {
		lock.lock();
		try {
			returnInto_impl(methodOwner, methodSignature, methodContext, exception, METHOD_END_TOKEN_FROM_EXCEPTION);
		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR params: \n" //
					+ methodOwner + "\n" //
					+ methodSignature + "\n" //
					+ methodContext + "\n" //
					+ exception + "\n", //
					t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Triggered when the app method throws an exception. This might be an uncaught
	 * or a caught one and we need this to distinhguish whether the an exception
	 * that we captured is caused by an app method or a lib call.
	 * 
	 * @param methodOwner     The object owning the method raising this exception
	 * @param methodSignature Method throwing the exception
	 * @param methodContext   Method throwing the exception
	 * @param exception       Exception being thrown and captured by themethod
	 * @throws Throwable
	 */
	public static void onAppMethodThrowException(Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			Object exception) throws Throwable {
		lock.lock();
		try {

			/*
			 * Store that the method executed by thread T threw an exception. If this
			 * exception is caught, it will clear the data otherwise that is the exception
			 * that crashed the app (at least, if thread is UI/Main)
			 */
			String threadData = getThreadData();
			thrownExceptions.put(threadData, exception);

		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR !", t);
			throw t;
		} finally {
			lock.unlock();
		}

	}

	/**
	 * This method is triggered when an app method captures an exception in one of
	 * its traps. We need to understand who raised this exception
	 * 
	 * @param methodOwner     The object owning the method that contains this trap
	 * @param methodSignature Method containing the trap handling the exception
	 * @param methodContext   Method containing the trap handling the exception
	 * @param exception       The Exception being captured by the trap handler
	 * @throws Throwable
	 */
	public static void onAppMethodCaptureException(Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			Object exception) throws Throwable {
		lock.lock();
		try {

			// Clear the data. Note that the exception received as input should match with
			// the one stored here
			// TODO Not sure what's the point here.
			String threadData = getThreadData();
			thrownExceptions.remove(threadData);

			StackElement lastMethodCall = peekMethodCall(methodSignature);

			if (lastMethodCall.isLibCall) {

//				android.util.Log.i(ABC_TAG, "Captured exception raised by LIB method " + lastMethodCall.methodSignature
//						+ " in context " + lastMethodCall.methodContext + " inside method " + methodSignature);

				returnInto_impl(lastMethodCall.methodOwner, lastMethodCall.methodSignature,
						lastMethodCall.methodContext, exception, METHOD_END_TOKEN_FROM_EXCEPTION);
			}
//			else {
//				android.util.Log.i(ABC_TAG, "Captured exception raised by APP method " + lastMethodCall.methodSignature
//						+ " in context " + lastMethodCall.methodContext + " inside method " + methodSignature);
//			}

		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR !", t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	/*
	 * Implementation methods follow
	 */

	/**
	 * 
	 * @param methodOwner
	 * @param methodSignature
	 * @param methodContext
	 * @param methodParameters
	 * @throws IOException
	 */
	private static void libCall_impl(//
			Object methodOwner, //
			String methodSignature, //
			String methodContext, //
			java.lang.Object[] methodParameters) throws IOException {

		// Log everything
		if (isLogEverything() && !isStringMethod(methodSignature)) {

			synchronized (g_counter) {
				g_counter = g_lgclock.getLTS();
//				Integer curTS = (Integer) F.get(methodSignature);
//				if (null == curTS) {
//					curTS = 0;
//					F.put(methodSignature, g_counter);
//				}
//				L.put(methodSignature, g_counter);

//				if (!DynCGequenceOnly) {
//					A.put(g_counter, methodSignature + ":e");
//				}
				g_counter++;
				g_lgclock.increment();

				StringBuffer sb = methodStart(LIB_METHOD_START_TOKEN, methodOwner, methodSignature, methodContext,
						methodParameters);
				// android.util.Log.e("ABC:: " + g_counter, sb.toString());
				traceFileOutputWriter.write("ABC:: " + g_counter + " ");
				traceFileOutputWriter.write(sb.toString());
				traceFileOutputWriter.newLine();
				traceFileOutputWriter.flush();

			}
		}
	}

	private static String getThreadData() {
		StringBuffer content = new StringBuffer();
		content.append("[");
		if (isUiThread()) {
			content.append("UI:");
		}
		content.append(Thread.currentThread().getName()).append("]");
		return content.toString();
	}

	private static boolean isUiThread() {
		return VERSION.SDK_INT >= VERSION_CODES.M ? //
				Looper.getMainLooper().isCurrentThread() : //
				Thread.currentThread() == Looper.getMainLooper().getThread();
	}

	public static void enter_impl(//
			Object methodOwner, //
			String methodSignature, //
			java.lang.Object[] methodParameters, //
			String token) throws IOException {

		if (isLogEverything() && !isStringMethod(methodSignature)) {
			synchronized (g_counter) {
				g_counter = g_lgclock.getLTS();
//				Integer curTS = (Integer) F.get(methodSignature);
//				if (null == curTS) {
//					curTS = 0;
//					F.put(methodSignature, g_counter);
//				}
//				L.put(methodSignature, g_counter);
//
//				if (!DynCGequenceOnly) {
//					A.put(g_counter, methodSignature + ":e");
//				}
				g_counter++;
				g_lgclock.increment();

				StringBuffer sb = methodStart(token, methodOwner, methodSignature, null, methodParameters);
				// android.util.Log.e("ABC:: " + g_counter, sb.toString());
				traceFileOutputWriter.write("ABC:: " + g_counter + " ");
				traceFileOutputWriter.write(sb.toString());
				traceFileOutputWriter.newLine();
				traceFileOutputWriter.flush();
			}
		}

	}

	/* the callee could be either an actual method called or a trap */
	public static void returnInto_impl(Object methodOwner, String methodSignature, String methodContext,
			Object returnValue, String token) throws IOException {
		if (isLogEverything() && !isStringMethod(methodSignature)) {
			synchronized (g_counter) {

				// Since we return pop this call from the stack
				popMethodCall(methodSignature);

//				Integer curTS = (Integer) L.get(methodSignature);
//				if (null == curTS) {
//					curTS = 0;
//				}
				g_counter = g_lgclock.getLTS();
//				L.put(methodSignature, g_counter);
//				if (!DynCGequenceOnly) {
//					A.put(g_counter, methodSignature + ":i");
//				}

				g_counter++;
				g_lgclock.increment();

				// Write
				StringBuffer sb = methodStop(token, methodOwner, methodSignature, methodContext, returnValue);

				//
				// android.util.Log.e("ABC:: " + g_counter, sb.toString());
				//
				traceFileOutputWriter.write("ABC:: " + g_counter + " ");
				traceFileOutputWriter.write(sb.toString());
				traceFileOutputWriter.newLine();
				traceFileOutputWriter.flush();
			}
		}
	}

	/*
	 * dump the Execute-After sequence that is converted from the two event maps
	 * upon program termination event this is, however, not required but useful for
	 * debugging
	 * 
	 * 
	 * TODO I do not think we ever use this one...
	 */
	@Deprecated
	public static void terminate(String where) throws Exception {
		lock.lock();
		try {
			if (bInitialized) {
				bInitialized = false;
				terminate_impl(where);
			} else {
				// Already terminated here
				return;
			}

			synchronized (g_counter) {
				g_counter = g_lgclock.getLTS();
//				A.put(g_counter, "program end");
			}

			/** need permission to write files in android environment */
			/*
			 * serializeEvents();
			 *
			 * if (dumpCallmap) { dumpCallmap(); }
			 */
		} catch (Throwable t) {
			android.util.Log.e(ABC_TAG, "ERROR !", t);
			throw t;
		} finally {
			lock.unlock();
		}
	}

	public static void terminate_impl(String where) {
//		if (dumpCallmap) {
		android.util.Log.e(ABC_TAG + g_counter, "Application Terminated at " + where);
//		}
	}

	private static String[] extractParameterTypes(String method) {
		Pattern argsPattern = Pattern.compile("\\((.*?)\\)");
		Matcher matchPattern = argsPattern.matcher(method);
		if (matchPattern.find()) {
			return matchPattern.group(1).split(",");
		}
		return new String[] {};
	}

	// Public for testability
	public static String extractOwnerType(String method) {
		Pattern argsPattern = Pattern.compile("^<(.*):");
		Matcher matchPattern = argsPattern.matcher(method);
		if (matchPattern.find()) {
			return matchPattern.group(1);
		}
		return null;
	}

	private static boolean isString(String type) {
		return type.equals("java.lang.String");
	}

	private static boolean isArray(String type) {
		return type.startsWith("[");
	}

	private static boolean isPrimitive(String type) {
		return type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short")
				|| type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double");
	}

	/**
	 * Return a string describing the current method, its formal parameters
	 * (signature), and its actual parameters.
	 *
	 * TODO We do not TRACE calls to strings, strings are considered primitive types
	 * For Boxed Types instead we treat the as Objects, but also report their string
	 * representation... since for those there's not really need for complex
	 * Mocking...
	 *
	 */
	public static StringBuffer methodStart(String token, Object methodOwner, String method, String methodContext,
			Object... objects) {

		StringBuffer content = new StringBuffer();

		String threadData = getThreadData();

		// Append Thread information
		content.append(threadData).append(DELIMITER);

		// Append context information if any
		content.append("[" + methodContext + "]").append(DELIMITER);

		// Append opening tag
		content.append(token).append(DELIMITER);

		// TODO Why method owner is not null for CONSTRUCTORS ?!!
		// if (method.contains(": void <init>(")) {

		/*
		 * Append method owner if any but discard Strings as we treat them as
		 * primitives. Note that an array cannot be owner of method !
		 */
		if (methodOwner != null && !isString(extractOwnerType(method))) {
			// OwnerType we know it from the method signature, here we need the
			// actual object type instead...

			String actualOwnerType = methodOwner.getClass().getName();
			String formalOwnerType = extractOwnerType(method);

			if (isArray(actualOwnerType)) {
				String arrayType = getArrayType(actualOwnerType, formalOwnerType);
				content.append(arrayType + "[]" + "@" + System.identityHashCode(methodOwner));
			} else {
				// regular object
				content.append(methodOwner.getClass().getName() + "@" + System.identityHashCode(methodOwner));
			}

			// Owner can be an array

		} else if (methodOwner == null && !isString(extractOwnerType(method))) {
			content.append(extractOwnerType(method) + "@0");
		}

		// TODO NOT SURE WE NEED TO REPORT BOXED TYPE AS OWNER WITH THEIR VALUE.
		// IN CASE ADD THE CODE HERE !

		content.append(DELIMITER);

		// Append method signature
		content.append(method).append(DELIMITER);

		// Append formal/actual parameters if any or an empty "();"
		content.append("(");
		if (objects.length > 0) {
			/*
			 * The formal parameters might be different than the actual ones! Object ->
			 * String
			 *
			 * So use the actual type for the trace !
			 */
			String[] formalParametersType = extractParameterTypes(method);

			/*
			 * If an object is not null and not primitive use its actual type, otherwise use
			 * its formal type. Primitives must be always checked with formal parameters
			 * because we BOXED them already !
			 */
			for (int i = 0; i < objects.length; i++) {

				if (isPrimitive(formalParametersType[i])) {
					/*
					 * Primitive Types are passed by VALUE
					 */
					content.append(String.valueOf(objects[i]));
				} else if (objects[i] == null) {
					/*
					 * For null objects shall use their formal parameter unless those are strings or
					 * arrays
					 */

					if (isString(formalParametersType[i])) {
						/*
						 * We handle strings as primitives, with two catches: String can be null, String
						 * can be multi-line A null string corresponds to an "empty slot", and empty
						 * string corresponds to an empty array, all the others are reported on one line
						 * as byte arrays
						 */
						content.append("");
					} else if (isArray(formalParametersType[i])) {
						String arrayType = getArrayType(formalParametersType[i], formalParametersType[i]);
						content.append(arrayType + "[]" + "@" + System.identityHashCode(objects[i]));
					} else {
						// Use the formal type for null objects and @0 to indicate null value
						content.append(extractReturnType(method) + "@" + "0");
					}
				} else {
					/*
					 * Non-null objects use their ACTUAL parameter but take car of Arrays ? TODO
					 * Does this conflict with StringContent ?
					 */
					String actualParameterType = objects[i].getClass().getName();

					// Note that an array can be passed as Object to a method
					// call !
					if (isArray(actualParameterType)) {
						String arrayType = getArrayType(actualParameterType, formalParametersType[i]);
						content.append(arrayType + "[]" + "@" + System.identityHashCode(objects[i]));
					} else if (isString(actualParameterType)) {
						/*
						 * A non-null String might be disguised as java.lang.Object
						 */
						content.append(Arrays.toString(((String) objects[i]).getBytes()));
					} else {
						/*
						 * Regular Objects
						 */
						content.append(actualParameterType + "@" + System.identityHashCode(objects[i]));
						/*
						 * For non-null boxed primitives we include also their value/string
						 * representation to ease their reconstruction
						 */
						if (isBoxedPrimitive(actualParameterType)) {
							content.append(":").append(objects[i]);
						} else if (isClassObject(actualParameterType)) {
							content.append(":").append(((Class) objects[i]).getName());
						}

					}
				}

				if (i != objects.length - 1) {
					content.append(",");
				}
			}
		}
		content.append(")").append(DELIMITER);

		return content;

	}

	/*
	 * Nested arrays look like [[L -> [][], so the element type of a [[LString...
	 * array is String[], while the element type of [LString is String, the type of
	 * a [[[LString is String[][]
	 */
	private static String getArrayType(String actualArrayType, String formalArrayType) {
		// Base type
		if (actualArrayType.contains("[L")) {
			String nesting = "";
			for (char c : actualArrayType.toCharArray()) {
				if (c == '[') {
					nesting = nesting + "[]";
				} else {
					break;
				}
			}
			// We need to skip the first level of nesting.
			nesting = nesting.replaceFirst("\\[\\]", "");
			String baseType = actualArrayType.replace("[L", "").replaceAll("\\[", "").replace(";", "");
			// This is an array of Objects we return whatever is actually there.
			// TODO Problem with nested arrays ?!
			return baseType + nesting;

		} else {
			// TODO Not sure how those look like...
			// This is an array of primitives
			return formalArrayType.replace("[", "").replace("]", "");
		}
		//

	}

	private static boolean isBoxedPrimitive(String actualParameterType) {
		return boxedPrimitiveTypes.contains(actualParameterType);
	}

	private static boolean isClassObject(String actualParameterType) {
		return actualParameterType.equals("java.lang.Class");
	}

	private static String extractReturnType(String methodName) {
		return methodName.split(" ")[1];
	}

	public static boolean isVoid(String type) {
		return type.equals("void");
	}

	public static StringBuffer methodStop(String token, Object methodOwner, String method, String context,
			Object returnValueOrException) {

		/*
		 * Problem in capturing return into of <java.lang.String[][]: java.lang.String
		 * get(int)> with owner [[Ljava.lang.String;@d7b8f and return value
		 * [Ljava.lang.String;@fd5e1c 05-08 15:49:13.397 4465 4465 E ABC :
		 * java.lang.String[] cannot be cast to java.lang.String
		 */

		try {
			// We distinguish primitives and boxed using methodName which
			// specifies
			// the return type !
			StringBuffer content = new StringBuffer();
			// Append Thread information
			content.append(getThreadData()).append(DELIMITER);
			// Append Context information
			content.append("[" + context + "]").append(DELIMITER);
			// Opening Token
			content.append(token).append(DELIMITER);
			// Method Owner. But not String objects
			if (methodOwner != null && !isString(extractOwnerType(method))) {
				// OwnerType we know it from the method signature, here we need
				// the
				// actual object type instead...

				String actualOwnerType = methodOwner.getClass().getName();
				String formalOwnerType = extractOwnerType(method);

				if (isArray(actualOwnerType)) {
					String arrayType = getArrayType(actualOwnerType, formalOwnerType);
					content.append(arrayType + "[]" + "@" + System.identityHashCode(methodOwner));
				} else {
					// regular object
					content.append(methodOwner.getClass().getName() + "@" + System.identityHashCode(methodOwner));
				}

				// Owner can be an array

			} else if (methodOwner == null && !isString(extractOwnerType(method))) {
				content.append(extractOwnerType(method) + "@0");
			}

			content.append(DELIMITER);
			// Method Signature
			content.append(method).append(DELIMITER);
			// Return Value or Exception
			content.append("(");
			if (METHOD_END_TOKEN.equals(token)) {
				// Return Value
				if (isPrimitive(extractReturnType(method))) {
					content.append(returnValueOrException.toString());
				} else if (isVoid(extractReturnType(method))) {
					content.append("");
				} else if (isString(extractReturnType(method))) {
					// Null String -> empty
					if (returnValueOrException == null) {
						content.append("");
					} else {
						content.append(Arrays.toString(((String) returnValueOrException).getBytes()));
					}
				} else {
					// What about String content ?!
					if (returnValueOrException != null) {
						String returnValueActualType = returnValueOrException.getClass().getName();
						if (isArray(returnValueActualType)) {
							String arrayType = getArrayType(returnValueActualType, returnValueActualType);
							content.append(arrayType + "[]" + "@" + System.identityHashCode(returnValueOrException));
						} else {
							// Use the actual type for non-null non-string
							// objects
							content.append(returnValueOrException.getClass().getName() + "@"
									+ System.identityHashCode(returnValueOrException));
							// For class objects encode their value in the trace
							if (isClassObject(returnValueActualType)) {
								content.append(":").append(((Class) returnValueOrException).getName());
							}
						}
					} else {
						// Use the formal type for null objects
						content.append(
								extractReturnType(method) + "@" + System.identityHashCode(returnValueOrException));
					}
				}
			} else {
				// Raised Exception - cannot be null
				content.append(returnValueOrException.getClass().getName() + "@"
						+ System.identityHashCode(returnValueOrException));
			}
			content.append(")").append(DELIMITER);
			return content;
		} catch (Throwable t) {
			Log.wtf("ABC", "Problem in capturing return into of " + method + " with owner " + methodOwner
					+ " and return value " + returnValueOrException);
			Log.wtf("ABC", t);
			throw t;
		}
	}
}
