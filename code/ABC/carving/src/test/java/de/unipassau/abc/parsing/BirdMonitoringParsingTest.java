package de.unipassau.abc.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.StaticParsedTraceDecorator;

public class BirdMonitoringParsingTest {

    //
    private String traceFileName = null;
    private String traceFolder = "/Users/gambi/action-based-test-carving/apps-src/bird-monitor/";

    private ParsedTrace runParser() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(traceFolder, traceFileName);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace parsedTrace = parser.parseTrace(traceFile);
        //
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);
        //
        ParsedTraceDecorator staticDecorator = new StaticParsedTraceDecorator();
        parsedTrace = staticDecorator.decorate(parsedTrace);
        //

        return parsedTrace;
    }

    @Test
    public void testFailedParsingBecauseOfSameSinkAndSouceIntents() throws FileNotFoundException, IOException, ABCException {
        traceFileName = "traces/nz.org.cacophony.birdmonitor.CreateAccountTest#createAccountTest/Trace-1641749933533.txt";
        ParsedTrace parsedTrace = runParser();
        Assert.assertNotNull(parsedTrace);
    }
    
    @Test
    public void testFailedParsing() throws FileNotFoundException, IOException, ABCException {
        traceFolder = "src/test/resources/nz.org.cacophony.birdmonitor";
        traceFileName = "trace.txt";
        ParsedTrace parsedTrace = runParser();
        Assert.assertNotNull(parsedTrace);
    }

}
