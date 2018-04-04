package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileRead3_1
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_2() throws Exception {
        this.userInputs.provideLines(new String[] { "1", "admin" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        final FileRead3 fileRead3 = new FileRead3(file);
    }
    
    public TestFileRead3_1() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}