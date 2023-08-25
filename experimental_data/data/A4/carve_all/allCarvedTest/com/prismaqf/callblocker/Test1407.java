package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1407 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#checkThatTurningOnServiceChangesText/Trace-1651091761666.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallDetectService: void <init>()>_108_216
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallDetectService_constructor_001() throws Exception {
        org.robolectric.android.controller.ServiceController servicecontroller1 = org.robolectric.Robolectric.buildService(com.prismaqf.callblocker.CallDetectService.class);
        servicecontroller1.get();
    }
}
