package org.employee;

import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestDummyObjectToPassAsParameter
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_3() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_4() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_6() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_16() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_17() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_20() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_47() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_48() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_49() throws Exception {
        this.userInputs.provideLines(new String[] { "2" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_61() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_62() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_63() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_93() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_94() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_97() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_103() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_104() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_105() throws Exception {
        this.userInputs.provideLines(new String[] { "1" });
        final InputStream in = System.in;
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        final File file = new File((new String[] { tempWorkingDir.getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_118() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    @Test
    public void test_119() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new DummyObjectToPassAsParameter().isDummy();
    }
    
    @Test
    public void test_124() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        new Scanner(in).next();
        final DummyObjectToPassAsParameter dummyObjectToPassAsParameter = new DummyObjectToPassAsParameter();
    }
    
    public TestDummyObjectToPassAsParameter() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
