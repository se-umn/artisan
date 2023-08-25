package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test680 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_362_724
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_002() throws Exception {
        android.content.Intent intent12 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox28 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber38 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox29 = stubber38.when(appcompatcheckbox28);
        appcompatcheckbox29.setEnabled(true);
        org.mockito.stubbing.Stubber stubber39 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox30 = stubber39.when(appcompatcheckbox28);
        appcompatcheckbox30.setEnabled(true);
        org.mockito.stubbing.Stubber stubber40 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox31 = stubber40.when(appcompatcheckbox28);
        appcompatcheckbox31.setChecked(true);
        org.mockito.stubbing.Stubber stubber41 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox32 = stubber41.when(appcompatcheckbox28);
        appcompatcheckbox32.setChecked(false);
        org.mockito.stubbing.Stubber stubber42 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox33 = stubber42.when(appcompatcheckbox28);
        appcompatcheckbox33.getId();
        org.mockito.stubbing.Stubber stubber43 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox34 = stubber43.when(appcompatcheckbox28);
        appcompatcheckbox34.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox35 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber44 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox36 = stubber44.when(appcompatcheckbox35);
        appcompatcheckbox36.setEnabled(true);
        org.mockito.stubbing.Stubber stubber45 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox37 = stubber45.when(appcompatcheckbox35);
        appcompatcheckbox37.setEnabled(true);
        org.mockito.stubbing.Stubber stubber46 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox38 = stubber46.when(appcompatcheckbox35);
        appcompatcheckbox38.setChecked(true);
        org.mockito.stubbing.Stubber stubber47 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox39 = stubber47.when(appcompatcheckbox35);
        appcompatcheckbox39.setChecked(false);
        org.mockito.stubbing.Stubber stubber48 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox40 = stubber48.when(appcompatcheckbox35);
        appcompatcheckbox40.getId();
        org.mockito.stubbing.Stubber stubber49 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox41 = stubber49.when(appcompatcheckbox35);
        appcompatcheckbox41.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton21 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber50 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton22 = stubber50.when(appcompatbutton21);
        appcompatbutton22.setEnabled(true);
        org.mockito.stubbing.Stubber stubber51 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton23 = stubber51.when(appcompatbutton21);
        appcompatbutton23.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller31 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent12);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule13 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller31.get();
        org.robolectric.android.controller.ActivityController activitycontroller32 = activitycontroller31.create();
        activitycontroller32.visible();
        neweditcalendarrule13.onNoDays(appcompatbutton23);
        neweditcalendarrule13.onCheckDay(appcompatcheckbox34);
        neweditcalendarrule13.onCheckDay(appcompatcheckbox41);
    }
}
