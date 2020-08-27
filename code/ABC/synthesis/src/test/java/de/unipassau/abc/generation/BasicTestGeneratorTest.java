package de.unipassau.abc.generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assume;
import org.junit.Test;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.testwriters.JUnitTestCaseWriter;
import de.unipassau.abc.generation.utils.TestCase;
import de.unipassau.abc.generation.utils.TestCaseOrganizer;
import de.unipassau.abc.generation.utils.TestCaseOrganizers;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;

public class BasicTestGeneratorTest {

  private final static File ANDROID_JAR = new File(
      "/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

  @Test
  public void testEndToEnd() throws FileNotFoundException, IOException, ABCException {
    File traceFile = new File(
        "./src/test/resources/abc.basiccalculator/testNullPointerThrownBySystem-trace.txt");
    File apk_file = new File("./src/test/resources/abc.basiccalculator/app-debug.apk");

    // Really needed?
    ParsingUtils.setupSoot(ANDROID_JAR, apk_file);

    TraceParser parser = new TraceParserImpl();
    ParsedTrace _parsedTrace = parser.parseTrace(traceFile);

    // TODO How do we handle extensions like Android automatically?
    ParsedTraceDecorator decorator = new AndroidParsedTraceDecorator();
    ParsedTrace parsedTrace = decorator.decorate(_parsedTrace);

    Set<MethodInvocation> targetMethodsInvocations = new HashSet<MethodInvocation>();
    final String targetSignature = "<abc.basiccalculator.MainActivity: void sendResult(android.view.View)>";

    // TODO Expose some method to easily find the method calls that we need
    parsedTrace.getUIThreadParsedTrace().getFirst().getOrderedMethodInvocations().stream()
        .filter(methodInvocation -> targetSignature.equals(methodInvocation.getMethodSignature()))
        .forEach(targetMethodsInvocations::add);

    // One only for this test
    Assume.assumeTrue(targetMethodsInvocations.size() == 1);

    TestGenerator basicTestGenerator = new BasicTestGenerator();
    Collection<CarvedTest> carvedTests = basicTestGenerator
        .generateTests(targetMethodsInvocations, parsedTrace);

    // Group CarvedTests in classes
    TestCaseOrganizer organizer = TestCaseOrganizers.byAllTestsTogether();
    Set<TestCase> testSuite = organizer.organize(carvedTests.toArray(new CarvedTest[]{}));

    JUnitTestCaseWriter writer = new JUnitTestCaseWriter();

    for (TestCase testCase : testSuite) {
      CompilationUnit cu = writer.generateJUnitTestCase(testCase);
      System.out.println(cu);
    }
  }
}
