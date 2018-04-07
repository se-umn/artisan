package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestValidation_12 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_34() throws Exception {
        java.lang.String javalangstring00 = null;
        java.lang.String javalangstring01 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.lang.String javalangstring04 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.io.InputStream javaioinputStream00 = null;
        org.employee.Validation orgemployeevalidation00 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring09 = null;
        java.lang.String javalangstring10 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring11 = null;
        java.io.File javaiofile01 = null;
        java.lang.String javalangstring12 = null;
        java.io.File javaiofile02 = null;
        java.io.File javaiofile03 = null;
        java.lang.String javalangstring13 = null;
        java.lang.String javalangstring14 = null;
        int returnValue;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[1];
        $r0[0] = "1";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile00 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstring03 = "username.txt";
        javalangstring14 = "123";
        javaiofile03 = org.employee.systemtest.SystemTestUtils.createFileInWorkingDir(javaiofile00, javalangstring03, javalangstring14);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring02 = "Username:-->123\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring02);
        javalangstring05 = "Name:-->test\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring05);
        javalangstring06 = "Designnation:-->SeniorSoftwareEngineer Software Engineer\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring06);
        javalangstring13 = "Gender-->M\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring13);
        javalangstring11 = "Age:-->50\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring11);
        javalangstring12 = "Experience-->10\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring12);
        javalangstring09 = "30\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring09);
        javalangstring00 = "20\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring00);
        javalangstring01 = "10\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring01);
        javalangstring07 = "96000.0\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring07);
        javalangstring08 = javalangstringBuilder00.toString();
        javalangstring14 = "123";
        javaiofile01 = org.employee.systemtest.SystemTestUtils.createFileInWorkingDir(javaiofile00, javalangstring14, javalangstring08);
        javalangstringArray00 = new java.lang.String[1];
        javalangstring10 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring10;
        javalangstring10 = javalangstringArray00[0];
        javaiofile02 = new java.io.File(javalangstring10);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring04 = javautilscanner00.next();
        orgemployeevalidation00 = new org.employee.Validation();
        returnValue = orgemployeevalidation00.numberValidation(javalangstring04);
    }

    public TestValidation_12() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
