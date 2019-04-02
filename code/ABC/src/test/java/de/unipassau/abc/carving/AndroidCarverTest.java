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
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    @Test
    public void testMain() throws IOException, InterruptedException, URISyntaxException, CarvingException {
        String parsedTrace = new File("src/test/resources/android-28-traces/parsed.xml").getAbsolutePath();

        File outputTo = new File("src/test/resources/android-28-carved-tests");
        // tempFolder.newFolder("carved-tests");

        /*
         * Util classes
         */
//        String classToCarve = "com.farmerbb.notepad.util.NoteListItem";
//        String classToCarve = "com.farmerbb.notepad.util.ScrollPositions";

        /*
         * Activity class.
         */
         String classToCarve = "com.farmerbb.notepad.activity.MainActivity";
//         String classToCarve = "com.farmerbb.notepad.activity.NoteEditActivity";
//         String classToCarve = "com.farmerbb.notepad.activity.SettingsActivity";
         
        // //

        /*
         * Fragment class
         */
        // String classToCarve = "com.farmerbb.notepad.activity.MainActivity";
        // //

//       String methodInvocation =
//         String methodInvocation = "<com.farmerbb.notepad.activity.MainActivity: void <init>()>_922";
        String[] args = new String[] { "--parsed-traces", parsedTrace, //
                "--output-carved-tests-to", outputTo.getAbsolutePath(), //
//                "--method-invocation-to-carve", methodInvocation };
         "--class-to-carve", classToCarve};

        AndroidCarver.main(args);

        // Check that the directory is not empty..
        File[] files = outputTo.listFiles();
        Assert.assertTrue(files.length > 0);
    }
}
