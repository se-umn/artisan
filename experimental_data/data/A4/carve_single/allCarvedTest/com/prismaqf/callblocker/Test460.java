package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test460 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCreate(android.os.Bundle)>_358_716
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCreate_001() throws Exception {
        android.content.Intent intent12 = null;
        java.util.EnumSet enumset2 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule5 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber49 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule6 = stubber49.when(calendarrule5);
        calendarrule6.setDayMask(enumset2);
        java.util.EnumSet enumset3 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule7 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber50 = org.mockito.Mockito.doReturn(calendarrule6);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule8 = stubber50.when(calendarrule7);
        calendarrule8.clone();
        org.mockito.stubbing.Stubber stubber51 = org.mockito.Mockito.doReturn(enumset3);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule10 = stubber51.when(calendarrule7);
        calendarrule10.getDayMask();
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
        intent12 = new android.content.Intent(context19, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent13 = intent12.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent14 = intent13.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:edit");
        android.content.Intent intent15 = intent14.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule10);
        android.content.Intent intent16 = intent15.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        org.robolectric.android.controller.ActivityController activitycontroller42 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent16);
        activitycontroller42.get();
        activitycontroller42.create();
    }
}
