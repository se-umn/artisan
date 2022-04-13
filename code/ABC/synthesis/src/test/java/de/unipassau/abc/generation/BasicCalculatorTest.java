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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.BaseDebuggingTest;
import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.evaluation.MethodUnderTestWithLocalCounterNamer;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.shadowwriter.ShadowWriter;
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

public class BasicCalculatorTest extends BaseDebuggingTest {

    @BeforeClass
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(BasicCalculatorTest.class);
    }
    
    @Before
    public void setup() {
        this.traceFolder = "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/traces/";
    }



    @Test
    public void testCannotParse() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom(
                "abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity");

        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void checkResult(java.lang.String)>";
        int invocationCount = 92;
        int invocationTraceId = 183;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }
   
    // Error Carving <abc.basiccalculator.ExtendedResultActivity: void checkResult(java.lang.String)>_92_183 from 
    //  abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity/Trace-testCalculateNullPointerThrownByResultActivity-1649347464306.txt
    //  4612 [main] ERROR de.unipassau.abc.generation.BasicTestGenerator - Reason: We cannot make visible <abc.basiccalculator.ExtendedResultActivity: void checkResult(java.lang.String)>_92_183 because it is subsumed by necessary invocations: [<org.robolectric.android.controller.ActivityController org.robolectric.android.controller.ActivityController create()>_10014_112]

    @Test
    public void testCannotGenerateTestBecauseOfRobolectricWrapping() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom(
                "abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity");

        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void checkResult(java.lang.String)>";
        int invocationCount = 92;
        int invocationTraceId = 183;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * This seems to be a critical test case
     * abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity: 
     *  <abc.basiccalculator.ExtendedResultActivity: void onCreate(android.os.Bundle)>_56_112 
     */
    @Test
    public void testCannotFilterOutMultipleActivities() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom(
                "abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity");

        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 56;
        int invocationTraceId = 112;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testGlobalCache() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("abc.basiccalculator.MainActivityTest#testCalculateAndIncrementByTwo");

        String methodSignature = "<abc.basiccalculator.MainActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 3;
        int invocationTraceId = 6;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

        methodSignature = "<abc.basiccalculator.MainActivity: void sendResult(android.view.View)>";
        invocationCount = 6;
        invocationTraceId = 12;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

        methodSignature = "<abc.basiccalculator.MainActivity: java.lang.String eval(java.lang.String)>";
        invocationCount = 13;
        invocationTraceId = 25;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

    @Test
    public void testCannotFindFormalType() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom(
                "abc.basiccalculator.ExtendedMainActivityTest#testCalculateWithCommentThrowingIllegalArgumentException");

        String methodSignature = "<abc.basiccalculator.ExtendedMainActivity: void sendResult(android.view.View)>";
        int invocationCount = 34;
        int invocationTraceId = 68;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

    @Test
    public void testCannotMakeMethodVisible() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("abc.basiccalculator.MainActivityTest#testCalculateAndIncrementByTwo");

        String methodSignature = "<abc.basiccalculator.MainActivity: java.lang.String eval(java.lang.String)>";
        int invocationCount = 13;
        int invocationTraceId = 25;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

    @Test
    public void testAndroidMultiActivitySimplificationFail() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom(
                "abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity");

        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 60;
        int invocationTraceId = 120;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

    @Test
    public void testDuplicatedActivityController() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("abc.basiccalculator.MainActivityTest#testCalculateAndIncrementByOneWithLogging");

        String methodSignature = "<abc.basiccalculator.ResultActivity: void checkResult(java.lang.String)>";
        int invocationCount = 51;
        int invocationTraceId = 101;

        List<TestClass> testClasses = new ArrayList<TestClass>(
                runTheTest(methodSignature, invocationCount, invocationTraceId));

        // We cannot generate this test because it is impossible to carve out
        // checkResult invoked inside onCreate
        Assert.assertTrue(testClasses.size() == 0);
    }

    @Test
    public void testEmptyShadows() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("abc.basiccalculator.MainActivityTest#testCalculateAndIncrementByTwo");

        String methodSignature = "<abc.basiccalculator.MainActivity: void sendResult(android.view.View)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        List<TestClass> testClasses = new ArrayList<TestClass>(
                runTheTest(methodSignature, invocationCount, invocationTraceId));

        File tmpOutputFolder = tempFolder.newFolder();
        ShadowWriter shadowWriter = new ShadowWriter();
        shadowWriter.generateAndWriteShadows(testClasses, tmpOutputFolder);
    }

    @Test
    public void testAssertionFailAsFirstInstruction() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("abc.basiccalculator.MainActivityTest#testNullPointerThrownBySystem");

        String methodSignature = "<abc.basiccalculator.MainActivity: void sendResult(android.view.View)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        // This should NOT fail
        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    //
    // Mocks are shadows are misplaced and incomplete (no findViewByID anywhere!)
    // Not all shadows contain the logic to program the mocks
    //

    @Test
    public void testNpeWhileShadowing() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("abc.basiccalculator.MainActivityTest#testCalculateAndIncrementByOneWithLogging");

        String methodSignature = "<abc.basiccalculator.MainActivity: void sendResult(android.view.View)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        // This should NOT fail
        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

}
