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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.CompilationUnit;

import de.unipassau.abc.BaseDebuggingTest;
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

public class MySteamGamesTest extends BaseDebuggingTest {

    @BeforeClass
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(MySteamGamesTest.class);
    }

    @Before
    public void setup() {
        this.traceFolder = "/Users/gambi/action-based-test-carving/apps-src/MySteamGames/traces/";
    }

    // Generated from
    // /Users/gambi/action-based-test-carving/apps-src/MySteamGames/traces/com.joffreylagut.mysteamgames.mysteamgames.ui.HomeFragmentTests#displayFavorites_noFavorites/Trace-1649771983930.txt
//    * Method invocation under test: <com.joffreylagut.mysteamgames.mysteamgames.ui.MainActivity: void onCreate(android.os.Bundle)>_8_16
    // * Generated from
    // /Users/gambi/action-based-test-carving/apps-src/MySteamGames/traces/com.joffreylagut.mysteamgames.mysteamgames.ui.HomeFragmentTests#displayFavorites_noFavorites/Trace-1649771983930.txt
//    * Method invocation under test: <com.joffreylagut.mysteamgames.mysteamgames.ui.MainActivity: void onPageScrolled(int,float,int)>_364_728

    @Test
    public void testDoNotMockIntents() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom(
                "com.joffreylagut.mysteamgames.mysteamgames.ui.HomeFragmentTests#displayFavorites_noFavorites");
        //
        String methodSignature = "<com.joffreylagut.mysteamgames.mysteamgames.ui.MainActivity: void onCreate(android.os.Bundle)>";
        int invocationCount = 8;
        int invocationTraceId = 16;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

    /**
     * We do not treat floats as such (i.e., missing "f" after them or a cast double
     * to float)
     * 
     * Solved by ensuring toString add the missing "f"
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test
    @Ignore
    public void testFloatNotDouble() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom(
                "com.joffreylagut.mysteamgames.mysteamgames.ui.HomeFragmentTests#displayFavorites_noFavorites");
        //
        String methodSignature = "<com.joffreylagut.mysteamgames.mysteamgames.ui.MainActivity: void onPageScrolled(int,float,int)>";
        int invocationCount = 364;
        int invocationTraceId = 728;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }

    // This is fixed now
    @Test
    @Ignore
    public void testClassAsPrimitiveType() throws FileNotFoundException, IOException, ABCException {
        traceFolder = "src/test/resources/com.joffreylagut.mysteamgames.mysteamgames";
        file = "Trace-1641930295651";

        String methodSignature = "<com.prismaqf.callblocker.PickActionFragment: void onStart()>";
        int invocationCount = 357;
        int invocationTraceId = 714;

        runTheTest(methodSignature, invocationCount, invocationTraceId);
    }
}
