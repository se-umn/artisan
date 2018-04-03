package org.employee;

import org.junit.Test;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestEmployee
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_2() throws Exception {
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        this.userInputs.provideLines(new String[] { "1", "admin" });
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createEmptyFileInWorkingDir(tempWorkingDir, "username.txt");
        Employee.main(new String[] { tempWorkingDir.getAbsolutePath() });
    }
    
    @Test
    public void test_14() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createTempWorkingDir();
        final Employee employee = new Employee();
    }
    
    @Test
    public void test_15() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        Employee.main(new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() });
    }
    
    @Test
    public void test_46() throws Exception {
        this.userInputs.provideLines(new String[] { "2", "123" });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        Employee.main(new String[] { tempWorkingDir.getAbsolutePath() });
    }
    
    @Test
    public void test_60() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        Employee.main(new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() });
    }
    
    @Test
    public void test_92() throws Exception {
        this.userInputs.provideLines(new String[] { "4" });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        Employee.main(new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() });
    }
    
    @Test
    public void test_102() throws Exception {
        this.userInputs.provideLines(new String[] { "1", "admin" });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
        Employee.main(new String[] { tempWorkingDir.getAbsolutePath() });
    }
    
    @Test
    public void test_116() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createTempWorkingDir();
        final Employee employee = new Employee();
    }
    
    @Test
    public void test_117() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        Employee.main(new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() });
    }
    
    public TestEmployee() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
