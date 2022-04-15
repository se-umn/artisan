package coverage;

import com.lexicalscope.jewel.cli.CliFactory;
import de.unipassau.abc.Coverage;
import de.unipassau.abc.ExecDump;
import de.unipassau.abc.ExecDump.CLI;
import de.unipassau.abc.IWriter;
import de.unipassau.abc.IWriter.TextWriter;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.TraceParser;
import de.unipassau.abc.parsing.TraceParserImpl;
import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.event.Level;

public class SmokeTest {

    @Rule
    public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.WARN);

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testBasicCalculator() throws IOException {
        File tempFile = folder.newFile("exec-coverage.csv");
        String[] args = { "-e",
                "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/espresso-test-coverage-for-abc.basiccalculator.ExtendedMainActivityTest__testCalculateAndReturnBackToMain/abc.basiccalculator.ExtendedMainActivityTest#testCalculateAndReturnBackToMain.ec",
                "-f", tempFile.getAbsolutePath(), "-c",
                "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app/build/intermediates/javac/debug/classes",
                "-n" };
        ExecDump.main(args);

        try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
    
    @Test
    public void testCoverage() throws IOException {
        File tempFile = folder.newFile("exec-coverage.csv");
        String[] args = { "-e",
                "src/test/resources/coverage.ec",
                "-f", tempFile.getAbsolutePath(), "-c",
                "/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app/build/intermediates/javac/debug/classes",
                "-n" };
        ExecDump.main(args);

        try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void testPrismaCallblockerExecDump() throws IOException {
        File tempFile = folder.newFile("exec-coverage.csv");
        String[] args = { "-e",
              "/Volumes/TOSHIBA/carving/apps-src/bird-monitor/jacoco-espresso-coverage/nz.org.cacophony.birdmonitor.RegisterPhoneTest#unRegisterPhoneTest.ec",
              "-f", tempFile.getAbsolutePath(), "-c",
                "/Volumes/TOSHIBA/carving/apps-src/bird-monitor/app/build/intermediates/javac/debug/classes",
              "-n"
                 };
        ExecDump.main(args);

        try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}
