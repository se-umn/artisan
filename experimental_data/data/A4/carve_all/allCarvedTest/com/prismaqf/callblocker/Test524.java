package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test524 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_600_1199
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek7 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.THURSDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek8 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.WEDNESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek9 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SATURDAY;
        android.os.Parcel parcel55 = null;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek10 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.FRIDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek11 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.MONDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek12 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.TUESDAY;
        android.content.Intent intent30 = null;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek13 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SUNDAY;
        java.util.EnumSet enumset46 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule65 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber212 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule66 = stubber212.when(calendarrule65);
        calendarrule66.setDayMask(enumset46);
        java.util.EnumSet enumset47 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset48 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber213 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset49 = stubber213.when(enumset48);
        enumset49.contains(dayofweek11);
        org.mockito.stubbing.Stubber stubber214 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset50 = stubber214.when(enumset48);
        enumset50.contains(dayofweek12);
        org.mockito.stubbing.Stubber stubber215 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset51 = stubber215.when(enumset48);
        enumset51.contains(dayofweek8);
        org.mockito.stubbing.Stubber stubber216 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset52 = stubber216.when(enumset48);
        enumset52.contains(dayofweek7);
        org.mockito.stubbing.Stubber stubber217 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset53 = stubber217.when(enumset48);
        enumset53.contains(dayofweek10);
        org.mockito.stubbing.Stubber stubber218 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset54 = stubber218.when(enumset48);
        enumset54.contains(dayofweek9);
        org.mockito.stubbing.Stubber stubber219 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset55 = stubber219.when(enumset48);
        enumset55.contains(dayofweek13);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule67 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber220 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule68 = stubber220.when(calendarrule67);
        calendarrule68.writeToParcel(parcel55, 0);
        org.mockito.stubbing.Stubber stubber221 = org.mockito.Mockito.doReturn(calendarrule66);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule69 = stubber221.when(calendarrule67);
        calendarrule69.clone();
        org.mockito.stubbing.Stubber stubber222 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule71 = stubber222.when(calendarrule67);
        calendarrule71.getDayMask();
        org.mockito.stubbing.Stubber stubber223 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule72 = stubber223.when(calendarrule67);
        calendarrule72.getName();
        org.mockito.stubbing.Stubber stubber224 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule73 = stubber224.when(calendarrule67);
        calendarrule73.setName("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber225 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule74 = stubber225.when(calendarrule67);
        calendarrule74.getDayMask();
        org.mockito.stubbing.Stubber stubber226 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule75 = stubber226.when(calendarrule67);
        calendarrule75.getDayMask();
        org.mockito.stubbing.Stubber stubber227 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule76 = stubber227.when(calendarrule67);
        calendarrule76.getDayMask();
        org.mockito.stubbing.Stubber stubber228 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule77 = stubber228.when(calendarrule67);
        calendarrule77.getDayMask();
        org.mockito.stubbing.Stubber stubber229 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule78 = stubber229.when(calendarrule67);
        calendarrule78.getDayMask();
        org.mockito.stubbing.Stubber stubber230 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule79 = stubber230.when(calendarrule67);
        calendarrule79.getDayMask();
        org.mockito.stubbing.Stubber stubber231 = org.mockito.Mockito.doReturn(enumset55);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule80 = stubber231.when(calendarrule67);
        calendarrule80.getDayMask();
        org.mockito.stubbing.Stubber stubber232 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule81 = stubber232.when(calendarrule67);
        calendarrule81.getStartTime();
        org.mockito.stubbing.Stubber stubber233 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule82 = stubber233.when(calendarrule67);
        calendarrule82.getEndTime();
        android.os.Parcel parcel61 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber234 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel62 = stubber234.when(parcel61);
        parcel62.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber235 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel63 = stubber235.when(parcel61);
        parcel63.writeSerializable(enumset47);
        org.mockito.stubbing.Stubber stubber236 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel64 = stubber236.when(parcel61);
        parcel64.writeInt(1);
        org.mockito.stubbing.Stubber stubber237 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel65 = stubber237.when(parcel61);
        parcel65.writeInt(2);
        org.mockito.stubbing.Stubber stubber238 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel66 = stubber238.when(parcel61);
        parcel66.writeInt(23);
        org.mockito.stubbing.Stubber stubber239 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel67 = stubber239.when(parcel61);
        parcel67.writeInt(22);
        android.content.Context context27 = ApplicationProvider.getApplicationContext();
        intent30 = new android.content.Intent(context27, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent31 = intent30.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent32 = intent31.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:edit");
        android.content.Intent intent33 = intent32.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule82);
        android.content.Intent intent34 = intent33.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        calendarrule82.writeToParcel(parcel67, 0);
        org.robolectric.android.controller.ActivityController activitycontroller57 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent34);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule5 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller57.get();
        org.robolectric.android.controller.ActivityController activitycontroller58 = activitycontroller57.create();
        activitycontroller58.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity9 = org.robolectric.Shadows.shadowOf(neweditcalendarrule5);
        shadowactivity9.clickMenuItem(2131230736);
        neweditcalendarrule5.validateActions();
    }
}
