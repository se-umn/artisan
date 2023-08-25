package fifthelement.theelement.presentation.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test10112 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#searchSongsTest/Trace-1650046542039.txt
     * Method invocation under test: <fifthelement.theelement.presentation.services.MusicService: boolean isPlaying()>_370_738
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_services_MusicService_isPlaying_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        org.robolectric.android.controller.ServiceController servicecontroller22 = org.robolectric.Robolectric.buildService(fifthelement.theelement.presentation.services.MusicService.class);
        fifthelement.theelement.presentation.services.MusicService musicservice18 = (fifthelement.theelement.presentation.services.MusicService) servicecontroller22.get();
        org.robolectric.android.controller.ServiceController servicecontroller23 = servicecontroller22.create();
        servicecontroller23.bind();
        musicservice18.reset();
        musicservice18.isPlaying();
    }
}
