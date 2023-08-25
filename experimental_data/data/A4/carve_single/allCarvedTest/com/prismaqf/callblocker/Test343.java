package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test343 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#testStartServiceWhenInBackground/Trace-1651091754395.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onStop()>_83_166
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onStop_001() throws Exception {
        java.lang.String[] string2 = null;
        int[] int2 = null;
        string2 = new java.lang.String[0];
        int2 = new int[1];
        int2[0] = 0;
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller17 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager9 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller17.get();
        org.robolectric.android.controller.ActivityController activitycontroller18 = activitycontroller17.create();
        org.robolectric.android.controller.ActivityController activitycontroller19 = activitycontroller18.start();
        callblockermanager9.onRequestPermissionsResult(103, string2, int2);
        org.robolectric.android.controller.ActivityController activitycontroller20 = activitycontroller19.visible();
        activitycontroller20.stop();
    }
}
