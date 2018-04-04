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

public class TestHotelView_44
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_106() throws Exception {
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String s = hotelView.signIn(scanner).split(" ")[0];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s);
        final ResultSet executeQuery = prepareStatement.executeQuery();
        executeQuery.next();
        executeQuery.getString("pw");
        executeQuery.getInt("userID");
        hotelView.signInSuccess(new User(3, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.reservedRooms();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        prepareStatement2.setInt(1, 3);
        prepareStatement2.executeQuery().next();
        hotelView.noReservedRooms();
        hotelView.optionsMenu(false);
        scanner.nextInt();
        final java.util.Date checkInDate = hotelView.checkInDate(scanner);
        hotelView.checkOutDate(scanner, checkInDate);
        final String s2 = "";
        s2.equals(s2);
        final String roomTypes = hotelView.roomTypes(scanner);
        roomTypes.equals(s2);
        checkInDate.getTime();
        final Date date = new Date(1522792800000L);
        final Date date2 = new Date(1522965600000L);
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
        prepareStatement3.setString(1, roomTypes);
        prepareStatement3.setDate(2, date);
        prepareStatement3.setDate(3, date2);
        final ResultSet executeQuery2 = prepareStatement3.executeQuery();
        executeQuery2.next();
        executeQuery2.getInt("roomID");
        executeQuery2.getInt("price");
        executeQuery2.getInt("maxOccupancy");
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        executeQuery2.next();
        hotelView.roomConfirmation(scanner, 6, roomTypes);
        hotelView.totalAdults(scanner, 4);
        hotelView.totalChildren(scanner, 2);
        final PreparedStatement prepareStatement4 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement4.setInt(1, 3);
        prepareStatement4.setInt(2, 4);
        prepareStatement4.setInt(3, 2);
        prepareStatement4.setInt(4, 2);
        prepareStatement4.setDate(5, date);
        prepareStatement4.setDate(6, date2);
        prepareStatement4.executeUpdate();
        final PreparedStatement prepareStatement5 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement5.setInt(1, 4);
        prepareStatement5.setInt(2, 3);
        prepareStatement5.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    public TestHotelView_44() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
