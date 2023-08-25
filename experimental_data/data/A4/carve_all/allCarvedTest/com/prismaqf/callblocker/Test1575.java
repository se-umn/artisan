package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1575 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheFilterRule/Trace-1651091813651.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onDestroy()>_862_1724
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onDestroy_001() throws Exception {
        int[] int3 = null;
        java.lang.String[] string3 = null;
        int3 = new int[1];
        int3[0] = 0;
        string3 = new java.lang.String[0];
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller71 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager11 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller71.get();
        org.robolectric.android.controller.ActivityController activitycontroller72 = activitycontroller71.create();
        org.robolectric.android.controller.ActivityController activitycontroller73 = activitycontroller72.start();
        callblockermanager11.onRequestPermissionsResult(103, string3, int3);
        org.robolectric.android.controller.ActivityController activitycontroller74 = activitycontroller73.visible();
        org.robolectric.android.controller.ActivityController activitycontroller75 = activitycontroller74.stop();
        activitycontroller75.destroy();
    }
}
