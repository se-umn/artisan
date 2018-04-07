package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileHandle_20 {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_58() throws Exception {
        java.lang.String javalangstring00 = null;
        java.io.FileWriter javaiofileWriter00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.io.FileWriter javaiofileWriter01 = null;
        java.io.File javaiofile00 = null;
        java.lang.String javalangstring03 = null;
        java.io.File javaiofile01 = null;
        java.lang.String javalangstring04 = null;
        java.io.FileWriter javaiofileWriter02 = null;
        java.io.File javaiofile02 = null;
        java.io.FileWriter javaiofileWriter03 = null;
        java.io.File javaiofile03 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.io.File javaiofile04 = null;
        java.io.File javaiofile05 = null;
        java.lang.String javalangstring05 = null;
        java.io.File javaiofile06 = null;
        org.employee.FileHandle orgemployeefileHandle00 = null;
        java.lang.String javalangstring06 = null;
        java.io.FileWriter javaiofileWriter04 = null;
        java.lang.String javalangstring07 = null;
        java.io.File javaiofile07 = null;
        java.io.FileWriter javaiofileWriter05 = null;
        java.io.FileWriter javaiofileWriter06 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring09 = null;
        java.io.File javaiofile08 = null;
        java.util.Scanner javautilscanner00 = null;
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
        javaiofile08 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstringArray00 = new java.lang.String[1];
        javalangstring00 = javaiofile08.getAbsolutePath();
        javalangstringArray00[0] = javalangstring00;
        javalangstring00 = javalangstringArray00[0];
        javaiofile02 = new java.io.File(javalangstring00);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring03 = javautilscanner00.next();
        javalangstring06 = javautilscanner00.next();
        javalangstring09 = javautilscanner00.next();
        javalangstring08 = javautilscanner00.next();
        javalangstring01 = javautilscanner00.next();
        javalangstring02 = javautilscanner00.next();
        javalangstring07 = javautilscanner00.next();
        javalangstring04 = "username.txt";
        javaiofile01 = new java.io.File(javaiofile02, javalangstring04);
        javaiofileWriter00 = new java.io.FileWriter(javaiofile01, false);
        javaiofile04 = new java.io.File(javaiofile02, javalangstring07);
        javaiofileWriter01 = new java.io.FileWriter(javaiofile04, false);
        javaiofile00 = new java.io.File(javaiofile02, javalangstring07);
        javaiofileWriter04 = new java.io.FileWriter(javaiofile00, false);
        javaiofile03 = new java.io.File(javaiofile02, javalangstring07);
        javaiofileWriter02 = new java.io.FileWriter(javaiofile03, false);
        javaiofile07 = new java.io.File(javaiofile02, javalangstring07);
        javaiofileWriter06 = new java.io.FileWriter(javaiofile07, false);
        javaiofile05 = new java.io.File(javaiofile02, javalangstring07);
        javaiofileWriter05 = new java.io.FileWriter(javaiofile05, false);
        javaiofile06 = new java.io.File(javaiofile02, javalangstring07);
        javaiofileWriter03 = new java.io.FileWriter(javaiofile06, false);
        orgemployeefileHandle00 = new org.employee.FileHandle(javaiofile02);
        javalangstring05 = java.lang.Integer.toString(20);
        orgemployeefileHandle00.addInFile(javalangstring07, javalangstring05);
    }

    public TestFileHandle_20() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
