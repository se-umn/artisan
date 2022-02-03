package de.unipassau.abc.instrumentation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xmlpull.v1.XmlPullParserException;

public class MainTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testMain() throws IOException, XmlPullParserException {

        File outputDIR = temporaryFolder.newFolder();

        File theAPK = new File("/Users/gambi/action-based-test-carving/apps-src/BasicCalculator/app-original.apk");
        File androidJAR = new File("src/test/resources/android-28.jar");
        String[] args = new String[] { "--apk", theAPK.getAbsolutePath(), //
                "--android-jar", androidJAR.getAbsolutePath(), //
                "--output-to", outputDIR.getAbsolutePath(),
                //
//                "--filter-package", "java.io", //
                "--filter-class", "java.io.PrintWriter", //
                "--filter-method", "java.util.ArrayList.get", //
                "--filter-method", "androidx.loader.content.Loader.isReset", //
                "--skip-package", "org.junit"//
        };

//        System.setProperty("abc.taint.android.intents", "true");
        System.setProperty("abc.instrument.fields.operations", "true");
        System.setProperty("abc.instrument.debug", "false");
        System.setProperty("abc.instrument.multithreaded", "false");
        System.setProperty("abc.output.instrumented.code", "true");

        Main m = new Main();
        m.main(args);
    }

    @Test
    public void testPrismaCaller() throws IOException, XmlPullParserException {

        File outputDIR = temporaryFolder.newFolder();

        File theAPK = new File("/Users/gambi/action-based-test-carving/apps-src/PrismaCallBlocker/app-original.apk");
        File androidJAR = new File("src/test/resources/android-28.jar");
        String[] args = new String[] { "--apk", theAPK.getAbsolutePath(), //
                "--android-jar", androidJAR.getAbsolutePath(), //
                "--output-to", outputDIR.getAbsolutePath() };

//        System.setProperty("abc.taint.android.intents", "true");
//        System.setProperty("abc.instrument.fields.operations", "true");
//        System.setProperty("abc.instrument.debug", "true");
//        System.setProperty("abc.instrument.multithreaded", "true");
        System.setProperty("abc.instrument.array.operations", "true");
        System.setProperty("abc.output.instrumented.code", "true");

        Main m = new Main();
        m.main(args);
    }
}
