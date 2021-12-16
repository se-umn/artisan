package de.unipassau.abc.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;
import org.xmlpull.v1.XmlPullParserException;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.BasicTestGenerator;
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
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class SmokeTest {

    public static File androidJAR = new File("/Users/gambi/Library/Android/sdk/platforms/android-29/android.jar");

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public Slf4jSimpleLoggerRule loggerRule = new Slf4jSimpleLoggerRule(Level.DEBUG);

    @Ignore // This has hardcoded paths
    @Test
    public void testSetupSoot() throws IOException, XmlPullParserException, ABCException {
        File theAPK = new File("/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app-original.apk");
        String[] args = new String[] { "--apk", theAPK.getAbsolutePath(), //
                "--android-jar", androidJAR.getAbsolutePath(), //
                "--output-to", temporaryFolder.newFolder().getAbsolutePath() };

        de.unipassau.abc.instrumentation.Main.main(args);

    }

    /*
     * java.lang.NullPointerException at
     * de.unipassau.abc.generation.BasicTestGenerator.
     * generateCarvedTestFromCarvedExecution(BasicTestGenerator.java:154) at
     * de.unipassau.abc.generation.BasicTestGenerator.generateTests(
     * BasicTestGenerator.java:79) at
     * de.unipassau.abc.evaluation.Main.main(Main.java:202)
     * 
     */
//    String methodSignature = "<com.prismaqf.callblocker.filters.FilterHandle: java.lang.String getName()>_238_473";
//    int invocationCount = 238;
//    int invocationTraceId = 473;

    // <com.prismaqf.callblocker.RuleNameValidator: void
    // beforeTextChanged(java.lang.CharSequence,int,int,int)>_240_476
    // void onTextChanged(java.lang.CharSequence,int,int,int)>_241_478
    // <com.prismaqf.callblocker.filters.FilterHandle: java.lang.String
    // getCalendarRuleName()>_256_509
//    <com.prismaqf.callblocker.filters.FilterHandle: java.lang.String getFilterRuleName()>_259_515

//    <com.prismaqf.callblocker.filters.FilterHandle: java.lang.String getActionSimpleName()>_262_521
//    <com.prismaqf.callblocker.filters.FilterHandle: boolean equals(java.lang.Object)>_298_592
//    <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getName()>_510_1018

//    <com.prismaqf.callblocker.rules.FilterRule: java.lang.String getDescription()>_530_1058
//    <com.prismaqf.callblocker.NewEditActivity: void refreshWidgets(boolean)>_563_1124

    // Problems with synthesis:
//    <com.prismaqf.callblocker.FilterFragment: void <init>()>_88_175
//    com.prismaqf.callblocker.EditCursorListFragment: void <init>()>_89_176
//    <com.prismaqf.callblocker.EditCursorListFragment: void onStart()>_99_198
//    <com.prismaqf.callblocker.FilterFragment: android.widget.SimpleCursorAdapter getAdapter()>_107_213

//    <com.prismaqf.callblocker.FilterFragment: void initLoader()>_112_223
    @Test
    public void testPrismCallblocker() throws FileNotFoundException, IOException, ABCException {

        File traceFile = new File("./src/test/resources/com.prismaqf.callblocker/Trace-1632734872536.txt");
        File theAPK = new File("./src/test/resources/com.prismaqf.callblocker/app-original.apk");

        ParsingUtils.setupSoot(androidJAR, theAPK);

        // Parse and start the carving
        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        // Manually set the method invocation to carve
        List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<MethodInvocation>();

        // This one is broken badly
        String methodSignature = "<com.prismaqf.callblocker.filters.FilterHandle: void writeToParcel(android.os.Parcel,int)>";
        int invocationCount = 391;
        int invocationTraceId = 782;

        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        targetMethodsInvocationsList.addAll(mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                parsedTrace);

        // Put each test in a separate test case
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();
        TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
        Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

        // Write test cases to files and try to compile them
        JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

        Map<TestClass, File> generatedTests = new HashMap<TestClass, File>();

        // sort by the trace id of the first method under test (this helps debugging)
        List<TestClass> testSuiteList = new ArrayList<TestClass>();
        testSuiteList.addAll(testSuite);
        List<Integer> indexInTraceList = new ArrayList<Integer>();
        for (TestClass testClass : testSuiteList) {
            int minValue = Integer.MAX_VALUE;
            for (CarvedTest carvedTest : testClass.getCarvedTests()) {
                if (carvedTest.getMethodUnderTest().getInvocationTraceId() < minValue) {
                    minValue = carvedTest.getMethodUnderTest().getInvocationTraceId();
                }
            }
            indexInTraceList.add(new Integer(minValue));
        }
        ArrayList<TestClass> sortedTestSuiteList = new ArrayList(testSuiteList);
        Collections.sort(sortedTestSuiteList, new Comparator<TestClass>() {
            public int compare(TestClass left, TestClass right) {
                return indexInTraceList.get(testSuiteList.indexOf(left))
                        - indexInTraceList.get(testSuiteList.indexOf(right));
            }
        });

        // generate unit tests
        for (TestClass testCase : sortedTestSuiteList) {
            try {
                TestMethodNamer testMethodNamer = new MethodUnderTestWithLocalCounterNamer();
                CompilationUnit cu = writer.generateJUnitTestCase(testCase, testMethodNamer);
                System.out.print(cu.toString());

            } catch (Exception e) {
                System.err.println("Cannot generate test " + testCase.getName());
                e.printStackTrace();
            }
        }

    }
    
    
//    Trace-NPE

    @Ignore // This has hardcoded paths
    @Test
    public void testFixNPE() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(
                "/Users/gambi/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoTest#checkManualBackButtonNavigatesToSignBrowser/Trace-1631534738033.txt");
        File theAPK = new File(
                "/Users/gambi/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/app-original.apk");

        ParsingUtils.setupSoot(androidJAR, theAPK);

        // Parse and start the carving
        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        // Manually set the method invocation to carve
        List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<MethodInvocation>();

        String methodSignature = "<de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign: java.lang.String getNameLocaleDe()>";
        int invocationCount = 198;
        int invocationTraceId = 394;

        // Ensure we use the actual method invocation
        MethodInvocation targetMethodInvocation = new MethodInvocation(invocationTraceId, invocationCount,
                methodSignature);
        targetMethodsInvocationsList.add(targetMethodInvocation);

        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                parsedTrace);

    }

    @Test
    public void testSingleMethodInvocationSearch() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(
                "src/test/resources/de.lebenshilfe_muenster.uk_gebaerden_muensterland/traces/Trace-1631776083286.txt");
        File theAPK = new File(
                "src/test/resources/de.lebenshilfe_muenster.uk_gebaerden_muensterland/apks/app-original.apk");

        ParsingUtils.setupSoot(androidJAR, theAPK);
        
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);
        
        
        
        // Parse and start the carving
        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        MethodInvocationSelector mis = new MethodInvocationSelector();
        Set<MethodInvocation> unique = mis.findUniqueCarvableMethodInvocations(parsedTrace);
        System.out.println("Found unique carvable targets: " + unique.size());
        unique.forEach(System.out::println);
        Assert.assertEquals(32, unique.size());
    }

    @Test
    public void aTest() {
        int i = 10;
        int total = 102;

        System.out.println(String.format("Progress %.1f%%\n", 100.0 * i / total));
    }
}
