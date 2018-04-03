package org.hotelme;

import org.junit.Test;
import java.sql.DriverManager;
import org.hotelme.utils.SystemTestUtils;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

public class TestHotelController
{
    @Rule
    public final TextFromStandardInputStream userInputs;
    @Rule
    public final ExpectedSystemExit expectedSystemExit;
    
    @Test
    public void test_3() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_5() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_91() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_92() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDb();
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_110() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_111() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd", Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_120() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_121() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Mikaela", "Burkhardt", "mb", "qweasd", Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_130() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_131() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDb();
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_135() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_137() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_154() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_156() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(1), "test", "1234", Integer.toString(1), Integer.toString(2), "04/03/18", "04/05/18", Integer.toString(1), Integer.toString(1), Integer.toString(2), Integer.toString(2), Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/hotelme.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_240() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_241() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDb();
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_245() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDb();
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_247() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDb();
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    @Test
    public void test_264() throws Exception {
        this.userInputs.provideLines(new String[0]);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        final HotelController hotelController = new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView());
    }
    
    @Test
    public void test_266() throws Exception {
        this.userInputs.provideLines(new String[] { Integer.toString(2), "Jimbo", "Jambo", "jimbo-jambo", "1234", Integer.toString(5), Integer.toString(3) });
        this.expectedSystemExit.expectSystemExitWithStatus(0);
        SystemTestUtils.dropAndRecreateTheDbFromResource("/emptydb.sql");
        new HotelController(new HotelModel(DriverManager.getConnection("jdbc:mysql://localhost:3306/hotelme", "hotelmanager", "qweasd")), new HotelView()).start();
    }
    
    public TestHotelController() {
        this.userInputs = TextFromStandardInputStream.emptyStandardInputStream();
        this.expectedSystemExit = ExpectedSystemExit.none();
    }
}
