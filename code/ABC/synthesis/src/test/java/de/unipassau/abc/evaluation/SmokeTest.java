package de.unipassau.abc.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xmlpull.v1.XmlPullParserException;

import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.BasicTestGenerator;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.utils.NameTestCaseGlobally;
import de.unipassau.abc.generation.utils.TestCaseNamer;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsingUtils;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.parsing.postprocessing.AndroidParsedTraceDecorator;
import de.unipassau.abc.parsing.postprocessing.ParsedTraceDecorator;

public class SmokeTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Ignore // This has hardcoded paths
    @Test
    public void testSetupSoot() throws IOException, XmlPullParserException, ABCException {
        File theAPK = new File("/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app-original.apk");
        File androidJAR = new File("src/test/resources/android-28.jar");
        String[] args = new String[] { "--apk", theAPK.getAbsolutePath(), //
                "--android-jar", androidJAR.getAbsolutePath(), //
                "--output-to", temporaryFolder.newFolder().getAbsolutePath() };

        de.unipassau.abc.instrumentation.Main.main(args);

    }

    @Ignore // This has hardcoded paths
    @Test
    public void testFixNPE() throws FileNotFoundException, IOException, ABCException {
        File traceFile = new File(
                "/Users/gambi/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoTest#checkManualBackButtonNavigatesToSignBrowser/Trace-1631534738033.txt");
        File theAPK = new File(
                "/Users/gambi/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/app-original.apk");
        File androidJAR = new File("src/test/resources/android-28.jar");

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
        File androidJAR = new File("src/test/resources/android-28.jar");

        ParsingUtils.setupSoot(androidJAR, theAPK);

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
