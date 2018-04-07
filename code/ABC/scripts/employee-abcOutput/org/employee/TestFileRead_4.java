package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileRead_4 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_37() throws Exception {
        java.lang.String javalangstring00 = null;
        java.lang.String javalangstring01 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.lang.String javalangstring04 = null;
        java.io.FileInputStream javaiofileInputStream00 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring09 = null;
        java.io.InputStreamReader javaioinputStreamReader00 = null;
        java.lang.String javalangstring10 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring11 = null;
        java.io.File javaiofile01 = null;
        java.io.DataInputStream javaiodataInputStream00 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.io.File javaiofile02 = null;
        java.io.BufferedReader javaiobufferedReader00 = null;
        java.io.File javaiofile03 = null;
        java.lang.String javalangstring14 = null;
        java.lang.String javalangstring15 = null;
        java.io.File javaiofile04 = null;
        org.employee.FileRead orgemployeefileRead00 = null;
        java.lang.String javalangstring16 = null;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[2];
        $r0[0] = "1";
        $r0[1] = "admin";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile00 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstring03 = "username.txt";
        javalangstring16 = "123";
        javaiofile03 = org.employee.systemtest.SystemTestUtils.createFileInWorkingDir(javaiofile00, javalangstring03, javalangstring16);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring02 = "Username:-->123\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring02);
        javalangstring05 = "Name:-->test\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring05);
        javalangstring06 = "Designnation:-->SeniorSoftwareEngineer Software Engineer\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring06);
        javalangstring15 = "Gender-->M\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring15);
        javalangstring11 = "Age:-->50\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring11);
        javalangstring13 = "Experience-->10\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring13);
        javalangstring09 = "30\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring09);
        javalangstring00 = "20\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring00);
        javalangstring01 = "10\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring01);
        javalangstring07 = "96000.0\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring07);
        javalangstring08 = javalangstringBuilder00.toString();
        javalangstring16 = "123";
        javaiofile01 = org.employee.systemtest.SystemTestUtils.createFileInWorkingDir(javaiofile00, javalangstring16, javalangstring08);
        javalangstringArray00 = new java.lang.String[1];
        javalangstring10 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring10;
        javalangstring10 = javalangstringArray00[0];
        javaiofile02 = new java.io.File(javalangstring10);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring04 = javautilscanner00.next();
        javalangstring14 = javautilscanner00.next();
        javalangstring03 = "username.txt";
        javaiofile04 = new java.io.File(javaiofile02, javalangstring03);
        javaiofileInputStream00 = new java.io.FileInputStream(javaiofile04);
        javaiodataInputStream00 = new java.io.DataInputStream(javaiofileInputStream00);
        javaioinputStreamReader00 = new java.io.InputStreamReader(javaiodataInputStream00);
        javaiobufferedReader00 = new java.io.BufferedReader(javaioinputStreamReader00);
        javalangstring12 = javaiobufferedReader00.readLine();
        orgemployeefileRead00 = new org.employee.FileRead(javaiofile02);
        orgemployeefileRead00.fileIsRead(javalangstring12);
    }

    public TestFileRead_4() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
