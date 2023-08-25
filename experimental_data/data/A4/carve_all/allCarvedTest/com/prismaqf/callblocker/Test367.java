package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test367 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestAllDays/Trace-1651091749870.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onAllDays(android.view.View)>_351_702
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onAllDays_001() throws Exception {
        android.content.Intent intent10 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton12 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber8 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton13 = stubber8.when(appcompatbutton12);
        appcompatbutton13.setEnabled(true);
        org.mockito.stubbing.Stubber stubber9 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton14 = stubber9.when(appcompatbutton12);
        appcompatbutton14.setEnabled(true);
        androidx.appcompat.widget.AppCompatButton appcompatbutton15 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber10 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton16 = stubber10.when(appcompatbutton15);
        appcompatbutton16.setEnabled(true);
        org.mockito.stubbing.Stubber stubber11 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton17 = stubber11.when(appcompatbutton15);
        appcompatbutton17.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller23 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent10);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule9 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller23.get();
        org.robolectric.android.controller.ActivityController activitycontroller24 = activitycontroller23.create();
        activitycontroller24.visible();
        neweditcalendarrule9.onNoDays(appcompatbutton17);
        neweditcalendarrule9.onAllDays(appcompatbutton14);
    }
}
