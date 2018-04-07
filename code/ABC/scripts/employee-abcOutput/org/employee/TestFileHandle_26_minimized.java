package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_26_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_64() throws Exception {
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.String javalangstring04 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String javalangstring07 = null;
        java.lang.StringBuilder javalangstringBuilder01 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String javalangstring09 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring10 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.StringBuilder javalangstringBuilder02 = null;
        java.lang.StringBuilder javalangstringBuilder03 = null;
        java.lang.String javalangstring11 = null;
        java.lang.String javalangstring12 = null;
        java.lang.String javalangstring13 = null;
        java.lang.String javalangstring14 = null;
        java.lang.StringBuilder javalangstringBuilder04 = null;
        java.lang.String javalangstring15 = null;
        java.lang.String javalangstring16 = null;
        java.lang.String javalangstring17 = null;
        java.lang.String javalangstring18 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring19 = null;
        java.lang.String javalangstring20 = null;
        java.io.File javaiofile01 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring21 = null;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[7];
        $r0[0] = "3";
        $r0[1] = "Jimbo";
        $r0[2] = "3";
        $r0[3] = "0";
        $r0[4] = "19";
        $r0[5] = "M";
        $r0[6] = "345";
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
        javalangstring06 = javautilscanner00.next();
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile00);
        javalangstring12 = javautilscanner00.next();
        javalangstring19 = javautilscanner00.next();
        javalangstring18 = javautilscanner00.next();
        javalangstring03 = javautilscanner00.next();
        javalangstring04 = javautilscanner00.next();
        javalangstring14 = javautilscanner00.next();
        javalangstring08 = "username.txt";
        orgemployeefileHandle00.addInFile(javalangstring08, javalangstring14);
        javalangstringBuilder02 = new java.lang.StringBuilder();
        javalangstring11 = "Username:-->";
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring11);
        javalangstringBuilder02 = javalangstringBuilder02.append(javalangstring14);
        javalangstring05 = javalangstringBuilder02.toString();
        orgemployeefileHandle00.addInFile(javalangstring14, javalangstring05);
        javalangstringBuilder01 = new java.lang.StringBuilder();
        javalangstring21 = "Name:-->";
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring21);
        javalangstringBuilder01 = javalangstringBuilder01.append(javalangstring12);
        javalangstring09 = javalangstringBuilder01.toString();
        orgemployeefileHandle00.addInFile(javalangstring14, javalangstring09);
        javalangstring15 = "Designnation:-->Software Trainee";
        orgemployeefileHandle00.addInFile(javalangstring14, javalangstring15);
        javalangstringBuilder04 = new java.lang.StringBuilder();
        javalangstring02 = "Gender-->";
        javalangstringBuilder04 = javalangstringBuilder04.append(javalangstring02);
        javalangstringBuilder04 = javalangstringBuilder04.append(javalangstring04);
        javalangstring01 = javalangstringBuilder04.toString();
        orgemployeefileHandle00.addInFile(javalangstring14, javalangstring01);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring16 = "Age:-->";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring16);
        javalangstring17 = java.lang.Integer.toString(19);
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring17);
        javalangstring07 = javalangstringBuilder00.toString();
        orgemployeefileHandle00.addInFile(javalangstring14, javalangstring07);
        javalangstringBuilder03 = new java.lang.StringBuilder();
        javalangstring10 = "Experience-->";
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring10);
        javalangstring20 = java.lang.Integer.toString(0);
        javalangstringBuilder03 = javalangstringBuilder03.append(javalangstring20);
        javalangstring13 = javalangstringBuilder03.toString();
        orgemployeefileHandle00.addInFile(javalangstring14, javalangstring13);
    }

    public TestFileHandle_26_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
