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
    public void test_9() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileHandle fileHandle = new FileHandle(file);
    }
    
    @Test
    public void test_19() throws Exception {
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
    public void test_20() throws Exception {
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
    public void test_21() throws Exception {
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
    public void test_23() throws Exception {
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
    public void test_24() throws Exception {
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
    public void test_26() throws Exception {
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
    public void test_27() throws Exception {
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
    public void test_28() throws Exception {
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
    public void test_29() throws Exception {
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
    public void test_30() throws Exception {
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
    public void test_31() throws Exception {
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
    public void test_32() throws Exception {
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
    public void test_35() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileHandle fileHandle = new FileHandle(file);
    }
    
    @Test
    public void test_43() throws Exception {
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
    public void test_44() throws Exception {
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
    public void test_45() throws Exception {
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
    public void test_46() throws Exception {
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
    public void test_49() throws Exception {
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
    public void test_50() throws Exception {
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
    public void test_51() throws Exception {
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
    public void test_52() throws Exception {
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
    public void test_53() throws Exception {
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
    public void test_54() throws Exception {
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
    public void test_55() throws Exception {
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
    public void test_56() throws Exception {
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
    public void test_62() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final FileHandle fileHandle = new FileHandle(file);
    }
    
    @Test
    public void test_71() throws Exception {
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
    public void test_72() throws Exception {
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
    public void test_73() throws Exception {
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
    public void test_74() throws Exception {
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
    public void test_75() throws Exception {
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
    public void test_77() throws Exception {
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
    public void test_78() throws Exception {
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
    public void test_79() throws Exception {
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
    public void test_80() throws Exception {
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
    public void test_81() throws Exception {
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
    public void test_82() throws Exception {
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
    public void test_83() throws Exception {
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
