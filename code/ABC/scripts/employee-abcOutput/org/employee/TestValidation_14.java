package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestValidation_14 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_40() throws Exception {
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring00 = null;
        java.lang.String[] javalangstringArray00 = null;
        org.employee.Validation orgemployeevalidation00 = null;
        java.lang.String javalangstring01 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.io.File javaiofile01 = null;
        java.lang.String javalangstring02 = null;
        java.util.Scanner javautilscanner00 = null;
        int returnValue;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[1];
        $r0[0] = "1";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile01 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstring00 = "username.txt";
        org.employee.systemtest.SystemTestUtils.createEmptyFileInWorkingDir(javaiofile01, javalangstring00);
        javalangstringArray00 = new java.lang.String[1];
        javalangstring02 = javaiofile01.getAbsolutePath();
        javalangstringArray00[0] = javalangstring02;
        javalangstring02 = javalangstringArray00[0];
        javaiofile00 = new java.io.File(javalangstring02);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring01 = javautilscanner00.next();
        orgemployeevalidation00 = new org.employee.Validation();
        returnValue = orgemployeevalidation00.numberValidation(javalangstring01);
    }

    public TestValidation_14() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
