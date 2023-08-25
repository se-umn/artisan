package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test1314 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#checkStatsPage/Trace-1650046501797.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: fifthelement.theelement.objects.Song getMostPlayedSong()>_632_1262
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_getMostPlayedSong_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice4 = null;
        songservice4 = new fifthelement.theelement.business.services.SongService();
        songservice4.getSongs();
        songservice4.getSongs();
        songservice4.getSongs();
        songservice4.getMostPlayedSong();
    }
}
