package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class DuafDroidParserTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    @Test
    public void mainTest() throws IOException, CarvingException {

        File outputTo = new File("src/test/resources/android-28-traces/Notepad-Alessio"); // tempFolder.newFile("parsed.xml");
        outputTo.mkdirs();
        File traceFile = new File("src/test/resources/android-28-traces/Notepad-Alessio/trace.log");
        String apk = "/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk";
        String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";
        
//        File outputTo = new File("src/test/resources/android-28-traces/");
//        File traceFile = new File("src/test/resources/android-28-traces/org.asteroidos.sync.log");
//        String apk = "/Users/gambi/MyDroidFax/apks/org.asteroidos.sync_11.apk";
//        String androidJar = "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar";
        
        String[] args = new String[] { "--trace-files", traceFile.getAbsolutePath(), "--store-artifacts-to", outputTo.getAbsolutePath(),
                "--apk", apk, "--android-jar", androidJar };

        DuafDroidParser.main(args);

        // Check that this is actually happening
        XStream xStream = new XStream();

        // Read all the files from the folder
        Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraceFiles = new HashMap<>();
        for( File file  : outputTo.listFiles(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith( traceFile.getName()) && name.endsWith(".parsed.xml");
            }
        })){
            System.out.println("DuafDroidParserTest.mainTest() Reading from " + file);
            parsedTraceFiles.put(file.getAbsolutePath(),
                    (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>) xStream.fromXML(file));
        }
        Assert.assertEquals(1, parsedTraceFiles.size());
    }

}
