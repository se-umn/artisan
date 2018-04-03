package org.employee;

import java.io.FileWriter;
import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestFileHandle
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_21() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileHandle fileHandle = new FileHandle(file);
    }
    
    @Test
    public void test_32() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        fileHandle.addInFile("username.txt", scanner.next());
    }
    
    @Test
    public void test_33() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        final String next = scanner.next();
        fileHandle.addInFile("username.txt", next);
        fileHandle.addInFile(next, "Username:-->" + next);
    }
    
    @Test
    public void test_34() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
    }
    
    @Test
    public void test_35() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
    }
    
    @Test
    public void test_36() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next2, "Designnation:-->SeniorSoftwareEngineer Software Engineer");
    }
    
    @Test
    public void test_38() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        new FileHandle(file).addInFile(next, Integer.toString(30));
    }
    
    @Test
    public void test_39() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next, Integer.toString(30));
        fileHandle.addInFile(next, Integer.toString(20));
    }
    
    @Test
    public void test_40() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next, Integer.toString(30));
        fileHandle.addInFile(next, Integer.toString(20));
        fileHandle.addInFile(next, Integer.toString(10));
    }
    
    @Test
    public void test_41() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next3, "Designnation:-->SeniorSoftwareEngineer Software Engineer");
        fileHandle.addInFile(next3, "Gender-->" + next2);
    }
    
    @Test
    public void test_42() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next, Integer.toString(30));
        fileHandle.addInFile(next, Integer.toString(20));
        fileHandle.addInFile(next, Integer.toString(10));
        fileHandle.addInFile(next, Float.toString(96000.0f));
    }
    
    @Test
    public void test_43() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next3, "Designnation:-->SeniorSoftwareEngineer Software Engineer");
        fileHandle.addInFile(next3, "Gender-->" + next2);
        fileHandle.addInFile(next3, "Age:-->" + Integer.toString(50));
    }
    
    @Test
    public void test_44() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
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
        fileHandle.addInFile(next3, "Designnation:-->SeniorSoftwareEngineer Software Engineer");
        fileHandle.addInFile(next3, "Gender-->" + next2);
        fileHandle.addInFile(next3, "Age:-->" + Integer.toString(50));
        fileHandle.addInFile(next3, "Experience-->" + Integer.toString(10));
    }
    
    @Test
    public void test_64() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileHandle fileHandle = new FileHandle(file);
    }
    
    @Test
    public void test_77() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        fileHandle.addInFile("username.txt", scanner.next());
    }
    
    @Test
    public void test_78() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        final String next = scanner.next();
        fileHandle.addInFile("username.txt", next);
        fileHandle.addInFile(next, "Username:-->" + next);
    }
    
    @Test
    public void test_79() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
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
    }
    
    @Test
    public void test_80() throws Exception {
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
    }
    
    @Test
    public void test_82() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
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
        fileHandle.addInFile(next2, "Designnation:-->Software Trainee");
    }
    
    @Test
    public void test_84() throws Exception {
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
        new FileHandle(file).addInFile(next, Integer.toString(20));
    }
    
    @Test
    public void test_85() throws Exception {
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
    
    @Test
    public void test_86() throws Exception {
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
        fileHandle.addInFile(next, Integer.toString(10));
    }
    
    @Test
    public void test_87() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
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
        fileHandle.addInFile(next3, "Designnation:-->Software Trainee");
        fileHandle.addInFile(next3, "Gender-->" + next2);
    }
    
    @Test
    public void test_88() throws Exception {
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
        fileHandle.addInFile(next, Integer.toString(10));
        fileHandle.addInFile(next, Float.toString(45000.0f));
    }
    
    @Test
    public void test_89() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
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
        fileHandle.addInFile(next3, "Designnation:-->Software Trainee");
        fileHandle.addInFile(next3, "Gender-->" + next2);
        fileHandle.addInFile(next3, "Age:-->" + Integer.toString(19));
    }
    
    @Test
    public void test_90() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
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
        fileHandle.addInFile(next3, "Designnation:-->Software Trainee");
        fileHandle.addInFile(next3, "Gender-->" + next2);
        fileHandle.addInFile(next3, "Age:-->" + Integer.toString(19));
        fileHandle.addInFile(next3, "Experience-->" + Integer.toString(0));
    }
    
    @Test
    public void test_120() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileHandle fileHandle = new FileHandle(file);
    }
    
    @Test
    public void test_134() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        fileHandle.addInFile("username.txt", scanner.next());
    }
    
    @Test
    public void test_135() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final FileHandle fileHandle = new FileHandle(file);
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        scanner.next();
        final String next = scanner.next();
        fileHandle.addInFile("username.txt", next);
        fileHandle.addInFile(next, "Username:-->" + next);
    }
    
    @Test
    public void test_136() throws Exception {
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
    }
    
    @Test
    public void test_137() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
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
    }
    
    @Test
    public void test_138() throws Exception {
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
    
    @Test
    public void test_140() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
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
        new FileHandle(file).addInFile(next, Integer.toString(25));
    }
    
    @Test
    public void test_141() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
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
        fileHandle.addInFile(next, Integer.toString(25));
        fileHandle.addInFile(next, Integer.toString(20));
    }
    
    @Test
    public void test_142() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
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
        fileHandle.addInFile(next, Integer.toString(25));
        fileHandle.addInFile(next, Integer.toString(20));
        fileHandle.addInFile(next, Integer.toString(10));
    }
    
    @Test
    public void test_143() throws Exception {
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
    }
    
    @Test
    public void test_144() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
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
        fileHandle.addInFile(next, Integer.toString(25));
        fileHandle.addInFile(next, Integer.toString(20));
        fileHandle.addInFile(next, Integer.toString(10));
        fileHandle.addInFile(next, Float.toString(62000.0f));
    }
    
    @Test
    public void test_145() throws Exception {
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
    }
    
    @Test
    public void test_146() throws Exception {
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
    
    public TestFileHandle() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
