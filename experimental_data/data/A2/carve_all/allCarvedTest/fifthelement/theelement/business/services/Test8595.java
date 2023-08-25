package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test8595 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: int getTotalSongPlays()>_879_1756
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_getTotalSongPlays_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice5 = null;
        songservice5 = new fifthelement.theelement.business.services.SongService();
        songservice5.getSongs();
        songservice5.getSongs();
        songservice5.getSongs();
        songservice5.getMostPlayedSong();
        songservice5.getTotalSongPlays();
    }
}
