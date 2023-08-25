package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test017 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestCheckingPersistsWithRotation/Trace-1651091676947.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilterRule: void onCreate(android.os.Bundle)>_7_14
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilterRule_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        intent0 = new android.content.Intent();
        org.robolectric.android.controller.ActivityController activitycontroller1 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilterRule.class, intent0);
        activitycontroller1.get();
        activitycontroller1.create();
    }
}
