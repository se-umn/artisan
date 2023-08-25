package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test493 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#checkDisplay/Trace-1651091753437.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onRequestPermissionsResult(int,java.lang.String[],int[])>_79_158
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onRequestPermissionsResult_001() throws Exception {
        java.lang.String[] string0 = null;
        int[] int0 = null;
        string0 = new java.lang.String[0];
        int0 = new int[1];
        int0[0] = 0;
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller13 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager7 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller13.get();
        org.robolectric.android.controller.ActivityController activitycontroller14 = activitycontroller13.create();
        org.robolectric.android.controller.ActivityController activitycontroller15 = activitycontroller14.start();
        activitycontroller15.visible();
        callblockermanager7.onRequestPermissionsResult(103, string0, int0);
    }
}
