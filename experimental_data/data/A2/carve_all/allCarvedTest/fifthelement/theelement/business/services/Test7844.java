package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test7844 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: java.util.List getSongs()>_1372_2742
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_getSongs_006() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice8 = null;
        songservice8 = new fifthelement.theelement.business.services.SongService();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getMostPlayedSong();
        songservice8.getTotalSongPlays();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getSongs();
    }
}
