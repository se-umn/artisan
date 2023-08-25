package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test575 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#playSongWithSongPlayedCheck/Trace-1650046505062.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: int getTotalSongPlays()>_879_1756
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_getTotalSongPlays_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice3 = null;
        songservice3 = new fifthelement.theelement.business.services.SongService();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.getMostPlayedSong();
        songservice3.getTotalSongPlays();
    }
}
