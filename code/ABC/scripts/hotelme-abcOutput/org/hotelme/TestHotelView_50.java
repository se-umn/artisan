package org.hotelme;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelView_50
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_115() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    public TestHotelView_50() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
