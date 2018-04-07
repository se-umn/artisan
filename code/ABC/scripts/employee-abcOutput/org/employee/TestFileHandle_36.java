package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_36 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_84() throws Exception {
        java.lang.String javalangstring00 = null;
        java.util.Scanner javautilscanner00 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.lang.StringBuilder javalangstringBuilder01 = null;
        java.lang.String javalangstring04 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.io.File javaiofile01 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String javalangstring09 = null;
        java.lang.String javalangstring10 = null;
        java.lang.String javalangstring11 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.lang.StringBuilder javalangstringBuilder02 = null;
        java.lang.String javalangstring14 = null;
        java.lang.String javalangstring15 = null;
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
        javalangstring10 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring10;
        javalangstring10 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring10);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring04 = javautilscanner00.next();
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile01);
        javalangstring03 = javautilscanner00.next();
        javalangstring01 = javautilscanner00.next();
        javalangstring12 = javautilscanner00.next();
        javalangstring14 = javautilscanner00.next();
        javalangstring08 = javautilscanner00.next();
        javalangstring06 = javautilscanner00.next();
        javalangstring02 = "username.txt";
        orgemployeefileHandle00.addInFile(javalangstring02, javalangstring06);
        javalangstringBuilder01 = new java.lang.StringBuilder();
        javalangstring09 = "Username:-->";
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring09);
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring06);
        javalangstring11 = javalangstringBuilder01.toString();
        orgemployeefileHandle00.addInFile(javalangstring06, javalangstring11);
        javalangstringBuilder02 = new java.lang.StringBuilder();
        javalangstring15 = "Name:-->";
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring15);
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring03);
        javalangstring07 = javalangstringBuilder02.toString();
        orgemployeefileHandle00.addInFile(javalangstring06, javalangstring07);
        javalangstring13 = "Designnation:-->Software Engineer";
        orgemployeefileHandle00.addInFile(javalangstring06, javalangstring13);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring00 = "Gender-->";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring00);
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring08);
        javalangstring05 = javalangstringBuilder00.toString();
        orgemployeefileHandle00.addInFile(javalangstring06, javalangstring05);
    }

    public TestFileHandle_36() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
