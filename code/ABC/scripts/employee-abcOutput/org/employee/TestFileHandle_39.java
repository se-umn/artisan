package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileHandle_39
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_89() throws Exception {
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
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        fileHandle.addInFile("username.txt", next3);
        fileHandle.addInFile(next3, "Username:-->" + next3);
        fileHandle.addInFile(next3, "Name:-->" + next);
        fileHandle.addInFile(next3, "Designnation:-->Software Engineer");
        fileHandle.addInFile(next3, "Gender-->" + next2);
        fileHandle.addInFile(next3, "Age:-->" + Integer.toString(24));
        fileHandle.addInFile(next3, "Experience-->" + Integer.toString(5));
    }
    
    public TestFileHandle_39() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
