package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test146 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewCalendarRule/Trace-1651091796523.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditCalendarRules: void onActivityResult(int,int,android.content.Intent)>_643_1286
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditCalendarRules_onActivityResult_001() throws Exception {
        android.content.Intent intent30 = null;
        android.content.Intent intent31 = null;
        intent31 = new android.content.Intent();
        android.content.Intent intent32 = intent31.putExtra("com.prismaqft.callblocker:rulename", "New rule");
        android.content.Context context39 = ApplicationProvider.getApplicationContext();
        intent30 = new android.content.Intent(context39, com.prismaqf.callblocker.EditCalendarRules.class);
        android.content.Intent intent33 = intent30.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller64 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditCalendarRules.class, intent33);
        com.prismaqf.callblocker.EditCalendarRules editcalendarrules7 = (com.prismaqf.callblocker.EditCalendarRules) activitycontroller64.get();
        org.robolectric.android.controller.ActivityController activitycontroller65 = activitycontroller64.create();
        activitycontroller65.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity9 = org.robolectric.Shadows.shadowOf(editcalendarrules7);
        shadowactivity9.clickMenuItem(2131230754);
        editcalendarrules7.onActivityResult(1001, -1, intent32);
    }
}
