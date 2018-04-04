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

public class TestHotelView_30
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_54() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1) });
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
        hotelView.checkOutDate(scanner, hotelView.checkInDate(scanner));
        hotelView.roomTypes(scanner);
    }
    
    public TestHotelView_30() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
