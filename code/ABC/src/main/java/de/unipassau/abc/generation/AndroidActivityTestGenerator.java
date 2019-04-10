package de.unipassau.abc.generation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;
import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.DataNode;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodCallLiteralValue;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.NullInstance;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.PrimitiveValue;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.utils.JimpleUtils;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

/**
 * From: https://developer.android.com/guide/components/activities/testing To
 * test how your app's activities behave during device-level events, such as the
 * following:
 * 
 * Another app, such as the device's phone app, interrupts your app's activity.
 * The system destroys and recreates your activity. The user places your
 * activity in a new windowing environment, such as picture-in-picture (PIP) or
 * multi-window.
 * 
 * Setup the test, a.k.a, Drive an activity's state: One key aspect of testing
 * your app's activities involves placing your app's activities in particular
 * states.
 *
 * To define this "given" part of your tests, use instances of ActivityScenario,
 * part of the AndroidX Test library. By using this class, you can place your
 * activity in states that simulate the device-level events described at the
 * beginning of this page.
 * 
 * ActivityScenario is a cross-platform API that you can use in local unit tests
 * and on-device integration tests alike. On a real or virtual device,
 * ActivityScenario provides thread safety, synchronizing events between your
 * test's instrumentation thread and the thread that runs your activity under
 * test. The API is also particularly well-suited for evaluating how an activity
 * under test behaves when it's destroyed or created.
 * 
 * When one activity starts another, they both experience lifecycle transitions.
 * The first activity stops operating and enters the Paused or Stopped state,
 * while the other activity is created. In case these activities share data
 * saved to disc or elsewhere, it's important to understand that the first
 * activity is not completely stopped before the second one is created. Rather,
 * the process of starting the second one overlaps with the process of stopping
 * the first one.
 * 
 * 
 * Activities (And in general the various components interact) using mostly
 * Intents.
 * 
 * If the intent is generated inside the app we can see that, but if it is
 * generated outside I am not sure we can generate it. Possibly we can observe
 * it inside? Maybe via the filter?
 * 
 * There are separate methods for activating each type of component:
 * 
 * You can start an activity or give it something new to do by passing an Intent
 * to startActivity() or startActivityForResult() (when you want the activity to
 * return a result). With Android 5.0 (API level 21) and later, you can use the
 * JobScheduler class to schedule actions. For earlier Android versions, you can
 * start a service (or give new instructions to an ongoing service) by passing
 * an Intent to startService(). You can bind to the service by passing an Intent
 * to bindService(). You can initiate a broadcast by passing an Intent to
 * methods such as sendBroadcast(), sendOrderedBroadcast(), or
 * sendStickyBroadcast().
 * 
 * 
 * @author gambi
 *
 */
public class AndroidActivityTestGenerator {

    /*
     * TODO Move the logic which creates the ActivityController and such back to
     * the Carver. The carver does the logic, the generator simply translate the
     * ExecutionGraph to java code
     * 
     * @author gambi
     *
     */

    public interface TestGeneratorCLI {
        @Option(longName = "carve-test-files")
        List<File> getCarvedTestsFiles();

        @Option(longName = "apk")
        File getAPK();

        @Option(longName = "android-jar")
        File getAndroidJar();

        @Option(longName = "store-tests")
        File getOutputDir();
    }

    public static synchronized void loadDirectory(java.io.File dirClasses) {
        File[] subdirs = dirClasses.listFiles();
        for (File subdir : subdirs) {
            if (subdir.isDirectory()) {
                loadDirectory(subdir);
            } else {
                loadFile(subdir);
            }
        }

    }

    /*
     * Adds the supplied Java Archive library to java.class.path. This is benign
     * if the library is already loaded.
     */
    public static synchronized void loadFile(java.io.File jar) {
        try {
            System.out.println("AndroidActivityTestGenerator.loadFile() " + jar);
            /*
             * We are using reflection here to circumvent encapsulation; addURL
             * is not public
             */
            java.net.URLClassLoader loader = (java.net.URLClassLoader) ClassLoader.getSystemClassLoader();
            java.net.URL url = jar.toURI().toURL();
            /* Disallow if already loaded */
            for (java.net.URL it : java.util.Arrays.asList(loader.getURLs())) {
                if (it.equals(url)) {
                    return;
                }
            }
            java.lang.reflect.Method method = java.net.URLClassLoader.class.getDeclaredMethod("addURL",
                    new Class[] { java.net.URL.class });
            method.setAccessible(
                    true); /* promote the method to public access */
            method.invoke(loader, new Object[] { url });
        } catch (final java.lang.NoSuchMethodException | java.lang.IllegalAccessException
                | java.net.MalformedURLException | java.lang.reflect.InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * This is global anyway. Public for testing. Note that this loads all the
     * classes in my classpath ! COde Duplication !
     */
    /*
     * This is global anyway. Public for testing. Note that this loads all the
     * classes in my classpath ! COde Duplication !
     */
    public static void setupSoot(File androidJar, File apk) {
        G.reset();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);

        // Not sure this will work with the APK
        ArrayList<String> necessaryJar = new ArrayList<String>();
        // Include here entries from the classpath
        for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
            if (cpEntry.contains("robolectric")) {
                necessaryJar.add(cpEntry);
            }
        }
        necessaryJar.add(apk.getAbsolutePath());

        Options.v().set_soot_classpath(androidJar.getAbsolutePath());
        Options.v().set_process_dir(necessaryJar);
        Options.v().set_src_prec(soot.options.Options.src_prec_apk);

        // Soot has problems in working on mac.
        String osName = System.getProperty("os.name");
        System.setProperty("os.name", "Whatever");
        Scene.v().loadNecessaryClasses();
        System.setProperty("os.name", osName);
        // This is mostlty to check
        Scene.v().loadClassAndSupport(ActivityController.class.getName());
    }

    // https://guides.codepath.com/android/unit-testing-with-robolectric
    public static void main(String[] args) {
        AtomicInteger id = new AtomicInteger(0);

        try {
            TestGeneratorCLI cli = CliFactory.parseArguments(TestGeneratorCLI.class, args);
            XStream xStream = new XStream();

            setupSoot(cli.getAndroidJar(), cli.getAPK());

            // /*
            // * Hackinsh -- include the use provided jars to the classloader
            // * using reflection
            // */
            // System.out.println("AndroidActivityTestGenerator.main() Adding "
            // + cli.getAndroidJar() + " to CP");
            // loadFile(cli.getAndroidJar());
            //
            // for (File cpEntry : cli.getApplicationClassPath()) {
            // System.out.println("AndroidActivityTestGenerator.main() Adding "
            // + cpEntry + " to CP");
            // loadFile(cpEntry);
            // }

            /*
             * Start the test generation. At the moment we generate one test
             * class for each carved test.
             * 
             */
            for (File carveTestFile : cli.getCarvedTestsFiles()) {
                /*
                 * TODO Make a proper object out of this pair !
                 */
                Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest = (Pair<ExecutionFlowGraph, DataDependencyGraph>) xStream
                        .fromXML(carveTestFile);

                AndroidActivityTestGenerator testGenerator = new AndroidActivityTestGenerator();

                /*
                 * We cannot generate a test for testing the activity
                 * constructor "directly". We can technically, but not sure it
                 * makes sense
                 */
                if (!carvedTest.getFirst().getLastMethodInvocation().isConstructor()) {
                    String carvedTestName = "CarvedTest_" + id.incrementAndGet();
                    String testCode = testGenerator.generateTest(carvedTest, carvedTestName);
                    // Write to a file in output folder
                    File outputFile = new File(cli.getOutputDir(), carvedTestName + ".java");
                    try (PrintWriter out = new PrintWriter(outputFile)) {
                        out.println(testCode);
                    }
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static Class<?> mostSpecificCommonSuperclass(Class<?> a, Class<?> b) {
        Class<?> s = a;
        while (!s.isAssignableFrom(b)) {
            s = s.getSuperclass();
        }
        return s;
    }

    /**
     * Tests do not have control flow, they are just a list of statements to be
     * executed.
     * 
     * To test activity we rely on Roboelectrics.
     * 
     * To make tests run fine I suspect we might need to provide additional
     * configurations to Robolectric, Manifest, etc.
     * 
     * https://stackoverflow.com/questions/7249169/how-do-i-get-current-activity-with-robolectric
     * 
     * @param carvedTest
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public String generateTest(Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest, String carvedTestName)
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        /*
         * Loading class in the JVM ensures that the correct import statement
         * are generated, however, not all the classes cannot be imported so
         * easily (i.e., without tweaking the class loader or so). So we simply
         * add the import based on their package...
         */

        // https://www.baeldung.com/java-poet
        MethodInvocation methodInvocationUnderTest = carvedTest.getFirst().getLastMethodInvocation();

        // This must be an activity
        // TODO This is an heuristic we definitively shall improve DTO
        String classUnderTest = JimpleUtils.getClassNameForMethod(methodInvocationUnderTest.getMethodSignature());
        String packageName = classUnderTest.substring(0, classUnderTest.lastIndexOf("."));
        String activityClassName = classUnderTest.replace(packageName, "").replace(".", "");

        String methodUnderTest = JimpleUtils.getMethodName(methodInvocationUnderTest.getMethodSignature());
        String testName = "test" + methodUnderTest;

        ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();
        DataDependencyGraph dataDependencyGraph = carvedTest.getSecond();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(testName)//
                .addAnnotation(Test.class)//
                .addException(Exception.class) //
                .addModifiers(Modifier.PUBLIC).returns(void.class);

        /*
         * XXX Apparently I cannot call block.build() and then add the resulting
         * code block to the method... maybe some type inference maybe some
         * other stuff...
         */

        /*
         * Declare and initialize local variables to default values (i.e.,
         * null). Do not generate variables for ValueNodes/Primitives.
         * 
         * Note that because of aliases variables with different types are
         * actually the same variable !
         * 
         */

        /*
         * Resolve alias
         */

        resolveAliases(dataDependencyGraph);
        //

        // Declaring all the variables before using them. Note we do not
        // generate code at this point.
        for (DataNode dataNode : dataDependencyGraph.getDataNodes()) {

            if (dataNode instanceof MethodCallLiteralValue) {
                continue;
            }

            System.out.println("AndroidActivityTestGenerator.generateTest() Declaring variable for " + dataNode);
            // Pair<String, String> variable =
            declareVariableFor(dataNode, dataDependencyGraph);
        }

        /*
         * Unit tests cannot handle more than one activity ! The only case where
         * they handle two is to verify that the activity under test "triggers"
         * the correct activity via an intent.
         */
        for (MethodInvocation methodInvocation : executionFlowGraph.getOrderedMethodInvocations()) {
            System.out.println("AndroidActivityTestGenerator.generateTest() - " + methodInvocation);

            /*
             * Include a comment in the code pointing to the original method
             * invocation.
             */
            methodBuilder.addComment("$L", methodInvocation);// .toString().replaceAll("\\$",
                                                             // ".") );
            // Create the code here. Distingui
            // Check if the variables have
            // if (variable == null) {
            // System.out.println("AndroidActivityTestGenerator.generateTest()
            // Variable already declared " + dataNode);
            // // Variable is already declared
            // continue;
            // }
            // if (dataNode instanceof ObjectInstance) {
            //
            // /*
            // * Activities are managed objects, we cannot instantiate them
            // * directly, but we need to declare them anyway
            // */
            // methodBuilder.addStatement(variable.getFirst() + " " +
            // variable.getSecond() + " = null");
            // } else {
            // methodBuilder.addStatement(variable.getFirst() + " " +
            // variable.getSecond());
            // }

            // ATM We handle Robolectrics here:

            if (belongsToAndroidActivity(methodInvocation)) {
                generateCodeForAndroidActivity(methodBuilder, methodInvocation, activityClassName);
            } else {
                generateCode(methodBuilder, methodInvocation);
            }
        }

        // TODO Include assertions on state and behavior
        // Assertions shall be provided as well...
        // methodBuilder.addStatement("$T.assertNotNull( " +
        // getVariableFor(owner).getSecond() + " )", Assert.class);

        MethodSpec testMethod = methodBuilder.build();

        TypeSpec testCase = TypeSpec.classBuilder(carvedTestName)//
                .addAnnotation(AnnotationSpec.builder(RunWith.class)//
                        .addMember("value", "$T.class", RobolectricTestRunner.class)//
                        .build())//
                .addModifiers(Modifier.PUBLIC)//
                .addMethod(testMethod)//
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, testCase).build();
        javaFile.writeTo(System.out);

        return javaFile.toString();
    }

    private void generateCode(Builder methodBuilder, MethodInvocation methodInvocation) {
        /*
         * Android activity objects shall be handled (init, onCreate, etc..) via
         * an ActivityController
         */
        StringBuilder parameters = new StringBuilder();
        for (DataNode parameter : methodInvocation.getActualParameterInstances()) {

            parameters.append(getParameterFor(parameter));
            if (methodInvocation.getActualParameterInstances()
                    .indexOf(parameter) < methodInvocation.getActualParameterInstances().size() - 1) {
                parameters.append(",");
            }
        }

        // String returnValue = "";
        // if
        // (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature()))
        // {
        // returnValue =
        // getVariableFor(methodInvocation.getReturnValue()).getSecond() + " =
        // ";
        // }

        /*
         * Instantiate NON - Array objects
         */
        if (methodInvocation.isConstructor() && !JimpleUtils.isArray(methodInvocation.getOwner().getType())) {
            String type = getVariableFor(methodInvocation.getOwner()).getFirst();
            // Use simple names instead of fully qualified names...
            // type = type.substring(type.lastIndexOf(".") + 1,
            // type.length());

            String name = getVariableFor(methodInvocation.getOwner()).getSecond();
            if (firstUseOf(methodInvocation.getOwner())) {
                methodBuilder.addStatement(type + " " + name + " = new " + type + "(" + parameters.toString() + ")");
                initializedVariables.add(methodInvocation.getOwner());
            } else {
                methodBuilder.addStatement(name + " = new " + type + "(" + parameters.toString() + ")");
            }
        } else if (methodInvocation.isConstructor() && JimpleUtils.isArray(methodInvocation.getOwner().getType())) {
            /*
             * Instantiate Array. Object[] bla = new Object[0];
             */

            String type = getVariableFor(methodInvocation.getOwner()).getFirst();
            String baseArrayType = getVariableFor(methodInvocation.getOwner()).getFirst().replace("[]", "");
            String name = getVariableFor(methodInvocation.getOwner()).getSecond();

            if (firstUseOf(methodInvocation.getOwner())) {
                methodBuilder.addStatement(
                        type + " " + name + " = new " + baseArrayType + "[" + parameters.toString() + "]");
            } else {
                methodBuilder.addStatement(name + " = new " + baseArrayType + "[" + parameters.toString() + "]");
            }
        } else if (methodInvocation.isStatic()) {

            String returnValue = "";

            if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())
                    && firstUseOf(methodInvocation.getReturnValue())) {
                String type = getVariableFor(methodInvocation.getReturnValue()).getFirst();
                returnValue = getVariableFor(methodInvocation.getReturnValue()).getSecond();
                methodBuilder.addStatement(type + " " + returnValue + " = " 
                        + JimpleUtils.getFullyQualifiedMethodName(methodInvocation.getMethodSignature()) + "("
                        + parameters.toString() + ")");
            } else {
                methodBuilder.addStatement(
                        returnValue + JimpleUtils.getFullyQualifiedMethodName(methodInvocation.getMethodSignature())
                                + "(" + parameters.toString() + ")");
            }
        } else {
            String returnValue = "";

            if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())
                    && firstUseOf(methodInvocation.getReturnValue())) {
                String type = getVariableFor(methodInvocation.getReturnValue()).getFirst();
                returnValue = getVariableFor(methodInvocation.getReturnValue()).getSecond();
                methodBuilder
                        .addStatement(type + " " + returnValue  + " = " + getVariableFor(methodInvocation.getOwner()).getSecond()
                                + "." + JimpleUtils.getMethodName(methodInvocation.getMethodSignature()) + "("
                                + parameters.toString() + ")");
            } else {
                methodBuilder.addStatement(returnValue + getVariableFor(methodInvocation.getOwner()).getSecond() + "."
                        + JimpleUtils.getMethodName(methodInvocation.getMethodSignature()) + "(" + parameters.toString()
                        + ")");
            }
        }

    }

    private void generateCodeForAndroidActivity(Builder methodBuilder, MethodInvocation methodInvocation,
            String activityClassName) {
        /*
         * Android activity objects shall be handled (init, onCreate, etc..) via
         * an ActivityController
         */
        StringBuilder parameters = new StringBuilder();
        for (DataNode parameter : methodInvocation.getActualParameterInstances()) {

            parameters.append(getParameterFor(parameter));
            if (methodInvocation.getActualParameterInstances()
                    .indexOf(parameter) < methodInvocation.getActualParameterInstances().size() - 1) {
                parameters.append(",");
            }
        }

        String returnValue = "";
        if (!JimpleUtils.hasVoidReturnType(methodInvocation.getMethodSignature())) {
            returnValue = getVariableFor(methodInvocation.getReturnValue()).getSecond() + " = ";
        }

        ObjectInstance owner = methodInvocation.getOwner();

        /*
         * Configure the activityController to handle activity lifecycle events
         */
        if (methodInvocation.isConstructor()) {

            // Instantiate the ActivityController ?
            // TODO How to check this is the Activity Under Test ->
            // owner of last method invocation? Maybe an explicit
            // reference -> create a CarvedTest object
            methodBuilder
                    .addStatement("$T<$L> activityController = $T.buildActivity($L.class)", //
                            ActivityController.class, activityClassName, Robolectric.class, activityClassName) //
                    /*
                     * Assign the reference of the activity to its variable
                     */
                    .addStatement("$L $L = activityController.get()", getVariableFor(owner).getFirst(),
                            getVariableFor(owner).getSecond());

        } else if (methodInvocation.isAndroidActivityCallback()) {
            String androidLifeCycleEventName = getCorrespondingMethodCall(methodInvocation);
            methodBuilder.addStatement("activityController." + androidLifeCycleEventName + "(" + parameters + ")"); //

        } else {
            /*
             * This is a method which the class implements but is not related to
             * activity lifecycle. So we invoke it like plain java method.
             */
            methodBuilder.addStatement(returnValue + getVariableFor(owner).getSecond() + "."
                    + JimpleUtils.getMethodName(methodInvocation.getMethodSignature()) + "(" + parameters.toString()
                    + ")");
        }

    }

    private boolean belongsToAndroidActivity(MethodInvocation methodInvocation) {
        return methodInvocation.getOwner() != null && methodInvocation.getOwner().isAndroidActivity();
    }

    /*
     * Retype all the aliases and break
     */
    private void resolveAliases(DataDependencyGraph dataDependencyGraph) {

        List<Set<ObjectInstance>> aliases = new ArrayList<>();

        for (DataNode dataNode : dataDependencyGraph.getDataNodes()) {

            if (dataNode instanceof ObjectInstance) {
                Set<ObjectInstance> aliasSet = dataDependencyGraph.getAliasesOf((ObjectInstance) dataNode);
                aliasSet.add((ObjectInstance) dataNode);

                if (aliases.contains(aliasSet)) {
                    continue;
                }
                aliases.add(aliasSet);
            }
        }

        /*
         * Do we need to resolve aliases ?
         */
        // We cannot store typing information in terms of java classes but we
        // might be able to do so using SootClasses?
        // Map<Set<ObjectInstance>, String> aliasClassesToFormalType = new
        // HashMap<>();
        for (Set<ObjectInstance> aliasSet : aliases) {
            if (aliasSet.size() <= 1) {
                continue;
            }
            System.out.println("AndroidActivityTestGenerator.generateTest() Aliases " + aliasSet);
            // [java.util.List@184272637, java.util.Arrays$ArrayList@184272637]
            for (ObjectInstance alias : aliasSet) {
                if (alias.getType().equals("java.util.Arrays$ArrayList")) {
                    // Force update the type associated with this one.
                    ObjectInstance.retype(alias, "java.util.List");
                }
            }
            // TODO Maybe you can use Soot for this...
            // TODO Maybe we can create one variable of each type using formal
            // parameters?
            // Class commonSuperClass = findCommonSuperClass(aliasSet);
            // aliasToClass.put( aliasSet, commonSuperClass);
        }
    }

    private Class findCommonSuperClass(Set<ObjectInstance> aliases) throws ClassNotFoundException {
        Set<Class> aliasTypes = new HashSet<>();
        for (ObjectInstance objectInstance : aliases) {
            aliasTypes.add(Class.forName(objectInstance.getType()));
        }
        // Analyze each set and choose the best candidate for typing. This might
        // be the common interface or something?
        while (aliasTypes.size() > 1) {
            Iterator<Class> iterator = aliasTypes.iterator();
            // Take two
            Class a = iterator.next();
            Class b = iterator.next();
            // Remove them from the set
            aliasTypes.remove(a);
            aliasTypes.remove(b);
            // Add their common super class
            aliasTypes.add(mostSpecificCommonSuperclass(a, b));
        }
        // Return the remaining class
        return aliasTypes.iterator().next();
    }

    /*
     * This methods looks up into the hierarchy to get the reference to the
     * methods of the controller class
     */
    private String getCorrespondingMethodCall(MethodInvocation methodInvocation) {
        String eventName = JimpleUtils.getMethodName(methodInvocation.getMethodSignature()).replaceAll("on", "");

        SootClass controller = Scene.v().getSootClass(ActivityController.class.getName());

        for (SootClass sootClass : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(controller)) {
            for (SootMethod method : sootClass.getMethods()) {
                if (eventName.equalsIgnoreCase(method.getName())) {
                    return method.getName();
                }
            }

        }
        return null;
    }

    /**
     * Either return the variable corresponding to the parameter or the value of
     * that's a primitive type
     * 
     * @param parameter
     * @return
     */
    private String getParameterFor(DataNode dataNode) {
        if (dataNode instanceof PrimitiveValue) {
            /*
             * We use toString() instead of getStringValue() to wrap strings
             * which have two different representations.
             */
            return ((PrimitiveValue) dataNode).toString();
        } else if (dataNode instanceof MethodCallLiteralValue) {
            MethodCallLiteralValue methodCall = (MethodCallLiteralValue) dataNode;
            // This is to get the reference to the mocked object
            String mockedObject = getVariableFor(methodCall.getOwner()).getSecond();
            String methodToInvoke = JimpleUtils.getMethodName(methodCall.getMethodSignature());
            // THIS IS TO DO
            String parameterList = "";
            return mockedObject + "." + methodToInvoke + "(" + parameterList + ")";
        } else if (dataNode instanceof NullInstance) {
            return "null";
        } else {
            return getVariableFor(dataNode).getSecond();
        }
    }

    /**
     * There are some weird cases here. For example Arrays.asList() returns an
     * "java.util.Arrays$ArrayList" which is not a "real" type. So we cannot
     * directly instantiate that. We need somehow to resolve this to some valid
     * class (i.e. LIST)... We can look at aliases ?
     * 
     * 
     * @param dataNode
     * @param aliasToClass
     * @return
     */
    private String getVariableTypeFor(DataNode dataNode// ,
    // Rethinkg this aliasing stuff and type inference
    // Map<Set<ObjectInstance>, Class> aliasToClass
    ) {
        if (dataNode instanceof PrimitiveValue) {
            return ((PrimitiveValue) dataNode).getType();
        } else if (dataNode instanceof ObjectInstance) {
            String actualType = ((ObjectInstance) dataNode).getType();

            // THIS IS A PATCH
            // for( Set<ObjectInstance> aliases : aliasToClass.keySet() ){
            // if( aliases.contains(((ObjectInstance) dataNode).getType())){
            // actualType = aliasToClass.get( aliases ).getName();
            // break;
            // }
            // }
            // if( actualType == null ){
            // throw new RuntimeException("Cannot find actual type of " +
            // dataNode );
            // }
            // Not sure we can push this more than that...
            String formalType = actualType;
            if (actualType.equals("java.util.Arrays$ArrayList")) {
                formalType = "java.util.List";
            }
            //
            // If that's an inner class we might not be able to instantiate
            // it...
            if (formalType.contains("$")) {
                formalType = formalType.replaceAll("\\$", ".");
            }
            //
            // private static class ArrayList<E> extends AbstractList<E>
            /*
             * Inner classes are identified by $ replace this to but not always
             * those are visible (here, tho, we do not have such information)
             */
            return formalType;
        } else {
            throw new RuntimeException("Cannot find type for " + dataNode);
        }
    }

    // DataNode -> VarType+VarName
    private Map<DataNode, Pair<String, String>> declaredVariables = new HashMap<>();
    // Var Name -> ID
    private Map<String, AtomicInteger> declaredVariablesIndex = new HashMap<>();
    private Set<DataNode> initializedVariables = new HashSet<>();

    private boolean firstUseOf(DataNode dataNode) {
        return declaredVariables.containsKey(dataNode) && !initializedVariables.contains(dataNode);
    }

    private Pair<String, String> declareVariableFor(DataNode dataNode, DataDependencyGraph dataDependencyGraph) {
        String variableType = getVariableTypeFor(dataNode);
        String className = variableType.substring(variableType.lastIndexOf('.') + 1, variableType.length());

        if (declaredVariables.containsKey(dataNode)) {
            return null;
        }

        if (!declaredVariablesIndex.containsKey(className)) {
            declaredVariablesIndex.put(className, new AtomicInteger(0));
        }

        // Use classname to avoid clashing with variables of different classes
        // with the same name...
        String variableName = className.toLowerCase() + "" + declaredVariablesIndex.get(className).getAndIncrement();
        // Ensure names are valid (i.e., remove [] from the name
        variableName = variableName.replaceAll("\\[", "").replaceAll("\\]", "");

        Pair<String, String> variable = new Pair<String, String>(variableType, variableName);

        declaredVariables.put(dataNode, variable);

        // Link all the alias to the same variable if any
        if (dataNode instanceof ObjectInstance) {
            for (ObjectInstance alias : dataDependencyGraph.getAliasesOf((ObjectInstance) dataNode)) {
                declaredVariables.put(alias, variable);
            }
        }

        return variable;
    }

    /*
     * If a variable is used but not declared, that's an error
     */
    private Pair<String, String> getVariableFor(DataNode dataNode) {

        if (declaredVariables.containsKey(dataNode)) {
            return declaredVariables.get(dataNode);
        }
        throw new RuntimeException("Tring to use a variable for " + dataNode + " which is not yet declared !");
    }
}
