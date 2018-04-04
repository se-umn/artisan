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

public class TestUser_2
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_18() throws Exception {
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
    
    public TestUser_2() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
