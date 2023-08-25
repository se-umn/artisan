package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test368 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestAllDays/Trace-1651091749870.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void refreshWidgets(boolean)>_355_709
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_refreshWidgets_002() throws Exception {
        android.content.Intent intent11 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton21 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber14 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton22 = stubber14.when(appcompatbutton21);
        appcompatbutton22.setEnabled(true);
        org.mockito.stubbing.Stubber stubber15 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton23 = stubber15.when(appcompatbutton21);
        appcompatbutton23.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller27 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent11);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule11 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller27.get();
        org.robolectric.android.controller.ActivityController activitycontroller28 = activitycontroller27.create();
        activitycontroller28.visible();
        neweditcalendarrule11.onNoDays(appcompatbutton23);
        neweditcalendarrule11.refreshWidgets(true);
    }
}
