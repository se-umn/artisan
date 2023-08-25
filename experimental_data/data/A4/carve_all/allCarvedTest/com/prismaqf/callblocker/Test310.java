package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test310 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestUndoAction/Trace-1651091721515.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void refreshWidgets(boolean)>_403_805
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_refreshWidgets_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek7 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.TUESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek8 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.WEDNESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek9 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.MONDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek10 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.FRIDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek11 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SATURDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek12 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.THURSDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek13 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SUNDAY;
        android.content.Intent intent8 = null;
        java.util.EnumSet enumset74 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset75 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber152 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset76 = stubber152.when(enumset75);
        enumset76.contains(dayofweek9);
        org.mockito.stubbing.Stubber stubber153 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset77 = stubber153.when(enumset75);
        enumset77.contains(dayofweek7);
        org.mockito.stubbing.Stubber stubber154 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset78 = stubber154.when(enumset75);
        enumset78.contains(dayofweek8);
        org.mockito.stubbing.Stubber stubber155 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset79 = stubber155.when(enumset75);
        enumset79.contains(dayofweek12);
        org.mockito.stubbing.Stubber stubber156 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset80 = stubber156.when(enumset75);
        enumset80.contains(dayofweek10);
        org.mockito.stubbing.Stubber stubber157 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset81 = stubber157.when(enumset75);
        enumset81.contains(dayofweek11);
        org.mockito.stubbing.Stubber stubber158 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset82 = stubber158.when(enumset75);
        enumset82.contains(dayofweek13);
        java.util.EnumSet enumset83 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset84 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber159 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset85 = stubber159.when(enumset84);
        enumset85.contains(dayofweek9);
        org.mockito.stubbing.Stubber stubber160 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset86 = stubber160.when(enumset84);
        enumset86.contains(dayofweek7);
        org.mockito.stubbing.Stubber stubber161 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset87 = stubber161.when(enumset84);
        enumset87.contains(dayofweek8);
        org.mockito.stubbing.Stubber stubber162 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset88 = stubber162.when(enumset84);
        enumset88.contains(dayofweek12);
        org.mockito.stubbing.Stubber stubber163 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset89 = stubber163.when(enumset84);
        enumset89.contains(dayofweek10);
        org.mockito.stubbing.Stubber stubber164 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset90 = stubber164.when(enumset84);
        enumset90.contains(dayofweek11);
        org.mockito.stubbing.Stubber stubber165 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset91 = stubber165.when(enumset84);
        enumset91.contains(dayofweek13);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule98 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber166 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule99 = stubber166.when(calendarrule98);
        calendarrule99.setDayMask(enumset74);
        org.mockito.stubbing.Stubber stubber167 = org.mockito.Mockito.doReturn("My rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule100 = stubber167.when(calendarrule98);
        calendarrule100.getName();
        org.mockito.stubbing.Stubber stubber168 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule101 = stubber168.when(calendarrule98);
        calendarrule101.setName("My rule for testing");
        org.mockito.stubbing.Stubber stubber169 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule102 = stubber169.when(calendarrule98);
        calendarrule102.getDayMask();
        org.mockito.stubbing.Stubber stubber170 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule103 = stubber170.when(calendarrule98);
        calendarrule103.getDayMask();
        org.mockito.stubbing.Stubber stubber171 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule104 = stubber171.when(calendarrule98);
        calendarrule104.getDayMask();
        org.mockito.stubbing.Stubber stubber172 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule105 = stubber172.when(calendarrule98);
        calendarrule105.getDayMask();
        org.mockito.stubbing.Stubber stubber173 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule106 = stubber173.when(calendarrule98);
        calendarrule106.getDayMask();
        org.mockito.stubbing.Stubber stubber174 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule107 = stubber174.when(calendarrule98);
        calendarrule107.getDayMask();
        org.mockito.stubbing.Stubber stubber175 = org.mockito.Mockito.doReturn(enumset82);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule108 = stubber175.when(calendarrule98);
        calendarrule108.getDayMask();
        org.mockito.stubbing.Stubber stubber176 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule109 = stubber176.when(calendarrule98);
        calendarrule109.getStartTime();
        org.mockito.stubbing.Stubber stubber177 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule110 = stubber177.when(calendarrule98);
        calendarrule110.getEndTime();
        android.os.Parcel parcel21 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber178 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel22 = stubber178.when(parcel21);
        parcel22.writeString("My rule for testing");
        org.mockito.stubbing.Stubber stubber179 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel23 = stubber179.when(parcel21);
        parcel23.writeSerializable(enumset83);
        org.mockito.stubbing.Stubber stubber180 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel24 = stubber180.when(parcel21);
        parcel24.writeInt(1);
        org.mockito.stubbing.Stubber stubber181 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel25 = stubber181.when(parcel21);
        parcel25.writeInt(2);
        org.mockito.stubbing.Stubber stubber182 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel26 = stubber182.when(parcel21);
        parcel26.writeInt(23);
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel27 = stubber183.when(parcel21);
        parcel27.writeInt(22);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule111 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber184 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule112 = stubber184.when(calendarrule111);
        calendarrule112.writeToParcel(parcel27, 0);
        org.mockito.stubbing.Stubber stubber185 = org.mockito.Mockito.doReturn(calendarrule110);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule113 = stubber185.when(calendarrule111);
        calendarrule113.clone();
        org.mockito.stubbing.Stubber stubber186 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule115 = stubber186.when(calendarrule111);
        calendarrule115.getDayMask();
        org.mockito.stubbing.Stubber stubber187 = org.mockito.Mockito.doReturn("My rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule116 = stubber187.when(calendarrule111);
        calendarrule116.getName();
        org.mockito.stubbing.Stubber stubber188 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule117 = stubber188.when(calendarrule111);
        calendarrule117.setName("My rule for testing");
        org.mockito.stubbing.Stubber stubber189 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule118 = stubber189.when(calendarrule111);
        calendarrule118.getDayMask();
        org.mockito.stubbing.Stubber stubber190 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule119 = stubber190.when(calendarrule111);
        calendarrule119.getDayMask();
        org.mockito.stubbing.Stubber stubber191 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule120 = stubber191.when(calendarrule111);
        calendarrule120.getDayMask();
        org.mockito.stubbing.Stubber stubber192 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule121 = stubber192.when(calendarrule111);
        calendarrule121.getDayMask();
        org.mockito.stubbing.Stubber stubber193 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule122 = stubber193.when(calendarrule111);
        calendarrule122.getDayMask();
        org.mockito.stubbing.Stubber stubber194 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule123 = stubber194.when(calendarrule111);
        calendarrule123.getDayMask();
        org.mockito.stubbing.Stubber stubber195 = org.mockito.Mockito.doReturn(enumset91);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule124 = stubber195.when(calendarrule111);
        calendarrule124.getDayMask();
        org.mockito.stubbing.Stubber stubber196 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule125 = stubber196.when(calendarrule111);
        calendarrule125.getStartTime();
        org.mockito.stubbing.Stubber stubber197 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule126 = stubber197.when(calendarrule111);
        calendarrule126.getEndTime();
        android.content.Context context17 = ApplicationProvider.getApplicationContext();
        intent8 = new android.content.Intent(context17, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent9 = intent8.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent10 = intent9.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule126);
        android.content.Intent intent11 = intent10.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        calendarrule126.writeToParcel(parcel27, 0);
        org.robolectric.android.controller.ActivityController activitycontroller38 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent11);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule5 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller38.get();
        org.robolectric.android.controller.ActivityController activitycontroller39 = activitycontroller38.create();
        activitycontroller39.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity3 = org.robolectric.Shadows.shadowOf(neweditcalendarrule5);
        shadowactivity3.clickMenuItem(2131230736);
        neweditcalendarrule5.refreshWidgets(true);
    }
}
