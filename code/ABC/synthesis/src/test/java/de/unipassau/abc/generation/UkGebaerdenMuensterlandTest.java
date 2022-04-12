
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
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.carving.utils.MethodInvocationSelector;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.evaluation.Main;
import de.unipassau.abc.evaluation.MethodUnderTestWithLocalCounterNamer;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.generation.data.CarvedTest;
import de.unipassau.abc.generation.simplifiers.RobolectricActivitySimplifier;
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

public class UkGebaerdenMuensterlandTest extends BaseDebuggingTest {

    @BeforeClass
    public static void setupLogger() {
        logger = LoggerFactory.getLogger(UkGebaerdenMuensterlandTest.class);
    }
    
    @Before
    public void setup() {
        this.traceFolder = "/Users/gambi/action-based-test-carving/apps-src/UK-Gebaerden_Muensterland/traces/";
    }

    /**
     * Errors: Broken test: error: cannot find symbol
     * java.lang.Object object6 = org.mockito.Mockito.mock(android.widget.RelativeLayout$LayoutParams.class);
     * 
     * In general, the carver brings in tons of stuff that we might not need to test onBackPressed()
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test
    public void testBrokenTest() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom("de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.SignSearchTest#checkNavigatingToVideosAndReturningViaBackButtonWorks");

        String methodSignature = "<de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.search.video.SignSearchVideoActivity: void onBackPressed()>";
        int invocationCount = 551;
        int invocationTraceId = 1102;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }
    
    
    
    /**
     * Errors: This test brings in too many stuff, especially for mocking objects and then shadow fails with NPE
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test @Ignore
    public void testNPE() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom("de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoTest#checkManualBackButtonNavigatesToSignBrowser");

        String methodSignature = "<de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.SignBrowserAdapter: int getItemCount()>";
        int invocationCount = 6271;
        int invocationTraceId = 12542;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }
    
    
    /**
     * 44342 [main] ERROR de.unipassau.abc.evaluation.Main - ** Cannot generate test Test043
     *  java.lang.ArrayIndexOutOfBoundsException: -1
     *      at de.unipassau.abc.generation.utils.TypeUtils.lambda$getFormalTypeFor$2(TypeUtils.java:117)
     *      
     * One instance of this problem is caused by ENUMS that create static deps when our Monitor class calls values/clone.
     *  
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ABCException
     */
    @Test
    
    public void theTest() throws FileNotFoundException, IOException, ABCException {
        file = this.getTraceFileFrom("de.lebenshilfe_muenster.uk_gebaerden_muensterland.sign_browser.video.SignVideoTest#checkManualBackButtonNavigatesToSignBrowserInLandscapeOrientation");

        String methodSignature = "<de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign: java.lang.String getMnemonic()>";
        int invocationCount = 7234;
        int invocationTraceId = 14467;

        runTheTest(methodSignature, invocationCount, invocationTraceId);

    }
    
    
}
