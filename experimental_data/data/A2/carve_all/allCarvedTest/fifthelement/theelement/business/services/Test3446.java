package fifthelement.theelement.business.services;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

public class Test3446 {

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.business.services.SongService: void songIsSkipped(java.util.UUID)>_1729_3456
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_business_services_SongService_songIsSkipped_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.business.services.SongService songservice8 = null;
        java.util.UUID uuid49676 = org.mockito.Mockito.mock(java.util.UUID.class);
        org.mockito.stubbing.Stubber stubber15234 = org.mockito.Mockito.doReturn("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid49677 = stubber15234.when(uuid49676);
        uuid49677.toString();
        org.mockito.stubbing.Stubber stubber15235 = org.mockito.Mockito.doReturn("7dc53df5-703e-49b3-8670-b1c468f47f1f");
        java.util.UUID uuid49678 = stubber15235.when(uuid49676);
        uuid49678.toString();
        songservice8 = new fifthelement.theelement.business.services.SongService();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.getSongs();
        songservice8.songIsPlayed(uuid49678);
        songservice8.songIsSkipped(uuid49678);
    }
}
