package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test423 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestChangeAction/Trace-1651091726226.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_450_897
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek14 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SATURDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek15 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.TUESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek16 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.THURSDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek17 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.WEDNESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek18 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SUNDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek19 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.MONDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek20 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.FRIDAY;
        android.content.Intent intent12 = null;
        java.util.EnumSet enumset117 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset118 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset119 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber226 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset120 = stubber226.when(enumset119);
        enumset120.contains(dayofweek19);
        org.mockito.stubbing.Stubber stubber227 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset121 = stubber227.when(enumset119);
        enumset121.contains(dayofweek15);
        org.mockito.stubbing.Stubber stubber228 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset122 = stubber228.when(enumset119);
        enumset122.contains(dayofweek17);
        org.mockito.stubbing.Stubber stubber229 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset123 = stubber229.when(enumset119);
        enumset123.contains(dayofweek16);
        org.mockito.stubbing.Stubber stubber230 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset124 = stubber230.when(enumset119);
        enumset124.contains(dayofweek20);
        org.mockito.stubbing.Stubber stubber231 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset125 = stubber231.when(enumset119);
        enumset125.contains(dayofweek14);
        org.mockito.stubbing.Stubber stubber232 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset126 = stubber232.when(enumset119);
        enumset126.contains(dayofweek18);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule144 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber233 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule145 = stubber233.when(calendarrule144);
        calendarrule145.setDayMask(enumset117);
        android.os.Parcel parcel35 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber234 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel36 = stubber234.when(parcel35);
        parcel36.writeString("My rule for testing");
        org.mockito.stubbing.Stubber stubber235 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel37 = stubber235.when(parcel35);
        parcel37.writeSerializable(enumset118);
        org.mockito.stubbing.Stubber stubber236 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel38 = stubber236.when(parcel35);
        parcel38.writeInt(1);
        org.mockito.stubbing.Stubber stubber237 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel39 = stubber237.when(parcel35);
        parcel39.writeInt(2);
        org.mockito.stubbing.Stubber stubber238 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel40 = stubber238.when(parcel35);
        parcel40.writeInt(23);
        org.mockito.stubbing.Stubber stubber239 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel41 = stubber239.when(parcel35);
        parcel41.writeInt(22);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule146 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber240 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule147 = stubber240.when(calendarrule146);
        calendarrule147.writeToParcel(parcel41, 0);
        org.mockito.stubbing.Stubber stubber241 = org.mockito.Mockito.doReturn(calendarrule145);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule148 = stubber241.when(calendarrule146);
        calendarrule148.clone();
        org.mockito.stubbing.Stubber stubber242 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule150 = stubber242.when(calendarrule146);
        calendarrule150.getDayMask();
        org.mockito.stubbing.Stubber stubber243 = org.mockito.Mockito.doReturn("My rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule151 = stubber243.when(calendarrule146);
        calendarrule151.getName();
        org.mockito.stubbing.Stubber stubber244 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule152 = stubber244.when(calendarrule146);
        calendarrule152.setName("My rule for testing");
        org.mockito.stubbing.Stubber stubber245 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule153 = stubber245.when(calendarrule146);
        calendarrule153.getDayMask();
        org.mockito.stubbing.Stubber stubber246 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule154 = stubber246.when(calendarrule146);
        calendarrule154.getDayMask();
        org.mockito.stubbing.Stubber stubber247 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule155 = stubber247.when(calendarrule146);
        calendarrule155.getDayMask();
        org.mockito.stubbing.Stubber stubber248 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule156 = stubber248.when(calendarrule146);
        calendarrule156.getDayMask();
        org.mockito.stubbing.Stubber stubber249 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule157 = stubber249.when(calendarrule146);
        calendarrule157.getDayMask();
        org.mockito.stubbing.Stubber stubber250 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule158 = stubber250.when(calendarrule146);
        calendarrule158.getDayMask();
        org.mockito.stubbing.Stubber stubber251 = org.mockito.Mockito.doReturn(enumset126);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule159 = stubber251.when(calendarrule146);
        calendarrule159.getDayMask();
        org.mockito.stubbing.Stubber stubber252 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule160 = stubber252.when(calendarrule146);
        calendarrule160.getStartTime();
        org.mockito.stubbing.Stubber stubber253 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule161 = stubber253.when(calendarrule146);
        calendarrule161.getEndTime();
        android.content.Context context19 = ApplicationProvider.getApplicationContext();
        intent12 = new android.content.Intent(context19, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent13 = intent12.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent14 = intent13.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule161);
        android.content.Intent intent15 = intent14.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        calendarrule161.writeToParcel(parcel41, 0);
        org.robolectric.android.controller.ActivityController activitycontroller42 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent15);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule7 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller42.get();
        org.robolectric.android.controller.ActivityController activitycontroller43 = activitycontroller42.create();
        activitycontroller43.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity5 = org.robolectric.Shadows.shadowOf(neweditcalendarrule7);
        shadowactivity5.clickMenuItem(2131230736);
        neweditcalendarrule7.validateActions();
    }
}
