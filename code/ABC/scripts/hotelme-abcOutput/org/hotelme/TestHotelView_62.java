package org.hotelme;

import org.junit.Test;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.InputStream;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelView_62
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_138() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
    }
    
    public TestHotelView_62() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
