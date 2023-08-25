package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8345 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: int getTotalAlbumPlays()>_3282_6562
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_getTotalAlbumPlays_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice9 = null;
        albumservice9 = new fifthelement.theelement.business.services.AlbumService();
        albumservice9.getAlbums();
        albumservice9.getMostPlayedAlbum();
        albumservice9.getTotalAlbumPlays();
        albumservice9.getAlbums();
        albumservice9.getMostPlayedAlbum();
        albumservice9.getTotalAlbumPlays();
        albumservice9.getAlbums();
        albumservice9.getMostPlayedAlbum();
        albumservice9.getTotalAlbumPlays();
    }
}
