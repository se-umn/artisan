package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestEmployeeMetaData
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_11() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final EmployeeMetaData employeeMetaData = new EmployeeMetaData(file, scanner);
    }
    
    @Test
    public void test_12() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        new EmployeeMetaData(file, scanner).setCount(1);
    }
    
    @Test
    public void test_14() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Jimbo", "3", "0", "19", "M", "345" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final EmployeeMetaData employeeMetaData = new EmployeeMetaData(file, scanner);
        employeeMetaData.setCount(1);
        employeeMetaData.addMeta();
    }
    
    @Test
    public void test_34() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final EmployeeMetaData employeeMetaData = new EmployeeMetaData(file, scanner);
    }
    
    @Test
    public void test_37() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        new EmployeeMetaData(file, scanner).setCount(1);
    }
    
    @Test
    public void test_38() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "test", "1", "10", "50", "M", "123" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final EmployeeMetaData employeeMetaData = new EmployeeMetaData(file, scanner);
        employeeMetaData.setCount(1);
        employeeMetaData.addMeta();
    }
    
    @Test
    public void test_63() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final EmployeeMetaData employeeMetaData = new EmployeeMetaData(file, scanner);
    }
    
    @Test
    public void test_64() throws Exception {
        this.userInputs.provideLines(new String[] { "3" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        new EmployeeMetaData(file, scanner).setCount(1);
    }
    
    @Test
    public void test_67() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2", "5", "24", "F", "234" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        final EmployeeMetaData employeeMetaData = new EmployeeMetaData(file, scanner);
        employeeMetaData.setCount(1);
        employeeMetaData.addMeta();
    }
    
    public TestEmployeeMetaData() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}