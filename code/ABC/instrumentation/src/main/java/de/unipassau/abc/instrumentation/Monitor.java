/**
 * -------------------------------------------------------------------------------------------
 * Date			Author      	Changes
 * -------------------------------------------------------------------------------------------
 * 10/19/15		hcai			Created; for monitoring method events in DynCG 
 * 10/26/15		hcai			added ICC monitoring with time-stamping call events
 * 04/22/17		hcai		added instrumentation for tracking reflection-called method
 * 12/02/19		gambi		Reduce, clean up, update and remove all the un-necessary elements
 * 27/07/20		gambi		Changed the interface and strongly simplified implementation
 * 31/03/21     gambi       Simplified to focus only on MainThread 
 *
 */
package de.unipassau.abc.instrumentation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Looper;
import android.util.Log;

/**
 * All the methods are static because it makes it easy to call them inside the
 * instrumented code.
 * 
 * @author gambitemp
 *
 */
public class Monitor {

    public static final String ABC_TAG = "ABC::";

    public final static char DELIMITER = ';';

    public final static String METHOD_START_TOKEN = "[>]";
    public final static String SYNTHETIC_METHOD_START_TOKEN = "[>s]";
    public final static String PRIVATE_METHOD_START_TOKEN = "[>p]";

    // A method that belongs to the app, but its type is abstract
    public final static String ABSTRACT_METHOD_START_TOKEN = "[>a]";

    public final static String LIB_METHOD_START_TOKEN = "[>>]";
    public final static String METHOD_END_TOKEN = "[<]";
    public final static String METHOD_END_TOKEN_FROM_EXCEPTION = "[<E]";

    public final static String ENUM_CONSTANT_GETTER = "<abc.Enum: java.lang.Object syntheticEnumConstantGetter(java.lang.String)>";

    public final static String ENUM_PROCESSING = "<abc.Enum: void processingEnum()>";

    // This is public to enable EspressoTest changing it using reflection, see
    // MonitorRule or make Soot change these values dynamically during
    // instrumentation
    // Not those are inlined constants
    public static Boolean REPORT_ONLY_MAIN_THREAD = Boolean.TRUE;
    public static Boolean DEBUG = Boolean.FALSE;

    // We still need to account for the fact that there's many threads using this
    // class
    private static ReentrantLock lock = new ReentrantLock();

    // Store trace info inside the phone memory
    private static BufferedWriter traceFileOutputWriter;

    // Simplified version of the stack trace for
    private static Stack<StackElement> stackTrace = new Stack<StackElement>();

    /*
     * Store whether an exception is thrown (and not yet handled) TODO Not clear the
     * purpose, maybe book keeping of exceptions?
     */
    protected static Object thrownException = null;

    // the global counter for time stamping each method event
    protected static Integer globalCounter = 0;

    // Is this really needed?
//    private static final logicClock g_lgclock = new logicClock(new AtomicInteger(0), "" + android.os.Process.myUid());

    /*
     * a flag ensuring the initialization and termination are both executed exactly
     * once and they are paired
     */
    protected static boolean bInitialized = false;

    // Why not a simple int ?
    protected static Integer inside_clinit = 0;

    private final static List<String> boxedPrimitiveTypes = Arrays
            .asList(new String[] { Byte.class.getName(), Short.class.getName(), Integer.class.getName(),
                    Long.class.getName(), Float.class.getName(), Double.class.getName(), Boolean.class.getName(),

            });

    private static String testName = null;

    // Book-keeping enumtypes
    private static Set<Class> processedEnums = new HashSet<Class>();
    /*
     * Integration methods. The following methods enable to control or interact with
     * the Monitor class at runtime, e.g., from the test cases, using reflection
     */

    /**
     * Notify that a new test started to execute. Reset the initialization flag
     * 
     * @param _testName
     */
    public static void testStart(String _testName) {
        testName = _testName;
        // This forces the reinitialization at the first call to the monitor
        bInitialized = false;

        android.util.Log.e(ABC_TAG, "---- CALLED testStart from " + testName);
    }

    /**
     * Notify that a test finished
     * 
     * @param _testName
     */
    public static void testEnd() {
        if (!bInitialized) {
            android.util.Log.wtf(ABC_TAG, "A test was reported to end, but the Monitor isnot yet initialized !");
        } else {
            android.util.Log.e(ABC_TAG, "---- CALLED testEnd from " + testName);
        }
        testName = null;
        bInitialized = false;
    }

    /**
     * Ensures that the monitor class is properly initialized.
     * 
     * 
     * @throws Exception
     */
    public static void initialize(String packageName) throws Exception {

        android.util.Log.e(ABC_TAG, "---- Monitor initialized");
        android.util.Log.e(ABC_TAG, "---- Monitor version 2.2 Simple Monitor");
        android.util.Log.e(ABC_TAG, "---- DEBUG " + DEBUG);
        android.util.Log.e(ABC_TAG, "---- TRACE ONLY MAIN " + REPORT_ONLY_MAIN_THREAD);

        synchronized (globalCounter) {
            // Reset the counter.
            globalCounter = 1;

            bInitialized = true;

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

            // Open the writer to this file
            traceFileOutputWriter = new BufferedWriter(new FileWriter(traceFile));

            StringBuffer sb = new StringBuffer();
            sb.append("---- STARTING TRACING for ").append(packageName).append(" output to ")
                    .append(traceFile.getAbsolutePath()).append(" ----");

            android.util.Log.e(ABC_TAG, sb.toString());

            // For the fun of it... list all the files in this folder on Android log
            android.util.Log.e(ABC_TAG, "Content of the internal storage: ");
            for (File file : traceFile.getParentFile().listFiles()) {
                android.util.Log.e(ABC_TAG, "\t- " + file.getAbsolutePath());
            }
        }

    }

    /*
     * The following methods deal with tracing the methods invocation
     */

    private static String getClassNameFromMethodSignature(String methodSignature) {
        return methodSignature.replaceFirst("<", "").split(" ")[0].replaceAll(":", "");
    }

    /**
     * Triggered when a non-private method that belongs to the instrumented
     * application starts to execute
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

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }

            // Book keeping
            registerCallInStackTrace(methodOwner, methodSignature, null, false, false);

            // Make sure we distinguish abstract classes from regular classes. We need to
            // use reflection here otherwise we need to change the instrumentationn logic
            // The issue is that if methodOwner is null, we cannot call getClass()
            // But we need to use the formal type as per the

            String theToken = METHOD_START_TOKEN;
            // Probably we should cache this
            // Is this the same as getClassNameFromMethodSignnature ?
//            String typeName = extractOwnerType(methodSignature);

            try {
                // Note Lambdas cannot be loaded by name, so we need to consider that case as
                // well... this may fail
                Class theClass = Class.forName(getClassNameFromMethodSignature(methodSignature));
                theToken = (Modifier.isAbstract(theClass.getModifiers())) ? ABSTRACT_METHOD_START_TOKEN
                        : METHOD_START_TOKEN;
            } catch (Exception e) {

            }

            processEnums(methodParameters, null);

            enter_impl(methodOwner, methodSignature, methodParameters, theToken);
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

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }

            // Book keeping
            registerCallInStackTrace(methodOwner, methodSignature, null, false, false);

            processEnums(methodParameters, null);

            // Trace
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
     * Triggered when the execution starts a method that does not exist. For
     * example, accessing an array element or setting an array element are not
     * implemented as method calls, so our instrumentation fakes them as synthetic
     * methods.
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

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }

            // Book keeping
            boolean isSynthetic = true;
            registerCallInStackTrace(methodOwner, methodSignature, null, false, isSynthetic);

            processEnums(methodParameters, null);

            // Trace
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
     * Triggered when the execution starts a method that does not belong to the
     * instrumented app, so we cannot carver it
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
            // Assume the monitor is initialized at this point
            if (!bInitialized) {
                android.util.Log.e(ABC_TAG, "The Monitor is not yet initialized. Cannot trace: \n" //
                        + methodOwner + "\n" //
                        + methodSignature + "\n" //
                        + methodContext + "\n" //
                        + Arrays.toString(methodParameters));
                return;
            }

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }

            // Book keeping
            boolean isLibCall = true;
            registerCallInStackTrace(methodOwner, methodSignature, methodContext, isLibCall, false);

            processEnums(methodParameters, methodContext);

            // Trace
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

    /*
     * The following methods deal with return from methods
     */

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
            // Assume the monitor is initialized at this point
            if (!bInitialized) {
                android.util.Log.e(ABC_TAG, "The Monitor is not yet initialized. Cannot trace: \n" //
                        + methodOwner + "\n" //
                        + methodSignature + "\n" //
                        + methodContext);
                return;
            }

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }

            // Book keeping
            StackElement returningCall = popCallFromStackTrace(methodSignature);

            processEnums(new Object[] { returnValue }, methodContext);
            // Trace
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

            // Assume the monitor is initialized at this point
            if (!bInitialized) {
                android.util.Log.e(ABC_TAG, "The Monitor is not yet initialized. Cannot trace: \n" //
                        + methodOwner + "\n" //
                        + methodSignature + "\n" //
                        + methodContext);
                return;
            }

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }
            // Book keeping
            StackElement returningCall = popCallFromStackTrace(methodSignature);

            processEnums(new Object[] { returnValue }, methodContext);

            // Trace
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

    /*
     * The following methods deal with exceptional cases. They are relevant only for
     * app methods and not lib methods, because we cannot easily track inside the
     * libs where problems arise and how exceptions propagate
     */

    /**
     * This method is triggered when an app method captures an exception. This is
     * necessary to trace how exceptions propagate and requires to understand who
     * raised this exception.
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
            // Assume the monitor is initialized at this point
            if (!bInitialized) {
                android.util.Log.e(ABC_TAG, "The Monitor is not yet initialized. Cannot trace: \n" //
                        + methodOwner + "\n" //
                        + methodSignature + "\n" //
                        + methodContext + "\n" //
                        + exception);
                return;
            }

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }
            // Clear the data. Note that the exception received as input should match with
            // the one stored here.
            // TODO Do we need to check this?
            //
            thrownException = null;

            StackElement lastMethodCall = stackTrace.peek();

            // If the exception was thrown by someone else, not an app method we need to
            // forcefully log that that method returned exceptionally. This is because the
            // tracing logic that is meant to be executed after the method is not executed
            // because of the exception!
//            android.util.Log.e(ABC_TAG, "onAppMethodCaptureException: \n" //
//                    + methodOwner + "\n" //
//                    + methodSignature + "\n" //
//                    + methodContext + "\n" //
//                    + exception);
//            
//            android.util.Log.e(ABC_TAG, "onAppMethodCaptureException: lastMethodCall: " + lastMethodCall.methodSignature );

            if (lastMethodCall.isLibCall) {

                // Book keeping
                popCallFromStackTrace(lastMethodCall.methodSignature);

                processEnums(new Object[] { exception }, methodContext);
                // Trace
                returnInto_impl(lastMethodCall.methodOwner, lastMethodCall.methodSignature,
                        lastMethodCall.methodContext, exception, METHOD_END_TOKEN_FROM_EXCEPTION);
            }

            // If the exception was thrown by an app method, then this will be tracked by
            // capturing the moment it throws the exception. Our instrumentation ensures
            // that both handled and runtime exceptions are reported!

        } catch (Throwable t) {
            android.util.Log.e(ABC_TAG, "ERROR !", t);
            throw t;
        } finally {
            lock.unlock();
        }
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
            // Assume the monitor is initialized at this point
            if (!bInitialized) {
                android.util.Log.e(ABC_TAG, "The Monitor is not yet initialized. Cannot trace: \n" //
                        + methodOwner + "\n" //
                        + methodSignature + "\n" //
                        + methodContext);
                return;
            }

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }
            // Book keeping
            StackElement returningCall = popCallFromStackTrace(methodSignature);

            processEnums(new Object[] { exception }, methodContext);
            // Trace
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

            // Assume the monitor is initialized at this point
            if (!bInitialized) {
                android.util.Log.e(ABC_TAG, "The Monitor is not yet initialized. Cannot trace: \n" //
                        + methodOwner + "\n" //
                        + methodSignature + "\n" //
                        + methodContext + "\n" //
                        + exception);
                return;
            }

            // Respond to this method only if the method call is executed by the main thread
            if (REPORT_ONLY_MAIN_THREAD && !isUiThread()) {
                return;
            }
            /*
             * Store the exception that will be raised
             */
            thrownException = exception;

            // We don't do nothing more here because the onAppMethodReturnExceptionally will
            // take care of tracing the exceptional return

        } catch (Throwable t) {
            android.util.Log.e(ABC_TAG, "ERROR !", t);
            throw t;
        } finally {
            lock.unlock();
        }

    }

    // Utility methods

    private static boolean isStringMethod(String methodSignature) {
        return isString(methodSignature.replaceFirst("<", "").split(" ")[0].replaceAll(":", ""), null);
    }

    /**
     * This assumes that only calls from main thread are considered. We need this to
     * validate executions and to correctly match exceptional returns from method
     * calls. Assumes that the Main Thread is calling the method
     * 
     * @param methodOwner
     * @param methodSignature
     * @param methodContext
     * @param isLibCall
     */
    private static void registerCallInStackTrace(Object methodOwner, //
            String methodSignature, //
            String methodContext, //
            boolean isLibCall, boolean isSynthetic) {

        // Book keeping. TODO Can be improved by skipping calls to java.lang.String

        inside_clinit = (methodSignature.contains("clinit")) ? inside_clinit + 1 : inside_clinit;

        StackElement se = new StackElement();
        se.methodOwner = methodOwner;
        se.methodSignature = methodSignature;
        se.methodContext = methodContext;

        se.isLibCall = isLibCall;
        // TODO Remove this flag
        se.isSynthetic = isSynthetic;

        stackTrace.push(se);

//        if (DEBUG) {
//            Log.i(ABC_TAG, String.join("", Collections.nCopies(stackTrace.size(), "-") + "}" + se.methodSignature));
//        }
    }

    /**
     * This is the dual method of registerCallInStackTrace. It assumes to be invoked
     * from MainThread
     *
     * TODO: Note that the check is imprecise since we look only at the method
     * signature
     * 
     * @param methodSignature
     * @return
     */
    private synchronized static StackElement popCallFromStackTrace(String methodSignature) {
        // Use the stack of active lib calls. Match by signature
        StackElement activeCall = stackTrace.peek();

//        if (DEBUG) {
//            Log.i(ABC_TAG, String.join("", Collections.nCopies(stackTrace.size(), "-") + "{" + methodSignature));
//        }

        if (!activeCall.methodSignature.equals(methodSignature)) {
            android.util.Log.e(ABC_TAG, "Mismatch for active call " + activeCall.methodSignature + ". Got: "
                    + methodSignature + " instead");
            // Print stack data
            android.util.Log.e(ABC_TAG, "Stack content is:");
            while (!stackTrace.isEmpty()) {
                StackElement se = stackTrace.pop();
                android.util.Log.e(ABC_TAG, se.methodSignature);
            }

            throw new RuntimeException("Mismatch for active call " + activeCall.methodSignature + ". Got: "
                    + methodSignature + " instead");
        }
        // If the last active lib call matches, remove it and the other elements from
        // the stack. This is getting close to a execution stack with frames...
        StackElement se = stackTrace.pop();
        // If we are inside a clinit and we pop it, then we are not inside a clinit
        // TODO Assuming there's ONLY one clinit !

        if (inside_clinit > 0) {

            inside_clinit = methodSignature.contains("clinit") ? inside_clinit - 1 : inside_clinit;

//            if (methodSignature.contains("clinit")) {
//                Log.i(ABC_TAG, "<<< Moving outside clinit " + methodSignature + " level " + inside_clinit);
//            }
        }

        return se;

    }

    /*
     * The following methods implement the low level logic for tracing the events
     */

    /**
     * 
     * @param methodOwner
     * @param methodSignature
     * @param methodParameters
     * @param token
     * @throws IOException
     */
    public static void enter_impl(//
            Object methodOwner, //
            String methodSignature, //
            java.lang.Object[] methodParameters, //
            String token) throws IOException {

        synchronized (globalCounter) {
            if (!isStringMethod(methodSignature)) {
                globalCounter++;

                // We need to process the enums BEFORE the start of the method to ensure
                // EnumConstants are introduced before using them, but avoid clinit

                StringBuffer sb = methodStart(token, methodOwner, methodSignature, null, methodParameters);

                traceFileOutputWriter.write(ABC_TAG + " ");
                traceFileOutputWriter.write(globalCounter + " ");
                traceFileOutputWriter.write(sb.toString());
                traceFileOutputWriter.newLine();
                traceFileOutputWriter.flush();

            }
        }
    }

    // Make sure we output the values of any enum inside the trace !
    // This might create problems with the <clinit> method of the Enum itself
    // https://docs.oracle.com/javase/tutorial/reflect/special/enumMembers.html
    private static void processEnums(Object[] objects, String methodContext) throws IOException {

        if (inside_clinit > 0) {
//            android.util.Log.e(ABC_TAG, "     ----------- Skip because inside a clinit. level " + inside_clinit);
            return;
        }

        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null //
                    && objects[i].getClass().isEnum() //
                    && !processedEnums.contains(objects[i].getClass())) {

//                android.util.Log.e(ABC_TAG,
//                        "     ----------- Found Enum for " + objects[i] + " in context " + methodContext);
                // Avoid infinite recursion !
                processedEnums.add(objects[i].getClass());

                try {

                    Object enumObject = objects[i];
                    Class enumClass = enumObject.getClass();

                    // If this is an array class we need to extract the base type?
                    // We do not support

                    /*
                     * Calling enumClass.getEnumConstants() on an ENUM defined by the app results in
                     * logging calls to to values[] and clone() on the Enums which then might create
                     * wrong static dependencies.
                     * 
                     * To avoid this, we wrap the entire processing of enums inside a "fake call"
                     * that we can remove from the trace entirely
                     */
                    libCall_impl(null, ENUM_PROCESSING, methodContext, new Object[] {});

                    for (Object enumConstant : enumClass.getEnumConstants()) {
                        // DEBUG
//                        android.util.Log.e(ABC_TAG, "     ----------- Enum Constant " + enumConstant + " --> "
//                                + enumClass + "@" + System.identityHashCode(enumConstant));
                        // TODO Why not a syntetic call instead?
                        libCall_impl(null, ENUM_CONSTANT_GETTER, methodContext,
                                // The only parameter is the string name of the constant
                                new Object[] { enumConstant.toString() });
                        returnInto_impl(null, ENUM_CONSTANT_GETTER, methodContext,
                                // The return value is the objectConstant
                                enumConstant, METHOD_END_TOKEN);
                    }

                    returnInto_impl(null, ENUM_PROCESSING, methodContext, null, METHOD_END_TOKEN);

                } catch (Exception e) {
                    android.util.Log.e(ABC_TAG, "FAILED TO PROCESS ENUM FOR " + objects[i]);
                }
            }
        }
    }

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
        synchronized (globalCounter) {
            // We do not trace java.lang.String methods
            if (!isStringMethod(methodSignature)) {

                globalCounter++;

                StringBuffer sb = methodStart(LIB_METHOD_START_TOKEN, methodOwner, methodSignature, methodContext,
                        methodParameters);

                traceFileOutputWriter.write(ABC_TAG + " ");
                traceFileOutputWriter.write(globalCounter + " ");
                traceFileOutputWriter.write(sb.toString());
                traceFileOutputWriter.newLine();
                traceFileOutputWriter.flush();

            }
        }
    }

    public static void returnInto_impl(Object methodOwner, String methodSignature, String methodContext,
            Object returnValue, String token) throws IOException {
        synchronized (globalCounter) {
            if (!isStringMethod(methodSignature)) {
                // Do we really need the counter here?
                globalCounter++;

                // Write
                StringBuffer sb = methodStop(token, methodOwner, methodSignature, methodContext, returnValue);

                //
                // android.util.Log.e("ABC:: " + g_counter, sb.toString());
                //
                traceFileOutputWriter.write(ABC_TAG + " ");
                traceFileOutputWriter.write(globalCounter + " ");
                traceFileOutputWriter.write(sb.toString());
                traceFileOutputWriter.newLine();
                traceFileOutputWriter.flush();

            }
        }
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
    private static StringBuffer methodStart(String token, Object methodOwner, String method, String methodContext,
            Object... objects) {

        StringBuffer content = new StringBuffer();

        // We need this even if we log only UI thread
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
        if (methodOwner != null && !isString(extractOwnerType(method), methodOwner)) {
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

        } else if (methodOwner == null && !isString(extractOwnerType(method), methodOwner)) {
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

                    if (isString(formalParametersType[i], objects[i])) {
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
                        // Use the formal type for null parameter and @0 to indicate null value
                        content.append(formalParametersType[i] + "@" + "0");
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
                    } else if (isString(actualParameterType, objects[i])) {
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

    private static StringBuffer methodStop(String token, Object methodOwner, String method, String context,
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
            if (methodOwner != null && !isString(extractOwnerType(method), methodOwner)) {
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

            } else if (methodOwner == null && !isString(extractOwnerType(method), methodOwner)) {
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
                } else if (isString(extractReturnType(method), returnValueOrException)) {
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
            Log.wtf(ABC_TAG, "Problem in capturing return into of " + method + " with owner " + methodOwner
                    + " and return value " + returnValueOrException);
            Log.wtf(ABC_TAG, t);
            throw t;
        }
    }

    /*
     * Utility and other methods follow
     */

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

    private static boolean isString(String formalType, Object objectInstance) {
        String actualType = (objectInstance != null) ? objectInstance.getClass().getName() : formalType;
        return "java.lang.String".equals(formalType) || "java.lang.String".equals(actualType);
    }

    private static boolean isArray(String type) {
        return type.startsWith("[");
    }

    private static boolean isPrimitive(String type) {
        return type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short")
                || type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double");
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

}
