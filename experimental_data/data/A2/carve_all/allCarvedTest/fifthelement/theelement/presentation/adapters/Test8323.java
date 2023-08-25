package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test8323 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_3132_6264
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_042() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter1228 = null;
        java.util.ArrayList arraylist3761 = null;
        android.content.Context context245 = ApplicationProvider.getApplicationContext();
        arraylist3761 = new java.util.ArrayList();
        arraylist3761.iterator();
        songslistadapter1228 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context245, arraylist3761);
        songslistadapter1228.getCount();
        songslistadapter1228.getCount();
        songslistadapter1228.getCount();
    }
}
