package org.hotelme;

import org.junit.Test;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelController_2
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_5() throws Exception {
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    public TestHotelController_2() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
