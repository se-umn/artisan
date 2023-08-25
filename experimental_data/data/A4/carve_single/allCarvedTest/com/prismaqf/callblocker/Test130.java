package com.prismaqf.callblocker;

import org.junit.Before;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(RobolectricTestRunner.class)
public class Test130 {

    @Before()
    public void setup() {
        org.robolectric.shadows.ShadowSQLiteConnection.reset();
    }

    /**
     * Generated from /Users/mattia/Faculty/Research/2020_android_test_carving/repositories/action-based-test-carving/apps-src/PrismaCallBlocker/traces/com.prismaqf.callblocker.EditFiltersTest#CreateAndSelectNewFilterRule/Trace-1651091784191.txt
     * Method invocation under test: <com.prismaqf.callblocker.EditFilterRules: void onActivityResult(int,int,android.content.Intent)>_603_1206
     */
    @Test(timeout = 4000)
    public void test_com_prismaqf_callblocker_EditFilterRules_onActivityResult_001() throws Exception {
        android.content.Intent intent26 = null;
        android.content.Intent intent27 = null;
        intent26 = new android.content.Intent();
        android.content.Intent intent28 = intent26.putExtra("com.prismaqft.callblocker:rulename", "New rule");
        android.content.Context context31 = ApplicationProvider.getApplicationContext();
        intent27 = new android.content.Intent(context31, com.prismaqf.callblocker.EditFilterRules.class);
        android.content.Intent intent29 = intent27.putExtra("com.prismaqf.callblocker:context", "com.prismaqf.callblocker:pick");
        org.robolectric.android.controller.ActivityController activitycontroller60 = org.robolectric.Robolectric.buildActivity(com.prismaqf.callblocker.EditFilterRules.class, intent29);
        com.prismaqf.callblocker.EditFilterRules editfilterrules7 = (com.prismaqf.callblocker.EditFilterRules) activitycontroller60.get();
        org.robolectric.android.controller.ActivityController activitycontroller61 = activitycontroller60.create();
        activitycontroller61.visible();
        org.robolectric.shadows.ShadowActivity shadowactivity7 = org.robolectric.Shadows.shadowOf(editfilterrules7);
        shadowactivity7.clickMenuItem(2131230754);
        editfilterrules7.onActivityResult(1002, -1, intent28);
    }
}
