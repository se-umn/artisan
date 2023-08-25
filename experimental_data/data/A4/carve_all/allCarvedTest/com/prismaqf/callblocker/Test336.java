package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test336 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#checkStatusOfService/Trace-1651091763362.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallBlockerManager: void onCreate(android.os.Bundle)>_8_16
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallBlockerManager_onCreate_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller1 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.CallBlockerManager.class);
        activitycontroller1.get();
        activitycontroller1.create();
    }
}
