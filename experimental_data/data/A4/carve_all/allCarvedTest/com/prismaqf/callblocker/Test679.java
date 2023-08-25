package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test679 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_356_711
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_002() throws Exception {
        android.content.Intent intent11 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton15 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber22 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton16 = stubber22.when(appcompatbutton15);
        appcompatbutton16.setEnabled(true);
        org.mockito.stubbing.Stubber stubber23 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton17 = stubber23.when(appcompatbutton15);
        appcompatbutton17.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller27 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent11);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule11 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller27.get();
        org.robolectric.android.controller.ActivityController activitycontroller28 = activitycontroller27.create();
        activitycontroller28.visible();
        neweditcalendarrule11.onNoDays(appcompatbutton17);
        neweditcalendarrule11.validateActions();
    }
}
