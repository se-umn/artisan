package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test678 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateFilterRuleTest#TestChangeAction/Trace-1651091773967.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: boolean onCreateOptionsMenu(android.view.Menu)>_139_278
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller21 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class);
        activitycontroller21.get();
        org.robolectric.android.controller.ActivityController activitycontroller22 = activitycontroller21.create();
        activitycontroller22.visible();
    }
}
