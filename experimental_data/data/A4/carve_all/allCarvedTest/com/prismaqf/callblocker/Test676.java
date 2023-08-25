package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test676 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void refreshWidgets(boolean)>_296_591
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_refreshWidgets_001() throws Exception {
        android.content.Intent intent4 = null;
        intent4 = new android.content.Intent();
        android.content.Intent intent5 = intent4.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent6 = intent5.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        org.robolectric.android.controller.ActivityController activitycontroller15 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent6);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule5 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller15.get();
        org.robolectric.android.controller.ActivityController activitycontroller16 = activitycontroller15.create();
        activitycontroller16.visible();
        neweditcalendarrule5.refreshWidgets(true);
    }
}
