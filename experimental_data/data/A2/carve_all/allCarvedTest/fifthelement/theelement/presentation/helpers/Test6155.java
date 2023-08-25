package fifthelement.theelement.presentation.helpers;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test6155 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#playSongWithSongPlayedCheck/Trace-1650046467562.txt
     * Method invocation under test: <fifthelement.theelement.presentation.helpers.ToastHelper: void sendToast(java.lang.String)>_2880_5759
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_helpers_ToastHelper_sendToast_002() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.helpers.ToastHelper toasthelper5 = null;
        android.content.Context context227 = ApplicationProvider.getApplicationContext();
        toasthelper5 = new fifthelement.theelement.presentation.helpers.ToastHelper(context227);
        toasthelper5.sendToast("Now Playing: Adventure of a Lifetime");
        toasthelper5.sendToast("Now Playing: Adventure of a Lifetime");
    }
}
