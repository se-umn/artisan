package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test1745 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /home/mattia/Research/AndroidCarving/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.NewFilterRuleTest#AfterUpdatingTheRulePatternsTheRuleShouldSaveWhenActivityIsLeft/Trace-1651091712732.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: void onCreate(android.os.Bundle)>_631_1262
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onCreate_001() throws Exception {
        android.content.Intent intent31 = null;
        android.content.Context context49 = ApplicationProvider.getApplicationContext();
        intent31 = new android.content.Intent(context49, com.prismaqf.callblocker.EditFilterRules.class);
        org.robolectric.android.controller.ActivityController activitycontroller46 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class, intent31);
        activitycontroller46.get();
        activitycontroller46.create();
    }
}
