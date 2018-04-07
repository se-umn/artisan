package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_6_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_22() throws Exception {
        java.lang.String javalangstring00 = null;
        java.io.File javaiofile00 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring04 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String javalangstring09 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.StringBuilder javalangstringBuilder01 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring10 = null;
        java.lang.String javalangstring11 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.io.File javaiofile01 = null;
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
        javalangstring09 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring09;
        javalangstring09 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring09);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring04 = javautilscanner00.next();
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile01);
        javalangstring11 = javautilscanner00.next();
        javalangstring10 = javautilscanner00.next();
        javalangstring05 = javautilscanner00.next();
        javalangstring00 = javautilscanner00.next();
        javalangstring01 = javautilscanner00.next();
        javalangstring08 = javautilscanner00.next();
        javalangstring02 = "username.txt";
        orgemployeefileHandle00.addInFile(javalangstring02, javalangstring08);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring07 = "Username:-->";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring07);
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring08);
        javalangstring06 = javalangstringBuilder00.toString();
        orgemployeefileHandle00.addInFile(javalangstring08, javalangstring06);
        javalangstringBuilder01 = new java.lang.StringBuilder();
        javalangstring13 = "Name:-->";
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring13);
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring11);
        javalangstring12 = javalangstringBuilder01.toString();
        orgemployeefileHandle00.addInFile(javalangstring08, javalangstring12);
        javalangstring03 = "Designnation:-->SeniorSoftwareEngineer Software Engineer";
        orgemployeefileHandle00.addInFile(javalangstring08, javalangstring03);
    }

    public TestFileHandle_6_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
