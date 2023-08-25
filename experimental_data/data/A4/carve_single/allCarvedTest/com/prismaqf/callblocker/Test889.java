package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test889 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestDeleteRuleWhenUsedShouldFlag/Trace-1651091728932.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onStart()>_69_138
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onStart_001() throws Exception {
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller4 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        activitycontroller4.get();
        org.robolectric.android.controller.ActivityController activitycontroller5 = activitycontroller4.create();
        activitycontroller5.start();
    }
}
