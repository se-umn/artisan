package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_13 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_29() throws Exception {
        java.lang.String javalangstring00 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring03 = null;
        java.lang.StringBuilder javalangstringBuilder01 = null;
        java.lang.String javalangstring04 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring09 = null;
        java.lang.String javalangstring10 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.StringBuilder javalangstringBuilder02 = null;
        java.lang.String javalangstring11 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.lang.String javalangstring14 = null;
        java.lang.String javalangstring15 = null;
        java.lang.String javalangstring16 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.String javalangstring17 = null;
        java.lang.StringBuilder javalangstringBuilder03 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring18 = null;
        java.lang.String javalangstring19 = null;
        java.lang.StringBuilder javalangstringBuilder04 = null;
        java.lang.String javalangstring20 = null;
        java.lang.String javalangstring21 = null;
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
        javalangstring16 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring16;
        javalangstring16 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring16);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring10 = javautilscanner00.next();
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile01);
        javalangstring19 = javautilscanner00.next();
        javalangstring18 = javautilscanner00.next();
        javalangstring11 = javautilscanner00.next();
        javalangstring00 = javautilscanner00.next();
        javalangstring04 = javautilscanner00.next();
        javalangstring15 = javautilscanner00.next();
        javalangstring05 = "username.txt";
        orgemployeefileHandle00.addInFile(javalangstring05, javalangstring15);
        javalangstringBuilder01 = new java.lang.StringBuilder();
        javalangstring14 = "Username:-->";
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring14);
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring15);
        javalangstring13 = javalangstringBuilder01.toString();
        orgemployeefileHandle00.addInFile(javalangstring15, javalangstring13);
        javalangstringBuilder03 = new java.lang.StringBuilder();
        javalangstring21 = "Name:-->";
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring21);
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring19);
        javalangstring20 = javalangstringBuilder03.toString();
        orgemployeefileHandle00.addInFile(javalangstring15, javalangstring20);
        javalangstring06 = "Designnation:-->SeniorSoftwareEngineer Software Engineer";
        orgemployeefileHandle00.addInFile(javalangstring15, javalangstring06);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring01 = "Gender-->";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring01);
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring04);
        javalangstring09 = javalangstringBuilder00.toString();
        orgemployeefileHandle00.addInFile(javalangstring15, javalangstring09);
        javalangstringBuilder04 = new java.lang.StringBuilder();
        javalangstring17 = "Age:-->";
        javalangstringBuilder04 = javalangstringBuilder04.append(javalangstring17);
        javalangstring12 = java.lang.Integer.toString(50);
        javalangstringBuilder04 = javalangstringBuilder04.append(javalangstring12);
        javalangstring02 = javalangstringBuilder04.toString();
        orgemployeefileHandle00.addInFile(javalangstring15, javalangstring02);
        javalangstringBuilder02 = new java.lang.StringBuilder();
        javalangstring08 = "Experience-->";
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring08);
        javalangstring03 = java.lang.Integer.toString(10);
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring03);
        javalangstring07 = javalangstringBuilder02.toString();
        orgemployeefileHandle00.addInFile(javalangstring15, javalangstring07);
    }

    public TestFileHandle_13() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
