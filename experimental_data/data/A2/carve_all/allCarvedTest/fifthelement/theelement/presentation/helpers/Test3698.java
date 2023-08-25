package fifthelement.theelement.presentation.helpers;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test3698 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.PlayMusicTests#skipSongsTest/Trace-1650046483856.txt
     * Method invocation under test: <fifthelement.theelement.presentation.helpers.ToastHelper: void sendToast(java.lang.String)>_2346_4691
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_helpers_ToastHelper_sendToast_003() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.helpers.ToastHelper toasthelper39 = null;
        android.content.Context context229 = ApplicationProvider.getApplicationContext();
        toasthelper39 = new fifthelement.theelement.presentation.helpers.ToastHelper(context229);
        toasthelper39.sendToast("Now Playing: Adventure of a Lifetime");
        toasthelper39.sendToast("Now Playing: Classical Music");
        toasthelper39.sendToast("Now Playing: Adventure of a Lifetime");
    }
}
