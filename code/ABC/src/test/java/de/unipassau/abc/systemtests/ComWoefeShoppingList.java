package de.unipassau.abc.systemtests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.ActivityGraph;
import de.unipassau.abc.carving.AndroidCarver;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.generation.AndroidActivityTestGenerator;
import de.unipassau.abc.parsing.DuafDroidParser;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class ComWoefeShoppingList {

    private static String appName = "com.woefe.shoppinglist";

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    private String apk = "/Users/gambi/MyDroidFax/apks/working/" + "com.woefe.shoppinglist-debug.apk";
    private String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-25/android.jar";

    private File traceFile = new File(
            "src/test/resources/apps/com.woefe.shoppinglist/traces/trace.log");
    //
    private File outputParsedTracesTo = new File("src/test/resources/apps/" + appName + "/parsed");
    private File outputCarvedTestsTo = new File("src/test/resource/appss/" + appName + "/carved");
    private File outputGeneratedTestsTo = new File("src/test/resources/apps/" + appName + "/generated");

    @Test
    public void testParsing() throws IOException, CarvingException {
        outputParsedTracesTo.mkdirs();

        String[] args = new String[] { "--trace-files", traceFile.getAbsolutePath(), "--store-artifacts-to",
                outputParsedTracesTo.getAbsolutePath(), "--apk", apk, "--android-jar", androidJar };

        DuafDroidParser.main(args);
    }

    @Test
    public void testVisualizeActivityGraph() throws CarvingException, IllegalArgumentException, IllegalAccessException {
        
        AndroidCarver.setupSoot(new File(androidJar), new File(apk));
        
        File parsedTraceFile = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");
        
        // Load Parsed Traces
        XStream xStream = new XStream();
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>) xStream
                .fromXML(parsedTraceFile);

        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        // BUild the entire Graph
        ActivityGraph activityGraph = new ActivityGraph(callGraph, executionFlowGraph, dataDependencyGraph);

        List<MethodInvocation> allActivityMethodInvocations = executionFlowGraph.getOrderedMethodInvocations();
        // Keep only the methods owned by android activities
        MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.ownedByAnAndroidActivity(),
                allActivityMethodInvocations);
        /*
         * Remove the methods subsumed by a different activity. This removes
         * super/subclasses activities
         */
        MethodInvocationMatcher.filterByInPlace(
                MethodInvocationMatcher.isSubsumedByAnotherAndroidActivityMethod(callGraph),
                allActivityMethodInvocations);
        /*
         * Collect in order the appearing of all the activities, and call the
         * activityGraph
         */
        for (MethodInvocation methodInvocation : allActivityMethodInvocations) {
            ObjectInstance objectInstance = methodInvocation.getOwner();
            if (objectInstance.isAndroidActivity()) {
                activityGraph.add(objectInstance, methodInvocation);
            }
        }
        
        activityGraph.visualize();
        
        //
        System.out.println("OrgAmetroTest.testVisualizeActivityGraph() end...");

    }

    @Test
    public void testCarvingSingleMethodInvocation()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");

        String methodInvocation = "<com.woefe.shoppinglist.activity.MainActivity: boolean onCreateOptionsMenu(android.view.Menu)>_257";

        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", parsedTrace.getAbsolutePath(), //
                "--output-carved-tests-to", outputCarvedTestsTo.getAbsolutePath(), //
                "--method-invocation-to-carve", methodInvocation, //
        };

        AndroidCarver.main(args);

    }

    @Test
    public void testCarvingSingleActivity()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");

        // TODO This uses regex
        String activityClass = "\\borg.ametro.ui.activities.Map\\b";

        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", parsedTrace.getAbsolutePath(), //
                "--output-carved-tests-to", outputCarvedTestsTo.getAbsolutePath(), //
                "--class-to-carve", activityClass, //
        };

        AndroidCarver.main(args);

    }

    @Test
    public void testCarving() throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");
        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", parsedTrace.getAbsolutePath(), //
                "--output-carved-tests-to", outputCarvedTestsTo.getAbsolutePath(), //
        };

        AndroidCarver.main(args);

    }
    
    @Test
    public void testGenerateSingleTest(){
        outputGeneratedTestsTo.mkdirs();
        File carvedTestFile = new File(outputCarvedTestsTo, "org.ametro.ui.activities.MapList.onCreate_842");
        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--store-tests", outputGeneratedTestsTo.getAbsolutePath(), //
                "--carve-test-files", carvedTestFile.getAbsolutePath()
        };
        
        AndroidActivityTestGenerator.main( args);
        
    }
}
