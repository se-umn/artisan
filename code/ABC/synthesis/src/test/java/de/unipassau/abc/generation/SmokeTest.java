package de.unipassau.abc.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.BasicCarver;
import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.shadowwriter.ShadowWriter;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.NameTestCaseGlobally;
import de.unipassau.abc.generation.utils.TestCaseNamer;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestCaseOrganizers;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.StaticParsedTraceDecorator;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;

public class SmokeTest {

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    private final static Logger logger = LoggerFactory.getLogger(SmokeTest.class);

    /*
     * trace
     * traces/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt
     */

//    @Test
//    public void mainTest() throws IOException, ABCException {
//        String[] args = {
//
//                "--trace-files", //
//                "./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt", //
//                "--android-jar", //
//                "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar", //
//                "--apk", //
//                "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app/build/outputs/apk/debug/app-debug.apk" };
//
//        Main.main(args);
//
//    }
//
//    @Test
//    public void testBrokenTestsAftertGeneratingMocks() throws FileNotFoundException, IOException, ABCException {
//        File traceFile = new File(
//                "./src/test/resources/abc.basiccalculator/testNullPointerThrownBySystem-trace.txt");
//        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();
//
//        TraceParser parser = new TraceParserImpl();
//        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
//        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
//        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
//
//        MethodInvocationSearcher mis = new MethodInvocationSearcher();
//        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(mis.findAllCarvableMethodInvocations(parsedTrace));
//        Collections.sort( listOfTargetMethodsInvocations );
//
//        int allCarvableTargets = listOfTargetMethodsInvocations.size();
//
//        logger.info("Carvable targets ");
//        listOfTargetMethodsInvocations.forEach(System.out::println);
//
//        // SELECT THE ACTUAL carved execution to consider
//        Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
//        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(0));
//        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(1));
//        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(2));
//        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(3));
//
//
//        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
//        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations, parsedTrace);
//
//        int carvedTargets = carvedTests.size();
//
//        logger.info("Carved targets " + carvedTargets + " / " + allCarvableTargets);
//
//        // Put each test in a separate test case
//        TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
//        Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));
//
//        // Write test cases to files and try to compile them
//
//        JUnitTestCaseWriter writer = new JUnitTestCaseWriter();
//
//        for (TestClass testCase : testSuite) {
//            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
//            try (PrintStream out = System.out) {
//                out.print(cu.toString());
//            }
//        }
//    }
    
    @Test
    public void testNPEWhileGenerating() throws FileNotFoundException, IOException, ABCException {
        String folder =  "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/traces/abc.basiccalculator.ExtendedMainActivityTest#testCalculateAndReturnBackToMain";
        String file = "Trace-testCalculateAndReturnBackToMain-1639739283539.txt";
        
        File traceFile = new File(folder, file);
        
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        String methodSignature = "<abc.basiccalculator.ExtendedMainActivity: void sendResult(android.view.View)>";
        int invocationCount = 10;
        int invocationTraceId = 20;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
        
    }
//    traces/abc.basiccalculator.ExtendedMainActivityTest#testCalculateAndReturnBackToMain/Trace-testCalculateAndReturnBackToMain-1639739283539.txt
    @Test
    public void testNumberFormatException() throws FileNotFoundException, IOException, ABCException {
        String folder =  "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/traces/abc.basiccalculator.ExtendedMainActivityTest#testCalculateAndReturnBackToMain";
        String file = "Trace-testCalculateAndReturnBackToMain-1639041266003.txt";
        
        File traceFile = new File(folder, file);
        
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 60;
        int invocationTraceId = 120;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
        
        // TODO How does this work when we need to process multiple traces?
        logger.info("Generating shadows");
        // generate shadows needed for test cases
        
        List<TestClass> sortedTestSuiteList = new ArrayList(testSuite);
//        Collections.sort(sortedTestSuiteList, new Comparator<TestClass>() {
//            public int compare(TestClass left, TestClass right) {
//                return indexInTraceList.get(testSuiteList.indexOf(left))
//                        - indexInTraceList.get(testSuiteList.indexOf(right));
//            }
//        });

        ShadowWriter shadowWriter = new ShadowWriter();
        shadowWriter.generateAndWriteShadows(sortedTestSuiteList, tempFolder.newFolder());

    }
    
    
    @Test
    public void testAnotherNumberFormatException() throws FileNotFoundException, IOException, ABCException {
        String folder =  "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/traces/abc.basiccalculator.ExtendedMainActivityTest#testCalculateWithValidComment";
        String file = "Trace-testCalculateWithValidComment-1639041274941.txt";
        
        File traceFile = new File(folder, file);
        
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 88;
        int invocationTraceId = 176;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
        

    }

    @Test
    public void testNPWWhileGeneratingMocks() throws FileNotFoundException, IOException, ABCException {
        String folder =  "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/traces/abc.basiccalculator.MainActivityTest#testCalculateAnExceptionAcrossMethods";
        String file = "Trace-testCalculateAnExceptionAcrossMethods-1639041211999.txt";
        
        File traceFile = new File(folder, file);
        
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        String methodSignature = "<abc.basiccalculator.ResultActivity: void checkResult(java.lang.String)>";
        int invocationCount = 51;
        int invocationTraceId = 101;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
        
        // TODO How does this work when we need to process multiple traces?
        logger.info("Generating shadows");
        // generate shadows needed for test cases
        
        List<TestClass> sortedTestSuiteList = new ArrayList(testSuite);
//        Collections.sort(sortedTestSuiteList, new Comparator<TestClass>() {
//            public int compare(TestClass left, TestClass right) {
//                return indexInTraceList.get(testSuiteList.indexOf(left))
//                        - indexInTraceList.get(testSuiteList.indexOf(right));
//            }
//        });

        ShadowWriter shadowWriter = new ShadowWriter();
        shadowWriter.generateAndWriteShadows(sortedTestSuiteList, tempFolder.newFolder());

    }
    
    @Test
    public void testMissingShadows() throws FileNotFoundException, IOException, ABCException {
        String folder = "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/traces/abc.basiccalculator.ExtendedMainActivityTest#testCalculateNullPointerThrownByResultActivity";
        String file = "Trace-testCalculateNullPointerThrownByResultActivity-1638792884899.txt";
        
        File traceFile = new File(folder, file);
        
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        String methodSignature = "<abc.basiccalculator.UiStorage: java.util.List getElements()>";
        int invocationCount = 64;
        int invocationTraceId = 126;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
        
        // TODO How does this work when we need to process multiple traces?
        logger.info("Generating shadows");
        // generate shadows needed for test cases
        
        List<TestClass> sortedTestSuiteList = new ArrayList(testSuite);
//        Collections.sort(sortedTestSuiteList, new Comparator<TestClass>() {
//            public int compare(TestClass left, TestClass right) {
//                return indexInTraceList.get(testSuiteList.indexOf(left))
//                        - indexInTraceList.get(testSuiteList.indexOf(right));
//            }
//        });

        ShadowWriter shadowWriter = new ShadowWriter();
        shadowWriter.generateAndWriteShadows(sortedTestSuiteList, tempFolder.newFolder());

    }

    @Test
    public void testNPEonActivityConstructor() throws FileNotFoundException, IOException, ABCException {
        /*
         * The issue here is that ResultActivity logically requires an Intent, probably
         * because it gets one in one of its methods. However, it does NOT requires it
         * for creation <init>; therefore, the intent will NOT be included in the carved
         * data structures. Unfortunately, since the Activity object requires the intent
         * the synthesis tries to access that variable anyway.
         * 
         */

        File traceFile = new File("./src/test/resources/abc.basiccalculator/Trace-NPE_on_constructor.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        String methodSignature = "<abc.basiccalculator.ResultActivity: void <init>()>";
        int invocationCount = 29;
        int invocationTraceId = 58;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
    }

    // Trace-NPE
    @Test
    public void testStaticDependencies() throws FileNotFoundException, IOException, ABCException {

        // NOTE: This trace was patched: loadUiElements has been made PUBLIC !

        File traceFile = new File("./src/test/resources/abc.basiccalculator/Trace-UiStorage.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

        // Find the target method invocation
//        abc.basiccalculator.ExtendedResultActivity@259693653;
//        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void onCreate(android.os.Bundle)>";
//        int invocationCount = 85;
//        int invocationTraceId = 169;
        String methodSignature = "<abc.basiccalculator.ExtendedResultActivity: void loadUiElements()>";
        int invocationCount = 91;
        int invocationTraceId = 180;
        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }

    }

    @Test
    public void testFailedParsingForNullElementInPair() throws FileNotFoundException, IOException, ABCException {

        File traceFile = new File("./src/test/resources/abc.basiccalculator/Trace-NullPair.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        //

        // Make sure we do NOT decorate our own decorators and methods !
        ParsedTraceDecorator decorator = new StaticParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
        //
        decorator = new AndroidParsedTraceDecorator();
        parsedTrace = decorator.decorate(parsedTrace);

    }

    // Trace-NPE
    @Test
    public void testNPEAfterCarvingIntents() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File("./src/test/resources/abc.basiccalculator/Trace-NPE.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File("./src/test/resources/abc.basiccalculator/app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        // Find the target method invocation
        String methodSignature = "<abc.basiccalculator.MainActivity: java.lang.String eval(java.lang.String)>";
        int invocationCount = 16;
        int invocationTraceId = 31;

        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }

    }

    @Test
    public void brokenTestGeneration() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File("./src/test/resources/com.better.alarm/Trace-1622638473760.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        MethodInvocationSelector mis = new MethodInvocationSelector();
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findAllCarvableMethodInvocations(parsedTrace));

        System.out.println("Found " + listOfTargetMethodsInvocations.size() + " carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            System.out.println("\t - " + m);
        }

//        // SELECT THE ACTUAL carved execution to consider
        Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
//        for(MethodInvocation targetMethod:listOfTargetMethodsInvocations){
//            targetMethodsInvocations.add(targetMethod);
//        }
        targetMethodsInvocations.addAll(listOfTargetMethodsInvocations);

        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
        List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<MethodInvocation>();
        targetMethodsInvocationsList.addAll(targetMethodsInvocations);
        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                parsedTrace);

        int carvedTargets = carvedTests.size();

        System.out.println("Carved targets " + carvedTargets + " / " + listOfTargetMethodsInvocations.size());

        // Put each test in a separate test case
        TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
        Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

        // Write test cases to files and try to compile them

        JUnitTestCaseWriter writer = new JUnitTestCaseWriter();
        List<CompilationUnit> generatedTests = new ArrayList<CompilationUnit>();
        for (TestClass testCase : testSuite) {
            try {
                CompilationUnit cu = writer.generateJUnitTestCase(testCase);
                logger.info(cu.toString());
                generatedTests.add(cu);
            } catch (Exception e) {
                logger.error("Cannot generate test " + testCase.getPackageName() + "." + testCase.getName(), e);
            }
        }

        System.out.println("Generated tests " + generatedTests.size() + "/" + carvedTargets);
    }

    @Test
    public void testDebugMocks() throws FileNotFoundException, IOException, ABCException {
        // Test whether we can understand an intent is a dep to a method call
//        <abc.basiccalculator.ResultActivity: void onCreate(android.os.Bundle)>_29_57
        File traceFile = new File(
                "./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        MethodInvocationSelector mis = new MethodInvocationSelector();
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findAllCarvableMethodInvocations(parsedTrace));

        Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
//      for(MethodInvocation targetMethod:listOfTargetMethodsInvocations){
//          targetMethodsInvocations.add(targetMethod);
//      }
        targetMethodsInvocations.addAll(listOfTargetMethodsInvocations);

        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
        List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<MethodInvocation>();
        targetMethodsInvocationsList.addAll(targetMethodsInvocations);
        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                parsedTrace);

        int carvedTargets = carvedTests.size();

        logger.info("Carved targets " + carvedTargets + " / " + listOfTargetMethodsInvocations.size());

        // Put each test in a separate test case
        TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
        Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

        // Write test cases to files and try to compile them

        JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }

    }

    @Test
    public void testCarveIntents() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(
                "./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        // Find the target method invocation
        String methodSignature = "<abc.basiccalculator.ResultActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 29;
        int invocationTraceId = 57;

        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();
        MethodInvocationMatcher matcher = MethodInvocationMatcher
                .fromMethodInvocation(new MethodInvocation(invocationTraceId, invocationCount, methodSignature));
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(
                mis.findByMethodInvocationMatcher(parsedTrace, matcher));

        logger.debug("Carvable targets ");
        for (MethodInvocation m : listOfTargetMethodsInvocations) {
            logger.debug("" + m);
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

        for (TestClass testCase : testSuite) {
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            logger.info(cu.toString());
        }
    }

    // public void testEmptyCallGraph() throws FileNotFoundException, IOException,
    // ABCException {
//		File traceFile = new File("./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt");
//		File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");
//
//		// Really needed?
//		ParsingUtils.setupSoot(ANDROID_JAR, apk_file);
//
//		TraceParser parser = new TraceParserImpl();
//		ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
//
//		// TODO How do we handle extensions like Android automatically?
//		ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
//		ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);
//
//		Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
//		final String targetSignature = "<abc.basiccalculator.ResultActivity: void checkResult(java.lang.String)>";
//
//		// TODO Expose some method to easily find the method calls that we need
//		parsedTrace.getUIThreadParsedTrace().getFirst().getOrderedMethodInvocations().stream()
//				.filter(methodInvocation -> targetSignature.equals(methodInvocation.getMethodSignature()))
//				.forEach(targetMethodsInvocations::add);
//
//		// One only for this test
//		Assume.assumeTrue(targetMethodsInvocations.size() == 1);
//
//		TestGenerator basicTestGenerator = new BasicTestGenerator();
//		Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations, parsedTrace);
//
//		// Group CarvedTests in classes
//		TestCaseOrganizer organizer = TestCaseOrganizers.byAllTestsTogether();
//		Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));
//
//		JUnitTestCaseWriter writer = new JUnitTestCaseWriter();
//
//		for (TestClass testCase : testSuite) {
//			CompilationUnit cu = writer.generateJUnitTestCase(testCase);
//			logger.info(cu);
//		}
//	}
}
