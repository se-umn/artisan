package de.unipassau.abc.systemtests;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.AndroidCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.parsing.DuafDroidParser;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class OrgAmetroTest {

    private static String appName = "org.ametro";
    
    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

    private String apk = "/Users/gambi/MyDroidFax/apks/filtered-apps/"+ appName +"/apk/org.ametro_40.apk";
    private String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";
//    private File traceFile = new File(
//            "/Users/gambi/MyDroidFax/apks/filtered-apps/"+ appName +"/traces/abc-traces.txt");
    private File traceFile = new File("src/test/resources/"+ appName +"/traces/trace.log");
    //
    private File outputParsedTracesTo = new File("src/test/resources/"+ appName +"/parsed");
    private File outputCarvedTestsTo = new File("src/test/resources/"+ appName +"/carved");
    private File outputGeneratedTestsTo = new File("src/test/resources/"+ appName +"/generated");

    @Test
    public void testParsing() throws IOException, CarvingException {
        outputParsedTracesTo.mkdirs();

        String[] args = new String[] { "--trace-files", traceFile.getAbsolutePath(), "--store-artifacts-to",
                outputParsedTracesTo.getAbsolutePath(), "--apk", apk, "--android-jar", androidJar };

        DuafDroidParser.main(args);
    }

    @Test
    public void testCarvingSingleMethodInvocation() throws IOException, InterruptedException, URISyntaxException, CarvingException {
        outputCarvedTestsTo.mkdirs();
        File parsedTrace = new File(outputParsedTracesTo, traceFile.getName() + ".parsed.xml");
        
        String methodInvocation = "<org.ametro.ui.activities.Map: void onPause()>_10420";
        
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
}
