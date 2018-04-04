package org.hotelme;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelView_58
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_131() throws Exception {
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        this.userInputs.provideLines(new String[] { Integer.toString(3) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    public TestHotelView_58() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
