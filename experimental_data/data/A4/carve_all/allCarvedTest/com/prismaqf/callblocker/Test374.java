package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test374 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#checkDisplay/Trace-1651091753437.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onStop()>_83_166
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onStop_001() throws Exception {
        java.lang.String[] string1 = null;
        int[] int1 = null;
        string1 = new java.lang.String[0];
        int1 = new int[1];
        int1[0] = 0;
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ActivityController activitycontroller18 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        com.prismaqf.callblocker.CallBlockerManager callblockermanager9 = (com.prismaqf.callblocker.CallBlockerManager) activitycontroller18.get();
        org.robolectric.android.controller.ActivityController activitycontroller19 = activitycontroller18.create();
        org.robolectric.android.controller.ActivityController activitycontroller20 = activitycontroller19.start();
        org.robolectric.android.controller.ActivityController activitycontroller21 = activitycontroller20.visible();
        callblockermanager9.onRequestPermissionsResult(103, string1, int1);
        activitycontroller21.stop();
    }
}
