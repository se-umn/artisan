package fifthelement.theelement.presentation.helpers;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test945 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.SettingsTest#deleteSongsTest/Trace-1650046437980.txt
     * Method invocation under test: <fifthelement.theelement.presentation.helpers.ToastHelper: void sendToast(java.lang.String)>_1817_3632
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_helpers_ToastHelper_sendToast_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.helpers.ToastHelper toasthelper1 = null;
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
        toasthelper1 = new fifthelement.theelement.presentation.helpers.ToastHelper(context19);
        toasthelper1.sendToast("Deleted Adventure of a Lifetime");
    }
}