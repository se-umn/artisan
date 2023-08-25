package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test445 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onDestroy()>_424_848
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onDestroy_001() throws Exception {
        int[] int3 = null;
        java.lang.String[] string3 = null;
        int3 = new int[1];
        int3[0] = 0;
        string3 = new java.lang.String[0];
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller64 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager11 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller64.get();
        org.robolectric.android.controller.ActivityController activitycontroller65 = activitycontroller64.create();
        org.robolectric.android.controller.ActivityController activitycontroller66 = activitycontroller65.start();
        callblockermanager11.onRequestPermissionsResult(103, string3, int3);
        org.robolectric.android.controller.ActivityController activitycontroller67 = activitycontroller66.visible();
        org.robolectric.android.controller.ActivityController activitycontroller68 = activitycontroller67.stop();
        activitycontroller68.destroy();
    }
}
