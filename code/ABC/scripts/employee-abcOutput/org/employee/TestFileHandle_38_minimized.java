package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_38_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_86() throws Exception {
        java.lang.String javalangstring00 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.util.Scanner javautilscanner00 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.StringBuilder javalangstringBuilder01 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.lang.StringBuilder javalangstringBuilder02 = null;
        java.lang.String javalangstring04 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.io.File javaiofile01 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring09 = null;
        java.lang.String javalangstring10 = null;
        java.lang.String javalangstring11 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.lang.String javalangstring14 = null;
        java.lang.String javalangstring15 = null;
        java.lang.String javalangstring16 = null;
        java.lang.StringBuilder javalangstringBuilder03 = null;
        java.lang.String javalangstring17 = null;
        java.lang.String javalangstring18 = null;
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
        javalangstring12 = javaiofile00.getAbsolutePath();
        javalangstringArray00[0] = javalangstring12;
        javalangstring12 = javalangstringArray00[0];
        javaiofile01 = new java.io.File(javalangstring12);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring04 = javautilscanner00.next();
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile01);
        javalangstring03 = javautilscanner00.next();
        javalangstring01 = javautilscanner00.next();
        javalangstring15 = javautilscanner00.next();
        javalangstring17 = javautilscanner00.next();
        javalangstring09 = javautilscanner00.next();
        javalangstring07 = javautilscanner00.next();
        javalangstring02 = "username.txt";
        orgemployeefileHandle00.addInFile(javalangstring02, javalangstring07);
        javalangstringBuilder02 = new java.lang.StringBuilder();
        javalangstring10 = "Username:-->";
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring10);
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring07);
        javalangstring14 = javalangstringBuilder02.toString();
        orgemployeefileHandle00.addInFile(javalangstring07, javalangstring14);
        javalangstringBuilder03 = new java.lang.StringBuilder();
        javalangstring18 = "Name:-->";
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring18);
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring03);
        javalangstring08 = javalangstringBuilder03.toString();
        orgemployeefileHandle00.addInFile(javalangstring07, javalangstring08);
        javalangstring16 = "Designnation:-->Software Engineer";
        orgemployeefileHandle00.addInFile(javalangstring07, javalangstring16);
        javalangstringBuilder01 = new java.lang.StringBuilder();
        javalangstring00 = "Gender-->";
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring00);
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring09);
        javalangstring06 = javalangstringBuilder01.toString();
        orgemployeefileHandle00.addInFile(javalangstring07, javalangstring06);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring13 = "Age:-->";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring13);
        javalangstring05 = java.lang.Integer.toString(24);
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring05);
        javalangstring11 = javalangstringBuilder00.toString();
        orgemployeefileHandle00.addInFile(javalangstring07, javalangstring11);
    }

    public TestFileHandle_38_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
