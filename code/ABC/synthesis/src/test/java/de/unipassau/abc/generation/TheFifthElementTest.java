package de.unipassau.abc.generation;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.BaseDebuggingTest;
import de.unipassau.abc.exceptions.ABCException;

public class TheFifthElementTest extends BaseDebuggingTest {

    @BeforeClass
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(TheFifthElementTest.class);
    }

    @Before
    public void setup() {
        this.traceFolder = "/Users/gambi/action-based-test-carving/apps-src/FifthElement/traces/";
    }

    @Test
    public void testNPEwhileMocking() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest");

        String methodSignature = "<fifthelement.theelement.business.services.SongService: boolean updateSong(fifthelement.theelement.objects.Song)>";
        int invocationCount = 2313;
        int invocationTraceId = 4624;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

//     
    @Test
    public void testSkipPersistence() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest");

        String methodSignature = "<fifthelement.theelement.objects.Song: fifthelement.theelement.objects.Author getAuthor()>";
        int invocationCount = 210;
        int invocationTraceId = 416;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    // Does generating object instances require to start activities, and the like?
    @Test
    public void testCarvingDataObjects() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("fifthelement.theelement.presentation.activities.PlayMusicTests#seekTest");

        String methodSignature = "<fifthelement.theelement.objects.Author: void <init>(java.util.UUID,java.lang.String)>";
        int invocationCount = 88;
        int invocationTraceId = 172;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

}
