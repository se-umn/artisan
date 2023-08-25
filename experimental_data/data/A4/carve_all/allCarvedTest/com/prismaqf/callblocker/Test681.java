package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test681 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_367_733
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_003() throws Exception {
        android.content.Intent intent13 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox49 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber60 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox50 = stubber60.when(appcompatcheckbox49);
        appcompatcheckbox50.setEnabled(true);
        org.mockito.stubbing.Stubber stubber61 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox51 = stubber61.when(appcompatcheckbox49);
        appcompatcheckbox51.setEnabled(true);
        org.mockito.stubbing.Stubber stubber62 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox52 = stubber62.when(appcompatcheckbox49);
        appcompatcheckbox52.setChecked(true);
        org.mockito.stubbing.Stubber stubber63 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox53 = stubber63.when(appcompatcheckbox49);
        appcompatcheckbox53.setChecked(false);
        org.mockito.stubbing.Stubber stubber64 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox54 = stubber64.when(appcompatcheckbox49);
        appcompatcheckbox54.getId();
        org.mockito.stubbing.Stubber stubber65 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox55 = stubber65.when(appcompatcheckbox49);
        appcompatcheckbox55.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton27 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber66 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton28 = stubber66.when(appcompatbutton27);
        appcompatbutton28.setEnabled(true);
        org.mockito.stubbing.Stubber stubber67 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton29 = stubber67.when(appcompatbutton27);
        appcompatbutton29.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller35 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent13);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule15 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller35.get();
        org.robolectric.android.controller.ActivityController activitycontroller36 = activitycontroller35.create();
        activitycontroller36.visible();
        neweditcalendarrule15.onNoDays(appcompatbutton29);
        neweditcalendarrule15.onCheckDay(appcompatcheckbox55);
        neweditcalendarrule15.validateActions();
    }
}
