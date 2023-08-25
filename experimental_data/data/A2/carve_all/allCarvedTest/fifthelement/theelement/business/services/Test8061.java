package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8061 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: java.util.List getAlbums()>_2173_4344
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_getAlbums_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice4 = null;
        albumservice4 = new fifthelement.theelement.business.services.AlbumService();
        albumservice4.getAlbums();
        albumservice4.getMostPlayedAlbum();
        albumservice4.getTotalAlbumPlays();
        albumservice4.getAlbums();
    }
}
