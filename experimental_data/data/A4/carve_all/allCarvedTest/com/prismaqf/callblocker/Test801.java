package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test801 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateFilterRuleTest#TestDeleteRuleWhenUsedShouldFlag/Trace-1651091780144.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: void onCreate(android.os.Bundle)>_85_170
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreate_001() throws Exception {
        org.robolectric.android.controller.ActivityController activitycontroller17 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class);
        activitycontroller17.get();
        activitycontroller17.create();
    }
}
