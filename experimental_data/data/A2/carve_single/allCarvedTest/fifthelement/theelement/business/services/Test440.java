package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test440 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: void songIsSkipped(java.util.UUID)>_1729_3456
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_songIsSkipped_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice3 = null;
        java.util.UUID uuid4827 = org.mockito.Mockito.mock(java.util.UUID.class);
        org.mockito.stubbing.Stubber stubber894 = org.mockito.Mockito.doReturn("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid4828 = stubber894.when(uuid4827);
        uuid4828.toString();
        org.mockito.stubbing.Stubber stubber895 = org.mockito.Mockito.doReturn("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid4829 = stubber895.when(uuid4827);
        uuid4829.toString();
        songservice3 = new fifthelement.theelement.business.services.SongService();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.getSongs();
        songservice3.songIsPlayed(uuid4829);
        songservice3.songIsSkipped(uuid4829);
    }
}
