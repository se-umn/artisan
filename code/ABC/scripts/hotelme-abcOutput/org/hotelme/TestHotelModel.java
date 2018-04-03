package org.hotelme;

import java.sql.Date;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelModel
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_2() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_9() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
    }
    
    @Test
    public void test_11() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelModel.getUser();
    }
    
    @Test
    public void test_13() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_25() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
    }
    
    @Test
    public void test_28() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_30() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_32() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_36() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_38() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_39() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_53() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
        hotelModel.checkRoomsAvailable(date, date2, s2);
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        final Object value = hotelModel.getRoom().get(0);
        ((Room)value).getMaxOccupancy();
        scanner.nextInt();
        hotelModel.getRoom();
        ((Room)value).getRoomID();
        hotelModel.getRoom();
        final String roomType = ((Room)value).getRoomType();
        hotelModel.getRoom();
        hotelModel.reserveRoom(4, 2, roomType, 2, 3, date, date2);
    }
    
    @Test
    public void test_56() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
        hotelModel.checkRoomsAvailable(date, date2, s2);
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getRoomID();
        final String roomType = hotelModel.getRoom().get(0).getRoomType();
        hotelModel.getRoom();
        hotelModel.reserveRoom(4, 2, roomType, 2, 3, date, date2);
    }
    
    @Test
    public void test_89() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_98() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
    }
    
    @Test
    public void test_99() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelModel.getUser();
    }
    
    @Test
    public void test_108() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_116() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
    }
    
    @Test
    public void test_118() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_126() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
    }
    
    @Test
    public void test_128() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_133() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_143() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
    }
    
    @Test
    public void test_144() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelModel.getUser();
    }
    
    @Test
    public void test_153() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_160() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
    }
    
    @Test
    public void test_161() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelModel.getUser();
    }
    
    @Test
    public void test_164() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_176() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
    }
    
    @Test
    public void test_179() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_181() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_183() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_188() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_191() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_193() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        hotelModel.checkRoomsAvailable(new Date(1522706400000L), new Date(1522879200000L), s2);
        scanner.nextInt();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        scanner.nextInt();
        hotelModel.getRoom();
        hotelModel.getRoom();
        hotelModel.getRoom();
    }
    
    @Test
    public void test_201() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
        hotelModel.checkRoomsAvailable(date, date2, s2);
        scanner.nextInt();
        final Object value = hotelModel.getRoom().get(0);
        ((Room)value).getMaxOccupancy();
        scanner.nextInt();
        hotelModel.getRoom();
        ((Room)value).getMaxOccupancy();
        hotelModel.getRoom();
        ((Room)value).getMaxOccupancy();
        scanner.nextInt();
        hotelModel.getRoom();
        ((Room)value).getRoomID();
        hotelModel.getRoom();
        final String roomType = ((Room)value).getRoomType();
        hotelModel.getRoom();
        hotelModel.reserveRoom(4, 2, roomType, 2, 3, date, date2);
    }
    
    @Test
    public void test_205() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
        hotelModel.checkRoomsAvailable(date, date2, s2);
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        ((Room)hotelModel.getRoom().get(0)).getMaxOccupancy();
        scanner.nextInt();
        ((Room)hotelModel.getRoom().get(0)).getRoomID();
        final String roomType = hotelModel.getRoom().get(0).getRoomType();
        hotelModel.getRoom();
        hotelModel.reserveRoom(4, 2, roomType, 2, 3, date, date2);
    }
    
    @Test
    public void test_238() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_243() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_253() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
    }
    
    @Test
    public void test_254() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelModel.getUser();
    }
    
    @Test
    public void test_263() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
    }
    
    @Test
    public void test_272() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
    }
    
    @Test
    public void test_273() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelModel.getUser();
    }
    
    public TestHotelModel() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
