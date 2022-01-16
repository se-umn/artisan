package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.StaticParsedTraceDecorator;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class ParserSmokeTest {

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    // TODO Brittle, but cannot do otherwise for the moment. Maybe something from
    // the ENV?
    // Or some known location inside the project (with symlink as well)
    private final static File ANDROID_JAR = new File(
            "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

    private String traceFolder = "";
    private String file = "";

    private String getTraceFileFrom(String testDirectoryName) {
        // /Trace-1642241004125.txt
        File testDirectory = new File(traceFolder, testDirectoryName);
        return testDirectoryName + "/" + testDirectory.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("Trace") && pathname.getName().endsWith(".txt");
            }
        })[0].getName();

    }

    private ParsedTrace parseWithDecorators() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(traceFolder, file);

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File(traceFolder, "app-original.apk");
//        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        //
        decorator = new AndroidParsedTraceDecorator();

        parsedTrace = decorator.decorate(parsedTrace);

        return parsedTrace;
    }

    /*
     * traces/com.prismaqf.callblocker.actions.ActionsTest#testAvailableActions/Trace-1642349967582.txt
     * // java.lang.NullPointerException // at
     * de.unipassau.abc.carving.utils.MethodInvocationSelector.filterCarvableMethodInvocations(MethodInvocationSelector.java:96)
     * // at
     * de.unipassau.abc.carving.utils.MethodInvocationSelector.findAllCarvableMethodInvocations(MethodInvocationSelector.java:63)
     * // at de.unipassau.abc.evaluation.Main.main(Main.java:180)
     * 
     * This was caused because the trace file was empty !
     */
    @Test
    public void parseSelectorTriggersNPE() throws FileNotFoundException, IOException, ABCException {
        traceFolder = "/Users/gambi/action-based-test-carving/apps-src/PrismaCallBlocker/traces/";
        file = getTraceFileFrom("com.prismaqf.callblocker.actions.ActionsTest#testAvailableActions");
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        
        ParsedTrace parsedTrace = parseWithDecorators();
        mis.findAllCarvableMethodInvocations(parsedTrace);
    }

    @Test
    public void parseTrace() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-small.txt");
        File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

        ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace parsedTrace = parser.parseTrace(traceFile);

    }

    @Test
    public void parseTraceWithGremlin() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File("./src/test/resources/abc.basiccalculator/testCalculate-trace-small.txt");
        File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

        ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

        TraceParser parser = new GremlinParser();
        ParsedTrace parsedTrace = parser.parseTrace(traceFile);

    }

//	@Test
//	public void parseTraceWithManyThreads() throws FileNotFoundException, IOException, ABCException {
//		File traceFile = new File("./traces/ca.rmen.android.poetassistant/Crash.test-trace");
//		File apk_file = new File("./src/test/resources/apks/Poet.apk");
//		
//		TraceParserImpl.setupSoot(ANDROID_JAR, apk_file);
//		
//		
//		TraceParserImpl parser = new TraceParserImpl();
//		ParsedTrace parsedTrace = parser.parseFromTraceFile(traceFile);
//	}
}
