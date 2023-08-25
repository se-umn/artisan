package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3348 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: void songIsPlayed(java.util.UUID)>_1403_2805
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_songIsPlayed_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice7 = null;
        java.util.UUID uuid35250 = org.mockito.Mockito.mock(java.util.UUID.class);
        org.mockito.stubbing.Stubber stubber15223 = org.mockito.Mockito.doReturn("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid35251 = stubber15223.when(uuid35250);
        uuid35251.toString();
        songservice7 = new fifthelement.theelement.business.services.SongService();
        songservice7.getSongs();
        songservice7.getSongs();
        songservice7.getSongs();
        songservice7.getSongs();
        songservice7.getSongs();
        songservice7.getSongs();
        songservice7.songIsPlayed(uuid35251);
    }
}
