package org.hotelme;

import org.junit.Test;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelController_10
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_136() throws Exception {
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd", Integer.toString(3) });
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    public TestHotelController_10() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
