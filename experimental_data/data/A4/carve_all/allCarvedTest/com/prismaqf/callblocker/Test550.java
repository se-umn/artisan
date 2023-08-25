package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test550 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilterRule: void validateActions()>_236_471
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilterRule_validateActions_001() throws Exception {
        android.content.Intent intent3 = null;
        intent3 = new android.content.Intent();
        android.content.Intent intent4 = intent3.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent5 = intent4.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        org.robolectric.android.controller.ActivityController activitycontroller11 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilterRule.class, intent5);
        com.prismaqf.callblocker.NewEditFilterRule neweditfilterrule3 = (com.prismaqf.callblocker.NewEditFilterRule) activitycontroller11.get();
        org.robolectric.android.controller.ActivityController activitycontroller12 = activitycontroller11.create();
        activitycontroller12.visible();
        neweditfilterrule3.validateActions();
    }
}
