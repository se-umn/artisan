package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test9424 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#shuffleSongTest/Trace-1650046488177.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: void <init>()>_1227_2451
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice0 = null;
        albumservice0 = new fifthelement.theelement.business.services.AlbumService();
    }
}
