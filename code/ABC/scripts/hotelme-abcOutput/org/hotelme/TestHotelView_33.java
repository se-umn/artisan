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

public class TestHotelView_33
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_77() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelModel hotelModel = new HotelModel(connection);
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signIn(scanner).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelView.signInSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.reservedRooms();
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        prepareStatement.setInt(1, 3);
        prepareStatement.executeQuery().next();
        hotelView.noReservedRooms();
        hotelView.optionsMenu(false);
        scanner.nextInt();
        final java.util.Date checkInDate = hotelView.checkInDate(scanner);
        hotelView.checkOutDate(scanner, checkInDate);
        final String s = "";
        s.equals(s);
        final String roomTypes = hotelView.roomTypes(scanner);
        roomTypes.equals(s);
        checkInDate.getTime();
        final Date date = new Date(1522792800000L);
        final Date date2 = new Date(1522965600000L);
        final PreparedStatement prepareStatement2 = connection.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
        prepareStatement2.setString(1, roomTypes);
        prepareStatement2.setDate(2, date);
        prepareStatement2.setDate(3, date2);
        final ResultSet executeQuery = prepareStatement2.executeQuery();
        executeQuery.next();
        executeQuery.getInt("roomID");
        executeQuery.getInt("price");
        executeQuery.getInt("maxOccupancy");
        executeQuery.next();
        executeQuery.next();
        executeQuery.next();
        executeQuery.next();
        executeQuery.next();
        executeQuery.next();
        hotelView.roomConfirmation(scanner, 6, roomTypes);
        hotelView.totalAdults(scanner, 4);
        hotelView.totalChildren(scanner, 2);
    }
    
    public TestHotelView_33() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
