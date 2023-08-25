package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test222 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateFilterRuleTest#TestSelectFromListOfExisting/Trace-1651091776091.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: boolean onCreateOptionsMenu(android.view.Menu)>_80_160
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onCreateOptionsMenu_001() throws Exception {
        int[] int1 = null;
        java.lang.String[] string1 = null;
        int1 = new int[1];
        int1[0] = 0;
        string1 = new java.lang.String[0];
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller12 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager7 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller12.get();
        org.robolectric.android.controller.ActivityController activitycontroller13 = activitycontroller12.create();
        org.robolectric.android.controller.ActivityController activitycontroller14 = activitycontroller13.start();
        callblockermanager7.onRequestPermissionsResult(103, string1, int1);
        activitycontroller14.visible();
    }
}
