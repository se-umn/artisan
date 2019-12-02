package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.data.Triplette;

public class ParsedTrace {

    private static final Logger logger = LoggerFactory.getLogger(ParsedTrace.class);

    // Is this platform specific ?
    public final static String MAIN_THREAD = "UI:MainThread";

    // Thread Name vs Parsed Data
    private Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content = new HashMap<>();

    private String systemTestID = null;

    public ParsedTrace(String systemTestID) {
        this.systemTestID = systemTestID;
    }

    public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getUIThreadParsedTrace() {
        return content.get(MAIN_THREAD);
    }

    public void setContent(Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content) {
        this.content = content;

    }

    public void storeTo(File outputArtifactTo) throws FileNotFoundException, IOException {
        logger.info("Storing parsed trace to " + outputArtifactTo);
        String baseFileName = this.systemTestID;
        baseFileName += ".parsed.xml";

        // TODO Metadata include systemTestID and the list of thread/files?

        for (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> threadLocal : content
                .entrySet()) {

            String fileName = encode(threadLocal.getKey()) + "." + baseFileName;
            File storeTo = new File(outputArtifactTo, fileName);

            try (OutputStream writer = new FileOutputStream(storeTo)) {
                logger.info("\t Storing " + storeTo.getAbsolutePath());
                XStream xStream = new XStream();
                // Store the entire Entry, so we keep the threadName
                xStream.toXML(threadLocal, writer);
            }
        }
    }

    private String encode(String toEncode) {
        return toEncode.replaceAll("\\W+", "_");
    }

    public static ParsedTrace loadFromDirectory(File sourceDirectory) {
        logger.info("Loading parsed trace from " + sourceDirectory );
        
        Map<String, // ThreadName
                Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> content = new HashMap<>();

        XStream xStream = new XStream();

        ParsedTrace parsedTrace = null; 

        for (File file : sourceDirectory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".parsed.xml");
            }
        })) {
            if( parsedTrace == null ){
                // ModernAsyncTask_5.trace.log.parsed.xml -> trace
                String systemTestID = file.getName().replaceAll(".parsed.xml", "");
                systemTestID = systemTestID.split("\\.")[1];
                parsedTrace = new ParsedTrace(systemTestID);
            }
            logger.info("\t Loading data from " + file );
            Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> entry =
                    (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>) xStream.fromXML(file);
            content.put(entry.getKey(), entry.getValue());
        }
        
        parsedTrace.setContent( content );
        return parsedTrace;
    }

    // Mostly for testing
    public int getThreadCount() {
        return content.keySet().size();
    }

    // Static Load from File

    // Static Load from Directory

    // Static Store to directory

    // Static Store to file

}
