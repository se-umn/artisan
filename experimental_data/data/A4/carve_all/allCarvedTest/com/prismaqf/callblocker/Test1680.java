package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1680 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateFilterRuleTest#TestSelectFromListOfExisting/Trace-1651091776091.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: boolean onCreateOptionsMenu(android.view.Menu)>_139_278
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller20 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class);
        activitycontroller20.get();
        org.robolectric.android.controller.ActivityController activitycontroller21 = activitycontroller20.create();
        activitycontroller21.visible();
    }
}
