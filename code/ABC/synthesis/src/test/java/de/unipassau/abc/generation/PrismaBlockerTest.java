package de.unipassau.abc.generation;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
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
import soot.jimple.LongConstant;

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
            logger.info("" + m.isAbstract() + " " + m);
        }

        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = all.stream().filter(mi -> matcher.matches(mi))
                .collect(Collectors.toList());

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
    @Test
    public void testDoubleIntent() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1641539406706.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: void enableWidgets(boolean,boolean)>";
        int invocationCount = 312;
        int invocationTraceId = 621;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    //
    @Test
    public void testNPEOnActivityCreateForMockedIntent() throws FileNotFoundException, IOException, ABCException {
        // This issue is solved by preventing the carving of Activity Constructors
        // The explanation is: it does not make sense to carve them, the real reason is
        // that synthesis must bring in onCreate
        // to trigger the constructor, but carver does not know it, so it does not
        // consider the right mocking of the intent.
        try {
            file = "com.prismaqf.callblocker.NewCalendarRuleTest#TestDeleteActionMissingOnCreate/Trace-1641539537241.txt";

            String methodSignature = "<com.prismaqf.callblocker.EditCalendarRules: void <init>()>";
            int invocationCount = 1;
            int invocationTraceId = 2;

            runTheTest(methodSignature, invocationCount, invocationTraceId);
            //
            fail();
        } catch (AssertionError e) {
            Assert.assertTrue(e.getMessage().contains("No selected carvable tests"));
        }
    }

    // TODO How to test menus
    // https://stackoverflow.com/questions/21856053/how-to-test-menu-in-android-with-robolectric
    // MainActivity activity =
    // Robolectric.buildActivity(MainActivity.class).create().visible().get();
    // shadowOf(activity).getOptionsMenu()
    // shadowOf(activity).getOptionsMenu().findMenuItem(...)

    // onCreateOptionsMenu
    @Test
    public void testNPEOnCreateOptionMenu() throws FileNotFoundException, IOException, ABCException {
        // This issue is solved by preventing the carving of onCreateOptionMenu
        // This will not solve the issue because some activity requires to have the menu
        // set for testing something else...
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1641539406706.txt";

        String methodSignature = "<com.prismaqf.callblocker.EditFilters: boolean onCreateOptionsMenu(android.view.Menu)>";
        int invocationCount = 135;
        int invocationTraceId = 270;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

//    
    @Test
    public void testNPEOnWriteParcel() throws FileNotFoundException, IOException, ABCException {

        file = "com.prismaqf.callblocker.EditFiltersTest#UndoActionTest/Trace-1641539372205.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: void change()>";
        int invocationCount = 309;
        int invocationTraceId = 616;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * We instantiate an object of type FilterRule as an new Object()
     * 
     * TODO This problem is still open. The test needs to invoke a private
     * constructor but fails to do so. We need to invoke a static field (a BUILDER)
     * and one of the methods it provides use (createFromParcel)
     */
    @Test
    public void testIncompatibleTypes() throws FileNotFoundException, IOException, ABCException {

        file = "com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1641539434611.txt";

        String methodSignature = "<com.prismaqf.callblocker.rules.FilterRule: void addPattern(java.lang.String)>";
        int invocationCount = 431;
        int invocationTraceId = 856;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * Spurious static dependencies on package protected methods break the test (do
     * not compile). Unfortunately, we do not store data about protected methods,
     * but only private and non-private.
     * 
     * TODO This problem is still open ...
     */
    @Test
    public void testCallingPackageProtectedMethods() throws FileNotFoundException, IOException, ABCException {

        file = "com.prismaqf.callblocker.UpdateFilterRuleTest#TestAutoSaveAfterPatternsUpdate/Trace-1641380380857.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilterRule: void validateActions()>";
        int invocationCount = 719;
        int invocationTraceId = 1434;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testPrivateClassesInsteadOfEnumEntries() throws FileNotFoundException, IOException, ABCException {

        file = "com.prismaqf.callblocker.UpdateCalendarRuleTest#TestChangeAction/Trace-1641380352701.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>";
        int invocationCount = 455;
        int invocationTraceId = 907;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * There are issues with handling boolean and longs since soot represent them
     * with "ints"
     */
    @Test
    public void testWrongMockedType() throws FileNotFoundException, IOException, ABCException {

        file = "com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1641377668292.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditCalendarRule: void enableWidgets(boolean,boolean)>";
        int invocationCount = 574;
        int invocationTraceId = 1146;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * Problem was we missed the call to configure the mocks after calling `when`
     * 
     * @throws FileNotFoundException
     * 
     * @throws IOException
     * 
     * @throws ABCException
     */
    @Test
    public void testUnfinishedMocking() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1641317398097.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditActivity: boolean onOptionsItemSelected(android.view.MenuItem)>";
        int invocationCount = 619;
        int invocationTraceId = 1238;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

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

    @Test
    public void testBrokenMocks() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.CallBlockerManagerTest#testContactResolverDoesNotThrow/Trace-1641226161324.txt";

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onRequestPermissionsResult(int,java.lang.String\\[\\],int\\[\\])>";
        int invocationCount = 79;
        int invocationTraceId = 158;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }
}
