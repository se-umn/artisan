package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test682 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_373_746
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_003() throws Exception {
        android.content.Intent intent14 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox77 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber88 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox78 = stubber88.when(appcompatcheckbox77);
        appcompatcheckbox78.setEnabled(true);
        org.mockito.stubbing.Stubber stubber89 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox79 = stubber89.when(appcompatcheckbox77);
        appcompatcheckbox79.setEnabled(true);
        org.mockito.stubbing.Stubber stubber90 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox80 = stubber90.when(appcompatcheckbox77);
        appcompatcheckbox80.setChecked(true);
        org.mockito.stubbing.Stubber stubber91 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox81 = stubber91.when(appcompatcheckbox77);
        appcompatcheckbox81.setChecked(false);
        org.mockito.stubbing.Stubber stubber92 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox82 = stubber92.when(appcompatcheckbox77);
        appcompatcheckbox82.getId();
        org.mockito.stubbing.Stubber stubber93 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox83 = stubber93.when(appcompatcheckbox77);
        appcompatcheckbox83.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox84 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber94 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox85 = stubber94.when(appcompatcheckbox84);
        appcompatcheckbox85.setEnabled(true);
        org.mockito.stubbing.Stubber stubber95 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox86 = stubber95.when(appcompatcheckbox84);
        appcompatcheckbox86.setEnabled(true);
        org.mockito.stubbing.Stubber stubber96 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox87 = stubber96.when(appcompatcheckbox84);
        appcompatcheckbox87.setChecked(true);
        org.mockito.stubbing.Stubber stubber97 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox88 = stubber97.when(appcompatcheckbox84);
        appcompatcheckbox88.setChecked(false);
        org.mockito.stubbing.Stubber stubber98 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox89 = stubber98.when(appcompatcheckbox84);
        appcompatcheckbox89.getId();
        org.mockito.stubbing.Stubber stubber99 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox90 = stubber99.when(appcompatcheckbox84);
        appcompatcheckbox90.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox91 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber100 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox92 = stubber100.when(appcompatcheckbox91);
        appcompatcheckbox92.setEnabled(true);
        org.mockito.stubbing.Stubber stubber101 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox93 = stubber101.when(appcompatcheckbox91);
        appcompatcheckbox93.setEnabled(true);
        org.mockito.stubbing.Stubber stubber102 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox94 = stubber102.when(appcompatcheckbox91);
        appcompatcheckbox94.setChecked(true);
        org.mockito.stubbing.Stubber stubber103 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox95 = stubber103.when(appcompatcheckbox91);
        appcompatcheckbox95.setChecked(false);
        org.mockito.stubbing.Stubber stubber104 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox96 = stubber104.when(appcompatcheckbox91);
        appcompatcheckbox96.getId();
        org.mockito.stubbing.Stubber stubber105 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox97 = stubber105.when(appcompatcheckbox91);
        appcompatcheckbox97.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton33 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber106 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton34 = stubber106.when(appcompatbutton33);
        appcompatbutton34.setEnabled(true);
        org.mockito.stubbing.Stubber stubber107 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton35 = stubber107.when(appcompatbutton33);
        appcompatbutton35.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller39 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent14);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule17 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller39.get();
        org.robolectric.android.controller.ActivityController activitycontroller40 = activitycontroller39.create();
        activitycontroller40.visible();
        neweditcalendarrule17.onNoDays(appcompatbutton35);
        neweditcalendarrule17.onCheckDay(appcompatcheckbox83);
        neweditcalendarrule17.onCheckDay(appcompatcheckbox90);
        neweditcalendarrule17.onCheckDay(appcompatcheckbox97);
    }
}
