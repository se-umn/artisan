package fifthelement.theelement.presentation.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test1706 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#selectTheme3Test/Trace-1650046433616.txt
     * Method invocation under test: <fifthelement.theelement.presentation.services.MusicService: void <init>()>_318_636
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_services_MusicService_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        org.robolectric.android.controller.ServiceController servicecontroller1 = org.robolectric.Robolectric.buildService(fifthelement.theelement.presentation.services.MusicService.class);
        servicecontroller1.get();
    }
}
