package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1802 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1651091791193.txt
     * Method invocation under test: <com.prismaqf.callblocker.PickAction: boolean onCreateOptionsMenu(android.view.Menu)>_3522_7044
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_PickAction_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller49 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.PickAction.class);
        com.prismaqf.callblocker.PickAction pickaction3 = (com.prismaqf.callblocker.PickAction) activitycontroller49.get();
        org.robolectric.android.controller.ActivityController activitycontroller50 = activitycontroller49.create();
        pickaction3.getApplicationInfo();
        pickaction3.getClassLoader();
        activitycontroller50.visible();
    }
}
