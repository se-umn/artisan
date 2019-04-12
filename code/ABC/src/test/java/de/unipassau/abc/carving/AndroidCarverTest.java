package de.unipassau.abc.carving;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class AndroidCarverTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.INFO);

    @Test
    public void testMain() throws IOException, InterruptedException, URISyntaxException, CarvingException {

        String apk = "/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk";
        String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";
        String parsedTrace = new File("src/test/resources/android-28-traces/Notepad-Alessio/trace.log.parsed.xml").getAbsolutePath();
        File outputTo = new File("src/test/resources/android-28-carved-tests/Notepad-Alessio");
        outputTo.mkdirs();

//        String apk = "/Users/gambi/MyDroidFax/apks/org.asteroidos.sync_11.apk";
//        String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";
//        String parsedTrace = new File("src/test/resources/android-28-traces/org.asteroidos.sync.log.parsed.xml").getAbsolutePath();
//        File outputTo = new File("src/test/resources/android-28-carved-tests/org.asteroidos.sync");
//        outputTo.mkdirs();

        /*
         * Util classes
         */
        // String classToCarve = "com.farmerbb.notepad.util.NoteListItem";
        // String classToCarve = "com.farmerbb.notepad.util.ScrollPositions";

        String classToCarve = "com.farmerbb.notepad.activity.MainActivity";
        
        String methodInvocation = "<com.farmerbb.notepad.activity.MainActivity: void deleteNotes()>_1373";


        // If no carving directive is provided we carve all the Activity classes of the project
        String[] args = new String[] {
                "--apk", apk, "--android-jar", androidJar , //
                "--parsed-traces", parsedTrace, //
                "--output-carved-tests-to", outputTo.getAbsolutePath(), //
//                 "--method-invocation-to-carve", methodInvocation
//                "--class-to-carve", classToCarve 
                };

        AndroidCarver.main(args);

        // Check that the directory is not empty..
        File[] files = outputTo.listFiles();
        Assert.assertTrue(files.length > 0);
    }
}
