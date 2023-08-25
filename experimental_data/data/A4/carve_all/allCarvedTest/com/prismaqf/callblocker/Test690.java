package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test690 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onCheckDay(android.view.View)>_417_834
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onCheckDay_007() throws Exception {
        android.content.Intent intent22 = null;
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox553 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber528 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox554 = stubber528.when(appcompatcheckbox553);
        appcompatcheckbox554.setEnabled(true);
        org.mockito.stubbing.Stubber stubber529 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox555 = stubber529.when(appcompatcheckbox553);
        appcompatcheckbox555.setEnabled(true);
        org.mockito.stubbing.Stubber stubber530 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox556 = stubber530.when(appcompatcheckbox553);
        appcompatcheckbox556.setChecked(true);
        org.mockito.stubbing.Stubber stubber531 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox557 = stubber531.when(appcompatcheckbox553);
        appcompatcheckbox557.setChecked(false);
        org.mockito.stubbing.Stubber stubber532 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Sunday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox558 = stubber532.when(appcompatcheckbox553);
        appcompatcheckbox558.getId();
        org.mockito.stubbing.Stubber stubber533 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox559 = stubber533.when(appcompatcheckbox553);
        appcompatcheckbox559.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox560 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber534 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox561 = stubber534.when(appcompatcheckbox560);
        appcompatcheckbox561.setEnabled(true);
        org.mockito.stubbing.Stubber stubber535 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox562 = stubber535.when(appcompatcheckbox560);
        appcompatcheckbox562.setEnabled(true);
        org.mockito.stubbing.Stubber stubber536 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox563 = stubber536.when(appcompatcheckbox560);
        appcompatcheckbox563.setChecked(true);
        org.mockito.stubbing.Stubber stubber537 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox564 = stubber537.when(appcompatcheckbox560);
        appcompatcheckbox564.setChecked(false);
        org.mockito.stubbing.Stubber stubber538 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Monday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox565 = stubber538.when(appcompatcheckbox560);
        appcompatcheckbox565.getId();
        org.mockito.stubbing.Stubber stubber539 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox566 = stubber539.when(appcompatcheckbox560);
        appcompatcheckbox566.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox567 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber540 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox568 = stubber540.when(appcompatcheckbox567);
        appcompatcheckbox568.setEnabled(true);
        org.mockito.stubbing.Stubber stubber541 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox569 = stubber541.when(appcompatcheckbox567);
        appcompatcheckbox569.setEnabled(true);
        org.mockito.stubbing.Stubber stubber542 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox570 = stubber542.when(appcompatcheckbox567);
        appcompatcheckbox570.setChecked(true);
        org.mockito.stubbing.Stubber stubber543 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox571 = stubber543.when(appcompatcheckbox567);
        appcompatcheckbox571.setChecked(false);
        org.mockito.stubbing.Stubber stubber544 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Tuesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox572 = stubber544.when(appcompatcheckbox567);
        appcompatcheckbox572.getId();
        org.mockito.stubbing.Stubber stubber545 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox573 = stubber545.when(appcompatcheckbox567);
        appcompatcheckbox573.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox574 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber546 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox575 = stubber546.when(appcompatcheckbox574);
        appcompatcheckbox575.setEnabled(true);
        org.mockito.stubbing.Stubber stubber547 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox576 = stubber547.when(appcompatcheckbox574);
        appcompatcheckbox576.setEnabled(true);
        org.mockito.stubbing.Stubber stubber548 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox577 = stubber548.when(appcompatcheckbox574);
        appcompatcheckbox577.setChecked(true);
        org.mockito.stubbing.Stubber stubber549 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox578 = stubber549.when(appcompatcheckbox574);
        appcompatcheckbox578.setChecked(false);
        org.mockito.stubbing.Stubber stubber550 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Wednesday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox579 = stubber550.when(appcompatcheckbox574);
        appcompatcheckbox579.getId();
        org.mockito.stubbing.Stubber stubber551 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox580 = stubber551.when(appcompatcheckbox574);
        appcompatcheckbox580.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox581 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber552 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox582 = stubber552.when(appcompatcheckbox581);
        appcompatcheckbox582.setEnabled(true);
        org.mockito.stubbing.Stubber stubber553 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox583 = stubber553.when(appcompatcheckbox581);
        appcompatcheckbox583.setEnabled(true);
        org.mockito.stubbing.Stubber stubber554 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox584 = stubber554.when(appcompatcheckbox581);
        appcompatcheckbox584.setChecked(true);
        org.mockito.stubbing.Stubber stubber555 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox585 = stubber555.when(appcompatcheckbox581);
        appcompatcheckbox585.setChecked(false);
        org.mockito.stubbing.Stubber stubber556 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Friday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox586 = stubber556.when(appcompatcheckbox581);
        appcompatcheckbox586.getId();
        org.mockito.stubbing.Stubber stubber557 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox587 = stubber557.when(appcompatcheckbox581);
        appcompatcheckbox587.isChecked();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox588 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber558 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox589 = stubber558.when(appcompatcheckbox588);
        appcompatcheckbox589.setEnabled(true);
        org.mockito.stubbing.Stubber stubber559 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox590 = stubber559.when(appcompatcheckbox588);
        appcompatcheckbox590.setEnabled(true);
        org.mockito.stubbing.Stubber stubber560 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox591 = stubber560.when(appcompatcheckbox588);
        appcompatcheckbox591.setChecked(true);
        org.mockito.stubbing.Stubber stubber561 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox592 = stubber561.when(appcompatcheckbox588);
        appcompatcheckbox592.setChecked(false);
        org.mockito.stubbing.Stubber stubber562 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Saturday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox593 = stubber562.when(appcompatcheckbox588);
        appcompatcheckbox593.getId();
        org.mockito.stubbing.Stubber stubber563 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox594 = stubber563.when(appcompatcheckbox588);
        appcompatcheckbox594.isChecked();
        androidx.appcompat.widget.AppCompatButton appcompatbutton81 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber564 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton82 = stubber564.when(appcompatbutton81);
        appcompatbutton82.setEnabled(true);
        org.mockito.stubbing.Stubber stubber565 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton83 = stubber565.when(appcompatbutton81);
        appcompatbutton83.setEnabled(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox595 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatCheckBox.class);
        org.mockito.stubbing.Stubber stubber566 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox596 = stubber566.when(appcompatcheckbox595);
        appcompatcheckbox596.setEnabled(true);
        org.mockito.stubbing.Stubber stubber567 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox597 = stubber567.when(appcompatcheckbox595);
        appcompatcheckbox597.setEnabled(true);
        org.mockito.stubbing.Stubber stubber568 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox598 = stubber568.when(appcompatcheckbox595);
        appcompatcheckbox598.setChecked(true);
        org.mockito.stubbing.Stubber stubber569 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox599 = stubber569.when(appcompatcheckbox595);
        appcompatcheckbox599.setChecked(false);
        org.mockito.stubbing.Stubber stubber570 = org.mockito.Mockito.doReturn(com.prismaqf.callblocker.R.id.cb_Thursday);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox600 = stubber570.when(appcompatcheckbox595);
        appcompatcheckbox600.getId();
        org.mockito.stubbing.Stubber stubber571 = org.mockito.Mockito.doReturn(true);
        androidx.appcompat.widget.AppCompatCheckBox appcompatcheckbox601 = stubber571.when(appcompatcheckbox595);
        appcompatcheckbox601.isChecked();
        org.robolectric.android.controller.ActivityController activitycontroller71 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent22);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule33 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller71.get();
        org.robolectric.android.controller.ActivityController activitycontroller72 = activitycontroller71.create();
        activitycontroller72.visible();
        neweditcalendarrule33.onNoDays(appcompatbutton83);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox566);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox573);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox580);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox601);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox587);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox594);
        neweditcalendarrule33.onCheckDay(appcompatcheckbox559);
    }
}
