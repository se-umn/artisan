package coverage;

import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    public void testTODO() throws IOException {
        //TODO
    }

}
