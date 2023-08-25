package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1277 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#UndoActionTest/Trace-1651091801855.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onDestroy()>_541_1082
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onDestroy_001() throws Exception {
        java.lang.String[] string2 = null;
        int[] int2 = null;
        string2 = new java.lang.String[0];
        int2 = new int[1];
        int2[0] = 0;
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller65 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager11 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller65.get();
        org.robolectric.android.controller.ActivityController activitycontroller66 = activitycontroller65.create();
        org.robolectric.android.controller.ActivityController activitycontroller67 = activitycontroller66.start();
        org.robolectric.android.controller.ActivityController activitycontroller68 = activitycontroller67.visible();
        callblockermanager11.onRequestPermissionsResult(103, string2, int2);
        org.robolectric.android.controller.ActivityController activitycontroller69 = activitycontroller68.stop();
        activitycontroller69.destroy();
    }
}
