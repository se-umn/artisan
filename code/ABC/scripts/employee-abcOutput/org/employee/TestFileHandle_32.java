package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileHandle_32
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_81() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        final String next = scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        final String next2 = scanner.next();
        fileHandle.addInFile("username.txt", next2);
        fileHandle.addInFile(next2, "Username:-->" + next2);
        fileHandle.addInFile(next2, "Name:-->" + next);
        fileHandle.addInFile(next2, "Designnation:-->Software Engineer");
    }
    
    public TestFileHandle_32() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
