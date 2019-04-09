package de.unipassau.abc.generation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import edu.emory.mathcs.backport.java.util.Arrays;

public class AndroidActivityTestGenerationTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    private List<String> getTheFiles(File rootDirectory) {
        List<String> files = new ArrayList<>();
        File[] subdirs = rootDirectory.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(".*_\\d\\d*");
            }
        });
        for (File subdir : subdirs) {
            if (subdir.isDirectory()) {
                files.addAll(getTheFiles(subdir));
            } else {
                files.add(subdir.getAbsolutePath());
            }
        }
        return files;
    }

    @Test
    public void testMain() throws IOException, InterruptedException, URISyntaxException, CarvingException {

        // String carvedTestFile =
        // "src/test/resources/android-28-carved-tests/com.farmerbb.notepad.activity.MainActivity.onCreate_4";
        File carvedTestDirectory = new File("src/test/resources/android-28-carved-tests/");
        List<String> carvedTests = new ArrayList();
        carvedTests.addAll(getTheFiles(carvedTestDirectory));
        
//        String fileName ="com.farmerbb.notepad.activity.MainActivity.isShareIntent_516";
//        carvedTests.add( new File(carvedTestDirectory, fileName).getAbsolutePath() );
        
        
        System.out.println("AndroidActivityTestGenerationTest.testMain() " + carvedTests );
        
        String outputfolder = "src/test/resources/android-28-generated-tests";
        String apk = "/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk";
        String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";

        List<String> args = new ArrayList(
                Arrays.asList( 
                        new String[]{"--store-tests", outputfolder, "--android-jar", androidJar, "--apk", apk, "--carve-test-files",})) ;
        // Include the list of files as argument
        args.addAll( carvedTests );
        
        AndroidActivityTestGenerator.main(args.toArray(new String[]{}));

    }
}
