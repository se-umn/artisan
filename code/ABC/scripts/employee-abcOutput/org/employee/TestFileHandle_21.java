package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileHandle_21
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_61() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        final String next = scanner.next();
        final FileWriter fileWriter = new FileWriter(new File(file, "username.txt"), false);
        final FileWriter fileWriter2 = new FileWriter(new File(file, next), false);
        final FileWriter fileWriter3 = new FileWriter(new File(file, next), false);
        final FileWriter fileWriter4 = new FileWriter(new File(file, next), false);
        final FileWriter fileWriter5 = new FileWriter(new File(file, next), false);
        final FileWriter fileWriter6 = new FileWriter(new File(file, next), false);
        final FileWriter fileWriter7 = new FileWriter(new File(file, next), false);
        final FileHandle fileHandle = new FileHandle(file);
        fileHandle.addInFile(next, Integer.toString(20));
        fileHandle.addInFile(next, Integer.toString(20));
    }
    
    public TestFileHandle_21() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
