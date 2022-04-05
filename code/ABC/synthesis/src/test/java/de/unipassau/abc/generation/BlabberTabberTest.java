
package de.unipassau.abc.generation;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.BaseDebuggingTest;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.evaluation.MethodUnderTestWithLocalCounterNamer;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.simplifiers.RobolectricActivitySimplifier;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.NameTestCaseGlobally;
import de.unipassau.abc.generation.utils.TestCaseNamer;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestCaseOrganizers;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.generation.utils.TestMethodNamer;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.StaticParsedTraceDecorator;

public class BlabberTabberTest extends BaseDebuggingTest {

    @BeforeClass
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(BlabberTabberTest.class);
    }
    
    @Before
    public void setup() {
        this.traceFolder = "/Users/gambi/action-based-test-carving/apps-src/blabbertabber/traces/";
    }


    /**
     * Error: Activity event onResume called directly resulting in NPE, all tests using it as precondition fail.
     * Solution: Implement onResume method in {@link RobolectricActivitySimplifier}
     * 
     * com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest:
     * <com.blabbertabber.blabbertabber.MainActivity: void onResume()>_10_20
     * <com.blabbertabber.blabbertabber.MainActivity: void launchRecordingActivity(android.view.View)>_24_48
     * <com.blabbertabber.blabbertabber.MainActivity: void launchRecordingActivity()>_25_49
     * 
     * com.blabbertabber.blabbertabber.AboutActivityTest#buttonAckFinishTest:
     * <com.blabbertabber.blabbertabber.AboutActivity: void onResume()>_7_14
     * 
     * com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonTest:
     * <com.blabbertabber.blabbertabber.MainActivity: void onResume()>_10_20
     * 
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test @Ignore
    public void robolectricControllerPickTheWrongActivityControllerOnResume() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom("com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest");

        String methodSignature = "<com.blabbertabber.blabbertabber.MainActivity: void onResume()>";
        int invocationCount = 10;
        int invocationTraceId = 20;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }
    
    /**
     * Error: Activity event onPause called directly resulting in NPE, all tests using it as precondition fail.
     * Solution: Implement onPause method in {@link RobolectricActivitySimplifier}
     * 
     * com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest:
     * <com.blabbertabber.blabbertabber.MainActivity: void onPause()>_33_66
     * 
     * com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonTest:
     * <com.blabbertabber.blabbertabber.MainActivity: void onPause()>_24_48
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test @Ignore
    public void robolectricControllerPickTheWrongActivityControllerOnPause() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom("com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonClickedTest");
        
        String methodSignature = "<com.blabbertabber.blabbertabber.MainActivity: void onPause()>";
        int invocationCount = 33;
        int invocationTraceId = 66;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }
     
    @Test
    public void problematicTest() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom("com.blabbertabber.blabbertabber.MainActivityTest#recordingButtonTest");
        String methodSignature = "<com.blabbertabber.blabbertabber.MainActivity: void onPause()>";
        int invocationCount = 24;
        int invocationTraceId = 48;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }
   
}
