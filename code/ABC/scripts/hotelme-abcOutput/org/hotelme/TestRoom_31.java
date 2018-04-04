package org.hotelme;

import org.junit.Test;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.InputStream;
import java.sql.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestRoom_31
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_103() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final ArrayList<Room> list = new ArrayList<Room>();
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String s = (scanner.next() + " " + scanner.next()).split(" ")[0];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s);
        final ResultSet executeQuery = prepareStatement.executeQuery();
        executeQuery.next();
        executeQuery.getString("pw");
        executeQuery.getInt("userID");
        executeQuery.getString("fname");
        executeQuery.getString("lname");
        executeQuery.getString("uname");
        executeQuery.getString("pw");
        scanner.nextInt();
        list.clear();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        prepareStatement2.setInt(1, 3);
        prepareStatement2.executeQuery().next();
        scanner.nextInt();
        scanner.next();
        scanner.next();
        final String s2 = "";
        s2.equals(s2);
        scanner.nextInt();
        final String s3 = "Happy Double";
        final String s4 = "invalid";
        s3.equals(s4);
        s3.equals(s2);
        s3.equals(s4);
        s3.equals(s2);
        s3.equals(s2);
        final Date date = new Date(1522792800000L);
        final Date date2 = new Date(1522965600000L);
        list.clear();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
        prepareStatement3.setString(1, s3);
        prepareStatement3.setDate(2, date);
        prepareStatement3.setDate(3, date2);
        final ResultSet executeQuery2 = prepareStatement3.executeQuery();
        executeQuery2.next();
        executeQuery2.getInt("roomID");
        executeQuery2.getInt("price");
        executeQuery2.getInt("maxOccupancy");
        list.add(new Room(0, 4, s3, 4, 0, 0, 3, (java.util.Date)date, (java.util.Date)date2, 69.0f));
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        scanner.nextInt();
        list.get(0).getMaxOccupancy();
        scanner.nextInt();
        list.get(0).getMaxOccupancy();
        list.get(0).getMaxOccupancy();
        scanner.nextInt();
        list.get(0).getRoomID();
        list.get(0).getRoomType();
    }
    
    public TestRoom_31() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
