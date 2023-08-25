package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test462 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#EditTheCalendarRule/Trace-1651091808719.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_595_1190
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek0 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.THURSDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek1 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.WEDNESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek2 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SATURDAY;
        android.os.Parcel parcel32 = null;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek3 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.FRIDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek4 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.MONDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek5 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.TUESDAY;
        android.content.Intent intent21 = null;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek6 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SUNDAY;
        java.util.EnumSet enumset16 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber124 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset17 = stubber124.when(enumset16);
        enumset17.add(dayofweek6);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule29 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber125 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule30 = stubber125.when(calendarrule29);
        calendarrule30.setDayMask(enumset17);
        org.mockito.stubbing.Stubber stubber126 = org.mockito.Mockito.doReturn(enumset17);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule31 = stubber126.when(calendarrule29);
        calendarrule31.getDayMask();
        java.util.EnumSet enumset19 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset20 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber127 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset21 = stubber127.when(enumset20);
        enumset21.contains(dayofweek4);
        org.mockito.stubbing.Stubber stubber128 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset22 = stubber128.when(enumset20);
        enumset22.contains(dayofweek5);
        org.mockito.stubbing.Stubber stubber129 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset23 = stubber129.when(enumset20);
        enumset23.contains(dayofweek1);
        org.mockito.stubbing.Stubber stubber130 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset24 = stubber130.when(enumset20);
        enumset24.contains(dayofweek0);
        org.mockito.stubbing.Stubber stubber131 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset25 = stubber131.when(enumset20);
        enumset25.contains(dayofweek3);
        org.mockito.stubbing.Stubber stubber132 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset26 = stubber132.when(enumset20);
        enumset26.contains(dayofweek2);
        org.mockito.stubbing.Stubber stubber133 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset27 = stubber133.when(enumset20);
        enumset27.contains(dayofweek6);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox7 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber134 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox8 = stubber134.when(appcompatcheckbox7);
        appcompatcheckbox8.setEnabled(false);
        org.mockito.stubbing.Stubber stubber135 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox9 = stubber135.when(appcompatcheckbox7);
        appcompatcheckbox9.setEnabled(false);
        org.mockito.stubbing.Stubber stubber136 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox10 = stubber136.when(appcompatcheckbox7);
        appcompatcheckbox10.setChecked(false);
        org.mockito.stubbing.Stubber stubber137 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox11 = stubber137.when(appcompatcheckbox7);
        appcompatcheckbox11.setEnabled(true);
        org.mockito.stubbing.Stubber stubber138 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Sunday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox12 = stubber138.when(appcompatcheckbox7);
        appcompatcheckbox12.getId();
        org.mockito.stubbing.Stubber stubber139 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox13 = stubber139.when(appcompatcheckbox7);
        appcompatcheckbox13.isChecked();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule32 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber140 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule33 = stubber140.when(calendarrule32);
        calendarrule33.writeToParcel(parcel32, 0);
        org.mockito.stubbing.Stubber stubber141 = org.mockito.Mockito.doReturn(calendarrule31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule34 = stubber141.when(calendarrule32);
        calendarrule34.clone();
        org.mockito.stubbing.Stubber stubber142 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule36 = stubber142.when(calendarrule32);
        calendarrule36.getDayMask();
        org.mockito.stubbing.Stubber stubber143 = org.mockito.Mockito.doReturn("My calendar rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule37 = stubber143.when(calendarrule32);
        calendarrule37.getName();
        org.mockito.stubbing.Stubber stubber144 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule38 = stubber144.when(calendarrule32);
        calendarrule38.setName("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber145 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule39 = stubber145.when(calendarrule32);
        calendarrule39.getDayMask();
        org.mockito.stubbing.Stubber stubber146 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule40 = stubber146.when(calendarrule32);
        calendarrule40.getDayMask();
        org.mockito.stubbing.Stubber stubber147 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule41 = stubber147.when(calendarrule32);
        calendarrule41.getDayMask();
        org.mockito.stubbing.Stubber stubber148 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule42 = stubber148.when(calendarrule32);
        calendarrule42.getDayMask();
        org.mockito.stubbing.Stubber stubber149 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule43 = stubber149.when(calendarrule32);
        calendarrule43.getDayMask();
        org.mockito.stubbing.Stubber stubber150 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule44 = stubber150.when(calendarrule32);
        calendarrule44.getDayMask();
        org.mockito.stubbing.Stubber stubber151 = org.mockito.Mockito.doReturn(enumset27);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule45 = stubber151.when(calendarrule32);
        calendarrule45.getDayMask();
        org.mockito.stubbing.Stubber stubber152 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule46 = stubber152.when(calendarrule32);
        calendarrule46.getStartTime();
        org.mockito.stubbing.Stubber stubber153 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule47 = stubber153.when(calendarrule32);
        calendarrule47.getEndTime();
        android.os.Parcel parcel37 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber154 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel38 = stubber154.when(parcel37);
        parcel38.writeString("My calendar rule for testing");
        org.mockito.stubbing.Stubber stubber155 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel39 = stubber155.when(parcel37);
        parcel39.writeSerializable(enumset19);
        org.mockito.stubbing.Stubber stubber156 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel40 = stubber156.when(parcel37);
        parcel40.writeInt(1);
        org.mockito.stubbing.Stubber stubber157 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel41 = stubber157.when(parcel37);
        parcel41.writeInt(2);
        org.mockito.stubbing.Stubber stubber158 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel42 = stubber158.when(parcel37);
        parcel42.writeInt(23);
        org.mockito.stubbing.Stubber stubber159 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel43 = stubber159.when(parcel37);
        parcel43.writeInt(22);
        android.content.Context context23 = ApplicationProvider.getApplicationContext();
        intent21 = new android.content.Intent(context23, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent22 = intent21.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent23 = intent22.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:edit");
        android.content.Intent intent24 = intent23.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule47);
        android.content.Intent intent25 = intent24.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        calendarrule47.writeToParcel(parcel43, 0);
        org.robolectric.android.controller.ActivityController activitycontroller49 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent25);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule3 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller49.get();
        org.robolectric.android.controller.ActivityController activitycontroller50 = activitycontroller49.create();
        activitycontroller50.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity5 = org.robolectric.Shadows.shadowOf(neweditcalendarrule3);
        shadowactivity5.clickMenuItem(2131230736);
        neweditcalendarrule3.onCheckDay(appcompatcheckbox13);
    }
}
