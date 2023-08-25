package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test544 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestUndoAction/Trace-1651091721515.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: boolean onCreateOptionsMenu(android.view.Menu)>_139_278
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller21 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class);
        activitycontroller21.get();
        org.robolectric.android.controller.ActivityController activitycontroller22 = activitycontroller21.create();
        activitycontroller22.visible();
    }
}
