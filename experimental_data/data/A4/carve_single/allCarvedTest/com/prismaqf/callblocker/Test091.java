package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test091 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#PickActionTest/Trace-1651091791193.txt
     * Method invocation under test: <com.prismaqf.callblocker.PickAction: void onCreate(android.os.Bundle)>_334_668
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_PickAction_onCreate_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller42 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.PickAction.class);
        activitycontroller42.get();
        activitycontroller42.create();
    }
}
