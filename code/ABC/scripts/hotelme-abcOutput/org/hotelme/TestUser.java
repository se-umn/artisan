package org.hotelme;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestUser
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_12() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelModel.getUser().getFname();
    }
    
    @Test
    public void test_14() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
        connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        user.getUserID();
    }
    
    @Test
    public void test_23() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
    }
    
    @Test
    public void test_24() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
        new User(3, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname();
    }
    
    @Test
    public void test_26() throws Exception {
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
    
    @Test
    public void test_55() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    
    @Test
    public void test_62() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
        final PreparedStatement prepareStatement2 = connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        user.getUserID();
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
        prepareStatement3.setString(1, s3);
        prepareStatement3.setDate(2, date);
        prepareStatement3.setDate(3, date2);
        final ResultSet executeQuery2 = prepareStatement3.executeQuery();
        executeQuery2.next();
        executeQuery2.getInt("roomID");
        executeQuery2.getInt("price");
        executeQuery2.getInt("maxOccupancy");
        user.getUserID();
    }
    
    @Test
    public void test_100() throws Exception {
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
        hotelModel.getUser().getFname();
    }
    
    @Test
    public void test_105() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        final User user = new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw"));
    }
    
    @Test
    public void test_106() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname();
    }
    
    @Test
    public void test_145() throws Exception {
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
        hotelModel.getUser().getFname();
    }
    
    @Test
    public void test_150() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        final User user = new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw"));
    }
    
    @Test
    public void test_151() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname();
    }
    
    @Test
    public void test_163() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelModel hotelModel = new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd"));
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String[] split = (scanner.next() + " " + scanner.next()).split(" ");
        hotelModel.userLogin(split[0], split[1]);
        hotelModel.getUser().getFname();
    }
    
    @Test
    public void test_165() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1) });
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
        connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        user.getUserID();
    }
    
    @Test
    public void test_173() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
    }
    
    @Test
    public void test_175() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234" });
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
        new User(3, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname();
    }
    
    @Test
    public void test_177() throws Exception {
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
    
    @Test
    public void test_204() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
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
    
    @Test
    public void test_211() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1) });
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
        final PreparedStatement prepareStatement2 = connection.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
        user.getUserID();
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
        final Date date = new Date(1522706400000L);
        final Date date2 = new Date(1522879200000L);
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
        prepareStatement3.setString(1, s3);
        prepareStatement3.setDate(2, date);
        prepareStatement3.setDate(3, date2);
        final ResultSet executeQuery2 = prepareStatement3.executeQuery();
        executeQuery2.next();
        executeQuery2.getInt("roomID");
        executeQuery2.getInt("price");
        executeQuery2.getInt("maxOccupancy");
        user.getUserID();
    }
    
    @Test
    public void test_255() throws Exception {
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
        hotelModel.getUser().getFname();
    }
    
    @Test
    public void test_260() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        final User user = new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw"));
    }
    
    @Test
    public void test_261() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDb();
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname();
    }
    
    @Test
    public void test_274() throws Exception {
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
        hotelModel.getUser().getFname();
    }
    
    @Test
    public void test_279() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        final User user = new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw"));
    }
    
    @Test
    public void test_280() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234" });
        final InputStream in = System.in;
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final Scanner scanner = new Scanner(in);
        scanner.nextInt();
        final String next = scanner.next();
        final String next2 = scanner.next();
        final String next3 = scanner.next();
        final String next4 = scanner.next();
        final StringBuilder append = new StringBuilder().append(next);
        final String s = " ";
        final String[] split = append.append(s).append(next2).append(s).append(next3).append(s).append(next4).toString().split(" ");
        final String s2 = split[0];
        final String s3 = split[1];
        final String s4 = split[2];
        final String s5 = split[3];
        final PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement.setString(1, s4);
        prepareStatement.executeQuery().next();
        final PreparedStatement prepareStatement2 = connection.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
        prepareStatement2.setString(1, s2);
        prepareStatement2.setString(2, s3);
        prepareStatement2.setString(3, s4);
        prepareStatement2.setString(4, s5);
        prepareStatement2.executeUpdate();
        final PreparedStatement prepareStatement3 = connection.prepareStatement("SELECT * FROM users WHERE uname = ?");
        prepareStatement3.setString(1, s4);
        final ResultSet executeQuery = prepareStatement3.executeQuery();
        executeQuery.next();
        executeQuery.getInt("userID");
        new User(1, executeQuery.getString("fname"), executeQuery.getString("lname"), executeQuery.getString("uname"), executeQuery.getString("pw")).getFname();
    }
    
    public TestUser() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
