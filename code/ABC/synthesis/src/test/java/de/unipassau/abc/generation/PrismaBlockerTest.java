package de.unipassau.abc.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.evaluation.MethodUnderTestWithLocalCounterNamer;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
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

public class PrismaBlockerTest {

    private String traceFolder = "/Users/gambi/action-based-test-carving/apps-src/PrismaCallBlocker/traces/";
    private String file = "com.prismaqf.callblocker.NewFilterRuleTest#TestUsedNameShouldFlag/Trace-1640962229041.txt";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private final static Logger logger = LoggerFactory.getLogger(PrismaBlockerTest.class);

    // This is mostly to debug
    public void runTheTest(String methodSignature, int invocationCount, int invocationTraceId)
            throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(traceFolder, file);

        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File(traceFolder, "app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

     // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        
        
        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
    

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
    
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        //
        Set<MethodInvocation> all = mis.findAllCarvableMethodInvocations(_parsedTrace);
        logger.info("-- All possible carvable targets ");
        for (MethodInvocation m : all) {
            logger.info("" + m.isAbstract() + " " + m );
        }
        
        MethodInvocationMatcher matcher = MethodInvocationMatcher.fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = all.stream().filter( mi -> matcher.matches(mi)).collect( Collectors.toList()); 

        assert listOfTargetMethodsInvocations.size() > 0 : "No selected carvable tests"; 
        
        logger.info("Selected Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.info("" + m);
        }
        
        
        
        
        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
        List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<MethodInvocation>();
        targetMethodsInvocationsList.addAll(listOfTargetMethodsInvocations);
        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                parsedTrace);

        int carvedTargets = carvedTests.size();

        logger.info("Carved targets " + carvedTargets + " / " + listOfTargetMethodsInvocations.size());

        // Put each test in a separate test case
        TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
        Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

        // Write test cases to files and try to compile them

        JUnitTestCaseWriter writer = new JUnitTestCaseWriter();
        TestMethodNamer testMethodNamer = new MethodUnderTestWithLocalCounterNamer();
        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase, testMethodNamer);
            logger.info(cu.toString());
        }
    }

    //

    /*
     * TODO: This issue is a combination of an activity which needs to be
     * constructed and required an intent, but the intent is not really used for the
     * constructor rather it is used to call onCreate that, in turns, calls the
     * constructor? Basically, we need to mock the intent but really we should not
     * ... We say to do it because the activity requires the intent, but not for the
     * constructor
     * 
     * Possible solution: do not care about intents at all for constructors!
     * 
     * ISSUE IS OPEN
     */
    @Test
    @Ignore
    public void testConstructorForActivityThatRequiresIntent() throws FileNotFoundException, IOException, ABCException {
        String methodSignature = "<com.prismaqf.callblocker.EditCalendarRules: void <init>()>";
        int invocationCount = 1;
        int invocationTraceId = 2;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * java.lang.NullPointerException at
     * de.unipassau.abc.generation.mocks.MockGenerator.lambda$generateMocks$0(
     * MockGenerator.java:181) at
     * java.util.Arrays$ArrayList.replaceAll(Arrays.java:3889) at
     * de.unipassau.abc.generation.mocks.MockGenerator.generateMocks(MockGenerator.
     * java:187)
     * 
     * NOTE: We do not support the generation of tests that involve Fragments yet
     */
    @Test
    @Ignore
    public void testNPEWhileCreatingMocks() throws FileNotFoundException, IOException, ABCException {
        String methodSignature = "<com.prismaqf.callblocker.EditCursorListFragment: android.view.View onCreateView(android.view.LayoutInflater,android.view.ViewGroup,android.os.Bundle)>";
        int invocationCount = 23;
        int invocationTraceId = 46;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testNPE() throws FileNotFoundException, IOException, ABCException {
        String methodSignature = "<com.prismaqf.callblocker.rules.FilterRule: java.util.Set getPatternKeys()>";
        int invocationCount = 122;
        int invocationTraceId = 242;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * This test is broken because we cannot carve abstract methods !
     */
    @Test
    @Ignore
    public void testMissingIntentDeclaration() throws FileNotFoundException, IOException, ABCException {
        String methodSignature = "<com.prismaqf.callblocker.NewEditActivity: void <init>()>";
        int invocationCount = 68;
        int invocationTraceId = 135;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }
    
    @Test
    public void testAmbiguousCall() throws FileNotFoundException, IOException, ABCException {
        
        file = "com.prismaqf.callblocker.ManageFilterPatternsTest#EditAPatternShouldWork/Trace-1640965632808.txt";
        
        String methodSignature = "<com.prismaqf.callblocker.EditFilterPatterns: boolean onOptionsItemSelected(android.view.MenuItem)>";
        int invocationCount = 293;
        int invocationTraceId = 586;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }
    
    @Test
    public void testDirectAccessToAnonymClass() throws FileNotFoundException, IOException, ABCException {
    
        file = "com.prismaqf.callblocker.CallBlockerManagerTest#checkThatTurningOnServiceChangesText/Trace-1640972221017.txt";
        
        String methodSignature = "<com.prismaqf.callblocker.CallDetectService: void onDestroy()>";
        int invocationCount = 220;
        int invocationTraceId = 440;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }
}
