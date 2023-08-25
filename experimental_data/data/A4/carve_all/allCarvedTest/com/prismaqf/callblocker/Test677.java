package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test677 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_345_687
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_001() throws Exception {
        android.content.Intent intent7 = null;
        intent7 = new android.content.Intent();
        android.content.Intent intent8 = intent7.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        android.content.Intent intent9 = intent8.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:create");
        org.robolectric.android.controller.ActivityController activitycontroller19 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent9);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule7 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller19.get();
        org.robolectric.android.controller.ActivityController activitycontroller20 = activitycontroller19.create();
        activitycontroller20.visible();
        neweditcalendarrule7.validateActions();
    }
}
