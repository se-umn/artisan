package org.employee;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import org.junit.Test;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileRead
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_57() throws Exception {
        this.userInputs.provideLines(new String[] { "2", "123" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        final FileInputStream fileInputStream = new FileInputStream(new File(file, "username.txt"));
        final FileRead fileRead = new FileRead(file);
    }
    
    @Test
    public void test_58() throws Exception {
        this.userInputs.provideLines(new String[] { "2", "123" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final String next = scanner.next();
        final FileInputStream fileInputStream = new FileInputStream(new File(file, "username.txt"));
        new FileRead(file).fileIsRead(next);
    }
    
    @Test
    public void test_113() throws Exception {
        this.userInputs.provideLines(new String[] { "1", "admin" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        final FileInputStream fileInputStream = new FileInputStream(new File(file, "username.txt"));
        final FileRead fileRead = new FileRead(file);
    }
    
    @Test
    public void test_114() throws Exception {
        this.userInputs.provideLines(new String[] { "1", "admin" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new FileRead(file).fileIsRead(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(file, "username.txt"))))).readLine());
    }
    
    public TestFileRead() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
