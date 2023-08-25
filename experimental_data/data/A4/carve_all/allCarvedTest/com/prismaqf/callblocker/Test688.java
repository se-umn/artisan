package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test688 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_406_812
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_006() throws Exception {
        android.content.Intent intent20 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox392 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber382 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox393 = stubber382.when(appcompatcheckbox392);
        appcompatcheckbox393.setEnabled(true);
        org.mockito.stubbing.Stubber stubber383 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox394 = stubber383.when(appcompatcheckbox392);
        appcompatcheckbox394.setEnabled(true);
        org.mockito.stubbing.Stubber stubber384 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox395 = stubber384.when(appcompatcheckbox392);
        appcompatcheckbox395.setChecked(true);
        org.mockito.stubbing.Stubber stubber385 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox396 = stubber385.when(appcompatcheckbox392);
        appcompatcheckbox396.setChecked(false);
        org.mockito.stubbing.Stubber stubber386 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox397 = stubber386.when(appcompatcheckbox392);
        appcompatcheckbox397.getId();
        org.mockito.stubbing.Stubber stubber387 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox398 = stubber387.when(appcompatcheckbox392);
        appcompatcheckbox398.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox399 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber388 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox400 = stubber388.when(appcompatcheckbox399);
        appcompatcheckbox400.setEnabled(true);
        org.mockito.stubbing.Stubber stubber389 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox401 = stubber389.when(appcompatcheckbox399);
        appcompatcheckbox401.setEnabled(true);
        org.mockito.stubbing.Stubber stubber390 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox402 = stubber390.when(appcompatcheckbox399);
        appcompatcheckbox402.setChecked(true);
        org.mockito.stubbing.Stubber stubber391 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox403 = stubber391.when(appcompatcheckbox399);
        appcompatcheckbox403.setChecked(false);
        org.mockito.stubbing.Stubber stubber392 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox404 = stubber392.when(appcompatcheckbox399);
        appcompatcheckbox404.getId();
        org.mockito.stubbing.Stubber stubber393 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox405 = stubber393.when(appcompatcheckbox399);
        appcompatcheckbox405.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox406 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber394 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox407 = stubber394.when(appcompatcheckbox406);
        appcompatcheckbox407.setEnabled(true);
        org.mockito.stubbing.Stubber stubber395 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox408 = stubber395.when(appcompatcheckbox406);
        appcompatcheckbox408.setEnabled(true);
        org.mockito.stubbing.Stubber stubber396 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox409 = stubber396.when(appcompatcheckbox406);
        appcompatcheckbox409.setChecked(true);
        org.mockito.stubbing.Stubber stubber397 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox410 = stubber397.when(appcompatcheckbox406);
        appcompatcheckbox410.setChecked(false);
        org.mockito.stubbing.Stubber stubber398 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox411 = stubber398.when(appcompatcheckbox406);
        appcompatcheckbox411.getId();
        org.mockito.stubbing.Stubber stubber399 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox412 = stubber399.when(appcompatcheckbox406);
        appcompatcheckbox412.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox413 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber400 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox414 = stubber400.when(appcompatcheckbox413);
        appcompatcheckbox414.setEnabled(true);
        org.mockito.stubbing.Stubber stubber401 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox415 = stubber401.when(appcompatcheckbox413);
        appcompatcheckbox415.setEnabled(true);
        org.mockito.stubbing.Stubber stubber402 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox416 = stubber402.when(appcompatcheckbox413);
        appcompatcheckbox416.setChecked(true);
        org.mockito.stubbing.Stubber stubber403 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox417 = stubber403.when(appcompatcheckbox413);
        appcompatcheckbox417.setChecked(false);
        org.mockito.stubbing.Stubber stubber404 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Friday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox418 = stubber404.when(appcompatcheckbox413);
        appcompatcheckbox418.getId();
        org.mockito.stubbing.Stubber stubber405 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox419 = stubber405.when(appcompatcheckbox413);
        appcompatcheckbox419.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox420 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber406 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox421 = stubber406.when(appcompatcheckbox420);
        appcompatcheckbox421.setEnabled(true);
        org.mockito.stubbing.Stubber stubber407 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox422 = stubber407.when(appcompatcheckbox420);
        appcompatcheckbox422.setEnabled(true);
        org.mockito.stubbing.Stubber stubber408 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox423 = stubber408.when(appcompatcheckbox420);
        appcompatcheckbox423.setChecked(true);
        org.mockito.stubbing.Stubber stubber409 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox424 = stubber409.when(appcompatcheckbox420);
        appcompatcheckbox424.setChecked(false);
        org.mockito.stubbing.Stubber stubber410 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Saturday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox425 = stubber410.when(appcompatcheckbox420);
        appcompatcheckbox425.getId();
        org.mockito.stubbing.Stubber stubber411 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox426 = stubber411.when(appcompatcheckbox420);
        appcompatcheckbox426.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton69 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber412 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton70 = stubber412.when(appcompatbutton69);
        appcompatbutton70.setEnabled(true);
        org.mockito.stubbing.Stubber stubber413 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton71 = stubber413.when(appcompatbutton69);
        appcompatbutton71.setEnabled(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox427 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber414 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox428 = stubber414.when(appcompatcheckbox427);
        appcompatcheckbox428.setEnabled(true);
        org.mockito.stubbing.Stubber stubber415 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox429 = stubber415.when(appcompatcheckbox427);
        appcompatcheckbox429.setEnabled(true);
        org.mockito.stubbing.Stubber stubber416 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox430 = stubber416.when(appcompatcheckbox427);
        appcompatcheckbox430.setChecked(true);
        org.mockito.stubbing.Stubber stubber417 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox431 = stubber417.when(appcompatcheckbox427);
        appcompatcheckbox431.setChecked(false);
        org.mockito.stubbing.Stubber stubber418 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Thursday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox432 = stubber418.when(appcompatcheckbox427);
        appcompatcheckbox432.getId();
        org.mockito.stubbing.Stubber stubber419 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox433 = stubber419.when(appcompatcheckbox427);
        appcompatcheckbox433.isChecked();
        org.robolectric.android.controller.ActivityController activitycontroller63 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent20);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule29 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller63.get();
        org.robolectric.android.controller.ActivityController activitycontroller64 = activitycontroller63.create();
        activitycontroller64.visible();
        neweditcalendarrule29.onNoDays(appcompatbutton71);
        neweditcalendarrule29.onCheckDay(appcompatcheckbox398);
        neweditcalendarrule29.onCheckDay(appcompatcheckbox405);
        neweditcalendarrule29.onCheckDay(appcompatcheckbox412);
        neweditcalendarrule29.onCheckDay(appcompatcheckbox433);
        neweditcalendarrule29.onCheckDay(appcompatcheckbox419);
        neweditcalendarrule29.onCheckDay(appcompatcheckbox426);
    }
}
