package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test270 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: boolean onCreateOptionsMenu(android.view.Menu)>_687_1374
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent21 = null;
        android.content.Context context23 = ApplicationProvider.getApplicationContext();
        intent21 = new android.content.Intent(context23, com.prismaqf.callblocker.EditFilterRules.class);
        org.robolectric.android.controller.ActivityController activitycontroller37 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class, intent21);
        activitycontroller37.get();
        org.robolectric.android.controller.ActivityController activitycontroller38 = activitycontroller37.create();
        activitycontroller38.visible();
    }
}
