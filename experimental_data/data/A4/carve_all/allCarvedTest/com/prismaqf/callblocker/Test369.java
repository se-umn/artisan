package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test369 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewCalendarRuleTest#TestAllDays/Trace-1651091749870.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditCalendarRule: void validateActions()>_404_805
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditCalendarRule_validateActions_002() throws Exception {
        android.content.Intent intent12 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton27 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber18 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton28 = stubber18.when(appcompatbutton27);
        appcompatbutton28.setEnabled(true);
        org.mockito.stubbing.Stubber stubber19 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton29 = stubber19.when(appcompatbutton27);
        appcompatbutton29.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller31 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditCalendarRule.class, intent12);
        com.prismaqf.callblocker.NewEditCalendarRule neweditcalendarrule13 = (com.prismaqf.callblocker.NewEditCalendarRule) activitycontroller31.get();
        org.robolectric.android.controller.ActivityController activitycontroller32 = activitycontroller31.create();
        activitycontroller32.visible();
        neweditcalendarrule13.onNoDays(appcompatbutton29);
        neweditcalendarrule13.validateActions();
    }
}
