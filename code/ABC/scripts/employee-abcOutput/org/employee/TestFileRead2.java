package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileRead2
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_51() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileRead2 fileRead2 = new FileRead2(file);
    }
    
    @Test
    public void test_56() throws Exception {
        this.userInputs.provideLines(new String[] { "2", "123" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        new FileRead2(file).fileIsRead("username.txt", scanner.next());
    }
    
    public TestFileRead2() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
