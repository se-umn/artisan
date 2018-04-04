package org.hotelme;

import org.junit.Test;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.InputStream;
import java.sql.Date;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestUser_9
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_74() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelModel hotelModel = new HotelModel(connection);
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        final User user = hotelModel.getUser();
        user.getFname();
        scanner.nextInt();
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        user.getUserID();
        prepareStatement.setInt(1, 3);
        prepareStatement.executeQuery().next();
        scanner.nextInt();
        scanner.next();
        scanner.next();
        final String s = "";
        s.equals(s);
        scanner.nextInt();
        final String s2 = "Happy Double";
        final String s3 = "invalid";
        s2.equals(s3);
        s2.equals(s);
        s2.equals(s3);
        s2.equals(s);
        s2.equals(s);
        final Date date = new Date(1522792800000L);
        final Date date2 = new Date(1522965600000L);
        final PreparedStatement prepareStatement2 = connection.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setDate(2, date);
        prepareStatement2.setDate(3, date2);
        final ResultSet executeQuery = prepareStatement2.executeQuery();
        executeQuery.next();
        executeQuery.getInt("roomID");
        executeQuery.getInt("price");
        executeQuery.getInt("maxOccupancy");
        user.getUserID();
    }
    
    public TestUser_9() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
