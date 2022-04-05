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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
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

    private String getTraceFileFrom(String testDirectoryName) {
        // /Trace-1642241004125.txt
        File testDirectory = new File(traceFolder, testDirectoryName);
        return testDirectoryName + "/" + testDirectory.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("Trace") && pathname.getName().endsWith(".txt");
            }
        })[0].getName();

    }

    private void checkAssumptions(ExecutionFlowGraph efg, CallGraph cg) {
        Collection<MethodInvocation> efgMethodInvocations = efg.getOrderedMethodInvocations();
        Collection<MethodInvocation> cgMethodInvocations = cg.getAllMethodInvocations();

        if (!cgMethodInvocations.containsAll(efgMethodInvocations)) {
            Collection<MethodInvocation> copy = new ArrayList<MethodInvocation>(efgMethodInvocations);
            copy.removeAll(cgMethodInvocations);
            logger.error("Missing elements " + copy);
        }
        Assume.assumeTrue(cgMethodInvocations.containsAll(efgMethodInvocations));

        if (!efgMethodInvocations.containsAll(cgMethodInvocations)) {
            Collection<MethodInvocation> copy = new ArrayList<MethodInvocation>(cgMethodInvocations);
            copy.removeAll(efgMethodInvocations);
            logger.error("Missing elements " + copy);
        }
        Assume.assumeTrue(efgMethodInvocations.containsAll(cgMethodInvocations));
    }

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

        checkAssumptions(_parsedTrace.getUIThreadParsedTrace().getFirst(),
                _parsedTrace.getUIThreadParsedTrace().getThird());

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        checkAssumptions(parsedTrace.getUIThreadParsedTrace().getFirst(),
                parsedTrace.getUIThreadParsedTrace().getThird());

        // Somehow this breaks the trace
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        checkAssumptions(parsedTrace.getUIThreadParsedTrace().getFirst(),
                parsedTrace.getUIThreadParsedTrace().getThird());

        //
        Set<MethodInvocation> all = mis.findCarvableMethodInvocations(_parsedTrace);
//        logger.info("-- All possible carvable targets ");
//        for (MethodInvocation m : all) {
//            logger.info(" " + m);
//        }

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

    /**
     * Generated from
     * /Users/gambi/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestUpdate/Trace-1644245708853.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule:
     * void addPattern(java.lang.String)>_412_818
     */
    /**
     * Generated from
     * /Users/gambi/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateFilterRuleTest#TestAutoSaveAfterPatternsUpdate/Trace-1644245853085.txt
     * Method invocation under test: <com.prismaqf.callblocker.rules.FilterRule:
     * void addPattern(java.lang.String)>_631_1256
     */
    /*
     * Private constructor... the ideal solution would be to realize that there's
     * another constructor that enables setting the same parameters set here
     * (name/description) and then call the public method addPattern Definitively
     * out of the scope here....
     */

    // Classes that extends subtypes of activities, like AppCompactActivity are not
    // correctly handled.
    @Test
    public void testActivitiesMissed() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.CallBlockerManagerTest#checkDisplay");

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onCreate(android.os.Bundle)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    //
    @Test
    public void testMissingMocks() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.UpdateCalendarRuleTest#TestUndoAction");

        String methodSignature = "<com.prismaqf.callblocker.NewEditCalendarRule: void refreshWidgets(boolean)>";
        int invocationCount = 403;
        int invocationTraceId = 805;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testNPEonOnCreate() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.UpdateCalendarRuleTest#TestUndoAction");

        String methodSignature = "<com.prismaqf.callblocker.EditCalendarRules: void onCreate(android.os.Bundle)>";
        int invocationCount = 85;
        int invocationTraceId = 170;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testWrongCallToEnableWidgets() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: void enableWidgets(boolean,boolean)>";
        int invocationCount = 211;
        int invocationTraceId = 421;

        // This should NOT fail
        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testWrapOnStop() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onStop()>";
        int invocationCount = 141;
        int invocationTraceId = 282;

        // This should NOT fail
        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testWrapOnDestroy() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onDestroy()>";
        int invocationCount = 333;
        int invocationTraceId = 666;

        // This should NOT fail
        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testValidationShoulNotTrigger() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onDestroy()>";
        int invocationCount = 333;
        int invocationTraceId = 666;

        // This should NOT fail
        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    // This should fail because the methodSignature belongs to a mi who's owner is
    // NOT constructed in the trace
    @Test
    public void testNPEForTestingEquals() throws FileNotFoundException, IOException, ABCException {
        try {
            file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

            String methodSignature = "<com.prismaqf.callblocker.filters.FilterHandle: boolean equals(java.lang.Object)>";
            int invocationCount = 324;
            int invocationTraceId = 644;

            // This should fail with a message, not sure how I can check it...
            runTheTest(methodSignature, invocationCount, invocationTraceId);
            fail();
        } catch (AssertionError e) {
            // Pass this target should not be carvable
        }
    }

    @Test
    public void testDoNotMockEqualsAndDoNotMockAbstract() throws FileNotFoundException, IOException, ABCException {
        try {
            file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

            String methodSignature = "<com.prismaqf.callblocker.NewEditActivity: boolean onOptionsItemSelected(android.view.MenuItem)>";
            int invocationCount = 307;
            int invocationTraceId = 613;

            runTheTest(methodSignature, invocationCount, invocationTraceId);
            fail();
        } catch (AssertionError e) {
//            e.printStackTrace();
            // Pass this target should not be carvable
        }
    }

    @Test
    public void testDoubleDefinitionOfActivityController() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest");

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onRequestPermissionsResult(int,java.lang.String\\[\\],int\\[\\])>";
        int invocationCount = 79;
        int invocationTraceId = 158;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testNPWWhileMocking() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1642196224533.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: com.prismaqf.callblocker.RuleNameValidator getRuleNameValidator()>";
        int invocationCount = 223;
        int invocationTraceId = 444;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * This problem was caused by having onCreateMenu method calling
     * super.onCreateMenu, hence reporting it twice.
     * 
     * @throws FileNotFoundException
     * 
     * @throws IOException
     * 
     * @throws ABCException
     */
    @Test
    public void testWrapNonRoot() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1642095899694.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: boolean onOptionsItemSelected(android.view.MenuItem)>";
        int invocationCount = 305;
        int invocationTraceId = 610;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    /*
     * THis error was caused by changing the ID of the method invocation after
     * adding it to the graph. In this case the hash of the object when it was added
     * is different than the hash of the same object when it was updated and the map
     * could not find the connection. Either one has to force the rebuild of the
     * hashmap or avoid changing "immutable" fields of the method invocations
     */
    @Test
    public void testDuplicateDependency() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1642105160429.txt";

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onCreate(android.os.Bundle)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testEnumConstantsShouldNotBeMocked() throws FileNotFoundException, IOException, ABCException {
        file = getTraceFileFrom("com.prismaqf.callblocker.UpdateCalendarRuleTest#TestUndoAction");

        String methodSignature = "<com.prismaqf.callblocker.NewEditCalendarRule: void refreshWidgets(boolean)>";
        int invocationCount = 302;
        int invocationTraceId = 603;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    // THIS IS THE CRITICAL TEST CASE - This is good up to mocking/creating the
    // EnumSet
    @Test
    public void testMissingInitializationOfIntent() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1642188454547.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditActivity: boolean onCreateOptionsMenu(android.view.Menu)>";
        int invocationCount = 452;
        int invocationTraceId = 904;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testMissingRobolectricAnnotation() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1642001015193.txt";

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: boolean onCreateOptionsMenu(android.view.Menu)>";
        int invocationCount = 76;
        int invocationTraceId = 152;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testClassAsPrimitiveType() throws FileNotFoundException, IOException, ABCException {
        try {
            file = "com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1641988207212.txt";

            String methodSignature = "<com.prismaqf.callblocker.PickActionFragment: void onStart()>";
            int invocationCount = 357;
            int invocationTraceId = 714;

            runTheTest(methodSignature, invocationCount, invocationTraceId);
            fail();
        } catch (AssertionError e) {
            // Pass this target should not be carvable
        }
    }

    //
    /**
     * At first sight this error happens because the Object is shared between two
     * activities: one creates it and the other uses it and the carved cannot find a
     * way to break the dependencies on the methods that set and get the state of
     * this shared object.
     * 
     * Probably, one solution might be to expose the state setting methods and see
     * whether they can break the dep on the activity. However, if some state of the
     * object is set using fields or something, and in general NON-primitive types
     * this might be tricky
     * 
     * XXX THIS IS A CHALLENGING BUT NICE CASE TO ADDRESS
     * 
     * 
     * 
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test
    public void cannotSimplifyActivities() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.NewCalendarRuleTest#TestAllDays/Trace-1641977005604.txt";

        String methodSignature = "<com.prismaqf.callblocker.rules.CalendarRule: java.lang.String getName()>";
        int invocationCount = 297;
        int invocationTraceId = 592;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void cannotSimplifyActivitiesThatBelongToAnonymClasses()
            throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.NewCalendarRuleTest#TestAllDays/Trace-1641977005604.txt";

        String methodSignature = "<com.prismaqf.callblocker.RuleNameValidator: void beforeTextChanged(java.lang.CharSequence,int,int,int)>";
        int invocationCount = 230;
        int invocationTraceId = 457;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    // Generating boolean data results in an exception: null variable -> smells like
    // we do not properly introduce a boolean node here? The call is replaced !
    @Test
    public void testFailToGenerateWithBoolean() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1641976870408.txt";

        String methodSignature = "<com.prismaqf.callblocker.EditFilters: boolean onCreateOptionsMenu(android.view.Menu)>";
        int invocationCount = 135;
        int invocationTraceId = 270;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testBrokenMockingGeneration() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1641976870408.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: void onCreate(android.os.Bundle)>";
        int invocationCount = 173;
        int invocationTraceId = 346;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }

    @Test
    public void testBrokenSimplification() throws FileNotFoundException, IOException, ABCException {
        file = "com.prismaqf.callblocker.EditFiltersTest#ChangeActionTest/Trace-1641908258889.txt";

        String methodSignature = "<com.prismaqf.callblocker.CallBlockerManager: void onCreate(android.os.Bundle)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
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

    @Test
    public void testAnotherNPE() throws FileNotFoundException, IOException, ABCException {

        file = "com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1641908215702.txt";

        String methodSignature = "<com.prismaqf.callblocker.NewEditFilter: void validateActions()>";
        int invocationCount = 669;
        int invocationTraceId = 1335;

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
