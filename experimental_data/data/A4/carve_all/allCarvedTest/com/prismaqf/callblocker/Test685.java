package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test685 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_389_777
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_005() throws Exception {
        android.content.Intent intent17 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox203 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber208 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox204 = stubber208.when(appcompatcheckbox203);
        appcompatcheckbox204.setEnabled(true);
        org.mockito.stubbing.Stubber stubber209 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox205 = stubber209.when(appcompatcheckbox203);
        appcompatcheckbox205.setEnabled(true);
        org.mockito.stubbing.Stubber stubber210 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox206 = stubber210.when(appcompatcheckbox203);
        appcompatcheckbox206.setChecked(true);
        org.mockito.stubbing.Stubber stubber211 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox207 = stubber211.when(appcompatcheckbox203);
        appcompatcheckbox207.setChecked(false);
        org.mockito.stubbing.Stubber stubber212 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox208 = stubber212.when(appcompatcheckbox203);
        appcompatcheckbox208.getId();
        org.mockito.stubbing.Stubber stubber213 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox209 = stubber213.when(appcompatcheckbox203);
        appcompatcheckbox209.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox210 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber214 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox211 = stubber214.when(appcompatcheckbox210);
        appcompatcheckbox211.setEnabled(true);
        org.mockito.stubbing.Stubber stubber215 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox212 = stubber215.when(appcompatcheckbox210);
        appcompatcheckbox212.setEnabled(true);
        org.mockito.stubbing.Stubber stubber216 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox213 = stubber216.when(appcompatcheckbox210);
        appcompatcheckbox213.setChecked(true);
        org.mockito.stubbing.Stubber stubber217 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox214 = stubber217.when(appcompatcheckbox210);
        appcompatcheckbox214.setChecked(false);
        org.mockito.stubbing.Stubber stubber218 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox215 = stubber218.when(appcompatcheckbox210);
        appcompatcheckbox215.getId();
        org.mockito.stubbing.Stubber stubber219 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox216 = stubber219.when(appcompatcheckbox210);
        appcompatcheckbox216.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox217 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber220 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox218 = stubber220.when(appcompatcheckbox217);
        appcompatcheckbox218.setEnabled(true);
        org.mockito.stubbing.Stubber stubber221 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox219 = stubber221.when(appcompatcheckbox217);
        appcompatcheckbox219.setEnabled(true);
        org.mockito.stubbing.Stubber stubber222 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox220 = stubber222.when(appcompatcheckbox217);
        appcompatcheckbox220.setChecked(true);
        org.mockito.stubbing.Stubber stubber223 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox221 = stubber223.when(appcompatcheckbox217);
        appcompatcheckbox221.setChecked(false);
        org.mockito.stubbing.Stubber stubber224 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox222 = stubber224.when(appcompatcheckbox217);
        appcompatcheckbox222.getId();
        org.mockito.stubbing.Stubber stubber225 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox223 = stubber225.when(appcompatcheckbox217);
        appcompatcheckbox223.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton51 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber226 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton52 = stubber226.when(appcompatbutton51);
        appcompatbutton52.setEnabled(true);
        org.mockito.stubbing.Stubber stubber227 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton53 = stubber227.when(appcompatbutton51);
        appcompatbutton53.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller51 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent17);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule23 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller51.get();
        org.robolectric.android.controller.ActivityController activitycontroller52 = activitycontroller51.create();
        activitycontroller52.visible();
        neweditcalendarrule23.onNoDays(appcompatbutton53);
        neweditcalendarrule23.onCheckDay(appcompatcheckbox209);
        neweditcalendarrule23.onCheckDay(appcompatcheckbox216);
        neweditcalendarrule23.onCheckDay(appcompatcheckbox223);
        neweditcalendarrule23.validateActions();
    }
}
