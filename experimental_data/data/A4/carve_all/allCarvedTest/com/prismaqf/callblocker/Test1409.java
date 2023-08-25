package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1409 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.CallBlockerManagerTest#checkThatTurningOnServiceChangesText/Trace-1651091761666.txt
     * Method invocation under test: <com.prismaqf.callblocker.CallDetectService: void onDestroy()>_225_450
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_CallDetectService_onDestroy_001() throws Exception {
        ApplicationProvider.getApplicationContext();
        org.robolectric.android.controller.ServiceController servicecontroller6 = org.robolectric.Robolectric.buildService(com.prismaqf.callblocker.CallDetectService.class);
        com.prismaqf.callblocker.CallDetectService calldetectservice5 = (com.prismaqf.callblocker.CallDetectService) servicecontroller6.get();
        org.robolectric.android.controller.ServiceController servicecontroller7 = servicecontroller6.startCommand(0, 1);
        calldetectservice5.getString(2131558495);
        calldetectservice5.getString(2131558495);
        servicecontroller7.destroy();
    }
}
