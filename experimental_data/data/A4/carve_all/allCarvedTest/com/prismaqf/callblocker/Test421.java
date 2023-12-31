package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test421 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.UpdateCalendarRuleTest#TestChangeAction/Trace-1651091726226.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onNoDays(android.view.View)>_399_798
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onNoDays_001() throws Exception {
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek0 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.TUESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek1 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.WEDNESDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek2 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.MONDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek3 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.FRIDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek4 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SATURDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek5 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.THURSDAY;
        com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek dayofweek6 = com.prismaqf.callblocker.rules.CalendarRule.DayOfWeek.SUNDAY;
        android.content.Intent intent4 = null;
        java.util.EnumSet enumset23 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset24 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber56 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset25 = stubber56.when(enumset24);
        enumset25.contains(dayofweek2);
        org.mockito.stubbing.Stubber stubber57 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset26 = stubber57.when(enumset24);
        enumset26.contains(dayofweek0);
        org.mockito.stubbing.Stubber stubber58 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset27 = stubber58.when(enumset24);
        enumset27.contains(dayofweek1);
        org.mockito.stubbing.Stubber stubber59 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset28 = stubber59.when(enumset24);
        enumset28.contains(dayofweek5);
        org.mockito.stubbing.Stubber stubber60 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset29 = stubber60.when(enumset24);
        enumset29.contains(dayofweek3);
        org.mockito.stubbing.Stubber stubber61 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset30 = stubber61.when(enumset24);
        enumset30.contains(dayofweek4);
        org.mockito.stubbing.Stubber stubber62 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset31 = stubber62.when(enumset24);
        enumset31.contains(dayofweek6);
        java.util.EnumSet enumset32 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        java.util.EnumSet enumset33 = org.mockito.Mockito.mock(java.util.EnumSet.class);
        org.mockito.stubbing.Stubber stubber63 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset34 = stubber63.when(enumset33);
        enumset34.contains(dayofweek2);
        org.mockito.stubbing.Stubber stubber64 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset35 = stubber64.when(enumset33);
        enumset35.contains(dayofweek0);
        org.mockito.stubbing.Stubber stubber65 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset36 = stubber65.when(enumset33);
        enumset36.contains(dayofweek1);
        org.mockito.stubbing.Stubber stubber66 = org.mockito.Mockito.doReturn(true);
        java.util.EnumSet enumset37 = stubber66.when(enumset33);
        enumset37.contains(dayofweek5);
        org.mockito.stubbing.Stubber stubber67 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset38 = stubber67.when(enumset33);
        enumset38.contains(dayofweek3);
        org.mockito.stubbing.Stubber stubber68 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset39 = stubber68.when(enumset33);
        enumset39.contains(dayofweek4);
        org.mockito.stubbing.Stubber stubber69 = org.mockito.Mockito.doReturn(false);
        java.util.EnumSet enumset40 = stubber69.when(enumset33);
        enumset40.contains(dayofweek6);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule40 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber70 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule41 = stubber70.when(calendarrule40);
        calendarrule41.setDayMask(enumset23);
        org.mockito.stubbing.Stubber stubber71 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule42 = stubber71.when(calendarrule40);
        calendarrule42.setDayMask(enumset31);
        org.mockito.stubbing.Stubber stubber72 = org.mockito.Mockito.doReturn("My rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule43 = stubber72.when(calendarrule40);
        calendarrule43.getName();
        org.mockito.stubbing.Stubber stubber73 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule44 = stubber73.when(calendarrule40);
        calendarrule44.setName("My rule for testing");
        org.mockito.stubbing.Stubber stubber74 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule45 = stubber74.when(calendarrule40);
        calendarrule45.getDayMask();
        org.mockito.stubbing.Stubber stubber75 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule46 = stubber75.when(calendarrule40);
        calendarrule46.getDayMask();
        org.mockito.stubbing.Stubber stubber76 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule47 = stubber76.when(calendarrule40);
        calendarrule47.getDayMask();
        org.mockito.stubbing.Stubber stubber77 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule48 = stubber77.when(calendarrule40);
        calendarrule48.getDayMask();
        org.mockito.stubbing.Stubber stubber78 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule49 = stubber78.when(calendarrule40);
        calendarrule49.getDayMask();
        org.mockito.stubbing.Stubber stubber79 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule50 = stubber79.when(calendarrule40);
        calendarrule50.getDayMask();
        org.mockito.stubbing.Stubber stubber80 = org.mockito.Mockito.doReturn(enumset31);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule51 = stubber80.when(calendarrule40);
        calendarrule51.getDayMask();
        org.mockito.stubbing.Stubber stubber81 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule52 = stubber81.when(calendarrule40);
        calendarrule52.getStartTime();
        org.mockito.stubbing.Stubber stubber82 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule53 = stubber82.when(calendarrule40);
        calendarrule53.getEndTime();
        android.os.Parcel parcel7 = org.mockito.Mockito.mock(android.os.Parcel.class);
        org.mockito.stubbing.Stubber stubber83 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel8 = stubber83.when(parcel7);
        parcel8.writeString("My rule for testing");
        org.mockito.stubbing.Stubber stubber84 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel9 = stubber84.when(parcel7);
        parcel9.writeSerializable(enumset32);
        org.mockito.stubbing.Stubber stubber85 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel10 = stubber85.when(parcel7);
        parcel10.writeInt(1);
        org.mockito.stubbing.Stubber stubber86 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel11 = stubber86.when(parcel7);
        parcel11.writeInt(2);
        org.mockito.stubbing.Stubber stubber87 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel12 = stubber87.when(parcel7);
        parcel12.writeInt(23);
        org.mockito.stubbing.Stubber stubber88 = org.mockito.Mockito.doNothing();
        android.os.Parcel parcel13 = stubber88.when(parcel7);
        parcel13.writeInt(22);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule54 = org.mockito.Mockito.mock(com.prismaqf.callblocker.rules.CalendarRule.class);
        org.mockito.stubbing.Stubber stubber89 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule55 = stubber89.when(calendarrule54);
        calendarrule55.writeToParcel(parcel13, 0);
        org.mockito.stubbing.Stubber stubber90 = org.mockito.Mockito.doReturn(calendarrule53);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule56 = stubber90.when(calendarrule54);
        calendarrule56.clone();
        org.mockito.stubbing.Stubber stubber91 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule58 = stubber91.when(calendarrule54);
        calendarrule58.getDayMask();
        org.mockito.stubbing.Stubber stubber92 = org.mockito.Mockito.doReturn("My rule for testing");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule59 = stubber92.when(calendarrule54);
        calendarrule59.getName();
        org.mockito.stubbing.Stubber stubber93 = org.mockito.Mockito.doNothing();
        com.prismaqf.callblocker.rules.CalendarRule calendarrule60 = stubber93.when(calendarrule54);
        calendarrule60.setName("My rule for testing");
        org.mockito.stubbing.Stubber stubber94 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule61 = stubber94.when(calendarrule54);
        calendarrule61.getDayMask();
        org.mockito.stubbing.Stubber stubber95 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule62 = stubber95.when(calendarrule54);
        calendarrule62.getDayMask();
        org.mockito.stubbing.Stubber stubber96 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule63 = stubber96.when(calendarrule54);
        calendarrule63.getDayMask();
        org.mockito.stubbing.Stubber stubber97 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule64 = stubber97.when(calendarrule54);
        calendarrule64.getDayMask();
        org.mockito.stubbing.Stubber stubber98 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule65 = stubber98.when(calendarrule54);
        calendarrule65.getDayMask();
        org.mockito.stubbing.Stubber stubber99 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule66 = stubber99.when(calendarrule54);
        calendarrule66.getDayMask();
        org.mockito.stubbing.Stubber stubber100 = org.mockito.Mockito.doReturn(enumset40);
        com.prismaqf.callblocker.rules.CalendarRule calendarrule67 = stubber100.when(calendarrule54);
        calendarrule67.getDayMask();
        org.mockito.stubbing.Stubber stubber101 = org.mockito.Mockito.doReturn("From 01:02");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule68 = stubber101.when(calendarrule54);
        calendarrule68.getStartTime();
        org.mockito.stubbing.Stubber stubber102 = org.mockito.Mockito.doReturn("To 23:22");
        com.prismaqf.callblocker.rules.CalendarRule calendarrule69 = stubber102.when(calendarrule54);
        calendarrule69.getEndTime();
        androidx.appcompat.widget.AppCompatButton appcompatbutton4 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber103 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton5 = stubber103.when(appcompatbutton4);
        appcompatbutton5.setEnabled(false);
        org.mockito.stubbing.Stubber stubber104 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton6 = stubber104.when(appcompatbutton4);
        appcompatbutton6.setEnabled(false);
        org.mockito.stubbing.Stubber stubber105 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton7 = stubber105.when(appcompatbutton4);
        appcompatbutton7.setEnabled(true);
        android.content.Context context15 = ApplicationProvider.getApplicationContext();
        intent4 = new android.content.Intent(context15, com.prismaqf.callblocker.NewEditCalendarRule.class);
        android.content.Intent intent5 = intent4.putExtra("com.prismaqf.callblocker:action", "com.prismaqf.callblocker:update");
        android.content.Intent intent6 = intent5.putExtra("com.prismaqft.callblocker:keyorig", (android.os.Parcelable) calendarrule69);
        android.content.Intent intent7 = intent6.putExtra("com.prismaqft.callblocker:ruleid", 1L);
        calendarrule69.writeToParcel(parcel13, 0);
        org.robolectric.android.controller.ActivityController activitycontroller34 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent7);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule3 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller34.get();
        org.robolectric.android.controller.ActivityController activitycontroller35 = activitycontroller34.create();
        activitycontroller35.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity1 = org.robolectric.Shadows.shadowOf(neweditcalendarrule3);
        shadowactivity1.clickMenuItem(2131230736);
        neweditcalendarrule3.onNoDays(appcompatbutton7);
    }
}
