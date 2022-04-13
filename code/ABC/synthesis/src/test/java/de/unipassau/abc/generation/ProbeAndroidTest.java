
package de.unipassau.abc.generation;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.BaseDebuggingTest;
import de.unipassau.abc.exceptions.ABCException;

public class ProbeAndroidTest extends BaseDebuggingTest {

    @BeforeClass
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(ProbeAndroidTest.class);
    }

    @Before
    public void setup() {
        this.traceFolder = "/Users/gambi/action-based-test-carving/apps-src/probe-android/traces/";
    }

    /**
     * Error: Strings are not escaped so strings that contain " or ' breaks the
     * tests
     * 
     * org.openobservatory.ooniprobe.ui.MainActivityWebsitesTest#lunchCustomWebsiteIntentTest
     * <org.openobservatory.ooniprobe.test.TestAsyncTask: void
     * onPostExecute(java.lang.Object)>_2807_5614
     * 
     */
    @Test
    public void testEscapeStrings() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom(
                "org.openobservatory.ooniprobe.ui.MainActivityWebsitesTest#lunchCustomWebsiteIntentTest");
        //
        String methodSignature = "<org.openobservatory.ooniprobe.test.TestAsyncTask: void onPostExecute(java.lang.Object)>";
        int invocationCount = 2807;
        int invocationTraceId = 5614;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

}
