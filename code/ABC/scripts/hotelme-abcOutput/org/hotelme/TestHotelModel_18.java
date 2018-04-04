package org.hotelme;

import org.junit.Test;
import java.io.InputStream;
import java.util.Scanner;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelModel_18
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_117() throws Exception {
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
    
    public TestHotelModel_18() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
