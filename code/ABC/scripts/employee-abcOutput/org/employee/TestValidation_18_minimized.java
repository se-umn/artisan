package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestValidation_18_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_49() throws Exception {
        java.lang.String javalangstring00 = null;
        org.employee.Validation orgemployeevalidation00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.io.File javaiofile00 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String javalangstring03 = null;
        java.io.File javaiofile01 = null;
        java.util.Scanner javautilscanner00 = null;
        int returnValue;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[3];
        $r0[0] = "3";
        $r0[1] = "Jimbo";
        $r0[2] = "3";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile01 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstringArray00 = new java.lang.String[1];
        javalangstring00 = javaiofile01.getAbsolutePath();
        javalangstringArray00[0] = javalangstring00;
        javalangstring00 = javalangstringArray00[0];
        javaiofile00 = new java.io.File(javalangstring00);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring02 = javautilscanner00.next();
        javalangstring01 = javautilscanner00.next();
        javalangstring03 = javautilscanner00.next();
        orgemployeevalidation00 = new org.employee.Validation();
        returnValue = orgemployeevalidation00.numberValidation(javalangstring03);
    }

    public TestValidation_18_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
