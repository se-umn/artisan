package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8639 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.AlbumService: int getTotalAlbumPlays()>_997_1992
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_AlbumService_getTotalAlbumPlays_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.AlbumService albumservice3 = null;
        albumservice3 = new fifthelement.theelement.business.services.AlbumService();
        albumservice3.getAlbums();
        albumservice3.getMostPlayedAlbum();
        albumservice3.getTotalAlbumPlays();
    }
}
