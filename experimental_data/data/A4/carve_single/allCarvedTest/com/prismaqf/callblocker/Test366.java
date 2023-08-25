package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test366 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.ManageFilterPatternsTest#TestAddPatternWithExtraChars/Trace-1651091688218.txt
     * Method invocation under test: <com.prismaqf.callblocker.NewEditFilterRule: void onManagePatterns(android.view.View)>_148_296
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_NewEditFilterRule_onManagePatterns_001() throws Exception {
        android.content.Intent intent1 = null;
        androidx.appcompat.widget.AppCompatButton appcompatbutton3 = org.mockito.Mockito.mock(androidx.appcompat.widget.AppCompatButton.class);
        org.mockito.stubbing.Stubber stubber2 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton4 = stubber2.when(appcompatbutton3);
        appcompatbutton4.setEnabled(true);
        org.mockito.stubbing.Stubber stubber3 = org.mockito.Mockito.doNothing();
        androidx.appcompat.widget.AppCompatButton appcompatbutton5 = stubber3.when(appcompatbutton3);
        appcompatbutton5.setEnabled(true);
        org.robolectric.android.controller.ActivityController activitycontroller4 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.NewEditFilterRule.class, intent1);
        com.prismaqf.callblocker.NewEditFilterRule neweditfilterrule3 = (com.prismaqf.callblocker.NewEditFilterRule) activitycontroller4.get();
        org.robolectric.android.controller.ActivityController activitycontroller5 = activitycontroller4.create();
        activitycontroller5.visible();
        neweditfilterrule3.onManagePatterns(appcompatbutton5);
    }
}
