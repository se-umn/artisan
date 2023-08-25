package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test687 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_400_799
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_006() throws Exception {
        android.content.Intent intent19 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox322 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber318 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox323 = stubber318.when(appcompatcheckbox322);
        appcompatcheckbox323.setEnabled(true);
        org.mockito.stubbing.Stubber stubber319 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox324 = stubber319.when(appcompatcheckbox322);
        appcompatcheckbox324.setEnabled(true);
        org.mockito.stubbing.Stubber stubber320 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox325 = stubber320.when(appcompatcheckbox322);
        appcompatcheckbox325.setChecked(true);
        org.mockito.stubbing.Stubber stubber321 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox326 = stubber321.when(appcompatcheckbox322);
        appcompatcheckbox326.setChecked(false);
        org.mockito.stubbing.Stubber stubber322 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox327 = stubber322.when(appcompatcheckbox322);
        appcompatcheckbox327.getId();
        org.mockito.stubbing.Stubber stubber323 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox328 = stubber323.when(appcompatcheckbox322);
        appcompatcheckbox328.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox329 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber324 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox330 = stubber324.when(appcompatcheckbox329);
        appcompatcheckbox330.setEnabled(true);
        org.mockito.stubbing.Stubber stubber325 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox331 = stubber325.when(appcompatcheckbox329);
        appcompatcheckbox331.setEnabled(true);
        org.mockito.stubbing.Stubber stubber326 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox332 = stubber326.when(appcompatcheckbox329);
        appcompatcheckbox332.setChecked(true);
        org.mockito.stubbing.Stubber stubber327 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox333 = stubber327.when(appcompatcheckbox329);
        appcompatcheckbox333.setChecked(false);
        org.mockito.stubbing.Stubber stubber328 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox334 = stubber328.when(appcompatcheckbox329);
        appcompatcheckbox334.getId();
        org.mockito.stubbing.Stubber stubber329 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox335 = stubber329.when(appcompatcheckbox329);
        appcompatcheckbox335.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox336 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber330 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox337 = stubber330.when(appcompatcheckbox336);
        appcompatcheckbox337.setEnabled(true);
        org.mockito.stubbing.Stubber stubber331 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox338 = stubber331.when(appcompatcheckbox336);
        appcompatcheckbox338.setEnabled(true);
        org.mockito.stubbing.Stubber stubber332 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox339 = stubber332.when(appcompatcheckbox336);
        appcompatcheckbox339.setChecked(true);
        org.mockito.stubbing.Stubber stubber333 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox340 = stubber333.when(appcompatcheckbox336);
        appcompatcheckbox340.setChecked(false);
        org.mockito.stubbing.Stubber stubber334 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox341 = stubber334.when(appcompatcheckbox336);
        appcompatcheckbox341.getId();
        org.mockito.stubbing.Stubber stubber335 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox342 = stubber335.when(appcompatcheckbox336);
        appcompatcheckbox342.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton63 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber336 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton64 = stubber336.when(appcompatbutton63);
        appcompatbutton64.setEnabled(true);
        org.mockito.stubbing.Stubber stubber337 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton65 = stubber337.when(appcompatbutton63);
        appcompatbutton65.setEnabled(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox343 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber338 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox344 = stubber338.when(appcompatcheckbox343);
        appcompatcheckbox344.setEnabled(true);
        org.mockito.stubbing.Stubber stubber339 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox345 = stubber339.when(appcompatcheckbox343);
        appcompatcheckbox345.setEnabled(true);
        org.mockito.stubbing.Stubber stubber340 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox346 = stubber340.when(appcompatcheckbox343);
        appcompatcheckbox346.setChecked(true);
        org.mockito.stubbing.Stubber stubber341 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox347 = stubber341.when(appcompatcheckbox343);
        appcompatcheckbox347.setChecked(false);
        org.mockito.stubbing.Stubber stubber342 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Thursday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox348 = stubber342.when(appcompatcheckbox343);
        appcompatcheckbox348.getId();
        org.mockito.stubbing.Stubber stubber343 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox349 = stubber343.when(appcompatcheckbox343);
        appcompatcheckbox349.isChecked();
        org.robolectric.android.controller.ActivityController activitycontroller59 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent19);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule27 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller59.get();
        org.robolectric.android.controller.ActivityController activitycontroller60 = activitycontroller59.create();
        activitycontroller60.visible();
        neweditcalendarrule27.onNoDays(appcompatbutton65);
        neweditcalendarrule27.onCheckDay(appcompatcheckbox328);
        neweditcalendarrule27.onCheckDay(appcompatcheckbox335);
        neweditcalendarrule27.onCheckDay(appcompatcheckbox342);
        neweditcalendarrule27.onCheckDay(appcompatcheckbox349);
        neweditcalendarrule27.validateActions();
    }
}
