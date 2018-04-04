package org.employee;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.io.File;
import org.employee.systemtest.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestValidation_24
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_74() throws Exception {
        this.userInputs.provideLines(new String[] { "3", "Name", "2" });
        final InputStream in = System.in;
        final File file = new File((new String[] { SystemTestUtils.createTempWorkingDir().getAbsolutePath() })[0]);
        final Scanner scanner = new Scanner(in);
        scanner.next();
        scanner.next();
        new Validation().numberValidation(scanner.next());
    }
    
    public TestValidation_24() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}