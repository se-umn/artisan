package org.employee;

import org.junit.Rule;
import org.junit.Test;

public class TestFileRead2_2_minimized {

    @Rule()
    public org.junit.contrib.java.lang.system.TextFromStandardInputStream userInputs;

    @Rule()
    public org.junit.contrib.java.lang.system.ExpectedSystemExit expectedSystemExit;

    @Test()
    public void test_4() throws Exception {
        java.lang.String javalangstring00 = null;
        java.lang.String javalangstring01 = null;
        java.lang.String javalangstring02 = null;
        java.lang.String javalangstring03 = null;
        java.lang.String javalangstring04 = null;
        java.lang.String javalangstring05 = null;
        java.lang.String javalangstring06 = null;
        java.lang.String[] javalangstringArray00 = null;
        java.lang.String javalangstring07 = null;
        java.lang.String javalangstring08 = null;
        java.lang.String javalangstring09 = null;
        java.io.File javaiofile00 = null;
        java.io.InputStream javaioinputStream00 = null;
        java.lang.String javalangstring10 = null;
        java.io.File javaiofile01 = null;
        java.lang.StringBuilder javalangstringBuilder00 = null;
        java.lang.String javalangstring11 = null;
        java.io.File javaiofile02 = null;
        java.io.File javaiofile03 = null;
        java.lang.String javalangstring12 = null;
        java.util.Scanner javautilscanner00 = null;
        java.lang.String javalangstring13 = null;
        org.employee.FileRead2 orgemployeefileRead200 = null;
        java.lang.String javalangstring14 = null;
        java.lang.String javalangstring15 = null;
        int returnValue;
        java.lang.String[] $r0 = null;
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r1;
        $r0 = new java.lang.String[2];
        $r0[0] = "2";
        $r0[1] = "123";
        $r1 = this.userInputs;
        $r1.provideLines($r0);
        javaioinputStream00 = java.lang.System.in;
        javaiofile02 = org.employee.systemtest.SystemTestUtils.createTempWorkingDir();
        javalangstring06 = "username.txt";
        javalangstring15 = "123";
        javaiofile01 = org.employee.systemtest.SystemTestUtils.createFileInWorkingDir(javaiofile02, javalangstring06, javalangstring15);
        javalangstringBuilder00 = new java.lang.StringBuilder();
        javalangstring04 = "Username:-->123\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring04);
        javalangstring07 = "Name:-->test\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring07);
        javalangstring08 = "Designnation:-->SeniorSoftwareEngineer Software Engineer\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring08);
        javalangstring14 = "Gender-->M\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring14);
        javalangstring11 = "Age:-->50\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring11);
        javalangstring12 = "Experience-->10\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring12);
        javalangstring10 = "30\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring10);
        javalangstring02 = "20\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring02);
        javalangstring03 = "10\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring03);
        javalangstring09 = "96000.0\n";
        javalangstringBuilder00 = javalangstringBuilder00.append(javalangstring09);
        javalangstring13 = javalangstringBuilder00.toString();
        javalangstring15 = "123";
        javaiofile03 = org.employee.systemtest.SystemTestUtils.createFileInWorkingDir(javaiofile02, javalangstring15, javalangstring13);
        javalangstringArray00 = new java.lang.String[1];
        javalangstring00 = javaiofile02.getAbsolutePath();
        javalangstringArray00[0] = javalangstring00;
        javalangstring00 = javalangstringArray00[0];
        javaiofile00 = new java.io.File(javalangstring00);
        javautilscanner00 = new java.util.Scanner(javaioinputStream00);
        javalangstring05 = javautilscanner00.next();
        orgemployeefileRead200 = new org.employee.FileRead2(javaiofile00);
        javalangstring01 = javautilscanner00.next();
        javalangstring06 = "username.txt";
        returnValue = orgemployeefileRead200.fileIsRead(javalangstring06, javalangstring01);
    }

    public TestFileRead2_2_minimized() {
        org.junit.contrib.java.lang.system.TextFromStandardInputStream $r0;
        org.junit.contrib.java.lang.system.ExpectedSystemExit $r1;
        $r0 = org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream();
        this.userInputs = $r0;
        $r1 = org.junit.contrib.java.lang.system.ExpectedSystemExit.none();
        this.expectedSystemExit = $r1;
    }
}
