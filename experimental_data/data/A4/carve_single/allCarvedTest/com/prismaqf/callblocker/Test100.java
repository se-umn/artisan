package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test100 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1651091791193.txt
     * Method invocation under test: <com.prismaqf.callblocker.PickAction: boolean onCreateOptionsMenu(android.view.Menu)>_3522_7044
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_PickAction_onCreateOptionsMenu_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller45 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.PickAction.class);
        com.prismaqf.callblocker.PickAction pickaction3 = (com.prismaqf.callblocker.PickAction) activitycontroller45.get();
        org.robolectric.android.controller.ActivityController activitycontroller46 = activitycontroller45.create();
        pickaction3.getApplicationInfo();
        pickaction3.getClassLoader();
        activitycontroller46.visible();
    }
}
