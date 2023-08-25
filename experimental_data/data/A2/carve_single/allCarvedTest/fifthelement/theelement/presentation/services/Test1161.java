package fifthelement.theelement.presentation.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test1161 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#playPausingTest/Trace-1650046475193.txt
     * Method invocation under test: <fifthelement.theelement.presentation.services.MusicService: void reset()>_353_705
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_services_MusicService_reset_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        org.robolectric.android.controller.ServiceController servicecontroller14 = org.robolectric.Robolectric.buildService(fifthelement.theelement.presentation.services.MusicService.class);
        fifthelement.theelement.presentation.services.MusicService musicservice11 = (fifthelement.theelement.presentation.services.MusicService) servicecontroller14.get();
        org.robolectric.android.controller.ServiceController servicecontroller15 = servicecontroller14.create();
        servicecontroller15.bind();
        musicservice11.reset();
    }
}
