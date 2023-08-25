package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test769 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.MusicLibraryTests#playSongFromSongsList/Trace-1650046532385.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: void songIsPlayed(java.util.UUID)>_1403_2805
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_songIsPlayed_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice2 = null;
        java.util.UUID uuid1807 = org.mockito.Mockito.mock(java.util.UUID.class);
        org.mockito.stubbing.Stubber stubber883 = org.mockito.Mockito.doReturn("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid1808 = stubber883.when(uuid1807);
        uuid1808.toString();
        songservice2 = new fifthelement.theelement.business.services.SongService();
        songservice2.getSongs();
        songservice2.getSongs();
        songservice2.getSongs();
        songservice2.getSongs();
        songservice2.getSongs();
        songservice2.getSongs();
        songservice2.songIsPlayed(uuid1808);
    }
}
