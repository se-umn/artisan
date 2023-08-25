package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test552 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#TestDynamicStateOnRotation/Trace-1651091708855.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilterRule: void onManagePatterns(android.view.View)>_246_492
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilterRule_onManagePatterns_001() throws Exception {
        android.content.Intent intent6 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton3 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber2 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton4 = stubber2.when(appcompatbutton3);
        appcompatbutton4.setEnabled(true);
        org.mockito.stubbing.Stubber stubber3 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton5 = stubber3.when(appcompatbutton3);
        appcompatbutton5.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller15 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilterRule.class, intent6);
        com.prismaqf.callblocker.NewEditFilterRule neweditfilterrule5 = (com.prismaqf.callblocker.NewEditFilterRule) activitycontroller15.get();
        org.robolectric.android.controller.ActivityController activitycontroller16 = activitycontroller15.create();
        activitycontroller16.visible();
        neweditfilterrule5.validateActions();
        neweditfilterrule5.onManagePatterns(appcompatbutton5);
    }
}
