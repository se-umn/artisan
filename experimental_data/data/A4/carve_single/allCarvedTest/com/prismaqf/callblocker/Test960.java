package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test960 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#NavigateUpIsContextSensitive/Trace-1651091805625.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onStop()>_141_282
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onStop_001() throws Exception {
        int[] int2 = null;
        java.lang.String[] string2 = null;
        int2 = new int[1];
        int2[0] = 0;
        string2 = new java.lang.String[0];
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller24 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager9 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller24.get();
        org.robolectric.android.controller.ActivityController activitycontroller25 = activitycontroller24.create();
        org.robolectric.android.controller.ActivityController activitycontroller26 = activitycontroller25.start();
        callblockermanager9.onRequestPermissionsResult(103, string2, int2);
        org.robolectric.android.controller.ActivityController activitycontroller27 = activitycontroller26.visible();
        activitycontroller27.stop();
    }
}
