package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestValidation
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_1() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_2() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_3() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_5() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_10() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_13() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_15() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        scanner.next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_16() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_17() throws Exception {
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
        scanner.next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_18() throws Exception {
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
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_33() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_36() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_39() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        scanner.next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_40() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_41() throws Exception {
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
        scanner.next();
        final Validation validation = new Validation();
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
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_57() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_59() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_61() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_65() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_66() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        scanner.next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_68() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_69() throws Exception {
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
        scanner.next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_70() throws Exception {
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
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_84() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_86() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    public TestValidation() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
