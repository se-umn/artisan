package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test2352 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#skipSongWithSongPlayedCheck/Trace-1650046455133.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: java.util.List getSongs()>_1955_3909
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_getSongs_007() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice9 = null;
        songservice9 = new fifthelement.theelement.business.services.SongService();
        songservice9.getSongs();
        songservice9.getSongs();
        songservice9.getSongs();
        songservice9.getMostPlayedSong();
        songservice9.getTotalSongPlays();
        songservice9.getSongs();
        songservice9.getSongs();
        songservice9.getSongs();
        songservice9.getSongs();
    }
}
