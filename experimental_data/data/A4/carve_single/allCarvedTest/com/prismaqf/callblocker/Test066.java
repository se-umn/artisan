package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test066 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestSingleDays/Trace-1651091736894.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void onNoDays(android.view.View)>_292_584
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_onNoDays_001() throws Exception {
        android.content.Intent intent3 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton3 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber2 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton4 = stubber2.when(appcompatbutton3);
        appcompatbutton4.setEnabled(true);
        org.mockito.stubbing.Stubber stubber3 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton5 = stubber3.when(appcompatbutton3);
        appcompatbutton5.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller11 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent3);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule3 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller11.get();
        org.robolectric.android.controller.ActivityController activitycontroller12 = activitycontroller11.create();
        activitycontroller12.visible();
        neweditcalendarrule3.onNoDays(appcompatbutton5);
    }
}
