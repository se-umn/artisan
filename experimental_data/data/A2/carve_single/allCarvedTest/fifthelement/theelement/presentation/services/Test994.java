package fifthelement.theelement.presentation.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test994 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.presentation.services.MusicService: android.os.IBinder onBind(android.content.Intent)>_337_674
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_services_MusicService_onBind_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        org.robolectric.android.controller.ServiceController servicecontroller6 = org.robolectric.Robolectric.buildService(fifthelement.theelement.presentation.services.MusicService.class);
        servicecontroller6.get();
        org.robolectric.android.controller.ServiceController servicecontroller7 = servicecontroller6.create();
        servicecontroller7.bind();
    }
}
