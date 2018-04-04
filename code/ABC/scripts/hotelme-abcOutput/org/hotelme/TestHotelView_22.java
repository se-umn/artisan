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

public class TestHotelView_22
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_38() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1) });
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
        hotelView.checkOutDate(scanner, hotelView.checkInDate(scanner));
        hotelView.roomTypes(scanner);
    }
    
    public TestHotelView_22() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
