package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test234 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestUsedNameShouldFlag/Trace-1651091745358.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCreate(android.os.Bundle)>_76_152
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        intent0 = new android.content.Intent();
        android.content.Intent intent1 = intent0.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent2 = intent1.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        org.robolectric.android.controller.ActivityController activitycontroller8 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent2);
        activitycontroller8.get();
        activitycontroller8.create();
    }
}
