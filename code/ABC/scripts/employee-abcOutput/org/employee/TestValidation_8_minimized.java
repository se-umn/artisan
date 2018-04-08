package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestValidation_8_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_16() throws Exception {
        java.lang.String javalangstring00 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring04 = null;
        java.lang.String[] javalangstringArray00 = null;
        org.employee.Validation orgemployeevalidation00 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String javalangstring07 = null;
        java.io.File javaiofile01 = null;
        int returnValue;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[7];
        $r0[0] = "3";
        $r0[1] = "test";
        $r0[2] = "1";
        $r0[3] = "10";
        $r0[4] = "50";
        $r0[5] = "M";
        $r0[6] = "123";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile00 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstringArray00 = new java.lang.String[1];
        javalangstring03 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring03;
        javalangstring03 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring03);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring05 = javautilscanner00.next();
        javalangstring06 = javautilscanner00.next();
        javalangstring04 = javautilscanner00.next();
        javalangstring07 = javautilscanner00.next();
        javalangstring00 = javautilscanner00.next();
        javalangstring02 = javautilscanner00.next();
        javalangstring01 = javautilscanner00.next();
        orgemployeevalidation00 = new org.employee.Validation();
        returnValue = orgemployeevalidation00.numberValidation(javalangstring01);
    }

    public TestValidation_8_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}