package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3294 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: java.util.List getSongs()>_1254_2507
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_getSongs_006() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice6 = null;
        songservice6 = new fifthelement.theelement.business.services.SongService();
        songservice6.getSongs();
        songservice6.getSongs();
        songservice6.getSongs();
        songservice6.getSongs();
        songservice6.getSongs();
        songservice6.getSongs();
    }
}
