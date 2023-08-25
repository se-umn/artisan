package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test689 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_411_821
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_007() throws Exception {
        android.content.Intent intent21 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox469 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber452 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox470 = stubber452.when(appcompatcheckbox469);
        appcompatcheckbox470.setEnabled(true);
        org.mockito.stubbing.Stubber stubber453 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox471 = stubber453.when(appcompatcheckbox469);
        appcompatcheckbox471.setEnabled(true);
        org.mockito.stubbing.Stubber stubber454 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox472 = stubber454.when(appcompatcheckbox469);
        appcompatcheckbox472.setChecked(true);
        org.mockito.stubbing.Stubber stubber455 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox473 = stubber455.when(appcompatcheckbox469);
        appcompatcheckbox473.setChecked(false);
        org.mockito.stubbing.Stubber stubber456 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox474 = stubber456.when(appcompatcheckbox469);
        appcompatcheckbox474.getId();
        org.mockito.stubbing.Stubber stubber457 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox475 = stubber457.when(appcompatcheckbox469);
        appcompatcheckbox475.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox476 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber458 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox477 = stubber458.when(appcompatcheckbox476);
        appcompatcheckbox477.setEnabled(true);
        org.mockito.stubbing.Stubber stubber459 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox478 = stubber459.when(appcompatcheckbox476);
        appcompatcheckbox478.setEnabled(true);
        org.mockito.stubbing.Stubber stubber460 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox479 = stubber460.when(appcompatcheckbox476);
        appcompatcheckbox479.setChecked(true);
        org.mockito.stubbing.Stubber stubber461 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox480 = stubber461.when(appcompatcheckbox476);
        appcompatcheckbox480.setChecked(false);
        org.mockito.stubbing.Stubber stubber462 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox481 = stubber462.when(appcompatcheckbox476);
        appcompatcheckbox481.getId();
        org.mockito.stubbing.Stubber stubber463 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox482 = stubber463.when(appcompatcheckbox476);
        appcompatcheckbox482.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox483 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber464 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox484 = stubber464.when(appcompatcheckbox483);
        appcompatcheckbox484.setEnabled(true);
        org.mockito.stubbing.Stubber stubber465 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox485 = stubber465.when(appcompatcheckbox483);
        appcompatcheckbox485.setEnabled(true);
        org.mockito.stubbing.Stubber stubber466 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox486 = stubber466.when(appcompatcheckbox483);
        appcompatcheckbox486.setChecked(true);
        org.mockito.stubbing.Stubber stubber467 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox487 = stubber467.when(appcompatcheckbox483);
        appcompatcheckbox487.setChecked(false);
        org.mockito.stubbing.Stubber stubber468 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox488 = stubber468.when(appcompatcheckbox483);
        appcompatcheckbox488.getId();
        org.mockito.stubbing.Stubber stubber469 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox489 = stubber469.when(appcompatcheckbox483);
        appcompatcheckbox489.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox490 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber470 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox491 = stubber470.when(appcompatcheckbox490);
        appcompatcheckbox491.setEnabled(true);
        org.mockito.stubbing.Stubber stubber471 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox492 = stubber471.when(appcompatcheckbox490);
        appcompatcheckbox492.setEnabled(true);
        org.mockito.stubbing.Stubber stubber472 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox493 = stubber472.when(appcompatcheckbox490);
        appcompatcheckbox493.setChecked(true);
        org.mockito.stubbing.Stubber stubber473 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox494 = stubber473.when(appcompatcheckbox490);
        appcompatcheckbox494.setChecked(false);
        org.mockito.stubbing.Stubber stubber474 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Friday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox495 = stubber474.when(appcompatcheckbox490);
        appcompatcheckbox495.getId();
        org.mockito.stubbing.Stubber stubber475 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox496 = stubber475.when(appcompatcheckbox490);
        appcompatcheckbox496.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton75 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber476 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton76 = stubber476.when(appcompatbutton75);
        appcompatbutton76.setEnabled(true);
        org.mockito.stubbing.Stubber stubber477 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton77 = stubber477.when(appcompatbutton75);
        appcompatbutton77.setEnabled(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox497 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber478 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox498 = stubber478.when(appcompatcheckbox497);
        appcompatcheckbox498.setEnabled(true);
        org.mockito.stubbing.Stubber stubber479 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox499 = stubber479.when(appcompatcheckbox497);
        appcompatcheckbox499.setEnabled(true);
        org.mockito.stubbing.Stubber stubber480 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox500 = stubber480.when(appcompatcheckbox497);
        appcompatcheckbox500.setChecked(true);
        org.mockito.stubbing.Stubber stubber481 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox501 = stubber481.when(appcompatcheckbox497);
        appcompatcheckbox501.setChecked(false);
        org.mockito.stubbing.Stubber stubber482 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Thursday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox502 = stubber482.when(appcompatcheckbox497);
        appcompatcheckbox502.getId();
        org.mockito.stubbing.Stubber stubber483 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox503 = stubber483.when(appcompatcheckbox497);
        appcompatcheckbox503.isChecked();
        org.robolectric.android.controller.ActivityController activitycontroller67 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent21);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule31 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller67.get();
        org.robolectric.android.controller.ActivityController activitycontroller68 = activitycontroller67.create();
        activitycontroller68.visible();
        neweditcalendarrule31.onNoDays(appcompatbutton77);
        neweditcalendarrule31.onCheckDay(appcompatcheckbox475);
        neweditcalendarrule31.onCheckDay(appcompatcheckbox482);
        neweditcalendarrule31.onCheckDay(appcompatcheckbox489);
        neweditcalendarrule31.onCheckDay(appcompatcheckbox503);
        neweditcalendarrule31.onCheckDay(appcompatcheckbox496);
        neweditcalendarrule31.validateActions();
    }
}
