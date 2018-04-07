package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestEmployeeMetaData_5 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_45() throws Exception {
        java.lang.String javalangstring00 = null;
        org.employee.EmployeeMetaData orgemployeeemployeeMetaData00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.io.File javaiofile00 = null;
        java.util.Scanner javautilscanner00 = null;
        java.io.File javaiofile01 = null;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[1];
        $r0[0] = "3";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile00 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstringArray00 = new java.lang.String[1];
        javalangstring00 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring00;
        javalangstring00 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring00);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring01 = javautilscanner00.next();
        orgemployeeemployeeMetaData00 = new org.employee.EmployeeMetaData(javaiofile01, javautilscanner00);
        orgemployeeemployeeMetaData00.setCount(1);
    }

    public TestEmployeeMetaData_5() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
