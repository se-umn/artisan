package de.unipassau.abc.systemtests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.AndroidCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.generation.AndroidActivityTestGenerator;
import de.unipassau.abc.parsing.DuafDroidParser;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class Notepad {

    private static String appName = "Notepad-Alessio";

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    private String apk = "/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk";
    private String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";
    private File traceFile = new File("src/test/resources/apps/" + appName + "/traces/trace.log");
    //
    private File outputParsedTracesTo = new File("src/test/resources/apps/" + appName + "/parsed");
    private File outputCarvedTestsTo = new File("src/test/resources/apps/" + appName + "/carved");
    private File outputGeneratedTestsTo = new File("src/test/resources/apps/" + appName + "/generated");

    @Test
    public void testParsing() throws IOException, CarvingException {
        outputParsedTracesTo.mkdirs();

        String[] args = new String[] { "--trace-files", traceFile.getAbsolutePath(), "--store-artifacts-to",
                outputParsedTracesTo.getAbsolutePath(), "--apk", apk, "--android-jar", androidJar };

        DuafDroidParser.main(args);
    }

    @Test
    public void testCarvingSingleMethodInvocation()
            throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");

        // This imports "weird" things since those are calls made to the
        // Activity INSIDE its fragments.
        // And I am not sure if robolectrics invoke them as well within the
        // lifecycle of the app
        String methodInvocation = "<com.farmerbb.notepad.activity.MainActivity: java.util.ArrayList getCabArray()>_315";

        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--parsed-traces", parsedTrace.getAbsolutePath(), //
                "--output-carved-tests-to", outputCarvedTestsTo.getAbsolutePath(), //
                "--method-invocation-to-carve", methodInvocation, //
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
        File carvedTestFile = new File(outputCarvedTestsTo,
                "com.farmerbb.notepad.activity.MainActivity.getCabArray_315");
        String[] args = new String[] { "--apk", apk, "--android-jar", androidJar, //
                "--store-tests", outputGeneratedTestsTo.getAbsolutePath(), //
                "--carve-test-files", carvedTestFile.getAbsolutePath() };

        AndroidActivityTestGenerator.main(args);
    }
}
