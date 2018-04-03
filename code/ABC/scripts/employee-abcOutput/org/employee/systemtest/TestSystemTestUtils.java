package org.employee.systemtest;

import java.io.File;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestSystemTestUtils
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_1() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createEmptyFileInWorkingDir(SystemTestUtils.createTempWorkingDir(), "username.txt");
    }
    
    @Test
    public void test_13() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createTempWorkingDir();
    }
    
    @Test
    public void test_45() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
    }
    
    @Test
    public void test_59() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createTempWorkingDir();
    }
    
    @Test
    public void test_91() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createTempWorkingDir();
    }
    
    @Test
    public void test_101() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final File tempWorkingDir = SystemTestUtils.createTempWorkingDir();
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "username.txt", "123");
        SystemTestUtils.createFileInWorkingDir(tempWorkingDir, "123", "Username:-->123\n" + "Name:-->test\n" + "Designnation:-->SeniorSoftwareEngineer Software Engineer\n" + "Gender-->M\n" + "Age:-->50\n" + "Experience-->10\n" + "30\n" + "20\n" + "10\n" + "96000.0\n");
    }
    
    @Test
    public void test_115() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.createTempWorkingDir();
    }
    
    public TestSystemTestUtils() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
