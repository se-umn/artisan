package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8338 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: fifthelement.theelement.objects.Album getMostPlayedAlbum()>_3233_6464
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_getMostPlayedAlbum_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice8 = null;
        albumservice8 = new fifthelement.theelement.business.services.AlbumService();
        albumservice8.getAlbums();
        albumservice8.getMostPlayedAlbum();
        albumservice8.getTotalAlbumPlays();
        albumservice8.getAlbums();
        albumservice8.getMostPlayedAlbum();
        albumservice8.getTotalAlbumPlays();
        albumservice8.getAlbums();
        albumservice8.getMostPlayedAlbum();
    }
}
