package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test681 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateFilterRuleTest#TestChangeAction/Trace-1651091773967.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onDestroy()>_378_756
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onDestroy_001() throws Exception {
        int[] int2 = null;
        java.lang.String[] string2 = null;
        int2 = new int[1];
        int2[0] = 0;
        string2 = new java.lang.String[0];
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller34 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager11 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller34.get();
        org.robolectric.android.controller.ActivityController activitycontroller35 = activitycontroller34.create();
        org.robolectric.android.controller.ActivityController activitycontroller36 = activitycontroller35.start();
        org.robolectric.android.controller.ActivityController activitycontroller37 = activitycontroller36.visible();
        callblockermanager11.onRequestPermissionsResult(103, string2, int2);
        org.robolectric.android.controller.ActivityController activitycontroller38 = activitycontroller37.stop();
        activitycontroller38.destroy();
    }
}
