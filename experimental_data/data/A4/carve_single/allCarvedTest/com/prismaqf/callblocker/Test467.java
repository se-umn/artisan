package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test467 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheFilterRule/Trace-1651091813651.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onRequestPermissionsResult(int,java.lang.String[],int[])>_76_152
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onRequestPermissionsResult_001() throws Exception {
        int[] int0 = null;
        java.lang.String[] string0 = null;
        int0 = new int[1];
        int0[0] = 0;
        string0 = new java.lang.String[0];
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager5 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller8.get();
        org.robolectric.android.controller.ActivityController activitycontroller9 = activitycontroller8.create();
        activitycontroller9.start();
        callblockermanager5.onRequestPermissionsResult(103, string0, int0);
    }
}
