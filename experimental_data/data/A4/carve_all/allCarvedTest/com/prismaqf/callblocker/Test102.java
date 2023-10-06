package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test102 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestDeleteRuleWhenUsedShouldFlag/Trace-1651091728932.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: void onCreate(android.os.Bundle)>_85_170
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onCreate_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller17 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class);
        activitycontroller17.get();
        activitycontroller17.create();
    }
}