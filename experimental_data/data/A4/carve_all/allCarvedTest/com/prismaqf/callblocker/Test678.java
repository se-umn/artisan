package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test678 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_351_702
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_001() throws Exception {
        android.content.Intent intent10 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox7 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber12 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox8 = stubber12.when(appcompatcheckbox7);
        appcompatcheckbox8.setEnabled(true);
        org.mockito.stubbing.Stubber stubber13 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox9 = stubber13.when(appcompatcheckbox7);
        appcompatcheckbox9.setEnabled(true);
        org.mockito.stubbing.Stubber stubber14 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox10 = stubber14.when(appcompatcheckbox7);
        appcompatcheckbox10.setChecked(true);
        org.mockito.stubbing.Stubber stubber15 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox11 = stubber15.when(appcompatcheckbox7);
        appcompatcheckbox11.setChecked(false);
        org.mockito.stubbing.Stubber stubber16 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox12 = stubber16.when(appcompatcheckbox7);
        appcompatcheckbox12.getId();
        org.mockito.stubbing.Stubber stubber17 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox13 = stubber17.when(appcompatcheckbox7);
        appcompatcheckbox13.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton9 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber18 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton10 = stubber18.when(appcompatbutton9);
        appcompatbutton10.setEnabled(true);
        org.mockito.stubbing.Stubber stubber19 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton11 = stubber19.when(appcompatbutton9);
        appcompatbutton11.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller23 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent10);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule9 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller23.get();
        org.robolectric.android.controller.ActivityController activitycontroller24 = activitycontroller23.create();
        activitycontroller24.visible();
        neweditcalendarrule9.onNoDays(appcompatbutton11);
        neweditcalendarrule9.onCheckDay(appcompatcheckbox13);
    }
}
