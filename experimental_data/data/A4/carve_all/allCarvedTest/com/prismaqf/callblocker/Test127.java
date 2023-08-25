package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test127 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewCalendarRule/Trace-1651091796523.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: boolean onOptionsItemSelected(android.view.MenuItem)>_415_830
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onOptionsItemSelected_001() throws Exception {
        android.content.Intent intent24 = null;
        android.content.Context context31 = ApplicationProvider.getApplicationContext();
        intent24 = new android.content.Intent(context31, com.prismaqf.callblocker.EditCalendarRules.class);
        android.content.Intent intent25 = intent24.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller57 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class, intent25);
        com.prismaqf.callblocker.EditCalendarRules editcalendarrules5 = (com.prismaqf.callblocker.EditCalendarRules) activitycontroller57.get();
        org.robolectric.android.controller.ActivityController activitycontroller58 = activitycontroller57.create();
        activitycontroller58.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity7 = org.robolectric.Shadows.shadowOf(editcalendarrules5);
        shadowactivity7.clickMenuItem(2131230754);
    }
}
