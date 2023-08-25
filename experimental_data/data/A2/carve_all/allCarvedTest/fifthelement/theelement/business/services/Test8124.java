package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8124 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: int getTotalAlbumPlays()>_2383_4764
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_getTotalAlbumPlays_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice6 = null;
        albumservice6 = new fifthelement.theelement.business.services.AlbumService();
        albumservice6.getAlbums();
        albumservice6.getMostPlayedAlbum();
        albumservice6.getTotalAlbumPlays();
        albumservice6.getAlbums();
        albumservice6.getMostPlayedAlbum();
        albumservice6.getTotalAlbumPlays();
    }
}
