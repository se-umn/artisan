package de.unipassau.abc.evaluation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
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
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

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

    }

    // NOTE: If a class has inner classes more than one file will be generated by
    // the compiler
    @Deprecated
    private static Collection<File> compileTest(File sourceFolder, File testFile, String theClassPath)
            throws IOException {
        // https://www.logicbig.com/tutorials/core-java-tutorial/java-se-compiler-api/compiler-api-string-source.html
        // https://stackoverflow.com/questions/1563909/how-to-set-classpath-when-i-use-javax-tools-javacompiler-compile-the-source
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        Collection<File> compiledTests = new ArrayList<File>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(testFile.getAbsolutePath()));

            List<String> optionList = new ArrayList<String>();

            // Include test-utils on the compilation classpath because we need the Carved
            // annotation
            for (String cpEntry : System.getProperty("java.class.path").split(File.pathSeparator)) {
                if (cpEntry.contains("test-utils")) {
                    theClassPath += File.pathSeparator + cpEntry;
                }
            }

            optionList.addAll(Arrays.asList("-cp", theClassPath));
            optionList.addAll(Arrays.asList("-d", sourceFolder.getAbsolutePath()));

            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null,
                    compilationUnits);

            if (!task.call()) {
                diagnostics.getDiagnostics().forEach(System.out::println);
            } else {
                for (JavaFileObject jfo : fileManager.list(StandardLocation.CLASS_OUTPUT, "",
                        Collections.singleton(JavaFileObject.Kind.CLASS), true)) {
                    System.out.println("Compiled :" + jfo.getName());
                    compiledTests.add(new File(jfo.getName()));
                }
            }
        }

        return compiledTests;
    }

    public static void main(String[] args) throws IOException, ABCException {

        CLI cli = CliFactory.parseArguments(CLI.class, args);

        /*
         * Soot is a singleton and exposes static methods only, so we encapsulate its
         * configuration inside static method calls
         */
        ParsingUtils.setupSoot(cli.getAndroidJar(), cli.getApk());
        idsInApk = ParsingUtils.getIdsMap(cli.getApk());

        TestCaseNamer testClassNameBasedOnCarvedTest = new NameTestCaseBasedOnCarvedTest();

        // Collect some stats
        int totalTraces = 0;
        int totalParsedTraces = 0;
        int totalCarvableTargets = 0;
        int totalCarvedTests = 0;
        int totalGeneratedTests = 0;

        for (File traceFile : cli.getTraceFiles()) {
            // avoid processing unrelated files (based on extension)
            if (!traceFile.getAbsolutePath().endsWith(".txt")) {
                continue;
            }
            try {

                totalTraces = totalTraces + 1;

                TraceParser parser = new TraceParserImpl();
                ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
                ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
                ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

                totalParsedTraces = totalParsedTraces + 1;

                MethodInvocationSelector mis = new MethodInvocationSelector();

                Set<MethodInvocation> targetMethodsInvocations = null;
                switch (cli.getSelectionStrategy()) {
                case SELECT_ONE:
                    targetMethodsInvocations = mis.findUniqueCarvableMethodInvocations(parsedTrace);
                    break;
                case SELECT_ALL:
                default:
                    targetMethodsInvocations = mis.findAllCarvableMethodInvocations(parsedTrace);
                    break;
                }

                int selectedCarvableTargets = targetMethodsInvocations.size();

                logger.info("Selected " + selectedCarvableTargets + " targets from trace file " + traceFile);
                
                totalCarvableTargets = totalCarvableTargets + selectedCarvableTargets;

                List<MethodInvocation> targetMethodsInvocationsList = new ArrayList<MethodInvocation>();
                targetMethodsInvocationsList.addAll(targetMethodsInvocations);
                Collections.sort(targetMethodsInvocationsList, new Comparator<MethodInvocation>() {
                    @Override
                    public int compare(MethodInvocation o1, MethodInvocation o2) {
                        if (o1.getInvocationTraceId() < o2.getInvocationTraceId()) {
                            return -1;
                        } else if (o1.getInvocationTraceId() == o2.getInvocationTraceId()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                for (MethodInvocation mi : targetMethodsInvocationsList) {
                    logger.info(mi.toString());
                }

                BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
                Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocationsList,
                        parsedTrace);

                int carvedTargets = carvedTests.size();

                logger.info("Carved targets " + carvedTargets + " / " + selectedCarvableTargets);
                
                totalCarvedTests = totalCarvedTests + carvedTargets;
                
                // Put each test in a separate test case
                TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameBasedOnCarvedTest);
                Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

                // Write test cases to files and try to compile them
                JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

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

                // generate unit tests
                for (TestClass testCase : sortedTestSuiteList) {
                    try {
                        CompilationUnit cu = writer.generateJUnitTestCase(testCase);
                        File testFileFolder = new File(sourceFolder,
                                testCase.getPackageName().replaceAll("\\.", File.separator));
                        testFileFolder.mkdirs();
                        File testFile = new File(testFileFolder, testCase.getName() + ".java");
                        try (PrintStream out = new PrintStream(new FileOutputStream(testFile))) {
                            out.print(cu.toString());
                        }
                        generatedTests.put(testCase, testFile);

                    } catch (Exception e) {
                        System.err.println("Cannot generate test " + testCase.getName());
                        e.printStackTrace();
                    }
                }

                logger.info("Generated tests " + generatedTests.size() + " / " + carvedTargets);

                totalGeneratedTests = totalGeneratedTests + generatedTests.size();
                
                // TODO How does this work when we need to process multiple traces?
                logger.info("Generating shadows");
                // generate shadows needed for test cases
                ShadowWriter shadowWriter = new ShadowWriter();
                shadowWriter.generateAndWriteShadows(sortedTestSuiteList, sourceFolder);

            } catch (Exception e) {
                System.err.println("Error while processing trace " + traceFile);
                e.printStackTrace();
            }

        }

        // Output some statistics
        StringBuffer stats = new StringBuffer();
        stats.append("Input traces: ").append( totalTraces ).append("\n");
        stats.append("Parsed traces: ").append( totalParsedTraces ).append("\n");
        stats.append("Carvable Targets: ").append( totalCarvableTargets).append("\n");
        stats.append("Carved Tests: ").append( totalCarvedTests).append("\n");
        stats.append("Generated Tests: ").append( totalGeneratedTests).append("\n");
        System.out.println(stats);
    }
}
