package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test697 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewCalendarRule/Trace-1651091796523.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: boolean onOptionsItemSelected(android.view.MenuItem)>_415_830
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onOptionsItemSelected_001() throws Exception {
        android.content.Intent intent20 = null;
        android.content.Context context25 = ApplicationProvider.getApplicationContext();
        intent20 = new android.content.Intent(context25, com.prismaqf.callblocker.EditCalendarRules.class);
        android.content.Intent intent21 = intent20.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller53 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class, intent21);
        com.prismaqf.callblocker.EditCalendarRules editcalendarrules5 = (com.prismaqf.callblocker.EditCalendarRules) activitycontroller53.get();
        org.robolectric.android.controller.ActivityController activitycontroller54 = activitycontroller53.create();
        activitycontroller54.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity5 = org.robolectric.Shadows.shadowOf(editcalendarrules5);
        shadowactivity5.clickMenuItem(2131230754);
    }
}
