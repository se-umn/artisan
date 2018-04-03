package org.hotelme;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelView
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_1() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_4() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_6() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signIn(scanner);
    }
    
    @Test
    public void test_7() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signIn(scanner).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelView.signInSuccess(hotelModel.getUser().getFname());
    }
    
    @Test
    public void test_8() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signIn(scanner).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelView.signInSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
    }
    
    @Test
    public void test_10() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
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
    }
    
    @Test
    public void test_15() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_16() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_17() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18" });
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
        hotelView.checkInDate(scanner);
    }
    
    @Test
    public void test_18() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18" });
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
    }
    
    @Test
    public void test_19() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
    
    @Test
    public void test_20() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
    }
    
    @Test
    public void test_21() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
    }
    
    @Test
    public void test_22() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_27() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_29() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_33() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18" });
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
        hotelView.checkInDate(scanner);
    }
    
    @Test
    public void test_34() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18" });
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
    }
    
    @Test
    public void test_35() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
    
    @Test
    public void test_54() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_57() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_58() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    
    @Test
    public void test_61() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_63() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_64() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_65() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
    }
    
    @Test
    public void test_67() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
    }
    
    @Test
    public void test_68() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_71() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_81() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_83() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_85() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_87() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    
    @Test
    public void test_88() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_90() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_93() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signUp(scanner);
    }
    
    @Test
    public void test_94() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
    }
    
    @Test
    public void test_95() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
    }
    
    @Test
    public void test_96() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_97() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_101() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
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
    }
    
    @Test
    public void test_102() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
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
    }
    
    @Test
    public void test_103() throws Exception {
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
    
    @Test
    public void test_104() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
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
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_107() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_109() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_112() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signUp(scanner);
    }
    
    @Test
    public void test_113() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
    }
    
    @Test
    public void test_114() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_115() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd", Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_117() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_119() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_122() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signUp(scanner);
    }
    
    @Test
    public void test_123() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
    }
    
    @Test
    public void test_124() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_125() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd", Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        final String s = split[0];
        final String s2 = split[1];
        final String s3 = split[2];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s3);
        prepareStatement.executeQuery().next();
        hotelView.signUpFailure();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_127() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_129() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_132() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_134() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_136() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_138() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signUp(scanner);
    }
    
    @Test
    public void test_139() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
    }
    
    @Test
    public void test_140() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
    }
    
    @Test
    public void test_141() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_142() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_146() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
    }
    
    @Test
    public void test_147() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
    }
    
    @Test
    public void test_148() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
    
    @Test
    public void test_149() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_152() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_155() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_157() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signIn(scanner);
    }
    
    @Test
    public void test_158() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signIn(scanner).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelView.signInSuccess(hotelModel.getUser().getFname());
    }
    
    @Test
    public void test_159() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signIn(scanner).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelView.signInSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
    }
    
    @Test
    public void test_162() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
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
    }
    
    @Test
    public void test_166() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_167() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_168() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18" });
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
        hotelView.checkInDate(scanner);
    }
    
    @Test
    public void test_169() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18" });
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
    }
    
    @Test
    public void test_170() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
    
    @Test
    public void test_171() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
    }
    
    @Test
    public void test_172() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
    }
    
    @Test
    public void test_174() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_178() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_180() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
    }
    
    @Test
    public void test_184() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18" });
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
        hotelView.checkInDate(scanner);
    }
    
    @Test
    public void test_185() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18" });
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
    }
    
    @Test
    public void test_187() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
    
    @Test
    public void test_203() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_206() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_208() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    
    @Test
    public void test_210() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_212() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_213() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_214() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
    }
    
    @Test
    public void test_215() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
    }
    
    @Test
    public void test_217() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_220() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
        final PreparedStatement prepareStatement3 = connection.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
        prepareStatement3.setInt(1, 3);
        prepareStatement3.setInt(2, 4);
        prepareStatement3.setInt(3, 2);
        prepareStatement3.setInt(4, 2);
        prepareStatement3.setDate(5, date);
        prepareStatement3.setDate(6, date2);
        prepareStatement3.executeUpdate();
        final PreparedStatement prepareStatement4 = connection.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
        prepareStatement4.setInt(1, 4);
        prepareStatement4.setInt(2, 3);
        prepareStatement4.executeQuery().next();
        hotelView.successfulRoomReg();
        hotelView.optionsMenu(true);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_230() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_232() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_234() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    }
    
    @Test
    public void test_236() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    
    @Test
    public void test_237() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_239() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_242() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_244() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_246() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_248() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signUp(scanner);
    }
    
    @Test
    public void test_249() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
    }
    
    @Test
    public void test_250() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
    }
    
    @Test
    public void test_251() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_252() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_256() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
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
    }
    
    @Test
    public void test_257() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
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
    }
    
    @Test
    public void test_258() throws Exception {
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
    
    @Test
    public void test_259() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
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
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_262() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    @Test
    public void test_265() throws Exception {
        this.userInputs.provideLines(new String[0]);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
    }
    
    @Test
    public void test_267() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.signUp(scanner);
    }
    
    @Test
    public void test_268() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
    }
    
    @Test
    public void test_269() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
    }
    
    @Test
    public void test_270() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
    }
    
    @Test
    public void test_271() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final HotelView hotelView = new HotelView();
        final Scanner scanner = new Scanner(in);
        hotelView.mainMenu();
        scanner.nextInt();
        final String[] split = hotelView.signUp(scanner).split(" ");
        hotelModel.userSignUp(split[0], split[1], split[2], split[3]);
        hotelView.signUpSuccess(hotelModel.getUser().getFname());
        hotelView.optionsMenu(false);
        scanner.nextInt();
        hotelView.mainMenu();
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    @Test
    public void test_275() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
    }
    
    @Test
    public void test_276() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
    }
    
    @Test
    public void test_277() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5) });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
    
    @Test
    public void test_278() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
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
        scanner.nextInt();
        hotelView.exitMessage();
    }
    
    public TestHotelView() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
