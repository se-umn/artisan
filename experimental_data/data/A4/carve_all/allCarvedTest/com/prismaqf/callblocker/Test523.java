package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test523 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_595_1190
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek0 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.THURSDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek1 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.WEDNESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek2 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SATURDAY;
        android.os.Parcel parcel42 = null;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek3 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.FRIDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek4 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.MONDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek5 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.TUESDAY;
        android.content.Intent intent25 = null;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek6 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SUNDAY;
        java.util.EnumSet enumset16 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber148 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset17 = stubber148.when(enumset16);
        enumset17.add(dayofweek6);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule29 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber149 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule30 = stubber149.when(calendarrule29);
        calendarrule30.setDayMask(enumset17);
        org.mockito.stubbing.Stubber stubber150 = org.mockito.Mockito.doReturn(enumset17);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule31 = stubber150.when(calendarrule29);
        calendarrule31.getDayMask();
        java.util.EnumSet enumset19 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset20 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber151 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset21 = stubber151.when(enumset20);
        enumset21.contains(dayofweek4);
        org.mockito.stubbing.Stubber stubber152 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset22 = stubber152.when(enumset20);
        enumset22.contains(dayofweek5);
        org.mockito.stubbing.Stubber stubber153 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset23 = stubber153.when(enumset20);
        enumset23.contains(dayofweek1);
        org.mockito.stubbing.Stubber stubber154 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset24 = stubber154.when(enumset20);
        enumset24.contains(dayofweek0);
        org.mockito.stubbing.Stubber stubber155 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset25 = stubber155.when(enumset20);
        enumset25.contains(dayofweek3);
        org.mockito.stubbing.Stubber stubber156 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset26 = stubber156.when(enumset20);
        enumset26.contains(dayofweek2);
        org.mockito.stubbing.Stubber stubber157 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset27 = stubber157.when(enumset20);
        enumset27.contains(dayofweek6);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox7 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber158 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox8 = stubber158.when(appcompatcheckbox7);
        appcompatcheckbox8.setEnabled(false);
        org.mockito.stubbing.Stubber stubber159 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox9 = stubber159.when(appcompatcheckbox7);
        appcompatcheckbox9.setEnabled(false);
        org.mockito.stubbing.Stubber stubber160 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox10 = stubber160.when(appcompatcheckbox7);
        appcompatcheckbox10.setChecked(false);
        org.mockito.stubbing.Stubber stubber161 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox11 = stubber161.when(appcompatcheckbox7);
        appcompatcheckbox11.setEnabled(true);
        org.mockito.stubbing.Stubber stubber162 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Sunday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox12 = stubber162.when(appcompatcheckbox7);
        appcompatcheckbox12.getId();
        org.mockito.stubbing.Stubber stubber163 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox13 = stubber163.when(appcompatcheckbox7);
        appcompatcheckbox13.isChecked();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule32 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber164 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule33 = stubber164.when(calendarrule32);
        calendarrule33.writeToParcel(parcel42, 0);
        org.mockito.stubbing.Stubber stubber165 = org.mockito.Mockito.doReturn(calendarrule31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule34 = stubber165.when(calendarrule32);
        calendarrule34.clone();
        org.mockito.stubbing.Stubber stubber166 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule36 = stubber166.when(calendarrule32);
        calendarrule36.getDayMask();
        org.mockito.stubbing.Stubber stubber167 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule37 = stubber167.when(calendarrule32);
        calendarrule37.getName();
        org.mockito.stubbing.Stubber stubber168 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule38 = stubber168.when(calendarrule32);
        calendarrule38.setName("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber169 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule39 = stubber169.when(calendarrule32);
        calendarrule39.getDayMask();
        org.mockito.stubbing.Stubber stubber170 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule40 = stubber170.when(calendarrule32);
        calendarrule40.getDayMask();
        org.mockito.stubbing.Stubber stubber171 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule41 = stubber171.when(calendarrule32);
        calendarrule41.getDayMask();
        org.mockito.stubbing.Stubber stubber172 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule42 = stubber172.when(calendarrule32);
        calendarrule42.getDayMask();
        org.mockito.stubbing.Stubber stubber173 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule43 = stubber173.when(calendarrule32);
        calendarrule43.getDayMask();
        org.mockito.stubbing.Stubber stubber174 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule44 = stubber174.when(calendarrule32);
        calendarrule44.getDayMask();
        org.mockito.stubbing.Stubber stubber175 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule45 = stubber175.when(calendarrule32);
        calendarrule45.getDayMask();
        org.mockito.stubbing.Stubber stubber176 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule46 = stubber176.when(calendarrule32);
        calendarrule46.getStartTime();
        org.mockito.stubbing.Stubber stubber177 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule47 = stubber177.when(calendarrule32);
        calendarrule47.getEndTime();
        android.os.Parcel parcel47 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber178 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel48 = stubber178.when(parcel47);
        parcel48.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber179 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel49 = stubber179.when(parcel47);
        parcel49.writeSerializable(enumset19);
        org.mockito.stubbing.Stubber stubber180 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel50 = stubber180.when(parcel47);
        parcel50.writeInt(1);
        org.mockito.stubbing.Stubber stubber181 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel51 = stubber181.when(parcel47);
        parcel51.writeInt(2);
        org.mockito.stubbing.Stubber stubber182 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel52 = stubber182.when(parcel47);
        parcel52.writeInt(23);
        org.mockito.stubbing.Stubber stubber183 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel53 = stubber183.when(parcel47);
        parcel53.writeInt(22);
        android.content.Context context25 = ApplicationProvider.getApplicationContext();
        intent25 = new android.content.Intent(context25, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent26 = intent25.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent27 = intent26.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:edit");
        android.content.Intent intent28 = intent27.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule47);
        android.content.Intent intent29 = intent28.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        calendarrule47.writeToParcel(parcel53, 0);
        org.robolectric.android.controller.ActivityController activitycontroller53 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent29);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule3 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller53.get();
        org.robolectric.android.controller.ActivityController activitycontroller54 = activitycontroller53.create();
        activitycontroller54.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity7 = org.robolectric.Shadows.shadowOf(neweditcalendarrule3);
        shadowactivity7.clickMenuItem(2131230736);
        neweditcalendarrule3.onCheckDay(appcompatcheckbox13);
    }
}
