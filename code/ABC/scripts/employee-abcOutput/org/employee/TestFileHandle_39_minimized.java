package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_39_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_87() throws Exception {
        java.lang.String javalangstring00 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.util.Scanner javautilscanner00 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.lang.StringBuilder javalangstringBuilder01 = null;
        java.lang.String javalangstring03 = null;
        java.lang.String javalangstring04 = null;
        java.lang.String javalangstring05 = null;
        java.lang.StringBuilder javalangstringBuilder02 = null;
        java.lang.String javalangstring06 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String javalangstring09 = null;
        java.lang.String javalangstring10 = null;
        java.io.File javaiofile01 = null;
        java.lang.String javalangstring11 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.lang.String javalangstring14 = null;
        java.lang.String javalangstring15 = null;
        java.lang.String javalangstring16 = null;
        java.lang.String javalangstring17 = null;
        java.lang.StringBuilder javalangstringBuilder03 = null;
        java.lang.String javalangstring18 = null;
        java.lang.String javalangstring19 = null;
        java.lang.StringBuilder javalangstringBuilder04 = null;
        java.lang.String javalangstring20 = null;
        java.lang.String javalangstring21 = null;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[7];
        $r0[0] = "3";
        $r0[1] = "Name";
        $r0[2] = "2";
        $r0[3] = "5";
        $r0[4] = "24";
        $r0[5] = "F";
        $r0[6] = "234";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile00 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstringArray00 = new java.lang.String[1];
        javalangstring15 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring15;
        javalangstring15 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring15);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring06 = javautilscanner00.next();
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile01);
        javalangstring05 = javautilscanner00.next();
        javalangstring02 = javautilscanner00.next();
        javalangstring18 = javautilscanner00.next();
        javalangstring20 = javautilscanner00.next();
        javalangstring12 = javautilscanner00.next();
        javalangstring09 = javautilscanner00.next();
        javalangstring03 = "username.txt";
        orgemployeefileHandle00.addInFile(javalangstring03, javalangstring09);
        javalangstringBuilder02 = new java.lang.StringBuilder();
        javalangstring13 = "Username:-->";
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring13);
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring09);
        javalangstring17 = javalangstringBuilder02.toString();
        orgemployeefileHandle00.addInFile(javalangstring09, javalangstring17);
        javalangstringBuilder04 = new java.lang.StringBuilder();
        javalangstring21 = "Name:-->";
        javalangstringBuilder04 = javalangstringBuilder04.append(javalangstring21);
        javalangstringBuilder04 = javalangstringBuilder04.append(javalangstring05);
        javalangstring10 = javalangstringBuilder04.toString();
        orgemployeefileHandle00.addInFile(javalangstring09, javalangstring10);
        javalangstring19 = "Designnation:-->Software Engineer";
        orgemployeefileHandle00.addInFile(javalangstring09, javalangstring19);
        javalangstringBuilder01 = new java.lang.StringBuilder();
        javalangstring00 = "Gender-->";
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring00);
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring12);
        javalangstring08 = javalangstringBuilder01.toString();
        orgemployeefileHandle00.addInFile(javalangstring09, javalangstring08);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring16 = "Age:-->";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring16);
        javalangstring07 = java.lang.Integer.toString(24);
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring07);
        javalangstring14 = javalangstringBuilder00.toString();
        orgemployeefileHandle00.addInFile(javalangstring09, javalangstring14);
        javalangstringBuilder03 = new java.lang.StringBuilder();
        javalangstring04 = "Experience-->";
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring04);
        javalangstring11 = java.lang.Integer.toString(5);
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring11);
        javalangstring01 = javalangstringBuilder03.toString();
        orgemployeefileHandle00.addInFile(javalangstring09, javalangstring01);
    }

    public TestFileHandle_39_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
