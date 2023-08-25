package fifthelement.theelement.presentation.helpers;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import java.io.File;
import fifthelement.theelement.utils.TestDatabaseUtil;

@RunWith(RobolectricTestRunner.class)
public class Test8891 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/FifthElement/traces/fifthelement.theelement.presentation.activities.CustomizeTests#deleteSong/Trace-1650046523752.txt
     * Method invocation under test: <fifthelement.theelement.presentation.helpers.ToastHelper: void <init>(android.content.Context)>_1814_3625
     */
    @Test(timeout = 4000)
    public void test_fifthelement_theelement_presentation_helpers_ToastHelper_constructor_001() throws Exception {
        File tempDB = TestDatabaseUtil.copyDB();
        fifthelement.theelement.presentation.helpers.ToastHelper toasthelper0 = null;
        android.content.Context context107 = ApplicationProvider.getApplicationContext();
        toasthelper0 = new fifthelement.theelement.presentation.helpers.ToastHelper(context107);
    }
}
