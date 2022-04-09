package de.unipassau.abc;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.evaluation.MethodUnderTestWithLocalCounterNamer;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.BasicTestGenerator;
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

public abstract class BaseDebuggingTest {

    public String traceFolder = null;
    public String file = null;
    public static Logger logger = null;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    public String getTraceFileFrom(String testDirectoryName) {
        // /Trace-1642241004125.txt
        File testDirectory = new File(traceFolder, testDirectoryName);
        return testDirectoryName + "/" + testDirectory.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith("Trace") && pathname.getName().endsWith(".txt");
            }
        })[0].getName();

    }

    public void checkAssumptions(ExecutionFlowGraph efg, CallGraph cg) {
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
    public Set<TestClass> runTheTest(String methodSignature, int invocationCount, int invocationTraceId)
            throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(traceFolder, file);

        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        // TODO Is is not going to work, since the IDs are regenerated every time...
        File theAPK = new File(new File(traceFolder).getParent(), "app-original.apk");
        Main.idsInApk = ParsingUtils.getIdsMap(theAPK);

        // Ensure we use the actual method invocation, not a shallow copy of it!
        MethodInvocationSelector mis = new MethodInvocationSelector();

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

        checkAssumptions(_parsedTrace.getUIThreadParsedTrace().getFirst(),
                _parsedTrace.getUIThreadParsedTrace().getThird());

        List<ParsedTraceDecorator> decorators = new ArrayList<ParsedTraceDecorator>();
        decorators.add( new AndroidParsedTraceDecorator());
        
        
        String packageName = ParsingUtils.findApkPackageName(theAPK);
        boolean useOnlyOwnDependencies = (packageName != "");
        
        decorators.add( new StaticParsedTraceDecorator(useOnlyOwnDependencies, packageName));
        
        // Decorate the trace
        for( ParsedTraceDecorator decorator : decorators ) {
            _parsedTrace = decorator.decorate(_parsedTrace); 
        }
        
        ParsedTrace parsedTrace = _parsedTrace;
        
        checkAssumptions(parsedTrace.getUIThreadParsedTrace().getFirst(),
                parsedTrace.getUIThreadParsedTrace().getThird());
        
        Set<MethodInvocation> all = mis.findCarvableMethodInvocations(parsedTrace);
        logger.debug("-- All possible carvable targets from " + traceFile);

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
        
        return testSuite;
    }
}
