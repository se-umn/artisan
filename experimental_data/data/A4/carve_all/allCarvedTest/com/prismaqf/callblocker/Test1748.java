package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1748 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: boolean onCreateOptionsMenu(android.view.Menu)>_687_1374
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreateOptionsMenu_001() throws Exception {
        android.content.Intent intent32 = null;
        android.content.Context context55 = ApplicationProvider.getApplicationContext();
        intent32 = new android.content.Intent(context55, com.prismaqf.callblocker.EditFilterRules.class);
        org.robolectric.android.controller.ActivityController activitycontroller49 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class, intent32);
        activitycontroller49.get();
        org.robolectric.android.controller.ActivityController activitycontroller50 = activitycontroller49.create();
        activitycontroller50.visible();
    }
}
