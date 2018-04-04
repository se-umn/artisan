package org.hotelme;

import org.junit.Test;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.InputStream;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelView_54
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_122() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final String s4 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s);
        prepareStatement2.setString(2, s2);
        prepareStatement2.setString(3, s3);
        prepareStatement2.setString(4, s4);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s3);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        hotelView.signUpSuccess(new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    public TestHotelView_54() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
