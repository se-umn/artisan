package de.unipassau.abc.carving;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.carving.carvers.AndroidMethodCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.JimpleUtils;
import edu.emory.mathcs.backport.java.util.Arrays;
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
    public static final String MOCKITO_JAR = "/Users/gambi/.m2/repository/org/mockito/mockito-core/1.10.19/mockito-core-1.10.19.jar";

    public interface CarverCLI {

        @Option(longName = "parsed-traces")
        File getParsedTracesFile();

        // @Option(longName = "project-jar")
        // List<File> getProjectJar();

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
        File parsedTracesFile = cli.getParsedTracesFile();
        File storeCarvedTestsTo = cli.getOutputDir();

        // Validate the input
        MethodInvocationMatcher carveBy = null;
        
        String classToCarve = cli.getClassToCarve();
        if (classToCarve == null || ( classToCarve != null && classToCarve.isEmpty())){
            String methodInvocationToCarve = cli.getMethodInvocationToCarve();
            
            if (methodInvocationToCarve != null && ! methodInvocationToCarve.isEmpty()){
                carveBy = MethodInvocationMatcher.byMethodInvocation(methodInvocationToCarve);
            }
        } else {
            carveBy = MethodInvocationMatcher.byClass(classToCarve);
        }

        if( carveBy == null ){
            throw new CarvingException("Wrong carve by criterion");
        }
        
        
        // Not sure what this is used...
        List<MethodInvocationMatcher> excludeBy = new ArrayList<>();
        // We cannot carve static initializers
        excludeBy.add(MethodInvocationMatcher.clinit());

        // Load the parsed traces
        XStream xStream = new XStream();
        Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraces = (Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>) xStream
                .fromXML(parsedTracesFile);

        List<File> projectJars = new ArrayList<>();
        projectJars.add( new File(MOCKITO_JAR));
        setupSoot(projectJars);
        
        logger.info("Start carving of " + parsedTraces.size() + " traces " );

        /*
         * At this level of abstraction carved tests are simply a number of
         * method invocations
         */
        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvedTests = new ArrayList<>();

        long carvingTime = System.currentTimeMillis();

        int methodInvocationCount = 0;
        for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace : parsedTraces.values()) {
            // TODO Possibly at this level we can also select the most appropriate method carver ?

            List<MethodInvocation> methodsInvocationsToCarve = new ArrayList<>(parsedTrace.getFirst().getMethodInvocationsFor(carveBy, excludeBy.toArray(new MethodInvocationMatcher[] {})));
            
            
            // Remove the synthetic methods
            MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.isSynthetic(), methodsInvocationsToCarve);
            
            methodInvocationCount += methodsInvocationsToCarve.size();
            
            /*
             * There might be different stragegies for Carving. We use the
             * Level_0 carving, which does not inspect the implementations of
             * the CUT. This aims to generate test cases which contains only
             * "callable" (i.e., public) methods of the CUTs
             */
            AndroidMethodCarver testCarver = new AndroidMethodCarver(
                    parsedTrace.getFirst(), // 
                    parsedTrace.getSecond(), //
                    parsedTrace.getThird());
            
            carvedTests.addAll(testCarver.carve(methodsInvocationsToCarve));
        }

        // TODO Post processing of carved tests...
        /*
         * Simplify ? Shorten ?
         */
        
        /*
         * Remove duplicates. A duplicate is code clone were the method under test is the same and the parameters
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

    /*
     * This is global anyway. Public for testing. Note that this loads all the
     * classes in my classpath !
     */
    public static void setupSoot(List<File> projectJars) {
        G.reset();
        //

        // System Settings Begin
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_whole_program(true);

        // String junit4Jar = null;
        // String hamcrestCoreJar = null;
        //
        // for (String cpEntry :
        // SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
        // if (cpEntry.contains("junit-4")) {
        // junit4Jar = cpEntry;
        // } else if (cpEntry.contains("hamcrest-core")) {
        // hamcrestCoreJar = cpEntry;
        // }
        // }
        //
        // String traceJar = ABCUtils.getTraceJar();
        // String systemRulesJar = ABCUtils.getSystemRulesJar();
        // List<File> xStreamJars = ABCUtils.getXStreamJars();

        System.out.println("Carver.setupSoot() Project Jars: ");
        ArrayList<String> necessaryJar = new ArrayList<String>();
        // Include here JUnit and Hamcrest
        for (File projectLib : projectJars) {
            System.out.println("- " + projectLib);
            necessaryJar.add(projectLib.getAbsolutePath());
        }
        // necessaryJar.add(junit4Jar);
        // necessaryJar.add(hamcrestCoreJar);
        // necessaryJar.add(traceJar);
        //
        // necessaryJar.add(systemRulesJar);
        //
        // for (File projectLib : xStreamJars) {
        // necessaryJar.add(projectLib.getAbsolutePath());
        // }

        /*
         * To process a JAR file, just use the same option but provide a path to
         * a JAR instead of a directory.
         */
        Options.v().set_process_dir(necessaryJar);

        // Soot has problems in working on mac.
        String osName = System.getProperty("os.name");
        System.setProperty("os.name", "Whatever");
        Scene.v().loadNecessaryClasses();
        System.setProperty("os.name", osName);
        //
        // p = Paths.get(file.getAbsolutePath());
        // System.out.println("P = " + p);

    }
}
