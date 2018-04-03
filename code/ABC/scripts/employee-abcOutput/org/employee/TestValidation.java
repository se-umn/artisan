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
    public void test_5() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_7() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_9() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_10() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_11() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_18() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_22() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_24() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_25() throws Exception {
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
    public void test_27() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_28() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_29() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_30() throws Exception {
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
    public void test_31() throws Exception {
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
    public void test_50() throws Exception {
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
    public void test_52() throws Exception {
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
    public void test_53() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_54() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_55() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_65() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_66() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_68() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_70() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_72() throws Exception {
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
    public void test_73() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_74() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_75() throws Exception {
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
    public void test_76() throws Exception {
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
    public void test_95() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_96() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_98() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_99() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_100() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_106() throws Exception {
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
    public void test_107() throws Exception {
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
    public void test_108() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_110() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_111() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_122() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_123() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final Validation validation = new Validation();
    }
    
    @Test
    public void test_125() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next());
    }
    
    @Test
    public void test_127() throws Exception {
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
    public void test_128() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Validation().numberValidation(new Scanner(in).next(), new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_130() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    @Test
    public void test_131() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final String next = new Scanner(in).next();
        final Validation validation = new Validation();
        validation.numberValidation(next);
        validation.numberValidation(next, new DummyObjectToPassAsParameter());
    }
    
    @Test
    public void test_132() throws Exception {
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
    public void test_133() throws Exception {
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
    
    public TestValidation() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
