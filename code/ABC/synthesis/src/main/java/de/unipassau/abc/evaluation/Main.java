package de.unipassau.abc.evaluation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.BasicTestGenerator;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.shadowwriter.ShadowWriter;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.NameTestCaseBasedOnCarvedTest;
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

public class Main {

    // https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html
    // simple log 4j does not enable programmatic changes?
    // - Set to true if you want the current
    // date and time to be included in output messages. Default is false
//    org.slf4j.simpleLogger.dateTimeFormat - The date and time format to be used in the output messages. The pattern describing the date and time format is defined by SimpleDateFormat. If the format is not specified or is invalid, the number of milliseconds since start up will be output.

    // Must be invoke before any other call to Logger
    static {
        System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
    }

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    // ALESSIO: THIS IS PROBABLY MISPLACED? Why this should be a property of the
    // Main method?
    public static Map<Integer, String> idsInApk;

    public interface CLI {
        @Option(longName = "apk")
        public File getApk();

        @Option(longName = "trace-files")
        public List<File> getTraceFiles();

        @Option(longName = "output-to", defaultValue = "./carved-tests/")
        public File getOutputDir();

        @Option(longName = "android-jar")
        public File getAndroidJar();

        @Option(longName = "selection-strategy", defaultValue = "SELECT_ALL")
        public MethodInvocationSelector.StrategyEnum getSelectionStrategy();

        @Option(longName = "filter-method", defaultValue = "")
        public List<String> getFilterMethods();
        
        @Option(longName = "relaxed")
        public boolean getRelaxed();

    }

    public static void main(String[] args) throws IOException, ABCException {

        CLI cli = CliFactory.parseArguments(CLI.class, args);

        /*
         * Soot is a singleton and exposes static methods only, so we encapsulate its
         * configuration inside static method calls
         */
        ParsingUtils.setupSoot(cli.getAndroidJar(), cli.getApk());
        idsInApk = ParsingUtils.getIdsMap(cli.getApk());

        assert idsInApk != null;

        //
        String packageName = ParsingUtils.findApkPackageName(cli.getApk());
        boolean useOnlyOwnDependencies = (packageName != "");

        /*
         * This class is in charge of give a proper name to the test classes, not test
         * methods !
         */
        TestCaseNamer testClassNameBasedOnCarvedTest = new NameTestCaseBasedOnCarvedTest();

        // Collect some stats
        int totalTraces = 0;
        int totalParsedTraces = 0;
        int totalCarvableTargets = 0;
        int totalCarvedExecutions = 0;
        int totalGeneratedTests = 0;

        for (File traceFile : cli.getTraceFiles()) {
            // avoid processing unrelated files (based on extension)
            if (!traceFile.getAbsolutePath().endsWith(".txt")) {
                continue;
            }
            try {

                totalTraces = totalTraces + 1;

                TraceParser parser = new TraceParserImpl();
                ParsedTrace parsedTrace = parser.parseTrace(traceFile);

                // Decorate first android and then static
                ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
                parsedTrace = decorator.decorate(parsedTrace);

                ParsedTraceDecorator staticDecorator = new StaticParsedTraceDecorator(useOnlyOwnDependencies, packageName);
                parsedTrace = staticDecorator.decorate(parsedTrace);

                // Collect Stats - TODO Move to a separate class
                final Map<String, AtomicInteger> stats = new HashMap();
                final AtomicInteger total = new AtomicInteger(0);
                parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
                    ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
                    total.set(executionFlowGraph.getOrderedMethodInvocations().size());
                    executionFlowGraph.getOrderedMethodInvocations().forEach(mi -> {

                        if (!stats.containsKey(mi.getMethodSignature())) {
                            stats.put(mi.getMethodSignature(), new AtomicInteger(0));
                        }

                        stats.get(mi.getMethodSignature()).incrementAndGet();

                    });
                });

                // Print Stats
                logger.info("***********************************************************************");
                logger.info("Summary of the trace " + traceFile);
                logger.info("   contains the following " + total.get() + " invocations:");
                Object[] a = stats.entrySet().toArray();
                Arrays.sort(a, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        int o1Value = ((Map.Entry<String, AtomicInteger>) o1).getValue().intValue();
                        int o2Value = ((Map.Entry<String, AtomicInteger>) o2).getValue().intValue();
                        if (o1Value < o2Value) {
                            return -1;
                        } else if (o1Value == o2Value) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                for (int i = (a.length - 1); i >= 0; --i) {
                    logger.info("\t" + ((Map.Entry<String, AtomicInteger>) a[i]).getValue().intValue() + "\t"
                            + ((Map.Entry<String, Integer>) a[i]).getKey());
                }

                totalParsedTraces = totalParsedTraces + 1;

                MethodInvocationSelector mis = new MethodInvocationSelector();

                List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<>(
                        mis.findCarvableMethodInvocations(parsedTrace, cli.getSelectionStrategy()));
                targetMethodsInvocationsList.sort(Comparator.comparingInt(MethodInvocation::getInvocationTraceId));

                int selectedCarvableTargets = targetMethodsInvocationsList.size();

                if (selectedCarvableTargets == 0) {
                    logger.warn("** There are no targets carvable from trace file " + traceFile);
                    continue;
                } else {
                    logger.info("** Selected " + selectedCarvableTargets + " targets from trace file " + traceFile
                            + " using strategy " + cli.getSelectionStrategy());
                }

                totalCarvableTargets = totalCarvableTargets + selectedCarvableTargets;

                if (logger.isInfoEnabled()) {
                    targetMethodsInvocationsList.forEach(mi -> logger.info("** " + mi.toString()));
                }

                
                boolean relaxMode = cli.getRelaxed();
                logger.info("Use RELAXED mode");
                        
                BasicTestGenerator basicTestGenerator = new BasicTestGenerator(relaxMode);
                Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                        parsedTrace);

                int carvedTargets = carvedTests.size();

                logger.info("** Carved targets " + carvedTargets + " / " + selectedCarvableTargets);

                totalCarvedExecutions = totalCarvedExecutions + carvedTargets;

                // Put each test in a separate test case
                TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameBasedOnCarvedTest);
                Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

                // Write test cases to files and try to compile them
                JUnitTestCaseWriter writer = new JUnitTestCaseWriter();
                //
                writer.setFilterMethods(cli.getFilterMethods());

                Map<TestClass, File> generatedTests = new HashMap<TestClass, File>();

                File sourceFolder = cli.getOutputDir();

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

                // This create names like test001, test002, etc.
                // TestMethodNamer testMethodNamer = new LocalCounterNamer();
                //
                TestMethodNamer testMethodNamer = new MethodUnderTestWithLocalCounterNamer();

                // generate unit tests
                for (TestClass testCase : sortedTestSuiteList) {
                    try {
                        CompilationUnit cu = writer.generateJUnitTestCase(testCase, testMethodNamer);
                        File testFileFolder = new File(sourceFolder,
                                testCase.getPackageName().replaceAll("\\.", File.separator));
                        testFileFolder.mkdirs();
                        File testFile = new File(testFileFolder, testCase.getName() + ".java");
                        try (PrintStream out = new PrintStream(new FileOutputStream(testFile))) {
                            out.print(cu.toString());
                        }
                        generatedTests.put(testCase, testFile);

                    } catch (NotImplementedException e) {
                        logger.error("** Cannot generate test " + testCase.getName() + ":" + e.getMessage());
                    } catch (Throwable e) {
                        logger.error("** Cannot generate test " + testCase.getName());
                        e.printStackTrace();
                    }
                }

                logger.info("** Generated tests " + generatedTests.size() + " / " + carvedTargets);

                totalGeneratedTests = totalGeneratedTests + generatedTests.size();

                // TODO How does this work when we need to process multiple traces?
                // Shouldn't this be done inside the test generation?
                logger.info("** Generating shadows");
                // generate shadows needed for test cases
                ShadowWriter shadowWriter = new ShadowWriter();
                shadowWriter.generateAndWriteShadows(sortedTestSuiteList, sourceFolder);

            } catch (Exception e) {
                logger.error("** Error while processing trace " + traceFile);
                e.printStackTrace();
            }

        }

        // Output some statistics
        StringBuffer stats = new StringBuffer("\n");
        stats.append("Input traces: ").append(totalTraces).append("\n");
        stats.append("Parsed traces: ").append(totalParsedTraces).append("\n");
        stats.append("Carvable Targets: ").append(totalCarvableTargets).append("\n");
        stats.append("Carved Executions: ").append(totalCarvedExecutions).append("\n");
        stats.append("Generated Tests: ").append(totalGeneratedTests).append("\n");
        ///
        logger.info(stats.toString());
    }
}
