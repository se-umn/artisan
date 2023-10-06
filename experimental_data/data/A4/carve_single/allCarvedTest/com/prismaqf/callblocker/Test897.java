package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test897 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestDeleteRuleWhenUsedShouldFlag/Trace-1651091728932.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCreate(android.os.Bundle)>_178_356
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCreate_001() throws Exception {
        android.content.Intent intent0 = null;
        java.util.EnumSet enumset2 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule5 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber3 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule6 = stubber3.when(calendarrule5);
        calendarrule6.setDayMask(enumset2);
        java.util.EnumSet enumset3 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule7 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber4 = org.mockito.Mockito.doReturn(calendarrule6);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule8 = stubber4.when(calendarrule7);
        calendarrule8.clone();
        org.mockito.stubbing.Stubber stubber5 = org.mockito.Mockito.doReturn(enumset3);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule10 = stubber5.when(calendarrule7);
        calendarrule10.getDayMask();
        android.content.Context context13 = ApplicationProvider.getApplicationContext();
        intent0 = new android.content.Intent(context13, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent1 = intent0.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent2 = intent1.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule10);
        android.content.Intent intent3 = intent2.putExtra("com.prismaqft.callblocker:ruleid", 2L);
        org.robolectric.android.controller.ActivityController activitycontroller30 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent3);
        activitycontroller30.get();
        activitycontroller30.create();
    }
}