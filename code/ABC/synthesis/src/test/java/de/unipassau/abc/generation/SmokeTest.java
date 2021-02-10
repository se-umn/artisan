package de.unipassau.abc.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.carving.utils.MethodInvocationSearcher;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.NameTestCaseGlobally;
import de.unipassau.abc.generation.utils.TestCaseNamer;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestCaseOrganizers;
import de.unipassau.abc.generation.utils.TestClass;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;
import edu.emory.mathcs.backport.java.util.Collections;

public class SmokeTest {

    /*
     * trace
     * traces/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt
     */

    @Test
    public void mainTest() throws IOException, ABCException {
        String[] args = {

                "--trace-files", //
                "./src/test/resources/abc.basiccalculator/Trace-testCalculateAndIncrementByOneWithLogging-1603454200461.txt", //
                "--android-jar", //
                "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar", //
                "--apk", //
                "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app/build/outputs/apk/debug/app-debug.apk" };

        Main.main(args);

    }

    @Test
    public void testBrokenTestsAftertGeneratingMocks() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(
                "./src/test/resources/abc.basiccalculator/Trace-testNullPointerThrownBySystem-1612085878869.txt");
        TestCaseNamer testClassNameUsingGlobalId = new NameTestCaseGlobally();

        TraceParser parser = new TraceParserImpl();
        ParsedTrace _parsedTrace = parser.parseTrace(traceFile);
        ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
        ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

        MethodInvocationSearcher mis = new MethodInvocationSearcher();
        List<MethodInvocation> listOfTargetMethodsInvocations = new ArrayList(mis.findAllCarvableMethodInvocations(parsedTrace));
        Collections.sort( listOfTargetMethodsInvocations );
        
        int allCarvableTargets = listOfTargetMethodsInvocations.size();

        System.out.println("Carvable targets ");
        listOfTargetMethodsInvocations.forEach(System.out::println);
        
        // SELECT THE ACTUAL carved execution to considel
        Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(0));
        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(1));
        //
        //
        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(2));
        targetMethodsInvocations.add( listOfTargetMethodsInvocations.get(3));
                

        BasicTestGenerator basicTestGenerator = new BasicTestGenerator();
        Collection<CarvedTest> carvedTests = basicTestGenerator.generateTests(targetMethodsInvocations, parsedTrace);

        int carvedTargets = carvedTests.size();

        System.out.println("Carved targets " + carvedTargets + " / " + allCarvableTargets);

        // Put each test in a separate test case
        TestCaseOrganizer organizer = TestCaseOrganizers.byEachTestAlone(testClassNameUsingGlobalId);
        Set<TestClass> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[] {}));

        // Write test cases to files and try to compile them
    
        JUnitTestCaseWriter writer = new JUnitTestCaseWriter();
        
        for (TestClass testCase : testSuite) {    
            CompilationUnit cu = writer.generateJUnitTestCase(testCase);
            try (PrintStream out = System.out) {
                out.print(cu.toString());
            }
        }
    }
}