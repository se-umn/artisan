package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test521 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCreate(android.os.Bundle)>_358_716
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCreate_001() throws Exception {
        android.content.Intent intent16 = null;
        java.util.EnumSet enumset2 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule5 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber73 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule6 = stubber73.when(calendarrule5);
        calendarrule6.setDayMask(enumset2);
        java.util.EnumSet enumset3 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule7 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber74 = org.mockito.Mockito.doReturn(calendarrule6);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule8 = stubber74.when(calendarrule7);
        calendarrule8.clone();
        org.mockito.stubbing.Stubber stubber75 = org.mockito.Mockito.doReturn(enumset3);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule10 = stubber75.when(calendarrule7);
        calendarrule10.getDayMask();
        android.content.Context context21 = ApplicationProvider.getApplicationContext();
        intent16 = new android.content.Intent(context21, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent17 = intent16.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent18 = intent17.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:edit");
        android.content.Intent intent19 = intent18.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule10);
        android.content.Intent intent20 = intent19.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        org.robolectric.android.controller.ActivityController activitycontroller46 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent20);
        activitycontroller46.get();
        activitycontroller46.create();
    }
}
