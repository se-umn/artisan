package fifthelement.theelement.presentation.adapters;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test8320 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.presentation.adapters.SongsListAdapter: void <init>(android.content.Context,java.util.List)>_3123_6244
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_adapters_SongsListAdapter_constructor_009() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.adapters.SongsListAdapter songslistadapter1225 = null;
        java.util.ArrayList arraylist3758 = null;
        android.content.Context context239 = ApplicationProvider.getApplicationContext();
        arraylist3758 = new java.util.ArrayList();
        arraylist3758.iterator();
        songslistadapter1225 = new fifthelement.theelement.presentation.adapters.SongsListAdapter(context239, arraylist3758);
    }
}
