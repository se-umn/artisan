package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test2967 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.PlaylistService: void <init>()>_187_373
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_PlaylistService_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.PlaylistService playlistservice0 = null;
        playlistservice0 = new fifthelement.theelement.business.services.PlaylistService();
    }
}
