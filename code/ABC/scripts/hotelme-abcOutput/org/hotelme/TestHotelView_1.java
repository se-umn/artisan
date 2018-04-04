package org.hotelme;

import org.junit.Test;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelView_1
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_2() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd");
        final HotelView hotelView = new HotelView();
    }
    
    public TestHotelView_1() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}