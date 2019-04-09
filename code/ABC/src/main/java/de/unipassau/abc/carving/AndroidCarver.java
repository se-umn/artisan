package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;
import org.robolectric.android.controller.ActivityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.carvers.AndroidMethodCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.JimpleUtils;
import soot.G;
import soot.Scene;
import soot.options.Options;

/**
 * Sucks up a parsed trace xml file and tries to carve out all the methods for a
 * given class.
 * 
 * TODO For the moment we output JIMPLE text files
 * 
 * @author gambi
 *
 */
public class AndroidCarver {

    private static final Logger logger = LoggerFactory.getLogger(AndroidCarver.class);
    // public static final String MOCKITO_JAR =
    // "/Users/gambi/.m2/repository/org/mockito/mockito-core/1.10.19/mockito-core-1.10.19.jar";

    public interface CarverCLI {

        @Option(longName = "parsed-traces")
        List<File> getParsedTraceFiles();

        @Option(longName = "apk")
        File getAPK();

        @Option(longName = "android-jar")
        File getAndroidJar();

        @Option(longName = "output-carved-tests-to")
        File getOutputDir();

        @Option(longName = "class-to-carve", description = " fully qualified name of the class to carve", defaultToNull = true)
        String getClassToCarve();

        @Option(longName = "method-invocation-to-carve", description = "", defaultToNull = true)
        String getMethodInvocationToCarve();
    }

    public static MethodInvocationMatcher getMatcherFor(final String type, final String regEx) {
        switch (type) {
        case "package":
            return MethodInvocationMatcher.byPackage(regEx);
        case "class":
            // return MethodInvocationMatcher.byClass(regEx);
            return MethodInvocationMatcher.byClassLiteral(regEx);
        case "method":
            // PAY ATTENTION TO THIS
            return MethodInvocationMatcher.byMethodLiteral(regEx);
        case "regex":
            return MethodInvocationMatcher.byMethod(regEx);
        case "invocation":
            return MethodInvocationMatcher.byMethodInvocation(regEx);
        case "return":
            return MethodInvocationMatcher.byReturnType(regEx);
        case "instance":
            // TODO how to rule out primitive, null, and possibly Strings ?
            return MethodInvocationMatcher.byInstance(new ObjectInstance(regEx));
        default:
            throw new ArgumentValidationException("Unknown matcher specification " + type + "=" + regEx);
        }
    }

    public static void main(String[] args)
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        long startTime = System.nanoTime();

        CarverCLI cli = CliFactory.parseArguments(CarverCLI.class, args);
        List<File> parsedTracesFiles = cli.getParsedTraceFiles();
        File storeCarvedTestsTo = cli.getOutputDir();

        // Validate the input
        MethodInvocationMatcher carveBy = null;

        String classToCarve = cli.getClassToCarve();
        if (classToCarve == null || (classToCarve != null && classToCarve.isEmpty())) {
            String methodInvocationToCarve = cli.getMethodInvocationToCarve();

            if (methodInvocationToCarve != null && !methodInvocationToCarve.isEmpty()) {
                carveBy = MethodInvocationMatcher.byMethodInvocation(methodInvocationToCarve);
            }
        } else {
            carveBy = MethodInvocationMatcher.byClass(classToCarve);
        }

        setupSoot(cli.getAndroidJar(), cli.getAPK());

        // Not sure what this is used...
        List<MethodInvocationMatcher> excludeBy = new ArrayList<>();
        // We cannot carve static initializers
        excludeBy.add(MethodInvocationMatcher.clinit());

        // Load the parsed traces
        Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces = new HashMap<>();
        XStream xStream = new XStream();
        for (File parsedTrace : parsedTracesFiles) {
            try {
                parsedTraces.put(parsedTrace.getAbsolutePath(),
                        (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>) xStream.fromXML(parsedTrace));
            } catch (Exception e) {
                logger.error("Cannot parse " + parsedTrace);
            }
        }

        logger.info("Start carving of " + parsedTraces.size() + " traces ");

        if (carveBy == null) {
            carveBy = generateMethodInvocationMatcherForAllTheActivities(parsedTraces);
        }

        if (carveBy == null) {
            // By default we look for the Activity classes in the APK and carve
            // them all...

            throw new CarvingException("Wrong carve by criterion");
        }
        
        
        /*
         * At this level of abstraction carved tests are simply a number of
         * method invocations
         */
        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

        long carvingTime = System.currentTimeMillis();

        int methodInvocationCount = 0;
        for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace : parsedTraces.values()) {
            // TODO Possibly at this level we can also select the most
            // appropriate method carver ?

            List<MethodInvocation> methodsInvocationsToCarve = new ArrayList<>(parsedTrace.getFirst()
                    .getMethodInvocationsFor(carveBy, excludeBy.toArray(new MethodInvocationMatcher[] {})));

            // Remove the synthetic methods
            MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.isSynthetic(), methodsInvocationsToCarve);

            methodInvocationCount += methodsInvocationsToCarve.size();

            /*
             * There might be different stragegies for Carving. We use the
             * Level_0 carving, which does not inspect the implementations of
             * the CUT. This aims to generate test cases which contains only
             * "callable" (i.e., public) methods of the CUTs
             */
            AndroidMethodCarver testCarver = new AndroidMethodCarver(parsedTrace.getFirst(), //
                    parsedTrace.getSecond(), //
                    parsedTrace.getThird());

            carvedTests.addAll(testCarver.carve(methodsInvocationsToCarve));
        }

        // TODO Post processing of carved tests...
        /*
         * Simplify ? Shorten ?
         */

        /*
         * Remove duplicates. A duplicate is code clone were the method under
         * test is the same and the parameters
         */

        carvingTime = System.currentTimeMillis() - carvingTime;

        logger.info("End carving");

        logger.info(">> Carved tests : " + carvedTests.size() + "/" + methodInvocationCount);

        logger.debug("Storing carved tests to : " + storeCarvedTestsTo.getAbsolutePath());

        // TODO Store carved tests to storeCarvedTestsTo
        for (Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest : carvedTests) {
            MethodInvocation carvedMethodInvocation = carvedTest.getFirst().getLastMethodInvocation();
            String methodSignature = carvedMethodInvocation.getMethodSignature();

            String carvedClass = JimpleUtils.getClassNameForMethod(methodSignature);
            String carvedMethod = JimpleUtils.getMethodName(methodSignature);

            // Store to outputfile
            File outputFile = new File(storeCarvedTestsTo,
                    carvedClass + "." + carvedMethod + "_" + carvedMethodInvocation.getInvocationCount());
            logger.trace("Store carved test to " + outputFile.getAbsolutePath());
            xStream.toXML(carvedTest, new FileWriter(outputFile));
        }
        return;
    }

    private static MethodInvocationMatcher generateMethodInvocationMatcherForAllTheActivities(
            Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces) {
        Set<String> activities = new HashSet<>();
        for( Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace : parsedTraces.values()){
            DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
            for(ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances() ){
                if( objectInstance.isAndroidActivity() ){
                    activities.add( objectInstance.getType() );
                }
            }
        }
        logger.info("Found the following activities in the application: \n" + activities );
        List<MethodInvocationMatcher> matchers = new ArrayList<>();
        for(String activityType : activities ){
            matchers.add( MethodInvocationMatcher.byClass( activityType ) );
        }
        
        if( matchers.isEmpty() ){
            return null;
        } else if( matchers.size() == 1 ){
            return matchers.get( 0 );
        } else {
            return MethodInvocationMatcher.or( //
                    matchers.get(0),
                    matchers.get(1), 
                    matchers.subList(2, matchers.size()).toArray( new MethodInvocationMatcher[]{}));
        }
    }

    private static void setupSoot(File androidJar, File apk) {
        G.reset();
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);

        // Not sure this will work with the APK
        ArrayList<String> necessaryJar = new ArrayList<String>();
        // Include here entries from the classpath
        for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
            if (cpEntry.contains("mockito-core-1.10.19.jar")) {
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
}
