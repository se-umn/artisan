package de.unipassau.abc.systemtests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Ignore;
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

public class OrgAmetroTest {

    private static String appName = "org.ametro";

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    private String apk = "/Users/gambi/MyDroidFax/apks/filtered-apps/" + appName + "/apk/org.ametro_40.apk";
    private String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";

    private File traceFile = new File("/Users/gambi/MyDroidFax/apks/filtered-apps/" + appName + "/traces/multi");
    //
    private File outputParsedTracesTo = new File("src/test/resources/apps/" + appName + "/parsed/multi");
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

    private void carveSingleMethod(String methodInvocation)
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");

        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", parsedTrace.getAbsolutePath(), //
                "--output-carved-tests-to", outputCarvedTestsTo.getAbsolutePath(), //
                "--method-invocation-to-carve", methodInvocation, //
        };

        AndroidCarver.main(args);
    }

    @Test
    public void testSimpleLifeCycleMethod()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        /*
         * Basic Method of an inner most activity (it is started by another
         activity but does not start another activity
         */
        String methodInvocation = "org.ametro.ui.activities.CityList: void onCreate(android.os.Bundle)>_987";
        carveSingleMethod(methodInvocation);

    }
    
    @Test
    public void testCarvingSingleMethodInvocation()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
//        String methodInvocation = "<org.ametro.ui.activities.CityList: boolean onCreateOptionsMenu(android.view.Menu)>_1426";
        String methodInvocation = "<org.ametro.ui.activities.CityList: void onCitySelected(org.ametro.catalog.entities.MapInfo\\[\\])>_6739";
        // String methodInvocation = "<org.ametro.ui.activities.CityList: void onMapDownloadingComplete(org.ametro.catalog.entities.MapInfo[])>_6933";
        carveSingleMethod(methodInvocation);

    }


    @Test
//    @Ignore
    public void testCarvingSingleActivity_Map_MainActivity()
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
    @Ignore
    public void testCarvingSingleActivity_MapList_MiddleActivity()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");

        // TODO This uses regex
        String activityClass = "\\borg.ametro.ui.activities.MapList\\b";

        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", parsedTrace.getAbsolutePath(), //
                "--output-carved-tests-to", outputCarvedTestsTo.getAbsolutePath(), //
                "--class-to-carve", activityClass, //
        };

        AndroidCarver.main(args);

    }

    // This one I cannot carve. It somehow belongs to Robolectrics as well ->
    // visualize()
    // <org.ametro.ui.activities.CityList: boolean
    // onCreateOptionsMenu(android.view.Menu)>_1031

    @Test
    public void testCarvingSingleActivity_CityList_InnerMostActivity()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
//        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");

        // TODO This uses regex
        String activityClass = "\\borg.ametro.ui.activities.CityList\\b";

        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", outputParsedTracesTo.getAbsolutePath() , //parsedTrace.getAbsolutePath(), //
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
    public void testGenerateSingleTest() {
        outputGeneratedTestsTo.mkdirs();
        File carvedTestFile = new File(outputCarvedTestsTo, "org.ametro.ui.activities.MapList.onCreate_842");
        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--store-tests", outputGeneratedTestsTo.getAbsolutePath(), //
                "--carve-test-files", carvedTestFile.getAbsolutePath() };

        AndroidActivityTestGenerator.main(args);

    }
}
