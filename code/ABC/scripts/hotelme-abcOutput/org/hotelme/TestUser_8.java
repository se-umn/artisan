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

public class TestUser_8
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_45() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String s = (scanner.next() + " " + scanner.next()).split(" ")[0];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s);
        final ResultSet executeQuery = prepareStatement.executeQuery();
        executeQuery.next();
        executeQuery.getString("pw");
        executeQuery.getInt("userID");
        final User user = new User(3, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw"));
        user.getFname();
        scanner.nextInt();
        connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        user.getUserID();
    }
    
    public TestUser_8() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
