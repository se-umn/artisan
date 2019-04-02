package de.unipassau.abc.parsing;

import java.io.File;
import java.io.IOException;
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
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class DuafDroidParserTest {
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);
    
    @Test
    public void mainTest() throws IOException{
        
        File outputTo = new File("src/test/resources/android-28-traces/parsed.xml"); //tempFolder.newFile("parsed.xml");
        String trace = new File("src/test/resources/android-28-traces/trace.log").getAbsolutePath();
        
        String[] args = new String[]{"--trace-files", trace, "--store-artifacts-to", outputTo.getAbsolutePath()};
        
        DuafDroidParser.main(args);
        
        // Check that this is actually happening
        XStream xStream = new XStream();
        
        Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTraceFiles = (Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>) xStream.fromXML( outputTo );
        
        Assert.assertEquals( 1, parsedTraceFiles.size());
        
        System.out.println("DuafDroidParserTest.mainTest() " + parsedTraceFiles.entrySet().iterator().next().getKey() );
        System.out.println("DuafDroidParserTest.mainTest() " + parsedTraceFiles.entrySet().iterator().next().getValue() );
        
        
    }
    

}
