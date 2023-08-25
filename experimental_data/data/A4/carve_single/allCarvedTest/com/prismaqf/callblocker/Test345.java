package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test345 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#testStartServiceWhenInBackground/Trace-1651091754395.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallDetectService: int onStartCommand(android.content.Intent,int,int)>_102_204
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallDetectService_onStartCommand_001() throws Exception {
        org.robolectric.android.controller.ServiceController servicecontroller3 = org.robolectric.Robolectric.buildService(com.prismaqf.callblocker.CallDetectService.class);
        servicecontroller3.get();
        servicecontroller3.startCommand(0, 1);
    }
}
