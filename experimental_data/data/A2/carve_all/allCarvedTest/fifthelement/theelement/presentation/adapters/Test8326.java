package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test8326 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: int getCount()>_3138_6276
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_getCount_045() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter1231 = null;
        java.util.ArrayList arraylist3764 = null;
        android.content.Context context251 = ApplicationProvider.getApplicationContext();
        arraylist3764 = new java.util.ArrayList();
        arraylist3764.iterator();
        songslistadapter1231 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context251, arraylist3764);
        songslistadapter1231.getCount();
        songslistadapter1231.getCount();
        songslistadapter1231.getCount();
        songslistadapter1231.getCount();
        songslistadapter1231.getCount();
        songslistadapter1231.getCount();
    }
}
