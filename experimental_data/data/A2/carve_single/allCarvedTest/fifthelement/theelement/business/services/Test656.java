package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test656 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: java.util.List getAlbums()>_583_1164
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_getAlbums_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice1 = null;
        albumservice1 = new fifthelement.theelement.business.services.AlbumService();
        albumservice1.getAlbums();
    }
}
