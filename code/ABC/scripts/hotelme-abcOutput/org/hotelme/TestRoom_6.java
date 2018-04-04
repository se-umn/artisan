package org.hotelme;

import org.junit.Test;
import java.util.ArrayList;
import java.io.InputStream;
import java.sql.Date;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestRoom_6
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_62() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/04/18", "04/06/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelModel.getUser();
        scanner.nextInt();
        hotelModel.checkReservedRooms();
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
        hotelModel.checkRoomsAvailable(new Date(1522792800000L), new Date(1522965600000L), s2);
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        scanner.nextInt();
        final ArrayList<Room> room = hotelModel.getRoom();
        room.get(0).getMaxOccupancy();
        room.get(0).getMaxOccupancy();
        scanner.nextInt();
        room.get(0).getRoomID();
    }
    
    public TestRoom_6() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}