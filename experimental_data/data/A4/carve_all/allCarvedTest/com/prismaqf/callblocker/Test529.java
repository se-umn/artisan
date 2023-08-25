package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test529 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCreate(android.os.Bundle)>_701_1402
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCreate_002() throws Exception {
        android.content.Intent intent53 = null;
        java.util.EnumSet enumset66 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset67 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule88 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber533 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule89 = stubber533.when(calendarrule88);
        calendarrule89.setDayMask(enumset66);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule90 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber534 = org.mockito.Mockito.doReturn(calendarrule89);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule91 = stubber534.when(calendarrule90);
        calendarrule91.clone();
        org.mockito.stubbing.Stubber stubber535 = org.mockito.Mockito.doReturn(enumset67);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule93 = stubber535.when(calendarrule90);
        calendarrule93.getDayMask();
        android.content.Context context37 = ApplicationProvider.getApplicationContext();
        intent53 = new android.content.Intent(context37, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent54 = intent53.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent55 = intent54.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:edit");
        android.content.Intent intent56 = intent55.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule93);
        android.content.Intent intent57 = intent56.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        org.robolectric.android.controller.ActivityController activitycontroller69 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent57);
        activitycontroller69.get();
        activitycontroller69.create();
    }
}
